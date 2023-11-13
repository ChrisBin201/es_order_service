package com.chris.orderservice.service.impl;

import com.chris.common.caller.ProductServiceCaller;
import com.chris.common.config.UserDetailsInfo;
import com.chris.common.handler.CommonErrorCode;
import com.chris.common.handler.CommonException;
import com.chris.data.dto.order.InvoiceDTO;
import com.chris.data.dto.order.OrderLineDTO;
import com.chris.data.entity.order.Invoice;
import com.chris.data.entity.order.InvoiceLine;
import com.chris.data.entity.order.OrderLine;
import com.chris.data.entity.order.Shipment;
import com.chris.data.entity.order.sub.ProductItemDetail;
import com.chris.data.entity.product.ProductItem;
import com.chris.orderservice.repo.InvoiceLineRepo;
import com.chris.orderservice.repo.InvoiceRepo;
import com.chris.orderservice.repo.OrderLineRepo;
import com.chris.orderservice.repo.ShipmentRepo;
import com.chris.orderservice.service.InvoiceService;
import com.chris.orderservice.service.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private OrderLineRepo orderLineRepo;

    @Autowired
    private ShipmentRepo shipmentRepo;

    @Autowired
    ProductServiceCaller productServiceCaller;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public Invoice create(InvoiceDTO invoiceDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        Shipment shipment = shipmentRepo.findById(invoiceDTO.getShipmentId())
                .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.SHIPMENT_NOT_FOUND.getCode(), CommonErrorCode.SHIPMENT_NOT_FOUND.getMessage()));

        Invoice invoice = new Invoice();
        invoice.setStatus(Invoice.InvoiceStatus.PAID);
        invoice.setCustomerId(userLogin.getId());
        invoice.setShipment(shipment);
        invoice.setShipmentPrice(shipment.getPrice());

        List<ProductItemDetail> productItems = productServiceCaller.checkInventoryManyProductItem(
                invoiceDTO.getOrderLines())
                .onErrorResume(e -> {
                    throw new RuntimeException(e);
                })
                .blockOptional()
                .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getCode(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getMessage()));

        List<InvoiceLine> invoiceLines = new ArrayList<>();
        invoiceDTO.getOrderLines().forEach(orderLineDTO -> {

            InvoiceLine invoiceLine = new InvoiceLine();
//            invoiceLine.setInvoice(invoice);
            invoiceLine.setStatus(Invoice.InvoiceStatus.PAID);

            //create orderline
            ProductItemDetail productItem  = productItems.stream()
                    .filter(pi -> pi.getId() == orderLineDTO.getProductItemId())
                    .findFirst()
                    .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(),
                            CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getCode(),
                            CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getMessage()));

            OrderLine orderLine = new OrderLine();
            orderLine.setProductItem(productItem);
            orderLine.setQuantity(orderLineDTO.getQuantity());
            orderLine.setStatus(OrderLine.OrderStatus.CONFIRM_PENDING);
            orderLine.setCustomerId(userLogin.getId());
//            orderLine.setInvoiceLine(invoiceLine);
            invoiceLine.setOrderLine(orderLine);
            invoiceLine.setInvoice(invoice);
            invoiceLines.add(invoiceLine);
        });

        invoice.setInvoiceLines(invoiceLines);
        Invoice savedInvoice = invoiceRepo.save(invoice);

//        List<OrderLine> savedOrderLines = orderLineRepo.findAllById(savedInvoice.getInvoiceLines().stream().map(invoiceLine -> invoiceLine.getOrderLine().getId()).collect(Collectors.toList()));
        List<OrderLine> savedOrderLines = savedInvoice.getInvoiceLines().stream()
                .map(invoiceLine -> {
                    OrderLine orderLine = invoiceLine.getOrderLine();
                    invoiceLine.setOrderLine(null);
                    invoiceLine.setInvoice(savedInvoice);
                    orderLine.setInvoiceLine(invoiceLine);
                    return orderLine;
                }).collect(Collectors.toList());
        //send message
        savedOrderLines.forEach(orderLine -> {
            orderLine.getInvoiceLine().getInvoice().setInvoiceLines(null);
        });
        kafkaProducer.createOrders(savedOrderLines);
        return savedInvoice;
    }
}

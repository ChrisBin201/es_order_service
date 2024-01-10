package com.chris.orderservice.service.impl;

import com.chris.common.caller.ProductServiceCaller;
import com.chris.common.config.UserDetailsInfo;
import com.chris.common.handler.CommonErrorCode;
import com.chris.common.handler.CommonException;
import com.chris.data.dto.order.InvoiceDTO;
import com.chris.data.dto.order.OrderDTO;
import com.chris.data.dto.order.CheckoutInfo;
import com.chris.data.entity.order.Invoice;
import com.chris.data.entity.order.Order;
import com.chris.data.entity.order.OrderLine;
import com.chris.data.entity.order.Shipment;
import com.chris.data.entity.order.sub.ProductItemDetail;
import com.chris.orderservice.repo.InvoiceRepo;
import com.chris.orderservice.repo.OrderRepo;
import com.chris.orderservice.repo.OrderLineRepo;
import com.chris.orderservice.repo.ShipmentRepo;
import com.chris.orderservice.service.OrderService;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderLineRepo orderLineRepo;

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private ShipmentRepo shipmentRepo;

    @Autowired
    ProductServiceCaller productServiceCaller;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public InvoiceDTO create(InvoiceDTO invoiceDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();

        Invoice invoice = new Invoice();
        invoice.setCheckoutStatus(Order.InvoiceStatus.PENDING);
        invoice.setPaymentId(invoiceDTO.getPaymentId());
        invoice.setCustomerId(userLogin.getId());


        List<Order> orders = new ArrayList<>();

        invoiceDTO.getOrders().forEach(orderDTO -> {
            Shipment shipment = shipmentRepo.findById(orderDTO.getShipmentId())
                    .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.SHIPMENT_NOT_FOUND.getCode(), CommonErrorCode.SHIPMENT_NOT_FOUND.getMessage()));

            Order order = new Order();
//        invoice.setStatus(Invoice.InvoiceStatus.PAID);
            order.setStatus(Order.OrderStatus.PAYMENT_PENDING);
            order.setPayoutStatus(Order.InvoiceStatus.PENDING);
            order.setShipmentCheckoutStatus(Order.InvoiceStatus.PENDING);
            order.setShipment(shipment);
            order.setShipmentPrice(shipment.getPrice());

            List<ProductItemDetail> productItems = productServiceCaller.checkInventoryManyProductItem(
                            orderDTO.getOrderLines())
                    .onErrorResume(e -> {
                        throw new RuntimeException(e);
                    })
                    .blockOptional()
                    .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(),
                            CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getCode(),
                            CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getMessage()));

            long sellerId = productItems.get(0).getProductInfo().getSellerId();
            if (productItems.stream().anyMatch(pi -> pi.getProductInfo().getSellerId() != sellerId)) {
                throw new CommonException(HttpStatus.BAD_REQUEST.value(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getCode(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getMessage());
            }
            order.setSellerId(sellerId);

            List<OrderLine> orderLines = new ArrayList<>();
            orderDTO.getOrderLines().forEach(orderLineDTO -> {

                OrderLine orderLine = new OrderLine();

                //create orderline
                ProductItemDetail productItem  = productItems.stream()
                        .filter(pi -> pi.getId() == orderLineDTO.getProductItemId())
                        .findFirst()
                        .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(),
                                CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getCode(),
                                CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getMessage()));

                orderLine.setProductItem(productItem);
                orderLine.setQuantity(orderLineDTO.getQuantity());
//            orderLine.setStatus(OrderLine.OrderStatus.CONFIRM_PENDING);
//            orderLine.setStatus(OrderLine.OrderStatus.PAYMENT_PENDING);
//            orderLine.setCheckoutStatus(Order.InvoiceStatus.PENDING);
//            orderLine.setPayoutStatus(Order.InvoiceStatus.PENDING);
//            orderLine.setCustomerId(userLogin.getId());
//            orderLine.setInvoiceLine(invoiceLine);
                orderLine.setOrder(order);
                orderLines.add(orderLine);
            });

            order.setOrderLines(orderLines);
//            Order savedOrder = orderRepo.save(order);

//        List<OrderLine> savedOrderLines = orderLineRepo.findAllById(savedInvoice.getInvoiceLines().stream().map(invoiceLine -> invoiceLine.getOrderLine().getId()).collect(Collectors.toList()));
//            List<OrderLine> savedOrderLines = savedOrder.getOrderLines().stream()
//                    .map(orderLine -> {
//                        orderLine.setOrder(savedOrder);
//                        return orderLine;
//                    }).collect(Collectors.toList());
//            savedOrderLines.forEach(orderLine -> {
//                orderLine.getOrder().setOrderLines(null);
//            });
            order.setInvoice(invoice);
            orders.add(order);
        });
        invoice.setOrders(orders);
        Invoice savedInvoice = invoiceRepo.save(invoice);

        List<Order> savedOrders = savedInvoice.getOrders();
        savedOrders.forEach(order -> {
//            order.setInvoice(savedInvoice);
            order.getInvoice().setOrders(null);
            order.getOrderLines().forEach(orderLine -> {
                orderLine.setOrder(null);
            });
        });
        invoiceDTO.setId(savedInvoice.getId());
        kafkaProducer.createOrders(orders);
        return invoiceDTO;
    }

    @Override
    public Order updateStatus(long id, String status) {
        Order.OrderStatus orderStatus = Order.OrderStatus.fromString(status);
        Order order = orderRepo.findById(id).
                orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.ORDER_NOT_FOUND.getCode(), CommonErrorCode.ORDER_NOT_FOUND.getMessage()));


        if( !orderStatus.name().equals(Order.OrderStatus.CANCEL.name()) && !order.findNextStatus().name().equals(orderStatus.name())){
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVALID_ORDER_STATUS_SEQUENCE.getCode(), CommonErrorCode.INVALID_ORDER_STATUS_SEQUENCE.getMessage());
        }

        List<Order.OrderStatus> allowCancelStatus = List.of(Order.OrderStatus.PAYMENT_PENDING , Order.OrderStatus.CONFIRM_PENDING, Order.OrderStatus.SHIPMENT_PENDING);
        if(orderStatus.name().equals(Order.OrderStatus.CANCEL.name()) && allowCancelStatus.stream().anyMatch(allowStatus -> allowStatus.name().equals(order.getStatus().name()))) {

            order.setPayoutStatus(Order.InvoiceStatus.CANCEL);

        } else if(orderStatus.name().equals(Order.OrderStatus.DELIVERING.name())){
//            Order order = orderLine.getOrder();
            order.setShipmentCheckoutStatus(Order.InvoiceStatus.PAID);
        }

        order.setStatus(orderStatus);
        Order savedOrder =  orderRepo.save(order);
//        savedOrderLine.getOrder().setOrderLines(null);

        //send message to kafka
        kafkaProducer.updateOrderStatus(savedOrder);

        return savedOrder;
    }

//    @Override
//    public CheckoutInfo getCheckoutInfo(long id) {
//        Order order = orderRepo.findById(id).orElse(null);
//        if (order == null) {
//            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVOICE_NOT_FOUND.getCode(), CommonErrorCode.INVOICE_NOT_FOUND.getMessage());
//        }
////        List<OrderLine> orderLines = order.getOrderLines().stream()
////                .filter(orderLine -> orderLine.getStatus().name().equals(OrderLine.OrderStatus.PAYMENT_PENDING.name()))
////                .collect(Collectors.toList());
////
////        orderLines.forEach(orderLine -> {
////            orderLine.getOrder().setOrderLines(null);
////        });
//
////        return CheckoutInfo.from(order, order.getOrderLines());
//        return null;
//    }
}

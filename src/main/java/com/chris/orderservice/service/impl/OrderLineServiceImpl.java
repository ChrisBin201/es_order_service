package com.chris.orderservice.service.impl;

import com.chris.common.handler.CommonErrorCode;
import com.chris.common.handler.CommonException;
import com.chris.data.entity.order.Invoice;
import com.chris.data.entity.order.InvoiceLine;
import com.chris.data.entity.order.OrderLine;
import com.chris.orderservice.repo.InvoiceRepo;
import com.chris.orderservice.repo.OrderLineRepo;
import com.chris.orderservice.service.OrderLineService;
import com.chris.orderservice.service.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderLineServiceImpl implements OrderLineService {

    @Autowired
    private OrderLineRepo orderLineRepo;

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public OrderLine updateStatus(long id, String status) {
        OrderLine.OrderStatus orderStatus = OrderLine.OrderStatus.fromString(status);
        OrderLine orderLine = orderLineRepo.findById(id).
                orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.ORDER_NOT_FOUND.getCode(), CommonErrorCode.ORDER_NOT_FOUND.getMessage()));


        if( !orderStatus.name().equals(OrderLine.OrderStatus.CANCEL.name()) && !orderLine.findNextStatus().name().equals(orderStatus.name())){
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVALID_ORDER_STATUS_SEQUENCE.getCode(), CommonErrorCode.INVALID_ORDER_STATUS_SEQUENCE.getMessage());
        }

        if(orderStatus.name().equals(OrderLine.OrderStatus.CANCEL.name())) {
            orderLine.getInvoiceLine().setStatus(Invoice.InvoiceStatus.CANCEL);
            Invoice invoice = orderLine.getInvoiceLine().getInvoice();
            //check if all invoice lines are cancelled
            if(invoice.getInvoiceLines().stream().allMatch(invoiceLine -> invoiceLine.getStatus().name().equals(Invoice.InvoiceStatus.CANCEL.name()))){
                invoice.setStatus(Invoice.InvoiceStatus.CANCEL);
                invoiceRepo.save(invoice);
            }
        }

        orderLine.setStatus(orderStatus);
        OrderLine savedOrderLine =  orderLineRepo.save(orderLine);
        savedOrderLine.getInvoiceLine().getInvoice().setInvoiceLines(null);

        //send message to kafka
        kafkaProducer.updateOrderStatus(savedOrderLine);

        return savedOrderLine;
    }
}

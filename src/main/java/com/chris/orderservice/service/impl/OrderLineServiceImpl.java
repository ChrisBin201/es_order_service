package com.chris.orderservice.service.impl;

import com.chris.common.handler.CommonErrorCode;
import com.chris.common.handler.CommonException;
import com.chris.data.entity.order.Order;
import com.chris.data.entity.order.OrderLine;
import com.chris.orderservice.repo.OrderRepo;
import com.chris.orderservice.repo.OrderLineRepo;
import com.chris.orderservice.service.OrderLineService;
import com.chris.orderservice.service.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineServiceImpl implements OrderLineService {

    @Autowired
    private OrderLineRepo orderLineRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private KafkaProducer kafkaProducer;

//    @Override
//    public OrderLine updateStatus(long id, String status) {
//        OrderLine.OrderStatus orderStatus = OrderLine.OrderStatus.fromString(status);
//        OrderLine orderLine = orderLineRepo.findById(id).
//                orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.ORDER_NOT_FOUND.getCode(), CommonErrorCode.ORDER_NOT_FOUND.getMessage()));
//
//
//        if( !orderStatus.name().equals(OrderLine.OrderStatus.CANCEL.name()) && !orderLine.findNextStatus().name().equals(orderStatus.name())){
//            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVALID_ORDER_STATUS_SEQUENCE.getCode(), CommonErrorCode.INVALID_ORDER_STATUS_SEQUENCE.getMessage());
//        }
//
//        List<OrderLine.OrderStatus> allowCancelStatus = List.of(OrderLine.OrderStatus.PAYMENT_PENDING , OrderLine.OrderStatus.CONFIRM_PENDING, OrderLine.OrderStatus.SHIPMENT_PENDING);
//        if(orderStatus.name().equals(OrderLine.OrderStatus.CANCEL.name()) && allowCancelStatus.stream().anyMatch(allowStatus -> allowStatus.name().equals(orderLine.getStatus().name()))) {
////            orderLine.setCheckoutStatus(Invoice.InvoiceStatus.CANCEL);
//            orderLine.setPayoutStatus(Order.InvoiceStatus.CANCEL);
////            Invoice invoice = orderLine.getInvoice();
////            //check if all invoice lines are cancelled
////            if(invoice.getOrderLines().stream().allMatch(or -> or.getStatus().name().equals(Invoice.InvoiceStatus.CANCEL.name()))){
////                invoice.setStatus(Invoice.InvoiceStatus.CANCEL);
////                invoice.setShipmentCheckoutStatus(Invoice.InvoiceStatus.CANCEL);
////                invoiceRepo.save(invoice);
////            }
//        } else if(orderStatus.name().equals(OrderLine.OrderStatus.DELIVERING.name())){
//            Order order = orderLine.getOrder();
//            order.setShipmentCheckoutStatus(Order.InvoiceStatus.PAID);
//        }
//
//        orderLine.setStatus(orderStatus);
//        OrderLine savedOrderLine =  orderLineRepo.save(orderLine);
//        savedOrderLine.getOrder().setOrderLines(null);
//
//        //send message to kafka
//        kafkaProducer.updateOrderStatus(savedOrderLine);
//
//        return savedOrderLine;
//    }

    @Override
    public OrderLine findById(long id) {
        return orderLineRepo.findById(id).
                orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.ORDER_NOT_FOUND.getCode(), CommonErrorCode.ORDER_NOT_FOUND.getMessage()));
    }
}

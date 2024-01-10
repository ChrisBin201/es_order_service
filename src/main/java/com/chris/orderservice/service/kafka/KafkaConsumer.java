package com.chris.orderservice.service.kafka;


import com.chris.common.constant.MessageEvent;
import com.chris.data.entity.order.Invoice;
import com.chris.data.entity.order.Order;
import com.chris.data.entity.order.OrderLine;
import com.chris.orderservice.repo.InvoiceRepo;
import com.chris.orderservice.repo.OrderRepo;
import com.chris.orderservice.repo.OrderLineRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    InvoiceRepo invoiceRepo;

//    @Autowired
//    InvoiceLineRepo invoiceLineRepo;
    @Autowired
    OrderLineRepo orderLineRepo;

    @Autowired
    KafkaProducer kafkaProducer;
    @KafkaListener(
            topics = {MessageEvent.COMPLETE_CHECKOUT},
            groupId="es_order_service"
    )
    public void completeCheckoutListener( String invoiceIdStr) {
        long invoiceId = Long.parseLong(invoiceIdStr);
        log.info("completeCheckoutListener [{}]", invoiceId);
        Invoice invoice =  invoiceRepo.findById(invoiceId).get();
//        List<OrderLine> orderLines = orderLineRepo.findAllByInvoiceId(orderId);
        invoice.getOrders().forEach(order -> {
            if(order.getStatus().name().equals(Order.OrderStatus.PAYMENT_PENDING.name())){
                order.setStatus(order.findNextStatus());
                order.getInvoice().setCheckoutStatus(Order.InvoiceStatus.PAID);
            }
            Order savedOrder =  orderRepo.save(order);
            kafkaProducer.updateOrderStatus(savedOrder);
        });
//            orderLines.forEach(orderLine -> {
//                if(orderLine.getStatus().name().equals(OrderLine.OrderStatus.PAYMENT_PENDING.name())){
//                    orderLine.setStatus(orderLine.findNextStatus());
//                    orderLine.setCheckoutStatus(Order.InvoiceStatus.PAID);
//                }
//            });
//            List<OrderLine> savedOrderLines =  orderLineRepo.saveAll(orderLines);
//            order.setCheckoutStatus(Order.InvoiceStatus.PAID);

//        savedOrderLines.forEach(orderLine -> {
//            orderLine.getOrder().setOrderLines(null);
//            kafkaProducer.updateOrderStatus(orderLine);
//        });
    }

    @KafkaListener(
            topics = {MessageEvent.COMPLETE_PAYOUT},
            groupId="es_order_service"
    )
    public void completePayoutListener( String orderIdStr) {
        long orderId = Long.parseLong(orderIdStr);
        log.info("completePayoutListener [{}]", orderId);
        Order order =  orderRepo.findById(orderId).get();
        order.setPayoutStatus(Order.InvoiceStatus.PAID);
        Order savedOrder =  orderRepo.save(order);
//        savedOrderLines.getOrder().setOrderLines(null);
        kafkaProducer.syncOrder(savedOrder);


//        OrderLine orderLine =  orderLineRepo.findById(orderLineId).get();
//        orderLine.setPayoutStatus(Order.InvoiceStatus.PAID);
//        OrderLine savedOrderLines =  orderLineRepo.save(orderLine);
//        savedOrderLines.getOrder().setOrderLines(null);
//        kafkaProducer.updateOrderStatus(savedOrderLines);

    }

    @KafkaListener(
            topics = {MessageEvent.COMPLETE_REFUND},
            groupId="es_order_service"
    )
    public void completeRefundListener( String orderIdStr) {
        long orderId = Long.parseLong(orderIdStr);
        log.info("completeRefundListener [{}]", orderId);
        Order order =  orderRepo.findById(orderId).get();
        order.getInvoice().setCheckoutStatus(Order.InvoiceStatus.CANCEL);
//        Order order = orderLine.getOrder();
//        //check if all order lines are cancelled
//        if(order.getOrderLines().stream().allMatch(or -> or.getCheckoutStatus().name().equals(Order.InvoiceStatus.CANCEL.name()))){
//            order.setCheckoutStatus(Order.InvoiceStatus.CANCEL);
//            order.setShipmentCheckoutStatus(Order.InvoiceStatus.CANCEL);
//            orderRepo.save(order);
//        }

        Order savedOrder =  orderRepo.save(order);
//        savedOrderLines.getOrder().setOrderLines(null);
        kafkaProducer.updateOrderStatus(savedOrder);

    }

}

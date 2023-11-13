package com.chris.orderservice.service.kafka;

import com.chris.common.constant.MessageEvent;
import com.chris.common.utils.JsonUtil;
import com.chris.data.elasticsearch.OrderInfo;
import com.chris.data.entity.order.OrderLine;
import com.chris.data.entity.order.Rating;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaProducer {


    private final KafkaTemplate<String, Object> productKafkaTemplate;


    @Autowired
    public KafkaProducer(KafkaTemplate<String, Object> productKafkaTemplate) {
        this.productKafkaTemplate = productKafkaTemplate;
    }

    public void createOrders(List<OrderLine> orderLines) {
        orderLines.forEach(orderLine -> {
            try {
                String orderLineJson = JsonUtil.convertToJson(orderLine);
                productKafkaTemplate.send(MessageEvent.CREATE_ORDER, orderLineJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateOrderStatus(OrderLine orderLine) {
        try {
            String orderLineJson = JsonUtil.convertToJson(orderLine);
            productKafkaTemplate.send(MessageEvent.UPDATE_ORDER_STATUS, orderLineJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createRating(Rating rating) {
        try {
            String ratingJson = JsonUtil.convertToJson(rating);
            productKafkaTemplate.send(MessageEvent.CREATE_RATING, ratingJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRating(Rating rating) {
        try {
            String ratingJson = JsonUtil.convertToJson(rating);
            productKafkaTemplate.send(MessageEvent.UPDATE_RATING, ratingJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

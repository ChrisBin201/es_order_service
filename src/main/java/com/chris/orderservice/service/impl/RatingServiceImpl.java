package com.chris.orderservice.service.impl;

import com.chris.common.config.UserDetailsInfo;
import com.chris.common.handler.CommonErrorCode;
import com.chris.common.handler.CommonException;
import com.chris.data.dto.order.RatingDTO;
import com.chris.data.entity.order.OrderLine;
import com.chris.data.entity.order.Rating;
import com.chris.orderservice.repo.OrderLineRepo;
import com.chris.orderservice.repo.RatingRepo;
import com.chris.orderservice.service.RatingService;
import com.chris.orderservice.service.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private OrderLineRepo orderLineRepo;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public Rating create(RatingDTO ratingDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();

        OrderLine orderLine = orderLineRepo.findById(ratingDTO.getOrderLineId())
                .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.ORDER_NOT_FOUND.getCode(), CommonErrorCode.ORDER_NOT_FOUND.getMessage()));

        if(orderLine.isRated()) return null;

        Rating rating = new Rating();

        rating.setRating(ratingDTO.getRating());
        rating.setMessage(ratingDTO.getMessage());
        rating.setOrderLine(orderLine);
        rating.setCustomerId(userLogin.getId());

        Rating savedRating =  ratingRepo.save(rating);
//        orderLine.setStatus(OrderLine.OrderStatus.COMPLETE);
//        OrderLine savedOrderLine =  orderLineRepo.save(orderLine);
//        savedRating.getOrderLine().getOrder().setOrderLines(null);
//        savedOrderLine.getOrder().setOrderLines(null);

        // send message to kafka
        kafkaProducer.createRating(savedRating);
//        kafkaProducer.updateOrderStatus(savedOrderLine);
        return savedRating;
    }

    @Override
    public Rating update(RatingDTO ratingDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();

        Rating rating = ratingRepo.findById(ratingDTO.getId())
                .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.RATING_NOT_FOUND.getCode(), CommonErrorCode.RATING_NOT_FOUND.getMessage()));

        rating.setRating(ratingDTO.getRating());
        rating.setMessage(ratingDTO.getMessage());

        Rating savedRating =  ratingRepo.save(rating);

        // send message to kafka
        kafkaProducer.updateRating(savedRating);
        return savedRating;
    }
}

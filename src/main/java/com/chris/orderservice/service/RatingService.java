package com.chris.orderservice.service;

import com.chris.data.dto.order.RatingDTO;
import com.chris.data.entity.order.Rating;

public interface RatingService {

    Rating create(RatingDTO ratingDTO);
    Rating update(RatingDTO ratingDTO);

}

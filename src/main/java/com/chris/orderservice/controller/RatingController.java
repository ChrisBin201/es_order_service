package com.chris.orderservice.controller;

import com.chris.data.dto.order.RatingDTO;
import com.chris.data.dto.order.RatingStat;
import com.chris.data.entity.order.Rating;
import com.chris.orderservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public Rating createRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.create(ratingDTO);
    }

    @PutMapping("/{id}")
    public Rating updateRating(@RequestBody RatingDTO ratingDTO, @PathVariable long id) {
        ratingDTO.setId(id);
        return ratingService.update(ratingDTO);
    }

    @GetMapping("statistics")
    public RatingStat getRatingStat(@RequestParam(name = "product_item_id") long productItemId) {
//        return ratingService.getRatingStat(productItemId);
        return null;
    }
}

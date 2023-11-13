package com.chris.orderservice.controller;

import com.chris.data.dto.ResponseData;
import com.chris.data.entity.order.Shipment;
import com.chris.data.redis.Cart;
import com.chris.orderservice.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllByCustomer() {
        ResponseData<List<Shipment>> response = new ResponseData<>();
        response.initData(shipmentService.getAll());
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.chris.orderservice.controller;

import com.chris.data.dto.ResponseData;
import com.chris.data.dto.order.InvoiceDTO;
import com.chris.data.dto.order.OrderLineDTO;
import com.chris.data.dto.product.ProductDTO;
import com.chris.data.entity.order.OrderLine;
import com.chris.orderservice.service.InvoiceService;
import com.chris.orderservice.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderLineService orderLineService;

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable long id, @RequestParam String status) {
        ResponseData<OrderLine> response = new ResponseData<>();
        OrderLine orderLine =  orderLineService.updateStatus(id, status);
        response.initData(orderLine);
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}

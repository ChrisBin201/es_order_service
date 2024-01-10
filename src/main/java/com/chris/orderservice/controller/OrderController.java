package com.chris.orderservice.controller;

import com.chris.data.dto.ResponseData;
import com.chris.data.dto.order.InvoiceDTO;
import com.chris.data.dto.order.OrderDTO;
import com.chris.data.entity.order.Invoice;
import com.chris.data.entity.order.Order;
import com.chris.data.entity.order.OrderLine;
import com.chris.orderservice.repo.InvoiceRepo;
import com.chris.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;

    @Autowired
    private InvoiceRepo invoiceRepo;

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        ResponseData<InvoiceDTO> response = new ResponseData<>();
        invoiceDTO = orderService.create(invoiceDTO);
        response.initData(invoiceDTO);
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable long id, @RequestParam String status) {
        ResponseData<Order> response = new ResponseData<>();
        Order order =  orderService.updateStatus(id, status);
        response.initData(order);
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/invoice/{id}/checkout-info")
    public ResponseEntity<?> getCheckoutInfo(@PathVariable long id) {
        Invoice invoice = invoiceRepo.findById(id).get();
        invoice.getOrders().forEach(order -> {
            order.setInvoice(null);
            order.getOrderLines().forEach(orderLine -> {
                orderLine.setOrder(null);
            });
        });
        return ResponseEntity.ok(invoice);
    }
}

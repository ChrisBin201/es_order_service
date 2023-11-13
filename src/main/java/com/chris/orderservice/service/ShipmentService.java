package com.chris.orderservice.service;

import com.chris.data.entity.order.Shipment;
import com.chris.data.redis.Cart;

import java.util.List;

public interface ShipmentService {

    List<Shipment> getAll();

    Shipment findById(long id);

    Shipment create(Shipment shipment);

    Shipment update(Shipment shipment);

    void delete(long id);



}

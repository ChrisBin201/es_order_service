package com.chris.orderservice.service.impl;

import com.chris.common.handler.CommonErrorCode;
import com.chris.common.handler.CommonException;
import com.chris.data.entity.order.Shipment;
import com.chris.orderservice.repo.ShipmentRepo;
import com.chris.orderservice.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private ShipmentRepo shipmentRepo;

    @Override
    public List<Shipment> getAll() {
        return shipmentRepo.findAll();
    }

    @Override
    public Shipment findById(long id) {
        return shipmentRepo.findById(id).orElseThrow(
                () -> new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.SHIPMENT_NOT_FOUND.getCode(), CommonErrorCode.SHIPMENT_NOT_FOUND.getMessage())
        );
    }

    @Override
    public Shipment create(Shipment shipment) {
        return null;
    }

    @Override
    public Shipment update(Shipment shipment) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}

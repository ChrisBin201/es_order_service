package com.chris.orderservice.service;

import com.chris.data.entity.order.OrderLine;

public interface OrderLineService {

    OrderLine updateStatus(long id, String status);
}

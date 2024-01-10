package com.chris.orderservice.service;

import com.chris.data.dto.order.InvoiceDTO;
import com.chris.data.dto.order.OrderDTO;
import com.chris.data.dto.order.CheckoutInfo;
import com.chris.data.entity.order.Order;

public interface OrderService {

    InvoiceDTO create(InvoiceDTO invoiceDTO);

    Order updateStatus (long id, String status);
//    CheckoutInfo getCheckoutInfo(long id);


}

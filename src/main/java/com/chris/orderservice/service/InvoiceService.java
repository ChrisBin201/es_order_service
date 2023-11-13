package com.chris.orderservice.service;

import com.chris.data.dto.order.InvoiceDTO;
import com.chris.data.entity.order.Invoice;

public interface InvoiceService {

    Invoice create(InvoiceDTO invoiceDTO);


}

package com.chris.orderservice.repo;

import com.chris.data.entity.order.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepo extends JpaRepository<OrderLine, Long> {

    @Query("""
            SELECT ol
            FROM OrderLine ol
            WHERE ol.order.id = :invoiceId
    """)
    List<OrderLine> findAllByInvoiceId(long invoiceId);
}

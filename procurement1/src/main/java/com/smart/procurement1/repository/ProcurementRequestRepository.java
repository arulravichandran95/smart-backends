package com.smart.procurement1.repository;

import com.smart.procurement1.model.ProcurementRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProcurementRequestRepository
        extends JpaRepository<ProcurementRequest, Long> {

    // Vendor can view their own requests
    List<ProcurementRequest> findByVendorUsername(String vendorUsername);

    // Admin can view pending / approved / rejected
    List<ProcurementRequest> findByStatus(String status);

    // ✅ Portal: show ALL approved products (no limit, latest first)
    List<ProcurementRequest> findByStatusOrderByCreatedAtDesc(String status);
}

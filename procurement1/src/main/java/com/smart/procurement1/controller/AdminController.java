package com.smart.procurement1.controller;

import com.smart.procurement1.Service.ProcurementRequestService;
import com.smart.procurement1.model.ProcurementRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProcurementRequestService service;

    public AdminController(ProcurementRequestService service) {
        this.service = service;
    }

    // ✅ APPROVED LIST
    @GetMapping("/approved")
    public List<ProcurementRequest> getApprovedRequests() {
        return service.getApprovedRequests();
    }

    // ⏳ PENDING LIST
    @GetMapping("/pending")
    public List<ProcurementRequest> getPendingRequests() {
        return service.getPendingRequests();
    }

    // ✅ APPROVE REQUEST
    @PutMapping("/approve/{id}")
    public ProcurementRequest approveRequest(@PathVariable Long id) {
        return service.updateStatus(id, "APPROVED", null);
    }

    // ❌ REJECT REQUEST (PENDING OR APPROVED) WITH REASON
    @PutMapping("/reject/{id}")
    public ProcurementRequest rejectRequest(
            @PathVariable Long id,
            @RequestBody String reason) {

        return service.updateStatus(id, "REJECTED", reason);
    }
}

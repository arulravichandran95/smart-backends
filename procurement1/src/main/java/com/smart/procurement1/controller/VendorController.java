package com.smart.procurement1.controller;

import com.smart.procurement1.Service.ProcurementRequestService;
import com.smart.procurement1.model.ProcurementRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private final ProcurementRequestService service;

    // ✅ Constructor injection (BEST PRACTICE)
    public VendorController(ProcurementRequestService service) {
        this.service = service;
    }

    /* ================= SUBMIT ================= */
    @PostMapping("/submit")
    public ProcurementRequest submitRequest(
            @RequestBody ProcurementRequest request,
            Authentication authentication) {

        return service.submitRequest(request, authentication);
    }

    /* ================= STATUS (VENDOR ONLY) ================= */
    @GetMapping("/status")
    public List<ProcurementRequest> getStatus(Authentication authentication) {
        return service.getVendorRequests(authentication);
    }

    /* ================= HISTORY (VENDOR ONLY) ================= */
    @GetMapping("/history")
    public List<ProcurementRequest> getHistory(Authentication authentication) {
        return service.getVendorRequests(authentication);
    }

    /* ================= UPDATE (ONLY OWN + PENDING) ================= */
    @PutMapping("/update/{id}")
    public ProcurementRequest update(
            @PathVariable Long id,
            @RequestBody ProcurementRequest req,
            Authentication authentication) {

        return service.updateRequest(id, req, authentication);
    }

    /* ================= DELETE (ONLY OWN + PENDING) ================= */
    @DeleteMapping("/delete/{id}")
    public void delete(
            @PathVariable Long id,
            Authentication authentication) {

        service.deleteRequest(id, authentication);
    }
}

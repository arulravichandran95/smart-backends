package com.smart.procurement1.controller;

import com.smart.procurement1.Service.ProcurementRequestService;
import com.smart.procurement1.model.ProcurementRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portal")
public class PortalController {

    private final ProcurementRequestService service;

    // ✅ Constructor injection (BEST PRACTICE)
    public PortalController(ProcurementRequestService service) {
        this.service = service;
    }

    // 🌐 PUBLIC PORTAL – Approved products (>5 allowed)
    @GetMapping("/products")
    public List<ProcurementRequest> getApprovedProducts() {
        return service.getApprovedProductsForPortal();
    }
}

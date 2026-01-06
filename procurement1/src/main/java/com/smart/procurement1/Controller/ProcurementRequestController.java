package com.smart.procurement1.Controller;

import com.smart.procurement1.model.ProcurementRequest;
import com.smart.procurement1.Service.ProcurementRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class ProcurementRequestController {

    @Autowired
    private ProcurementRequestService service;

    // Vendor submits request
    @PostMapping("/submit-request")
    public ProcurementRequest submit(@RequestBody ProcurementRequest request, Authentication authentication) {
        return service.submitRequest(request, authentication);
    }
}

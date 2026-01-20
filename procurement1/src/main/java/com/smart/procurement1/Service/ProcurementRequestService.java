package com.smart.procurement1.Service;

import com.smart.procurement1.model.ProcurementRequest;
import com.smart.procurement1.repository.ProcurementRequestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcurementRequestService {

    private final ProcurementRequestRepository repository;
    private final SmsService smsService;

    // ✅ Constructor injection (BEST PRACTICE)
    public ProcurementRequestService(ProcurementRequestRepository repository, SmsService smsService) {
        this.repository = repository;
        this.smsService = smsService;
    }

    /* ================= SUBMIT REQUEST (VENDOR) ================= */
    public ProcurementRequest submitRequest(
            ProcurementRequest request,
            Authentication authentication) {

        // 🔐 bind request to logged-in vendor
        request.setVendorUsername(authentication.getName());

        // Auto approval logic
        if (request.getQuantity() >= 3 && request.getPrice() <= 30000) {
            request.setStatus("APPROVED");
        } else {
            request.setStatus("PENDING");
        }

        ProcurementRequest savedRequest = repository.save(request);

        // 📨 Send SMS Notification
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            String msg = "Hello " + authentication.getName() + ", your product '"
                    + request.getProductName() + "' has been submitted successfully! Status: " + request.getStatus();
            smsService.sendSms(request.getPhoneNumber(), msg);
        }

        return savedRequest;
    }

    /* ================= VENDOR: VIEW OWN REQUESTS ================= */
    public List<ProcurementRequest> getVendorRequests(Authentication authentication) {
        String username = authentication.getName();
        return repository.findByVendorUsername(username);
    }

    /* ================= ADMIN: VIEW APPROVED ================= */
    public List<ProcurementRequest> getApprovedRequests() {
        return repository.findByStatus("APPROVED");
    }

    /* ================= ADMIN: VIEW PENDING ================= */
    public List<ProcurementRequest> getPendingRequests() {
        return repository.findByStatus("PENDING");
    }

    /* ================= VENDOR: UPDATE (ONLY OWN + PENDING) ================= */
    public ProcurementRequest updateRequest(
            Long id,
            ProcurementRequest updated,
            Authentication authentication) {

        ProcurementRequest existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // 🔐 ownership check
        if (!existing.getVendorUsername().equals(authentication.getName())) {
            throw new RuntimeException("Unauthorized");
        }

        // 🚫 only pending editable
        if (!"PENDING".equals(existing.getStatus())) {
            throw new RuntimeException("Only pending requests can be edited");
        }

        existing.setProductName(updated.getProductName());
        existing.setCategory(updated.getCategory());
        existing.setQuantity(updated.getQuantity());
        existing.setPrice(updated.getPrice());

        return repository.save(existing);
    }

    /* ================= VENDOR: DELETE (ONLY OWN + PENDING) ================= */
    public void deleteRequest(Long id, Authentication authentication) {

        ProcurementRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getVendorUsername().equals(authentication.getName())) {
            throw new RuntimeException("Unauthorized");
        }

        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Cannot delete approved/rejected request");
        }

        repository.delete(request);
    }

    /* ================= ADMIN: APPROVE / REJECT WITH REASON ================= */
    public ProcurementRequest updateStatus(
            Long id,
            String status,
            String reason) {

        ProcurementRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(status);

        if ("REJECTED".equals(status)) {
            request.setRejectionReason(reason);
        } else {
            request.setRejectionReason(null);
        }

        // 📨 Send SMS on Approval
        if ("APPROVED".equals(status) && request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            String msg = "Congratulations " + request.getVendorUsername() + "! Your product '"
                    + request.getProductName() + "' has been APPROVED by the admin.";
            smsService.sendSms(request.getPhoneNumber(), msg);
        }

        // 📨 Send SMS on Rejection
        if ("REJECTED".equals(status) && request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            String msg = "Alert: " + request.getVendorUsername() + ", your product '"
                    + request.getProductName() + "' has been REJECTED. Reason: " + reason;
            smsService.sendSms(request.getPhoneNumber(), msg);
        }

        return repository.save(request);
    }

    /* ================= PORTAL: SHOW APPROVED PRODUCTS ================= */
    public List<ProcurementRequest> getApprovedProductsForPortal() {
        return repository.findByStatusOrderByCreatedAtDesc("APPROVED");
    }
}

package com.smart.procurement1.controller;

import com.smart.procurement1.model.User;
import com.smart.procurement1.repository.UserRepository;
import com.smart.procurement1.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

        // 🔐 HARD-CODED ADMIN (ONLY ADMIN)
        private static final String ADMIN_USERNAME = "Arul";
        private static final String ADMIN_PASSWORD = "Arul123";

        public AuthController(
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {

                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtUtil = jwtUtil;
        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody User user) {

                // ❌ Prevent admin registration
                if ("ROLE_ADMIN".equals(user.getRole())) {
                        return ResponseEntity
                                        .status(403)
                                        .body("Admin registration not allowed");
                }

                // ❌ Username already exists
                if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                        return ResponseEntity
                                        .badRequest()
                                        .body("Username already exists");
                }

                // ✅ Encode password
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                // ✅ Force vendor role
                user.setRole("ROLE_VENDOR");

                userRepository.save(user);

                return ResponseEntity.ok("Vendor registered successfully");
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody User login) {

                /* ================= ADMIN ONLY (HARDCODED) ================= */
                if (ADMIN_USERNAME.equals(login.getUsername())
                                && ADMIN_PASSWORD.equals(login.getPassword())) {

                        String token = jwtUtil.generateToken(
                                        ADMIN_USERNAME,
                                        "ROLE_ADMIN");

                        return ResponseEntity.ok(
                                        Map.of(
                                                        "token", token,
                                                        "role", "ROLE_ADMIN"));
                }

                /* ================= VENDOR LOGIN (UNCHANGED) ================= */
                return userRepository.findByUsername(login.getUsername())
                                .map(user -> {

                                        if (!passwordEncoder.matches(
                                                        login.getPassword(),
                                                        user.getPassword())) {

                                                return ResponseEntity
                                                                .status(401)
                                                                .body("Invalid credentials");
                                        }

                                        String token = jwtUtil.generateToken(
                                                        user.getUsername(),
                                                        user.getRole());

                                        return ResponseEntity.ok(
                                                        Map.of(
                                                                        "token", token,
                                                                        "role", user.getRole()));
                                })
                                .orElse(
                                                ResponseEntity.status(401)
                                                                .body("User not found"));
        }
}

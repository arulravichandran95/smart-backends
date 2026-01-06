package com.smart.procurement1.Controller;

import com.smart.procurement1.model.User;
import com.smart.procurement1.repository.UserRepository;
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

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole());
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // ✅ LOGIN (NO JWT)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        return userRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (!passwordEncoder.matches(
                            loginRequest.getPassword(),
                            user.getPassword())) {
                        return ResponseEntity.status(401).body("Invalid password");
                    }

                    return ResponseEntity.ok(
                            Map.of(
                                    "username", user.getUsername(),
                                    "role", user.getRole()
                            )
                    );
                })
                .orElse(ResponseEntity.status(401).body("User not found"));
    }
}

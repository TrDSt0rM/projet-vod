package combis.controllers;

import combis.dtos.Authresponse;
import combis.dtos.LoginRequest;
import combis.dtos.RegisterRequest;
import combis.dtos.user;
import combis.services.impl.AuthServiceIMPL;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceIMPL authService;

    public AuthController(AuthServiceIMPL authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public user register(@Valid @RequestBody RegisterRequest dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public Authresponse login(@Valid @RequestBody LoginRequest dto) {
        return authService.login(dto);
    }

    @GetMapping("/me")
    public user me(@RequestHeader(name = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return authService.me(null);
        }
        String token = authorization.substring("Bearer ".length()).trim();
        return authService.me(token);
    }
}

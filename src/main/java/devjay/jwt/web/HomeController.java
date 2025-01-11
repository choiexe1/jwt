package devjay.jwt.web;

import devjay.jwt.service.AuthService;
import devjay.jwt.web.argumentresolver.Payload;
import devjay.jwt.web.dto.request.LoginDTO;
import devjay.jwt.web.dto.request.RegisterDTO;
import devjay.jwt.web.dto.response.MemberResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDTO> register(@Validated @RequestBody RegisterDTO dto) {

        return ResponseEntity.ok(authService.register(dto).toResponseDTO());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginDTO dto, HttpServletResponse response) {
        Tokens tokens = authService.authenticate(dto);

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.accessToken());
        response.setHeader("Set-Cookie", "refresh_token=" + tokens.refreshToken() + "; HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=3600");

        return ResponseEntity.ok("Login successful");
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secure(@Payload TokenPayload payload) {
        return ResponseEntity.ok("Authenticated");
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@Payload TokenPayload payload, @CookieValue("refresh_token") String refreshToken) {
        log.info("refreshToken = {}", refreshToken);

        String newRefreshToken = authService.verifyRefreshToken(payload.id(), refreshToken);

        HashMap<String, String> map = new HashMap<>();
        map.put("oldRefreshToken", refreshToken);
        map.put("newRefreshToken", newRefreshToken);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Payload TokenPayload payload,
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse response) {
        response.setHeader(HttpHeaders.AUTHORIZATION, null);
        response.setHeader("Set-Cookie", "refresh_token=; HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=0");

        return ResponseEntity.ok("logout");
    }
}

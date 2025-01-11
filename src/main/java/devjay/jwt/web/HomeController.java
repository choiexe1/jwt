package devjay.jwt.web;

import com.auth0.jwt.interfaces.Claim;
import devjay.jwt.Tokens;
import devjay.jwt.domain.Member;
import devjay.jwt.service.AuthService;
import devjay.jwt.web.dto.LoginDTO;
import devjay.jwt.web.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Member> register(@Validated @RequestBody RegisterDTO dto) {

        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginDTO dto, HttpServletResponse response) {
        Tokens tokens = authService.authenticate(dto);

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.accessToken());
        response.setHeader("Set-Cookie", "refresh_token=" + tokens.refreshToken() + "; HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=3600");

        return ResponseEntity.ok("Login successful");
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secure(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        String accessToken = bearerToken.substring(7);
        Map<String, Claim> claims = JwtUtil.verifyAccessToken(accessToken);
        Long id = claims.get("id").asLong();
        String username = claims.get("username").asString();

        log.info("id = {}", id);
        log.info("username = {}", username);

        return ResponseEntity.ok("Authenticated");
    }
}

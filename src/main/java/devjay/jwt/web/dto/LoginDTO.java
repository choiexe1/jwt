package devjay.jwt.web.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(
        @NotEmpty(message = "유저네임은 필수 입력 값입니다.") String username,
        @NotEmpty(message = "비밀번호는 필수 입력 값입니다.") String password
) {
}

package devjay.jwt.web.dto;

import devjay.jwt.domain.Member;
import devjay.jwt.domain.Role;
import jakarta.validation.constraints.NotEmpty;

public record RegisterDTO(
        @NotEmpty(message = "유저네임은 필수 입력 값입니다.") String username,
        @NotEmpty(message = "비밀번호는 필수 입력 값입니다.") String password
) {

    public Member toMember(Role role) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setRole(role);

        return member;
    }
}

package devjay.jwt.web.dto.response;

import devjay.jwt.domain.Role;

public record MemberResponseDTO(
        Long id,
        String username,
        Role role
) {
}

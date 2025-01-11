package devjay.jwt.domain;


import devjay.jwt.web.dto.response.MemberResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private String refreshToken;

    public MemberResponseDTO toResponseDTO() {
        return new MemberResponseDTO(id, username, role);
    }
}

package id.co.plniconplus.newito.auth.dto;

import id.co.plniconplus.newito.user.dto.UserDto;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse implements Serializable {
    private boolean success;

    private String message;

    private String token;

    private String accessToken;

    private UserDto data;
}

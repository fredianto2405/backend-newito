package id.co.plniconplus.newito.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements Serializable {
    @NotNull
    @Schema(name = "username", example = "53871.ADMIN")
    private String username;

    @NotNull
    @Schema(name = "password", example = "12345678")
    private String password;
}

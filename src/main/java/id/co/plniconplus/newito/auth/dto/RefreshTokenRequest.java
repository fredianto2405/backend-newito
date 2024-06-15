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
public class RefreshTokenRequest implements Serializable {
    @NotNull
    @Schema(name = "token", example = "738480b3-102f-4628-8307-31b8e21fff5f")
    private String token;
}

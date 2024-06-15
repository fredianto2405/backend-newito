package id.co.plniconplus.newito.user.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionDto implements Serializable {
    private String username;

    private String password;

    private String nama;
}

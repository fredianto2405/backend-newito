package id.co.plniconplus.newito.user.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private String username;

    private String nama;
}

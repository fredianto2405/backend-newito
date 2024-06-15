package id.co.plniconplus.newito.system.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PingResponse implements Serializable {
    private String pong;
}

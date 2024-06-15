package id.co.plniconplus.newito.exception.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {
    private final boolean success = false;

    private String message;

    private LocalDateTime timestamp;
}

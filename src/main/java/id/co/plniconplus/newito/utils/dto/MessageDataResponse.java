package id.co.plniconplus.newito.utils.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDataResponse implements Serializable {
    private boolean success;

    private String message;

    private Object data;
}

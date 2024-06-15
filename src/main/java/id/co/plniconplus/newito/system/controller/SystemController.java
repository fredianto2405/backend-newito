package id.co.plniconplus.newito.system.controller;

import id.co.plniconplus.newito.exception.dto.ErrorResponse;
import id.co.plniconplus.newito.system.dto.PingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "System", description = "System Controller")
public class SystemController {
    @Operation(summary = "Check System Health")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PingResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/api/system/ping")
    @CrossOrigin(value = "*")
    public ResponseEntity<?> ping(Authentication auth) {
        log.info("Hello you're login as {}", auth.getName());
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            PingResponse pingResponse = PingResponse.builder()
                    .pong(currentDateTime.toString())
                    .build();
            return ResponseEntity.ok(pingResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

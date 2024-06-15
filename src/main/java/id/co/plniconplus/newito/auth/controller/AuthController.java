package id.co.plniconplus.newito.auth.controller;

import id.co.plniconplus.newito.auth.dto.LoginRequest;
import id.co.plniconplus.newito.auth.dto.LoginResponse;
import id.co.plniconplus.newito.auth.dto.RefreshTokenRequest;
import id.co.plniconplus.newito.exception.dto.ErrorResponse;
import id.co.plniconplus.newito.security.jwt.JwtTokenUtil;
import id.co.plniconplus.newito.security.service.CustomUserDetailsService;
import id.co.plniconplus.newito.session.service.SessionService;
import id.co.plniconplus.newito.user.dto.UserDto;
import id.co.plniconplus.newito.user.dto.UserSessionDto;
import id.co.plniconplus.newito.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Auth Controller")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;

    private final SessionService sessionService;

    private final UserService userService;

    private final CustomUserDetailsService customUserDetailsService;

    @Operation(summary = "Authenticate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/auth/login")
    @CrossOrigin(value = "*")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        userService.authenticate(request.getUsername(), request.getPassword());

        String sessionToken = sessionService.createRefreshToken(request.getUsername());

        customUserDetailsService.loadUserByUsername(request.getUsername());
        UserSessionDto userSession = customUserDetailsService.getUserSession();
        String accessToken = jwtTokenUtil.generateAccessToken(userSession);

        UserDto data = UserDto.builder()
                .nama(userSession.getNama())
                .username(userSession.getUsername())
                .build();

        log.info("Login successful for user: {}", request.getUsername());
        return ResponseEntity.ok().body(
                LoginResponse.builder()
                        .success(true)
                        .message("success")
                        .token(sessionToken)
                        .accessToken(accessToken)
                        .data(data)
                        .build()
        );
    }

    @Operation(summary = "Refresh Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/auth/refresh-token")
    @CrossOrigin(value = "*")
    public LoginResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        sessionService.verifyToken(request.getToken());
        UserSessionDto userSession = sessionService.updateExpiryDate(request.getToken());
        String accessToken = jwtTokenUtil.generateAccessToken(userSession);

        log.info("Refresh token successful: {}", request.getToken());
        return LoginResponse.builder()
                .success(true)
                .message("success")
                .token(request.getToken())
                .accessToken(accessToken)
                .build();
    }
}

package id.co.plniconplus.newito.session.service;

import id.co.plniconplus.newito.user.dto.UserSessionDto;
import id.co.plniconplus.newito.utils.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {
    private final JdbcTemplate jdbcTemplate;

    public String createRefreshToken(String username) {
        try {
            String token = UUID.randomUUID().toString();
            Instant expiryDate = Instant.now().plusMillis(Parameters.JWT_EXPIRE_DURATION + 60000);

            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String sql = "{? = call newito.bo_create_refresh_token(?, ?, ?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, token);
            callableStatement.setString(3, convertExpiryDateToDateString(expiryDate));
            callableStatement.setString(4, username);
            callableStatement.execute();
            String result = callableStatement.getString(1);

            callableStatement.close();
            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void verifyToken(String token) {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String sql = "{? = call newito.bo_verify_token(?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, token);
            callableStatement.execute();
            String result = callableStatement.getString(1);

            callableStatement.close();
            connection.close();

            String[] resultArray = result.split("##");
            String callback = resultArray[0];
            String message = resultArray[1];

            if (callback.equals("-1")) {
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserSessionDto updateExpiryDate(String token) {
        try {
            Instant expiryDate = Instant.now().plusMillis(Parameters.JWT_EXPIRE_DURATION + 60000);

            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String sql = "{? = call newito.bo_update_expiry_date(?, ?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, token);
            callableStatement.setString(3, convertExpiryDateToDateString(expiryDate));
            callableStatement.execute();
            String result = callableStatement.getString(1);

            callableStatement.close();
            connection.close();

            return convertResultToUserSessionDto(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private String convertExpiryDateToDateString(Instant expiryDate) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiryDate, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    private UserSessionDto convertResultToUserSessionDto(String result) {
        String[] resultArray = result.split("#");
        String username = resultArray[0];
        String password = resultArray[1];
        String nama = resultArray[2];

        return UserSessionDto.builder()
                .username(username)
                .password(password)
                .nama(nama)
                .build();
    }
}

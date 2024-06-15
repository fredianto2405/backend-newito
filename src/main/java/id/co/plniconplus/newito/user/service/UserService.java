package id.co.plniconplus.newito.user.service;

import id.co.plniconplus.newito.auth.dto.LoginResponse;
import id.co.plniconplus.newito.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    public LoginResponse authenticate(String username, String password) {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String sql = "{? = call newito.bo_getlogin(?, ?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, username);
            callableStatement.setString(3, password);
            callableStatement.execute();
            String result = callableStatement.getString(1);

            callableStatement.close();
            connection.close();

            return convertResultToLoginResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private LoginResponse convertResultToLoginResponse(String result) {
        String[] resultArray = result.split("##");
        String callback = resultArray[0];
        String message = resultArray[1];

        if (callback.equals("1")) {
            UserDto userDto = convertDataUserDto(resultArray[2]);
            return LoginResponse.builder()
                    .success(true)
                    .message(message)
                    .data(userDto)
                    .build();
        } else {
            throw new RuntimeException(message);
        }
    }

    private UserDto convertDataUserDto(String data) {
        String[] dataArray = data.split("#");
        return UserDto.builder()
                .username(dataArray[0])
                .nama(dataArray[1])
                .build();
    }
}

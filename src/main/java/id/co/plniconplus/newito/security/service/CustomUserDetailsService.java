package id.co.plniconplus.newito.security.service;

import id.co.plniconplus.newito.user.dto.UserSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    UserSessionDto userSession;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String sql = "{? = call newito.bo_find_user_by_username(?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.setString(2, username);
            callableStatement.execute();
            String result = callableStatement.getString(1);

            callableStatement.close();
            connection.close();

            UserSessionDto userSessionDto = convertResultToUserSessionDto(result);
            this.userSession = userSessionDto;

            return new User(userSessionDto.getUsername(), userSessionDto.getPassword(), new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserSessionDto getUserSession() {
        return this.userSession;
    }

    private UserSessionDto convertResultToUserSessionDto(String result) {
        String[] resultArray = result.split("##");
        String callback = resultArray[0];
        String message = resultArray[1];

        if (callback.equals("1")) {
            String[] dataArray = resultArray[2].split("#");
            return UserSessionDto.builder()
                    .username(dataArray[0])
                    .password(dataArray[1])
                    .nama(dataArray[2])
                    .build();
        } else {
            throw new RuntimeException(message);
        }
    }
}

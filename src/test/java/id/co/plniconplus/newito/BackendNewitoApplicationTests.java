package id.co.plniconplus.newito;

import id.co.plniconplus.newito.auth.dto.LoginResponse;
import id.co.plniconplus.newito.session.service.SessionService;
import id.co.plniconplus.newito.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class BackendNewitoApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	SessionService sessionService;

	@Test
	void contextLoads() {
	}

	@Test
	void authenticateTest() {
		String username = "53871.ADMIN";
		String password = "12345678";

		LoginResponse response = userService.authenticate(username, password);

		log.info("success={}, message={}, data={}", response.isSuccess(),
				response.getMessage(),
				response.getData());
	}

	@Test
	void createRefreshTokenTest() {
		String username = "53871.ADMIN";
		String token = sessionService.createRefreshToken(username);
		log.info("token={}", token);
	}
}

package babji.sharesafe.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;

//@TypeHints({
//		@TypeHint(types = HttpStatus.class)
//})
@OpenAPIDefinition(info = @Info(title = "Share Safe API", version = "1.0", description = "API to secure share files"))
@SpringBootApplication
public class ShareSafeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareSafeApiApplication.class, args);
	}

}

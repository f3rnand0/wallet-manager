package leovegas.challenge.walletmanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Wallet Manager", version = "1.0",
    description = "A simple API to manage wallet from users"))
@SpringBootApplication
public class WalletManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletManagerApplication.class, args);
    }

}

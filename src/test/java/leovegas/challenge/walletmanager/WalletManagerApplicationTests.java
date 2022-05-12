package leovegas.challenge.walletmanager;

import static org.assertj.core.api.Assertions.assertThat;

import leovegas.challenge.walletmanager.controller.IdGenerationController;
import leovegas.challenge.walletmanager.controller.WalletManagerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WalletManagerApplicationTests {

    @Autowired
    IdGenerationController idGenerationController;

    @Autowired
    WalletManagerController walletManagerController;

    @Test
    void contextLoads() {
        assertThat(idGenerationController).isNotNull();
        assertThat(walletManagerController).isNotNull();
    }
}

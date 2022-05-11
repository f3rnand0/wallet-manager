package leovegas.challenge.walletmanager;

import leovegas.challenge.walletmanager.controller.IdGenerationController;
import leovegas.challenge.walletmanager.controller.WalletManagerController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WalletManagerApplicationTests {

    @Autowired
    IdGenerationController idGenerationController;

    @Autowired
    WalletManagerController walletManagerController;

    @Test
    public void contextLoads() {
        Assertions.assertThat(idGenerationController).isNot(null);
        Assertions.assertThat(walletManagerController).isNot(null);
    }
}

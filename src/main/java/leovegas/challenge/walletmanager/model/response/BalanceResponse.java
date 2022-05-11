package leovegas.challenge.walletmanager.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceResponse {

    private String firstName;

    private String lastName;

    private String username;

    private Integer accountNumber;

    private Double balance;
}

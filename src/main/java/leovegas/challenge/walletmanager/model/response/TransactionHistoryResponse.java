package leovegas.challenge.walletmanager.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionHistoryResponse {

    private String username;

    private Integer accountNumber;

    private List<TransactionResponse> transactions;


}

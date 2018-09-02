package tduong.de.types;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionInput {
	private String transactionOutputId;
	private TransactionOutput UTXO;

	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

}

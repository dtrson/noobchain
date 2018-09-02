package tduong.de.types;

import lombok.Getter;

@Getter
public class TransactionInput {
	private String transactionOutputId;
	private TransactionOutput UTXO;

	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

}

package tduong.de.types;

import java.security.PublicKey;

import lombok.Getter;
import tduong.de.utils.HashUtil;

@Getter
public class TransactionOutput {
	private String id;
	private PublicKey reciepient; // also known as the new owner of these coins.
	private float value;
	private String parentTransactionId; // the id of the transaction this output was created in

	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = HashUtil
				.applySha256(HashUtil.getStringFromKey(reciepient) + Float.toString(value) + parentTransactionId);
	}

	// check if coin belongs to you
	public boolean isMine(PublicKey publicKey) {
		return publicKey == reciepient;
	}
}

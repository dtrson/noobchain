package tduong.de.types;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import tduong.de.utils.HashUtil;

@Getter
public class Transaction {

	private String transactionId;
	private PublicKey sender;
	private PublicKey reciepient;
	private float value;
	private byte[] signature;
	private List<TransactionInput> inputs = new ArrayList<TransactionInput>();
	private List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	private static int sequence = 0;

	public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
		super();
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}

	// Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(reciepient) + Float.toString(value);
		signature = HashUtil.applyECDSASig(privateKey, data);
	}

	// Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		String data = HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(reciepient) + Float.toString(value);
		return HashUtil.verifyECDSASig(sender, data, signature);
	}

	private String calulateHash() {
		sequence++; // increase the sequence to avoid 2 identical transactions having the same hash
		return HashUtil.applySha256(HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(reciepient)
				+ Float.toString(value) + sequence);
	}

}

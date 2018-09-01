package tduong.de.types;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import tduong.de.utils.HashUtil;

public class Transaction {

	public String transactionId;
	public PublicKey sender;
	public PublicKey reciepient;
	public float value;
	public byte[] signature;
	public List<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	private static int sequence = 0;

	public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
		super();
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}

	private String calulateHash() {
		sequence++; // increase the sequence to avoid 2 identical transactions having the same hash
		return HashUtil.applySha256(HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(reciepient)
				+ Float.toString(value) + sequence);
	}

}
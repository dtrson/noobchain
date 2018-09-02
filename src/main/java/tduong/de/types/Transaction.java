package tduong.de.types;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import tduong.de.NoobChain;
import tduong.de.utils.HashUtil;

@Getter
@Setter
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

	public boolean processTransaction() {

		if (!verifiySignature()) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
		// gather transaction inputs (Make sure they are unspent):
		for (TransactionInput input : inputs) {
			input.setUTXO(NoobChain.UTXOs.get(input.getTransactionOutputId()));

		}
		// check if transaction is valid:
		if (getInputsValue() < NoobChain.minimumTransaction) {
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}
		// generate transaction outputs:
		float leftOver = getInputsValue() - value; // get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); // send value to recipient
		outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); // send the left over 'change' back to
																					// sender

		// add outputs to Unspent list
		for (TransactionOutput o : outputs) {
			NoobChain.UTXOs.put(o.getId(), o);
		}

		// remove transaction inputs from UTXO lists as spent:
		for (TransactionInput i : inputs) {
			if (i.getUTXO() == null)
				continue; // if Transaction can't be found skip it
			NoobChain.UTXOs.remove(i.getUTXO().getId());
		}
		return true;

	}

	// returns sum of inputs(UTXOs) values
	public float getInputsValue() {
		float total = 0;
		for (TransactionInput i : inputs) {
			if (i.getUTXO() == null)
				continue; // if Transaction can't be found skip it
			total += i.getUTXO().getValue();
		}
		return total;
	}

	// returns sum of outputs:
	public float getOutputsValue() {
		float total = 0;
		for (TransactionOutput o : outputs) {
			total += o.getValue();
		}
		return total;
	}

	private String calulateHash() {
		sequence++; // increase the sequence to avoid 2 identical transactions having the same hash
		return HashUtil.applySha256(HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(reciepient)
				+ Float.toString(value) + sequence);
	}

}

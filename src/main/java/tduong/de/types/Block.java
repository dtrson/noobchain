package tduong.de.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import tduong.de.utils.HashUtil;

@Getter
public class Block {

	private String hash;
	private String previousHash;
	private String merkleRoot;
	public List<Transaction> transactions = new ArrayList<Transaction>();
	private String data; // our data will be a simple message.
	private long timeStamp; // as number of milliseconds since 1/1/1970.
	private int nonce;

	// Block Constructor.
	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}

	// Block Constructor.
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();

		this.hash = calculateHash(); // Making sure we do this after we set the other values.
	}

	public String calculateHash() {
		String calculatedhash = HashUtil
				.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
		return calculatedhash;
	}

	// Increases nonce value until hash target is reached.
	public void mineBlock(int difficulty) {
		merkleRoot = HashUtil.getMerkleRoot(transactions);
		String target = HashUtil.getDificultyString(difficulty); // Create a string with difficulty * "0"
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}

	// Add transactions to this block
	public boolean addTransaction(Transaction transaction) {
		// process transaction and check if valid, unless block is genesis block then
		// ignore.
		if (transaction == null)
			return false;
		if ((previousHash != "0")) {
			if ((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
}
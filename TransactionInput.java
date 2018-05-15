public class TransactionInput {
	public String transactionOutputId; //ссылка на TransactionOutputs -> transactionId
	public TransactionOutput UTXO; //Содержит вывод неизрасходованных транзакций
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
}

import java.security.*;
import java.util.ArrayList;

public class Transaction {
	
	public String transactionId; //Содержит хэш транзакции
	public PublicKey sender; //адрес отправителя /public key
	public PublicKey reciepient; //адрес получателя /public key
	public float value; //Количество коинов для отправки
	public byte[] signature; //Это должно помешать использовать средства в кошельке кому либо еще
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int sequence = 0; //Число транзакций
	
	//Конструктор:
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	public boolean processTransaction() {
		
		if(verifySignature() == false) {
			System.out.println("#Подпись транзакции не удалось проверить");
			return false;
		}
				
		//Сборка входов транзакций
		for(TransactionInput i : inputs) {
			i.UTXO = SimpleBlockchain.UTXOs.get(i.transactionOutputId);
		}

		//Проверяем валидность транзакции
		if(getInputsValue() < SimpleBlockchain.minimumTransaction) {
			System.out.println("В транзакции сумма меньше минимальной: " + getInputsValue());
			System.out.println("Пожалуйста, введите сумму превышающую " + SimpleBlockchain.minimumTransaction);
			return false;
		}
		
		//Генерируем выходы транзакции
		float leftOver = getInputsValue() - value;
		transactionId = calulateHash();
		outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //отправляем коины получателю
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //аналогично отправителю
				
		//Добавляем выходы в UTXO
		for(TransactionOutput o : outputs) {
			SimpleBlockchain.UTXOs.put(o.id , o);
		}
		
		//Удаляем входы транзакции из UTXO как потраченные:
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //Пропускем, если не была найдена транзакция
			SimpleBlockchain.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
	
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //Пропускем, если не была найдена транзакция
			total += i.UTXO.value;
		}
		return total;
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		signature = HashUtil.applyECDSASig(privateKey,data);		
	}
	
	public boolean verifySignature() {
		String data = HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		return HashUtil.verifyECDSASig(sender, data, signature);
	}
	
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}
	
	private String calulateHash() {
		sequence++; //увеличиваем sequence для избежания идентичных транзакций с одинаковым хэшем
		return HashUtil.applySha256(
				HashUtil.getStringFromKey(sender) +
				HashUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	}
}

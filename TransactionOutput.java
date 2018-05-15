import java.security.PublicKey;

public class TransactionOutput {
	public String id;
	public PublicKey reciepient; //получатель
	public float value; //количество монет
	public String parentTransactionId; //id родительской транзакции
	
	//Конструктор
	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = HashUtil.applySha256(HashUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
	}
	
	//Проверяем, принадлежит ли нам монета
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == reciepient);
	}
	
}

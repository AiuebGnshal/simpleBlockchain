import java.util.ArrayList;
import java.util.Date;

public class Block {
	
	public String hash;
	public static long ID = 0;
	public long blockID;
	public String previousHash; 
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	public long timeStamp;
	public int nonce;
	
	//Конструктор
	public Block(String previousHash ) {
		this.blockID = ID++;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		
		this.hash = calculateHash(); //Рассчитываем хэш
	}
	
	//Расчет нового хэша на основе данных блока
	public String calculateHash() {
		String calculatedhash = HashUtil.applySha256( 
				previousHash +
				Long.toString(timeStamp) +
				blockID +
				Integer.toString(nonce) + 
				merkleRoot
				);
		return calculatedhash;
	}
	
	//увеличиваем "счетчик" nonce для генерации хэша необходимой сложности
	public void mineBlock(int difficulty) {
		merkleRoot = HashUtil.getMerkleRoot(transactions);
		String target = HashUtil.getDificultyString(difficulty); //Создаем строку со сложностью "0"
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Блок замайнен : " + hash);
	}
	
	//Добавляем транзакцию в блок
	public boolean addTransaction(Transaction transaction) {
		//Проверяем на валидность и первичность транзакцию и затем добавляем
		if(transaction == null) return false;		
		if((!"0".equals(previousHash))) {
			if((transaction.processTransaction() != true)) {
				System.out.println("Транзакция не была обработана");
				return false;
			}
		}

		transactions.add(transaction);
		System.out.println("Транзакция успешно добавлена в блок");
		return true;
	}
	
}

import java.security.Security;
import java.util.*;
import com.google.gson.GsonBuilder;

public class SimpleBlockchain {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static int difficulty = 3;
	public static float minimumTransaction = 0.1f;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;

	public static void main(String[] args) {	
		//добавляем блоки в blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Инициализация провайдера bc
		
		//Создаем 2 кошелька:
		walletA = new Wallet();
		walletB = new Wallet();		
		Wallet coinbase = new Wallet();
		
		//создаем первую (genesis) транзакцию, которая присваивает 100 коинов кошельку walletA:
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //вручную подпишем первичную транзакцию
		genesisTransaction.transactionId = "0"; //вручную добавляем transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //вручную добавляем Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //сохраняем первую транзакцию в список UTXO.
		
		System.out.println("Создаем и майним первичный блок... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		//тест
		Block block1 = new Block(genesis.hash);
		System.out.println("\nБаланс WalletA: " + walletA.getBalance());
		System.out.println("\nПытаемся отправить 40 коинов из кошелька WalletA в кошелек WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nБаланс WalletA: " + walletA.getBalance());
		System.out.println("Баланс WalletB: " + walletB.getBalance());
		
		Block block2 = new Block(block1.hash);
		System.out.println("\nПытаемся отправить 1000 коинов из кошелька WalletA в кошелек WalletB...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nБаланс WalletA: " + walletA.getBalance());
		System.out.println("Баланс WalletB: " + walletB.getBalance());
		
		Block block3 = new Block(block2.hash);
		System.out.println("\nПытаемся отправить 20 коинов из кошелька WalletB в кошелек WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
		System.out.println("\nБаланс WalletA: " + walletA.getBalance());
		System.out.println("\nБаланс WalletB: " + walletB.getBalance());
		
		isChainValid();
		
	}
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //временный рабочий список неизрасходованных транзакций в заданном состоянии блока.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		
		//проходим по цепи и проверяем хэши
		for(int i=1; i < blockchain.size(); i++) {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//сравниваем хэш с расчитанным
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("#Текущие хэши не эквиваленты");
				return false;
			}
			//сравниваем previousHash с хэшем предыдущего блока
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("#Хэш предыдущего блока не совпал с previousHash текущего");
				return false;
			}
			//проверка на сложность при майнинге
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("#Блок не был замайнен");
				return false;
			}
			
			//проходимся по блоку транзакций
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				
				if(!currentTransaction.verifySignature()) {
					System.out.println("#Подпись (" + t + ") транзакции неверная");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Вход и выход (" + t + ") транзакции не равны");
					return false; 
				}
				
				for(TransactionInput input: currentTransaction.inputs) {	
					tempOutput = tempUTXOs.get(input.transactionOutputId);
					
					if(tempOutput == null) {
						System.out.println("#Отсутствует ссылка на транзакцию(" + t + ")");
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Транзакция (" + t + ") имеет некорректное значение");
						return false;
					}
					
					tempUTXOs.remove(input.transactionOutputId);
				}
				
				for(TransactionOutput output: currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}
				
				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
					System.out.println("#Транзакция (" + t + ") имеет другого получателя");
					return false;
				}
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
					System.out.println("#Транзакция (" + t + ") не имеет отправителя.");
					return false;
				}
				
			}
			
		}
		System.out.println("Цепочка валидна");
		return true;
	}
	
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}

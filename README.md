# simple blockchain


## class Block
|                |                    |
|----------------|--------------------|
|**Fields**	                          |
|                |static ID           |
|                |ID                  |
|                |timeStamp           |
|                |previousHash        |
|                |hash                |
|                |transactions[]      |
|                |merkleRoot          |
|                |nonce               |
|**Methods**                          |
|                |mineBlock           |
|                |addTransaction      |
|                |calculateHash       |

## class HashUtil
|                |                    |
|----------------|--------------------|
|**Methods**                          |
|                |applySha256         |
|                |applyECDSASig       |
|                |verifyECDSASig      |
|                |getDificultyString  |
|                |getStringFromKey    |
|                |getMerkleRoot       |
|                |getJson             |


## class Transaction and Wallet

|**Transaction**    |   **Wallet**   |
|-------------------|----------------|
|**Fields**         | privateKey     |
|transactionId      | publicKey      |  
|sender             | UTXOs          |
|recepient          |**Methods**     |
|value              |generateKeyPair |
|signature[]        |getBalance      |
|**Methods**        |sendFunds       |
|processTransaction ||
|getInputsValue     ||
|calulateHash       ||
|getOutputsValue    ||
|verifySignature    ||
|generateSignature  ||


## class TransactionInput and TransactionOutput

|**TransactionInput**|**TransactionOutput**|
|--------------------|---------------------|
|**Fields**          |                     |
|transactionOutputId | id                  |  
|UTXO                | reciepient          |
|                    | value               |
|                    | parentTransactionId |
|                    | **Methods**         |
|                    | isMine              |


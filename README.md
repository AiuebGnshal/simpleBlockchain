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
|**Fields**         |                |
|transactionId      | privateKey     |  
|sender             | publicKey      |
|recepient          | UTXOs          |
|value              |                |
|signature[]        |                |
|**Methods**        |                |
|processTransaction |generateKeyPair |
|getInputsValue     |getBalance      |
|calulateHash       |sendFunds       |
|getOutputsValue    |                |
|verifySignature    |                |
|generateSignature  |                |


## class TransactionInput and TransactionOutput

|**TransactionInput**|**TransactionOutput**|
|--------------------|---------------------|
|**Fields**          |                     |
|transactionOutputId | id                  |  
|UTXO                | reciepient          |
|                    | value               |
|                    | parentTransactionId |
| **Methods**        |                     |
|                    | isMine              |


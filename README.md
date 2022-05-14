# WALLET MANAGER

## Description

This Java Rest API provides some endpoints to manage wallets from different users. Every user owns
only one account, and every account is related with zero or more transactions. A transaction can be
of two types: DEBIT and CREDIT.
This application uses a file-based storage, so it's possible to keep the information across
restarts.
To access the Open API documentation go to http://localhost:8080/swagger-ui/index.html
A database diagram is inside the docs folder.

### Endpoints

1. /api/generateId: generates a type 4 UUID string.
2. /api/getBalance/{username}: returns the current balance of a user's account by using the provided
   {username}.
3. /api/debit: debits a specified amount to a user's account by providing an amount and a unique
   transaction id. An error will be returned if the username or account doesn't exist; the same
   transaction id was used by a previous transaction; or there aren't enough funds.
4. /api/credit: credits a specified amount to a user's account by providing an amount and a unique
   transaction id. An error will be returned if the username or account doesn't exist; or the same
   transaction id was used by a previous transaction.
5. /api/getTransactionHistory/{username}: provides a list of transactions ordered upwardly by
   creation date of a user's account by using the provided {username}.

### Prerequisites

- You will need the following installed:

```
Java 11 or above
Gradle 7.4.1 or above
```

### Building

- There are two ways to build this application. One way is to by getting a jar file using gradle,
  just run:

```
./gradlew build (Linux/Mac) or gradlew.bat build (Windows)
```

- The other way is to build a Docker image using the provided Dockerfile. The following command will
  do so:

```
docker build -t wallet-manager .
```

### Executing

- To execute the application from a single jar to test it, just run:

```
DATABASE_URL=jdbc:h2:file:~/walletmanager DATABASE_USERNAME=sa DATABASE_PASSWORD= java -jar wallet-manager-1.0.0.jar
```

- To generate a container from the image of the previous step execute:

```
docker run -p 8080:8080 -d --name wallet-manager-1.0.0 wallet-manager
```
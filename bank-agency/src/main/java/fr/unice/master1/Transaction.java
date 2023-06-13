package fr.unice.master1;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.*;
/**
 * @author Mohamed ELYSALEM
 * @author Eliel WOTOBE
 */
public class Transaction {
    private MongoDatabase database;
    private String dbName="bankAgency";
    private String hostName="localhost";
    private int port=27017;
    private String userName="urh";
    private String passWord="passUrh";
    private String TransactionCollectionName="transactions";


    public static void main( String[] args ) {
        try{
            Transaction trans = new Transaction();
            // Creation : test des fonctions de gestion d'une collection et d'ajout de documents
            System.out.println("\n\nCreation : ...");
            //trans.dropCollectionTransaction(trans.TransactionCollectionName);
            //trans.createCollectionTransaction(trans.TransactionCollectionName);
            //trans.deleteTransactions(trans.TransactionCollectionName, new Document());
            //trans.testInsertOneTransaction();
            //trans.testInsertManyTransactions();
            //trans.getTransactionById(trans.TransactionCollectionName, 10);

            // affichage : Afficher tous les Transactions sans tri ni projection

            System.out.println("\n\n affichage : tous les Transactions sans tri ni projection ");
            trans.getTransactions(trans.TransactionCollectionName,new Document(),new Document(),new Document());


            // affichage : Afficher tous les Transactions
            // Trie en ordre croissant sur amount


            System.out.println("\n\naffichage : Trie en ordre croissant sur amount");
            trans.getTransactions(trans.TransactionCollectionName,
                    new Document(),
                    new Document(),
                    new Document("amount" ,1)
            );
            // affichage : Afficher tous les Transactions
            // Trie en ordre croissant sur la date
            System.out.println("\n\naffichage  : Trie en ordre croissant sur la date");
            trans.getTransactions(trans.TransactionCollectionName,
                    new Document(),
                    new Document(),
                    new Document("dateTransaction" ,1)
            );

            // TD4 : Afficher tous les Transactions
            // Avec projections sur les champs _id et amount
            // Triés en order décroissant sur amount


            System.out.println("\n\naffichage : Triés en order décroissant sur amount Avec projections sur les champs _id et amount ");

            trans.getTransactions(trans.TransactionCollectionName,
                    new Document(),
                    new Document("_id",1).append("amount",1),
                    new Document("amount",-1)
            );

            // affichage  : Afficher tous les Transactions Dont le status est complet
            // projection: tous les champs
            // tri : pas de tri

            System.out.println("\n\naffichage : Afficher tous les Transactions Dont le status est complet ");

            trans.getTransactions(trans.TransactionCollectionName,
                    new Document("status","completed"),
                    new Document(),
                    new Document()
            );

            // affichage: Afficher tous les Transactions Dont le montant est >= à 500
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\naffichage : Afficher tous les Transactions Dont le montant est >= à 500");

             trans.getTransactions(trans.TransactionCollectionName,
             new Document(new Document("amount", new Document("$gte", 500))),
             new Document(),
             new Document()
            );

            // affichage: Afficher tous les Transactions Dont le montant est < à 500
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\naffichage : Afficher tous les Transactions Dont le montant est < à 500");

            trans.getTransactions(trans.TransactionCollectionName,
                    new Document(new Document("amount", new Document("$lte", 500))),
                    new Document(),
                    new Document()
            );


            // modifier : modifier une trnsaction

            System.out.println("\n\nmettre à jour une transaction (in progress ) ---> (completed) ");

            trans.updateTransactions(trans.TransactionCollectionName,
                    new Document("status","in progress"),
                    new Document("$set", new Document("status", "completed")),
                    new UpdateOptions()
            );



            // TD9 : Supprimer la Transaction Nr 1

            System.out.println("\n\nsupprimer une transaction ");
            // c'est OKAY
            //trans.deleteTransactions(trans.TransactionCollectionName,
            //new Document("_id",1)
            //);

            // TD10 : Supprimer tous les transaction (filters = new Document() a vide )
            System.out.println("\n\nSupprimer tous les transaction : ...");
            //trans.deleteTransactions(
            //trans.TransactionCollectionName,
            //new Document()
            //);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Transaction(){
        // FD1 : Creating a Mongo client

        MongoClient mongoClient = new MongoClient( hostName , port );

        // Creating Credentials
        // RH : Ressources Humaines
        MongoCredential credential;
        credential = MongoCredential.createCredential(userName, dbName,
                passWord.toCharArray());
        System.out.println("Connected to the database successfully");
        System.out.println("Credentials ::"+ credential);
        // Accessing the database
        database = mongoClient.getDatabase(dbName);
    }



    public void dropCollectionTransaction(String nomCollection){
        //Drop a collection
        MongoCollection<Document> colTransactions=null;
        System.out.println("\n\n\n*********** dans dropCollectionClient *****************");

        System.out.println("!!!! Collection Client : "+colTransactions);

        colTransactions=database.getCollection(nomCollection);
        System.out.println("!!!! Collection Client : "+colTransactions);
        // Visiblement jamais !!!
        if (colTransactions==null) {
            System.out.println("Collection inexistante");
        }
        else {
            colTransactions.drop();
            System.out.println("Collection colTransactions removed successfully !!!");

        }
    }

    public void createCollectionTransaction(String nomCollection){
        //Creating a collection
        database.createCollection(nomCollection);
        System.out.println("Collection Transaction created successfully");
    }

    public void createCollectionTransaction(String nomCollection, Document validator){
        database.createCollection(nomCollection,new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator)));
    }

    public void deleteTransactions(String nomCollection, Document filters){
        System.out.println("\n\n\n*********** dans deleteTransactions *****************");
        MongoCollection<Document> colTransactions=database.getCollection(nomCollection);
        DeleteResult dresult  = colTransactions.deleteMany(filters);
        System.out.println(dresult);
    }

    public void testInsertOneTransaction(){
        Document transaction = new Document("_id", 4)
                .append("dateTransaction", "05/10/2022")
                .append("status", "completed")
                .append("amount", 250.00)
                .append("destination_id_client", 3)
                .append("clients_id", 2);
        this.insertOneTransaction(this.TransactionCollectionName, transaction);
        System.out.println("Document inserted successfully");
    }

    public void insertOneTransaction(String nomCollection, Document transaction){
        //Drop a collection
        MongoCollection<Document> colTransactions=database.getCollection(nomCollection);
        colTransactions.insertOne(transaction);
        //System.out.println("Document inserted successfully");
    }

    public void insertManyTransactions(String nomCollection, List<Document> transactions){
        //Drop a collection
        MongoCollection<Document> coltransactions=database.getCollection(nomCollection);
        coltransactions.insertMany(transactions);
        System.out.println("Many Documents inserted successfully");
    }

    public void testInsertManyTransactions(){
        // Création des documents à insérer
        List<Document> transactions = Arrays.asList(
                new Document("_id", 1)
                        .append("dateTransaction", "02/01/2022")
                        .append("status", "in progress")
                        .append("amount", 100.00)
                        .append("destination_id_client", 2)
                        .append("clients_id", 1),
                new Document("_id", 2)
                        .append("dateTransaction", "05/12/2022")
                        .append("status", "completed")
                        .append("amount", 500.00)
                        .append("destination_id_client", 1)
                        .append("clients_id", 2),
                new Document("_id", 3)
                        .append("dateTransaction", "08/02/2022")
                        .append("status", "completed")
                        .append("amount", 1000.00)
                        .append("destination_id_client", 3)
                        .append("clients_id", 3)
        );


        this.insertManyTransactions(this.TransactionCollectionName, transactions);
    }

    public void getTransactionById(String nomCollection, Integer transactionId){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getTransactionById *****************");

        MongoCollection<Document> colTransactions=database.getCollection(nomCollection);

        //BasicDBObject whereQuery = new BasicDBObject();
        Document whereQuery = new Document();

        whereQuery.put("_id", transactionId );
        FindIterable<Document> listTransaction=colTransactions.find(whereQuery);

        // Getting the iterator
        Iterator it = listTransaction.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void getTransactions(String nomCollection,
                           Document whereQuery,
                           Document projectionFields,
                           Document sortFields){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getTransactions *****************");

        MongoCollection<Document> colTransactions=database.getCollection(nomCollection);

        FindIterable<Document> listTransaction=colTransactions.find(whereQuery).sort(sortFields).projection(projectionFields);

        // Getting the iterator
        Iterator it = listTransaction.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }


    public Map<Integer, Double> getTransactionsUpdate(String nomCollection,
                                                       Document whereQuery,
                                                       Document projectionFields,
                                                       Document sortFields){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getTransactions Update *****************");

        MongoCollection<Document> colTransactions=database.getCollection(nomCollection);

        FindIterable<Document> listTransaction=colTransactions.find(whereQuery).sort(sortFields).projection(projectionFields);

        // Récupérer les IDs des transactions modifiées

        Map<Integer, Double> modifiedData = new HashMap<>();

        // Getting the iterator
        Iterator<Document> iterator = listTransaction.iterator();
        while (iterator.hasNext()) {
            Document transaction = iterator.next();
            Integer destinationClientId = transaction.getInteger("destination_id_client");
            Double amount = transaction.getDouble("amount");
            modifiedData.put(destinationClientId, amount);
        }
        return modifiedData;
    }
    public void updateTransactions(String nomCollection,Document whereQuery,Document updateExpressions, UpdateOptions updateOptions){
        //Drop a collection
        System.out.println("\n\n\n*********** dans updateTransactions *****************");
        // Convertir l'itérable en liste de documents
        Transaction trans = new Transaction();
        //List<Integer> modifiedTransactionIds = new ArrayList<>();
        Map<Integer, Double> modifiedData = new HashMap<>();
        trans.getTransactions(nomCollection,whereQuery,new Document(),new Document());
        System.out.println("Avant appel  :");
        // Effectuer une requête pour récupérer les transactions  à modifiées
        modifiedData=trans.getTransactionsUpdate(nomCollection,whereQuery,new Document(),new Document());
        System.out.println("Apres appel  :");
        MongoCollection<Document> colTransactions=database.getCollection(nomCollection);
        MongoCollection<Document> accountCollection = database.getCollection("accounts");
        UpdateResult updateResult = colTransactions.updateMany(whereQuery, updateExpressions,updateOptions);
        // Vérifier si des documents ont été modifiés
        if (updateResult.getModifiedCount() > 0) {
            // Mettre à jour le solde du compte associé pour chaque transaction mise à jour
            for (Map.Entry<Integer, Double> entry : modifiedData.entrySet()) {
                Integer destinationClientId = entry.getKey();
                Double amount = entry.getValue();
                Document accountFilter = new Document("clients_id", destinationClientId);
                Document accountUpdate = new Document("$inc", new Document("balance",amount));

                // Effectuer la mise à jour du solde du compte
                UpdateResult accountUpdateResult = accountCollection.updateOne(accountFilter, accountUpdate);
                System.out.println("Infos mise à jour du compte : " + accountUpdateResult);
            }
            System.out.println("Les transactions ont été mises à jour avec succès. Les soldes des comptes associés ont été mis à jour.");

        }
        else{
            System.out.println("Aucune transaction mise à jour.");
        }
    }






}

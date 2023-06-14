package fr.unice.master1;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Compte {

    private MongoDatabase database;
    private String dbName="testBase";
    private String hostName="localhost";
    private int port=27017;
    private String userName="urh";
    private String passWord="passUrh";
    private String CompteCollectionName="accounts";


    public static void main( String args[] ) {
        try{
            Compte compte = new Compte();
            // Creation  : test des fonctions de gestion d'une collection et d'ajout de documents
            System.out.println("\n\nCreation  : ...");
            //compte.dropCollectionCompte(compte.CompteCollectionName);
            //compte.createCollectionCompte(compte.CompteCollectionName);
            //compte.deleteComptes(compte.CompteCollectionName, new Document());
            //compte.testInsertOneCompte();
            //compte.testInsertManyComptes();
            //compte.getCompteById(compte.CompteCollectionName, 10);

            // Affichage : Afficher tous les comptes sans tri ni projection
            System.out.println("\n\nAfficher tous les comptes sans tri ni projection");
            compte.getComptes(compte.CompteCollectionName,
                    new Document(),
                    new Document(),
                    new Document()
            );


            // Affichage : Afficher tous les Comptes
            // where
            // Projeter sur _id, balance et created_at
            // Trie en ordre croissant sur le balance sur le created_at
            System.out.println("\n\nAfficher tous les Comptes Trie en ordre croissant sur le balance sur le created_at");
            compte.getComptes(compte.CompteCollectionName,
                    new Document(),
                    new Document("_id",1).append("balance" ,1).append("created_at",1),
                    new Document("balance" ,1).append("created_at", 1)
            );

            // Affichage : Afficher tous les Comptes
            // Avec projections sur les champs _id et balance
            // Triés en order décroissant sur  balance
            System.out.println("\n\nAfficher tous les Comptes Trie en ordre décroissant sur le balance ");
            compte.getComptes(compte.CompteCollectionName,
                    new Document(),
                    new Document("_id",1).append("balance",1),
                    new Document("balance",-1)
            );

            // Affichage : Afficher  le Comptes
            // Dont l'id  est  "1"
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\nAfficher le Comptes Dont l'id  est  \"1\"");
            compte.getComptes(compte.CompteCollectionName,
                    new Document("_id",1),
                    new Document(),
                    new Document()
            );

            // affichage: Afficher tous les Comptes Dont le balance est >= à 500
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\naffichage : Afficher tous les Comptes Dont le balance est >= à 2000");
            compte.getComptes(compte.CompteCollectionName,
                    new Document(new Document("balance", new Document("$gte", 2000))),
                    new Document(),
                    new Document()
            );

            // affichage: Afficher tous les Comptes Dont le balance est < à 2000
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\naffichage : Afficher tous les Comptes Dont le balance est < à 2000");
            compte.getComptes(compte.CompteCollectionName,
                    new Document(new Document("balance", new Document("$lte", 2000))),
                    new Document(),
                    new Document()
            );


            // Modifier : modifier le compte nr (_id = 2) pour faire un dépôt de 1000 .
            System.out.println("\n\nmodifier le compte nr (_id = 2) pour faire un dépôt de 1000 ");
            compte.updateComptes(compte.CompteCollectionName,
                    new Document("_id",2),
                    new Document("$inc", new Document("balance",1000)),
                    new UpdateOptions()
            );


            // TD9 : Supprimer le compte Nr 4

            System.out.println("\n\nSupprimer le compte Nr 4");
            //compte.deleteComptes(compte.CompteCollectionName,
              //      new Document("_id",10)
            //);

            // TD10 : Supprimer tous les comptes (filters = new Document() a vide )
            System.out.println("\n\nSupprimer tous les comptes ");
            //compte.deleteComptes(compte.CompteCollectionName,
              //      new Document()
            //);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    Compte(){
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



    public void dropCollectionCompte(String nomCollection){
        //Drop a collection
        MongoCollection<Document> colComptes=null;
        System.out.println("\n\n\n*********** dans dropCollectionCompte *****************");

        System.out.println("!!!! Collection Compte : "+colComptes);

        colComptes=database.getCollection(nomCollection);
        System.out.println("!!!! Collection Compte : "+colComptes);
        // Visiblement jamais !!!
        if (colComptes==null) {
            System.out.println("Collection inexistante");
        }
        else {
            colComptes.drop();
            System.out.println("Collection colCompte removed successfully !!!");

        }
    }

    public void createCollectionCompte(String nomCollection){
        //Creating a collection
        database.createCollection(nomCollection);
        System.out.println("Collection Compte created successfully");
    }

    public void deleteComptes(String nomCollection, Document filters){
        System.out.println("\n\n\n*********** dans deleteComptes *****************");
        MongoCollection<Document> colComptes=database.getCollection(nomCollection);
        DeleteResult dresult  = colComptes.deleteMany(filters);
        System.out.println(dresult);
    }

    public void testInsertOneCompte(){
        Document compte = new Document("_id", 1)
                .append("balance", 2000.0)
                .append("created_at", "02/01/2022")
                .append("closed_at", "null")
                .append("type", "current account")
                .append("clients_id", 3)
                .append("agencies_id", 2);

        this.insertOneCompte(this.CompteCollectionName, compte);
        System.out.println("Document inserted successfully");
    }

    public void insertOneCompte(String nomCollection, Document compte){
        //Drop a collection
        MongoCollection<Document> colComptes=database.getCollection(nomCollection);
        colComptes.insertOne(compte);
        //System.out.println("Document inserted successfully");
    }

    public void insertManyComptes(String nomCollection, List<Document> comptes){
        //Drop a collection
        MongoCollection<Document> colComptes=database.getCollection(nomCollection);
        colComptes.insertMany(comptes);
        System.out.println("Many Documents inserted successfully");
    }

    public void testInsertManyComptes(){
        List<Document> comptes = Arrays.asList(
                new Document("_id", 2)
                        .append("balance", 1000.0)
                        .append("created_at", "02/01/2022")
                        .append("closed_at", "null")
                        .append("type", "current account")
                        .append("clients_id", 1)
                        .append("agencies_id", 1),
                new Document("_id", 3)
                        .append("balance", 5000.0)
                        .append("created_at", "05/12/2022")
                        .append("closed_at", "null")
                        .append("type", "savings account")
                        .append("clients_id", 2)
                        .append("agencies_id", 1),
                new Document("_id", 4)
                        .append("balance", 2000.0)
                        .append("created_at", "08/02/2022")
                        .append("closed_at", "null")
                        .append("type", "savings account")
                        .append("clients_id", 3)
                        .append("agencies_id", 2)
        );


        this.insertManyComptes(this.CompteCollectionName, comptes);
    }

    public void getCompteById(String nomCollection, Integer compteId){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getCompteById *****************");

        MongoCollection<Document> colComptes=database.getCollection(nomCollection);

        //BasicDBObject whereQuery = new BasicDBObject();
        Document whereQuery = new Document();

        whereQuery.put("_id", compteId );
        FindIterable<Document> listCompte=colComptes.find(whereQuery);

        // Getting the iterator
        Iterator it = listCompte.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void getComptes(String nomCollection,
                           Document whereQuery,
                           Document projectionFields,
                           Document sortFields){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getAgences *****************");

        MongoCollection<Document> colComptes=database.getCollection(nomCollection);

        FindIterable<Document> listComptes=colComptes.find(whereQuery).sort(sortFields).projection(projectionFields);

        // Getting the iterator
        Iterator it = listComptes.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }


    public void updateComptes(String nomCollection,Document whereQuery,Document updateExpressions, UpdateOptions updateOptions){
        //Drop a collection
        System.out.println("\n\n\n*********** dans updateComptes *****************");
        MongoCollection<Document> colComptes=database.getCollection(nomCollection);
        UpdateResult updateResult = colComptes.updateMany(whereQuery, updateExpressions,updateOptions);
        System.out.println("Info Maj : "+ updateResult);
    }



}

package fr.unice.master1.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mohamed ELYSALEM
 * @author Eliel WOTOBE
 */
public class Agence {
    private MongoDatabase database;
    private String dbName="bankAgency";
    private String hostName="localhost";
    private int port=27017;
    private String userName="urh";
    private String passWord="passUrh";
    private String AgenceCollectionName="agencies";


    public static void main( String[] args ) {
        try{
            Agence agence = new Agence();
            // Creation : test des fonctions de gestion d'une collection et d'ajout de documents
            System.out.println("\n\nCreation : ...");
            //agence.dropCollectionAgence(agence.AgenceCollectionName);
            //agence.createCollectionAgence(agence.AgenceCollectionName);
            //agence.deleteAgence(agence.AgenceCollectionName, new Document());
            //agence.testInsertOneAgence();
            //agence.testInsertManyAgences();
            //agence.getAgenceById(agence.AgenceCollectionName, 10);

            // Affichage : Afficher tous les agences sans tri ni projection
            System.out.println("\n\nAfficher tous les agences sans tri ni projection");
            agence.getAgences(agence.AgenceCollectionName,
                    new Document(),
                    new Document(),
                    new Document()
            );

            // Affichage : Afficher tous les Agences
            // where
            // Projeter sur _id, name et accounts
            // Trie en ordre croissant sur le name
            System.out.println("\n\nAfficher tous les Agences Trie en ordre croissant sur le name");
            agence.getAgences(agence.AgenceCollectionName,
                    new Document(),
                    new Document("_id",1).append("name" ,1).append("accounts",1),
                    new Document("balance" ,1).append("created_at", 1)
            );

            // Affichage : Afficher tous les Agence
            // projection: tous les champs
            // Triés en order décroissant sur _id et balance
            System.out.println("\n\nAfficher tous les Agences Trie en ordre décroissant sur le name");
            agence.getAgences(agence.AgenceCollectionName,
                    new Document(),
                    new Document(),
                    new Document("name",-1)
            );

            // Affichage : Afficher tous les Agence
            // Dont l'id  est  "1"
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\nAfficher l'Agence Dont l'id  est  \"1\" ");
            agence.getAgences(agence.AgenceCollectionName,
                    new Document("_id",1),
                    new Document(),
                    new Document()
            );

            // Affichage : Afficher l'agence Dont le town est Fréjus
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\nAfficher l'Agence Dont le town est Fréjus");
            agence.getAgences(agence.AgenceCollectionName,
                    new Document("address.town","Fréjus"),
                    new Document(),
                    new Document()
            );

            // Affichage : Afficher tous les  agences
            // projection: tous les champs
            // tri :  l'ordre croissant en fonction du nombre d'éléments dans le tableau "accounts" c'est dire nombre des clients
            System.out.println("\n\nAfficher dans l'ordre croissant les Agences en fonction du nombre des clients ");
            agence.getAgencesOrderAccounts(agence.AgenceCollectionName,
                    new Document(),
                    new Document(),
                    new Document("accounts",1)
            );

            // Modifier : modifier l'agence nr (_id = 2). Mettre son adresse  (town à "Nice")
            System.out.println("\n\nmodifier l'agence nr (_id = 2). Mettre son adresse (town à \"Nice\") ");
            agence.updateAgences(agence.AgenceCollectionName,
                    new Document("_id",2),
                    new Document("$set", new Document("address.town", "Nice")),
                    new UpdateOptions()
            );

            // Supprimer : Supprimer l'agence Nr 4
            System.out.println("\n\nSupprimer l'agence Nr 4");
            //agence.deleteAgences(agence.AgenceCollectionName,
              //      new Document("_id",4)
            //);

            // Supprimer : Supprimer tous les agences (filters = new Document() a vide )
            System.out.println("\n\nSupprimer tous les agences");
            //agence.deleteAgences(agence.AgenceCollectionName,
              //      new Document()
            //);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Agence(){
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



    public void dropCollectionAgence(String nomCollection){
        //Drop a collection
        MongoCollection<Document> colAgences=null;
        System.out.println("\n\n\n*********** dans dropCollectionAgence *****************");

        System.out.println("!!!! Collection Agence : "+colAgences);

        colAgences=database.getCollection(nomCollection);
        System.out.println("!!!! Collection Agence : "+colAgences);
        // Visiblement jamais !!!
        if (colAgences==null) {
            System.out.println("Collection inexistante");
        }
        else {
            colAgences.drop();
            System.out.println("Collection colAgences removed successfully !!!");

        }
    }

    public void createCollectionAgence(String nomCollection){
        //Creating a collection
        database.createCollection(nomCollection);
        System.out.println("Collection Agence created successfully");
    }

    public void createCollectionAgence(String nomCollection, Document validator){
        database.createCollection(nomCollection,new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator)));
    }

    public void deleteAgences(String nomCollection, Document filters){
        System.out.println("\n\n\n*********** dans deleteAgences *****************");
        MongoCollection<Document> colAgences=database.getCollection(nomCollection);
        DeleteResult dresult  = colAgences.deleteMany(filters);
        System.out.println(dresult);
    }

    public void testInsertOneAgence(){
        // Création du document de l'agence
        Document address = new Document()
                .append("numberStreet", 22)
                .append("street", "Main Street")
                .append("town", "BMD")
                .append("postalCode", "99999")
                .append("country", "aravat");

        Document agence = new Document("_id", 4)
                .append("name", "Agency D")
                .append("address", address)
                .append("managers_id", 4)
                .append("accounts", Arrays.asList(6, 7, 8));
        this.insertOneAgence(this.AgenceCollectionName, agence);
        System.out.println("Document inserted successfully");
    }

    public void insertOneAgence(String nomCollection, Document agence){
        //Drop a collection
        MongoCollection<Document> colAgences=database.getCollection(nomCollection);
        colAgences.insertOne(agence);   
        //System.out.println("Document inserted successfully");
    }

    public void insertManyAgences(String nomCollection, List<Document> agences){
        //Drop a collection
        MongoCollection<Document> colAgences=database.getCollection(nomCollection);
        colAgences.insertMany(agences);
        System.out.println("Many Documents inserted successfully");
    }

    public void testInsertManyAgences(){
        List<Document> agences = Arrays.asList(
                new Document("_id", 10)
                        .append("dname", "ACCOUNTING")
                        .append("loc", "New York"),
                new Document("_id", 20)
                        .append("dname", "RESEARCH")
                        .append("loc", "Dallas"),
                new Document("_id", 30)
                        .append("dname", "SALES")
                        .append("loc", "Chicago"),
                new Document("_id", 40)
                        .append("dname", "OPERATIONS")
                        .append("loc", "Boston")
        );

        this.insertManyAgences(this.AgenceCollectionName, agences);
    }

    public void getAgenceById(String nomCollection, Integer agenceId){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getAgenceById *****************");

        MongoCollection<Document> colAgences=database.getCollection(nomCollection);

        //BasicDBObject whereQuery = new BasicDBObject();
        Document whereQuery = new Document();

        whereQuery.put("_id", agenceId );
        FindIterable<Document> listAgence=colAgences.find(whereQuery);

        // Getting the iterator
        Iterator it = listAgence.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void getAgences(String nomCollection,
                           Document whereQuery,
                           Document projectionFields,
                           Document sortFields){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getAgences *****************");

        MongoCollection<Document> colAgences=database.getCollection(nomCollection);

        FindIterable<Document> listDept=colAgences.find(whereQuery).sort(sortFields).projection(projectionFields);

        // Getting the iterator
        Iterator it = listDept.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void getAgencesOrderAccounts(String nomCollection,
                           Document whereQuery,
                           Document projectionFields,
                           Document sortFields) {
        System.out.println("\n\n\n*********** dans getAgencesOrderAccounts *****************");

        MongoCollection<Document> colAgences = database.getCollection(nomCollection);

        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(whereQuery),
                Aggregates.project(
                        Projections.fields(
                                Projections.include("_id", "name", "address", "accounts"),
                                Projections.computed("numAccounts", new Document("$size", "$accounts"))
                        )
                ),
                Aggregates.sort(new Document("numAccounts",1)),
                Aggregates.project(
                        Projections.exclude("numAccounts")
                )
        );

        AggregateIterable<Document> result = colAgences.aggregate(pipeline);

        for (Document document : result) {
            System.out.println(document);
        }
    }



    public void updateAgences(String nomCollection,Document whereQuery,Document updateExpressions, UpdateOptions updateOptions){
        //Drop a collection
        System.out.println("\n\n\n*********** dans updateAgences *****************");
        MongoCollection<Document> colAgences=database.getCollection(nomCollection);
        UpdateResult updateResult = colAgences.updateMany(whereQuery, updateExpressions,updateOptions);
        System.out.println("Info Maj : "+ updateResult);
    }



}


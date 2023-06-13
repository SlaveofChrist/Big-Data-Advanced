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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * @author Mohamed ELYSALEM
 * @author Eliel WOTOBE
 */
public class Gestionnaire {

    private MongoDatabase database;
    private String dbName="bankAgency";
    private String hostName="localhost";
    private int port=27017;
    private String userName="urh";
    private String passWord="passUrh";
    private String GestionnaireCollectionName="managers";


    public static void main( String[] args ) {
        try{
            Gestionnaire gestionnaire = new Gestionnaire();
            // Creation : test des fonctions de gestion d'une collection et d'ajout de documents
            System.out.println("\n\nCreation : ...");
            //gestionnaire.dropCollectionGestionnaire(gestionnaire.GestionnaireCollectionName);
            //gestionnaire.createCollectionGestionnaire(gestionnaire.GestionnaireCollectionName);
            //gestionnaire.deleteGestionnaire(gestionnaire.GestionnaireCollectionName, new Document());
            //gestionnaire.testInsertOneGestionnaire();
            //gestionnaire.testInsertManyGestionnaires();
            //gestionnaire.getGestionnaireById(gestionnaire.GestionnaireCollectionName, 10);

            // affichage : afficher tous les gestionnaires sans tri ni projection
            System.out.println("\n\nafficher tous les gestionnaires sans tri ni projection");
            gestionnaire.getGestionnaires(gestionnaire.GestionnaireCollectionName,new Document(),new Document(),new Document());


            // affichage : Afficher tous les gestionnaires
            // where
            // Projeter sur agencies_id, name
            // Trie en ordre croissant sur le name
            System.out.println("\n\nTD3 : ...");
            gestionnaire.getGestionnaires(gestionnaire.GestionnaireCollectionName,
                    new Document(),
                    new Document("agencies_id",1).append("name" ,1),
                    new Document("name", 1)
            );



            // affichage : Afficher  le Gestionnaire dont
            //  l'id  est  "1"
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\nAfficher  le Gestionnaire dont l'id  est  \"1\" ");
            gestionnaire.getGestionnaires(gestionnaire.GestionnaireCollectionName,
                    new Document("_id",1),
                    new Document(),
                    new Document()
            );

            // affichage : Afficher le Gestionnaire dont le nom est "Robert Brown"
            // projection sur name,agencies_id
            // tri : pas de tri
            System.out.println("\n\nAfficher le Gestionnaire dont le nom est \"Robert Brown\"");
             gestionnaire.getGestionnaires(gestionnaire.GestionnaireCollectionName,
               new Document("name", "Robert Brown"),
               new Document("name",1).append("agencies_id",1),
               new Document()
             );


            // modifier : modifier le gestionnaire nr (_id = 2).
            System.out.println("\n\nmodifier : modifier le gestionnaire nr (_id = 2) ");
            gestionnaire.updateGestionnaires(gestionnaire.GestionnaireCollectionName,
                new Document("_id",2),
                new Document("$set", new Document("agencies_id", 4)),
                new UpdateOptions()
            );

            // Supprimer : Supprimer le gestionnaire Nr 4

            System.out.println("\n\nSupprimer le gestionnaire Nr 4");
            gestionnaire.deleteGestionnaires(gestionnaire.GestionnaireCollectionName,
                    new Document("_id",4)
            );

            // Supprimer : Supprimer tous les gestionnaires (filters = new Document() a vide )
            System.out.println("\n\nSupprimer tous les gestionnaires ");
            gestionnaire.deleteGestionnaires(gestionnaire.GestionnaireCollectionName,
                    new Document()
            );

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Gestionnaire(){
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



    public void dropCollectionGestionnaire(String nomCollection){
        //Drop a collection
        MongoCollection<Document> colGestionnaires=null;
        System.out.println("\n\n\n*********** dans dropCollectionGestionnaire *****************");

        System.out.println("!!!! Collection Gestionnaire : "+colGestionnaires);

        colGestionnaires=database.getCollection(nomCollection);
        System.out.println("!!!! Collection Gestionnaire : "+colGestionnaires);
        // Visiblement jamais !!!
        if (colGestionnaires==null) {
            System.out.println("Collection inexistante");
        }
        else {
            colGestionnaires.drop();
            System.out.println("Collection colGestionnaire removed successfully !!!");

        }
    }

    public void createCollectionGestionnaire(String nomCollection){
        //Creating a collection
        database.createCollection(nomCollection);
        System.out.println("Collection Gestionnaire created successfully");
    }

    public void createCollectionGestionnaire(String nomCollection, Document validator){
        database.createCollection(nomCollection,new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator)));
    }

    public void deleteGestionnaires(String nomCollection, Document filters){
        System.out.println("\n\n\n*********** dans deleteGestionnaires *****************");
        MongoCollection<Document> colGestionnaires=database.getCollection(nomCollection);
        DeleteResult dresult  = colGestionnaires.deleteMany(filters);
        System.out.println(dresult);
    }

    public void testInsertOneGestionnaire(){
        Document gestionnaire = new Document("_id", 1)
                .append("name", "John Smith")
                .append("agencies_id", 4);
        this.insertOneGestionnaire(this.GestionnaireCollectionName, gestionnaire);
        System.out.println("Document inserted successfully");
    }

    public void insertOneGestionnaire(String nomCollection, Document gestionnaire){
        //Drop a collection
        MongoCollection<Document> colGestionnaires=database.getCollection(nomCollection);
        colGestionnaires.insertOne(gestionnaire);
        //System.out.println("Document inserted successfully");
    }

    public void insertManyGestionnaires(String nomCollection, List<Document> gestionnaires){
        //Drop a collection
        MongoCollection<Document> colGestionnaires=database.getCollection(nomCollection);
        colGestionnaires.insertMany(gestionnaires);
        System.out.println("Many Documents inserted successfully");
    }

    public void testInsertManyGestionnaires(){
        List<Document> gestionnaires = Arrays.asList(
                new Document("_id", 2)
                        .append("name", "Alice Johnson")
                        .append("agencies_id", 2),
                new Document("_id", 3)
                        .append("name", "Robert Brown")
                        .append("agencies_id", 1),
                new Document("_id", 4)
                        .append("name", "Emily Davis")
                        .append("agencies_id", 3)
        );


        this.insertManyGestionnaires(this.GestionnaireCollectionName, gestionnaires);
    }

    public void getGestionnaireById(String nomCollection, Integer gestionnaireId){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getGestionnaireById *****************");

        MongoCollection<Document> colGestionnaires=database.getCollection(nomCollection);

        //BasicDBObject whereQuery = new BasicDBObject();
        Document whereQuery = new Document();

        whereQuery.put("_id", gestionnaireId );
        FindIterable<Document> listCompte=colGestionnaires.find(whereQuery);

        // Getting the iterator
        Iterator it = listCompte.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void getGestionnaires(String nomCollection,
                           Document whereQuery,
                           Document projectionFields,
                           Document sortFields){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getGestionnaires *****************");

        MongoCollection<Document> colGestionnaires=database.getCollection(nomCollection);

        FindIterable<Document> listDept=colGestionnaires.find(whereQuery).sort(sortFields).projection(projectionFields);

        // Getting the iterator
        Iterator it = listDept.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }


    public void updateGestionnaires(String nomCollection,Document whereQuery,Document updateExpressions, UpdateOptions updateOptions){
        //Drop a collection
        System.out.println("\n\n\n*********** dans updateGestionnaires *****************");
        MongoCollection<Document> colGestionnaires=database.getCollection(nomCollection);
        UpdateResult updateResult = colGestionnaires.updateMany(whereQuery, updateExpressions,updateOptions);
        System.out.println("Info Maj : "+ updateResult);
    }

}

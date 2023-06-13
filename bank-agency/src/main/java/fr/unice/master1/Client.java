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
/**
 * @author Mohamed ELYSALEM
 * @author Eliel WOTOBE
 */
public class Client {

    private MongoDatabase database;
    private String dbName="bankAgency";
    private String hostName="localhost";
    private int port=27017;
    private String userName="urh";
    private String passWord="passUrh";
    private String ClientCollectionName="clients";



    public static void main( String[] args ) {
        try{
            Client cli = new Client();
            // Creation : test des fonctions de gestion d'une collection et d'ajout de documents
            System.out.println("\n\nCreation : ...");
            //cli.dropCollectionClient(cli. ClientCollectionName);
            //cli.createCollectionClient(cli.ClientCollectionName);
            //cli.deleteClients(cli.ClientCollectionName, new Document());
            //cli.testInsertOneClient();
            //dept.testInsertManyDepts();
            //dept.getDeptById(dept.DeptCollectionName, 10);

            // Affichage : Afficher tous les Clients sans tri ni projection
            System.out.println("\n\nAfficher tous les Clients sans tri ni projection");
            cli.getClients(cli.ClientCollectionName,
                    new Document(),
                    new Document(),
                    new Document()
            );

            // Affichage : Afficher tous les Clients
            // where
            // Projeter sur _id, first_name et last_name
            // Trie en ordre croissant sur le first_name sur le last_name
            System.out.println("\n\nAfficher tous les Clients Trie en ordre croissant sur le first_name sur le last_name ");
             cli.getClients(cli.ClientCollectionName,
                     new Document(),
                     new Document("_id",1).append("first_name" ,1).append("last_name",1),
                     new Document("first_name" ,1).append("last_name", 1)
            );

            // Affichage : Afficher tous les Clients
            // Avec projections sur les champs _id et first_name
            // Triés en order décroissant sur _id
            System.out.println("\n\nAfficher tous les Clients Triés en order décroissant sur _id ");
            cli.getClients(cli.ClientCollectionName,
                    new Document(),
                    new Document("_id",1).append("first_name",1),
                    new Document("_id",-1)
            );

            // Affichage : Afficher le Client
            // Dont le first_name est  "Land"
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\nAfficher le Client Dont le first_name est  \"Land\" ");
            cli.getClients(cli.ClientCollectionName,
                    new Document("first_name","Land"),
                    new Document(),
                    new Document()
            );

            // Affichage: Afficher le Client Dont le town est City
            // projection: tous les champs
            // tri : pas de tri
            System.out.println("\n\nAfficher le Client Dont le town est City");
            cli.getClients(cli.ClientCollectionName,
                    new Document("address.town", "City"),
                    new Document(),
                    new Document()
             );

            // Modifier : modifier le client nr (_id = 2). Mettre son adresse  (town à "Marseille")
            System.out.println("\n\nmodifier le client nr (_id = 2). Mettre son adresse (town à \"Marseille\") ");
            cli.updateClients(cli.ClientCollectionName,
                    new Document("_id",2),
                    new Document("$set", new Document("address.town", "Marseille")),
                    new UpdateOptions()
             );


            // Supprimer : Supprimer le Client Nr 4
            System.out.println("\n\nSupprimer le Client Nr 4");
            //cli.deleteClients(cli.ClientCollectionName,
                    //new Document("_id",4)
            //);

            // Supprimer : Supprimer tous les clients (filters = new Document() a vide )
            System.out.println("\n\nSupprimer tous les clients");
            //cli.deleteClients(cli.ClientCollectionName,
              //      new Document()
            //);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public Client(){
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

    public void dropCollectionClient(String nomCollection){
        //Drop a collection
        MongoCollection<Document> colClients=null;
        System.out.println("\n\n\n*********** dans dropCollectionClient *****************");

        System.out.println("!!!! Collection Client : "+colClients);

        colClients=database.getCollection(nomCollection);
        System.out.println("!!!! Collection Client : "+colClients);
        // Visiblement jamais !!!
        if (colClients==null) {
            System.out.println("Collection inexistante");
        }
        else {
            colClients.drop();
            System.out.println("Collection colClients removed successfully !!!");

        }
    }

    public void createCollectionClient(String nomCollection){
        //Creating a collection
        database.createCollection(nomCollection);
        System.out.println("Collection Client created successfully");
    }

    public void createCollectionClient(String nomCollection, Document validator){
        database.createCollection(nomCollection,new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(validator)));
    }

    public void deleteClients(String nomCollection, Document filters){
        System.out.println("\n\n\n*********** dans deleteClients *****************");
        MongoCollection<Document> colClients=database.getCollection(nomCollection);
        DeleteResult dresult  = colClients.deleteMany(filters);
        System.out.println(dresult);
    }

    public void testInsertOneClient(){
        // Création du document du client
        Document address = new Document()
                .append("numberStreet", "133")
                .append("street", "Main Street")
                .append("town", "City")
                .append("postalCode", "10305")
                .append("country", "nktt");

        Document client = new Document("_id", 4)
                .append("lastName", "ELY")
                .append("firstName", "Salem")
                .append("phone", "06452786542")
                .append("address", address)
                .append("accounts", Arrays.asList(6))
                .append("transactions", Arrays.asList(7, 8));;
        this.insertOneClient(this.ClientCollectionName, client);
        System.out.println("Document inserted successfully");
    }

    public void insertOneClient(String nomCollection, Document client){
        //Drop a collection
        MongoCollection<Document> colClients=database.getCollection(nomCollection);
        colClients.insertOne(client);
        //System.out.println("Document inserted successfully");
    }


    public void getClients(String nomCollection,
                         Document whereQuery,
                         Document projectionFields,
                         Document sortFields){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getClients *****************");

        MongoCollection<Document> colClients=database.getCollection(nomCollection);

        FindIterable<Document> listDept=colClients.find(whereQuery).sort(sortFields).projection(projectionFields);

        // Getting the iterator
        Iterator it = listDept.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }


    public void updateClients(String nomCollection,Document whereQuery,Document updateExpressions, UpdateOptions updateOptions){
        //Drop a collection
        System.out.println("\n\n\n*********** dans updateDepts *****************");
        MongoCollection<Document> colClients=database.getCollection(nomCollection);
        UpdateResult updateResult = colClients.updateMany(whereQuery, updateExpressions,updateOptions);
        System.out.println("Info Maj : "+ updateResult);
    }






}

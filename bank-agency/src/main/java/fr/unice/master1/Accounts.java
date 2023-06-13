package fr.unice.master1;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Accounts {
    private MongoDatabase database;
    private String dbName="bankAgency";
    private String hostName="localhost";
    private int port=27017; //port par defaut de mongodb
    private String userName="urh";
    private String passWord="passUrh";
    private String DeptCollectionName="accounts";

    Accounts(){

        MongoClient mongoClient = new MongoClient( hostName , port );
        MongoCredential credential;
        credential = MongoCredential.createCredential(userName, dbName,
                passWord.toCharArray());
        System.out.println("Connected to the database successfully");
        System.out.println("Credentials ::"+ credential);
        database = mongoClient.getDatabase(dbName);



    }

    /**
     Cette fonction permet de créer une collection
     de nom nomCollection.
     */
    public void createCollectionDept(String nomCollection){
        database.createCollection(nomCollection);
        System.out.println("Collection Accounts created successfully");

    }

    /**
     Cette fonction permet de supprimer une collection
     de nom nomCollection.
     */
    public void dropCollectionDept(String nomCollection){


        MongoCollection<Document> colAccounts=null;
        System.out.println("\n\n\n*********** dans dropCollectionDept *****************");

        System.out.println("!!!! Collection Dept : "+colAccounts);

        colAccounts=database.getCollection(nomCollection);
        System.out.println("!!!! Collection Dept : "+colAccounts);


        if (colAccounts==null)
            System.out.println("Collection inexistante");
        else {
            colAccounts.drop();
            System.out.println("Collection colAccounts removed successfully !!!");

        }
    }

    /**
     Cette fonction permet d'insérer un compte dans la collection appropriée.
     */

    public void insertOneDept(String nomCollection, Document account){
        //Drop a collection
        MongoCollection<Document> colAccounts=database.getCollection(nomCollection);
        colAccounts.insertOne(account);
        System.out.println("Document inserted successfully");
    }


    /**
     FD5 : Cette fonction permet de tester la m�thode insertOneDept.
     */

    public void testInsertOneDept(){
        Document dept = new Document("_id", 50)
                .append("dname", "FORMATION")
                .append("loc", "Nice");
        this.insertOneDept(this.DeptCollectionName, dept);
        System.out.println("Document inserted successfully");
    }

    /**
     FD6 : Cette fonction permet d'ins�rer plusieurs D�partements dans une collection
     */

    public void insertManyDepts(String nomCollection, List<Document> depts){
        //Drop a collection
        MongoCollection<Document> colAccounts=database.getCollection(nomCollection);
        colAccounts.insertMany(depts);
        System.out.println("Many Documents inserted successfully");
    }

    /**
     FD7 : Cette fonction permet de tester la fonction insertManyDepts
     */

    public void testInsertManyDepts(){
        List<Document> depts = Arrays.asList(
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

        this.insertManyDepts(this.DeptCollectionName, depts);
    }

    /**
     FD8 : Cette fonction permet de rechercher un d�partement dans une collection
     connaissant son id.
     */
    public void getDeptById(String nomCollection, Integer deptId){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getDeptById *****************");

        MongoCollection<Document> colAccounts=database.getCollection(nomCollection);

        //BasicDBObject whereQuery = new BasicDBObject();
        Document whereQuery = new Document();

        whereQuery.put("_id", deptId );
        //DBCursor cursor = colAccounts.find(whereQuery);
        FindIterable<Document> listDept=colAccounts.find(whereQuery);

        // Getting the iterator
        Iterator it = listDept.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }


    /**
     FD9 : Cette fonction permet de rechercher des d�partements dans une collection.
     Le param�tre whereQuery : permet de passer des conditions de rechercher
     Le param�tre projectionFields : permet d'indiquer les champs � afficher
     Le param�tre sortFields : permet d'indiquer les champs de tri.
     */
    public void getDepts(String nomCollection,
                         Document whereQuery,
                         Document projectionFields,
                         Document sortFields){
        //Drop a collection
        System.out.println("\n\n\n*********** dans getDepts *****************");

        MongoCollection<Document> colAccounts=database.getCollection(nomCollection);

        FindIterable<Document> listDept=colAccounts.find(whereQuery).sort(sortFields).projection(projectionFields);

        // Getting the iterator
        Iterator it = listDept.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }


    /**
     FD10 : Cette fonction permet de modifier des d�partements dans une collection.
     Le param�tre whereQuery : permet de passer des conditions de recherche
     Le param�tre updateExpressions : permet d'indiquer les champs � modifier
     Le param�tre UpdateOptions : permet d'indiquer les options de mise � jour :
     .upSert : ins�re si le document n'existe pas
     */
    public void updateDepts(String nomCollection,
                            Document whereQuery,
                            Document updateExpressions,
                            UpdateOptions updateOptions
    ){
        //Drop a collection
        System.out.println("\n\n\n*********** dans updateDepts *****************");

        MongoCollection<Document> colAccounts=database.getCollection(nomCollection);
        UpdateResult updateResult = colAccounts.updateMany(whereQuery, updateExpressions);

        System.out.println("\nR�sultat update : "
                +"getUpdate id: "+updateResult
                +" getMatchedCount : "+updateResult.getMatchedCount()
                +" getModifiedCount : "+updateResult.getModifiedCount()
        );

    }


    /**
     FD11 : Cette fonction permet de supprimer des d�partements dans une collection.
     Le param�tre filters : permet de passer des conditions de recherche des employ�s � supprimer
     */
    public void deleteDepts(String nomCollection, Document filters){

        System.out.println("\n\n\n*********** dans deleteDepts *****************");
        FindIterable<Document> listDept;
        Iterator it;
        MongoCollection<Document> colAccounts=database.getCollection(nomCollection);

        listDept=colAccounts.find(filters).sort(new Document("_id", 1));
        it = listDept.iterator();// Getting the iterator
        this.displayIterator(it, "Dans deleteDepts: avant suppression");

        colAccounts.deleteMany(filters);
        listDept=colAccounts.find(filters).sort(new Document("_id", 1));
        it = listDept.iterator();// Getting the iterator
        this.displayIterator(it, "Dans deleteDepts: Apres suppression");
    }

    /**
     FD12 : Parcours un it�rateur et affiche les documents qui s'y trouvent
     */
    public void displayIterator(Iterator it, String message){
        System.out.println(" \n #### "+ message + " ################################");
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }
}

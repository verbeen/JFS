package jfs.data.connections;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

import java.net.UnknownHostException;

/**
 * Created by lpuddu on 29-10-2015.
 *
 * This class is used for access to the Mongo DB server
 *
 */
public class DataClient {
    public static DataClient defaultClient = new DataClient("jfs");

    private MongoDatabase database;

    /**
     * Create DataClient for access to Mongo DB
     * Connection can be locally or in OpenShift environment
     * The differentiation is made if the environemnt variable OPENSHIFT_MONGODB_DB_URL is set or not
     */
     public DataClient(String databaseName)
    {
        MongoClient client;
        String dbUrl = System.getenv("OPENSHIFT_MONGODB_DB_URL");
        if(dbUrl != null){
                client = new MongoClient(new MongoClientURI(dbUrl));
        }
        else{
            client = new MongoClient();
        }
        this.database = client.getDatabase(databaseName);
    }

    /**
     * Get a collection from the MongoDB server by name and docType
     */
    public <T> MongoCollection<T> getCollection(String name, Class<T> docType)
    {
        MongoCollection collection = this.database.getCollection(name, docType);

        if(collection == null)
        {
            this.database.createCollection(name);
            collection = this.database.getCollection(name, docType);
        }

        return collection;
    }
}
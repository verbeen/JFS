package jfs.data.connections;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.net.UnknownHostException;

/**
 * Created by lpuddu on 29-10-2015.
 */
public class DataClient {
    public static DataClient defaultClient = new DataClient("jfs");
    private MongoDatabase database;

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

    public MongoCollection getCollection(String name)
    {
        MongoCollection collection = this.database.getCollection(name);

        if(collection == null)
        {
            this.database.createCollection(name);
            collection = this.database.getCollection(name);
        }

        return collection;
    }
}

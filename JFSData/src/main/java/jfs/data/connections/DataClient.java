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
        String dbUrl = System.getenv("OPENSHIFT_MONGODB_DB_URL");
        MongoClient client;
        if(dbUrl != null){
            /*String dbPort = System.getenv("OPENSHIFT_MONGODB_DB_PORT");
            String dbUser = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
            String dbPass = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
            client = new MongoClient(dbIP, Integer.parseInt(dbPort));
            MongoCredential credential = MongoCredential.createCredential(dbUser, databaseName, dbPass.toCharArray());*/
            client = new MongoClient(new MongoClientURI(dbUrl));
        }
        else{
            client = new MongoClient();
        }
        this.database = client.getDatabase(databaseName);
    }

    public DataClient(String serverAddress, String databaseName) throws UnknownHostException
    {
        MongoClient client = new MongoClient(serverAddress);
        this.database = client.getDatabase(databaseName);

    }

    public DataClient(String serverAddress, String databaseName, int port) throws UnknownHostException
    {
        MongoClient client = new MongoClient(serverAddress, port);
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

package jfs.data.connections;

import com.mongodb.MongoClient;
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
        MongoClient client = new MongoClient();
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

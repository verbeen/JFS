package jfs.data.stores;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import jfs.data.connections.DataClient;
import jfs.data.serializers.Serializer;
import org.bson.Document;

import java.util.logging.Level;

/**
 * Created by lpuddu on 2-11-2015.
 */
public abstract class DataStore {
    // Mongocollection is thread safe, as well as the serializer. So this class can be used concurrently.
    protected MongoCollection<Document> collection;
    protected Serializer serializer = Serializer.DefaultSerializer;

    public DataStore(String dbName) {
        this.collection = DataClient.defaultClient.getCollection(dbName);
    }

    public boolean insert(Object obj, Object id){
        Document doc = Document.parse(this.serializer.Serialize(obj));
        doc.put("_id", id);

        try {
            this.collection.insertOne(doc);
            return true;
        } catch(MongoWriteException ex) {
            if(ex.getError().getCode() == 11000){
                return false;
            }else {
                //Logger.getLogger(this.getClass()).log(Level.INFO, ex.getMessage(), ex);
                return false;
            }
        }
    }
}

package jfs.data.stores;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import jfs.data.connections.DataClient;
import jfs.data.serializers.Serializer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Mongocollection is thread safe, as well as the serializer. So this class is thread safe.
 * Created by lpuddu on 2-11-2015.
 */
public abstract class DataStore {
    protected MongoCollection<Document> collection;
    protected Serializer serializer = Serializer.DefaultSerializer;

    public DataStore(String dbName) {
        this.collection = DataClient.defaultClient.getCollection(dbName);
    }

    public boolean insert(Object obj){
        return this.insert(obj, null);
    }

    public boolean insert(Object obj, Object id){
        Document doc = Document.parse(this.serializer.Serialize(obj));

        if(id != null) {
            doc.put("_id", id);
        }

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

    //TODO public boolean replace(Object obj){}

    public Document getOneDocument(String key, Object value){
        return this.collection.find(new Document(key, value)).first();
    }

    public <T> T getOneObject(String key, Object value, Class<T> type){
        T result = null;
        Document doc = this.getOneDocument(key, value);
        if(doc != null){
            result = (T)this.serializer.DeSerialize(doc.toJson(), type);
        }
        return result;
    }

    protected <T> List<Document> createDocumentList(List<T> objects){
        List<Document> docs = new ArrayList<Document>();
        for(T object : objects){
            docs.add(Document.parse(this.serializer.Serialize(object)));
        }
        return docs;
    }
}

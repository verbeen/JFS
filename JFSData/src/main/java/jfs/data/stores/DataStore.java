package jfs.data.stores;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import jfs.data.connections.DataClient;
import jfs.data.dataobjects.DataObject;
import jfs.data.serializers.Serializer;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lpuddu on 2-11-2015.
 *
 * Class used for access to the DataStore. All other store classes will inherit from this one.
 * MongoCollection is thread safe, as well as the serializer. So the class is thread safe.
 *
 */
public abstract class DataStore<T> {
    protected MongoCollection<DBObject> collection;
    protected Serializer serializer = Serializer.DefaultSerializer;

    public DataStore(String dbName) {
        this.collection = DataClient.defaultClient.getCollection(dbName, DBObject.class);
    }

    /**
     * Insert an object into the database
     */
    public Boolean insert(DataObject obj){
        return this.insert(obj, null);
    }

    public Boolean insert(DataObject obj, String id){
        String json = this.serializer.serialize(obj);
        DBObject doc = BasicDBObject.parse(json);

        try {
            this.collection.insertOne(doc);
            return true;
        }
        catch(MongoWriteException ex) {
            if(ex.getError().getCode() == 11000){ // 11000 represents a duplicate key error
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
            }else {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return false;
    }

    /*
     * Function to replace and existing object in the collection
     * key is an unique ObjectId
     * value is the new document
     */
    public <T> Boolean replace(String key, T value){
        BasicDBObject doc = (BasicDBObject) this.collection.find(new BasicDBObject("_id", key)).first();
        UpdateResult updateResult = this.collection.replaceOne(doc, BasicDBObject.parse(this.serializer.serialize(value)));
        return updateResult.wasAcknowledged();
    }

    public DBObject getOneDocument(String key, Object value){
        return this.collection.find(new BasicDBObject(key, value)).first();
    }

    public <T> T getOneObject(String key, Object value, Class<T> type){
        T result = null;
        DBObject doc = this.getOneDocument(key, value);
        if(doc != null){
            result = (T)this.serializer.deSerialize(doc.toString(), type);
        }
        return result;
    }

    protected <T> List<DBObject> createDocumentList(List<T> objects){
        List<DBObject> docs = new ArrayList<DBObject>();
        for(T object : objects){
            docs.add(BasicDBObject.parse(this.serializer.serialize(object)));
        }
        return docs;
    }
}

package jfs.data.stores;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import jfs.data.connections.DataClient;
import jfs.data.serializers.Serializer;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Mongocollection is thread safe, as well as the serializer. So this class is thread safe.
 * Created by lpuddu on 2-11-2015.
 */
public abstract class DataStore<T> {
    protected MongoCollection<DBObject> collection;
    protected Serializer serializer = Serializer.DefaultSerializer;

    public DataStore(String dbName) {
        this.collection = DataClient.defaultClient.getCollection(dbName, DBObject.class);
    }

    public String insert(Object obj){
        return this.insert(obj, null);
    }

    public String insert(Object obj, Object id){
        String json = this.serializer.Serialize(obj);
        DBObject doc = BasicDBObject.parse(json);

        if(id != null) {
            doc.put("_id", id);
        }

        try {
            this.collection.insertOne(doc);
            return doc.get("_id").toString();
        }
        catch(MongoWriteException ex) {
            if(ex.getError().getCode() == 11000){
                return null;
            }else {
                //Logger.getLogger(this.getClass()).log(Level.INFO, ex.getMessage(), ex);
                return null;
            }
        }
    }

    public <T> Boolean replace(String key, T value){
        UpdateResult updateResult = this.collection.replaceOne(new BasicDBObject("_id", new ObjectId(key)), BasicDBObject.parse(this.serializer.Serialize(value)));
        return updateResult.wasAcknowledged();
    }

    //TODO public boolean replace(Object obj){}

    public DBObject getOneDocument(String key, Object value){
        return this.collection.find(new BasicDBObject(key, value)).first();
    }

    public <T> T getOneObject(String key, Object value, Class<T> type){
        T result = null;
        DBObject doc = this.getOneDocument(key, value);
        if(doc != null){
            result = (T)this.serializer.DeSerialize(doc.toString(), type);
        }
        return result;
    }

    protected <T> List<DBObject> createDocumentList(List<T> objects){
        List<DBObject> docs = new ArrayList<DBObject>();
        for(T object : objects){
            docs.add(BasicDBObject.parse(this.serializer.Serialize(object)));
        }
        return docs;
    }
}

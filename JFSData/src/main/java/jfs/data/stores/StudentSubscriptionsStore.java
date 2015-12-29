package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jfs.data.dataobjects.StudentProfileDO;
import jfs.data.dataobjects.StudentSubscriptionsDO;
import jfs.data.serializers.Serializer;
import org.bson.Document;

/**
 * Created by Hulk-A on 10.11.2015.
 */
public class StudentSubscriptionsStore extends DataStore {
    public static final StudentSubscriptionsStore store = new StudentSubscriptionsStore();

    public StudentSubscriptionsStore() {
        super("studentSubscription");
    }

    public Boolean addStudentSubscription(StudentSubscriptionsDO studentSubscription) {
        if (studentSubscription != null) {
            return this.insert(studentSubscription, studentSubscription._id) != null;
        } else {
            throw new NullPointerException("StudentSubscriptionsDO studentSubscription parameter is null");
        }
    }

    public StudentSubscriptionsDO getStudentSubscriptions(String userId) {
        StudentSubscriptionsDO studentSubscription = null;
        DBObject doc = (DBObject)this.collection.find(new BasicDBObject("_id", userId)).first();
        if (doc != null) {
            studentSubscription = (StudentSubscriptionsDO)  this.serializer.deSerialize((doc).toString(), StudentSubscriptionsDO.class);
        }
        return studentSubscription;
    }

    public Boolean updateStudentSubscription(String userId, StudentSubscriptionsDO studentSubscription){
        if (studentSubscription != null) {
            Document query = new Document().append("_id", studentSubscription._id);
            DBObject obj = BasicDBObject.parse(this.serializer.serialize(studentSubscription));
            return this.collection.replaceOne(query, obj, new UpdateOptions().upsert(true)).wasAcknowledged();
        } else {
            throw new NullPointerException("StudentSubscriptionsDO studentSubscription parameter is null");
        }
    }

    public Boolean updateLastView(String userId, long lastView) {
        if ((userId != null)) {
            UpdateResult myResult = this.collection.updateOne(new BasicDBObject("_id", userId), new BasicDBObject("$set", new BasicDBObject("lastView", lastView)));
            return myResult.wasAcknowledged();
        } else {
            throw new NullPointerException("userId parameter is null");
        }
    }

    public Boolean deleteStudentSubscription(String userId){
        if (userId != null || !userId.isEmpty()){
            BasicDBObject filter = new BasicDBObject("_id", userId);
            DeleteResult deleteResult = this.collection.deleteOne(filter);
            return deleteResult.wasAcknowledged();
        }else {
            throw new NullPointerException("userId parameter is null or empty");
        }

    }
}

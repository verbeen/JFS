package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jfs.data.dataobjects.StudentProfileDO;
import jfs.data.dataobjects.StudentSubscriptionsDO;

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
            studentSubscription = (StudentSubscriptionsDO) new Gson().fromJson((doc).toString(), StudentSubscriptionsDO.class);
        }
        return studentSubscription;
    }
}

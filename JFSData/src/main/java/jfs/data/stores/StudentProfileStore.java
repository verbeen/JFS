package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jfs.data.connections.DataClient;
import jfs.data.dataobjects.StudentProfileDO;
import jfs.data.dataobjects.UserDO;
import jfs.data.serializers.Serializer;
import org.bson.Document;

/**
 * Created by Hulk-A on 10.11.2015.
 */
public class StudentProfileStore extends DataStore {
    public static final StudentProfileStore store = new StudentProfileStore();

    protected Serializer serializer = Serializer.DefaultSerializer;

    public StudentProfileStore() {
        super("studentProfiles");
    }

    public Boolean addStudentProfile(StudentProfileDO studentProfile) {
        if (studentProfile != null) {
            return this.insert(studentProfile, studentProfile._id) != null;
        } else {
            throw new NullPointerException("StudentProfileDO studentProfile parameter is null");
        }
    }

    public StudentProfileDO getStudentProfile(String user_id) {
        StudentProfileDO studentProfile = null;
        DBObject doc = (DBObject)this.collection.find(new BasicDBObject("_id", user_id)).first();
        if (doc != null) {
            studentProfile = (StudentProfileDO) this.serializer.deSerialize(doc.toString(), StudentProfileDO.class);
        }
        return studentProfile;
    }
}

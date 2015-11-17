package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jfs.data.connections.DataClient;
import jfs.data.dataobjects.StudentProfileDO;
import jfs.data.dataobjects.UserDO;
import org.bson.Document;

/**
 * Created by Hulk-A on 10.11.2015.
 */
public class StudentProfileStore extends DataStore {
    public static final StudentProfileStore store = new StudentProfileStore();

    public StudentProfileStore() {
        super("studentProfiles");
    }

    public Boolean addStudentProfile(StudentProfileDO studentProfile) {
        if (studentProfile != null) {
            return this.insert(studentProfile, studentProfile._id) != null; //TODO does user_id has to be unique here? In that case the user_id which is userDO.id cannot be used
        } else {
            throw new NullPointerException("StudentProfileDO studentProfile parameter is null");
        }
    }

    public StudentProfileDO getStudentProfile(String user_id) {
        StudentProfileDO studentProfile = null;
        DBObject doc = (DBObject)this.collection.find(new BasicDBObject("_id", user_id)).first();
        if (doc != null) {
            studentProfile = (StudentProfileDO) new Gson().fromJson((doc).toString(), StudentProfileDO.class);
        }
        return studentProfile;
    }

    //TODO public StudentProfileDO editStudentProfile(String user_id, StudentProfileDO studentProfile){}
}

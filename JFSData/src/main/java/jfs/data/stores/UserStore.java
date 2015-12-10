package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import jfs.data.dataobjects.UserDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 29-10-2015.
 */
public class UserStore extends DataStore {
    public static final UserStore store = new UserStore();

    private UserStore() {
        super("users");
    }

    public Boolean addUser(UserDO user){
        if (user != null) {
            return this.insert(user, user._id) != null;
        } else {
            throw new NullPointerException("UserDO user parameter is null");
        }
    }

    public UserDO getUser(String id, String password){
        UserDO user = null;
        DBObject doc = (DBObject)this.collection.find(new BasicDBObject("_id", id)).first();
        if (doc != null) {
            user = (UserDO) this.serializer.deSerialize((doc).toString(), UserDO.class);
            if(user.password.equals(password)){
                return user;
            }
        }
        return null;
    }

    public List<UserDO> getAllUsers(){
        List<UserDO> users = new ArrayList<UserDO>();
        FindIterable<DBObject> results = this.collection.find();
        for(DBObject obj : results){
            users.add(this.serializer.deSerialize(obj.toString(), UserDO.class));
        }
        return users;
    }
}


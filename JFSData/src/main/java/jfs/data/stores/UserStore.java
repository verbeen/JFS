package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jfs.data.dataobjects.UserDO;

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
            return this.insert(user, user.id) != null;
        } else {
            throw new NullPointerException("UserDO user parameter is null");
        }
    }

    public UserDO getUser(String id, String password){
        UserDO user = null;
        DBObject doc = (DBObject)this.collection.find(new BasicDBObject("_id", id)).first();
        if (doc != null) {
            user = (UserDO) new Gson().fromJson((doc).toString(), UserDO.class);
            if(user.password.equals(password)){
                return user;
            }
        }
        return null;
    }
}


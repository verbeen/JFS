package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import jfs.data.dataobjects.UserDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 29-10-2015.
 *
 * Class used for access to the UserStore
 *
 */
public class UserStore extends DataStore {
    public static final UserStore store = new UserStore();

    private UserStore() {
        super("users");
    }

    /**
     * Add a new user by UserDO
     */
    public Boolean addUser(UserDO user){
        if (user != null) {
            return this.insert(user, user._id);
        } else {
            throw new NullPointerException("UserDO user parameter is null");
        }
    }

    /**
     * Get a UserDO by id and password
     */
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

    /**
     * Get all users as a List<UserDO>
     */
    public List<UserDO> getAllUsers(){
        List<UserDO> users = new ArrayList<UserDO>();
        FindIterable<DBObject> results = this.collection.find();
        for(DBObject obj : results){
            users.add(this.serializer.deSerialize(obj.toString(), UserDO.class));
        }
        return users;
    }

    /**
     * Get a user by userId
     */
    public UserDO getUser(String userId){
        DBObject user = this.getOneDocument("_id", userId);
        if (user != null){
            UserDO userDO = this.serializer.deSerialize(user.toString(),UserDO.class);
            return userDO;
        }
        else{
            return null;
        }
    }

    /**
     * Delete a user by userId
     */
    public boolean deleteUser(String userId){
        if (userId != null || !userId.isEmpty()) {
            BasicDBObject filter = new BasicDBObject("_id", userId);
            DeleteResult userDeleteResult = this.collection.deleteOne(filter);
            return userDeleteResult.wasAcknowledged();
        }else{
            throw new NullPointerException("userId parameter is null or empty");
        }
    }
}
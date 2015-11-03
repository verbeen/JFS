package jfs.data.stores;

import com.google.gson.Gson;
import jfs.data.connections.DataClient;
import jfs.data.dataobjects.UserDO;
import org.bson.Document;

/**
 * Created by lpuddu on 29-10-2015.
 */
public class UserStore extends DataStore {

    public UserStore(DataClient client) {
        super(client, "users");
    }

    public Boolean addUser(UserDO user){
        if (user != null) {
            return this.insert(user, user.id);
        } else {
            throw new NullPointerException("UserDO user parameter is null");
        }
    }

    public UserDO getUser(String id, String password){
        UserDO user = null;
        Document doc = this.store.find(new Document("_id", id)).first();
        if (doc != null) {
            user = (UserDO) new Gson().fromJson((doc).toJson(), UserDO.class);
            if(user.password.equals(password)){
                return user;
            }
        }
        return null;
    }
}

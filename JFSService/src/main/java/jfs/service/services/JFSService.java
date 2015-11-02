package jfs.service.services;

import jfs.data.connections.DataClient;
import jfs.data.dataobjects.UserDO;
import jfs.data.stores.UserStore;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created by lpuddu on 29-10-2015.
 */
@Startup @Singleton
public class JFSService {
    private UserStore userStore;

    @PostConstruct
    public void init() {
        DataClient client = new DataClient("jfs");
        this.userStore = new UserStore(client);
    }

    public void RegisterUser(String email, String password, String name){
        UserDO user = new UserDO(email + password, email, name);
        this.userStore.addUser(user);
    }

    public UserDO loginUser(String email, String password) {
        UserDO user = this.userStore.getUser(email + password);
        return user;
    }
}

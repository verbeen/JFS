package jfs.service.services;

import jfs.data.connections.DataClient;
import jfs.data.dataobjects.UserDO;
import jfs.data.dataobjects.enums.UserType;
import jfs.data.stores.UserStore;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Created by lpuddu on 29-10-2015.
 */
@Singleton
public class UserService {
    @Inject
    private DataService dataService;
    private UserStore userStore;

    @PostConstruct
    public void init() {
        this.userStore = new UserStore(this.dataService.getDataClient());
    }

    public Boolean registerStudent(String email, String password){
        UserDO user = new UserDO(email, password, UserType.STUDENT);
        return this.userStore.addUser(user);
    }

    public UserDO loginUser(String email, String password) {
        UserDO user = this.userStore.getUser(email, password);
        return user;
    }
}

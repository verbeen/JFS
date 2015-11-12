package jfs.service.services;

import jfs.data.dataobjects.UserDO;
import jfs.data.dataobjects.enums.UserType;
import jfs.data.stores.UserStore;
import jfs.service.transferobjects.LoginResultDTO;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.UUID;

/**
 * Created by lpuddu on 29-10-2015.
 */
@Singleton
public class UserService {
    @Inject
    private SessionService sessionService;
    private UserStore userStore = UserStore.store;

    public Boolean registerUser(String email, String password, UserType type){
        UserDO user = new UserDO(email, password, type);
        return this.userStore.addUser(user);
    }

    public LoginResultDTO loginUser(String email, String password) {
        UserDO user = this.userStore.getUser(email, password);
        LoginResultDTO result = new LoginResultDTO();
        if(user != null){
            result.isLoggedIn = true;
            result.token = UUID.randomUUID().toString();
            this.sessionService.sessions.put(result.token, user.id);
        }else{
            result.isLoggedIn = false;
        }
        return result;
    }
}

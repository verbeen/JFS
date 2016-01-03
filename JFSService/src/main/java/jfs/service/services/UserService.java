package jfs.service.services;

import jfs.data.dataobjects.UserDO;
import jfs.data.dataobjects.enums.UserType;
import jfs.data.stores.UserStore;
import jfs.transferdata.transferobjects.LoginResultDTO;
import jfs.service.sessions.Session;
import jfs.transferdata.transferobjects.UserDTO;
import jfs.transferdata.transferobjects.enums.UserTypeDTO;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lpuddu on 29-10-2015.
 *
 * User service wrapper for userStore
 *
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
            result.type = UserTypeDTO.valueOf(user.type.name());
            this.sessionService.sessions.put(result.token, new Session(user._id, UserTypeDTO.valueOf(user.type.name())));
        }else{
            result.isLoggedIn = false;
        }
        return result;
    }

    public List<UserDTO> getAllUsers(){
        List<UserDTO> users = new ArrayList<UserDTO>();
        for(UserDO userDO : this.userStore.getAllUsers()){
            users.add(this.createUserDTO(userDO));
        }
        return users;
    }

    private UserDTO createUserDTO(UserDO userDO){
        return new UserDTO(
                userDO._id, UserTypeDTO.valueOf(userDO.type.name())
        );
    }
}

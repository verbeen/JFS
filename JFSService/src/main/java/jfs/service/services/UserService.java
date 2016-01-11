package jfs.service.services;

import com.mongodb.BasicDBObject;
import jfs.data.dataobjects.UserDO;
import jfs.data.dataobjects.enums.UserType;
import jfs.data.stores.JobOfferMetricsStore;
import jfs.data.stores.JobOfferStore;
import jfs.data.stores.StudentSubscriptionsStore;
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
    private StudentSubscriptionsStore studentSubscriptionsStore = StudentSubscriptionsStore.store;
    private JobOfferMetricsStore metricsStore = JobOfferMetricsStore.jobOfferMetricsStore;
    private JobOfferStore jobOfferStore = JobOfferStore.store;

    //Register a user by email, password and UserType
    //Returns boolean for succcess
    public Boolean registerUser(String email, String password, UserType type){
        UserDO user = new UserDO(email, password, type);
        return this.userStore.addUser(user);
    }

    //Login a user by email and password
    //Returns a LoginResultDTO which can be verified
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

    //Get all users as List<UserDTO>
    public List<UserDTO> getAllUsers(){
        List<UserDTO> users = new ArrayList<UserDTO>();
        for(UserDO userDO : this.userStore.getAllUsers()){
            users.add(this.createUserDTO(userDO));
        }
        return users;
    }

    //Delete a user by userId
    public boolean deleteUser(String userId){
        UserDO user = this.userStore.getUser(userId);
        boolean result = false;
        if (user != null){
            switch (user.type){
                case STUDENT:
                    // delete student
                    if (this.userStore.deleteUser(userId)){
                        // delete student notifications
                        studentSubscriptionsStore.deleteStudentSubscription(userId);
                        result=true;
                    }
                    break;
                case COMPANY:
                    // delete company
                    if (this.userStore.deleteUser(userId)){
                        // delete job metrics
                        metricsStore.deleteJobMetrics(userId);
                        // delete job offers
                        jobOfferStore.deleteJobOffers(userId);
                        result = true;
                    }
                    break;
            }
        }
        return result;
    }

    //Used for creating a UserDTO out of a UserDO
    private UserDTO createUserDTO(UserDO userDO){
        return new UserDTO(
                userDO._id, UserTypeDTO.valueOf(userDO.type.name())
        );
    }
}
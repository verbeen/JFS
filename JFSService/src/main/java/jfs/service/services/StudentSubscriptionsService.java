package jfs.service.services;

import jfs.data.dataobjects.StudentProfileDO;
import jfs.data.dataobjects.StudentSubscriptionsDO;
import jfs.data.stores.StudentProfileStore;
import jfs.data.stores.StudentSubscriptionsStore;
import jfs.transferdata.transferobjects.StudentProfileDTO;
import jfs.transferdata.transferobjects.StudentSubscriptionsDTO;

import javax.ejb.Singleton;

/**
 * Created by Hulk-A on 13.11.2015.
 */
@Singleton
public class StudentSubscriptionsService {
    private StudentSubscriptionsStore studentSubscriptionsStore = StudentSubscriptionsStore.store;

    private StudentSubscriptionsDO createStudentSubscriptionDO(StudentSubscriptionsDTO studentSubscriptionsDTO){
        return new StudentSubscriptionsDO(
                studentSubscriptionsDTO.userId, studentSubscriptionsDTO.subTypes, studentSubscriptionsDTO.subLocation , studentSubscriptionsDTO.subSkills
        );
    }

    private StudentSubscriptionsDTO createStudentSubscriptionsDTO(StudentSubscriptionsDO studentSubscriptionsDO){
        return new StudentSubscriptionsDTO(
                studentSubscriptionsDO._id, studentSubscriptionsDO.subTypes, studentSubscriptionsDO.subLocation , studentSubscriptionsDO.subSkills
        );
    }

    public Boolean addStudentSubscriptions(StudentSubscriptionsDTO studentSubscriptionsDTO){
        return this.studentSubscriptionsStore.addStudentSubscription(this.createStudentSubscriptionDO(studentSubscriptionsDTO));
    }

    public StudentSubscriptionsDTO getStudentSubscriptions(String userId){
        return createStudentSubscriptionsDTO(this.studentSubscriptionsStore.getStudentSubscriptions(userId));
    }

    //TODO update
}

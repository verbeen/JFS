package jfs.service.services;

import jfs.data.dataobjects.StudentSubscriptionsDO;
import jfs.data.stores.StudentSubscriptionsStore;
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
                studentSubscriptionsDTO.userId, studentSubscriptionsDTO.types, studentSubscriptionsDTO.location , studentSubscriptionsDTO.skills
        );
    }

    private StudentSubscriptionsDTO createStudentSubscriptionsDTO(StudentSubscriptionsDO studentSubscriptionsDO){
        return new StudentSubscriptionsDTO(
                studentSubscriptionsDO._id, studentSubscriptionsDO.types, studentSubscriptionsDO.location , studentSubscriptionsDO.skills
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

package jfs.service.services;

import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.StudentSubscriptionsDO;
import jfs.data.stores.JobOfferStore;
import jfs.data.stores.StudentSubscriptionsStore;
import jfs.transferdata.transferobjects.JobOfferDTO;
import jfs.transferdata.transferobjects.StudentSubscriptionsDTO;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hulk-A on 13.11.2015.
 */
@Singleton
public class StudentSubscriptionsService {
    private StudentSubscriptionsStore studentSubscriptionsStore = StudentSubscriptionsStore.store;
    private JobOfferStore jobOfferStore = JobOfferStore.store;

    @Inject
    private JobOfferService jobOfferService;

    private StudentSubscriptionsDO createStudentSubscriptionDO(StudentSubscriptionsDTO studentSubscriptionsDTO){
        return new StudentSubscriptionsDO(
                studentSubscriptionsDTO.userId, studentSubscriptionsDTO.types, studentSubscriptionsDTO.location , studentSubscriptionsDTO.skills, studentSubscriptionsDTO.lastView
        );
    }

    private StudentSubscriptionsDTO createStudentSubscriptionsDTO(StudentSubscriptionsDO studentSubscriptionsDO){
        return new StudentSubscriptionsDTO(
                studentSubscriptionsDO._id, studentSubscriptionsDO.types, studentSubscriptionsDO.location , studentSubscriptionsDO.skills, studentSubscriptionsDO.lastView
        );
    }

    public Boolean addStudentSubscriptions(StudentSubscriptionsDTO studentSubscriptionsDTO){
        return this.studentSubscriptionsStore.addStudentSubscription(this.createStudentSubscriptionDO(studentSubscriptionsDTO));
    }

    public StudentSubscriptionsDTO getStudentSubscriptions(String userId){
        return createStudentSubscriptionsDTO(this.studentSubscriptionsStore.getStudentSubscriptions(userId));
    }

    public Boolean updateStudentSubscriptions(String userId, StudentSubscriptionsDTO studentSubscriptionsDTO){
        return this.studentSubscriptionsStore.updateStudentSubscription(userId, this.createStudentSubscriptionDO(studentSubscriptionsDTO));
    }

    public Boolean updateLastView(String userId, long lastView){
        return this.studentSubscriptionsStore.updateLastView(userId, lastView);
    }

    public List<JobOfferDTO> checkSubscriptions(StudentSubscriptionsDTO studentSubscriptionsDTO){
        List<JobOfferDO> offerDOs = jobOfferStore.getJobOffersByCriteria(this.createStudentSubscriptionDO(studentSubscriptionsDTO));
        return jobOfferService.createOfferDTOList(offerDOs);
    }
}

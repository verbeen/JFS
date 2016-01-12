package jfs.service.services;

import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.StudentSubscriptionsDO;
import jfs.data.dataobjects.enums.JobType;
import jfs.data.stores.JobOfferStore;
import jfs.data.stores.StudentSubscriptionsStore;
import jfs.transferdata.transferobjects.JobOfferDTO;
import jfs.transferdata.transferobjects.StudentSubscriptionsDTO;
import jfs.transferdata.transferobjects.enums.JobTypeDTO;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Hulk-A on 13.11.2015.
 *
 * Student subscription service wrapper for studentSubscriptionStore
 *
 */
@Singleton
public class StudentSubscriptionsService {
    private StudentSubscriptionsStore studentSubscriptionsStore = StudentSubscriptionsStore.store;
    private JobOfferStore jobOfferStore = JobOfferStore.store;

    @Inject
    private JobOfferService jobOfferService;

    /**
     * Create StudentSubscriptionsDO out of StudentSubscriptionsDTO
     */
    private StudentSubscriptionsDO createStudentSubscriptionDO(StudentSubscriptionsDTO studentSubscriptionsDTO){
        return new StudentSubscriptionsDO(
                studentSubscriptionsDTO.userId, JobType.valueOf(studentSubscriptionsDTO.type.name()), studentSubscriptionsDTO.location , studentSubscriptionsDTO.skills, studentSubscriptionsDTO.lastView
        );
    }

    /**
     * Create StudentSubscriptionsDTO out of StudentSubscriptionsDO
     */
    private StudentSubscriptionsDTO createStudentSubscriptionsDTO(StudentSubscriptionsDO studentSubscriptionsDO){
        return new StudentSubscriptionsDTO(
                studentSubscriptionsDO._id, JobTypeDTO.valueOf(studentSubscriptionsDO.type.name()), studentSubscriptionsDO.location , studentSubscriptionsDO.skills, studentSubscriptionsDO.lastView
        );
    }

    /**
     * Add a student subscription by StudentSubscriptionsDTO
     * Returns boolean for success
     */
    public Boolean addStudentSubscriptions(StudentSubscriptionsDTO studentSubscriptionsDTO){
        return this.studentSubscriptionsStore.addStudentSubscription(this.createStudentSubscriptionDO(studentSubscriptionsDTO));
    }

    /**
     * Get a student subscription by userID
     */
    public StudentSubscriptionsDTO getStudentSubscriptions(String userId){
        StudentSubscriptionsDO studentSubscriptionsDO = this.studentSubscriptionsStore.getStudentSubscriptions(userId);
        if(studentSubscriptionsDO != null){
            return createStudentSubscriptionsDTO(studentSubscriptionsDO);
        }
        else {
            return null;
        }
    }

    /**
     * Update a student subscription by userID and StudentSubscriptionsDTO
     * Returns boolean for success
     */
    public Boolean updateStudentSubscriptions(String userId, StudentSubscriptionsDTO studentSubscriptionsDTO){
        return this.studentSubscriptionsStore.updateStudentSubscription(userId, this.createStudentSubscriptionDO(studentSubscriptionsDTO));
    }

    /**
     * Update lastView field of a student subscription by userID and lastView
     */
    public Boolean updateLastView(String userId, long lastView){
        return this.studentSubscriptionsStore.updateLastView(userId, lastView);
    }

    /**
     * Returns a job offer list List<JobOfferDTO> which contains all job offers that fit the criteria linked
     * to the student subscription for userId
     */
    public List<JobOfferDTO> checkSubscriptions(String userId){
        long jobOfferVisibilityBuffer = (60*60*24*3);
        //jobOfferVisibilityBuffer: amount of time in seconds that results get shown after they were created
        //(if they apply to the notification settings=
        StudentSubscriptionsDO studentSubscriptionsDO = this.studentSubscriptionsStore.getStudentSubscriptions(userId);

        List<JobOfferDO> offerDOs = jobOfferStore.getJobOffersByCriteria(studentSubscriptionsDO, jobOfferVisibilityBuffer);
        return jobOfferService.createOfferDTOList(offerDOs);
   }
}
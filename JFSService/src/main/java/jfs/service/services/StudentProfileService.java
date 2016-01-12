package jfs.service.services;

import jfs.data.dataobjects.StudentProfileDO;
import jfs.data.stores.JobOfferStore;
import jfs.data.stores.StudentProfileStore;
import jfs.transferdata.transferobjects.StudentProfileDTO;

import javax.ejb.Singleton;

/**
 * Created by Hulk-A on 13.11.2015.
 *
 * Student profile service wrapper for studentProfileStore
 *
 */
@Singleton
public class StudentProfileService {
    private StudentProfileStore studentProfileStore = StudentProfileStore.store;

    /**
     * Created StudentProfileDO out of StudentProfileDTO
     */
    private StudentProfileDO createStudentDO(StudentProfileDTO studentProfileDTO){
        return new StudentProfileDO(
                studentProfileDTO.userId, studentProfileDTO.name, studentProfileDTO.email , studentProfileDTO.organization, studentProfileDTO.address, studentProfileDTO.skills,
                studentProfileDTO.experience, studentProfileDTO.resume, studentProfileDTO.courseDetails
        );
    }

    /**
     * Created StudentProfileDTO out of StudentProfileDO
     */
    private StudentProfileDTO createStudentDTO(StudentProfileDO studentDO){
        return new StudentProfileDTO(
                studentDO._id, studentDO.name, studentDO.email , studentDO.organization, studentDO.address, studentDO.skills,
                studentDO.experience, studentDO.resume, studentDO.courseDetails
        );
    }

    /**
     * Add a student profile
     * @return boolean for success
     */
    public Boolean addStudentProfile(StudentProfileDTO studentProfileDTO){
        return this.studentProfileStore.addStudentProfile(this.createStudentDO(studentProfileDTO));
    }

    /**
     * Get a student profile by userId
     */
    public StudentProfileDTO getStudentProfile(String userId){
        return createStudentDTO(this.studentProfileStore.getStudentProfile(userId));
    }
}

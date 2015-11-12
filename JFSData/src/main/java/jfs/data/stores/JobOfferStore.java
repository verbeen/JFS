package jfs.data.stores;

import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.UserDO;
import org.bson.Document;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class JobOfferStore extends DataStore {
    public static final JobOfferStore store = new JobOfferStore();

    public JobOfferStore(){
        super("joboffers");
        this.collection.createIndex(new Document("location", "text"));
    }

    public Boolean addUser(JobOfferDO offer){
        if (offer != null) {
            return this.insert(offer, offer.id);
        } else {
            throw new NullPointerException("JobOfferDO offer parameter is null");
        }
    }

    public JobOfferDO getById(String id){
        return this.getOneObject("_id", id, JobOfferDO.class);
    }
}

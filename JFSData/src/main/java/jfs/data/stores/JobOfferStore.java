package jfs.data.stores;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.IndexOptions;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.UserDO;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class JobOfferStore extends DataStore {
    public static final JobOfferStore store = new JobOfferStore();

    public JobOfferStore(){
        super("joboffers");
        Document index = new Document().append("location", "text")
                                       .append("function", "text")
                                       .append("description", "text")
                                       .append("name", "text")
                                       .append("function", "text");
        this.collection.createIndex(index, new IndexOptions().name("textSearch"));
    }

    public Boolean addOffer(JobOfferDO offer){
        if (offer != null) {
            return this.insert(offer, offer.id);
        } else {
            throw new NullPointerException("JobOfferDO offer parameter is null");
        }
    }

    public JobOfferDO getById(String id){
        return this.getOneObject("_id", id, JobOfferDO.class);
    }

    public List<JobOfferDO> getJobOffersText(String term){
        return this.getJobOffersText(term, 100);
    }

    public List<JobOfferDO> getJobOffersText(String term, int amount){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<Document> results = this.collection.find(new Document("textSearch", term)).limit(amount);
        for(Document doc : results){
            offers.add(this.serializer.DeSerialize(doc.toJson(), JobOfferDO.class));
        }
        return offers;
    }
}

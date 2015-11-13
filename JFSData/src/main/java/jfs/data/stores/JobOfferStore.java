package jfs.data.stores;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Sorts;
import javafx.util.Pair;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.UserDO;
import org.bson.BSON;
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

    public Boolean addOffers(List<JobOfferDO> offers){
        List<Document> docs = this.createDocumentList(offers);
        try{
            this.collection.insertMany(docs);
            return true;
        }
        catch (MongoException ex){
            return false;
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

    public List<JobOfferDO> getJobOffersRecent(int amount){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<Document> results = this.collection.find().sort(Sorts.orderBy(Sorts.ascending("validity"))).limit(amount);
        for(Document doc : results){
            offers.add(this.serializer.DeSerialize(doc.toJson(), JobOfferDO.class));
        }
        return offers;
    }

    public List<JobOfferDO> getJobOffers(List<Pair<String, Object>> pairs){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        Document query = new Document();
        for(Pair<String, Object> pair : pairs){
            query.append(pair.getKey(), pair.getValue());
        }
        FindIterable<Document> results = this.collection.find(query);
        for(Document doc : results){
            offers.add(this.serializer.DeSerialize(doc.toJson(), JobOfferDO.class));
        }
        return offers;
    }
}

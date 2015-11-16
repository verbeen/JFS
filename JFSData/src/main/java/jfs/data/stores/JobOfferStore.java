package jfs.data.stores;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Sorts;
import javafx.util.Pair;
import jfs.data.dataobjects.JobOfferDO;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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

    public List<JobOfferDO> getAllOffers(){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results = this.collection.find();
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    public Boolean addOffer(JobOfferDO offer){
        if (offer != null) {
            String id = this.insert(offer);
            if(id != null){
                offer.id = id;
                return this.replace(id, offer);
            }else{
                return false;
            }
        } else {
            throw new NullPointerException("JobOfferDO offer parameter is null");
        }
    }

    public Boolean addOffers(List<JobOfferDO> offers){
        List<DBObject> docs = this.createDocumentList(offers);
        try{
            this.collection.insertMany(docs);
            return true;
        }
        catch (MongoException ex){
            return false;
        }
    }

    public JobOfferDO getById(String id){
        DBObject obj = (DBObject)this.collection.find(new BasicDBObject("_id", id)).first();
        if(obj != null){
            return this.extractJobOffer(obj);
        }else{
            return null;
        }
    }

    public List<JobOfferDO> getJobOffersText(String term){
        return this.getJobOffersText(term, 100);
    }

    public List<JobOfferDO> getJobOffersText(String term, int amount){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results = this.collection.find(new BasicDBObject("textSearch", term)).limit(amount);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    public List<JobOfferDO> getJobOffersRecent(int amount){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results = this.collection.find().sort(Sorts.orderBy(Sorts.ascending("validity"))).limit(amount);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    public List<JobOfferDO> getJobOffers(List<Pair<String, Object>> pairs){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        Document query = new Document();
        for(Pair<String, Object> pair : pairs){
            query.append(pair.getKey(), pair.getValue());
        }
        FindIterable<DBObject> results = this.collection.find(query);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    private JobOfferDO extractJobOffer(DBObject object){
        return this.serializer.DeSerialize(object.toString(), JobOfferDO.class);
    }
}

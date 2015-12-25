package jfs.data.stores;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Sorts;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.StudentSubscriptionsDO;
import jfs.data.dataobjects.enums.JobType;
import jfs.data.dataobjects.helpers.Pair;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class JobOfferStore extends DataStore {
    public static final JobOfferStore store = new JobOfferStore();
    private JobOfferMetricsStore metricsStore = JobOfferMetricsStore.jobOfferMetricsStore;

    public JobOfferStore(){
        super("joboffers");
    }

    public List<JobOfferDO> getAllOffers(){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results = this.collection.find();
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    public Boolean addOffer(JobOfferDO offer, String companyId){
        if (offer != null) {
            return this.insert(offer) && this.metricsStore.add(offer._id, companyId);
        } else {
            throw new NullPointerException("JobOfferDO offer parameter is null");
        }
    }

    public Boolean addOffers(List<JobOfferDO> offers, String companyId){
        List<DBObject> docs = this.createDocumentList(offers);
        try{
            this.collection.insertMany(docs);
            this.metricsStore.addManyByDO(offers, companyId);
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
        FindIterable<DBObject> results = this.collection.find().sort(Sorts.descending("_id")).limit(amount);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    public List<JobOfferDO> getJobOffersByCriteria(StudentSubscriptionsDO studentSubscriptionsDO, long jobOfferVisibilityBuffer){

        ArrayList<Pair<String, Object>> pairs = new ArrayList<Pair<String, Object>>();

        pairs.add(new Pair("_id", new BasicDBObject("$gt", new ObjectId(Long.toHexString((studentSubscriptionsDO.lastView / 1000) - jobOfferVisibilityBuffer) + "0000000000000000").toString())));

        //Explanation for "_id" seach query
        //"_id" Timestamp will be compared with $gt greater than an ObjectId(X).toString()
        //Format that the long lastView should be in seconds instead of milliseconds 1000000000
        // See: http://stackoverflow.com/questions/8749971/can-i-query-mongodb-objectid-by-date

        if(studentSubscriptionsDO.type != null && !"".equals(studentSubscriptionsDO.type) && (studentSubscriptionsDO.type != JobType.all)){
            pairs.add(new Pair("type", studentSubscriptionsDO.type.name()));
        }
        if(studentSubscriptionsDO.location != null && !"".equals(studentSubscriptionsDO.location)){
            pairs.add(new Pair("location", new BasicDBObject("$regex", studentSubscriptionsDO.location)));
        }
        if(studentSubscriptionsDO.skills != null && !"".equals(studentSubscriptionsDO.skills)){
            /*
                For easier creation of the search string and array is created
                which also removes all whitespaces before or after commas that are seperating the skills
                \\Q...\\E is used to quote string in Regex. This is necessary in order to not take e.g. the + character as a command
                | is used to combine all skills by OR
            */
            List<String> myList = Arrays.asList(studentSubscriptionsDO.skills.split("\\s*,\\s*"));

            String skillsRegex = "\\Q";
            if (myList.size() > 1) {
                for (int i = 0; i < myList.size(); i++){
                    skillsRegex += myList.get(i);
                    if (i != myList.size() -1) {
                        skillsRegex += "\\E|\\Q";
                    }
                }
            }else{
                skillsRegex += myList.get(0);
            }
            skillsRegex += "\\E";

            pairs.add(new Pair("skills", new BasicDBObject("$regex", skillsRegex)));
        }

        List<JobOfferDO> doList = this.getJobOffers(pairs);
        return doList;
    }

    public List<JobOfferDO> getJobOffers(List<Pair<String, Object>> pairs){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        Document query = new Document();
        for(Pair<String, Object> pair : pairs){
            query.append(pair.key, pair.value);
        }
        FindIterable<DBObject> results = this.collection.find(query);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    private JobOfferDO extractJobOffer(DBObject object){
        return this.serializer.deSerialize(object.toString(), JobOfferDO.class);
    }
}

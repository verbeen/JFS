package jfs.data.stores;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
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
 *
 * Class used for access to the job offer store
 *
 */
public class JobOfferStore extends DataStore {
    public static final JobOfferStore store = new JobOfferStore();
    private JobOfferMetricsStore metricsStore = JobOfferMetricsStore.jobOfferMetricsStore;

    public JobOfferStore(){
        super("joboffers");

        Document index2dSphere = new Document("location.coordinates", "2dsphere");
        this.collection.createIndex(index2dSphere);
    }

    //Get all job offers
    public List<JobOfferDO> getAllOffers(){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results = this.collection.find();
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }
    public List<JobOfferDO> getAllOffersCompany(String companyId){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        Document select = new Document("userId", companyId);
        FindIterable<DBObject> results = this.collection.find(select);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    //Add a new job offer by JobOfferDO and companyId
    public Boolean addOffer(JobOfferDO offer, String companyId){
        if (offer != null) {
            return this.insert(offer) && this.metricsStore.add(offer._id, companyId);
        } else {
            throw new NullPointerException("JobOfferDO offer parameter is null");
        }
    }

    //Add a several job offer by List<JobOfferDO> and companyId
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

    //Get a specific job offer by id
    public JobOfferDO getById(String id){
        DBObject obj = (DBObject)this.collection.find(new BasicDBObject("_id", id)).first();
        if(obj != null){
            return this.extractJobOffer(obj);
        }else{
            return null;
        }
    }

    //Get a list of the job offers by a search term, limited to 100 findings
    public List<JobOfferDO> getJobOffersText(String term){
        return this.getJobOffersText(term, 100);
    }

    //Get a list of the job offers by a search term and an amount
    public List<JobOfferDO> getJobOffersText(String term, int amount){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results = this.collection.find(new BasicDBObject("textSearch", term)).limit(amount);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    //Get a list of the job offers sorted by recency and a maximum amount
    public List<JobOfferDO> getJobOffersRecent(int amount){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results = this.collection.find().sort(Sorts.descending("_id")).limit(amount);
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    //Get a list of the job offers by a specific studentSubscriptionDO and jobOfferVisibilityBuffer
    //jobOfferVisibilityBuffer is giving as a long and represents the seconds since the job offer was created
    //all offers that were created before the seconds will not be considered even if they would quality the criteria in the jobOfferVisibilityBuffer
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
            pairs.add(new Pair("location.address", new BasicDBObject("$regex", studentSubscriptionsDO.location)));
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

    public void appendTypeQuery(Document doc, String type){
        doc.append("type", type);
    }

    public void appendAddressQuery(Document doc, String address){
        doc.append("location.address", new Document("$regex", address));
    }

    public void appendSkillsQuery(Document doc, String skills){
        //For easier creation of the search string and array is created
        //which also removes all whitespaces before or after commas that are seperating the skills
        //\\Q...\\E is used to quote string in Regex. This is necessary in order to not take e.g. the + character as a command
        //| is used to combine all skills by OR
        List<String> myList = Arrays.asList(skills.split("\\s*,\\s*"));

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

        doc.append("skills", new Document("$regex", skillsRegex));
    }

    /***
     * Creates a pair that contains a proper search query that searches job offers for nearby coordinates
     * @param lat latitude in WGS-84 coordinate double
     * @param lng longitude in WGS-84 coordinate double
     * @param radius radius of the circle to be searched in kilometers
     * @return
     */
    public void appendGeoQuery(Document doc, Double lat, Double lng, int radius){
        //convert radius of the circle to radians by dividing it by the radius of the earth
        double radians = (double)radius / 6378.1d;

        //using geoWithin and not nearSphere since geoWithing is faster because it doesn't sort the results
        Document geo = new Document("$centerSphere", Arrays.asList(Arrays.asList(lng, lat), radians));
        doc.append("location.coordinates", new Document("$geoWithin", geo));
    }

    public List<JobOfferDO> getJobOffers(Document doc, int amount){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        FindIterable<DBObject> results;
        if(amount == 0) {
            results = this.collection.find(doc);
        }
        else{
            results = this.collection.find(doc).limit(amount);
        }
        for(DBObject obj : results){
            offers.add(this.extractJobOffer(obj));
        }
        return offers;
    }

    //Function to search for specific job offers by pairs of search terms
    //Reused in other functions
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

    public boolean delete(String jobOfferId, String userType, String companyId){
        if (jobOfferId != null || !jobOfferId.isEmpty() || companyId != null || !companyId.isEmpty()) {
            DeleteResult result;
            BasicDBObject filter = new BasicDBObject("_id", jobOfferId);
            if (userType == "ADMIN") {
                result = this.collection.deleteOne(filter);
            } else {
                filter.append("userId", companyId);
                result = this.collection.deleteOne(filter);
            }
            metricsStore.deleteSingleJobMetrics(jobOfferId);
            return result.wasAcknowledged();
        }else{
            throw new NullPointerException("jobOfferId parameter is null");
        }
    }

    public boolean deleteJobOffers(String companyId){
        if (companyId != null || !companyId.isEmpty()) {
            BasicDBObject filter = new BasicDBObject("userId", companyId);
            DeleteResult result = this.collection.deleteMany(filter);
            return result.wasAcknowledged();
        }else{
            throw new NullPointerException("companyId parameter is null or empty");
        }
    }

    //deserialize a JobOfferDO from a DBObject
    //Reused in other functions
    private JobOfferDO extractJobOffer(DBObject object){
        return this.serializer.deSerialize(object.toString(), JobOfferDO.class);
    }
}

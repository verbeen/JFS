package jfs.data.stores;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.JobOfferMetricsDO;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 13-12-2015.
 */
public class JobOfferMetricsStore extends DataStore {
    public static final JobOfferMetricsStore jobOfferMetricsStore = new JobOfferMetricsStore();

    public JobOfferMetricsStore() {
        super("joboffermetrics");
    }

    public boolean add(String jobOfferId, String companyId) {
        return this.insert(new JobOfferMetricsDO(jobOfferId, companyId));
    }

    public boolean addManyByDO(List<JobOfferDO> offers, String companyId) {
        List<DBObject> metrics = new ArrayList<DBObject>();
        for (JobOfferDO offer : offers) {
            metrics.add(BasicDBObject.parse(this.serializer.serialize(new JobOfferMetricsDO(offer._id, companyId))));
        }
        try {
            this.collection.insertMany(metrics);
            return true;
        } catch (MongoException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean incrementListViewCountMany(List<String> offerIds){
        Bson select = Filters.in("_id", offerIds);
        Document increment = new Document("$inc", new Document("listViewCount", 1));

        try {
            return this.collection.updateMany(select, increment).wasAcknowledged();
        } catch (MongoException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean incrementDetailViewCount(String offerId){
        Document select = new Document("_id", offerId);
        Document increment = new Document("$inc", new Document("detailViewCount", 1));

        try {
            return this.collection.updateOne(select, increment).wasAcknowledged();
        }
        catch(MongoException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public JobOfferMetricsDO getMetricsById(String jobOfferId) {
        DBObject obj = (DBObject) this.collection.find(new Document("_id", jobOfferId)).first();
        if (obj == null) {
            return null;
        } else {
            return this.serializer.deSerialize(obj.toString(), JobOfferMetricsDO.class);
        }
    }

    public List<JobOfferMetricsDO> getAllMetricsByCompanyId(String companyId){
        List<JobOfferMetricsDO> metricsDOList = new ArrayList<>();
        Document select = new Document("companyId", companyId);

        try{
            FindIterable<DBObject> iterator = this.collection.find(select);
            for(DBObject obj : iterator){
                metricsDOList.add(this.serializer.deSerialize(obj.toString(), JobOfferMetricsDO.class));
            }

        }catch(MongoException ex){
            ex.printStackTrace();
        }

        return metricsDOList;
    }
}

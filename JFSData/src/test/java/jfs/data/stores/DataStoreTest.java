package jfs.data.stores;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import jfs.data.serializers.Serializer;
import org.jongo.Jongo;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Thorsten on 01.12.15.
 */
public class DataStoreTest {

    private static DB db;
    private static Jongo jongo;

    protected MongoCollection<DBObject> collection;
    protected Serializer serializer = Serializer.DefaultSerializer;

    @BeforeClass
    public static void setup() {
        db = new Fongo("Test").getDB("Database");
        jongo = new Jongo(db);
    }

    @Test
    public void insertDataObject() {

    }
}

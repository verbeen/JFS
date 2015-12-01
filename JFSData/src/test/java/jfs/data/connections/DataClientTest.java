package jfs.data.connections;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fakemongo.Fongo;
import com.mongodb.DB;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Thorsten on 01.12.15.
 */
public class DataClientTest {

    // implemented to see if in memory mongodb works
    @Test
    public void test_in_memory_mongodb() {
        DB db = new Fongo("Test").getDB("Database");
        Jongo jongo = new Jongo(db);

        MongoCollection friends = jongo.getCollection("friends");

        friends.insert(
                new Friend("Sheldon"),
                new Friend("Leonard"),
                new Friend("Penny"),
                new Friend("Howard"),
                new Friend("Rajesh"),
                new Friend("Bernadette"),
                new Friend("Amy"));

        assertThat(friends.count()).isEqualTo(7);

        //assertThat(friends.findOne("{name: #}", "Sheldon").as(Friend.class)).isNotNull();
        //assertThat(friends.findOne("{name: #}", "Ross").as(Friend.class)).isNull();
    }

    static class Friend {
        String name;

        @JsonCreator
        Friend(@JsonProperty("name") String name) {
            this.name = name;
        }
    }
}

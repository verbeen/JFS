package jfs.data.serializers;

import com.google.gson.Gson;

/**
 * Created by lpuddu on 2-11-2015.
 *
 * Class that extends Serializer and Overrides serialize and deSerialize methods
 *
 */
public class SerializerGson extends Serializer {
    private Gson gson = new Gson();

    @Override
    public <T> T deSerialize(String raw, Class<T> type) {
        return this.gson.fromJson(raw, type);
    }

    @Override
    public String serialize(Object obj) {
        return this.gson.toJson(obj);
    }
}

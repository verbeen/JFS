package jfs.data.serializers;

/**
 * Created by lpuddu on 30-10-2015.
 *
 * Abstract class for serializing and deserializing objects
 * The DefaultSerializer is to the SerializerGson and can be used throughout JFSData
 *
 */
public abstract class Serializer {
    public static Serializer DefaultSerializer = new SerializerGson();

    public abstract <T> T deSerialize(String raw, Class<T> type);
    public abstract String serialize(Object obj);
}

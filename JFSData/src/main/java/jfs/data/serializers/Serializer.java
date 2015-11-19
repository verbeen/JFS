package jfs.data.serializers;

/**
 * Created by lpuddu on 30-10-2015.
 */
public abstract class Serializer {
    public static Serializer DefaultSerializer = new SerializerGson();

    public abstract Object DeSerialize(String raw, Class type);
    public abstract String Serialize(Object obj);
}

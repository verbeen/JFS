package jfs.data.dataobjects.helpers;

/**
 * Created by lpuddu on 17-11-2015.
 */
public class Pair<T, U> {
    public T key;
    public U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }
}

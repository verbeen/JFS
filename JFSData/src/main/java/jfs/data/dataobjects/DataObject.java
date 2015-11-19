package jfs.data.dataobjects;

import org.bson.types.ObjectId;

/**
 * Created by lpuddu on 17-11-2015.
 */
public class DataObject {
    public String _id;

    public DataObject() {
        this._id = new ObjectId().toString();
    }

    public DataObject(String _id){
        this._id = _id;
    }
}

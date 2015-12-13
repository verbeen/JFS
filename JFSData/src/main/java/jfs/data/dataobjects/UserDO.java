package jfs.data.dataobjects;

import jfs.data.dataobjects.enums.UserType;

import java.io.Serializable;

/**
 * Created by lpuddu on 29-10-2015.
 */
public class UserDO extends DataObject {
    public String password;
    public UserType type;

    public UserDO(String id, String password, UserType type) {
        super(id);
        this.password = password;
        this.type = type;
    }
}

package jfs.data.dataobjects;

import java.io.Serializable;

/**
 * Created by lpuddu on 29-10-2015.
 */
public class UserDO {
    public String Id;
    public String Email;
    public String Name;

    public UserDO() {}

    public UserDO(String id, String email, String name) {
        Id = id;
        Email = email;
        Name = name;
    }
}

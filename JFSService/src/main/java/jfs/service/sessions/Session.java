package jfs.service.sessions;

import jfs.transferdata.transferobjects.enums.UserTypeDTO;

/**
 * Created by lpuddu on 16-11-2015.
 *
 * Class used in the SessionService
 *
 */
public class Session {
    public String userId;
    public UserTypeDTO type;

    /**
     * Session represents a userId and the UserType
     */
    public Session(String userId, UserTypeDTO type) {
        this.userId = userId;
        this.type = type;
    }
}



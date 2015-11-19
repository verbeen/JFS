package jfs.transferdata.transferobjects;

import jfs.transferdata.transferobjects.enums.UserTypeDTO;

/**
 * Created by Hulk-A on 17.11.2015.
 */
public class UserDTO {
    public String userId;
    public UserTypeDTO type;

    public UserDTO() {}

    public UserDTO(String userId, UserTypeDTO type) {
        this.userId = userId;
        this.type = type;
    }
}

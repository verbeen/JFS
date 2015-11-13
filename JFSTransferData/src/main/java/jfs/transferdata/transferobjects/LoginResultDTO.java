package jfs.transferdata.transferobjects;

import jfs.transferdata.transferobjects.enums.UserTypeDTO;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class LoginResultDTO {
    public Boolean isLoggedIn;
    public UserTypeDTO type;
    public String token;
}

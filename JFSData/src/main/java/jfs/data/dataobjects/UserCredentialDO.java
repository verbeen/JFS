package jfs.data.dataobjects;

/**
 * Created by lpuddu on 2-11-2015.
 */
public class UserCredentialDO {
    public String UserId;
    public String UserSalt;
    public String AccountSalt;
    public String AccountSalted;

    public UserCredentialDO(String userId, String userSalt, String accountSalt, String accountSalted) {
        UserId = userId;
        UserSalt = userSalt;
        AccountSalt = accountSalt;
        AccountSalted = accountSalted;
    }
}

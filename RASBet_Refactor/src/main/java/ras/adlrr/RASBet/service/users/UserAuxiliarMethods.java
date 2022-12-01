package ras.adlrr.RASBet.service.users;

import ras.adlrr.RASBet.model.User;

import java.util.regex.Pattern;

public class UserAuxiliarMethods {

    //* @param currentEmail Current email of the user. Needed to not throw exception when a verification of the attributes is requested when performing an update.
    /**
     * Validates the attributes common to all types of users, the name, the password and email.
     * @param user Object that extends the class User, which contains the attributes referred in the description of the function.
     * @return "null" if all attributes are valid or string mentioning error
     */
    public static String validateUserAttributes(User user){
        //if(!user.getEmail().equals(currentEmail) && existsUserWithEmail(user.getEmail()))
        //    return "Email already used by another user!";
        if(!Pattern.matches("^\\w[\\w ]*$", user.getName()))
            return "A name can only contain alphanumeric characters and spaces. Must also start with an alphanumeric.";
        if(!Pattern.matches("^\\w[\\w.]*@\\w+(?:\\.\\w+)+$", user.getEmail()))
            return "Invalid email format.";
        return null;
    }

}

package  com.Client;

import java.util.regex.*;

public class Regex {

    private static final String password = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$"; 
    private static final String userName = "^[a-zA-Z0-9]([a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
    private static final String email = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String phoneNumber = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
    
    private static final Pattern passwordPattern = Pattern.compile(password);
    private static final Pattern usernamePattern = Pattern.compile(userName);
    private static final Pattern emailPattern = Pattern.compile(email);  
    private static final Pattern phonePattern = Pattern.compile(phoneNumber);


    public static boolean isPassValid(final String string) {
        Matcher matcher = passwordPattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isUsernameValid(final String string) {
        Matcher matcher = usernamePattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isEmailValid(final String string) {
        Matcher matcher = emailPattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isNumberValid(final String string) {
        Matcher matcher = phonePattern.matcher(string);
        return matcher.matches();
    }
}


package ke.co.greencredit.nunua.General;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MyMSI on 9/17/2015.
 */
public class AppConstants {
    //    public String baseURL = "http://freshgreen.co.ke/api/";//deprecated
    public String baseURL = "http://46.101.2.224/";//deprecated
//    public String imageURL = "http://freshgreen.co.ke/images/";
    public String imageURL = "http://46.101.2.224/images/";
    public static final String PREFS_NAME = "MyPrefs";
    private Pattern pattern;
    private Matcher matcher;

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

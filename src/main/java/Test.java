import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xunbai
 * @Date 2023-11-02 23:24
 * @description
 **/
public class Test {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    public static boolean validatePassword(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String password = "aabc123@";
        boolean isValid = validatePassword(password);
        System.out.println("Password is valid: " + isValid);
    }
}

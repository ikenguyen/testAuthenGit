import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by LiemTran on 1/17/16.
 */
public class AppTest {


    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));

    }
}

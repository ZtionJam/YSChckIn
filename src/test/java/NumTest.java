import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NumTest {
    public static void main(String[] args) {
//        Random random=new Random();
//        int num=random.nextInt((2000-1000)+1)+1000;
//        System.out.println(num);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ");
        String format = simpleDateFormat.format(new Date());
        System.out.println(format);
    }
}

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeTest {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 16, 55, 00);//设置执行时间
        Date defaultdate =calendar.getTime();
        if (defaultdate.before(new Date())) {
            // 将发送时间设为明天
            calendar.add(Calendar.DATE, 1);
            defaultdate = calendar.getTime();
        }
        System.out.println(defaultdate);
        Timer dTimer = new Timer();
        Date finalDefaultdate = defaultdate;
        dTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("当前执行时间"+ finalDefaultdate);
            }
        }, defaultdate , 24* 60* 60 * 1000);// 24* 60* 60 * 1000  24小时

    }
}

import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.util.*;

public class MyTimeTest {
    static Logger logger=Logger.getLogger(MyTimeTest.class);
    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("程序正常运行中");
            }
        },1000,1000);
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)
                , 17, 35, 00);
        Date time = calendar.getTime();
        timer.schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                Random random=new Random();
                System.out.println("新线程");
                new Thread(()->{
                    int num = random.nextInt((10000 - 5000) + 1)+5000;
                    try {
                        Thread.sleep(num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("输出"+num);
                }).start();

            }
        }, time, 2000);
    }
}

package cn.ztion;


import cn.ztion.bot.BotMaker;
import cn.ztion.entity.User;
import cn.ztion.net.ApiTakumi;
import cn.ztion.net.Res;
import cn.ztion.tools.ReadFile;
import net.mamoe.mirai.Bot;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class Start {
    static Logger logger = Logger.getLogger(Start.class);

    public static void main(String[] args) {
        began();
    }

    public static void began() {
        ReadFile rf = new ReadFile();
        BotMaker bm = new BotMaker();
        logger.info("程序开始!开始加载cookie数据!!");
        List<User> userList = rf.getQC();
        ApiTakumi.getSignInfo(userList.get(0).getCookie());
//        logger.info("开始登录机器人");
        bm.makeBot();
        ApiTakumi.getRoles(userList);
        ApiTakumi.signIn(userList);
        logger.info("已经开启定时任务,每天将自动签到");
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)
                , 1, 10, 00);
        Date time = calendar.getTime();
        if (time.before(new Date())) {
            calendar.add(Calendar.DATE, 1);
            time = calendar.getTime();
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ");
                String format = simpleDateFormat.format(new Date());
                logger.info("当前时间:" + format + ",到点开始签到啦!");
                new Thread(() -> {
                    Random random = new Random();
                    int interval = random.nextInt((3 * 60 * 60 * 1000 - 1 * 60 * 60 * 1000) + 1) + 1 * 60 * 60 * 1000;
                    try {
                        Thread.sleep(interval);
                        ApiTakumi.signIn(userList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }, time, 24 * 60 * 60 * 1000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                logger.info("程序正常运行中");
            }
        }, 0, 1 * 60 * 60 * 1000);

    }

    public static void oldYS() {
//        logger.info("程序开始执行");
//        ReadFile rf = new ReadFile();
//        List cookies = rf.getCookie();
//        if (cookies != null & cookies.size() > 0) {
////            List roles = ApiTakumi.getRoles(cookies);
////            ApiTakumi.signIn(roles);
//            logger.info("开启定时任务,每天将自动签到");
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(calendar.get(Calendar.YEAR)
//                    , calendar.get(Calendar.MONTH)
//                    , calendar.get(Calendar.DAY_OF_MONTH)
//                    , 1, 10, 00);
//            Date time = calendar.getTime();
//            if (time.before(new Date())) {
//                calendar.add(Calendar.DATE, 1);
//                time = calendar.getTime();
//            }
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ");
//                    String format = simpleDateFormat.format(new Date());
//                    logger.info("当前时间:" + format + ",到点开始签到啦!");
//                    new Thread(() -> {
//                        Random random = new Random();
//                        int interval = random.nextInt((3 * 60 * 60 * 1000 - 1 * 60 * 60 * 1000) + 1) + 1 * 60 * 60 * 1000;
//                        try {
//                            Thread.sleep(interval);
////                            ApiTakumi.signIn(roles);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }).start();
//                }
//            }, time, 24 * 60 * 60 * 1000);
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    logger.info("程序正常运行中");
//                }
//            }, 0, 1 * 60 * 60 * 1000);
//        } else {
//            logger.info("cookie.txt文件里面没有cookie");
//        }
    }
}















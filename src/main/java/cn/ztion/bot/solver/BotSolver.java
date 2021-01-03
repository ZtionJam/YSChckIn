package cn.ztion.bot.solver;

import kotlin.coroutines.Continuation;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.LoginSolver;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.UUID;

public class BotSolver extends LoginSolver {

    private Logger logger = Logger.getLogger(this.getClass());
    private Scanner input = new Scanner(System.in);

    @Nullable
    @Override
    public Object onSolvePicCaptcha(@NotNull Bot bot, @NotNull byte[] bytes, @NotNull Continuation<? super String> continuation) {
        logger.info("需要进行图片验证:");
        String filename = UUID.randomUUID().toString().replace("-", "") + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("cache/"+filename);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            logger.info("写入验证图片错误");
            e.printStackTrace();
        }
        logger.info("请输入验证码,验证码图片在程序目录下cache/img/"+filename);
        String code= input.next();
        if(code.length()>2){
            return code;
        }
        logger.info("请重试");
        return null;
    }

    @Nullable
    @Override
    public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String s, @NotNull Continuation<? super String> continuation) {
        logger.info("滑块验证");
        return null;
    }

    @Nullable
    @Override
    public Object onSolveUnsafeDeviceLoginVerify(@NotNull Bot bot, @NotNull String s, @NotNull Continuation<? super String> continuation) {
        logger.info("不安全设备登录,复制链接到浏览器验证后，输入任意内容回车即可，输入exit退出登录");
        System.out.println(s);
        if ("exit".equals(input.next())) {
            logger.info("程序退出了！");
            System.exit(0);
        }
        logger.info("登陆中......");
        return null;
    }
}

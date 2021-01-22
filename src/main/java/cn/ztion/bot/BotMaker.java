package cn.ztion.bot;

import cn.ztion.bot.solver.BotSolver;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.utils.BotConfiguration;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BotMaker {
    public static Bot bot;
    private Logger logger = Logger.getLogger(this.getClass());

    public Map<String, String> initRes() {
        String path = System.getProperty("user.dir") + File.separator + "config.properties";
        try {
            InputStream is = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(is);
            Map<String, String> map = new HashMap<>();
            if("close".equals(properties.getProperty("qq.sender"))){
                return null;
            }
            map.put("id", properties.getProperty("qq.id"));
            map.put("pass", properties.getProperty("qq.pass"));
            return map;
        } catch (Exception e) {
            logger.info("机器人配置文件读取失败");
            e.printStackTrace();
        }
        return null;
    }

    public Bot makeBot() {
        Map<String, String> map = initRes();
        if (map != null) {
            BotConfiguration bc = new BotConfiguration();
            bc.noNetworkLog();
            bc.noBotLog();
            bc.setLoginSolver(new BotSolver());
            bc.fileBasedDeviceInfo();
            bot = BotFactoryJvm.newBot(Long.parseLong(map.get("id")), map.get("pass"), bc);
            register(bot);
            logger.info("QQ推送已开启，登录中......");
            try {
                bot.login();
            }catch (Exception e){
                logger.info("QQ账号登录失败,跳过登录，原因:"+e.getMessage());
                logger.info("请检查QQ账号配置，Ctrl+C停止程序");
//                System.exit(0);
                return null;
            }
            if (bot.isOnline()) {
                logger.info("账号登录成功:" + map.get("id"));
                logger.info("好友数量:" + bot.getFriends().size());
                logger.info("群聊数量:" + bot.getGroups().size());
                return bot;
            }
        }else {
            logger.info("未启用QQ推送，跳过机器人初始化！");
        }
        return null;
    }

    public void register(Bot bot) {
//        Events.registerEvents(bot,new FriendMessageHandler());
//        Events.registerEvents(bot,new GroupMessageHandler());
    }
}

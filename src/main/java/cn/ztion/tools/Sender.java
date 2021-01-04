package cn.ztion.tools;

import cn.ztion.bot.BotMaker;
import cn.ztion.entity.Result;
import cn.ztion.entity.Reward;
import cn.ztion.entity.User;
import cn.ztion.net.Res;
import lombok.Data;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class Sender {

    private User user;
    private List<Result> results;
    private Bot bot;
    private List<Reward> rewards;
    Logger logger = Logger.getLogger(this.getClass());

    public Sender(User user, List<Result> results) {
        this.bot = BotMaker.bot;
        this.rewards = Res.rewards;
        this.results = results;
        this.user = user;
    }

    public Sender() {

    }

    //发送消息
    public void send() {
        if (bot == null) return;
        bot.getFriends().forEach(friend -> {
            if (friend.getId() == user.getQq()) {
                String avatarUrl = friend.getAvatarUrl();
                try {
                    String file = DownImg.download(avatarUrl);
                    InputStream is = new FileInputStream("cache"+File.separator+"img"+File.separator+ file);
                    Image image = friend.uploadImage(is);
                    MessageChain messages = new MessageChainBuilder()
                            .append(image)
                            .append(format())
                            .build();
                    friend.sendMessage(messages);
                } catch (Exception e) {
                    logger.info("图片加载失败");
                    e.printStackTrace();
                }
            }
        });
    }

    //格式化字符串
    public String format() {
        String message = getFile();
        if (message != null) {
            StringBuffer sb = new StringBuffer();
            String ys = message.substring(message.indexOf("=start") + 7, message.indexOf("=end"));
            results.forEach(result -> {
                sb.append(ys.replace("{nickname}", result.getNickname())
                        .replace("{award}", result.getAward().getName())
                        .replace("{signDay}", result.getSignDay().toString())
                        .replace("{cnt}", result.getAward().getCnt().toString())
                        .replace("{signResult}", result.getSignResult()));
            });
            message = message.replace("=start\n" + ys + "=end", sb.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            message = message.replace("{time}", date);
            return message;
        }
        return null;
    }

    //读取format文件
    public String getFile() {
        String path = System.getProperty("user.dir") + File.separator + "award-format.txt";
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("未找到推送格式化文件文件，并且创建新文件失败，程序退出");
                System.exit(0);
            }
        }
        try {
            InputStream fis = new FileInputStream(file);
            Reader reader = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line= br.readLine()) != null) {
                if (line.length() > 1 && line.indexOf("#") != 0) {
                    sb.append(line + "\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("cookie加载失败，程序退出");
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}














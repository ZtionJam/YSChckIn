package cn.ztion.net;

import cn.ztion.entity.GameRole;
import cn.ztion.entity.Result;
import cn.ztion.entity.Reward;
import cn.ztion.entity.User;
import cn.ztion.tools.Sender;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ApiTakumi {
    static Logger logger = Logger.getLogger(ApiTakumi.class);
    static OkHttpClient client = new OkHttpClient();

    public static Integer getSignDay(GameRole role) {
        String url = Res.signDay;
        url = url.replace("{region}", role.getRegion())
                .replace("{uid}", role.getGameUid().toString())
                .replace("{act_id}", Res.act_id);
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(role.getCookie()))
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                if ("OK".equals(response.message())) {
                    JSONObject jo = JSONObject.parseObject(response.body().string()).getJSONObject("data");
                    Integer day = jo.getInteger("total_sign_day");
                    return day;
                }
            }
        } catch (Exception e) {
            logger.info("获取签到信息失败");
        }
        return null;
    }

    public static void getSignInfo(String cookie) {
        logger.info("获取签到奖励列表");
        Request request = new Request.Builder()
                .url(Res.signInfo)
                .headers(getHeaders(cookie))
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                if ("OK".equals(response.message())) {
                    JSONObject jo = JSONObject.parseObject(response.body().string());
                    JSONArray jsonArray = jo.getJSONObject("data").getJSONArray("awards");
                    jsonArray.forEach(object -> {
                        JSONObject award = JSONObject.parseObject(object.toString());
                        Reward reward = new Reward()
                                .setName(award.getString("name"))
                                .setIcon(award.getString("icon"))
                                .setCnt(award.getInteger("cnt"));
                        Res.rewards.add(reward);
                    });
                }
            }
        } catch (Exception e) {
            logger.info("获取签到奖励列表失败");
        }
    }

    public static void signIn(List<User> users) {
        logger.info("开始签到");
        AtomicInteger num = new AtomicInteger();
        users.forEach(user -> {
            List<Result> list = new ArrayList<>();
            user.getRole().forEach(role -> {
                logger.info("-----------------------");
                logger.info("开始签到角色:" + role.getNickname());
                JSONObject data = new JSONObject();
                data.put("act_id", Res.act_id);
                data.put("region", role.getRegion());
                data.put("uid", role.getGameUid());
                Request request = new Request.Builder()
                        .url(Res.signUrl)
                        .post(RequestBody.create(data.toJSONString(), Res.JSON_TYPE))
                        .headers(getHeaders(role.getCookie()))
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        JSONObject jo = JSONObject.parseObject(response.body().string());
//                        System.out.println(user);
                        logger.info(role.getNickname() + " 签到结果: "
                                + (("OK".equals(jo.getString("message"))) ? "签到成功" : jo.getString("message")));
                        Result result = new Result();
                        result.setNickname(role.getNickname())
                                .setAward(Res.rewards.get(role.getSignDay() - 1))
                                .setSignDay(role.getSignDay())
                                .setSignResult((("OK".equals(jo.getString("message"))) ? "签到成功" : jo.getString("message")));
                        num.getAndIncrement();
                        list.add(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //QQ消息推送
            Sender sender = new Sender(user, list);
            sender.send();
        });
    }

    public static void getRoles(List<User> users) {
        logger.info("开始加载游戏角色");
        AtomicInteger num=new AtomicInteger();
        users.forEach(item -> {
            List<GameRole> roles = new ArrayList<>();
            Request request = new Request.Builder()
                    .url(Res.gameRole)
                    .headers(getHeaders(item.getCookie()))
                    .build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    JSONObject ret = JSONObject.parseObject(response.body().string());
                    if ("OK".equals(ret.getString("message"))) {
                        JSONArray jo = ret.getJSONObject("data")
                                .getJSONArray("list");
                        jo.forEach(roleStr -> {
                            JSONObject roleJson = (JSONObject) roleStr;
                            GameRole role = new GameRole();
                            role.setCookie(item.getCookie()).setNickname(roleJson.getString("nickname"))
                                    .setGame_biz(roleJson.getString("game_biz"))
                                    .setLevel(roleJson.getInteger("level"))
                                    .setGameUid(roleJson.getLong("game_uid"))
                                    .setRegion_name(roleJson.getString("region_name"))
                                    .setRegion(roleJson.getString("region"));
                            logger.info("加载到角色:'" + roleJson.getString("nickname") + "',UID:" + roleJson.getLong("game_uid"));
                            role.setSignDay(getSignDay(role));
                            roles.add(role);
                            num.getAndIncrement();
                        });
                    } else {
                        logger.info("游戏角色加载失败");
                    }

                }
            } catch (IOException e) {
                logger.info("游戏角色加载错误!");
                e.printStackTrace();
            }
            item.setRole(roles);
        });

        if (num.get()>0) {
            logger.info("共加载到:" + num + "个游戏角色");
        } else {
            logger.error("一个游戏角色也没有加载到，请检查cookie,程序退出");
            System.exit(0);
        }
    }

    //包装请求头
    public static Headers getHeaders(String cookie) {
        Headers headers = new Headers.Builder()
                .add("x-rpc-device_id", Res.device_id)
                .add("Host", "api-takumi.mihoyo.com")
                .add("Content-type", "application/json;charset=utf-8")
                .add("Accept", "application/json, text/plain, */*")
                .add("x-rpc-client_type", "5")
                .add("x-rpc-app_version", Res.app_version)
                .add("Cookie", cookie)
                .add("DS", getDS())
                .add("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) miHoYoBBS/2.2.0")
                .build();
        return headers;
    }

    //生成DS
    public static String getDS() {
        Random random = new Random();
        int nextInt = random.nextInt(15);
        String a = "h8w582wxwgqvahcdkpvdhbh2w9casgfl";
        String b = new Date().getTime() + "";
        b = b.substring(0, 10);
        String c = UUID.randomUUID().toString().replace("-", "").substring(nextInt, nextInt + 6);
        String d = DigestUtils.md5Hex("salt=" + a + "&t=" + b + "&r=" + c);
        String ret = b + "," + c + "," + d + ",";
        return ret;
    }
}

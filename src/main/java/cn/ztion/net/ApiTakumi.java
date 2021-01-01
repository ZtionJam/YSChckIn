package cn.ztion.net;

import cn.ztion.entity.GameRole;
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

    public static void signIn(List<GameRole> roles) {
        logger.info("开始签到");
        AtomicInteger num = new AtomicInteger();
        roles.forEach(role -> {
            logger.info("-----------------------");
            logger.info("开始签到角色:" + role.getNickname());
            JSONObject data = new JSONObject();
            data.put("act_id", Res.act_id);
            data.put("region", Res.region);
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
                    String ok = "{\"retcode\":0,\"message\":\"OK\",\"data\":{\"code\":\"ok\"}}";
                    JSONObject jo = JSONObject.parseObject(response.body().string());
                    logger.info(role.getNickname() + " 签到结果: "
                            + (("OK".equals(jo.getString("message"))) ? "签到成功" : jo.getString("message")));
                    num.getAndIncrement();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        logger.info("-----------------------");
        logger.info("总共签到数量:" + roles.size() + ",成功返回数量:" + num);
    }

    public static List<GameRole> getRoles(List<String> cookies) {
        logger.info("开始加载游戏角色");
        List<GameRole> roles = new ArrayList<>();
        cookies.forEach(item -> {
            Request request = new Request.Builder()
                    .url(Res.gameRole)
                    .headers(getHeaders(item))
                    .build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    JSONObject ret=JSONObject.parseObject(response.body().string());
                    if("OK".equals(ret.getString("message"))){
                        JSONObject jo = ret.getJSONObject("data")
                                .getJSONArray("list")
                                .getJSONObject(0);
                        GameRole role = new GameRole();
                        role.setCookie(item).setNickname(jo.getString("nickname"))
                                .setGame_biz(jo.getString("game_biz"))
                                .setLevel(jo.getInteger("level"))
                                .setGameUid(jo.getLong("game_uid"))
                                .setRegion_name(jo.getString("region_name"))
                                .setRegion(jo.getString("region"));
                        logger.info("加载到角色:'" + jo.getString("nickname") + "',UID:" + jo.getLong("game_uid"));
                        roles.add(role);
                    }else{
                        logger.info("游戏角色加载失败");
                    }

                }
            } catch (IOException e) {
                logger.info("游戏角色加载错误!");
                e.printStackTrace();
            }
        });
        if (roles.size() > 0) {
            logger.info("共加载到:" + roles.size() + "个游戏角色");
            return roles;
        } else {
            logger.error("一个游戏角色也没有加载到，请检查cookie,程序退出");
            System.exit(0);
            return null;
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

package cn.ztion.net;

import okhttp3.MediaType;

public class Res {
    //签到
    final static String signUrl = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign";
    //签到信息
    final static String signInfo = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/home?act_id={}";
    //签到天数
    final static String signDay = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/info?region={}&act_id={}&uid={}";
    //游戏信息
    final static String gameRole = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn";

    //设备id
    final static String device_id = "94588081EDD446EFAA3A45B8CC436CCF";
    //软件版本
    final static String app_version = "2.1.0";
    //服务器
    final static String region = "cn_gf01";
    //??
    final static String act_id = "e202009291139501";
    //mamitype
    final static MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

}

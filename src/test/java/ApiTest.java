import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;

public class ApiTest {
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    static String device_id="94588081EDD446EFAA3A45B8CC436CCF";
    static String app_version="2.1.0";
    //    static String Cookie="UM_distinctid=1751ca1f04d196-0ecfdf73e9001b-4b524b5f-1fa400-1751ca1f04e1d2; mi18nLang=zh-cn; _ga_KJ6J9V9VZQ=GS1.1.1608877945.1.1.1608878649.0; CNZZDATA1275023096=1288160218-1602497824-%7C1608877797; _ga=GA1.2.166112445.1602503180; _gid=GA1.2.557170292.1608880901; login_uid=163249873; login_ticket=KBT5RONbEyNulypQozGgcPflFUJ8iBTic0FbU5r3; account_id=163249873; cookie_token=WyHHJwOcaot8OtEsEIShILVFsPsrq6s5PWfqNCyw; ltoken=w51JgrkAKhqPa10IXXzvrb2oq56VBTPoREr9vD5A; ltuid=163249873";
    static String Cookie="_MHYUUID=8b8c9ad7-100b-42b4-8ca1-a4a0d454f0ea; UM_distinctid=17587ed32d314e-0e7f07d8a11e5e8-4c3f257b-144000-17587ed32d41db; CNZZDATA1275023096=498449084-1606473825-https%253A%252F%252Fys.mihoyo.com%252F%7C1608877797; _ga=GA1.2.1083731160.1606476795; _gid=GA1.2.1432359390.1608882079; login_uid=246727503; login_ticket=waFJLIjQqdLiUOaDDWzmH84EnL5qRZfUGfdADCT1; account_id=246727503; cookie_token=tdg8mVOvX6vFSk8aEjAHZBecZUWTpf7rmEwoxPTw; ltoken=Bcr7cY16ZrT44yLC1HotC1ZYYovIUKHqOpRGYaTN; ltuid=246727503; _gat=1";
    static String region="cn_gf01";
    static String act_id="e202009291139501";
    public static void main(String[] args) throws Exception{
        System.out.println("开始请求");
//            String url = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/home?act_id=e202009291139501";
        String url="https://api-takumi.mihoyo.com/event/bbs_sign_reward/info?region="+region+"&act_id="+act_id+"&uid=145602663";//签到信息
//        String url="https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign";//签到
//        String url = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn";//游戏信息
        OkHttpClient client=new OkHttpClient();
        JSONObject json=new JSONObject();
        json.put("act_id","e202009291139501");
        json.put("region","cn_gf01");
        json.put("uid","145602663");
        RequestBody requestBody=RequestBody.create(json.toJSONString(),JSON_TYPE);
        Request request=new Request.Builder()
                .url(url)
//                .post(requestBody)
                .headers(getHeaders())
                .build();
        Call call=client.newCall(request);
        Response response = call.execute();
        if(response.isSuccessful()){
            System.out.println(response.body().string());
        }
    }

    public static Headers getHeaders(){
        Headers headers=new Headers.Builder()
                .add("x-rpc-device_id",device_id)
                .add("Host","api-takumi.mihoyo.com")
                .add("Content-type","application/json;charset=utf-8")
                .add("Accept","application/json, text/plain, */*")
                .add("x-rpc-client_type","5")
                .add("x-rpc-app_version",app_version)
                .add("Cookie",Cookie)
                .add("DS",getDS())
                .add("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) miHoYoBBS/2.2.0")
                .build();
        return headers;
    }

    public static String getDS(){
        Random random=new Random();
        int nextInt = random.nextInt(15);
        String a= DigestUtils.md5Hex(app_version);
        String b=new Date().getTime()+"";
        b=b.substring(0,10);
        String c= UUID.randomUUID().toString().replace("-","").substring(nextInt, nextInt + 6);
//        c = md5("salt=" + n + "&t=" + i + "&r=" + r)
        String d=DigestUtils.md5Hex("salt="+a+"&t="+b+"&r="+c);
        String ret=b+","+c+","+d+",";
        return ret;
    }
}

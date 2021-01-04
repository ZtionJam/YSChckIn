package cn.ztion.tools;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class DownImg {
    static Logger logger = Logger.getLogger(DownImg.class);

    public static String download(String urlStr) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(urlStr).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        InputStream is = null;
        if (response.isSuccessful()) {
            is = response.body().byteStream();
        } else {
            logger.info("图片下载请求失败了");
            return null;
        }
        byte[] bs = new byte[1024];
        int len;
        String file = UUID.randomUUID().toString().replace("-", "") + ".jpg";
        OutputStream os = new FileOutputStream("cache"+File.separator+"img"+File.separator+ file);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        is.close();
        return file;
    }
}

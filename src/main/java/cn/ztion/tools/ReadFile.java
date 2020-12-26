package cn.ztion.tools;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    Logger logger=Logger.getLogger(this.getClass());
    public List getCookie(){
        String path = System.getProperty("user.dir")+ File.separator+"cookie.txt";
        File file=new File(path);
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                logger.error("未找到cookie文件，并且创建新文件失败，程序退出");
                System.exit(0);
            }
        }
        logger.info("加载cookie文件...");
        try{
            InputStream fis=new FileInputStream(file);
            Reader reader = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(reader);
            List<String> list=new ArrayList<String>();
            String line="";
            while ((line = br.readLine()) != null) {
                if(line.length()>1&&line.indexOf("#")!=0){
                    list.add(line);
//                    logger.info("获取到cookie:"+line);
                }
            }
            logger.info("加载到"+list.size()+"条cookie数据");
            return list;
        }catch (Exception e){
            logger.error("cookie加载失败，程序退出");
            System.exit(0);
        }
        return null;
    }
}

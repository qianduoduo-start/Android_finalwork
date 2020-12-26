package com.example.finalwork.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousFileChannel;

/*
/读取Assets文件夹下的工具类
*/
public class AssetsUtils {
    public static String getAssetsContent(Context context, String filename){
        //获取assets文件夹管理者对象
        AssetManager manager = context.getResources().getAssets();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = manager.open(filename);
            int hasRead = 0;
            byte[]buf = new byte[1024];
            while (true){
                hasRead = is.read(buf);
                if (hasRead == -1){
                    break;
                }else{
                    baos.write(buf,0,hasRead);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return baos.toString();
    }
}

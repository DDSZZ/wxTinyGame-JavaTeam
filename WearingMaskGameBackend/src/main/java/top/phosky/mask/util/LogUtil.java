package top.phosky.mask.util;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * 程序日志工具【还没有写数据量太大更换存储文件】
 *
 * @author Phosky, NEU
 * @version 0.4
 */
public class LogUtil {
    public static int LIMIT_SIZE = 1024 * 1024;//每个日志文件的大小
    private static LogUtil singleton;

    private LogUtil() {
    }

    //单例模式
    public static LogUtil getSingleton() {
        if (singleton == null) {
            singleton = new LogUtil();
        }
        return singleton;
    }

    public void log(String path, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(path, true);
            String formattedContent = format(content);
            fos.write(formattedContent.getBytes("UTF-8"));
            fos.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public String format(String content) {
        StringBuilder sb = new StringBuilder();
        String dateNow = DateUtil.getSingleton().getInstanceDate(DateUtil.SDF_yyyyMMddHHmmssSSS_0);
        sb.append('[').append(dateNow).append("]: ");
        sb.append(content);
        sb.append("\r\n");
        return sb.toString();
    }
}

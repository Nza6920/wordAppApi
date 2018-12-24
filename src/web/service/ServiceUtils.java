package web.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.json.JSONObject;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;

// 工具类
public class ServiceUtils {

    static SqlSession session = null;

    // 从 Stream 中读取数据直到读完
    public static String readAsText(InputStream inStream, String charset, int maxSize) throws IOException
    {
        ByteArrayOutputStream cache = new ByteArrayOutputStream(1024 * 16);
        byte[] data = new byte[1024];

        int numOfWait = 0;
        while (true)
        {
            int n = inStream.read(data); // n: 实际读取的字节数
            if (n < 0)
                break; // 连接已经断开
            if (n == 0)
            {
                if (numOfWait++ >= 3)
                    break; // 此种情况不得连续3次
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                }
                continue;// 数据未完
            }
            numOfWait = 0;

            // 缓存起来
            cache.write(data, 0, n);
            if (cache.size() > maxSize) // 上限, 最多读取512K
                break;
        }

        return cache.toString(charset);
    }

    // 解析 Query 字符串
    public static HashMap<String, String> parseQuery(String query, String charset)
    {
        HashMap<String, String> params = new HashMap<String, String>();
        if (query == null)
            return params; // 为空

        String[] ppp = query.split("&"); // 用&分隔
        for (String p : ppp)
        {
            String[] kv = p.split("="); // key=value
            String key = kv[0];
            String value = "";
            if (key.length() > 1)
                value = kv[1]; // 有时候参数里传的是空值
            if (value.indexOf('%') >= 0)
            {
                // 如果存在百分号, 则进行URL解码
                try
                {
                    value = URLDecoder.decode(value, charset);
                } catch (Exception e)
                {
                }
            }
            params.put(key, value);
        }
        return params;
    }

    // 获取数据库连接(用完需要关闭) 自动提交
    public static Object getSqlSession(Class daoClass) throws Exception {
        String resource = readConfig("myBatisResource");
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        session = sqlMapper.openSession(true);

        Object dao = session.getMapper(daoClass);

        return dao;
    }

    // 获取数据库连接(用完需要关闭) autoCommit = true 自动提交 autoCommit = false 非自动提交
    public static Object getSqlSession(Class daoClass, boolean autoCommit) throws Exception {
        String resource = readConfig("myBatisResource");
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

        session = sqlMapper.openSession(autoCommit);

        Object dao = session.getMapper(daoClass);

        return dao;
    }

    // 提交数据库事务
    public static void commitSqlSession() {
        session.commit();
    }

    // 回滚数据库事务
    public static void rollbackSqlSession() {
        session.rollback();
    }

    // 关闭数据库连接(用完需要关闭)
    public static void closeSqlSession() {
        try {
            session.close();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            session = null;
        }
    }

    // 读取配置文件
    public static String readConfig(String key) throws IOException {

        Properties properties = new Properties();
        String url = ServiceUtils.class.getResource("/systemConfig.properties").getPath();
        BufferedReader reader = new BufferedReader(new FileReader(url));
        properties.load(reader);
        String con = properties.getProperty(key);

        return con;
    }

    // MD5 加密
    public static String md5(String text) throws Exception {
        // 读取 key 值
        String key = readConfig("md5Key");
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text + key);
//        Option.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }

    // MD5 验证
    public static boolean md5Verify(String text,String md5) throws Exception {
        // 根据传入的密钥进行验证
        String md5Text = md5(text);

        if(md5Text.equalsIgnoreCase(md5))
        {
            return true;
        }
        return false;
    }


    // 验证请求数据格式
    public static boolean jsonVerify(JSONObject jsonObject, String[] verify)
    {
        for (String t : verify) {
            if (jsonObject.isNull(t)) return false;
        }
        return true;
    }
}

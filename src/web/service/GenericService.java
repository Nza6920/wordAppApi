package web.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

public class GenericService extends HttpServlet {

    protected boolean enableErrorLog = false;       // 是否打印异常输出
    protected int MAX_REQUEST_SIZE = 1024 * 512;    // 允许上传的JSON最大长度
    protected HashMap<String, ConfigItem> configs = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {
            loadConfig();       // 加载配置文件
        } catch (Exception e) {
            e.printStackTrace();

            throw new Error("service_config.xml 格式不正确!");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleRequest(request, response);
        } catch (Exception e) {
            if(enableErrorLog) e.printStackTrace();
            response.sendError(500, e.getMessage());
            return;
        }
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 从 URL 中解析 API 的名字
        // servletPath: "/.../hello.api"
        String servletPath = request.getServletPath();
        int p1 = servletPath.lastIndexOf("/");
        int p2 = servletPath.lastIndexOf(".");
        String apiName = servletPath.substring(p1 + 1, p2);
        System.out.println("请求服务名: " + apiName);

        // 查找相关的配置
        ConfigItem cfg = configs.get(apiName);

        if(cfg == null)
            throw new Exception("服务" + apiName + "在service_config.xml里没有配置!");

        if(cfg.clazz == null)
        {
            try{
                cfg.clazz = Class.forName(cfg.className);
            }catch(Exception e)	{
                throw new Exception("找不到服务" + apiName + "的类" + cfg.className );
            }
        }

        // 创建服务类的对象, 处理该请求
        GenericApi instance = null;

        try{
            instance = (GenericApi) cfg.clazz.newInstance();
        }catch(InstantiationException e){
            e.printStackTrace();
            throw new Exception(cfg.className + "无法实例化, 请确保构造方法不带参数!");
        }catch(IllegalAccessException e){
            e.printStackTrace();
            throw new Exception(cfg.className + "无法实例化, 请确保构造方法为public!");
        }catch(ClassCastException e){
            e.printStackTrace();
            throw new Exception(cfg.className + "必须是 GenericApi 的子类(或子类的子类)!");
        }catch(Exception e)	{
            e.printStackTrace();
            throw new Exception("在创建 " + cfg.className + "实例的时候出错!请检查构造方法是否有异常!");
        }

        // 读取请求数据 和 URL 里的参数
        String charset = "UTF-8";

        String strReq = (request.getAttribute("strReq") == null) ?
                        ServiceUtils.readAsText(request.getInputStream(), charset, MAX_REQUEST_SIZE) :
                        ServiceUtils.readAsText((InputStream) request.getAttribute("strReq"), charset, MAX_REQUEST_SIZE);

        String query = request.getQueryString();                       // 获取查询字符串
        HashMap<String , String> queryParams = ServiceUtils.parseQuery(query, charset);

        // 执行具体业务逻辑
        instance.httpReq = request;
        instance.httpResp = response;
        instance.queryParams = queryParams;
        instance.charset = charset;

        String strResp = instance.execute(strReq);

        // 发送应答给客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        Writer out = response.getWriter();
        out.write(strResp);
        out.close();
    }


    // 配置类
    class ConfigItem
    {
        public String name;  // 名称
        public String className; // 类名
        public Class clazz;      // 类的实体
        public String charset = "UTF-8";   // 字符集

        public ConfigItem(String name, String className)
        {
            this.name = name;
            this.className = className;
        }
    }

    // 读取配置文件
    private void loadConfig() throws DocumentException, IOException {
        InputStream stream = this.getClass().getResourceAsStream("/service_config.xml");
        SAXReader reader = new SAXReader();
        Document doc = reader.read(stream);
        stream.close();

        Element root = doc.getRootElement();
        List<Element> XServiceList = root.elements("service");
        for (Element e: XServiceList)
        {
            String name = e.attributeValue("name");
            String className = e.attributeValue("class");
            configs.put(name, new ConfigItem(name, className));
        }
    }

}

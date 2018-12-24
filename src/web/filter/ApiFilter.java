package web.filter;

import org.json.JSONObject;
import web.service.ServiceUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Element;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

// api 过滤器
public class ApiFilter implements javax.servlet.Filter {

    public ArrayList<String> ignored = new ArrayList();  // 白名单
    public String charset = "UTF-8";                     // 字符集
    public int MAX_REQUEST_SIZE = 1024 * 512;            // 最大json上传数

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 判断是否为白名单
        for (String t: this.ignored) {
            if (request.getServletPath().equals(t)) {
                System.out.println("通过");
                // 白名单 api 直接放行
                chain.doFilter(req, resp);
            }
        }

        InputStream in = request.getInputStream();

        // 由于InputStream只能读取一次, 需要复制一份
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) > -1) {
            baos.write(buffer,0, len);
        }
        baos.flush();

        // 复制品A
        InputStream inA = new ByteArrayInputStream(baos.toByteArray());

        // 复制品B
        InputStream inB = new ByteArrayInputStream(baos.toByteArray());

        // 读取一份复制的inputStream
        String strReq = ServiceUtils.readAsText(inA, charset, MAX_REQUEST_SIZE);
        JSONObject reqJson = new JSONObject(strReq);

        // 将第二份复制品通过 req 带入 Servlet
        req.setAttribute("strReq", inB);

        // 判断请求格式是否正确
        if(!ServiceUtils.jsonVerify(reqJson, new String[]{"username", "userId", "apiKey", "updated_at"})) {
            // 请求格式不正确
            response.setContentType("text/plain");
            response.setCharacterEncoding(charset);
            response.setStatus(403);
            response.getWriter().write(new JSONObject().put("message", "请求格式不正确!").toString());
        }


        String userName = reqJson.getString("username");
        int userId = reqJson.getInt("userId");
        String apiKay = reqJson.getString("apiKey");
        String updated_at = reqJson.getString("updated_at");

        try {
            // 验证 appKey 是否合法
            if (ServiceUtils.md5Verify(userId + userName + updated_at, apiKay)) {
                chain.doFilter(req,resp);
            }

            // 签名不合法的逻辑
            JSONObject jresp = new JSONObject();
            jresp.put("error", 1);
            jresp.put("message", "非法签名!");
            response.setContentType("text/plain");
            response.setCharacterEncoding(charset);
            response.setStatus(403);
            response.getWriter().write(jresp.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        Enumeration elements = config.getInitParameterNames();

        for (int i = 0 ; elements.hasMoreElements(); i++) {
            String name = (String) elements.nextElement();
            this.ignored.add(config.getInitParameter(name));
        }
    }
}

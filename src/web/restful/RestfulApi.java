package web.restful;

import org.json.JSONObject;
import web.service.GenericApi;

public abstract class RestfulApi extends GenericApi {

    protected boolean enableErrorLog = true;    // 是否打印异常输出
    protected boolean niceJSONFormat = true;    // 输出的JSON是否缩进(影响运行效率)

    public abstract Object execute(JSONObject jreq) throws Exception;

    @Override
    public String execute(String strReq) throws Exception {

        // 处理请求数据
        JSONObject jresp = new JSONObject();

        try {
            JSONObject jreq = null;
            if (strReq.length() > 0)
            {
                jreq = new JSONObject(strReq);
            }

            Object data = execute(jreq);

            jresp.put("error", 0);       // 错误码, 0表示成功
            jresp.put("reason", "OK");   // 错误原因描述, 如果没有错误则提示OK
            if (data != null){
                jresp.put("data", data);
            }
        } catch (RestfulException e){
            jresp.put("error", e.error);
            jresp.put("reason", e.getMessage());
            if (enableErrorLog) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            jresp.put("error", -1);
            jresp.put("reason",e.getMessage());

            if (enableErrorLog){
                e.printStackTrace();
            }
        }

        // 返回给客户端
        if (niceJSONFormat) {
            return jresp.toString(2);
        } else{
            return jresp.toString();
        }
    }
}

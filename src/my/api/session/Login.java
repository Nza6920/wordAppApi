package my.api.session;

import my.bean.User;
import my.dao.UserDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

/**
 * 用户登录接口
 * 接收参数: username, password
 */
public class Login extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {
        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"username","password"})) {
            this.httpResp.setStatus(403);   // 服务器拒绝了请求
            throw new RestfulException(1,"请求参数不正确!");
        }

        User user = null;                                   // 用户
        JSONObject resultData  = new JSONObject();          // 返回数据
        String username = jreq.getString("username");   // 用户名
        String password = jreq.getString("password");   // 密码

        try {
            // 打开数据库句柄
            UserDao userDao = (UserDao) ServiceUtils.getSqlSession(UserDao.class);
            user = userDao.getUserByName(username);

            // 如果用户不存在 or 密码错误, 直接返回
            if (user == null) {
                resultData.put("message","用户名不存在, 请先注册.");
                this.httpResp.setStatus(401);
                return resultData;
            } else if(! ServiceUtils.md5Verify(password, user.getPassword())) {
                resultData.put("message", "密码错误, 请重新输入.");
                this.httpResp.setStatus(401);
                return resultData;
            }
        } finally {
            // 关闭数据库句柄
            ServiceUtils.closeSqlSession();
        }

        // 给当前用户设置api请求秘钥
        String apiKey = ServiceUtils.md5(user.getId() + user.getUsername() + user.getUpdated_at());
        // 给客户端返回当前登陆用户
        resultData.put("userId", user.getId());
        resultData.put("username", user.getUsername());
        resultData.put("updated_at", user.getUpdated_at());
        resultData.put("apiKey", apiKey);

        return resultData;
    }

}

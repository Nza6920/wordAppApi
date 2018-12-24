package my.api.options;

import my.dao.UserDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.Date;

public class SetPassword extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"password"})) {
            this.httpResp.setStatus(403);   // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        String password = jreq.getString("password");
        int userId = jreq.getInt("userId");
        try {
            UserDao userDao = (UserDao) ServiceUtils.getSqlSession(UserDao.class, false);

            // 更新用户密码
            userDao.updatePassword(userId, ServiceUtils.md5(password));
            userDao.setUpdatedAt(userId, new Date());

            // 提交数据库事务
            ServiceUtils.commitSqlSession();
        } catch (Exception e) {

            // 出错, 回滚数据库操作
            ServiceUtils.rollbackSqlSession();
        }finally {
            ServiceUtils.closeSqlSession();
        }

        this.httpResp.setStatus(201);
        return null;
    }
}

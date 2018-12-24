package my.api.session;

import my.bean.Option;
import my.bean.Review;
import my.bean.Unskilled;
import my.bean.User;
import my.dao.OptionDao;
import my.dao.ReviewDao;
import my.dao.UnskilledDao;
import my.dao.UserDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.Date;

/**
 * 用户注册接口
 * 接收参数: username, password
 */
public class Register extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"username","password"})) {
            this.httpResp.setStatus(403);   // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONObject resultData = new JSONObject();
        String username = jreq.getString("username");
        String password = jreq.getString("password");
        User user = null;
        try {
            // 打开数据库连接
            UserDao userDao = (UserDao) ServiceUtils.getSqlSession(UserDao.class, false);
            user = userDao.getUserByName(username);

            // 检查用户名是否重复
            if (user != null) {
                resultData.put("message", "用户名已存在.");
                this.httpResp.setStatus(403);
                return resultData;
            }
            // 创建一个用户
            Date now = new Date();
            user = new User(null, username, ServiceUtils.md5(password), now, now);
            // 向数据库添加一个用户
            userDao.addUser(user);     // 会返回创建的主键

            ServiceUtils.commitSqlSession();
        } catch (Exception e) {
            ServiceUtils.rollbackSqlSession();
            resultData.put("message", e.getMessage());
            return resultData;
        }finally{
            // 关闭数据库连接
            ServiceUtils.closeSqlSession();
        }

        // 创建复习本
        try {
            ReviewDao reviewDao = (ReviewDao) ServiceUtils.getSqlSession(ReviewDao.class, false);
            Review review = new Review(user.getId(), user.getCreated_at());

            reviewDao.addReview(review);
            ServiceUtils.commitSqlSession();
        }catch (Exception e) {
            ServiceUtils.rollbackSqlSession();
            resultData.put("message", e.getMessage());
            return resultData;
        }finally {
            ServiceUtils.closeSqlSession();
        }

        // 创建生词本
        try {
            UnskilledDao unskilledDao = (UnskilledDao) ServiceUtils.getSqlSession(UnskilledDao.class, false);
            Unskilled unskilled = new Unskilled(user.getId(), user.getCreated_at());

            unskilledDao.addUnskill(unskilled);
            ServiceUtils.commitSqlSession();
        }catch (Exception e) {
            ServiceUtils.rollbackSqlSession();
            resultData.put("message", e.getMessage());
            return resultData;
        }finally {
            ServiceUtils.closeSqlSession();
        }

        // 创建系统设置
        try {
            OptionDao optionDao = (OptionDao) ServiceUtils.getSqlSession(OptionDao.class, false);
            Option option = new Option(user.getId(), 1, 10, 20, null);

            optionDao.addOption(option);
            ServiceUtils.commitSqlSession();
        }catch (Exception e) {
            ServiceUtils.rollbackSqlSession();
            resultData.put("message", e.getMessage());
            return resultData;
        }finally {
            ServiceUtils.closeSqlSession();
        }

        this.httpResp.setStatus(201);   // 新的资源被创建
        return null;
    }
}

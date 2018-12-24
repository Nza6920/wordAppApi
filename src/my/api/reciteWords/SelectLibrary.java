package my.api.reciteWords;

import my.bean.LibraryAndUser;
import my.dao.UserDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.Date;

// 收藏词库
public class SelectLibrary extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"libraryId"})) {
            this.httpResp.setStatus(403);   // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONObject resultData = new JSONObject();

        try {
            UserDao userDao = (UserDao) ServiceUtils.getSqlSession(UserDao.class);
            LibraryAndUser libraryAndUser = userDao.selectLibrartById(jreq.getInt("userId"), jreq.getInt("libraryId"));

            // 判断是否已收藏
            if (libraryAndUser != null) {
                this.httpResp.setStatus(403);
                throw new RestfulException(2, "已收藏, 不要重复收藏.");
            }

            userDao.selectLibrary(jreq.getInt("userId"),jreq.getInt("libraryId"), new Date());
        } finally {
            ServiceUtils.closeSqlSession();
        }

        return null;
    }
}

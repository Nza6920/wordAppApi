package my.api.reciteWords;

import my.bean.LibraryAndUser;
import my.bean.Option;
import my.dao.UserDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

// 删除指定收藏词库
public class DeleteSelectLibrary extends RestfulApi {

    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"libraryId"})) {
            this.httpResp.setStatus(403);   // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        try {
            UserDao userDao = (UserDao) ServiceUtils.getSqlSession(UserDao.class);
            LibraryAndUser libraryAndUser = userDao.selectLibrartById(jreq.getInt("userId"), jreq.getInt("libraryId"));
            if (libraryAndUser == null) {
                this.httpResp.setStatus(403);
                throw new RestfulException(2, "该用户没有收藏该词库");
            }

            userDao.deleteSelectLibraryById(jreq.getInt("userId"), jreq.getInt("libraryId"));

        } finally {
            ServiceUtils.closeSqlSession();
        }

        return null;
    }
}

package my.api.reciteWords;

import my.bean.Library;
import my.bean.LibraryAndUser;
import my.dao.LibraryDao;
import my.dao.UserDao;
import org.json.JSONArray;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

// 获得已收藏的所有词库
public class GetSelectLibrary extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        JSONObject resultData = new JSONObject();
        List<LibraryAndUser> stars;

        try {
            UserDao userDao = (UserDao) ServiceUtils.getSqlSession(UserDao.class);
            stars = userDao.getSelectLibrary(jreq.getInt("userId"));
        } finally {
            ServiceUtils.closeSqlSession();
        }

        // 判断用户是否有收藏
        if (stars == null || stars.size() <= 0) {
            this.httpResp.setStatus(404);
            throw new RestfulException(2, "该用户还没有收藏词库");
        }

        ArrayList<Library> libraries = new ArrayList<>();
        try {
            LibraryDao libraryDao = (LibraryDao) ServiceUtils.getSqlSession(LibraryDao.class);
            for (int i = 0 ; i < stars.size(); i++) {
                libraries.add(libraryDao.getById(stars.get(i).getLibrary_id()));
            }
            return new JSONArray(libraries);
        } finally {
            ServiceUtils.closeSqlSession();
        }
    }
}

package my.api.reciteWords;

import my.bean.Library;
import my.dao.LibraryDao;
import org.json.JSONArray;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.service.ServiceUtils;
import java.util.List;

// 得到系统所有词库
public class GetLibrary extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {
        JSONArray resultArray = null;
        List<Library> libraryList = null;

        try {
            LibraryDao libraryDao = (LibraryDao) ServiceUtils.getSqlSession(LibraryDao.class);
            libraryList = libraryDao.getAll();
            resultArray = new JSONArray(libraryList);
        } finally {
            ServiceUtils.closeSqlSession();
        }

        // 一个词都没有
        if (resultArray.length() == 0) {
            JSONObject resultData = new JSONObject();
            resultData.put("message", "系统中还没有词库");
            return resultData;
        }
        return resultArray;
    }
}

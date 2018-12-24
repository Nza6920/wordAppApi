package my.api.reciteWords;

import my.bean.Library;
import my.dao.LibraryDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

public class GetLibraryById extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"libraryId"})) {
            this.httpResp.setStatus(403);            // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONObject resultData = new JSONObject();

        try {
            LibraryDao libraryDao = (LibraryDao) ServiceUtils.getSqlSession(LibraryDao.class);

            Library library = libraryDao.getById(jreq.getInt("libraryId"));
            resultData.put("id", library.getId());
            resultData.put("name", library.getName());
            resultData.put("count", library.getCount());
            resultData.put("created_at", library.getCreated_at());
        } finally {
            ServiceUtils.closeSqlSession();
        }

        return resultData;
    }
}

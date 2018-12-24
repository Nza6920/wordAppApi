package my.api.options;

import my.dao.OptionDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

public class SetCurrentWord extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {
        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"wordId"})) {
            this.httpResp.setStatus(403);   // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        try {
            OptionDao optionDao = (OptionDao) ServiceUtils.getSqlSession(OptionDao.class);
            optionDao.setCurrentWord(jreq.getInt("userId"), jreq.getInt("wordId"));
        } finally {
            ServiceUtils.closeSqlSession();
        }

        this.httpResp.setStatus(201);

        return null;
    }
}

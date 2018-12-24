package my.api.unskilled;

import my.bean.Unskilled;
import my.dao.UnskilledDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

public class AddUnSkillWord extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"word"})) {
            this.httpResp.setStatus(403);
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONObject resultData = new JSONObject();

        try {
            UnskilledDao unskilledDao = (UnskilledDao) ServiceUtils.getSqlSession(UnskilledDao.class, false);

            Unskilled unskilled = unskilledDao.getUnskillById(jreq.getInt("userId"));
            Integer unskilledId = unskilled.getId();

            if (unskilled == null) {
                resultData.put("message", "该用户没有生词表");
                this.httpResp.setStatus(403);
                return resultData;
            }

            JSONObject wordJson = jreq.getJSONObject("word");
            unskilledDao.addUnskillWord(unskilledId, wordJson.getInt("id"));
            ServiceUtils.commitSqlSession();

        }catch (Exception e) {
            e.printStackTrace();
            ServiceUtils.rollbackSqlSession();
            resultData.put("message", e.getMessage());
            return resultData;
        } finally {
            ServiceUtils.closeSqlSession();
        }
        return null;
    }
}

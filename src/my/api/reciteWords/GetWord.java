package my.api.reciteWords;

import my.bean.Word;
import my.dao.WordDao;
import org.json.JSONArray;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.List;

// 获取指定词库单词
public class GetWord extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"libraryId"})) {
            this.httpResp.setStatus(403);            // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONArray resultArray = null;               // 成功返回
        JSONObject resultData = new JSONObject();   // 错误返回

        try {
            WordDao wordDao = (WordDao) ServiceUtils.getSqlSession(WordDao.class);
            List<Word> wordList = wordDao.getWordByLibrary(jreq.getInt("libraryId"));
            resultArray = new JSONArray(wordList);
        } finally {
            ServiceUtils.closeSqlSession();
        }

        // 词库不存在
        if (resultArray.length() == 0) {
            resultData.put("message", "词库不存在");
            this.httpResp.setStatus(404);
            return resultData;
        }

        return resultArray;
    }
}

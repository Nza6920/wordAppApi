package my.api.review;

import my.bean.Word;
import my.dao.ReviewDao;
import org.json.JSONArray;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.List;

public class GetReviewWords extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {
        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"offset"})) {
            this.httpResp.setStatus(403);   // 服务器拒绝了请求
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONArray resultArray;
        JSONObject resultData = new JSONObject();
        List<Word> wordList;

        try {
            ReviewDao reviewDao = (ReviewDao) ServiceUtils.getSqlSession(ReviewDao.class);
            Integer offset = jreq.getInt("offset")-1;
            wordList = reviewDao.getReviewWord(jreq.getInt("userId"), offset, offset + 30);
        } finally {
            ServiceUtils.closeSqlSession();
        }

        if(wordList == null || wordList.size() == 0) {
            resultData.put("message", "没有需要复习的单词");
            this.httpResp.setStatus(403);
            return resultData;
        }

        resultArray = new JSONArray(wordList);

        return resultArray;
    }
}

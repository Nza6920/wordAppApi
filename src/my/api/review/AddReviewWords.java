package my.api.review;

import my.bean.Review;
import my.bean.Word;
import my.dao.ReviewDao;
import org.json.JSONArray;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class AddReviewWords extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"wordList"})) {
            this.httpResp.setStatus(403);
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONObject resultData = new JSONObject();
        JSONArray jsonArray;
        List<Word> wordList = new ArrayList<>();

        try {
            ReviewDao reviewDao = (ReviewDao) ServiceUtils.getSqlSession(ReviewDao.class);

            // 查询用户对应的复习表
            Review review = reviewDao.getReviewById(jreq.getInt("userId"));
            Integer reviewId = review.getId();

            // 判断复习表是否存在(健壮性)
            if (reviewId == null) {
                resultData.put("message", "该用户的复习表不存在");
                this.httpResp.setStatus(403);
                return resultData;
            }

            jsonArray = jreq.getJSONArray("wordList");


            // 判断参数中是否有传值
            if (jsonArray == null || jsonArray.length() == 0) {
                resultData.put("message", "没有数据");
                this.httpResp.setStatus(403);
                return resultData;
            }

            // 解析 JSONArray 到 wordList
            for (int i = 0; i< jsonArray.length(); i++) {
                JSONObject wordJson = jsonArray.getJSONObject(i);
                wordList.add(new Word(
                            wordJson.getInt("id"), wordJson.getString("content"),
                            wordJson.getString("translation"), wordJson.getInt("library_id"), 1)
                );
            }
            // 添加复习单词
            reviewDao.addReviewWord(reviewId, wordList);
            this.httpResp.setStatus(201);
        }catch (Exception e) {
            e.printStackTrace();
            resultData.put("message", e.getMessage());
            this.httpResp.setStatus(403);
            return resultData;
        } finally {
            ServiceUtils.closeSqlSession();
        }

        return null;
    }
}

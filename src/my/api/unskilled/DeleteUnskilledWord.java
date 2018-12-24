package my.api.unskilled;

import my.bean.Review;
import my.bean.Unskilled;
import my.bean.Word;
import my.dao.ReviewDao;
import my.dao.UnskilledDao;
import org.json.JSONArray;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class DeleteUnskilledWord extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        // 请求参数不正确, 返回403
        if (!ServiceUtils.jsonVerify(jreq, new String[]{"words"})) {
            this.httpResp.setStatus(403);
            throw new RestfulException(1, "请求参数不正确!");
        }

        JSONObject resultData = new JSONObject();

        JSONArray wordsJ = jreq.getJSONArray("words");

        ArrayList<Word> words = new ArrayList<>();

        for (int i=0; i<wordsJ.length(); i++)
        {
            JSONObject wordJ = wordsJ.getJSONObject(i);
            words.add(new Word(
                    wordJ.getInt("id"),
                    wordJ.getString("content"),
                    wordJ.getString("translation"),
                    wordJ.getInt("library_id"),
                    wordJ.getInt("skill_level")
            ));
        }


        try {
            UnskilledDao unskilledDao = (UnskilledDao) ServiceUtils.getSqlSession(UnskilledDao.class);

            Unskilled unskilled = unskilledDao.getUnskillById(jreq.getInt("userId"));
            Integer unskilledId = unskilled.getId();

            if (unskilledId == null) {
                resultData.put("message", "该用户没有生词表");
                this.httpResp.setStatus(403);
                return resultData;
            }

            unskilledDao.deleteUnskilledWord(unskilledId, words);

        }catch (Exception e) {
            e.printStackTrace();
            ServiceUtils.rollbackSqlSession();
            resultData.put("message", e.getMessage());
            return resultData;
        } finally {
            ServiceUtils.closeSqlSession();
        }

        // 把从生词表删除的单词存入复习表
        try {
            ReviewDao reviewDao = (ReviewDao) ServiceUtils.getSqlSession(ReviewDao.class);
            Review review = reviewDao.getReviewById(jreq.getInt("userId"));

            reviewDao.addReviewWord(review.getId(),words);
        }catch (Exception e){
            e.printStackTrace();
            ServiceUtils.rollbackSqlSession();
            resultData.put("message", e.getMessage());
            return resultData;
        }finally {
            ServiceUtils.closeSqlSession();
        }
        return null;
    }
}

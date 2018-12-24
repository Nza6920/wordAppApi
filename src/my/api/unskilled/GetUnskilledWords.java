package my.api.unskilled;

import my.bean.Word;
import my.dao.UnskilledDao;
import org.json.JSONArray;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;

import java.util.List;

public class GetUnskilledWords extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {

        JSONArray resultArray;
        JSONObject resultData = new JSONObject();
        List<Word> wordList;

        try {
            UnskilledDao unskilledDao = (UnskilledDao) ServiceUtils.getSqlSession(UnskilledDao.class);
            wordList = unskilledDao.getUnskilledWord(jreq.getInt("userId"));
        } finally {
            ServiceUtils.closeSqlSession();
        }

        if(wordList == null || wordList.size() == 0) {
            resultData.put("message", "没有陌生的单词.");
            return resultData;
        }

        resultArray = new JSONArray(wordList);

        return resultArray;

    }
}

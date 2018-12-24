package my.api.options;

import my.bean.Option;
import my.dao.OptionDao;
import org.json.JSONObject;
import web.restful.RestfulApi;
import web.restful.RestfulException;
import web.service.ServiceUtils;


/**
 * 获取指定用户的系统设置
 * 接收参数: userId
 */
public class GetOption extends RestfulApi {
    @Override
    public Object execute(JSONObject jreq) throws Exception {
        JSONObject resultData = new JSONObject();
        Option option = null;

        try {
            OptionDao optionDao  = (OptionDao) ServiceUtils.getSqlSession(OptionDao.class);
            option = optionDao.getOptionById(jreq.getInt("userId"));
        } finally {
            ServiceUtils.closeSqlSession();
        }

        // 用户不存在, 返回404
        if (option == null) {
            resultData.put("message", "用户不存在");
            this.httpResp.setStatus(404);
            return resultData;
        }

        resultData.put("userId", option.getUser_id());
        resultData.put("libraryId", option.getLibrary_id());
        resultData.put("errorInterval", option.getError_interval());
        resultData.put("randInterval", option.getRand_interval());
        resultData.put("currentWord", option.getCurrent_word());
        resultData.put("currentReview", option.getCurrent_review());
        resultData.put("currentReview2", option.getCurrent_review2());
        resultData.put("currentTest", option.getCurrent_test());

        return resultData;
    }
}

package my.dao;

import my.bean.Option;
import org.apache.ibatis.annotations.Param;

public interface OptionDao {

    // 新增设置
    public void addOption(Option option);

    // 获取指定用户系统设置
    public Option getOptionById(Integer id);

    // 设置当前词库
    public void setLibrary(@Param("userId") Integer userId, @Param("libraryId") Integer libraryId);

    // 设置错误间隔与随机测试次数
    public void setErrorandRand(@Param("userId") Integer userId, @Param("randInterval") Integer rand, @Param("errorInterval") Integer error);

    // 设置当前单词进度
    public void setCurrentWord(@Param("userId") Integer userId, @Param("id") Integer id);

    // 设置当前Cn复习进度
    public void setCurrentReviewCn(@Param("userId") Integer userId, @Param("count") Integer count);

    // 设置当前En复习进度
    public void setCurrentReviewEn(@Param("userId") Integer userId, @Param("count") Integer count);

    // 设置当前测试进度
    public void setCurrentTest(@Param("userId") Integer userId, @Param("count") Integer count);

}

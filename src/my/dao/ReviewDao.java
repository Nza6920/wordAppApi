package my.dao;

import my.bean.Review;
import my.bean.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ReviewDao {

    // 新建复习
    public void addReview(Review review);

    // 获得所有复习单词 (一次返回30个)
    public List<Word> getReviewWord(@Param("userId") Integer userId,@Param("offset") Integer offset,@Param("end") Integer end);

    // 获取复习单词2
    public List<Word> getReviewWord2(@Param("userId") Integer userId,@Param("start") Integer start,@Param("end") Integer end);

    // 查询用户复习表
    public Review getReviewById(Integer userId);

    // 添加复习单词
    public void addReviewWord(@Param("review_id") Integer reviewId, @Param("words") List<Word> wordList);

    // 更新复习单词
    public void updateReviewWord(@Param("review_id") Integer reviewId, @Param("words") List<Word> wordList);
}

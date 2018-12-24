package my.dao;

import my.bean.Unskilled;
import my.bean.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnskilledDao {
    // 新建生词表
    public void addUnskill(Unskilled unskilled);

    // 添加一个生词
    public void addUnskillWord(@Param("unskilled_id") Integer unskilledId, @Param("word_id") Integer wordId);

    // 查询用户生词表
    public Unskilled getUnskillById(Integer userId);

    // 获得当前用户生词
    public List<Word> getUnskilledWord(@Param("userId") Integer userId);

    // 删除所选生词
    public void deleteUnskilledWord(@Param("unskilled_id") Integer unskilledId,@Param("words") List<Word> wordList);

    // 批量添加生词
    public void addUnskillWords(@Param("unskilled_id") Integer Id, @Param("unskills") List<Word> wordList);

}

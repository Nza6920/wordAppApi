package my.dao;

import my.bean.Word;

import java.util.List;

public interface WordDao {

    // 获取指定词库的单词
    public List<Word> getWordByLibrary(Integer id);
}

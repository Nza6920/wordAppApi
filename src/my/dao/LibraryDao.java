package my.dao;

import my.bean.Library;

import java.util.List;

public interface LibraryDao {
    public List<Library> getAll();

    public Library getById(Integer id);
}

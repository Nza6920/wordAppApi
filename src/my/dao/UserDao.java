package my.dao;

import my.bean.LibraryAndUser;
import my.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserDao {
    // 获取所有用户
    public List<User> getAll();

    // 按名字查询用户
    public User getUserByName(String name);

    // 新增用户
    public void addUser(User user);

    // 设置用户密码
    public int updatePassword(@Param("id") Integer id, @Param("password") String password);

    // 更新用户更新时间
    public void setUpdatedAt(@Param("id") Integer id, @Param("updated_at") Date updated_at);

    // 收藏词库
    public void selectLibrary(@Param("user_id") Integer userId, @Param("library_id") Integer libraryId ,@Param("created_at") Date createdAt);

    // 获得用户收藏
    public List<LibraryAndUser> getSelectLibrary(Integer userId);

    // 查看是否已收藏
    public LibraryAndUser selectLibrartById(@Param("user_id") Integer userId, @Param("library_id") Integer libraryId);

    // 删除收藏
    public void deleteSelectLibraryById(@Param("user_id") Integer userId, @Param("library_id") Integer libraryId);
}

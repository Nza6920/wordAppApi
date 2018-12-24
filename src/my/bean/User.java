package my.bean;


import java.util.Date; 

// 用户
public class User 
{ 
 
	private Integer id ; 
	private String username ; 
	private String password ; 
	private Date created_at ;
	private Date updated_at ;

	public User()
	{

	}

	public User(Integer id, String username, String password, Date created_at, Date updated_at)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public void setId(Integer id)
	{
		this.id=id;
	}
	public Integer getId()
	{
		return this.id;
	}
	public void setUsername(String username)
	{
		this.username=username;
	}
	public String getUsername()
	{
		return this.username;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getPassword()
	{
		return this.password;
	}
	public void setCreated_at(Date created_at)
	{
		this.created_at=created_at;
	}
	public Date getCreated_at()
	{
		return this.created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", created_at=" + created_at +
				", updated_at=" + updated_at +
				'}';
	}

}
 
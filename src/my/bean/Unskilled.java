package my.bean;

import java.util.Date; 

// 生词
public class Unskilled 
{ 
 
	private Integer id ; 
	private Integer user_id ; 
	private Date created_at ; 

	public Unskilled()
	{

	}

	public Unskilled(Integer user_id, Date created_at)
	{
		this.user_id = user_id;
		this.created_at = created_at;
	}

	public void setId(Integer id)
	{
		this.id=id;
	}
	public Integer getId()
	{
		return this.id;
	}
	public void setUser_id(Integer user_id)
	{
		this.user_id=user_id;
	}
	public Integer getUser_id()
	{
		return this.user_id;
	}
	public void setCreated_at(Date created_at)
	{
		this.created_at=created_at;
	}
	public Date getCreated_at()
	{
		return this.created_at;
	}

} 
 
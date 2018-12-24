package my.bean;


import java.util.Date; 

// 词库
public class Library 
{ 
 
	private Integer id ; 
	private String name ; 
	private Integer count ; 
	private Date created_at ; 


	public void setId(Integer id)
	{
		this.id=id;
	}
	public Integer getId()
	{
		return this.id;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getName()
	{
		return this.name;
	}
	public void setCount(Integer count)
	{
		this.count=count;
	}
	public Integer getCount()
	{
		return this.count;
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
 
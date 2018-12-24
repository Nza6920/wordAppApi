package my.bean;

// 单词
public class Word 
{ 
 
	private Integer id ; 
	private String content ; 
	private String translation ; 
	private Integer library_id ;
	private Integer skill_level;

	public Word()
	{

	}

	public Word(Integer id, String content, String translation, Integer library_id, Integer skill_level)
	{
		this.id = id;
		this.content = content;
		this.translation = translation;
		this.library_id = library_id;
		this.skill_level = skill_level;
	}

	public void setId(Integer id)
	{
		this.id=id;
	}
	public Integer getId()
	{
		return this.id;
	}
	public void setContent(String content)
	{
		this.content=content;
	}
	public String getContent()
	{
		return this.content;
	}
	public void setTranslation(String translation)
	{
		this.translation=translation;
	}
	public String getTranslation()
	{
		return this.translation;
	}
	public void setLibrary_id(Integer library_id)
	{
		this.library_id=library_id;
	}
	public Integer getLibrary_id()
	{
		return this.library_id;
	}
	public Integer getSkill_level() {
		return skill_level;
	}
	public void setSkill_level(Integer skill_level) {
		this.skill_level = skill_level;
	}
}
 
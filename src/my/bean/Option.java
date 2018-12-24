package my.bean;

// 系统设置
public class Option
{ 
 
	private Integer id ; 
	private Integer user_id ; 
	private Integer library_id ; 
	private Integer error_interval ; 
	private Integer rand_interval ;
	private Integer current_word;
	private Integer current_review;
    private Integer current_review2;
    private Integer current_test;


    public Option()
	{

	}

	public Option(Integer user_id, Integer library_id, Integer error_interval, Integer rand_interval, Integer current_word) {
		this.user_id = user_id;
		this.library_id = library_id;
		this.error_interval = error_interval;
		this.rand_interval = rand_interval;
		this.current_word = current_word;
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
	public void setLibrary_id(Integer library_id)
	{
		this.library_id=library_id;
	}
	public Integer getLibrary_id()
	{
		return this.library_id;
	}
	public void setError_interval(Integer error_interval)
	{
		this.error_interval=error_interval;
	}
	public Integer getError_interval()
	{
		return this.error_interval;
	}
	public void setRand_interval(Integer rand_interval)
	{
		this.rand_interval=rand_interval;
	}
	public Integer getRand_interval()
	{
		return this.rand_interval;
	}

	public Integer getCurrent_word() {
		return current_word;
	}

	public void setCurrent_word(Integer current_word) {
		this.current_word = current_word;
	}

	public Integer getCurrent_review() {
		return current_review;
	}
	public void setCurrent_review(Integer current_review) {
		this.current_review = current_review;
	}

    public Integer getCurrent_review2() {
        return current_review2;
    }

    public void setCurrent_review2(Integer current_review2) {
        this.current_review2 = current_review2;
    }

	public Integer getCurrent_test() {
		return current_test;
	}

	public void setCurrent_test(Integer current_test) {
		this.current_test = current_test;
	}
}
 
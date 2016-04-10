package member;

public class user{
	
	private int id;
	private String type;
	private String name;
	private String age;
	private String account;
	private String password;
	private String tel;
	private String email;
	private String address;
	
	public void setID(int id){
		this.id = id;
	}
	public void setType(String type){
		this.type = type;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setAge(String age){
		this.age = age;
	}
	public void setAccount(String account){
		this.account = account;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public Integer getID(){
		return this.id;
	}
	public String getType(){
		return this.type;
	}
	public String getName(){
		return this.name;
	}
	public String getAge(){
		return this.age;
	}
	public String getAccount(){
		return this.account;
	}
	public String getPassword(){
		return this.password;
	}
	public String getEmail(){
		return this.email;
	}
}

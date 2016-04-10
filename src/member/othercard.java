package member;

public class othercard {
	private String othercard_id;
	private String othercard_type , othercard_name , othercard_image , othercard_description;
	
	public void setOtherCardID(String id){
		this.othercard_id = id;
	}
	public void setOtherCardType(String type){
		this.othercard_type = type;
	}
	public void setOtherCardName(String name){
		this.othercard_name = name;
	}
	public void setOtherCardImage(String image){
		this.othercard_image= image;
	}
	public void setOtherCardDescription(String description){
		this.othercard_description = description;
	}
	
	public String getOtherCardID(){
		return this.othercard_id;
	}
	public String getOtherCardType(){
		return this.othercard_type;
	}
	public String getOtherCardName(){
		return this.othercard_name;
	}
	public String getOtherCardImage(){
		return this.othercard_image;
	}
	public String getOtherCardDescription(){
		return this.othercard_description;
	}
	
}

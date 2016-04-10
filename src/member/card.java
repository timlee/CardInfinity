package member;

import java.io.Serializable;

public class card implements Serializable
{
	private String Card_ID;
	private String Card_Name,Card_Company,Card_Department ,Card_JobTitle ,Card_Email ,Card_Tel ,  
					Card_MobilePhone,Card_Address,Card_Fax ,Card_Image ,Card_Url,Card_Type ;
	
	
	
	public void setCardID(String id){
		this.Card_ID = id;
	}
	public void setCardName(String name){
		this.Card_Name = name;
	}
	public void setCardCompany(String company){
		this.Card_Company = company;
	}
	public void setCardDepartment(String department){
		this.Card_Department = department;
	}
	public void setCardJobTitle(String jobTitle){
		this.Card_JobTitle = jobTitle;
	}
	public void setCardEmail(String email){
		this.Card_Email = email;
	}
	public void setCardTel(String tel){
		this.Card_Tel = tel;
	}
	public void setCardMobilePhone(String mobile){
		this.Card_MobilePhone = mobile;
	}
	public void setCardAddress(String address){
		this.Card_Address = address;
	}
	public void setCardFax(String fax){
		this.Card_Fax = fax;
	}
	public void setCardImage(String image){
		this.Card_Image = image;
	}
	public void setCardUrl(String url){
		this.Card_Url = url;
	}
	public void setCardType(String type){
		this.Card_Type = type;
	}
	
	public String getCardID(){
		return this.Card_ID;
	}
	public String getCardName(){
		return this.Card_Name;
	}
	public String getCardCompany(){
		return this.Card_Company;
	}
	public String getCardDepartment(){
		return this.Card_Department;
	}
	public String getCardJobTitle(){
		return this.Card_JobTitle;
	}
	public String getCardEmail(){
		return this.Card_Email;
	}
	public String getCardTel(){
		return this.Card_Tel;
	}
	public String getCardMobile(){
		return this.Card_MobilePhone;
	}
	public String getCardAddress(){
		return this.Card_Address;
	}
	public String getCardFax(){
		return this.Card_Fax;
	}
	public String getCardImage(){
		return this.Card_Image;
	}
	public String getCardUrl(){
		return this.Card_Url;
	}
	public String getCardType(){
		return this.Card_Type;
	}
}

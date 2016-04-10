package myDataBase;

import java.io.File;
import java.util.ArrayList;

import member.card;
import member.othercard;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class dbCard {
	private  SQLiteDatabase db;
	
	public dbCard(myDB database){
		this.db = database.db;
	}
	//�T�{��Ʈw�O�_�}��
	private boolean checkDB()
	{
		if(db.isOpen() == true)
		{
			return true;
		}
		else
		{
			System.out.println("Database not opened");
			return false;
		}
	}
	//�s�W�W��
	public boolean addCard(String id,String cardName , String cardCompany,String cardDepartment , String cardJobTitle , String cardEmail ,
			String cardTel ,  String cardMobilePhone,String cardAddress , String cardFax , String cardImage , String cardUrl ,
			String cardType)
	{
		String imagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/cardImage/";	
		if(checkDB())
		{
			String SQL = "INSERT OR IGNORE INTO card(Card_ID,Card_Name,Card_Company,Card_Department,Card_JobTitle,Card_Email,"
													+ "Card_Tel,Card_MobilePhone,Card_Address,Card_Fax,Card_Image,Card_Url,Card_Type)"
					+ " VALUES(" + "'" + id + "'" +","+
								   "'" + cardName + "'" +","+
								   "'" + cardCompany + "'" +","+ 
								   "'" + cardDepartment + "'" +","+ 
					               "'" + cardJobTitle + "'" +","+
								   "'" + cardEmail + "'" +","+ 					 
					               "'" +cardTel + "'" +","+
					               "'" +cardMobilePhone + "'" +","+ 
					               "'" +cardAddress + "'" +","+ 
					               "'" +cardFax + "'" +","+ 
					               "'"+ imagePath +cardImage + "'" +","+ 
								   "'" + cardUrl +"'" + ","+
								   "'" + cardType +"'" + 
					               ")";
			db.execSQL(SQL);
			return true;
		}
		else
		{
			return true;
		}
	}
	//�s�W�d��
	public boolean addOtherCard(String id , String type , String name , String image , String description)
	{
		String imagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/otherCardImage/";	
		if(checkDB())
		{
			String SQL = "INSERT OR IGNORE INTO othercard(OtherCard_ID,OtherCard_Type,OtherCard_Name,OtherCard_Image,OtherCard_Description"+")"
					+ " VALUES(" + "'" + id + "'" +","+
								   "'" + type + "'" +","+
								   "'" + name + "'" +","+ 
								   "'" + imagePath + image + ".jpg" +"'" +","+ 
								   "'" + description +"'" + 
					               ")";
			db.execSQL(SQL);
			return true;
		}
		else
		{
			return true;
		}
	}
	//�ǥѦW��id���o�d��
	public card getCardByID(String id)
	{
		if(checkDB())
		{
			String where = "Card_ID" + "=" + "'" +id + "'"; //�d��sql�y�l
			card result = null;
			Cursor cursor = db.query
						("card",null,where,null,null,null,null,null);
					
					
			if(cursor.moveToNext())//����Ʈ�
			{
				result = setCard(cursor);
				cursor.close();
				return result;
			}
			else//�S����Ʈ�
			{
				System.out.println("Data not found");
				cursor.close();
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	
	//�ǥѦW��id���o�d��
		public othercard getOtherCardByID(String id)
		{
			if(checkDB())
			{
				String where = "OtherCard_ID" + "=" + "'" +id + "'"; //�d��sql�y�l
				othercard result = null;
				Cursor cursor = db.query
							("othercard",null,where,null,null,null,null,null);
						
						
				if(cursor.moveToNext())//����Ʈ�
				{
					result = setOtherCard(cursor);
					cursor.close();
					return result;
				}
				else//�S����Ʈ�
				{
					System.out.println("Data not found");
					cursor.close();
					return null;
				}
			}
			else
			{
				return null;
			}
		}
	
	//���o�����d��
		public ArrayList<othercard> getIdentificationCards()
		{
			ArrayList<othercard> resultList = new ArrayList<othercard>(2);
			if(checkDB())
			{
				String where = "OtherCard_Type" + "=" + "'Identification'" ; //�d��sql�y�l
				Cursor cursor = db.query
							("othercard",null,where,null,null,null,null,null);

				int count = cursor.getCount();
				cursor.moveToFirst();
				for(int i=0;i<count;i++)
				{
					resultList.add(setOtherCard(cursor));
					cursor.moveToNext();
				}
				cursor.close();
				return resultList;
			}
			else
			{
				return null;
			}
		}
		//���o�u�f�d��
				public ArrayList<othercard> getPreferentialCards()
				{
					ArrayList<othercard> resultList = new ArrayList<othercard>(2);
					if(checkDB())
					{
						String where = "OtherCard_Type" + "=" + "'Preferential'" ; //�d��sql�y�l
						Cursor cursor = db.query
									("othercard",null,where,null,null,null,null,null);

						int count = cursor.getCount();
						cursor.moveToFirst();
						for(int i=0;i<count;i++)
						{
							resultList.add(setOtherCard(cursor));
							cursor.moveToNext();
						}
						cursor.close();
						return resultList;
					}
					else
					{
						return null;
					}
				}
		//���o��L�d��
		public ArrayList<othercard> getOtherCards()
		{
			ArrayList<othercard> resultList = new ArrayList<othercard>(2);
			if(checkDB())
			{
				String where = "OtherCard_Type" + "=" + "'Other'" ; //�d��sql�y�l
				Cursor cursor = db.query
							("othercard",null,where,null,null,null,null,null);

				int count = cursor.getCount();
				cursor.moveToFirst();
				for(int i=0;i<count;i++)
				{
					resultList.add(setOtherCard(cursor));
					cursor.moveToNext();
				}
				cursor.close();
				return resultList;
			}
			else
			{
				return null;
			}
		}
	//���o�d���ƶq
	public int getCardCount(){
		if(checkDB())
		{
			int result = 0;
			Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + "card", null);
			if(cursor.moveToNext()){
				result = cursor.getInt(0);
			}
			else{
				System.out.println("card not found");
				return -1;
			}
			cursor.close();
			return result;
		}
		else
		{
			return -1;
		}
		
	}
	//��s�W�����
	public boolean updateByID(String id , card updateCard){
		String imagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/cardImage/";	
		if(checkDB())
		{
			String where = "Card_ID" + "=" + "'" + id + "'";
			ContentValues cv = new ContentValues();
			cv.put("Card_ID", updateCard.getCardID());
			cv.put("Card_Name", updateCard.getCardName());
			cv.put("Card_Company", updateCard.getCardCompany());
			cv.put("Card_Department", updateCard.getCardDepartment());
			cv.put("Card_JobTitle", updateCard.getCardJobTitle());
			cv.put("Card_Email", updateCard.getCardEmail());
			cv.put("Card_Tel", updateCard.getCardTel());
			cv.put("Card_MobilePhone", updateCard.getCardMobile());
			cv.put("Card_Address", updateCard.getCardAddress());
			cv.put("Card_Fax", updateCard.getCardFax());
			cv.put("Card_Image", imagePath + updateCard.getCardImage());
			cv.put("Card_Url", updateCard.getCardUrl());
			cv.put("Card_Type", updateCard.getCardType());
			db.update("card", cv, where, null);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//��s�d�����
		public boolean updateOtherByID(String id , othercard updateCard){
			String imagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/otherCardImage/";	
			if(checkDB())
			{
				String where = "OtherCard_ID" + "=" + "'" + id + "'";
				ContentValues cv = new ContentValues();
				cv.put("OtherCard_ID", updateCard.getOtherCardID());
				cv.put("OtherCard_Type", updateCard.getOtherCardType());
				cv.put("OtherCard_Name", updateCard.getOtherCardName());
				cv.put("OtherCard_Image", imagePath + updateCard.getOtherCardImage());
				cv.put("OtherCard_Description", updateCard.getOtherCardDescription());
				db.update("othercard", cv, where, null);
				return true;
			}
			else
			{
				return false;
			}
		}
	
	
	public boolean deleteByID(String id)
	{
		String imagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/cardImage/";	
		if(checkDB())
		{
			if(getCardByID(id) != null)
			{
				
				card card = this.getCardByID(id);
				
				File file = new File(card.getCardImage());
				//�D�w�]�Ϯ� �R���Ϥ�
				if(!card.getCardImage().equals(imagePath + "default_image.jpg")){file.delete();}
				
				String where = "Card_ID" + "=" +"'" + id + "'";
				db.delete("card", where, null);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public boolean deleteOtherByID(String id)
	{
		if(checkDB())
		{
			if(getOtherCardByID(id) != null)
			{
				
				othercard card = this.getOtherCardByID(id);
				
				File file = new File(card.getOtherCardImage());
				file.delete();
				
				String where = "OtherCard_ID" + "=" +"'" + id + "'";
				db.delete("othercard", where, null);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	//���o�Ҧ��d��
	public ArrayList<card> getAllCard()
	{
		ArrayList<card> result = new ArrayList<card>(2);
		if(checkDB())
		{
			Cursor cursor = db.query("card", null, null,null, null, null, null);
			int count = cursor.getCount();
			cursor.moveToFirst();
			for(int i=0;i<count;i++)
			{
				result.add(setCard(cursor));
				cursor.moveToNext();
			}
			cursor.close();
			return result;
		}
		return result;
		}
	//�ʸ˦W��
		private card setCard(Cursor result)
		{
			card temp = new card();
			temp.setCardID(result.getString(0));
			temp.setCardName(result.getString(1));
			temp.setCardCompany(result.getString(2));
			temp.setCardDepartment(result.getString(3));
			temp.setCardJobTitle(result.getString(4));
			temp.setCardEmail(result.getString(5));
			temp.setCardTel(result.getString(6));
			temp.setCardMobilePhone(result.getString(7));
			temp.setCardAddress(result.getString(8));
			temp.setCardFax(result.getString(9));
			temp.setCardImage(result.getString(10));
			temp.setCardUrl(result.getString(11));
			
			return temp;
			
		}
		//�ʸ˥d��
		private othercard setOtherCard(Cursor result)
		{
			othercard temp = new othercard();
			temp.setOtherCardID(result.getString(0));
			temp.setOtherCardType(result.getString(1));
			temp.setOtherCardName(result.getString(2));
			temp.setOtherCardImage(result.getString(3));
			temp.setOtherCardDescription(result.getString(4));
			
			return temp;
		}
}

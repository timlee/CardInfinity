package phoneContact;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Contacts.Intents.Insert;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.util.Log;
import android.widget.Toast;

public class addPhoneContactBuilder 
{
	private int rawContactID;
	private Activity activity;
	
	ArrayList<ContentProviderOperation> ops;
	
	public addPhoneContactBuilder(Activity activity)
	{
		this.activity = activity;
		this.ops = new ArrayList<ContentProviderOperation>();
		this.rawContactID = ops.size();
	}
	public addPhoneContactBuilder init(){
		// 首先向RawContacts.CONTENT_URI 執行一個空值插入(raw_contacts 表),
				ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
						.withValue(RawContacts.ACCOUNT_NAME, null)
						.build());
				return this;
	}
	//新增名稱
	public addPhoneContactBuilder addName(String name)
	{
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
				.withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, name)
				.build());
		Log.i("text", name);
		return this;
	}
	//新增E-mail
	//type
	/*
	 *Phone.TYPE_HOME -> 住家e-mail
	 *Phone.TYPE_WORK -> 工作e-mail
	 *Phone.TYPE_OTHER -> 其他e-mail */
	public addPhoneContactBuilder addContactEmail (String email , int type)
	{
		switch(type)
		{
			// 住家 e-mail
			case Email.TYPE_HOME:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email)
						.withValue(Email.TYPE, type)
						.build());
				break;
			//工作e-mail
			case Email.TYPE_WORK:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email)
						.withValue(Email.TYPE, type)
						.build());
				break;
			//其他e-mail
			case Email.TYPE_OTHER:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email)
						.withValue(Email.TYPE, type)
						.build());
				break;
			//自定義標籤email
				/*
			case Phone.TYPE_CUSTOM:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email)
						.withValue(Email.TYPE, Email.TYPE_CUSTOM)
						.withValue(Email.LABEL, "custom")
						.build());*/
		}
		return this;
	}
	//新增電話號碼
	/*type 
	Phone.TYPE_HOME -> 家裡電話 , 
	CommonDataKinds.Phone.TYPE_MOBILE -> 行動電話 , 
	CommonDataKinds.Phone.TYPE_WORK -> 公司電話 */
	public addPhoneContactBuilder addPhoneNumber(String number , int type)
	{
		switch(type)
		{
			//家裡電話
			case Phone.TYPE_HOME:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, number)
						.withValue(Phone.TYPE, type)
						.build());
				break;
			//行動電話
			case CommonDataKinds.Phone.TYPE_MOBILE:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, number)
						.withValue(Phone.TYPE, type)
						.build());
				break;
			//公司電話
			case CommonDataKinds.Phone.TYPE_WORK:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, number)
						.withValue(Phone.TYPE, type)
						.build());
				break;
		}
			
				
		return this;
	}
	//新增住址
	/*type
	 * StructuredPostal.TYPE_HOME -> 家裡住址
	 * StructuredPostal.TYPE_WORK -> 公司住址 */
	public addPhoneContactBuilder addAddress(String address , int type)
	{
		switch(type)
		{
			//家裡住址
			case StructuredPostal.TYPE_HOME:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(StructuredPostal.FORMATTED_ADDRESS , address)
						.withValue(StructuredPostal.TYPE, type)
						.build());
				break;
			//公司住址
			case StructuredPostal.TYPE_WORK:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(StructuredPostal.FORMATTED_ADDRESS , address)
						.withValue(StructuredPostal.TYPE, type)
						.build());
				break;
			
		} 
		return this;
	}
	//新增公司
	/*type
	 * Organization.TYPE_WORK -> 公司 */
	public addPhoneContactBuilder addOrganization(String org  , String title ,int type)
	{
		switch(type)
		{
			case Organization.TYPE_WORK:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
						.withValue(Organization.COMPANY , org)
						.withValue(Organization.TITLE, title)
						.withValue(Organization.TYPE, type)
						.build());	
		}
		
			
		return this;
	}
	//新增大頭貼
	public addPhoneContactBuilder addPhoto (Bitmap bmp)
	{
        if(bmp == null)
        	return this;
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);   
        byte[] b = baos.toByteArray();
		
        	System.out.println(bmp);
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
				.withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
				.withValue(CommonDataKinds.Photo.DATA15 , b)
				.build());	
		return this;
	}
	// execution whole ops
	public addPhoneContactBuilder exec()
	{
		try 
		{
			activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			ops.clear();
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return this;
	}
}

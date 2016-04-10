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
		// �����VRawContacts.CONTENT_URI ����@�Ӫŭȴ��J(raw_contacts ��),
				ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
						.withValue(RawContacts.ACCOUNT_NAME, null)
						.build());
				return this;
	}
	//�s�W�W��
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
	//�s�WE-mail
	//type
	/*
	 *Phone.TYPE_HOME -> ��ae-mail
	 *Phone.TYPE_WORK -> �u�@e-mail
	 *Phone.TYPE_OTHER -> ��Le-mail */
	public addPhoneContactBuilder addContactEmail (String email , int type)
	{
		switch(type)
		{
			// ��a e-mail
			case Email.TYPE_HOME:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email)
						.withValue(Email.TYPE, type)
						.build());
				break;
			//�u�@e-mail
			case Email.TYPE_WORK:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email)
						.withValue(Email.TYPE, type)
						.build());
				break;
			//��Le-mail
			case Email.TYPE_OTHER:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.ADDRESS, email)
						.withValue(Email.TYPE, type)
						.build());
				break;
			//�۩w�q����email
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
	//�s�W�q�ܸ��X
	/*type 
	Phone.TYPE_HOME -> �a�̹q�� , 
	CommonDataKinds.Phone.TYPE_MOBILE -> ��ʹq�� , 
	CommonDataKinds.Phone.TYPE_WORK -> ���q�q�� */
	public addPhoneContactBuilder addPhoneNumber(String number , int type)
	{
		switch(type)
		{
			//�a�̹q��
			case Phone.TYPE_HOME:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, number)
						.withValue(Phone.TYPE, type)
						.build());
				break;
			//��ʹq��
			case CommonDataKinds.Phone.TYPE_MOBILE:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, number)
						.withValue(Phone.TYPE, type)
						.build());
				break;
			//���q�q��
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
	//�s�W��}
	/*type
	 * StructuredPostal.TYPE_HOME -> �a�̦�}
	 * StructuredPostal.TYPE_WORK -> ���q��} */
	public addPhoneContactBuilder addAddress(String address , int type)
	{
		switch(type)
		{
			//�a�̦�}
			case StructuredPostal.TYPE_HOME:
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(StructuredPostal.FORMATTED_ADDRESS , address)
						.withValue(StructuredPostal.TYPE, type)
						.build());
				break;
			//���q��}
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
	//�s�W���q
	/*type
	 * Organization.TYPE_WORK -> ���q */
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
	//�s�W�j�Y�K
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

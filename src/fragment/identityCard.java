package fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import member.card;
import member.othercard;
import myDataBase.dbCard;
import myDataBase.myDB;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cardinfinity.CardInfoActivity;
import com.example.cardinfinity.OtherCardInfoActivity;
import com.example.cardinfinity.R;

import fragment.bussinessCard.cardOnClick;

public class identityCard extends Fragment{
	
	View v;
	dbCard cardHelper;
	ArrayList<othercard> allCard;
	LinearLayout cardContent;
	
	private void initModule(){
		cardHelper = new dbCard(new myDB(getActivity()));
	}
	private Bitmap decodeFile(File f) 
	{
	    try 
	    {
	        // Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f), null, o);

	        // The new size we want to scale to
	        final int REQUIRED_SIZE=100;

	        // Find the correct scale value. It should be the power of 2.
	        int scale = 10;
	        while(o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) 
	        {
	            scale *= 2;
	        }
	        // Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } 
	    catch (FileNotFoundException e) 
	    {
	    	System.out.println(e);
	    }
	    return null;
	}
	
	private void addCardItem(LinearLayout layout, othercard card) throws FileNotFoundException
	{
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity()); 
		View v =layoutInflater.inflate (R.layout.card_item, null);
		
		LinearLayout item = (LinearLayout) v.findViewById(R.id.card1);
		item.setTag(card.getOtherCardID()); // set card id to card item
		
		ImageView icon = (ImageView) v.findViewById(R.id.addBusinessCard_Photo);
		TextView cardName = (TextView) v.findViewById(R.id.cardName);
		TextView cardCompanyAndTitle = (TextView) v.findViewById(R.id.cardCompany);
		
		Bitmap image = decodeFile(new File(card.getOtherCardImage()));
		icon.setImageBitmap(image);
		
		cardName.setText(card.getOtherCardName());
		cardCompanyAndTitle.setText(card.getOtherCardDescription());
		
		item.setOnClickListener(new cardOnClick());
	
		layout.addView(v);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
 Bundle savedInstanceState) {
		this.initModule();

	    v = inflater.inflate(R.layout.fragment_identitycard, container, false);
		cardContent = (LinearLayout) v.findViewById(R.id.CardContent);
		cardContent.removeAllViews();
		allCard = cardHelper.getIdentificationCards();
		for(int i=0;i<allCard.size();i++)
		{
			try 
			{
				addCardItem(cardContent , allCard.get(i)); // add card item
				
			} 
			catch (FileNotFoundException e) 
			{
		
				e.printStackTrace();
			}
		}
		allCard.clear();
		return v;
		
    }
	
	@Override
	  public void onResume() {
		super.onResume();
		refreshCard();  // reload card item
	  }
	
	private void refreshCard()
	{
		cardContent.removeAllViews();
		
		allCard = cardHelper.getIdentificationCards();
		for(int i=0;i<allCard.size();i++)
		{
			try 
			{
				addCardItem(cardContent , allCard.get(i));
				
			} 
			catch (FileNotFoundException e) 
			{
		
				e.printStackTrace();
			}
		}
	}
	
	
	class cardOnClick implements View.OnClickListener
	{
		String cardID;
		@Override
		public void onClick(View v) {
			cardID = (String) v.getTag();
			v.setAlpha((float) 0.7);
		
			Intent intent = new Intent(getActivity(), OtherCardInfoActivity.class);
	        intent.putExtra("CardID", cardID);
			startActivity(intent);
		}

	}
}

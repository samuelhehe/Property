package xj.property.widget.pinyinsearch.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.List;

import xj.property.R;
import xj.property.beans.InvitedNeighborBean;
import xj.property.utils.other.UserUtils;
import xj.property.widget.pinyinsearch.model.Contacts;
import xj.property.widget.pinyinsearch.utils.ViewUtil;
import xj.property.widget.pinyinsearch.view.QuickAlphabeticBar;


/**
 * 联系人适配
 */
public class ContactsAdapter extends ArrayAdapter<Contacts> implements SectionIndexer{
	//public static final String PINYIN_FIRST_LETTER_DEFAULT_VALUE="#";
	private Context mContext;
	private int mTextViewResourceId;
	private List<Contacts> mContacts;
	private OnContactsAdapter mOnContactsAdapter;
	
	public interface OnContactsAdapter{
		//void onContactsSelectedChanged(List<Contacts> contacts);
		void onAddContactsSelected(Contacts contacts);
		void onRemoveContactsSelected(Contacts contacts);
		void onContactsCall(Contacts contacts);
		void onContactsSms(Contacts contacts);

		void onContactsRefreshView();

        void onInviteNeighbors(Contacts contacts);

	}
	
	public ContactsAdapter(Context context, int textViewResourceId,
			List<Contacts> contacts) {
		super(context, textViewResourceId, contacts);
		mContext=context;
		mTextViewResourceId=textViewResourceId;
		mContacts=contacts;
	
	}
	
	public OnContactsAdapter getOnContactsAdapter() {
		return mOnContactsAdapter;
	}

	public void setOnContactsAdapter(OnContactsAdapter onContactsAdapter) {
		mOnContactsAdapter = onContactsAdapter;
	}
	
	public void clearSelectedContacts(){
		//clear data
		for(Contacts contacts:mContacts){
			contacts.setSelected(false);
			
			//other phoneNumber
			
			if(null!=contacts.getNextContacts()){
				Contacts currentContact=contacts.getNextContacts();
				Contacts nextContact=null;
				while(null!=currentContact){
					currentContact.setSelected(false);
					nextContact=currentContact;
					currentContact=nextContact.getNextContacts();
				}
			}
		}
		
		//refresh view
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		ViewHolder viewHolder;
		Contacts contacts=getItem(position);
		if(null==convertView){

			view=LayoutInflater.from(mContext).inflate(mTextViewResourceId, null);
			viewHolder=new ViewHolder();
			viewHolder.mAlphabetTv=(TextView)view.findViewById(R.id.alphabet_text_view);
			viewHolder.mContactsMultiplePhoneOperationPromptIv=(ImageView)view.findViewById(R.id.contacts_multiple_phone_operation_prompt_image_view);
			viewHolder.mSelectContactsIconIv =(ImageView) view.findViewById(R.id.select_contacts_iv);
			viewHolder.mNameTv=(TextView) view.findViewById(R.id.name_text_view);
			viewHolder.mPhoneNumber=(TextView) view.findViewById(R.id.phone_number_text_view);
			viewHolder.mOperationViewTv =(TextView) view.findViewById(R.id.operation_view_image_view);
//			viewHolder.mOperationViewLayout=(View) view.findViewById(R.id.operation_view_layout);
//			viewHolder.mCallIv=(ImageView) view.findViewById(R.id.call_image_view);
//			viewHolder.mSmsIv=(ImageView) view.findViewById(R.id.sms_image_view);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}
        if(!TextUtils.isEmpty(contacts.getRegistAvatar())){
            ImageLoader.getInstance().displayImage(contacts.getRegistAvatar(),viewHolder.mSelectContactsIconIv, UserUtils.options);
        }else{
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.head_portrait_personage,viewHolder.mSelectContactsIconIv);
        }
        if(!TextUtils.isEmpty(contacts.getRegistNickname())){
            viewHolder.mNameTv.setText(contacts.getRegistNickname());
            viewHolder.mOperationViewTv.setText("已邀请");
            viewHolder.mOperationViewTv.setTextColor(Color.GRAY);
            viewHolder.mOperationViewTv.setBackground(null);
            viewHolder.mOperationViewTv.setEnabled(false);
        }

		//show the first alphabet of name
		showAlphabetIndex(viewHolder.mAlphabetTv, position, contacts);
		//show name and phone number
		switch (contacts.getSearchByType()) {
		case SearchByNull:
			ViewUtil.showTextNormal(viewHolder.mNameTv, contacts.getName());
			
			if(false==contacts.isBelongMultipleContactsPhone()){
				ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
				ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber());
			}else{
				if(true==contacts.isFirstMultipleContacts()){
					if(true==contacts.getNextContacts().isHideMultipleContacts()){
						ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
						ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber()+mContext.getString(R.string.phone_number_count, Contacts.getMultipleNumbersContactsCount(contacts)+1));
					}else{
						ViewUtil.showView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
						ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber()+"("+mContext.getString(R.string.click_to_hide)+")");
					}
				}else{
					if(false==contacts.isHideMultipleContacts()){
						ViewUtil.invisibleView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
					}else{
						ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
					}
					ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber());
				}
			}
			break;
		case SearchByPhoneNumber:
			ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
			ViewUtil.showTextNormal(viewHolder.mNameTv, contacts.getName());
			ViewUtil.showTextHighlight(viewHolder.mPhoneNumber, contacts.getPhoneNumber(), contacts.getMatchKeywords().toString());
			break;
		case SearchByName:
			ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
			ViewUtil.showTextHighlight(viewHolder.mNameTv, contacts.getName(), contacts.getMatchKeywords().toString());
			ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber());
			break;
		default:
			break;
		}

//		viewHolder.mSelectContactsIconIv.setTag(position);
//		viewHolder.mSelectContactsIconIv.setChecked(contacts.isSelected());
//		viewHolder.mSelectContactsIconIv.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                int position = (Integer) buttonView.getTag();
//                Contacts contacts = getItem(position);
//                if ((true == isChecked) && (false == contacts.isSelected())) {
//                    contacts.setSelected(isChecked);
//                    addSelectedContacts(contacts);
//
//                } else if ((false == isChecked) && (true == contacts.isSelected())) {
//                    contacts.setSelected(isChecked);
//                    removeSelectedContacts(contacts);
//                } else {
//                    return;
//                }
//            }
//        });
		
		viewHolder.mOperationViewTv.setTag(position);

        /// 根据下边内容的显示隐藏进行设置箭头朝上朝下
//		int resid=(true==contacts.isHideOperationView())?(R.drawable.arrow_down):(R.drawable.arrow_up);
//		viewHolder.mOperationViewTv.setBackgroundResource(resid);

//        if(true==contacts.isHideOperationView()){
//			ViewUtil.hideView(viewHolder.mOperationViewLayout);
//		}else{
//			ViewUtil.showView(viewHolder.mOperationViewLayout);
//		}
		viewHolder.mOperationViewTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Contacts contacts = getItem(position);
//				contacts.setHideOperationView(!contacts.isHideOperationView());

                if (null != mOnContactsAdapter) {
//                    mOnContactsAdapter.onContactsRefreshView();
                    mOnContactsAdapter.onInviteNeighbors(contacts);
                }else{
                    Log.e("mOnContactsAdapter", "mOnContactsAdapter is null error ");
                }

            }
        });

//		viewHolder.mCallIv.setTag(position);
//		viewHolder.mCallIv.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int position = (Integer) v.getTag();
//				Contacts contacts = getItem(position);
//				if(null!=mOnContactsAdapter){
//					mOnContactsAdapter.onContactsCall(contacts);
//				}
//
//			}
//		});
//
//		viewHolder.mSmsIv.setTag(position);
//		viewHolder.mSmsIv.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int position = (Integer) v.getTag();
//				Contacts contacts = getItem(position);
//				if(null!=mOnContactsAdapter){
//					mOnContactsAdapter.onContactsSms(contacts);
//				}
//			}
//		});

		return view;
	}
	


	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		Contacts contacts=null;
		if(QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER==section){
			return 0;
		}else{
			int count=getCount();
			for(int i=0; i<count; i++){
				contacts=getItem(i);
				char firstChar=contacts.getSortKey().charAt(0);
				if(firstChar==section){
					return i;
				}
			}
		}
		
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private class ViewHolder{

		TextView mAlphabetTv;

		ImageView mContactsMultiplePhoneOperationPromptIv;

		ImageView mSelectContactsIconIv;


        TextView mNameTv;

        TextView mPhoneNumber;

        TextView mOperationViewTv;
		
//		View mOperationViewLayout;
//		ImageView mCallIv;
//		ImageView mSmsIv;
	}
	
	private void showAlphabetIndex(TextView textView, int position, final Contacts contacts){
		if((null==textView)||position<0||(null==contacts)){
			return;
		}
		String curAlphabet=getAlphabet(contacts.getSortKey());
		if(position>0){
			Contacts preContacts=getItem(position-1);
			String preAlphabet=getAlphabet(preContacts.getSortKey());
			if(curAlphabet.equals(preAlphabet)||(Contacts.SearchByType.SearchByNull!=contacts.getSearchByType())){
				textView.setVisibility(View.GONE);
				textView.setText(curAlphabet);
			}else{
				textView.setVisibility(View.VISIBLE);
				textView.setText(curAlphabet);
			}
		}else {
			if((Contacts.SearchByType.SearchByNull==contacts.getSearchByType())){
				textView.setVisibility(View.VISIBLE);
				textView.setText(curAlphabet);
			}else{
				textView.setVisibility(View.GONE);
			}
		}
		
		return ;
	}
	
	private String getAlphabet(String str){
		if((null==str)||(str.length()<=0)){
			return String.valueOf(QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER);
		}
		String alphabet=null;
		char chr=str.charAt(0);
		if (chr >= 'A' && chr <= 'Z') {
			alphabet = String.valueOf(chr);
		} else if (chr >= 'a' && chr <= 'z') {
			alphabet = String.valueOf((char) ('A' + chr - 'a'));
		} else {
			alphabet = String.valueOf(QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER);
		}
		return alphabet;
	}
	
	private boolean addSelectedContacts(Contacts contacts){
		
		
		do{
			if(null==contacts){
				break;
			}
			
			if(null!=mOnContactsAdapter){
				mOnContactsAdapter.onAddContactsSelected(contacts);
			}
			
			return true;
		}while(false);
		
		return false;
	
	}
	
	private void removeSelectedContacts(Contacts contacts){
		if(null==contacts){
			return;
		}
		
		if(null!=mOnContactsAdapter){
			mOnContactsAdapter.onRemoveContactsSelected(contacts);
		}
	}
}

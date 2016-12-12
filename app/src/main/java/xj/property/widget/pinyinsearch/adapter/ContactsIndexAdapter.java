package xj.property.widget.pinyinsearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import xj.property.R;
import xj.property.widget.pinyinsearch.model.Contacts;


public class ContactsIndexAdapter extends ArrayAdapter<Contacts> {
	private Context mContext;
	private int mTextViewResourceId;
	private List<Contacts> mContacts;
	private OnContactsIndexAdapter mOnContactsIndexAdapter;

	public interface OnContactsIndexAdapter{
		public void onIndexKeyClick(final Contacts contacts);
	}
	
	public ContactsIndexAdapter(Context context, int textViewResourceId,
			List<Contacts> contacts) {
		super(context, textViewResourceId, contacts);
		mContext = context;
		mTextViewResourceId = textViewResourceId;
		mContacts = contacts;
		mOnContactsIndexAdapter=null;
	}

	public OnContactsIndexAdapter getOnContactsIndexAdapter() {
		return mOnContactsIndexAdapter;
	}

	public void setOnContactsIndexAdapter(
			OnContactsIndexAdapter onContactsIndexAdapter) {
		mOnContactsIndexAdapter = onContactsIndexAdapter;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder;
		Contacts contacts = getItem(position);
		if (null == convertView) {
			view = LayoutInflater.from(mContext).inflate(mTextViewResourceId,
					null);
			viewHolder = new ViewHolder();
			viewHolder.mIndexKeyTv = (TextView) view
					.findViewById(R.id.index_value_text_view);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.mIndexKeyTv.setText(String.valueOf(contacts.getName().charAt(0)));
		viewHolder.mIndexKeyTv.setTextColor(mContext.getResources().getColor(
				R.color.black_deep));
		viewHolder.mIndexKeyTv.setTag(position);
		viewHolder.mIndexKeyTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = (Integer) v.getTag();
				Contacts cs = getItem(pos);
				clickIndexKey(cs);
			}
		});

		return view;
	}
	
	private class ViewHolder {
		TextView mIndexKeyTv;
	}

	private void clickIndexKey(final Contacts contacts) {
		if (null == contacts) {
			return;
		}

		if(null!=mOnContactsIndexAdapter){
		/*Toast.makeText(mContext,PinyinUtil.getFirstCharacter(contacts.getNamePinyinUnits()),
				Toast.LENGTH_SHORT).show();*/
			mOnContactsIndexAdapter.onIndexKeyClick(contacts);
		}
	}

}

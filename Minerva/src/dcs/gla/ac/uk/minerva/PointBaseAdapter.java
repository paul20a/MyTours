package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 
 * 
 * @author Paul Cairney
 *
 */
public class PointBaseAdapter extends BaseAdapter {
	private LayoutInflater lInflater;
	private ArrayList<Object> content;
	private static MinervaLruCache mLruCache;	
	
	/**
	 * @param context
	 *            - Context
	 * @param in
	 *            - ArrayList of objects to display
	 */
	public PointBaseAdapter(Context context,ArrayList<Object> in) {
		content=in;
		lInflater = LayoutInflater.from(context);
		mLruCache=new MinervaLruCache();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return content.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		Object o=null;
		if(this.getCount()!=0) {
			o=content.get(position);
		}
		return o;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// holder to represent a single row
		ViewHolder holder;
		if (convertView == null) {
			// set up a new view consisting of textViews, assign the holder to
			// it.
			convertView = lInflater.inflate(R.layout.row_layout, parent, false);
			holder = new ViewHolder();
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.txtViewRowTitle);
			holder.thumbImageView = (ImageView) convertView
					.findViewById(R.id.imageViewThumbnail);

			convertView.setTag(holder);
		} else {
			// assign the holder to an existing view
			holder = (ViewHolder) convertView.getTag();
		}
		// update the view
		Waypoint item = ((Waypoint) getItem(position));
		holder.nameTextView.setText(item.getTitle());
		LoadBitmap(holder, item);
		return convertView;
	}

	/**
	 * @param holder
	 *            - the entry to load the bitmap into
	 * @param item
	 *            - waypoint to load image of
	 */
	public void LoadBitmap(ViewHolder holder, Waypoint item) {
		//set variables
		Context context = lInflater.getContext();
		Resources r = context.getResources();
		int rID = r.getIdentifier(item.getImage(), "raw",
				context.getPackageName());
		Bitmap bitmap = mLruCache.getCachedBitmap(String.valueOf(rID));
		//If the bitmap is null
		if (bitmap != null) {
			//set the image and return
			holder.thumbImageView.setImageBitmap(bitmap);
			return;
		}
		// otherwise cancel any potential work
		else if (BitmapProcessor.cancelPotentialWork(rID, holder.thumbImageView)) {
			final BitmapProcessor task = new BitmapProcessor(
					holder.thumbImageView, r,mLruCache);
			task.execute(rID);
			Bitmap b = null;
			//create an async drawable from the task
			final AsyncDrawable asyncDrawable = new AsyncDrawable(r, b, task);
			//set the image of the imageview to the asyncdrawable
			holder.thumbImageView.setImageDrawable(asyncDrawable);
		}
	}

	/**
	 *static class to hold an entry in the list
	 */
	static class ViewHolder {
		TextView nameTextView;
		ImageView thumbImageView;
	}
}

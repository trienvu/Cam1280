package studio.elnino.com.cameditor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by trien on 8/30/2015.
 */
public class PhotosAdapter extends PagerAdapter {
    Activity context;
    LayoutInflater inflater;
    File[] mFiles;


    public PhotosAdapter(Activity context, File[] items) {
        this.context = context;
        this.mFiles = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mFiles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = inflater.inflate(R.layout.photo_item, container, false);
        final ImageZoom imageView = (ImageZoom) convertView.findViewById(R.id.imageView);

        File file = mFiles[position];
        if (file.getAbsolutePath() != null)
            Glide.with(context)
                    .load(file.getAbsolutePath())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap arg0, GlideAnimation<? super Bitmap> arg1) {
                            imageView.setImageBitmap(arg0);
                        }
                    });


        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    public void refreshData(File[] files) {
        Log.d("XX", "New size: " + files.length);
        mFiles = files.clone();
        Log.d("XX", "clone size: " + mFiles.length);
        notifyDataSetChanged();
    }
}
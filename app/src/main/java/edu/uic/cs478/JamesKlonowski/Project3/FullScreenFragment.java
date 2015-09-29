package edu.uic.cs478.JamesKlonowski.Project3;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class FullScreenFragment extends Fragment {
    public Intent intent;
    public ImageView mImageView;
    public static Bitmap mBitmap;

    public static FullScreenFragment newInstance(ImageView imageView){
        FullScreenFragment fullScreenFragment = new FullScreenFragment();
        mBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        return fullScreenFragment;
    }

    public static void replaceImage(ImageView imageView){
        mBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_screen, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageview);
        mImageView.setImageBitmap(mBitmap);
        return view;
    }



}

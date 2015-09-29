package edu.uic.cs478.JamesKlonowski.Project3;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;



public class BottomFragment extends Fragment {
    private final int NUM_IMAGES = 6;
    private GridView gridView;
    private String[] imageURLs;
    private Bitmap[] animals = new Bitmap[NUM_IMAGES];
    private Bitmap[] flowers = new Bitmap[NUM_IMAGES];
    private Bitmap[] cars = new Bitmap[NUM_IMAGES];
    private int mCurrentIndex = -1;
    private ImageSelectionListener mListener = null;
    private Activity mActivity;
    private FragmentManager fragmentManager;

    public interface ImageSelectionListener{
        public void onImageSelection(ImageView imageView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bottom, container, false);
        gridView = (GridView) v.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onImageSelection((ImageView) view);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        mActivity = a;
        try{
            //set the listener
            mListener = (ImageSelectionListener) a;
        }catch (ClassCastException e){
            throw new ClassCastException(a.toString()
                    + " must implement OnArticleSelectedListener");
        }
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    public void updateImages(int index) {
        switch(index){
            case 0: mCurrentIndex = index;
                    imageURLs = getActivity().getResources().getStringArray(R.array.animals);
                    gridView.setAdapter(new ImageAdapter(getActivity(), animals));
                    break;
            case 1: mCurrentIndex = index;
                    imageURLs = getActivity().getResources().getStringArray(R.array.flowers);
                    gridView.setAdapter(new ImageAdapter(getActivity(), flowers));
                    break;
            case 2: mCurrentIndex = index;
                    imageURLs = getActivity().getResources().getStringArray(R.array.cars);
                    gridView.setAdapter(new ImageAdapter(getActivity(), cars));
                    break;
            default:    return;
        }
    }

    public int getShownIndex(){
        return mCurrentIndex;
    }


    public class ImageAdapter extends BaseAdapter{
        private Bitmap[] bitmaps;
        private Context mContext;
        private DownloadImagesTask downloadImagesTask;

        public ImageAdapter(Context context, Object previousList){
            mContext = context;
            downloadImagesTask = new DownloadImagesTask();


            // If we've already downloaded the images, just use them instead of re-downloading
            if(previousList != null){
                bitmaps = (Bitmap[]) previousList;
                downloadImagesTask.execute(bitmaps);
                return;
            }
            bitmaps = new Bitmap[imageURLs.length];
            downloadImagesTask.execute(bitmaps);
        }


        @Override
        public int getCount(){  return bitmaps.length;  }
        @Override
        public Object getItem(int position){    return bitmaps[position];   }
        @Override
        public long getItemId(int position){   return position; }
        // Returns array of Bitmap


        public Object getData(){
            // cancel task if it hasn't finished by now.
            if(downloadImagesTask != null && downloadImagesTask.getStatus() != AsyncTask.Status.FINISHED){
                downloadImagesTask.cancel(true);
            }
            // Return the bitmap array
            return bitmaps;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ImageView imgView;
            Bitmap bitmap = bitmaps[position];

            // Check if we can recycle the old view
            if(convertView == null){
                imgView = new ImageView(mContext);
                imgView.setLayoutParams(new GridView.LayoutParams(300,300));
            }else{
                // We can recycle
                imgView = (ImageView) convertView;
            }

            // Is the image already downloaded?
            if(bitmap == null){
                //image isnt cached, so set view as blank
                imgView.setImageResource(R.drawable.blank);
                imgView.setScaleType(ImageView.ScaleType.CENTER);
            }else{
                //image IS cached, so use it
                imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgView.setImageBitmap(bitmap);
            }
            return imgView;
        }

        // Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
        private void cacheUpdated(){
            this.notifyDataSetChanged();
        }

        // Download, subsample, and return the bitmap requested
        private Bitmap loadBitmap(String url){
            Bitmap bitmap = null;

            // Subsample so we dont run out of memory
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            // Connect to the URL and get the image
            try{
                URL u = new URL(url);
                URLConnection urlConnection = u.openConnection();
                urlConnection.connect();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
                // Decode and Subsample
                bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
                bufferedInputStream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            // Returns the downloaded bitmap
            return bitmap;
        }

        // AsyncTask
        private class DownloadImagesTask extends AsyncTask<Bitmap, Void, Void> {
            @Override
            protected Void doInBackground(Bitmap... params) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                for (int i = 0; i < NUM_IMAGES; i++) {
                    // Stop processing if task was cancelled
                    if (isCancelled()) return null;
                    // Skip an image if it's already been generated
                    if (params[i] != null) {
                        continue;
                    }
                    // Download and generate bitmap
                    params[i] = loadBitmap(imageURLs[i]);
                    publishProgress();
                }
                return null;
            }
            // Update the UI thread when publishProgress wants to
            @Override
            protected void onProgressUpdate(Void... param) {
                cacheUpdated();
            }


        }
    }

}

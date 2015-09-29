package edu.uic.cs478.JamesKlonowski.Project3;



import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import edu.uic.cs478.JamesKlonowski.Project3.TopFragment.ListSelectionListener;
import edu.uic.cs478.JamesKlonowski.Project3.BottomFragment.ImageSelectionListener;


public class MainActivity extends Activity implements ListSelectionListener, ImageSelectionListener {
    public static String[] mOptionArray;
    private BottomFragment mBottomFragment;
    private FullScreenFragment fullScreenFragment;
    private FragmentManager mFragmentManager;
    private FrameLayout mTopFrameLayout, mBottomFrameLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the string array of the options
        mOptionArray = getResources().getStringArray(R.array.Options);

        // Get the layout of the top fragment
        mTopFrameLayout = (FrameLayout) findViewById(R.id.top_fragment_container);
        mBottomFrameLayout = (FrameLayout) findViewById(R.id.bottom_fragment_container);

        // Get reference to FragmentManager
        mFragmentManager = getFragmentManager();


        // Begin a new FragmentTransaction
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // Add top fragment to the layout and commit
        fragmentTransaction.add(R.id.top_fragment_container, new TopFragment());
        //fragmentTransaction.add(R.id.bottom_fragment_container, new BottomFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

       /* mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged(){
                setLayout();
            }
        });
    }

    public void setLayout(){
        */
    }

    @Override
    public void onListSelection(int index){
        if(mBottomFragment == null){
            mBottomFragment = new BottomFragment();
        }
        //if bottom fragment isnt added yet, do it now.
        if(!mBottomFragment.isAdded()){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            //Add bottomFragment to the layout
            fragmentTransaction.add(R.id.bottom_fragment_container, mBottomFragment);
            //Add transaction to the backstack
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }
        if(mBottomFragment.getShownIndex() != index){
            //if the selected index is NOT shown, tell bottom fragment to show it
            mBottomFragment.updateImages(index);
        }
    }


    // User selects an image, so go to fullscreen fragment
    @Override
    public void onImageSelection(ImageView imageView){
        if(fullScreenFragment == null) {
            fullScreenFragment = FullScreenFragment.newInstance(imageView);
        }else{
            fullScreenFragment.replaceImage(imageView);
        }
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.bottom_fragment_container, fullScreenFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
    }


}

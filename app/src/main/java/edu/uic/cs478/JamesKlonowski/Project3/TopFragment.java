/*******************************
 * James Klonowski
 * CS478 - Project 3
 * Downloading images from web using AsyncTask
 * 3/30/2015
 */

package edu.uic.cs478.JamesKlonowski.Project3;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class TopFragment extends ListFragment {
    private ListSelectionListener mListener = null;
    private Button mButton;
    private Activity mActivity;
    private int mCurrentIndex = -1;
    private int mPreviousIndex = -1;

    // Callback interface that lets this Fragment notify the MainActivity when users selects an item
    public interface ListSelectionListener {
        public void onListSelection(int index);
    }



    // Called when users selects an item
    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        mPreviousIndex = mCurrentIndex;
        mCurrentIndex = pos;
        getListView().setItemChecked(pos, true);    // selected item has been checked
        mListener.onListSelection(pos);             // Let MainActivity know that item at position pos is selected
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        mActivity = a;
        try{
            //set the listener
            mListener = (ListSelectionListener) a;
        }catch (ClassCastException e){
            throw new ClassCastException(a.toString()
                    + " must implement OnArticleSelectedListener");
        }
    }


/*
    public void onBackPressed(){
        getListView().clearChoices();
        getListView().setItemChecked(mPreviousIndex, true);
        mListener.onListSelection(mPreviousIndex);
    }
*/

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("selected", mCurrentIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        /*if(savedInstanceState!=null){
            int selected = savedInstanceState.getInt("selected");
            onListItemClick(getListView(), null, selected, selected);
        }*/

        //set the list adapter for the ListView
        setListAdapter(new ArrayAdapter<String>(mActivity, R.layout.fragment_top, MainActivity.mOptionArray));

        //set the list choice mode to only let one option to be selected at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mButton = (Button) mActivity.findViewById(R.id.status_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "I'm alive!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

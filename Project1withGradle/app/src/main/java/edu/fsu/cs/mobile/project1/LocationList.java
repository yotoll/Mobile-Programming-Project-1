
package edu.fsu.cs.mobile.project1;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.fsu.cs.mobile.project1.R;


public class LocationList extends ListFragment {

    ListView list;
    public ArrayList<String> locations;
    public ArrayAdapter<String> adapt;
    String messageContent;
    String messageSender;

    public Button test;

    private int taken;
    public static final String TAG = LocationList.class.getCanonicalName();

    public LocationList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location_list, container, false);
        locations = new ArrayList<String>();
        locations.add("No results found");
        adapt = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, locations);
        taken = 0;
        setListAdapter(adapt);
        list = new ListView(getActivity());
        
        // Phone Number and Message from BroadCast Receiver
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            messageContent = extras.getString("msgContent");
            messageSender = extras.getString("phoneNum");

        }


        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id)
    {
        super.onListItemClick(l,v,pos,id);
        if(taken > 0)
        {
            Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_SHORT).show();
            //pull up location
            locations.get(pos);
        }

    }

    public void addResult(String x)
    {
        Log.i("###LOCATION LIST###","addingResult");
        if(taken==0)
        {
            //replace with location
            locations.add(0,x);
            locations.remove(1);
            taken++;
            adapt = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, locations);
            setListAdapter(adapt);
        }
        else
        {
            //push down one
            //add to front


            locations.add(0,x);

            adapt = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, locations);
            setListAdapter(adapt);
            if(taken < 5)
                taken++;
            else
                locations.remove(5);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

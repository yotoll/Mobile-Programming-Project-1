
package edu.fsu.cs.mobile.project1;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class LocationList extends ListFragment {

    ListView list;
    public ArrayList<String> locations, contacts;
    public ArrayAdapter<String> adapt;

    String messageContent;
    String messageSender;

    private int taken;
    public static final String TAG = LocationList.class.getCanonicalName();
    public static final String Prefs = "RecentLocFile";
    public static final String TAKEN = "taken";

    public LocationList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location_list, container, false);

        //Recent location and contact it was from
        locations = new ArrayList<String>();
        contacts = new ArrayList<String>();

        SharedPreferences preferences = getActivity().getSharedPreferences(TAKEN, Activity.MODE_PRIVATE);
        taken = preferences.getInt(TAKEN,0);

        //Reading from SharedPreferences for previous locations
        SharedPreferences settings = getActivity().getSharedPreferences(Prefs, 0);


        if (taken > 0)
        {
            locations.add(settings.getString("FirstLoc", "Empty"));
            contacts.add(settings.getString("FirstCont", "No Results found"));
            if(taken>1)
            {
                locations.add(settings.getString("SecondLoc", "Empty"));
                contacts.add(settings.getString("SecondCont", "No Results found"));
            }
            if(taken>2)
            {
                locations.add(settings.getString("ThirdLoc", "Empty"));
                contacts.add(settings.getString("ThirdCont", "No Results found"));

            }
            if(taken>3)
            {
                locations.add(settings.getString("FourthLoc", "Empty"));
                contacts.add(settings.getString("FourthCont", "No Results found"));
            }
            if(taken>4)
            {
                locations.add(settings.getString("FifthLoc", "Empty"));
                contacts.add(settings.getString("FifthCont", "No Results found"));
            }
        }
        else
        {
            contacts.add("No results found");
        }

        adapt = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, contacts);

        setListAdapter(adapt);
        list = new ListView(getActivity());

        // Phone Number and Message from BroadCast Receiver
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            messageContent = extras.getString("msgContent");
            messageSender = extras.getString("phoneNum");
            addResult(messageContent,messageSender);

        }

        return v;
    }


    //Send user over to recent location
    @Override
    public void onListItemClick(ListView l, View v, int pos, long id)
    {
        super.onListItemClick(l,v,pos,id);
        double lat, lon;

        if(taken > 0)
        {
            //pull up location
            String del = "[ ]+";
            String [] temp = locations.get(pos).split(del);

            lat = Double.parseDouble(temp[0]);
            lon = Double.parseDouble(temp[1]);

            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("LAT", lat);
            intent.putExtra("LON", lon);
            startActivity(intent);

        }

    }


    //Add on to contact list
    public void addResult(String loc, String con)
    {

        String x, y;

        x=loc;
        y=con;

        if(taken==0)
        {
            //replace with location
            locations.add(0,x);

            contacts.add(0,y);
            contacts.remove(1);

            taken++;
            adapt = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, contacts);
            setListAdapter(adapt);
        }
        else
        {

            locations.add(0,x);
            contacts.add(0,y);

            adapt = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, contacts);
            setListAdapter(adapt);

            if(taken < 5)
                taken++;
            else
            {
                locations.remove(5);
                contacts.remove(5);
            }
        }
        SharedPreferences preferences = getActivity().getSharedPreferences(TAKEN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TAKEN, taken);
        editor.commit();
    }


    @Override
    public void onDetach() {
        //Write to Shared Prefs?
        super.onDetach();


        //Write recent locations to the shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences(Prefs,0);
        SharedPreferences.Editor edit = preferences.edit();

        edit.putInt("Taken",taken);

        if(taken>0)
        {
            edit.putString("FirstLoc",locations.get(0));
            edit.putString("FirstCont",contacts.get(0));
        }

        if(taken>1)
        {
            edit.putString("SecondLoc",locations.get(1));
            edit.putString("SecondCont",contacts.get(1));
        }

        if(taken>2)
        {
            edit.putString("ThirdLoc",locations.get(2));
            edit.putString("ThirdCont",contacts.get(2));
        }

        if(taken>3)
        {
            edit.putString("FourthLoc",locations.get(3));
            edit.putString("FourthCont",contacts.get(3));
        }

        if(taken>4)
        {
            edit.putString("FifthLoc", locations.get(4));
            edit.putString("FifthCont", contacts.get(4));
        }
        edit.commit();
    }

}

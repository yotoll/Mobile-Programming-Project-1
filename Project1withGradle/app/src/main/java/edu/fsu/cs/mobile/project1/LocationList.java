
package edu.fsu.cs.mobile.project1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import edu.fsu.cs.mobile.project1.R;


public class LocationList extends ListFragment {

    ListView list;
    public ArrayList<String> locations, contacts;
    public ArrayAdapter<String> adapt;


    private int taken;
    public static final String TAG = LocationList.class.getCanonicalName();
    public static final String Prefs = "RecentLocFile";
    public static final String TAKEN = "Taken";

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
            locations.add("Empty");
            contacts.add("No results found");
        }

        adapt = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, contacts);
        setListAdapter(adapt);
        list = new ListView(getActivity());

        return v;
    }


    //Send user over to recent location
    @Override
    public void onListItemClick(ListView l, View v, int pos, long id)
    {
        super.onListItemClick(l,v,pos,id);

        if(taken > 0)
        {
            //Parse string for location
            String temp = locations.get(pos).replace("http://maps.google.com/maps?saddr=",
                    "");

            if(temp.contains("URGENT "))
            {
                temp.replace("URGENT ","");
            }

            //pull up location
            String del = ",";
            String [] loca = locations.get(pos).split(del);

            double lat = Double.parseDouble(loca[0]);
            double lon = Double.parseDouble(loca[1]);

            //Send location to MapsActivity to pull it up
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("LAT", lat);
            intent.putExtra("LON", lon);
            startActivity(intent);
        }

    }


    //Add on to recent location list
    public void addResult(String loc, String con)
    {
        String x, y;

        x=loc;
        y=con;

        if(taken==0)
        {   //First result case
            //replace with location
            locations.add(0,x);
            locations.remove(1);

            contacts.add(0,y);
            contacts.remove(1);

            taken++;
            adapt = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, contacts);

            setListAdapter(adapt);
        }
        else
        {
            //1 or more results
            locations.add(0,x);
            contacts.add(0,y);

            adapt = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, contacts);
            setListAdapter(adapt);

            //Keep only the 5 most recent locations
            if(taken < 5)
                taken++;
            else
            {
                locations.remove(5);
                contacts.remove(5);
            }
            setPreferences();
        }
    }

    public void setPreferences()
    {
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

    @Override
    public void onDetach() {
        //Write to Shared Prefs?
        super.onDetach();
        setPreferences();
        //Write recent locations to the shared preferences

    }

}

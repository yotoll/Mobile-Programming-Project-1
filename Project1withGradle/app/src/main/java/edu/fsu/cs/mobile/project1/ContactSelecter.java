package edu.fsu.cs.mobile.project1;

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactSelecter extends ListActivity {

    public ListView contactList;
    public Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contact_selecter);

        // querying the content provider
        c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" ASC"); // alphabetizes the results

        // selecting name and number to display
        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        int [] to = {android.R.id.text1, android.R.id.text2};

        // creating and adapter for the cursor
        final SimpleCursorAdapter liadap = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,c,from,to);

        setListAdapter(liadap);

        contactList = getListView();
        contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // setting onItemClickListener for the list view
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                c.moveToPosition(i); // move to selected position
                String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                Toast.makeText(getApplicationContext(),name + " " + number, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

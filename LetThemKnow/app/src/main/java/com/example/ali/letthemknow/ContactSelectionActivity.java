package com.example.ali.letthemknow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ContactSelectionActivity extends AppCompatActivity {

    public RecyclerView contactSelection;
    public Button saveContactsButton;
    final static int CONTACTS_REQUEST = 97;
    final static String SAVED_CONTACTS = "SavedContacts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);

        contactSelection = (RecyclerView) findViewById(R.id.contactSelectionRecycleView);
        saveContactsButton = (Button) findViewById(R.id.saveContactsButton);
        contactSelection.setAdapter(new TeamRecycleViewAdapter(getContacts()));
        contactSelection.setLayoutManager(new LinearLayoutManager(this));

        saveContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                ArrayList<Contact> selectedContacts = ((TeamRecycleViewAdapter) contactSelection.getAdapter())
                        .getSelectedContacts();
                returnIntent.putParcelableArrayListExtra("CONTACTS",selectedContacts);
                addContactsToPreference(selectedContacts);
                setResult(CONTACTS_REQUEST,returnIntent);
                finish();
            }
        });
    }

    public void addContactsToPreference(ArrayList<Contact> selectedContacts){

        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pr.edit();
        String savedContacts = pr.getString(SAVED_CONTACTS,null);
        Gson gson = new Gson();
        String json;
        if(savedContacts == null) {
            json = gson.toJson(selectedContacts);

        }else{
            Type t = new TypeToken<ArrayList<Contact>>(){}.getType();
            ArrayList<Contact> contacts;
            contacts = gson.fromJson(savedContacts,t);
            Set<Contact> s = new HashSet<>();
            s.addAll(contacts);
            s.addAll(selectedContacts);
            contacts.clear();
            contacts.addAll(s);
            json = gson.toJson(contacts);
        }
        editor.putString(SAVED_CONTACTS, json);
        editor.commit();
    }
    public ArrayList<Contact> getContacts(){


        ArrayList<Contact> contact = new ArrayList<>();
        Cursor contacts =
                 getContentResolver()
                .query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        if(contacts == null){
            Log.e("TEAMFRAGMENT","Error Contacts cursor is null");

        }
        int i = 0;
        contacts.moveToFirst();
        while (contacts.moveToNext()){
            String name = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String id = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));

            if(contacts.getInt(contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){

                Cursor data = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",new String[]{id}
                        , null);
                data.moveToFirst();
                String number ="";
                while (data.moveToNext()){
                    number = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    break;

                }
                if(number.length() > 6) {
                    contact.add(new Contact(name, "" + number));
                    i++;
                }
                if(i > 10)
                    break;
                data.close();

            }

        }
        contact.add(new Contact("",""));
        return contact;
    }
}

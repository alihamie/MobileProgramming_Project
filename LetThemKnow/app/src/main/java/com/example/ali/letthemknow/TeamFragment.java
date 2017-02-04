package com.example.ali.letthemknow;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Ali on 1/31/2017.
 */

public class TeamFragment extends Fragment {

    public FloatingActionButton addContactsButton;
    public static final String ARG_PAGE = "ARG_PAGE";
    public RecyclerView mRecycleView;

    public static TeamFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TeamFragment fragment = new TeamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public ArrayList<Contact> getContactsFromPreference(){

        //get saved contacts
        SharedPreferences readPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = readPrefs.getString(ContactSelectionActivity.SAVED_CONTACTS,null);
        Type t = new TypeToken<ArrayList<Contact>>(){}.getType();
        ArrayList<Contact> contacts;
        if(json != null)
            contacts = gson.fromJson(json,t);
        else{
            contacts = new ArrayList<Contact>();
            contacts.add(new Contact("No team selected please add a team",""));
        }
        return contacts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout, container, false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.teamRecycleView);



        mRecycleView.setAdapter(new TeamRecycleViewAdapter(getContactsFromPreference()));
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        addContactsButton = (FloatingActionButton) view.findViewById(R.id.addConactsButton);

        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_CONTACTS)) {

                    } else {

                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                1);

                    }


                } else{
                    Intent i = new Intent(getContext(),ContactSelectionActivity.class);
                    startActivityForResult(i,1);

                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == ContactSelectionActivity.CONTACTS_REQUEST){

            //ArrayList<Contact> c = data.getParcelableArrayListExtra("CONTACTS");
           // c.addAll(getContactsFromPreference());
            ((TeamRecycleViewAdapter)mRecycleView.getAdapter()).swap(getContactsFromPreference());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(getContext(),ContactSelectionActivity.class);
                    startActivityForResult(i,1);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}

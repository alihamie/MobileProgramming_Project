package com.example.ali.letthemknow;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebugFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebugFragment extends Fragment {


    private Button mCompleteButton;
    private Button mFailButton;

    public final static String COMPLETEMESSAGE = "I HAVE SUCCESFULLY COMPLETED MY TASK TODAY! Lets get a pizza to celebrate!";
    public final static String FAILTASK = "I Have failed to exercise today, please make sure I do it the next time";


    public DebugFragment() {
        // Required empty public constructor
    }


    public static DebugFragment newInstance() {
        DebugFragment fragment = new DebugFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {

            } else {

                requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                        1);

            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_debug, container, false);
        mCompleteButton = (Button) view.findViewById(R.id.complete_task_button);
        mFailButton = (Button) view.findViewById(R.id.fail_task_button);

        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendTextToTeam(true);
            }
        });


        mFailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextToTeam(false);
            }
        });

        // Inflate the layout for this fragment
        return view;
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


    public void sendTextToTeam(Boolean win){

        ArrayList<Contact> contacts = getContactsFromPreference();
        String message;
        if(win)
        message = COMPLETEMESSAGE;
        else message = FAILTASK;
        for(int i = 0; i < contacts.size(); ++i){
            sendSMS(contacts.get(i).getNumber(),message);
        }

    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}

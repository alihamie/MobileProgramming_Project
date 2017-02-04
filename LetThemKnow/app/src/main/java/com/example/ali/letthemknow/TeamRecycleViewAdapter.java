package com.example.ali.letthemknow;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ali on 2/3/2017.
 */

public class TeamRecycleViewAdapter extends RecyclerView.Adapter<TeamRecycleViewAdapter.ViewHolder> {

    ArrayList<Contact> contacts;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView ;
        public TextView numberTextView;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            numberTextView= (TextView) itemView.findViewById(R.id.contact_number);
            view = itemView;
        }
    }


    public void swap(ArrayList<Contact> data){
        contacts.clear();
        contacts.addAll(data);
        notifyDataSetChanged();
    }

    public TeamRecycleViewAdapter(ArrayList<Contact> c){
        contacts = new ArrayList<>(c);
    }

    public ArrayList<Contact> getSelectedContacts(){

        ArrayList<Contact> selected = new ArrayList<Contact>();

        for(Contact c: contacts){
            if(c.isSelected) {
                c.isSelected = false;
                selected.add(c);
            }
        }
        return selected;
    }

    @Override
    public TeamRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.teamlayout,parent,false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final TeamRecycleViewAdapter.ViewHolder holder, int position) {
        final Contact contact = contacts.get(position);

        holder.nameTextView.setText(contact.getName());
        holder.numberTextView.setText(contact.getNumber());

        holder.view.setBackgroundColor(contact.isSelected ? Color.CYAN : Color.WHITE);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.isSelected = !contact.isSelected;
                holder.view.setBackgroundColor(contact.isSelected ? Color.CYAN : Color.WHITE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }



}

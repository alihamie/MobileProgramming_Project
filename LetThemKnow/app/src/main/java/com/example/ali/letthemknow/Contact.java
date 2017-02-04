package com.example.ali.letthemknow;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created by Ali on 2/3/2017.
 */

public class Contact implements Parcelable {

    private String name;
    private String number;
    public boolean isSelected = false;

    public Contact(String name, String number){
        this.name = name;
        this.number = number;

    }


    protected Contact(Parcel in) {
        name = in.readString();
        number = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName(){

        return this.name;
    }

    public String getNumber(){

        return this.number;
    }

    @Override
    public boolean equals(Object o){

        if(o == this) return true;

        Contact c = (Contact) o;

        return (c.getNumber().equals(number) && c.getName().equals(name));

     }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + number.hashCode();

        return result;
    }
}

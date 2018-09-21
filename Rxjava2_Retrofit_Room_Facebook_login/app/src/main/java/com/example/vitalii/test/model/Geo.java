
package com.example.vitalii.test.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geo implements Parcelable {
    public Geo() {
    }

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    @Expose
    private String lat;

    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    @Expose
    private String lng;

    protected Geo(Parcel in) {
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<Geo> CREATOR = new Creator<Geo>() {
        @Override
        public Geo createFromParcel(Parcel in) {
            return new Geo(in);
        }

        @Override
        public Geo[] newArray(int size) {
            return new Geo[size];
        }
    };

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lat);
        dest.writeString(lng);
    }
}

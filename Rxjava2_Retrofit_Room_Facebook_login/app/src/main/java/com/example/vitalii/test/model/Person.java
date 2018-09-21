
package com.example.vitalii.test.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Person implements Parcelable {

    public Person() {
    }

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private int id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "username")
    @SerializedName("username")
    @Expose
    private String username;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    @Expose
    private String email;

    @Embedded
    @SerializedName("address")
    @Expose
    private Address address;

    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    @Expose
    private String phone;

    @ColumnInfo(name = "website")
    @SerializedName("website")
    @Expose
    private String website;

    @Embedded
    @SerializedName("company")
    @Expose
    private Company company;

    protected Person(Parcel in) {
        id = in.readInt();
        name = in.readString();
        username = in.readString();
        email = in.readString();
        this.address = in.readParcelable(Address.class.getClassLoader());
        phone = in.readString();
        website = in.readString();
        this.company = in.readParcelable(Company.class.getClassLoader());

    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeParcelable(address, flags);
        dest.writeString(phone);
        dest.writeString(website);
        dest.writeParcelable(company, flags);
    }
}

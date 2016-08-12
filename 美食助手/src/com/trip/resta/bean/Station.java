package com.trip.resta.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Station implements Parcelable {

	private String name;
	private String navigation;
	private String city;
	private String address;
	private String phone;
	private String avg_price;
	private double lat;
	private double lon;
	private double stars;
	private String good_remarks;
	private String common_remarks;
	private String bad_remarks;
	private String very_bad_remarks;
	private String tags;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeString(navigation);
		dest.writeString(city);
		dest.writeString(address);
		dest.writeString(phone);
		dest.writeString(avg_price);
		dest.writeString(tags);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
		dest.writeDouble(stars);
		dest.writeString(good_remarks);
		dest.writeString(common_remarks);
		dest.writeString(bad_remarks);
		dest.writeString(very_bad_remarks);
	}

	public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {

		@Override
		public Station createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			Station s = new Station();
			s.name = source.readString();
			s.navigation = source.readString();
			s.city = source.readString();
			s.address = source.readString();
			s.phone = source.readString();
			s.avg_price = source.readString();
			s.tags = source.readString();
			s.lat = source.readDouble();
			s.lon = source.readDouble();
			s.stars = source.readDouble();
			s.good_remarks = source.readString();
			s.common_remarks = source.readString();
			s.bad_remarks = source.readString();
			s.very_bad_remarks = source.readString();
			return s;
		}

		@Override
		public Station[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvg_price() {
		return avg_price;
	}

	public void setAvg_price(String avg_price) {
		this.avg_price = avg_price;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getStars() {
		return stars;
	}

	public void setStars(double stars) {
		this.stars = stars;
	}

	public String getGood_remarks() {
		return good_remarks;
	}

	public void setGood_remarks(String good_remarks) {
		this.good_remarks = good_remarks;
	}

	public String getCommon_remarks() {
		return common_remarks;
	}

	public void setCommon_remarks(String common_remarks) {
		this.common_remarks = common_remarks;
	}

	public String getBad_remarks() {
		return bad_remarks;
	}

	public void setBad_remarks(String bad_remarks) {
		this.bad_remarks = bad_remarks;
	}

	public String getVery_bad_remarks() {
		return very_bad_remarks;
	}

	public void setVery_bad_remarks(String very_bad_remarks) {
		this.very_bad_remarks = very_bad_remarks;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public static Parcelable.Creator<Station> getCreator() {
		return CREATOR;
	}

}

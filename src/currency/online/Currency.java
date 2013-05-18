/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package currency.online;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author petroff
 */
public class Currency implements Parcelable {

	private String charCode;
	private String name;
	private Double value;
	private String date;
	private Integer nominal;

	public Currency() {
	}
	
	public void setNominal(Integer nominal) {
		this.nominal = nominal;
	}

	public void setCharCode(String charCode) {
		this.charCode = charCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public Integer getNominal() {
		return this.nominal;
	}

	public String getName() {
		return this.name;
	}

	public String getCharCode() {
		return this.charCode;
	}

	public Double getValue() {
		return this.value;
	}

	public String getDate() {
		return this.date;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(charCode);
		out.writeString(name);
		out.writeDouble(value);
		out.writeString(date);
		out.writeInt(nominal);
	}
	
	public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {

		public Currency createFromParcel(Parcel in) {
			return new Currency(in);
		}

		public Currency[] newArray(int size) {
			return new Currency[size];
		}
	};

	private Currency(Parcel in) {
		charCode = in.readString();
		name = in.readString();
		value = in.readDouble();
		date = in.readString();
		nominal = in.readInt();
	}

	public Currency(String charCode, String name, Double value, String date, Integer nominal) {
		this.charCode = charCode;
		this.name = name;
		this.value = value;
		this.date = date;
		this.nominal = nominal;
	}
}

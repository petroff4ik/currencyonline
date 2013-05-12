package currency.online;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable {
	private String currentCurency;
	List<Currency> currency;

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(currentCurency);
		out.writeList(currency);
	}

	public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>() {
		public MyParcelable createFromParcel(Parcel in) {
			return new MyParcelable(in);
		}

		public MyParcelable[] newArray(int size) {
			return new MyParcelable[size];
		}
	};

	private MyParcelable(Parcel in) {
		currentCurency = in.readString();
		in.readList(currency, null);
	}

	MyParcelable(List<Currency> currency, String currentCurency) {
		this.currentCurency = currentCurency;
		this.currency = currency;
	}
}

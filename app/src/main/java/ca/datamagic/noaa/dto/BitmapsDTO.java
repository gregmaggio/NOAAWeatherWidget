package ca.datamagic.noaa.dto;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class BitmapsDTO implements Parcelable {
    private List<Bitmap> _items = new ArrayList<Bitmap>();

    public BitmapsDTO() {

    }

    public BitmapsDTO(List<Bitmap> items) {
        _items = items;
    }

    public BitmapsDTO(Parcel in) {
        int items = in.readInt();
        for (int ii = 0; ii < items; ii++) {
            Bitmap item = (Bitmap)in.readParcelable(ClassLoader.getSystemClassLoader());
            _items.add(item);
        }
    }

    public Bitmap get(int index) {
        return _items.get(index);
    }

    public int size() {
        return _items.size();
    }

    public List<Bitmap> getItems() {
        return _items;
    }

    public void setItems(List<Bitmap> newVal) {
        _items = newVal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        if ((_items != null) && (_items.size() > 0)) {
            out.writeInt(_items.size());
            for (int ii = 0; ii < _items.size(); ii++) {
                Bitmap item = _items.get(ii);
                out.writeParcelable(item, flags);
            }
        } else {
            out.writeInt(0);
        }
    }

    public static final Parcelable.Creator<BitmapsDTO> CREATOR  = new Parcelable.Creator<BitmapsDTO>() {
        public BitmapsDTO createFromParcel(Parcel in) {
            return new BitmapsDTO(in);
        }
        public BitmapsDTO[] newArray(int size) {
            return new BitmapsDTO[size];
        }
    };
}

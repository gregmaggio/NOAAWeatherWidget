package ca.datamagic.noaa.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class StringListDTO implements Parcelable, Comparable<StringListDTO> {
    private List<String> _items = new ArrayList<String>();

    public StringListDTO() {

    }

    public StringListDTO(List<String> items) {
        _items = items;
    }

    public StringListDTO(Parcel in) {
        int items = in.readInt();
        for (int ii = 0; ii < items; ii++) {
            String item = in.readString();
            _items.add(item);
        }
    }

    public String get(int index) {
        return _items.get(index);
    }

    public int size() {
        return _items.size();
    }

    public List<String> getItems() {
        return _items;
    }

    public void setItems(List<String> newVal) {
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
                String item = _items.get(ii);
                out.writeString(item);
            }
        } else {
            out.writeInt(0);
        }
    }

    @Override
    public int compareTo(@NonNull StringListDTO o) {
        if ((_items != null) && (o._items != null)) {
            if (_items.size() == o._items.size()) {
                for (int ii = 0; ii < _items.size(); ii++) {
                    int result = _items.get(ii).compareTo(o._items.get(ii));
                    if (result != 0) {
                        return result;
                    }
                }
            } else  if (_items.size() < o._items.size()) {
                return  -1;
            } else {
                return 1;
            }
        }
        return 0;
    }

    public static final Parcelable.Creator<StringListDTO> CREATOR  = new Parcelable.Creator<StringListDTO>() {
        public StringListDTO createFromParcel(Parcel in) {
            return new StringListDTO(in);
        }
        public StringListDTO[] newArray(int size) {
            return new StringListDTO[size];
        }
    };
}

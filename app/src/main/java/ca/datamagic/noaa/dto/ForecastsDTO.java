package ca.datamagic.noaa.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.datamagic.noaa.dao.ForecastsDAO;

/**
 * Created by Greg on 4/14/2018.
 */

public class ForecastsDTO implements Parcelable {
    private boolean _cached = false;
    private List<ForecastDTO> _items = null;

    public ForecastsDTO() {

    }

    public ForecastsDTO(Parcel in) {
        _cached = (in.readByte() == (byte)1) ? true : false;
        _items = new ArrayList<ForecastDTO>();
        int items = in.readInt();
        for (int ii = 0; ii < items; ii++) {
            ForecastDTO item = new ForecastDTO();
            item.setDayOfMonth(in.readString());
            item.setDayOfWeek(in.readString());
            item.setSummary(in.readString());
            item.setTemperature(in.readDouble());
            item.setPOP(in.readDouble());
            item.setImageUrl(in.readString());
            item.setWordedForecast(in.readString());
            _items.add(item);
        }
    }

    public boolean isCached() {
        return _cached;
    }

    public void setCached(boolean newVal) {
        _cached = newVal;
    }

    public List<ForecastDTO> getItems() {
        return _items;
    }

    public void setItems(List<ForecastDTO> newVal) {
        _items = newVal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeByte((_cached ? (byte)1 : (byte)0));
        if ((_items != null) && (_items.size() > 0)) {
            out.writeInt(_items.size());
            for (int ii = 0; ii < _items.size(); ii++) {
                ForecastDTO item = _items.get(ii);
                out.writeString(((item.getDayOfMonth() == null) ? "" : item.getDayOfMonth()));
                out.writeString(((item.getDayOfWeek() == null) ? "" : item.getDayOfWeek()));
                out.writeString(((item.getSummary() == null) ? "" : item.getSummary()));
                out.writeDouble(((item.getTemperature() == null) ? Double.NaN : item.getTemperature()));
                out.writeDouble(((item.getPOP() == null) ? Double.NaN : item.getPOP()));
                out.writeString(((item.getImageUrl() == null) ? "" : item.getImageUrl()));
                out.writeString(((item.getWordedForecast() == null) ? "" : item.getWordedForecast()));
            }
        } else {
            out.writeInt(0);
        }
    }

    public static final Parcelable.Creator<ForecastsDTO> CREATOR  = new Parcelable.Creator<ForecastsDTO>() {
        public ForecastsDTO createFromParcel(Parcel in) {
            return new ForecastsDTO(in);
        }

        public ForecastsDTO[] newArray(int size) {
            return new ForecastsDTO[size];
        }
    };
}

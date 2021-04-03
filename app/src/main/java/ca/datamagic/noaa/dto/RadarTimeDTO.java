package ca.datamagic.noaa.dto;

import java.util.Comparator;
import java.util.Date;

public class RadarTimeDTO {
    private String _timeStamp = null;
    private Date _time = null;

    public RadarTimeDTO(String timeStamp, Date time) {
        _timeStamp = timeStamp;
        _time = time;
    }

    public String getTimeStamp() {
        return _timeStamp;
    }

    public Date getTime() {
        return _time;
    }

    public static class RadarTimeComparator implements Comparator<RadarTimeDTO> {
        @Override
        public int compare(RadarTimeDTO t1, RadarTimeDTO t2) {
            if ((t1._timeStamp != null) && (t2._timeStamp != null)) {
                return t1._timeStamp.compareToIgnoreCase(t2._timeStamp);
            }
            return 0;
        }
    }
}

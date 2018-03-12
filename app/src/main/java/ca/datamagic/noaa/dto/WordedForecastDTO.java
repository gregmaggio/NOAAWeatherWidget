package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The WordedForecast class represents the wordedForecast element in the dwml xml stream.
 *
 * <wordedForecast time-layout="k-p12h-n13-1" dataSource="loxNetcdf" wordGenerator="markMitchell">
 * <name>Text Forecast</name>
 * <text>Sunny, with a high near 82. East northeast wind around 5 mph becoming calm  in the morning. </text>
 * <text>Increasing clouds, with a low around 48. Calm wind. </text>
 * <text>Mostly cloudy, with a high near 72. Calm wind becoming west southwest around 5 mph in the morning. </text>
 * <text>Patchy fog after 10pm.  Otherwise, mostly cloudy, with a low around 48. Southwest wind around 5 mph becoming calm. </text>
 * <text>Patchy fog before 10am.  Otherwise, mostly sunny, with a high near 70. Calm wind becoming southwest around 5 mph in the morning. </text>
 * <text>Mostly cloudy, with a low around 52.</text>
 * <text>Partly sunny, with a high near 67.</text>
 * <text>Mostly cloudy, with a low around 52.</text>
 * <text>A chance of showers.  Mostly cloudy, with a high near 66.</text>
 * <text>Showers likely.  Mostly cloudy, with a low around 54.</text>
 * <text>A chance of showers.  Partly sunny, with a high near 65.</text>
 * <text>A chance of showers.  Mostly cloudy, with a low around 51.</text>
 * <text>A chance of showers.  Partly sunny, with a high near 68.</text>
 * </wordedForecast>
 *
 * Created by Greg on 3/6/2018.
 */

public class WordedForecastDTO {
    public static final String NodeName = "wordedForecast";
    public static final String TimeLayoutAttribute = "time-layout";
    public static final String DataSourceAttribute = "dataSource";
    public static final String WordGeneratorAttribute = "wordGenerator";
    public static final String NameNode = "name";
    public static final String TextNode = "text";
    private String _timeLayout = null;
    private String _dataSource = null;
    private String _wordGenerator = null;
    private String _name = null;
    private List<String> _text = new ArrayList<String>();

    public String getTimeLayout() {
        return _timeLayout;
    }

    public void setTimeLayout(String newVal) {
        _timeLayout = newVal;
    }

    public String getDataSource() {
        return _dataSource;
    }

    public void setDataSource(String newVal) {
        _dataSource = newVal;
    }

    public String getWordGenerator() {
        return _wordGenerator;
    }

    public void setWordGenerator(String newVal) {
        _wordGenerator = newVal;
    }

    public String getName() {
        return _name;
    }

    public void setName(String newVal) {
        _name = newVal;
    }

    public List<String> getText() {
        return _text;
    }

    public void setText(List<String> newVal) {
        _text = newVal;
    }
}

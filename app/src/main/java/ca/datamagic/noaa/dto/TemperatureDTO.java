/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The Temperature class represents the temperature element in the dwml xml stream.
 * 
  <temperature type="minimum" units="Fahrenheit" time-layout="k-p24h-n7-1">
    <name>Daily Minimum Temperature</name>
    <value>72</value>
    <value>74</value>
    <value>73</value>
    <value>67</value>
    <value>64</value>
    <value>64</value>
    <value>65</value>
  </temperature>
 * 
 * @author greg
 *
 */
public class TemperatureDTO {
	public static final String NodeName = "temperature";
	public static final String TypeAttribute = "type";
	public static final String UnitsAttribute = "units";
	public static final String TimeLayoutAttribute = "time-layout";
	public static final String NameNode = "name";
	public static final String ValueNode = "value";
	private String _type = null;
	private String _units = null;
	private String _timeLayout = null;
	private String _name = null;
	private List<Double> _values = new ArrayList<Double>();
	
	public String getType() {
		return _type;
	}
	
	public void setType(String newVal) {
		_type = newVal;
	}
	
	public String getUnits() {
		return _units;
	}
	
	public void setUnits(String newVal) {
		_units = newVal;
	}
	
	public String getTimeLayout() {
		return _timeLayout;
	}
	
	public void setTimeLayout(String newVal) {
		_timeLayout = newVal;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String newVal) {
		_name = newVal;
	}
	
	public List<Double> getValues() {
		return _values;
	}
	
	public void setValues(List<Double> newVal) {
		_values = newVal;
	}
}

/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The ProbabilityOfPrecipitation class represents the probability-of-precipitation element in the dwml xml stream.
 * 
  <probability-of-precipitation type="12 hour" units="percent" time-layout="k-p12h-n14-2">
    <name>12 Hourly Probability of Precipitation</name>
    <value>0</value>
    <value>12</value>
    <value>3</value>
    <value>4</value>
    <value>5</value>
    <value>36</value>
    <value>20</value>
    <value>13</value>
    <value>10</value>
    <value>19</value>
    <value>14</value>
    <value>11</value>
    <value>11</value>
    <value>26</value>
  </probability-of-precipitation>
 * 
 * @author greg
 *
 */
public class ProbabilityOfPrecipitationDTO {
	public static final String NodeName = "probability-of-precipitation";
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

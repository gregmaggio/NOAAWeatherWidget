/**
 *
 */
package ca.datamagic.noaa.dto;

/**
 * The Head class represents the head element in the dwml xml stream.
 * 
 * <head>
 *   <product srsName="WGS 1984" concise-name="dwmlByDay" operational-mode="official">
 *     <title>NOAA's National Weather Service Forecast by 24 Hour Period</title>
 *     <field>meteorological</field>
 *     <category>forecast</category>
 *     <creation-date refresh-frequency="PT1H">2011-05-30T03:18:45Z</creation-date>
 *   </product>
 *   <source>
 *     <more-information>http://www.nws.noaa.gov/forecasts/xml/</more-information>
 *     <production-center>Meteorological Development Laboratory<sub-center>Product Generation Branch</sub-center></production-center>
 *     <disclaimer>http://www.nws.noaa.gov/disclaimer.html</disclaimer>
 *     <credit>http://www.weather.gov/</credit>
 *     <credit-logo>http://www.weather.gov/images/xml_logo.gif</credit-logo>
 *     <feedback>http://www.weather.gov/feedback.php</feedback>
 *   </source>
 * </head>
 * 
 * @author greg
 *
 */
public class HeadDTO {
	public static final String NodeName = "head";
	private ProductDTO _product = null;
	private SourceDTO _source = null;
	
	public ProductDTO getProduct() {
		return _product;
	}
	
	public void setProduct(ProductDTO newVal) {
		_product = newVal;
	}
	
	public SourceDTO getSource() {
		return _source;
	}
	
	public void setSource(SourceDTO newVal) {
		_source = newVal;
	}
}

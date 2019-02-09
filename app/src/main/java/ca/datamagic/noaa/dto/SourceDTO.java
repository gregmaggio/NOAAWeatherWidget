/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Source class represents the source element in the dwml xml stream.
 * 
 * <source>
 *    <more-information>http://www.nws.noaa.gov/forecasts/xml/</more-information>
 *    <production-center>Meteorological Development Laboratory<sub-center>Product Generation Branch</sub-center></production-center>
 *    <disclaimer>http://www.nws.noaa.gov/disclaimer.html</disclaimer>
 *    <credit>http://www.weather.gov/</credit>
 *    <credit-logo>http://www.weather.gov/images/xml_logo.gif</credit-logo>
 *    <feedback>http://www.weather.gov/feedback.php</feedback>
 * </source>
 *
 * @author greg
 *
 */
public class SourceDTO {
	public static final String NodeName = "source";
	public static final String MoreInformationNode = "more-information";
	public static final String DisclaimerNode = "disclaimer";
	public static final String CreditNode = "credit";
	public static final String CreditLogoNode = "credit-logo";
	public static final String FeedbackNode = "feedback";
	private String _moreInformationLink = null;
	private ProductionCenterDTO _productionCenter = null;
	private String _disclaimerLink = null;
	private String _credit = null;
	private String _creditLogo = null;
	private String _feedbackLink = null;
	
	public String getMoreInformationLink() {
		return _moreInformationLink;
	}
	
	public void setMoreInformationLink(String newVal) {
		_moreInformationLink = newVal;
	}
	
	public ProductionCenterDTO getProductionCenter() {
		return _productionCenter;
	}
	
	public void setProductionCenter(ProductionCenterDTO newVal) {
		_productionCenter = newVal;
	}
	
	public String getDisclaimerLink() {
		return _disclaimerLink;
	}
	
	public void setDisclaimerLink(String newVal) {
		_disclaimerLink = newVal;
	}
	
	public String getCredit() {
		return _credit;
	}
	
	public void setCredit(String newVal) {
		_credit = newVal;
	}
	
	public String getCreditLogo() {
		return _creditLogo;
	}
	
	public void setCreditLogo(String newVal) {
		_creditLogo = newVal;
	}
	
	public String getFeedbackLink() {
		return _feedbackLink;
	}
	
	public void setFeedbackLink(String newVal) {
		_feedbackLink = newVal;
	}
}

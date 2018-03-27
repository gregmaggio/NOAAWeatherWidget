package ca.datamagic.noaa.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ca.datamagic.noaa.dto.ConditionsIconDTO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.DirectionDTO;
import ca.datamagic.noaa.dto.HazardConditionsDTO;
import ca.datamagic.noaa.dto.HazardDTO;
import ca.datamagic.noaa.dto.HazardsDTO;
import ca.datamagic.noaa.dto.HeightDTO;
import ca.datamagic.noaa.dto.HumidityDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.MoreWeatherInformationDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.PressureDTO;
import ca.datamagic.noaa.dto.ProbabilityOfPrecipitationDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.TimeLayoutDTO;
import ca.datamagic.noaa.dto.ValidTimeDTO;
import ca.datamagic.noaa.dto.ValueDTO;
import ca.datamagic.noaa.dto.VisibilityDTO;
import ca.datamagic.noaa.dto.WeatherConditionsDTO;
import ca.datamagic.noaa.dto.WeatherDTO;
import ca.datamagic.noaa.dto.WindSpeedDTO;
import ca.datamagic.noaa.util.NumberUtils;
import ca.datamagic.noaa.util.XmlUtils;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLHandler {
    private DWMLDTO _dwml = null;

    public DWMLDTO parse(String xml) throws Exception {
        return parse(new ByteArrayInputStream(xml.getBytes()));
    }

    public DWMLDTO parse(InputStream inputStream) throws Exception {
        _dwml = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setIgnoringComments(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        NodeList nodeList = document.getElementsByTagName(DWMLDTO.NodeName);
        if (nodeList.getLength() > 0) {
            traverseDocumentElement((Element) nodeList.item(0));
        }
        return _dwml;
    }

    private void traverseDocumentElement(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(DWMLDTO.NodeName.toLowerCase())) {
            traverseDWML(element);
        }
    }

    private void traverseDWML(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(DWMLDTO.NodeName.toLowerCase())) {
            _dwml = new DWMLDTO();
            _dwml.setVersion(element.getAttribute(DWMLDTO.VersionAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(DataDTO.NodeName.toLowerCase())) {
                            traverseData((Element) child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseData(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(DataDTO.NodeName.toLowerCase())) {
            String type = element.getAttribute(DataDTO.TypeAttribute);
            if ((type != null) && (type.length() > 0)) {
                DataDTO data = new DataDTO();
                if (type.compareToIgnoreCase("forecast") == 0) {
                    _dwml.setForecast(data);
                } else  if (type.compareToIgnoreCase("current observations") == 0) {
                    _dwml.setObservation(data);
                }
                if ((data != null) && element.hasChildNodes()) {
                    NodeList childNodes = element.getChildNodes();
                    for (int index = 0; index < childNodes.getLength(); index++) {
                        Node child = childNodes.item(index);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            if (child.getNodeName().toLowerCase().contains(LocationDTO.NodeName.toLowerCase())) {
                                traverseLocation(data, (Element) child);
                                continue;
                            }
                            if (child.getNodeName().toLowerCase().contains(MoreWeatherInformationDTO.NodeName.toLowerCase())) {
                                traverseMoreWeatherInformation(data, (Element) child);
                                continue;
                            }
                            if (child.getNodeName().toLowerCase().contains(TimeLayoutDTO.NodeName.toLowerCase())) {
                                traverseTimeLayout(data, (Element) child);
                                continue;
                            }
                            if (child.getNodeName().toLowerCase().contains(ParametersDTO.NodeName.toLowerCase())) {
                                traverseParameters(data, (Element) child);
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    private void traverseLocation(DataDTO data, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(LocationDTO.NodeName.toLowerCase())) {
            data.setLocation(new LocationDTO());
            LocationDTO location = data.getLocation();
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(LocationDTO.LocationKeyNode.toLowerCase())) {
                            location.setLocationKey(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(LocationDTO.AreaDescriptionNode.toLowerCase())) {
                            location.setAreaDescription(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(PointDTO.NodeName.toLowerCase())) {
                            traversePoint(location, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HeightDTO.NodeName.toLowerCase())) {
                            traverseHeight(location, (Element) child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseHeight(LocationDTO location, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HeightDTO.NodeName.toLowerCase())) {
            location.setHeight(new HeightDTO());

            HeightDTO height = location.getHeight();
            height.setDatum(element.getAttribute(HeightDTO.DatumAttribute));
            height.setHeightUnits(element.getAttribute(HeightDTO.HeightUnitsAttribute));
            String value = XmlUtils.getText(element);
            if ((value != null) && (value.length() > 0)) {
                height.setValue(NumberUtils.toDouble(value));
            }
        }
    }

    private void traversePoint(LocationDTO location, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(PointDTO.NodeName.toLowerCase())) {
            location.setPoint(new PointDTO());

            PointDTO point = location.getPoint();
            point.setLatitude(NumberUtils.toDouble(element.getAttribute(PointDTO.LatitudeAttribute)));
            point.setLongitude(NumberUtils.toDouble(element.getAttribute(PointDTO.LongitudeAttribute)));
        }
    }

    private void traverseMoreWeatherInformation(DataDTO data, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(MoreWeatherInformationDTO.NodeName.toLowerCase())) {
            data.setMoreWeatherInformation(new MoreWeatherInformationDTO());
            MoreWeatherInformationDTO moreWeatherInformation = data.getMoreWeatherInformation();
            moreWeatherInformation.setLink(XmlUtils.getText(element));
            moreWeatherInformation.setApplicableLocation(element.getAttribute(MoreWeatherInformationDTO.ApplicableLocationAttribute));
        }
    }

    private void traverseTimeLayout(DataDTO data, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(TimeLayoutDTO.NodeName.toLowerCase())) {
            List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
            TimeLayoutDTO timeLayout = new TimeLayoutDTO();
            timeLayout.setTimeCoordinate(element.getAttribute(TimeLayoutDTO.TimeCoordinateAttribute));
            timeLayout.setSummarization(element.getAttribute(TimeLayoutDTO.SummarizationAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element)child;
                        if (child.getNodeName().toLowerCase().contains(TimeLayoutDTO.LayoutKeyNode.toLowerCase())) {
                            timeLayout.setLayoutKey(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(TimeLayoutDTO.StartValidTimeNode.toLowerCase())) {
                            String periodName = "";
                            if (childElement.hasAttribute(ValidTimeDTO.PeriodNameAttribute)) {
                                periodName = childElement.getAttribute(ValidTimeDTO.PeriodNameAttribute);
                            }
                            timeLayout.getStartTimes().add(new ValidTimeDTO(XmlUtils.getText(child), periodName));
                            continue;
                        }
                    }
                }
            }

            timeLayouts.add(timeLayout);
        }
    }

    private void traverseParameters(DataDTO data, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ParametersDTO.NodeName.toLowerCase())) {
            data.setParameters(new ParametersDTO());
            ParametersDTO parameters = data.getParameters();
            parameters.setApplicableLocation(element.getAttribute(ParametersDTO.ApplicableLocationAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(TemperatureDTO.NodeName.toLowerCase())) {
                            traverseTemperature(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ProbabilityOfPrecipitationDTO.NodeName.toLowerCase())) {
                            traverseProbabilityOfPrecipitation(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HumidityDTO.NodeName.toLowerCase())) {
                            traverseHumidity(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(WeatherDTO.NodeName.toLowerCase())) {
                            traverseWeather(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ConditionsIconDTO.NodeName.toLowerCase())) {
                            traverseConditionsIcon(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(DirectionDTO.NodeName.toLowerCase())) {
                            traverseDirection(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(WindSpeedDTO.NodeName.toLowerCase())) {
                            traverseWindSpeed(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(PressureDTO.NodeName.toLowerCase())) {
                            traversePressure(parameters, (Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HazardsDTO.NodeName.toLowerCase())) {
                            traverseHazards(parameters, (Element)child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseTemperature(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(TemperatureDTO.NodeName.toLowerCase())) {
            TemperatureDTO temperature = new TemperatureDTO();
            temperature.setType(element.getAttribute(TemperatureDTO.TypeAttribute));
            temperature.setUnits(element.getAttribute(TemperatureDTO.UnitsAttribute));
            temperature.setTimeLayout(element.getAttribute(TemperatureDTO.TimeLayoutAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(TemperatureDTO.NameNode.toLowerCase())) {
                            temperature.setName(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(TemperatureDTO.ValueNode.toLowerCase())) {
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                Double value = NumberUtils.toDouble(text);
                                if (value != null) {
                                    temperature.getValues().add(value);
                                } else {
                                    temperature.getValues().add(Double.NaN);
                                }
                            }
                            continue;
                        }
                    }
                }
            }
            parameters.getTemperatures().add(temperature);
        }
    }

    private void traverseProbabilityOfPrecipitation(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ProbabilityOfPrecipitationDTO.NodeName.toLowerCase())) {
            ProbabilityOfPrecipitationDTO probabilityOfPrecipitation = new ProbabilityOfPrecipitationDTO();
            probabilityOfPrecipitation.setType(element.getAttribute(ProbabilityOfPrecipitationDTO.TypeAttribute));
            probabilityOfPrecipitation.setUnits(element.getAttribute(ProbabilityOfPrecipitationDTO.UnitsAttribute));
            probabilityOfPrecipitation.setTimeLayout(element.getAttribute(ProbabilityOfPrecipitationDTO.TimeLayoutAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(ProbabilityOfPrecipitationDTO.NameNode.toLowerCase())) {
                            probabilityOfPrecipitation.setName(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ProbabilityOfPrecipitationDTO.ValueNode.toLowerCase())) {
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                Double value = NumberUtils.toDouble(text);
                                if (value != null) {
                                    probabilityOfPrecipitation.getValues().add(value);
                                } else {
                                    probabilityOfPrecipitation.getValues().add(Double.NaN);
                                }
                            }
                            continue;
                        }
                    }
                }
            }
            parameters.setProbabilityOfPrecipitation(probabilityOfPrecipitation);
        }
    }

    private void traverseHumidity(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HumidityDTO.NodeName.toLowerCase())) {
            parameters.setHumidity(new HumidityDTO());
            HumidityDTO humidity = parameters.getHumidity();
            humidity.setType(element.getAttribute(HumidityDTO.TypeAttribute));
            humidity.setTimeLayout(element.getAttribute(HumidityDTO.TimeLayoutAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(HumidityDTO.ValueNode.toLowerCase())) {
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                Double value = NumberUtils.toDouble(text);
                                if (value != null) {
                                    humidity.setValue(value);
                                } else {
                                    humidity.setValue(Double.NaN);
                                }
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseWeather(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(WeatherDTO.NodeName.toLowerCase())) {
            parameters.setWeather(new WeatherDTO());
            WeatherDTO weather = parameters.getWeather();
            weather.setTimeLayout(element.getAttribute(WeatherDTO.TimeLayoutAttribute));
            List<WeatherConditionsDTO> weatherConditions = new ArrayList<WeatherConditionsDTO>();
            if (element.hasChildNodes()) {
                for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
                    if (element.getChildNodes().item(ii).getNodeType() == Node.ELEMENT_NODE) {
                        Element child = (Element)element.getChildNodes().item(ii);
                        if (child.getNodeName().compareToIgnoreCase(WeatherDTO.NameNode) == 0) {
                            weather.setName(XmlUtils.getText(child));
                        } else  if (child.getNodeName().compareToIgnoreCase(WeatherConditionsDTO.NodeName) == 0) {
                            WeatherConditionsDTO weatherCondition = new WeatherConditionsDTO();
                            traverseWeatherCondition(weatherCondition, child);
                            weatherConditions.add(weatherCondition);
                        }
                    }
                }
            }
            weather.setWeatherConditions(weatherConditions);
        }
    }

    private void traverseWeatherCondition(WeatherConditionsDTO weatherCondition, Element element) throws  Exception {
        if (element.getNodeName().toLowerCase().contains(WeatherConditionsDTO.NodeName.toLowerCase())) {
            weatherCondition.setWeatherSummary(element.getAttribute(WeatherConditionsDTO.WeatherSummaryAttribute));
            traverseValues(weatherCondition, element);
        }
    }

    private void traverseValues(WeatherConditionsDTO weatherCondition, Element element) throws  Exception {
        if (element.getNodeName().toLowerCase().contains(WeatherConditionsDTO.NodeName.toLowerCase())) {
            List<ValueDTO> values = new ArrayList<ValueDTO>();
            if (element.hasChildNodes()) {
                for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
                    if (element.getChildNodes().item(ii).getNodeType() == Node.ELEMENT_NODE) {
                        Element child = (Element) element.getChildNodes().item(ii);
                        if (child.getNodeName().compareToIgnoreCase(ValueDTO.NodeName) == 0) {
                            ValueDTO value = new ValueDTO();
                            traverseValue(value, child);
                            values.add(value);
                        }
                    }
                }
            }
            weatherCondition.setValues(values);
        }
    }

    private void traverseValue(ValueDTO value, Element element) throws  Exception {
        if (element.getNodeName().toLowerCase().contains(ValueDTO.NodeName.toLowerCase())) {
            if (element.hasChildNodes()) {
                for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
                    if (element.getChildNodes().item(ii).getNodeType() == Node.ELEMENT_NODE) {
                        Element child = (Element) element.getChildNodes().item(ii);
                        if (child.getNodeName().compareToIgnoreCase(VisibilityDTO.NodeName) == 0) {
                            VisibilityDTO visibility = new VisibilityDTO();
                            visibility.setUnits(child.getAttribute(VisibilityDTO.UnitsAttribute));
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                Double doubleValue = NumberUtils.toDouble(text);
                                if (doubleValue != null) {
                                    visibility.setValue(doubleValue);
                                } else {
                                    visibility.setValue(Double.NaN);
                                }
                            }
                            value.setVisibility(visibility);
                        }
                    }
                }
            }
        }
    }

    private void traverseConditionsIcon(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ConditionsIconDTO.NodeName.toLowerCase())) {
            parameters.setConditionsIcon(new ConditionsIconDTO());

            ConditionsIconDTO conditionsIcon = parameters.getConditionsIcon();
            conditionsIcon.setType(element.getAttribute(ConditionsIconDTO.TypeAttribute));
            conditionsIcon.setTimeLayout(element.getAttribute(ConditionsIconDTO.TimeLayoutAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(ConditionsIconDTO.NameNode.toLowerCase())) {
                            conditionsIcon.setName(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ConditionsIconDTO.IconLinkNode.toLowerCase())) {
                            conditionsIcon.getIconLink().add(XmlUtils.getText(child));
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseDirection(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(DirectionDTO.NodeName.toLowerCase())) {
            parameters.setDirection(new DirectionDTO());
            DirectionDTO direction = parameters.getDirection();
            direction.setType(element.getAttribute(DirectionDTO.TypeAttribute));
            direction.setTimeLayout(element.getAttribute(DirectionDTO.TimeLayoutAttribute));
            direction.setUnits(element.getAttribute(DirectionDTO.UnitsAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(DirectionDTO.ValueNode.toLowerCase())) {
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                Double value = NumberUtils.toDouble(text);
                                if (value != null) {
                                    direction.setValue(value);
                                } else {
                                    direction.setValue(Double.NaN);
                                }
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseWindSpeed(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(WindSpeedDTO.NodeName.toLowerCase())) {
            parameters.getWindSpeeds().add(new WindSpeedDTO());
            WindSpeedDTO windSpeed = parameters.getWindSpeeds().get(parameters.getWindSpeeds().size() - 1);
            windSpeed.setType(element.getAttribute(WindSpeedDTO.TypeAttribute));
            windSpeed.setTimeLayout(element.getAttribute(WindSpeedDTO.TimeLayoutAttribute));
            windSpeed.setUnits(element.getAttribute(WindSpeedDTO.UnitsAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(WindSpeedDTO.ValueNode.toLowerCase())) {
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                Double value = NumberUtils.toDouble(text);
                                if (value != null) {
                                    windSpeed.setValue(value);
                                } else {
                                    windSpeed.setValue(Double.NaN);
                                }
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traversePressure(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(PressureDTO.NodeName.toLowerCase())) {
            parameters.setPressure(new PressureDTO());
            PressureDTO pressure = parameters.getPressure();
            pressure.setType(element.getAttribute(PressureDTO.TypeAttribute));
            pressure.setTimeLayout(element.getAttribute(PressureDTO.TimeLayoutAttribute));
            pressure.setUnits(element.getAttribute(PressureDTO.UnitsAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(PressureDTO.ValueNode.toLowerCase())) {
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                Double value = NumberUtils.toDouble(text);
                                if (value != null) {
                                    pressure.setValue(value);
                                } else {
                                    pressure.setValue(Double.NaN);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void traverseHazards(ParametersDTO parameters, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HazardsDTO.NodeName.toLowerCase())) {
            HazardsDTO hazards = new HazardsDTO();
            hazards.setTimeLayout(element.getAttribute(HazardsDTO.TimeLayoutAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int ii = 0; ii < childNodes.getLength(); ii++) {
                    Node child = childNodes.item(ii);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(HazardsDTO.NameNode.toLowerCase())) {
                            hazards.setName(XmlUtils.getText(child));
                        } else if (child.getNodeName().toLowerCase().contains(HazardConditionsDTO.NodeName.toLowerCase())) {
                            HazardConditionsDTO hazardConditions = new HazardConditionsDTO();
                            traverseHazardConditions(hazardConditions, (Element) child);
                            hazards.setHazardConditions(hazardConditions);
                        }
                    }
                }
            }
            parameters.setHazards(hazards);
        }
    }

    private void traverseHazardConditions(HazardConditionsDTO hazardConditions, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HazardConditionsDTO.NodeName.toLowerCase())) {
            List<HazardDTO> hazards = new ArrayList<HazardDTO>();
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int ii = 0; ii < childNodes.getLength(); ii++) {
                    Node child = childNodes.item(ii);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(HazardDTO.NodeName.toLowerCase())) {
                            HazardDTO hazard = new HazardDTO();
                            traverseHazard(hazard, (Element) child);
                            hazards.add(hazard);
                        }
                    }
                }
            }
            hazardConditions.setHazards(hazards);
        }
    }

    private void traverseHazard(HazardDTO hazard, Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HazardDTO.NodeName.toLowerCase())) {
            if (element.hasAttribute(HazardDTO.HeadlineAttribute)) {
                hazard.setHeadline(element.getAttribute(HazardDTO.HeadlineAttribute));
            }
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int ii = 0; ii < childNodes.getLength(); ii++) {
                    Node child = childNodes.item(ii);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(HazardDTO.HazardTextUrlNode.toLowerCase())) {
                            hazard.setHazardTextUrl(XmlUtils.getText(child));
                        }
                    }
                }
            }
        }
    }
}

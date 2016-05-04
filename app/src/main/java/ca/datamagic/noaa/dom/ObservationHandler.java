package ca.datamagic.noaa.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;

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
import ca.datamagic.noaa.dto.HazardsDTO;
import ca.datamagic.noaa.dto.HeightDTO;
import ca.datamagic.noaa.dto.HumidityDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.MoreWeatherInformationDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.PressureDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.TimeLayoutDTO;
import ca.datamagic.noaa.dto.ValidTimeDTO;
import ca.datamagic.noaa.dto.ValueDTO;
import ca.datamagic.noaa.dto.VisibilityDTO;
import ca.datamagic.noaa.dto.WeatherConditionsDTO;
import ca.datamagic.noaa.dto.WeatherDTO;
import ca.datamagic.noaa.dto.WindSpeedDTO;
import ca.datamagic.util.XmlUtils;

/**
 * Created by Greg on 1/4/2016.
 */
public class ObservationHandler extends DefaultHandler {
    private String _currentElement = null;
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
                if (type.compareToIgnoreCase("current observations") == 0) {
                    _dwml.setData(new DataDTO());
                    if (element.hasChildNodes()) {
                        NodeList childNodes = element.getChildNodes();
                        for (int index = 0; index < childNodes.getLength(); index++) {
                            Node child = childNodes.item(index);
                            if (child.getNodeType() == Node.ELEMENT_NODE) {
                                if (child.getNodeName().toLowerCase().contains(LocationDTO.NodeName.toLowerCase())) {
                                    traverseLocation((Element) child);
                                    continue;
                                }
                                if (child.getNodeName().toLowerCase().contains(MoreWeatherInformationDTO.NodeName.toLowerCase())) {
                                    traverseMoreWeatherInformation((Element) child);
                                    continue;
                                }
                                if (child.getNodeName().toLowerCase().contains(TimeLayoutDTO.NodeName.toLowerCase())) {
                                    traverseTimeLayout((Element) child);
                                    continue;
                                }
                                if (child.getNodeName().toLowerCase().contains(ParametersDTO.NodeName.toLowerCase())) {
                                    traverseParameters((Element) child);
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void traverseLocation(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(LocationDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
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
                            traversePoint((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HeightDTO.NodeName.toLowerCase())) {
                            traverseHeight((Element) child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseHeight(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HeightDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            LocationDTO location = data.getLocation();
            location.setHeight(new HeightDTO());

            HeightDTO height = location.getHeight();
            height.setDatum(element.getAttribute(HeightDTO.DatumAttribute));
            height.setHeightUnits(element.getAttribute(HeightDTO.HeightUnitsAttribute));
            String value = XmlUtils.getText(element);
            if ((value != null) && (value.length() > 0)) {
                height.setValue(new Double(value));
            }
        }
    }

    private void traversePoint(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(PointDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();

            LocationDTO location = data.getLocation();
            location.setPoint(new PointDTO());

            PointDTO point = location.getPoint();
            point.setLatitude(new Double(element.getAttribute(PointDTO.LatitudeAttribute)));
            point.setLongitude(new Double(element.getAttribute(PointDTO.LongitudeAttribute)));
        }
    }

    private void traverseMoreWeatherInformation(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(MoreWeatherInformationDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            data.setMoreWeatherInformation(new MoreWeatherInformationDTO());

            MoreWeatherInformationDTO moreWeatherInformation = data.getMoreWeatherInformation();
            moreWeatherInformation.setLink(XmlUtils.getText(element));
            moreWeatherInformation.setApplicableLocation(element.getAttribute(MoreWeatherInformationDTO.ApplicableLocationAttribute));
        }
    }

    private void traverseTimeLayout(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(TimeLayoutDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();

            TimeLayoutDTO timeLayout = new TimeLayoutDTO();
            timeLayout.setTimeCoordinate(element.getAttribute(TimeLayoutDTO.TimeCoordinateAttribute));
            timeLayout.setSummarization(element.getAttribute(TimeLayoutDTO.SummarizationAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(TimeLayoutDTO.LayoutKeyNode.toLowerCase())) {
                            timeLayout.setLayoutKey(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(TimeLayoutDTO.StartValidTimeNode.toLowerCase())) {
                            timeLayout.getStartTimes().add(new ValidTimeDTO(XmlUtils.getText(child)));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(TimeLayoutDTO.EndValidTimeNode.toLowerCase())) {
                            timeLayout.getEndTimes().add(new ValidTimeDTO(XmlUtils.getText(child)));
                            continue;
                        }
                    }
                }
            }

            timeLayouts.add(timeLayout);
        }
    }

    private void traverseParameters(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ParametersDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            data.setParameters(new ParametersDTO());

            ParametersDTO parameters = data.getParameters();
            parameters.setApplicableLocation(element.getAttribute(ParametersDTO.ApplicableLocationAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(TemperatureDTO.NodeName.toLowerCase())) {
                            traverseTemperature((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HumidityDTO.NodeName.toLowerCase())) {
                            traverseHumidity((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(WeatherDTO.NodeName.toLowerCase())) {
                            traverseWeather((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ConditionsIconDTO.NodeName.toLowerCase())) {
                            traverseConditionsIcon((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(DirectionDTO.NodeName.toLowerCase())) {
                            traverseDirection((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(WindSpeedDTO.NodeName.toLowerCase())) {
                            traverseWindSpeed((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(PressureDTO.NodeName.toLowerCase())) {
                            traversePressure((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HazardsDTO.NodeName.toLowerCase())) {
                            //traverseHazards((Element)child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseTemperature(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(TemperatureDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
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
                                temperature.getValues().add(new Double(text));
                            }
                            continue;
                        }
                    }
                }
            }
            parameters.getTemperatures().add(temperature);
        }
    }

    private void traverseHumidity(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HumidityDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
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
                            try {
                                humidity.setValue(new Double(XmlUtils.getText(child)));
                            } catch (Throwable t) {
                                humidity.setValue(null);
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseWeather(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(WeatherDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
            parameters.setWeather(new WeatherDTO());
            WeatherDTO weather = parameters.getWeather();
            weather.setTimeLayout(element.getAttribute(WeatherDTO.TimeLayoutAttribute));
            if (element.hasChildNodes()) {
                for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
                    if (element.getChildNodes().item(ii).getNodeType() == Node.ELEMENT_NODE) {
                        Element child = (Element)element.getChildNodes().item(ii);
                        if (child.getNodeName().compareToIgnoreCase(WeatherDTO.NameNode) == 0) {
                            weather.setName(XmlUtils.getText(child));
                        } else  if (child.getNodeName().compareToIgnoreCase(WeatherConditionsDTO.NodeName) == 0) {
                            traverseWeatherConditions(child);
                        }
                    }
                }
            }
        }
    }

    private void traverseWeatherConditions(Element element) throws  Exception {
        if (element.getNodeName().toLowerCase().contains(WeatherConditionsDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
            WeatherDTO weather = parameters.getWeather();
            weather.setWeatherConditions(new ArrayList<WeatherConditionsDTO>());
            List<WeatherConditionsDTO> weatherConditionsList = weather.getWeatherConditions();
            weatherConditionsList.add(new WeatherConditionsDTO());
            WeatherConditionsDTO weatherConditions = weatherConditionsList.get(0);
            weatherConditions.setWeatherSummary(element.getAttribute(WeatherConditionsDTO.WeatherSummaryAttribute));
            if (element.hasChildNodes()) {
                for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
                    if (element.getChildNodes().item(ii).getNodeType() == Node.ELEMENT_NODE) {
                        Element child = (Element) element.getChildNodes().item(ii);
                        if (child.getNodeName().compareToIgnoreCase(ValueDTO.NodeName) == 0) {
                            traverseValue(child);
                        }
                    }
                }
            }
        }
    }

    private void traverseValue(Element element) throws  Exception {
        if (element.getNodeName().toLowerCase().contains(ValueDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
            WeatherDTO weather = parameters.getWeather();
            List<WeatherConditionsDTO> weatherConditionsList = weather.getWeatherConditions();
            WeatherConditionsDTO weatherConditions = weatherConditionsList.get(0);
            weatherConditions.setValues(new ArrayList<ValueDTO>());
            List<ValueDTO> values = weatherConditions.getValues();
            values.add(new ValueDTO());
            ValueDTO value = values.get(0);
            if (element.hasChildNodes()) {
                for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
                    if (element.getChildNodes().item(ii).getNodeType() == Node.ELEMENT_NODE) {
                        Element child = (Element) element.getChildNodes().item(ii);
                        if (child.getNodeName().compareToIgnoreCase(VisibilityDTO.NodeName) == 0) {
                            value.setVisibility(new VisibilityDTO());
                            VisibilityDTO visibility = value.getVisibility();
                            visibility.setUnits(child.getAttribute(VisibilityDTO.UnitsAttribute));
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                visibility.setValue(new Double(text));
                            }
                        }
                    }
                }
            }
        }
    }

    private void traverseConditionsIcon(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ConditionsIconDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();

            ParametersDTO parameters = data.getParameters();
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

    private void traverseDirection(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(DirectionDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
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
                            try {
                                direction.setValue(new Double(XmlUtils.getText(child)));
                            } catch (Throwable t) {
                                direction.setValue(null);
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseWindSpeed(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(WindSpeedDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
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
                            try {
                                windSpeed.setValue(new Double(XmlUtils.getText(child)));
                            } catch (Throwable t) {
                                windSpeed.setValue(null);
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traversePressure(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(PressureDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
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
                            try {
                                pressure.setValue(new Double(XmlUtils.getText(child)));
                            } catch (Throwable t) {
                                pressure.setValue(null);
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }
}

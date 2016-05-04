package ca.datamagic.noaa.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ca.datamagic.noaa.dto.ConditionsIconDTO;
import ca.datamagic.noaa.dto.CreationDateDTO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.HazardConditionsDTO;
import ca.datamagic.noaa.dto.HazardDTO;
import ca.datamagic.noaa.dto.HazardsDTO;
import ca.datamagic.noaa.dto.HeadDTO;
import ca.datamagic.noaa.dto.HeightDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.MoreWeatherInformationDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.ProbabilityOfPrecipitationDTO;
import ca.datamagic.noaa.dto.ProductDTO;
import ca.datamagic.noaa.dto.ProductionCenterDTO;
import ca.datamagic.noaa.dto.SourceDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.TimeLayoutDTO;
import ca.datamagic.noaa.dto.ValidTimeDTO;
import ca.datamagic.noaa.dto.ValueDTO;
import ca.datamagic.noaa.dto.WeatherConditionsDTO;
import ca.datamagic.noaa.dto.WeatherDTO;
import ca.datamagic.util.DateTimeUTC;
import ca.datamagic.util.XmlUtils;

/**
 * Created by Greg on 1/1/2016.
 */
public class DWMLByDayOutHandler {
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
                        if (child.getNodeName().toLowerCase().contains(HeadDTO.NodeName.toLowerCase())) {
                            traverseHead((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(DataDTO.NodeName.toLowerCase())) {
                            traverseData((Element) child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseHead(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HeadDTO.NodeName.toLowerCase())) {
            _dwml.setHead(new HeadDTO());
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(ProductDTO.NodeName.toLowerCase())) {
                            traverseProduct((Element) child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(SourceDTO.NodeName.toLowerCase())) {
                            traverseSource((Element) child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseProduct(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ProductDTO.NodeName.toLowerCase())) {
            HeadDTO head = _dwml.getHead();
            head.setProduct(new ProductDTO());

            ProductDTO product = head.getProduct();
            product.setSrsName(element.getAttribute(ProductDTO.SrsNameAttribute));
            product.setConciseName(element.getAttribute(ProductDTO.ConciseNameAttribute));
            product.setOperationalMode(element.getAttribute(ProductDTO.OperationalModeAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(ProductDTO.TitleNode.toLowerCase())) {
                            product.setTitle(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ProductDTO.FieldNode.toLowerCase())) {
                            product.setField(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ProductDTO.CategoryNode.toLowerCase())) {
                            product.setCategory(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(CreationDateDTO.NodeName.toLowerCase())) {
                            traverseCreationDate((Element) child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseCreationDate(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(CreationDateDTO.NodeName.toLowerCase())) {
            HeadDTO head = _dwml.getHead();

            ProductDTO product = head.getProduct();
            product.setCreationDate(new CreationDateDTO());

            CreationDateDTO creationDate = product.getCreationDate();
            creationDate.setRefreshFrequency(element.getAttribute(CreationDateDTO.RefreshFrequencyAttribute));
            creationDate.setCreationDate((new DateTimeUTC(XmlUtils.getText(element))).getCalendar());
        }
    }

    private void traverseSource(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(SourceDTO.NodeName.toLowerCase())) {
            HeadDTO head = _dwml.getHead();
            head.setSource(new SourceDTO());

            SourceDTO source = head.getSource();
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(SourceDTO.MoreInformationNode.toLowerCase())) {
                            source.setMoreInformationLink(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ProductionCenterDTO.NodeName.toLowerCase())) {
                            traverseProductionCenter((Element)child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(SourceDTO.DisclaimerNode.toLowerCase())) {
                            source.setDisclaimerLink(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(SourceDTO.CreditNode.toLowerCase())) {
                            source.setCreditLink(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(SourceDTO.CreditLogoNode.toLowerCase())) {
                            source.setCreditLogo(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(SourceDTO.FeedbackNode.toLowerCase())) {
                            source.setFeedbackLink(XmlUtils.getText(child));
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseProductionCenter(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ProductionCenterDTO.NodeName.toLowerCase())) {
            HeadDTO head = _dwml.getHead();

            SourceDTO source = head.getSource();
            source.setProductionCenter(new ProductionCenterDTO());

            ProductionCenterDTO productionCenter = source.getProductionCenter();
            productionCenter.setDescription(XmlUtils.getText(element));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(ProductionCenterDTO.SubCenterNode.toLowerCase())) {
                            productionCenter.setSubCenter(XmlUtils.getText(child));
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseData(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(DataDTO.NodeName.toLowerCase())) {
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
                            traverseTemperature((Element)child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ProbabilityOfPrecipitationDTO.NodeName.toLowerCase())) {
                            traverseProbabilityOfPrecipitation((Element)child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(WeatherDTO.NodeName.toLowerCase())) {
                            traverseWeather((Element)child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(ConditionsIconDTO.NodeName.toLowerCase())) {
                            traverseConditionsIcon((Element)child);
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HazardsDTO.NodeName.toLowerCase())) {
                            traverseHazards((Element)child);
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

    private void traverseProbabilityOfPrecipitation(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(ProbabilityOfPrecipitationDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
            ProbabilityOfPrecipitationDTO probabilityOfPrecipitation = new ProbabilityOfPrecipitationDTO();
            probabilityOfPrecipitation.setType(element.getAttribute(ProbabilityOfPrecipitationDTO.TypeAttribute));
            probabilityOfPrecipitation.setUnits(element.getAttribute(ProbabilityOfPrecipitationDTO.UnitsAttribute));
            probabilityOfPrecipitation.setTimeLayout(element.getAttribute(ProbabilityOfPrecipitationDTO.TimeLayoutAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(TemperatureDTO.NameNode.toLowerCase())) {
                            probabilityOfPrecipitation.setName(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(TemperatureDTO.ValueNode.toLowerCase())) {
                            String text = XmlUtils.getText(child);
                            if ((text != null) && (text.length() > 0)) {
                                probabilityOfPrecipitation.getValues().add(new Double(text));
                            }
                            continue;
                        }
                    }
                }
            }
            parameters.setProbabilityOfPrecipitation(probabilityOfPrecipitation);
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
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(WeatherDTO.NameNode.toLowerCase())) {
                            weather.setName(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(WeatherConditionsDTO.NodeName.toLowerCase())) {
                            traverseWeatherConditions((Element)child);
                            continue;
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

    private void traverseHazards(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HazardsDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();

            ParametersDTO parameters = data.getParameters();
            parameters.setHazards(new HazardsDTO());

            HazardsDTO hazards = parameters.getHazards();
            hazards.setTimeLayout(element.getAttribute(HazardsDTO.TimeLayoutAttribute));

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(HazardsDTO.NameNode.toLowerCase())) {
                            hazards.setName(XmlUtils.getText(child));
                            continue;
                        }
                        if (child.getNodeName().toLowerCase().contains(HazardConditionsDTO.NodeName.toLowerCase())) {
                            traverseHazardConditions((Element)child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseHazardConditions(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HazardConditionsDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();

            HazardsDTO hazards = parameters.getHazards();
            hazards.setHazardConditions(new HazardConditionsDTO());

            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(HazardsDTO.NodeName.toLowerCase())) {
                            traverseHazard((Element)child);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void traverseHazard(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(HazardsDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
            HazardsDTO hazards = parameters.getHazards();
            HazardConditionsDTO hazardConditions = hazards.getHazardConditions();
            HazardDTO hazard = new HazardDTO();
            hazard.setHazardCode(element.getAttribute(HazardDTO.HazardCodeAttribute));
            hazard.setPhenomena(element.getAttribute(HazardDTO.PhenomenaAttribute));
            hazard.setSignificance(element.getAttribute(HazardDTO.SignificanceAttribute));
            hazard.setHazardType(element.getAttribute(HazardDTO.HazardTypeAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(HazardDTO.HazardTextUrlNode.toLowerCase())) {
                            hazard.setHazardTextUrl(XmlUtils.getText(child));
                            continue;
                        }
                    }
                }
            }
            hazardConditions.getHazards().add(hazard);
        }
    }

    private void traverseWeatherConditions(Element element) throws Exception {
        if (element.getNodeName().toLowerCase().contains(WeatherConditionsDTO.NodeName.toLowerCase())) {
            DataDTO data = _dwml.getData();
            ParametersDTO parameters = data.getParameters();
            WeatherDTO weather = parameters.getWeather();
            WeatherConditionsDTO weatherConditions = new WeatherConditionsDTO();
            weatherConditions.setWeatherSummary(element.getAttribute(WeatherConditionsDTO.WeatherSummaryAttribute));
            if (element.hasChildNodes()) {
                NodeList childNodes = element.getChildNodes();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node child = childNodes.item(index);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().toLowerCase().contains(ValueDTO.NodeName.toLowerCase())) {
                            Element childElement = (Element)child;
                            ValueDTO value = new ValueDTO();
                            value.setCoverage(childElement.getAttribute(ValueDTO.CoverageAttribute));
                            value.setIntensity(childElement.getAttribute(ValueDTO.IntensityAttribute));
                            value.setAdditive(childElement.getAttribute(ValueDTO.AdditiveAttribute));
                            value.setWeatherType(childElement.getAttribute(ValueDTO.WeatherTypeAttribute));
                            value.setQualifier(childElement.getAttribute(ValueDTO.QualifierAttribute));
                            weatherConditions.getValues().add(value);
                            continue;
                        }
                    }
                }
            }
            weather.getWeatherConditions().add(weatherConditions);
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
}

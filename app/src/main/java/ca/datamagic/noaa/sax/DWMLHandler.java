package ca.datamagic.noaa.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ca.datamagic.noaa.dto.ConditionsIconDTO;
import ca.datamagic.noaa.dto.CreationDateDTO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.HeadDTO;
import ca.datamagic.noaa.dto.HeightDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.MoreWeatherInformationDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.ProbabilityOfPrecipitationDTO;
import ca.datamagic.noaa.dto.ProductDTO;
import ca.datamagic.noaa.dto.SourceDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.TimeLayoutDTO;
import ca.datamagic.noaa.dto.ValidTimeDTO;
import ca.datamagic.noaa.dto.WeatherConditionsDTO;
import ca.datamagic.noaa.dto.WeatherDTO;
import ca.datamagic.noaa.dto.WordedForecastDTO;

/**
 * Created by Greg on 3/5/2018.
 */

public class DWMLHandler extends DefaultHandler {
    private static final Pattern _timeStampRegex = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})-(\\d{2}):(\\d{2})");
    private String _currentNodeName = null;
    private DWMLDTO _dwml = null;
    private HeadDTO _head = null;
    private ProductDTO _product = null;
    private CreationDateDTO _creationDate = null;
    private SourceDTO _source = null;
    private DataDTO _data = null;
    private LocationDTO _location = null;
    private HeightDTO _height = null;
    private MoreWeatherInformationDTO _moreWeatherInformation = null;
    private TimeLayoutDTO _timeLayout = null;
    private ValidTimeDTO _validTime = null;
    private ParametersDTO _parameters = null;
    private TemperatureDTO _temperature = null;
    private ProbabilityOfPrecipitationDTO _probabilityOfPrecipitation = null;
    private WeatherDTO _weather = null;
    private ConditionsIconDTO _conditionsIcon = null;
    private WordedForecastDTO _wordedForecast = null;

    private static Calendar getCalendar(String timeStamp) {
        Matcher matcher = _timeStampRegex.matcher(timeStamp);
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            int hour = Integer.parseInt(matcher.group(4));
            int minute = Integer.parseInt(matcher.group(5));
            int second = Integer.parseInt(matcher.group(6));
            int hourAdjust = Integer.parseInt(matcher.group(7));
            int minuteAdjust = Integer.parseInt(matcher.group(8));
            Calendar utc = Calendar.getInstance();
            utc.set(Calendar.YEAR, year);
            utc.set(Calendar.MONTH, month - 1);
            utc.set(Calendar.DATE, day);
            utc.set(Calendar.HOUR_OF_DAY, hour);
            utc.set(Calendar.MINUTE, minute);
            utc.set(Calendar.SECOND, second);
            utc.set(Calendar.MILLISECOND, 0);
            utc.add(Calendar.HOUR, -1 * hourAdjust);
            utc.add(Calendar.MINUTE, -1 * minuteAdjust);
            return utc;
        }
        return null;
    }

    public DWMLDTO parse(String xml) throws SAXException, IOException, ParserConfigurationException {
        return  parse(new ByteArrayInputStream(xml.getBytes()));
    }

    public DWMLDTO parse(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);

        SAXParser parser = factory.newSAXParser();
        parser.parse(inputStream, this);

        return _dwml;
    }

    @Override
    public void startDocument() throws SAXException {
        _dwml = new DWMLDTO();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        _currentNodeName = qName;
        if (_currentNodeName.compareToIgnoreCase(DWMLDTO.NodeName) == 0) {
            _dwml.setVersion(attributes.getValue(DWMLDTO.VersionAttribute));
        } else if (_currentNodeName.compareToIgnoreCase(HeadDTO.NodeName) == 0) {
            _head = new HeadDTO();
            if (_dwml != null) {
                _dwml.setHead(_head);
            }
        } else if (_currentNodeName.compareToIgnoreCase(ProductDTO.NodeName) == 0) {
            _product = new ProductDTO();
            _product.setConciseName(attributes.getValue(ProductDTO.ConciseNameAttribute));
            _product.setOperationalMode(attributes.getValue(ProductDTO.OperationalModeAttribute));
            _product.setSrsName(attributes.getValue(ProductDTO.SrsNameAttribute));
            if (_head != null) {
                _head.setProduct(_product);
            }
        } else if (_currentNodeName.compareToIgnoreCase(CreationDateDTO.NodeName) == 0) {
            _creationDate = new CreationDateDTO();
            _creationDate.setRefreshFrequency(attributes.getValue(CreationDateDTO.RefreshFrequencyAttribute));
            if (_product != null) {
                _product.setCreationDate(_creationDate);
            }
        } else if (_currentNodeName.compareToIgnoreCase(SourceDTO.NodeName) == 0) {
            _source = new SourceDTO();
            if (_head != null) {
                _head.setSource(_source);
            }
        } else if (_currentNodeName.compareToIgnoreCase(DataDTO.NodeName) == 0) {
            _data = new DataDTO();
            String type = attributes.getValue(DataDTO.TypeAttribute);
            if ((type != null) && (type.length() > 0)) {
                if (type.compareToIgnoreCase("forecast") == 0) {
                    _dwml.setForecast(_data);
                } else  if (type.compareToIgnoreCase("current observations") == 0) {
                    _dwml.setObservation(_data);
                }
            }
        } else if (_currentNodeName.compareToIgnoreCase(LocationDTO.NodeName) == 0) {
            _location = new LocationDTO();
            if (_data != null) {
                _data.setLocation(_location);
            }
        } else if (_currentNodeName.compareToIgnoreCase(PointDTO.NodeName) == 0) {
            PointDTO point = new PointDTO();
            point.setLatitude(new Double(attributes.getValue(PointDTO.LatitudeAttribute)));
            point.setLongitude(new Double(attributes.getValue(PointDTO.LongitudeAttribute)));
            if (_location != null) {
                _location.setPoint(point);
            }
        } else if (_currentNodeName.compareToIgnoreCase(HeightDTO.NodeName) == 0) {
            _height = new HeightDTO();
            _height.setDatum(attributes.getValue(HeightDTO.DatumAttribute));
            _height.setHeightUnits(attributes.getValue(HeightDTO.HeightUnitsAttribute));
            if (_location != null) {
                _location.setHeight(_height);
            }
        } else if (_currentNodeName.compareToIgnoreCase(MoreWeatherInformationDTO.NodeName) == 0) {
            _moreWeatherInformation = new MoreWeatherInformationDTO();
            _moreWeatherInformation.setApplicableLocation(attributes.getValue(MoreWeatherInformationDTO.ApplicableLocationAttribute));
            if (_data != null) {
                _data.setMoreWeatherInformation(_moreWeatherInformation);
            }
        } else if (_currentNodeName.compareToIgnoreCase(TimeLayoutDTO.NodeName) == 0) {
            _timeLayout = new TimeLayoutDTO();
            _timeLayout.setTimeCoordinate(attributes.getValue(TimeLayoutDTO.TimeCoordinateAttribute));
            _timeLayout.setSummarization(attributes.getValue(TimeLayoutDTO.SummarizationAttribute));
        } else if (_currentNodeName.compareToIgnoreCase(TimeLayoutDTO.StartValidTimeNode) == 0) {
            _validTime = new ValidTimeDTO();
            _validTime.setPeriodName(attributes.getValue(ValidTimeDTO.PeriodNameAttribute));
            if (_timeLayout != null) {
                _timeLayout.getStartTimes().add(_validTime);
            }
        } else if (_currentNodeName.compareToIgnoreCase(ParametersDTO.NodeName) == 0) {
            _parameters = new ParametersDTO();
            _parameters.setApplicableLocation(attributes.getValue(ParametersDTO.ApplicableLocationAttribute));
            if (_data != null) {
                _data.setParameters(_parameters);
            }
        } else if (_currentNodeName.compareToIgnoreCase(TemperatureDTO.NodeName) == 0) {
            _temperature = new TemperatureDTO();
            _temperature.setType(attributes.getValue(TemperatureDTO.TypeAttribute));
            _temperature.setUnits(attributes.getValue(TemperatureDTO.UnitsAttribute));
            _temperature.setTimeLayout(attributes.getValue(TemperatureDTO.TimeLayoutAttribute));
            if (_parameters != null) {
                _parameters.getTemperatures().add(_temperature);
            }
        } else if (_currentNodeName.compareToIgnoreCase(ProbabilityOfPrecipitationDTO.NodeName) == 0) {
            _probabilityOfPrecipitation = new ProbabilityOfPrecipitationDTO();
            _probabilityOfPrecipitation.setType(attributes.getValue(ProbabilityOfPrecipitationDTO.TypeAttribute));
            _probabilityOfPrecipitation.setUnits(attributes.getValue(ProbabilityOfPrecipitationDTO.UnitsAttribute));
            _probabilityOfPrecipitation.setTimeLayout(attributes.getValue(ProbabilityOfPrecipitationDTO.TimeLayoutAttribute));
            if (_parameters != null) {
                _parameters.setProbabilityOfPrecipitation(_probabilityOfPrecipitation);
            }
        } else if (_currentNodeName.compareToIgnoreCase(ProbabilityOfPrecipitationDTO.ValueNode) == 0) {
            if (_probabilityOfPrecipitation != null) {
                String nil = attributes.getValue("xsi:nil");
                if ((nil != null) && (nil.length() > 0) && (nil.compareToIgnoreCase("true") == 0)) {
                    _probabilityOfPrecipitation.getValues().add(new Double(0));
                }
            }
        } else if (_currentNodeName.compareToIgnoreCase(WeatherDTO.NodeName) == 0) {
            _weather = new WeatherDTO();
            _weather.setTimeLayout(attributes.getValue(WeatherDTO.TimeLayoutAttribute));
            if (_parameters != null) {
                _parameters.setWeather(_weather);
            }
        } else  if (_weather != null) {
            if (_currentNodeName.compareToIgnoreCase(WeatherConditionsDTO.NodeName) == 0) {
                WeatherConditionsDTO weatherConditions = new WeatherConditionsDTO();
                weatherConditions.setWeatherSummary(attributes.getValue(WeatherConditionsDTO.WeatherSummaryAttribute));
                _weather.getWeatherConditions().add(weatherConditions);
            }
        } else if (_currentNodeName.compareToIgnoreCase(ConditionsIconDTO.NodeName) == 0) {
            _conditionsIcon = new ConditionsIconDTO();
            if (_parameters != null) {
                _parameters.setConditionsIcon(_conditionsIcon);
            }
        } else if (_currentNodeName.compareToIgnoreCase(WordedForecastDTO.NodeName) == 0) {
            _wordedForecast = new WordedForecastDTO();
            _wordedForecast.setTimeLayout(attributes.getValue(WordedForecastDTO.TimeLayoutAttribute));
            _wordedForecast.setDataSource(attributes.getValue(WordedForecastDTO.DataSourceAttribute));
            _wordedForecast.setWordGenerator(attributes.getValue(WordedForecastDTO.WordGeneratorAttribute));
            if (_parameters != null) {
                _parameters.setWordedForecast(_wordedForecast);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        _currentNodeName = null;
        if (qName.compareToIgnoreCase(HeadDTO.NodeName) == 0) {
            _head = null;
        } else if (qName.compareToIgnoreCase(ProductDTO.NodeName) == 0) {
            _product = null;
        } else if (qName.compareToIgnoreCase(CreationDateDTO.NodeName) == 0) {
            _creationDate = null;
        } else if (qName.compareToIgnoreCase(SourceDTO.NodeName) == 0) {
            _source = null;
        } else if (qName.compareToIgnoreCase(DataDTO.NodeName) == 0) {
            _data = null;
        } else if (qName.compareToIgnoreCase(LocationDTO.NodeName) == 0) {
            _location = null;
        } else if (qName.compareToIgnoreCase(HeightDTO.NodeName) == 0) {
            _height = null;
        } else if (qName.compareToIgnoreCase(MoreWeatherInformationDTO.NodeName) == 0) {
            _moreWeatherInformation = null;
        } else if (qName.compareToIgnoreCase(TimeLayoutDTO.NodeName) == 0) {
            _timeLayout = null;
        } else if (qName.compareToIgnoreCase(TimeLayoutDTO.StartValidTimeNode) == 0) {
            _validTime = null;
        } else if (qName.compareToIgnoreCase(ParametersDTO.NodeName) == 0) {
            _parameters = null;
        } else if (qName.compareToIgnoreCase(TemperatureDTO.NodeName) == 0) {
            _temperature = null;
        } else if (qName.compareToIgnoreCase(ProbabilityOfPrecipitationDTO.NodeName) == 0) {
            _probabilityOfPrecipitation = null;
        } else if (qName.compareToIgnoreCase(WeatherDTO.NodeName) == 0) {
            _weather = null;
        } else if (qName.compareToIgnoreCase(ConditionsIconDTO.NodeName) == 0) {
            _conditionsIcon = null;
        } else if (qName.compareToIgnoreCase(WordedForecastDTO.NodeName) == 0) {
            _wordedForecast = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length);
        if ((_currentNodeName != null) && (_currentNodeName.length() > 0)) {
            if (_currentNodeName.compareToIgnoreCase(ProductDTO.CategoryNode) == 0) {
                if (_product != null) {
                    _product.setCategory(text);
                }
            } else if (_currentNodeName.compareToIgnoreCase(CreationDateDTO.NodeName) == 0) {
                if (_creationDate != null) {
                    _creationDate.setCreationDate(getCalendar(text));
                }
            } else if (_currentNodeName.compareToIgnoreCase(LocationDTO.LocationKeyNode) == 0) {
                if (_location != null) {
                    _location.setLocationKey(text);
                }
            } else  if (_currentNodeName.compareToIgnoreCase(LocationDTO.AreaDescriptionNode) == 0) {
                if (_location != null) {
                    _location.setAreaDescription(text);
                }
            } else  if (_currentNodeName.compareToIgnoreCase(HeightDTO.NodeName) == 0) {
                if (_height != null) {
                    if (text.length() > 0) {
                        _height.setValue(new Double(text));
                    }
                }
            } else  if (_currentNodeName.compareToIgnoreCase(MoreWeatherInformationDTO.NodeName) == 0) {
                if (_moreWeatherInformation != null) {
                    _moreWeatherInformation.setLink(text);
                }
            } else  if (_currentNodeName.compareToIgnoreCase(TimeLayoutDTO.LayoutKeyNode) == 0) {
                if (_timeLayout != null) {
                    _timeLayout.setLayoutKey(text);
                }
            } else if (_currentNodeName.compareToIgnoreCase(TimeLayoutDTO.StartValidTimeNode) == 0) {
                if (_validTime != null) {
                    _validTime.setTimeStamp(getCalendar(text));
                }
            } else  if (_temperature != null) {
                if (_currentNodeName.compareToIgnoreCase(TemperatureDTO.NameNode) == 0) {
                    _temperature.setName(text);
                } else if (_currentNodeName.compareToIgnoreCase(TemperatureDTO.ValueNode) == 0) {
                    if (text.length() > 0) {
                        _temperature.getValues().add(new Double(text));
                    }
                }
            } else if (_probabilityOfPrecipitation != null) {
                if (_currentNodeName.compareToIgnoreCase(ProbabilityOfPrecipitationDTO.NameNode) == 0) {
                    _probabilityOfPrecipitation.setName(text);
                } else if (_currentNodeName.compareToIgnoreCase(ProbabilityOfPrecipitationDTO.ValueNode) == 0) {
                    if (text.length() > 0) {
                        _probabilityOfPrecipitation.getValues().add(new Double(text));
                    }
                }
            } else if (_weather != null) {
                if (_currentNodeName.compareToIgnoreCase(WeatherDTO.NameNode) == 0) {
                    _weather.setName(text);
                }
            } else if (_conditionsIcon != null) {
                if (_currentNodeName.compareToIgnoreCase(ConditionsIconDTO.NameNode) == 0) {
                    _conditionsIcon.setName(text);
                } else if (_currentNodeName.compareToIgnoreCase(ConditionsIconDTO.IconLinkNode) == 0) {
                    _conditionsIcon.getIconLink().add(text);
                }
            } else if (_wordedForecast != null) {
                if (_currentNodeName.compareToIgnoreCase(WordedForecastDTO.NameNode) == 0) {
                    _wordedForecast.setName(text);
                } else if (_currentNodeName.compareToIgnoreCase(WordedForecastDTO.TextNode) == 0) {
                    _wordedForecast.getText().add(text);
                }
            }
        }
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        super.warning(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        super.fatalError(e);
    }
}

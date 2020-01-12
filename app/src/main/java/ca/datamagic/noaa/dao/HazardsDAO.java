package ca.datamagic.noaa.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.HazardConditionsDTO;
import ca.datamagic.noaa.dto.HazardDTO;
import ca.datamagic.noaa.dto.HazardsDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class HazardsDAO {
    private static Logger _logger = LogFactory.getLogger(HazardsDAO.class);
    private static String _hazardStart = "<pre>";
    private static String _hazardEnd = "</pre>";
    private enum ParseState {
        None,
        ParsingHazard
    }

    private static List<String> downloadHazards(String urlSpec) throws Throwable {
        URL url = new URL(urlSpec.replaceAll("http://", "https://"));
        _logger.info("url: " + url.toString());
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            connection.setRequestProperty("Sec-Fetch-Site", "none");
            connection.connect();
            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
            if ((responseCode > 299) && (responseCode < 400)) {
                String location = connection.getHeaderField("Location");
                connection = (HttpURLConnection) new URL(location).openConnection();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            ParseState parseState = ParseState.None;
            List<String> hazards = new ArrayList<String>();
            StringBuilder builder = null;
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.trim();
                if (parseState == ParseState.None) {
                    int index = currentLine.toLowerCase().indexOf(_hazardStart);
                    if (index > -1) {
                        builder = new StringBuilder();
                        if ((index + _hazardStart.length() + 1) < currentLine.length()) {
                            builder.append(currentLine.substring(index + _hazardStart.length() + 1));
                        }
                        parseState = ParseState.ParsingHazard;
                    }
                } else if (parseState == ParseState.ParsingHazard) {
                    int index = currentLine.toLowerCase().indexOf(_hazardEnd);
                    if (index > -1) {
                        if (index > 0) {
                            builder.append(currentLine.substring(0, index));
                        }
                        hazards.add(builder.toString());
                        builder = null;
                        parseState = ParseState.None;
                    } else {
                        if (builder.length() > 0) {
                            builder.append("\n");
                        }
                        builder.append(currentLine);
                    }
                }
            }
            return hazards;
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("timeout")) {
                return null;
            }
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
        }
        return null;
    }

    public List<String> getHazards(DWMLDTO dwml) throws Throwable {
        List<String> watchesAndWarnings = new ArrayList<String>();
        if (dwml != null) {
            DataDTO forecast = dwml.getForecast();
            if (forecast != null) {
                ParametersDTO parameters = forecast.getParameters();
                if (parameters != null) {
                    List<HazardsDTO> hazards = parameters.getHazards();
                    if (hazards != null) {
                        for (int ii = 0; ii < hazards.size(); ii++) {
                            HazardConditionsDTO hazardConditions = hazards.get(ii).getHazardConditions();
                            if (hazardConditions != null) {
                                List<HazardDTO> hazardList = hazardConditions.getHazards();
                                if (hazardList != null) {
                                    for (int jj = 0; jj < hazardList.size(); jj++) {
                                        HazardDTO hazard = hazardList.get(jj);
                                        List<String> hazardTextList = downloadHazards(hazard.getHazardTextUrl());
                                        if (hazardTextList != null) {
                                            watchesAndWarnings.addAll(hazardTextList);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return watchesAndWarnings;
    }
}

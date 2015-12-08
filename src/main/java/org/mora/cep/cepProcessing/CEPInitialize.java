package org.mora.cep.cepProcessing;

import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.config.SiddhiConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruveni on 08/12/15.
 */
public class CEPInitialize {
    public SiddhiManager CEPInit(){

        //configuration to add siddhi extension
        List extensionClasses = new ArrayList();
        extensionClasses.add(org.mora.cep.sidhdhiExtention.CalculateBoundary.class);
        extensionClasses.add(org.mora.cep.sidhdhiExtention.RadarFilePath.class);
        extensionClasses.add(org.mora.cep.sidhdhiExtention.IsNearStation.class);
        extensionClasses.add(org.mora.cep.sidhdhiExtention.IsNearTimestamp.class);


        SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();
        siddhiConfiguration.setSiddhiExtensions(extensionClasses);

        SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

        //stream definitions
        siddhiManager.defineStream("define stream reflectStream (reflexMatrix string )  ");
        siddhiManager.defineStream("define stream boundaryStream ( filePath string, boundary string )  ");
        siddhiManager.defineStream("define stream WeatherStream (stationId string, dateTime string, dewTemperature double, relativeHumidity double, seaPressure double, pressure double, temperature double, windDirection double, windSpeed double, latitude double, longitude double) ");
        siddhiManager.defineStream("define stream FilterStream (stationId string, dateTime string,latitude double, longitude double,temperature double) ");

        return siddhiManager;
    }
}
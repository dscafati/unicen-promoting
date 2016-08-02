package com.unicen.app.indicators;

import java.util.HashMap;
import java.util.Set;

public class Factory {
    private static HashMap<String, Indicator> indicators = new HashMap<String, Indicator>();
    private static Boolean registered = false;

    /**
     * Edit this method to register new indicators
     */
    private static void _registerIndicators() {

        // Register just once, trying to avoid singleton here
        if (registered) {
            return;
        } else {
            registered = true;
        }

        // List of indicators
        indicators.put("Average", new AverageIndicator());
        indicators.put("Degree of progress", new ProgressIndicator());

    }

    /**
     * @return HashMap<String, String> Pairs <factory key,  human readable name>
     */
    public static HashMap<String, String> listIndicators() {
        _registerIndicators();

        HashMap<String, String> ret = new HashMap<String, String>();

        for (String i : indicators.keySet()) {
            ret.put(i, indicators.get(i).getName());
        }

        return ret;
    }


    public static Indicator get(String key) {
        _registerIndicators();

        return indicators.get(key);
    }
}

package com.mapr.mgrsvc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("volume")
@PropertySource("classpath:/maprparm.properties")

public class VolumeParm {

        private final Map<String, String> parmMap = new HashMap<>();

        public Map<String,String> getParm() {
            return parmMap;
        }
        public String getParmValue(String parmkey, String user) {
            String value = parmMap.get(parmkey);
            if (value.equals("$user"))
                return user;
            else
                return value;
        }


    }


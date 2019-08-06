/*
 * Copyright 2017 </>DevStudy.net.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.devstudy.myphotos.common.producer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

/**
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class ApplicationPropertiesStorage extends AbstractPropertiesLoader {

    private static final String APPLICATION_CONFIG_FILE = "MYPHOTOS_CONFIG_FILE";

    private static final String APPLICATION_CONFIG_PROPERTY_PREFFIX = "myphotos.";

    private final Properties applicationProperties = new Properties();

    Properties getApplicationProperties() {
        return applicationProperties;
    }

    @PostConstruct
    private void postConstruct() {
        loadProperties(applicationProperties, "classpath:application.properties");
        overrideApplicationProperties(applicationProperties, System.getenv(), "System environment");
        overrideApplicationProperties(applicationProperties, System.getProperties(), "System properties");
        resolvePropertyVariables(applicationProperties);
        logger.log(Level.INFO, "Application properties loaded successful");
    }

    private void overrideApplicationProperties(Properties applicationProperties, Map<?, ?> map, String description) {
        String configFilePath = null;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            if (key.startsWith(APPLICATION_CONFIG_PROPERTY_PREFFIX)) {
                applicationProperties.setProperty(key, String.valueOf(entry.getValue()));
                logger.log(Level.INFO, "Overrided application property {0}, defined in the {1}", new String[]{key, description});
            } else if (APPLICATION_CONFIG_FILE.equals(key)) {
                configFilePath = String.valueOf(entry.getValue());
            }
        }
        if (configFilePath != null && Files.exists(Paths.get(configFilePath))) {
            loadProperties(applicationProperties, configFilePath);
            logger.log(Level.INFO, "Overrided application properties from file {0}, defined in the {1}", new String[]{configFilePath, description});
        }
    }

    private void resolvePropertyVariables(Properties applicationProperties) {
        List<String> variables = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : applicationProperties.entrySet()) {
            String value = (String) entry.getValue();
            if (value.startsWith("${")) {
                if (value.endsWith("}")) {
                    String var = value.substring(2, value.length() - 1);
                    entry.setValue(resolvePropertyVariable(var));
                    variables.add(var);
                } else {
                    throw new IllegalArgumentException("Missing '}' for property value: " + value);
                }
            }
        }
        if (!variables.isEmpty()) {
            logger.log(Level.INFO, "Application property variables resolved successful: " + variables);
        }
    }

    private String resolvePropertyVariable(String var) {
        String value = System.getProperty(var);
        if (value != null) {
            return value;
        }
        value = System.getenv(var);
        if (value != null) {
            return value;
        }
        throw new IllegalArgumentException("Application variable '" + var + "' not defined");
    }
}

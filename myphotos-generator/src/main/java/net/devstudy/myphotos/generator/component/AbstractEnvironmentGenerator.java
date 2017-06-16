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
package net.devstudy.myphotos.generator.component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public abstract class AbstractEnvironmentGenerator {

    private final Properties environment = new Properties();
    private final Map<String, String> variables = new HashMap<>();

    protected final void execute() throws Exception {
        Map<String, Object> properties = setupProperties();
        try (EJBContainer ec = EJBContainer.createEJBContainer(properties)) {
            Context ctx = ec.getContext();
            ctx.bind("inject", this);
            generate();
        }
    }

    protected abstract void generate() throws Exception;

    private Map<String, Object> setupProperties() throws IOException {
        Map<String, Object> properties = new HashMap<>();
        readEnvironmentProperties();
        setupVariables();
        setupClasspathEnvironmentProperties(properties);
        properties.put(EJBContainer.MODULES, getModulePath());
        return properties;
    }

    private void readEnvironmentProperties() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("environment.properties")) {
            environment.load(in);
        }
    }

    private void setupVariables() {
        String userHome = System.getProperty("user.home");
        variables.put("${M2_LOCAL}", userHome + "/.m2/repository");
    }

    private void setupClasspathEnvironmentProperties(Map<String, Object> properties) throws IOException {
        for (Map.Entry<Object, Object> entry : environment.entrySet()) {
            properties.put((String) entry.getKey(), entry.getValue());
        }
    }

    private File[] getModulePath() {
        List<File> files = new ArrayList<>();
        String[] modules = environment.getProperty(EJBContainer.MODULES).split(",");
        for (String module : modules) {
            files.add(new File(resolveModule(module)));
        }
        files.add(new File("target/classes"));
        return files.toArray(new File[files.size()]);
    }

    private String resolveModule(String module) {
        String result = module;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }
}


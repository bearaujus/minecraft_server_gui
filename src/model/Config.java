/*
 * Copyright (c) 2020, Bear Au Jus - ジュースとくま
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package model;

import engine.AppCore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class Config {

    private List<String> key = new ArrayList<>();
    private HashMap<String, String> table = new HashMap<>();
    private String path_config;
    private File file_config;
    private boolean changeChecker = false;

    public Config() {
        // Current Config Entity List
        key.add("current-default-jar-name=");   // Key 0
        key.add("log-creation=");               // Key 1
        key.forEach((px) -> {
            table.put(px, "");
        });
        loadConfig();
    }

    private void loadConfig() {
        path_config = AppCore.getProgramPath() + File.separator + "config.conf";
        file_config = new File(path_config);
        if (!file_config.exists()) {
            loadRawConfig();
            createNew();
            AppCore.addConsole("Server config created.");
        } else {
            try {
                try (BufferedReader br = new BufferedReader(new FileReader(getFileConfig()))) {
                    String line;
                    int checker = 0;
                    boolean separatorChecker = false;
                    while ((line = br.readLine()) != null) {
                        if (!(line.charAt(0) == '#')) {
                            checker++;
                            String value = "", tmpKey = "";
                            for (int i = 0; i < line.length(); i++) {
                                if (separatorChecker) {
                                    value += String.valueOf(line.charAt(i));
                                } else {
                                    tmpKey += String.valueOf(line.charAt(i));
                                }
                                if (line.charAt(i) == '=') {
                                    separatorChecker = true;
                                }
                            }
                            separatorChecker = false;
                            table.replace(tmpKey, value);
                        }
                    }
                    if (checker != key.size()) {
                        loadRawConfig();
                        createNew();
                    }
                }
            } catch (IOException e) {
                loadRawConfig();
                createNew();
            }
        }
    }

    private void createNew() {
        StringBuilder queueData = new StringBuilder();
        Date date = new Date(System.currentTimeMillis());
        queueData.append("#Minecraft Server GUI properties").append("\n");
        queueData.append("#").append(date).append("\n");
        for (int i = 0; i < key.size(); i++) {
            queueData.append(key.get(i)).append(table.get(key.get(i))).append("\n");
        }
        path_config = AppCore.getProgramPath() + File.separator + "config.conf";
        file_config = new File(path_config);
        File tmp = file_config;
        try {
            if (tmp.exists()) {
                tmp.delete();
            }
            FileOutputStream fop = new FileOutputStream(tmp);
            tmp.createNewFile();
            byte[] contentInBytes = queueData.toString().getBytes();
            fop.write(contentInBytes);
            fop.flush();
        } catch (IOException e) {
        }
        loadConfig();
    }

    public void save() {
        if (changeChecker) {
            createNew();
            AppCore.addConsole("Server config updated.");
        } else {
            AppCore.addConsole("No changes made.");
        }
        loadConfig();
        changeChecker = false;
    }

    public void save(String jarName) {
        if (AppCore.SERVERJAR.isJarExist(jarName)) {
            if (changeChecker) {
                createNew();
                AppCore.addConsole("Server config updated.");
            } else {
                AppCore.addConsole("No changes made.");
            }
            loadConfig();
            changeChecker = false;
        } else {
            AppCore.addConsole("Ups, we cant save your configuration because the JAR files is missing, used alternate JAR instead.");
            createNew();
            AppCore.addConsole("Default server JAR executor changed to '" + getCurrentDefaultJarName() + "'.");
        }
        AppCore.APPMAIN.redirectPanel_serverMainJar();
    }

    private File getFileConfig() {
        path_config = AppCore.getProgramPath() + File.separator + "config.conf";
        file_config = new File(path_config);
        if (!file_config.exists()) {
            loadRawConfig();
            createNew();
        }
        return file_config;
    }

    public void resetMainServerJarOnly() {
        table.replace("current-default-jar-name=", "server.jar");
        createNew();
    }

    private void loadRawConfig() {
        table.replace("current-default-jar-name=", "server.jar");
        table.replace("log-creation=", "true");
    }

    public String getPath_config() {
        loadConfig();
        classChecker();
        return path_config;
    }

    public String getJarName() {
        loadConfig();
        classChecker();
        return table.get("current-default-jar-name=");
    }

    public void createNewConfig() {
        loadRawConfig();
        createNew();
    }

    private void classChecker() {
        if (!getFileConfig().exists()) {
            loadRawConfig();
            createNew();
        }
    }

    public String getCurrentDefaultJarName() {
        return table.get("current-default-jar-name=");
    }

    public void setCurrentDefaultJarName(String currentDefaultJarName) {
        final String tmp = getCurrentDefaultJarName();
        table.replace("current-default-jar-name=", currentDefaultJarName);
        if (!tmp.equals(getCurrentDefaultJarName())) {
            changeChecker = true;
            AppCore.addConsole("Default server JAR executor changed to '" + currentDefaultJarName + "'.");
        }
    }

    public String getLogCreation() {
        return table.get("log-creation=");
    }

    public void setLogCreation(boolean logCreation) {
        final String tmp = getLogCreation();
        table.replace("log-creation=", String.valueOf(tmp));
        if (!tmp.equals(getLogCreation())) {
            changeChecker = true;
            AppCore.addConsole("'Log Creation' changed to '" + logCreation + "'.");
        }
    }
}

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

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class ServerProperties {

    private final ArrayList<String> LIST_ENTITY_KEY = new ArrayList<>();
    private HashMap<String, String> LIST_ENTITY_PROPERTIES = new HashMap<>();
    private Boolean changeChecker = false;
    private String path_serverProperties;
    private File file_serverProperties;

    public ServerProperties() {
        LIST_ENTITY_KEY.add("spawn-protection=");
        LIST_ENTITY_KEY.add("max-tick-time=");
        LIST_ENTITY_KEY.add("query.port=");
        LIST_ENTITY_KEY.add("generator-settings=");
        LIST_ENTITY_KEY.add("force-gamemode=");
        LIST_ENTITY_KEY.add("allow-nether=");
        LIST_ENTITY_KEY.add("enforce-whitelist=");
        LIST_ENTITY_KEY.add("gamemode=");
        LIST_ENTITY_KEY.add("broadcast-console-to-ops=");
        LIST_ENTITY_KEY.add("enable-query=");
        LIST_ENTITY_KEY.add("player-idle-timeout=");
        LIST_ENTITY_KEY.add("difficulty=");
        LIST_ENTITY_KEY.add("spawn-monsters=");
        LIST_ENTITY_KEY.add("broadcast-rcon-to-ops=");
        LIST_ENTITY_KEY.add("op-permission-level=");
        LIST_ENTITY_KEY.add("pvp=");
        LIST_ENTITY_KEY.add("snooper-enabled=");
        LIST_ENTITY_KEY.add("level-type=");
        LIST_ENTITY_KEY.add("hardcore=");
        LIST_ENTITY_KEY.add("enable-command-block=");
        LIST_ENTITY_KEY.add("max-players=");
        LIST_ENTITY_KEY.add("network-compression-threshold=");
        LIST_ENTITY_KEY.add("resource-pack-sha1=");
        LIST_ENTITY_KEY.add("max-world-size=");
        LIST_ENTITY_KEY.add("function-permission-level=");
        LIST_ENTITY_KEY.add("rcon.port=");
        LIST_ENTITY_KEY.add("server-port=");
        LIST_ENTITY_KEY.add("debug=");
        LIST_ENTITY_KEY.add("server-ip=");
        LIST_ENTITY_KEY.add("spawn-npcs=");
        LIST_ENTITY_KEY.add("allow-flight=");
        LIST_ENTITY_KEY.add("level-name=");
        LIST_ENTITY_KEY.add("view-distance=");
        LIST_ENTITY_KEY.add("resource-pack=");
        LIST_ENTITY_KEY.add("spawn-animals=");
        LIST_ENTITY_KEY.add("white-list=");
        LIST_ENTITY_KEY.add("rcon.password=");
        LIST_ENTITY_KEY.add("generate-structures=");
        LIST_ENTITY_KEY.add("online-mode=");
        LIST_ENTITY_KEY.add("max-build-height=");
        LIST_ENTITY_KEY.add("level-seed=");
        LIST_ENTITY_KEY.add("prevent-proxy-connections=");
        LIST_ENTITY_KEY.add("use-native-transport=");
        LIST_ENTITY_KEY.add("motd=");
        LIST_ENTITY_KEY.add("enable-rcon=");
        LIST_ENTITY_KEY.forEach((px) -> {
            LIST_ENTITY_PROPERTIES.put(px, null);
        });

        if (!getFileServerProperties().exists()) {
            createNew();
        }
        loadExist();
    }

    private void createNew() {
        loadRaw();
        createFile();
        AppCore.addConsole("Server properties created.");
    }

    private void createFile() {
        StringBuilder queueData = new StringBuilder();
        Date date = new Date(System.currentTimeMillis());
        queueData.append("#Minecraft server properties").append("\n");
        queueData.append("#").append(date).append("\n");

        for (int i = 0; i < LIST_ENTITY_KEY.size(); i++) {
            queueData.append(LIST_ENTITY_KEY.get(i)).append(LIST_ENTITY_PROPERTIES.get(LIST_ENTITY_KEY.get(i))).append("\n");
        }

        try {
            FileOutputStream fop = new FileOutputStream(getFileServerProperties());
            getFileServerProperties().createNewFile();
            byte[] contentInBytes = queueData.toString().getBytes();
            fop.write(contentInBytes);
            fop.flush();
        } catch (IOException e) {
            createNew();
        }
    }

    public void save() {
        if (changeChecker) {
            createFile();
            AppCore.addConsole("Server properties saved.");
            changeChecker = false;
        } else {
            AppCore.addConsole("No changes made.");
        }
        loadExist();
    }

    public void run() {
        AppCore.APPMAIN.setServerProperties();
        AppCore.APPMAIN.initServerProperties();
        if (changeChecker) {
            createFile();
            AppCore.addConsole("Server properties saved.");
            changeChecker = false;
        }
        loadExist();
        AppCore.addConsole("Server properties loaded.");
    }

    public void reset() {
        loadRaw();
        createFile();
        AppCore.addConsole("Server properties reseted.");
        AppCore.addConsole("Server properties saved.");
    }

    private void loadExist() {
        if (!getFileServerProperties().exists()) {
            createNew();
        }
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(getFileServerProperties()))) {
                String line;
                int checker = 0;
                boolean separatorChecker = false;
                while ((line = br.readLine()) != null) {
                    if (!(line.charAt(0) == '#')) {
                        checker++;
                        String value = "", key = "";
                        for (int i = 0; i < line.length(); i++) {
                            if (separatorChecker) {
                                value += String.valueOf(line.charAt(i));
                            } else {
                                key += String.valueOf(line.charAt(i));
                            }

                            if (line.charAt(i) == '=') {
                                separatorChecker = true;
                            }
                        }
                        separatorChecker = false;
                        LIST_ENTITY_PROPERTIES.replace(key, value);
                    }
                }
                if (checker != LIST_ENTITY_PROPERTIES.size()) {
                    createNew();
                }
            }
        } catch (IOException e) {
            createNew();
        }
    }

    private void loadRaw() {
        LIST_ENTITY_PROPERTIES.replace("spawn-protection=", "16");
        LIST_ENTITY_PROPERTIES.replace("max-tick-time=", "60000");
        LIST_ENTITY_PROPERTIES.replace("query.port=", "25565");
        LIST_ENTITY_PROPERTIES.replace("generator-settings=", "");
        LIST_ENTITY_PROPERTIES.replace("force-gamemode=", "false");
        LIST_ENTITY_PROPERTIES.replace("allow-nether=", "true");
        LIST_ENTITY_PROPERTIES.replace("enforce-whitelist=", "false");
        LIST_ENTITY_PROPERTIES.replace("gamemode=", "survival");
        LIST_ENTITY_PROPERTIES.replace("broadcast-console-to-ops=", "true");
        LIST_ENTITY_PROPERTIES.replace("enable-query=", "false");
        LIST_ENTITY_PROPERTIES.replace("player-idle-timeout=", "0");
        LIST_ENTITY_PROPERTIES.replace("difficulty=", "easy");
        LIST_ENTITY_PROPERTIES.replace("spawn-monsters=", "true");
        LIST_ENTITY_PROPERTIES.replace("broadcast-rcon-to-ops=", "true");
        LIST_ENTITY_PROPERTIES.replace("op-permission-level=", "4");
        LIST_ENTITY_PROPERTIES.replace("pvp=", "true");
        LIST_ENTITY_PROPERTIES.replace("snooper-enabled=", "true");
        LIST_ENTITY_PROPERTIES.replace("level-type=", "default");
        LIST_ENTITY_PROPERTIES.replace("hardcore=", "false");
        LIST_ENTITY_PROPERTIES.replace("enable-command-block=", "false");
        LIST_ENTITY_PROPERTIES.replace("max-players=", "20");
        LIST_ENTITY_PROPERTIES.replace("network-compression-threshold=", "256");
        LIST_ENTITY_PROPERTIES.replace("resource-pack-sha1=", "");
        LIST_ENTITY_PROPERTIES.replace("max-world-size=", "29999984");
        LIST_ENTITY_PROPERTIES.replace("function-permission-level=", "2");
        LIST_ENTITY_PROPERTIES.replace("rcon.port=", "25575");
        LIST_ENTITY_PROPERTIES.replace("server-port=", "25565");
        LIST_ENTITY_PROPERTIES.replace("debug=", "false");
        LIST_ENTITY_PROPERTIES.replace("server-ip=", "");
        LIST_ENTITY_PROPERTIES.replace("spawn-npcs=", "true");
        LIST_ENTITY_PROPERTIES.replace("allow-flight=", "false");
        LIST_ENTITY_PROPERTIES.replace("level-name=", "world");
        LIST_ENTITY_PROPERTIES.replace("view-distance=", "10");
        LIST_ENTITY_PROPERTIES.replace("resource-pack=", "");
        LIST_ENTITY_PROPERTIES.replace("spawn-animals=", "true");
        LIST_ENTITY_PROPERTIES.replace("white-list=", "false");
        LIST_ENTITY_PROPERTIES.replace("rcon.password=", "");
        LIST_ENTITY_PROPERTIES.replace("generate-structures=", "true");
        LIST_ENTITY_PROPERTIES.replace("online-mode=", "true");
        LIST_ENTITY_PROPERTIES.replace("max-build-height=", "256");
        LIST_ENTITY_PROPERTIES.replace("level-seed=", "");
        LIST_ENTITY_PROPERTIES.replace("prevent-proxy-connections=", "false");
        LIST_ENTITY_PROPERTIES.replace("use-native-transport=", "true");
        LIST_ENTITY_PROPERTIES.replace("motd=", "A Minecraft Server");
        LIST_ENTITY_PROPERTIES.replace("enable-rcon=", "false");
    }

    public String getServerIP() {
        return LIST_ENTITY_PROPERTIES.get("server-ip=");
    }

    public String getServerPort() {
        return LIST_ENTITY_PROPERTIES.get("server-port=");
    }

    public String getRconPort() {
        return LIST_ENTITY_PROPERTIES.get("rcon.port=");
    }

    public String getOnlineMode() {
        return LIST_ENTITY_PROPERTIES.get("online-mode=");
    }

    public String getMOTD() {
        return LIST_ENTITY_PROPERTIES.get("motd=");
    }

    public String getLevelName() {
        return LIST_ENTITY_PROPERTIES.get("level-name=");
    }

    public String getDifficulty() {
        return LIST_ENTITY_PROPERTIES.get("difficulty=");
    }

    public String getGameMode() {
        return LIST_ENTITY_PROPERTIES.get("gamemode=");
    }

    public String getSpawnMonsters() {
        return LIST_ENTITY_PROPERTIES.get("spawn-monsters=");
    }

    public String getSpawnNPCS() {
        return LIST_ENTITY_PROPERTIES.get("spawn-npcs=");
    }

    public String getPVP() {
        return LIST_ENTITY_PROPERTIES.get("pvp=");
    }

    public String getAllowNether() {
        return LIST_ENTITY_PROPERTIES.get("allow-nether=");
    }

    public String getHardcore() {
        return LIST_ENTITY_PROPERTIES.get("hardcore=");
    }

    public void setServerIP(String serverIP) {
        final String tmp = getServerIP();
        LIST_ENTITY_PROPERTIES.replace("server-ip=", serverIP);
        if (!tmp.equals(getServerIP())) {
            changeChecker = true;
            AppCore.addConsole("'SERVER IP' changed to '" + serverIP + "' !");
        }
    }

    public void setServerPort(String serverPort) {
        final String tmp = getServerPort();
        LIST_ENTITY_PROPERTIES.replace("server-port=", serverPort);
        if (!tmp.equals(getServerPort())) {
            changeChecker = true;
            AppCore.addConsole("'SERVER PORT' changed to '" + serverPort + "' !");
        }
    }

    public void setRconPort(String rconPort) {
        final String tmp = getRconPort();
        LIST_ENTITY_PROPERTIES.replace("rcon.port=", rconPort);
        if (!tmp.equals(getRconPort())) {
            changeChecker = true;
            AppCore.addConsole("'RCON PORT' changed to '" + rconPort + "' !");
        }
    }

    public void setOnlineMode(Boolean onlineMode) {
        final String tmp = getOnlineMode();
        LIST_ENTITY_PROPERTIES.replace("online-mode=", onlineMode.toString());
        if (!tmp.equals(getOnlineMode())) {
            changeChecker = true;
            AppCore.addConsole("'ONLINE MODE' changed to '" + onlineMode + "' !");
        }
    }

    public void setMOTD(String motd) {
        final String tmp = getMOTD();
        LIST_ENTITY_PROPERTIES.replace("motd=", motd);
        if (!tmp.equals(getMOTD())) {
            changeChecker = true;
            AppCore.addConsole("'MOTD' changed to '" + motd + "' !");
        }
    }

    public void setLevelName(String levelName) {
        final String tmp = getLevelName();
        LIST_ENTITY_PROPERTIES.replace("level-name=", levelName);
        if (!tmp.equals(getLevelName())) {
            changeChecker = true;
            AppCore.addConsole("'LEVEL-NAME' changed to '" + levelName + "' !");
        }
    }

    public void setDifficulty(String difficulty) {
        final String tmp = getDifficulty();
        LIST_ENTITY_PROPERTIES.replace("difficulty=", difficulty);
        if (!tmp.equals(getDifficulty())) {
            changeChecker = true;
            AppCore.addConsole("'DIFFICULTY' changed to '" + difficulty + "' !");
        }
    }

    public void setGameMode(String gameMode) {
        final String tmp = getGameMode();
        LIST_ENTITY_PROPERTIES.replace("gamemode=", gameMode);
        if (!tmp.equals(getGameMode())) {
            changeChecker = true;
            AppCore.addConsole("'GAMEMODE' changed to '" + gameMode + "' !");
        }
    }

    public void setSpawnMonsters(Boolean spawnMonsters) {
        final String tmp = getSpawnMonsters();
        LIST_ENTITY_PROPERTIES.replace("spawn-monsters=", spawnMonsters.toString());
        if (!tmp.equals(getSpawnMonsters())) {
            changeChecker = true;
            AppCore.addConsole("'SPAWN MONSTERS' changed to '" + spawnMonsters + "' !");
        }
    }

    public void setSpawnNPCS(Boolean spawnNPCS) {
        final String tmp = getSpawnNPCS();
        LIST_ENTITY_PROPERTIES.replace("spawn-npcs=", spawnNPCS.toString());
        if (!tmp.equals(getSpawnNPCS())) {
            changeChecker = true;
            AppCore.addConsole("'SPAWN NPCS' changed to '" + spawnNPCS + "' !");
        }
    }

    public void setPVP(Boolean pvp) {
        final String tmp = getPVP();
        LIST_ENTITY_PROPERTIES.replace("pvp=", pvp.toString());
        if (!tmp.equals(getPVP())) {
            changeChecker = true;
            AppCore.addConsole("'PVP' changed to '" + pvp + "' !");
        }
    }

    public void setAllowNether(Boolean allowNether) {
        final String tmp = getAllowNether();
        LIST_ENTITY_PROPERTIES.replace("allow-nether=", allowNether.toString());
        if (!tmp.equals(getAllowNether())) {
            changeChecker = true;
            AppCore.addConsole("'ALLOW NETHER' changed to '" + allowNether + "' !");
        }
    }

    public void setHardcore(Boolean hardcore) {
        final String tmp = getHardcore();
        LIST_ENTITY_PROPERTIES.replace("hardcore=", hardcore.toString());
        if (!tmp.equals(getHardcore())) {
            changeChecker = true;
            AppCore.addConsole("'HARDCORE' changed to '" + hardcore + "' !");
        }
    }

    private File getFileServerProperties() {
        path_serverProperties = DataStructure.getPath_serverData() + "server.properties";
        file_serverProperties = new File(path_serverProperties);
        return file_serverProperties;
    }
}

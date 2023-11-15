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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import tools.DIRTools;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class ServerJar {

    private List<String> currentJar;

    public ServerJar() {
        refreshJarList();
    }

    public boolean run() {
        refreshJarList();
        if (currentJar.isEmpty()) {
            AppCore.addConsole("Not any jar exist, directed to force download JAR menu.");
            return false;
        }
        boolean doubleCheck = false;
        for (int i = 0; i < currentJar.size(); i++) {
            if (currentJar.get(i).equals(AppCore.CONFIG.getJarName())) {
                doubleCheck = true;
            }
        }
        if (!doubleCheck) {
            AppCore.addConsole("Cannot load configured main server JAR !");
            AppCore.CONFIG.setCurrentDefaultJarName(currentJar.get(0));
            AppCore.CONFIG.save();
            AppCore.addConsole("Using alternate JAR '" + AppCore.CONFIG.getJarName() + "'.");
            currentJar.stream().filter((px) -> (px.equals(AppCore.CONFIG.getJarName()))).forEachOrdered((px) -> {
                AppCore.addConsole("'" + px + "' loaded !");
            });
        }
        return true;
    }

    private void refreshJarList() {
        currentJar = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(DataStructure.getPath_serverJar()))) {
            paths.filter(Files::isRegularFile).forEach((t) -> {
                try {
                    String fileName = DIRTools.getFileNameFromPath(t.toString());
                    if (DIRTools.isJarFile(fileName)) {
                        currentJar.add(fileName);
                    }
                } catch (ArrayIndexOutOfBoundsException x) {
                }
            });
        } catch (NoSuchFileException x) {
        } catch (IOException ex) {
        }
    }

    public String getCurrentJarName() {
        refreshJarList();
        for (String px : currentJar) {
            if (px.equals(AppCore.CONFIG.getJarName())) {
                AppCore.addConsole("'" + px + "' loaded !");
                return px;
            }
        }
        AppCore.addConsole("Fail to load server main JAR, config reseted to default !");
        AppCore.CONFIG.createNewConfig();
        return AppCore.CONFIG.getJarName();
    }

    public String getCurrentJarFilePath() {
        refreshJarList();
        return DataStructure.getPath_serverJar() + getCurrentJarName();
    }

    public String[] getCBmodel() {
        refreshJarList();
        String output[];
        if (currentJar.isEmpty()) {
            output = new String[1];
            output[0] = "No Item - Please Download Item";
        } else {
            output = new String[currentJar.size()];
            for (int i = 0; i < currentJar.size(); i++) {
                output[i] = currentJar.get(i);
            }
        }
        return output;
    }

    public Integer getCBSelectedindex() {
        refreshJarList();
        for (int i = 0; i < currentJar.size(); i++) {
            if (currentJar.get(i).equals(AppCore.CONFIG.getJarName())) {
                return i;
            }
        }
        return 0;
    }

    public void check() {
        refreshJarList();
        if (currentJar.isEmpty()) {
            AppCore.addConsole("Cannot load config main Server JAR !");
            AppCore.APPMAIN.redirectPanel_serverMainJar_forceDownloadJar();
            return;
        }
        for (int i = 0; i < currentJar.size(); i++) {
            if (currentJar.get(i).equals(AppCore.CONFIG.getJarName())) {
                return;
            }
        }
        AppCore.addConsole("Cannot load configured main server JAR !");
        AppCore.CONFIG.setCurrentDefaultJarName(currentJar.get(0));
        AppCore.CONFIG.save();
        AppCore.addConsole("Using alternate JAR '" + AppCore.CONFIG.getJarName() + "'.");
        currentJar.stream().filter((px) -> (px.equals(AppCore.CONFIG.getJarName()))).forEachOrdered((px) -> {
            AppCore.addConsole("'" + px + "' loaded !");
        });
        AppCore.APPMAIN.redirectPanel_serverMainJar();
    }

    public boolean isJarExist(String jarName) {
        refreshJarList();
        for (int i = 0; i < currentJar.size(); i++) {
            if (currentJar.get(i).equals(jarName)) {
                return true;
            }
        }
        return false;
    }

    public void refreshJarPublic() {
        currentJar = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(DataStructure.getPath_serverJar()))) {
            paths.filter(Files::isRegularFile).forEach((t) -> {
                try {
                    String fileName = DIRTools.getFileNameFromPath(t.toString());
                    if (DIRTools.isJarFile(fileName)) {
                        currentJar.add(fileName);
                    }
                } catch (ArrayIndexOutOfBoundsException x) {
                }
            });
        } catch (NoSuchFileException x) {
        } catch (IOException ex) {
        }
    }
}

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
package engine;

import model.ApplicationUI;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AgreedServerEula;
import model.Config;
import model.ServerExec;
import model.ServerJar;
import model.ServerProperties;
import thread.ServerIP;
import thread.ServerThread;
import thread.ServerTimerEvent;
import tools.StatusScanner;
import view.AppMainFrame;
import view.AppStartUp;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class AppCore {

    private static StringBuffer CONSOLE = new StringBuffer();
    private static ServerThread SERVERTHREAD;

    public static ApplicationUI APPUI = new ApplicationUI();
    public static Config CONFIG = new Config();
    public static ServerJar SERVERJAR = new ServerJar();
    public static AgreedServerEula AGREEDSERVEREULA;
    public static ServerExec SERVEREXEC;
    public static ServerProperties SERVERPROPERTIES;

    public static AppMainFrame APPMAIN;
    public static ServerTimerEvent SERVERTIMEREVENT;
    public static ServerIP SERVERIPTHREAD;

    public static void main(String[] args) {
        if (startupChecker()) {
            new AppStartUp().setVisible(true);
        } else {
            init();
        }
    }

    public static void init() {
        CONSOLE = new StringBuffer();
        SERVEREXEC = new ServerExec();
        AGREEDSERVEREULA = new AgreedServerEula();
        SERVERPROPERTIES = new ServerProperties();
        SERVERTIMEREVENT = new ServerTimerEvent();
        SERVERIPTHREAD = new ServerIP();
        APPMAIN = new AppMainFrame();
        APPMAIN.setVisible(true);
    }

    public static void addConsole(String param) {
        AppCore.CONSOLE.append("[ System ] ").append(param).append("\n");
        StatusScanner.statusChecker(param);
    }

    public static void addConsoleThread(String param) {
        String tmpUnableMoveFile = "", tmpUnableDeleteFile = "";
        try {
            tmpUnableMoveFile = getStringOnlyFromString(param).substring(0, 38);
            tmpUnableDeleteFile = getStringOnlyFromString(param).substring(0, 39);
        } catch (Exception e) {
        }
        if (getStringOnlyFromString(param).equals("[::] [Server thread/ERROR]: Exception stopping the server")
                || getStringOnlyFromString(param).equals("java.lang.NullPointerException: null")
                || getStringOnlyFromString(param).equals("	at net.minecraft.server.MinecraftServer.a(SourceFile:) ~[server.jar:?]")
                || getStringOnlyFromString(param).equals("	at net.minecraft.server.MinecraftServer.s(SourceFile:) ~[server.jar:?]")
                || getStringOnlyFromString(param).equals("	at wd.s(SourceFile:) ~[server.jar:?]")
                || getStringOnlyFromString(param).equals("	at net.minecraft.server.MinecraftServer.run(SourceFile:) [server.jar:?]")
                || getStringOnlyFromString(param).equals("	at java.lang.Thread.run(Unknown Source) [?:.._]")
                || tmpUnableMoveFile.equals("-- ::, main ERROR Unable to move file")
                || tmpUnableDeleteFile.equals("-- ::, main ERROR Unable to delete file")) {
            StatusScanner.setFileIsUsedByANotherProcess(true);
        } else {
            AppCore.CONSOLE.append(param).append("\n");
            StatusScanner.statusChecker(param);
        }
    }

    public static StringBuffer getCONSOLE() {
        return CONSOLE;
    }

    private static void newServerThread() {
        SERVERTHREAD = null;
        SERVERTHREAD = new ServerThread();
    }

    public static void startServerThread() {
        newServerThread();
        SERVERTHREAD.start();
    }

    public static ServerThread getServerThread() {
        return SERVERTHREAD;
    }

    public static Integer getIntegerFromString(String textField) {
        int px = 0;
        String tmp2 = textField;
        tmp2 = tmp2.replaceAll("[^\\d.]", "");
        tmp2 = tmp2.replaceAll("\\.", "");
        try {
            px = Integer.parseInt(tmp2);
        } catch (NumberFormatException e) {
        }
        return px;
    }

    public static String getStringOnlyFromString(String param) {
        return param.replaceAll("[0-9]", "");
    }

    public static final String getProgramPath() {
        URL url = AppCore.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;
        try {
            jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AppCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        String parentPath = new File(jarPath).getParentFile().getPath();
        return parentPath;
    }

    private static boolean startupChecker() {
        final File check_folderServer = new File(AppCore.getProgramPath() + File.separator + "ServerData" + File.separator);
        return !check_folderServer.exists();
    }
}

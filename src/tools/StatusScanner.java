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
package tools;

import engine.AppCore;
import static engine.AppCore.APPMAIN;
import static engine.AppCore.SERVERTIMEREVENT;
import static engine.AppCore.addConsole;
import static engine.AppCore.getStringOnlyFromString;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class StatusScanner {

    public static boolean fileIsUsedByANotherProcess = false;
    private static boolean failedToBindPort = false, serverStartedOnLocalHostOnly = false;
    private static Integer counter = 0;

    public static void statusChecker(String param) {
        if (APPMAIN != null) {
            APPMAIN.setConsole();
        }
        if (getStringOnlyFromString(param).equals("[::] [Server thread/INFO]: Done (.s)! For help, type \"help\"")) {
            if (!SERVERTIMEREVENT.isExitingEventIsExecuted() && !serverStartedOnLocalHostOnly) {
                AppCore.SERVERTIMEREVENT.IPRefresher_start();
                AppCore.SERVERTIMEREVENT.serverActivatingEvent_stop();
                APPMAIN.serverStarted();
                APPMAIN.refreshIP();
                APPMAIN.sm_l_serverState.setIcon(AppCore.APPUI.getServerOnline());
            }
            if (serverStartedOnLocalHostOnly) {
                AppCore.SERVERTIMEREVENT.serverActivatingEvent_stop();
                APPMAIN.serverStarted();
                APPMAIN.refreshIPforLocalHost();
                APPMAIN.sm_l_serverState.setIcon(AppCore.APPUI.getServerOnline());
                APPMAIN.sm_t_shareThisToYourFriend.setText("-");
                APPMAIN.sm_t_shareThisToYourFriend.setEnabled(false);
                APPMAIN.sm_t_publicIPAddress.setText("-");
                APPMAIN.sm_t_publicIPAddress.setEnabled(false);
                APPMAIN.sm_b_copy.setEnabled(false);
                APPMAIN.sm_b_refreshNow.setEnabled(false);
                APPMAIN.sm_t_status.setText("ACTIVE - LOCALHOST ONLY");
                APPMAIN.sm_t_status.setForeground(new Color(255, 153, 0));
                AppCore.APPMAIN.sm_l_serverState.setIcon(AppCore.APPUI.getServerRunning());
                if (!AppCore.SERVERTIMEREVENT.isBoolean_serverExitingEvent()) {
                    JOptionPane.showMessageDialog(null, "Server started in localhost only, \nPlease check your server properties settings !");
                }
                serverStartedOnLocalHostOnly = false;
            }
        }
        if (param.substring(param.lastIndexOf(':') + 1).equals(" All chunks are saved")) {
            counter++;
            if (counter == 6) {
                APPMAIN.serverStopped();
                counter = 0;
            }
        }
        if (getStringOnlyFromString(param).equals("[::] [Server thread/WARN]: **** FAILED TO BIND TO PORT!")) {
            SERVERTIMEREVENT.IPRefresher_stop();
            SERVERTIMEREVENT.serverActivatingEvent_stop();
            SERVERTIMEREVENT.serverStoppingEvent_stop();
            failedToBindPort = true;
        }
        if (failedToBindPort) {
            if (getStringOnlyFromString(param).equals("[::] [Server thread/INFO]: Saving worlds")) {
                APPMAIN.serverStopped();
                failedToBindPort = false;
                if (fileIsUsedByANotherProcess) {
                    if (!AppCore.SERVERTIMEREVENT.isBoolean_serverExitingEvent()) {
                        JOptionPane.showMessageDialog(null, "File is used by another process, \nPlease dont run multiple instance of application !");
                    }
                    fileIsUsedByANotherProcess = false;
                } else {
                    if (!AppCore.SERVERTIMEREVENT.isBoolean_serverExitingEvent()) {
                        JOptionPane.showMessageDialog(null, "Failed to bind port, \nPlease check your port settings !");
                    }
                }
            }
        }
        try {
            String localHostChecker[] = param.substring(param.lastIndexOf(' ') + 1).split(":");
            if (getStringOnlyFromString(param).equals("[::] [Server thread/INFO]: Starting Minecraft server on *:") || localHostChecker[0].equals("127.0.0.1")) {
                serverStartedOnLocalHostOnly = true;
            }
        } catch (Exception e) {
        }
        if (APPMAIN != null) {
            APPMAIN.setConsole();
        }
        if (param.equals("Server stopped running.")) {
            SERVERTIMEREVENT.serverExitingEvent_stop();
        }
        if (param.equals("exit")) {
            addConsole("Server executor successfully closed.");
        }
    }

    public static void setFileIsUsedByANotherProcess(boolean fileIsUsedByANotherProcess) {
        StatusScanner.fileIsUsedByANotherProcess = fileIsUsedByANotherProcess;
    }
}

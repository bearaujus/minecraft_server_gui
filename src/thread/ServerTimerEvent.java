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
package thread;

import engine.AppCore;
import static engine.AppCore.addConsole;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import model.Log;
import model.WaitingQuotes;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class ServerTimerEvent {

    Integer ipRefresherCounter;
    private Timer serverActivatingEvent, serverStoppingEvent, IPRefresher, serverExitingEvent, serverCleanningAfterExit;
    private boolean boolean_serverActivatingEvent = false,
            boolean_serverStoppingEvent = false,
            boolean_IPRefresher = false,
            boolean_serverExitingEvent = false,
            boolean_serverCleanningAfterExit = false,
            pointer = false;

    public boolean isExitingEventIsExecuted() {
        if (serverExitingEvent == null) {
            return false;
        } else if (!serverExitingEvent.isRunning()) {
            return false;
        }
        return true;
    }

    public void IPRefresher_stop() {
        if (IPRefresher != null) {
            if (IPRefresher.isRunning()) {
                IPRefresher.stop();
                boolean_IPRefresher = false;
            }
        }
    }

    public void serverActivatingEvent_stop() {
        if (serverActivatingEvent != null) {
            if (serverActivatingEvent.isRunning()) {
                serverActivatingEvent.stop();
                boolean_serverActivatingEvent = false;
            }
        }
    }

    public void serverStoppingEvent_stop() {
        if (serverStoppingEvent != null) {
            if (serverStoppingEvent.isRunning()) {
                serverStoppingEvent.stop();
                boolean_serverStoppingEvent = false;
            }
        }
    }

    public void serverExitingEvent_stop() {
        if (serverExitingEvent != null) {
            if (serverExitingEvent.isRunning()) {
                serverExitingEvent.stop();
                boolean_serverExitingEvent = false;
                serverCleanningAfterExit_start();
            }
        }
    }

    public void IPRefresher_RefreshNow() {
        AppCore.APPMAIN.refreshIP();
        ipRefresherCounter = -1;
    }

    public void serverActivatingEvent_start() {
        addConsole("Attempting to start server.");
        if (!boolean_serverActivatingEvent) {
            boolean_serverActivatingEvent = true;
            serverActivatingEvent = new Timer(400, new ActionListener() {
                int counter = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (counter) {
                        case 0:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server");
                            if (!pointer) {
                                AppCore.APPMAIN.sm_t_status.setForeground(new Color(255, 153, 0));
                            }
                            break;
                        case 1:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server .");
                            if (!pointer) {
                                AppCore.APPMAIN.sm_l_serverState.setIcon(AppCore.APPUI.getServerRunning());
                            }
                            break;
                        case 2:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server . .");
                            if (!pointer) {
                                AppCore.AGREEDSERVEREULA.run();
                            }

                            break;
                        case 3:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server . . .");
                            if (!pointer) {
                                AppCore.SERVERPROPERTIES.run();
                            }

                            break;
                        case 4:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server");
                            if (!pointer) {
                                AppCore.SERVEREXEC.run();
                            }

                            break;
                        case 5:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server .");
                            if (!pointer) {
                                if (AppCore.SERVERJAR.run()) {
                                    AppCore.startServerThread();
                                } else {
                                    AppCore.SERVERTIMEREVENT.serverActivatingEvent_stop();
                                    AppCore.APPMAIN.serverStopped();
                                    AppCore.APPMAIN.redirectPanel_serverMainJar_forceDownloadJar();
                                    AppCore.APPMAIN.cameFromActivatingServer = true;
                                }
                            }
                            break;
                        case 6:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server . .");
                            if (!pointer) {
                                AppCore.addConsole(WaitingQuotes.getRandomWaiting());
                                pointer = true;
                            }
                            break;
                        case 7:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to start server . .");
                            break;
                    }
                    if (counter == 8) {
                        counter = 0;
                    }
                }
            });
            serverActivatingEvent.start();
            pointer = false;
        }
    }

    public void serverStoppingEvent_start() {
        addConsole("Attempting to stop server.");
        serverActivatingEvent_stop();
        if (!boolean_serverStoppingEvent) {
            boolean_serverStoppingEvent = true;
            AppCore.APPMAIN.sm_t_status.setText("Cleanning Up . . .");
            AppCore.APPMAIN.sm_t_status.setForeground(Color.red);
            serverStoppingEvent = new Timer(400, new ActionListener() {
                int counter = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (counter) {
                        case 0:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to stop server");
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.red);
                            break;
                        case 1:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to stop server .");
                            break;
                        case 2:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to stop server . .");
                            break;
                        case 3:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to stop server . . .");
                            break;
                    }
                    if (counter == 4) {
                        counter = 0;
                    }
                }
            });
            serverStoppingEvent.start();
        }
    }

    public void IPRefresher_start() {
        if (!boolean_IPRefresher) {
            boolean_IPRefresher = true;
            ipRefresherCounter = 120;
            IPRefresher = new Timer(1000, new ActionListener() {
                boolean notCd = true;
                int cd = 10;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (ipRefresherCounter == -1) {
                        AppCore.APPMAIN.refreshIP();
                        notCd = false;
                    }
                    if (notCd) {
                        AppCore.APPMAIN.sm_b_refreshNow.setText("Refresh Now ( Auto in : " + ipRefresherCounter-- + " sec )");
                    } else {
                        ipRefresherCounter--;
                        AppCore.APPMAIN.sm_b_refreshNow.setText("Cooldown : " + cd-- + " sec ");
                        AppCore.APPMAIN.sm_b_refreshNow.setEnabled(false);
                        if (cd == -1) {
                            ipRefresherCounter = 120;
                            AppCore.APPMAIN.sm_b_refreshNow.setText("Refresh Now ( Auto in : " + ipRefresherCounter-- + " sec )");
                            cd = 10;
                            notCd = true;
                            AppCore.APPMAIN.sm_b_refreshNow.setEnabled(true);
                        }
                    }
                }
            });
            IPRefresher.start();
        }
    }

    public void serverExit_start() {
        if (!boolean_serverExitingEvent) {
            boolean_serverExitingEvent = true;
            IPRefresher_stop();
            serverActivatingEvent_stop();
            serverStoppingEvent_stop();
            AppCore.APPMAIN.sm_t_status.setText("Cleanning up . . .");
            AppCore.APPMAIN.disableAll();
            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
            serverExitingEvent = new Timer(400, new ActionListener() {
                int counter = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isThreadActive = true;
                    if (AppCore.getServerThread() == null) {
                        isThreadActive = false;
                    }
                    switch (counter) {
                        case 0:
                            IPRefresher_stop();
                            serverActivatingEvent_stop();
                            serverStoppingEvent_stop();
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to exit program");
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_b_activeServer.setText("<Html>Please don't force close this program to avoid any corruption files !</Html>");
                            break;
                        case 1:
                            IPRefresher_stop();
                            serverActivatingEvent_stop();
                            serverStoppingEvent_stop();
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to exit program .");
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_b_activeServer.setText("<Html>Please don't force close this program to avoid any corruption files !</Html>");
                            break;
                        case 2:
                            IPRefresher_stop();
                            serverActivatingEvent_stop();
                            serverStoppingEvent_stop();
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to exit program . .");
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_b_activeServer.setText("<Html>Please don't force close this program to avoid any corruption files !</Html>");
                            if (!isThreadActive) {
                                serverExitingEvent_stop();
                                serverCleanningAfterExit_start();
                            }
                            break;
                        case 3:
                            IPRefresher_stop();
                            serverActivatingEvent_stop();
                            serverStoppingEvent_stop();
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("Attempting to exit program . . .");
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_b_activeServer.setText("<Html>Please don't force close this program to avoid any corruption files !</Html>");
                            break;
                    }
                    if (counter == 4) {
                        counter = 0;
                    }
                }
            });
            serverExitingEvent.start();
        }
    }

    public void serverCleanningAfterExit_start() {
        addConsole("Attempting to exit program.");
        if (!boolean_serverCleanningAfterExit) {
            boolean_serverCleanningAfterExit = true;
            IPRefresher_stop();
            serverActivatingEvent_stop();
            serverStoppingEvent_stop();
            serverExitingEvent_stop();
            AppCore.APPMAIN.sm_t_status.setText("Finalizing . . .");
            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
            serverCleanningAfterExit = new Timer(400, new ActionListener() {
                int counter = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (counter) {
                        case 0:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_t_status.setText("Cleanning started process and threat");
                            if (AppCore.getServerThread() != null) {
                                if (AppCore.getServerThread().isAlive()) {
                                    AppCore.getServerThread().addCommand("exit");
                                    AppCore.getServerThread().terminate();
                                    AppCore.getServerThread().PROCESS.destroy();
                                    AppCore.getServerThread().RUNTIME.freeMemory();
                                    Runtime.getRuntime().freeMemory();
                                    addConsole("Memory final cleaning successfully. ");
                                }
                            }
                            break;
                        case 1:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_t_status.setText("Cleanning started process and threat .");
                            Log.createLog();
                            break;
                        case 2:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_t_status.setText("Making log");
                            break;
                        case 3:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setForeground(Color.RED);
                            AppCore.APPMAIN.sm_t_status.setText("Making log .");
                            break;
                        case 4:
                            counter++;
                            AppCore.APPMAIN.sm_t_status.setText("All Green !");
                            AppCore.APPMAIN.sm_t_status.setForeground(new Color(68, 204, 0));
                            break;
                        case 5:
                            counter++;
                            serverCleanningAfterExit.stop();
                            System.exit(0);
                            break;
                    }
                }
            });
        }
        serverCleanningAfterExit.start();
    }

    public Timer getServerActivatingEvent() {
        return serverActivatingEvent;
    }

    public Timer getServerStoppingEvent() {
        return serverStoppingEvent;
    }

    public Timer getIPRefresher() {
        return IPRefresher;
    }

    public Timer getServerExitingEvent() {
        return serverExitingEvent;
    }

    public Timer getServerCleanningAfterExit() {
        return serverCleanningAfterExit;
    }

    public boolean isBoolean_serverActivatingEvent() {
        return boolean_serverActivatingEvent;
    }

    public boolean isBoolean_serverStoppingEvent() {
        return boolean_serverStoppingEvent;
    }

    public boolean isBoolean_IPRefresher() {
        return boolean_IPRefresher;
    }

    public boolean isBoolean_serverExitingEvent() {
        return boolean_serverExitingEvent;
    }

    public boolean isBoolean_serverCleanningAfterExit() {
        return boolean_serverCleanningAfterExit;
    }

}

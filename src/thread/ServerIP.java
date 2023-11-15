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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class ServerIP {

    private String publicIP, privateIP;
    private Thread fetchPublicIP;
    private Runnable processFetchingPublicIP;

    public ServerIP() {
        processFetchingPublicIP = getRunnable();
        fetchPublicIP = new Thread(processFetchingPublicIP);
        init();
    }

    public String getPublicIP() {
        processFetchingPublicIP = getRunnable();
        fetchPublicIP = new Thread(processFetchingPublicIP);
        fetchPublicIP.start();
        return publicIP;
    }

    public String getPrivateIP() {
        try {
            privateIP = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            AppCore.addConsole("Cannot load private ip, please check your ethernet adapter !");
            privateIP = "-";
        }
        return privateIP;
    }

    private void init() {
        getPrivateIP();
        fetchPublicIP.start();
    }

    private Runnable getRunnable() {
        return (new Runnable() {
            Boolean status = true;

            @Override
            public void run() {
                while (status) {
                    try {
                        URL whatismyip = new URL("http://bot.whatismyipaddress.com");
                        URLConnection con = whatismyip.openConnection();
                        con.setConnectTimeout(5000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                whatismyip.openStream()));
                        publicIP = in.readLine();
                    } catch (IOException ex) {
                        AppCore.addConsole("Failed to fetch public ip, make sure you are connected to the internet !");
                    }
                    terminate();
                }
            }

            public void terminate() {
                status = false;
            }
        });
    }
}

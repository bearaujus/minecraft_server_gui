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
import java.util.Date;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class Log {

    public static void createLog() {
        try {
            File newLog = new File(DataStructure.getPath_serverLog() + "LatestLog.txt");
            if (newLog.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(newLog));
                    String tmp = "", line, outputLine = "";
                    int p = 0;
                    while ((line = br.readLine()) != null) {
                        if (p == 0) {
                            tmp = line;
                            p++;
                        }
                        outputLine += line + "\n";
                    }
                    p = 0;
                    tmp = tmp.replace(":", "-");
                    String nameOldFile[] = tmp.split(" "), t = "";
                    for (String string : nameOldFile) {
                        p++;
                        if (p > 3) {
                            t += string + " ";
                        }
                    }
                    t += ".txt";
                    File oldLog = new File(DataStructure.getPath_oldLog() + t);
                    FileOutputStream fop = new FileOutputStream(oldLog);
                    byte[] dataOldLog = outputLine.getBytes();
                    fop.write(dataOldLog);
                    fop.flush();

                    Date date = new Date(System.currentTimeMillis());
                    FileOutputStream fop2 = new FileOutputStream(newLog);
                    AppCore.addConsole("Log created.");
                    String outputContainer = "#Created on : " + date.toString() + "\n\n" + AppCore.getCONSOLE().toString();
                    byte[] contentInBytes = outputContainer.getBytes();
                    fop2.write(contentInBytes);
                    fop2.flush();
                } catch (IOException ex) {
                }
            } else {
                Date date = new Date(System.currentTimeMillis());
                FileOutputStream fop = new FileOutputStream(newLog);
                AppCore.addConsole("Log created.");
                String outputContainer = "#Created on : " + date.toString() + "\n\n" + AppCore.getCONSOLE().toString();
                byte[] contentInBytes = outputContainer.getBytes();
                fop.write(contentInBytes);
                fop.flush();
            }
        } catch (IOException z) {
            AppCore.addConsole("Fail to create log.");
        }
    }

}

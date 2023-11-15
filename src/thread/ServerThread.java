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
import static engine.AppCore.addConsoleThread;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import model.DataStructure;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class ServerThread extends Thread implements Runnable {

    public Runtime RUNTIME = Runtime.getRuntime();
    private String COMMAND;

    private BufferedWriter WRITER;
    public Process PROCESS;

    @Override
    public void run() {
        COMMAND = "cmd.exe /k \"cd /d " + DataStructure.getPath_serverData() + " && java -Xms2G -Xmx2G -jar \"" + AppCore.SERVERJAR.getCurrentJarFilePath() + "\" nogui\"";
        try {
            PROCESS = RUNTIME.exec(COMMAND);
            InputStream is = PROCESS.getInputStream();
            OutputStream os = PROCESS.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            WRITER = new BufferedWriter(new OutputStreamWriter(os));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    addConsoleThread(line);
                }
            }
        } catch (IOException ex) {
        }
    }

    public void addCommand(String command) {
        if (PROCESS.isAlive()) {
            try {
                WRITER.write(command + "\n");
                WRITER.flush();
            } catch (IOException ex) {
            }
        }
    }

    public void terminate() {
        if (PROCESS.isAlive()) {
            PROCESS.destroy();
            PROCESS.destroyForcibly();
        }

    }
}

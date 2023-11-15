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
import java.util.List;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class ServerExec {
    
    private String path_exec;
    private File file_exec;
    private int XMS, XMX;

    private String CONTAINER;

    public int getXMS() {
        return XMS;
    }

    public void setXMS(Integer XMS) {
        this.XMS = XMS;
        AppCore.addConsole("'XMS' run parameter changed to '" + XMS + "' !");
    }

    public int getXMX() {
        return XMX;
    }

    public void setXMX(Integer XMX) {
        this.XMX = XMX;
        AppCore.addConsole("'XMX' run parameter changed to '" + XMX + "' !");
    }

    public ServerExec() {
        if (!getFileExec().exists()) {
            createServerExec();
        }
        readAttributeMainExec();
    }

    public void run() {
        final int xms = Integer.parseInt(AppCore.APPMAIN.sm_js_xms.getValue().toString());
        final int xmx = Integer.parseInt(AppCore.APPMAIN.sm_js_xmx.getValue().toString());
        if (!getFileExec().exists()) {
            createServerExec();
            readAttributeMainExec();
        }
        if (XMS != xms || XMX != xmx) {
            if (XMS != xms && XMX != xmx) {
                setXMS(xms);
                setXMX(xmx);
            } else if (XMS != xms) {
                setXMS(xms);
            } else if (XMX != xmx) {
                setXMX(xmx);
            }
            updateServerExec();
        }
        AppCore.APPMAIN.sm_js_xms.setValue(getXMS());
        AppCore.APPMAIN.sm_js_xmx.setValue(getXMX());
        AppCore.addConsole("Server executor running.");
    }

    private void createServerExec() {
        XMS = 1;
        XMX = 1;
        if (getFileExec().exists()) {
            getFileExec().delete();
        }
        try (FileOutputStream fop = new FileOutputStream(getFileExec())) {
            getFileExec().createNewFile();
            CONTAINER = "java -Xms" + 1 + "G -Xmx" + 1 + "G -jar \"" + AppCore.SERVERJAR.getCurrentJarFilePath() + "\" nogui";
            byte[] containerInByte = CONTAINER.getBytes();
            fop.write(containerInByte);
            fop.flush();
            AppCore.addConsole("Server executor created.");
        } catch (IOException ex) {
        }
    }

    private void updateServerExec() {
        if (getFileExec().exists()) {
            getFileExec().delete();
        }
        try (FileOutputStream fop = new FileOutputStream(getFileExec())) {
            getFileExec().createNewFile();
            CONTAINER = "java -Xms" + XMS + "G -Xmx" + XMX + "G -jar \"" + AppCore.SERVERJAR.getCurrentJarFilePath() + "\" nogui";
            byte[] containerInByte = CONTAINER.getBytes();
            fop.write(containerInByte);
            fop.flush();
            AppCore.addConsole("Server executor updated.");
        } catch (IOException ex) {
        }
    }

    private void readAttributeMainExec() {
        List<String> outputLine = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFileExec()));
            String line;
            while ((line = br.readLine()) != null) {
                outputLine.add(line);
            }
        } catch (IOException ex) {
            createServerExec();
        }
        String tmp[] = outputLine.get(0).split(" ");
        int Pointer = 0;
        for (String px : tmp) {
            if (Pointer == 1) {
                XMS = AppCore.getIntegerFromString(px);
            }
            if (Pointer == 2) {
                XMX = AppCore.getIntegerFromString(px);
            }
            Pointer++;
        }
    }
    
    private File getFileExec() {
        path_exec = DataStructure.getPath_serverData() + "Executor.bat";
        file_exec = new File(path_exec);
        return file_exec;
    }
    
    public String getFilePath() {
        path_exec = DataStructure.getPath_serverData() + "Executor.bat";
        file_exec = new File(path_exec);
        return path_exec;
    }
}

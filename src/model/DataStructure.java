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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.DIRTools;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class DataStructure {

    private static String path_serverData = getProgramPath() + File.separator + "ServerData" + File.separator,
            path_serverJar = getProgramPath() + File.separator + "ServerJar" + File.separator,
            path_serverLog = getProgramPath() + File.separator + "ServerLog" + File.separator,
            path_oldLog = getProgramPath() + File.separator + "ServerLog" + File.separator + "OldLog" + File.separator,
            path_tmpDownload = getProgramPath() + File.separator + "tmpdl.tmp";
    private static File folder_serverJar = new File(getProgramPath() + File.separator + "ServerJar" + File.separator),
            folder_serverData = new File(getProgramPath() + File.separator + "ServerData" + File.separator),
            folder_serverLog = new File(getProgramPath() + File.separator + "ServerLog" + File.separator),
            folder_oldLog = new File(getProgramPath() + File.separator + "ServerLog" + File.separator + "OldLog" + File.separator),
            file_tmpDownload = new File(getProgramPath() + File.separator + "tmpdl.tmp");

    public static String getPath_serverData() {
        DIRTools.checkerFolderPath(path_serverData);
        return path_serverData;
    }

    public static String getPath_serverJar() {
        DIRTools.checkerFolderPath(path_serverJar);
        return path_serverJar;
    }

    public static String getPath_serverLog() {
        DIRTools.checkerFolderPath(path_serverLog);
        return path_serverLog;
    }

    public static String getPath_oldLog() {
        DIRTools.checkerFolderPath(path_serverLog);
        DIRTools.checkerFolderPath(path_oldLog);
        return path_oldLog;
    }

    public static String getPath_tmpDownload() {
        return path_tmpDownload;
    }

    public static File getFolder_serverJar() {
        DIRTools.checkerFolderPath(path_serverJar);
        return folder_serverJar;
    }

    public static File getFolder_serverData() {
        DIRTools.checkerFolderPath(path_serverData);
        return folder_serverData;
    }

    public static File getFolder_serverLog() {
        DIRTools.checkerFolderPath(path_serverLog);
        return folder_serverLog;
    }

    public static File getFolder_oldLog() {
        DIRTools.checkerFolderPath(path_serverLog);
        DIRTools.checkerFolderPath(path_oldLog);
        return folder_oldLog;
    }

    public static File getFile_tmpDownload() {
        return file_tmpDownload;
    }

    public static final String getProgramPath() {
        URL url = DataStructure.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;
        try {
            jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        String parentPath = new File(jarPath).getParentFile().getPath();
        return parentPath;
    }
}

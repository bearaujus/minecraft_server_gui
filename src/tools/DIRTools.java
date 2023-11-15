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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import model.DataStructure;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
final public class DIRTools {

    public static void checkerFolderPath(String folderPath) {
        final File tmp = new File(folderPath);
        if (!tmp.exists()) {
            tmp.mkdir();
        }
    }

    public static boolean isFilePathExsist(File file) {
        return file.exists();
    }

    public static boolean isJarFile(String fileName) {
        final String tmp = fileName.substring(fileName.lastIndexOf("."));
        return tmp.equals(".jar");
    }

    public static String getFileNameFromPath(String url) {
        return url.substring(url.lastIndexOf('\\') + 1);
    }

    public static String moveDownloadedTmpJar(String newFileName) {
        String filename = newFileName;
        int pointer = 0;
        while (!DataStructure.getFile_tmpDownload().renameTo(new File(DataStructure.getPath_serverJar() + filename))) {
            pointer++;
            filename = removeExtensionFiles(newFileName) + "_" + pointer + ".jar";
        }
        if (pointer == 0) {
            return "Download completed, saved as '" + newFileName + "' !";
        }
        return "Download completed, file '" + newFileName + "' is already exist, your files saved as '" + filename + "' !";
    }

    public static String moveCopiedTmpJar(String newFileName) {
        String filename = newFileName;
        int pointer = 0;
        while (!DataStructure.getFile_tmpDownload().renameTo(new File(DataStructure.getPath_serverJar() + filename))) {
            pointer++;
            filename = removeExtensionFiles(newFileName) + "_" + pointer + ".jar";
        }
        if (pointer == 0) {
            return "Adding JAR Success, saved as '" + newFileName + "' !";
        }
        return "Adding JAR Success, file '" + newFileName + "' is already exist, your files saved as '" + filename + "' !";
    }

    public static String removeExtensionFiles(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }

    public static void openFolder(String path) {
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException ex) {
        }
    }
}

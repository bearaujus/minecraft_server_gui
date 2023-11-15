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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class AgreedServerEula {

    StringBuilder agreedServerEula;
    Date curDate;
    private String path_agreedServerEula;
    private File file_agreedServerEula;

    public AgreedServerEula() {
        if (!getFileAgreedServerEula().exists()) {
            createNew();
            AppCore.addConsole("Server eula created.");
        }
    }

    private void createNew() {
        loadNew();
        try {
            getFileAgreedServerEula().createNewFile();
            FileOutputStream fop = new FileOutputStream(getFileAgreedServerEula());
            getFileAgreedServerEula().createNewFile();
            byte[] contentInBytes = agreedServerEula.toString().getBytes();
            fop.write(contentInBytes);
            fop.flush();
        } catch (IOException e) {
        }
    }

    public void run() {
        createNew();
    }

    private void loadNew() {
        curDate = new Date(System.currentTimeMillis());
        agreedServerEula = new StringBuilder();
        agreedServerEula.append("#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).")
                .append("\n")
                .append("#")
                .append(curDate)
                .append("\n")
                .append("eula=true");
    }

    private File getFileAgreedServerEula() {
        path_agreedServerEula = DataStructure.getPath_serverData() + "eula.txt";
        file_agreedServerEula = new File(path_agreedServerEula);
        return file_agreedServerEula;
    }
}

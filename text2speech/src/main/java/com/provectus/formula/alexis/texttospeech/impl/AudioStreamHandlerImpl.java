package com.provectus.formula.alexis.texttospeech.impl;

import com.provectus.formula.alexis.texttospeech.AudioStreamHandler;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;


public class AudioStreamHandlerImpl implements AudioStreamHandler {
    String file;


    public AudioStreamHandlerImpl(String file_name) {
        file = file_name;

    }

    public AudioStreamHandlerImpl() {
        file = "test.mp3";
    }

    @Override
    public void processAudioStream(InputStream audio) {


        File targetFile = new File(file);

        try {
            java.nio.file.Files.copy(
                    audio,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IOUtils.closeQuietly(audio);

    }

}

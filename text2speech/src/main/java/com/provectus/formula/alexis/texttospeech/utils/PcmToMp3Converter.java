package com.provectus.formula.alexis.texttospeech.utils;

import net.sourceforge.lame.lowlevel.LameEncoder;
import net.sourceforge.lame.mp3.Lame;
import net.sourceforge.lame.mp3.MPEGMode;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.IOUtils.toByteArray;

public class PcmToMp3Converter {

    public static InputStream encodePcmToMp3(AudioInputStream audio) {

        byte[] pcm = {-1};

        try {
            pcm = toByteArray(audio);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AudioFormat inputFormat = audio.getFormat();

        LameEncoder encoder = new LameEncoder(
                inputFormat,
                48,
                MPEGMode.MONO,
                Lame.QUALITY_HIGH,
                false);


        ByteArrayOutputStream mp3 = new ByteArrayOutputStream();
        byte[] buffer = new byte[encoder.getPCMBufferSize()];

        int bytesToTransfer = Math.min(buffer.length, pcm.length);
        int bytesWritten;
        int currentPcmPosition = 0;
        while (0 < (bytesWritten = encoder.encodeBuffer(pcm, currentPcmPosition, bytesToTransfer, buffer))) {
            currentPcmPosition += bytesToTransfer;
            bytesToTransfer = Math.min(buffer.length, pcm.length - currentPcmPosition);

            mp3.write(buffer, 0, bytesWritten);
        }

        encoder.close();

        InputStream out;
        out = new ByteArrayInputStream(mp3.toByteArray());

        return out;
    }
}

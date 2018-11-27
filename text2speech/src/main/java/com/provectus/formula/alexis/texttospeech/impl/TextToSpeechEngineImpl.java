package com.provectus.formula.alexis.texttospeech.impl;

import com.provectus.formula.alexis.texttospeech.AudioStreamHandler;
import com.provectus.formula.alexis.texttospeech.TextToSpeechEngine;
import com.provectus.formula.alexis.texttospeech.utils.PcmToMp3Converter;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.server.MaryProperties;

import java.io.InputStream;
import java.util.Locale;


public class TextToSpeechEngineImpl implements TextToSpeechEngine {

    private MaryInterface marytts;
    private static final String RU_VOICE = "ac-nsh";
    private static final String EN_VOICE = "cmu-slt-hsmm";
    private static final Locale RUSSIAN = new Locale("ru");
    private static final Locale ENGLISH = new Locale("en_US");

    public MaryInterface getMarytts() {
        return marytts;
    }

    public TextToSpeechEngineImpl() {
        System.out.println("#########" + MaryProperties.maryBase());
        try {
            marytts = new LocalMaryInterface();
            marytts.setLocale(RUSSIAN);
            marytts.setVoice(RU_VOICE);

        } catch (MaryConfigurationException e) {
            e.printStackTrace();
        }
    }


    public InputStream getAudioStream(String text) {
        InputStream audio = null;
        try {
            audio = PcmToMp3Converter.encodePcmToMp3(marytts.generateAudio(text));
        } catch (SynthesisException e) {
            e.printStackTrace();
        }
        return audio;
    }

    public void getAudioStreamAsync(String text, AudioStreamHandler Saver) {
        AsyncThread aThread = new AsyncThread(text, Saver);
        aThread.start();
    }

    private class AsyncThread extends Thread {
        String text;
        AudioStreamHandler saver;


        AsyncThread(String inText, AudioStreamHandler inSaver) {
            saver = inSaver;
            text = inText;
        }


        @Override
        public void run() {
            InputStream audio;
            try {
                audio = PcmToMp3Converter.encodePcmToMp3(marytts.generateAudio(text));
                saver.processAudioStream(audio);
            } catch (SynthesisException e) {
                e.printStackTrace();
            }
        }
    }

}
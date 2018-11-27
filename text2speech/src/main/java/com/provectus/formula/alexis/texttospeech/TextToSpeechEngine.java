package com.provectus.formula.alexis.texttospeech;

import java.io.InputStream;

public interface TextToSpeechEngine {

    void getAudioStreamAsync(String text, AudioStreamHandler Saver);

    InputStream getAudioStream(String text);

}

package com.provectus.formula.alexis.texttospeech;

import java.io.InputStream;

public interface AudioStreamHandler {

    void processAudioStream(InputStream audio);

}

package com.provectus.formula.alexis.texttospeech.main;

import com.provectus.formula.alexis.texttospeech.impl.AudioStreamHandlerImpl;
import com.provectus.formula.alexis.texttospeech.impl.TextToSpeechEngineImpl;

public class Main {

    /**
     * The main method from which our application is starting
     *
     * @param args
     */
    public static void main(String[] args) {


        TextToSpeechEngineImpl tts1 =new TextToSpeechEngineImpl();
        AudioStreamHandlerImpl TASS1 = new AudioStreamHandlerImpl();

 /*
        System.out.println("основной поток 1" );
        tts1.getAudioStreamAsync("Привет, проверка работы асинхронного метода","test1",TASS1);
        System.out.println("основной поток 2" );
        tts1.getAudioStreamAsync("Бла бла бла бла","test2",TASS1);
        System.out.println("основной поток 3" );
        tts1.getAudioStreamAsync("тест один два три","test3",TASS1);
        System.out.println("основной поток 4" );

        long i=0;
        for (i=0;i<10; i++){
            tts1.getAudioStreamAsync("тест один два три"+i,"тест"+i,TASS1);
            System.out.println("поток :"+i );
        }
// */
/*
      if (args.length>1){
          TASS1.setFullFileName(args[0]);
          String text="";
          for(int i=1;i<args.length; i++){text=text+" "+args[i];}
          tts1.getAudioStreamAsync(text,TASS1);
      }
*/

    }

}
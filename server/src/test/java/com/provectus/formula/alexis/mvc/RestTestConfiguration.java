package com.provectus.formula.alexis.mvc;

import com.provectus.formula.alexis.services.TextToSpeechService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("rest-test")
@Configuration
public class RestTestConfiguration {

    @Bean
    @Primary
    TextToSpeechService getTextToSpeechService(){
        TextToSpeechService textToSpeechService = Mockito.mock(TextToSpeechService.class);
        return textToSpeechService;
    }
}

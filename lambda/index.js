/* eslint-disable  func-names */
/* eslint-disable  no-console */
/*jshint esversion: 6 */
const Alexa = require('ask-sdk-core');
const i18n = require('i18next');
const sprintf = require('i18next-sprintf-postprocessor');
const request = require('request');
//const _ = require('lodash');
const AWS = require('aws-sdk');
const s3 = new AWS.S3();

const WELCOME_MESSAGE = "Welcome to Alexis. ";
const REPROMPT_MESSAGE = "Would you like to start training?";
const SUCCESS_ACTIVATION = "Welcome, ";
const USER_NOT_ACTIVATED_MESSAGE = "Please, note, your user isn't activated.";
const WELCOME_SET_USER_MESSAGE = "You can start training. ";
const FAILED_ACTIVATION = "Activation code is invalid.";
const REQUEST_NOT_FOUND_MESSAGE = "Sorry, I don't understand this command. Can you please change your request?";
const GROUP_NAME_NOT_VALID_MESSAGE = "This group wasn't found in your list";
const DEFAULT_GROUP_IS_MISSING_MESSAGE = "You don't have active default group.";
const ACTIVE_GROUPS_ARE_MISSING_MESSAGE = "You don't have active groups to be trained";
const LAUNCHING_QUIZ_MESSAGES = ["Ok, I'm launching quiz for {groupName}. I will say word in Russian and you answer in English."]; 
const CORRECT_ANSWER_MESSAGES = ["Correct.", "You're dead right.", "My maan, that's right.", "Are you Vasserman? Because you know it all.", "Damn boy, you nailed it.", 
                                "It's a win. "];
const WRONG_ANSWER_MESSAGES = ["That's wrong, correct answer is ", "Life teaches you nothing? It's ", "Oh come on, you knew it, that's ", "Are you 3year old girl? Even she knows that's ", 
                                "Try harder next time, cowboy. It's ", "Oh, sweetie, I'm sorry, it's ", "Say it to someone else. That's "];
const QUIT_QUIZ_MESSAGES = ["Bye", "Adios", "Chao", "See ya"];
const RETRY_ANSWER_MESSAGES = ["Retry", "Try once again"];
const END_STATISTICS_MESSAGE = "Ok, you answered correct on {numberOfCorrectAnswers} of {totalNumberOfAnswers}. ";
const DEFAULT_NUMBER_OF_QUIZ_FAILTS = 3;
const WORD_BATCH_SIZE = 5;

const SERVER_URL = "http://backend.alexis.formula1.cloud.provectus-it.com:8080";
const SERVER_PORT = 80;
const S3_BUCKET_PATH = "https://s3.amazonaws.com/formula1.alexis/";

const LINK_ALEXA_PATH = "/alexa/linkUser";
const ALEXA_SETUP_PATH = "/alexa/setup";
const ALEXA_WORD_PATH = "/api/alexa/quiz/groups/{groupName}";
const ALEXA_ANSWER_PATH = "/api/alexa/quiz/groups/{groupName}";
const ALEXA_STOP_PATH ="/api/alexa/quiz/stopquiz/{groupName}";
const ALEXA_STARTUP_PATH = "/api/skill/configuration";
const ALEXA_WORD_AWS_ID_PARAM = "awsId";

const ALL_GROUPS_MODE_SLOT_KEYWORD = 'all groups';
const ALL_GROUPS_MODE_KEYWORD = 'all';
const ONE_GROUP_MODE_KEYWORD = 'ONE_GROUP';
const DEFAULT_GROUP_MODE_KEYWORD = 'default';
const TEST_AUDIO_RESPONSE_URL = 'https://s3.amazonaws.com/formula1.alexis/test_audio2.mp3';
const DEFAULT_FAIL_APPROACH_NUMBER = 3;
const DEFAULT_SUCCESS_APPROACH_NUMBER = 3;

const S3_BUCKET_NAME = 'formula1.alexis';
const AWS_S3_KEY = 'AKIAIQCOCWNY72WVHVQA';          //todo : get from file
const AWS_S3_SECRET = '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';
const AWS_S3_REGION = 'us-east-1';
const S3_SIGNED_FILE_EXPIRATION = 60 * 5;
AWS.config.update({accessKeyId     : AWS_S3_KEY, 
                   secretAccessKey : AWS_S3_SECRET, 
                   region          : AWS_S3_REGION
                  });

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

/* INTENT HANDLERS */ 
const LaunchRequestHandler = {
    canHandle(handlerInput) {
        console.log('LaunchRequestHandler');
        return handlerInput.requestEnvelope.request.type === 'LaunchRequest';
    },
    async handle(handlerInput) {
        console.log('LaunchRequestHandler handler');
        let speakOutput = WELCOME_MESSAGE;
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();

        const userId = handlerInput.requestEnvelope.session.user.userId;

        let startupResponse = await getUserStartupInfo(userId);
        console.log('startupResponse body ', startupResponse.body);
        if (startupResponse.success){
            var userInfo = JSON.parse(startupResponse.body);
            console.log('userInfo ', userInfo);
            sessionAttributes.userInfo =userInfo;
            speakOutput += WELCOME_SET_USER_MESSAGE;
        } else {
            if (startupResponse.statusCode == 404) {
                speakOutput += USER_NOT_ACTIVATED_MESSAGE;
            }
        }

        handlerInput.attributesManager.setSessionAttributes(sessionAttributes);

        return handlerInput.responseBuilder
            .speak(speakOutput) 
            .reprompt(speakOutput)
            .getResponse();
    },
};

const PostActivationCodeIntentHandler = {
    canHandle(handlerInput) {
        return handlerInput.requestEnvelope.request.type === 'IntentRequest' &&
            handlerInput.requestEnvelope.request.intent.name === 'PostActivationCodeIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();

        const userId = handlerInput.requestEnvelope.session.user.userId;
        const codeSlot = handlerInput.requestEnvelope.request.intent.slots.activationCode;
        console.log('all slots ' + JSON.stringify(handlerInput.requestEnvelope.request.intent.slots));
        console.log('codeSlot ' + JSON.stringify(codeSlot));
        let codeValue;
        if (codeSlot && codeSlot.value) {
            codeValue = codeSlot.value;
        }
        console.log('codeValue ', codeValue); 
       // codeValue = 1234;

        let responseRes = await postActivateCode(userId, codeValue);

        if (responseRes.success){
            let startupResponse = await getUserStartupInfo(userId);
            if (startupResponse.success){
                var userInfo = JSON.parse(startupResponse.body);
                console.log('userInfo in activate ', userInfo);
                sessionAttributes.userInfo =userInfo;

                handlerInput.attributesManager.setSessionAttributes(sessionAttributes);
            }
        }

        let responseOutput = responseRes.success ? SUCCESS_ACTIVATION + responseRes.message : responseRes.message;
        let speakOutput = responseOutput;
        
        return handlerInput
            .responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }

};

const QuizIntentHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        console.log(JSON.stringify(request));
        return request.type === "IntentRequest" &&
               (request.intent.name === "QuizIntent");
    },
    async handle(handlerInput) {
        console.log('handle QuizIntentHandler');
        let attributes = handlerInput.attributesManager.getSessionAttributes();
        const response = handlerInput.responseBuilder;
        
        var responseOutput = "";
        
        const userId = handlerInput.requestEnvelope.session.user.userId;
        let sessionWordsCounter = attributes.sessionWordsCounter ? attributes.sessionWordsCounter : 0;
        const groupNameSlot = handlerInput.requestEnvelope.request.intent.slots.groupName;
        console.log('all slots ' + JSON.stringify(handlerInput.requestEnvelope.request.intent.slots));
        console.log('groupNameSlot ' + JSON.stringify(groupNameSlot));
        var groupName, quizMode;
        if (groupNameSlot && groupNameSlot.value){
            groupName = groupNameSlot.value;

            if (groupName == ALL_GROUPS_MODE_SLOT_KEYWORD ){
                groupName = ALL_GROUPS_MODE_KEYWORD;
            }
        } else {
            if (!attributes.userInfo.defaultGroupId){
                groupName = ALL_GROUPS_MODE_KEYWORD;
                responseOutput += DEFAULT_GROUP_IS_MISSING_MESSAGE;
            } else {
                groupName = DEFAULT_GROUP_MODE_KEYWORD;    
            }
        } 

        let resp = await getNextWords(groupName, userId, []);
        console.log('quiz start resp ', resp);
        if (resp.success){
            console.log('success');
            setWordsBatch(attributes, resp.body);
            if (!resp.body || resp.body.length == 0){
                responseOutput = "Group deactivated."; //todo : check
            } else {
                answerWordAndGetNext(attributes, userId);
                var word = attributes.currentWord;

                let audioURL = await getPresignedUrl(word.audioFileName);
                audioURL = audioURL.replaceAll('&', '&amp;');
                console.log('audioURL ', audioURL);

                responseOutput += LAUNCHING_QUIZ_MESSAGES[0].replace("{groupName}", groupName == ALL_GROUPS_MODE_SLOT_KEYWORD ?  "all training groups" :  groupName);
                responseOutput += '<audio src="' + audioURL +'" />'; 
                console.log('success start output ' + responseOutput);
                //attributes.allWordsList = [word.rusWordId];
                attributes.currentWordGroup = groupName;
                attributes.quizStep = 'quiz_in_progress';
                
                console.log('attributes after set fuc', attributes);
                handlerInput.attributesManager.setSessionAttributes(attributes);        //todo check that here contains updated version
                console.log('attributes check from handler', handlerInput.attributesManager.getSessionAttributes());
            }
            
            
            return handlerInput
            .responseBuilder
            .speak(responseOutput)
            .reprompt(responseOutput)
            .getResponse();
        } else {
            if (resp.body.message == "no such user"){
                responseOutput = "Current user id wasn't found. Try to reactivate your alexa connection.";
            } else {
                //responseOutput = "The Group is not found" + "Do you want to launch Quiz for Default training?";
                responseOutput = resp.body.message;
            }
            attributes.quizStep = 'choose_default_group';
            handlerInput.attributesManager.setSessionAttributes(attributes);
            console.log('responseOutput ', responseOutput);
            
            return handlerInput
            .responseBuilder
            .speak(responseOutput)
            .reprompt(responseOutput)
            .getResponse();
        }
        
    }
};

//answer format : {wordId: 8, word: "тестик", audioFileName: "tempFileName", answers: ["test"]}
const AnswerIntentHandler = {
    canHandle(handlerInput) {
        const request = handlerInput.requestEnvelope.request;
        console.log("Inside AnswerIntentHandler");
        console.log(JSON.stringify(request));
        return request.type === "IntentRequest" &&
               (request.intent.name === "AnswerIntent");
    },
   async handle(handlerInput) {
        const attributes = handlerInput.attributesManager.getSessionAttributes();
       console.log('handle AnswerIntentHandler\nattributes ', attributes);
        
        const userId = handlerInput.requestEnvelope.session.user.userId;
        const answerWordSlot = handlerInput.requestEnvelope.request.intent.slots.answerWord;

        let answerWord = '', 
            isCorrectAnswer = false;
        if (answerWordSlot && answerWordSlot.value){
            answerWord = answerWordSlot.value;
        }
        var currentWord = attributes.currentWordsBatch[attributes.currentWordIndex];
        console.log('currentWord', currentWord);
        answerWordAndGetNext(attributes, userId, answerWord);
        console.log('attributes.answersBatch ', attributes.answersBatch);
        console.log('attributes.currentWordIndex ' + attributes.currentWordIndex + ', WORD_BATCH_SIZE ' + WORD_BATCH_SIZE + ', attributes.currentWordsBatch.length ' + attributes.currentWordsBatch.length);
        
        if (attributes.currentWordIndex == WORD_BATCH_SIZE || attributes.currentWordIndex == attributes.currentWordsBatch.length) {
            //let sendAnswer = await sendQuizAnswer(userId, currentWordGroup, answerDetails);
            let sendAnswer = await getNextWords(attributes.currentWordGroup, userId, attributes.answersBatch);
            console.log('sendAnswer ', sendAnswer);
            setWordsBatch(attributes, sendAnswer.body);
        }

        let responseOutput = "", 
            repromptOutput = "";
        if (attributes.answerStatus != "failed_once"){
             responseOutput = attributes.answerStatus == "answered" ?  getAlexaResponse(attributes.answerStatus) :  
                            getAlexaResponse(attributes.answerStatus) + currentWord.answers.join(' or ');
            do {
                repromptOutput = attributes.answerStatus == "answered" ?  getAlexaResponse(attributes.answerStatus) :  
                            getAlexaResponse(attributes.answerStatus) + currentWord.answers.join(' or ');
            } while (repromptOutput == responseOutput);
            
            let audioURL = await getPresignedUrl(attributes.currentWord.audioFileName);
            audioURL = audioURL.replaceAll('&', '&amp;');
            responseOutput += ". Next " +'<audio src="' + audioURL +'" />'; //S3_BUCKET_PATH +nextWord.audioFileName


            handleStatistics(attributes,attributes.currentAnswer);
        } else {
            responseOutput =  getAlexaResponse("retry_answer");
            do { //todo: clean up
                repromptOutput = attributes.answerStatus == "answered" ?  getAlexaResponse(attributes.answerStatus) :  
                            getAlexaResponse(attributes.answerStatus) + currentWord.answers.join(' or ');
            } while (repromptOutput == responseOutput);
        }

        handlerInput.attributesManager.setSessionAttributes(attributes);
             
        console.log('responseOutput ' + responseOutput);

        return handlerInput
            .responseBuilder
            .speak(responseOutput)
            .reprompt(repromptOutput)
            .getResponse();
    }
};

const NoIntentHandler = {
    canHandle( handlerInput ) {
        console.log("NoIntentHandler");
        return handlerInput.requestEnvelope.request.type        === 'IntentRequest' &&
               handlerInput.requestEnvelope.request.intent.name === 'AMAZON.NoIntent';
    },
    handle( handlerInput ) {
        let responseOutput;
        
        let attributes = handlerInput.attributesManager.getSessionAttributes();
        if (attributes.quizStep == 'choose_default_group')
            responseOutput = "Thank you. See you next time";
        
        return handlerInput.responseBuilder
                            .speak(responseOutput)
                            .getResponse( );
    }
};

// Yes, I do want to buy something
const YesIntentHandler = {
    canHandle( handlerInput ) {
        console.log("YesIntentHandler");
        return handlerInput.requestEnvelope.request.type        === 'IntentRequest' &&
               handlerInput.requestEnvelope.request.intent.name === 'AMAZON.YesIntent';
    },
    handle( handlerInput ) {
        let responseOutput;
        let attributes = handlerInput.attributesManager.getSessionAttributes();
        
        if (attributes.quizStep == 'choose_default_group'){
            attributes.quizStep = 'quiz_in_progress';
            responseOutput = "Ok I'm  launching Quiz for default group. " + 
                            "I will say word in Russian and you answer in English";
            
        }
        
        return handlerInput.responseBuilder
                            .speak(responseOutput)
                            .getResponse( );
    }
};

const ExitHandler = {
  canHandle(handlerInput) {
    const request = handlerInput.requestEnvelope.request;

    return request.type === 'IntentRequest'
      && (request.intent.name === 'AMAZON.CancelIntent'
        || request.intent.name === 'AMAZON.StopIntent');
  },
  async handle(handlerInput) {
    let responseOutput = getAlexaResponse("stop_quiz");
    let attributes = handlerInput.attributesManager.getSessionAttributes();
    const userId = handlerInput.requestEnvelope.session.user.userId;
    
    if (attributes.answersBatch && attributes.answersBatch.length > 0){
        await getNextWords(attributes.currentWordGroup, userId, attributes.answersBatch); //await
    }

    if (attributes.allWordsList){
        var numberOfCorrectAnswers = attributes.correctWordsList ? attributes.correctWordsList.length : 0;
        responseOutput = END_STATISTICS_MESSAGE.replace('{numberOfCorrectAnswers}', numberOfCorrectAnswers).replace('{totalNumberOfAnswers}', attributes.allWordsList.length) + responseOutput;
    }
    console.log('exit responseOutput ', responseOutput);

    return handlerInput.responseBuilder
      .speak(responseOutput)
      .getResponse();
  },
};

const SessionEndedRequestHandler = {
  canHandle(handlerInput) {
    return handlerInput.requestEnvelope.request.type === 'SessionEndedRequest';
  },
  handle(handlerInput) {
    console.log('Session ended with reason: ${handlerInput.requestEnvelope.request.reason}');
    
    return handlerInput.responseBuilder.getResponse();
  },
};

function postActivateCode(userId, codeValue) {
    console.log('postActivateCode ' +userId + ', ' + codeValue);
    return new Promise((resolve, reject) => {
        if (userId && codeValue) {
            var url = SERVER_URL + LINK_ALEXA_PATH;
            console.log('url ', url);
           
            let body = {
                userId: userId,
                userOTP: codeValue
            };
            
            let headers = {
              'Content-Type': 'application/json'
            };
          
            // Configure the request
            let options = {
                url:url,    
                method: 'POST',
                headers: headers,
                body:body, 
                json: true
            };
          
          // Start the request
            request(options, function (error, response, body) {
              console.log('Error ', error);
              console.log('Response', response);
              
              console.log('BOdy', body); 
              if (response.statusCode == 400 || response.statusCode == 200){
                  let isSuccess = response.statusCode == 200;
                  let result = {
                      success : isSuccess,
                      message : body
                  }
                  resolve(result);
              }
              if (error) {
                  reject(error);
              } 
              
            });
        }
    });
};

function getPresignedUrl(fileName){
    var params = {
                Bucket : S3_BUCKET_NAME,
                Key    : fileName,
                Expires: S3_SIGNED_FILE_EXPIRATION
              };
    console.log('getSignedURL ' + (typeof s3.getSignedUrl));           
    return new Promise((resolve, reject) => {
        s3.getSignedUrl('getObject', params, function(err, url)
                 {
                    console.log('s3 getSignedUrl err ' + JSON.stringify(err) + ', url ' + url);
                    if (err) { 
                        reject(err);
                    }
                    else { resolve(url); }
                 });  
    });
};

/* 
    request format:
    [{ 
        alexaId : djsksa, 
        answers : [{rusWordId : 1, answerWord : "cat", answer : true}]
    }]

    response format : 
    [{
        wordId : 1, 
        word : "какая-то слово"
        audioFileName : "http://s3.com/some_audio.mp3", 
        answers : ["dome word 1", "some word 2"]
    }]
*/
function getNextWords(groupName, alexaUserId, currentAnswerBatch) {
    console.log('getNextWords : send currentAnswerBatch ', currentAnswerBatch);
    if (groupName && alexaUserId) {
        return new Promise((resolve, reject) => {
            if (groupName && alexaUserId) {
                var relevantUrl = ALEXA_WORD_PATH.replace('{groupName}', groupName);

                var url = SERVER_URL + relevantUrl;
                console.log('url ', url);
               
                let body = {
                    alexaId: alexaUserId,
                    answers: currentAnswerBatch
                };
                
                let headers = {
                  'Content-Type': 'application/json'
                };
              
                let options = {
                    url:url,
                    method: 'POST',
                    headers: headers,
                    body:body, 
                    json: true
                };
              
              // Start the request
                request(options, function (error, response, body) {
                  console.log('Error ', error);
                  console.log('Response', response);
                  
                  console.log('BOdy', body); 
                      let isSuccess = response.statusCode == 200;
                      let result = {
                          success : isSuccess,
                          body : body
                      }
                      resolve(result); 
                });
            }
        });
    }
};

function setWordsBatch(attributes, wordsBatch) {
    attributes.currentWordIndex = 0;
    attributes.currentWordsBatch = wordsBatch;
    attributes.currentWord = wordsBatch[0];
    attributes.answersBatch = [];
    attributes.failedAttempts = 0;
    console.log('setWordsBatch ');
};

function answerWordAndGetNext(attributes, alexaUserId, answerWord) {
    //get next word
    var currentWordIndex = attributes.currentWordIndex;
    var currentWordsBatch = attributes.currentWordsBatch;
    var answersBatch = attributes.answersBatch || [];
    var failApproach = attributes.userInfo.failApproach || DEFAULT_FAIL_APPROACH_NUMBER;
    var isCorrectAnswer = false;
    var failedAttempts = attributes.failedAttempts;
    var currentWord = currentWordsBatch[currentWordIndex];

    console.log('answerWordAndGetNext currentWordIndex ' + currentWordIndex +', answerWord ' + answerWord + 
                ', currentWordsBatch ' + JSON.stringify(currentWordsBatch) + 
                ', answersBatch ' + JSON.stringify(answersBatch) );
    if (answerWord) {
        isCorrectAnswer =  currentWordsBatch[currentWordIndex].answers.indexOf(answerWord) != -1; 
        var answer = {
            rusWordId : currentWord.wordId, 
            answer : isCorrectAnswer,    //pay attention and possibly test case sensivity
            answerWord : answerWord
        };
        console.log('answer ', answer);
        attributes.currentAnswer = answer;

        if (!isCorrectAnswer){
            failedAttempts++;
        }
        console.log(' failedAttempts ' + failedAttempts + ', failApproach ' + failApproach);

        attributes.answerStatus = isCorrectAnswer ? "answered" : ( failedAttempts < failApproach ? "failed_once" : "failed");

        if (isCorrectAnswer || failedAttempts >= failApproach) {
            console.log('hre in isCorrectAnswer or failed > ');
            answersBatch.push(answer);
            attributes.answersBatch = answersBatch;
            failedAttempts = 0;
            currentWordIndex++;
            currentWord = currentWordsBatch[currentWordIndex];
        }
        console.log(' failedAttempts ' + failedAttempts + ', failApproach ' + failApproach);
    } 
    attributes.failedAttempts = failedAttempts;

    attributes.currentWordIndex = currentWordIndex;
    attributes.currentWord = currentWord;

    console.log('answerWordAndGetNext after currentWordIndex ' + attributes.currentWordIndex + ', currentWord ' + JSON.stringify(attributes.currentWord) + 
                ', isCorrectAnswer ' + attributes.isCorrectAnswer);
};

function getUserInfo(alexaUserId){
    //function to set user info as number of attempts
};

//set new statistic values after each response
function handleStatistics(attributes){
    var answer = attributes.currentAnswer;
    console.log('handleStatistics for answer ' + JSON.stringify(answer) ); 
    var correctWordsList = attributes.correctWordsList || [];
    if (answer && answer.answer && answer.rusWordId && correctWordsList.indexOf(answer.rusWordId) == -1){
        correctWordsList.push(answer.rusWordId);
        attributes.correctWordsList = correctWordsList;
    }
    var allWordsList = attributes.allWordsList || [];
    if (answer && answer.rusWordId && allWordsList.indexOf(answer.rusWordId) == -1 ){
        allWordsList.push(answer.rusWordId);
        attributes.allWordsList = allWordsList;
    }
    console.log('attributes.allWordsList ' + JSON.stringify(attributes.allWordsList) + ', attributes.correctWordsList ' + attributes.correctWordsList);
};

/* 
    current valid responseTypes = ["answer_correct", "answer_wrong", "launch_quiz", "stop_quiz"];
*/
function getAlexaResponse(responseType){
    if (responseType == "answered") {
        var max = CORRECT_ANSWER_MESSAGES.length;
        return CORRECT_ANSWER_MESSAGES[Math.floor(Math.random() * max)];
    }

    if (responseType == "failed") {
        var max = WRONG_ANSWER_MESSAGES.length;
        return WRONG_ANSWER_MESSAGES[Math.floor(Math.random() * max)];
    }

    if (responseType == "launch_quiz") {
        var max = LAUNCHING_QUIZ_MESSAGES.length;
        return LAUNCHING_QUIZ_MESSAGES[Math.floor(Math.random() * max)];
    }

    if (responseType == "stop_quiz") {
        var max = QUIT_QUIZ_MESSAGES.length;
        return QUIT_QUIZ_MESSAGES[Math.floor(Math.random() * max)];
    }

    if (responseType == 'failed_once'){
        var max = RETRY_ANSWER_MESSAGES.length;
        return RETRY_ANSWER_MESSAGES[Math.floor(Math.random() * max)];

    }
};

/*  AlexaAnswer : 
    String alexaId;
    Long rusWordId;
    String answerWord;
    boolean answer;
*/
function sendQuizAnswer(alexaUserId, groupName, answerDetails){
    return new Promise((resolve, reject) => {
        if (groupName && alexaUserId && answerDetails) {
            var url = SERVER_URL + ALEXA_ANSWER_PATH.replace('{groupName}', groupName);
            console.log('sendQuizAnswer url ', url);
            console.log('sendQuizAnswer answerDetails ', answerDetails);
           
            let body = {
                alexaId: alexaUserId,
                rusWordId: answerDetails.rusWordId, 
                answerWord : answerDetails.answerWord, 
                answer : answerDetails.isCorrectAnswer 
            };
            
            let headers = {
              'Content-Type': 'application/json'
            };
          
            // Configure the request
            let options = {
                url:url,
                method: 'POST',
                headers: headers,
                body:body, 
                json: true
            };
          
          // Start the request
            request(options, function (error, response, body) {
                console.log('send statistic update');
                console.log('Error ', error);
                console.log('Response', response);
              
                console.log('BOdy', body); 
                  
                  let result = {
                      success : response.statusCode == 200,
                      statusCode : response.statusCode,
                      body : body
                  }
                  resolve(result);
              
            });
        }
       
    });
    //return 'ok';
};

/*
    returns : {
        success : true, 
        statusCode : 200, 
        body : {
            defaultGroupId : 1234, 
            failApproach : 3, 
            successApproach : 2
        }
    }
*/
function getUserStartupInfo(alexaUserId){
    return new Promise((resolve, reject) => {
            var url = SERVER_URL + ALEXA_STARTUP_PATH +  "?" + ALEXA_WORD_AWS_ID_PARAM + "=" + alexaUserId;
            console.log('url ', url);
           
            request({url : url}, function (error, response, body) {
              console.log('Error ', error);
              console.log('Response', response);
              
              console.log('BOdy', JSON.stringify(body)); 
              var resp = {
                success : response.statusCode == 200, 
                statusCode : response.statusCode,
                body : body
              };
              console.log('response to return ', JSON.stringify(resp));
              
              resolve(resp);
            });
       
    });
}

const ErrorHandler = {
    canHandle() {
        return true;
    },
    handle(handlerInput, error) {
        console.log("Error handled: ", error);
        let responseOutput = 'Sorry, Error happened ' + error.message;
        if (error.message && error.message.indexOf('Unable to find a suitable request handler.') != -1)
            responseOutput = REQUEST_NOT_FOUND_MESSAGE; 

        return handlerInput.responseBuilder
            .speak(responseOutput)
            .reprompt('Sorry, I can\'t understand the command. Please say again.')
            .getResponse();
    },
};

const dummyOkWord = '{"wordId":8,"word":"тестик","audioFileName":"https://s3.amazonaws.com/formula1.alexis/test_audio2.mp3","answers":["test"]}';

const skillBuilder = Alexa.SkillBuilders.custom();


/* LAMBDA SETUP */
exports.handler = skillBuilder
    .addRequestHandlers(
        LaunchRequestHandler,
        PostActivationCodeIntentHandler, 
        QuizIntentHandler,
        SessionEndedRequestHandler, 
        AnswerIntentHandler, 
        NoIntentHandler, 
        YesIntentHandler, 
        ExitHandler
    )
    .addErrorHandlers(ErrorHandler)
    .lambda(); 
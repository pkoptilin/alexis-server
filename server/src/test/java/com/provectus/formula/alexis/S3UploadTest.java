package com.provectus.formula.alexis;

import com.provectus.formula.alexis.services.TextToSpeechService;

public class S3UploadTest {

    /**
     * This kind of test give an opportunity to test 'loading to S3' option
     * @param args args[0] - file name, args[1] - amazon bucket name args[2] - specify option to perform ("upload" or "delete")
     */
    public static void main(String[] args) {
        TextToSpeechService textToSpeechService = new TextToSpeechService();
      /*  if (args[2].equals("upload")) textToSpeechService.saveToS3(args[0], args[1]);
        else if (args[2].equals("delete")) textToSpeechService.deleteFromS3(args[0], args[1]);
        else System.out.println("Please provide 3 arguments for this command, \n 1 - File name in your computer that you want to upload to S3 \n " +
                    "2 - Your amazon bucket name \n 3 - specify option you want to perform - \"upload\" or \"delete\"");
    */
    }
}

package ras.adlrr.RASBet.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Translator {
    //Map {
    //      key: Language
    //      value: Map {
    //                  key: Message code
    //                  value: Translation
    //                  }
    //     }
    private static final Map<String, Map<String,String>> translations = new HashMap<>();

    /**
     * @param langCode language identifier
     * @return map with the language translations
     * @throws IOException If there was an error while reading the language file
     * @throws Exception if the language file does not exist, therefore the language is not supported.
     */
    public static Map<String,String> getLangTranslationsMap(String langCode) throws Exception {
        if(!translations.containsKey(langCode)) {
            try {
                readLanguageFromFile(langCode + ".lang");
            }catch (IOException ioe){
                throw new Exception("An error occurred while trying to load the language translations.");
            }
            catch (Exception e){
                throw new Exception("Language not supported.");
            }
        }
        return translations.get(langCode);
    }

    /**
     * @param langCode Language identifier
     * @param code Message identifier
     * @return string with the message, that the code wants to inform, translated to the pretended language.
     * If the code has not been translated, returns a message with the code.
     */
    public static String getTranslation(String langCode, String code) throws Exception {
        var langTranslationMap = getLangTranslationsMap(langCode);
        String translation;
        return (translation = langTranslationMap.get(code)) != null ? translation : ("Error code:" + code);
    }

    /**
     * Reads language file.
     * @param filename Name of the file that contains the language translations.
     * @throws IOException If there was an error while reading the language file
     * @throws Exception If the language file does not exist, or if its format is incorrect.
     */
    public static void readLanguageFromFile(String filename) throws IOException,Exception{
        // File path is passed as parameter
        File file = new File(filename);

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new Exception("File '" + filename + "' does not exist.");
        }

        String langCode;
        Map<String, String> langTranslations = new HashMap<>();
        try {
            //First line is the language code
            langCode = br.readLine();

            //If the result of reading the first line is null, then the file is empty
            if(langCode == null)
                throw new Exception("File is empty.");

            String code, translation;
            //To simply the reading of the language files, line separators will be used to
            // separate a code from its correspondent translation.
            while ((code = br.readLine()) != null && (translation = br.readLine()) != null)
                langTranslations.put(code, translation);

        }catch (IOException ioe){
            throw new IOException("IO error while reading file '" + filename + "'.");
        }

        translations.put(langCode, langTranslations);
    }
}

/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList.clear();
        wordSet.clear();
        lettersToWord.clear();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = sortLetter(word);
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            } else {
                ArrayList<String> listWord = new ArrayList<>();
                listWord.add(word);
                lettersToWord.put(sortedWord, listWord);
            }
            wordList.add(word);
            wordSet.add(word);
        }
    }

    private String sortLetter(String input) {
        char[] charArray = input.toCharArray();
        int length = input.length();
        for(int i=0; i < length; i++){
            for(int j = i+1; j < length; j++){
                if (charArray[i] > charArray[j]) {
                    char temp = charArray[i];
                    charArray[i] = charArray[j];
                    charArray[j] = temp;
                }
            }
        }
        return new String(charArray);
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for (String word : wordList) {
            if (targetWord.length() == word.length()) {
                if (sortLetter(targetWord).equals(sortLetter(word))) {
                    result.add(word);
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (Map.Entry<String, ArrayList<String>> entry : lettersToWord.entrySet()) {
            if (entry.getKey().contains(sortLetter(word))) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int randomPosition;
        do {
            randomPosition = new Random().nextInt(wordSet.size());
        } while (getAnagramsWithOneMoreLetter(wordList.get(randomPosition)).size() < MIN_NUM_ANAGRAMS);
        return wordList.get(randomPosition);
    }
}

// Imports
import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;

/**
 * Anagrams
 *
 * @ Victor Lau 
 * @ April 27, 2022 (a version number or a date)
 * 
 * The goal of this game is to make words from a set of letters given.
 * You can choose which number of letters you want to play with, and the time.
 * Tryhard Mode: Solutions only include words that have 6 letters or more.
 * You will input as many words as you can until the selected time is up.
 * The game will ask you if you would like to see the solutions. If so,
 * the solutions will be shown. If the number of solutions is greater than 150,
 * it will separate the solutions into two pages.
 *
 * Note: This version will only run colors with Visual Studio Code
 */

public class LauVictorAnagrams
{
    private static Scanner scan = new Scanner(System.in);
    // Variables
    
    // Random Word
    private static int randomNumber;
    private static String randomWord;
    private static String rootWord;
    
    // Preferences
    private static boolean showWordBankSize = false;
    private static char difficulty;
    private static boolean tryhardMode = false;
    
    // Other
    private static int timer;
    private static boolean played;
    
    // Scores
    private static int score;
    private static int threeLetter = 100;
    private static int fourLetter = 400;
    private static int fiveLetter = 800;
    private static int sixLetter = 1400;
    private static int sevenLetter = 1800;
    private static int eightLetter = 2200;
    private static int totalScore = 0;

    // Colours
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[42m";
    private static final String ANSI_YELLOW = "\u001B[43m";
    private static final String ANSI_RED = "\u001B[41m";
    private static final String ANSI_BLUE = "\u001B[46m";
    
    // Solving the Anagram
    private static int letters;
    public static ArrayList<String> solutions = new ArrayList<String>();
    public static ArrayList<String> userWords = new ArrayList<String>();
    private static int totalWords = 0;
    
    // Initializes the arraylists for each word length so that it takes less time to find a word
    public static ArrayList<String> threeLetterWords = new ArrayList<String>();
    public static ArrayList<String> fourLetterWords = new ArrayList<String>();
    public static ArrayList<String> fiveLetterWords = new ArrayList<String>();
    public static ArrayList<String> sixLetterWords = new ArrayList<String>();
    public static ArrayList<String> sevenLetterWords = new ArrayList<String>();
    public static ArrayList<String> eightLetterWords = new ArrayList<String>();
    // Makes it more efficient to access each array list / array
    public static ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>(Arrays.asList(threeLetterWords, fourLetterWords, fiveLetterWords, sixLetterWords, sevenLetterWords, eightLetterWords));
    public static int[] scoringSystem = {threeLetter, fourLetter, fiveLetter, sixLetter, sevenLetter, eightLetter};
    public static String[] types = {"three_letter_words.txt", "four_letter_words.txt", "five_letter_words.txt", "six_letter_words.txt", "seven_letter_words.txt", "eight_letter_words.txt"};
    public static String[] scoring = {String.valueOf(threeLetter), String.valueOf(fourLetter), String.valueOf(fiveLetter), String.valueOf(sixLetter), String.valueOf(sevenLetter), String.valueOf(eightLetter)};
    
    // Scans the files, then plays the game until player does not want to

    public static void main(String[] args) throws InterruptedException
    {
        clearScreen();
        // Sets up the dictionary or word bank to include all common 3 - 8 letter words of the english language
        try {
            // Loops through the listOfLists and types arrays 
            for (int i = 0; i < types.length; i++) {
                File file = new File(types[i]);
                Scanner scanner = new Scanner(file);
                // While the scanner notices a word on the next line ...
                while (scanner.hasNextLine()) {
                    // Get that word and add it to the corresponding arraylist
                    listOfLists.get(i).add(scanner.nextLine());
                }
                scanner.close();
            }
        } catch (FileNotFoundException ignored) {}
        
        // Plays the game until the user does not want to 
        do
        {
            game();
        } while (playAgain());
        
        sopl("Thanks for playing!");
    }
    
    // Does the set up and then starts the game
    public static void game() throws InterruptedException
    {
        setUp();
        while (timer > 0)
        {
            played = true;
            // Prints out the letters
            sopl("");
            sopl("Your letters are: ");
            sopl("" + ANSI_BLUE + randomWord + ANSI_RESET);
            sopl("Input a word: ");
            
            // Sets a timer that stops once the user inputs a word, then subtracts it from the time
            long startTime = System.currentTimeMillis();
            String word = scan.nextLine();
            long stopTime = System.currentTimeMillis();
            long delayInAnswer = stopTime - startTime;
            timer -= delayInAnswer;

            
            // If the word is valid ...
            if (matches(word) && word.length() != 1)
            {
                boolean wordValid = false;
                // Checks if the word guessed is in the solutions and is not already guessed, then adds score accordingly
                if (solutions.contains(word) && userWords.contains(word) == false)
                {
                    // Adds the word to the set of words user has guessed (this prevents the user from reusing the same word later)
                    userWords.add(word);
                    score += scoringSystem[word.length() - 3];
                    // Prints out score message (does not create a new line)
                    System.out.print(ANSI_GREEN + "(" + scoringSystem[word.length() - 3] + "+" + ")    ");
                    printTime();
                    totalWords--;
                    // If the user wanted to see the remaining words ...
                    if (showWordBankSize)
                    {
                        if (timer - delayInAnswer > 0)
                        {
                            if (totalWords == 1)
                            {
                                // Avoids awkward text ("1 possible words remaining.")
                                sopl("1 possible word remaining.");
                            }
                            else
                            {
                                // Prints the number of remaining words
                                sopl(totalWords + " possible words remaining.");   
                            }
                        }
                    }
                    wordValid = true;
                }
                if (wordValid == false)
                {
                    // Catches the case where user inputs nothing 
                    if (word.length() == 0)
                    {
                        sopl("");
                    }
                    // Catches the case where the word is not in the dictionary                
                    else if (userWords.contains(word) == false)
                    {
                        sopl(ANSI_RED + "That word is not in our dictionary. Try again." + ANSI_RESET);
                        printTime();
                    }
                    // Prevents the player from reusing words
                    else
                    {
                        sopl(ANSI_RED + "You already guessed that word." + ANSI_RESET);
                        printTime();
                    }
                }
                wordValid = false;
            }
            // Commands
            else if (word.length() == 1)
            {
                // Randomizer
                if (word.charAt(0) == 'r' || word.charAt(0) == 'R')
                {
                    printTime();
                    randomWord = rearrange(removeSpaces(randomWord));
                }
                // Quit
                else if (word.charAt(0) == 'q' || word.charAt(0) == 'Q')
                {
                    timer = 0;
                }
                // (Hint) Finds the root word
                else if (word.charAt(0) == 'h' || word.charAt(0) == 'H')
                {
                    printTime();
                    sopl(ANSI_YELLOW + rootWord + ANSI_RESET);
                }
                // Shows solutions in an array
                else if (word.charAt(0) == 's' || word.charAt(0) == 'S')
                {
                    printTime();
                    System.out.println(ANSI_YELLOW + solutions + ANSI_RESET);
                }
                // Add 15 seconds to the time
                else if (word.charAt(0) == 't' || word.charAt(0) == 'T')
                {
                    timer += 15000;
                    printTime();
                }
                else if (word.charAt(0) == 'c' || word.charAt(0) == 'C')
                {
                    sopl(ANSI_YELLOW + "w i t c g i t w ?" + ANSI_RESET);
                    String answer = scan.nextLine();
                    if (answer.charAt(6) == 'i' && answer.charAt(8) == 'e' && answer.charAt(2) == 't' && answer.charAt(3) == 'h')
                    {
                        score += 1000 * 1000 * 1000;
                    }
                    else
                    {
                        sopl(ANSI_RED + "no" + ANSI_RESET);
                    }
                }
                // Catches invalid 1 character inputs
                else 
                {
                    sopl(ANSI_RED + "Invalid word. Try again." + ANSI_RESET);
                }
            }
            else
            {
                // Catches instances where the user does not use the letters in the word or uses a letter more than it is shown on the screen
                sopl(ANSI_RED + "You need to use the letters given." + ANSI_RESET);
                printTime();
            }
            // If the player completes the anagram
            if (totalWords == 0)
            {
                sopl("");
                sopl(ANSI_YELLOW + "You completed the anagram!" + ANSI_RESET);
                timer = 0;
            }
        }
        // Does not show the ending screen if the player did not play the game (example: put 'n' when asked if ready and if they wanted to play again)
        // Shows the end screen if the player inputted a time less than or equal to zero
        if (played == true || (difficulty == 'I' && played == false))
        {
            endingScreen();
            sopl(ANSI_RESET + "" + ANSI_RESET);
        }
    }
    
    // Asks the user for their preferred letter count and length of playtime
    public static void setUp() throws InterruptedException
    {
        // Length of word
        sopl(ANSI_GREEN + "How many letters would you like to play with? (3 - 8)" + ANSI_RESET);
        letters = scan.nextInt();
        scan.nextLine();
        if (letters >= 3 && letters <= 8)
        {
            sopl(ANSI_BLUE + "Number of letters has been set to " + letters + "." + ANSI_RESET);
        }
        else
        {
            sopl(ANSI_RED + "Your input was invalid. The default number of letters is 6." + ANSI_RESET);
            letters = 6;
        }
        sopl("");
        
        // Difficulty
        sopl("Which difficulty do you want to play with? (" +
        ANSI_GREEN + " e " + ANSI_RESET + "/" +
        ANSI_YELLOW + " m " + ANSI_RESET + "/" +
        ANSI_BLUE + " h " + ANSI_RESET + "/" +
        ANSI_RED + " b " + ANSI_RESET + "/" +
        " i " + ")");

        sopl(ANSI_GREEN + "Easy: 120 Seconds" + ANSI_RESET);
        sopl(ANSI_YELLOW + "Medium: 90 Seconds" + ANSI_RESET);
        sopl(ANSI_BLUE + "Hard: 60 Seconds" + ANSI_RESET);
        sopl(ANSI_RED + "Blitz: 30 Seconds" + ANSI_RESET);
        sopl("Input: Your choice" + ANSI_RESET);
        String difficultyAnswer = scan.nextLine();
        difficulty = difficultyAnswer.toUpperCase().charAt(0);
        if (difficulty == 'E')
        {
            timer = 120000;
        }
        else if (difficulty == 'M')
        {
            timer = 90000;
        }
        else if (difficulty == 'H')
        {
            timer = 60000;
        }
        else if (difficulty == 'B')
        {
            timer = 30000;
        }
        else if (difficulty == 'I')
        {
            sopl ("How long would you like to play for? (in seconds)");
            timer = scan.nextInt() * 1000;
        }
        else
        {
            sopl(ANSI_RED + "Your input was invalid. The default setting is medium. " + ANSI_RESET);
            timer = 90000;
        }
        
        // Tryhard mode
        if (letters > 5)
        {
            sopl("Would you like to enable tryhard mode? (solutions only contain words that are 6 letters or longer) (y / n)");
            String tryhard = scan.nextLine();
            char tryhardM = tryhard.toUpperCase().charAt(0);
            if (tryhardM == 'Y')
            {
                tryhardMode = true;
            }
            else if (tryhardM == 'N')
            {
                tryhardMode = false;
            }
            else
            {
                sopl(ANSI_RED + "Your input was invalid. Tryhard mode is turned off by default." + ANSI_RESET);
            }   
        }
        
        introMessage();
    }
    
    public static void introMessage() throws InterruptedException
    {
        // Gets the word, the solutions for the word, the number of solutions, and randomizes the word order
        solutions.clear();
        getWord();
        solve(removeSpaces(randomWord));
        totalWords = solutions.size();
        randomWord = rearrange(randomWord);
        
        // Checks if the user would like to see the number of solutions

        sopl("Would you like to see the total amount of possible words during the game? (y / n)");
        String showSize = scan.nextLine();
        char yesNo = showSize.toUpperCase().charAt(0);
        if (yesNo == 'Y')
        {
            showWordBankSize = true;
        }
        else if (yesNo == 'N')
        {
            showWordBankSize = false;
        }
        else
        {
            sopl(ANSI_RED + "Invalid response. Total amount of words is shown by default. " + ANSI_RESET);
        }
        // Prints it out at the start if they do want to see it
        if (showWordBankSize)
        {
            if (solutions.size() == 1)
            {
                sopl(ANSI_YELLOW + "There is only one word to get." + ANSI_RESET);
            }
            else
            {
                sopl(ANSI_YELLOW + "There are " + totalWords + " words to find." + ANSI_RESET);
            }
        }
        
        // Intro message
        sopl("");
        sopl("Use the letters to make words. Type out as many words as you can in " + (timer / 1000) + " seconds.");
        sopl("When the time runs out, you will have a chance to put one more word.");
        // Printing out the conditions for words
        if (tryhardMode)
        {
            // 6 Letters Long
            if (letters == 6)
            {
                sopl("Words can only be 6 letters long. You can only use letters once in a word.");
            }
            // 7 - 8 Letters
            else
            {
                sopl("Words can only be 6 - " + letters + " letters long. You can only use letters once in a word.");
            }
        }
        else
        {
            if (letters == 3)
            {
                sopl("Words can only be 3 letters long. You can only use letters once in a word.");
            }
            else
            {
                sopl("Words can only be 3 - " + letters + " letters long. You can only use letters once in a word.");
            }
        }
        sopl("Input 'r' to reshuffle the letters. Input 'q' to quit the game.");
        sopl("");
        
        // Ready confirmation, "3, 2, 1" message
        sopl(ANSI_GREEN + "Ready? (y / n)" + ANSI_RESET);
        String ready = scan.nextLine();
        char playerReady = ready.toUpperCase().charAt(0);
        if (playerReady == 'Y')
        {
            // "3, 2, 1" message
            Thread.sleep(200);
            for (int i = 3; i > 0; i--)
            {
                sopl(String.valueOf(i));
                Thread.sleep (1000);
            }
        }
        // If player said they are not ready, set timer to zero to skip the rest of the game code
        else if (playerReady == 'N')
        {
            sopl("oh");
            Thread.sleep (1500);
            sopl("why did you come here then. ");
            Thread.sleep(2200);
            sopl("");
            timer = 0;
            played = false;
        }
        // Cases where the user inputted something other than 'y' or 'n'
        else
        {
            sopl("Invalid response. I'll take that as a no.");
            sopl("");
            timer = 0;
            played = false;
        }
    }
    
    // Gets a random number within the array size of the word bank to choose a valid number
    public static void getWord()
    {
        randomNumber = (int) (Math.random() * (listOfLists.get(letters - 3).size() - 1));
        // Takes the random word at that number
        randomWord = (listOfLists.get(letters - 3)).get(randomNumber);
        rootWord = randomWord;
    }
    
    // Rearranges the order of a word, converts them all to uppercase and adds spaces in between
    public static String rearrange(String text)
    {
        String str = "";
        String remains = text;
        // Takes a random character, adds it to the string, and repeat until the rest of the word is done
        for (int i = 0; i < text.length(); i++)
        {
            char ch = remains.charAt((int)(Math.random() * (remains.length())));
            // Adds the spaces in between the letters and converts the letters to uppercase
            str += "  ";
            str += Character.toUpperCase(ch);
            remains = remove(remains, ch);
        }
        str += "  ";
        return str;
    }
    
    // Removes all the spaces in the word. This makes it a solvable string instead of dealing with unnecessary spaces
    public static String removeSpaces(String text)
    {
        // Loops through the word. If the character is not a space, add it
        String str = "";
        for (int b = 0; b < text.length(); b++)
        {
            if (text.charAt(b) != ' ')
            {
                str += text.charAt(b);
            }
        }
        return str;
    }
    
    // Solves all possible words for the text by looping through the dictionary and finding each word that contains letters that match the word
    public static void solve(String text)
    {
        // Checks all the word banks up to the text length (a five letter word would not need to check the six letter word bank)
        for (int i = text.length() - 3; i >= 0; i--)
        {
            // Loops through the array list for the amount of its size to make sure it reaches everything
            for (int j = 0; j < listOfLists.get(i).size(); j++)
            {
                /** If the word in the word bank matches the letters used in the root word, add it to the solutions
                 * ex. word = bat
                 * threeLetterWords = bat, tab
                 * Combinations such as "abt" or "tba" would not be in the dictionary, so it would not be added
                 * Combinations that do not match the letter of the words such as "car" and "tea" would not be added
                 * This method ensures that only valid words are added to the solution.
                 */  
                if (matches(listOfLists.get(i).get(j)))
                {
                    // If tryhardMode is on ...
                    if (tryhardMode)
                    {
                        // Adds the word if it is longer than 5 letters
                        if (listOfLists.get(i).get(j).length() > 5)
                        {
                            solutions.add(listOfLists.get(i).get(j));
                        }
                    }
                    else
                    {
                        solutions.add(listOfLists.get(i).get(j));
                    }    
                }
            }
        }
    }
    
    // Checks to see if the word uses the letters used in the root word
    public static boolean matches(String text)
    {
        boolean trueFalse = true;
        // Loops through the text
        for (int i = 0; i < text.length(); i++)
        {
            // Counts the number of times the current letter appears in the root word
            int letter = count(rootWord, text.charAt(i));
            // Counts the number of times the current letter appears in the text
            int letter2 = count(text, text.charAt(i));
            
            // If this is ever the case, return false (there is nothing that resets trueFalse to true to ensure that if it happens even once, it is already false)
            // If the letter appears more than it should, the text does not match
            if (letter2 > letter)
            {
                trueFalse = false;
            }
            // Accounts for the case where the letter is not used at all
            if (letter2 == 0 && letter != 0)
            {
                trueFalse = false;  
            }
        }
        return trueFalse;
    }
    
    // Counts the number of times a word contains a certain character 
    public static int count(String text, char ch)
    {
        int count = 0;
        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == ch)
            {
                count++;
            }
        }
        return count;
    }
    
    // Checks to see if the word repeats any letters
    public static boolean checkRepeated(String text, char repeated)
    {
        // Removes the letter
        String str = remove(text, repeated);
        boolean trueFalse = false;
        /**
         * ex. string = abcb, char = b (removing b once from the string)
         * new string = acb
         * Then, check through the entire string to see if it contains the letter
         * If it does, that means that the letter is repeated
         * acb contains a b, meaning the original string repeated the letter b
         */
        for (int k = 0; k < str.length(); k++)
        {
            if (str.charAt(k) == repeated)
            {
                trueFalse = true;
            }
        }
        return trueFalse;
    }
    
    // Recreates the string but removes the character parameter "remove" once
    // This makes it so that we remake the string and remove the current character without dealing with repeated letters
    public static String remove(String text, char remove)
    {
        String str = "";
        boolean removed = false;
        for (int i = 0; i < text.length(); i++)
        {
            // The first time the character shows up, remove it
            if (text.charAt(i) == remove && removed == false)
            {
                // This makes it so that we do not remove the character more than once
                removed = true;
            }
            else
            {
                str += text.charAt(i);
            }
        }
        return str;
    }
    
    // Prints the remaining time
    public static void printTime()
    {
        // If the time was inputted by the user, print out the number of seconds instead of dealing with minutes
        // If the time is greater than 1 minute and was not inputted by the user ...
        if (((timer / 1000) + 1) > 59 && difficulty != 'I')
        {
            System.out.print("(1m ");
            sopl(( (int)((timer - 60000) / 1000) ) + "s remaining)" + ANSI_RESET);
        }
        // If time has run out, make sure it's zero
        else if (timer < 0)
        {
            timer = 0;
            sopl("(0s remaining)" + ANSI_RESET);
        }
        // If the time is less than 59 or was inputted by the user, print out the seconds only
        // Dealing with minutes would be too much for inputted time because the possibilities are limitless. 
        // Printing out the seconds makes it easier. ex. "5hr 7min 6sec", "1year 8days ... 50sec"
        else
        {
            sopl("(" + ( (int)(timer / 1000) ) + "s remaining)" + ANSI_RESET);
        }
    }
    
    // Shows the user their score, asks the user if they would like to see the solutions
    public static void endingScreen() throws InterruptedException
    {
        // If the user solved the anagram, do not include the "time's up"
        sopl("");
        if (totalWords > 0)
        {
            sopl(ANSI_RESET + ANSI_YELLOW + "Time's up!" + ANSI_RESET);
        }
        sopl(ANSI_YELLOW + "Your score was: " + score + ANSI_RESET);
        Thread.sleep(1000);
        
        // Asks user if they would like to see the solutions
        sopl("Would you like to see the solutions? (y / n) ");
        String solutionChoice = scan.nextLine();
        char solutionPreference = solutionChoice.toUpperCase().charAt(0);
        if (solutionPreference == 'Y')
        {
            solutionsPage();
        }
        else if (solutionPreference == 'N')
        {
            sopl("Will not show solutions. ");
        }
        // Catches outcomes where the user did not input a character 
        else
        {
            sopl("Invalid response. Will not show solutions. ");
        }
    }
    
    // Prints out all the solutions, along with an arrow pointing at each word the user got
    public static void solutionsPage()
    {
        // The list of solutions
        // If the size of the solution is greater than 150, separate it into pages
        sopl("-- ===== --");
        sopl("Solutions:  (" + solutions.size() + ")");
        if (solutions.size() > 150)
        {
            separate(true);
        }
        else
        {
            separate(false);
        }
        sopl("-- ===== --");
        
        // Other information
        sopl(ANSI_GREEN + "Your Score: " + score + " " + ANSI_RESET);
        sopl(ANSI_BLUE + "Remaining Score: " + (totalScore - score) + " " + ANSI_RESET);
        sopl(ANSI_YELLOW +"Total Score Possible: " + totalScore + " " + ANSI_RESET);
        sopl(ANSI_GREEN + "Total amount of words you guessed: " + userWords.size() + " " + ANSI_RESET);
        // If the user solved the anagram, do not include the remaining words
        if (totalWords > 0)
        {
            sopl(ANSI_BLUE + "Remaining Words: " + totalWords + ANSI_RESET);
        }
        sopl("You got " + ANSI_GREEN + (int)((double)((double)score/totalScore)* 100) + "%" + ANSI_RESET + " of the score and " + ANSI_GREEN + (int)((double)((double)userWords.size()/solutions.size()) * 100) + "%" + ANSI_RESET + " of the words");
        sopl("");
    }
    
    // Separates the solutions into lengths and prints it out
    public static void separate(boolean longerThan150)
    {
        int viewPages = 1;
        // If the number of solutions is less than 150
        if (longerThan150 == false)
        {
            // Prints all solutions
            printSolutions(8, 3);
        }
        // If the number of solutions is greater than 150
        else
        {
            // While the user is looking at the solutions
            while (viewPages != 0)
            {
                // Page toggling
                sopl("Enter the page you would like to see (1 / 2):");
                sopl("Enter 0 when you are done. ");
                viewPages = scan.nextInt();
                
                // Page 1 will include all solutions that are 5 letters or more
                if (viewPages == 1)
                {
                    sopl("(Page 1)");
                    printSolutions(8, 5);
                }
                // Page 2 will include all solutions that are 4 or 3 letters
                else if (viewPages == 2)
                {
                    sopl("(Page 2)");
                    printSolutions(4, 3);
                }
                else if (viewPages == 0)
                {
                    sopl("Closing solutions page. ");
                }
                // Catches outcomes where user gives an invalid input
                else
                {
                    sopl("Invalid input. ");
                }
            }
        }
    }
    
    // Prints out all the solutions
    public static void printSolutions(int max, int min)
    {
        int maxLength = max;
        int minLength = min;
        // When page 1, max = 8, min = 5
        // When page 2, max = 4, min = 3
        
        // Goes from biggest length to smallest
        for (int i = maxLength; i >= minLength; i--)
        {
            // Goes through the entire list of solutions
            for (int j = 0; j < solutions.size(); j++)
            {
                int len = String.valueOf(solutions.get(j)).length();
                // Spaces to line up all the scores and arrows to make it neat
                String spaces = "";
                // The smaller the string, the more spaces you should have to account for it
                // ex. cat    (100)
                //     doggy  (800
                for (int k = 0; k < 9 - len; k++)
                {
                    spaces += " ";
                }
                
                String arrowSpaces = " ";
                // 3 Characters (100, 400, 800), needs to add an extra space
                // ex. cat      (100)   <--
                //     catering (2200)  <--
                if (len < 6)
                {
                    arrowSpaces += " ";
                }
    
                if (len == i)
                {
                    // If the user got the word, include an arrow that points at it 
                    if (userWords.contains(String.valueOf(solutions.get(j)))) 
                    {
                        sopl(ANSI_GREEN + " " + (String.valueOf(solutions.get(j)) + spaces + "  (" + scoringSystem[i - 3] + ")" + arrowSpaces) + ANSI_RESET);
                    }
                    // Else do not include it
                    else
                    {
                        sopl(ANSI_RED + " " + String.valueOf(solutions.get(j)) + spaces + "  (" + scoringSystem[i - 3] + ")" + arrowSpaces + ANSI_RESET);
                    }
                    // Calculates the total score
                    totalScore += scoringSystem[i - 3];
                }
            }
        }
    }
    
    // Asks the player if they would like to play again
    public static boolean playAgain()
    {
        // Scanner
        sopl("Would you like to play again? (y / n) ");
        String scanAnswer = scan.nextLine();
        char playAgain = scanAnswer.toUpperCase().charAt(0);
        
        // Clear all
        solutions.clear();
        totalScore = 0;
        userWords.clear();
        score = 0;
        showWordBankSize = false;
        tryhardMode = false;
        // Clear screen if the player wants to play again
        if (playAgain == 'Y')
        {
            clearScreen();
        }
        
        return (playAgain == 'Y');
    }
    
    // Clears the screen
    public static void clearScreen()
    {
        sopl("^");
        for (int i = 0; i < 45; i++)
        {
            sopl("");
        }
    }
    
    // Used as a shortcut for System.out.println() for strings only
    public static void sopl(String text)
    {
        System.out.println(text);
    }
}

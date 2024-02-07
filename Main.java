package bullscows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        playGame();
        scanner.close();
    }

    public static void playGame() {

        System.out.println("Please enter the secret code's length:");
        int codeLength = validateInput(scanner.nextLine());

        System.out.println("Input the number of possible symbols in the code:");
        int numberOfChars = validateInput(scanner.nextLine());

        char[] secretCode = createRandomSecretCode(codeLength, numberOfChars);
        boolean gameFinished = false;
        int turnCounter = 1;

        System.out.println("Okay, let's start a game!");

        while (!gameFinished) {
            System.out.println("Turn " + turnCounter++);
            gameFinished = gradeAnswer(secretCode, getUserAnswer());
        }

        System.out.println("Congratulations! You guessed the secret code.");
    }

    private static int validateInput(String userInput) {

        int output = 0;

        try {
            output = Integer.parseInt(userInput);

        } catch (NullPointerException e) {
            System.out.println("Error: Empty input");
            System.exit(0);

        } catch (NumberFormatException e) {
            System.out.println("Error: \"" + userInput + "\" isn't a valid number");
            System.exit(0);
        }

        return output;
    }

    public static char[] createRandomSecretCode(int codeLength, int numberOfChars) {

        if(codeLength == 0) {
            System.out.println("Error: the code length must be greater than 0");
            System.exit(0);
        }

        if (numberOfChars < codeLength) {
            System.out.println("Error: it's not possible to generate a code with a length of "
                    + codeLength + " with " + numberOfChars + " unique symbols");
            System.exit(0);
        }

        if (codeLength > 36 || numberOfChars > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        }

        List<Character> randomizedChars = new ArrayList<>();

        if (codeLength <= 10) {

            for (char digit = '0'; digit <= '9'; digit++) {
                randomizedChars.add(digit);
            }

        } else {
            for (char digit = '0'; digit <= '9'; digit++) {
                randomizedChars.add(digit);
            }

            for (char letter = 'a'; letter <= 'z'; letter++) {
                randomizedChars.add(letter);
            }
        }

        Collections.shuffle(randomizedChars);

        char[] secretCode = new char[codeLength];

        for (int i = 0; i < secretCode.length; i++) {
            secretCode[i] = randomizedChars.get(i);
        }

        char[] ranges = findRanges(numberOfChars);
        printCodeGenerationMessage(ranges, codeLength);
        return secretCode;
    }

    private static char[] findRanges(int numberOfChars) {

        char minDigit = '0';
        char minLetter = 'a';
        char maxDigit;
        char maxLetter;

        if (numberOfChars <= 10) {
            maxDigit = (char) (minDigit + Math.min(9, numberOfChars));
            return new char[]{minDigit, maxDigit};

        } else {
            maxDigit = '9';
            maxLetter = (char) (minLetter + Math.min(26, numberOfChars - 10) - 1);
            return new char[]{minDigit, maxDigit, minLetter, maxLetter};
        }
    }

    private static void printCodeGenerationMessage(char[] ranges, int codeLength) {

        String stars = "*".repeat(codeLength);
        String letterRangeToPrint = " ";
        String digitRangeToPrint = String.format("(%c-%c)", ranges[0], ranges[1]);
        String formatted = String.format("%s %s", stars, digitRangeToPrint);

        if (ranges.length == 4) {
            digitRangeToPrint = String.format("(%c-%c,", ranges[0], ranges[1]);
            letterRangeToPrint = String.format("%c-%c)", ranges[2], ranges[3]);
            formatted = String.format("%s %s %s", stars, digitRangeToPrint, letterRangeToPrint);
        }

        System.out.println("The secret code is prepared: " + formatted);
    }

    public static char[] getUserAnswer() {
        return scanner.nextLine().toCharArray();
    }

    public static boolean gradeAnswer(char[] secretCode, char[] userAnswer) {

        int bullCounter = 0;
        int cowCounter = 0;
        String grade = "";

        for (int i = 0; i < secretCode.length; i++) {
            for (int j = 0; j < userAnswer.length; j++) {
                if (secretCode[i] == userAnswer[j]) {
                    if (i == j) {
                        bullCounter++;
                    } else {
                        cowCounter++;
                    }
                }
            }
        }

        if (bullCounter > 0) {
            grade += bullCounter + (bullCounter == 1 ? " bull" : " bulls");
            if (cowCounter > 0) grade += " and ";
        }

        if (cowCounter > 0) {
            grade += cowCounter + (cowCounter == 1 ? " cow" : " cows");
        }

        if (bullCounter == 0 && cowCounter == 0) {
            grade = "None";
        }

        System.out.println("Grade: " + grade);
        return bullCounter == secretCode.length;
    }
}
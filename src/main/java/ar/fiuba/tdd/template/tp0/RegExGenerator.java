package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RegExGenerator {
    // TODO: Uncomment this field
    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    // TODO: Uncomment parameters
    public List<String> generate(String regEx, int numberOfResults) {

        ArrayList<String> resultArray = new ArrayList<String>();
        for (int i = 0; i < numberOfResults; i++) {
            resultArray = this.generateRegEx(regEx);
        }

        return resultArray;
    }

    private ArrayList<String>  generateRegEx(String expression) {

        ArrayList<String> workArray = new ArrayList<String>();
        for (int n = 0; n < expression.length(); n++) {

            char actualChar = expression.charAt(n);
            switch (actualChar) {
                case '.':
                    workArray.add(this.generateRandomChar(expression,n));
                    break;
                case '[':
                    workArray.add(this.generateRandomCharSet(expression,n,expression.indexOf(']')));
                    break;
                case '\\':
                    workArray.add(this.generateLiteral(expression,n + 1));
                    break;
                default:
                    workArray.add(this.generateLiteral(expression,n));
                    break;
            }
        }
        return workArray;
    }

    private String cuantZeroOrOne(String expression) {

        int randomNumber = ThreadLocalRandom.current().nextInt(0, 2);
        int randomIndex = ThreadLocalRandom.current().nextInt(0, expression.length() + 1);
        if (randomNumber == 0) {
            return "";
        }
        else {
            return expression.substring(randomIndex,randomIndex + 1);
        }
    }

    private String cuantZeroToMany(String expression) {

        String chooseString = " ";
        chooseString = chooseString + expression;

        return this.getReturnString(chooseString);
    }

    private String cuantOneToMany(String expression) {

        return this.getReturnString(expression);
    }

    private String generateRandomChar(String expression, int index) {

        int randomNumber = ThreadLocalRandom.current().nextInt(0, 256);
        String randomChar = Character.toString((char) randomNumber);

        return this.manageCuant(expression.charAt(index + 1),randomChar,randomChar);
    }

    private String generateRandomCharSet(String expression, int indexIni, int indexFin) {

        String workExpression = expression.substring(indexIni,indexFin);
        char cuant = expression.charAt(indexFin + 1);
        int randomInt = ThreadLocalRandom.current().nextInt(0, workExpression.length() + 1);
        String randomString = Character.toString((char) randomInt);

        return this.manageCuant(cuant,workExpression,randomString);

    }

    private String generateLiteral(String expression, int index) {

        char specialChar = expression.charAt(index + 1);
        int randomChar = ThreadLocalRandom.current().nextInt(0, 256);
        String randomString = Character.toString((char) randomChar);

        switch (specialChar) {
            case '+':
                return this.cuantOneToMany(randomString);
            case '*':
                return this.cuantZeroToMany(randomString);
            case '?':
                return this.cuantZeroOrOne(randomString);
            default:
                return expression.substring(index, index + 1);
        }

    }

    private String manageCuant(char specialChar, String toWork, String defaultString) {
        switch (specialChar) {
            case '+':
                return this.cuantOneToMany(toWork);
            case '*':
                return this.cuantZeroToMany(toWork);
            case '?':
                return this.cuantZeroOrOne(toWork);
            default:
                return defaultString;
        }
    }

    private String getReturnString(String characters) {

        StringBuffer buffer = new StringBuffer();

        int randomMax = ThreadLocalRandom.current().nextInt(0, this.maxLength + 1);
        int randomIndex = ThreadLocalRandom.current().nextInt(0, characters.length() + 1);
        for (int i = 0; i < randomMax; i++) {
            buffer.append(characters.substring(randomIndex,randomIndex + 1));
        }

        return buffer.toString();
    }
}
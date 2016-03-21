package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RegExGenerator {

    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) {

        ArrayList<String> resultArray = new ArrayList<String>();
        for (int i = 0; i < numberOfResults; i++) {
            resultArray.add(this.generateRegEx(regEx));
        }

        return resultArray;
    }

    private String generateRegEx(String expression) {

        StringBuffer workArray = new StringBuffer();
        for (int n = 0; n < expression.length(); n++) {

            char actualChar = expression.charAt(n);
            switch (actualChar) {
                case '.':
                    workArray.append(this.generateRandomChar(expression,n));
                    break;
                case '[':
                    workArray.append(this.generateRandomCharSet(expression,n,expression.indexOf(']')));
                    break;
                case '\\':
                    workArray.append(this.generateLiteral(expression,n + 1));
                    break;
                default:
                    workArray.append(this.generateLiteral(expression,n));
                    break;
            }
            n = this.getNextIndex(expression,n);
        }
        return workArray.toString();
    }

    private String quantifierZeroOrOne(String expression) {

        int randomNumber = ThreadLocalRandom.current().nextInt(0, 2);
        int randomIndex = ThreadLocalRandom.current().nextInt(0, expression.length());
        if (randomNumber == 0) {
            return "";
        }
        else {
            return expression.substring(randomIndex,randomIndex + 1);
        }
    }

    private String quantifierZeroOrMany(String expression) {

        return this.getReturnStringZeroOrMany(expression);
    }

    private String quantifierOneOrMany(String expression) {

        return this.getReturnStringOneOrMany(expression);
    }

    private String generateRandomChar(String expression, int index) {

        int randomNumber = ThreadLocalRandom.current().nextInt(32, 256);
        String randomChar = Character.toString((char) randomNumber);
        if (index < expression.length() - 1) {
            return this.manageQuantifier(expression.charAt(index + 1),randomChar,randomChar);
        }
        else {
            return randomChar;
        }

    }

    private String generateRandomCharSet(String expression, int indexIni, int indexFin) {

        String workExpression = expression.substring(indexIni + 1,indexFin);
        char quant;
        if (indexFin < expression.length() - 1) {
            quant = expression.charAt(indexFin + 1);
        }
        else {
            quant = expression.charAt(indexFin);
        }
        int randomInt = ThreadLocalRandom.current().nextInt(0, workExpression.length());
        String randomString = workExpression.substring(randomInt,randomInt + 1);

        return this.manageQuantifier(quant,workExpression,randomString);

    }

    private String generateLiteral(String expression, int index) {

        char specialChar;
        if (index < expression.length() - 1) {
            specialChar = expression.charAt(index + 1);
        }
        else {
            specialChar = expression.charAt(index);
        }
        String thisChar = expression.substring(index, index + 1);

        switch (specialChar) {
            case '+':
                return this.quantifierOneOrMany(thisChar);
            case '*':
                return this.quantifierZeroOrMany(thisChar);
            case '?':
                return this.quantifierZeroOrOne(thisChar);
            default:
                return expression.substring(index, index + 1);
        }

    }

    private String manageQuantifier(char specialChar, String toWork, String defaultString) {
        switch (specialChar) {
            case '+':
                return this.quantifierOneOrMany(toWork);
            case '*':
                return this.quantifierZeroOrMany(toWork);
            case '?':
                return this.quantifierZeroOrOne(toWork);
            default:
                return defaultString;
        }
    }

    private String getReturnStringZeroOrMany(String characters) {

        int randomMax = ThreadLocalRandom.current().nextInt(0, this.maxLength + 1);

        return this.buffering(randomMax, characters);
    }

    private String getReturnStringOneOrMany(String str) {

        int max = ThreadLocalRandom.current().nextInt(1, this.maxLength + 1);

        return this.buffering(max, str);
    }

    private int getNextIndex(String workExp, int pos) {
        if (pos < workExp.length() - 1) {
            char actualChar = workExp.charAt(pos);
            switch (actualChar) {
                case '[':
                    char q;
                    if (workExp.indexOf(']', pos) < workExp.length() - 1) {
                        q = workExp.charAt(workExp.indexOf(']', pos) + 1);
                    }
                    else {
                        q = workExp.charAt(workExp.indexOf(']', pos));
                    }
                    if (this.isQuantifier(q)) {
                        return workExp.indexOf(']', pos) + 1;
                    }
                    else {
                        return workExp.indexOf(']', pos);
                    }
                case '\\':
                    if (this.isQuantifier(workExp.charAt(pos + 1))) {
                        return pos + 2;
                    }
                    else {
                        return pos + 1;
                    }
                default:
                    if (this.isQuantifier(workExp.charAt(pos + 1))) {
                        return pos + 1;
                    }
                    else {
                        return pos;
                    }
            }
        }
        else {
            return pos;
        }
    }

    private boolean isQuantifier(char quant) {

        if (quant == '+' || quant == '*' || quant == '?') {
            return true;
        }
        else {
            return false;
        }
    }

    private String buffering(int maxLength, String toBuffer) {
        StringBuffer bufferig = new StringBuffer();

        int index = ThreadLocalRandom.current().nextInt(0, toBuffer.length());
        for (int i = 0; i < maxLength; i++) {
            bufferig.append(toBuffer.substring(index,index + 1));
        }

        return bufferig.toString();
    }
}
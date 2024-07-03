package Ui.AdminUI;

public class ConvertCurrencyToEnglish {
    private static final String[] ones = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private static final String[] tens = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    private static final String[] teens = {"", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private final float amount;

    public ConvertCurrencyToEnglish(float amount) {
        this.amount = amount;
    }

    // Function to convert integer part to English
    private String convertIntToEnglish(int num) {
        if (num == 0) {
            return "zero";
        }
        StringBuilder result = new StringBuilder();
        if (num >= 1000) {
            result.append(convertIntToEnglish(num / 1000)).append(" thousand ");
            num %= 1000;
        }
        if (num >= 100) {
            result.append(ones[num / 100]).append(" hundred ");
            num %= 100;
        }
        if (num >= 20) {
            result.append(tens[num / 10]).append(" ");
            num %= 10;
        } else if (num >= 11) {
            result.append(teens[num - 10]).append(" ");
            num = 0;
        }
        if (num > 0) {
            result.append(ones[num]).append(" ");
        }
        return result.toString();
    }

    // Function to convert decimal part to English
    private String convertCentsToEnglish(int num) {
        return num == 0 ? "" : "and " + (num < 10 ? ones[num] : tens[num / 10]) + " cents";
    }

    // Function to convert float value to English
    public String convertFloatToEnglish() {
        int ringgit = (int) amount;
        int cents = Math.round((amount - ringgit) * 100);
        return convertIntToEnglish(ringgit) + "ringgit" + convertCentsToEnglish(cents);
    }
}

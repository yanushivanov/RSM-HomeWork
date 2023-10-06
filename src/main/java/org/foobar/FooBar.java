package org.foobar;
import java.util.HashMap;
import java.util.Map;

public class FooBar {

    public String fooBar(String input) {
        String[] numbers = input.split(",");
        Map<Integer, Integer> seenNumbers = new HashMap<>();
        StringBuilder output = new StringBuilder();

        for (String num : numbers) {
            int number = Integer.parseInt(num.trim());
            seenNumbers.putIfAbsent(number, 0);
            int count = seenNumbers.get(number);
            seenNumbers.put(number, count + 1);

            if (count > 0) {
                output.append(getStringValue(number)).append("-copy,");
            } else {
                output.append(getStringValue(number)).append(",");
            }
        }
        return output.substring(0, output.length() - 1);
    }

    private String getStringValue(int iNum ) {

        if (iNum % 3 == 0 && iNum % 5 == 0) {
            return "foobar";
        } else if (iNum % 3 == 0) {
            return"foo";
        } else if (iNum % 5 == 0) {
            return "bar";
        } else {
            return String.valueOf(iNum);
        }
    }
}

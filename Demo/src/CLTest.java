package src;

import java.util.Map;

public class CLTest extends TestType{

    public CLTest() {
        pos1 = 15;
        pos2 = 24;
        neg1 = 3;
        neg2 = 12;
        blank = 0;
        testValueCount = 4;
    }

    @Override
    void setReportParameters(Map parameters) {
        StringBuilder headerText = new StringBuilder();

        headerText.append("Optical Density (OD) results for serum samples that are equal to or greater than ");
        headerText.append(String.format("%.3f", getMargins() + 0.3));
        headerText.append(" are considered positive. Results between ");
        headerText.append(String.format("%.3f", getMargins()));
        headerText.append(" and ");
        headerText.append(String.format("%.3f", getMargins() + 0.3));
        headerText.append(" are considered marginal");
        parameters.put("HEADER_TEXT", headerText.toString());
        //parameters.put("POSITIVE_OD", String.format("%.3f", getMargins() + 0.3));
        //parameters.put("MARGINAL_LOW", String.format("%.3f", getMargins()));
        //parameters.put("MARGINAL_HIGH", String.format("%.3f", getMargins() + 0.3));
    }

    public String getResult(Float testResult) {
        Float marginalCutoff = getMargins();
        System.out.println("Calculating testResult with margin of " + marginalCutoff);
        System.out.println("test result: " + testResult);
        //test negative
        if (testResult < marginalCutoff) {
            System.out.println("Is less than " + marginalCutoff + " so, negative");

            return "Negative";

        } else if (testResult > marginalCutoff && testResult < (marginalCutoff + 0.3) ) {

            return "Marginal";
        } else if (testResult > marginalCutoff) {
            System.out.println("Is greater than " + marginalCutoff + " and not less than " + (marginalCutoff + 0.3) + " so, positive");
            return "Positive";
        }

        return "";
    }

    public Float getMargins() {
        System.out.println("Calculating Margin");
        Float negativeControlOne = controlValues.get(0);

        Float negativeControlTwo = controlValues.get(1);

        Float positiveControlOne = controlValues.get(2);
        Float positiveControlTwo = controlValues.get(3);

        Float negAvg = (negativeControlOne + negativeControlTwo)/2;
        //not actually average of negative/positive control values
        System.out.println("Negavg: (0.146)" + negativeControlOne + "+ (1.2)" +  positiveControlOne);
        Float posAvg = (positiveControlTwo + positiveControlOne)/2;
        System.out.println("Posavg: (1.105)" + positiveControlTwo + "+ (0.146)" +  negativeControlTwo);

        Float marginalCutoff = ((posAvg - negAvg)/4) + negAvg;
        return marginalCutoff;
    }

}

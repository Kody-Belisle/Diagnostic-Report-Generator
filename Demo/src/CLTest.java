package src;

import java.util.Map;

public class CLTest extends TestType{

    public CLTest() {
        pos1 = 15;
        pos2 = 24;
        neg1 = 3;
        neg2 = 12;
        blank = 0;
    }

    @Override
    void setReportParameters(Map parameters) {
        parameters.put("POSITIVE_OD", String.format("%.3f", getMargins() + 0.3));
        parameters.put("MARGINAL_LOW", String.format("%.3f", getMargins()));
        parameters.put("MARGINAL_HIGH", String.format("%.3f", getMargins() + 0.3));
    }

    public String getResult(Float testResult) {

        Float marginalCutoff = getMargins();
        //test negative
        if (testResult < marginalCutoff) {

            return "Negative";

        } else if (testResult > marginalCutoff && testResult < (marginalCutoff + 0.3) ) {

            return "Marginal";
        } else if (testResult > marginalCutoff) {

            return "Positive";
        }

        return "";
    }

    public Float getMargins() {
        Float negativeControlOne = controlValues.get(0);
        Float negativeControlTwo = controlValues.get(1);

        Float positiveControlOne = controlValues.get(2);
        Float positiveControlTwo = controlValues.get(3);

        Float negAvg = (negativeControlOne + negativeControlTwo)/2;
        Float posAvg = (positiveControlTwo + positiveControlOne)/2;

        Float marginalCutoff = ((posAvg - negAvg)/4) + negAvg;
        return marginalCutoff;
    }

}

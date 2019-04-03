package src;

import java.util.Map;

public class BLVTest extends TestType{

    public BLVTest() {
        pos1 = 37;
        pos2 = 44;
        neg1 = 25;
        neg2 = 32;
        blank = -1;
    }

    @Override
    void setReportParameters(Map parameters) {

    }

    @Override
    String getResult(Float testResult) {

        if (testResult > (controlValues.get(2) + controlValues.get(1) / 2) ) {
            return "Positive";
        } else if (testResult < (controlValues.get(2) + controlValues.get(1) / 2) ) {
            return "Negative";
        }

        return "Error";
    }

    //if x < average(pos1, neg2) result is negative else positive

}

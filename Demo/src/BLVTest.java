package src;

import java.util.Map;

public class BLVTest extends TestType{

    public BLVTest() {
        pos1 = 37;
        pos2 = 44;
        neg1 = 25;
        neg2 = 32;
        blank = -1;
        testValueCount = 4;
    }

    @Override
    void setReportParameters(Map parameters) {

    }

    @Override
    String getResult(Float testResult) {

        Float pos1 = controlValues.get(2);
        Float neg2 = controlValues.get(1);

        Float poscheck = ( (pos1 + neg2) / 2 );

        //System.out.println("poscheck is " + poscheck);
        if (testResult > (poscheck) ) {
            //System.out.println("Control value " + testResult + " is greater than" +  (poscheck) + " returning positive" );
            return "Positive";
        } else if (testResult < (poscheck) ) {
            //System.out.println("Control value " + testResult + " is less than" +  (poscheck) + " returning negative" );
            return "Negative";
        }

        return "Error";
    }

    //if x < average(pos1, neg2) result is negative else positive

}

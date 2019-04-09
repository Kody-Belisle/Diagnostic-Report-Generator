package src;

import java.util.Map;

public class JohnesTest extends TestType{
    String animalType;

    public JohnesTest() {
        pos1 = 0;
        pos2 = 12;
        neg1 = 24;
        neg2 = 36;
        blank = -1;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    @Override
    void setReportParameters(Map parameters) {

    }

    @Override
    String getResult(Float testResult) {
        Float PP;
        int compareVal;
        Float averageNeg = ( (controlValues.get(0) + controlValues.get(1)) / 2);
        Float averagePos = ( (controlValues.get(2) + controlValues.get(3)) / 2);
        //PP = ((TESTVALUE-AverageNeg) / (AveragePos - AverageNeg))*100

        PP = ((testResult - averageNeg) / (averagePos - averageNeg)) * 100;

        if (animalType.equals("Cow")) {
            compareVal = 10;
        } else if (animalType.equals("Goat") || animalType.equals("Sheep") || animalType.equals("Buffalo") || animalType.equals("Other")) {
            compareVal = 15;
        } else {
            compareVal = -1;
        }

        if (PP >= compareVal) {
            return "Positive";
        } else {
            return "Negative";
        }
    }
}

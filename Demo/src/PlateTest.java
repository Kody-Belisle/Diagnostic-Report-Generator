package src;
public class PlateTest {

    private String animalID;
    private float opticalDensity;
    private int testResult;

    public PlateTest(String animalID, float opticalDensity, int testResult) {
        this.animalID = animalID;
        this.opticalDensity = opticalDensity;
        this.testResult = testResult;
    }

    public String toString() { 

        String testResultToString = "ERROR";

        if (testResult == -1) {
            testResultToString = "NEGATIVE";
        }

        if (testResult == 0) {
            testResultToString = "MARGINAL";
        }

        if (testResult == 1) {
            testResultToString = "POSITIVE";
        }

        return animalID + " " + opticalDensity + " " + testResultToString;
    } 
    
}
package src;

import java.util.ArrayList;
import java.util.Map;

public abstract class TestType implements java.io.Serializable {
    ArrayList<String> animalIDList;
    ArrayList<Report> reportList;

    ArrayList<Float> controlValues;
    int pos1;
    int pos2;
    int neg1;
    int neg2;
    int blank;
    int testValueCount;

    protected TestType() {
        reportList = new ArrayList<Report>();
        animalIDList = new ArrayList<String>();
    }

    public void clearList() {
        animalIDList.clear();
    }

    public int getNeg1() {
        return neg1;
    }

    public int getNeg2() {
        return neg2;
    }

    public int getPos1() {
        return pos1;
    }

    public int getPos2() {
        return pos2;
    }

    public int getBlank() {
        return blank;
    }

    public int getTestValueCount() {return testValueCount;}

    public void setControlValues(ArrayList<Float> controlValues) {
        this.controlValues = controlValues;
    }

    abstract void setReportParameters(Map parameters);

    abstract String getResult(Float testResult);
}

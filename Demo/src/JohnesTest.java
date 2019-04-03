package src;

import java.util.Map;

public class JohnesTest extends TestType{

    public JohnesTest() {
        pos1 = 0;
        pos2 = 12;
        neg1 = 24;
        neg2 = 36;
        blank = -1;
    }

    @Override
    void setReportParameters(Map parameters) {

    }

    @Override
    String getResult(Float testResult) {
        return null;
    }
}

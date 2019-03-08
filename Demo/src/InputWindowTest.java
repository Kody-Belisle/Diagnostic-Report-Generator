package src;

import jdk.internal.util.xml.impl.Input;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputWindowTest extends InputWindow {

    @Test
    void setTestValsOne() {
        setTestVals(1);
        testXVals.get(1);
        testYVals.get(1);
    }

    @Test
    void setTestValsTwo() {
        setTestVals(2);
        testXVals.get(1);
        testYVals.get(1);
    }

    @Test
    void setTestValsThree() {
        setTestVals(3);
        testXVals.get(1);
        testYVals.get(1);
    }
}
package src;

import javax.swing.table.DefaultTableModel;
import java.io.Serializable;
import java.util.ArrayList;

public class WellMap implements Serializable {
    private ArrayList<String> animalList;
    String resultFile;
    int fillX;
    int fillY;
    private DefaultTableModel singleModel;

    public WellMap(ArrayList<String> list, int x, int y, DefaultTableModel model) {
        this.animalList = list;
        this.fillX = x;
        this.fillY = y;
        this.singleModel = model;
        this.resultFile = "";
    }

    public ArrayList<String> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(ArrayList<String> animalList) {
        this.animalList.clear();
        for (String animal: animalList) {
            this.animalList.add(animal);
        }
    }

    public int getFillX() {
        return fillX;
    }

    public void setFillX(int fillX) {
        this.fillX = fillX;
    }

    public int getFillY() {
        return fillY;
    }

    public void setFillY(int fillY) {
        this.fillY = fillY;
    }

    public DefaultTableModel getSingleModel() {
        return singleModel;
    }

    public void setSingleModel(DefaultTableModel singleModel) {
        this.singleModel = singleModel;
    }
}

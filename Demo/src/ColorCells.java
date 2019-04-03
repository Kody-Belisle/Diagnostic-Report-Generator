package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ColorCells extends DefaultTableCellRenderer {

    Color[][] tableColors;

    public ColorCells(JTable table, String fileName, int testID) {
        tableColors = new Color[8][13];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 8; j++) {
                tableColors[j][i] = Color.WHITE;
            }
        }
        getCellColor(fileName, testID);


        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = ColorCells.super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(tableColors[row][column]);
                System.out.println("Set row: " + row + " column: " + column);
                System.out.println("To color: " + tableColors[row][column]);
                return c;
            }
        });
    }

    private void getCellColor(String fileName, int testID) {
        ArrayList<Float> parsedData = new ArrayList<Float>();
        ParsePlateReaderData parse = new ParsePlateReaderData(new File(fileName), testID);
        TestType testObject;

        switch (testID) {
            case 1:
                testObject = new BLVTest();
                break;
            case 2:
                testObject = new CLTest();
                break;
            case 3:
                testObject = new JohnesTest();
                break;
                default:
                    testObject = new CLTest();
        }
        parsedData = parse.parseValuesWithoutArranging();

        for (Float e : parse.getUnorderedControlValues()) {
            System.out.println("ctrl val: " + e);
        }
        testObject.setControlValues(parse.getUnorderedControlValues());
        int parsedDataIndex = 0;
        int maxDataIndex = parsedData.size();

        System.out.println("Parsed data size: " + parsedData.size());

        for (int j = 0; j < 8; j++) {
            for (int i = 1; i < 13; i++) {

                System.out.println("Calculating cell i: " + i + " j: " + j);

                //check if we're out of data
                if (parsedDataIndex >= maxDataIndex) {
                    return;
                }

                System.out.println("test value: " + parsedData.get(parsedDataIndex));
                //check if we're looking at a test value
                if (parsedDataIndex == testObject.getNeg1() || parsedDataIndex == testObject.getNeg2() ||
                    parsedDataIndex == testObject.getPos1() || parsedDataIndex == testObject.getPos2() ||
                    parsedDataIndex == testObject.getBlank()) {
                    parsedDataIndex++;
                    continue;
                }

                if (testObject.getResult(parsedData.get(parsedDataIndex)).equals("Positive")) {  //if the result is positive
                    tableColors[j][i] = Color.RED;
                    System.out.println("Setting row:" + j + " column:" + i + "to red");
                } else if (testObject.getResult(parsedData.get(parsedDataIndex)).equals("Negative")) {
                    tableColors[j][i] = Color.GREEN;
                    System.out.println("Setting row:" + j + " column:" + i + "to green");
                }

                parsedDataIndex++;
            }
        }
    }
}
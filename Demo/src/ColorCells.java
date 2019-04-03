package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ColorCells extends DefaultTableCellRenderer {

    public ColorCells(JTable table, String fileName, int testID) {
        ArrayList<Float> parsedData = new ArrayList<Float>();
        ParsePlateReaderData parse = new ParsePlateReaderData(new File(fileName), testID);
        parsedData = parse.parseValues();


        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JPanel pane = new JPanel();
                pane.setBackground(Color.RED);
                return pane;
            }
        });
    }

    private void getCellColor() {

    }
}
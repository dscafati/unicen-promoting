package com.unicen.app.gui;

import javax.swing.table.DefaultTableModel;

/**
 * Created by Alfonso on 2/8/16.
 */
public class MatrixModel extends DefaultTableModel{
    private boolean[][] editable_cells;

    public MatrixModel(int rows, int cols) {
        super(rows, cols);
        this.editable_cells = new boolean[rows][cols];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return this.editable_cells[row][column];
    }

    public void setCellEditable(int row, int col, boolean value) {
        this.editable_cells[row][col] = value;
        this.fireTableCellUpdated(row, col);
    }

    @Override
    public void setValueAt (Object aValue, int row, int column) {

        super.setValueAt(aValue,row,column);

        if (row>0 && column >0 && (row!=column)) {

            Double value= Double.parseDouble(aValue.toString());
            super.setValueAt(1/value.doubleValue(), column, row);
        }
    }

}

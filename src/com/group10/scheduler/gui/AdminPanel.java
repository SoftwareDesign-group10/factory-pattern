package com.group10.scheduler.gui;

import com.group10.scheduler.domain.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {
    private final GUIController controller;

    private JTextField roomIdField = new JTextField(10);
    private JTextField capacityField = new JTextField(6);
    private JTextField buildingField = new JTextField(10);
    private JTextField roomNumberField = new JTextField(8);
    private JLabel statusLabel = new JLabel(" ");

    private DefaultTableModel roomsModel = new DefaultTableModel(
            new Object[]{"Room ID", "Building", "Room #", "Capacity", "Status"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable roomsTable = new JTable(roomsModel);

    public AdminPanel(GUIController controller) {
        this.controller = controller;
        buildUI();
        refreshRooms();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel addForm = new JPanel();
        addForm.add(new JLabel("Room ID:")); addForm.add(roomIdField);
        addForm.add(new JLabel("Building:")); addForm.add(buildingField);
        addForm.add(new JLabel("Room #:")); addForm.add(roomNumberField);
        addForm.add(new JLabel("Capacity:")); addForm.add(capacityField);
        JButton addBtn = new JButton("Add Room");
        addForm.add(addBtn);
        addBtn.addActionListener(e -> handleAddRoom());

        add(addForm, BorderLayout.NORTH);
        add(new JScrollPane(roomsTable), BorderLayout.CENTER);

        JPanel actionsRow = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton enableBtn = new JButton("Enable Selected");
        JButton disableBtn = new JButton("Disable Selected");
        JButton closeBtn = new JButton("Close for Maintenance");
        actionsRow.add(refreshBtn);
        actionsRow.add(enableBtn);
        actionsRow.add(disableBtn);
        actionsRow.add(closeBtn);

        refreshBtn.addActionListener(e -> refreshRooms());
        enableBtn.addActionListener(e -> withSelectedRoom(controller::onEnableRoomClicked));
        disableBtn.addActionListener(e -> withSelectedRoom(controller::onDisableRoomClicked));
        closeBtn.addActionListener(e -> withSelectedRoom(controller::onCloseRoomClicked));

        JPanel south = new JPanel(new BorderLayout());
        south.add(actionsRow, BorderLayout.NORTH);
        south.add(statusLabel, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }

    private void handleAddRoom() {
        try {
            int capacity = Integer.parseInt(capacityField.getText().trim());
            Room room = controller.onAddRoomClicked(
                    roomIdField.getText().trim(), capacity,
                    buildingField.getText().trim(), roomNumberField.getText().trim());
            setStatus("Room added: " + room.getRoomId(), false);
            refreshRooms();
        } catch (NumberFormatException nfe) {
            setStatus("Capacity must be a number.", true);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), true);
        }
    }

    private void withSelectedRoom(java.util.function.Predicate<String> action) {
        int row = roomsTable.getSelectedRow();
        if (row < 0) { setStatus("Select a room first.", true); return; }
        String roomId = (String) roomsModel.getValueAt(row, 0);
        boolean ok = action.test(roomId);
        setStatus(ok ? "Updated " + roomId : "Update failed for " + roomId, !ok);
        refreshRooms();
    }

    public void refreshRooms() {
        roomsModel.setRowCount(0);
        for (Room r : controller.getAllRooms()) {
            roomsModel.addRow(new Object[]{r.getRoomId(), r.getBuilding(), r.getRoomNumber(), r.getCapacity(), r.getStatus()});
        }
    }

    private void setStatus(String text, boolean isError) {
        statusLabel.setText(text);
        statusLabel.setForeground(isError ? Color.RED : new Color(0, 128, 0));
    }
}

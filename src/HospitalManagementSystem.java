import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HospitalManagementSystem extends JFrame {

    private static final String DATA_DIR = "data";
    private static final String DOCTORS_FILE = DATA_DIR + "/doctors.txt";
    private static final String PATIENTS_FILE = DATA_DIR + "/patients.txt";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.txt";
    private static final String TESTS_FILE = DATA_DIR + "/tests.txt";

    // Colors
    private static final Color PRIMARY_COLOR = new Color(30, 144, 255);
    private static final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;

    // Card Layout for main panel
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    // Dashboard Cards UI
    private JLabel totalDoctorsLabel, totalPatientsLabel, totalAppointmentsLabel, totalTestsLabel, todayAptsLabel;
    private JTextArea recentActivityArea;

    // Tables
    private JTable docTable, patTable, aptTable, testTable;
    private DefaultTableModel docModel, patModel, aptModel, testModel;

    public HospitalManagementSystem() {
        initDataFiles();
        showLoginDialog();
    }

    private void initDataFiles() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
        createFileIfNotExists(DOCTORS_FILE);
        createFileIfNotExists(PATIENTS_FILE);
        createFileIfNotExists(APPOINTMENTS_FILE);
        createFileIfNotExists(TESTS_FILE);
    }

    private void createFileIfNotExists(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Hospital Management System - Login", true);
        loginDialog.setSize(400, 250);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setLayout(new BorderLayout());
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginDialog.add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (username.equals("admin") && password.equals("admin123")) {
                loginDialog.dispose();
                buildUI();
                setVisible(true);
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid Credentials! (Default: admin / admin123)", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginDialog.setVisible(true);
        if (!isVisible()) {
            System.exit(0);
        }
    }

    private void buildUI() {
        setTitle("Hospital Management System");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar Navigation
        // Sidebar Navigation
        QVBox sidebar = new QVBox();
        sidebar.setPreferredSize(new Dimension(220, 750));

        JLabel brandLabel = new JLabel("HMS PRO", SwingConstants.CENTER);
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        sidebar.add(brandLabel);

        String[] navItems = {"Dashboard", "Doctor Management", "Patient Management", "Appointment Management", "Laboratory Management", "Reports", "Logout"};
        for (String item : navItems) {
            JButton navButton = createNavButton(item);
            sidebar.add(navButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        // Main Content Area
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BG_COLOR);

        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createDoctorPanel(), "Doctor Management");
        mainContentPanel.add(createPatientPanel(), "Patient Management");
        mainContentPanel.add(createAppointmentPanel(), "Appointment Management");
        mainContentPanel.add(createLaboratoryPanel(), "Laboratory Management");
        mainContentPanel.add(createReportsPanel(), "Reports");

        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        refreshDashboard();
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR_COLOR);
            }
        });

        btn.addActionListener(e -> {
            if (text.equals("Logout")) {
                int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    new HospitalManagementSystem();
                }
            } else {
                cardLayout.show(mainContentPanel, text);
                if (text.equals("Dashboard")) refreshDashboard();
                else if (text.equals("Doctor Management")) refreshDoctorTable();
                else if (text.equals("Patient Management")) refreshPatientTable();
                else if (text.equals("Appointment Management")) refreshAppointmentTable();
                else if (text.equals("Laboratory Management")) refreshLaboratoryTable();
            }
        });
        return btn;
    }

    // --- DASHBOARD PANEL ---
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("System Overview & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        cardsPanel.setBackground(BG_COLOR);

        totalDoctorsLabel = new JLabel("0", SwingConstants.CENTER);
        totalPatientsLabel = new JLabel("0", SwingConstants.CENTER);
        totalAppointmentsLabel = new JLabel("0", SwingConstants.CENTER);
        totalTestsLabel = new JLabel("0", SwingConstants.CENTER);
        todayAptsLabel = new JLabel("0", SwingConstants.CENTER);

        cardsPanel.add(createMetricCard("Total Doctors", totalDoctorsLabel, new Color(41, 128, 185)));
        cardsPanel.add(createMetricCard("Total Patients", totalPatientsLabel, new Color(39, 174, 96)));
        cardsPanel.add(createMetricCard("Total Appointments", totalAppointmentsLabel, new Color(142, 68, 173)));
        cardsPanel.add(createMetricCard("Total Lab Tests", totalTestsLabel, new Color(230, 126, 34)));
        cardsPanel.add(createMetricCard("Today's Appointments", todayAptsLabel, new Color(231, 76, 60)));

        recentActivityArea = new JTextArea();
        recentActivityArea.setEditable(false);
        recentActivityArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane activityScroll = new JScrollPane(recentActivityArea);
        activityScroll.setBorder(BorderFactory.createTitledBorder("Recent Activity Log"));

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(BG_COLOR);
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(activityScroll, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 10));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void refreshDashboard() {
        List<String[]> doctors = readData(DOCTORS_FILE);
        List<String[]> patients = readData(PATIENTS_FILE);
        List<String[]> appointments = readData(APPOINTMENTS_FILE);
        List<String[]> tests = readData(TESTS_FILE);

        totalDoctorsLabel.setText(String.valueOf(doctors.size()));
        totalPatientsLabel.setText(String.valueOf(patients.size()));
        totalAppointmentsLabel.setText(String.valueOf(appointments.size()));
        totalTestsLabel.setText(String.valueOf(tests.size()));

        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        long todayCount = appointments.stream().filter(a -> a.length > 3 && a[3].equals(today)).count();
        todayAptsLabel.setText(String.valueOf(todayCount));

        StringBuilder sb = new StringBuilder();
        sb.append("=== SYSTEM LOG & STATS ===\n");
        sb.append("System Date: ").append(today).append("\n");
        sb.append("Loaded Doctors: ").append(doctors.size()).append("\n");
        sb.append("Loaded Patients: ").append(patients.size()).append("\n");
        sb.append("Loaded Appointments: ").append(appointments.size()).append("\n");
        sb.append("Loaded Lab Tests: ").append(tests.size()).append("\n\n");
        sb.append("--- Recent Appointments ---\n");
        for (int i = Math.max(0, appointments.size() - 5); i < appointments.size(); i++) {
            String[] a = appointments.get(i);
            if (a.length >= 7) {
                sb.append(String.format("ID: %s | Pat: %s | Doc: %s | Date: %s | Status: %s\n", a[0], a[1], a[2], a[3], a[6]));
            }
        }
        recentActivityArea.setText(sb.toString());
    }

    // --- DOCTOR MANAGEMENT PANEL ---
    private JPanel createDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(BG_COLOR);

        JButton addBtn = new JButton("Add Doctor");
        JButton editBtn = new JButton("Update Doctor");
        JButton deleteBtn = new JButton("Delete Doctor");
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(refreshBtn);

        String[] cols = {"ID", "Name", "Age", "Gender", "Specialization", "Qualification", "Exp (Yrs)", "Phone", "Email", "Fee ($)"};
        docModel = new DefaultTableModel(cols, 0);
        docTable = new JTable(docModel);
        JScrollPane scroll = new JScrollPane(docTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        addBtn.addActionListener(e -> showAddDoctorDialog(null));
        editBtn.addActionListener(e -> {
            int row = docTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) docModel.getValueAt(row, 0);
                showAddDoctorDialog(id);
            } else {
                JOptionPane.showMessageDialog(this, "Select a doctor to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = docTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) docModel.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete Doctor " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    deleteRecord(DOCTORS_FILE, id);
                    refreshDoctorTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a doctor to delete.");
            }
        });

        searchBtn.addActionListener(e -> filterTable(docTable, docModel, DOCTORS_FILE, searchField.getText().trim()));
        refreshBtn.addActionListener(e -> refreshDoctorTable());

        return panel;
    }

    private void refreshDoctorTable() {
        docModel.setRowCount(0);
        List<String[]> data = readData(DOCTORS_FILE);
        for (String[] row : data) {
            docModel.addRow(row);
        }
    }

    private void showAddDoctorDialog(String existingId) {
        boolean isEdit = existingId != null;
        JDialog dialog = new JDialog(this, isEdit ? "Update Doctor" : "Add Doctor", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(11, 2, 5, 5));

        JTextField nameF = new JTextField(), ageF = new JTextField(), genF = new JTextField();
        JTextField specF = new JTextField(), qualF = new JTextField(), expF = new JTextField();
        JTextField phoneF = new JTextField(), emailF = new JTextField(), feeF = new JTextField();

        if (isEdit) {
            String[] existingData = findRecord(DOCTORS_FILE, existingId);
            if (existingData != null) {
                nameF.setText(existingData[1]);
                ageF.setText(existingData[2]);
                genF.setText(existingData[3]);
                specF.setText(existingData[4]);
                qualF.setText(existingData[5]);
                expF.setText(existingData[6]);
                phoneF.setText(existingData[7]);
                emailF.setText(existingData[8]);
                feeF.setText(existingData[9]);
            }
        }

        dialog.add(new JLabel(" Name:")); dialog.add(nameF);
        dialog.add(new JLabel(" Age:")); dialog.add(ageF);
        dialog.add(new JLabel(" Gender:")); dialog.add(genF);
        dialog.add(new JLabel(" Specialization:")); dialog.add(specF);
        dialog.add(new JLabel(" Qualification:")); dialog.add(qualF);
        dialog.add(new JLabel(" Experience:")); dialog.add(expF);
        dialog.add(new JLabel(" Phone:")); dialog.add(phoneF);
        dialog.add(new JLabel(" Email:")); dialog.add(emailF);
        dialog.add(new JLabel(" Fee ($):")); dialog.add(feeF);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        dialog.add(saveBtn); dialog.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            try {
                String id = isEdit ? existingId : generateId(DOCTORS_FILE, "DOC");
                String record = String.join("|", id, nameF.getText().trim(), ageF.getText().trim(),
                        genF.getText().trim(), specF.getText().trim(), qualF.getText().trim(),
                        expF.getText().trim(), phoneF.getText().trim(), emailF.getText().trim(), feeF.getText().trim());

                if (isEdit) {
                    updateRecord(DOCTORS_FILE, existingId, record);
                } else {
                    appendRecord(DOCTORS_FILE, record);
                }
                dialog.dispose();
                refreshDoctorTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving record: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // --- PATIENT MANAGEMENT PANEL ---
    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(BG_COLOR);

        JButton addBtn = new JButton("Register Patient");
        JButton editBtn = new JButton("Update Patient");
        JButton deleteBtn = new JButton("Delete Patient");
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search ID/Name");
        JButton refreshBtn = new JButton("Refresh");

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(refreshBtn);

        String[] cols = {"ID", "Name", "Age", "Gender", "Blood Group", "Phone", "Email", "Address", "Medical History"};
        patModel = new DefaultTableModel(cols, 0);
        patTable = new JTable(patModel);
        JScrollPane scroll = new JScrollPane(patTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        addBtn.addActionListener(e -> showAddPatientDialog(null));
        editBtn.addActionListener(e -> {
            int row = patTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) patModel.getValueAt(row, 0);
                showAddPatientDialog(id);
            } else {
                JOptionPane.showMessageDialog(this, "Select a patient to edit.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = patTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) patModel.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete Patient " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    deleteRecord(PATIENTS_FILE, id);
                    refreshPatientTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a patient to delete.");
            }
        });

        searchBtn.addActionListener(e -> filterTable(patTable, patModel, PATIENTS_FILE, searchField.getText().trim()));
        refreshBtn.addActionListener(e -> refreshPatientTable());

        return panel;
    }

    private void refreshPatientTable() {
        patModel.setRowCount(0);
        List<String[]> data = readData(PATIENTS_FILE);
        for (String[] row : data) {
            patModel.addRow(row);
        }
    }

    private void showAddPatientDialog(String existingId) {
        boolean isEdit = existingId != null;
        JDialog dialog = new JDialog(this, isEdit ? "Update Patient" : "Register Patient", true);
        dialog.setSize(400, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(10, 2, 5, 5));

        JTextField nameF = new JTextField(), ageF = new JTextField(), genF = new JTextField();
        JTextField bgF = new JTextField(), phoneF = new JTextField(), emailF = new JTextField();
        JTextField addrF = new JTextField(), histF = new JTextField();

        if (isEdit) {
            String[] existingData = findRecord(PATIENTS_FILE, existingId);
            if (existingData != null) {
                nameF.setText(existingData[1]);
                ageF.setText(existingData[2]);
                genF.setText(existingData[3]);
                bgF.setText(existingData[4]);
                phoneF.setText(existingData[5]);
                emailF.setText(existingData[6]);
                addrF.setText(existingData[7]);
                histF.setText(existingData[8]);
            }
        }

        dialog.add(new JLabel(" Name:")); dialog.add(nameF);
        dialog.add(new JLabel(" Age:")); dialog.add(ageF);
        dialog.add(new JLabel(" Gender:")); dialog.add(genF);
        dialog.add(new JLabel(" Blood Group:")); dialog.add(bgF);
        dialog.add(new JLabel(" Phone:")); dialog.add(phoneF);
        dialog.add(new JLabel(" Email:")); dialog.add(emailF);
        dialog.add(new JLabel(" Address:")); dialog.add(addrF);
        dialog.add(new JLabel(" Medical History:")); dialog.add(histF);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        dialog.add(saveBtn); dialog.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            try {
                String id = isEdit ? existingId : generateId(PATIENTS_FILE, "PAT");
                String record = String.join("|", id, nameF.getText().trim(), ageF.getText().trim(),
                        genF.getText().trim(), bgF.getText().trim(), phoneF.getText().trim(),
                        emailF.getText().trim(), addrF.getText().trim(), histF.getText().trim());

                if (isEdit) {
                    updateRecord(PATIENTS_FILE, existingId, record);
                } else {
                    appendRecord(PATIENTS_FILE, record);
                }
                dialog.dispose();
                refreshPatientTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // --- APPOINTMENT MANAGEMENT PANEL ---
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(BG_COLOR);

        JButton bookBtn = new JButton("Book Appointment");
        JButton editBtn = new JButton("Reschedule/Update");
        JButton cancelBtn = new JButton("Cancel Appointment");
        JButton refreshBtn = new JButton("Refresh");

        toolbar.add(bookBtn);
        toolbar.add(editBtn);
        toolbar.add(cancelBtn);
        toolbar.add(refreshBtn);

        String[] cols = {"APT ID", "Patient ID", "Doctor ID", "Date (YYYY-MM-DD)", "Time", "Reason", "Status"};
        aptModel = new DefaultTableModel(cols, 0);
        aptTable = new JTable(aptModel);
        JScrollPane scroll = new JScrollPane(aptTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        bookBtn.addActionListener(e -> showBookAppointmentDialog(null));
        editBtn.addActionListener(e -> {
            int row = aptTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) aptModel.getValueAt(row, 0);
                showBookAppointmentDialog(id);
            } else {
                JOptionPane.showMessageDialog(this, "Select an appointment to reschedule/update.");
            }
        });

        cancelBtn.addActionListener(e -> {
            int row = aptTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) aptModel.getValueAt(row, 0);
                String[] record = findRecord(APPOINTMENTS_FILE, id);
                if (record != null) {
                    record[6] = "Cancelled";
                    updateRecord(APPOINTMENTS_FILE, id, String.join("|", record));
                    refreshAppointmentTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an appointment to cancel.");
            }
        });

        refreshBtn.addActionListener(e -> refreshAppointmentTable());

        return panel;
    }

    private void refreshAppointmentTable() {
        aptModel.setRowCount(0);
        List<String[]> data = readData(APPOINTMENTS_FILE);
        for (String[] row : data) {
            aptModel.addRow(row);
        }
    }

    private void showBookAppointmentDialog(String existingId) {
        boolean isEdit = existingId != null;
        JDialog dialog = new JDialog(this, isEdit ? "Reschedule Appointment" : "Book Appointment", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));

        JComboBox<String> patCombo = new JComboBox<>();
        JComboBox<String> docCombo = new JComboBox<>();

        for (String[] p : readData(PATIENTS_FILE)) patCombo.addItem(p[0] + " - " + p[1]);
        for (String[] d : readData(DOCTORS_FILE)) docCombo.addItem(d[0] + " - " + d[1]);

        JTextField dateF = new JTextField(LocalDate.now().toString());
        JTextField timeF = new JTextField("10:00");
        JTextField reasonF = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Scheduled", "Completed", "Cancelled"});

        if (isEdit) {
            String[] existingData = findRecord(APPOINTMENTS_FILE, existingId);
            if (existingData != null) {
                dateF.setText(existingData[3]);
                timeF.setText(existingData[4]);
                reasonF.setText(existingData[5]);
                statusCombo.setSelectedItem(existingData[6]);
            }
        }

        dialog.add(new JLabel(" Select Patient:")); dialog.add(patCombo);
        dialog.add(new JLabel(" Select Doctor:")); dialog.add(docCombo);
        dialog.add(new JLabel(" Date (YYYY-MM-DD):")); dialog.add(dateF);
        dialog.add(new JLabel(" Time (HH:MM):")); dialog.add(timeF);
        dialog.add(new JLabel(" Reason:")); dialog.add(reasonF);
        dialog.add(new JLabel(" Status:")); dialog.add(statusCombo);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        dialog.add(saveBtn); dialog.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            try {
                if (patCombo.getSelectedItem() == null || docCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(dialog, "Please ensure patients and doctors exist.");
                    return;
                }
                String patId = ((String) patCombo.getSelectedItem()).split(" - ")[0];
                String docId = ((String) docCombo.getSelectedItem()).split(" - ")[0];
                String date = dateF.getText().trim();
                String time = timeF.getText().trim();
                String reason = reasonF.getText().trim();
                String status = (String) statusCombo.getSelectedItem();

                // Check slot conflict
                List<String[]> apts = readData(APPOINTMENTS_FILE);
                for (String[] a : apts) {
                    if (!isEdit || !a[0].equals(existingId)) {
                        if (a[2].equals(docId) && a[3].equals(date) && a[4].equals(time) && !a[6].equalsIgnoreCase("Cancelled")) {
                            JOptionPane.showMessageDialog(dialog, "Time slot already booked for Doctor " + docId + " at " + time + " on " + date, "Slot Conflict", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                String id = isEdit ? existingId : generateId(APPOINTMENTS_FILE, "APT");
                String record = String.join("|", id, patId, docId, date, time, reason, status);

                if (isEdit) {
                    updateRecord(APPOINTMENTS_FILE, existingId, record);
                } else {
                    appendRecord(APPOINTMENTS_FILE, record);
                }
                dialog.dispose();
                refreshAppointmentTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // --- LABORATORY MANAGEMENT PANEL ---
    private JPanel createLaboratoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(BG_COLOR);

        JButton addBtn = new JButton("Add Lab Test");
        JButton editBtn = new JButton("Update Test / Result");
        JButton deleteBtn = new JButton("Delete Test");
        JButton refreshBtn = new JButton("Refresh");

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);

        String[] cols = {"Test ID", "Patient ID", "Patient Name", "Test Name", "Date", "Cost ($)", "Result", "Status"};
        testModel = new DefaultTableModel(cols, 0);
        testTable = new JTable(testModel);
        JScrollPane scroll = new JScrollPane(testTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        addBtn.addActionListener(e -> showAddTestDialog(null));
        editBtn.addActionListener(e -> {
            int row = testTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) testModel.getValueAt(row, 0);
                showAddTestDialog(id);
            } else {
                JOptionPane.showMessageDialog(this, "Select a test to update.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = testTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) testModel.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete Test " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    deleteRecord(TESTS_FILE, id);
                    refreshLaboratoryTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a test to delete.");
            }
        });

        refreshBtn.addActionListener(e -> refreshLaboratoryTable());

        return panel;
    }

    private void refreshLaboratoryTable() {
        testModel.setRowCount(0);
        List<String[]> data = readData(TESTS_FILE);
        for (String[] row : data) {
            testModel.addRow(row);
        }
    }

    private void showAddTestDialog(String existingId) {
        boolean isEdit = existingId != null;
        JDialog dialog = new JDialog(this, isEdit ? "Update Test" : "Add Lab Test", true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(8, 2, 5, 5));

        JComboBox<String> patCombo = new JComboBox<>();
        for (String[] p : readData(PATIENTS_FILE)) patCombo.addItem(p[0] + " - " + p[1]);

        JTextField testNameF = new JTextField();
        JTextField dateF = new JTextField(LocalDate.now().toString());
        JTextField costF = new JTextField("50.00");
        JTextField resultF = new JTextField("Pending");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "Completed", "Cancelled"});

        if (isEdit) {
            String[] existingData = findRecord(TESTS_FILE, existingId);
            if (existingData != null) {
                testNameF.setText(existingData[3]);
                dateF.setText(existingData[4]);
                costF.setText(existingData[5]);
                resultF.setText(existingData[6]);
                statusCombo.setSelectedItem(existingData[7]);
            }
        }

        dialog.add(new JLabel(" Select Patient:")); dialog.add(patCombo);
        dialog.add(new JLabel(" Test Name:")); dialog.add(testNameF);
        dialog.add(new JLabel(" Date:")); dialog.add(dateF);
        dialog.add(new JLabel(" Cost ($):")); dialog.add(costF);
        dialog.add(new JLabel(" Result:")); dialog.add(resultF);
        dialog.add(new JLabel(" Status:")); dialog.add(statusCombo);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        dialog.add(saveBtn); dialog.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            try {
                if (patCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(dialog, "No patient selected.");
                    return;
                }
                String sel = (String) patCombo.getSelectedItem();
                String patId = sel.split(" - ")[0];
                String patName = sel.split(" - ")[1];

                String id = isEdit ? existingId : generateId(TESTS_FILE, "TST");
                String record = String.join("|", id, patId, patName, testNameF.getText().trim(),
                        dateF.getText().trim(), costF.getText().trim(), resultF.getText().trim(), (String) statusCombo.getSelectedItem());

                if (isEdit) {
                    updateRecord(TESTS_FILE, existingId, record);
                } else {
                    appendRecord(TESTS_FILE, record);
                }
                dialog.dispose();
                refreshLaboratoryTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // --- REPORTS PANEL ---
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnPanel.setBackground(BG_COLOR);

        JButton docRepBtn = new JButton("Doctors Summary");
        JButton patRepBtn = new JButton("Patients Summary");
        JButton aptRepBtn = new JButton("Appointments Report");
        JButton testRepBtn = new JButton("Lab Tests Report");

        btnPanel.add(docRepBtn);
        btnPanel.add(patRepBtn);
        btnPanel.add(aptRepBtn);
        btnPanel.add(testRepBtn);

        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(reportArea);

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        docRepBtn.addActionListener(e -> generateReport(reportArea, DOCTORS_FILE, "DOCTORS SUMMARY REPORT"));
        patRepBtn.addActionListener(e -> generateReport(reportArea, PATIENTS_FILE, "PATIENTS SUMMARY REPORT"));
        aptRepBtn.addActionListener(e -> generateReport(reportArea, APPOINTMENTS_FILE, "APPOINTMENTS REPORT"));
        testRepBtn.addActionListener(e -> generateReport(reportArea, TESTS_FILE, "LABORATORY TESTS REPORT"));

        return panel;
    }

    private void generateReport(JTextArea area, String filePath, String title) {
        List<String[]> records = readData(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("=======================================================================\n");
        sb.append("                       ").append(title).append("\n");
        sb.append("=======================================================================\n");
        sb.append("Generated On: ").append(LocalDate.now()).append("\n");
        sb.append("Total Count: ").append(records.size()).append("\n");
        sb.append("-----------------------------------------------------------------------\n\n");

        for (String[] row : records) {
            sb.append(String.join(" | ", row)).append("\n");
        }

        area.setText(sb.toString());
    }

    // --- FILE I/O & UTILS ---
    private synchronized List<String[]> readData(String filePath) {
        List<String[]> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(line.split("\\|", -1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private synchronized void appendRecord(String filePath, String record) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(record);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void updateRecord(String filePath, String id, String newRecord) {
        List<String[]> records = readData(filePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String[] r : records) {
                if (r.length > 0 && r[0].equals(id)) {
                    bw.write(newRecord);
                } else {
                    bw.write(String.join("|", r));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void deleteRecord(String filePath, String id) {
        List<String[]> records = readData(filePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String[] r : records) {
                if (r.length > 0 && !r[0].equals(id)) {
                    bw.write(String.join("|", r));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] findRecord(String filePath, String id) {
        for (String[] r : readData(filePath)) {
            if (r.length > 0 && r[0].equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    private String generateId(String filePath, String prefix) {
        List<String[]> list = readData(filePath);
        int maxId = 1000;
        for (String[] r : list) {
            if (r.length > 0 && r[0].startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(r[0].substring(prefix.length()));
                    if (num > maxId) maxId = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return prefix + (maxId + 1);
    }

    private void filterTable(JTable table, DefaultTableModel model, String filePath, String query) {
        model.setRowCount(0);
        List<String[]> list = readData(filePath);
        for (String[] r : list) {
            boolean match = false;
            for (String field : r) {
                if (field.toLowerCase().contains(query.toLowerCase())) {
                    match = true;
                    break;
                }
            }
            if (match) model.addRow(r);
        }
    }

    private static class QVBox extends JPanel {
        public QVBox() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(SIDEBAR_COLOR);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new HospitalManagementSystem();
        });
    }
}
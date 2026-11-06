# Hospital Management System (Core Java 21)

A complete, advanced, fully functional Hospital Management System developed using **Core Java** and Java Swing.

## Technologies Used
- **Java 21** (Core Java)
- **Java Swing & AWT** (GUI Interface)
- **Java Collections Framework** (`ArrayList`, `List`)
- **Java File I/O** (`BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`)
- **Pipe-Delimited Safe File Persistence** (`data/*.txt`)

---

## Default Admin Credentials
- **Username:** `admin`
- **Password:** `admin123`

---

## Directory Structure
```
HospitalManagementSystem/
│
├── src/
│   └── HospitalManagementSystem.java
│
├── data/
│   ├── doctors.txt
│   ├── patients.txt
│   ├── appointments.txt
│   └── tests.txt
│
└── README.md
```

---

## Features
1. **Admin Authentication**: Secure login & logout.
2. **Dashboard**: Metrics card display (Total Doctors, Patients, Appointments, Lab Tests, Today's Appointments) + Live Activity Log.
3. **Doctor Management**: Add, View, Edit, Delete, and Search Doctors with auto-generated `DOCxxxx` IDs.
4. **Patient Management**: Complete record keeping, medical history tracking, auto-generated `PATxxxx` IDs.
5. **Appointment Management**: Slot booking with **duplicate doctor time-slot conflict prevention**, status tracking (Scheduled, Completed, Cancelled).
6. **Laboratory Management**: Track test requests, record test results, costs, and status.
7. **Reports**: Detailed breakdown and summary text reports for doctors, patients, appointments, and laboratory procedures.

#Run the Project

javac -d bin src/HospitalManagementSystem.java
j
ava -cp bin HospitalManagementSystem


🏗️ Future Enhancements
🔹 Graphical User Interface (GUI) using JavaFX/Swing.
🔹 Database Integration with MySQL or PostgreSQL.
🔹 User Authentication for secure access.
🔹 Doctor Availability System to avoid overlapping appointments.

🏆 Why Use This Project?
✅ Simple and Effective: Console-based system for quick access.
✅ Easy to Modify: Readable Java code structure.
✅ Persistent Data Storage: Keeps data across sessions.

---


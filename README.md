# 🏥 Hospital Management System 
A complete desktop application built using Core Java 21 and Java Swing to manage hospital operations efficiently. It features a modern graphical interface with secure admin authentication, a real-time analytics dashboard displaying key metrics and activity logs, and dedicated management modules for doctors, patients, appointments (with automated time-slot conflict prevention), and laboratory tests. All records are automatically initialized and permanently stored using local text files (data/*.txt), ensuring complete data persistence across application restarts without relying on external databases.

## 🚀 Features
1. **Admin Authentication**: Secure login & logout.
2. **Dashboard**: Metrics card display (Total Doctors, Patients, Appointments, Lab Tests, Today's Appointments) + Live Activity Log.
3. **Doctor Management**: Add, View, Edit, Delete, and Search Doctors with auto-generated `DOCxxxx` IDs.
4. **Patient Management**: Complete record keeping, medical history tracking, auto-generated `PATxxxx` IDs.
5. **Appointment Management**: Slot booking with **duplicate doctor time-slot conflict prevention**, status tracking (Scheduled, Completed, Cancelled).
6. **Laboratory Management**: Track test requests, record test results, costs, and status.
7. **Reports**: Detailed breakdown and summary text reports for doctors, patients, appointments, and laboratory procedures.
      
## 🛠️ Technologies Used
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

## 📂 Directory Structure
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
## 📥Clone the Repository
https://github.com/prodduturukullayappa/Hospital-Management-System.git
cd HospitalManagementSystem

## ▶️Run the Project
javac -d bin src/HospitalManagementSystem.java
java -cp bin HospitalManagementSystem

## 📜How the System Works
LoginLog in securely with the username admin and password admin123.DashboardSee a quick overview of total doctors, patients, appointments, and lab tests.
Management SectionsDoctors: Add, search, update, or delete doctor profiles.   
Patients: Register new patients and keep track of their medical history.   
Appointments: Book and manage visits while avoiding double-booking doctor time slots.   
Laboratory: Create lab tests, track costs, and update results.  
Reports & DataView summary reports for all records.All data is saved automatically in local text files so nothing is lost when you close the app.

## 🏗️ Future Enhancements
🔹 Graphical User Interface (GUI) using JavaFX/Swing.
🔹 Database Integration with MySQL or PostgreSQL.
🔹 User Authentication for secure access.
🔹 Doctor Availability System to avoid overlapping appointments.

## 🏆 Why Use This Project?
✅ Simple and Effective: Console-based system for quick access.
✅ Easy to Modify: Readable Java code structure.
✅ Persistent Data Storage: Keeps data across sessions.

---
## 📞 Contact
For any issues or suggestions, feel free to reach out:
📧 Email: prodduturukullayappa@gmail.com


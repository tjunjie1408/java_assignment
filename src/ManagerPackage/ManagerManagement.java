package ManagerPackage;

import UserPackage.*;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class ManagerManagement {
    private HashMap<String, Manager> managers;
    private List<UserActivity> activityLog;
    private static final String MANAGERS_FILE = "managers.txt";
    private static final String ACTIVITY_LOG = "manager_activity.log";

    public ManagerManagement() {
        managers = new HashMap<>();
        activityLog = new ArrayList<>();
        loadManagers();
    }

    // CRUD Operations
    public boolean addManager(Manager manager) {
        if (managers.containsKey(manager.getUsername())) {
            return false;
        }
        managers.put(manager.getUsername(), manager);
        logActivity(manager.getUsername(), "ADD", "New manager added");
        saveManagers();
        return true;
    }

    public Manager getManager(String username) {
        return managers.get(username);
    }

    public boolean updateManager(String username, Manager updatedManager) {
        if (!managers.containsKey(username)) {
            return false;
        }
        managers.put(username, updatedManager);
        logActivity(username, "UPDATE", "Manager information updated");
        saveManagers();
        return true;
    }

    public boolean deleteManager(String username) {
        if (!managers.containsKey(username)) {
            return false;
        }
        managers.remove(username);
        logActivity(username, "DELETE", "Manager deleted");
        saveManagers();
        return true;
    }

    // Profile Management
    public boolean updateProfile(String username, String phoneNumber, String officeLocation) {
        Manager manager = managers.get(username);
        if (manager == null) {
            return false;
        }
        manager.setPhoneNumber(phoneNumber);
        manager.setOfficeLocation(officeLocation);
        logActivity(username, "PROFILE_UPDATE", "Profile information updated");
        saveManagers();
        return true;
    }

    // Salesman Management
    public boolean assignSalesman(String managerUsername, String salesmanId) {
        Manager manager = managers.get(managerUsername);
        if (manager == null) {
            return false;
        }
        manager.addManagedSalesman(salesmanId);
        logActivity(managerUsername, "ASSIGN_SALESMAN", "Assigned salesman: " + salesmanId);
        saveManagers();
        return true;
    }

    public boolean removeSalesman(String managerUsername, String salesmanId) {
        Manager manager = managers.get(managerUsername);
        if (manager == null) {
            return false;
        }
        manager.getManagedSalesmen().remove(salesmanId);
        logActivity(managerUsername, "REMOVE_SALESMAN", "Removed salesman: " + salesmanId);
        saveManagers();
        return true;
    }

    // Approval Management
    public boolean addApproval(String username, String approvalId) {
        Manager manager = managers.get(username);
        if (manager == null) {
            return false;
        }
        manager.addApproval(approvalId);
        logActivity(username, "APPROVAL_ADDED", "New approval added: " + approvalId);
        saveManagers();
        return true;
    }

    // File Operations
    private void loadManagers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MANAGERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Manager manager = Manager.fromCSV(line);
                managers.put(manager.getUsername(), manager);
            }
        } catch (IOException e) {
            System.out.println("Error loading managers: " + e.getMessage());
        }
    }

    private void saveManagers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MANAGERS_FILE))) {
            for (Manager manager : managers.values()) {
                writer.write(manager.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving managers: " + e.getMessage());
        }
    }

    // Activity Logging
    private void logActivity(String username, String action, String details) {
        UserActivity activity = new UserActivity(username, action, details);
        activityLog.add(activity);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACTIVITY_LOG, true))) {
            writer.write(activity.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging activity: " + e.getMessage());
        }
    }

    // Search Operations
    public List<Manager> searchByDepartment(String department) {
        return managers.values().stream()
                .filter(m -> m.getDepartment().equals(department))
                .collect(Collectors.toList());
    }

    public List<Manager> searchByPosition(String position) {
        return managers.values().stream()
                .filter(m -> m.getPosition().equals(position))
                .collect(Collectors.toList());
    }

    public List<Manager> searchByAccessLevel(String accessLevel) {
        return managers.values().stream()
                .filter(m -> m.getAccessLevel().equals(accessLevel))
                .collect(Collectors.toList());
    }

    // Activity History
    public List<UserActivity> getActivityHistory(String username) {
        return activityLog.stream()
                .filter(a -> a.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // Import/Export
    public boolean importManagers(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Manager manager = Manager.fromCSV(line);
                managers.put(manager.getUsername(), manager);
            }
            saveManagers();
            return true;
        } catch (IOException e) {
            System.out.println("Error importing managers: " + e.getMessage());
            return false;
        }
    }

    public boolean exportManagers(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Manager manager : managers.values()) {
                writer.write(manager.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error exporting managers: " + e.getMessage());
            return false;
        }
    }
}
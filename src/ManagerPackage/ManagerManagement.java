package ManagerPackage;

import UserPackage.*;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class ManagerManagement {
    private List<Manager> managers;
    private List<UserActivity> activityLog;
    private static final String MANAGERS_FILE = "managers.txt";
    private static final String ACTIVITY_LOG = "manager_activity.log";
    private static ManagerManagement instance;

    public ManagerManagement() {
        managers = new ArrayList<>();
        activityLog = new ArrayList<>();
        loadManagers();
    }

    public static ManagerManagement getInstance() {
        if (instance == null) {
            instance = new ManagerManagement();
        }
        return instance;
    }

    // CRUD Operations
    public boolean addManager(Manager manager) {
        for (Manager m : managers) {
            if (m.getUsername().equals(manager.getUsername())) {
                return false;
            }
        }

        int maxId = 0;
        for (Manager m : managers) {
            try {
                int currentId = Integer.parseInt(m.getId());
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                // Ignore non-numeric IDs
            }
        }
        String newId = String.valueOf(maxId + 1);
        manager.setId(newId);

        managers.add(manager);
        logActivity(manager.getUsername(), "ADD", "New manager added with id: " + newId);
        saveManagers();
        return true;
    }

    public Manager getManager(String username) {
        for (Manager m : managers) {
            if (m.getUsername().equals(username)) {
                return m;
            }
        }
        return null;
    }

    public boolean updateManager(String username, Manager updatedManager) {
        for (int i = 0; i < managers.size(); i++) {
            if (managers.get(i).getUsername().equals(username)) {
                managers.set(i, updatedManager);
                logActivity(username, "UPDATE", "Manager information updated");
                saveManagers();
                return true;
            }
        }
        return false;
    }

    public boolean deleteManager(String username) {
        for (int i = 0; i < managers.size(); i++) {
            if (managers.get(i).getUsername().equals(username)) {
                managers.remove(i);
                logActivity(username, "DELETE", "Manager deleted");
                saveManagers();
                return true;
            }
        }
        return false;
    }

    // Profile Management
    public boolean updateProfile(String username, String phoneNumber, String officeLocation) {
        Manager manager = getManager(username);
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
        Manager manager = getManager(managerUsername);
        if (manager == null) {
            return false;
        }
        manager.addManagedSalesman(salesmanId);
        logActivity(managerUsername, "ASSIGN_SALESMAN", "Assigned salesman: " + salesmanId);
        saveManagers();
        return true;
    }

    public boolean removeSalesman(String managerUsername, String salesmanId) {
        Manager manager = getManager(managerUsername);
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
        Manager manager = getManager(username);
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
                managers.add(manager);
            }
        } catch (IOException e) {
            System.out.println("Error loading managers: " + e.getMessage());
        }
    }

    private void saveManagers() {
        System.out.println("Saving managers to " + MANAGERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MANAGERS_FILE))) {
            for (Manager manager : managers) {
                System.out.println("Writing manager: " + manager.toCSV());
                writer.write(manager.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving managers: " + e.getMessage());
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
        List<Manager> result = new ArrayList<>();
        for (Manager m : managers) {
            if (m.getDepartment().equals(department)) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Manager> searchByPosition(String position) {
        List<Manager> result = new ArrayList<>();
        for (Manager m : managers) {
            if (m.getPosition().equals(position)) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Manager> searchByAccessLevel(String accessLevel) {
        List<Manager> result = new ArrayList<>();
        for (Manager m : managers) {
            if (m.getAccessLevel().equals(accessLevel)) {
                result.add(m);
            }
        }
        return result;
    }

    // Activity History
    public List<UserActivity> getActivityHistory(String username) {
        List<UserActivity> result = new ArrayList<>();
        for (UserActivity a : activityLog) {
            if (a.getUsername().equals(username)) {
                result.add(a);
            }
        }
        return result;
    }

    // Import/Export
    public boolean importManagers(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Manager manager = Manager.fromCSV(line);
                managers.add(manager);
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
            for (Manager manager : managers) {
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
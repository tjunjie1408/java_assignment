package ManagerPackage;

import UserPackage.*;
import java.util.*;
import java.io.*;

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

    public Manager findById(String id) {
        for (Manager m : managers) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public boolean updateManager(Manager updatedManager) {
        for (int i = 0; i < managers.size(); i++) {
            if (managers.get(i).getId().equals(updatedManager.getId())) {
                managers.set(i, updatedManager);
                logActivity(updatedManager.getUsername(), "UPDATE", "Manager info updated");
                saveManagers();
                return true;
            }
        }
        return false;
    }

    public boolean deleteManager(String id) {
        Iterator<Manager> it = managers.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(id)) {
                it.remove();
                saveManagers();
                logActivity(id, "DELETE", "Manager account deleted");
                return true;
            }
        }
        return false;
    }
    // File Operations
    private void loadManagers() {
        File f = new File(MANAGERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                Manager m = Manager.fromCSV(line);
                if (m != null) managers.add(m);
            }
        } catch (IOException e) {
            System.err.println("Error loading managers: " + e.getMessage());
        }
    }

    private void saveManagers() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(MANAGERS_FILE))) {
            for (Manager m : managers) {
                w.write(m.toCSV());
                w.newLine();
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
}
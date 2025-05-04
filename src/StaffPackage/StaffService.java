package StaffPackage;

import java.util.*;

public class StaffService {
    private final Map<String, Staff> staffMap = new HashMap<>();

    public boolean addStaff(Staff staff) {
        if (staff == null || staffMap.containsKey(staff.getStaffId())) {
            return false;
        }
        staffMap.put(staff.getStaffId(), staff);
        return true;
    }

    public boolean deleteStaff(String id) {
        return staffMap.remove(id) != null;
    }

    public Staff findStaffById(String id) {
        return staffMap.get(id);
    }

    public boolean updateStaff(Staff updated) {
        if (updated == null || !staffMap.containsKey(updated.getStaffId())) {
            return false;
        }
        staffMap.put(updated.getStaffId(), updated);
        return true;
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffMap.values());
    }
}

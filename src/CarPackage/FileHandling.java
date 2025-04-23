package CarPackage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandling {
    private String filePath;

    public FileHandling(String filePath) {
        this.filePath = filePath;
    }

    public boolean saveCarsToFile(List<Car> cars){
        try(BufferedWriter w = new BufferedWriter(new FileWriter(filePath))){
            w.write("# Car Inventory Data Format: ID,Make,Model,Year,Price,Colour,Status");
            w.newLine();

            for (Car car : cars) {
                w.write(car.toFileString());
                w.newLine();
            }
            return true;
        } catch(IOException e){
            System.err.println("Error saving cars to file: " + e.getMessage());
            return false;
        }
    }

    public List<Car> loadCarsFile() throws IOException{
        List<Car> cars = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            try{
                file.createNewFile();
                try(BufferedWriter w = new BufferedWriter(new FileWriter(filePath))){
                    w.write("# Car Inventory Data Format: ID,Make,Model,Year,Price,Colour,Status");
                    w.newLine();
                }
            } catch(IOException e){
                System.err.println("Error creating new file: " + e.getMessage());
            }
            return cars;
        }
        try(BufferedReader r = new BufferedReader(new FileReader(filePath))){
            String line;
            boolean firstLine = true;

            while((line = r.readLine()) != null){
                if(firstLine || line.trim().isEmpty() || line.startsWith("#")){
                    firstLine = false;
                    continue;
                }

                Car car = Car.fromFileString(line);
                if(car != null){
                    cars.add(car);
                }
            }
        }catch(IOException e){
            System.err.println("Error loading cars from file: " + e.getMessage());
        }
        return cars;
    }
    public boolean fileExists(){
        File file = new File(filePath);
        return file.exists();
    }

    public boolean createBackup(){
        if(!fileExists()){
            return false;
        }
        String backupFilePath = filePath + ".bak";
        try(BufferedReader r = new BufferedReader(new FileReader(filePath));
            BufferedWriter w = new BufferedWriter(new FileWriter(backupFilePath))){
                String line;
                while((line = r.readLine()) != null){
                    w.write(line);
                    w.newLine();
                }
                return true;
            }catch(IOException e){
                System.err.println("Error creating backup: " + e.getMessage());
                return false;
            }
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getFilePath(){
        return filePath;
    }
}

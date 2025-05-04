package CarPackage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarFileManage {
    private static final Logger logger = Logger.getLogger(CarFileManage.class.getName());
    private static final String HEADER = "# ID,Model,Year,Price,Color,Status";
    private Path filePath;

    public CarFileManage(String path) {
        this.filePath = Paths.get(path);
    }

    public List<Car> loadCarsFile() {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                Files.write(filePath, List.of(HEADER), StandardCharsets.UTF_8);
            }
            return Files.lines(filePath, StandardCharsets.UTF_8)
                    .filter(l -> !l.isBlank() && !l.startsWith("#"))
                    .map(Car::fromFileString)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load car data", e);
            throw new UncheckedIOException(e);
        }
    }

    public void saveCarsToFile(List<Car> cars) {
        Path tmp = filePath.resolveSibling(filePath.getFileName() + ".tmp");
        try {
            try (var writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write(HEADER);
                writer.newLine();
                for (Car car : cars) {
                    writer.write(car.toFileString());
                    writer.newLine();
                }
            }
            Files.move(tmp, filePath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failure to save car data", e);
            throw new UncheckedIOException(e);
        }
    }

    public boolean createBackup() {
        Path backup = filePath.resolveSibling(filePath.getFileName() + ".bak");
        try {
            Files.copy(filePath, backup, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Backup file failure", e);
            return false;
        }
    }

    public String getFilePath() {
        return filePath.toString();
    }


    public void setFilePath(String filePath) {
        this.filePath = Path.of(filePath);
    }
}


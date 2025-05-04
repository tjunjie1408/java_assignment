package StaffPackage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * StaffFileManager：管理 Manager 和 Salesman 对象的文件持久化
 */
public class StaffFileManage {
    private static final Logger logger = Logger.getLogger(StaffFileManage.class.getName());
    private static final String HEADER = "Name,Age,Gender,Phone,Email,StaffId";

    private Path managerFile;
    private Path salesmanFile;

    public StaffFileManage(String managerFilePath, String salesmanFilePath) {
        this.managerFile  = Paths.get(Objects.requireNonNull(managerFilePath));
        this.salesmanFile = Paths.get(Objects.requireNonNull(salesmanFilePath));
    }

    /** 加载所有管理人员 */
    public List<Manager> loadManagers() {
        return loadAll(managerFile, Manager::fromCsv);
    }

    /** 保存所有管理人员 */
    public void saveManagers(List<Manager> list) {
        List<String> lines = list.stream()
                .map(Manager::toCsv)
                .collect(Collectors.toList());
        saveAll(managerFile, lines);
    }

    /** 加载所有销售员 */
    public List<Salesman> loadSalesmen() {
        return loadAll(salesmanFile, Salesman::fromCsv);
    }

    /** 保存所有销售员 */
    public void saveSalesmen(List<Salesman> list) {
        List<String> lines = list.stream()
                .map(Salesman::toCsv)
                .collect(Collectors.toList());
        saveAll(salesmanFile, lines);
    }

    // ------ 通用加载与保存方法 ------

    private <T> List<T> loadAll(Path file, CsvMapper<T> mapper) {
        try {
            if (Files.notExists(file)) {
                Files.createFile(file);
                Files.write(file, List.of(HEADER), StandardCharsets.UTF_8);
            }
            return Files.lines(file, StandardCharsets.UTF_8)
                    .filter(l -> !l.isBlank() && !l.startsWith("#"))
                    .map(mapper::map)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load from " + file, e);
            throw new UncheckedIOException(e);
        }
    }

    private void saveAll(Path file, List<String> dataLines) {
        Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
        try {
            // 写入临时文件
            try (var writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write(HEADER);
                writer.newLine();
                for (String line : dataLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            // 原子替换
            Files.move(tmp, file,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save to " + file, e);
            throw new UncheckedIOException(e);
        }
    }

    /** 备份两个文件 */
    public boolean backupAll() {
        return backup(managerFile) & backup(salesmanFile);
    }

    private boolean backup(Path file) {
        Path bak = file.resolveSibling(file.getFileName() + ".bak");
        try {
            Files.copy(file, bak, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Backup failed for " + file, e);
            return false;
        }
    }

    /** 更改文件路径 */
    public void setManagerFile(String path)  { this.managerFile  = Paths.get(path); }
    public void setSalesmanFile(String path) { this.salesmanFile = Paths.get(path); }
    public String getManagerFile()  { return managerFile.toString(); }
    public String getSalesmanFile() { return salesmanFile.toString(); }

    @FunctionalInterface
    private interface CsvMapper<T> {
        T map(String csv);
    }
}

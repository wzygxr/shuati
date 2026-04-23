import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class MergeFiles {
    public static void main(String[] args) {
        String outputPath = "e:/代码/class187/all_content.txt";
        String directoryPath = "e:/代码/class187";

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {

            Path dirPath = Paths.get(directoryPath);

            // 获取所有.md和.java文件
            try (Stream<Path> paths = Files.walk(dirPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".md") || path.toString().endsWith(".java"))
                    .filter(path -> {
                        String fileName = path.getFileName().toString();
                        return !fileName.equals("all_content.txt") &&
                                !fileName.equals("merged_file.txt") &&
                                !fileName.equals("merged_all.txt") &&
                                !fileName.equals("class186+这届的数据结构预算法.txt") &&
                                !fileName.equals("MergeFiles.java") &&
                                !fileName.equals("MergeFiles.ps1") &&
                                !fileName.equals("CombineFiles.java");
                    })) { // 避免包含输出文件本身

                paths.sorted().forEach(filePath -> {
                    try {
                        // 写入文件名标识
                        writer.write("# File: " + filePath.getFileName().toString());
                        writer.newLine();
                        writer.newLine();

                        // 读取并写入文件内容
                        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                        for (String line : lines) {
                            writer.write(line);
                            writer.newLine();
                        }

                        // 添加分隔符
                        writer.newLine();
                        writer.write("=".repeat(80));
                        writer.newLine();
                        writer.newLine();

                    } catch (IOException e) {
                        System.err.println("Error processing file: " + filePath);
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            System.err.println("Error creating output file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("File merging completed.");
    }
}
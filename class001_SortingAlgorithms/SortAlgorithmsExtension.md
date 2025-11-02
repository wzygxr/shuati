# 排序算法扩展题目与解法

本文档补充了排序算法库中的更多相关题目、解法和优化技巧，帮助深入理解排序算法的应用场景和实现细节。

## 目录

1. [高级排序算法应用](#1-高级排序算法应用)
2. [特殊排序算法](#2-特殊排序算法)
3. [排序算法优化技巧](#3-排序算法优化技巧)
4. [排序算法在实际场景中的应用](#4-排序算法在实际场景中的应用)
5. [经典排序题目详解](#5-经典排序题目详解)

## 1. 高级排序算法应用

### 1.1 外部排序

**题目**: 如何对一个10GB的文件进行排序，但内存只有2GB？

**解法**:
1. **分割阶段**: 将大文件分成多个小块，每个小块大小为内存可容纳的最大值（如1.5GB）
2. **排序阶段**: 对每个小块在内存中进行排序（如使用快速排序）
3. **合并阶段**: 使用多路归并算法合并所有排序好的小块

**代码框架**:
```java
// 分割大文件为小块
public static void splitFile(String inputFile, int chunkSize) {
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
        String line;
        int fileCount = 0;
        List<String> chunk = new ArrayList<>();
        
        while ((line = reader.readLine()) != null) {
            chunk.add(line);
            
            if (chunk.size() >= chunkSize) {
                sortAndSaveChunk(chunk, "chunk_" + fileCount + ".txt");
                chunk.clear();
                fileCount++;
            }
        }
        
        if (!chunk.isEmpty()) {
            sortAndSaveChunk(chunk, "chunk_" + fileCount + ".txt");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// 排序并保存小块
private static void sortAndSaveChunk(List<String> chunk, String outputFile) {
    // 对小块进行排序
    Collections.sort(chunk);
    
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        for (String line : chunk) {
            writer.write(line);
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// 多路归并
public static void mergeChunks(String[] chunkFiles, String outputFile) {
    // 使用优先队列进行多路归并
    PriorityQueue<ChunkReader> pq = new PriorityQueue<>();
    
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        // 初始化每个块的读取器
        for (String file : chunkFiles) {
            ChunkReader reader = new ChunkReader(file);
            if (reader.hasNext()) {
                pq.add(reader);
            }
        }
        
        // 多路归并
        while (!pq.isEmpty()) {
            ChunkReader reader = pq.poll();
            writer.write(reader.current());
            writer.newLine();
            
            if (reader.hasNext()) {
                pq.add(reader);
            } else {
                reader.close();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
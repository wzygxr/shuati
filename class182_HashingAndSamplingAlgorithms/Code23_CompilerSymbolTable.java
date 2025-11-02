package class107_HashingAndSamplingAlgorithms;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 编译器符号表实现 - 使用完美哈希技术
 * 
 * 应用场景：编译器中的符号表管理、静态字典、关键字查找
 * 
 * 算法原理：
 * 1. 使用两级哈希结构实现完美哈希
 * 2. 第一级哈希将关键字分组到桶中
 * 3. 为每个桶构建无冲突的二级哈希表
 * 4. 保证O(1)时间复杂度的查找操作
 * 
 * 时间复杂度：
 * - 构建：O(n) 平均情况
 * - 查找：O(1) 最坏情况
 * 
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 适用于静态数据集（构建后不修改）
 * 2. 内存效率优化
 * 3. 快速查找性能
 * 4. 异常处理和边界情况
 */
public class Code23_CompilerSymbolTable {
    
    /**
     * 符号表条目
     */
    public static class SymbolEntry {
        private final String name;      // 符号名称
        private final String type;      // 符号类型
        private final int scope;        // 作用域
        private final int lineNumber;   // 行号
        
        public SymbolEntry(String name, String type, int scope, int lineNumber) {
            this.name = name;
            this.type = type;
            this.scope = scope;
            this.lineNumber = lineNumber;
        }
        
        // Getters
        public String getName() { return name; }
        public String getType() { return type; }
        public int getScope() { return scope; }
        public int getLineNumber() { return lineNumber; }
        
        @Override
        public String toString() {
            return String.format("SymbolEntry{name='%s', type='%s', scope=%d, line=%d}", 
                               name, type, scope, lineNumber);
        }
    }
    
    /**
     * 完美哈希符号表实现
     */
    public static class PerfectHashSymbolTable {
        private final int firstLevelSize;                           // 一级哈希表大小
        private final List<SymbolEntry>[] secondLevelTables;       // 二级哈希表数组
        private final int[] secondLevelSizes;                      // 每个二级表的大小
        private final int[] hashParams;                            // 每个二级表的哈希参数
        private final List<String> allKeys;                        // 所有键的列表
        
        /**
         * 构造函数：根据关键字集合构建完美哈希表
         * 
         * @param symbols 符号条目集合
         */
        @SuppressWarnings("unchecked")
        public PerfectHashSymbolTable(Collection<SymbolEntry> symbols) {
            // 收集所有关键字
            this.allKeys = new ArrayList<>();
            Map<String, SymbolEntry> symbolMap = new HashMap<>();
            for (SymbolEntry symbol : symbols) {
                allKeys.add(symbol.getName());
                symbolMap.put(symbol.getName(), symbol);
            }
            
            int n = allKeys.size();
            this.firstLevelSize = Math.max(1, (int) Math.ceil(n * 1.5)); // 一级表大小
            this.secondLevelTables = new List[firstLevelSize];
            this.secondLevelSizes = new int[firstLevelSize];
            this.hashParams = new int[firstLevelSize];
            
            // 初始化二级表
            for (int i = 0; i < firstLevelSize; i++) {
                secondLevelTables[i] = new ArrayList<>();
            }
            
            // 第一级分组
            Map<Integer, List<String>> groups = new HashMap<>();
            for (String key : allKeys) {
                int groupIndex = Math.abs(key.hashCode()) % firstLevelSize;
                groups.computeIfAbsent(groupIndex, k -> new ArrayList<>()).add(key);
            }
            
            // 为每个组构建无冲突的二级哈希表
            for (Map.Entry<Integer, List<String>> groupEntry : groups.entrySet()) {
                int groupIndex = groupEntry.getKey();
                List<String> groupKeys = groupEntry.getValue();
                
                if (groupKeys.isEmpty()) continue;
                
                // 计算二级表大小（平方大小以保证高概率无冲突）
                int groupSize = groupKeys.size();
                int secondLevelSize = groupSize * groupSize;
                secondLevelSizes[groupIndex] = secondLevelSize;
                
                // 寻找无冲突的哈希函数
                int hashParam = findPerfectHashFunction(groupKeys, secondLevelSize);
                hashParams[groupIndex] = hashParam;
                
                // 初始化二级表
                SymbolEntry[] tempTable = new SymbolEntry[secondLevelSize];
                
                // 填充二级表
                for (String key : groupKeys) {
                    int hash = perfectHash(key, hashParam, secondLevelSize);
                    tempTable[hash] = symbolMap.get(key);
                }
                
                // 转换为列表存储
                for (int i = 0; i < secondLevelSize; i++) {
                    if (tempTable[i] != null) {
                        secondLevelTables[groupIndex].add(tempTable[i]);
                    }
                }
            }
        }
        
        /**
         * 查找无冲突的哈希函数参数
         */
        private int findPerfectHashFunction(List<String> keys, int tableSize) {
            if (keys.size() <= 1) return 0;
            
            ThreadLocalRandom random = ThreadLocalRandom.current();
            
            for (int attempt = 0; attempt < 1000; attempt++) {
                int param = random.nextInt(1, Integer.MAX_VALUE);
                Set<Integer> hashes = new HashSet<>();
                
                boolean collision = false;
                for (String key : keys) {
                    int hash = perfectHash(key, param, tableSize);
                    if (hashes.contains(hash)) {
                        collision = true;
                        break;
                    }
                    hashes.add(hash);
                }
                
                if (!collision) {
                    return param; // 找到无冲突的哈希函数
                }
            }
            
            // 如果找不到完美的哈希函数，使用简单的方法
            return 1;
        }
        
        /**
         * 完美哈希函数
         */
        private int perfectHash(String key, int param, int tableSize) {
            int hash = key.hashCode();
            hash = (hash ^ param) * 0x9e3779b9; // 乘以黄金比例
            return Math.abs(hash) % tableSize;
        }
        
        /**
         * 查找符号
         * 
         * @param symbolName 符号名称
         * @return 符号条目，如果未找到返回null
         */
        public SymbolEntry lookup(String symbolName) {
            if (symbolName == null) return null;
            
            int firstHash = Math.abs(symbolName.hashCode()) % firstLevelSize;
            int secondLevelSize = secondLevelSizes[firstHash];
            int hashParam = hashParams[firstHash];
            
            if (secondLevelSize == 0) return null;
            
            int secondHash = perfectHash(symbolName, hashParam, secondLevelSize);
            
            List<SymbolEntry> secondLevelTable = secondLevelTables[firstHash];
            if (secondHash < secondLevelTable.size()) {
                return secondLevelTable.get(secondHash);
            }
            
            return null;
        }
        
        /**
         * 获取所有符号名称
         */
        public List<String> getAllSymbolNames() {
            return new ArrayList<>(allKeys);
        }
        
        /**
         * 获取符号表大小
         */
        public int size() {
            return allKeys.size();
        }
    }
    
    /**
     * 优化版本：使用更高效的完美哈希实现
     */
    public static class OptimizedPerfectHashSymbolTable {
        private final int firstLevelSize;
        private final SymbolEntry[][] secondLevelTables;  // 使用数组提高访问速度
        private final int[] secondLevelSizes;
        private final int[] hashParams;
        
        @SuppressWarnings("unchecked")
        public OptimizedPerfectHashSymbolTable(Collection<SymbolEntry> symbols) {
            Map<String, SymbolEntry> symbolMap = new HashMap<>();
            List<String> keys = new ArrayList<>();
            
            for (SymbolEntry symbol : symbols) {
                symbolMap.put(symbol.getName(), symbol);
                keys.add(symbol.getName());
            }
            
            int n = keys.size();
            this.firstLevelSize = Math.max(1, (int) Math.ceil(n * 2.0)); // 增大一级表减少冲突
            this.secondLevelTables = new SymbolEntry[firstLevelSize][];
            this.secondLevelSizes = new int[firstLevelSize];
            this.hashParams = new int[firstLevelSize];
            
            // 分组
            Map<Integer, List<String>> groups = new HashMap<>();
            for (String key : keys) {
                int groupIndex = Math.abs(key.hashCode()) % firstLevelSize;
                groups.computeIfAbsent(groupIndex, k -> new ArrayList<>()).add(key);
            }
            
            // 为每组构建二级表
            for (Map.Entry<Integer, List<String>> groupEntry : groups.entrySet()) {
                int groupIndex = groupEntry.getKey();
                List<String> groupKeys = groupEntry.getValue();
                
                if (groupKeys.isEmpty()) continue;
                
                // 计算二级表大小
                int groupSize = groupKeys.size();
                int secondLevelSize = groupSize <= 2 ? groupSize * 2 : groupSize * groupSize;
                
                // 寻找无冲突哈希函数
                int hashParam = findHashFunction(groupKeys, secondLevelSize);
                hashParams[groupIndex] = hashParam;
                secondLevelSizes[groupIndex] = secondLevelSize;
                
                // 创建二级表
                SymbolEntry[] table = new SymbolEntry[secondLevelSize];
                for (String key : groupKeys) {
                    int index = hash(key, hashParam, secondLevelSize);
                    table[index] = symbolMap.get(key);
                }
                secondLevelTables[groupIndex] = table;
            }
        }
        
        private int findHashFunction(List<String> keys, int tableSize) {
            if (keys.size() <= 1) return 0;
            
            ThreadLocalRandom random = ThreadLocalRandom.current();
            
            for (int attempt = 0; attempt < 1000; attempt++) {
                int param = random.nextInt(1, 1000000);
                boolean[] used = new boolean[tableSize];
                boolean collision = false;
                
                for (String key : keys) {
                    int index = hash(key, param, tableSize);
                    if (used[index]) {
                        collision = true;
                        break;
                    }
                    used[index] = true;
                }
                
                if (!collision) return param;
            }
            
            return 1; // fallback
        }
        
        private int hash(String key, int param, int tableSize) {
            int hash = key.hashCode();
            hash = (hash ^ param) * 0x9e3779b9;
            return Math.abs(hash) % tableSize;
        }
        
        public SymbolEntry lookup(String symbolName) {
            if (symbolName == null) return null;
            
            int firstIndex = Math.abs(symbolName.hashCode()) % firstLevelSize;
            SymbolEntry[] secondLevelTable = secondLevelTables[firstIndex];
            
            if (secondLevelTable == null) return null;
            
            int secondLevelSize = secondLevelSizes[firstIndex];
            int hashParam = hashParams[firstIndex];
            int secondIndex = hash(symbolName, hashParam, secondLevelSize);
            
            if (secondIndex < secondLevelTable.length) {
                return secondLevelTable[secondIndex];
            }
            
            return null;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 编译器符号表（完美哈希实现） ===");
        
        // 创建测试符号
        List<SymbolEntry> symbols = createTestSymbols();
        
        // 基础版本测试
        testBasicVersion(symbols);
        
        // 优化版本测试
        testOptimizedVersion(symbols);
        
        // 性能测试
        performanceTest();
        
        // 边界情况测试
        edgeCaseTest();
    }
    
    private static List<SymbolEntry> createTestSymbols() {
        List<SymbolEntry> symbols = new ArrayList<>();
        symbols.add(new SymbolEntry("int", "keyword", 0, 1));
        symbols.add(new SymbolEntry("float", "keyword", 0, 1));
        symbols.add(new SymbolEntry("double", "keyword", 0, 1));
        symbols.add(new SymbolEntry("char", "keyword", 0, 1));
        symbols.add(new SymbolEntry("if", "keyword", 0, 2));
        symbols.add(new SymbolEntry("else", "keyword", 0, 2));
        symbols.add(new SymbolEntry("while", "keyword", 0, 3));
        symbols.add(new SymbolEntry("for", "keyword", 0, 3));
        symbols.add(new SymbolEntry("return", "keyword", 0, 4));
        symbols.add(new SymbolEntry("main", "function", 0, 5));
        symbols.add(new SymbolEntry("printf", "function", 0, 6));
        symbols.add(new SymbolEntry("scanf", "function", 0, 6));
        return symbols;
    }
    
    private static void testBasicVersion(List<SymbolEntry> symbols) {
        System.out.println("--- 基础版本测试 ---");
        PerfectHashSymbolTable symbolTable = new PerfectHashSymbolTable(symbols);
        
        // 测试查找功能
        SymbolEntry intSymbol = symbolTable.lookup("int");
        SymbolEntry mainSymbol = symbolTable.lookup("main");
        SymbolEntry nonExistent = symbolTable.lookup("nonexistent");
        
        System.out.println("查找 'int': " + intSymbol);
        System.out.println("查找 'main': " + mainSymbol);
        System.out.println("查找不存在的符号: " + nonExistent);
        
        // 测试所有符号
        System.out.println("所有符号名称: " + symbolTable.getAllSymbolNames());
        System.out.println("符号表大小: " + symbolTable.size());
        System.out.println();
    }
    
    private static void testOptimizedVersion(List<SymbolEntry> symbols) {
        System.out.println("--- 优化版本测试 ---");
        OptimizedPerfectHashSymbolTable symbolTable = new OptimizedPerfectHashSymbolTable(symbols);
        
        // 测试查找功能
        SymbolEntry intSymbol = symbolTable.lookup("int");
        SymbolEntry mainSymbol = symbolTable.lookup("main");
        SymbolEntry nonExistent = symbolTable.lookup("nonexistent");
        
        System.out.println("查找 'int': " + intSymbol);
        System.out.println("查找 'main': " + mainSymbol);
        System.out.println("查找不存在的符号: " + nonExistent);
        System.out.println();
    }
    
    private static void performanceTest() {
        System.out.println("--- 性能测试 ---");
        
        // 创建大量测试符号
        List<SymbolEntry> largeSymbols = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeSymbols.add(new SymbolEntry("symbol" + i, "variable", 0, i));
        }
        
        // 测试基础版本
        long startTime = System.currentTimeMillis();
        PerfectHashSymbolTable basicTable = new PerfectHashSymbolTable(largeSymbols);
        long buildTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            basicTable.lookup("symbol" + (i % 1000));
        }
        long lookupTime = System.currentTimeMillis() - startTime;
        
        System.out.println("基础版本:");
        System.out.println("  构建时间: " + buildTime + "ms");
        System.out.println("  10000次查找时间: " + lookupTime + "ms");
        System.out.println("  平均每次查找: " + (lookupTime / 10000.0) + "ms");
        
        // 测试优化版本
        startTime = System.currentTimeMillis();
        OptimizedPerfectHashSymbolTable optimizedTable = new OptimizedPerfectHashSymbolTable(largeSymbols);
        long optimizedBuildTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            optimizedTable.lookup("symbol" + (i % 1000));
        }
        long optimizedLookupTime = System.currentTimeMillis() - startTime;
        
        System.out.println("优化版本:");
        System.out.println("  构建时间: " + optimizedBuildTime + "ms");
        System.out.println("  10000次查找时间: " + optimizedLookupTime + "ms");
        System.out.println("  平均每次查找: " + (optimizedLookupTime / 10000.0) + "ms");
        System.out.println();
    }
    
    private static void edgeCaseTest() {
        System.out.println("--- 边界情况测试 ---");
        
        // 测试空符号表
        PerfectHashSymbolTable emptyTable = new PerfectHashSymbolTable(new ArrayList<>());
        System.out.println("空符号表大小: " + emptyTable.size());
        System.out.println("空符号表查找: " + emptyTable.lookup("test"));
        
        // 测试单个符号
        List<SymbolEntry> singleSymbol = new ArrayList<>();
        singleSymbol.add(new SymbolEntry("single", "variable", 0, 1));
        PerfectHashSymbolTable singleTable = new PerfectHashSymbolTable(singleSymbol);
        System.out.println("单符号表查找: " + singleTable.lookup("single"));
        
        // 测试null查找
        System.out.println("null查找: " + singleTable.lookup(null));
        
        System.out.println();
    }
}
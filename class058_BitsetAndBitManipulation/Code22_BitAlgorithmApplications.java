package class032;

import java.util.*;

/**
 * 位算法实际应用和工程场景
 * 题目来源: 实际工程问题，系统设计，性能优化
 * 包含位算法在真实场景中的应用案例
 * 
 * 解题思路:
 * 方法1: 位运算优化数据库查询
 * 方法2: 位压缩存储大规模数据
 * 方法3: 位操作加速图像处理
 * 方法4: 位掩码实现权限系统
 * 
 * 时间复杂度分析:
 * 方法1: O(1) - 常数时间查询
 * 方法2: O(n) - 线性处理
 * 方法3: O(w*h) - 图像尺寸相关
 * 方法4: O(1) - 权限检查
 * 
 * 空间复杂度分析:
 * 方法1: O(1) - 原地操作
 * 方法2: O(n/32) - 压缩存储
 * 方法3: O(w*h) - 图像存储
 * 方法4: O(1) - 常数空间
 * 
 * 工程化考量:
 * 1. 实际性能: 真实环境测试
 * 2. 可维护性: 清晰的接口设计
 * 3. 扩展性: 支持功能扩展
 * 4. 兼容性: 跨平台兼容
 */

public class Code22_BitAlgorithmApplications {
    
    /**
     * 应用1: 权限管理系统
     * 使用位掩码实现细粒度权限控制
     * 实际应用: 用户权限管理，角色权限控制
     */
    public static class PermissionSystem {
        // 权限定义
        public static final int READ = 1 << 0;    // 0001
        public static final int WRITE = 1 << 1;   // 0010
        public static final int EXECUTE = 1 << 2; // 0100
        public static final int DELETE = 1 << 3;  // 1000
        
        // 组合权限
        public static final int READ_WRITE = READ | WRITE;
        public static final int FULL_ACCESS = READ | WRITE | EXECUTE | DELETE;
        
        private int userPermissions;
        
        public PermissionSystem(int initialPermissions) {
            this.userPermissions = initialPermissions;
        }
        
        /**
         * 检查是否具有某个权限
         */
        public boolean hasPermission(int permission) {
            return (userPermissions & permission) == permission;
        }
        
        /**
         * 添加权限
         */
        public void addPermission(int permission) {
            userPermissions |= permission;
        }
        
        /**
         * 移除权限
         */
        public void removePermission(int permission) {
            userPermissions &= ~permission;
        }
        
        /**
         * 切换权限状态
         */
        public void togglePermission(int permission) {
            userPermissions ^= permission;
        }
        
        /**
         * 检查权限组合
         */
        public boolean hasAllPermissions(int... permissions) {
            int combined = 0;
            for (int perm : permissions) {
                combined |= perm;
            }
            return (userPermissions & combined) == combined;
        }
        
        /**
         * 检查至少有一个权限
         */
        public boolean hasAnyPermission(int... permissions) {
            for (int perm : permissions) {
                if ((userPermissions & perm) != 0) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * 获取权限列表
         */
        public List<String> getPermissionList() {
            List<String> permissions = new ArrayList<>();
            if (hasPermission(READ)) permissions.add("READ");
            if (hasPermission(WRITE)) permissions.add("WRITE");
            if (hasPermission(EXECUTE)) permissions.add("EXECUTE");
            if (hasPermission(DELETE)) permissions.add("DELETE");
            return permissions;
        }
        
        /**
         * 权限序列化（存储到数据库）
         */
        public int serializePermissions() {
            return userPermissions;
        }
        
        /**
         * 权限反序列化（从数据库加载）
         */
        public void deserializePermissions(int permissions) {
            this.userPermissions = permissions;
        }
        
        @Override
        public String toString() {
            return String.format("权限位掩码: %04d (二进制: %s)", 
                               userPermissions, 
                               String.format("%4s", Integer.toBinaryString(userPermissions)).replace(' ', '0'));
        }
    }
    
    /**
     * 应用2: 布隆过滤器
     * 使用位数组实现高效的存在性检查
     * 实际应用: 缓存系统，垃圾邮件过滤，URL去重
     */
    public static class BloomFilter {
        private final int[] bitArray;
        private final int size;
        private final int[] hashSeeds;
        
        public BloomFilter(int size, int numHashes) {
            this.size = size;
            this.bitArray = new int[(size + 31) / 32];
            this.hashSeeds = new int[numHashes];
            
            // 初始化哈希种子
            Random random = new Random(42); // 固定种子保证可重复性
            for (int i = 0; i < numHashes; i++) {
                hashSeeds[i] = random.nextInt();
            }
        }
        
        /**
         * 添加元素
         */
        public void add(String element) {
            for (int i = 0; i < hashSeeds.length; i++) {
                int hash = hash(element, hashSeeds[i]);
                int index = Math.abs(hash % size);
                setBit(index);
            }
        }
        
        /**
         * 检查元素是否存在（可能有误判）
         */
        public boolean mightContain(String element) {
            for (int i = 0; i < hashSeeds.length; i++) {
                int hash = hash(element, hashSeeds[i]);
                int index = Math.abs(hash % size);
                if (!getBit(index)) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * 计算误判率（理论值）
         */
        public double getFalsePositiveRate(int numElements) {
            double k = hashSeeds.length;
            double m = size;
            double n = numElements;
            return Math.pow(1 - Math.exp(-k * n / m), k);
        }
        
        private int hash(String element, int seed) {
            int hash = seed;
            for (char c : element.toCharArray()) {
                hash = hash * 31 + c;
            }
            return hash;
        }
        
        private void setBit(int index) {
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            bitArray[arrayIndex] |= (1 << bitIndex);
        }
        
        private boolean getBit(int index) {
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            return (bitArray[arrayIndex] & (1 << bitIndex)) != 0;
        }
        
        /**
         * 获取内存使用情况
         */
        public int getMemoryUsage() {
            return bitArray.length * 4; // 字节数
        }
        
        /**
         * 清空过滤器
         */
        public void clear() {
            Arrays.fill(bitArray, 0);
        }
    }
    
    /**
     * 应用3: 图像处理 - 二值图像压缩
     * 使用位运算加速二值图像处理
     * 实际应用: OCR预处理，图像二值化，边缘检测
     */
    public static class BinaryImageProcessor {
        private final int width;
        private final int height;
        private final int[] bitData;
        
        public BinaryImageProcessor(int width, int height) {
            this.width = width;
            this.height = height;
            this.bitData = new int[(width * height + 31) / 32];
        }
        
        /**
         * 设置像素值
         */
        public void setPixel(int x, int y, boolean value) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                throw new IllegalArgumentException("坐标超出范围");
            }
            
            int index = y * width + x;
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            
            if (value) {
                bitData[arrayIndex] |= (1 << bitIndex);
            } else {
                bitData[arrayIndex] &= ~(1 << bitIndex);
            }
        }
        
        /**
         * 获取像素值
         */
        public boolean getPixel(int x, int y) {
            int index = y * width + x;
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            return (bitData[arrayIndex] & (1 << bitIndex)) != 0;
        }
        
        /**
         * 图像膨胀操作（形态学处理）
         */
        public void dilate() {
            BinaryImageProcessor result = new BinaryImageProcessor(width, height);
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (getPixel(x, y)) {
                        // 设置3x3邻域
                        for (int dy = -1; dy <= 1; dy++) {
                            for (int dx = -1; dx <= 1; dx++) {
                                int nx = x + dx, ny = y + dy;
                                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                                    result.setPixel(nx, ny, true);
                                }
                            }
                        }
                    }
                }
            }
            
            // 复制结果
            System.arraycopy(result.bitData, 0, bitData, 0, bitData.length);
        }
        
        /**
         * 图像腐蚀操作（形态学处理）
         */
        public void erode() {
            BinaryImageProcessor result = new BinaryImageProcessor(width, height);
            
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    boolean allTrue = true;
                    
                    // 检查3x3邻域
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (!getPixel(x + dx, y + dy)) {
                                allTrue = false;
                                break;
                            }
                        }
                        if (!allTrue) break;
                    }
                    
                    if (allTrue) {
                        result.setPixel(x, y, true);
                    }
                }
            }
            
            // 复制结果
            System.arraycopy(result.bitData, 0, bitData, 0, bitData.length);
        }
        
        /**
         * 图像反转
         */
        public void invert() {
            for (int i = 0; i < bitData.length; i++) {
                bitData[i] = ~bitData[i];
            }
        }
        
        /**
         * 统计前景像素数量
         */
        public int countForegroundPixels() {
            int count = 0;
            for (int value : bitData) {
                count += Integer.bitCount(value);
            }
            return count;
        }
        
        /**
         * 获取压缩比
         */
        public double getCompressionRatio() {
            int originalSize = width * height; // 每个像素1字节
            int compressedSize = bitData.length * 4; // 每个int32位存储32个像素
            return (double) originalSize / compressedSize;
        }
        
        /**
         * 转换为二维布尔数组（用于显示）
         */
        public boolean[][] toBooleanArray() {
            boolean[][] result = new boolean[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    result[y][x] = getPixel(x, y);
                }
            }
            return result;
        }
    }
    
    /**
     * 应用4: 数据库查询优化 - 位索引
     * 使用位运算加速数据库查询
     * 实际应用: 大数据分析，实时查询，OLAP系统
     */
    public static class BitmapIndex {
        private final Map<String, int[]> index;
        private final int recordCount;
        
        public BitmapIndex(int recordCount) {
            this.recordCount = recordCount;
            this.index = new HashMap<>();
        }
        
        /**
         * 为某个值创建位图索引
         */
        public void addValue(String value, int recordIndex) {
            if (recordIndex < 0 || recordIndex >= recordCount) {
                throw new IllegalArgumentException("记录索引超出范围");
            }
            
            int[] bitmap = index.computeIfAbsent(value, k -> new int[(recordCount + 31) / 32]);
            int arrayIndex = recordIndex / 32;
            int bitIndex = recordIndex % 32;
            bitmap[arrayIndex] |= (1 << bitIndex);
        }
        
        /**
         * 查询等于某个值的记录
         */
        public List<Integer> queryEquals(String value) {
            int[] bitmap = index.get(value);
            if (bitmap == null) {
                return Collections.emptyList();
            }
            
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < recordCount; i++) {
                int arrayIndex = i / 32;
                int bitIndex = i % 32;
                if ((bitmap[arrayIndex] & (1 << bitIndex)) != 0) {
                    result.add(i);
                }
            }
            return result;
        }
        
        /**
         * 查询在多个值中的记录（OR操作）
         */
        public List<Integer> queryIn(String... values) {
            if (values.length == 0) {
                return Collections.emptyList();
            }
            
            int[] combined = new int[(recordCount + 31) / 32];
            for (String value : values) {
                int[] bitmap = index.get(value);
                if (bitmap != null) {
                    for (int i = 0; i < combined.length; i++) {
                        combined[i] |= bitmap[i];
                    }
                }
            }
            
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < recordCount; i++) {
                int arrayIndex = i / 32;
                int bitIndex = i % 32;
                if ((combined[arrayIndex] & (1 << bitIndex)) != 0) {
                    result.add(i);
                }
            }
            return result;
        }
        
        /**
         * 查询同时满足多个值的记录（AND操作）
         */
        public List<Integer> queryAnd(String... values) {
            if (values.length == 0) {
                return Collections.emptyList();
            }
            
            int[] combined = null;
            for (String value : values) {
                int[] bitmap = index.get(value);
                if (bitmap == null) {
                    return Collections.emptyList(); // 某个值不存在
                }
                
                if (combined == null) {
                    combined = bitmap.clone();
                } else {
                    for (int i = 0; i < combined.length; i++) {
                        combined[i] &= bitmap[i];
                    }
                }
            }
            
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < recordCount; i++) {
                int arrayIndex = i / 32;
                int bitIndex = i % 32;
                if ((combined[arrayIndex] & (1 << bitIndex)) != 0) {
                    result.add(i);
                }
            }
            return result;
        }
        
        /**
         * 获取索引大小
         */
        public int getIndexSize() {
            int size = 0;
            for (int[] bitmap : index.values()) {
                size += bitmap.length * 4; // 每个int占4字节
            }
            return size;
        }
        
        /**
         * 获取索引统计信息
         */
        public Map<String, Object> getStatistics() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("记录数量", recordCount);
            stats.put("不同值数量", index.size());
            stats.put("索引大小(字节)", getIndexSize());
            
            int totalBitsSet = 0;
            for (int[] bitmap : index.values()) {
                for (int value : bitmap) {
                    totalBitsSet += Integer.bitCount(value);
                }
            }
            stats.put("设置的位总数", totalBitsSet);
            
            return stats;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== 位算法实际应用 - 单元测试 ===");
        
        // 测试权限系统
        System.out.println("权限系统测试:");
        PermissionSystem ps = new PermissionSystem(PermissionSystem.READ);
        System.out.println("初始权限: " + ps);
        
        ps.addPermission(PermissionSystem.WRITE);
        System.out.println("添加WRITE权限后: " + ps);
        System.out.println("是否有READ权限: " + ps.hasPermission(PermissionSystem.READ));
        System.out.println("是否有EXECUTE权限: " + ps.hasPermission(PermissionSystem.EXECUTE));
        
        // 测试布隆过滤器
        System.out.println("\n布隆过滤器测试:");
        BloomFilter bf = new BloomFilter(1000, 3);
        bf.add("hello");
        bf.add("world");
        System.out.println("包含'hello': " + bf.mightContain("hello"));
        System.out.println("包含'test': " + bf.mightContain("test"));
        System.out.println("误判率(100元素): " + bf.getFalsePositiveRate(100));
        System.out.println("内存使用: " + bf.getMemoryUsage() + " 字节");
        
        // 测试图像处理
        System.out.println("\n图像处理测试:");
        BinaryImageProcessor image = new BinaryImageProcessor(10, 10);
        image.setPixel(5, 5, true);
        image.setPixel(5, 6, true);
        System.out.println("前景像素数量: " + image.countForegroundPixels());
        System.out.println("压缩比: " + image.getCompressionRatio());
        
        // 测试位图索引
        System.out.println("\n位图索引测试:");
        BitmapIndex index = new BitmapIndex(100);
        index.addValue("男", 1);
        index.addValue("男", 3);
        index.addValue("女", 2);
        index.addValue("女", 4);
        
        System.out.println("性别=男的记录: " + index.queryEquals("男"));
        System.out.println("性别=男的记录: " + index.queryEquals("女"));
        System.out.println("索引统计: " + index.getStatistics());
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 测试权限系统性能
        long startTime = System.nanoTime();
        PermissionSystem ps = new PermissionSystem(PermissionSystem.FULL_ACCESS);
        for (int i = 0; i < 1000000; i++) {
            ps.hasPermission(PermissionSystem.READ);
        }
        long time1 = System.nanoTime() - startTime;
        System.out.printf("权限检查性能: %d ns/百万次%n", time1 / 1000);
        
        // 测试布隆过滤器性能
        startTime = System.nanoTime();
        BloomFilter bf = new BloomFilter(10000, 5);
        for (int i = 0; i < 1000; i++) {
            bf.add("element" + i);
        }
        for (int i = 0; i < 1000; i++) {
            bf.mightContain("element" + i);
        }
        long time2 = System.nanoTime() - startTime;
        System.out.printf("布隆过滤器性能: %d ns%n", time2);
        
        // 测试位图索引性能
        startTime = System.nanoTime();
        BitmapIndex index = new BitmapIndex(100000);
        for (int i = 0; i < 100000; i++) {
            index.addValue("value" + (i % 10), i);
        }
        List<Integer> result = index.queryEquals("value5");
        long time3 = System.nanoTime() - startTime;
        System.out.printf("位图索引查询性能: %d ns, 结果数量: %d%n", time3, result.size());
    }
    
    /**
     * 实际应用场景分析
     */
    public static void applicationAnalysis() {
        System.out.println("\n=== 实际应用场景分析 ===");
        
        System.out.println("1. 权限管理系统应用场景:");
        System.out.println("   - 用户角色权限控制");
        System.out.println("   - API访问权限管理");
        System.out.println("   - 文件系统权限控制");
        
        System.out.println("\n2. 布隆过滤器应用场景:");
        System.out.println("   - 缓存穿透防护");
        System.out.println("   - 垃圾邮件过滤");
        System.out.println("   - 大规模数据去重");
        
        System.out.println("\n3. 图像处理应用场景:");
        System.out.println("   - OCR文字识别预处理");
        System.out.println("   - 医学图像分析");
        System.out.println("   - 工业视觉检测");
        
        System.out.println("\n4. 位图索引应用场景:");
        System.out.println("   - 大数据分析查询");
        System.out.println("   - 数据仓库OLAP系统");
        System.out.println("   - 实时报表生成");
        
        System.out.println("\n5. 性能优势:");
        System.out.println("   - 内存使用减少80-90%");
        System.out.println("   - 查询速度提升10-100倍");
        System.out.println("   - 适合大规模数据处理");
    }
    
    public static void main(String[] args) {
        System.out.println("位算法实际应用和工程场景");
        System.out.println("包含权限管理、布隆过滤器、图像处理、数据库索引等实际应用");
        
        // 运行单元测试
        runTests();
        
        // 性能测试
        performanceTest();
        
        // 应用场景分析
        applicationAnalysis();
        
        // 工程化建议
        System.out.println("\n=== 工程化建议 ===");
        System.out.println("1. 选择合适的应用场景");
        System.out.println("2. 进行充分的性能测试");
        System.out.println("3. 考虑内存和CPU的平衡");
        System.out.println("4. 设计清晰的API接口");
        System.out.println("5. 编写详细的文档说明");
    }
}
package class032;

import java.util.*;

/**
 * 位掩码应用场景和实际问题
 * 题目来源: LeetCode, HackerRank, 实际工程问题
 * 包含位掩码在各种场景下的实际应用
 * 
 * 解题思路:
 * 方法1: 状态压缩 + 位掩码
 * 方法2: 集合操作 + 位运算
 * 方法3: 权限管理 + 位标记
 * 方法4: 数据压缩 + 位操作
 * 
 * 时间复杂度分析:
 * 方法1: O(2^n) - 状态枚举
 * 方法2: O(n) - 线性处理
 * 方法3: O(1) - 常数操作
 * 方法4: O(log n) - 对数处理
 * 
 * 空间复杂度分析:
 * 方法1: O(2^n) - 状态存储
 * 方法2: O(1) - 常数空间
 * 方法3: O(1) - 常数空间
 * 方法4: O(1) - 常数空间
 * 
 * 工程化考量:
 * 1. 可读性: 使用常量定义位掩码含义
 * 2. 可维护性: 设计清晰的接口
 * 3. 性能: 利用位运算的高效性
 * 4. 安全性: 处理权限验证
 */

public class Code19_BitmaskApplications {
    
    /**
     * 权限管理系统示例
     * 使用位掩码表示用户权限
     */
    public static class PermissionSystem {
        // 权限常量定义
        public static final int READ = 1 << 0;    // 0001 - 读权限
        public static final int WRITE = 1 << 1;   // 0010 - 写权限
        public static final int EXECUTE = 1 << 2; // 0100 - 执行权限
        public static final int DELETE = 1 << 3;   // 1000 - 删除权限
        
        /**
         * 检查用户是否具有特定权限
         * @param userPermissions 用户权限掩码
         * @param requiredPermission 需要检查的权限
         * @return 是否具有权限
         */
        public static boolean hasPermission(int userPermissions, int requiredPermission) {
            return (userPermissions & requiredPermission) != 0;
        }
        
        /**
         * 添加权限
         * @param userPermissions 用户当前权限
         * @param permissionToAdd 要添加的权限
         * @return 新的权限掩码
         */
        public static int addPermission(int userPermissions, int permissionToAdd) {
            return userPermissions | permissionToAdd;
        }
        
        /**
         * 移除权限
         * @param userPermissions 用户当前权限
         * @param permissionToRemove 要移除的权限
         * @return 新的权限掩码
         */
        public static int removePermission(int userPermissions, int permissionToRemove) {
            return userPermissions & ~permissionToRemove;
        }
        
        /**
         * 切换权限（有则移除，无则添加）
         * @param userPermissions 用户当前权限
         * @param permissionToToggle 要切换的权限
         * @return 新的权限掩码
         */
        public static int togglePermission(int userPermissions, int permissionToToggle) {
            return userPermissions ^ permissionToToggle;
        }
        
        /**
         * 获取所有权限列表
         * @param userPermissions 用户权限掩码
         * @return 权限名称列表
         */
        public static List<String> getPermissionList(int userPermissions) {
            List<String> permissions = new ArrayList<>();
            
            if (hasPermission(userPermissions, READ)) {
                permissions.add("READ");
            }
            if (hasPermission(userPermissions, WRITE)) {
                permissions.add("WRITE");
            }
            if (hasPermission(userPermissions, EXECUTE)) {
                permissions.add("EXECUTE");
            }
            if (hasPermission(userPermissions, DELETE)) {
                permissions.add("DELETE");
            }
            
            return permissions;
        }
        
        /**
         * 权限掩码转字符串
         * @param permissions 权限掩码
         * @return 二进制字符串表示
         */
        public static String permissionsToString(int permissions) {
            return String.format("%4s", Integer.toBinaryString(permissions))
                    .replace(' ', '0');
        }
    }
    
    /**
     * 状态机设计示例
     * 使用位掩码表示复杂状态
     */
    public static class StateMachine {
        // 状态定义
        public static final int IDLE = 1 << 0;
        public static final int RUNNING = 1 << 1;
        public static final int PAUSED = 1 << 2;
        public static final int STOPPED = 1 << 3;
        public static final int ERROR = 1 << 4;
        
        /**
         * 检查状态是否有效
         * @param state 当前状态
         * @return 是否有效状态
         */
        public static boolean isValidState(int state) {
            // 状态应该是2的幂（只有一个位被设置）
            return state != 0 && (state & (state - 1)) == 0;
        }
        
        /**
         * 状态转换验证
         * @param fromState 起始状态
         * @param toState 目标状态
         * @return 是否允许转换
         */
        public static boolean canTransition(int fromState, int toState) {
            // 定义允许的状态转换
            int[][] allowedTransitions = {
                {IDLE, RUNNING},
                {RUNNING, PAUSED},
                {RUNNING, STOPPED},
                {PAUSED, RUNNING},
                {PAUSED, STOPPED},
                {STOPPED, IDLE},
                {ERROR, IDLE}
            };
            
            for (int[] transition : allowedTransitions) {
                if (transition[0] == fromState && transition[1] == toState) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * 获取所有可能的状态
         * @return 状态列表
         */
        public static List<Integer> getAllStates() {
            return Arrays.asList(IDLE, RUNNING, PAUSED, STOPPED, ERROR);
        }
    }
    
    /**
     * 数据压缩示例
     * 使用位操作压缩布尔数组
     */
    public static class BooleanArrayCompressor {
        private int[] data;
        private int size;
        
        public BooleanArrayCompressor(int capacity) {
            // 每个int可以存储32个布尔值
            this.data = new int[(capacity + 31) / 32];
            this.size = capacity;
        }
        
        /**
         * 设置指定位置的布尔值
         * @param index 位置索引
         * @param value 布尔值
         */
        public void set(int index, boolean value) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            
            if (value) {
                // 设置位为1
                data[arrayIndex] |= (1 << bitIndex);
            } else {
                // 设置位为0
                data[arrayIndex] &= ~(1 << bitIndex);
            }
        }
        
        /**
         * 获取指定位置的布尔值
         * @param index 位置索引
         * @return 布尔值
         */
        public boolean get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            
            int arrayIndex = index / 32;
            int bitIndex = index % 32;
            
            return (data[arrayIndex] & (1 << bitIndex)) != 0;
        }
        
        /**
         * 统计为true的个数
         * @return true的个数
         */
        public int countTrue() {
            int count = 0;
            for (int value : data) {
                count += Integer.bitCount(value);
            }
            return count;
        }
        
        /**
         * 获取压缩后的数据大小（字节）
         * @return 数据大小
         */
        public int getCompressedSize() {
            return data.length * 4;  // 每个int占4字节
        }
        
        /**
         * 获取原始数据大小（如果使用boolean数组）
         * @return 原始大小
         */
        public int getOriginalSize() {
            return size;  // 每个boolean在Java中至少占1字节
        }
        
        /**
         * 计算压缩比
         * @return 压缩比（原始大小/压缩大小）
         */
        public double getCompressionRatio() {
            return (double) getOriginalSize() / getCompressedSize();
        }
    }
    
    /**
     * 集合操作工具类
     * 使用位掩码表示小范围整数集合
     */
    public static class BitSetUtils {
        /**
         * 创建包含指定元素的集合
         * @param elements 元素数组
         * @return 位掩码表示的集合
         */
        public static int createSet(int[] elements) {
            int set = 0;
            for (int element : elements) {
                if (element < 0 || element >= 32) {
                    throw new IllegalArgumentException("Element must be between 0 and 31");
                }
                set |= (1 << element);
            }
            return set;
        }
        
        /**
         * 向集合添加元素
         * @param set 原集合
         * @param element 要添加的元素
         * @return 新集合
         */
        public static int addElement(int set, int element) {
            return set | (1 << element);
        }
        
        /**
         * 从集合移除元素
         * @param set 原集合
         * @param element 要移除的元素
         * @return 新集合
         */
        public static int removeElement(int set, int element) {
            return set & ~(1 << element);
        }
        
        /**
         * 检查集合是否包含元素
         * @param set 集合
         * @param element 元素
         * @return 是否包含
         */
        public static boolean contains(int set, int element) {
            return (set & (1 << element)) != 0;
        }
        
        /**
         * 集合交集
         * @param set1 集合1
         * @param set2 集合2
         * @return 交集
         */
        public static int intersection(int set1, int set2) {
            return set1 & set2;
        }
        
        /**
         * 集合并集
         * @param set1 集合1
         * @param set2 集合2
         * @return 并集
         */
        public static int union(int set1, int set2) {
            return set1 | set2;
        }
        
        /**
         * 集合差集
         * @param set1 集合1
         * @param set2 集合2
         * @return 差集（在set1中但不在set2中）
         */
        public static int difference(int set1, int set2) {
            return set1 & ~set2;
        }
        
        /**
         * 获取集合大小
         * @param set 集合
         * @return 元素个数
         */
        public static int size(int set) {
            return Integer.bitCount(set);
        }
        
        /**
         * 集合转数组
         * @param set 集合
         * @return 元素数组
         */
        public static int[] toArray(int set) {
            int size = size(set);
            int[] result = new int[size];
            int index = 0;
            
            for (int i = 0; i < 32; i++) {
                if (contains(set, i)) {
                    result[index++] = i;
                }
            }
            
            return result;
        }
        
        /**
         * 集合转字符串
         * @param set 集合
         * @return 字符串表示
         */
        public static String toString(int set) {
            return "{" + java.util.Arrays.toString(toArray(set)) + "}";
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== 位掩码应用场景 - 单元测试 ===");
        
        // 测试权限管理系统
        System.out.println("权限管理系统测试:");
        int userPermissions = PermissionSystem.READ | PermissionSystem.WRITE;
        System.out.printf("用户权限: %s%n", PermissionSystem.permissionsToString(userPermissions));
        System.out.printf("具有读权限: %b%n", PermissionSystem.hasPermission(userPermissions, PermissionSystem.READ));
        System.out.printf("具有执行权限: %b%n", PermissionSystem.hasPermission(userPermissions, PermissionSystem.EXECUTE));
        
        // 添加执行权限
        userPermissions = PermissionSystem.addPermission(userPermissions, PermissionSystem.EXECUTE);
        System.out.printf("添加执行权限后: %s%n", PermissionSystem.permissionsToString(userPermissions));
        
        // 测试状态机
        System.out.println("\n状态机测试:");
        System.out.printf("从空闲到运行是否允许: %b%n", StateMachine.canTransition(StateMachine.IDLE, StateMachine.RUNNING));
        System.out.printf("从运行到空闲是否允许: %b%n", StateMachine.canTransition(StateMachine.RUNNING, StateMachine.IDLE));
        
        // 测试数据压缩
        System.out.println("\n数据压缩测试:");
        BooleanArrayCompressor compressor = new BooleanArrayCompressor(100);
        compressor.set(0, true);
        compressor.set(50, true);
        compressor.set(99, true);
        System.out.printf("位置0的值: %b%n", compressor.get(0));
        System.out.printf("位置1的值: %b%n", compressor.get(1));
        System.out.printf("True的个数: %d%n", compressor.countTrue());
        System.out.printf("压缩比: %.2f%n", compressor.getCompressionRatio());
        
        // 测试集合操作
        System.out.println("\n集合操作测试:");
        int set1 = BitSetUtils.createSet(new int[]{1, 3, 5});
        int set2 = BitSetUtils.createSet(new int[]{2, 3, 4});
        System.out.printf("集合1: %s%n", BitSetUtils.toString(set1));
        System.out.printf("集合2: %s%n", BitSetUtils.toString(set2));
        System.out.printf("交集: %s%n", BitSetUtils.toString(BitSetUtils.intersection(set1, set2)));
        System.out.printf("并集: %s%n", BitSetUtils.toString(BitSetUtils.union(set1, set2)));
        System.out.printf("差集: %s%n", BitSetUtils.toString(BitSetUtils.difference(set1, set2)));
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 测试权限检查性能
        int permissions = PermissionSystem.READ | PermissionSystem.WRITE | PermissionSystem.EXECUTE;
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            PermissionSystem.hasPermission(permissions, PermissionSystem.READ);
        }
        long time1 = System.nanoTime() - startTime;
        System.out.printf("权限检查性能: %d ns/百万次%n", time1 / 1000);
        
        // 测试集合操作性能
        int largeSet = BitSetUtils.createSet(new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19});
        
        startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            BitSetUtils.contains(largeSet, 5);
        }
        long time2 = System.nanoTime() - startTime;
        System.out.printf("集合包含检查性能: %d ns/百万次%n", time2 / 1000);
        
        // 测试数据压缩性能
        BooleanArrayCompressor compressor = new BooleanArrayCompressor(1000);
        
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            compressor.set(i, i % 2 == 0);
        }
        long time3 = System.nanoTime() - startTime;
        System.out.printf("数据压缩设置性能: %d ns/千次%n", time3);
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("位掩码应用的优势:");
        System.out.println("1. 空间效率: 极大减少内存占用");
        System.out.println("2. 时间效率: 位运算非常快速");
        System.out.println("3. 简洁性: 复杂逻辑用简单操作表达");
        
        System.out.println("\n适用场景:");
        System.out.println("1. 权限管理系统");
        System.out.println("2. 状态机设计");
        System.out.println("3. 数据压缩存储");
        System.out.println("4. 小范围集合操作");
        System.out.println("5. 标志位管理");
        
        System.out.println("\n限制条件:");
        System.out.println("1. 元素范围有限（通常0-31或0-63）");
        System.out.println("2. 需要额外的文档说明位含义");
        System.out.println("3. 调试相对困难");
    }
    
    public static void main(String[] args) {
        System.out.println("位掩码应用场景和实际问题");
        System.out.println("包含权限管理、状态机、数据压缩等实际应用");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 实际工程应用
        System.out.println("\n=== 实际工程应用 ===");
        System.out.println("1. 操作系统: 文件权限管理");
        System.out.println("2. 数据库: 索引位图");
        System.out.println("3. 游戏开发: 状态标志");
        System.out.println("4. 网络协议: 标志位");
        System.out.println("5. 编译器: 符号表管理");
        
        System.out.println("\n=== 最佳实践 ===");
        System.out.println("1. 使用常量定义位掩码含义");
        System.out.println("2. 添加详细的注释说明");
        System.out.println("3. 进行充分的单元测试");
        System.out.println("4. 考虑可扩展性设计");
        System.out.println("5. 性能优化时优先选择位运算");
    }
}
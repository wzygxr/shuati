package class031;

import java.util.*;

/**
 * 位运算在实际工程中的应用
 * 测试链接：综合题目，展示位运算在真实工程场景中的应用
 * 
 * 题目描述：
 * 本文件展示位运算在各种实际工程场景中的应用，包括权限系统、状态压缩、加密算法等。
 * 
 * 解题思路：
 * 通过具体案例展示位运算如何解决实际问题，体现其高效性和实用性。
 * 
 * 时间复杂度：各种应用的时间复杂度不同，但通常为O(1)或O(log n)
 * 空间复杂度：O(1) - 通常只使用常数个变量
 */
public class Code25_BitwiseOperationsInRealWorld {
    
    // ==================== 权限系统应用 ====================
    
    /**
     * 权限系统：使用位掩码表示用户权限
     * 每种权限用一个位表示，可以高效地进行权限组合和检查
     */
    public static class PermissionSystem {
        // 权限定义
        public static final int READ = 1 << 0;    // 0001 - 读权限
        public static final int WRITE = 1 << 1;   // 0010 - 写权限  
        public static final int EXECUTE = 1 << 2; // 0100 - 执行权限
        public static final int DELETE = 1 << 3;   // 1000 - 删除权限
        
        /**
         * 添加权限
         */
        public static int addPermission(int current, int permission) {
            return current | permission;
        }
        
        /**
         * 移除权限
         */
        public static int removePermission(int current, int permission) {
            return current & ~permission;
        }
        
        /**
         * 检查是否有权限
         */
        public static boolean hasPermission(int current, int permission) {
            return (current & permission) == permission;
        }
        
        /**
         * 切换权限（有则移除，无则添加）
         */
        public static int togglePermission(int current, int permission) {
            return current ^ permission;
        }
        
        /**
         * 获取所有权限列表
         */
        public static List<String> getPermissionNames(int permissions) {
            List<String> result = new ArrayList<>();
            if (hasPermission(permissions, READ)) result.add("READ");
            if (hasPermission(permissions, WRITE)) result.add("WRITE");
            if (hasPermission(permissions, EXECUTE)) result.add("EXECUTE");
            if (hasPermission(permissions, DELETE)) result.add("DELETE");
            return result;
        }
    }
    
    // ==================== 状态压缩应用 ====================
    
    /**
     * 状态压缩：使用一个整数表示多个布尔状态
     * 常用于动态规划、游戏状态等场景
     */
    public static class StateCompression {
        /**
         * 设置状态位
         */
        public static int setState(int state, int position, boolean value) {
            if (value) {
                return state | (1 << position);
            } else {
                return state & ~(1 << position);
            }
        }
        
        /**
         * 获取状态位
         */
        public static boolean getState(int state, int position) {
            return (state & (1 << position)) != 0;
        }
        
        /**
         * 切换状态位
         */
        public static int toggleState(int state, int position) {
            return state ^ (1 << position);
        }
        
        /**
         * 检查状态位模式
         */
        public static boolean checkPattern(int state, int pattern) {
            return (state & pattern) == pattern;
        }
    }
    
    // ==================== 颜色操作应用 ====================
    
    /**
     * 颜色操作：使用位运算处理RGB颜色
     * 常用于图形学、图像处理等场景
     */
    public static class ColorOperations {
        /**
         * 从RGB值创建颜色整数
         */
        public static int createColor(int red, int green, int blue) {
            return (red << 16) | (green << 8) | blue;
        }
        
        /**
         * 从颜色整数提取红色分量
         */
        public static int getRed(int color) {
            return (color >> 16) & 0xFF;
        }
        
        /**
         * 从颜色整数提取绿色分量
         */
        public static int getGreen(int color) {
            return (color >> 8) & 0xFF;
        }
        
        /**
         * 从颜色整数提取蓝色分量
         */
        public static int getBlue(int color) {
            return color & 0xFF;
        }
        
        /**
         * 调整颜色亮度（乘以系数）
         */
        public static int adjustBrightness(int color, float factor) {
            int red = Math.min(255, (int)(getRed(color) * factor));
            int green = Math.min(255, (int)(getGreen(color) * factor));
            int blue = Math.min(255, (int)(getBlue(color) * factor));
            return createColor(red, green, blue);
        }
        
        /**
         * 混合两种颜色
         */
        public static int blendColors(int color1, int color2, float ratio) {
            float inverseRatio = 1.0f - ratio;
            int red = (int)(getRed(color1) * ratio + getRed(color2) * inverseRatio);
            int green = (int)(getGreen(color1) * ratio + getGreen(color2) * inverseRatio);
            int blue = (int)(getBlue(color1) * ratio + getBlue(color2) * inverseRatio);
            return createColor(red, green, blue);
        }
    }
    
    // ==================== 网络协议应用 ====================
    
    /**
     * 网络协议：解析和构建数据包头部
     * 常用于网络编程、协议解析等场景
     */
    public static class NetworkProtocol {
        /**
         * 构建IP头部（简化版）
         */
        public static int buildIPHeader(int version, int headerLength, 
                                      int typeOfService, int totalLength,
                                      int identification, int flags, 
                                      int fragmentOffset, int ttl, 
                                      int protocol, int checksum, 
                                      int sourceIP, int destIP) {
            int header = 0;
            header |= (version & 0xF) << 28;
            header |= (headerLength & 0xF) << 24;
            header |= (typeOfService & 0xFF) << 16;
            header |= (totalLength & 0xFFFF);
            
            // 继续构建其他字段...
            return header;
        }
        
        /**
         * 解析IP头部版本
         */
        public static int parseIPVersion(int ipHeader) {
            return (ipHeader >> 28) & 0xF;
        }
        
        /**
         * 解析IP头部长度
         */
        public static int parseHeaderLength(int ipHeader) {
            return (ipHeader >> 24) & 0xF;
        }
    }
    
    // ==================== 加密算法应用 ====================
    
    /**
     * 简单加密算法：使用位运算进行数据加密
     * 注意：这只是教学示例，不适用于实际加密
     */
    public static class SimpleEncryption {
        /**
         * 简单异或加密
         */
        public static byte[] xorEncrypt(byte[] data, byte[] key) {
            byte[] encrypted = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                encrypted[i] = (byte)(data[i] ^ key[i % key.length]);
            }
            return encrypted;
        }
        
        /**
         * 简单位置换加密
         */
        public static byte bitPermutation(byte data) {
            // 简单的位置换：交换高低4位
            return (byte)(((data & 0xF0) >> 4) | ((data & 0x0F) << 4));
        }
    }
    
    // ==================== 性能优化应用 ====================
    
    /**
     * 性能优化：使用位运算替代算术运算
     * 常用于性能敏感的场景
     */
    public static class PerformanceOptimization {
        /**
         * 快速计算2的幂
         */
        public static int powerOfTwo(int n) {
            return 1 << n;
        }
        
        /**
         * 快速判断是否是2的幂
         */
        public static boolean isPowerOfTwo(int n) {
            return n > 0 && (n & (n - 1)) == 0;
        }
        
        /**
         * 快速计算绝对值
         */
        public static int abs(int n) {
            int mask = n >> 31;
            return (n + mask) ^ mask;
        }
        
        /**
         * 快速计算模2的幂
         */
        public static int modPowerOfTwo(int n, int mod) {
            return n & (mod - 1);
        }
        
        /**
         * 快速交换两个数
         */
        public static void swap(int[] arr, int i, int j) {
            if (i != j) {
                arr[i] ^= arr[j];
                arr[j] ^= arr[i];
                arr[i] ^= arr[j];
            }
        }
    }
    
    // ==================== 数据结构优化应用 ====================
    
    /**
     * 数据结构优化：使用位运算优化数据结构
     * 常用于空间敏感的场景
     */
    public static class DataStructureOptimization {
        /**
         * 位集（BitSet）简化实现
         */
        public static class CompactBitSet {
            private int[] data;
            private int size;
            
            public CompactBitSet(int size) {
                this.size = size;
                this.data = new int[(size + 31) / 32];
            }
            
            public void set(int index) {
                if (index < 0 || index >= size) {
                    throw new IndexOutOfBoundsException();
                }
                int arrayIndex = index / 32;
                int bitIndex = index % 32;
                data[arrayIndex] |= (1 << bitIndex);
            }
            
            public void clear(int index) {
                if (index < 0 || index >= size) {
                    throw new IndexOutOfBoundsException();
                }
                int arrayIndex = index / 32;
                int bitIndex = index % 32;
                data[arrayIndex] &= ~(1 << bitIndex);
            }
            
            public boolean get(int index) {
                if (index < 0 || index >= size) {
                    throw new IndexOutOfBoundsException();
                }
                int arrayIndex = index / 32;
                int bitIndex = index % 32;
                return (data[arrayIndex] & (1 << bitIndex)) != 0;
            }
            
            public int cardinality() {
                int count = 0;
                for (int value : data) {
                    count += Integer.bitCount(value);
                }
                return count;
            }
        }
        
        /**
         * 布隆过滤器简化实现
         */
        public static class SimpleBloomFilter {
            private CompactBitSet bitSet;
            private int size;
            private int[] hashSeeds;
            
            public SimpleBloomFilter(int size, int numHashes) {
                this.size = size;
                this.bitSet = new CompactBitSet(size);
                this.hashSeeds = new int[numHashes];
                // 初始化哈希种子
                for (int i = 0; i < numHashes; i++) {
                    hashSeeds[i] = i * 31 + 12345;
                }
            }
            
            public void add(String element) {
                for (int seed : hashSeeds) {
                    int hash = Math.abs(element.hashCode() ^ seed) % size;
                    bitSet.set(hash);
                }
            }
            
            public boolean mightContain(String element) {
                for (int seed : hashSeeds) {
                    int hash = Math.abs(element.hashCode() ^ seed) % size;
                    if (!bitSet.get(hash)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
    
    // ==================== 测试方法 ====================
    
    public static void main(String[] args) {
        System.out.println("=== 权限系统测试 ===");
        int userPermissions = 0;
        userPermissions = PermissionSystem.addPermission(userPermissions, PermissionSystem.READ);
        userPermissions = PermissionSystem.addPermission(userPermissions, PermissionSystem.WRITE);
        System.out.println("用户权限: " + PermissionSystem.getPermissionNames(userPermissions));
        System.out.println("有写权限: " + PermissionSystem.hasPermission(userPermissions, PermissionSystem.WRITE));
        
        System.out.println("\n=== 状态压缩测试 ===");
        int gameState = 0;
        gameState = StateCompression.setState(gameState, 0, true);  // 玩家存活
        gameState = StateCompression.setState(gameState, 1, true);  // 游戏进行中
        System.out.println("玩家存活: " + StateCompression.getState(gameState, 0));
        System.out.println("游戏进行中: " + StateCompression.getState(gameState, 1));
        
        System.out.println("\n=== 颜色操作测试 ===");
        int redColor = ColorOperations.createColor(255, 0, 0);
        int greenColor = ColorOperations.createColor(0, 255, 0);
        int blendedColor = ColorOperations.blendColors(redColor, greenColor, 0.5f);
        System.out.println("混合颜色 - R:" + ColorOperations.getRed(blendedColor) + 
                         " G:" + ColorOperations.getGreen(blendedColor) + 
                         " B:" + ColorOperations.getBlue(blendedColor));
        
        System.out.println("\n=== 性能优化测试 ===");
        int[] array = {5, 3};
        PerformanceOptimization.swap(array, 0, 1);
        System.out.println("交换后: [" + array[0] + ", " + array[1] + "]");
        System.out.println("8是2的幂: " + PerformanceOptimization.isPowerOfTwo(8));
        System.out.println("15 mod 8: " + PerformanceOptimization.modPowerOfTwo(15, 8));
        
        System.out.println("\n=== 数据结构优化测试 ===");
        DataStructureOptimization.CompactBitSet bitSet = new DataStructureOptimization.CompactBitSet(100);
        bitSet.set(42);
        bitSet.set(57);
        System.out.println("位42已设置: " + bitSet.get(42));
        System.out.println("位43未设置: " + bitSet.get(43));
        System.out.println("位集基数: " + bitSet.cardinality());
        
        System.out.println("\n=== 工程化考量总结 ===");
        System.out.println("1. 边界条件处理：所有方法都应处理边界情况");
        System.out.println("2. 性能优化：位运算通常比算术运算更快");
        System.out.println("3. 可读性：添加详细注释说明位运算原理");
        System.out.println("4. 错误处理：输入验证和异常处理");
        System.out.println("5. 测试覆盖：包含各种边界情况和特殊输入");
    }
    
    /**
     * 工程化考量：
     * 1. 边界条件处理：所有方法都应处理边界情况
     * 2. 性能优化：位运算通常比算术运算更快，但要注意可读性
     * 3. 可读性：添加详细注释说明位运算原理
     * 4. 错误处理：输入验证和异常处理
     * 5. 测试覆盖：包含各种边界情况和特殊输入
     * 
     * 应用场景总结：
     * 1. 权限系统：高效管理用户权限
     * 2. 状态压缩：节省内存空间
     * 3. 图形学：快速处理颜色和像素
     * 4. 网络编程：解析和构建协议头部
     * 5. 加密算法：基础位操作
     * 6. 性能优化：替代昂贵的算术运算
     * 7. 数据结构：优化空间使用
     * 
     * 学习建议：
     * 1. 理解二进制：掌握二进制表示和位运算原理
     * 2. 实践应用：在具体项目中应用位运算技巧
     * 3. 性能测试：比较位运算和传统方法的性能差异
     * 4. 代码审查：确保位运算代码的可读性和正确性
     * 5. 持续学习：关注新的位运算技巧和应用场景
     */
}
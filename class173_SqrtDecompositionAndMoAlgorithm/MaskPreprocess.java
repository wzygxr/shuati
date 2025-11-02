package class175.随机化与复杂度分析;

/**
 * 掩码预处理工具类
 * 提供常用位掩码的预处理和位运算优化功能
 * 用于算法中的位操作加速
 */
public class MaskPreprocess {
    // 预定义的最大数组大小
    private static final int MAX_N = 1 << 20; // 1,048,576
    
    // 预处理数组
    private static int[] lowbit;         // 最低位1的值
    private static int[] highestBit;     // 最高位1的位置（从0开始）
    private static int[] bitCount;       // 二进制中1的个数（popcount）
    private static int[] nextSetBit;     // 下一个设置位的位置
    private static int[] prevSetBit;     // 上一个设置位的位置
    private static int[] parity;         // 奇偶校验位（1的个数的奇偶性）
    
    // 常用掩码
    public static final int ALL_ONES_8 = 0xFF;       // 8位全1掩码
    public static final int ALL_ONES_16 = 0xFFFF;    // 16位全1掩码
    public static final int ALL_ONES_32 = 0xFFFFFFFF; // 32位全1掩码
    
    public static final int ALTERNATE_ODD = 0x55555555;  // 01010101...
    public static final int ALTERNATE_EVEN = 0xAAAAAAAA; // 10101010...
    
    public static final int ALL_ONES_LAST_4 = 0x0000000F; // 最后4位全1
    public static final int ALL_ONES_FIRST_4 = 0xF0000000; // 最前4位全1
    
    // 静态初始化块，预处理所有数据
    static {
        preprocessAll();
    }
    
    /**
     * 预处理所有掩码数据
     */
    private static void preprocessAll() {
        lowbit = new int[MAX_N];
        highestBit = new int[MAX_N];
        bitCount = new int[MAX_N];
        nextSetBit = new int[MAX_N];
        prevSetBit = new int[MAX_N];
        parity = new int[MAX_N];
        
        // 初始化最低位1
        for (int i = 1; i < MAX_N; i++) {
            lowbit[i] = i & (-i);
        }
        
        // 初始化最高位1的位置
        highestBit[0] = -1; // 特殊情况
        for (int i = 1; i < MAX_N; i++) {
            highestBit[i] = highestBit[i >> 1] + 1;
        }
        
        // 初始化bitCount（使用动态规划）
        bitCount[0] = 0;
        for (int i = 1; i < MAX_N; i++) {
            bitCount[i] = bitCount[i & (i - 1)] + 1;
        }
        
        // 初始化nextSetBit
        for (int i = 0; i < MAX_N; i++) {
            nextSetBit[i] = -1; // 默认-1表示没有下一个设置位
            if (i == 0) continue;
            
            int temp = i;
            int lsb = temp & -temp;
            temp ^= lsb; // 清除最低位1
            if (temp != 0) {
                nextSetBit[i] = Integer.numberOfTrailingZeros(temp & -temp);
            }
        }
        
        // 初始化prevSetBit
        for (int i = 0; i < MAX_N; i++) {
            prevSetBit[i] = -1; // 默认-1表示没有上一个设置位
            if (i == 0) continue;
            
            int temp = i;
            int highest = 31 - Integer.numberOfLeadingZeros(temp);
            temp ^= (1 << highest); // 清除最高位1
            if (temp != 0) {
                prevSetBit[i] = 31 - Integer.numberOfLeadingZeros(temp);
            }
        }
        
        // 初始化奇偶校验
        parity[0] = 0;
        for (int i = 1; i < MAX_N; i++) {
            parity[i] = parity[i >> 1] ^ (i & 1);
        }
    }
    
    /**
     * 获取最低位1的值
     * 等同于 x & (-x)
     * 
     * @param x 输入整数
     * @return 最低位1的值
     */
    public static int getLowbit(int x) {
        if (x == 0) return 0;
        if (x < MAX_N) return lowbit[x];
        return x & (-x); // 对于超出预计算范围的数
    }
    
    /**
     * 获取最高位1的位置（从0开始）
     * 
     * @param x 输入整数
     * @return 最高位1的位置
     */
    public static int getHighestBitPosition(int x) {
        if (x == 0) return -1;
        if (x < MAX_N) return highestBit[x];
        return 31 - Integer.numberOfLeadingZeros(x); // 对于超出预计算范围的数
    }
    
    /**
     * 获取二进制中1的个数
     * 
     * @param x 输入整数
     * @return 1的个数
     */
    public static int getBitCount(int x) {
        if (x == 0) return 0;
        if (x < MAX_N) return bitCount[x];
        return Integer.bitCount(x); // 对于超出预计算范围的数
    }
    
    /**
     * 获取下一个设置位的位置
     * 
     * @param x 输入整数
     * @return 下一个设置位的位置，如果没有返回-1
     */
    public static int getNextSetBit(int x) {
        if (x < MAX_N) return nextSetBit[x];
        // 动态计算
        int temp = x;
        int lsb = temp & -temp;
        temp ^= lsb;
        return temp == 0 ? -1 : Integer.numberOfTrailingZeros(temp & -temp);
    }
    
    /**
     * 获取上一个设置位的位置
     * 
     * @param x 输入整数
     * @return 上一个设置位的位置，如果没有返回-1
     */
    public static int getPrevSetBit(int x) {
        if (x < MAX_N) return prevSetBit[x];
        // 动态计算
        int temp = x;
        int highest = 31 - Integer.numberOfLeadingZeros(temp);
        temp ^= (1 << highest);
        return temp == 0 ? -1 : 31 - Integer.numberOfLeadingZeros(temp);
    }
    
    /**
     * 获取奇偶校验位（1的个数的奇偶性）
     * 
     * @param x 输入整数
     * @return 1表示奇数个1，0表示偶数个1
     */
    public static int getParity(int x) {
        if (x < MAX_N) return parity[x];
        // 动态计算
        x ^= x >>> 16;
        x ^= x >>> 8;
        x ^= x >>> 4;
        x ^= x >>> 2;
        x ^= x >>> 1;
        return x & 1;
    }
    
    /**
     * 生成特定长度的全1掩码
     * 
     * @param length 掩码长度
     * @return 全1掩码
     */
    public static int generateAllOnesMask(int length) {
        return (1 << length) - 1;
    }
    
    /**
     * 生成交替位掩码
     * 
     * @param startWithOne 是否以1开始
     * @return 交替位掩码
     */
    public static int generateAlternatingMask(boolean startWithOne) {
        return startWithOne ? ALTERNATE_ODD : ALTERNATE_EVEN;
    }
    
    /**
     * 检查数x是否是2的幂
     * 
     * @param x 输入整数
     * @return 是否是2的幂
     */
    public static boolean isPowerOfTwo(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }
    
    /**
     * 对齐到下一个2的幂
     * 
     * @param x 输入整数
     * @return 下一个大于等于x的2的幂
     */
    public static int nextPowerOfTwo(int x) {
        if (x <= 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试基本功能
        int testNum = 0b10110100; // 180
        System.out.println("测试数字: " + testNum + " (二进制: " + 
                          Integer.toBinaryString(testNum) + ")");
        
        System.out.println("最低位1的值: " + getLowbit(testNum) + 
                          " (二进制: " + Integer.toBinaryString(getLowbit(testNum)) + ")");
        
        System.out.println("最高位1的位置: " + getHighestBitPosition(testNum));
        
        System.out.println("二进制中1的个数: " + getBitCount(testNum));
        
        System.out.println("下一个设置位的位置: " + getNextSetBit(testNum));
        
        System.out.println("上一个设置位的位置: " + getPrevSetBit(testNum));
        
        System.out.println("奇偶校验位: " + getParity(testNum));
        
        // 测试掩码生成
        System.out.println("\n掩码生成测试:");
        System.out.println("8位全1掩码: " + generateAllOnesMask(8) + 
                          " (二进制: " + Integer.toBinaryString(generateAllOnesMask(8)) + ")");
        
        System.out.println("交替位掩码(以1开始): " + 
                          Integer.toBinaryString(generateAlternatingMask(true)));
        
        System.out.println("交替位掩码(以0开始): " + 
                          Integer.toBinaryString(generateAlternatingMask(false)));
        
        // 测试工具方法
        System.out.println("\n工具方法测试:");
        System.out.println("64是否是2的幂: " + isPowerOfTwo(64));
        System.out.println("100是否是2的幂: " + isPowerOfTwo(100));
        
        System.out.println("100的下一个2的幂: " + nextPowerOfTwo(100));
        System.out.println("128的下一个2的幂: " + nextPowerOfTwo(128));
    }
}
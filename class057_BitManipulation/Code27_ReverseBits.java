/**
 * 颠倒二进制位
 * 测试链接：https://leetcode.cn/problems/reverse-bits/
 * 
 * 题目描述：
 * 颠倒给定的 32 位无符号整数的二进制位。
 * 
 * 解题思路：
 * 1. 逐位反转：从最低位开始，依次将每一位移动到对应的高位位置
 * 2. 分治反转：使用分治思想，先交换16位块，再交换8位块，依此类推
 * 3. 查表法：对于8位进行预计算，然后组合成32位
 * 
 * 时间复杂度：O(1) - 固定32次操作
 * 空间复杂度：O(1) - 只使用常数个变量
 */
public class Code27_ReverseBits {
    
    /**
     * 方法1：逐位反转
     * 时间复杂度：O(1) - 固定32次循环
     * 空间复杂度：O(1)
     */
    public int reverseBits1(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            // 取最低位
            int bit = n & 1;
            // 将最低位移动到对应的高位位置
            result = (result << 1) | bit;
            // 右移处理下一位
            n = n >>> 1;  // 使用无符号右移
        }
        return result;
    }
    
    /**
     * 方法2：分治反转（更高效）
     * 时间复杂度：O(1) - 固定5次操作
     * 空间复杂度：O(1)
     */
    public int reverseBits2(int n) {
        // 分治思想：先交换16位块，再交换8位块，依此类推
        n = (n >>> 16) | (n << 16);  // 交换16位块
        n = ((n & 0xff00ff00) >>> 8) | ((n & 0x00ff00ff) << 8);  // 交换8位块
        n = ((n & 0xf0f0f0f0) >>> 4) | ((n & 0x0f0f0f0f) << 4);  // 交换4位块
        n = ((n & 0xcccccccc) >>> 2) | ((n & 0x33333333) << 2);  // 交换2位块
        n = ((n & 0xaaaaaaaa) >>> 1) | ((n & 0x55555555) << 1);  // 交换1位块
        return n;
    }
    
    /**
     * 方法3：查表法（最优解，适合多次调用）
     * 时间复杂度：O(1) - 固定4次查表操作
     * 空间复杂度：O(256) - 预计算表
     */
    private static final int[] REVERSE_TABLE = new int[256];
    
    static {
        // 预计算0-255的8位反转结果
        for (int i = 0; i < 256; i++) {
            REVERSE_TABLE[i] = reverseByte(i);
        }
    }
    
    private static int reverseByte(int b) {
        // 反转8位字节
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 1) | (b & 1);
            b >>>= 1;
        }
        return result;
    }
    
    public int reverseBits3(int n) {
        // 将32位分成4个8位字节，分别反转后重新组合
        return (REVERSE_TABLE[n & 0xff] << 24) |          // 最低8位移到最高8位
               (REVERSE_TABLE[(n >>> 8) & 0xff] << 16) | // 次低8位移到次高8位
               (REVERSE_TABLE[(n >>> 16) & 0xff] << 8) |  // 次高8位移到次低8位
               (REVERSE_TABLE[(n >>> 24) & 0xff]);        // 最高8位移到最低8位
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code27_ReverseBits solution = new Code27_ReverseBits();
        
        // 测试用例1：正常情况
        int n1 = 43261596;  // 二进制: 00000010100101000001111010011100
        int result1 = solution.reverseBits1(n1);
        int result2 = solution.reverseBits2(n1);
        int result3 = solution.reverseBits3(n1);
        System.out.println("测试用例1 - 输入: " + n1);
        System.out.println("方法1结果: " + result1 + " (预期: 964176192)");
        System.out.println("方法2结果: " + result2 + " (预期: 964176192)");
        System.out.println("方法3结果: " + result3 + " (预期: 964176192)");
        
        // 测试用例2：边界情况（全0）
        int n2 = 0;
        int result4 = solution.reverseBits1(n2);
        System.out.println("测试用例2 - 输入: " + n2);
        System.out.println("方法1结果: " + result4 + " (预期: 0)");
        
        // 测试用例3：边界情况（全1）
        int n3 = -1;  // 二进制全1
        int result5 = solution.reverseBits1(n3);
        System.out.println("测试用例3 - 输入: " + n3);
        System.out.println("方法1结果: " + result5 + " (预期: -1)");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 逐位反转:");
        System.out.println("  时间复杂度: O(1) - 固定32次操作");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 分治反转:");
        System.out.println("  时间复杂度: O(1) - 固定5次位操作");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法3 - 查表法:");
        System.out.println("  时间复杂度: O(1) - 固定4次查表操作");
        System.out.println("  空间复杂度: O(256) - 预计算表");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 方法选择：");
        System.out.println("   - 单次调用：方法2（分治）最优");
        System.out.println("   - 多次调用：方法3（查表）最优");
        System.out.println("2. 边界处理：正确处理无符号整数");
        System.out.println("3. 性能优化：避免不必要的循环");
        System.out.println("4. 可读性：添加详细注释说明位操作原理");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 位掩码技巧：使用十六进制常量作为位掩码");
        System.out.println("2. 分治思想：将大问题分解为小问题解决");
        System.out.println("3. 查表优化：空间换时间，适合重复计算");
        System.out.println("4. 无符号右移：使用>>>避免符号扩展问题");
    }
}
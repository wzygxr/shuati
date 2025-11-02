/**
 * 2的幂
 * 测试链接：https://leetcode.cn/problems/power-of-two/
 * 
 * 题目描述：
 * 给你一个整数 n，请你判断该整数是否是 2 的幂次方。如果是，返回 true ；否则，返回 false 。
 * 如果存在一个整数 x 使得 n == 2^x ，则认为 n 是 2 的幂次方。
 * 
 * 解题思路：
 * 1. 循环除法：不断除以2直到结果为1
 * 2. 位运算技巧：利用 n & (n-1) == 0 的性质
 * 3. 数学方法：利用对数运算
 * 4. 查表法：预计算所有2的幂
 * 
 * 时间复杂度：O(1) - 最多32次操作
 * 空间复杂度：O(1) - 只使用常数个变量
 */
public class Code29_PowerOfTwo {
    
    /**
     * 方法1：循环除法
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfTwo1(int n) {
        if (n <= 0) {
            return false;
        }
        
        while (n % 2 == 0) {
            n = n / 2;
        }
        
        return n == 1;
    }
    
    /**
     * 方法2：位运算技巧（最优解）
     * 利用性质：2的幂的二进制表示中只有一个1
     * n & (n-1) 可以消除最低位的1，如果结果为0则是2的幂
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfTwo2(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * 方法3：数学方法
     * 利用对数运算：log2(n) 应该是整数
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfTwo3(int n) {
        if (n <= 0) {
            return false;
        }
        
        double logResult = Math.log(n) / Math.log(2);
        // 检查是否为整数（考虑浮点数精度）
        return Math.abs(logResult - Math.round(logResult)) < 1e-10;
    }
    
    /**
     * 方法4：查表法（适合多次调用）
     * 预计算所有32位有符号整数范围内的2的幂
     * 时间复杂度：O(1)
     * 空间复杂度：O(32)
     */
    private static final int[] POWER_OF_TWO_TABLE;
    
    static {
        // 预计算所有2的幂（32位有符号整数范围内）
        POWER_OF_TWO_TABLE = new int[32];
        for (int i = 0; i < 32; i++) {
            POWER_OF_TWO_TABLE[i] = 1 << i;
        }
    }
    
    public boolean isPowerOfTwo4(int n) {
        if (n <= 0) {
            return false;
        }
        
        // 在预计算表中查找
        for (int power : POWER_OF_TWO_TABLE) {
            if (n == power) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 方法5：利用最大2的幂的约数性质
     * 在32位有符号整数范围内，最大的2的幂是 2^30 = 1073741824
     * 如果n是2的幂，那么最大2的幂应该能被n整除
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfTwo5(int n) {
        return n > 0 && (1073741824 % n == 0);
    }
    
    /**
     * 方法6：利用Integer.bitCount方法
     * 检查1的个数是否为1
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfTwo6(int n) {
        return n > 0 && Integer.bitCount(n) == 1;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code29_PowerOfTwo solution = new Code29_PowerOfTwo();
        
        // 测试用例1：正常情况（是2的幂）
        int n1 = 16;
        boolean result1 = solution.isPowerOfTwo1(n1);
        boolean result2 = solution.isPowerOfTwo2(n1);
        boolean result3 = solution.isPowerOfTwo3(n1);
        boolean result4 = solution.isPowerOfTwo4(n1);
        boolean result5 = solution.isPowerOfTwo5(n1);
        boolean result6 = solution.isPowerOfTwo6(n1);
        System.out.println("测试用例1 - 输入: " + n1 + " (是2的幂)");
        System.out.println("方法1结果: " + result1 + " (预期: true)");
        System.out.println("方法2结果: " + result2 + " (预期: true)");
        System.out.println("方法3结果: " + result3 + " (预期: true)");
        System.out.println("方法4结果: " + result4 + " (预期: true)");
        System.out.println("方法5结果: " + result5 + " (预期: true)");
        System.out.println("方法6结果: " + result6 + " (预期: true)");
        
        // 测试用例2：正常情况（不是2的幂）
        int n2 = 18;
        boolean result7 = solution.isPowerOfTwo2(n2);
        System.out.println("测试用例2 - 输入: " + n2 + " (不是2的幂)");
        System.out.println("方法2结果: " + result7 + " (预期: false)");
        
        // 测试用例3：边界情况（0）
        int n3 = 0;
        boolean result8 = solution.isPowerOfTwo2(n3);
        System.out.println("测试用例3 - 输入: " + n3);
        System.out.println("方法2结果: " + result8 + " (预期: false)");
        
        // 测试用例4：边界情况（负数）
        int n4 = -8;
        boolean result9 = solution.isPowerOfTwo2(n4);
        System.out.println("测试用例4 - 输入: " + n4);
        System.out.println("方法2结果: " + result9 + " (预期: false)");
        
        // 测试用例5：边界情况（1）
        int n5 = 1;
        boolean result10 = solution.isPowerOfTwo2(n5);
        System.out.println("测试用例5 - 输入: " + n5);
        System.out.println("方法2结果: " + result10 + " (预期: true)");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 循环除法:");
        System.out.println("  时间复杂度: O(log n)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 位运算技巧:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法3 - 数学方法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法4 - 查表法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(32)");
        
        System.out.println("方法5 - 约数性质:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法6 - bitCount方法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 方法选择：");
        System.out.println("   - 实际工程：方法2（位运算）最优");
        System.out.println("   - 面试场景：方法2（位运算）最优");
        System.out.println("   - 多次调用：方法4（查表法）最优");
        System.out.println("2. 边界处理：必须检查n>0");
        System.out.println("3. 性能优化：位运算最快，只需一次操作");
        System.out.println("4. 可读性：方法2代码简洁易懂");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. n & (n-1) 技巧：判断是否只有一个1");
        System.out.println("2. 位运算性质：2的幂的二进制特性");
        System.out.println("3. 查表优化：预计算所有可能值");
        System.out.println("4. 数学性质：利用对数运算");
        System.out.println("5. 边界情况：0和负数都不是2的幂");
        
        // 应用场景
        System.out.println("\n=== 应用场景 ===");
        System.out.println("1. 内存分配：判断是否是2的幂以便对齐");
        System.out.println("2. 哈希表：选择合适的大小（2的幂）");
        System.out.println("3. 位图操作：处理2的幂相关的位操作");
        System.out.println("4. 性能优化：利用2的幂的数学特性");
    }
}
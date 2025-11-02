package class031;

/**
 * 4的幂（位运算解法）
 * 测试链接：https://leetcode.cn/problems/power-of-four/
 * 
 * 题目描述：
 * 给定一个整数，写一个函数来判断它是否是 4 的幂次方。如果是，返回 true；否则，返回 false。
 * 整数 n 是 4 的幂次方需满足：存在整数 x 使得 n == 4^x
 * 
 * 示例：
 * 输入：n = 16
 * 输出：true
 * 
 * 输入：n = 5
 * 输出：false
 * 
 * 输入：n = 1
 * 输出：true
 * 
 * 提示：
 * -2^31 <= n <= 2^31 - 1
 * 
 * 解题思路：
 * 1. 数学方法：循环除以4
 * 2. 位运算法：利用4的幂的二进制特性
 * 3. 对数方法：使用对数函数判断
 * 4. 位运算优化：结合2的幂和特殊位置判断
 * 5. 查表法：预计算所有4的幂
 * 
 * 时间复杂度分析：
 * - 数学方法：O(log₄n)
 * - 位运算法：O(1)
 * - 对数方法：O(1)
 * - 位运算优化：O(1)
 * - 查表法：O(1)
 * 
 * 空间复杂度分析：
 * - 数学方法：O(1)
 * - 位运算法：O(1)
 * - 对数方法：O(1)
 * - 位运算优化：O(1)
 * - 查表法：O(32) = O(1)
 */
public class Code39_PowerofFour {
    
    /**
     * 方法1：数学方法（循环除以4）
     * 时间复杂度：O(log₄n)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfFour1(int n) {
        if (n <= 0) {
            return false;
        }
        
        while (n % 4 == 0) {
            n /= 4;
        }
        
        return n == 1;
    }
    
    /**
     * 方法2：位运算法（推荐）
     * 核心思想：4的幂一定是2的幂，且1出现在奇数位
     * 4的幂的二进制特点：
     * 1. 只有一个1（是2的幂）
     * 2. 1出现在奇数位（从1开始计数）
     * 3. 满足 n & (n-1) == 0 且 n & 0x55555555 != 0
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfFour2(int n) {
        // 检查n是否为正数，且是2的幂，且1出现在奇数位
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    /**
     * 方法3：对数方法
     * 使用换底公式：log₄n = logn / log4
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfFour3(int n) {
        if (n <= 0) {
            return false;
        }
        
        double logResult = Math.log(n) / Math.log(4);
        // 检查结果是否为整数（考虑浮点数精度）
        return Math.abs(logResult - Math.round(logResult)) < 1e-10;
    }
    
    /**
     * 方法4：位运算优化版
     * 结合多种位运算技巧
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfFour4(int n) {
        if (n <= 0) {
            return false;
        }
        
        // 检查是否是2的幂
        if ((n & (n - 1)) != 0) {
            return false;
        }
        
        // 检查1是否出现在奇数位
        // 0x55555555 = 01010101010101010101010101010101
        return (n & 0x55555555) != 0;
    }
    
    /**
     * 方法5：查表法
     * 预计算所有32位整数范围内的4的幂
     * 时间复杂度：O(1)
     * 空间复杂度：O(16) = O(1)
     */
    public boolean isPowerOfFour5(int n) {
        // 32位整数范围内所有4的幂
        int[] powersOfFour = {
            1,          // 4^0
            4,          // 4^1
            16,         // 4^2
            64,         // 4^3
            256,        // 4^4
            1024,       // 4^5
            4096,       // 4^6
            16384,      // 4^7
            65536,      // 4^8
            262144,     // 4^9
            1048576,    // 4^10
            4194304,    // 4^11
            16777216,   // 4^12
            67108864,   // 4^13
            268435456,  // 4^14
            1073741824  // 4^15
        };
        
        for (int power : powersOfFour) {
            if (n == power) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 方法6：递归方法
     * 时间复杂度：O(log₄n)
     * 空间复杂度：O(log₄n) - 递归栈深度
     */
    public boolean isPowerOfFour6(int n) {
        if (n <= 0) {
            return false;
        }
        if (n == 1) {
            return true;
        }
        if (n % 4 != 0) {
            return false;
        }
        return isPowerOfFour6(n / 4);
    }
    
    /**
     * 方法7：位运算+数学验证
     * 结合位运算和数学验证
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfFour7(int n) {
        // 检查n是否为正数且是2的幂
        if (n <= 0 || (n & (n - 1)) != 0) {
            return false;
        }
        
        // 4的幂除以3余数为1
        // 2的幂但不是4的幂除以3余数为2
        return n % 3 == 1;
    }
    
    /**
     * 方法8：位计数法
     * 统计1后面的0的个数，检查是否为偶数
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean isPowerOfFour8(int n) {
        if (n <= 0) {
            return false;
        }
        
        // 检查是否是2的幂
        if ((n & (n - 1)) != 0) {
            return false;
        }
        
        // 统计末尾0的个数（从最低位开始）
        int count = 0;
        while (n > 1) {
            n >>= 1;
            count++;
        }
        
        // 4的幂要求0的个数为偶数
        return count % 2 == 0;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code39_PowerofFour solution = new Code39_PowerofFour();
        
        // 测试用例1：4的幂
        int n1 = 16;
        boolean result1 = solution.isPowerOfFour2(n1);
        System.out.println("测试用例1 - 输入: " + n1);
        System.out.println("结果: " + result1 + " (预期: true)");
        
        // 测试用例2：不是4的幂
        int n2 = 5;
        boolean result2 = solution.isPowerOfFour2(n2);
        System.out.println("测试用例2 - 输入: " + n2);
        System.out.println("结果: " + result2 + " (预期: false)");
        
        // 测试用例3：1（4^0）
        int n3 = 1;
        boolean result3 = solution.isPowerOfFour2(n3);
        System.out.println("测试用例3 - 输入: " + n3);
        System.out.println("结果: " + result3 + " (预期: true)");
        
        // 测试用例4：2的幂但不是4的幂
        int n4 = 2;
        boolean result4 = solution.isPowerOfFour2(n4);
        System.out.println("测试用例4 - 输入: " + n4);
        System.out.println("结果: " + result4 + " (预期: false)");
        
        // 测试用例5：边界情况（0）
        int n5 = 0;
        boolean result5 = solution.isPowerOfFour2(n5);
        System.out.println("测试用例5 - 输入: " + n5);
        System.out.println("结果: " + result5 + " (预期: false)");
        
        // 测试用例6：负数
        int n6 = -16;
        boolean result6 = solution.isPowerOfFour2(n6);
        System.out.println("测试用例6 - 输入: " + n6);
        System.out.println("结果: " + result6 + " (预期: false)");
        
        // 所有方法结果对比
        System.out.println("\n=== 所有方法结果对比 ===");
        int testNum = 64;  // 4^3
        System.out.println("测试数字: " + testNum + " (二进制: " + Integer.toBinaryString(testNum) + ")");
        System.out.println("方法1 (数学方法): " + solution.isPowerOfFour1(testNum));
        System.out.println("方法2 (位运算法): " + solution.isPowerOfFour2(testNum));
        System.out.println("方法3 (对数方法): " + solution.isPowerOfFour3(testNum));
        System.out.println("方法4 (位运算优化): " + solution.isPowerOfFour4(testNum));
        System.out.println("方法5 (查表法): " + solution.isPowerOfFour5(testNum));
        System.out.println("方法6 (递归方法): " + solution.isPowerOfFour6(testNum));
        System.out.println("方法7 (位运算+数学): " + solution.isPowerOfFour7(testNum));
        System.out.println("方法8 (位计数法): " + solution.isPowerOfFour8(testNum));
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 数学方法:");
        System.out.println("  时间复杂度: O(log₄n)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 位运算法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法3 - 对数方法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法4 - 位运算优化:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法5 - 查表法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(16) = O(1)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 算法选择：方法2 (位运算法) 最优");
        System.out.println("2. 性能优化：避免循环和函数调用");
        System.out.println("3. 边界处理：处理0和负数");
        System.out.println("4. 可读性：清晰的位运算逻辑");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 4的幂特性：是2的幂且1在奇数位");
        System.out.println("2. 位运算技巧：n & (n-1) 判断2的幂");
        System.out.println("3. 掩码应用：0x55555555 检查奇数位");
        System.out.println("4. 数学特性：4的幂除以3余数为1");
        
        // 二进制特性分析
        System.out.println("\n=== 二进制特性分析 ===");
        System.out.println("4的幂的二进制表示特点：");
        System.out.println("  4^0 = 1: 二进制 1");
        System.out.println("  4^1 = 4: 二进制 100");
        System.out.println("  4^2 = 16: 二进制 10000");
        System.out.println("  4^3 = 64: 二进制 1000000");
        System.out.println("规律：1后面跟着偶数个0");
        
        // 位运算验证
        System.out.println("\n=== 位运算验证 ===");
        int[] testCases = {1, 4, 16, 64, 256};
        for (int num : testCases) {
            boolean isPowerOf2 = (num & (num - 1)) == 0;
            boolean isPowerOf4 = (num & 0x55555555) != 0;
            System.out.println("数字: " + num + ", 是2的幂: " + isPowerOf2 + ", 是4的幂: " + isPowerOf4);
        }
    }
}
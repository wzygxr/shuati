/**
 * 洛谷 P2054 [AHOI2005] 洗牌
 * 题目要求：模拟洗牌过程，查询最终位置
 * 核心技巧：分块优化模拟
 * 时间复杂度：O(√n) / 操作
 * 测试链接：https://www.luogu.com.cn/problem/P2054
 * 
 * 算法思想详解：
 * 1. 观察洗牌过程的数学规律
 * 2. 直接模拟洗牌会超时，需要找到数学规律
 * 3. 对于大次数的洗牌操作，可以利用数学公式快速计算位置
 * 4. 分块处理大次数洗牌，优化性能
 */

import java.util.Scanner;

public class Code36_LuoguP2054_Java {
    private long n; // 牌的数量（偶数）
    private long m; // 洗牌次数
    private long pos; // 目标牌的初始位置
    
    /**
     * 构造函数，初始化问题参数
     */
    public Code36_LuoguP2054_Java(long n, long m, long pos) {
        this.n = n;
        this.m = m;
        this.pos = pos;
    }
    
    /**
     * 计算一次洗牌后的位置
     * 
     * @param x 当前位置
     * @return 洗牌后的位置
     */
    public long getNextPosition(long x) {
        if (x <= n / 2) {
            // 前半部分的牌会被放到位置 2x-1
            return 2 * x - 1;
        } else {
            // 后半部分的牌会被放到位置 2(x - n/2)
            return 2 * (x - n / 2);
        }
    }
    
    /**
     * 暴力模拟洗牌过程（用于小数组测试）
     * 
     * @return 最终位置
     */
    public long bruteForce() {
        long current = pos;
        for (long i = 0; i < m; i++) {
            current = getNextPosition(current);
        }
        return current;
    }
    
    /**
     * 数学优化解法 - 利用模运算快速计算
     * 
     * @return 最终位置
     */
    public long mathematicalSolution() {
        // 观察数学规律：每次洗牌相当于位置乘以2 mod (n+1)
        // 因此m次洗牌相当于乘以 2^m mod (n+1)
        long result = powMod(2, m, n + 1) * pos % (n + 1);
        // 如果余数为0，则位置为n
        return result == 0 ? n : result;
    }
    
    /**
     * 分块优化解法 - 适用于超大规模数据
     * 
     * @return 最终位置
     */
    public long blockOptimizedSolution() {
        // 对于这个问题，数学解法已经是最优的，分块优化主要体现在快速幂的实现上
        return mathematicalSolution();
    }
    
    /**
     * 快速幂取模运算
     * 
     * @param base 底数
     * @param exponent 指数
     * @param mod 模数
     * @return (base^exponent) mod mod
     */
    public long powMod(long base, long exponent, long mod) {
        long result = 1;
        base = base % mod; // 先取模避免溢出
        
        while (exponent > 0) {
            if ((exponent & 1) == 1) { // 如果exponent是奇数
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent >>= 1; // 右移一位，相当于除以2取整
        }
        
        return result;
    }
    
    /**
     * 运行标准测试
     */
    public static void runTest() {
        Scanner scanner = new Scanner(System.in);
        
        long n = scanner.nextLong();
        long m = scanner.nextLong();
        long pos = scanner.nextLong();
        
        Code36_LuoguP2054_Java solution = new Code36_LuoguP2054_Java(n, m, pos);
        
        // 根据数据规模选择合适的解法
        long result;
        if (n <= 1000 && m <= 1000) { // 小规模数据，使用暴力解法验证
            result = solution.bruteForce();
        } else {
            result = solution.mathematicalSolution();
        }
        
        System.out.println(result);
        
        scanner.close();
    }
    
    /**
     * 验证两种解法结果一致性的测试
     */
    public static void consistencyTest() {
        System.out.println("=== 一致性测试 ===");
        
        // 测试用例
        long[][] testCases = {
            {6, 1, 2},  // 6张牌，洗1次，初始位置2
            {6, 2, 2},  // 6张牌，洗2次，初始位置2
            {8, 3, 5},  // 8张牌，洗3次，初始位置5
            {10, 1, 6}  // 10张牌，洗1次，初始位置6
        };
        
        for (long[] test : testCases) {
            long n = test[0];
            long m = test[1];
            long pos = test[2];
            
            Code36_LuoguP2054_Java solution = new Code36_LuoguP2054_Java(n, m, pos);
            
            long brute = solution.bruteForce();
            long math = solution.mathematicalSolution();
            
            System.out.printf("n=%d, m=%d, pos=%d => 暴力结果=%d, 数学结果=%d, 一致=%s\n", 
                n, m, pos, brute, math, brute == math ? "是" : "否");
        }
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 测试不同规模的数据
        long[][] testCases = {
            {1000, 1000},               // 小规模
            {1000000, 1000000},         // 中等规模
            {1000000000, 1000000000L}   // 大规模
        };
        
        for (long[] test : testCases) {
            long n = test[0];
            long m = test[1];
            long pos = 1; // 任意位置
            
            Code36_LuoguP2054_Java solution = new Code36_LuoguP2054_Java(n, m, pos);
            
            long startTime = System.currentTimeMillis();
            long result = solution.mathematicalSolution();
            long endTime = System.currentTimeMillis();
            
            System.out.printf("n=%d, m=%d => 耗时: %d ms, 结果=%d\n", 
                n, m, endTime - startTime, result);
        }
    }
    
    /**
     * 原理解释演示
     */
    public static void principleDemo() {
        System.out.println("=== 洗牌原理解释 ===");
        System.out.println("洗牌过程：");
        System.out.println("假设n=6张牌，初始位置为1,2,3,4,5,6");
        System.out.println("洗牌后变为：1,4,2,5,3,6");
        System.out.println("\n位置映射规律：");
        System.out.println("前半部分(1-3)：位置x → 2x-1");
        System.out.println("后半部分(4-6)：位置x → 2(x-3)");
        
        System.out.println("\n数学规律推导：");
        System.out.println("对于n=6，观察各位置的变化：");
        for (long pos = 1; pos <= 6; pos++) {
            Code36_LuoguP2054_Java solution = new Code36_LuoguP2054_Java(6, 1, pos);
            long next = solution.getNextPosition(pos);
            long math = (2 * pos) % 7; // 7 = n + 1
            if (math == 0) math = 7;
            System.out.printf("位置%d → %d (数学计算: 2*%d mod 7 = %d)\n", 
                pos, next, pos, math);
        }
        
        System.out.println("\n结论：每次洗牌相当于位置乘以2 mod (n+1)");
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        System.out.println("洛谷 P2054 [AHOI2005] 洗牌 解决方案");
        System.out.println("1. 运行标准测试（按题目输入格式）");
        System.out.println("2. 运行一致性测试");
        System.out.println("3. 运行性能测试");
        System.out.println("4. 查看原理解释");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("请选择测试类型：");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        switch (choice) {
            case 1:
                runTest();
                break;
            case 2:
                consistencyTest();
                break;
            case 3:
                performanceTest();
                break;
            case 4:
                principleDemo();
                break;
            default:
                System.out.println("无效选择，运行原理解释");
                principleDemo();
                break;
        }
        
        scanner.close();
    }
    
    /**
     * 时间复杂度分析：
     * - 暴力解法：O(m)，其中m是洗牌次数
     * - 数学解法：O(log m)，主要是快速幂的时间复杂度
     * - 分块优化解法：O(log m)，与数学解法相同
     * 
     * 空间复杂度分析：
     * - 所有解法：O(1)，只需要常量级额外空间
     * 
     * 优化技巧：
     * 1. 数学规律发现：将复杂的位置变换转换为简单的模运算
     * 2. 快速幂算法：高效计算大指数幂取模
     * 3. 溢出处理：在乘法运算中及时取模避免溢出
     * 
     * 题目本质：
     * 这道题的关键在于发现洗牌操作的数学规律，而不是真正进行模拟
     * 这体现了在算法设计中，寻找数学规律往往比直接模拟更高效
     * 时间复杂度从O(m)降低到O(log m)，适用于非常大的m值
     */
}
package class146_CombinatorialAndMathematicalAlgorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 约瑟夫环问题算法实现
 * 经典约瑟夫问题：n个人围成一圈，每次数到k的人出列，求最后剩下的人的位置
 * 
 * 适用场景：
 * - 循环淘汰问题
 * - 环状结构中的选择问题
 * - 递推算法的典型应用
 * 
 * 相关题目:
 * 1. LeetCode 390. Elimination Game (消除游戏)
 *    链接: https://leetcode.cn/problems/elimination-game/
 * 2. LeetCode 1823. Find the Winner of the Circular Game (找出游戏的获胜者)
 *    链接: https://leetcode.cn/problems/find-the-winner-of-the-circular-game/
 * 3. POJ 1012 Joseph
 *    链接: http://poj.org/problem?id=1012
 * 4. POJ 2886 Who Gets the Most Candies?
 *    链接: http://poj.org/problem?id=2886
 * 5. Luogu P1996 约瑟夫问题
 *    链接: https://www.luogu.com.cn/problem/P1996
 */
public class Code03_Joseph {

    /**
     * 使用递推公式求解约瑟夫环问题的最优解
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 递推公式：f(n,k) = (f(n-1,k) + k) % n
     * 其中f(n,k)表示n个人数k时最后剩下的人的索引（从0开始）
     * 这里返回的是从1开始计数的结果
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的位置（从1开始计数）
     * @throws IllegalArgumentException 当参数不合法时抛出异常
     */
    public static int compute(int n, int k) {
        // 参数校验
        if (n <= 0 || k <= 0) {
            throw new IllegalArgumentException("n和k必须为正整数");
        }
        
        // 特殊情况优化：当k=1时，最后剩下的是第n个人
        if (k == 1) {
            return n;
        }
        
        // 特殊情况优化：当n=1时，只剩一个人，就是他自己
        if (n == 1) {
            return 1;
        }
        
        // 使用递推法求解
        // 初始条件：当只有1个人时，位置就是1
        int ans = 1;
        
        // 从2个人开始递推，直到n个人
        for (int c = 2; c <= n; c++) {
            // 递推公式：新位置 = (旧位置 + k - 1) % 当前人数 + 1
            // +k-1是因为数到第k个人，-1是为了从0开始计算
            // %c是为了处理环形结构
            // +1是为了将结果转换回从1开始计数
            ans = (ans + k - 1) % c + 1;
        }
        
        return ans;
    }
    
    /**
     * 使用递推公式（索引从0开始）
     * 这是标准的约瑟夫环递推公式实现
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的索引（从0开始）
     * @throws IllegalArgumentException 当参数不合法时抛出异常
     */
    public static int josephus0Based(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw new IllegalArgumentException("n和k必须为正整数");
        }
        
        int res = 0; // f(1) = 0
        for (int i = 2; i <= n; i++) {
            res = (res + k) % i;
        }
        return res;
    }
    
    /**
     * 使用模拟法求解约瑟夫环问题
     * 适用于小数据量，直观但效率较低
     * 时间复杂度: O(nk)
     * 空间复杂度: O(n)
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的位置（从1开始计数）
     * @throws IllegalArgumentException 当参数不合法时抛出异常
     */
    public static int simulate(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw new IllegalArgumentException("n和k必须为正整数");
        }
        
        // 创建列表存储所有人的位置
        List<Integer> people = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            people.add(i);
        }
        
        int index = 0; // 当前开始计数的位置
        
        // 不断删除出列的人，直到只剩一个人
        while (people.size() > 1) {
            // 计算要删除的人的位置
            // (index + k - 1) % people.size() 确保在列表范围内循环
            index = (index + k - 1) % people.size();
            people.remove(index);
        }
        
        // 返回最后剩下的人的位置
        return people.get(0);
    }
    
    /**
     * 递归求解约瑟夫环问题
     * 适用于理解算法原理，但对于大n可能导致栈溢出
     * 时间复杂度: O(n)
     * 空间复杂度: O(n) 递归调用栈
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的索引（从0开始）
     * @throws IllegalArgumentException 当参数不合法时抛出异常
     * @throws StackOverflowError 当递归深度过大时抛出栈溢出异常
     */
    public static int recursive(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw new IllegalArgumentException("n和k必须为正整数");
        }
        
        // 基本情况：只有一个人时，索引为0
        if (n == 1) {
            return 0;
        }
        
        // 递推公式：f(n,k) = (f(n-1,k) + k) % n
        return (recursive(n - 1, k) + k) % n;
    }
    
    /**
     * 优化的约瑟夫环算法，当k远小于n时可以进一步优化
     * 时间复杂度: O(n) 最坏情况，但在k较小的情况下性能更好
     * 
     * @param n 总人数
     * @param k 每次数到k的人出列
     * @return 最后剩下的人的位置（从1开始计数）
     * @throws IllegalArgumentException 当参数不合法时抛出异常
     */
    public static int optimizedJosephus(int n, int k) {
        if (n <= 0 || k <= 0) {
            throw new IllegalArgumentException("n和k必须为正整数");
        }
        
        // 当k=1时，最后剩下的是第n个人
        if (k == 1) {
            return n;
        }
        
        // 当k较大时，使用标准递推
        if (k > n) {
            return compute(n, k % n == 0 ? n : k % n);
        }
        
        int res = 0;
        for (int i = 2; i <= n; i++) {
            if (res + k < i) {
                // 可以跳过多个步骤
                res += k;
            } else {
                res = (res + k) % i;
            }
        }
        
        return res + 1; // 转换为从1开始计数
    }
    
    /**
     * 约瑟夫环问题的完整单元测试
     * 测试各种边界情况和异常场景
     */
    public static void runUnitTests() {
        System.out.println("=== 约瑟夫环算法单元测试 ===");
        
        // 基础测试用例
        int[][] testCases = {
            {1, 1, 1},   // n=1, k=1, 结果=1
            {5, 2, 3},   // 经典约瑟夫环
            {7, 3, 4},   // 标准测试
            {10, 2, 5},  // 偶数人数
            {10, 3, 4},  // 奇数步长
            {100, 10, 26}, // 大数据量
            {1000, 7, 609} // 更大数据量
        };
        
        boolean allPassed = true;
        
        // 测试递推算法
        System.out.println("
1. 测试递推算法:");
        for (int[] testCase : testCases) {
            int n = testCase[0];
            int k = testCase[1];
            int expected = testCase[2];
            int actual = compute(n, k);
            
            if (actual == expected) {
                System.out.printf("✅ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
            } else {
                System.out.printf("❌ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
                allPassed = false;
            }
        }
        
        // 测试模拟算法
        System.out.println("
2. 测试模拟算法:");
        for (int[] testCase : testCases) {
            if (testCase[0] <= 1000) { // 只测试小数据量
                int n = testCase[0];
                int k = testCase[1];
                int expected = testCase[2];
                int actual = simulate(n, k);
                
                if (actual == expected) {
                    System.out.printf("✅ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
                } else {
                    System.out.printf("❌ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
                    allPassed = false;
                }
            }
        }
        
        // 测试递归算法
        System.out.println("
3. 测试递归算法:");
        for (int[] testCase : testCases) {
            if (testCase[0] <= 100) { // 防止栈溢出
                int n = testCase[0];
                int k = testCase[1];
                int expected = testCase[2];
                int actual = recursive(n, k) + 1; // 转换为从1开始
                
                if (actual == expected) {
                    System.out.printf("✅ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
                } else {
                    System.out.printf("❌ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
                    allPassed = false;
                }
            }
        }
        
        // 测试优化算法
        System.out.println("
4. 测试优化算法:");
        for (int[] testCase : testCases) {
            int n = testCase[0];
            int k = testCase[1];
            int expected = testCase[2];
            int actual = optimizedJosephus(n, k);
            
            if (actual == expected) {
                System.out.printf("✅ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
            } else {
                System.out.printf("❌ n=%d, k=%d: 期望=%d, 实际=%d
", n, k, expected, actual);
                allPassed = false;
            }
        }
        
        // 边界情况测试
        System.out.println("
5. 边界情况测试:");
        
        // 测试n=1的各种k值
        for (int k = 1; k <= 10; k++) {
            int result = compute(1, k);
            if (result == 1) {
                System.out.printf("✅ n=1, k=%d: 结果=1
", k);
            } else {
                System.out.printf("❌ n=1, k=%d: 期望=1, 实际=%d
", k, result);
                allPassed = false;
            }
        }
        
        // 测试k=1的各种n值
        for (int n = 1; n <= 10; n++) {
            int result = compute(n, 1);
            if (result == n) {
                System.out.printf("✅ n=%d, k=1: 结果=%d
", n, result);
            } else {
                System.out.printf("❌ n=%d, k=1: 期望=%d, 实际=%d
", n, n, result);
                allPassed = false;
            }
        }
        
        // 异常输入测试
        System.out.println("
6. 异常输入测试:");
        
        try {
            compute(0, 5);
            System.out.println("❌ 应抛出异常但未抛出");
            allPassed = false;
        } catch (IllegalArgumentException e) {
            System.out.println("✅ 正确检测到n=0异常");
        }
        
        try {
            compute(5, 0);
            System.out.println("❌ 应抛出异常但未抛出");
            allPassed = false;
        } catch (IllegalArgumentException e) {
            System.out.println("✅ 正确检测到k=0异常");
        }
        
        try {
            compute(-1, 5);
            System.out.println("❌ 应抛出异常但未抛出");
            allPassed = false;
        } catch (IllegalArgumentException e) {
            System.out.println("✅ 正确检测到n=-1异常");
        }
        
        System.out.println("
=== 测试结果 ===");
        if (allPassed) {
            System.out.println("🎉 所有测试通过!");
        } else {
            System.out.println("❌ 部分测试失败!");
        }
    }
    
    /**
     * 性能测试：比较不同算法的执行效率
     */
    public static void runPerformanceTests() {
        System.out.println("=== 约瑟夫环算法性能测试 ===");
        
        int[] testSizes = {1000, 10000, 100000, 1000000};
        int k = 3; // 固定步长
        
        for (int n : testSizes) {
            System.out.printf("
测试规模: n=%d, k=%d
", n, k);
            
            // 测试递推算法
            long startTime = System.nanoTime();
            int result1 = compute(n, k);
            long endTime = System.nanoTime();
            System.out.printf("递推算法: 结果=%d, 耗时=%.3fms
", 
                result1, (endTime - startTime) / 1_000_000.0);
            
            // 测试优化算法
            startTime = System.nanoTime();
            int result2 = optimizedJosephus(n, k);
            endTime = System.nanoTime();
            System.out.printf("优化算法: 结果=%d, 耗时=%.3fms
", 
                result2, (endTime - startTime) / 1_000_000.0);
            
            // 验证结果一致性
            if (result1 != result2) {
                System.out.println("❌ 算法结果不一致!");
            } else {
                System.out.println("✅ 算法结果一致");
            }
            
            // 对于小数据量，测试模拟算法
            if (n <= 10000) {
                startTime = System.nanoTime();
                int result3 = simulate(n, k);
                endTime = System.nanoTime();
                System.out.printf("模拟算法: 结果=%d, 耗时=%.3fms
", 
                    result3, (endTime - startTime) / 1_000_000.0);
                
                if (result1 != result3) {
                    System.out.println("❌ 模拟算法结果不一致!");
                }
            }
        }
        
        // 测试不同k值对性能的影响
        System.out.println("
=== 不同k值性能测试 (n=100000) ===");
        int n = 100000;
        int[] kValues = {2, 10, 100, 1000, 10000};
        
        for (int kVal : kValues) {
            long startTime = System.nanoTime();
            int result = compute(n, kVal);
            long endTime = System.nanoTime();
            System.out.printf("k=%d: 结果=%d, 耗时=%.3fms
", 
                kVal, result, (endTime - startTime) / 1_000_000.0);
        }
    }
    
    /**
     * 工程化实践：面试准备和算法应用
     */
    public static void showEngineeringPractices() {
        System.out.println("=== 约瑟夫环算法工程化实践 ===");
        
        System.out.println("
1. 算法复杂度分析:");
        System.out.println("   - 递推算法: O(n) 时间, O(1) 空间");
        System.out.println("   - 模拟算法: O(nk) 时间, O(n) 空间");
        System.out.println("   - 递归算法: O(n) 时间, O(n) 空间（栈）");
        
        System.out.println("
2. 适用场景:");
        System.out.println("   - 递推算法: 大数据量，通用场景");
        System.out.println("   - 模拟算法: 小数据量，直观理解");
        System.out.println("   - 递归算法: 教学演示，小规模数据");
        
        System.out.println("
3. 面试要点:");
        System.out.println("   - 理解递推公式的数学原理");
        System.out.println("   - 能够推导时间复杂度");
        System.out.println("   - 掌握边界条件处理");
        System.out.println("   - 了解不同算法的适用场景");
        
        System.out.println("
4. 常见错误:");
        System.out.println("   - 忘记处理从0开始和从1开始的转换");
        System.out.println("   - 边界条件处理不当（n=1, k=1）");
        System.out.println("   - 模运算处理错误");
        
        System.out.println("
5. 优化技巧:");
        System.out.println("   - 当k=1时直接返回n");
        System.out.println("   - 当k>n时使用模运算优化");
        System.out.println("   - 避免不必要的递归调用");
    }
    
    /**
     * 交互式测试界面
     */
    public static void interactiveTest() {
        System.out.println("=== 约瑟夫环算法交互测试 ===");
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        while (true) {
            System.out.println("
请选择操作:");
            System.out.println("1. 计算约瑟夫环结果");
            System.out.println("2. 运行单元测试");
            System.out.println("3. 运行性能测试");
            System.out.println("4. 查看工程化实践");
            System.out.println("5. 退出");
            
            System.out.print("请输入选择(1-5): ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.print("请输入总人数n: ");
                    int n = scanner.nextInt();
                    System.out.print("请输入步长k: ");
                    int k = scanner.nextInt();
                    scanner.nextLine(); // 消耗换行符
                    
                    try {
                        int result = compute(n, k);
                        System.out.printf("最后剩下的人的位置是: %d
", result);
                        
                        // 显示不同算法的结果对比
                        System.out.println("
不同算法结果对比:");
                        System.out.printf("递推算法: %d
", compute(n, k));
                        System.out.printf("优化算法: %d
", optimizedJosephus(n, k));
                        
                        if (n <= 1000) {
                            System.out.printf("模拟算法: %d
", simulate(n, k));
                        }
                        
                        if (n <= 100) {
                            System.out.printf("递归算法: %d
", recursive(n, k) + 1);
                        }
                    } catch (Exception e) {
                        System.out.println("计算错误: " + e.getMessage());
                    }
                    break;
                    
                case "2":
                    runUnitTests();
                    break;
                    
                case "3":
                    runPerformanceTests();
                    break;
                    
                case "4":
                    showEngineeringPractices();
                    break;
                    
                case "5":
                    System.out.println("感谢使用，再见!");
                    return;
                    
                default:
                    System.out.println("无效选择，请重新输入");
            }
        }
    }
    
    /**
     * 主函数：支持命令行参数和交互式模式
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            // 命令行模式
            switch (args[0]) {
                case "--test":
                    runUnitTests();
                    break;
                case "--performance":
                    runPerformanceTests();
                    break;
                case "--interactive":
                    interactiveTest();
                    break;
                case "--help":
                    System.out.println("约瑟夫环算法使用说明:");
                    System.out.println("  --test: 运行单元测试");
                    System.out.println("  --performance: 运行性能测试");
                    System.out.println("  --interactive: 启动交互模式");
                    System.out.println("  无参数: 读取标准输入进行计算");
                    break;
                default:
                    // 尝试解析为n和k
                    try {
                        int n = Integer.parseInt(args[0]);
                        int k = Integer.parseInt(args[1]);
                        int result = compute(n, k);
                        System.out.println(result);
                    } catch (Exception e) {
                        System.out.println("参数错误，使用 --help 查看帮助");
                    }
            }
        } else {
            // 标准输入模式
            try {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                System.out.print("请输入总人数n: ");
                int n = scanner.nextInt();
                System.out.print("请输入步长k: ");
                int k = scanner.nextInt();
                
                int result = compute(n, k);
                System.out.printf("最后剩下的人的位置是: %d
", result);
                
                // 显示算法复杂度信息
                System.out.println("
算法复杂度分析:");
                System.out.println("时间复杂度: O(n)");
                System.out.println("空间复杂度: O(1)");
                System.out.println("算法类型: 递推算法（最优解）");
                
            } catch (Exception e) {
                System.out.println("输入错误: " + e.getMessage());
                System.out.println("启动交互模式...");
                interactiveTest();
            }
        }
    }
}
    
    /**
     * 运行性能测试，比较不同实现方法的效率
     */
    public static void runPerformanceTest() {
        // 性能测试用例
        int[][] testCases = {
            {5, 3},        // 小数据量基本测试
            {100, 5},      // 中等数据量
            {1000, 10},    // 较大数据量
            {10000, 100}   // 大数据量
        };
        
        System.out.println("性能测试结果:");
        printSeparator(60);
        System.out.printf("%15s%15s%15s%15s\n", "测试用例", "递推法(ms)", "模拟法(ms)", "优化法(ms)");
        printSeparator(60);
        
        for (int[] testCase : testCases) {
            int n = testCase[0];
            int k = testCase[1];
            
            // 测试递推法
            long startTime = System.currentTimeMillis();
            int res1 = compute(n, k);
            long recursiveTime = System.currentTimeMillis() - startTime;
            
            // 只在小数据量时测试模拟法
            String simulateTimeStr = "-";
            if (n <= 10000) {
                startTime = System.currentTimeMillis();
                int res2 = simulate(n, k);
                simulateTimeStr = String.valueOf(System.currentTimeMillis() - startTime);
            }
            
            // 测试优化法
            startTime = System.currentTimeMillis();
            int res3 = optimizedJosephus(n, k);
            long optimizedTime = System.currentTimeMillis() - startTime;
            
            System.out.printf("(n=%d,k=%d)%5s%12d%15s%12d\n", 
                             n, k, "", recursiveTime, simulateTimeStr, optimizedTime);
        }
        
        printSeparator(60);
    }
    
    /**
     * 运行正确性测试，验证所有实现方法的结果一致性
     */
    public static void runCorrectnessTest() {
        // 正确性测试用例: {n, k, expected}
        int[][] testCases = {
            {1, 1, 1},     // n=1特殊情况
            {5, 3, 2},     // 经典示例
            {10, 2, 5},    // 常见测试用例
            {7, 3, 4},     // 另一个示例
            {1, 100, 1},   // k远大于n的情况
            {10, 1, 10}    // k=1的特殊情况
        };
        
        System.out.println("正确性测试结果:");
        printSeparator(80);
        System.out.printf("%15s%10s%10s%15s%10s%10s\n", 
                         "测试用例", "预期结果", "递推法", "递推法(0基)", "模拟法", "优化法");
        printSeparator(80);
        
        boolean allPassed = true;
        
        for (int[] testCase : testCases) {
            int n = testCase[0];
            int k = testCase[1];
            int expected = testCase[2];
            
            try {
                int res1 = compute(n, k);
                int res2 = josephus0Based(n, k) + 1; // 转换为从1开始
                int res3 = n <= 10000 ? simulate(n, k) : res1; // 大数据量跳过模拟法
                int res4 = optimizedJosephus(n, k);
                
                // 检查结果是否正确
                boolean passed1 = res1 == expected;
                boolean passed2 = res2 == expected;
                boolean passed3 = res3 == expected;
                boolean passed4 = res4 == expected;
                
                boolean currentTestPassed = passed1 && passed2 && passed3 && passed4;
                String status = currentTestPassed ? "✓" : "✗";
                
                System.out.printf("(n=%d,k=%d)%5d%10d%10d%15d%10d%10d%2s\n", 
                                 n, k, "", expected, res1, res2, 
                                 (n <= 10000 ? res3 : -1), res4, status);
                
                if (!currentTestPassed) {
                    allPassed = false;
                }
                
            } catch (Exception e) {
                System.out.printf("(n=%d,k=%d)%5s测试出错: %s\n", n, k, "", e.getMessage());
                allPassed = false;
            }
        }
        
        printSeparator(80);
        System.out.println("整体测试结果: " + (allPassed ? "全部通过" : "存在错误"));
    }
    
    /**
     * 打印分隔线
     * @param length 分隔线长度
     */
    private static void printSeparator(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("=");
        }
        System.out.println();
    }
    
    /**
     * 主函数，提供命令行界面和测试功能
     * @param args 命令行参数，支持n和k参数
     */
    public static void main(String[] args) {
        // 支持命令行参数
        int n = -1;
        int k = -1;
        
        // 从命令行参数解析
        if (args.length == 2) {
            try {
                n = Integer.parseInt(args[0]);
                k = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("命令行参数格式错误，请输入两个整数 n 和 k");
                printUsage();
                return;
            }
        }
        
        // 如果命令行没有提供参数，从标准输入读取
        if (n == -1 || k == -1) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("请输入约瑟夫环问题参数：");
                
                // 读取n
                while (n <= 0) {
                    System.out.print("总人数n (1-1000000): ");
                    try {
                        n = Integer.parseInt(reader.readLine().trim());
                        if (n <= 0) {
                            System.out.println("错误: n必须为正整数");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("错误: 请输入有效的整数");
                    }
                }
                
                // 读取k
                while (k <= 0) {
                    System.out.print("报数k (1-1000000): ");
                    try {
                        k = Integer.parseInt(reader.readLine().trim());
                        if (k <= 0) {
                            System.out.println("错误: k必须为正整数");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("错误: 请输入有效的整数");
                    }
                }
            } catch (IOException e) {
                System.out.println("输入错误: " + e.getMessage());
                return;
            }
        }
        
        try {
            // 计算并输出结果
            long startTime = System.currentTimeMillis();
            int result = compute(n, k);
            long endTime = System.currentTimeMillis();
            
            System.out.println("\n计算结果：");
            System.out.println("最后剩下的人的位置是: " + result);
            System.out.println("计算耗时: " + (endTime - startTime) + " ms");
            
            // 测试其他实现方法
            System.out.println("\n不同实现方法结果对比：");
            System.out.println("递推法结果(从0开始): " + josephus0Based(n, k));
            
            // 只在小数据量时测试模拟法，避免超时
            if (n <= 10000) {
                startTime = System.currentTimeMillis();
                int simulateResult = simulate(n, k);
                endTime = System.currentTimeMillis();
                System.out.println("模拟法结果: " + simulateResult + "，耗时: " + (endTime - startTime) + " ms");
            } else {
                System.out.println("模拟法对于大数据量n=" + n + "可能耗时较长，跳过测试");
            }
            
            // 只在小数据量时测试递归法，避免栈溢出
            if (n <= 1000) {
                try {
                    startTime = System.currentTimeMillis();
                    int recursiveResult = recursive(n, k) + 1; // 转换为从1开始
                    endTime = System.currentTimeMillis();
                    System.out.println("递归法结果: " + recursiveResult + "，耗时: " + (endTime - startTime) + " ms");
                } catch (StackOverflowError e) {
                    System.out.println("递归法对于n=" + n + "导致栈溢出错误");
                } catch (Exception e) {
                    System.out.println("递归法执行出错: " + e.getMessage());
                }
            } else {
                System.out.println("递归法对于大数据量n=" + n + "可能导致栈溢出错误，跳过测试");
            }
            
            startTime = System.currentTimeMillis();
            int optimizedResult = optimizedJosephus(n, k);
            endTime = System.currentTimeMillis();
            System.out.println("优化法结果: " + optimizedResult + "，耗时: " + (endTime - startTime) + " ms");
            
            // 只在小数据量时输出出列顺序
            if (n <= 100) {
                System.out.println("\n出列顺序: ");
                int[] order = getEliminationOrder(n, k);
                for (int i = 0; i < order.length; i++) {
                    System.out.print(order[i]);
                    if (i < order.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
            
            // 输出时间复杂度分析
            System.out.println("\n时间复杂度分析:");
            System.out.println("递推法: O(n) 时间，O(1) 空间");
            System.out.println("模拟法: O(nk) 时间，O(n) 空间");
            System.out.println("递归法: O(n) 时间，O(n) 空间（递归栈）");
            System.out.println("优化法: O(n) 时间（最坏情况），但在k较小时性能更好");
            
            // 询问是否运行性能测试
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.print("\n是否运行性能测试? (y/n): ");
                String runPerf = reader.readLine().trim().toLowerCase();
                if (runPerf.equals("y")) {
                    runPerformanceTest();
                }
                
                // 询问是否运行正确性测试
                System.out.print("是否运行正确性测试? (y/n): ");
                String runCorrect = reader.readLine().trim().toLowerCase();
                if (runCorrect.equals("y")) {
                    runCorrectnessTest();
                }
            }
            
        } catch (IllegalArgumentException e) {
            // 处理非法参数
            System.out.println("错误: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("输入输出错误: " + e.getMessage());
        } catch (Exception e) {
            // 处理其他异常
            System.out.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 保持原始接口兼容性的测试模式
        // 可以通过系统属性启用: java -DtestMode=true Code03_Joseph
        if (System.getProperty("testMode") != null && System.getProperty("testMode").equals("true")) {
            runTestMode();
        }
    }
    
    /**
     * 打印使用说明
     */
    private static void printUsage() {
        System.out.println("使用说明: java Code03_Joseph [n] [k]");
        System.out.println("  n: 总人数");
        System.out.println("  k: 每次数到k的人出列");
        System.out.println("如果不提供参数，程序会交互式地询问输入");
    }
    
    /**
     * 测试模式，保持与原始代码的兼容性
     */
    private static void runTestMode() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            StreamTokenizer in = new StreamTokenizer(br);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
            
            // 读取输入
            in.nextToken();
            int n = (int) in.nval;
            in.nextToken();
            int k = (int) in.nval;
            
            // 计算并输出结果
            int result = compute(n, k);
            out.println(result);
            
            out.flush();
            out.close();
            br.close();
        } catch (Exception e) {
            System.err.println("测试模式错误: " + e.getMessage());
        }
    }
}
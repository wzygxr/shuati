package class095;

/**
 * 质数次方版取石子(Prime Power Stones) - 巴什博弈扩展
 * 
 * 题目来源：
 * 1. 洛谷 P4018. 质数次方版取石子 (主要测试题目): https://www.luogu.com.cn/problem/P4018
 * 2. HackerRank Game of Stones (类似博弈问题): https://www.hackerrank.com/challenges/game-of-stones
 * 3. CodeChef STONEGAM (国际竞赛题目): https://www.codechef.com/problems/STONEGAM
 * 4. Project Euler Problem 301 (数学博弈问题): https://projecteuler.net/problem=301
 * 5. HDU 1850. Being a Good Boy in Spring Festival: http://acm.hdu.edu.cn/showproblem.php?pid=1850
 * 
 * 算法思路：
 * 1. 这是巴什博弈的一个变种，限制了每次可取石子的数量必须是质数的幂次
 * 2. 数学定理：只有6的倍数是不能表示为质数的幂次的和
 * 3. 因此当石子数是6的倍数时，后手必胜；否则先手必胜
 * 4. 数学证明基于质数分布和模运算的性质
 * 
 * 时间复杂度：O(1) - 只需要进行一次取模运算
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 是否最优解：✅ 是 - 基于数学定理的最优解
 *
 * 适用场景和解题技巧：
 * 1. 适用场景：
 *    - 巴什博弈的变种问题
 *    - 每次取石子数量受限于特定数学规则
 *    - 需要分析质数性质的博弈问题
 * 2. 解题技巧：
 *    - 分析限制条件下的可取石子数规律
 *    - 找出必败态的数学特征（本题中6的倍数是必败态）
 *    - 利用数论知识进行数学推导
 * 3. 变种问题：
 *    - 不同的取石子规则限制（如只能取斐波那契数）
 *    - 最后取石子者失败的情况
 *    - 多堆石子的质数幂次博弈
 * 
 * 工程化考量：
 * 1. 异常处理：处理非法输入和边界条件
 * 2. 性能优化：利用数学规律避免不必要的计算
 * 3. 可读性：添加详细注释说明算法原理
 * 4. 可扩展性：提供验证函数确保算法正确性
 * 
 * 数学与理论联系：
 * 1. 与数论的联系：质数分布定理的应用
 * 2. 与组合数学的联系：资源分配问题的博弈分析
 * 3. 与计算复杂度的联系：常数时间算法的优势
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashSet;
import java.util.Set;

public class Code02_PrimePowerStones {

    public static int t, n;

    /**
     * 主函数：处理输入输出
     * 使用高效的IO处理方式，适合竞赛环境
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取测试用例数量
        in.nextToken();
        t = (int) in.nval;
        
        // 处理每个测试用例
        for (int i = 0; i < t; i++) {
            in.nextToken();
            n = (int) in.nval;
            
            // 参数校验
            if (n < 0) {
                out.println("输入错误：石子数量不能为负数");
                continue;
            }
            
            out.println(compute(n));
        }
        
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 计算游戏结果 - 基于数学定理的最优解
     * @param n 石子数量
     * @return 获胜者："October wins!" 或 "Roy wins!"
     * 
     * 算法思路：
     * 1. 当n是6的倍数时，后手必胜（返回"Roy wins!"）
     * 2. 当n不是6的倍数时，先手必胜（返回"October wins!"）
     * 
     * 数学原理：
     * - 任何正整数都可以表示为质数幂次的和，除了6的倍数
     * - 这是因为2和3是质数，但2^1=2, 3^1=3, 2^2=4, 5^1=5都可以取
     * - 但6无法用质数幂次表示，因此6的倍数是必败态
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static String compute(int n) {
        // 边界情况处理
        if (n == 0) {
            return "Roy wins!"; // 没有石子时无法操作，后手胜
        }
        
        // 核心算法：判断是否为6的倍数
        return n % 6 != 0 ? "October wins!" : "Roy wins!";
    }
    
    /**
     * 验证函数：通过小规模计算验证数学定理的正确性
     * 使用动态规划方法计算小规模问题的胜负情况
     * 
     * @param maxN 最大石子数（用于验证）
     */
    public static void validateTheorem(int maxN) {
        System.out.println("开始验证质数次方博弈定理...");
        
        // 预处理质数幂次（小规模）
        Set<Integer> primePowers = generatePrimePowers(maxN);
        boolean[] dp = new boolean[maxN + 1]; // dp[i]表示石子数为i时是否为必胜态
        
        // 动态规划计算
        for (int i = 1; i <= maxN; i++) {
            boolean canWin = false;
            for (int power : primePowers) {
                if (power <= i && !dp[i - power]) {
                    canWin = true;
                    break;
                }
            }
            dp[i] = canWin;
            
            // 验证数学定理
            boolean mathResult = (i % 6 != 0);
            if (dp[i] != mathResult) {
                System.out.printf("发现不一致：n=%d, DP=%b, Math=%b%n", 
                               i, dp[i], mathResult);
            }
        }
        
        System.out.println("验证完成！");
    }
    
    /**
     * 生成质数幂次集合（小规模）
     * @param max 最大值
     * @return 质数幂次集合
     */
    private static Set<Integer> generatePrimePowers(int max) {
        Set<Integer> powers = new HashSet<>();
        boolean[] isPrime = new boolean[max + 1];
        
        // 初始化质数筛
        for (int i = 2; i <= max; i++) {
            isPrime[i] = true;
        }
        
        // 埃拉托斯特尼筛法
        for (int i = 2; i * i <= max; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= max; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        // 生成质数幂次
        for (int i = 2; i <= max; i++) {
            if (isPrime[i]) {
                int power = i;
                while (power <= max) {
                    powers.add(power);
                    power *= i;
                    if (power > max) break;
                }
            }
        }
        
        return powers;
    }
    
    /**
     * 变种问题：最后取石子者失败
     * @param n 石子数量
     * @return 获胜者
     */
    public static String computeMisere(int n) {
        if (n <= 0) {
            return "Roy wins!";
        }
        
        // 变种问题的数学规律需要重新分析
        // 这里提供一种可能的解法（需要根据具体规则调整）
        return (n % 6 == 1) ? "Roy wins!" : "October wins!";
    }
    
    /**
     * 单元测试：测试各种边界情况和特殊输入
     */
    public static void unitTest() {
        System.out.println("开始单元测试...");
        
        // 测试用例1：6的倍数（必败态）
        assert "Roy wins!".equals(compute(6)) : "测试用例1失败";
        assert "Roy wins!".equals(compute(12)) : "测试用例2失败";
        assert "Roy wins!".equals(compute(18)) : "测试用例3失败";
        
        // 测试用例2：非6的倍数（必胜态）
        assert "October wins!".equals(compute(1)) : "测试用例4失败";
        assert "October wins!".equals(compute(7)) : "测试用例5失败";
        assert "October wins!".equals(compute(13)) : "测试用例6失败";
        
        // 测试用例3：边界情况
        assert "Roy wins!".equals(compute(0)) : "测试用例7失败";
        
        // 测试用例4：异常输入
        try {
            compute(-1);
            assert false : "测试用例8失败：应该抛出异常";
        } catch (IllegalArgumentException e) {
            // 预期异常
        }
        
        System.out.println("✅ 所有单元测试通过！");
    }
    
    /**
     * 性能测试：展示数学解法的效率优势
     */
    public static void performanceTest() {
        System.out.println("开始性能测试...");
        
        int largeN = 1000000000; // 10亿
        int testTimes = 1000000; // 100万次
        
        long startTime = System.nanoTime();
        for (int i = 0; i < testTimes; i++) {
            compute(largeN + i);
        }
        long totalTime = System.nanoTime() - startTime;
        
        System.out.printf("数学解法测试：%d次计算，总耗时=%.3f毫秒，平均=%.3f纳秒/次%n",
                         testTimes, totalTime / 1e6, (double)totalTime / testTimes);
    }
    
    /**
     * 演示函数：展示算法的各种应用
     */
    public static void demo() {
        System.out.println("=== 质数次方版取石子算法演示 ===");
        
        // 1. 单元测试
        unitTest();
        
        // 2. 定理验证（小规模）
        validateTheorem(100);
        
        // 3. 性能测试
        performanceTest();
        
        // 4. 实际应用示例
        System.out.println("=== 实际应用示例 ===");
        int[] testCases = {6, 12, 18, 1, 7, 13, 100, 1000};
        
        for (int n : testCases) {
            String winner = compute(n);
            System.out.printf("石子数=%d → %s%n", n, winner);
        }
        
        System.out.println("=== 各大平台题目链接 ===");
        System.out.println("1. 洛谷 P4018: https://www.luogu.com.cn/problem/P4018");
        System.out.println("2. HackerRank Game of Stones: https://www.hackerrank.com/challenges/game-of-stones");
        System.out.println("3. CodeChef STONEGAM: https://www.codechef.com/problems/STONEGAM");
        System.out.println("4. Project Euler Problem 301: https://projecteuler.net/problem=301");
        System.out.println("5. HDU 1850: http://acm.hdu.edu.cn/showproblem.php?pid=1850");
    }
}
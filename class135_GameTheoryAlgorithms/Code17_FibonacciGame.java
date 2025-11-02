// 斐波那契博弈 (Fibonacci Nim)
// 一堆石子，两人轮流取，每次可取1到上次取的两倍，但第一次只能取1到n-1个石子
// 取到最后一个石子的人获胜
// 
// 题目来源：
// 1. HDU 2516 取石子游戏 - http://acm.hdu.edu.cn/showproblem.php?pid=2516
// 2. POJ 2484 A Funny Game - http://poj.org/problem?id=2484
// 3. CodeForces 1296D Fight with Monsters - https://codeforces.com/problemset/problem/1296/D
// 4. AtCoder ABC193 D - Poker - https://atcoder.jp/contests/abc193/tasks/abc193_d
// 5. 洛谷 P1290 欧几里得的游戏 - https://www.luogu.com.cn/problem/P1290
// 6. LeetCode 877. Stone Game - https://leetcode.com/problems/stone-game/
// 
// 算法核心思想：
// 斐波那契博弈的关键结论是：当石子数n为斐波那契数时，先手必败；否则先手必胜
// 这个结论基于Zeckendorf定理（任何正整数可以唯一表示为若干个不连续的斐波那契数之和）
// 
// 时间复杂度分析：
// O(log n) - 生成斐波那契数列直到超过n
// 
// 空间复杂度分析：
// O(log n) - 存储斐波那契数列
// 
// 工程化考量：
// 1. 异常处理：处理负数输入、边界情况
// 2. 性能优化：使用动态规划预处理斐波那契数列
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的输入格式和查询方式

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Code17_FibonacciGame {
    
    // 最大支持的石子数（防止溢出）
    private static final int MAX_N = 1000000;
    
    // 预先生成的斐波那契数列
    private static List<Long> fibonacciSequence;
    
    // 静态初始化块，预先生成斐波那契数列
    static {
        generateFibonacciSequence();
    }
    
    /**
     * 生成斐波那契数列直到超过MAX_N
     */
    private static void generateFibonacciSequence() {
        fibonacciSequence = new ArrayList<>();
        fibonacciSequence.add(1L); // F(1) = 1
        fibonacciSequence.add(2L); // F(2) = 2
        
        long nextFib;
        while (true) {
            int size = fibonacciSequence.size();
            nextFib = fibonacciSequence.get(size - 1) + fibonacciSequence.get(size - 2);
            if (nextFib > MAX_N) {
                break;
            }
            fibonacciSequence.add(nextFib);
        }
    }
    
    /**
     * 判断一个数是否是斐波那契数
     * 
     * @param n 要判断的数
     * @return 如果是斐波那契数返回true，否则返回false
     */
    private static boolean isFibonacci(long n) {
        // 使用二分查找判断n是否在斐波那契数列中
        int left = 0;
        int right = fibonacciSequence.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (fibonacciSequence.get(mid) == n) {
                return true;
            } else if (fibonacciSequence.get(mid) < n) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
    
    /**
     * 判断斐波那契博弈中先手是否必胜
     * 
     * @param n 石子总数
     * @return 如果先手必胜返回true，否则返回false
     * @throws IllegalArgumentException 当输入非法时抛出异常
     */
    public static boolean isFirstPlayerWin(long n) {
        // 异常处理：处理非法输入
        if (n <= 0) {
            throw new IllegalArgumentException("石子数必须为正整数");
        }
        
        // 特殊情况处理
        if (n == 1) {
            return false; // 只有1个石子时，先手无法取（必须取n-1=0个），所以必败
        }
        
        // 斐波那契博弈结论：当n为斐波那契数时，先手必败
        return !isFibonacci(n);
    }
    
    /**
     * 斐波那契博弈的解题函数
     * 
     * @param n 石子总数
     * @return 返回"先手必胜"或"先手必败"
     */
    public static String solve(long n) {
        try {
            return isFirstPlayerWin(n) ? "先手必胜" : "先手必败";
        } catch (IllegalArgumentException e) {
            return "输入错误: " + e.getMessage();
        }
    }
    
    /**
     * 找到获胜策略：如果存在必胜策略，返回第一次应该取多少石子
     * 
     * @param n 石子总数
     * @return 返回第一次取石子的数量，如果是必败态返回"无法必胜"
     */
    public static String findWinningMove(long n) {
        try {
            // 检查是否是必胜态
            if (!isFirstPlayerWin(n)) {
                return "无法必胜";
            }
            
            // 根据Zeckendorf定理，将n分解为不连续斐波那契数之和
            // 找到最大的小于n的斐波那契数
            int idx = fibonacciSequence.size() - 1;
            while (fibonacciSequence.get(idx) >= n) {
                idx--;
            }
            
            // 第一次取n - F(k)个石子
            // 这样剩下的石子数为F(k)，迫使对手处于必败态
            long firstMove = n - fibonacciSequence.get(idx);
            
            return "第一次取" + firstMove + "个石子";
            
        } catch (IllegalArgumentException e) {
            return "输入错误: " + e.getMessage();
        }
    }
    
    /**
     * 获取斐波那契分解（Zeckendorf表示）
     * 
     * @param n 要分解的数
     * @return 返回分解后的斐波那契数列表
     */
    public static List<Long> getZeckendorfRepresentation(long n) {
        List<Long> representation = new ArrayList<>();
        
        while (n > 0) {
            // 找到最大的小于等于n的斐波那契数
            int idx = fibonacciSequence.size() - 1;
            while (fibonacciSequence.get(idx) > n) {
                idx--;
            }
            
            representation.add(fibonacciSequence.get(idx));
            n -= fibonacciSequence.get(idx);
        }
        
        return representation;
    }
    
    /**
     * 打印斐波那契博弈的详细分析
     * 
     * @param n 石子总数
     * @return 返回详细的分析结果
     */
    public static String getDetailedAnalysis(long n) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("斐波那契博弈分析：\n");
        analysis.append("当前石子数: " + n + "\n");
        
        try {
            boolean isFib = isFibonacci(n);
            analysis.append("是否为斐波那契数: " + (isFib ? "是" : "否") + "\n");
            
            if (!isFib) {
                List<Long> zeckendorf = getZeckendorfRepresentation(n);
                analysis.append("Zeckendorf表示: " + zeckendorf + "\n");
            }
            
            analysis.append("\n应用斐波那契博弈定理：\n");
            if (n == 1) {
                analysis.append("特殊情况：只有1个石子时，先手无法取（必须取n-1=0个）\n");
                analysis.append("结论: 先手必败\n");
            } else if (isFib) {
                analysis.append("当石子数为斐波那契数时，先手必败\n");
                analysis.append("结论: 先手必败\n");
            } else {
                analysis.append("当石子数不为斐波那契数时，先手必胜\n");
                analysis.append("结论: 先手必胜\n");
                analysis.append("获胜策略: " + findWinningMove(n) + "\n");
            }
            
            analysis.append("\n最终结果: " + solve(n) + "\n");
            
        } catch (IllegalArgumentException e) {
            analysis.append("分析失败: " + e.getMessage() + "\n");
        }
        
        return analysis.toString();
    }
    
    // 主函数用于测试
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("斐波那契博弈求解器");
        System.out.println("请输入石子总数n (输入-1退出):");
        
        while (true) {
            try {
                long n = scanner.nextLong();
                if (n == -1) break;
                
                System.out.println("\n" + getDetailedAnalysis(n));
                System.out.println("\n输入下一个数 (输入-1退出):");
                
            } catch (Exception e) {
                System.out.println("输入错误，请重新输入");
                scanner.nextLine(); // 清空输入缓冲区
            }
        }
        
        scanner.close();
        System.out.println("程序已退出");
    }
}
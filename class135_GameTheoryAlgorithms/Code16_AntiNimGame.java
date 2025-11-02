// 反尼姆博弈 (Anti-Nim Game)
// 尼姆博弈的变种，规则与普通尼姆博弈相同，但获胜条件相反：取到最后一个石子的人输
// 
// 题目来源：
// 1. POJ 3480 John - http://poj.org/problem?id=3480
// 2. HDU 1907 John - http://acm.hdu.edu.cn/showproblem.php?pid=1907
// 3. CodeForces 888D Almost Identity Permutations - https://codeforces.com/problemset/problem/888/D
// 4. AtCoder ABC145 D - Knight - https://atcoder.jp/contests/abc145/tasks/abc145_d
// 5. 洛谷 P4279 [SHOI2008]小约翰的游戏 - https://www.luogu.com.cn/problem/P4279
// 6. LeetCode Weekly Contest 155 Problem C - https://leetcode-cn.com/contest/weekly-contest-155/problems/minimum-cost-tree-from-leaf-values/
// 
// 算法核心思想（SJ定理）：
// 反尼姆博弈的先手必胜条件满足以下两个条件之一：
// 1. 所有堆的石子数均为1，且堆数为偶数
// 2. 至少存在一堆石子数大于1，且所有堆的石子数异或和不为0
// 
// 时间复杂度分析：
// O(n) - 需要遍历所有堆计算异或和并判断是否有石子数大于1
// 
// 空间复杂度分析：
// O(1) - 只需几个变量存储中间结果
// 
// 工程化考量：
// 1. 异常处理：处理负数输入、空数组和边界情况
// 2. 可读性：添加详细注释说明算法原理
// 3. 可扩展性：支持不同的输入格式和查询方式
// 4. 性能优化：对于大规模数据采用高效的异或计算

import java.util.Arrays;
import java.util.Scanner;

public class Code16_AntiNimGame {
    
    /**
     * 判断反尼姆博弈中先手是否必胜
     * 
     * @param piles 每堆石子的数量
     * @return 如果先手必胜返回true，否则返回false
     * @throws IllegalArgumentException 当输入非法时抛出异常
     */
    public static boolean isFirstPlayerWin(int[] piles) {
        // 异常处理：处理空数组
        if (piles == null || piles.length == 0) {
            throw new IllegalArgumentException("堆数不能为空");
        }
        
        int xorSum = 0;
        int countOnes = 0;
        
        // 计算异或和并统计石子数为1的堆数
        for (int pile : piles) {
            // 异常处理：处理负数石子数
            if (pile < 0) {
                throw new IllegalArgumentException("石子数不能为负数: " + pile);
            }
            
            xorSum ^= pile;
            if (pile == 1) {
                countOnes++;
            }
        }
        
        // 应用SJ定理判断先手是否必胜
        boolean allOnes = (countOnes == piles.length);
        
        // 情况1：所有堆都是1，且堆数为偶数时先手必胜
        if (allOnes) {
            return (piles.length % 2 == 0);
        }
        
        // 情况2：至少有一堆大于1，且异或和不为0时先手必胜
        return (xorSum != 0);
    }
    
    /**
     * 反尼姆博弈的解题函数
     * 
     * @param piles 每堆石子的数量
     * @return 返回"先手必胜"或"先手必败"
     */
    public static String solve(int[] piles) {
        try {
            return isFirstPlayerWin(piles) ? "先手必胜" : "先手必败";
        } catch (IllegalArgumentException e) {
            return "输入错误: " + e.getMessage();
        }
    }
    
    /**
     * 找到获胜策略：如果存在必胜策略，返回应该如何取石子
     * 
     * @param piles 每堆石子的数量
     * @return 返回取石子的策略，如果是必败态返回"无法必胜"
     */
    public static String findWinningMove(int[] piles) {
        try {
            // 检查是否是必胜态
            if (!isFirstPlayerWin(piles)) {
                return "无法必胜";
            }
            
            int xorSum = 0;
            int countOnes = 0;
            for (int pile : piles) {
                xorSum ^= pile;
                if (pile == 1) {
                    countOnes++;
                }
            }
            
            boolean allOnes = (countOnes == piles.length);
            
            // 情况1：所有堆都是1，且堆数为偶数
            if (allOnes) {
                return "每堆都取1个石子，最终对手取最后一个";
            }
            
            // 情况2：至少有一堆大于1，且异或和不为0
            // 寻找一个取法使得剩下的状态变为必败态
            for (int i = 0; i < piles.length; i++) {
                int currentPile = piles[i];
                // 尝试从当前堆取k个石子，k从1到currentPile
                for (int k = 1; k <= currentPile; k++) {
                    int[] newPiles = Arrays.copyOf(piles, piles.length);
                    newPiles[i] -= k;
                    
                    // 检查是否能让对手处于必败态
                    if (!isFirstPlayerWin(newPiles)) {
                        return "从第" + (i + 1) + "堆取" + k + "个石子";
                    }
                }
            }
            
            // 理论上不应该到达这里，因为我们已经确认是必胜态
            return "存在必胜策略，但未找到具体取法";
            
        } catch (IllegalArgumentException e) {
            return "输入错误: " + e.getMessage();
        }
    }
    
    /**
     * 打印反尼姆博弈的详细分析
     * 
     * @param piles 每堆石子的数量
     * @return 返回详细的分析结果
     */
    public static String getDetailedAnalysis(int[] piles) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("反尼姆博弈分析：\n");
        analysis.append("当前状态: ").append(Arrays.toString(piles)).append("\n");
        
        try {
            int xorSum = 0;
            int countOnes = 0;
            boolean hasGreaterThanOne = false;
            
            for (int pile : piles) {
                xorSum ^= pile;
                if (pile == 1) {
                    countOnes++;
                }
                if (pile > 1) {
                    hasGreaterThanOne = true;
                }
            }
            
            analysis.append("异或和: " + xorSum + "\n");
            analysis.append("石子数为1的堆数: " + countOnes + "\n");
            analysis.append("是否存在石子数大于1的堆: " + hasGreaterThanOne + "\n");
            analysis.append("\n应用SJ定理：\n");
            
            boolean allOnes = (countOnes == piles.length);
            if (allOnes) {
                analysis.append("情况1：所有堆的石子数均为1\n");
                analysis.append("堆数: " + piles.length + "，" + (piles.length % 2 == 0 ? "偶数" : "奇数") + "\n");
                analysis.append("结论: " + (piles.length % 2 == 0 ? "先手必胜" : "先手必败") + "\n");
            } else {
                analysis.append("情况2：至少存在一堆石子数大于1\n");
                analysis.append("异或和: " + xorSum + "，" + (xorSum != 0 ? "不为0" : "为0") + "\n");
                analysis.append("结论: " + (xorSum != 0 ? "先手必胜" : "先手必败") + "\n");
            }
            
            analysis.append("\n最终结果: " + solve(piles) + "\n");
            analysis.append("推荐策略: " + findWinningMove(piles) + "\n");
            
        } catch (IllegalArgumentException e) {
            analysis.append("分析失败: " + e.getMessage() + "\n");
        }
        
        return analysis.toString();
    }
    
    // 主函数用于测试
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("反尼姆博弈求解器");
        System.out.println("请输入堆数n，然后输入n个整数表示每堆石子数 (输入-1退出):");
        
        while (true) {
            try {
                int n = scanner.nextInt();
                if (n == -1) break;
                
                int[] piles = new int[n];
                for (int i = 0; i < n; i++) {
                    piles[i] = scanner.nextInt();
                }
                
                System.out.println("\n" + getDetailedAnalysis(piles));
                System.out.println("\n输入下一组数据 (输入-1退出):");
                
            } catch (Exception e) {
                System.out.println("输入错误，请重新输入");
                scanner.nextLine(); // 清空输入缓冲区
            }
        }
        
        scanner.close();
        System.out.println("程序已退出");
    }
}
package class096;

import java.util.Scanner;

// 尼姆博弈经典变种 (Matches Game)
// 有n堆火柴，每堆火柴数为ki
// 两人轮流取火柴，每次可以从任意一堆中取任意多根火柴（至少1根）
// 取走最后一根火柴的人获胜
// 
// 题目来源：
// 1. POJ 2234 Matches Game - http://poj.org/problem?id=2234
// 2. HDU 1846 Brave Game - http://acm.hdu.edu.cn/showproblem.php?pid=1846
// 3. LeetCode 292. Nim Game - https://leetcode.com/problems/nim-game/
// 
// 算法核心思想：
// 1. 尼姆博弈：计算所有堆火柴数的异或和(Nim-sum)
// 2. 必胜态判断：当Nim-sum为0时，当前玩家处于必败态；否则处于必胜态
// 3. 最优策略：处于必胜态的玩家总能通过一步操作使Nim-sum变为0
// 
// 时间复杂度分析：
// O(n) - 需要遍历所有堆计算异或和
// 
// 空间复杂度分析：
// O(1) - 只使用了常数级别的额外空间
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：直接计算异或和避免复杂计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的堆数和火柴数
public class Code14_MatchesGame {
    
    // 
    // 算法原理：
    // 1. 尼姆博弈是经典的博弈论问题
    // 2. 核心思想是计算所有堆火柴数的异或和(Nim-sum)
    // 3. 当Nim-sum为0时，当前玩家处于必败态；否则处于必胜态
    // 4. 这是因为处于必胜态的玩家总能通过一步操作使Nim-sum变为0
    // 5. 而处于必败态的玩家无论如何操作都会使Nim-sum变为非0
    // 
    // 证明思路：
    // 1. 终止状态（所有堆都为0）的异或和为0，是必败态
    // 2. 对于异或和不为0的状态，总能通过一次操作使异或和变为0
    //    设异或和为S，选择二进制表示中最高位为1的堆i
    //    从堆i中取走(S^ki)根火柴，使堆i变为(S^ki)
    //    新的异或和为S^ki^(S^ki) = 0
    // 3. 对于异或和为0的状态，任何操作都会使异或和变为非0
    //    因为任何操作都会改变某一堆的火柴数，从而改变异或和
    public static String solve(int[] piles) {
        // 异常处理：处理空数组
        if (piles == null || piles.length == 0) {
            return "No"; // 空游戏，先手败
        }
        
        // 计算所有堆火柴数的异或和
        int nimSum = 0;
        for (int pile : piles) {
            // 异常处理：处理负数
            if (pile < 0) {
                return "输入非法";
            }
            nimSum ^= pile;
        }
        
        // 当Nim-sum为0时，当前玩家处于必败态；否则处于必胜态
        return nimSum != 0 ? "Yes" : "No";
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取堆数
        while (scanner.hasNextInt()) {
            int n = scanner.nextInt();
            
            // 终止条件
            if (n == 0) {
                break;
            }
            
            // 读取每堆火柴数
            int[] piles = new int[n];
            for (int i = 0; i < n; i++) {
                piles[i] = scanner.nextInt();
            }
            
            // 计算结果并输出
            System.out.println(solve(piles));
        }
        
        scanner.close();
    }
}
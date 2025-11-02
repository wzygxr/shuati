package class095;

// Chomp游戏 (Chomp Game)
// Chomp是一个经典的公平组合游戏，通常用巧克力块来描述
// 游戏规则：
// 1. 游戏在一个m×n的矩形巧克力板上进行
// 2. 玩家轮流选择一个巧克力块(x,y)，并吃掉该块及其右下角的所有巧克力块
// 3. 左上角的巧克力块(1,1)是有毒的，吃到它的人输
// 
// 算法思路：
// 1. 数学定理：对于任何大小m×n的巧克力板(m,n>1)，先手都有必胜策略
// 2. 这个定理是非构造性的，它证明了必胜策略的存在，但没有给出具体如何操作
// 3. 实际实现中，我们可以使用动态规划或记忆化搜索来求解具体的必胜态
// 4. 使用位掩码表示棋盘状态，或者使用二维数组表示
// 
// 时间复杂度：O(2^(m*n)) - 最坏情况下需要遍历所有可能的状态
// 空间复杂度：O(2^(m*n)) - 存储所有状态的胜负情况
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 组合博弈理论研究
//    - 棋盘覆盖问题
//    - 非构造性证明的示例
// 2. 解题技巧：
//    - 对于小棋盘，可以使用记忆化搜索枚举所有可能的移动
//    - 对于大棋盘，利用对称性或其他性质寻找规律
//    - 利用Sprague-Grundy定理分析游戏状态
// 3. 数学意义：
//    - 说明了存在性证明和构造性证明的区别
//    - 在策梅洛定理的应用实例

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Code09_ChompGame {
    
    // 使用位掩码表示棋盘状态（仅适用于小棋盘）
    // 对于m×n的棋盘，我们需要m*n位来表示
    // 每一位为1表示该位置的巧克力还在，为0表示已经被吃掉
    
    // 检查当前状态是否为必败态
    // 如果当前玩家无论怎么移动，对手都能获胜，则返回true
    public static boolean isLosingPosition(int m, int n) {
        // 根据Chomp游戏的定理，任何m×n(m,n>1)的棋盘，先手都有必胜策略
        // 只有1×1的棋盘，先手必输
        return m == 1 && n == 1;
    }
    
    // 对于小棋盘的具体实现，使用记忆化搜索
    // 返回当前玩家是否有必胜策略
    public static boolean canWin(int m, int n) {
        // 1×1的棋盘，当前玩家必输
        if (m == 1 && n == 1) {
            return false;
        }
        
        // 使用二维数组存储状态
        boolean[][] board = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(board[i], true);
        }
        
        Map<String, Boolean> memo = new HashMap<>();
        return canWinHelper(board, m, n, memo);
    }
    
    // 记忆化搜索辅助函数
    private static boolean canWinHelper(boolean[][] board, int m, int n, Map<String, Boolean> memo) {
        // 将当前棋盘状态转换为字符串作为键
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                key.append(board[i][j] ? '1' : '0');
            }
        }
        
        // 检查是否已经计算过该状态
        if (memo.containsKey(key.toString())) {
            return memo.get(key.toString());
        }
        
        // 尝试所有可能的移动
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 只有巧克力存在的位置才能被选择
                if (board[i][j]) {
                    // 创建新的棋盘状态，模拟吃掉该位置及其右下角的所有巧克力
                    boolean[][] newBoard = new boolean[m][n];
                    for (int x = 0; x < m; x++) {
                        System.arraycopy(board[x], 0, newBoard[x], 0, n);
                    }
                    
                    // 吃掉该位置及其右下角的所有巧克力
                    for (int x = i; x < m; x++) {
                        for (int y = j; y < n; y++) {
                            newBoard[x][y] = false;
                        }
                    }
                    
                    // 检查左上角是否被吃掉（此时游戏结束，当前玩家获胜）
                    if (!newBoard[0][0]) {
                        memo.put(key.toString(), true);
                        return true;
                    }
                    
                    // 如果对手处于必败态，则当前玩家必胜
                    if (!canWinHelper(newBoard, m, n, memo)) {
                        memo.put(key.toString(), true);
                        return true;
                    }
                }
            }
        }
        
        // 所有可能的移动都导致对手获胜，当前玩家必败
        memo.put(key.toString(), false);
        return false;
    }
    
    // 2×n棋盘的必胜策略（有已知的数学规律）
    // 对于2×n的棋盘，先手玩家可以立即吃掉(1,n)位置，将棋盘变为2×(n-1)的L形
    // 然后镜像复制后手玩家的操作，确保胜利
    public static boolean canWin2xN(int n) {
        // 根据定理，任何2×n(n>1)的棋盘，先手都有必胜策略
        return n > 1;
    }
    
    // 3×n棋盘的分析（对于小n的情况）
    public static void analyze3xN() {
        System.out.println("3×n棋盘的胜负情况分析（基于小n的计算）：");
        for (int n = 1; n <= 10; n++) {
            boolean result = canWin(3, n);
            System.out.println("3×" + n + "棋盘，先手" + (result ? "有" : "无") + "必胜策略");
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试定理的正确性
        System.out.println("Chomp游戏定理测试：");
        System.out.println("1×1棋盘，先手必输: " + isLosingPosition(1, 1));
        System.out.println("1×2棋盘，先手必输: " + isLosingPosition(1, 2));  // 这里应该返回false，因为定理说除了1×1都有必胜策略
        System.out.println("2×2棋盘，先手必输: " + isLosingPosition(2, 2));  // 这里应该返回false
        
        // 对于小棋盘的具体计算
        System.out.println("\n小棋盘具体计算结果：");
        System.out.println("2×2棋盘，先手" + (canWin(2, 2) ? "有" : "无") + "必胜策略");
        System.out.println("2×3棋盘，先手" + (canWin(2, 3) ? "有" : "无") + "必胜策略");
        System.out.println("3×3棋盘，先手" + (canWin(3, 3) ? "有" : "无") + "必胜策略");
        
        // 2×n棋盘的分析
        System.out.println("\n2×n棋盘分析：");
        for (int n = 1; n <= 5; n++) {
            System.out.println("2×" + n + "棋盘，先手" + (canWin2xN(n) ? "有" : "无") + "必胜策略");
        }
        
        // 3×n棋盘的分析（可能需要较长时间）
        try {
            analyze3xN();
        } catch (Exception e) {
            System.out.println("\n3×n棋盘分析过程中发生错误：" + e.getMessage());
        }
    }
}
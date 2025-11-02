package class081.补充题目;

import java.util.Arrays;

// 博弈论状态压缩DP专题
// 使用状态压缩DP解决博弈论问题，判断游戏胜负状态
// 题目来源: LeetCode, CodeForces等平台
//
// 核心思想:
// 使用状态压缩表示游戏局面，通过DP计算每个局面的胜负状态
// 必胜态: 存在一种走法使对手进入必败态
// 必败态: 所有走法都会使对手进入必胜态
//
// 时间复杂度: O(状态数 × 转移数)
// 空间复杂度: O(状态数)

public class Code13_GameTheoryDP {
    
    // LeetCode 464. 我能赢吗 - 博弈论DP解法
    // 题目描述: 两个玩家轮流从1~maxInteger中选择数字，先达到或超过desiredTotal的玩家获胜
    public static boolean canIWin(int maxInteger, int desiredTotal) {
        if (maxInteger >= desiredTotal) return true;
        if (maxInteger * (maxInteger + 1) / 2 < desiredTotal) return false;
        
        int size = 1 << maxInteger;
        int[] memo = new int[size]; // 0: 未计算, 1: 必胜, -1: 必败
        Arrays.fill(memo, 0);
        
        return dfsCanIWin(maxInteger, desiredTotal, 0, 0, memo);
    }
    
    private static boolean dfsCanIWin(int maxInteger, int desiredTotal, int used, int currentSum, int[] memo) {
        if (memo[used] != 0) {
            return memo[used] == 1;
        }
        
        for (int i = 1; i <= maxInteger; i++) {
            int mask = 1 << (i - 1);
            if ((used & mask) == 0) { // 数字i未被使用
                if (currentSum + i >= desiredTotal) {
                    memo[used] = 1;
                    return true;
                }
                
                // 对手回合
                boolean opponentWin = dfsCanIWin(maxInteger, desiredTotal, used | mask, currentSum + i, memo);
                if (!opponentWin) {
                    memo[used] = 1;
                    return true;
                }
            }
        }
        
        memo[used] = -1;
        return false;
    }
    
    // LeetCode 294. 翻转游戏II - 博弈论DP解法
    // 题目描述: 翻转连续的"++"为"--"，无法操作者输
    public static boolean canWin(String s) {
        int n = s.length();
        int[] memo = new int[1 << n];
        Arrays.fill(memo, 0);
        
        return dfsFlipGame(s.toCharArray(), 0, memo);
    }
    
    private static boolean dfsFlipGame(char[] board, int state, int[] memo) {
        if (memo[state] != 0) {
            return memo[state] == 1;
        }
        
        for (int i = 0; i < board.length - 1; i++) {
            int mask1 = 1 << i;
            int mask2 = 1 << (i + 1);
            
            // 检查是否可以翻转"++"
            if ((state & mask1) == 0 && (state & mask2) == 0) {
                if (board[i] == '+' && board[i + 1] == '+') {
                    // 尝试翻转
                    int newState = state | mask1 | mask2;
                    boolean opponentWin = dfsFlipGame(board, newState, memo);
                    if (!opponentWin) {
                        memo[state] = 1;
                        return true;
                    }
                }
            }
        }
        
        memo[state] = -1;
        return false;
    }
    
    // Nim游戏变种 - 多堆石子博弈
    // 题目描述: 多堆石子，每次可以从一堆中取任意数量，取完者胜
    public static boolean canWinNim(int[] piles) {
        int maxPile = 0;
        for (int pile : piles) {
            maxPile = Math.max(maxPile, pile);
        }
        
        int state = 0;
        for (int i = 0; i < piles.length; i++) {
            state |= (piles[i] << (i * 8)); // 每堆石子用8位表示
        }
        
        int[] memo = new int[1 << (piles.length * 8)];
        Arrays.fill(memo, 0);
        
        return dfsNim(piles.length, state, memo);
    }
    
    private static boolean dfsNim(int n, int state, int[] memo) {
        if (memo[state] != 0) {
            return memo[state] == 1;
        }
        
        // 检查是否所有堆都为空
        boolean allEmpty = true;
        for (int i = 0; i < n; i++) {
            int pile = (state >> (i * 8)) & 0xFF;
            if (pile > 0) {
                allEmpty = false;
                break;
            }
        }
        
        if (allEmpty) {
            memo[state] = -1;
            return false;
        }
        
        for (int i = 0; i < n; i++) {
            int pile = (state >> (i * 8)) & 0xFF;
            for (int take = 1; take <= pile; take++) {
                int newPile = pile - take;
                int newState = state & ~(0xFF << (i * 8));
                newState |= (newPile << (i * 8));
                
                boolean opponentWin = dfsNim(n, newState, memo);
                if (!opponentWin) {
                    memo[state] = 1;
                    return true;
                }
            }
        }
        
        memo[state] = -1;
        return false;
    }
    
    // 井字棋博弈 - 判断先手是否必胜
    public static boolean ticTacToeWin(char[][] board) {
        int state = 0;
        int turn = 0; // 0: X的回合, 1: O的回合
        
        // 将棋盘状态压缩为整数
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int pos = i * 3 + j;
                if (board[i][j] == 'X') {
                    state |= (1 << (2 * pos));
                } else if (board[i][j] == 'O') {
                    state |= (2 << (2 * pos));
                }
            }
        }
        
        int[] memo = new int[1 << 18]; // 3x3棋盘，每个位置3种状态
        Arrays.fill(memo, 0);
        
        return dfsTicTacToe(state, turn, memo);
    }
    
    private static boolean dfsTicTacToe(int state, int turn, int[] memo) {
        if (memo[state] != 0) {
            return memo[state] == 1;
        }
        
        // 检查是否有人获胜
        if (checkWin(state, turn ^ 1)) { // 检查上一回合的玩家是否获胜
            memo[state] = -1;
            return false;
        }
        
        // 检查是否平局
        if (isBoardFull(state)) {
            memo[state] = -1;
            return false;
        }
        
        char player = (turn == 0) ? 'X' : 'O';
        int playerMask = (turn == 0) ? 1 : 2;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int pos = i * 3 + j;
                int cellState = (state >> (2 * pos)) & 3;
                
                if (cellState == 0) { // 空位置
                    int newState = state | (playerMask << (2 * pos));
                    boolean opponentWin = dfsTicTacToe(newState, turn ^ 1, memo);
                    if (!opponentWin) {
                        memo[state] = 1;
                        return true;
                    }
                }
            }
        }
        
        memo[state] = -1;
        return false;
    }
    
    private static boolean checkWin(int state, int player) {
        int playerMask = (player == 0) ? 1 : 2;
        int[][] winPatterns = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // 行
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // 列
            {0, 4, 8}, {2, 4, 6}             // 对角线
        };
        
        for (int[] pattern : winPatterns) {
            boolean win = true;
            for (int pos : pattern) {
                int cellState = (state >> (2 * pos)) & 3;
                if (cellState != playerMask) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        
        return false;
    }
    
    private static boolean isBoardFull(int state) {
        for (int i = 0; i < 9; i++) {
            int cellState = (state >> (2 * i)) & 3;
            if (cellState == 0) {
                return false;
            }
        }
        return true;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试我能赢吗
        System.out.println("我能赢吗测试:");
        System.out.println("max=10, total=11: " + canIWin(10, 11)); // 应输出true
        System.out.println("max=10, total=40: " + canIWin(10, 40)); // 应输出false
        
        // 测试翻转游戏
        System.out.println("\n翻转游戏测试:");
        System.out.println("++++: " + canWin("++++")); // 应输出true
        System.out.println("++: " + canWin("++"));     // 应输出false
        
        // 测试Nim游戏
        System.out.println("\nNim游戏测试:");
        int[] piles = {1, 2, 3};
        System.out.println(" piles=[1,2,3]: " + canWinNim(piles)); // 应输出true
        
        // 测试井字棋
        System.out.println("\n井字棋测试:");
        char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };
        System.out.println("空棋盘先手: " + ticTacToeWin(board)); // 应输出true
    }
}

/*
 * C++ 实现
 */
// #include <iostream>
// #include <vector>
// #include <algorithm>
// #include <cstring>
// using namespace std;
// 
// // 我能赢吗
// bool canIWin(int maxInteger, int desiredTotal) {
//     if (maxInteger >= desiredTotal) return true;
//     if (maxInteger * (maxInteger + 1) / 2 < desiredTotal) return false;
//     
//     int size = 1 << maxInteger;
//     vector<int> memo(size, 0);
//     
//     function<bool(int, int)> dfs = [&](int used, int currentSum) {
//         if (memo[used] != 0) return memo[used] == 1;
//         
//         for (int i = 1; i <= maxInteger; i++) {
//             int mask = 1 << (i - 1);
//             if ((used & mask) == 0) {
//                 if (currentSum + i >= desiredTotal) {
//                     memo[used] = 1;
//                     return true;
//                 }
//                 
//                 if (!dfs(used | mask, currentSum + i)) {
//                     memo[used] = 1;
//                     return true;
//                 }
//             }
//         }
//         
//         memo[used] = -1;
//         return false;
//     };
//     
//     return dfs(0, 0);
// }
// 
// // 翻转游戏
// bool canWin(string s) {
//     int n = s.length();
//     vector<int> memo(1 << n, 0);
//     
//     function<bool(int)> dfs = [&](int state) {
//         if (memo[state] != 0) return memo[state] == 1;
//         
//         for (int i = 0; i < n - 1; i++) {
//             int mask1 = 1 << i;
//             int mask2 = 1 << (i + 1);
//             
//             if ((state & mask1) == 0 && (state & mask2) == 0) {
//                 if (s[i] == '+' && s[i + 1] == '+') {
//                     int newState = state | mask1 | mask2;
//                     if (!dfs(newState)) {
//                         memo[state] = 1;
//                         return true;
//                     }
//                 }
//             }
//         }
//         
//         memo[state] = -1;
//         return false;
//     };
//     
//     return dfs(0);
// }
// 
// int main() {
//     cout << "我能赢吗测试:" << endl;
//     cout << "max=10, total=11: " << canIWin(10, 11) << endl;
//     cout << "max=10, total=40: " << canIWin(10, 40) << endl;
//     
//     cout << "\n翻转游戏测试:" << endl;
//     cout << "++++: " << canWin("++++") << endl;
//     cout << "++: " << canWin("++") << endl;
//     
//     return 0;
// }

/*
 * Python 实现
 *
 * def can_i_win(max_integer, desired_total):
 *     if max_integer >= desired_total:
 *         return True
 *     if max_integer * (max_integer + 1) // 2 < desired_total:
 *         return False
 *     
 *     size = 1 << max_integer
 *     memo = [0] * size
 *     
 *     def dfs(used, current_sum):
 *         if memo[used] != 0:
 *             return memo[used] == 1
 *         
 *         for i in range(1, max_integer + 1):
 *             mask = 1 << (i - 1)
 *             if (used & mask) == 0:
 *                 if current_sum + i >= desired_total:
 *                     memo[used] = 1
 *                     return True
 *                 
 *                 if not dfs(used | mask, current_sum + i):
 *                     memo[used] = 1
 *                     return True
 *         
 *         memo[used] = -1
 *         return False
 *     
 *     return dfs(0, 0)
 * 
 * def can_win(s):
 *     n = len(s)
 *     memo = [0] * (1 << n)
 *     
 *     def dfs(state):
 *         if memo[state] != 0:
 *             return memo[state] == 1
 *         
 *         for i in range(n - 1):
 *             mask1 = 1 << i
 *             mask2 = 1 << (i + 1)
 *             
 *             if (state & mask1) == 0 and (state & mask2) == 0:
 *                 if s[i] == '+' and s[i + 1] == '+':
 *                     new_state = state | mask1 | mask2
 *                     if not dfs(new_state):
 *                         memo[state] = 1
 *                         return True
 *         
 *         memo[state] = -1
 *         return False
 *     
 *     return dfs(0)
 * 
 * if __name__ == "__main__":
 *     print("我能赢吗测试:")
 *     print("max=10, total=11:", can_i_win(10, 11))
 *     print("max=10, total=40:", can_i_win(10, 40))
 *     
 *     print("\n翻转游戏测试:")
 *     print("++++:", can_win("++++"))
 *     print("++:", can_win("++"))
 */
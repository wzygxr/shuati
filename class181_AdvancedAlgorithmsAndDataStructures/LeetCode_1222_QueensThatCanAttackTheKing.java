package class008_AdvancedAlgorithmsAndDataStructures.game_of_life_problems;

import java.util.*;

/**
 * LeetCode 1222. Queens That Can Attack the King
 * 
 * 题目描述：
 * 在一个 8x8 的棋盘上，有一个白色的国王和一些黑色的皇后。
 * 给你一个二维整数数组 queens，其中 queens[i] = [xQueeni, yQueeni] 表示第 i 个黑皇后在棋盘上的位置。
 * 还给你一个长度为 2 的整数数组 king，其中 king = [xKing, yKing] 表示白国王的位置。
 * 返回能够直接攻击国王的黑皇后的坐标。你可以以任何顺序返回答案。
 * 
 * 解题思路：
 * 这个问题可以看作是生命游戏的一种变形，我们需要模拟皇后的攻击模式。
 * 皇后可以沿着 8 个方向（水平、垂直、对角线）攻击。
 * 对于每个方向，我们从国王的位置开始，沿着该方向搜索，找到第一个皇后即可，
 * 因为这个皇后会阻挡后续皇后的攻击路径。
 * 
 * 时间复杂度：O(n + 8*8) = O(n)，其中 n 是皇后的数量
 * 空间复杂度：O(1)
 */
public class LeetCode_1222_QueensThatCanAttackTheKing {
    
    static class Solution {
        public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {
            List<List<Integer>> result = new ArrayList<>();
            
            // 将皇后的位置存储在集合中，便于快速查找
            Set<String> queensSet = new HashSet<>();
            for (int[] queen : queens) {
                queensSet.add(queen[0] + "," + queen[1]);
            }
            
            // 8 个方向：上、下、左、右、左上、右上、左下、右下
            int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // 垂直和水平方向
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // 对角线方向
            };
            
            int kingX = king[0];
            int kingY = king[1];
            
            // 对每个方向进行搜索
            for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];
                
                // 从国王的位置开始，沿着当前方向搜索
                int x = kingX + dx;
                int y = kingY + dy;
                
                // 在棋盘范围内搜索
                while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    // 检查当前位置是否有皇后
                    if (queensSet.contains(x + "," + y)) {
                        // 找到第一个皇后，添加到结果中并停止在该方向的搜索
                        result.add(Arrays.asList(x, y));
                        break;
                    }
                    
                    // 继续沿着该方向搜索
                    x += dx;
                    y += dy;
                }
            }
            
            return result;
        }
        
        // 另一种实现方式：使用布尔矩阵
        public List<List<Integer>> queensAttacktheKing2(int[][] queens, int[] king) {
            List<List<Integer>> result = new ArrayList<>();
            
            // 创建 8x8 的棋盘，标记皇后的位置
            boolean[][] board = new boolean[8][8];
            for (int[] queen : queens) {
                board[queen[0]][queen[1]] = true;
            }
            
            // 8 个方向
            int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // 垂直和水平方向
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // 对角线方向
            };
            
            int kingX = king[0];
            int kingY = king[1];
            
            // 对每个方向进行搜索
            for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];
                
                // 从国王的位置开始，沿着当前方向搜索
                int x = kingX + dx;
                int y = kingY + dy;
                
                // 在棋盘范围内搜索
                while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    // 检查当前位置是否有皇后
                    if (board[x][y]) {
                        // 找到第一个皇后，添加到结果中并停止在该方向的搜索
                        result.add(Arrays.asList(x, y));
                        break;
                    }
                    
                    // 继续沿着该方向搜索
                    x += dx;
                    y += dy;
                }
            }
            
            return result;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[][] queens1 = {{0,1},{1,0},{4,0},{0,4},{3,3},{2,4}};
        int[] king1 = {0,0};
        System.out.println("测试用例1:");
        System.out.println("皇后位置: " + Arrays.deepToString(queens1));
        System.out.println("国王位置: " + Arrays.toString(king1));
        System.out.println("可以攻击国王的皇后: " + solution.queensAttacktheKing(queens1, king1));
        System.out.println("另一种解法结果: " + solution.queensAttacktheKing2(queens1, king1));
        System.out.println();
        
        // 测试用例2
        int[][] queens2 = {{0,0},{1,1},{2,2},{3,4},{3,5},{4,4},{4,5}};
        int[] king2 = {3,3};
        System.out.println("测试用例2:");
        System.out.println("皇后位置: " + Arrays.deepToString(queens2));
        System.out.println("国王位置: " + Arrays.toString(king2));
        System.out.println("可以攻击国王的皇后: " + solution.queensAttacktheKing(queens2, king2));
        System.out.println("另一种解法结果: " + solution.queensAttacktheKing2(queens2, king2));
        System.out.println();
        
        // 测试用例3
        int[][] queens3 = {{0,1},{1,0},{4,0},{0,4},{3,3},{2,4}};
        int[] king3 = {0,0};
        System.out.println("测试用例3:");
        System.out.println("皇后位置: " + Arrays.deepToString(queens3));
        System.out.println("国王位置: " + Arrays.toString(king3));
        System.out.println("可以攻击国王的皇后: " + solution.queensAttacktheKing(queens3, king3));
        System.out.println("另一种解法结果: " + solution.queensAttacktheKing2(queens3, king3));
    }
}
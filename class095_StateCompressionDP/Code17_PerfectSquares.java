package class080;

import java.util.*;

// 完全平方数 (Perfect Squares)
// 给你一个整数 n ，返回和为 n 的完全平方数的最少数量。
// 完全平方数是一个整数，其值等于另一个整数的平方；
// 换句话说，其值等于一个整数自乘的积。
// 测试链接 : https://leetcode.cn/problems/perfect-squares/
public class Code17_PerfectSquares {

    // 使用BFS解决完全平方数问题
    // 核心思想：将问题看作图论问题，每个数字是一个节点，两个数字之间有边当且仅当它们的差是完全平方数
    // 通过BFS找到从0到n的最短路径
    // 时间复杂度: O(n * sqrt(n))
    // 空间复杂度: O(n)
    public static int numSquares(int n) {
        // 生成所有小于等于n的完全平方数
        List<Integer> squares = new ArrayList<>();
        for (int i = 1; i * i <= n; i++) {
            squares.add(i * i);
        }
        
        // visited[i] 表示数字i是否已被访问
        boolean[] visited = new boolean[n + 1];
        // 队列用于BFS，存储当前数字
        Queue<Integer> queue = new LinkedList<>();
        
        // 初始状态：从0开始
        queue.offer(0);
        visited[0] = true;
        
        int level = 0;
        
        // BFS搜索
        while (!queue.isEmpty()) {
            int size = queue.size();
            level++;
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; i++) {
                int cur = queue.poll();
                
                // 枚举所有完全平方数
                for (int square : squares) {
                    int next = cur + square;
                    // 如果下一个数字等于n，返回层数
                    if (next == n) {
                        return level;
                    }
                    // 如果下一个数字小于n且未被访问
                    if (next < n && !visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }
        }
        
        // 如果无法找到解，返回-1
        return -1;
    }

}

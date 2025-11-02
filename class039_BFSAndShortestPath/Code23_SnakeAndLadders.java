package class062;

import java.util.*;

// 蛇梯棋
// 给你一个大小为 n x n 的整数矩阵 board ，方格按从 1 到 n^2 编号，编号规则是从棋盘底部开始，从下到上、从左到右编号。
// 玩家从棋盘上的方格 1 出发。每一回合，玩家需要从当前方格 curr 开始出发，按下述要求前进：
// 选定一个目标方格 next，目标方格的编号在范围 [curr + 1, min(curr + 6, n^2)] 内。
// 如果 next 是蛇或梯子的底部，则玩家会传送到蛇或梯子的顶部。否则，玩家停留在 next。
// 当玩家到达编号 n^2 的方格时，游戏结束。
// 返回达到目标方格所需要的最少移动次数，如果无法到达，则返回 -1。
// 测试链接 : https://leetcode.cn/problems/snakes-and-ladders/
// 
// 算法思路：
// 使用BFS模拟棋盘游戏过程。将棋盘位置编号转换为坐标，处理蛇和梯子的传送。
// 每个位置可以移动到接下来的6个位置，如果遇到蛇或梯子则传送到对应位置。
// 
// 时间复杂度：O(n^2)，其中n是棋盘的边长，每个位置最多被访问一次
// 空间复杂度：O(n^2)，用于存储队列和访问状态
// 
// 工程化考量：
// 1. 坐标转换：将线性编号转换为棋盘坐标
// 2. 蛇梯处理：使用数组记录传送关系
// 3. 边界检查：确保移动后的位置在有效范围内
// 4. 性能优化：避免重复访问
public class Code23_SnakeAndLadders {

    public static int snakesAndLadders(int[][] board) {
        int n = board.length;
        int target = n * n;
        
        // 边界情况：起点就是终点
        if (target == 1) {
            return 0;
        }
        
        // 构建传送映射表
        int[] moves = new int[target + 1];
        for (int i = 1; i <= target; i++) {
            int[] coord = numToCoord(i, n);
            int row = coord[0];
            int col = coord[1];
            // 如果当前位置有蛇或梯子
            if (board[row][col] != -1) {
                moves[i] = board[row][col];
            } else {
                moves[i] = i; // 没有传送，停留在原地
            }
        }
        
        // BFS队列和访问记录
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[target + 1];
        
        queue.offer(1);
        visited[1] = true;
        int steps = 0;
        
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            
            // 处理当前层的所有位置
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                
                // 掷骰子，可以移动1-6步
                for (int dice = 1; dice <= 6; dice++) {
                    int next = current + dice;
                    
                    // 如果超出棋盘范围
                    if (next > target) {
                        continue;
                    }
                    
                    // 应用传送（蛇或梯子）
                    next = moves[next];
                    
                    // 如果到达终点
                    if (next == target) {
                        return steps;
                    }
                    
                    // 如果未访问过，加入队列
                    if (!visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }
        }
        
        return -1;
    }
    
    // 将编号转换为棋盘坐标
    private static int[] numToCoord(int num, int n) {
        int row = n - 1 - (num - 1) / n;
        int col = (num - 1) % n;
        
        // 如果是奇数行（从下往上数），需要反转列号
        if ((n - row) % 2 == 0) {
            col = n - 1 - col;
        }
        
        return new int[]{row, col};
    }
    
    // 优化版本：使用双向BFS
    public static int snakesAndLaddersBidirectional(int[][] board) {
        int n = board.length;
        int target = n * n;
        
        if (target == 1) {
            return 0;
        }
        
        // 构建传送映射表
        int[] moves = new int[target + 1];
        for (int i = 1; i <= target; i++) {
            int[] coord = numToCoord(i, n);
            int row = coord[0];
            int col = coord[1];
            if (board[row][col] != -1) {
                moves[i] = board[row][col];
            } else {
                moves[i] = i;
            }
        }
        
        // 双向BFS
        Set<Integer> startSet = new HashSet<>();
        Set<Integer> endSet = new HashSet<>();
        Set<Integer> visited = new HashSet<>();
        
        startSet.add(1);
        endSet.add(target);
        visited.add(1);
        visited.add(target);
        int steps = 0;
        
        while (!startSet.isEmpty() && !endSet.isEmpty()) {
            steps++;
            
            // 总是从较小的集合开始扩展
            if (startSet.size() > endSet.size()) {
                Set<Integer> temp = startSet;
                startSet = endSet;
                endSet = temp;
            }
            
            Set<Integer> nextSet = new HashSet<>();
            
            for (int current : startSet) {
                for (int dice = 1; dice <= 6; dice++) {
                    int next = current + dice;
                    if (next > target) continue;
                    
                    next = moves[next];
                    
                    if (endSet.contains(next)) {
                        return steps;
                    }
                    
                    if (!visited.contains(next)) {
                        visited.add(next);
                        nextSet.add(next);
                    }
                }
            }
            
            startSet = nextSet;
        }
        
        return -1;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        int[][] board1 = {
            {-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1},
            {-1,35,-1,-1,13,-1},
            {-1,-1,-1,-1,-1,-1},
            {-1,15,-1,-1,-1,-1}
        };
        System.out.println("测试用例1 - 最少步数: " + snakesAndLadders(board1)); // 期望输出: 4
        
        // 测试用例2：简单棋盘
        int[][] board2 = {
            {-1,-1},
            {-1, 3}
        };
        System.out.println("测试用例2 - 最少步数: " + snakesAndLadders(board2)); // 期望输出: 1
        
        // 测试用例3：无法到达
        int[][] board3 = {
            {-1,-1,-1},
            {-1, 9, 8},
            {-1, 8, 9}
        };
        System.out.println("测试用例3 - 最少步数: " + snakesAndLadders(board3)); // 期望输出: 1
        
        // 测试用例4：复杂传送
        int[][] board4 = {
            {-1, 4,-1},
            { 6,-1,-1},
            {-1,-1,-1}
        };
        System.out.println("测试用例4 - 最少步数: " + snakesAndLadders(board4)); // 期望输出: 2
    }
}
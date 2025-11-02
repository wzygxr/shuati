package class065;

import java.util.*;

// LeetCode 1293. 网格中的最短路径 - A*算法实现
// 题目链接: https://leetcode.cn/problems/shortest-path-in-a-grid-with-obstacles-elimination/
// 题目描述: 给你一个 m * n 的网格，其中每个单元格不是 0 （空）就是 1 （障碍物）。
// 每一步，您都可以在空白单元格中上、下、左、右移动。
// 如果您最多可以消除 k 个障碍物，请找出从左上角 (0, 0) 到右下角 (m-1, n-1) 的最短路径，
// 并返回通过该路径所需的步数。如果找不到这样的路径，则返回 -1。
//
// A*算法核心思想:
// 使用优先队列按估价函数f=g+h排序状态，其中g是已走步数，h是曼哈顿距离启发函数
// 每个状态由(位置坐标,已走步数,已消除障碍物数)组成
// 通过访问数组避免重复处理相同状态(相同位置且障碍物消除数相同)
//
// 时间复杂度: O(M*N*K*log(M*N*K))，其中M和N是网格的行数和列数，K是最多可以消除的障碍物数量
// 空间复杂度: O(M*N*K)，visited数组和优先队列的最大存储量

public class Code05_AStarLeetcode1293 {
    
    // 方向数组：上、右、下、左
    public static int[] move = new int[] { -1, 0, 1, 0, -1 };
    
    public static int shortestPath(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        
        // 特殊情况：起点就是终点
        if (m == 1 && n == 1) {
            return 0;
        }
        
        // 如果k足够大，可以直接走曼哈顿距离最短路径
        if (k >= m + n - 2) {
            return m + n - 2;
        }
        
        // visited[x][y][obs]表示在位置(x,y)且已经消除obs个障碍物的状态是否已经访问过
        boolean[][][] visited = new boolean[m][n][k + 1];
        
        // 优先队列，存储状态{ x, y, steps, obstacles, f }
        // x: 行坐标
        // y: 列坐标
        // steps: 已走步数
        // obstacles: 已消除障碍物数
        // f: 估价函数值
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[4] - b[4]);
        
        // 初始状态
        int startX = 0, startY = 0;
        int startObstacles = grid[0][0] == 1 ? 1 : 0;
        if (startObstacles <= k) {
            int h = manhattanDistance(0, 0, m - 1, n - 1);
            pq.offer(new int[] { startX, startY, 0, startObstacles, h });
            visited[startX][startY][startObstacles] = true;
        }
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int x = current[0];
            int y = current[1];
            int steps = current[2];
            int obstacles = current[3];
            
            // 到达终点
            if (x == m - 1 && y == n - 1) {
                return steps;
            }
            
            // 四个方向探索
            for (int i = 0; i < 4; i++) {
                int nx = x + move[i];
                int ny = y + move[i + 1];
                
                // 检查边界
                if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                    // 计算新的障碍物数量
                    int newObstacles = obstacles + grid[nx][ny];
                    
                    // 如果障碍物数量不超过k且该状态未访问过
                    if (newObstacles <= k && !visited[nx][ny][newObstacles]) {
                        visited[nx][ny][newObstacles] = true;
                        int newSteps = steps + 1;
                        int h = manhattanDistance(nx, ny, m - 1, n - 1);
                        int f = newSteps + h;
                        pq.offer(new int[] { nx, ny, newSteps, newObstacles, f });
                    }
                }
            }
        }
        
        return -1;
    }
    
    // 曼哈顿距离启发函数
    public static int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1
        int[][] grid1 = {{0,0,0},{1,1,0},{0,0,0},{0,1,1},{0,0,0}};
        int k1 = 1;
        System.out.println("测试用例1结果: " + shortestPath(grid1, k1)); // 期望输出: 6
        
        // 测试用例2
        int[][] grid2 = {{0,1,1},{1,1,1},{1,0,0}};
        int k2 = 1;
        System.out.println("测试用例2结果: " + shortestPath(grid2, k2)); // 期望输出: -1
    }
}


/* ----------------------------- 补充题目1: LeetCode 773. 滑动谜题 ----------------------------- */
// 题目链接: https://leetcode.cn/problems/sliding-puzzle/
// 题目描述: 在一个2x3的板上（board）有5个砖块，数字为1~5，以及一个空位（用0表示）。
// 一次移动定义为选择0与一个相邻的数字（上下左右）进行交换。
// 返回将board变为[[1,2,3],[4,5,0]]的最小移动次数。如果无法完成，则返回-1。

// A*算法解决思路:
// 1. 状态表示: 将2x3网格转换为字符串表示，例如"123450"
// 2. 启发函数: 计算每个数字当前位置到目标位置的曼哈顿距离之和
// 3. 优先队列: 按照f(n) = g(n) + h(n)排序，g(n)是已移动次数，h(n)是启发函数值

class SlidingPuzzleSolver {
    // 目标状态
    private static final String TARGET = "123450";
    // 每个位置的可能移动方向（上下左右）
    private static final int[][] DIRECTIONS = {{1, 3}, {0, 2, 4}, {1, 5}, {0, 4}, {1, 3, 5}, {2, 4}};
    // 每个数字的目标位置
    private static final int[][] TARGET_POSITIONS = {
        {1, 2}, // 0的目标位置是(1,2)
        {0, 0}, {0, 1}, {0, 2}, // 1,2,3的目标位置
        {1, 0}, {1, 1}  // 4,5的目标位置
    };
    
    public int slidingPuzzle(int[][] board) {
        // 将二维数组转换为字符串
        StringBuilder startBuilder = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                startBuilder.append(board[i][j]);
            }
        }
        String start = startBuilder.toString();
        
        // 特殊情况：已经是目标状态
        if (start.equals(TARGET)) {
            return 0;
        }
        
        // 优先队列：按f = g + h排序
        PriorityQueue<State> pq = new PriorityQueue<>((a, b) -> a.f - b.f);
        // 记录已访问的状态
        Set<String> visited = new HashSet<>();
        
        // 初始状态
        int h = calculateHeuristic(start);
        pq.offer(new State(start, 0, h));
        visited.add(start);
        
        while (!pq.isEmpty()) {
            State current = pq.poll();
            String state = current.state;
            int g = current.g;
            
            // 找到0的位置
            int zeroIndex = state.indexOf('0');
            
            // 尝试所有可能的移动
            for (int nextIndex : DIRECTIONS[zeroIndex]) {
                // 交换0和相邻数字
                char[] chars = state.toCharArray();
                chars[zeroIndex] = chars[nextIndex];
                chars[nextIndex] = '0';
                String nextState = new String(chars);
                
                // 如果是目标状态
                if (nextState.equals(TARGET)) {
                    return g + 1;
                }
                
                // 如果没有访问过
                if (!visited.contains(nextState)) {
                    visited.add(nextState);
                    int nextH = calculateHeuristic(nextState);
                    pq.offer(new State(nextState, g + 1, nextH));
                }
            }
        }
        
        // 无法到达目标状态
        return -1;
    }
    
    // 计算启发函数：每个数字的曼哈顿距离之和
    private int calculateHeuristic(String state) {
        int heuristic = 0;
        for (int i = 0; i < state.length(); i++) {
            char c = state.charAt(i);
            if (c != '0') { // 忽略空格
                int num = c - '0';
                int row = i / 3;
                int col = i % 3;
                // 计算曼哈顿距离
                heuristic += Math.abs(row - TARGET_POSITIONS[num][0]) + 
                             Math.abs(col - TARGET_POSITIONS[num][1]);
            }
        }
        return heuristic;
    }
    
    // 状态类
    class State {
        String state; // 当前状态
        int g;        // 已走步数
        int h;        // 启发函数值
        int f;        // f = g + h
        
        State(String state, int g, int h) {
            this.state = state;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }
}

/* ----------------------------- 补充题目2: 八数码问题 ----------------------------- */
// 题目描述: 在3x3的网格中，放置了数字1到8的方块，以及一个空格，目标是将数字方块滑动成特定顺序
// A*算法解决思路:
// 1. 启发函数: 曼哈顿距离或错位棋子数
// 2. 状态表示: 3x3网格的一维表示
// 3. 优先队列按f = g + h排序，其中g是已走步数

/* ----------------------------- 补充题目3: LeetCode 1129. 颜色交替的最短路径 ----------------------------- */
// 题目链接: https://leetcode.cn/problems/shortest-path-with-alternating-colors/
// 可以使用A*算法优化，启发函数设计为当前节点到目标节点的估计距离

/* ----------------------------- A*算法工程实践建议 ----------------------------- */
// 1. 启发函数设计关键原则:
//    - 必须是可接受的（不能高估实际代价）
//    - 尽可能接近实际代价以提高效率
//    - 对于网格问题: 曼哈顿距离(四向移动)、切比雪夫距离(八向移动)、欧式距离(任意移动)
//
// 2. 性能优化技巧:
//    - 使用优先队列实现，通常用堆数据结构
//    - 维护开放列表(优先队列)和关闭列表(已处理节点)
//    - 使用哈希表或数组快速检查节点是否已访问
//    - 对于大规模问题，考虑使用迭代加深A*算法(IDA*)
//
// 3. 常见应用场景:
//    - 游戏开发中的寻路算法
//    - 机器人路径规划
//    - 迷宫求解
//    - 地图导航
//    - 资源调度优化
//
// 4. 与其他算法对比:
//    - 相比BFS: A*有目标导向，通常更快找到最短路径
//    - 相比Dijkstra: 当有明确目标时，A*效率更高
//    - 相比贪心最佳优先搜索: A*能保证找到最短路径
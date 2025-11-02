package class062;

import java.util.*;

// 为高尔夫比赛砍树
// 你被请来给一个要举办高尔夫比赛的树林砍树。树林由一个 m x n 的矩阵表示， 在这个矩阵中：
// 0 表示障碍，无法触碰
// 1 表示地面，可以行走
// 比 1 大的数表示有树的单元格，可以行走，数值表示树的高度
// 每一步，你都可以向上、下、左、右四个方向之一移动一个单位。
// 你需要按照树的高度从低向高砍掉所有的树，每砍过一颗树，该单元格的值变为 1（即变为地面）。
// 返回砍完所有树需要走的最小步数。 如果你无法砍完所有的树，返回 -1 。
// 测试链接 : https://leetcode.cn/problems/cut-off-trees-for-golf-event/
// 
// 算法思路：
// 1. 首先收集所有需要砍的树，按高度排序
// 2. 从起点(0,0)开始，依次计算到每棵树的最短路径
// 3. 使用BFS计算两点之间的最短路径
// 4. 累加所有路径长度即为总步数
// 
// 时间复杂度：O(T * m * n)，其中T是树的数量，m和n是矩阵尺寸
// 空间复杂度：O(m * n)，用于BFS队列和访问状态
// 
// 工程化考量：
// 1. 树的高度排序：确保按正确顺序砍树
// 2. 最短路径计算：对每对相邻树计算BFS最短路径
// 3. 障碍物处理：0表示障碍，无法通过
// 4. 性能优化：使用A*算法或双向BFS优化大规模数据
public class Code26_CutOffTreesForGolfEvent {

    // 四个方向的移动：上、右、下、左
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    
    public static int cutOffTree(List<List<Integer>> forest) {
        if (forest == null || forest.isEmpty() || forest.get(0).isEmpty()) {
            return 0;
        }
        
        int m = forest.size();
        int n = forest.get(0).size();
        
        // 步骤1：收集所有需要砍的树，按高度排序
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int height = forest.get(i).get(j);
                if (height > 1) {
                    trees.add(new int[]{i, j, height});
                }
            }
        }
        
        // 按树的高度排序
        trees.sort((a, b) -> a[2] - b[2]);
        
        // 步骤2：依次计算从当前位置到每棵树的最短路径
        int totalSteps = 0;
        int startX = 0, startY = 0;
        
        for (int[] tree : trees) {
            int targetX = tree[0];
            int targetY = tree[1];
            
            // 计算从当前位置到目标树的最短路径
            int steps = bfs(forest, startX, startY, targetX, targetY, m, n);
            if (steps == -1) {
                return -1; // 无法到达某棵树
            }
            
            totalSteps += steps;
            // 更新当前位置为砍完树后的位置
            startX = targetX;
            startY = targetY;
            // 砍掉树，该位置变为地面
            forest.get(startX).set(startY, 1);
        }
        
        return totalSteps;
    }
    
    // BFS计算两点之间的最短路径
    private static int bfs(List<List<Integer>> forest, int startX, int startY, 
                          int targetX, int targetY, int m, int n) {
        // 边界情况：起点就是终点
        if (startX == targetX && startY == targetY) {
            return 0;
        }
        
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        
        queue.offer(new int[]{startX, startY});
        visited[startX][startY] = true;
        int steps = 0;
        
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                
                for (int[] dir : DIRECTIONS) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    
                    // 检查边界、障碍物和访问状态
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && 
                        !visited[nx][ny] && forest.get(nx).get(ny) != 0) {
                        
                        // 如果到达目标
                        if (nx == targetX && ny == targetY) {
                            return steps;
                        }
                        
                        visited[nx][ny] = true;
                        queue.offer(new int[]{nx, ny});
                    }
                }
            }
        }
        
        return -1; // 无法到达目标
    }
    
    // 优化版本：使用A*算法优化大规模数据
    public static int cutOffTreeAStar(List<List<Integer>> forest) {
        if (forest == null || forest.isEmpty() || forest.get(0).isEmpty()) {
            return 0;
        }
        
        int m = forest.size();
        int n = forest.get(0).size();
        
        // 收集所有需要砍的树，按高度排序
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int height = forest.get(i).get(j);
                if (height > 1) {
                    trees.add(new int[]{i, j, height});
                }
            }
        }
        
        trees.sort((a, b) -> a[2] - b[2]);
        
        int totalSteps = 0;
        int startX = 0, startY = 0;
        
        for (int[] tree : trees) {
            int targetX = tree[0];
            int targetY = tree[1];
            
            int steps = aStar(forest, startX, startY, targetX, targetY, m, n);
            if (steps == -1) {
                return -1;
            }
            
            totalSteps += steps;
            startX = targetX;
            startY = targetY;
            forest.get(startX).set(startY, 1);
        }
        
        return totalSteps;
    }
    
    // A*算法实现
    private static int aStar(List<List<Integer>> forest, int startX, int startY,
                            int targetX, int targetY, int m, int n) {
        if (startX == targetX && startY == targetY) return 0;
        
        // 优先队列，按f值（g + h）排序
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            int f1 = a[2] + a[3]; // g + h
            int f2 = b[2] + b[3];
            return f1 - f2;
        });
        
        boolean[][] visited = new boolean[m][n];
        
        // (x, y, g, h)
        pq.offer(new int[]{startX, startY, 0, heuristic(startX, startY, targetX, targetY)});
        visited[startX][startY] = true;
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int x = current[0];
            int y = current[1];
            int g = current[2];
            
            for (int[] dir : DIRECTIONS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && 
                    !visited[nx][ny] && forest.get(nx).get(ny) != 0) {
                    
                    if (nx == targetX && ny == targetY) {
                        return g + 1;
                    }
                    
                    visited[nx][ny] = true;
                    int newG = g + 1;
                    int h = heuristic(nx, ny, targetX, targetY);
                    pq.offer(new int[]{nx, ny, newG, h});
                }
            }
        }
        
        return -1;
    }
    
    // 启发式函数：曼哈顿距离
    private static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        List<List<Integer>> forest1 = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(0, 0, 4),
            Arrays.asList(7, 6, 5)
        );
        System.out.println("测试用例1 - 最少步数: " + cutOffTree(forest1)); // 期望输出: 6
        
        // 测试用例2：无法砍完所有树
        List<List<Integer>> forest2 = Arrays.asList(
            Arrays.asList(1, 1, 0, 2),
            Arrays.asList(0, 0, 0, 3),
            Arrays.asList(0, 0, 0, 4)
        );
        System.out.println("测试用例2 - 最少步数: " + cutOffTree(forest2)); // 期望输出: -1
        
        // 测试用例3：简单情况
        List<List<Integer>> forest3 = Arrays.asList(
            Arrays.asList(2, 3, 4),
            Arrays.asList(0, 0, 5),
            Arrays.asList(8, 7, 6)
        );
        System.out.println("测试用例3 - 最少步数: " + cutOffTree(forest3)); // 期望输出: 6
    }
}
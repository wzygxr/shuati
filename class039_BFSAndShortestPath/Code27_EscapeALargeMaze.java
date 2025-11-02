package class062;

import java.util.*;

// 逃离大迷宫
// 在一个 10^6 x 10^6 的网格中，每个网格块的坐标为 (x, y)，其中 0 <= x, y < 10^6。
// 我们从源方格 source 开始出发，意图赶往目标方格 target。每次移动，我们都可以走到网格中在四个方向上相邻的方格。
// 但是网格中有一些障碍物，用数组 blocked 表示，其中 blocked[i] = [xi, yi] 表示坐标为 (xi, yi) 的方格是障碍物。
// 只有在网格中不被障碍物阻挡的方格才能通过。
// 如果我们可以从源方格到达目标方格，返回 true；否则返回 false。
// 测试链接 : https://leetcode.cn/problems/escape-a-large-maze/
// 
// 算法思路：
// 由于网格非常大（10^6 x 10^6），不能直接使用BFS遍历整个网格。
// 关键观察：如果障碍物无法将起点和终点完全隔离，那么只需要搜索有限的范围。
// 使用有限BFS：如果从起点或终点能够到达超过一定数量的点，说明没有被障碍物完全包围。
// 
// 时间复杂度：O(B^2)，其中B是障碍物的数量
// 空间复杂度：O(B^2)，用于存储访问状态
// 
// 工程化考量：
// 1. 有限BFS：设置最大搜索点数，避免无限搜索
// 2. 双向搜索：同时从起点和终点开始搜索
// 3. 哈希优化：使用HashSet存储障碍物和访问点，提高查找效率
// 4. 边界判断：搜索范围限制
public class Code27_EscapeALargeMaze {

    // 四个方向的移动
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    // 最大搜索点数（基于障碍物数量的平方）
    private static final int MAX_SEARCH = 20000;
    
    public static boolean isEscapePossible(int[][] blocked, int[] source, int[] target) {
        if (blocked == null || blocked.length == 0) {
            return true; // 没有障碍物，肯定可以到达
        }
        
        // 将障碍物转换为集合，提高查找效率
        Set<Long> blockedSet = new HashSet<>();
        for (int[] block : blocked) {
            blockedSet.add(hash(block[0], block[1]));
        }
        
        // 双向BFS搜索
        return bfs(blockedSet, source, target) && bfs(blockedSet, target, source);
    }
    
    private static boolean bfs(Set<Long> blockedSet, int[] start, int[] target) {
        Set<Long> visited = new HashSet<>();
        Queue<long[]> queue = new LinkedList<>();
        
        long startHash = hash(start[0], start[1]);
        long targetHash = hash(target[0], target[1]);
        
        queue.offer(new long[]{start[0], start[1]});
        visited.add(startHash);
        
        int searched = 0;
        
        while (!queue.isEmpty() && searched < MAX_SEARCH) {
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                long[] current = queue.poll();
                long x = current[0];
                long y = current[1];
                
                // 如果到达目标点
                if (x == target[0] && y == target[1]) {
                    return true;
                }
                
                searched++;
                
                // 向四个方向扩展
                for (int[] dir : DIRECTIONS) {
                    long nx = x + dir[0];
                    long ny = y + dir[1];
                    long nextHash = hash(nx, ny);
                    
                    // 检查边界、障碍物和访问状态
                    if (nx >= 0 && nx < 1000000 && ny >= 0 && ny < 1000000 &&
                        !blockedSet.contains(nextHash) && !visited.contains(nextHash)) {
                        
                        visited.add(nextHash);
                        queue.offer(new long[]{nx, ny});
                    }
                }
            }
            
            // 如果搜索的点数足够多，说明没有被障碍物完全包围
            if (searched >= MAX_SEARCH) {
                return true;
            }
        }
        
        return searched >= MAX_SEARCH;
    }
    
    // 优化版本：使用双向BFS同时搜索
    public static boolean isEscapePossibleBidirectional(int[][] blocked, int[] source, int[] target) {
        if (blocked == null || blocked.length == 0) return true;
        
        Set<Long> blockedSet = new HashSet<>();
        for (int[] block : blocked) {
            blockedSet.add(hash(block[0], block[1]));
        }
        
        Set<Long> visited1 = new HashSet<>();
        Set<Long> visited2 = new HashSet<>();
        Queue<long[]> queue1 = new LinkedList<>();
        Queue<long[]> queue2 = new LinkedList<>();
        
        long sourceHash = hash(source[0], source[1]);
        long targetHash = hash(target[0], target[1]);
        
        queue1.offer(new long[]{source[0], source[1]});
        queue2.offer(new long[]{target[0], target[1]});
        visited1.add(sourceHash);
        visited2.add(targetHash);
        
        int searched1 = 0, searched2 = 0;
        
        while ((!queue1.isEmpty() && searched1 < MAX_SEARCH) || 
               (!queue2.isEmpty() && searched2 < MAX_SEARCH)) {
            
            // 从起点搜索
            if (!queue1.isEmpty() && searched1 < MAX_SEARCH) {
                int size = queue1.size();
                for (int i = 0; i < size; i++) {
                    long[] current = queue1.poll();
                    long x = current[0], y = current[1];
                    
                    // 检查是否与终点搜索相遇
                    if (visited2.contains(hash(x, y))) {
                        return true;
                    }
                    
                    searched1++;
                    
                    for (int[] dir : DIRECTIONS) {
                        long nx = x + dir[0], ny = y + dir[1];
                        long nextHash = hash(nx, ny);
                        
                        if (isValid(nx, ny) && !blockedSet.contains(nextHash) && !visited1.contains(nextHash)) {
                            visited1.add(nextHash);
                            queue1.offer(new long[]{nx, ny});
                        }
                    }
                }
                
                if (searched1 >= MAX_SEARCH) return true;
            }
            
            // 从终点搜索
            if (!queue2.isEmpty() && searched2 < MAX_SEARCH) {
                int size = queue2.size();
                for (int i = 0; i < size; i++) {
                    long[] current = queue2.poll();
                    long x = current[0], y = current[1];
                    
                    if (visited1.contains(hash(x, y))) {
                        return true;
                    }
                    
                    searched2++;
                    
                    for (int[] dir : DIRECTIONS) {
                        long nx = x + dir[0], ny = y + dir[1];
                        long nextHash = hash(nx, ny);
                        
                        if (isValid(nx, ny) && !blockedSet.contains(nextHash) && !visited2.contains(nextHash)) {
                            visited2.add(nextHash);
                            queue2.offer(new long[]{nx, ny});
                        }
                    }
                }
                
                if (searched2 >= MAX_SEARCH) return true;
            }
        }
        
        return false;
    }
    
    private static boolean isValid(long x, long y) {
        return x >= 0 && x < 1000000 && y >= 0 && y < 1000000;
    }
    
    // 哈希函数：将二维坐标映射为一维长整型
    private static long hash(long x, long y) {
        return x * 1000001 + y;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：可以被障碍物阻挡
        int[][] blocked1 = {{0,1},{1,0}};
        int[] source1 = {0,0};
        int[] target1 = {0,2};
        System.out.println("测试用例1 - 是否可以逃离: " + isEscapePossible(blocked1, source1, target1)); // 期望输出: false
        
        // 测试用例2：无法被障碍物阻挡
        int[][] blocked2 = {};
        int[] source2 = {0,0};
        int[] target2 = {999999,999999};
        System.out.println("测试用例2 - 是否可以逃离: " + isEscapePossible(blocked2, source2, target2)); // 期望输出: true
        
        // 测试用例3：障碍物形成环但仍有路径
        int[][] blocked3 = {{0,3},{1,3},{2,3},{3,3},{3,2},{3,1},{3,0}};
        int[] source3 = {0,0};
        int[] target3 = {3,3};
        System.out.println("测试用例3 - 是否可以逃离: " + isEscapePossible(blocked3, source3, target3)); // 期望输出: false
    }
}
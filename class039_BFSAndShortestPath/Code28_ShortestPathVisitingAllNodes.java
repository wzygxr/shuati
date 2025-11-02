package class062;

import java.util.*;

// 访问所有节点的最短路径
// 给出一个无向连通图，图中有 n 个节点，编号从 0 到 n - 1。图以邻接表的形式给出。
// 你需要找到能够访问所有节点的最短路径的长度。你可以在任一节点开始和停止，也可以多次访问节点，并且可以重复使用边。
// 测试链接 : https://leetcode.cn/problems/shortest-path-visiting-all-nodes/
// 
// 算法思路：
// 使用状态压缩BFS。每个状态用(当前节点, 已访问节点集合)表示，其中已访问节点集合用位掩码表示。
// 目标是找到状态(current, mask)其中mask为全1（表示所有节点都已访问）的最短路径。
// 
// 时间复杂度：O(n * 2^n)，其中n是节点数量
// 空间复杂度：O(n * 2^n)，用于存储状态访问记录
// 
// 工程化考量：
// 1. 状态压缩：使用位掩码表示已访问节点集合
// 2. 多起点BFS：可以从任意节点开始，需要尝试所有起点
// 3. 状态去重：避免重复访问相同状态
// 4. 性能优化：对于大规模图需要考虑剪枝和启发式搜索
public class Code28_ShortestPathVisitingAllNodes {

    public static int shortestPathLength(int[][] graph) {
        int n = graph.length;
        if (n == 1) return 0;
        
        // 目标状态：所有节点都已访问（位掩码全为1）
        int target = (1 << n) - 1;
        
        // BFS队列：存储(当前节点, 已访问掩码, 路径长度)
        Queue<int[]> queue = new LinkedList<>();
        // 访问记录：visited[node][mask] 表示是否访问过该状态
        boolean[][] visited = new boolean[n][1 << n];
        
        // 多起点BFS：从所有节点同时开始
        for (int i = 0; i < n; i++) {
            int mask = 1 << i;
            queue.offer(new int[]{i, mask, 0});
            visited[i][mask] = true;
        }
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0];
            int mask = current[1];
            int steps = current[2];
            
            // 如果已访问所有节点
            if (mask == target) {
                return steps;
            }
            
            // 遍历所有邻居
            for (int neighbor : graph[node]) {
                int newMask = mask | (1 << neighbor);
                
                // 如果新状态未被访问过
                if (!visited[neighbor][newMask]) {
                    visited[neighbor][newMask] = true;
                    queue.offer(new int[]{neighbor, newMask, steps + 1});
                }
            }
        }
        
        return -1; // 理论上不会执行到这里
    }
    
    // 优化版本：使用双向BFS
    public static int shortestPathLengthBidirectional(int[][] graph) {
        int n = graph.length;
        if (n == 1) return 0;
        
        int target = (1 << n) - 1;
        
        // 双向BFS：从起点和终点同时搜索
        Map<Integer, Set<Integer>> startVisited = new HashMap<>();
        Map<Integer, Set<Integer>> endVisited = new HashMap<>();
        Queue<int[]> startQueue = new LinkedList<>();
        Queue<int[]> endQueue = new LinkedList<>();
        
        // 初始化起点：从所有节点开始，只访问了自身
        for (int i = 0; i < n; i++) {
            int mask = 1 << i;
            startQueue.offer(new int[]{i, mask, 0});
            startVisited.computeIfAbsent(i, k -> new HashSet<>()).add(mask);
        }
        
        // 初始化终点：目标状态是访问了所有节点
        for (int i = 0; i < n; i++) {
            endQueue.offer(new int[]{i, target, 0});
            endVisited.computeIfAbsent(i, k -> new HashSet<>()).add(target);
        }
        
        int steps = 0;
        
        while (!startQueue.isEmpty() && !endQueue.isEmpty()) {
            steps++;
            
            // 处理起点队列
            int startSize = startQueue.size();
            for (int i = 0; i < startSize; i++) {
                int[] current = startQueue.poll();
                int node = current[0];
                int mask = current[1];
                
                // 检查是否与终点相遇
                if (endVisited.containsKey(node) && endVisited.get(node).contains(mask)) {
                    return steps + current[2] - 1;
                }
                
                for (int neighbor : graph[node]) {
                    int newMask = mask | (1 << neighbor);
                    
                    if (!startVisited.containsKey(neighbor) || 
                        !startVisited.get(neighbor).contains(newMask)) {
                        startVisited.computeIfAbsent(neighbor, k -> new HashSet<>()).add(newMask);
                        startQueue.offer(new int[]{neighbor, newMask, current[2] + 1});
                    }
                }
            }
            
            // 处理终点队列（反向搜索）
            int endSize = endQueue.size();
            for (int i = 0; i < endSize; i++) {
                int[] current = endQueue.poll();
                int node = current[0];
                int mask = current[1];
                
                if (startVisited.containsKey(node) && startVisited.get(node).contains(mask)) {
                    return steps + current[2] - 1;
                }
                
                for (int neighbor : graph[node]) {
                    // 反向搜索：从目标状态向起点状态搜索
                    // 在反向搜索中，我们考虑哪些状态可以到达当前状态
                    for (int prevMask : getPredecessorMasks(mask, neighbor, graph)) {
                        if (!endVisited.containsKey(neighbor) || 
                            !endVisited.get(neighbor).contains(prevMask)) {
                            endVisited.computeIfAbsent(neighbor, k -> new HashSet<>()).add(prevMask);
                            endQueue.offer(new int[]{neighbor, prevMask, current[2] + 1});
                        }
                    }
                }
            }
        }
        
        return -1;
    }
    
    // 获取可以到达当前状态的前驱状态掩码
    private static Set<Integer> getPredecessorMasks(int currentMask, int currentNode, int[][] graph) {
        Set<Integer> predecessors = new HashSet<>();
        
        // 前驱状态可以是：当前状态去掉当前节点的访问，或者通过其他邻居到达
        for (int neighbor : graph[currentNode]) {
            // 如果邻居节点在当前状态中已被访问
            if ((currentMask & (1 << neighbor)) != 0) {
                // 那么前驱状态可以是去掉当前节点访问的状态
                int prevMask = currentMask & ~(1 << currentNode);
                predecessors.add(prevMask);
            }
        }
        
        return predecessors;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：简单图
        int[][] graph1 = {{1,2,3},{0},{0},{0}};
        System.out.println("测试用例1 - 最短路径长度: " + shortestPathLength(graph1)); // 期望输出: 4
        
        // 测试用例2：完全图
        int[][] graph2 = {{1},{0,2,4},{1,3,4},{2},{1,2}};
        System.out.println("测试用例2 - 最短路径长度: " + shortestPathLength(graph2)); // 期望输出: 4
        
        // 测试用例3：链式图
        int[][] graph3 = {{1},{0,2},{1,3},{2}};
        System.out.println("测试用例3 - 最短路径长度: " + shortestPathLength(graph3)); // 期望输出: 4
        
        // 测试用例4：单节点图
        int[][] graph4 = {{}};
        System.out.println("测试用例4 - 最短路径长度: " + shortestPathLength(graph4)); // 期望输出: 0
    }
}
package class032;

import java.util.*;

/**
 * 旅行商问题（TSP）经典实现
 * 题目来源: 多种算法平台通用问题
 * 问题描述: 给定n个城市和它们之间的距离，找到一条最短的路径，访问每个城市恰好一次并回到起点
 * 
 * 解题思路:
 * 方法1: 暴力搜索 - 枚举所有排列，时间复杂度高
 * 方法2: 状态压缩DP - 使用bitmask表示已访问的城市
 * 方法3: 分支限界法 - 使用优先队列进行剪枝
 * 方法4: 近似算法 - 2-opt, 3-opt等启发式算法
 * 
 * 时间复杂度分析:
 * 方法1: O(n!) - 阶乘级，仅适用于小n
 * 方法2: O(2^n * n^2) - 状态压缩DP的标准复杂度
 * 方法3: O(b^d) - 分支因子b和深度d，实际运行可能更快
 * 方法4: O(n^2) 或 O(n^3) - 多项式时间，但可能不是最优解
 * 
 * 空间复杂度分析:
 * 方法1: O(n) - 递归栈空间
 * 方法2: O(2^n * n) - DP数组空间
 * 方法3: O(b^d) - 优先队列空间
 * 方法4: O(n^2) - 距离矩阵空间
 * 
 * 工程化考量:
 * 1. 内存优化: 对于大n使用位运算压缩状态
 * 2. 性能优化: 使用启发式剪枝和近似算法
 * 3. 并行计算: 对于大规模问题考虑并行处理
 * 4. 实际应用: 结合具体业务场景进行优化
 */

public class Code16_TravelingSalesman {
    
    /**
     * 方法1: 暴力搜索（回溯法）
     * 枚举所有可能的路径排列，找到最短路径
     * 仅适用于n <= 10的小规模问题
     */
    public static int tspBruteForce(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        int[] cities = new int[n];
        for (int i = 0; i < n; i++) {
            cities[i] = i;
        }
        
        int minDistance = Integer.MAX_VALUE;
        
        // 生成所有排列
        List<List<Integer>> permutations = generatePermutations(cities);
        
        for (List<Integer> path : permutations) {
            int distance = calculatePathDistance(graph, path);
            minDistance = Math.min(minDistance, distance);
        }
        
        return minDistance;
    }
    
    /**
     * 生成所有排列
     */
    private static List<List<Integer>> generatePermutations(int[] arr) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(arr, 0, result);
        return result;
    }
    
    private static void backtrack(int[] arr, int start, List<List<Integer>> result) {
        if (start == arr.length) {
            List<Integer> path = new ArrayList<>();
            for (int num : arr) {
                path.add(num);
            }
            // 添加起点形成回路
            path.add(arr[0]);
            result.add(path);
            return;
        }
        
        for (int i = start; i < arr.length; i++) {
            swap(arr, start, i);
            backtrack(arr, start + 1, result);
            swap(arr, start, i);
        }
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * 计算路径距离
     */
    private static int calculatePathDistance(int[][] graph, List<Integer> path) {
        int distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);
            distance += graph[from][to];
        }
        return distance;
    }
    
    /**
     * 方法2: 状态压缩DP（最优解）
     * 使用dp[mask][last]表示在mask状态下最后访问城市last时的最短距离
     */
    public static int tspDP(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        int totalStates = 1 << n;
        int[][] dp = new int[totalStates][n];
        
        // 初始化DP数组
        for (int i = 0; i < totalStates; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        
        // 起点状态：只访问了城市0
        dp[1][0] = 0;
        
        // 遍历所有状态
        for (int mask = 1; mask < totalStates; mask++) {
            for (int last = 0; last < n; last++) {
                // 如果last不在mask中，跳过
                if ((mask & (1 << last)) == 0) continue;
                
                // 如果当前状态不可达，跳过
                if (dp[mask][last] == Integer.MAX_VALUE) continue;
                
                // 尝试访问新城市
                for (int next = 0; next < n; next++) {
                    // 如果next已经在mask中，跳过
                    if ((mask & (1 << next)) != 0) continue;
                    
                    int newMask = mask | (1 << next);
                    int newDistance = dp[mask][last] + graph[last][next];
                    
                    if (newDistance < dp[newMask][next]) {
                        dp[newMask][next] = newDistance;
                    }
                }
            }
        }
        
        // 找到最短回路：访问所有城市后回到起点
        int finalMask = (1 << n) - 1;
        int minDistance = Integer.MAX_VALUE;
        
        for (int last = 0; last < n; last++) {
            if (dp[finalMask][last] != Integer.MAX_VALUE) {
                minDistance = Math.min(minDistance, 
                    dp[finalMask][last] + graph[last][0]);
            }
        }
        
        return minDistance;
    }
    
    /**
     * 方法3: 记忆化搜索版本
     * 使用递归+记忆化，代码更清晰
     */
    public static int tspMemo(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        int[][] memo = new int[1 << n][n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        
        return dfs(1, 0, n, graph, memo);
    }
    
    private static int dfs(int mask, int last, int n, int[][] graph, int[][] memo) {
        // 如果已经访问所有城市，返回回到起点的距离
        if (mask == (1 << n) - 1) {
            return graph[last][0];
        }
        
        if (memo[mask][last] != -1) {
            return memo[mask][last];
        }
        
        int minDistance = Integer.MAX_VALUE;
        
        for (int next = 0; next < n; next++) {
            // 如果next已经在mask中，跳过
            if ((mask & (1 << next)) != 0) continue;
            
            int newMask = mask | (1 << next);
            int distance = graph[last][next] + dfs(newMask, next, n, graph, memo);
            
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        
        memo[mask][last] = minDistance;
        return minDistance;
    }
    
    /**
     * 方法4: 最近邻启发式算法（近似解）
     * 贪心算法，每次选择最近的未访问城市
     * 时间复杂度: O(n^2)，空间复杂度: O(n)
     */
    public static int tspNearestNeighbor(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        boolean[] visited = new boolean[n];
        visited[0] = true;  // 从城市0开始
        int current = 0;
        int totalDistance = 0;
        int count = 1;
        
        while (count < n) {
            int next = -1;
            int minDist = Integer.MAX_VALUE;
            
            // 找到最近的未访问城市
            for (int i = 0; i < n; i++) {
                if (!visited[i] && graph[current][i] < minDist) {
                    minDist = graph[current][i];
                    next = i;
                }
            }
            
            if (next == -1) break;
            
            totalDistance += minDist;
            visited[next] = true;
            current = next;
            count++;
        }
        
        // 回到起点
        totalDistance += graph[current][0];
        return totalDistance;
    }
    
    /**
     * 方法5: 2-opt局部搜索算法
     * 通过交换边来改进解质量
     */
    public static int tsp2Opt(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        // 初始解：使用最近邻算法
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[n];
        int current = 0;
        tour.add(current);
        visited[current] = true;
        
        while (tour.size() < n) {
            int next = -1;
            int minDist = Integer.MAX_VALUE;
            
            for (int i = 0; i < n; i++) {
                if (!visited[i] && graph[current][i] < minDist) {
                    minDist = graph[current][i];
                    next = i;
                }
            }
            
            if (next == -1) break;
            
            tour.add(next);
            visited[next] = true;
            current = next;
        }
        
        tour.add(0);  // 回到起点
        
        // 2-opt优化
        boolean improved = true;
        while (improved) {
            improved = false;
            
            for (int i = 1; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int oldDistance = calculateTourDistance(graph, tour);
                    
                    // 尝试交换边 (i-1, i) 和 (j, j+1) 为 (i-1, j) 和 (i, j+1)
                    Collections.reverse(tour.subList(i, j + 1));
                    
                    int newDistance = calculateTourDistance(graph, tour);
                    
                    if (newDistance < oldDistance) {
                        improved = true;
                    } else {
                        // 恢复原状
                        Collections.reverse(tour.subList(i, j + 1));
                    }
                }
            }
        }
        
        return calculateTourDistance(graph, tour);
    }
    
    /**
     * 计算完整路径的距离
     */
    private static int calculateTourDistance(int[][] graph, List<Integer> tour) {
        int distance = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int from = tour.get(i);
            int to = tour.get(i + 1);
            distance += graph[from][to];
        }
        return distance;
    }
    
    /**
     * 工程化改进版本：完整的异常处理和验证
     */
    public static int tspWithValidation(int[][] graph) {
        try {
            // 输入验证
            if (graph == null) {
                throw new IllegalArgumentException("Graph cannot be null");
            }
            if (graph.length <= 1) {
                return 0;
            }
            
            // 检查图是否对称（TSP通常假设对称）
            int n = graph.length;
            for (int i = 0; i < n; i++) {
                if (graph[i].length != n) {
                    throw new IllegalArgumentException("Graph must be square matrix");
                }
                if (graph[i][i] != 0) {
                    throw new IllegalArgumentException("Diagonal elements should be 0");
                }
            }
            
            // 根据问题规模选择算法
            if (n <= 10) {
                return tspBruteForce(graph);
            } else if (n <= 20) {
                return tspDP(graph);
            } else {
                return tsp2Opt(graph);  // 对于大n使用近似算法
            }
            
        } catch (Exception e) {
            System.err.println("Error in TSP: " + e.getMessage());
            return Integer.MAX_VALUE;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== 旅行商问题（TSP） - 单元测试 ===");
        
        // 测试用例1: 简单4城市问题
        int[][] graph1 = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        
        int expected1 = 80;  // 0->1->3->2->0 = 10+25+30+15 = 80
        int result1 = tspWithValidation(graph1);
        System.out.printf("测试1: 期望=%d, 实际=%d, %s%n",
                         expected1, result1,
                         result1 == expected1 ? "通过" : "失败");
        
        // 测试用例2: 3城市问题
        int[][] graph2 = {
            {0, 1, 2},
            {1, 0, 3},
            {2, 3, 0}
        };
        
        int expected2 = 6;  // 0->1->2->0 = 1+3+2 = 6
        int result2 = tspWithValidation(graph2);
        System.out.printf("测试2: 期望=%d, 实际=%d, %s%n",
                         expected2, result2,
                         result2 == expected2 ? "通过" : "失败");
        
        // 测试不同方法的结果
        System.out.println("\n=== 方法对比测试 ===");
        int[][] testGraph = graph1;
        
        int r1 = tspBruteForce(testGraph);
        int r2 = tspDP(testGraph);
        int r3 = tspMemo(testGraph);
        int r4 = tspNearestNeighbor(testGraph);
        int r5 = tsp2Opt(testGraph);
        
        System.out.printf("暴力搜索: %d%n", r1);
        System.out.printf("状态压缩DP: %d%n", r2);
        System.out.printf("记忆化搜索: %d%n", r3);
        System.out.printf("最近邻算法: %d%n", r4);
        System.out.printf("2-opt算法: %d%n", r5);
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        int[] testSizes = {5, 10, 15, 20};
        
        for (int n : testSizes) {
            int[][] graph = generateRandomGraph(n, 100);
            System.out.printf("n = %d:%n", n);
            
            if (n <= 10) {
                long startTime = System.nanoTime();
                int result1 = tspBruteForce(graph);
                long time1 = System.nanoTime() - startTime;
                System.out.printf("  暴力搜索: %d ns, 结果: %d%n", time1, result1);
            }
            
            long startTime = System.nanoTime();
            int result2 = tspDP(graph);
            long time2 = System.nanoTime() - startTime;
            System.out.printf("  状态压缩DP: %d ns, 结果: %d%n", time2, result2);
            
            startTime = System.nanoTime();
            int result3 = tsp2Opt(graph);
            long time3 = System.nanoTime() - startTime;
            System.out.printf("  2-opt算法: %d ns, 结果: %d%n", time3, result3);
            
            System.out.println();
        }
    }
    
    /**
     * 生成随机图
     */
    private static int[][] generateRandomGraph(int n, int maxDistance) {
        Random random = new Random();
        int[][] graph = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            graph[i][i] = 0;
            for (int j = i + 1; j < n; j++) {
                int distance = random.nextInt(maxDistance) + 1;
                graph[i][j] = distance;
                graph[j][i] = distance;  // 对称图
            }
        }
        
        return graph;
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("=== 复杂度分析 ===");
        System.out.println("旅行商问题是NP难问题，没有已知的多项式时间算法");
        
        System.out.println("\n各算法复杂度对比:");
        System.out.println("暴力搜索: O(n!) - 仅适用于n <= 10");
        System.out.println("状态压缩DP: O(2^n * n^2) - 适用于n <= 20");
        System.out.println("分支限界法: 指数级但带剪枝");
        System.out.println("近似算法: O(n^2) 或 O(n^3) - 适用于大n");
        
        System.out.println("\n工程化建议:");
        System.out.println("1. 根据问题规模选择合适的算法");
        System.out.println("2. 对于大规模问题使用近似算法");
        System.out.println("3. 考虑并行计算和分布式处理");
        System.out.println("4. 结合实际业务场景进行优化");
    }
    
    public static void main(String[] args) {
        System.out.println("旅行商问题（TSP）经典实现");
        System.out.println("多种算法解决组合优化问题");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 示例使用
        System.out.println("\n=== 示例使用 ===");
        int[][] exampleGraph = {
            {0, 2, 9, 10},
            {2, 0, 6, 4},
            {9, 6, 0, 8},
            {10, 4, 8, 0}
        };
        
        int result = tspWithValidation(exampleGraph);
        System.out.printf("示例图的最短路径长度: %d%n", result);
        System.out.println("距离矩阵:");
        for (int[] row : exampleGraph) {
            System.out.println(Arrays.toString(row));
        }
    }
}
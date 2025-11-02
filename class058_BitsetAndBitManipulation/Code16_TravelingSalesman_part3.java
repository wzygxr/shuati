package class032;

import java.util.*;

/**
 * 旅行商问题（TSP）第三部分 - 测试和工具方法
 */

public class Code16_TravelingSalesman_part3 {
    
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
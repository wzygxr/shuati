package class176_MoAlgorithm;

import java.util.*;

/**
 * Mo's Algorithm 综合测试类
 * 验证所有实现的正确性和性能
 */
public class MoAlgorithm_Comprehensive_Test {
    
    public static void main(String[] args) {
        System.out.println("=== Mo's Algorithm 综合测试开始 ===\n");
        
        // 测试1: 基础Mo's Algorithm
        testBasicMoAlgorithm();
        
        // 测试2: DQUERY问题
        testDQUERY();
        
        // 测试3: COT2问题
        testCOT2();
        
        // 测试4: 历史研究问题
        testHistoricalResearch();
        
        // 测试5: 性能对比
        testPerformanceComparison();
        
        System.out.println("\n=== Mo's Algorithm 综合测试完成 ===");
    }
    
    /**
     * 测试基础Mo's Algorithm实现
     */
    private static void testBasicMoAlgorithm() {
        System.out.println("测试1: 基础Mo's Algorithm");
        
        // 测试数据
        int[] arr = {1, 2, 3, 1, 2, 3, 1, 2, 3};
        int[][] queries = {
            {0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 6}, {5, 7}, {6, 8}
        };
        
        // 使用基础Mo's Algorithm
        Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
        int[] result = mo.processQueries(arr, queries);
        
        // 验证结果
        boolean passed = true;
        for (int i = 0; i < queries.length; i++) {
            int l = queries[i][0];
            int r = queries[i][1];
            int expected = countDistinctNaive(arr, l, r);
            if (result[i] != expected) {
                System.out.printf("错误: 查询[%d,%d] 期望:%d 实际:%d\n", 
                    l, r, expected, result[i]);
                passed = false;
            }
        }
        
        if (passed) {
            System.out.println("✓ 基础Mo's Algorithm测试通过");
        } else {
            System.out.println("✗ 基础Mo's Algorithm测试失败");
        }
    }
    
    /**
     * 测试DQUERY问题
     */
    private static void testDQUERY() {
        System.out.println("\n测试2: DQUERY问题");
        
        // 测试数据
        int[] arr = {1, 1, 2, 1, 3, 4, 2, 3, 1};
        int[][] queries = {
            {0, 4}, {1, 5}, {2, 6}, {3, 7}, {4, 8}
        };
        
        DQUERY_Solution dquery = new DQUERY_Solution();
        int[] result = dquery.processQueries(arr, queries);
        
        boolean passed = true;
        for (int i = 0; i < queries.length; i++) {
            int l = queries[i][0];
            int r = queries[i][1];
            int expected = countDistinctNaive(arr, l, r);
            if (result[i] != expected) {
                System.out.printf("错误: DQUERY查询[%d,%d] 期望:%d 实际:%d\n", 
                    l, r, expected, result[i]);
                passed = false;
            }
        }
        
        if (passed) {
            System.out.println("✓ DQUERY测试通过");
        } else {
            System.out.println("✗ DQUERY测试失败");
        }
    }
    
    /**
     * 测试COT2问题
     */
    private static void testCOT2() {
        System.out.println("\n测试3: COT2问题");
        
        // 创建测试树结构
        int n = 6;
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            tree.add(new ArrayList<>());
        }
        
        // 构建树: 1-2, 1-3, 2-4, 2-5, 3-6
        tree.get(1).add(2); tree.get(2).add(1);
        tree.get(1).add(3); tree.get(3).add(1);
        tree.get(2).add(4); tree.get(4).add(2);
        tree.get(2).add(5); tree.get(5).add(2);
        tree.get(3).add(6); tree.get(6).add(3);
        
        int[] colors = {0, 1, 2, 1, 3, 2, 1}; // 节点颜色(索引从1开始)
        
        COT2_Java cot2 = new COT2_Java();
        
        // 测试查询
        int[][] queries = {
            {1, 4}, {1, 5}, {2, 6}, {4, 6}
        };
        
        boolean passed = true;
        for (int[] query : queries) {
            int u = query[0];
            int v = query[1];
            int result = cot2.countDistinctColors(tree, colors, u, v);
            int expected = countDistinctOnPath(tree, colors, u, v);
            
            if (result != expected) {
                System.out.printf("错误: COT2查询[%d,%d] 期望:%d 实际:%d\n", 
                    u, v, expected, result);
                passed = false;
            }
        }
        
        if (passed) {
            System.out.println("✓ COT2测试通过");
        } else {
            System.out.println("✗ COT2测试失败");
        }
    }
    
    /**
     * 测试历史研究问题
     */
    private static void testHistoricalResearch() {
        System.out.println("\n测试4: 历史研究问题");
        
        int[] arr = {1, 2, 3, 1, 2, 3, 4, 5, 1};
        int[][] queries = {
            {0, 2}, {1, 4}, {2, 6}, {3, 8}
        };
        
        HistoricalResearch_Java hr = new HistoricalResearch_Java();
        int[] result = hr.processQueries(arr, queries);
        
        boolean passed = true;
        for (int i = 0; i < queries.length; i++) {
            int l = queries[i][0];
            int r = queries[i][1];
            int expected = countDistinctNaive(arr, l, r);
            if (result[i] != expected) {
                System.out.printf("错误: 历史研究查询[%d,%d] 期望:%d 实际:%d\n", 
                    l, r, expected, result[i]);
                passed = false;
            }
        }
        
        if (passed) {
            System.out.println("✓ 历史研究测试通过");
        } else {
            System.out.println("✗ 历史研究测试失败");
        }
    }
    
    /**
     * 性能对比测试
     */
    private static void testPerformanceComparison() {
        System.out.println("\n测试5: 性能对比测试");
        
        // 生成大规模测试数据
        int n = 10000;
        int q = 1000;
        int[] arr = generateRandomArray(n, 100);
        int[][] queries = generateRandomQueries(n, q);
        
        System.out.printf("测试规模: n=%d, q=%d\n", n, q);
        
        // 测试基础Mo's Algorithm性能
        long startTime = System.currentTimeMillis();
        Code01_MoAlgorithm1_Fixed mo = new Code01_MoAlgorithm1_Fixed();
        int[] result1 = mo.processQueries(arr, queries);
        long moTime = System.currentTimeMillis() - startTime;
        
        // 测试DQUERY性能
        startTime = System.currentTimeMillis();
        DQUERY_Solution dquery = new DQUERY_Solution();
        int[] result2 = dquery.processQueries(arr, queries);
        long dqueryTime = System.currentTimeMillis() - startTime;
        
        // 验证结果一致性
        boolean consistent = true;
        for (int i = 0; i < q; i++) {
            if (result1[i] != result2[i]) {
                consistent = false;
                break;
            }
        }
        
        System.out.printf("基础Mo's Algorithm: %d ms\n", moTime);
        System.out.printf("DQUERY实现: %d ms\n", dqueryTime);
        System.out.printf("结果一致性: %s\n", consistent ? "✓" : "✗");
        
        if (consistent) {
            System.out.println("✓ 性能对比测试通过");
        } else {
            System.out.println("✗ 性能对比测试失败");
        }
    }
    
    // ========== 辅助方法 ==========
    
    /**
     * 朴素方法计算区间内不同元素个数
     */
    private static int countDistinctNaive(int[] arr, int l, int r) {
        Set<Integer> set = new HashSet<>();
        for (int i = l; i <= r; i++) {
            set.add(arr[i]);
        }
        return set.size();
    }
    
    /**
     * 计算树上路径的不同颜色数量
     */
    private static int countDistinctOnPath(List<List<Integer>> tree, int[] colors, int u, int v) {
        // 简单实现：使用DFS遍历路径
        boolean[] visited = new boolean[tree.size()];
        List<Integer> path = new ArrayList<>();
        dfsFindPath(tree, u, v, visited, path);
        
        Set<Integer> colorSet = new HashSet<>();
        for (int node : path) {
            colorSet.add(colors[node]);
        }
        return colorSet.size();
    }
    
    private static boolean dfsFindPath(List<List<Integer>> tree, int current, int target, 
                                      boolean[] visited, List<Integer> path) {
        visited[current] = true;
        path.add(current);
        
        if (current == target) {
            return true;
        }
        
        for (int neighbor : tree.get(current)) {
            if (!visited[neighbor]) {
                if (dfsFindPath(tree, neighbor, target, visited, path)) {
                    return true;
                }
            }
        }
        
        path.remove(path.size() - 1);
        return false;
    }
    
    /**
     * 生成随机数组
     */
    private static int[] generateRandomArray(int n, int maxValue) {
        Random rand = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(maxValue) + 1;
        }
        return arr;
    }
    
    /**
     * 生成随机查询
     */
    private static int[][] generateRandomQueries(int n, int q) {
        Random rand = new Random();
        int[][] queries = new int[q][2];
        for (int i = 0; i < q; i++) {
            int l = rand.nextInt(n);
            int r = l + rand.nextInt(Math.min(100, n - l));
            queries[i][0] = l;
            queries[i][1] = r;
        }
        return queries;
    }
}
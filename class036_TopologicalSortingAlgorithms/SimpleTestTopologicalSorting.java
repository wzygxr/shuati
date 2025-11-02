package class059;

import java.util.*;

/**
 * 简化版拓扑排序测试类
 * 避免外部依赖，直接实现核心算法进行测试
 */

public class SimpleTestTopologicalSorting {

    public static void main(String[] args) {
        System.out.println("=== 简化版拓扑排序测试 ===");
        
        // 测试基本拓扑排序
        testBasicTopologicalSort();
        
        // 测试课程表问题
        testCourseSchedule();
        
        // 测试外星字典问题
        testAlienDictionary();
        
        // 测试性能
        performanceTest();
        
        // 测试边界情况
        boundaryTest();
        
        System.out.println("=== 所有测试完成 ===");
    }
    
    /**
     * 测试基本拓扑排序功能
     */
    public static void testBasicTopologicalSort() {
        System.out.println("\n--- 测试基本拓扑排序 ---");
        
        // 测试1：简单DAG
        int n1 = 4;
        int[][] edges1 = {{1, 0}, {2, 1}, {3, 2}};
        List<Integer> result1 = basicTopologicalSort(n1, edges1);
        System.out.println("测试1 - 简单DAG: " + result1);
        assert result1.size() == n1 : "简单DAG测试失败";
        
        // 测试2：包含环的图
        int n2 = 3;
        int[][] edges2 = {{1, 0}, {2, 1}, {0, 2}}; // 形成环
        List<Integer> result2 = basicTopologicalSort(n2, edges2);
        System.out.println("测试2 - 包含环: " + result2);
        assert result2.size() < n2 : "环检测测试失败";
        
        // 测试3：多个入度为0的节点
        int n3 = 5;
        int[][] edges3 = {{1, 0}, {2, 0}, {3, 1}, {4, 2}};
        List<Integer> result3 = basicTopologicalSort(n3, edges3);
        System.out.println("测试3 - 多个起点: " + result3);
        assert result3.size() == n3 : "多个起点测试失败";
        
        System.out.println("基本拓扑排序测试通过 ✓");
    }
    
    /**
     * 基本拓扑排序实现
     */
    private static List<Integer> basicTopologicalSort(int n, int[][] edges) {
        // 构建图
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[n];
        
        // 添加边
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            if (from < n && to < n) {
                graph.get(from).add(to);
                inDegree[to]++;
            }
        }
        
        // Kahn算法
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);
            
            for (int next : graph.get(current)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 测试课程表问题
     */
    public static void testCourseSchedule() {
        System.out.println("\n--- 测试课程表问题 ---");
        
        // 测试1：无环情况
        boolean result1 = canFinish(4, new int[][]{{1, 0}, {2, 1}, {3, 2}});
        int[] order1 = findOrder(4, new int[][]{{1, 0}, {2, 1}, {3, 2}});
        System.out.println("测试1 - 无环课程表: " + result1 + ", 顺序: " + Arrays.toString(order1));
        assert result1 == true : "无环课程表测试失败";
        assert order1.length == 4 : "课程顺序长度错误";
        
        // 测试2：有环情况
        boolean result2 = canFinish(3, new int[][]{{1, 0}, {2, 1}, {0, 2}});
        int[] order2 = findOrder(3, new int[][]{{1, 0}, {2, 1}, {0, 2}});
        System.out.println("测试2 - 有环课程表: " + result2 + ", 顺序: " + Arrays.toString(order2));
        assert result2 == false : "有环检测测试失败";
        assert order2.length == 0 : "有环时应返回空数组";
        
        // 测试3：空课程表
        boolean result3 = canFinish(0, new int[][]{});
        int[] order3 = findOrder(0, new int[][]{});
        System.out.println("测试3 - 空课程表: " + result3 + ", 顺序: " + Arrays.toString(order3));
        assert result3 == true : "空课程表测试失败";
        
        System.out.println("课程表问题测试通过 ✓");
    }
    
    /**
     * LeetCode 207 实现 - 课程表环检测
     */
    private static boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            graph.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int count = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            for (int next : graph.get(course)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return count == numCourses;
    }
    
    /**
     * LeetCode 210 实现 - 课程表顺序生成
     */
    private static int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            graph.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int[] result = new int[numCourses];
        int index = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            result[index++] = course;
            for (int next : graph.get(course)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return index == numCourses ? result : new int[0];
    }
    
    /**
     * 测试外星字典问题
     */
    public static void testAlienDictionary() {
        System.out.println("\n--- 测试外星字典问题 ---");
        
        // 测试1：正常情况
        String[] words1 = {"wrt", "wrf", "er", "ett", "rftt"};
        String result1 = alienOrder(words1);
        System.out.println("测试1 - 正常字典: " + result1);
        assert result1.length() > 0 : "正常字典测试失败";
        
        // 测试2：有环情况
        String[] words2 = {"z", "x", "z"};
        String result2 = alienOrder(words2);
        System.out.println("测试2 - 有环字典: " + result2);
        assert result2.equals("") : "有环检测测试失败";
        
        // 测试3：前缀关系无效
        String[] words3 = {"abc", "ab"};
        String result3 = alienOrder(words3);
        System.out.println("测试3 - 前缀无效: " + result3);
        assert result3.equals("") : "前缀关系测试失败";
        
        System.out.println("外星字典问题测试通过 ✓");
    }
    
    /**
     * LeetCode 269 实现 - 外星字典字符顺序推断
     */
    private static String alienOrder(String[] words) {
        if (words == null || words.length == 0) return "";
        
        // 构建字符图
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();
        
        // 初始化所有字符
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }
        }
        
        // 构建边关系
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            
            // 检查前缀关系
            if (word1.length() > word2.length() && word1.startsWith(word2)) {
                return "";
            }
            
            int minLen = Math.min(word1.length(), word2.length());
            for (int j = 0; j < minLen; j++) {
                char c1 = word1.charAt(j);
                char c2 = word2.charAt(j);
                if (c1 != c2) {
                    if (!graph.get(c1).contains(c2)) {
                        graph.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    break;
                }
            }
        }
        
        // 拓扑排序
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.offer(c);
            }
        }
        
        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            char current = queue.poll();
            result.append(current);
            
            for (char next : graph.get(current)) {
                inDegree.put(next, inDegree.get(next) - 1);
                if (inDegree.get(next) == 0) {
                    queue.offer(next);
                }
            }
        }
        
        // 检查是否有环
        return result.length() == graph.size() ? result.toString() : "";
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n--- 性能测试 ---");
        
        int[] sizes = {100, 500, 1000};
        
        for (int size : sizes) {
            long startTime = System.currentTimeMillis();
            
            // 生成测试数据
            int n = size;
            int[][] edges = generateTestData(n);
            
            // 执行拓扑排序
            List<Integer> result = basicTopologicalSort(n, edges);
            
            long endTime = System.currentTimeMillis();
            System.out.println("规模 " + n + " 的图处理时间: " + (endTime - startTime) + "ms");
        }
    }
    
    /**
     * 生成测试数据
     */
    private static int[][] generateTestData(int n) {
        Random random = new Random();
        List<int[]> edges = new ArrayList<>();
        
        // 生成近似DAG的边（避免环）
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < Math.min(i + 10, n); j++) {
                if (random.nextDouble() < 0.3) {
                    edges.add(new int[]{i, j});
                }
            }
        }
        
        return edges.toArray(new int[0][]);
    }
    
    /**
     * 边界情况测试
     */
    public static void boundaryTest() {
        System.out.println("\n--- 边界情况测试 ---");
        
        // 测试空图
        List<Integer> emptyResult = basicTopologicalSort(0, new int[0][]);
        System.out.println("空图测试: " + emptyResult);
        assert emptyResult.size() == 0 : "空图测试失败";
        
        // 测试单节点图
        List<Integer> singleResult = basicTopologicalSort(1, new int[0][]);
        System.out.println("单节点测试: " + singleResult);
        assert singleResult.size() == 1 : "单节点测试失败";
        
        // 测试完全图（注意避免环）
        int n = 5;
        int[][] completeEdges = new int[n*(n-1)/2][2];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                completeEdges[index++] = new int[]{i, j};
            }
        }
        List<Integer> completeResult = basicTopologicalSort(n, completeEdges);
        System.out.println("完全图测试: " + completeResult);
        assert completeResult.size() == n : "完全图测试失败";
        
        System.out.println("边界情况测试通过 ✓");
    }
    
    /**
     * 复杂度分析演示
     */
    public static void complexityAnalysis() {
        System.out.println("\n--- 复杂度分析 ---");
        
        System.out.println("基本拓扑排序算法复杂度:");
        System.out.println("- 时间复杂度: O(V + E)");
        System.out.println("- 空间复杂度: O(V + E)");
        System.out.println("- 其中 V 是节点数，E 是边数");
        
        System.out.println("\n优化技巧:");
        System.out.println("1. 使用邻接表存储图: O(V + E) 空间");
        System.out.println("2. Kahn算法: 每个节点和边只访问一次");
        System.out.println("3. 队列操作: O(V) 的额外空间");
        
        System.out.println("\n实际性能考虑:");
        System.out.println("- 小规模图 (<1000节点): 毫秒级");
        System.out.println("- 中等规模图 (1000-10000节点): 秒级");
        System.out.println("- 大规模图 (>10000节点): 需要优化");
    }
}

/**
 * 测试运行器
 */
class SimpleTestRunner {
    public static void runAllTests() {
        try {
            SimpleTestTopologicalSorting.testBasicTopologicalSort();
            SimpleTestTopologicalSorting.testCourseSchedule();
            SimpleTestTopologicalSorting.testAlienDictionary();
            SimpleTestTopologicalSorting.performanceTest();
            SimpleTestTopologicalSorting.boundaryTest();
            SimpleTestTopologicalSorting.complexityAnalysis();
            
            System.out.println("\n🎉 所有测试通过！代码质量优秀。");
        } catch (AssertionError e) {
            System.err.println("❌ 测试失败: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ 测试异常: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        runAllTests();
    }
}
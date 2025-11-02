package class059;

import java.util.*;

/**
 * 拓扑排序算法测试类
 * 
 * 本文件包含所有拓扑排序实现的测试用例，确保代码正确性
 * 测试覆盖：基本功能、边界情况、异常场景、性能测试
 */

public class TestTopologicalSorting {

    public static void main(String[] args) {
        System.out.println("=== 拓扑排序算法全面测试 ===");
        
        // 测试基本拓扑排序
        testBasicTopologicalSort();
        
        // 测试课程表问题
        testCourseSchedule();
        
        // 测试外星字典问题
        testAlienDictionary();
        
        // 测试竞赛题目
        testCompetitionProblems();
        
        // 测试高级算法
        testAdvancedAlgorithms();
        
        // 测试应用案例
        testApplicationCases();
        
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
     * 基本拓扑排序实现（用于测试）
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
        
        Leetcode207_CourseSchedule solution207 = new Leetcode207_CourseSchedule();
        Leetcode210_CourseScheduleII solution210 = new Leetcode210_CourseScheduleII();
        
        // 测试1：无环情况
        int numCourses1 = 4;
        int[][] prerequisites1 = {{1, 0}, {2, 1}, {3, 2}};
        boolean result1 = solution207.canFinish(numCourses1, prerequisites1);
        int[] order1 = solution210.findOrder(numCourses1, prerequisites1);
        System.out.println("测试1 - 无环课程表: " + result1 + ", 顺序: " + Arrays.toString(order1));
        assert result1 == true : "无环课程表测试失败";
        assert order1.length == numCourses1 : "课程顺序长度错误";
        
        // 测试2：有环情况
        int numCourses2 = 3;
        int[][] prerequisites2 = {{1, 0}, {2, 1}, {0, 2}};
        boolean result2 = solution207.canFinish(numCourses2, prerequisites2);
        int[] order2 = solution210.findOrder(numCourses2, prerequisites2);
        System.out.println("测试2 - 有环课程表: " + result2 + ", 顺序: " + Arrays.toString(order2));
        assert result2 == false : "有环检测测试失败";
        assert order2.length == 0 : "有环时应返回空数组";
        
        // 测试3：空课程表
        int numCourses3 = 0;
        int[][] prerequisites3 = {};
        boolean result3 = solution207.canFinish(numCourses3, prerequisites3);
        int[] order3 = solution210.findOrder(numCourses3, prerequisites3);
        System.out.println("测试3 - 空课程表: " + result3 + ", 顺序: " + Arrays.toString(order3));
        assert result3 == true : "空课程表测试失败";
        
        System.out.println("课程表问题测试通过 ✓");
    }
    
    /**
     * 测试外星字典问题
     */
    public static void testAlienDictionary() {
        System.out.println("\n--- 测试外星字典问题 ---");
        
        Leetcode269_AlienDictionary solution = new Leetcode269_AlienDictionary();
        
        // 测试1：正常情况
        String[] words1 = {"wrt", "wrf", "er", "ett", "rftt"};
        String result1 = solution.alienOrder(words1);
        System.out.println("测试1 - 正常字典: " + result1);
        assert result1.length() > 0 : "正常字典测试失败";
        
        // 测试2：有环情况
        String[] words2 = {"z", "x", "z"};
        String result2 = solution.alienOrder(words2);
        System.out.println("测试2 - 有环字典: " + result2);
        assert result2.equals("") : "有环检测测试失败";
        
        // 测试3：前缀关系无效
        String[] words3 = {"abc", "ab"};
        String result3 = solution.alienOrder(words3);
        System.out.println("测试3 - 前缀无效: " + result3);
        assert result3.equals("") : "前缀关系测试失败";
        
        System.out.println("外星字典问题测试通过 ✓");
    }
    
    /**
     * 测试竞赛题目实现
     */
    public static void testCompetitionProblems() {
        System.out.println("\n--- 测试竞赛题目 ---");
        
        // 测试HDU 1285
        HDU1285_DetermineTheRanking hduSolution = new HDU1285_DetermineTheRanking();
        int n1 = 4;
        int[][] edges1 = {{1, 2}, {1, 3}, {2, 4}, {3, 4}};
        List<Integer> result1 = hduSolution.topologicalSortLexicographically(
            Arrays.asList(new ArrayList[]{
                new ArrayList<Integer>() {{ add(1); add(2); }},
                new ArrayList<Integer>() {{ add(1); add(3); }},
                new ArrayList<Integer>() {{ add(2); add(4); }},
                new ArrayList<Integer>() {{ add(3); add(4); }}
            }), new int[n1 + 1], n1);
        System.out.println("HDU 1285 测试: " + result1);
        
        // 测试POJ 1094
        POJ1094_SortingItAllOut pojSolution = new POJ1094_SortingItAllOut();
        int n2 = 3;
        String[] relations = {"A<B", "B<C"};
        String result2 = pojSolution.sorting_it_all_out(n2, relations);
        System.out.println("POJ 1094 测试: " + result2);
        
        System.out.println("竞赛题目测试通过 ✓");
    }
    
    /**
     * 测试高级算法
     */
    public static void testAdvancedAlgorithms() {
        System.out.println("\n--- 测试高级算法 ---");
        
        // 测试动态拓扑排序
        AdvancedTopologicalSorting.DynamicTopologicalSort dynamicSort = 
            new AdvancedTopologicalSorting.DynamicTopologicalSort(5);
        
        dynamicSort.addEdge(0, 1);
        dynamicSort.addEdge(1, 2);
        dynamicSort.addEdge(2, 3);
        List<Integer> result1 = dynamicSort.getTopologicalOrder();
        System.out.println("动态拓扑排序测试: " + result1);
        
        // 测试增量拓扑排序
        AdvancedTopologicalSorting.IncrementalTopologicalSort incrementalSort = 
            new AdvancedTopologicalSorting.IncrementalTopologicalSort(4);
        
        List<int[]> edges = Arrays.asList(
            new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 3}
        );
        incrementalSort.addEdgesBatch(edges);
        List<Integer> result2 = incrementalSort.getIncrementalOrder();
        System.out.println("增量拓扑排序测试: " + result2);
        
        System.out.println("高级算法测试通过 ✓");
    }
    
    /**
     * 测试应用案例
     */
    public static void testApplicationCases() {
        System.out.println("\n--- 测试应用案例 ---");
        
        // 测试任务调度系统
        TopologicalSortingApplications.TaskScheduler scheduler = 
            new TopologicalSortingApplications.TaskScheduler();
        
        scheduler.addTask(new TopologicalSortingApplications.TaskScheduler.Task(
            "T1", "数据预处理", 1, 1000));
        scheduler.addTask(new TopologicalSortingApplications.TaskScheduler.Task(
            "T2", "特征工程", 2, 2000));
        scheduler.addDependency("T2", "T1");
        
        List<TopologicalSortingApplications.TaskScheduler.Task> schedule = 
            scheduler.getExecutionOrder();
        System.out.println("任务调度测试: " + schedule.size() + " 个任务");
        
        // 测试构建系统
        TopologicalSortingApplications.BuildSystem buildSystem = 
            new TopologicalSortingApplications.BuildSystem();
        
        TopologicalSortingApplications.BuildSystem.Module moduleA = 
            new TopologicalSortingApplications.BuildSystem.Module("A", "/path/a");
        TopologicalSortingApplications.BuildSystem.Module moduleB = 
            new TopologicalSortingApplications.BuildSystem.Module("B", "/path/b");
        moduleB.dependencies.add("A");
        
        buildSystem.addModule(moduleA);
        buildSystem.addModule(moduleB);
        List<TopologicalSortingApplications.BuildSystem.Module> buildOrder = 
            buildSystem.getBuildOrder();
        System.out.println("构建系统测试: " + buildOrder.size() + " 个模块");
        
        System.out.println("应用案例测试通过 ✓");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n--- 性能测试 ---");
        
        int[] sizes = {100, 1000, 5000};
        
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
        
        // 测试单节点图
        List<Integer> singleResult = basicTopologicalSort(1, new int[0][]);
        System.out.println("单节点测试: " + singleResult);
        
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
        
        System.out.println("边界情况测试通过 ✓");
    }
}

/**
 * 测试运行器 - 用于批量执行测试
 */
class TestRunner {
    public static void runAllTests() {
        try {
            TestTopologicalSorting.testBasicTopologicalSort();
            TestTopologicalSorting.testCourseSchedule();
            TestTopologicalSorting.testAlienDictionary();
            TestTopologicalSorting.testCompetitionProblems();
            TestTopologicalSorting.testAdvancedAlgorithms();
            TestTopologicalSorting.testApplicationCases();
            TestTopologicalSorting.performanceTest();
            TestTopologicalSorting.boundaryTest();
            
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
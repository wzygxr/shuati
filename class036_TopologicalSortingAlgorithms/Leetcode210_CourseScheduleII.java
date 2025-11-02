import java.util.*;
import java.util.stream.Collectors;

/**
 * LeetCode 210. Course Schedule II
 * 
 * 题目链接: https://leetcode.com/problems/course-schedule-ii/
 * 
 * 题目描述：
 * 现在你总共有 numCourses 门课需要选，记为 0 到 numCourses - 1。
 * 给你一个数组 prerequisites ，它的每一个元素 prerequisites[i] = [ai, bi] 表示在选修课程 ai 前必须先选修 bi。
 * 请你返回你为了学完所有课程所安排的学习顺序。可能会有多个正确的顺序，你只要返回任意一种就可以。
 * 如果不可能完成所有课程，返回一个空数组。
 * 
 * 解题思路：
 * 这是Course Schedule I的进阶版本。我们不仅需要判断是否可以完成所有课程，
 * 还需要返回一个可行的学习顺序。这可以通过拓扑排序来解决。
 * 我们使用Kahn算法：
 * 1. 计算每个节点的入度
 * 2. 将所有入度为0的节点加入队列
 * 3. 不断从队列中取出节点，将其加入结果数组，并将其所有邻居节点的入度减1
 * 4. 如果邻居节点的入度变为0，则将其加入队列
 * 5. 重复步骤3-4直到队列为空
 * 6. 最后检查结果数组的长度是否等于总课程数
 * 
 * 时间复杂度：O(V + E)，其中V是课程数，E是先修关系数
 * 空间复杂度：O(V + E)，用于存储图和入度数组
 * 
 * 示例：
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：[0,1]
 * 解释：总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，一个正确的课程顺序是 [0,1]。
 * 
 * 输入：numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * 输出：[0,2,1,3]
 * 解释：总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。
 *       并且课程 1 和课程 2 都应该排在课程 0 之后。
 *       因此，一个正确的课程顺序是 [0,1,2,3] 或 [0,2,1,3]。
 * 
 * 输入：numCourses = 1, prerequisites = []
 * 输出：[0]
 */
public class Leetcode210_CourseScheduleII {

    public static void main(String[] args) {
        Leetcode210_CourseScheduleII solution = new Leetcode210_CourseScheduleII();
        
        // 测试用例1
        int numCourses1 = 2;
        int[][] prerequisites1 = {{1, 0}};
        int[] result1 = solution.findOrder(numCourses1, prerequisites1);
        System.out.println("Test Case 1: " + Arrays.toString(result1)); // 应该输出 [0, 1]
        
        // 测试用例2
        int numCourses2 = 4;
        int[][] prerequisites2 = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        int[] result2 = solution.findOrder(numCourses2, prerequisites2);
        System.out.println("Test Case 2: " + Arrays.toString(result2)); // 应该输出 [0, 1, 2, 3] 或 [0, 2, 1, 3]
        
        // 测试用例3
        int numCourses3 = 1;
        int[][] prerequisites3 = {};
        int[] result3 = solution.findOrder(numCourses3, prerequisites3);
        System.out.println("Test Case 3: " + Arrays.toString(result3)); // 应该输出 [0]
        
        // 测试用例4 - 存在环的情况
        int numCourses4 = 2;
        int[][] prerequisites4 = {{1, 0}, {0, 1}};
        int[] result4 = solution.findOrder(numCourses4, prerequisites4);
        System.out.println("Test Case 4: " + Arrays.toString(result4)); // 应该输出 []
    }
    
    /**
     * 返回完成所有课程的学习顺序
     * @param numCourses 课程总数
     * @param prerequisites 先修课程关系数组
     * @return 完成所有课程的学习顺序，如果无法完成则返回空数组
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // 构建邻接表表示的图
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 初始化入度数组
        int[] inDegree = new int[numCourses];
        
        // 构建图和入度数组
        for (int[] prerequisite : prerequisites) {
            int course = prerequisite[0];      // 当前课程
            int preCourse = prerequisite[1];   // 先修课程
            
            // 添加边：先修课程 -> 当前课程
            graph.get(preCourse).add(course);
            
            // 当前课程入度加1
            inDegree[course]++;
        }
        
        // 使用Kahn算法进行拓扑排序
        return topologicalSort(graph, inDegree, numCourses);
    }
    
    /**
     * 使用Kahn算法进行拓扑排序，返回课程顺序
     * @param graph 邻接表表示的图
     * @param inDegree 入度数组
     * @param numCourses 课程总数
     * @return 完成所有课程的学习顺序，如果无法完成则返回空数组
     */
    private int[] topologicalSort(List<List<Integer>> graph, int[] inDegree, int numCourses) {
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> result = new ArrayList<>();
        
        // 将所有入度为0的节点加入队列
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // Kahn算法进行拓扑排序
        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            result.add(currentCourse);
            
            // 遍历当前课程的所有后续课程
            for (int nextCourse : graph.get(currentCourse)) {
                // 将后续课程的入度减1
                inDegree[nextCourse]--;
                
                // 如果后续课程的入度变为0，则加入队列
                if (inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        
        // 如果处理的课程数等于总课程数，说明不存在环，可以完成所有课程
        if (result.size() == numCourses) {
            // 将List转换为数组
            int[] res = new int[numCourses];
            for (int i = 0; i < numCourses; i++) {
                res[i] = result.get(i);
            }
            return res;
        } else {
            // 存在环，返回空数组
            return new int[0];
        }
    }
}
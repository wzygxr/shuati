package class060;

// 课程表 - 拓扑排序判环
// 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1
// 在选修某些课程之前需要一些先修课程
// 先修课程按数组 prerequisites 给出，其中 prerequisites[i] = [ai, bi]
// 表示如果要学习课程 ai 则必须先学习课程 bi
// 请你判断是否可能完成所有课程的学习
// 测试链接 : https://leetcode.cn/problems/course-schedule/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.*;

/**
 * 题目解析：
 * 这是拓扑排序判环的经典题目，判断课程安排图中是否存在环。
 * 如果存在环，则无法完成所有课程的学习。
 * 
 * 算法思路：
 * 1. 使用邻接表存储课程依赖关系
 * 2. 计算每个课程的入度
 * 3. 使用队列进行BFS拓扑排序
 * 4. 如果结果序列长度等于课程数，说明无环；否则有环
 * 
 * 时间复杂度：O(V + E)，其中V是课程数，E是依赖关系数
 * 空间复杂度：O(V + E)
 * 
 * 相关题目扩展：
 * 1. LeetCode 207. 课程表 - https://leetcode.cn/problems/course-schedule/
 * 2. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
 * 3. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
 * 4. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
 * 
 * 工程化考虑：
 * 1. 输入验证：验证课程数和依赖关系的有效性
 * 2. 边界处理：空课程、单课程、无依赖等情况
 * 3. 性能优化：使用邻接表存储图结构
 * 4. 异常处理：处理非法输入数据
 * 5. 模块化设计：分离建图和拓扑排序逻辑
 */
public class Code13_CourseScheduleCheckCycle {

    /**
     * 判断是否能够完成所有课程
     * @param numCourses 课程数量
     * @param prerequisites 先修课程关系
     * @return 如果能够完成返回true，否则返回false
     */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // 特殊情况处理
        if (numCourses <= 0) return true;
        if (prerequisites == null || prerequisites.length == 0) return true;
        
        // 构建邻接表
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 入度数组
        int[] indegree = new int[numCourses];
        
        // 构建图并计算入度
        for (int[] pre : prerequisites) {
            int course = pre[0];
            int prerequisite = pre[1];
            graph.get(prerequisite).add(course);
            indegree[course]++;
        }
        
        // 使用队列进行拓扑排序
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int count = 0; // 已完成的课程数
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            
            // 更新依赖该课程的课程的入度
            for (int nextCourse : graph.get(course)) {
                if (--indegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        
        return count == numCourses;
    }

    public static void main(String[] args) {
        // 测试用例1：无环，可以完成
        int numCourses1 = 2;
        int[][] prerequisites1 = {{1, 0}};
        System.out.println("测试用例1: " + canFinish(numCourses1, prerequisites1)); // 输出: true
        
        // 测试用例2：有环，无法完成
        int numCourses2 = 2;
        int[][] prerequisites2 = {{1, 0}, {0, 1}};
        System.out.println("测试用例2: " + canFinish(numCourses2, prerequisites2)); // 输出: false
        
        // 测试用例3：空课程
        int numCourses3 = 0;
        int[][] prerequisites3 = {};
        System.out.println("测试用例3: " + canFinish(numCourses3, prerequisites3)); // 输出: true
        
        // 测试用例4：单课程无依赖
        int numCourses4 = 1;
        int[][] prerequisites4 = {};
        System.out.println("测试用例4: " + canFinish(numCourses4, prerequisites4)); // 输出: true
    }
}
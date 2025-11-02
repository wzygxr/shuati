package class060;

import java.util.ArrayList;

/**
 * 课程表
 * 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1。
 * 在选修某些课程之前需要一些先修课程。先修课程按数组 prerequisites 给出，
 * 其中 prerequisites[i] = [ai, bi] ，表示如果要学习课程 ai 则必须先学习课程 bi。
 * 请你判断是否可能完成所有课程的学习？如果可以，返回 true；否则，返回 false。
 * 测试链接 : https://leetcode.cn/problems/course-schedule/
 * 
 * 算法思路：
 * 这是一个典型的拓扑排序判环问题。我们需要判断给定的课程依赖关系是否构成一个有向无环图(DAG)。
 * 如果图中存在环，说明存在循环依赖，无法完成所有课程；否则可以完成。
 * 
 * 解法一：Kahn算法（BFS）
 * 1. 构建邻接表表示的图和入度数组
 * 2. 将所有入度为0的节点加入队列
 * 3. 不断从队列中取出节点，将其所有邻居的入度减1
 * 4. 如果邻居的入度减为0，则加入队列
 * 5. 统计处理的节点数，如果等于总节点数，说明无环，可以完成；否则不能完成
 * 
 * 解法二：DFS + 三色标记法
 * 1. 使用三色标记法检测环：
 *    - 白色(0)：未访问
 *    - 灰色(1)：正在访问（在当前DFS路径中）
 *    - 黑色(2)：已访问完成
 * 2. 对每个未访问的节点进行DFS
 * 3. 如果在DFS过程中遇到灰色节点，说明存在环
 * 
 * 时间复杂度：O(N + M)，其中N是课程数，M是先修关系数
 * 空间复杂度：O(N + M)
 * 
 * 相关题目扩展：
 * 1. LeetCode 207. 课程表 - https://leetcode.cn/problems/course-schedule/
 * 2. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
 * 3. LeetCode 1494. 并行课程 II - https://leetcode.cn/problems/parallel-courses-ii/
 * 4. LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/
 * 5. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
 * 6. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
 * 7. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
 * 8. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 9. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
 * 10. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e
 * 
 * 工程化考虑：
 * 1. 边界处理：处理课程数为0、先修关系为空等特殊情况
 * 2. 输入验证：验证课程编号的有效性
 * 3. 内存优化：合理使用ArrayList和数组
 * 4. 异常处理：对非法输入进行检查
 * 5. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 拓扑排序的核心是入度的概念
 * 2. Kahn算法通过不断移除入度为0的节点来实现拓扑排序
 * 3. 判环的关键是检查是否所有节点都能被处理
 * 4. DFS方法通过三色标记法检测环的存在
 * 5. 两种方法的时间复杂度相同，但适用场景略有不同
 */
public class Code05_CourseSchedule {

    /**
     * 方法一：Kahn算法（BFS）
     * 使用拓扑排序判断图中是否有环
     * 
     * @param numCourses 课程总数
     * @param prerequisites 先修关系数组
     * @return 是否可以完成所有课程
     */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 计算每个节点的入度
        int[] indegree = new int[numCourses];
        for (int[] edge : prerequisites) {
            // edge[1] -> edge[0]，即学习edge[0]前必须学习edge[1]
            graph.get(edge[1]).add(edge[0]);
            indegree[edge[0]]++;
        }
        
        // 拓扑排序使用的队列
        int[] queue = new int[numCourses];
        int l = 0;
        int r = 0;
        
        // 将所有入度为0的节点加入队列
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue[r++] = i;
            }
        }
        
        // 统计处理的节点数
        int processed = 0;
        
        // 拓扑排序过程
        while (l < r) {
            // 取出队首元素
            int cur = queue[l++];
            processed++;
            
            // 遍历当前节点的所有邻居
            for (int next : graph.get(cur)) {
                // 将邻居节点的入度减1
                if (--indegree[next] == 0) {
                    // 如果邻居节点的入度变为0，则加入队列
                    queue[r++] = next;
                }
            }
        }
        
        // 如果处理的节点数等于总节点数，说明无环，可以完成所有课程
        return processed == numCourses;
    }
    
    /**
     * 方法二：DFS + 三色标记法
     * 使用深度优先搜索和三色标记法判断图中是否有环
     * 
     * @param numCourses 课程总数
     * @param prerequisites 先修关系数组
     * @return 是否可以完成所有课程
     */
    public static boolean canFinishDFS(int numCourses, int[][] prerequisites) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 建图
        for (int[] edge : prerequisites) {
            // edge[1] -> edge[0]，即学习edge[0]前必须学习edge[1]
            graph.get(edge[1]).add(edge[0]);
        }
        
        // 三色标记数组：0-白色(未访问)，1-灰色(正在访问)，2-黑色(已访问完成)
        int[] color = new int[numCourses];
        
        // 对每个未访问的节点进行DFS
        for (int i = 0; i < numCourses; i++) {
            if (color[i] == 0) {
                if (hasCycle(graph, color, i)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * DFS检测环
     * 
     * @param graph 图的邻接表表示
     * @param color 三色标记数组
     * @param cur 当前节点
     * @return 是否存在环
     */
    private static boolean hasCycle(ArrayList<ArrayList<Integer>> graph, int[] color, int cur) {
        // 将当前节点标记为灰色（正在访问）
        color[cur] = 1;
        
        // 遍历当前节点的所有邻居
        for (int next : graph.get(cur)) {
            // 如果邻居节点是灰色，说明存在环
            if (color[next] == 1) {
                return true;
            }
            // 如果邻居节点是白色，递归访问
            if (color[next] == 0) {
                if (hasCycle(graph, color, next)) {
                    return true;
                }
            }
        }
        
        // 将当前节点标记为黑色（已访问完成）
        color[cur] = 2;
        return false;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: 可以完成
        int numCourses1 = 2;
        int[][] prerequisites1 = {{1, 0}};
        System.out.println("测试用例1 - Kahn算法: " + canFinish(numCourses1, prerequisites1)); // true
        System.out.println("测试用例1 - DFS方法: " + canFinishDFS(numCourses1, prerequisites1)); // true
        
        // 测试用例2: 无法完成（存在循环依赖）
        int numCourses2 = 2;
        int[][] prerequisites2 = {{1, 0}, {0, 1}};
        System.out.println("测试用例2 - Kahn算法: " + canFinish(numCourses2, prerequisites2)); // false
        System.out.println("测试用例2 - DFS方法: " + canFinishDFS(numCourses2, prerequisites2)); // false
        
        // 测试用例3: 复杂情况
        int numCourses3 = 4;
        int[][] prerequisites3 = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        System.out.println("测试用例3 - Kahn算法: " + canFinish(numCourses3, prerequisites3)); // true
        System.out.println("测试用例3 - DFS方法: " + canFinishDFS(numCourses3, prerequisites3)); // true
    }
}
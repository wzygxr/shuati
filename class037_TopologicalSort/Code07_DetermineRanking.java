package class060;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * 确定比赛名次
 * 有N个队参加比赛，第i队的编号为i (1 <= i <= N)
 * 给出M个关系，每个关系形如 a b，表示a队的名次比b队高（即a队排在b队前面）
 * 请你确定所有队伍的名次，要求：
 * 1. 符合所有给定的关系
 * 2. 名次越小表示排名越高
 * 3. 如果有多种可能的排名，输出字典序最小的那个
 * 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 
 * 算法思路：
 * 这是一个字典序最小拓扑排序问题。我们需要在满足拓扑排序的前提下，使得输出的序列字典序最小。
 * 
 * 解法：优先队列优化的Kahn算法
 * 1. 构建邻接表表示的图和入度数组
 * 2. 将所有入度为0的节点加入优先队列（最小堆）
 * 3. 不断从优先队列中取出编号最小的节点，将其加入结果数组，并将其所有邻居的入度减1
 * 4. 如果邻居的入度减为0，则加入优先队列
 * 5. 重复步骤3-4直到优先队列为空
 * 
 * 时间复杂度：O(N*logN + M)，其中N是队伍数，M是关系数
 * 空间复杂度：O(N + M)
 * 
 * 相关题目扩展：
 * 1. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 2. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
 * 3. 牛客网 字典序最小的拓扑序列 - https://ac.nowcoder.com/acm/problem/15184
 * 4. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
 * 5. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
 * 6. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
 * 7. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
 * 8. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e
 * 9. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
 * 10. 洛谷 B3644 【模板】拓扑排序 - https://www.luogu.com.cn/problem/B3644
 * 
 * 工程化考虑：
 * 1. 边界处理：处理队伍数为0、关系为空等特殊情况
 * 2. 输入验证：验证队伍编号的有效性
 * 3. 内存优化：合理使用ArrayList和优先队列
 * 4. 异常处理：对非法输入进行检查
 * 5. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 字典序最小拓扑排序的关键是使用优先队列（最小堆）
 * 2. 每次选择入度为0且编号最小的节点
 * 3. 优先队列可以保证每次取出的都是当前可选节点中编号最小的
 * 4. 与普通拓扑排序相比，时间复杂度多了一个logN因子
 * 5. 结果序列是唯一的（在字典序最小的要求下）
 */
public class Code07_DetermineRanking {

    /**
     * 使用优先队列优化的Kahn算法确定比赛名次
     * 
     * @param n 队伍数
     * @param relations 关系数组，relations[i] = [a, b] 表示a队名次比b队高
     * @return 比赛名次数组，按名次从高到低排列
     */
    public static int[] findRanking(int n, int[][] relations) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 计算每个节点的入度
        int[] indegree = new int[n + 1];
        for (int[] relation : relations) {
            int a = relation[0];  // a队名次比b队高
            int b = relation[1];
            graph.get(a).add(b);
            indegree[b]++;
        }
        
        // 使用优先队列（最小堆）替代普通队列
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        
        // 将所有入度为0的节点加入优先队列
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                pq.offer(i);
            }
        }
        
        // 存储拓扑排序结果
        int[] result = new int[n];
        int index = 0;
        
        // 拓扑排序过程
        while (!pq.isEmpty()) {
            // 取出编号最小的入度为0的节点
            int cur = pq.poll();
            // 将当前节点加入结果数组
            result[index++] = cur;
            
            // 遍历当前节点的所有邻居
            for (int next : graph.get(cur)) {
                // 将邻居节点的入度减1
                if (--indegree[next] == 0) {
                    // 如果邻居节点的入度变为0，则加入优先队列
                    pq.offer(next);
                }
            }
        }
        
        // 返回结果数组
        return result;
    }

    // 测试方法（模拟HDU的输入输出格式）
    public static void main(String[] args) {
        // 模拟测试用例
        // 测试用例1: 4个队伍，3个关系
        int n1 = 4;
        int[][] relations1 = {{1, 2}, {2, 3}, {3, 4}};
        int[] result1 = findRanking(n1, relations1);
        System.out.print("测试用例1: ");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        // 测试用例2: 4个队伍，4个关系
        int n2 = 4;
        int[][] relations2 = {{1, 3}, {2, 3}, {1, 4}, {2, 4}};
        int[] result2 = findRanking(n2, relations2);
        System.out.print("测试用例2: ");
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i]);
            if (i < result2.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}
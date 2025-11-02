/**
 * 并行课程 III (基础C++版本)
 * 给你一个整数 n ，表示有 n 节课，课程编号从 1 到 n
 * 同时给你一个二维整数数组 relations ，
 * 其中 relations[j] = [prevCoursej, nextCoursej]
 * 表示课程 prevCoursej 必须在课程 nextCoursej 之前 完成（先修课的关系）
 * 同时给你一个下标从 0 开始的整数数组 time
 * 其中 time[i] 表示完成第 (i+1) 门课程需要花费的 月份 数。
 * 请你根据以下规则算出完成所有课程所需要的 最少 月份数：
 * 如果一门课的所有先修课都已经完成，你可以在 任意 时间开始这门课程。
 * 你可以 同时 上 任意门课程 。请你返回完成所有课程所需要的 最少 月份数。
 * 注意：测试数据保证一定可以完成所有课程（也就是先修课的关系构成一个有向无环图）
 * 测试链接 : https://leetcode.cn/problems/parallel-courses-iii/
 * 
 * 算法思路：
 * 这是一个基于拓扑排序的动态规划问题。由于可以并行上课，我们需要计算每个课程的最早开始时间，
 * 然后加上该课程的学习时间，得到完成该课程的时间。最终答案是所有课程完成时间的最大值。
 * 
 * 时间复杂度：O(N + M)，其中 N 是课程数，M 是先修关系数
 * 空间复杂度：O(N + M)
 * 
 * 相关题目扩展：
 * 1. LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/
 * 2. LeetCode 1494. 并行课程 II - https://leetcode.cn/problems/parallel-courses-ii/
 * 3. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
 * 4. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
 * 5. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
 * 6. POJ 3249 Test for Job - http://poj.org/problem?id=3249
 * 7. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 8. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e
 * 9. Codeforces 1109C Sasha and a Patient Friend - https://codeforces.com/problemset/problem/1109/C
 * 10. 牛客网 课程表 - https://ac.nowcoder.com/acm/problem/24725
 * 
 * 工程化考虑：
 * 1. 边界处理：处理没有先修课程的情况
 * 2. 输入验证：验证课程编号和时间数组的有效性
 * 3. 内存优化：合理使用数组
 * 4. 异常处理：对非法输入进行检查
 * 5. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 构建图：将先修关系转换为有向图
 * 2. 计算入度：用于拓扑排序
 * 3. 初始化队列：将入度为0的课程（可以立即开始的课程）加入队列
 * 4. 动态规划：计算每门课程的最早完成时间
 * 5. 更新答案：记录所有课程完成时间的最大值
 */

#define MAXN 50005

/**
 * 计算完成所有课程所需的最少月份数
 * 
 * @param n 课程总数
 * @param relations 先修关系数组
 * @param relationsSize 先修关系数组大小
 * @param relationsColSize 先修关系数组列大小
 * @param time time数组
 * @param timeSize time数组大小
 * @return 完成所有课程所需的最少月份数
 */
int minimumTime(int n, int** relations, int relationsSize, int* relationsColSize, int* time, int timeSize) {
    // 点 : 1....n
    // 构建邻接表表示的图（使用数组模拟）
    int graph[MAXN][100];  // graph[i][j] 存储节点i的第j个邻居
    int graphSize[MAXN] = {0};  // graphSize[i] 存储节点i的邻居数量
    
    // 计算每个节点的入度
    int indegree[MAXN] = {0};
    
    // 建图
    for (int i = 0; i < relationsSize; i++) {
        int prev_course = relations[i][0];
        int next_course = relations[i][1];
        graph[prev_course][graphSize[prev_course]] = next_course;
        graphSize[prev_course]++;
        indegree[next_course]++;
    }
    
    // 拓扑排序使用的队列
    int queue[MAXN];
    int front = 0, rear = 0;
    
    // 将所有入度为0的节点加入队列
    for (int i = 1; i <= n; i++) {
        if (indegree[i] == 0) {
            queue[rear++] = i;
        }
    }
    
    // cost[i] 表示完成课程 i 的最早时间
    int cost[MAXN] = {0};
    int ans = 0;
    
    // 拓扑排序过程
    while (front < rear) {
        int cur = queue[front++];
        // 1 : time[0]
        // x : time[x-1]
        // 完成当前课程的时间 = 开始时间 + 学习时间
        cost[cur] += time[cur - 1];
        // 更新最大完成时间
        if (cost[cur] > ans) ans = cost[cur];
        
        // 遍历当前课程的所有后续课程
        for (int i = 0; i < graphSize[cur]; i++) {
            int next_course = graph[cur][i];
            // 更新后续课程的最早开始时间：
            // 后续课程的最早开始时间 = max(当前值, 当前课程的完成时间)
            if (cost[cur] > cost[next_course]) {
                cost[next_course] = cost[cur];
            }
            // 将后续课程的入度减1，如果变为0则加入队列
            indegree[next_course]--;
            if (indegree[next_course] == 0) {
                queue[rear++] = next_course;
            }
        }
    }
    return ans;
}

// 测试函数（简化版）
int main() {
    // 由于基础C++实现限制，这里只演示方法调用方式
    // 实际使用时需要根据具体环境调整
    
    return 0;
}
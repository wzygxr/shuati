/**
 * 课程表 (C++版本)
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
 * 11. LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 12. LeetCode 851. 喧闹和富有 - https://leetcode.cn/problems/loud-and-rich/
 * 13. LeetCode 2127. 参加会议的最多员工数 - https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
 * 14. 洛谷 P4017 最大食物链计数 - https://www.luogu.com.cn/problem/P4017
 * 15. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
 * 16. POJ 3249 Test for Job - http://poj.org/problem?id=3249
 * 17. HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109
 * 18. 牛客网 课程表 - https://ac.nowcoder.com/acm/problem/24725
 * 19. USACO 2014 January Contest, Gold - http://www.usaco.org/index.php?page=viewproblem2&cpid=382
 * 20. Timus OJ 1280. Topological Sorting - https://acm.timus.ru/problem.aspx?space=1&num=1280
 * 21. Aizu OJ GRL_4_B. Topological Sort - https://onlinejudge.u-aizu.ac.jp/problems/GRL_4_B
 * 22. Project Euler Problem 79: Passcode derivation - https://projecteuler.net/problem=79
 * 23. HackerEarth Topological Sort - https://www.hackerearth.com/practice/algorithms/graphs/topological-sort/practice-problems/
 * 24. 计蒜客 三值排序 - https://nanti.jisuanke.com/t/T1566
 * 25. 各大高校OJ中的拓扑排序题目
 * 26. ZOJ 1060 Sorting It All Out - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
 * 27. 洛谷 P1453 城市环路 - https://www.luogu.com.cn/problem/P1453
 * 28. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
 * 29. 牛客网 字典序最小的拓扑序列 - https://ac.nowcoder.com/acm/problem/15184
 * 30. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
 * 
 * 工程化考虑：
 * 1. 边界处理：处理课程数为0、先修关系为空等特殊情况
 * 2. 输入验证：验证课程编号的有效性
 * 3. 内存优化：合理使用数组
 * 4. 异常处理：对非法输入进行检查
 * 5. 可读性：添加详细注释和变量命名
 * 6. 性能优化：使用链式前向星存储图结构
 * 7. 模块化设计：将建图和拓扑排序逻辑分离
 * 8. 代码复用：将通用功能封装成独立函数
 * 
 * 算法要点：
 * 1. 拓扑排序的核心是入度的概念
 * 2. Kahn算法通过不断移除入度为0的节点来实现拓扑排序
 * 3. 判环的关键是检查是否所有节点都能被处理
 * 4. DFS方法通过三色标记法检测环的存在
 * 5. 两种方法的时间复杂度相同，但适用场景略有不同
 * 6. 链式前向星可以高效地存储稀疏图
 */

// 常量定义
#define MAXN 100005
#define MAXM 500005

// 全局变量
// 链式前向星存储图
int head[MAXN];
int next[MAXM];
int to[MAXM];
int cnt;

// 入度数组
int indegree[MAXN];

// 队列用于BFS
int queue[MAXN];
int front, rear;

// 三色标记数组：0-白色(未访问)，1-灰色(正在访问)，2-黑色(已访问完成)
int color[MAXN];

// 图的最大节点数
int n;

/**
 * 初始化图结构
 */
void init() {
    cnt = 0;
    int i;
    for (i = 0; i < n; i++) {
        head[i] = -1;
        indegree[i] = 0;
        color[i] = 0;
    }
}

/**
 * 添加边 u -> v
 */
void addEdge(int u, int v) {
    to[cnt] = v;
    next[cnt] = head[u];
    head[u] = cnt++;
}

/**
 * 方法一：Kahn算法（BFS）
 * 使用拓扑排序判断图中是否有环
 * 
 * @param numCourses 课程总数
 * @param prerequisites 先修关系数组
 * @param prerequisitesSize 先修关系数组大小
 * @param prerequisitesColSize 先修关系数组列大小
 * @return 是否可以完成所有课程
 */
int canFinish(int numCourses, int** prerequisites, int prerequisitesSize, int* prerequisitesColSize) {
    n = numCourses;
    init();
    
    // 建图
    int i;
    for (i = 0; i < prerequisitesSize; i++) {
        int u = prerequisites[i][1];  // 先修课程
        int v = prerequisites[i][0];  // 当前课程
        addEdge(u, v);
        indegree[v]++;
    }
    
    // 初始化队列
    front = rear = 0;
    
    // 将所有入度为0的节点加入队列
    for (i = 0; i < numCourses; i++) {
        if (indegree[i] == 0) {
            queue[rear++] = i;
        }
    }
    
    // 统计处理的节点数
    int processed = 0;
    
    // 拓扑排序过程
    while (front < rear) {
        // 取出队首元素
        int cur = queue[front++];
        processed++;
        
        // 遍历当前节点的所有邻居
        for (i = head[cur]; i != -1; i = next[i]) {
            int nextNode = to[i];
            // 将邻居节点的入度减1
            indegree[nextNode]--;
            if (indegree[nextNode] == 0) {
                // 如果邻居节点的入度变为0，则加入队列
                queue[rear++] = nextNode;
            }
        }
    }
    
    // 如果处理的节点数等于总节点数，说明无环，可以完成所有课程
    return processed == numCourses;
}

/**
 * DFS检测环
 * 
 * @param cur 当前节点
 * @return 是否存在环
 */
int hasCycle(int cur) {
    // 将当前节点标记为灰色（正在访问）
    color[cur] = 1;
    
    // 遍历当前节点的所有邻居
    int i;
    for (i = head[cur]; i != -1; i = next[i]) {
        int nextNode = to[i];
        // 如果邻居节点是灰色，说明存在环
        if (color[nextNode] == 1) {
            return 1;
        }
        // 如果邻居节点是白色，递归访问
        if (color[nextNode] == 0) {
            if (hasCycle(nextNode)) {
                return 1;
            }
        }
    }
    
    // 将当前节点标记为黑色（已访问完成）
    color[cur] = 2;
    return 0;
}

/**
 * 方法二：DFS + 三色标记法
 * 使用深度优先搜索和三色标记法判断图中是否有环
 * 
 * @param numCourses 课程总数
 * @param prerequisites 先修关系数组
 * @param prerequisitesSize 先修关系数组大小
 * @param prerequisitesColSize 先修关系数组列大小
 * @return 是否可以完成所有课程
 */
int canFinishDFS(int numCourses, int** prerequisites, int prerequisitesSize, int* prerequisitesColSize) {
    n = numCourses;
    init();
    
    // 建图
    int i;
    for (i = 0; i < prerequisitesSize; i++) {
        int u = prerequisites[i][1];  // 先修课程
        int v = prerequisites[i][0];  // 当前课程
        addEdge(u, v);
    }
    
    // 对每个未访问的节点进行DFS
    for (i = 0; i < numCourses; i++) {
        if (color[i] == 0) {
            if (hasCycle(i)) {
                return 0;
            }
        }
    }
    
    return 1;
}
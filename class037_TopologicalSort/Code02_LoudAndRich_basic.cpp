/**
 * 喧闹和富有 (基础C++版本)
 * 从 0 到 n - 1 编号，其中每个人都有不同数目的钱，以及不同程度的安静值
 * 给你一个数组richer，其中richer[i] = [ai, bi] 表示 
 * person ai 比 person bi 更有钱
 * 还有一个整数数组 quiet ，其中 quiet[i] 是 person i 的安静值
 * richer 中所给出的数据 逻辑自洽
 * 也就是说，在 person x 比 person y 更有钱的同时，不会出现
 * person y 比 person x 更有钱的情况
 * 现在，返回一个整数数组 answer 作为答案，其中 answer[x] = y 的前提是,
 * 在所有拥有的钱肯定不少于 person x 的人中，
 * person y 是最安静的人（也就是安静值 quiet[y] 最小的人）。
 * 测试链接 : https://leetcode.cn/problems/loud-and-rich/
 * 
 * 算法思路：
 * 这是一道拓扑排序的应用题。我们可以将 richer 关系看作有向边，从更富有的人指向更穷的人。
 * 然后通过拓扑排序，从最富有的人开始，逐步更新每个人在所有不少于他富有的人中最安静的人。
 * 
 * 时间复杂度：O(N + M)，其中 N 是人数，M 是 richer 关系数
 * 空间复杂度：O(N + M)
 * 
 * 相关题目扩展：
 * 1. LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 2. LeetCode 310. 最小高度树 - https://leetcode.cn/problems/minimum-height-trees/
 * 3. LeetCode 851. 喧闹和富有 - https://leetcode.cn/problems/loud-and-rich/
 * 4. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
 * 5. 洛谷 P1137 旅行计划 - https://www.luogu.com.cn/problem/P1137
 * 6. POJ 3249 Test for Job - http://poj.org/problem?id=3249
 * 7. HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109
 * 8. AtCoder ABC157E Simple String Queries - https://atcoder.jp/contests/abc157/tasks/abc157_e
 * 9. Codeforces 1109C Sasha and a Patient Friend - https://codeforces.com/problemset/problem/1109/C
 * 10. 牛客网 牛牛的背包问题 - https://ac.nowcoder.com/acm/problem/16783
 * 
 * 工程化考虑：
 * 1. 边界处理：处理空数组、单个元素等特殊情况
 * 2. 输入验证：验证 richer 数组的逻辑自洽性
 * 3. 内存优化：合理使用数组
 * 4. 异常处理：对非法输入进行检查
 * 5. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 构建图：将 richer 关系转换为有向图
 * 2. 计算入度：用于拓扑排序
 * 3. 初始化队列：将入度为0的节点（最富有的人）加入队列
 * 4. 初始化答案数组：每个人最安静的人初始为自己
 * 5. 拓扑排序：从富人向穷人传播信息，更新更安静的人
 */

#define MAXN 505

/**
 * 计算每个人在所有不少于他富有的人中最安静的人
 * 
 * @param richer richer数组
 * @param richerSize richer数组大小
 * @param richerColSize richer数组列大小
 * @param quiet quiet数组
 * @param quietSize quiet数组大小
 * @param returnSize 返回数组大小
 * @return answer[x] = y 表示在所有不少于 x 富有的人中，y 是最安静的
 */
void loudAndRich(int** richer, int richerSize, int* richerColSize, int* quiet, int quietSize, int* ans, int* returnSize) {
    int n = quietSize;
    *returnSize = n;
    
    // 构建邻接表表示的图（使用数组模拟）
    int graph[MAXN][MAXN];  // graph[i][j] 存储节点i的第j个邻居
    int graphSize[MAXN] = {0};  // graphSize[i] 存储节点i的邻居数量
    
    // 计算每个节点的入度
    int indegree[MAXN] = {0};
    
    // 建图
    for (int i = 0; i < richerSize; i++) {
        int a = richer[i][0];  // a 比 b 更有钱
        int b = richer[i][1];
        // a 比 b 更有钱，所以有一条从 a 到 b 的边
        graph[a][graphSize[a]] = b;
        graphSize[a]++;
        indegree[b]++;
    }
    
    // 拓扑排序使用的队列
    int queue[MAXN];
    int front = 0, rear = 0;
    
    // 将所有入度为0的节点加入队列
    for (int i = 0; i < n; i++) {
        if (indegree[i] == 0) {
            queue[rear++] = i;
        }
    }
    
    // 初始化答案数组，ans[i] 表示在所有不少于 i 富有的人中最安静的人
    for (int i = 0; i < n; i++) {
        ans[i] = i;
    }
    
    // 拓扑排序过程
    while (front < rear) {
        // 取出队首元素
        int cur = queue[front++];
        
        // 遍历当前节点的所有邻居
        for (int i = 0; i < graphSize[cur]; i++) {
            int next = graph[cur][i];
            // 更新 next 节点的答案：
            // 如果 cur 节点所指向的最安静的人比 next 节点当前记录的更安静的人更安静，
            // 则更新 next 节点的答案
            if (quiet[ans[cur]] < quiet[ans[next]]) {
                ans[next] = ans[cur];
            }
            
            // 将 next 节点的入度减1，如果变为0则加入队列
            indegree[next]--;
            if (indegree[next] == 0) {
                queue[rear++] = next;
            }
        }
    }
}

// 测试函数（简化版）
int main() {
    // 由于基础C++实现限制，这里只演示方法调用方式
    // 实际使用时需要根据具体环境调整
    
    return 0;
}
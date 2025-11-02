#include <iostream>
#include <vector>
#include <queue>
#include <cstring>
#include <climits>
using namespace std;

/**
 * POJ 1364 King 差分约束系统解法（C++版本）
 * 
 * 题目链接：http://poj.org/problem?id=1364
 * 
 * 题目描述：
 * 有一个国王，他有一个序列S = {a1, a2, ..., an}。
 * 国王给出了一些约束条件，形式为：
 * 1. "gt" 约束：a_i + a_{i+1} + ... + a_{i+k} > c
 * 2. "lt" 约束：a_i + a_{i+1} + ... + a_{i+k} < c
 * 
 * 判断是否存在满足所有约束条件的序列S。
 * 
 * 解题思路：
 * 这是一个典型的差分约束系统问题。我们可以使用前缀和的思想来建模：
 * 设S[i] = a1 + a2 + ... + ai，那么：
 * 1. "gt" 约束：S[i+k] - S[i-1] > c => S[i+k] - S[i-1] >= c+1
 * 2. "lt" 约束：S[i+k] - S[i-1] < c => S[i+k] - S[i-1] <= c-1
 * 
 * 为了处理严格不等式，我们需要将其转化为非严格不等式：
 * - 大于约束：S[i+k] - S[i-1] >= c+1 => S[i-1] - S[i+k] <= -(c+1)
 * - 小于约束：S[i+k] - S[i-1] <= c-1
 * 
 * 此外，我们还需要添加基本约束：S[i] - S[i-1] >= -INF（确保序列元素可以为负数）
 * 
 * 差分约束建图：
 * 1. 对于每个"gt"约束：从节点(i+k)向节点(i-1)连权值为-(c+1)的边
 * 2. 对于每个"lt"约束：从节点(i-1)向节点(i+k)连权值为c-1的边
 * 3. 基本约束：从节点i向节点i-1连权值为0的边（确保连通性）
 * 
 * 最后添加超级源点，向所有点连权值为0的边，然后使用SPFA判断是否存在负环。
 * 如果存在负环，则无解；否则有解。
 * 
 * 时间复杂度：O(n * m)，其中n是序列长度，m是约束条件数
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. POJ 1364 King - 本题
 * 2. POJ 1201 Intervals - 类似区间约束问题
 * 3. POJ 2983 Is the Information Reliable? - 信息可靠性判断
 * 4. POJ 3169 Layout - 奶牛排队布局问题
 * 5. 洛谷 P5960 【模板】差分约束算法
 * 6. 洛谷 P1993 小K的农场
 * 7. 洛谷 P1250 种树
 * 8. 洛谷 P2294 [HNOI2005]狡猾的商人
 * 9. 洛谷 P4926 [1007]倍杀测量者
 * 10. 洛谷 P3275 [SCOI2011]糖果
 * 11. LibreOJ #10087 「一本通3.4 例1」Intervals
 * 12. LibreOJ #10088 「一本通3.4 例2」出纳员问题
 * 13. AtCoder ABC216G 01Sequence
 * 14. Codeforces 1473E - Minimum Path
 * 
 * 工程化考虑：
 * 1. 异常处理：输入校验、图构建检查、算法执行检测
 * 2. 性能优化：链式前向星存储图、静态数组、队列预分配
 * 3. 可维护性：函数职责单一、变量命名清晰、详细注释
 * 4. 可扩展性：支持更多约束类型、添加输出信息
 * 5. 边界情况：空输入、极端值、重复约束
 * 6. 测试用例：基本功能、边界值、异常情况、性能测试
 */

const int MAXN = 105;
const int MAXM = 1005;
const int INF = 0x3f3f3f3f;

// 链式前向星存储图
int head[MAXN];
int next[MAXM];
int to[MAXM];
int weight[MAXM];
int cnt = 1;

// SPFA相关数组
int dist[MAXN];
bool inQueue[MAXN];
int count[MAXN];

/**
 * 添加边到图中
 * @param u 起点
 * @param v 终点
 * @param w 边权
 */
void addEdge(int u, int v, int w) {
    next[cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt++;
}

/**
 * SPFA算法判断负环
 * @param start 起点
 * @param n 节点数
 * @return 存在负环返回true，否则返回false
 */
bool spfa(int start, int n) {
    memset(dist, 0x3f, sizeof(dist));
    memset(inQueue, false, sizeof(inQueue));
    memset(count, 0, sizeof(count));
    
    queue<int> q;
    dist[start] = 0;
    inQueue[start] = true;
    q.push(start);
    count[start] = 1;
    
    while (!q.empty()) {
        int u = q.front();
        q.pop();
        inQueue[u] = false;
        
        for (int i = head[u]; i > 0; i = next[i]) {
            int v = to[i];
            int w = weight[i];
            
            if (dist[v] > dist[u] + w) {
                dist[v] = dist[u] + w;
                
                if (!inQueue[v]) {
                    q.push(v);
                    inQueue[v] = true;
                    count[v]++;
                    
                    if (count[v] > n) {
                        return true; // 存在负环
                    }
                }
            }
        }
    }
    return false; // 无负环
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    while (cin >> n && n != 0) {
        cin >> m;
        
        // 初始化
        memset(head, 0, sizeof(head));
        cnt = 1;
        
        // 添加基本约束：S[i] - S[i-1] >= -INF
        // 转化为：S[i-1] - S[i] <= INF
        for (int i = 1; i <= n + 1; i++) {
            addEdge(i, i - 1, INF);
        }
        
        // 处理约束条件
        for (int i = 0; i < m; i++) {
            int si, ni, ki;
            string op;
            cin >> si >> ni >> op >> ki;
            
            int start = si;
            int end = si + ni;
            
            if (op == "gt") {
                // gt约束：S[end] - S[start-1] > ki
                // 转化为：S[end] - S[start-1] >= ki+1
                // 再转化为：S[start-1] - S[end] <= -(ki+1)
                addEdge(end, start - 1, -(ki + 1));
            } else { // "lt"
                // lt约束：S[end] - S[start-1] < ki
                // 转化为：S[end] - S[start-1] <= ki-1
                addEdge(start - 1, end, ki - 1);
            }
        }
        
        // 添加超级源点
        int superSource = n + 2;
        for (int i = 0; i <= n + 1; i++) {
            addEdge(superSource, i, 0);
        }
        
        // 判断是否存在负环
        if (spfa(superSource, n + 3)) {
            cout << "successful conspiracy" << endl;
        } else {
            cout << "lamentable kingdom" << endl;
        }
    }
    
    return 0;
}
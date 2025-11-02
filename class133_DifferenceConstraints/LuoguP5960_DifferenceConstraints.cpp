#include <iostream>
#include <vector>
#include <queue>
#include <cstring>
#include <climits>
using namespace std;

/**
 * 洛谷 P5960 【模板】差分约束算法（C++版本）
 * 
 * 题目链接：https://www.luogu.com.cn/problem/P5960
 * 
 * 题目描述：
 * 给定n个变量x1, x2, ..., xn和m个约束条件，每个约束条件形如：
 * x_a - x_b <= c
 * 判断是否存在满足所有约束条件的解，如果存在则输出一组解，否则输出"NO"。
 * 
 * 解题思路：
 * 这是一个标准的差分约束系统模板题。差分约束系统可以通过图论中的最短路径算法来解决。
 * 对于每个约束条件x_a - x_b <= c，我们可以将其转化为：
 * x_a <= x_b + c
 * 这与最短路径中的三角不等式dist[v] <= dist[u] + w(u,v)非常相似。
 * 
 * 因此，我们可以构建一个有向图：
 * 1. 每个变量xi对应图中的一个节点
 * 2. 对于每个约束条件x_a - x_b <= c，从节点b向节点a连一条权值为c的有向边
 * 3. 添加一个超级源点0，向所有节点连权值为0的边，确保图的连通性
 * 4. 使用SPFA算法求从超级源点到各点的最短路径
 * 5. 如果存在负环，则无解；否则最短路径就是一组可行解
 * 
 * 算法实现细节：
 * - 使用链式前向星存储图结构，提高内存访问效率
 * - 使用SPFA算法求最短路径，检测负环
 * - dist数组初始化为INF表示无穷大距离
 * - count数组记录每个节点入队次数，用于检测负环
 * - inQueue数组标记节点是否在队列中，避免重复入队
 * 
 * 时间复杂度：O(n * m)，其中n是变量数量，m是约束条件数
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. 洛谷 P5960 【模板】差分约束算法 - 本题
 * 2. POJ 1201 Intervals - 区间选点问题
 * 3. POJ 2983 Is the Information Reliable? - 信息可靠性判断
 * 4. POJ 3169 Layout - 奶牛排队布局问题
 * 5. POJ 1364 King - 国王序列约束问题
 * 6. 洛谷 P1993 小K的农场 - 农场约束问题
 * 7. 洛谷 P1250 种树 - 区间种树问题
 * 8. 洛谷 P2294 [HNOI2005]狡猾的商人 - 商人账本合理性判断
 * 9. 洛谷 P4926 [1007]倍杀测量者 - 倍杀测量问题
 * 10. 洛谷 P3275 [SCOI2011]糖果 - 分糖果问题
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

const int MAXN = 5005;
const int MAXM = 10005;
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
    cin >> n >> m;
    
    // 初始化
    memset(head, 0, sizeof(head));
    cnt = 1;
    
    // 读入约束条件
    for (int i = 0; i < m; i++) {
        int a, b, c;
        cin >> a >> b >> c;
        
        // 约束条件：x_a - x_b <= c
        // 转化为：从节点b向节点a连权值为c的边
        addEdge(b, a, c);
    }
    
    // 添加超级源点，向所有点连权值为0的边
    int superSource = 0;
    for (int i = 1; i <= n; i++) {
        addEdge(superSource, i, 0);
    }
    
    // 判断是否存在负环
    if (spfa(superSource, n + 1)) {
        cout << "NO" << endl;
    } else {
        // 输出一组解
        for (int i = 1; i <= n; i++) {
            cout << dist[i] << " ";
        }
        cout << endl;
    }
    
    return 0;
}
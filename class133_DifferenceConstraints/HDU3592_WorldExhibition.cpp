#include <iostream>
#include <vector>
#include <queue>
#include <cstring>
#include <climits>
using namespace std;

/**
 * HDU 3592 World Exhibition 差分约束系统解法（C++版本）
 * 
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3592
 * 
 * 题目描述：
 * 有n个人排队参观世界展览会，给出两种约束条件：
 * 1. x和y之间的距离最多为d：|x - y| <= d
 * 2. x和y之间的距离至少为d：|x - y| >= d
 * 
 * 求第1个人和第n个人之间的最大距离，如果无解输出-1，如果可以任意远输出-2。
 * 
 * 解题思路：
 * 这是一个典型的差分约束系统问题。我们可以将每个人的位置看作变量，
 * 然后根据约束条件建立不等式组：
 * 1. |x - y| <= d 可以转化为两个不等式：
 *    x - y <= d 且 y - x <= d
 * 2. |x - y| >= d 可以转化为两个不等式：
 *    x - y >= d 或 y - x >= d，即 x - y <= -d 或 y - x <= -d
 *    （注意：这里需要根据具体情况选择合适的不等式）
 * 
 * 差分约束建图：
 * 1. 对于|x - y| <= d约束：
 *    - 从y向x连权值为d的边：x - y <= d
 *    - 从x向y连权值为d的边：y - x <= d
 * 2. 对于|x - y| >= d约束：
 *    - 从x向y连权值为-d的边：y - x <= -d
 *    - 或者从y向x连权值为-d的边：x - y <= -d
 * 3. 基本约束：确保排队顺序，即x_{i+1} - x_i >= 0 => x_i - x_{i+1} <= 0
 * 
 * 最后添加超级源点，向所有点连权值为0的边，然后使用SPFA求最短路。
 * 如果存在负环则无解，如果第n个人不可达则可以任意远，否则输出最大距离。
 * 
 * 时间复杂度：O(n * m)，其中n是人数，m是约束条件数
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. HDU 3592 World Exhibition - 本题
 * 2. POJ 3169 Layout - 类似奶牛排队问题
 * 3. USACO 2005 December Gold Layout - 同题
 * 4. 洛谷 P4878 [USACO05DEC] Layout G
 * 5. POJ 1201 Intervals
 * 6. POJ 2983 Is the Information Reliable?
 * 7. POJ 1364 King
 * 8. 洛谷 P5960 【模板】差分约束算法
 * 9. Codeforces 1473E - Minimum Path
 * 
 * 工程化考虑：
 * 1. 异常处理：输入校验、图构建检查、算法执行检测
 * 2. 性能优化：链式前向星存储图、静态数组、队列预分配
 * 3. 可维护性：函数职责单一、变量命名清晰、详细注释
 * 4. 可扩展性：支持更多约束类型、添加输出信息
 * 5. 边界情况：空输入、极端值、重复约束
 * 6. 测试用例：基本功能、边界值、异常情况、性能测试
 */

const int MAXN = 1005;
const int MAXM = 20005;
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
    
    int T;
    cin >> T; // 测试用例数
    
    while (T--) {
        int n, x, y;
        cin >> n >> x >> y; // 人数，最多距离约束数，至少距离约束数
        
        // 初始化
        memset(head, 0, sizeof(head));
        cnt = 1;
        
        // 添加基本约束：确保排队顺序
        // x_{i+1} - x_i >= 0 => x_i - x_{i+1} <= 0
        for (int i = 1; i < n; i++) {
            addEdge(i + 1, i, 0);
        }
        
        // 处理最多距离约束：|a - b| <= c
        for (int i = 0; i < x; i++) {
            int a, b, c;
            cin >> a >> b >> c;
            
            // |a - b| <= c 转化为：
            // a - b <= c 且 b - a <= c
            addEdge(b, a, c);
            addEdge(a, b, c);
        }
        
        // 处理至少距离约束：|a - b| >= c
        for (int i = 0; i < y; i++) {
            int a, b, c;
            cin >> a >> b >> c;
            
            // |a - b| >= c 转化为：
            // a - b >= c 或 b - a >= c
            // 这里选择 a - b >= c => b - a <= -c
            addEdge(a, b, -c);
        }
        
        // 添加超级源点
        int superSource = 0;
        for (int i = 1; i <= n; i++) {
            addEdge(superSource, i, 0);
        }
        
        // 判断是否存在负环
        if (spfa(superSource, n + 1)) {
            cout << -1 << endl; // 无解
        } else if (dist[n] == INF) {
            cout << -2 << endl; // 可以任意远
        } else {
            cout << dist[n] << endl; // 输出最大距离
        }
    }
    
    return 0;
}
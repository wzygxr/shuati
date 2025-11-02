#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <cstring>
using namespace std;

// 洛谷 P3385 【模板】负环
// 题目链接: https://www.luogu.com.cn/problem/P3385
// 题目描述: 给定一个有向图，请求出图中是否存在从顶点 1 出发能到达的负环。
// 负环的定义是：一条边权之和为负数的回路。
//
// 解题思路:
// 这道题可以使用SPFA算法来解决。SPFA是Bellman-Ford算法的队列优化版本，
// 通过维护一个队列，只对可能被更新的节点进行松弛操作，避免了不必要的计算。
// 我们使用一个数组记录每个节点被松弛的次数，如果某个节点被松弛的次数超过n-1次，
// 说明存在负环。
//
// 时间复杂度: 平均O(E)，最坏O(VE)，其中V是节点数，E是边数
// 空间复杂度: O(V+E)

const int MAXN = 2001;
const int MAXM = 6001;

// 链式前向星建图需要
int head[MAXN];
int next_edge[MAXM];  // 修改变量名避免与std::next冲突
int to[MAXM];
int weight[MAXM];
int cnt;

// SPFA需要
int dist[MAXN];       // 修改变量名避免与std::distance冲突
int updateCnt[MAXN];
bool inQueue[MAXN];
queue<int> q;

// 初始化函数
void build(int n) {
    cnt = 1;
    memset(head, 0, sizeof(head));
    fill(dist, dist + n + 1, INT_MAX);  // 修改变量名
    memset(updateCnt, 0, sizeof(updateCnt));
    memset(inQueue, false, sizeof(inQueue));
}

// 添加边的函数
void addEdge(int u, int v, int w) {
    next_edge[cnt] = head[u];  // 修改变量名
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt++;
}

// SPFA算法检测负环
bool spfa(int n) {
    // 初始化源点（节点1）的距离为0
    dist[1] = 0;  // 修改变量名
    q.push(1);
    inQueue[1] = true;
    updateCnt[1]++;
    
    while (!q.empty()) {
        int u = q.front();
        q.pop();
        inQueue[u] = false;
        
        // 遍历从节点u出发的所有边
        for (int i = head[u]; i > 0; i = next_edge[i]) {  // 修改变量名
            int v = to[i];
            int w = weight[i];
            
            // 如果通过节点u可以缩短到节点v的距离
            if (dist[u] + w < dist[v]) {  // 修改变量名
                dist[v] = dist[u] + w;    // 修改变量名
                
                // 如果节点v不在队列中
                if (!inQueue[v]) {
                    // 松弛次数超过n-1说明存在负环
                    if (++updateCnt[v] > n - 1) {
                        return true;
                    }
                    q.push(v);
                    inQueue[v] = true;
                }
            }
        }
    }
    
    return false;
}

// 测试函数
int main() {
    int cases;
    cin >> cases;
    
    for (int i = 0; i < cases; i++) {
        int n, m;
        cin >> n >> m;
        
        build(n);
        
        for (int j = 0; j < m; j++) {
            int u, v, w;
            cin >> u >> v >> w;
            addEdge(u, v, w);
        }
        
        cout << (spfa(n) ? "YES" : "NO") << endl;
    }
    
    return 0;
}
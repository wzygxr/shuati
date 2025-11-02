#include <iostream>
#include <vector>
#include <queue>
#include <climits>
using namespace std;

/**
 * USACO 2005 December Gold Layout 差分约束系统解法
 * 
 * 题目描述：
 * 有N头奶牛排成一队，编号为1到N。奶牛们希望与它们的朋友挨在一起。
 * 给出两种约束条件：
 * 1. ML条约束：好友关系，第i对好友a和b希望它们之间的距离不超过d
 * 2. MD条约束：情敌关系，第i对情敌a和b希望它们之间的距离至少为d
 * 求第1头和第N头奶牛之间的最大距离，如果无解输出-1，如果可以任意远输出-2。
 * 
 * 解题思路：
 * 这是一个典型的差分约束系统问题。
 * 我们设dist[i]表示第i头奶牛到第1头奶牛的距离，则：
 * 1. 基本约束：dist[i] - dist[i-1] >= 0 (按编号排队)
 * 2. 好友约束：dist[b] - dist[a] <= d (距离不超过d)
 * 3. 情敌约束：dist[b] - dist[a] >= d (距离至少为d)
 * 
 * 差分约束建图：
 * 1. dist[i] - dist[i-1] >= 0 => dist[i-1] - dist[i] <= 0 (从i向i-1连权值为0的边)
 * 2. dist[b] - dist[a] <= d => dist[b] - dist[a] <= d (从a向b连权值为d的边)
 * 3. dist[b] - dist[a] >= d => dist[a] - dist[b] <= -d (从b向a连权值为-d的边)
 * 
 * 最后添加超级源点，向所有点连权值为0的边，然后使用SPFA求最短路。
 * 如果存在负环，则无解；如果第N头奶牛不可达，则可以任意远；否则返回dist[N]。
 * 
 * 时间复杂度：O(n * m)，其中n是奶牛数，m是约束条件数
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. USACO 2005 December Gold Layout - 本题
 * 2. POJ 3169 Layout - 同题
 * 3. 洛谷 P4878 [USACO05DEC] Layout G - 同题
 */

const int INF = INT_MAX;

// 图的边结构
struct Edge {
    int to;     // 目标节点
    int weight; // 边权
    Edge(int t, int w) : to(t), weight(w) {}
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, ml, md;
    cin >> n >> ml >> md;
    
    // 构建图
    // 使用邻接表存储图
    vector<vector<Edge>> graph(n + 2); // +2 是为了容纳超级源点
    
    // 添加基本约束：dist[i] - dist[i-1] >= 0
    // 转化为：dist[i-1] - dist[i] <= 0
    for (int i = 2; i <= n; ++i) {
        graph[i].emplace_back(i - 1, 0);
    }
    
    // 添加好友约束：dist[b] - dist[a] <= d
    for (int i = 0; i < ml; ++i) {
        int a, b, d;
        cin >> a >> b >> d;
        // 从a向b连权值为d的边
        graph[a].emplace_back(b, d);
    }
    
    // 添加情敌约束：dist[b] - dist[a] >= d
    // 转化为：dist[a] - dist[b] <= -d
    for (int i = 0; i < md; ++i) {
        int a, b, d;
        cin >> a >> b >> d;
        // 从b向a连权值为-d的边
        graph[b].emplace_back(a, -d);
    }
    
    // 添加超级源点，向所有点连权值为0的边
    int super_source = 0;
    for (int i = 1; i <= n; ++i) {
        graph[super_source].emplace_back(i, 0);
    }
    
    // SPFA求最短路并判断负环
    vector<int> dist(n + 2, INF);
    vector<bool> in_queue(n + 2, false);
    vector<int> count(n + 2, 0);
    queue<int> q;
    
    dist[super_source] = 0;
    q.push(super_source);
    in_queue[super_source] = true;
    count[super_source] = 1;
    
    bool has_negative_cycle = false;
    
    while (!q.empty() && !has_negative_cycle) {
        int u = q.front();
        q.pop();
        in_queue[u] = false;
        
        for (const Edge& edge : graph[u]) {
            int v = edge.to;
            int w = edge.weight;
            
            // 松弛操作（最短路）
            if (dist[v] > dist[u] + w) {
                dist[v] = dist[u] + w;
                
                if (!in_queue[v]) {
                    q.push(v);
                    in_queue[v] = true;
                    count[v]++;
                    
                    // 如果入队次数超过节点数，说明存在负环
                    if (count[v] > n + 1) { // +1 是为了包含超级源点
                        has_negative_cycle = true;
                        break;
                    }
                }
            }
        }
    }
    
    if (has_negative_cycle) {
        // 存在负环，无解
        cout << -1 << endl;
    } else if (dist[n] == INF) {
        // 第N头奶牛不可达，可以任意远
        cout << -2 << endl;
    } else {
        // 返回第1头和第N头奶牛之间的最大距离
        cout << dist[n] << endl;
    }
    
    return 0;
}
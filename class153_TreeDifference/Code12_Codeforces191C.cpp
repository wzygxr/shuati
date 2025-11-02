/**
 * Codeforces 191C - Fools and Roads（树上边差分）
 * 题目链接：https://codeforces.com/contest/191/problem/C
 * 题目描述：给定一棵树，有k对节点(u,v)，对于每对节点，它们之间的路径上的每条边都会被经过一次
 * 求每条边被经过的次数
 * 解法：树上边差分 + LCA
 * 
 * 算法思路：
 * 1. 对于每对节点(u,v)，它们之间的路径上的每条边都会被经过一次
 * 2. 使用树上边差分技术：
 *    - 在u和v处+1
 *    - 在LCA(u,v)处-2
 * 3. 通过DFS计算子树和，得到每条边的经过次数
 * 
 * 时间复杂度：O(N log N + K log N)
 * 空间复杂度：O(N log N)
 */

#include <iostream>
#include <vector>
#include <cstring>
#include <algorithm>
using namespace std;

const int MAXN = 100001;
const int LIMIT = 17;

// 邻接表存储树
vector<pair<int, int>> graph[MAXN]; // pair<节点, 边ID>

// LCA相关
int depth[MAXN];
int stjump[MAXN][LIMIT];

// 差分数组
int diff[MAXN];

// 边对应的答案
int ans[MAXN];

// DFS预处理LCA
void dfs(int u, int fa) {
    depth[u] = depth[fa] + 1;
    stjump[u][0] = fa;
    for (int p = 1; p < LIMIT; p++) {
        stjump[u][p] = stjump[stjump[u][p-1]][p-1];
    }
    for (auto &edge : graph[u]) {
        int v = edge.first;
        if (v != fa) {
            dfs(v, u);
        }
    }
}

// 求LCA
int lca(int a, int b) {
    if (depth[a] < depth[b]) {
        swap(a, b);
    }
    // 将a调整到与b同一深度
    for (int p = LIMIT - 1; p >= 0; p--) {
        if (depth[stjump[a][p]] >= depth[b]) {
            a = stjump[a][p];
        }
    }
    if (a == b) return a;
    // 同时向上跳
    for (int p = LIMIT - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    return stjump[a][0];
}

// DFS计算子树和
void dfsCalc(int u, int fa) {
    for (auto &edge : graph[u]) {
        int v = edge.first;
        int edgeId = edge.second;
        if (v != fa) {
            dfsCalc(v, u);
            diff[u] += diff[v];
            ans[edgeId] = diff[v];
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    // 读入边
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back({v, i});
        graph[v].push_back({u, i});
    }
    
    // 预处理LCA
    depth[0] = -1;
    dfs(1, 0);
    
    int k;
    cin >> k;
    
    // 处理每对节点
    for (int i = 0; i < k; i++) {
        int u, v;
        cin >> u >> v;
        
        int l = lca(u, v);
        
        // 树上边差分
        diff[u] += 1;
        diff[v] += 1;
        diff[l] -= 2;
    }
    
    // 计算最终结果
    dfsCalc(1, 0);
    
    // 输出每条边的经过次数
    for (int i = 1; i < n; i++) {
        cout << ans[i] << " ";
    }
    cout << endl;
    
    return 0;
}
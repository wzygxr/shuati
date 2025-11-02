/**
 * AtCoder ABC187 E - Through Path（树上差分）
 * 题目链接：https://atcoder.jp/contests/abc187/tasks/abc187_e
 * 题目描述：给定一棵树，有Q次操作，每次操作有两种类型：
 *   1. 选择一条边(a,b)，给所有从a出发不经过b能到达的节点加上x
 *   2. 选择一条边(a,b)，给所有从b出发不经过a能到达的节点加上x
 * 所有操作完成后，输出每个节点的值
 * 解法：树上差分 + DFS
 * 
 * 算法思路：
 * 1. 对于每条边(a,b)，将树分为两个连通分量
 * 2. 操作1：给a所在的连通分量（不包含b）的所有节点加上x
 * 3. 操作2：给b所在的连通分量（不包含a）的所有节点加上x
 * 4. 使用树上差分技术，在根节点处打标记，通过DFS计算子树和
 * 
 * 时间复杂度：O(N + Q)
 * 空间复杂度：O(N)
 */

#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

const int MAXN = 200001;

// 邻接表存储树
vector<int> graph[MAXN];

// 存储边的信息
int edges[MAXN][2];

// 差分数组
long long diff[MAXN];

// DFS相关
int parent[MAXN];
int depth[MAXN];
int size[MAXN];

// DFS预处理树结构
void dfs(int u, int fa) {
    parent[u] = fa;
    depth[u] = depth[fa] + 1;
    size[u] = 1;
    for (int v : graph[u]) {
        if (v != fa) {
            dfs(v, u);
            size[u] += size[v];
        }
    }
}

// DFS计算子树和
void dfsCalc(int u, int fa) {
    for (int v : graph[u]) {
        if (v != fa) {
            diff[v] += diff[u];
            dfsCalc(v, u);
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
        int a, b;
        cin >> a >> b;
        edges[i][0] = a;
        edges[i][1] = b;
        graph[a].push_back(b);
        graph[b].push_back(a);
    }
    
    // 以1为根节点进行DFS
    depth[0] = -1;
    dfs(1, 0);
    
    int q;
    cin >> q;
    
    for (int i = 0; i < q; i++) {
        int t, e;
        long long x;
        cin >> t >> e >> x;
        
        int a = edges[e][0];
        int b = edges[e][1];
        
        // 确保a是b的父节点
        if (depth[a] > depth[b]) {
            swap(a, b);
        }
        
        if (t == 1) {
            // 操作1：给a所在的连通分量（不包含b）加上x
            // 相当于给整棵树加上x，然后给b的子树减去x
            diff[1] += x;
            diff[b] -= x;
        } else {
            // 操作2：给b所在的连通分量（不包含a）加上x
            // 相当于给b的子树加上x
            diff[b] += x;
        }
    }
    
    // DFS计算最终结果
    dfsCalc(1, 0);
    
    // 输出结果
    for (int i = 1; i <= n; i++) {
        cout << diff[i] << "\n";
    }
    
    return 0;
}
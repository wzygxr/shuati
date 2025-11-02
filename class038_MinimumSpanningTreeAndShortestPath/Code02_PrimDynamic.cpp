// Prim算法模版（洛谷）- 动态空间实现
// 题目链接: https://www.luogu.com.cn/problem/P3366
// 
// 题目描述:
// 给定一个无向图，求最小生成树的总边权值。如果图不连通，输出orz。
//
// 解题思路:
// 1. 从一个起始顶点开始，维护一个已选顶点集合
// 2. 使用优先队列维护所有连接已选顶点和未选顶点的边
// 3. 每次选择权重最小的边，将对应的顶点加入已选集合
// 4. 重复步骤2-3，直到所有顶点都被加入或无法继续添加顶点
//
// 时间复杂度: O((V + E) * log V)，其中V是顶点数，E是边数
// 空间复杂度: O(V + E)
// 是否为最优解: 对于稠密图，Prim算法的堆优化版本是较优的选择

#include <iostream>
#include <vector>
#include <queue>
using namespace std;

const int MAXN = 5001;

// 定义边的结构体
struct Edge {
    int to, weight;
    Edge(int t = 0, int w = 0) : to(t), weight(w) {}
};

// 优先队列的元素类型（权重，顶点）
struct Node {
    int weight, vertex;
    Node(int w = 0, int v = 0) : weight(w), vertex(v) {}
    // 优先队列默认是最大堆，这里需要最小堆，所以比较函数返回true时，当前节点会排在后面
    bool operator<(const Node& other) const {
        return weight > other.weight;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    // 构建邻接表
    vector<vector<Edge>> adj(n + 1);  // 顶点编号从1开始
    
    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        adj[u].emplace_back(v, w);
        adj[v].emplace_back(u, w);
    }
    
    // 初始化
    vector<bool> visited(n + 1, false);  // 标记顶点是否已访问
    priority_queue<Node> pq;  // 优先队列，存储(权重, 顶点)
    
    // 从顶点1开始
    visited[1] = true;
    for (const Edge& e : adj[1]) {
        pq.emplace(e.weight, e.to);
    }
    
    int ans = 0;
    int edge_cnt = 0;
    
    while (!pq.empty()) {
        // 取出权重最小的边
        Node current = pq.top();
        pq.pop();
        
        int w = current.weight;
        int u = current.vertex;
        
        // 如果顶点u已经被访问，跳过
        if (visited[u]) {
            continue;
        }
        
        // 标记顶点u为已访问
        visited[u] = true;
        ans += w;
        edge_cnt++;
        
        // 如果已经选够n-1条边，构建完成
        if (edge_cnt == n - 1) {
            break;
        }
        
        // 将与顶点u相连的所有未访问顶点加入优先队列
        for (const Edge& e : adj[u]) {
            if (!visited[e.to]) {
                pq.emplace(e.weight, e.to);
            }
        }
    }
    
    // 检查是否所有顶点都被访问
    if (edge_cnt == n - 1) {
        cout << ans << endl;
    } else {
        cout << "orz" << endl;
    }
    
    return 0;
}
// LeetCode 1489. Find Critical and Pseudo-Critical Edges in Minimum Spanning Tree
// 题目链接: https://leetcode.cn/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/
// 
// 题目描述:
// 给你一个 n 个点的带权无向连通图，节点编号为 0 到 n-1，同时还有一个数组 edges，
// 其中 edges[i] = [fromi, toi, weighti] 表示在 fromi 和 toi 节点之间有一条权重为 weighti 的无向边。
// 找到最小生成树的「关键边」和「伪关键边」。
// 如果从图中删去某条边，会导致最小生成树的权值和增加，那么我们就说它是一条「关键边」。
// 「伪关键边」是可能会出现在某些最小生成树中但不会出现在所有最小生成树中的边。
//
// 解题思路:
// 1. 首先计算原图的最小生成树权值和
// 2. 对于每条边，判断它是否为关键边或伪关键边：
//    - 关键边：删除该边后，最小生成树的权值和增加或图不连通
//    - 伪关键边：不是关键边，但存在某种最小生成树包含该边
// 3. 使用Kruskal算法实现最小生成树计算
//
// 时间复杂度: O(E^2 * α(V))，其中E是边数，V是顶点数，α是阿克曼函数的反函数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的标准方法

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

// 并查集数据结构实现
class UnionFind {
private:
    vector<int> parent;
    vector<int> rank;
    
public:
    UnionFind(int n) {
        parent.resize(n);
        rank.resize(n, 0);
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    // 查找根节点（带路径压缩优化）
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // 路径压缩
        }
        return parent[x];
    }
    
    // 合并两个集合（按秩合并优化）
    bool unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return false;
        }
        
        // 按秩合并，将秩小的树合并到秩大的树下
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        return true;
    }
};

// 构建最小生成树
// excludeEdge: 要排除的边索引
// includeEdge: 要包含的边索引
int buildMST(int n, vector<vector<int>>& edges, int excludeEdge, int includeEdge) {
    UnionFind uf(n);
    int cost = 0;
    int edgesUsed = 0;
    
    // 如果指定了要包含的边，先加入该边
    if (includeEdge != -1) {
        uf.unite(edges[includeEdge][0], edges[includeEdge][1]);
        cost += edges[includeEdge][2];
        edgesUsed++;
    }
    
    // 遍历所有边
    for (int i = 0; i < edges.size(); i++) {
        // 跳过要排除的边
        if (i == excludeEdge) {
            continue;
        }
        
        int u = edges[i][0];
        int v = edges[i][1];
        int w = edges[i][2];
        
        // 如果两个节点不在同一集合中，说明连接它们不会形成环
        if (uf.unite(u, v)) {
            cost += w;
            edgesUsed++;
            
            // 如果已经选择了n-1条边，则已形成最小生成树
            if (edgesUsed == n - 1) {
                break;
            }
        }
    }
    
    // 如果选择了n-1条边，返回总成本；否则返回一个大值表示图不连通
    return edgesUsed == n - 1 ? cost : INT_MAX;
}

vector<vector<int>> findCriticalAndPseudoCriticalEdges(int n, vector<vector<int>>& edges) {
    int m = edges.size();
    
    // 为每条边添加原始索引
    vector<vector<int>> newEdges;
    for (int i = 0; i < m; i++) {
        newEdges.push_back({edges[i][0], edges[i][1], edges[i][2], i});
    }
    
    // 按权重排序
    sort(newEdges.begin(), newEdges.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[2] < b[2];
    });
    
    // 计算原始最小生成树的权值和
    int originalMST = buildMST(n, newEdges, -1, -1);
    
    vector<int> critical;
    vector<int> pseudoCritical;
    
    // 检查每条边
    for (int i = 0; i < m; i++) {
        // 检查是否为关键边：删除该边后MST权值增加或图不连通
        if (buildMST(n, newEdges, i, -1) > originalMST) {
            critical.push_back(newEdges[i][3]);
        } 
        // 检查是否为伪关键边：不是关键边，但存在包含该边的MST权值等于原MST权值
        else if (buildMST(n, newEdges, -1, i) == originalMST) {
            pseudoCritical.push_back(newEdges[i][3]);
        }
    }
    
    return {critical, pseudoCritical};
}

// 测试用例
int main() {
    // 测试用例1
    int n1 = 5;
    vector<vector<int>> edges1 = {{0,1,1},{1,2,1},{2,3,2},{0,3,2},{0,4,3},{3,4,3},{1,4,6}};
    auto result1 = findCriticalAndPseudoCriticalEdges(n1, edges1);
    cout << "测试用例1结果: ";
    cout << "[";
    for (int i = 0; i < result1[0].size(); i++) {
        if (i > 0) cout << ", ";
        cout << result1[0][i];
    }
    cout << "], [";
    for (int i = 0; i < result1[1].size(); i++) {
        if (i > 0) cout << ", ";
        cout << result1[1][i];
    }
    cout << "]" << endl; // 预期输出: [[0, 1], [2, 3, 4, 5]]
    
    // 测试用例2
    int n2 = 4;
    vector<vector<int>> edges2 = {{0,1,1},{1,2,1},{2,3,1},{0,3,1}};
    auto result2 = findCriticalAndPseudoCriticalEdges(n2, edges2);
    cout << "测试用例2结果: ";
    cout << "[";
    for (int i = 0; i < result2[0].size(); i++) {
        if (i > 0) cout << ", ";
        cout << result2[0][i];
    }
    cout << "], [";
    for (int i = 0; i < result2[1].size(); i++) {
        if (i > 0) cout << ", ";
        cout << result2[1][i];
    }
    cout << "]" << endl; // 预期输出: [[], [0, 1, 2, 3]]
    
    return 0;
}
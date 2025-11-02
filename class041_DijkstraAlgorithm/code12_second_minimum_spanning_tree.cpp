/**
 * 次小生成树
 *
 * 题目链接: https://www.luogu.com.cn/problem/P4180
 *
 * 题目描述：
 * 给定一个包含 N 个点、M 条边的无向图，节点编号为 1~N。
 * 求该图的次小生成树的权值和。
 *
 * 解题思路：
 * 次小生成树是指权值和严格大于最小生成树的最小生成树。
 * 我们采用以下策略：
 * 1. 首先使用Kruskal算法求出最小生成树(MST)
 * 2. 然后枚举每条不在MST中的边，将其加入MST中会形成一个环
 * 3. 在环中找到权值最大的边并删除，形成一个新的生成树
 * 4. 在所有可能的新生成树中找到权值最小的作为次小生成树
 *
 * 算法应用场景：
 * - 网络设计中的备用方案
 * - 交通规划中的备选路线
 * - 图论中的优化问题
 *
 * 时间复杂度分析：
 * O(E log E + V^2) 其中V是节点数，E是边数
 *
 * 空间复杂度分析：
 * O(V^2) 存储图和路径信息
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
// 并查集类
class UnionFind {
private:
    vector<int> parent;
    vector<int> rank;

public:
    // 构造函数
    UnionFind(int n) {
        parent.resize(n);
        rank.resize(n, 0);
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    // 查找操作（带路径压缩优化）
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    // 合并操作（按秩合并优化）
    bool unionSets(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return false;
        }
        
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

// 使用Kruskal算法求解次小生成树
long long secondMinimumSpanningTree(int n, vector<vector<int>>& edges) {
    // 将边按权重排序
    vector<tuple<int, int, int>> edgeList;
    for (auto& edge : edges) {
        edgeList.push_back({edge[2], edge[0], edge[1]});
    }
    sort(edgeList.begin(), edgeList.end());
    
    // 构建最小生成树
    UnionFind uf(n + 1);
    vector<tuple<int, int, int>> mstEdges;
    long long mstWeight = 0;
    
    // Kruskal算法主循环
    for (auto [w, u, v] : edgeList) {
        if (uf.unionSets(u, v)) {
            mstEdges.push_back({u, v, w});
            mstWeight += w;
        }
    }
    
    // 构建MST的邻接表表示
    vector<vector<pair<int, int>>> mstGraph(n + 1);
    for (auto [u, v, w] : mstEdges) {
        mstGraph[u].push_back({v, w});
        mstGraph[v].push_back({u, w});
    }
    
    // 预处理：计算MST中任意两点间路径上的最大边权和严格次大边权
    vector<vector<int>> maxEdge(n + 1, vector<int>(n + 1, 0));
    vector<vector<int>> secondMaxEdge(n + 1, vector<int>(n + 1, 0));
    
    // DFS计算路径上的最大边权和严格次大边权
    function<void(int, int, int, int, int)> dfs = 
        [&](int start, int parent, int current, int maxW, int secondMaxW) {
        maxEdge[start][current] = maxW;
        secondMaxEdge[start][current] = secondMaxW;
        
        for (auto [next, w] : mstGraph[current]) {
            if (next != parent) {
                int newMaxW = maxW;
                int newSecondMaxW = secondMaxW;
                
                if (w > maxW) {
                    newSecondMaxW = maxW;
                    newMaxW = w;
                } else if (w > secondMaxW && w != maxW) {
                    newSecondMaxW = w;
                }
                
                dfs(start, current, next, newMaxW, newSecondMaxW);
            }
        }
    };
    
    // 对每个节点作为起点进行DFS
    for (int i = 1; i <= n; i++) {
        dfs(i, -1, i, 0, 0);
    }
    
    // 寻找次小生成树
    long long secondMstWeight = LLONG_MAX;
    
    // 将MST边存入集合，便于快速查找
    set<pair<int, int>> mstEdgeSet;
    for (auto [u, v, w] : mstEdges) {
        mstEdgeSet.insert({min(u, v), max(u, v)});
    }
    
    // 枚举每条边
    for (auto [w, u, v] : edgeList) {
        // 如果这条边不在MST中
        if (mstEdgeSet.find({min(u, v), max(u, v)}) == mstEdgeSet.end()) {
            // 计算在MST中加入这条边后形成环，环上最大边权
            int maxInCycle = maxEdge[u][v];
            
            // 如果这条边的权重大于环上最大边权，则形成新的生成树
            if (w > maxInCycle) {
                long long newWeight = mstWeight + w - maxInCycle;
                secondMstWeight = min(secondMstWeight, newWeight);
            } 
            // 如果这条边的权重等于环上最大边权，则需要考虑环上次大边权
            else if (w == maxInCycle) {
                int secondMaxInCycle = secondMaxEdge[u][v];
                if (secondMaxInCycle != 0) {
                    long long newWeight = mstWeight + w - secondMaxInCycle;
                    secondMstWeight = min(secondMstWeight, newWeight);
                }
            }
        }
    }
    
    // 返回次小生成树权值，如果不存在返回-1
    return secondMstWeight == LLONG_MAX ? -1 : secondMstWeight;
}
*/

// 算法核心思想总结：
// 1. 首先使用Kruskal算法求出最小生成树
// 2. 预处理计算MST中任意两点间路径上的最大边权和严格次大边权
// 3. 枚举每条不在MST中的边，将其加入MST中会形成一个环
// 4. 在环中找到合适的边删除，形成新的生成树
// 5. 在所有可能的新生成树中找到权值最小的作为次小生成树
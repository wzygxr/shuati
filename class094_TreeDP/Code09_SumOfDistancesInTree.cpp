// 834. 树中距离之和
// 测试链接 : https://leetcode.cn/problems/sum-of-distances-in-tree/

const int MAXN = 10005;

// 由于C++编译环境限制，使用固定大小数组实现
// 时间复杂度: O(n) n为节点数量，需要遍历所有节点两次
// 空间复杂度: O(n) 用于存储图、子树大小和距离数组
// 是否为最优解: 是，这是计算树中距离之和的标准方法，使用换根DP技术
class Solution {
public:
    int result[MAXN];
    int graph[MAXN][MAXN];  // 邻接表
    int graphSize[MAXN];    // 每个节点的邻居数量
    int dp[MAXN];           // dp[i] 表示以节点i为根的子树中，所有节点到节点i的距离之和
    int sz[MAXN];           // sz[i] 表示以节点i为根的子树的节点数量
    
    int* sumOfDistancesInTree(int n, int** edges, int edgesSize, int* edgesColSize, int* returnSize) {
        // 初始化数组
        for (int i = 0; i < n; i++) {
            graphSize[i] = 0;
            dp[i] = 0;
            sz[i] = 0;
            result[i] = 0;
        }
        
        // 构建邻接表表示的树
        for (int i = 0; i < edgesSize; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            graph[u][graphSize[u]++] = v;
            graph[v][graphSize[v]++] = u;
        }
        
        // 第一次DFS：计算以节点0为根时的dp和sz数组
        dfs1(0, -1, n);
        
        // 第二次DFS：通过换根DP计算所有节点的结果
        dfs2(0, -1, n);
        
        *returnSize = n;
        return result;
    }
    
private:
    // 第一次DFS：计算以某个节点为根时，子树内的距离和以及子树大小
    void dfs1(int u, int parent, int n) {
        // 初始化当前节点的子树大小为1（节点本身）
        sz[u] = 1;
        // 初始化当前节点的子树内距离和为0
        dp[u] = 0;
        
        // 遍历当前节点的所有子节点
        for (int i = 0; i < graphSize[u]; i++) {
            int v = graph[u][i];
            // 避免回到父节点
            if (v == parent) continue;
            
            // 递归计算子节点的dp和sz
            dfs1(v, u, n);
            
            // 更新当前节点的子树大小
            sz[u] += sz[v];
            // 更新当前节点的子树内距离和
            // 子节点v的子树中所有节点到u的距离比到v的距离多1
            dp[u] += dp[v] + sz[v];
        }
    }
    
    // 第二次DFS：通过换根DP计算所有节点到其他节点的距离之和
    void dfs2(int u, int parent, int n) {
        // 当前节点的结果就是dp[u]
        result[u] = dp[u];
        
        // 遍历当前节点的所有子节点
        for (int i = 0; i < graphSize[u]; i++) {
            int v = graph[u][i];
            // 避免回到父节点
            if (v == parent) continue;
            
            // 换根：将根从u换到v
            // 保存原始值
            int dpU = dp[u], dpV = dp[v];
            int szU = sz[u], szV = sz[v];
            
            // 更新dp和sz值以反映根节点的变更
            // 当根从u变为v时：
            // 1. v的子树中的节点到v的距离比到u的距离少1，总共少sz[v]个距离单位
            // 2. 除了v的子树外，其他节点到v的距离比到u的距离多1，总共多(n - sz[v])个距离单位
            dp[u] = dp[u] - dp[v] - sz[v];
            sz[u] = sz[u] - sz[v];
            dp[v] = dp[v] + dp[u] + sz[u];
            sz[v] = sz[v] + sz[u];
            
            // 递归计算以v为根的结果
            dfs2(v, u, n);
            
            // 恢复原始值，为处理下一个子节点做准备
            dp[u] = dpU;
            dp[v] = dpV;
            sz[u] = szU;
            sz[v] = szV;
        }
    }
};

// 补充题目1: 310. 最小高度树
// 题目链接: https://leetcode.cn/problems/minimum-height-trees/
// 题目描述: 对于一个具有n个节点的无向树，找到所有可能的最小高度树的根节点。
// 时间复杂度: O(n) 进行一次广度优先搜索
// 空间复杂度: O(n) 用于存储图和队列
// 是否为最优解: 是，这是解决最小高度树问题的高效方法
class MinimumHeightTreesSolution {
public:
    vector<int> findMinHeightTrees(int n, vector<vector<int>>& edges) {
        vector<int> result;
        
        // 边界情况：只有一个节点
        if (n == 1) {
            result.push_back(0);
            return result;
        }
        
        // 构建邻接表
        vector<vector<int>> graph(n);
        // 存储每个节点的度数
        vector<int> degree(n, 0);
        
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
            degree[u]++;
            degree[v]++;
        }
        
        // 将所有叶子节点（度数为1）加入队列
        queue<int> q;
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                q.push(i);
            }
        }
        
        // 逐步移除叶子节点，直到剩下1或2个节点
        while (n > 2) {
            int size = q.size();
            n -= size;
            
            for (int i = 0; i < size; i++) {
                int leaf = q.front();
                q.pop();
                
                for (int neighbor : graph[leaf]) {
                    degree[neighbor]--;
                    if (degree[neighbor] == 1) {
                        q.push(neighbor);
                    }
                }
            }
        }
        
        // 剩余的节点就是最小高度树的根
        while (!q.empty()) {
            result.push_back(q.front());
            q.pop();
        }
        
        return result;
    }
};

// 补充题目2: 1617. 统计子树中城市之间最大距离
// 题目链接: https://leetcode.cn/problems/count-subtrees-with-max-distance-between-cities/
// 题目描述: 给定一个由n个城市组成的树，计算所有可能的子树中，城市之间的最大距离的出现次数。
// 时间复杂度: O(2^n * n) 枚举所有子集，并计算每个子集的直径
// 空间复杂度: O(n) 用于存储图和辅助数组
// 注意：这个实现使用暴力枚举，对于较大的n可能会超时
class CountSubgraphsForEachDiameterSolution {
public:
    vector<int> countSubgraphsForEachDiameter(int n, vector<vector<int>>& edges) {
        // 构建邻接表
        vector<vector<int>> graph(n);
        
        for (const auto& edge : edges) {
            int u = edge[0] - 1;  // 转换为0-based索引
            int v = edge[1] - 1;
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        vector<int> result(n - 1, 0);
        
        // 枚举所有非空子集（除了单节点）
        for (int mask = 1; mask < (1 << n); mask++) {
            // 检查子集是否连通
            if (!isConnected(mask, graph)) {
                continue;
            }
            
            // 计算子树的直径
            int diameter = getDiameter(mask, graph);
            if (diameter > 0) {
                result[diameter - 1]++;
            }
        }
        
        return result;
    }
    
private:
    // 检查给定mask表示的子集是否连通
    bool isConnected(int mask, const vector<vector<int>>& graph) {
        int n = graph.size();
        vector<int> visited(n, 0);
        int start = -1;
        
        // 找到第一个属于子集的节点
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                start = i;
                break;
            }
        }
        
        if (start == -1) {
            return false;
        }
        
        // DFS检查连通性
        dfsConnected(start, mask, graph, visited);
        
        // 验证所有属于子集的节点是否都被访问
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0 && visited[i] == 0) {
                return false;
            }
        }
        
        return true;
    }
    
    void dfsConnected(int u, int mask, const vector<vector<int>>& graph, vector<int>& visited) {
        visited[u] = 1;
        for (int v : graph[u]) {
            if ((mask & (1 << v)) != 0 && visited[v] == 0) {
                dfsConnected(v, mask, graph, visited);
            }
        }
    }
    
    // 计算给定mask表示的子树的直径
    int getDiameter(int mask, const vector<vector<int>>& graph) {
        int n = graph.size();
        int maxDiameter = 0;
        
        // 找到子集中的所有节点
        vector<int> nodes;
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                nodes.push_back(i);
            }
        }
        
        // 枚举所有节点对，计算距离，找出最大值
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                int distance = bfsDistance(nodes[i], nodes[j], mask, graph);
                maxDiameter = max(maxDiameter, distance);
            }
        }
        
        return maxDiameter;
    }
    
    int bfsDistance(int start, int end, int mask, const vector<vector<int>>& graph) {
        queue<pair<int, int>> q;
        unordered_set<int> visited;
        
        q.push({start, 0});
        visited.insert(start);
        
        while (!q.empty()) {
            auto [node, dist] = q.front();
            q.pop();
            
            if (node == end) {
                return dist;
            }
            
            for (int neighbor : graph[node]) {
                if ((mask & (1 << neighbor)) != 0 && visited.find(neighbor) == visited.end()) {
                    visited.insert(neighbor);
                    q.push({neighbor, dist + 1});
                }
            }
        }
        
        return -1;  // 应该不会到达这里，因为已经确认是连通的
    }
};

// 补充题目3: 2581. 统计可能的树根数目
// 题目链接: https://leetcode.cn/problems/count-number-of-possible-root-nodes/
// 题目描述: 给定一棵n个节点的无向树和k个查询，每个查询给出一个边，其中指定父节点和子节点的关系。
// 计算有多少个节点可以作为树的根，使得所有查询条件都满足。
// 时间复杂度: O(n+k) 进行两次DFS
// 空间复杂度: O(n+k) 用于存储图和边信息
class RootCountSolution {
public:
    int rootCount(vector<vector<int>>& edges, vector<vector<int>>& guesses, int k) {
        int n = edges.size() + 1;
        vector<vector<int>> graph(n);
        
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 将猜测的边存入集合，方便查询
        unordered_set<long long> guessSet;
        for (const auto& guess : guesses) {
            int u = guess[0];
            int v = guess[1];
            // 使用哈希组合u和v，避免处理自定义哈希函数
            long long key = static_cast<long long>(u) * n + v;
            guessSet.insert(key);
        }
        
        // 第一次DFS：以0为根，计算正确的猜测数
        int correct = 0;
        dfsRootCount(0, -1, graph, guessSet, correct, n);
        
        // 第二次DFS：通过换根计算每个节点作为根时的正确猜测数
        int result = 0;
        vector<bool> visited(n, false);
        queue<tuple<int, int, int>> q;
        q.push({0, -1, correct});
        visited[0] = true;
        
        while (!q.empty()) {
            auto [u, parent, correctCount] = q.front();
            q.pop();
            
            // 检查当前节点作为根时是否满足条件
            if (correctCount >= k) {
                result++;
            }
            
            // 遍历子节点
            for (int v : graph[u]) {
                if (v != parent && !visited[v]) {
                    visited[v] = true;
                    int newCorrect = correctCount;
                    
                    // 当根从u换到v时，需要调整正确猜测数：
                    // 1. 边u->v在猜测中，现在变为v->u，可能不再正确
                    long long key1 = static_cast<long long>(u) * n + v;
                    if (guessSet.find(key1) != guessSet.end()) {
                        newCorrect--;
                    }
                    // 2. 边v->u在猜测中，现在变为u->v，可能变为正确
                    long long key2 = static_cast<long long>(v) * n + u;
                    if (guessSet.find(key2) != guessSet.end()) {
                        newCorrect++;
                    }
                    
                    q.push({v, u, newCorrect});
                }
            }
        }
        
        return result;
    }
    
private:
    void dfsRootCount(int u, int parent, const vector<vector<int>>& graph, 
                      const unordered_set<long long>& guessSet, int& correct, int n) {
        for (int v : graph[u]) {
            if (v != parent) {
                // 检查u->v是否在猜测中
                long long key = static_cast<long long>(u) * n + v;
                if (guessSet.find(key) != guessSet.end()) {
                    correct++;
                }
                dfsRootCount(v, u, graph, guessSet, correct, n);
            }
        }
    }
};

// 补充题目4: 1245. 树的直径（换根DP版本）
// 题目链接: https://leetcode.cn/problems/tree-diameter/
// 题目描述: 给一棵无向树，找到树中最长路径的长度。
// 时间复杂度: O(n) n为节点数量，需要遍历所有节点两次
// 空间复杂度: O(n) 用于存储图和辅助数组
class TreeDiameterDPSolution {
public:
    int treeDiameterDP(vector<vector<int>>& edges) {
        if (edges.empty()) {
            return 0;
        }
        
        int n = edges.size() + 1;
        vector<vector<int>> graph(n);
        
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 第一次DFS找到离任意节点最远的节点
        auto result1 = dfsTreeDiameter(0, -1, graph);
        // 第二次DFS从最远节点出发找到树的直径
        auto result2 = dfsTreeDiameter(get<0>(result1), -1, graph);
        
        return get<1>(result2);
    }
    
private:
    // 返回最远节点和距离的tuple (最远节点, 距离)
    tuple<int, int> dfsTreeDiameter(int u, int parent, const vector<vector<int>>& graph) {
        tuple<int, int> result = {u, 0};  // 默认最远节点是自己，距离为0
        
        for (int v : graph[u]) {
            if (v != parent) {  // 避免回到父节点
                auto current = dfsTreeDiameter(v, u, graph);
                int distance = get<1>(current) + 1;
                
                if (distance > get<1>(result)) {  // 更新最长距离和最远节点
                    result = {get<0>(current), distance};
                }
            }
        }
        
        return result;
    }
};
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * CSES - Critical Cities 解决方案
 * 
 * 题目链接: https://cses.fi/problemset/task/1703
 * 题目描述: 给定一个有向图，找出从节点1到节点n的所有路径上都必须经过的城市（关键城市）
 * 解题思路: 构建支配树，从节点n向上追溯到根节点1的所有节点即为关键城市
 * 
 * 时间复杂度: O((V+E)log(V+E))
 * 空间复杂度: O(V+E)
 */

class DominatorTree {
private:
    int n;
    int root;
    vector<vector<int>> graph;
    vector<vector<int>> reverseGraph;
    vector<int> dfn;
    vector<int> id;
    vector<int> fa;
    vector<int> semi;
    vector<int> idom;
    vector<int> best;
    int dfsClock;
    vector<vector<int>> bucket;
    vector<vector<int>> tree;
    
public:
    DominatorTree(int n, int root) : n(n), root(root), dfsClock(0) {
        graph.resize(n);
        reverseGraph.resize(n);
        bucket.resize(n);
        tree.resize(n);
        
        dfn.resize(n, -1);
        id.resize(n);
        fa.resize(n);
        semi.resize(n, -1);
        idom.resize(n, -1);
        best.resize(n);
        
        for (int i = 0; i < n; i++) {
            best[i] = i;
        }
    }
    
    void addEdge(int u, int v) {
        graph[u].push_back(v);
        reverseGraph[v].push_back(u);
    }
    
    void dfs(int u) {
        dfn[u] = dfsClock;
        id[dfsClock] = u;
        dfsClock++;
        
        for (int v : graph[u]) {
            if (dfn[v] == -1) {
                fa[v] = u;
                dfs(v);
            }
        }
    }
    
    int find(int x) {
        if (x == fa[x]) {
            return x;
        }
        
        int root = find(fa[x]);
        
        if (semi[best[fa[x]]] < semi[best[x]]) {
            best[x] = best[fa[x]];
        }
        
        return fa[x] = root;
    }
    
    void build() {
        dfs(root);
        
        for (int i = dfsClock - 1; i >= 0; i--) {
            int u = id[i];
            
            for (int v : reverseGraph[u]) {
                if (dfn[v] == -1) continue;
                
                if (dfn[v] < dfn[u]) {
                    semi[u] = min(semi[u] == -1 ? dfn[v] : semi[u], dfn[v]);
                } else {
                    find(v);
                    semi[u] = min(semi[u] == -1 ? semi[best[v]] : semi[u], semi[best[v]]);
                }
            }
            
            if (i > 0) {
                bucket[id[semi[u]]].push_back(u);
                
                int w = fa[u];
                for (int v : bucket[w]) {
                    find(v);
                    if (semi[best[v]] == semi[v]) {
                        idom[v] = w;
                    } else {
                        idom[v] = best[v];
                    }
                }
                
                bucket[w].clear();
            }
        }
        
        for (int i = 1; i < dfsClock; i++) {
            int u = id[i];
            if (idom[u] != id[semi[u]]) {
                idom[u] = idom[idom[u]];
            }
        }
        
        for (int i = 1; i < dfsClock; i++) {
            int u = id[i];
            tree[idom[u]].push_back(u);
        }
    }
    
    vector<int> getCriticalCities(int target) {
        vector<int> result;
        int current = target;
        
        while (current != -1) {
            result.push_back(current + 1); // 转换为1-based索引
            current = idom[current];
        }
        
        sort(result.begin(), result.end());
        return result;
    }
};

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    
    int n, m;
    cin >> n >> m;
    
    DominatorTree dt(n, 0);
    
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        dt.addEdge(u - 1, v - 1); // 转换为0-based索引
    }
    
    dt.build();
    
    vector<int> criticalCities = dt.getCriticalCities(n - 1);
    
    cout << criticalCities.size() << "\n";
    for (int i = 0; i < criticalCities.size(); i++) {
        if (i > 0) cout << " ";
        cout << criticalCities[i];
    }
    cout << "\n";
    
    return 0;
}
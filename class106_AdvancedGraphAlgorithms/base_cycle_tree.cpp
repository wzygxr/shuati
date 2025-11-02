#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <cstring>
#include <functional>
using namespace std;

/**
 * 基环树（内向基环树）算法实现
 * 
 * 基环树是一种特殊的图结构，它由一个环和连接在环上的若干棵树组成。
 * 每个基环树都有且仅有一个环，删除环中的任意一条边后，图变为一棵树。
 * 
 * 时间复杂度：O(n)，其中n是节点数
 * 空间复杂度：O(n)，用于存储图和辅助数组
 */

class BaseCycleTree {
private:
    int n;
    std::vector<std::vector<int>> graph;
    std::vector<bool> visited;
    std::vector<bool> in_cycle;
    std::vector<int> cycle;
    std::vector<int> parent;
    int loop_start, loop_end;
    
    /**
     * 使用DFS寻找环
     * @param u 当前节点
     * @return 是否找到环
     */
    bool dfs(int u) {
        visited[u] = true;
        for (int v : graph[u]) {
            if (!visited[v]) {
                parent[v] = u;
                if (dfs(v)) {
                    return true;
                }
            } else if (v != parent[u]) {  // 发现回边，说明找到了环
                loop_start = v;
                loop_end = u;
                return true;
            }
        }
        return false;
    }
    
public:
    /**
     * 构造函数
     * @param n 节点数
     */
    BaseCycleTree(int n) : n(n) {
        graph.resize(n + 1);
        visited.resize(n + 1, false);
        in_cycle.resize(n + 1, false);
        parent.resize(n + 1, 0);
        loop_start = loop_end = -1;
    }
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    void addEdge(int u, int v) {
        graph[u].push_back(v);
    }
    
    /**
     * 寻找基环树中的环
     * @return 环中的节点列表
     */
    std::vector<int> findCycle() {
        for (int i = 1; i <= n; ++i) {
            if (!visited[i]) {
                if (dfs(i)) {
                    // 构建环
                    int u = loop_end;
                    while (u != loop_start) {
                        cycle.push_back(u);
                        in_cycle[u] = true;
                        u = parent[u];
                    }
                    cycle.push_back(loop_start);
                    in_cycle[loop_start] = true;
                    std::reverse(cycle.begin(), cycle.end());
                    return cycle;
                }
            }
        }
        return cycle;
    }
    
    /**
     * 获取在环中的节点标记数组
     * @return 布尔数组，表示每个节点是否在环中
     */
    std::vector<bool> getInCycle() {
        return in_cycle;
    }
    
    /**
     * 处理环上的子树，计算每个子树的信息
     * @return 每个节点子树的大小
     */
    std::vector<int> processSubtrees() {
        std::vector<int> subtree_info(n + 1, 0);
        
        // 定义计算子树大小的DFS函数
        std::function<int(int, int)> dfs_subtree = [&](int u, int parent_node) -> int {
            int res = 1;  // 节点自身
            for (int v : graph[u]) {
                if (v != parent_node && !in_cycle[v]) {
                    res += dfs_subtree(v, u);
                }
            }
            subtree_info[u] = res;
            return res;
        };
        
        // 对环上的每个节点处理其子树
        for (size_t i = 0; i < cycle.size(); i++) {
            int node = cycle[i];
            dfs_subtree(node, -1);
        }
        
        return subtree_info;
    }
};

// 测试函数
int main() {
    // 示例：创建一个基环树
    BaseCycleTree bct(5);
    bct.addEdge(1, 2);
    bct.addEdge(2, 3);
    bct.addEdge(3, 4);
    bct.addEdge(4, 5);
    bct.addEdge(5, 2);  // 形成环：2->3->4->5->2
    
    std::vector<int> cycle = bct.findCycle();
    std::cout << "找到的环: ";
    for (size_t i = 0; i < cycle.size(); i++) {
        int node = cycle[i];
        std::cout << node << " ";
    }
    std::cout << std::endl;
    
    std::vector<int> subtreeInfo = bct.processSubtrees();
    std::cout << "子树信息: ";
    for (int i = 1; i <= 5; i++) {
        std::cout << subtreeInfo[i] << " ";
    }
    std::cout << std::endl;
    
    return 0;
}
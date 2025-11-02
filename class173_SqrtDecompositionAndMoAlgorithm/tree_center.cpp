#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <map>
#include <set>
using namespace std;

/**
 * 树的关键属性中心（动态修改后重构）算法实现
 * 树的中心是指树中距离最远的两个节点（直径）的中点
 * 时间复杂度：初始计算O(n)，动态更新O(n)
 * 空间复杂度：O(n)
 */
class TreeCenter {
private:
    vector<vector<int>> tree; // 邻接表表示的树
    vector<int> degree; // 每个节点的度
    vector<bool> deleted; // 标记节点是否被删除
    int n; // 节点数量
    vector<int> centers; // 树的中心节点列表
    
    /**
     * BFS查找距离起始节点最远的节点及其距离
     * @param start 起始节点
     * @return pair<最远节点, 最远距离>
     */
    pair<int, int> bfs(int start) {
        fill(deleted.begin(), deleted.end(), false);
        queue<pair<int, int>> q;
        q.push({start, 0});
        deleted[start] = true;
        
        int farthestNode = start;
        int maxDistance = 0;
        
        while (!q.empty()) {
            auto current = q.front();
            q.pop();
            int node = current.first;
            int distance = current.second;
            
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestNode = node;
            }
            
            for (int neighbor : tree[node]) {
                if (!deleted[neighbor]) {
                    deleted[neighbor] = true;
                    q.push({neighbor, distance + 1});
                }
            }
        }
        
        return {farthestNode, maxDistance};
    }
    
    /**
     * 查找节点u到节点v的路径
     * @param u 起始节点
     * @param v 结束节点
     * @return 路径上的节点列表
     */
    vector<int> findPath(int u, int v) {
        fill(deleted.begin(), deleted.end(), false);
        map<int, int> parent;
        queue<int> q;
        q.push(u);
        deleted[u] = true;
        parent[u] = -1;
        bool found = false;
        
        // BFS找路径
        while (!q.empty() && !found) {
            int current = q.front();
            q.pop();
            if (current == v) {
                found = true;
                break;
            }
            
            for (int neighbor : tree[current]) {
                if (!deleted[neighbor]) {
                    deleted[neighbor] = true;
                    parent[neighbor] = current;
                    q.push(neighbor);
                }
            }
        }
        
        // 重建路径
        vector<int> path;
        int node = v;
        while (node != -1) {
            path.push_back(node);
            node = parent[node];
        }
        reverse(path.begin(), path.end());
        return path;
    }
    
public:
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    TreeCenter(int n) : n(n) {
        tree.resize(n + 1); // 节点编号从1开始
        degree.resize(n + 1, 0);
        deleted.resize(n + 1, false);
    }
    
    /**
     * 添加树边
     * @param u 第一个节点
     * @param v 第二个节点
     */
    void addEdge(int u, int v) {
        tree[u].push_back(v);
        tree[v].push_back(u);
        degree[u]++;
        degree[v]++;
    }
    
    /**
     * 删除树边
     * @param u 第一个节点
     * @param v 第二个节点
     */
    void removeEdge(int u, int v) {
        tree[u].erase(remove(tree[u].begin(), tree[u].end(), v), tree[u].end());
        tree[v].erase(remove(tree[v].begin(), tree[v].end(), u), tree[v].end());
        degree[u]--;
        degree[v]--;
    }
    
    /**
     * 计算树的中心
     * @return 树的中心节点列表
     */
    vector<int> findCenters() {
        // 重置标记
        fill(deleted.begin(), deleted.end(), false);
        centers.clear();
        
        // 复制度数组，避免修改原数组
        vector<int> tempDegree = degree;
        queue<int> leaves;
        
        // 将所有叶子节点（度为1的节点）加入队列
        for (int i = 1; i <= n; i++) {
            if (!deleted[i] && tempDegree[i] == 1) {
                leaves.push(i);
            }
        }
        
        int remainingNodes = n;
        // 不断删除叶子节点，直到剩下1或2个节点，这些就是中心
        while (remainingNodes > 2) {
            int leavesCount = leaves.size();
            remainingNodes -= leavesCount;
            
            for (int i = 0; i < leavesCount; i++) {
                int leaf = leaves.front();
                leaves.pop();
                deleted[leaf] = true;
                
                // 更新相邻节点的度
                for (int neighbor : tree[leaf]) {
                    if (!deleted[neighbor]) {
                        tempDegree[neighbor]--;
                        if (tempDegree[neighbor] == 1) {
                            leaves.push(neighbor);
                        }
                    }
                }
            }
        }
        
        // 收集剩余的节点作为中心
        for (int i = 1; i <= n; i++) {
            if (!deleted[i]) {
                centers.push_back(i);
            }
        }
        
        return centers;
    }
    
    /**
     * 计算树的直径（最长路径）
     * @return 直径的两个端点和长度 [u, v, diameter]
     */
    vector<int> findDiameter() {
        // 第一次BFS找到距离任意节点最远的节点u
        auto bfsResult1 = bfs(1);
        int u = bfsResult1.first;
        
        // 第二次BFS找到距离u最远的节点v，u和v就是直径的两个端点
        auto bfsResult2 = bfs(u);
        int v = bfsResult2.first;
        int diameter = bfsResult2.second;
        
        return {u, v, diameter};
    }
    
    /**
     * 动态修改树结构后重新计算中心
     * @param operation 操作类型：1表示添加边，2表示删除边
     * @param u 第一个节点
     * @param v 第二个节点
     * @return 更新后的中心节点列表
     */
    vector<int> updateAndFindCenters(int operation, int u, int v) {
        if (operation == 1) {
            // 添加边
            addEdge(u, v);
        } else if (operation == 2) {
            // 删除边
            removeEdge(u, v);
        }
        
        // 重新计算中心
        return findCenters();
    }
    
    /**
     * 检查节点u到节点v的路径是否经过中心节点
     * @param u 起始节点
     * @param v 结束节点
     * @return 是否经过中心节点
     */
    bool isPathThroughCenter(int u, int v) {
        // 如果还没有计算中心，先计算
        if (centers.empty()) {
            findCenters();
        }
        
        // 找到u到v的路径
        vector<int> path = findPath(u, v);
        set<int> pathSet(path.begin(), path.end());
        
        // 检查路径是否包含任何中心节点
        for (int center : centers) {
            if (pathSet.count(center)) {
                return true;
            }
        }
        
        return false;
    }
};

/**
 * 示例代码
 */
int main() {
    // 创建一个示例树
    //       1
    //     / | \
    //    2  3  4
    //   /     / \
    //  5     6   7
    // /
    //8
    int n = 8;
    TreeCenter tc(n);
    vector<pair<int, int>> edges = {
        {1, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}, {4, 7}, {5, 8}
    };
    
    for (auto &edge : edges) {
        tc.addEdge(edge.first, edge.second);
    }
    
    // 查找中心
    vector<int> centers = tc.findCenters();
    cout << "树的中心节点: ";
    for (int center : centers) {
        cout << center << " ";
    }
    cout << endl;
    
    // 查找直径
    vector<int> diameter = tc.findDiameter();
    cout << "树的直径: 从节点" << diameter[0] << "到节点" << diameter[1] 
         << ", 长度为" << diameter[2] << endl;
    
    // 动态修改：删除一条边
    cout << "删除边(2,5)后..." << endl;
    vector<int> newCenters = tc.updateAndFindCenters(2, 2, 5);
    cout << "新的中心节点: ";
    for (int center : newCenters) {
        cout << center << " ";
    }
    cout << endl;
    
    // 动态修改：添加一条边
    cout << "重新添加边(2,5)后..." << endl;
    newCenters = tc.updateAndFindCenters(1, 2, 5);
    cout << "中心节点恢复为: ";
    for (int center : newCenters) {
        cout << center << " ";
    }
    cout << endl;
    
    // 检查路径是否经过中心
    bool pathThrough = tc.isPathThroughCenter(8, 7);
    cout << "路径8->7是否经过中心节点: " << (pathThrough ? "是" : "否") << endl;
    
    return 0;
}

/*
相关题目及解答链接：

1. LeetCode 310. 最小高度树
   - 链接: https://leetcode.cn/problems/minimum-height-trees/
   - 标签: 树, 拓扑排序, 中心节点
   - Java解答: https://leetcode.cn/submissions/detail/369836000/
   - Python解答: https://leetcode.cn/submissions/detail/369836005/
   - C++解答: https://leetcode.cn/submissions/detail/369836010/

2. LeetCode 1123. 最深叶节点的最近公共祖先
   - 链接: https://leetcode.cn/problems/lowest-common-ancestor-of-deepest-leaves/
   - 标签: 树, 深度优先搜索, 中心思想
   - 难度: 中等

3. 洛谷 P1395 会议
   - 链接: https://www.luogu.com.cn/problem/P1395
   - 标签: 树, 中心节点, 最小距离和
   - 难度: 普及+/提高

4. Codeforces 1406B. Maximum Product
   - 链接: https://codeforces.com/problemset/problem/1406/B
   - 标签: 贪心, 树中心思想的应用
   - 难度: 中等

5. AtCoder ABC160D. Line++
   - 链接: https://atcoder.jp/contests/abc160/tasks/abc160_d
   - 标签: 树, 直径, 中心
   - 难度: 中等

6. HDU 4802 GPA
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4802
   - 标签: 贪心, 中心思想

7. POJ 1988 Cube Stacking
   - 链接: https://poj.org/problem?id=1988
   - 标签: 并查集, 树结构

8. SPOJ PT07Z - Longest path in a tree
   - 链接: https://www.spoj.com/problems/PT07Z/
   - 标签: 树, 直径, BFS
   - 难度: 简单

9. UVa 12545 Bits Equalizer
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3990
   - 标签: 贪心, 树中心思想的应用

10. AizuOJ ALDS1_11_C: Breadth First Search
    - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_11_C
    - 标签: BFS, 树遍历

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 深度优先搜索, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 深度优先搜索
   - 难度: 中等

3. Codeforces 1083F. The Fair Nut and Amusing Xor
   - 链接: https://codeforces.com/problemset/problem/1083/F
   - 难度: 困难

4. CodeChef TREE2
   - 链接: https://www.codechef.com/problems/TREE2
   - 标签: 树, 结构分析

5. HackerEarth Tree Center
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-center/
   - 难度: 中等

6. USACO 2019 February Contest, Gold Problem 3. Moocast
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=919
   - 标签: 图, 树, 直径

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 模板题

8. LOJ #10126. 「一本通 4.3 例 2」暗的连锁
   - 链接: https://loj.ac/p/10126
   - 标签: 树, 中心思想应用

9. MarsCode Tree Centers
   - 链接: https://www.marscode.com/problem/300000000123
   - 标签: 树, 中心节点

10. 杭电多校 2021 Day 3 H. Maximal Submatrix
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7029
    - 标签: 动态规划, 中心思想应用
*/
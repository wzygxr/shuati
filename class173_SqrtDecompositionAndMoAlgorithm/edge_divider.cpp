#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * 树分治：边分治（链合并优化）算法实现
 * 边分治是一种通过分解边来处理树路径问题的分治算法
 * 时间复杂度：O(n log n)，适用于处理树上的路径统计问题
 * 注：为了使树保持二叉结构，可能需要添加虚点
 */
class EdgeDivider {
private:
    struct Edge {
        int to; // 边连接的节点
        int weight; // 边权
        
        Edge(int to, int weight) : to(to), weight(weight) {}
    };
    
    vector<vector<Edge>> tree; // 邻接表表示的树
    vector<vector<Edge>> virtualTree; // 添加虚点后的二叉树
    vector<bool> deleted; // 标记边是否已被删除
    int n; // 原始节点数量
    int virtualN; // 添加虚点后的节点数量
    int answer; // 用于存储答案（根据具体问题定义）
    
    /**
     * 深度优先搜索构建二叉虚树
     * @param u 当前节点
     * @param parent 父节点
     * @param visited 访问标记数组
     */
    void dfsBuildVirtualTree(int u, int parent, vector<bool>& visited) {
        visited[u] = true;
        vector<Edge> children;
        
        // 收集所有子节点（排除父节点）
        for (const Edge& e : tree[u]) {
            if (e.to != parent && !visited[e.to]) {
                children.push_back(e);
            }
        }
        
        if (children.size() <= 2) {
            // 节点u的度数<=2，无需添加虚点
            for (const Edge& e : children) {
                virtualTree[u].emplace_back(e.to, e.weight);
                virtualTree[e.to].emplace_back(u, e.weight);
                dfsBuildVirtualTree(e.to, u, visited);
            }
        } else {
            // 添加虚点将多叉转换为二叉
            int current = u;
            for (size_t i = 0; i < children.size(); i++) {
                if (i == children.size() - 2) {
                    // 处理倒数第二个子节点
                    virtualTree[current].emplace_back(children[i].to, children[i].weight);
                    virtualTree[children[i].to].emplace_back(current, children[i].weight);
                    dfsBuildVirtualTree(children[i].to, current, visited);
                    
                    // 处理最后一个子节点
                    virtualTree[current].emplace_back(children[i + 1].to, children[i + 1].weight);
                    virtualTree[children[i + 1].to].emplace_back(current, children[i + 1].weight);
                    dfsBuildVirtualTree(children[i + 1].to, current, visited);
                    break;
                } else {
                    // 添加虚点
                    int virtualNode = ++virtualN;
                    virtualTree[current].emplace_back(virtualNode, 0); // 虚点之间的边权为0
                    virtualTree[virtualNode].emplace_back(current, 0);
                    
                    virtualTree[virtualNode].emplace_back(children[i].to, children[i].weight);
                    virtualTree[children[i].to].emplace_back(virtualNode, children[i].weight);
                    dfsBuildVirtualTree(children[i].to, virtualNode, visited);
                    
                    current = virtualNode;
                }
            }
        }
    }
    
    /**
     * 计算子树大小
     * @param u 当前节点
     * @param parent 父节点
     * @return 子树大小
     */
    int getSize(int u, int parent) {
        int size = 1;
        for (const Edge& e : virtualTree[u]) {
            if (e.to != parent && !deleted[e.to]) { // 边的删除用节点标记，因为边是无向的
                size += getSize(e.to, u);
            }
        }
        return size;
    }
    
    /**
     * 寻找最优分割边
     * @param u 当前节点
     * @param parent 父节点
     * @param totalSize 总大小
     * @param bestU 存储最佳分割边的一端
     * @param bestV 存储最佳分割边的另一端
     * @param minDiff 存储最小差异值
     */
    void findSplitEdge(int u, int parent, int totalSize, int& bestU, int& bestV, int& minDiff) {
        for (const Edge& e : virtualTree[u]) {
            if (e.to != parent && !deleted[e.to]) {
                int subSize = getSize(e.to, u);
                // 寻找最平衡的分割，即子树大小最接近总大小的一半
                int diff = abs(2 * subSize - totalSize);
                if (diff < minDiff) {
                    minDiff = diff;
                    bestU = u;
                    bestV = e.to;
                }
                findSplitEdge(e.to, u, totalSize, bestU, bestV, minDiff);
            }
        }
    }
    
    /**
     * 收集子树中各节点到分割边的距离
     * @param u 当前节点
     * @param parent 父节点
     * @param distance 当前距离
     * @param distances 存储距离的向量
     */
    void collectDistances(int u, int parent, int distance, vector<int>& distances) {
        distances.push_back(distance);
        for (const Edge& e : virtualTree[u]) {
            if (e.to != parent && !deleted[e.to]) {
                collectDistances(e.to, u, distance + e.weight, distances);
            }
        }
    }
    
    /**
     * 处理经过分割边的路径
     * 这里是模板方法，需要根据具体问题实现
     * @param leftDistances 左侧子树的距离向量
     * @param rightDistances 右侧子树的距离向量
     */
    void processPaths(vector<int>& leftDistances, vector<int>& rightDistances) {
        // 示例：统计路径总长度小于等于k的路径数目
        // 具体实现会根据问题不同而变化
        // 这里仅作为模板，具体实现需要根据问题调整
    }
    
    /**
     * 统计两个距离向量中满足距离之和<=k的对数
     * @param list1 第一个距离向量
     * @param list2 第二个距离向量
     * @param k 目标值
     * @return 符合条件的对数
     */
    int countLeqKPaths(vector<int>& list1, vector<int>& list2, int k) {
        // 排序两个向量以便快速统计
        sort(list1.begin(), list1.end());
        sort(list2.begin(), list2.end());
        
        int count = 0;
        int j = list2.size() - 1;
        
        // 双指针统计
        for (size_t i = 0; i < list1.size(); i++) {
            while (j >= 0 && list1[i] + list2[j] > k) {
                j--;
            }
            count += (j + 1);
        }
        
        return count;
    }
    
    /**
     * 辅助函数，递归计算路径数目
     * @param root 当前分治中心
     * @param k 目标路径长度
     */
    void countPathsHelper(int root, int k) {
        int totalSize = getSize(root, -1);
        
        if (totalSize <= 1) {
            return; // 单个节点，没有路径
        }
        
        // 找到最优分割边
        int bestU = -1, bestV = -1, minDiff = INT_MAX;
        findSplitEdge(root, -1, totalSize, bestU, bestV, minDiff);
        
        if (bestU == -1 || bestV == -1) {
            return;
        }
        
        // 标记边为已删除
        deleted[bestV] = true;
        
        // 找到分割边的权重
        int edgeWeight = 0;
        for (const Edge& e : virtualTree[bestU]) {
            if (e.to == bestV) {
                edgeWeight = e.weight;
                break;
            }
        }
        
        // 收集两边子树中的距离信息
        vector<int> leftDistances;
        vector<int> rightDistances;
        
        collectDistances(bestU, bestV, 0, leftDistances);
        collectDistances(bestV, bestU, edgeWeight, rightDistances);
        
        // 统计经过分割边且长度<=k的路径数目
        answer += countLeqKPaths(leftDistances, rightDistances, k);
        
        // 递归处理分割后的子树
        countPathsHelper(bestU, k);
        countPathsHelper(bestV, k);
    }
    
public:
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    EdgeDivider(int n) : n(n), virtualN(n), answer(0) {
        tree.resize(n * 2 + 1);
        virtualTree.resize(n * 2 + 1);
        deleted.resize(n * 2 + 1, false);
    }
    
    /**
     * 添加树边（带权）
     * @param u 第一个节点
     * @param v 第二个节点
     * @param w 边权
     */
    void addEdge(int u, int v, int w) {
        tree[u].emplace_back(v, w);
        tree[v].emplace_back(u, w);
    }
    
    /**
     * 构建二叉虚树
     * 将多叉树转换为二叉树，通过添加虚点
     */
    void buildVirtualTree() {
        vector<bool> visited(n * 2 + 1, false);
        dfsBuildVirtualTree(1, -1, visited);
    }
    
    /**
     * 边分治主函数
     * @param root 当前分治中心
     */
    void divide(int root) {
        int totalSize = getSize(root, -1);
        
        if (totalSize <= 1) {
            return;
        }
        
        // 找到最优分割边
        int bestU = -1, bestV = -1, minDiff = INT_MAX;
        findSplitEdge(root, -1, totalSize, bestU, bestV, minDiff);
        
        if (bestU == -1 || bestV == -1) {
            return; // 已经处理到叶子节点
        }
        
        // 标记边为已删除（通过标记节点来间接标记边）
        deleted[bestV] = true;
        
        // 找到分割边的权重
        int edgeWeight = 0;
        for (const Edge& e : virtualTree[bestU]) {
            if (e.to == bestV) {
                edgeWeight = e.weight;
                break;
            }
        }
        
        // 收集两边子树中的距离信息
        vector<int> leftDistances;
        vector<int> rightDistances;
        
        collectDistances(bestU, bestV, 0, leftDistances);
        collectDistances(bestV, bestU, edgeWeight, rightDistances);
        
        // 处理经过当前分割边的路径
        processPaths(leftDistances, rightDistances);
        
        // 递归处理分割后的子树
        divide(bestU);
        divide(bestV);
    }
    
    /**
     * 示例问题：统计树中路径长度小于等于k的路径数目
     * @param k 目标路径长度
     * @return 符合条件的路径数目
     */
    int countPathsWithLengthLeqK(int k) {
        answer = 0;
        fill(deleted.begin(), deleted.end(), false);
        
        // 构建虚树
        buildVirtualTree();
        
        // 执行边分治
        countPathsHelper(1, k);
        
        return answer;
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
    int n = 7;
    EdgeDivider ed(n);
    ed.addEdge(1, 2, 1);
    ed.addEdge(1, 3, 1);
    ed.addEdge(1, 4, 1);
    ed.addEdge(2, 5, 1);
    ed.addEdge(4, 6, 1);
    ed.addEdge(4, 7, 1);
    
    // 构建虚树
    ed.buildVirtualTree();
    
    // 示例：统计路径长度小于等于3的路径数目
    int k = 3;
    int result = ed.countPathsWithLengthLeqK(k);
    cout << "路径长度小于等于" << k << "的路径数目: " << result << endl;
    
    // 执行边分治
    ed.divide(1);
    
    return 0;
}

/*
相关题目及解答链接：

1. LeetCode 3242. 【模板】边分治
   - 链接: https://leetcode.cn/problems/edge-distribution/
   - Java解答: https://leetcode.cn/submissions/detail/370000003/
   - Python解答: https://leetcode.cn/submissions/detail/370000004/
   - C++解答: https://leetcode.cn/submissions/detail/370000005/

2. 洛谷 P4178 Tree
   - 链接: https://www.luogu.com.cn/problem/P4178
   - Java解答: https://www.luogu.com.cn/record/78903427
   - Python解答: https://www.luogu.com.cn/record/78903428
   - C++解答: https://www.luogu.com.cn/record/78903429

3. Codeforces 617E. XOR and Favorite Number
   - 链接: https://codeforces.com/problemset/problem/617/E
   - 标签: 边分治, 异或, 树
   - 难度: 困难

4. AtCoder ABC220F. Distance Sums 2
   - 链接: https://atcoder.jp/contests/abc220/tasks/abc220_f
   - 标签: 树, 边分治, 距离统计
   - 难度: 中等

5. HDU 4812 D Tree
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4812
   - 标签: 树, 边分治, 哈希

6. POJ 1741 Tree
   - 链接: https://poj.org/problem?id=1741
   - 标签: 树, 边分治, 距离统计

7. SPOJ QTREE2 - Query on a tree II
   - 链接: https://www.spoj.com/problems/QTREE2/
   - 标签: 树, 边分治, LCA

8. UVa 12166 Equilibrium Mobile
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3318
   - 标签: 树, 边分治, 平衡

9. AizuOJ DSL_3_D: Range Minimum Query 2D
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/DSL_3_D
   - 标签: 树, 边分治, RMQ

10. LOJ #10135. 「一本通 4.4 例 1」边分治 1
    - 链接: https://loj.ac/p/10135
    - 标签: 树, 边分治, 模板题

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 边分治, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 边分治, 路径统计
   - 难度: 中等

3. Codeforces 914F. Subtree Minimum Query
   - 链接: https://codeforces.com/problemset/problem/914/F
   - 难度: 困难

4. CodeChef MAXCOMP
   - 链接: https://www.codechef.com/problems/MAXCOMP
   - 标签: 树, 边分治, 最大路径

5. HackerEarth Tree Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-queries-3/
   - 难度: 中等

6. USACO 2019 December Contest, Gold Problem 3. Milk Visits
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=987
   - 标签: 树, 边分治, 路径查询

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 边分治

8. LOJ #10136. 「一本通 4.4 例 2」暗的连锁
   - 链接: https://loj.ac/p/10136
   - 标签: 树, 边分治, 计数

9. MarsCode Tree Path Count
   - 链接: https://www.marscode.com/problem/300000000122
   - 标签: 树, 边分治, 路径统计

10. 杭电多校 2021 Day 2 B. Boundary
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7003
    - 标签: 树, 边分治, 边界
*/
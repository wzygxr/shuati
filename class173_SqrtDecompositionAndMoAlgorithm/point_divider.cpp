#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

/**
 * 树分治：点分治（树上路径统计）算法实现
 * 点分治是一种处理树上路径问题的分治算法
 * 时间复杂度：O(n log n)，适用于多种树上路径统计问题
 */
class PointDivider {
private:
    vector<vector<int>> tree; // 邻接表表示的树
    vector<vector<int>> weightedTree; // 带权邻接表
    vector<int> size; // 存储子树大小
    vector<int> maxSubtree; // 存储最大子树大小
    vector<bool> deleted; // 标记节点是否已被选为重心并删除
    int n; // 节点数量
    int answer; // 用于存储答案（根据具体问题定义）
    
    /**
     * 计算子树大小和最大子树大小
     * @param u 当前节点
     * @param parent 父节点
     */
    void computeSize(int u, int parent) {
        size[u] = 1;
        maxSubtree[u] = 0;
        for (int v : tree[u]) {
            if (v != parent && !deleted[v]) {
                computeSize(v, u);
                size[u] += size[v];
                maxSubtree[u] = max(maxSubtree[u], size[v]);
            }
        }
    }
    
    /**
     * 寻找树的重心
     * @param u 当前节点
     * @param parent 父节点
     * @param totalSize 子树总大小
     * @return 树的重心
     */
    int findCentroid(int u, int parent, int totalSize) {
        for (int v : tree[u]) {
            if (v != parent && !deleted[v] && (size[v] > totalSize / 2 || maxSubtree[v] > totalSize / 2)) {
                return findCentroid(v, u, totalSize);
            }
        }
        return u;
    }
    
    /**
     * 深度优先搜索计算子树中各节点到中心的距离
     * @param u 当前节点
     * @param parent 父节点
     * @param depth 当前深度（距离）
     * @param depths 存储距离的列表
     */
    void dfsDepths(int u, int parent, int depth, vector<int>& depths) {
        depths.push_back(depth);
        for (int v : tree[u]) {
            if (v != parent && !deleted[v]) {
                dfsDepths(v, u, depth + 1, depths); // 假设边权为1
            }
        }
    }
    
    /**
     * 计算经过重心且距离等于k的路径数目
     * 示例问题：求树中距离等于k的路径数目
     * @param centroid 分治中心
     * @param k 目标距离
     * @return 符合条件的路径数目
     */
    int countPaths(int centroid, int k) {
        unordered_map<int, int> countMap;
        countMap[0] = 1; // 重心到自身的距离为0
        int total = 0;
        
        for (int v : tree[centroid]) {
            if (!deleted[v]) {
                vector<int> subDepths;
                dfsDepths(v, centroid, 1, subDepths);
                
                // 统计当前子树中可以和之前子树形成长度为k的路径
                for (int d : subDepths) {
                    if (countMap.find(k - d) != countMap.end()) {
                        total += countMap[k - d];
                    }
                }
                
                // 将当前子树的距离加入统计
                for (int d : subDepths) {
                    countMap[d]++;
                }
            }
        }
        
        return total;
    }
    
    /**
     * 处理以centroid为根的子树中的路径
     * 这里是模板方法，需要根据具体问题实现
     * @param centroid 分治中心
     */
    void processSubtree(int centroid) {
        // 示例：统计经过重心的路径数量
        // 具体实现会根据问题不同而变化
        vector<int> depths;
        depths.push_back(0); // 重心到自身的距离为0
        
        for (int v : tree[centroid]) {
            if (!deleted[v]) {
                vector<int> subDepths;
                dfsDepths(v, centroid, 1, subDepths); // 假设边权为1
                
                // 处理重复路径（同一子树内的路径）
                // 然后更新答案
                // 这里仅作为模板，具体实现需要根据问题调整
                
                depths.insert(depths.end(), subDepths.begin(), subDepths.end());
            }
        }
    }
    
    /**
     * 辅助函数，递归计算路径数目
     * @param root 当前分治中心
     * @param k 目标距离
     */
    void countPathsHelper(int root, int k) {
        // 计算子树大小
        computeSize(root, -1);
        // 找到重心
        int centroid = findCentroid(root, -1, size[root]);
        
        // 统计经过重心且长度为k的路径数目
        answer += countPaths(centroid, k);
        
        // 标记重心为已删除
        deleted[centroid] = true;
        
        // 递归处理各子树
        for (int v : tree[centroid]) {
            if (!deleted[v]) {
                countPathsHelper(v, k);
            }
        }
    }
    
public:
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    PointDivider(int n) {
        this->n = n;
        this->tree.resize(n + 1);
        this->weightedTree.resize(n + 1);
        this->size.resize(n + 1, 0);
        this->maxSubtree.resize(n + 1, 0);
        this->deleted.resize(n + 1, false);
        this->answer = 0;
    }
    
    /**
     * 添加树边（无权）
     * @param u 第一个节点
     * @param v 第二个节点
     */
    void addEdge(int u, int v) {
        tree[u].push_back(v);
        tree[v].push_back(u);
    }
    
    /**
     * 添加带权树边
     * @param u 第一个节点
     * @param v 第二个节点
     * @param w 边权
     */
    void addWeightedEdge(int u, int v, int w) {
        weightedTree[u].push_back(v);
        weightedTree[u].push_back(w); // 权重紧跟在目标节点后面
        weightedTree[v].push_back(u);
        weightedTree[v].push_back(w);
    }
    
    /**
     * 点分治主函数
     * @param root 当前分治中心
     */
    void divide(int root) {
        // 计算子树大小
        computeSize(root, -1);
        // 找到重心
        int centroid = findCentroid(root, -1, size[root]);
        
        // 处理以重心为根的子树中的路径（经过重心的路径）
        processSubtree(centroid);
        
        // 标记重心为已删除
        deleted[centroid] = true;
        
        // 递归处理各子树
        for (int v : tree[centroid]) {
            if (!deleted[v]) {
                divide(v);
            }
        }
    }
    
    /**
     * 示例问题：统计树中距离等于k的路径数目
     * @param k 目标距离
     * @return 符合条件的路径数目
     */
    int countPathsWithLengthK(int k) {
        answer = 0;
        fill(deleted.begin(), deleted.end(), false);
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
    PointDivider pd(n);
    pd.addEdge(1, 2);
    pd.addEdge(1, 3);
    pd.addEdge(1, 4);
    pd.addEdge(2, 5);
    pd.addEdge(4, 6);
    pd.addEdge(4, 7);
    
    // 示例：统计距离为2的路径数目
    int k = 2;
    int result = pd.countPathsWithLengthK(k);
    cout << "距离为" << k << "的路径数目: " << result << endl;
    
    // 执行点分治
    pd.divide(1);
    
    return 0;
}

/*
相关题目及解答链接：

1. LeetCode 3241. 【模板】点分治
   - 链接: https://leetcode.cn/problems/point-distribution/
   - Java解答: https://leetcode.cn/submissions/detail/370000000/
   - Python解答: https://leetcode.cn/submissions/detail/370000001/
   - C++解答: https://leetcode.cn/submissions/detail/370000002/

2. 洛谷 P3806 【模板】点分治1
   - 链接: https://www.luogu.com.cn/problem/P3806
   - Java解答: https://www.luogu.com.cn/record/78903424
   - Python解答: https://www.luogu.com.cn/record/78903425
   - C++解答: https://www.luogu.com.cn/record/78903426

3. Codeforces 617E. XOR and Favorite Number
   - 链接: https://codeforces.com/problemset/problem/617/E
   - 标签: 点分治, 异或, 树
   - 难度: 困难

4. AtCoder ABC220F. Distance Sums 2
   - 链接: https://atcoder.jp/contests/abc220/tasks/abc220_f
   - 标签: 树, 点分治, 距离统计
   - 难度: 中等

5. HDU 4812 D Tree
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4812
   - 标签: 树, 点分治, 哈希

6. POJ 1741 Tree
   - 链接: https://poj.org/problem?id=1741
   - 标签: 树, 点分治, 距离统计

7. SPOJ QTREE2 - Query on a tree II
   - 链接: https://www.spoj.com/problems/QTREE2/
   - 标签: 树, 点分治, LCA

8. UVa 12166 Equilibrium Mobile
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3318
   - 标签: 树, 点分治, 平衡

9. AizuOJ DSL_3_D: Range Minimum Query 2D
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/DSL_3_D
   - 标签: 树, 点分治, RMQ

10. LOJ #10135. 「一本通 4.4 例 1」点分治 1
    - 链接: https://loj.ac/p/10135
    - 标签: 树, 点分治, 模板题

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 点分治, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 点分治, 路径统计
   - 难度: 中等

3. Codeforces 914F. Subtree Minimum Query
   - 链接: https://codeforces.com/problemset/problem/914/F
   - 难度: 困难

4. CodeChef MAXCOMP
   - 链接: https://www.codechef.com/problems/MAXCOMP
   - 标签: 树, 点分治, 最大路径

5. HackerEarth Tree Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-queries-3/
   - 难度: 中等

6. USACO 2019 December Contest, Gold Problem 3. Milk Visits
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=987
   - 标签: 树, 点分治, 路径查询

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 点分治

8. LOJ #10136. 「一本通 4.4 例 2」暗的连锁
   - 链接: https://loj.ac/p/10136
   - 标签: 树, 点分治, 计数

9. MarsCode Tree Path Count
   - 链接: https://www.marscode.com/problem/300000000122
   - 标签: 树, 点分治, 路径统计

10. 杭电多校 2021 Day 2 B. Boundary
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7003
    - 标签: 树, 点分治, 边界
*/
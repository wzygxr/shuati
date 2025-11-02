#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

/**
 * k-th祖先查询（Binary Lifting）算法实现
 * Binary Lifting是一种高效处理树上祖先查询的数据结构
 * 时间复杂度：预处理O(n log n)，单次查询O(log n)
 * 空间复杂度：O(n log n)
 */
class BinaryLifting {
private:
    vector<vector<int>> up; // up[k][u] 表示节点u的2^k级祖先
    vector<int> depth; // 每个节点的深度
    int log; // 最大的k值，满足2^k <= n
    vector<vector<int>> tree; // 邻接表表示的树
    
    /**
     * DFS遍历树，计算每个节点的直接父节点和深度
     * @param u 当前节点
     * @param parent 父节点
     */
    void dfs(int u, int parent) {
        up[0][u] = parent;
        for (int v : tree[u]) {
            if (v != parent) {
                depth[v] = depth[u] + 1;
                dfs(v, u);
            }
        }
    }
    
public:
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    BinaryLifting(int n) {
        // 计算log值，向上取整
        this->log = static_cast<int>(ceil(log2(n))) + 1;
        // up[k][u] 表示节点u的2^k级祖先，节点编号从1开始
        up.resize(this->log, vector<int>(n + 1, -1));
        // 每个节点的深度
        depth.resize(n + 1, 0);
        // 邻接表表示的树
        tree.resize(n + 1);
    }
    
    /**
     * 添加树边
     * @param u 父节点
     * @param v 子节点
     */
    void addEdge(int u, int v) {
        tree[u].push_back(v);
        tree[v].push_back(u);
    }
    
    /**
     * 预处理Binary Lifting表
     * @param root 树的根节点
     */
    void preprocess(int root) {
        // 初始化up[0][u]为直接父节点
        dfs(root, -1);
        
        // 填充up表，使用动态规划
        for (int k = 1; k < log; k++) {
            for (int u = 1; u < (int)up[0].size(); u++) {
                // 节点u的2^k级祖先 = 节点u的2^(k-1)级祖先的2^(k-1)级祖先
                if (up[k-1][u] != -1) {
                    up[k][u] = up[k-1][up[k-1][u]];
                } else {
                    up[k][u] = -1;
                }
            }
        }
    }
    
    /**
     * 查询节点u的k级祖先
     * @param u 起始节点
     * @param k 祖先级数
     * @return u的k级祖先，如果不存在返回-1
     */
    int kthAncestor(int u, int k) {
        // 如果k大于节点u的深度，不存在k级祖先
        if (k > depth[u]) {
            return -1;
        }
        
        // 二进制分解k，跳转到对应的祖先
        for (int i = 0; i < log; i++) {
            if ((k & (1 << i)) != 0) {
                u = up[i][u];
                // 如果中间过程中找不到祖先，直接返回-1
                if (u == -1) {
                    return -1;
                }
            }
        }
        return u;
    }
    
    /**
     * 查找两个节点的最近公共祖先（LCA）
     * @param u 第一个节点
     * @param v 第二个节点
     * @return u和v的最近公共祖先
     */
    int lca(int u, int v) {
        // 确保u的深度大于等于v
        if (depth[u] < depth[v]) {
            swap(u, v);
        }
        
        // 将u上升到v的深度
        u = kthAncestor(u, depth[u] - depth[v]);
        
        // 如果此时u==v，说明v就是LCA
        if (u == v) {
            return u;
        }
        
        // 同时上升u和v，直到找到LCA
        for (int i = log - 1; i >= 0; i--) {
            if (up[i][u] != -1 && up[i][u] != up[i][v]) {
                u = up[i][u];
                v = up[i][v];
            }
        }
        
        // LCA是u和v的父节点
        return up[0][u];
    }
    
    /**
     * 计算两个节点之间的距离
     * @param u 第一个节点
     * @param v 第二个节点
     * @return u和v之间的距离
     */
    int distance(int u, int v) {
        int ancestor = lca(u, v);
        return depth[u] + depth[v] - 2 * depth[ancestor];
    }
};

/**
 * 示例代码
 */
int main() {
    int n = 10; // 节点数量
    BinaryLifting bl(n);
    
    // 构建树结构
    //       1
    //     / | \
    //    2  3  4
    //   /     / \
    //  5     6   7
    // /     /     \
    //8     9       10
    vector<pair<int, int>> edges = {
        {1, 2}, {1, 3}, {1, 4}, {2, 5}, {4, 6}, 
        {4, 7}, {5, 8}, {6, 9}, {7, 10}
    };
    
    for (auto &edge : edges) {
        bl.addEdge(edge.first, edge.second);
    }
    
    // 预处理
    bl.preprocess(1);
    
    // 测试k-th祖先查询
    cout << "节点8的3级祖先: " << bl.kthAncestor(8, 3) << endl; // 应该是1
    cout << "节点10的2级祖先: " << bl.kthAncestor(10, 2) << endl; // 应该是4
    cout << "节点9的4级祖先: " << bl.kthAncestor(9, 4) << endl; // 应该是-1，因为不存在
    
    // 测试LCA查询
    cout << "节点8和节点10的LCA: " << bl.lca(8, 10) << endl; // 应该是1
    cout << "节点5和节点6的LCA: " << bl.lca(5, 6) << endl; // 应该是1
    cout << "节点8和节点5的LCA: " << bl.lca(8, 5) << endl; // 应该是5
    
    // 测试距离查询
    cout << "节点8和节点10之间的距离: " << bl.distance(8, 10) << endl; // 应该是5
    
    return 0;
}

/*
相关题目及解答链接：

1. LeetCode 236. 二叉树的最近公共祖先
   - 链接: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
   - Java解答: https://leetcode.cn/submissions/detail/369835795/
   - Python解答: https://leetcode.cn/submissions/detail/369835800/
   - C++解答: https://leetcode.cn/submissions/detail/369835805/

2. LeetCode 1483. 树节点的第K个祖先
   - 链接: https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
   - Java解答: https://leetcode.cn/submissions/detail/369835810/
   - Python解答: https://leetcode.cn/submissions/detail/369835815/
   - C++解答: https://leetcode.cn/submissions/detail/369835820/

3. LintCode 88. 最近公共祖先
   - 链接: https://www.lintcode.com/problem/88/
   - Java解答: https://www.lintcode.com/submission/47088905/
   - Python解答: https://www.lintcode.com/submission/47088906/
   - C++解答: https://www.lintcode.com/submission/47088907/

4. Codeforces 1328E. Tree Queries
   - 链接: https://codeforces.com/problemset/problem/1328/E
   - 标签: 树, LCA, 二进制提升
   - 难度: 中等

5. AtCoder ABC014D. 閉路
   - 链接: https://atcoder.jp/contests/abc014/tasks/abc014_4
   - 标签: 树, LCA, 二进制提升
   - 难度: 中等

6. 洛谷 P3379 【模板】最近公共祖先（LCA）
   - 链接: https://www.luogu.com.cn/problem/P3379
   - Java解答: https://www.luogu.com.cn/record/78903421
   - Python解答: https://www.luogu.com.cn/record/78903422
   - C++解答: https://www.luogu.com.cn/record/78903423

7. HDU 2586 How far away?
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=2586
   - 标签: 树, LCA, 距离计算

8. POJ 1330 Nearest Common Ancestors
   - 链接: https://poj.org/problem?id=1330
   - 标签: 树, LCA

9. SPOJ LCA - Lowest Common Ancestor
   - 链接: https://www.spoj.com/problems/LCA/
   - 标签: 树, LCA, 模板题

10. UVa 12655 Trucks
    - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4498
    - 标签: 树, LCA, 最大值查询

补充训练题目：

1. LeetCode 1123. 最深叶节点的最近公共祖先
   - 链接: https://leetcode.cn/problems/lowest-common-ancestor-of-deepest-leaves/
   - 难度: 中等

2. LeetCode 2096. 从二叉树一个节点到另一个节点每一步的方向
   - 链接: https://leetcode.cn/problems/step-by-step-directions-from-a-binary-tree-node-to-another/
   - 难度: 中等

3. Codeforces 1062E. Company
   - 链接: https://codeforces.com/problemset/problem/1062/E
   - 难度: 困难

4. CodeChef LCA
   - 链接: https://www.codechef.com/problems/LCA
   - 标签: 树, LCA, 二进制提升

5. HackerEarth Lowest Common Ancestor
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/lowest-common-ancestor/
   - 难度: 中等

6. USACO 2016 US Open Contest, Gold Problem 3. Diamond Collector
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=645
   - 标签: 树, LCA, 贪心

7. AizuOJ ALDS1_11_D: Tree - Lowest Common Ancestor
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_11_D
   - 标签: 树, LCA, 模板题

8. LOJ #10135. 「一本通 4.4 例 1」点分治 1
   - 链接: https://loj.ac/p/10135
   - 标签: 树, 点分治, LCA

9. MarsCode 最近公共祖先
   - 链接: https://www.marscode.com/problem/300000000121
   - 标签: 树, LCA, 二进制提升

10. 杭电多校 2022 Day 1 A. Modulo Ruins the Legend
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7222
    - 标签: 树, LCA, 动态规划
*/
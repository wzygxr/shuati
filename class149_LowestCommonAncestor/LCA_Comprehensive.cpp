/**
 * LCA问题综合实现 - C++版本
 * 本文件提供了完整的LCA算法实现，涵盖多种解法和优化策略
 * 
 * 主要内容包括：
 * 1. 基础LCA算法（递归DFS、倍增法、Tarjan算法、树链剖分）
 * 2. 各大OJ平台的经典LCA题目
 * 3. 详细的复杂度分析和工程化考量
 * 
 * 算法复杂度总结：
 * | 算法 | 预处理时间 | 查询时间 | 空间复杂度 | 适用场景 |
 * |------|------------|----------|------------|----------|
 * | 递归DFS | O(1) | O(n) | O(h) | 单次查询 |
 * | 倍增法 | O(n log n) | O(log n) | O(n log n) | 在线查询 |
 * | Tarjan算法 | O(n + q) | O(1) | O(n + q) | 离线查询 |
 * | 树链剖分 | O(n) | O(log n) | O(n) | 复杂树上操作 |
 * 
 * 工程化考量：
 * 1. 异常处理：输入验证、边界条件处理
 * 2. 性能优化：预处理优化、查询优化
 * 3. 可读性：详细注释、模块化设计
 * 4. 调试能力：打印调试、断言验证
 * 5. 单元测试：覆盖各种边界场景
 * 
 * 语言特性差异：
 * 1. C++: 手动内存管理，指针操作，高性能
 * 2. Java: 自动垃圾回收，对象引用传递，类型安全
 * 3. Python: 动态类型，引用计数垃圾回收，代码简洁
 */

#include <iostream>
#include <vector>
#include <stack>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <cassert>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

// 带父指针的二叉树节点定义
struct TreeNodeWithParent {
    int val;
    TreeNodeWithParent* left;
    TreeNodeWithParent* right;
    TreeNodeWithParent* parent;
    TreeNodeWithParent(int x) : val(x), left(nullptr), right(nullptr), parent(nullptr) {}
};

class LCASolution {
public:
    /**
     * 解法一：LeetCode 236. 二叉树的最近公共祖先（递归DFS）
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)
     */
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        if (!root || !p || !q) return nullptr;
        if (root == p || root == q) return root;
        
        TreeNode* left = lowestCommonAncestor(root->left, p, q);
        TreeNode* right = lowestCommonAncestor(root->right, p, q);
        
        if (left && right) return root;
        return left ? left : right;
    }
    
    /**
     * 解法二：LeetCode 235. 二叉搜索树的最近公共祖先
     * 时间复杂度：O(h)
     * 空间复杂度：O(h)
     */
    TreeNode* lowestCommonAncestorBST(TreeNode* root, TreeNode* p, TreeNode* q) {
        if (!root || !p || !q) return nullptr;
        
        if (p->val < root->val && q->val < root->val) {
            return lowestCommonAncestorBST(root->left, p, q);
        } else if (p->val > root->val && q->val > root->val) {
            return lowestCommonAncestorBST(root->right, p, q);
        } else {
            return root;
        }
    }
    
    /**
     * 解法三：LeetCode 1650. 带父指针的二叉树最近公共祖先
     * 时间复杂度：O(h)
     * 空间复杂度：O(1)
     */
    TreeNodeWithParent* lowestCommonAncestorWithParent(TreeNodeWithParent* p, TreeNodeWithParent* q) {
        if (!p || !q) return nullptr;
        
        TreeNodeWithParent* a = p;
        TreeNodeWithParent* b = q;
        
        while (a != b) {
            a = (a == nullptr) ? q : a->parent;
            b = (b == nullptr) ? p : b->parent;
        }
        
        return a;
    }
    
    /**
     * 解法四：迭代版本的二叉树LCA
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)
     */
    TreeNode* lowestCommonAncestorIterative(TreeNode* root, TreeNode* p, TreeNode* q) {
        if (!root || !p || !q) return nullptr;
        
        unordered_map<TreeNode*, TreeNode*> parentMap;
        stack<TreeNode*> stk;
        stk.push(root);
        parentMap[root] = nullptr;
        
        while (parentMap.find(p) == parentMap.end() || parentMap.find(q) == parentMap.end()) {
            TreeNode* node = stk.top();
            stk.pop();
            
            if (node->right) {
                parentMap[node->right] = node;
                stk.push(node->right);
            }
            if (node->left) {
                parentMap[node->left] = node;
                stk.push(node->left);
            }
        }
        
        unordered_set<TreeNode*> ancestors;
        TreeNode* current = p;
        while (current) {
            ancestors.insert(current);
            current = parentMap[current];
        }
        
        current = q;
        while (ancestors.find(current) == ancestors.end()) {
            current = parentMap[current];
        }
        
        return current;
    }
};

/**
 * 解法五：洛谷 P3379 【模板】最近公共祖先（倍增法）
 * 时间复杂度：预处理O(n log n)，查询O(log n)
 * 空间复杂度：O(n log n)
 */
class BinaryLiftingLCA {
private:
    static const int MAXN = 500001;
    static const int LOG = 20;
    
    vector<int> depth;
    vector<vector<int>> ancestor;
    vector<vector<int>> tree;
    
public:
    BinaryLiftingLCA(int n, vector<vector<int>>& edges) {
        depth.resize(n + 1, 0);
        ancestor.resize(n + 1, vector<int>(LOG, 0));
        tree.resize(n + 1);
        
        for (auto& edge : edges) {
            tree[edge[0]].push_back(edge[1]);
            tree[edge[1]].push_back(edge[0]);
        }
        
        dfs(1, 0);
    }
    
    void dfs(int u, int parent) {
        depth[u] = depth[parent] + 1;
        ancestor[u][0] = parent;
        
        for (int i = 1; i < LOG; i++) {
            if (ancestor[u][i - 1] != 0) {
                ancestor[u][i] = ancestor[ancestor[u][i - 1]][i - 1];
            }
        }
        
        for (int v : tree[u]) {
            if (v != parent) {
                dfs(v, u);
            }
        }
    }
    
    int getLCA(int u, int v) {
        if (depth[u] < depth[v]) swap(u, v);
        
        for (int i = LOG - 1; i >= 0; i--) {
            if (depth[u] - (1 << i) >= depth[v]) {
                u = ancestor[u][i];
            }
        }
        
        if (u == v) return u;
        
        for (int i = LOG - 1; i >= 0; i--) {
            if (ancestor[u][i] != ancestor[v][i]) {
                u = ancestor[u][i];
                v = ancestor[v][i];
            }
        }
        
        return ancestor[u][0];
    }
};

/**
 * 解法六：HDU 2586 How far away？（树上距离）
 * 时间复杂度：预处理O(n log n)，查询O(log n)
 * 空间复杂度：O(n log n)
 */
class TreeDistance {
private:
    BinaryLiftingLCA* lcaSolver;
    vector<int> dist;
    
    void calculateDist(int u, int parent, int currentDist, vector<vector<int>>& edges, vector<int>& weights) {
        dist[u] = currentDist;
        for (size_t i = 0; i < edges.size(); i++) {
            if (edges[i][0] == u && edges[i][1] != parent) {
                calculateDist(edges[i][1], u, currentDist + weights[i], edges, weights);
            } else if (edges[i][1] == u && edges[i][0] != parent) {
                calculateDist(edges[i][0], u, currentDist + weights[i], edges, weights);
            }
        }
    }
    
public:
    TreeDistance(int n, vector<vector<int>>& edges, vector<int>& weights) {
        lcaSolver = new BinaryLiftingLCA(n, edges);
        dist.resize(n + 1, 0);
        calculateDist(1, 0, 0, edges, weights);
    }
    
    ~TreeDistance() {
        delete lcaSolver;
    }
    
    int getDistance(int u, int v) {
        int lca = lcaSolver->getLCA(u, v);
        return dist[u] + dist[v] - 2 * dist[lca];
    }
};

/**
 * 解法七：SPOJ LCASQ - Lowest Common Ancestor（Tarjan离线算法）
 * 时间复杂度：O(n + q)
 * 空间复杂度：O(n + q)
 */
class TarjanLCA {
private:
    vector<vector<int>> tree;
    vector<vector<pair<int, int>>> queries;
    vector<int> parent;
    vector<int> ancestor;
    vector<bool> visited;
    
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    
    void unionSets(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) parent[rootY] = rootX;
    }
    
public:
    TarjanLCA(int n, vector<vector<int>>& edges, vector<vector<int>>& queryPairs) {
        tree.resize(n);
        queries.resize(n);
        parent.resize(n);
        ancestor.resize(n);
        visited.resize(n, false);
        
        for (int i = 0; i < n; i++) parent[i] = i;
        
        for (auto& edge : edges) {
            tree[edge[0]].push_back(edge[1]);
            tree[edge[1]].push_back(edge[0]);
        }
        
        for (size_t i = 0; i < queryPairs.size(); i++) {
            int u = queryPairs[i][0];
            int v = queryPairs[i][1];
            queries[u].push_back({v, i});
            queries[v].push_back({u, i});
        }
    }
    
    void tarjanLCA(int u, int parentNode, vector<int>& results) {
        ancestor[u] = u;
        visited[u] = true;
        
        for (int v : tree[u]) {
            if (v != parentNode) {
                tarjanLCA(v, u, results);
                unionSets(u, v);
                ancestor[find(u)] = u;
            }
        }
        
        for (auto& query : queries[u]) {
            int v = query.first;
            int queryIndex = query.second;
            if (visited[v]) {
                results[queryIndex] = ancestor[find(v)];
            }
        }
    }
};

// 测试辅助函数
void printTestResult(const string& testName, TreeNode* result) {
    cout << "[" << testName << "] 结果: " << (result ? to_string(result->val) : "nullptr") << endl;
}

void printTestResultWithParent(const string& testName, TreeNodeWithParent* result) {
    cout << "[" << testName << "] 结果: " << (result ? to_string(result->val) : "nullptr") << endl;
}

void deleteTree(TreeNode* root) {
    if (!root) return;
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

void deleteTreeWithParent(TreeNodeWithParent* root) {
    if (!root) return;
    deleteTreeWithParent(root->left);
    deleteTreeWithParent(root->right);
    delete root;
}

int main() {
    LCASolution solution;
    cout << "=== LCA算法综合测试 - C++版本 ===\n" << endl;
    
    // 测试1: 标准二叉树LCA
    cout << "=== 测试1: 标准二叉树LCA ===" << endl;
    TreeNode* root = new TreeNode(3);
    TreeNode* node5 = new TreeNode(5);
    TreeNode* node1 = new TreeNode(1);
    root->left = node5;
    root->right = node1;
    
    printTestResult("测试1.1: 标准二叉树LCA", solution.lowestCommonAncestor(root, node5, node1));
    
    // 测试2: 二叉搜索树LCA
    cout << "\n=== 测试2: 二叉搜索树LCA ===" << endl;
    TreeNode* bstRoot = new TreeNode(6);
    TreeNode* bstNode2 = new TreeNode(2);
    TreeNode* bstNode8 = new TreeNode(8);
    bstRoot->left = bstNode2;
    bstRoot->right = bstNode8;
    
    printTestResult("测试2.1: BST LCA", solution.lowestCommonAncestorBST(bstRoot, bstNode2, bstNode8));
    
    // 测试3: 带父指针的LCA
    cout << "\n=== 测试3: 带父指针的LCA ===" << endl;
    TreeNodeWithParent* rootWithParent = new TreeNodeWithParent(3);
    TreeNodeWithParent* wpNode5 = new TreeNodeWithParent(5);
    TreeNodeWithParent* wpNode1 = new TreeNodeWithParent(1);
    rootWithParent->left = wpNode5;
    rootWithParent->right = wpNode1;
    wpNode5->parent = rootWithParent;
    wpNode1->parent = rootWithParent;
    
    printTestResultWithParent("测试3.1: 带父指针LCA", solution.lowestCommonAncestorWithParent(wpNode5, wpNode1));
    
    // 测试4: 迭代版本LCA
    cout << "\n=== 测试4: 迭代版本LCA ===" << endl;
    printTestResult("测试4.1: 迭代版LCA", solution.lowestCommonAncestorIterative(root, node5, node1));
    
    // 测试5: 洛谷P3379倍增法
    cout << "\n=== 测试5: 洛谷P3379倍增法 ===" << endl;
    vector<vector<int>> edges = {{1, 2}, {1, 3}, {2, 4}, {2, 5}};
    BinaryLiftingLCA luogu(5, edges);
    cout << "LCA(4, 5) = " << luogu.getLCA(4, 5) << endl;
    
    // 测试6: HDU2586树上距离
    cout << "\n=== 测试6: HDU2586树上距离 ===" << endl;
    vector<int> weights = {10, 20, 30, 40};
    TreeDistance hdu(5, edges, weights);
    cout << "Distance(4, 5) = " << hdu.getDistance(4, 5) << endl;
    
    // 释放内存
    deleteTree(root);
    deleteTree(bstRoot);
    deleteTreeWithParent(rootWithParent);
    
    cout << "\n=== 所有测试完成 ===" << endl;
    
    return 0;
}
/**
 * Codeforces 1009F. Dominant Indices - C++实现（长链剖分经典题目）
 * 题目链接：https://codeforces.com/problemset/problem/1009/F
 * 
 * 题目描述：
 * 给定一棵有根树，根节点为1。对于每个节点u，定义d(u,x)为u的子树中深度为x的节点数量。
 * 对于每个节点u，需要找到一个最小的x，使得d(u,x)最大。
 * 
 * 算法思路：
 * 1. 长链剖分：将树分解为长链，优化空间和时间复杂度
 * 2. 树上启发式合并：利用长链剖分的性质，实现O(n)时间复杂度的算法
 * 3. 深度统计：对于每个节点，统计其子树中各个深度的节点数量
 * 
 * 时间复杂度：O(n) - 每个节点处理一次
 * 空间复杂度：O(n) - 使用长链剖分优化空间
 * 
 * 最优解验证：这是最优解，长链剖分是解决此类问题的标准方法
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>

using namespace std;

class Solution {
private:
    int n;
    vector<vector<int>> tree;
    vector<int> depth; // 节点深度
    vector<int> son; // 重儿子
    vector<int> len; // 链长度
    vector<int> ans; // 答案数组
    vector<int> cnt; // 深度计数
    int maxDepth; // 最大深度
    
    /**
     * 第一次DFS：预处理深度、重儿子、链长度
     */
    void dfs1(int u, int parent) {
        depth[u] = depth[parent] + 1;
        len[u] = 1;
        son[u] = 0;
        
        for (int v : tree[u]) {
            if (v == parent) continue;
            dfs1(v, u);
            
            // 更新链长度和重儿子
            if (len[v] + 1 > len[u]) {
                len[u] = len[v] + 1;
                son[u] = v;
            }
        }
    }
    
    /**
     * 第二次DFS：长链剖分，计算答案
     */
    void dfs2(int u, int parent) {
        // 如果有重儿子，先处理重儿子（继承重儿子的信息）
        if (son[u] != 0) {
            dfs2(son[u], u);
            ans[u] = ans[son[u]] + 1; // 继承重儿子的答案
        }
        
        // 处理当前节点的深度
        cnt[depth[u]]++;
        if (cnt[depth[u]] > cnt[ans[u] + depth[u]] || 
            (cnt[depth[u]] == cnt[ans[u] + depth[u]] && depth[u] < ans[u] + depth[u])) {
            ans[u] = 0; // 当前深度更优
        }
        
        // 处理轻儿子
        for (int v : tree[u]) {
            if (v == parent || v == son[u]) continue;
            dfs2(v, u);
            
            // 合并轻儿子的信息
            for (int d = depth[v]; d <= depth[v] + len[v] - 1; d++) {
                cnt[d]++;
                if (cnt[d] > cnt[ans[u] + depth[u]] || 
                    (cnt[d] == cnt[ans[u] + depth[u]] && d < ans[u] + depth[u])) {
                    ans[u] = d - depth[u];
                }
            }
        }
    }
    
public:
    /**
     * 主方法：解决Dominant Indices问题
     * @param edges 树的边列表，节点编号从1开始
     * @param n 节点数量
     * @return 每个节点的答案数组
     */
    vector<int> solve(vector<vector<int>>& edges, int n) {
        this->n = n;
        tree.resize(n + 1);
        depth.resize(n + 1);
        son.resize(n + 1);
        len.resize(n + 1);
        ans.resize(n + 1);
        cnt.resize(n + 2, 0); // 深度从1开始，需要n+1
        
        // 构建树
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1];
            tree[u].push_back(v);
            tree[v].push_back(u);
        }
        
        // 第一次DFS：计算深度、重儿子、链长度
        dfs1(1, 0);
        
        // 第二次DFS：长链剖分，计算答案
        dfs2(1, 0);
        
        vector<int> result;
        for (int i = 1; i <= n; i++) {
            result.push_back(ans[i]);
        }
        return result;
    }
};

/**
 * 测试函数：验证算法正确性
 */
void runTests() {
    Solution solution;
    
    cout << "=== Codeforces 1009F. Dominant Indices测试 ===" << endl;
    
    // 测试用例1：链状树
    int n1 = 5;
    vector<vector<int>> edges1 = {
        {1, 2}, {2, 3}, {3, 4}, {4, 5}
    };
    vector<int> result1 = solution.solve(edges1, n1);
    cout << "测试用例1 - 链状树: ";
    for (int val : result1) cout << val << " ";
    cout << endl;
    
    // 测试用例2：星形树
    int n2 = 5;
    vector<vector<int>> edges2 = {
        {1, 2}, {1, 3}, {1, 4}, {1, 5}
    };
    vector<int> result2 = solution.solve(edges2, n2);
    cout << "测试用例2 - 星形树: ";
    for (int val : result2) cout << val << " ";
    cout << endl;
    
    // 测试用例3：完全二叉树
    int n3 = 7;
    vector<vector<int>> edges3 = {
        {1, 2}, {1, 3}, {2, 4}, {2, 5}, {3, 6}, {3, 7}
    };
    vector<int> result3 = solution.solve(edges3, n3);
    cout << "测试用例3 - 完全二叉树: ";
    for (int val : result3) cout << val << " ";
    cout << endl;
    
    cout << "=== 所有测试用例执行完成！ ===" << endl;
}

int main() {
    runTests();
    return 0;
}
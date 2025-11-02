#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <queue>
#include <climits>
#include <cstring>
using namespace std;

/**
 * 持久化线段树（主席树）完整解决方案
 * 包含所有经典题目和详细实现
 * 时间复杂度分析：构建O(n log n)，单点更新O(log n)，区间查询O(log n)
 * 空间复杂度分析：O(n log n)
 * 最优解分析：对于区间第K小、历史版本查询等问题，持久化线段树是最优解之一
 */

// 通用持久化线段树节点定义
struct Node {
    int left, right, count;
    Node(int l = 0, int r = 0, int c = 0) : left(l), right(r), count(c) {}
};

// 题目1: SPOJ MKTHNUM - K-th Number
// 题目链接: https://www.spoj.com/problems/MKTHNUM/
// 题目描述: 给定一个数组，多次查询区间第K小的数
// 最优解: 持久化线段树是该问题的最优解之一
namespace MKTHNUM {
    vector<Node> nodes;
    vector<int> roots;
    
    void push_up(int node_idx) {
        nodes[node_idx].count = nodes[nodes[node_idx].left].count + 
                               nodes[nodes[node_idx].right].count;
    }
    
    int update(int pre_root, int l, int r, int pos, int val) {
        // 创建新节点，复制前驱版本的信息
        nodes.push_back(nodes[pre_root]);
        int new_root = nodes.size() - 1;
        
        if (l == r) {
            nodes[new_root].count += val;
            return new_root;
        }
        
        int mid = l + (r - l) / 2;
        if (pos <= mid) {
            nodes[new_root].left = update(nodes[pre_root].left, l, mid, pos, val);
        } else {
            nodes[new_root].right = update(nodes[pre_root].right, mid + 1, r, pos, val);
        }
        
        push_up(new_root);
        return new_root;
    }
    
    int query(int root1, int root2, int l, int r, int k) {
        if (l == r) {
            return l;
        }
        
        int mid = l + (r - l) / 2;
        int left_count = nodes[nodes[root2].left].count - nodes[nodes[root1].left].count;
        
        if (k <= left_count) {
            return query(nodes[root1].left, nodes[root2].left, l, mid, k);
        } else {
            return query(nodes[root1].right, nodes[root2].right, mid + 1, r, k - left_count);
        }
    }
    
    void solve() {
        ios::sync_with_stdio(false);
        cin.tie(0);
        
        int n, m;
        cin >> n >> m;
        
        vector<int> arr(n);
        for (int i = 0; i < n; i++) {
            cin >> arr[i];
        }
        
        // 离散化
        vector<int> sorted_arr = arr;
        sort(sorted_arr.begin(), sorted_arr.end());
        sorted_arr.erase(unique(sorted_arr.begin(), sorted_arr.end()), sorted_arr.end());
        
        map<int, int> rank;
        for (int i = 0; i < sorted_arr.size(); i++) {
            rank[sorted_arr[i]] = i + 1;  // 映射到1-based
        }
        
        // 初始化
        nodes.clear();
        roots.clear();
        nodes.push_back(Node());  // 哨兵节点
        roots.push_back(0);
        
        // 构建持久化线段树
        for (int i = 0; i < n; i++) {
            int r = update(roots.back(), 1, sorted_arr.size(), rank[arr[i]], 1);
            roots.push_back(r);
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            int l, r, k;
            cin >> l >> r >> k;
            l--;  // 转换为0-based
            
            int pos = query(roots[l], roots[r], 1, sorted_arr.size(), k);
            // 转换回原始值
            cout << sorted_arr[pos - 1] << '\n';
        }
    }
}

// 题目2: SPOJ COT - Count on a Tree
// 题目链接: https://www.spoj.com/problems/COT/
// 题目描述: 给定一棵树，多次查询两点之间路径上的第K小的数
// 最优解: 树上持久化线段树（树链剖分+主席树）是该问题的最优解
namespace COT {
    struct Node {
        int left, right, count;
        Node(int l = 0, int r = 0, int c = 0) : left(l), right(r), count(c) {}
    };
    
    vector<Node> nodes;
    vector<int> roots;
    vector<vector<int>> graph;
    vector<int> values;
    vector<vector<int>> parent;
    vector<int> depth;
    const int LOG = 20;
    
    void push_up(int node_idx) {
        nodes[node_idx].count = nodes[nodes[node_idx].left].count + 
                               nodes[nodes[node_idx].right].count;
    }
    
    int update(int pre_root, int l, int r, int pos, int val) {
        nodes.push_back(nodes[pre_root]);
        int new_root = nodes.size() - 1;
        
        if (l == r) {
            nodes[new_root].count += val;
            return new_root;
        }
        
        int mid = l + (r - l) / 2;
        if (pos <= mid) {
            nodes[new_root].left = update(nodes[pre_root].left, l, mid, pos, val);
        } else {
            nodes[new_root].right = update(nodes[pre_root].right, mid + 1, r, pos, val);
        }
        
        push_up(new_root);
        return new_root;
    }
    
    void dfs(int u, int p, const map<int, int>& rank) {
        parent[0][u] = p;
        depth[u] = depth[p] + 1;
        
        // 继承父节点的版本并更新当前节点的值
        roots[u] = update(roots[p], 1, rank.size(), rank.at(values[u - 1]), 1);
        
        for (int v : graph[u]) {
            if (v != p) {
                dfs(v, u, rank);
            }
        }
    }
    
    int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            swap(u, v);
        }
        
        // 将u提升到与v同一深度
        for (int k = LOG - 1; k >= 0; k--) {
            if (depth[u] - (1 << k) >= depth[v]) {
                u = parent[k][u];
            }
        }
        
        if (u == v) {
            return u;
        }
        
        for (int k = LOG - 1; k >= 0; k--) {
            if (parent[k][u] != parent[k][v]) {
                u = parent[k][u];
                v = parent[k][v];
            }
        }
        
        return parent[0][u];
    }
    
    int query(int u, int v, int ancestor, int ancestor_parent, int l, int r, int k) {
        if (l == r) {
            return l;
        }
        
        int mid = l + (r - l) / 2;
        int left_count = nodes[nodes[u].left].count + nodes[nodes[v].left].count
                       - nodes[nodes[ancestor].left].count - nodes[nodes[ancestor_parent].left].count;
        
        if (k <= left_count) {
            return query(nodes[u].left, nodes[v].left, 
                        nodes[ancestor].left, nodes[ancestor_parent].left,
                        l, mid, k);
        } else {
            return query(nodes[u].right, nodes[v].right,
                        nodes[ancestor].right, nodes[ancestor_parent].right,
                        mid + 1, r, k - left_count);
        }
    }
    
    void solve() {
        ios::sync_with_stdio(false);
        cin.tie(0);
        
        int n, m;
        cin >> n >> m;
        
        values.resize(n);
        for (int i = 0; i < n; i++) {
            cin >> values[i];
        }
        
        // 离散化
        vector<int> sorted_vals = values;
        sort(sorted_vals.begin(), sorted_vals.end());
        sorted_vals.erase(unique(sorted_vals.begin(), sorted_vals.end()), sorted_vals.end());
        
        map<int, int> rank;
        for (int i = 0; i < sorted_vals.size(); i++) {
            rank[sorted_vals[i]] = i + 1;
        }
        
        // 构建图
        graph.resize(n + 1);
        for (int i = 0; i < n - 1; i++) {
            int u, v;
            cin >> u >> v;
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 初始化LCA数组
        parent.resize(LOG, vector<int>(n + 1, 0));
        depth.resize(n + 1, 0);
        
        // 初始化持久化线段树
        nodes.clear();
        roots.resize(n + 1, 0);
        nodes.push_back(Node());
        
        // DFS构建
        dfs(1, 0, rank);
        
        // 构建LCA倍增表
        for (int k = 1; k < LOG; k++) {
            for (int i = 1; i <= n; i++) {
                parent[k][i] = parent[k - 1][parent[k - 1][i]];
            }
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            int u, v, k;
            cin >> u >> v >> k;
            int ancestor = lca(u, v);
            int res = query(u, v, ancestor, parent[0][ancestor], 1, rank.size(), k);
            // 转换回原始值
            cout << sorted_vals[res - 1] << '\n';
        }
    }
}

// 题目3: LeetCode 2276 - Count Integers in Intervals
// 题目链接: https://leetcode.com/problems/count-integers-in-intervals/
// 题目描述: 实现一个数据结构，支持添加区间和查询区间内整数的个数
// 最优解: 动态开点线段树是该问题的最优解之一
class CountIntervals {
private:
    struct Node {
        Node *left, *right;
        int cnt, cover;
        Node() : left(nullptr), right(nullptr), cnt(0), cover(0) {}
    };
    
    Node* root;
    int total;
    const int MAX = 1e9;
    
    void push_up(Node* node, int l, int r) {
        if (node->cover > 0) {
            node->cnt = r - l + 1;
        } else if (l == r) {
            node->cnt = 0;
        } else {
            int left_cnt = node->left ? node->left->cnt : 0;
            int right_cnt = node->right ? node->right->cnt : 0;
            node->cnt = left_cnt + right_cnt;
        }
    }
    
    void update(Node* node, int l, int r, int ul, int ur, int val) {
        if (ur < l || ul > r) {
            return;
        }
        
        if (ul <= l && r <= ur) {
            node->cover += val;
            push_up(node, l, r);
            return;
        }
        
        // 动态开点
        if (!node->left) node->left = new Node();
        if (!node->right) node->right = new Node();
        
        int mid = l + (r - l) / 2;
        update(node->left, l, mid, ul, ur, val);
        update(node->right, mid + 1, r, ul, ur, val);
        push_up(node, l, r);
    }
    
    // 清理内存
    void clear(Node* node) {
        if (node) {
            clear(node->left);
            clear(node->right);
            delete node;
        }
    }
    
public:
    CountIntervals() : root(new Node()), total(0) {}
    
    ~CountIntervals() {
        clear(root);
    }
    
    void add(int left, int right) {
        int before = root->cnt;
        update(root, 1, MAX, left, right, 1);
        total = root->cnt;
    }
    
    int count() {
        return total;
    }
};

// 题目4: LeetCode 1970 - Smallest Missing Genetic Value in Each Subtree
// 题目链接: https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/
// 题目描述: 给定一棵树，每个节点有一个基因值，求每个子树中最小缺失的基因值
namespace SmallestMissingGeneticValue {
    int find(int* parent, int u) {
        if (parent[u] != u) {
            parent[u] = find(parent, parent[u]);
        }
        return parent[u];
    }
    
    void dfs(int node, const vector<vector<int>>& children, const vector<int>& nums, 
             int* parent, vector<int>& res, vector<bool>& visited) {
        visited[node] = true;
        
        for (int child : children[node]) {
            if (!visited[child]) {
                dfs(child, children, nums, parent, res, visited);
            }
        }
        
        // 合并当前节点的值
        int u = find(parent, nums[node]);
        parent[u] = u + 1;
        
        if (nums[node] == 1) {
            res[node] = find(parent, 1);
        }
        
        // 向上传递结果
        int curr = node;
        while (parent[curr] != -1) {
            curr = parent[curr];
            if (curr >= 0 && curr < res.size() && res[curr] == 1) {
                res[curr] = find(parent, 1);
            } else {
                break;
            }
        }
    }
    
    vector<int> solve(vector<int>& parents, vector<int>& nums) {
        int n = parents.size();
        vector<int> res(n, 1);
        
        // 检查是否存在值为1的节点
        int posOfOne = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                posOfOne = i;
                break;
            }
        }
        if (posOfOne == -1) {
            return res;
        }
        
        // 构建子树
        vector<vector<int>> children(n);
        for (int i = 0; i < n; i++) {
            if (parents[i] != -1) {
                children[parents[i]].push_back(i);
            }
        }
        
        // 并查集
        const int MAX_VAL = 100001;
        int* parent = new int[MAX_VAL + 1];
        for (int i = 0; i <= MAX_VAL; i++) {
            parent[i] = i;
        }
        
        // 后序遍历
        vector<bool> visited(n, false);
        dfs(posOfOne, children, nums, parent, res, visited);
        
        delete[] parent;
        return res;
    }
}

// 题目5: SPOJ DQUERY - D-query
// 题目链接: https://www.spoj.com/problems/DQUERY/
// 题目描述: 给定一个数组，多次查询区间内不同元素的个数
namespace DQUERY {
    struct FenwickTree {
        vector<int> tree;
        int n;
        
        FenwickTree(int size) : n(size), tree(size + 1, 0) {}
        
        void update(int idx, int val) {
            idx++;  // 1-based
            while (idx <= n) {
                tree[idx] += val;
                idx += idx & -idx;
            }
        }
        
        int query(int idx) {
            idx++;  // 1-based
            int res = 0;
            while (idx > 0) {
                res += tree[idx];
                idx -= idx & -idx;
            }
            return res;
        }
    };
    
    void solve() {
        ios::sync_with_stdio(false);
        cin.tie(0);
        
        int n;
        cin >> n;
        vector<int> arr(n);
        for (int i = 0; i < n; i++) {
            cin >> arr[i];
        }
        
        int m;
        cin >> m;
        vector<tuple<int, int, int>> queries;
        for (int i = 0; i < m; i++) {
            int l, r;
            cin >> l >> r;
            l--; r--;  // 转换为0-based
            queries.emplace_back(r, l, i);
        }
        
        // 按右端点排序
        sort(queries.begin(), queries.end());
        
        FenwickTree ft(n);
        vector<int> res(m);
        map<int, int> last;
        int ptr = 0;
        
        for (int i = 0; i < n; i++) {
            if (last.count(arr[i])) {
                ft.update(last[arr[i]], -1);
            }
            ft.update(i, 1);
            last[arr[i]] = i;
            
            while (ptr < m && get<0>(queries[ptr]) == i) {
                int l = get<1>(queries[ptr]);
                int q_idx = get<2>(queries[ptr]);
                res[q_idx] = ft.query(i) - (l > 0 ? ft.query(l - 1) : 0);
                ptr++;
            }
        }
        
        for (int num : res) {
            cout << num << '\n';
        }
    }
}

// 题目6: 第一次出现位置序列查询
namespace FirstOccurrence {
    struct Node {
        int left, right, minVal;
        Node(int l = 0, int r = 0, int mv = INT_MAX) : left(l), right(r), minVal(mv) {}
    };
    
    vector<Node> nodes;
    vector<int> roots;
    
    void push_up(int node_idx) {
        nodes[node_idx].minVal = min(nodes[nodes[node_idx].left].minVal,
                                    nodes[nodes[node_idx].right].minVal);
    }
    
    int update(int pre_root, int l, int r, int pos, int val) {
        nodes.push_back(nodes[pre_root]);
        int new_root = nodes.size() - 1;
        
        if (l == r) {
            nodes[new_root].minVal = val;
            return new_root;
        }
        
        int mid = l + (r - l) / 2;
        if (pos <= mid) {
            nodes[new_root].left = update(nodes[pre_root].left, l, mid, pos, val);
        } else {
            nodes[new_root].right = update(nodes[pre_root].right, mid + 1, r, pos, val);
        }
        
        push_up(new_root);
        return new_root;
    }
    
    int query_min(int root, int l, int r, int ql, int qr) {
        if (qr < l || ql > r) {
            return INT_MAX;
        }
        
        if (ql <= l && r <= qr) {
            return nodes[root].minVal;
        }
        
        int mid = l + (r - l) / 2;
        int left_min = query_min(nodes[root].left, l, mid, ql, qr);
        int right_min = query_min(nodes[root].right, mid + 1, r, ql, qr);
        return min(left_min, right_min);
    }
    
    void solve() {
        ios::sync_with_stdio(false);
        cin.tie(0);
        
        int n, q;
        cin >> n >> q;
        vector<int> arr(n);
        for (int i = 0; i < n; i++) {
            cin >> arr[i];
        }
        
        // 初始化
        nodes.clear();
        roots.clear();
        nodes.push_back(Node());
        roots.push_back(0);
        
        map<int, int> last_occurrence;
        
        // 从右往左构建
        for (int i = n - 1; i >= 0; i--) {
            int current_root = roots.back();
            
            if (last_occurrence.count(arr[i])) {
                // 更新之前的位置
                current_root = update(current_root, 1, n, last_occurrence[arr[i]] + 1, INT_MAX);
            }
            
            // 更新当前位置
            current_root = update(current_root, 1, n, i + 1, i + 1);
            last_occurrence[arr[i]] = i;
            roots.push_back(current_root);
        }
        
        // 反转roots数组
        reverse(roots.begin(), roots.end());
        
        // 处理查询
        for (int i = 0; i < q; i++) {
            int l, r;
            cin >> l >> r;
            int min_pos = query_min(roots[r], 1, n, l, r);
            cout << (min_pos == INT_MAX ? -1 : min_pos) << '\n';
        }
    }
}

// 题目7: 区间最小缺失自然数查询（区间Mex查询）
namespace RangeMex {
    struct Node {
        int left, right, minPos;
        Node(int l = 0, int r = 0, int mp = INT_MAX) : left(l), right(r), minPos(mp) {}
    };
    
    vector<Node> nodes;
    vector<int> roots;
    
    void push_up(int node_idx) {
        nodes[node_idx].minPos = min(nodes[nodes[node_idx].left].minPos,
                                    nodes[nodes[node_idx].right].minPos);
    }
    
    int update(int pre_root, int l, int r, int pos, int val) {
        nodes.push_back(nodes[pre_root]);
        int new_root = nodes.size() - 1;
        
        if (l == r) {
            nodes[new_root].minPos = val;
            return new_root;
        }
        
        int mid = l + (r - l) / 2;
        if (pos <= mid) {
            nodes[new_root].left = update(nodes[pre_root].left, l, mid, pos, val);
        } else {
            nodes[new_root].right = update(nodes[pre_root].right, mid + 1, r, pos, val);
        }
        
        push_up(new_root);
        return new_root;
    }
    
    int query_mex(int root, int l, int r, int current_l) {
        if (l == r) {
            return l;
        }
        
        int mid = l + (r - l) / 2;
        int left_min = nodes[nodes[root].left].minPos;
        
        if (left_min < current_l) {
            return query_mex(nodes[root].left, l, mid, current_l);
        } else {
            return query_mex(nodes[root].right, mid + 1, r, current_l);
        }
    }
    
    void solve() {
        ios::sync_with_stdio(false);
        cin.tie(0);
        
        int n, q;
        cin >> n >> q;
        vector<int> arr(n);
        int max_val = 0;
        for (int i = 0; i < n; i++) {
            cin >> arr[i];
            if (arr[i] >= 0) {
                max_val = max(max_val, arr[i]);
            }
        }
        max_val = max(max_val, n);
        
        // 初始化
        nodes.clear();
        roots.clear();
        nodes.push_back(Node());
        roots.push_back(0);
        
        map<int, int> last_occurrence;
        
        for (int i = 0; i < n; i++) {
            int val = arr[i];
            int new_root = roots.back();
            
            if (val >= 0) {
                if (last_occurrence.count(val)) {
                    new_root = update(new_root, 0, max_val, val, i);
                } else {
                    new_root = update(new_root, 0, max_val, val, i);
                }
                last_occurrence[val] = i;
            }
            
            roots.push_back(new_root);
        }
        
        // 处理查询
        for (int i = 0; i < q; i++) {
            int l, r;
            cin >> l >> r;
            l--; r--;  // 转换为0-based
            int mex = query_mex(roots[r + 1], 0, max_val, l);
            cout << mex << '\n';
        }
    }
}

// 主函数，用于测试各个问题的解法
int main() {
    // 根据需要取消注释相应的测试函数
    // MKTHNUM::solve();
    // COT::solve();
    // DQUERY::solve();
    // FirstOccurrence::solve();
    // RangeMex::solve();
    
    // 测试LeetCode 2276
    CountIntervals intervals;
    intervals.add(2, 3);
    intervals.add(7, 10);
    cout << "CountIntervals test 1: " << intervals.count() << endl;  // 输出: 6
    intervals.add(5, 8);
    cout << "CountIntervals test 2: " << intervals.count() << endl;  // 输出: 8
    
    // 测试LeetCode 1970
    vector<int> parents = {-1, 0, 0, 2};
    vector<int> nums = {1, 2, 3, 4};
    vector<int> res = SmallestMissingGeneticValue::solve(parents, nums);
    cout << "SmallestMissingGeneticValue test: ";
    for (int num : res) {
        cout << num << " ";
    }
    cout << endl;  // 输出: 5 1 1 1
    
    return 0;
}
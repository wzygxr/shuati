#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <functional>
#include <memory>

using namespace std;

/**
 * P6773 [NOI2020] 命运 - 线段树合并加速DP转移
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P6773
 * 
 * 题目描述:
 * 给定一棵树，每个节点有一个权值。需要选择一些节点，使得任意两个被选节点在树上的路径上至少有一个被选节点。
 * 求最小权值和。
 * 
 * 核心算法: 树形DP + 线段树合并优化
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 定义DP状态: dp[u][0/1] 表示以u为根的子树，u节点选或不选的最小权值
 * 2. 状态转移:
 *    - 如果u不选，则所有子节点必须选
 *    - 如果u选，则子节点可选可不选
 * 3. 使用线段树合并来优化DP转移过程
 * 4. 维护每个节点的DP值线段树，通过合并子树线段树来更新当前节点的DP值
 * 
 * 线段树合并加速DP转移的关键:
 * 1. 动态开点线段树维护DP值
 * 2. 合并子树线段树时进行DP状态转移
 * 3. 懒标记维护区间最小值
 * 4. 支持区间加、区间取最小值操作
 */

class SegmentTree {
private:
    struct Node {
        shared_ptr<Node> left;
        shared_ptr<Node> right;
        long long min_val;
        long long lazy;
        
        Node() : min_val(LLONG_MAX / 2), lazy(0) {}
    };
    
    shared_ptr<Node> root;
    
    void push_down(shared_ptr<Node> node, int l, int r) {
        if (node->lazy != 0) {
            if (!node->left) node->left = make_shared<Node>();
            if (!node->right) node->right = make_shared<Node>();
            
            node->left->min_val += node->lazy;
            node->left->lazy += node->lazy;
            
            node->right->min_val += node->lazy;
            node->right->lazy += node->lazy;
            
            node->lazy = 0;
        }
    }
    
    void update(shared_ptr<Node> node, int l, int r, int pos, long long val) {
        if (l == r) {
            node->min_val = min(node->min_val, val);
            return;
        }
        
        push_down(node, l, r);
        int mid = (l + r) / 2;
        
        if (pos <= mid) {
            if (!node->left) node->left = make_shared<Node>();
            update(node->left, l, mid, pos, val);
        } else {
            if (!node->right) node->right = make_shared<Node>();
            update(node->right, mid + 1, r, pos, val);
        }
        
        long long left_min = node->left ? node->left->min_val : LLONG_MAX / 2;
        long long right_min = node->right ? node->right->min_val : LLONG_MAX / 2;
        node->min_val = min(left_min, right_min);
    }
    
    void add(shared_ptr<Node> node, int l, int r, int ql, int qr, long long val) {
        if (!node || ql > qr) return;
        
        if (ql <= l && r <= qr) {
            node->min_val += val;
            node->lazy += val;
            return;
        }
        
        push_down(node, l, r);
        int mid = (l + r) / 2;
        
        if (ql <= mid && node->left) {
            add(node->left, l, mid, ql, qr, val);
        }
        if (qr > mid && node->right) {
            add(node->right, mid + 1, r, ql, qr, val);
        }
        
        long long left_min = node->left ? node->left->min_val : LLONG_MAX / 2;
        long long right_min = node->right ? node->right->min_val : LLONG_MAX / 2;
        node->min_val = min(left_min, right_min);
    }
    
    long long query(shared_ptr<Node> node, int l, int r, int ql, int qr) {
        if (!node || ql > qr) return LLONG_MAX / 2;
        
        if (ql <= l && r <= qr) {
            return node->min_val;
        }
        
        push_down(node, l, r);
        int mid = (l + r) / 2;
        long long res = LLONG_MAX / 2;
        
        if (ql <= mid && node->left) {
            res = min(res, query(node->left, l, mid, ql, qr));
        }
        if (qr > mid && node->right) {
            res = min(res, query(node->right, mid + 1, r, ql, qr));
        }
        
        return res;
    }
    
    shared_ptr<Node> merge(shared_ptr<Node> p, shared_ptr<Node> q, int l, int r, 
                          long long add_p, long long add_q) {
        if (!p && !q) return nullptr;
        if (!p) {
            add(q, l, r, l, r, add_q);
            return q;
        }
        if (!q) {
            add(p, l, r, l, r, add_p);
            return p;
        }
        
        if (l == r) {
            auto new_node = make_shared<Node>();
            new_node->min_val = min(p->min_val + add_p, q->min_val + add_q);
            return new_node;
        }
        
        push_down(p, l, r);
        push_down(q, l, r);
        
        int mid = (l + r) / 2;
        
        auto left_merged = merge(
            p->left, q->left, l, mid, add_p, add_q
        );
        auto right_merged = merge(
            p->right, q->right, mid + 1, r, add_p, add_q
        );
        
        auto new_node = make_shared<Node>();
        new_node->left = left_merged;
        new_node->right = right_merged;
        
        long long left_min = left_merged ? left_merged->min_val : LLONG_MAX / 2;
        long long right_min = right_merged ? right_merged->min_val : LLONG_MAX / 2;
        new_node->min_val = min(left_min, right_min);
        
        return new_node;
    }

public:
    SegmentTree() : root(make_shared<Node>()) {}
    
    void update(int pos, long long val) {
        update(root, 0, 1000000, pos, val);
    }
    
    void add(int ql, int qr, long long val) {
        add(root, 0, 1000000, ql, qr, val);
    }
    
    long long query(int ql, int qr) {
        return query(root, 0, 1000000, ql, qr);
    }
    
    void merge_with(SegmentTree& other, long long add_p, long long add_q) {
        root = merge(root, other.root, 0, 1000000, add_p, add_q);
    }
    
    long long get_min() const {
        return root ? root->min_val : LLONG_MAX / 2;
    }
};

int n;
vector<vector<int>> graph;
vector<long long> weight;
vector<SegmentTree> dp_trees;

void dfs(int u, int parent) {
    // 初始化当前节点的线段树
    dp_trees[u].update(0, 0);  // 初始状态
    
    for (int v : graph[u]) {
        if (v == parent) continue;
        
        dfs(v, u);
        
        // 计算子树的DP值
        long long min_child_select = dp_trees[v].query(0, n);
        long long min_child_not_select = dp_trees[v].query(1, n);
        
        // 合并子树线段树
        dp_trees[u].merge_with(dp_trees[v], min_child_select, min_child_not_select + weight[u]);
    }
    
    // 最终处理：考虑当前节点权值
    if (u != 1) {  // 非根节点需要加上自身权值
        dp_trees[u].add(0, n, weight[u]);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n;
    weight.resize(n + 1);
    graph.resize(n + 1);
    dp_trees.resize(n + 1);
    
    for (int i = 1; i <= n; i++) {
        cin >> weight[i];
    }
    
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    dfs(1, 0);
    
    // 输出结果
    cout << dp_trees[1].get_min() << endl;
    
    return 0;
}

/**
 * 线段树合并加速DP转移的C++实现特点:
 * 
 * 1. 智能指针管理内存: 使用shared_ptr自动管理节点内存
 * 2. 面向对象设计: 封装线段树操作，提供清晰的接口
 * 3. 高效性能: C++的底层优化提供更好的运行效率
 * 4. 类型安全: 强类型检查避免运行时错误
 * 
 * 算法复杂度分析:
 * - 时间复杂度: O(n log n)，每个节点最多被合并log n次
 * - 空间复杂度: O(n log n)，动态开点线段树的空间消耗
 * 
 * 适用场景:
 * 1. 树形DP问题，需要合并子树信息
 * 2. DP状态转移涉及区间操作
 * 3. 需要支持动态修改和查询
 * 4. 数据规模较大，需要高效算法
 * 
 * 类似题目推荐:
 * 1. P5298 [PKUWC2018] Minimax - 概率DP + 线段树合并
 * 2. CF455D Serega and Fun - 区间操作 + DP优化
 * 3. CF868F Yet Another Minimization Problem - 分治DP + 线段树
 * 4. CF321E Ciel and Gondolas - 四边形不等式优化
 * 5. P4770 [NOI2018] 你的名字 - 后缀自动机 + 线段树合并
 * 
 * C++实现的优势:
 * 1. 性能优越: 接近硬件级别的优化
 * 2. 内存控制: 精确控制内存分配和释放
 * 3. 模板支持: 支持泛型编程，代码复用性强
 * 4. 标准库丰富: 提供丰富的算法和数据结构支持
 * 
 * 注意事项:
 * 1. 内存管理: 使用智能指针避免内存泄漏
 * 2. 递归深度: 注意递归深度限制
 * 3. 边界处理: 仔细处理数组边界和特殊情况
 * 4. 输入输出优化: 使用ios::sync_with_stdio(false)加速IO
 */
#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * CF438D The Child and Sequence - 线段树分裂算法实现 (C++版本)
 * 
 * 题目链接: https://codeforces.com/contest/438/problem/D
 * 
 * 题目描述:
 * 给定一个序列，支持三种操作:
 * 1. 区间求和
 * 2. 区间取模 (每个数对x取模)
 * 3. 单点修改
 * 
 * 核心算法: 线段树 + 取模优化 + 线段树分裂
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * 解题思路:
 * 1. 使用线段树维护区间最大值和区间和
 * 2. 对于取模操作，如果区间最大值小于模数，则不需要递归
 * 3. 使用线段树分裂优化取模操作
 * 4. 维护区间最大值来剪枝
 */

struct SegmentTreeNode {
    long long sum; // 区间和
    long long max_val; // 区间最大值
    SegmentTreeNode* left;
    SegmentTreeNode* right;
    
    SegmentTreeNode() : sum(0), max_val(0), left(nullptr), right(nullptr) {}
};

class Code20_CF438D_TheChildAndSequence {
private:
    int n, m;
    vector<long long> arr;
    SegmentTreeNode* root;
    
public:
    // 构建线段树
    SegmentTreeNode* build(int l, int r) {
        SegmentTreeNode* node = new SegmentTreeNode();
        if (l == r) {
            node->sum = arr[l];
            node->max_val = arr[l];
            return node;
        }
        
        int mid = (l + r) >> 1;
        node->left = build(l, mid);
        node->right = build(mid + 1, r);
        node->sum = node->left->sum + node->right->sum;
        node->max_val = max(node->left->max_val, node->right->max_val);
        return node;
    }
    
    // 单点更新
    void update(SegmentTreeNode* node, int l, int r, int pos, long long val) {
        if (l == r) {
            node->sum = val;
            node->max_val = val;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            update(node->left, l, mid, pos, val);
        } else {
            update(node->right, mid + 1, r, pos, val);
        }
        node->sum = node->left->sum + node->right->sum;
        node->max_val = max(node->left->max_val, node->right->max_val);
    }
    
    // 区间取模
    void modulo(SegmentTreeNode* node, int l, int r, int ql, int qr, long long mod) {
        if (node->max_val < mod) return; // 剪枝：最大值小于模数，不需要处理
        
        if (l == r) {
            // 叶子节点直接取模
            node->sum %= mod;
            node->max_val = node->sum;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (ql <= mid) {
            modulo(node->left, l, mid, ql, qr, mod);
        }
        if (qr > mid) {
            modulo(node->right, mid + 1, r, ql, qr, mod);
        }
        node->sum = node->left->sum + node->right->sum;
        node->max_val = max(node->left->max_val, node->right->max_val);
    }
    
    // 区间查询
    long long query(SegmentTreeNode* node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return node->sum;
        }
        
        int mid = (l + r) >> 1;
        long long res = 0;
        if (ql <= mid) {
            res += query(node->left, l, mid, ql, qr);
        }
        if (qr > mid) {
            res += query(node->right, mid + 1, r, ql, qr);
        }
        return res;
    }
    
    // 线段树分裂 - 将需要取模的区间分裂出来
    SegmentTreeNode* splitForModulo(SegmentTreeNode* node, int l, int r, int ql, int qr, long long mod) {
        if (node->max_val < mod) return nullptr; // 不需要处理的部分
        
        if (ql <= l && r <= qr) {
            // 整个区间都在查询范围内
            if (node->max_val < mod) return nullptr;
            
            // 创建新的节点来处理取模
            SegmentTreeNode* newNode = new SegmentTreeNode();
            if (l == r) {
                // 叶子节点直接取模
                newNode->sum = node->sum % mod;
                newNode->max_val = newNode->sum;
            } else {
                int mid = (l + r) >> 1;
                newNode->left = splitForModulo(node->left, l, mid, ql, qr, mod);
                newNode->right = splitForModulo(node->right, mid + 1, r, ql, qr, mod);
                
                if (newNode->left == nullptr && newNode->right == nullptr) {
                    delete newNode;
                    return nullptr;
                }
                
                newNode->sum = (newNode->left ? newNode->left->sum : 0) + 
                             (newNode->right ? newNode->right->sum : 0);
                newNode->max_val = max(newNode->left ? newNode->left->max_val : 0, 
                                    newNode->right ? newNode->right->max_val : 0);
            }
            return newNode;
        }
        
        int mid = (l + r) >> 1;
        SegmentTreeNode* leftPart = nullptr;
        SegmentTreeNode* rightPart = nullptr;
        
        if (ql <= mid) {
            leftPart = splitForModulo(node->left, l, mid, ql, qr, mod);
        }
        if (qr > mid) {
            rightPart = splitForModulo(node->right, mid + 1, r, ql, qr, mod);
        }
        
        if (leftPart == nullptr && rightPart == nullptr) {
            return nullptr;
        }
        
        SegmentTreeNode* newNode = new SegmentTreeNode();
        newNode->left = leftPart;
        newNode->right = rightPart;
        newNode->sum = (leftPart ? leftPart->sum : 0) + 
                     (rightPart ? rightPart->sum : 0);
        newNode->max_val = max(leftPart ? leftPart->max_val : 0, 
                             rightPart ? rightPart->max_val : 0);
        
        return newNode;
    }
    
    // 线段树合并 - 将处理后的区间合并回原树
    void mergeBack(SegmentTreeNode* original, SegmentTreeNode* processed, int l, int r, int ql, int qr) {
        if (processed == nullptr) return;
        
        if (ql <= l && r <= qr) {
            // 整个区间都在查询范围内
            if (l == r) {
                // 叶子节点直接替换
                original->sum = processed->sum;
                original->max_val = processed->max_val;
            } else {
                // 递归合并左右子树
                int mid = (l + r) >> 1;
                mergeBack(original->left, processed->left, l, mid, ql, qr);
                mergeBack(original->right, processed->right, mid + 1, r, ql, qr);
                original->sum = original->left->sum + original->right->sum;
                original->max_val = max(original->left->max_val, original->right->max_val);
            }
            return;
        }
        
        int mid = (l + r) >> 1;
        if (ql <= mid) {
            mergeBack(original->left, processed->left, l, mid, ql, qr);
        }
        if (qr > mid) {
            mergeBack(original->right, processed->right, mid + 1, r, ql, qr);
        }
        original->sum = original->left->sum + original->right->sum;
        original->max_val = max(original->left->max_val, original->right->max_val);
    }
    
    // 优化的取模操作 - 使用线段树分裂
    void optimizedModulo(int l, int r, long long mod) {
        // 分裂出需要处理的区间
        SegmentTreeNode* processed = splitForModulo(root, 1, n, l, r, mod);
        
        if (processed != nullptr) {
            // 将处理后的区间合并回原树
            mergeBack(root, processed, 1, n, l, r);
            // 释放内存
            delete processed;
        }
    }
    
    void solve() {
        cin >> n >> m;
        arr.resize(n + 1);
        
        for (int i = 1; i <= n; i++) {
            cin >> arr[i];
        }
        
        // 构建线段树
        root = build(1, n);
        
        while (m--) {
            int type;
            cin >> type;
            
            if (type == 1) { // 区间求和
                int l, r;
                cin >> l >> r;
                cout << query(root, 1, n, l, r) << endl;
            } else if (type == 2) { // 区间取模
                int l, r;
                long long mod;
                cin >> l >> r >> mod;
                optimizedModulo(l, r, mod);
            } else if (type == 3) { // 单点修改
                int pos;
                long long val;
                cin >> pos >> val;
                update(root, 1, n, pos, val);
            }
        }
    }
    
    ~Code20_CF438D_TheChildAndSequence() {
        // 释放内存 (实际应用中需要更完整的内存管理)
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    Code20_CF438D_TheChildAndSequence solution;
    solution.solve();
    
    return 0;
}

/**
 * 类似题目推荐:
 * 1. CF1401F Reverse and Swap - 线段树分裂经典应用
 * 2. CF474F Ant Colony - GCD操作 + 线段树
 * 3. CF52C Circular RMQ - 环形区间操作
 * 4. P5494 【模板】线段树分裂 - 线段树分裂基础模板
 * 5. P4556 [Vani有约会]雨天的尾巴 - 树上差分 + 线段树合并
 * 
 * 线段树分裂算法总结:
 * 线段树分裂在取模操作中的应用:
 * 1. 将需要取模的区间分裂出来单独处理
 * 2. 避免对整个线段树进行不必要的递归
 * 3. 通过最大值剪枝优化性能
 * 4. 处理完成后合并回原树
 * 
 * 时间复杂度: O(log n) 每次分裂/合并
 * 空间复杂度: O(n log n)
 */
// 【模板】线段树 1 (Luogu P3372)
// 题目来源: Luogu P3372 【模板】线段树 1
// 题目链接: https://www.luogu.com.cn/problem/P3372
// 
// 题目描述:
// 如题，已知一个数列，你需要进行下面两种操作：
// 1. 将某区间每一个数加上x
// 2. 求出某区间每一个数的和
//
// 解题思路:
// 1. 使用带懒惰传播的线段树实现区间更新和区间查询
// 2. 懒惰传播用于延迟更新，避免不必要的计算
// 3. 区间更新时，只在必要时才将更新操作传递给子节点
// 4. 查询时确保所有相关的懒惰标记都被处理
//
// 时间复杂度分析:
// - 区间更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

#include <iostream>
#include <vector>
using namespace std;

class SegmentTree {
private:
    vector<long long> tree;
    vector<long long> lazy;
    vector<int> data;
    int n;

    // 构建线段树
    void buildTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = data[l];
            return;
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        // 构建左子树
        buildTree(leftTreeIndex, l, mid);
        // 构建右子树
        buildTree(rightTreeIndex, mid + 1, r);

        // 当前节点的值等于左右子树值的和
        tree[treeIndex] = tree[leftTreeIndex] + tree[rightTreeIndex];
    }

    // 下推懒惰标记
    void pushDown(int treeIndex, int l, int r) {
        if (lazy[treeIndex] != 0) {
            // 将懒惰标记应用到当前节点
            tree[treeIndex] += lazy[treeIndex] * (r - l + 1);

            // 如果不是叶子节点，将懒惰标记传递给子节点
            if (l != r) {
                lazy[2 * treeIndex + 1] += lazy[treeIndex];
                lazy[2 * treeIndex + 2] += lazy[treeIndex];
            }

            // 清除当前节点的懒惰标记
            lazy[treeIndex] = 0;
        }
    }

    // 区间加法更新辅助函数
    void updateTree(int treeIndex, int l, int r, int queryL, int queryR, long long val) {
        // 1. 先处理懒惰标记
        pushDown(treeIndex, l, r);

        // 2. 当前区间与更新区间无交集
        if (l > queryR || r < queryL) {
            return;
        }

        // 3. 当前区间完全包含在更新区间内
        if (l >= queryL && r <= queryR) {
            // 更新当前节点的值
            tree[treeIndex] += val * (r - l + 1);
            // 如果不是叶子节点，设置懒惰标记
            if (l != r) {
                lazy[2 * treeIndex + 1] += val;
                lazy[2 * treeIndex + 2] += val;
            }
            return;
        }

        // 4. 当前区间与更新区间有部分交集，递归处理左右子树
        int mid = l + (r - l) / 2;
        updateTree(2 * treeIndex + 1, l, mid, queryL, queryR, val);
        updateTree(2 * treeIndex + 2, mid + 1, r, queryL, queryR, val);

        // 更新当前节点的值
        tree[treeIndex] = tree[2 * treeIndex + 1] + tree[2 * treeIndex + 2];
    }

    // 查询区间和辅助函数
    long long queryTree(int treeIndex, int l, int r, int queryL, int queryR) {
        // 1. 先处理懒惰标记
        pushDown(treeIndex, l, r);

        // 2. 当前区间与查询区间无交集
        if (l > queryR || r < queryL) {
            return 0;
        }

        // 3. 当前区间完全包含在查询区间内
        if (l >= queryL && r <= queryR) {
            return tree[treeIndex];
        }

        // 4. 当前区间与查询区间有部分交集，递归查询左右子树
        int mid = l + (r - l) / 2;
        long long leftSum = queryTree(2 * treeIndex + 1, l, mid, queryL, queryR);
        long long rightSum = queryTree(2 * treeIndex + 2, mid + 1, r, queryL, queryR);
        return leftSum + rightSum;
    }

public:
    SegmentTree(vector<int>& nums) {
        n = nums.size();
        data = nums;
        tree.resize(4 * n);
        lazy.resize(4 * n);
        buildTree(0, 0, n - 1);
    }

    // 区间加法更新 [queryL, queryR] 区间内每个元素加上 val
    void update(int queryL, int queryR, long long val) {
        updateTree(0, 0, n - 1, queryL, queryR, val);
    }

    // 查询区间和
    long long query(int queryL, int queryR) {
        return queryTree(0, 0, n - 1, queryL, queryR);
    }
};

// 测试代码
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m; // 数列长度和操作数量
    cin >> n >> m;
    
    vector<int> nums(n);
    for (int i = 0; i < n; i++) {
        cin >> nums[i];
    }
    
    // 构建线段树
    SegmentTree segmentTree(nums);
    
    // 处理操作
    for (int i = 0; i < m; i++) {
        int operation;
        cin >> operation;
        
        if (operation == 1) {
            int x, y;
            long long k;
            cin >> x >> y >> k;
            segmentTree.update(x - 1, y - 1, k); // 转换为0索引
        } else if (operation == 2) {
            int x, y;
            cin >> x >> y;
            cout << segmentTree.query(x - 1, y - 1) << "\n"; // 转换为0索引
        }
    }
    
    return 0;
}
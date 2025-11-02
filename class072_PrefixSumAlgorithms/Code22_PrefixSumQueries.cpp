// Prefix Sum Queries (CSES 2166)
// 
// 题目描述:
// 给定一个数组，支持两种操作：
// 1. 更新位置k的值为u
// 2. 查询区间[a,b]内的最大前缀和
// 
// 示例:
// 输入:
// 5 3
// 2 2 2 2 2
// 2 1 5
// 1 3 4
// 2 1 5
// 输出:
// 10
// 12
// 
// 提示:
// 1 <= n, q <= 2 * 10^5
// -10^9 <= x <= 10^9
// 
// 题目链接: https://cses.fi/problemset/task/2166
// 
// 解题思路:
// 使用线段树维护区间信息，每个节点存储：
// 1. 区间和
// 2. 区间最大前缀和
// 3. 区间最大后缀和
// 4. 区间最大子段和
// 
// 时间复杂度: 
// - 初始化: O(n) - 需要遍历整个数组构建线段树
// - 更新: O(log n) - 每次更新操作的时间复杂度
// - 查询: O(log n) - 每次查询操作的时间复杂度
// 空间复杂度: O(n) - 线段树需要额外的空间
// 
// 工程化考量:
// 1. 边界条件处理：空数组、单元素数组
// 2. 性能优化：使用线段树提供高效的区间查询和更新
// 3. 数据结构选择：线段树适合频繁的区间操作
// 4. 大数处理：元素值可能很大，需要确保整数范围
// 
// 最优解分析:
// 这是最优解，因为需要支持动态更新和查询操作，线段树提供了O(log n)的时间复杂度。
// 对于频繁的区间操作，线段树是最佳选择。
// 
// 算法核心:
// 线段树的合并操作：
// - 区间和 = 左子树区间和 + 右子树区间和
// - 区间最大前缀和 = max(左子树最大前缀和, 左子树区间和 + 右子树最大前缀和)
// - 区间最大后缀和 = max(右子树最大后缀和, 右子树区间和 + 左子树最大后缀和)
// - 区间最大子段和 = max(左子树最大子段和, 右子树最大子段和, 左子树最大后缀和 + 右子树最大前缀和)
// 
// 算法调试技巧:
// 1. 打印中间过程：显示线段树的构建和更新过程
// 2. 边界测试：测试空数组、单元素数组等特殊情况
// 3. 性能测试：测试大规模数组下的性能表现
// 
// 语言特性差异:
// C++中数组需要手动管理内存。
// 与Java相比，C++需要手动释放内存。
// 与Python相比，C++是静态类型语言，需要显式声明类型。

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

// 线段树节点结构
struct SegmentTreeNode {
    long long sum;           // 区间和
    long long maxPrefixSum;  // 区间最大前缀和
    long long maxSuffixSum;  // 区间最大后缀和
    long long maxSubarraySum; // 区间最大子段和
    
    SegmentTreeNode() : sum(0), maxPrefixSum(0), maxSuffixSum(0), maxSubarraySum(0) {}
};

class SegmentTree {
private:
    vector<SegmentTreeNode> tree;
    vector<long long> arr;
    int n;
    
    // 构建线段树
    void build(int node, int start, int end) {
        // 叶子节点
        if (start == end) {
            tree[node].sum = arr[start];
            tree[node].maxPrefixSum = arr[start];
            tree[node].maxSuffixSum = arr[start];
            tree[node].maxSubarraySum = arr[start];
            return;
        }
        
        int mid = (start + end) / 2;
        // 递归构建左右子树
        build(2 * node, start, mid);
        build(2 * node + 1, mid + 1, end);
        
        // 合并左右子树的信息
        merge(node);
    }
    
    // 合并左右子树的信息
    void merge(int node) {
        int leftChild = 2 * node;
        int rightChild = 2 * node + 1;
        
        // 区间和 = 左子树区间和 + 右子树区间和
        tree[node].sum = tree[leftChild].sum + tree[rightChild].sum;
        
        // 区间最大前缀和 = max(左子树最大前缀和, 左子树区间和 + 右子树最大前缀和)
        tree[node].maxPrefixSum = max(
            tree[leftChild].maxPrefixSum,
            tree[leftChild].sum + tree[rightChild].maxPrefixSum
        );
        
        // 区间最大后缀和 = max(右子树最大后缀和, 右子树区间和 + 左子树最大后缀和)
        tree[node].maxSuffixSum = max(
            tree[rightChild].maxSuffixSum,
            tree[rightChild].sum + tree[leftChild].maxSuffixSum
        );
        
        // 区间最大子段和 = max(左子树最大子段和, 右子树最大子段和, 左子树最大后缀和 + 右子树最大前缀和)
        tree[node].maxSubarraySum = max(
            max(tree[leftChild].maxSubarraySum, tree[rightChild].maxSubarraySum),
            tree[leftChild].maxSuffixSum + tree[rightChild].maxPrefixSum
        );
    }
    
    // 更新线段树中的值
    void update(int node, int start, int end, int index, long long value) {
        // 叶子节点
        if (start == end) {
            tree[node].sum = value;
            tree[node].maxPrefixSum = value;
            tree[node].maxSuffixSum = value;
            tree[node].maxSubarraySum = value;
            return;
        }
        
        int mid = (start + end) / 2;
        // 根据索引决定更新左子树还是右子树
        if (index <= mid) {
            update(2 * node, start, mid, index, value);
        } else {
            update(2 * node + 1, mid + 1, end, index, value);
        }
        
        // 更新后重新合并信息
        merge(node);
    }
    
    // 查询区间[left, right]内的最大前缀和
    long long queryMaxPrefixSum(int node, int start, int end, int left, int right) {
        // 完全不在查询区间内
        if (start > right || end < left) {
            return LLONG_MIN;
        }
        
        // 完全在查询区间内
        if (start >= left && end <= right) {
            return tree[node].maxPrefixSum;
        }
        
        int mid = (start + end) / 2;
        // 递归查询左右子树
        long long leftResult = queryMaxPrefixSum(2 * node, start, mid, left, right);
        long long rightResult = queryMaxPrefixSum(2 * node + 1, mid + 1, end, left, right);
        
        // 返回较大值
        return max(leftResult, rightResult);
    }
    
public:
    // 构造函数，初始化线段树
    SegmentTree(const vector<long long>& array) {
        this->n = array.size();
        this->arr = array;
        // 线段树数组大小通常为4*n
        this->tree.resize(4 * n);
        // 构建线段树
        build(1, 0, n - 1);
    }
    
    // 更新数组中某个位置的值
    void update(int index, long long value) {
        arr[index] = value;
        update(1, 0, n - 1, index, value);
    }
    
    // 查询区间[0, end]内的最大前缀和
    long long queryMaxPrefixSum(int end) {
        return queryMaxPrefixSum(1, 0, n - 1, 0, end);
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, q;
    cin >> n >> q;
    
    vector<long long> arr(n);
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }
    
    SegmentTree segTree(arr);
    
    for (int i = 0; i < q; i++) {
        int type;
        cin >> type;
        
        if (type == 1) {
            // 更新操作
            int k;
            long long u;
            cin >> k >> u;
            segTree.update(k - 1, u);  // 转换为0-based索引
        } else {
            // 查询操作
            int a, b;
            cin >> a >> b;
            long long result = segTree.queryMaxPrefixSum(b - 1);  // 转换为0-based索引
            cout << result << "\n";
        }
    }
    
    return 0;
}
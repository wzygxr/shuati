// Can you answer these queries I (GSS1)
// 题目来源: SPOJ GSS1 - Can you answer these queries I
// 题目链接: https://www.spoj.com/problems/GSS1/
// 
// 题目描述:
// 给定一个长度为N的整数序列A，需要处理M个查询。
// 每个查询给定两个整数X和Y，要求找出从第X个数到第Y个数之间的子段的最大和。
// 子段是指连续的一段数，空子段的和为0。
//
// 解题思路:
// 1. 使用线段树维护区间最大子段和
// 2. 每个节点需要维护以下信息：
//    - lSum: 以左端点为起点的最大子段和
//    - rSum: 以右端点为终点的最大子段和
//    - sum: 区间和
//    - maxSum: 区间最大子段和
// 3. 合并左右子树时，父节点的信息由左右子树的信息计算得出
//
// 时间复杂度分析:
// - 构建线段树: O(n)
// - 查询: O(log n)
// 空间复杂度: O(n)

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

struct SegmentTreeNode {
    int lSum;   // 以左端点为起点的最大子段和
    int rSum;   // 以右端点为终点的最大子段和
    int sum;    // 区间和
    int maxSum; // 区间最大子段和
    
    SegmentTreeNode() : lSum(0), rSum(0), sum(0), maxSum(0) {}
    
    SegmentTreeNode(int lSum, int rSum, int sum, int maxSum) 
        : lSum(lSum), rSum(rSum), sum(sum), maxSum(maxSum) {}
};

class SegmentTree {
private:
    vector<SegmentTreeNode> tree;
    vector<int> data;
    int n;

    // 构建线段树
    void buildTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = SegmentTreeNode(
                data[l], 
                data[l], 
                data[l], 
                data[l]
            );
            return;
        }
        
        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;
        
        // 构建左子树
        buildTree(leftTreeIndex, l, mid);
        // 构建右子树
        buildTree(rightTreeIndex, mid + 1, r);
        
        // 合并左右子树的信息
        tree[treeIndex] = merge(tree[leftTreeIndex], tree[rightTreeIndex]);
    }
    
    // 合并两个节点的信息
    SegmentTreeNode merge(SegmentTreeNode left, SegmentTreeNode right) {
        int sum = left.sum + right.sum;
        int lSum = max(left.lSum, left.sum + right.lSum);
        int rSum = max(right.rSum, right.sum + left.rSum);
        int maxSum = max(max(left.maxSum, right.maxSum), left.rSum + right.lSum);
        
        return SegmentTreeNode(lSum, rSum, sum, maxSum);
    }
    
    SegmentTreeNode queryTree(int treeIndex, int l, int r, int queryL, int queryR) {
        if (l == queryL && r == queryR) {
            return tree[treeIndex];
        }
        
        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;
        
        if (queryR <= mid) {
            // 查询区间完全在左子树
            return queryTree(leftTreeIndex, l, mid, queryL, queryR);
        } else if (queryL > mid) {
            // 查询区间完全在右子树
            return queryTree(rightTreeIndex, mid + 1, r, queryL, queryR);
        } else {
            // 查询区间跨越左右子树
            SegmentTreeNode leftResult = queryTree(leftTreeIndex, l, mid, queryL, mid);
            SegmentTreeNode rightResult = queryTree(rightTreeIndex, mid + 1, r, mid + 1, queryR);
            return merge(leftResult, rightResult);
        }
    }

public:
    SegmentTree(vector<int>& nums) {
        n = nums.size();
        data = nums;
        tree.resize(4 * n);
        buildTree(0, 0, n - 1);
    }
    
    // 查询区间最大子段和
    int query(int queryL, int queryR) {
        if (n == 0) return 0;
        return queryTree(0, 0, n - 1, queryL, queryR).maxSum;
    }
};

// 测试代码
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取序列长度
    int n;
    cin >> n;
    vector<int> nums(n);
    
    // 读取序列
    for (int i = 0; i < n; i++) {
        cin >> nums[i];
    }
    
    // 构建线段树
    SegmentTree segmentTree(nums);
    
    // 读取查询数量
    int m;
    cin >> m;
    
    // 处理查询
    for (int i = 0; i < m; i++) {
        int x, y;
        cin >> x >> y;
        // 转换为0索引
        x--;
        y--;
        cout << segmentTree.query(x, y) << "\n";
    }
    
    return 0;
}
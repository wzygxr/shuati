/**
 * AtCoder ABC185F. Range Xor Query (C++版本)
 * 题目链接: https://atcoder.jp/contests/abc185/tasks/abc185_f
 * 题目描述: 给定一个数组，支持两种操作：
 * 1. 更新数组中某个位置的值
 * 2. 查询区间[l,r]内所有元素的异或值
 *
 * 解题思路:
 * 使用线段树实现区间异或查询和单点更新操作
 * 1. 线段树每个节点存储对应区间的异或值
 * 2. 利用异或的性质：a ^ a = 0, a ^ 0 = a
 * 
 * 时间复杂度分析:
 * - 构建线段树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(4n) 线段树需要约4n的空间
 *
 * 算法详解:
 * 线段树是一种二叉树数据结构，每个节点代表数组的一个区间。对于区间异或查询问题，
 * 线段树的每个节点存储其对应区间的异或值。通过递归地将区间划分为两部分，我们可以
 * 高效地处理区间查询和单点更新操作。
 *
 * 异或运算性质:
 * 1. 交换律: a ^ b = b ^ a
 * 2. 结合律: (a ^ b) ^ c = a ^ (b ^ c)
 * 3. 自反性: a ^ a = 0
 * 4. 恒等性: a ^ 0 = a
 * 5. 逆运算: a ^ b = c 等价于 a ^ c = b
 *
 * 线段树结构:
 * 1. 每个节点代表一个区间[l,r]
 * 2. 叶子节点代表单个元素
 * 3. 非叶子节点的值等于其左右子节点值的异或
 */

#include <vector>
using namespace std;

class SegmentTreeXOR {
private:
    vector<int> tree;    // 线段树数组，存储各区间异或值
    vector<int> nums;    // 原始数组的副本
    int n;               // 数组大小
    
    /**
     * 构建线段树
     * @param node 当前节点在线段树数组中的索引
     * @param start 当前节点所代表区间的起始位置
     * @param end 当前节点所代表区间的结束位置
     */
    void build(int node, int start, int end) {
        // 如果是叶子节点，直接存储数组元素
        if (start == end) {
            tree[node] = nums[start];
        } else {
            // 计算中点，将区间分为两部分
            int mid = (start + end) / 2;
            // 递归构建左子树
            build(2 * node, start, mid);
            // 递归构建右子树
            build(2 * node + 1, mid + 1, end);
            // 当前节点的值等于左右子节点值的异或
            tree[node] = tree[2 * node] ^ tree[2 * node + 1];
        }
    }
    
    /**
     * 更新线段树中的值
     * @param node 当前节点在线段树数组中的索引
     * @param start 当前节点所代表区间的起始位置
     * @param end 当前节点所代表区间的结束位置
     * @param idx 要更新的数组元素索引
     * @param val 新的值
     */
    void update(int node, int start, int end, int idx, int val) {
        // 如果是叶子节点，直接更新
        if (start == end) {
            nums[idx] = val;
            tree[node] = val;
        } else {
            // 计算中点
            int mid = (start + end) / 2;
            // 根据索引位置决定更新左子树还是右子树
            if (idx <= mid) {
                update(2 * node, start, mid, idx, val);
            } else {
                update(2 * node + 1, mid + 1, end, idx, val);
            }
            // 更新当前节点的值
            tree[node] = tree[2 * node] ^ tree[2 * node + 1];
        }
    }
    
    /**
     * 查询线段树中指定区间的异或值
     * @param node 当前节点在线段树数组中的索引
     * @param start 当前节点所代表区间的起始位置
     * @param end 当前节点所代表区间的结束位置
     * @param l 查询区间的起始位置
     * @param r 查询区间的结束位置
     * @return 查询区间的异或值
     */
    int query(int node, int start, int end, int l, int r) {
        // 如果查询区间与当前节点区间无交集，返回0（异或的单位元）
        if (r < start || end < l) {
            return 0;
        }
        // 如果当前节点区间完全包含在查询区间内，直接返回当前节点的值
        if (l <= start && end <= r) {
            return tree[node];
        }
        // 计算中点
        int mid = (start + end) / 2;
        // 递归查询左右子树
        int p1 = query(2 * node, start, mid, l, r);
        int p2 = query(2 * node + 1, mid + 1, end, l, r);
        // 返回左右子树查询结果的异或值
        return p1 ^ p2;
    }
    
public:
    /**
     * 构造函数，初始化线段树
     * @param arr 输入数组
     */
    SegmentTreeXOR(vector<int>& arr) {
        nums = arr;
        n = arr.size();
        tree.resize(4 * n);  // 线段树数组大小通常设为4n
        build(1, 0, n - 1);  // 从根节点开始构建线段树
    }
    
    /**
     * 更新指定位置的值
     * @param idx 要更新的数组元素索引
     * @param val 新的值
     */
    void update(int idx, int val) {
        update(1, 0, n - 1, idx, val);
    }
    
    /**
     * 查询区间异或值
     * @param l 查询区间的起始位置
     * @param r 查询区间的结束位置
     * @return 查询区间的异或值
     */
    int xorRange(int l, int r) {
        return query(1, 0, n - 1, l, r);
    }
};

// 由于这是代码片段，不包含main函数和测试代码
// 在实际使用中，需要包含适当的头文件和main函数
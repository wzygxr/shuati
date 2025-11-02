// LeetCode 307. Range Sum Query - Mutable
// 题目描述：给定一个整数数组 nums，实现两个函数：
// 1. update(i, val)：将数组中索引 i 处的值更新为 val
// 2. sumRange(i, j)：返回数组中从索引 i 到 j 的元素之和
// 题目链接：https://leetcode.com/problems/range-sum-query-mutable/
// 解题思路：使用线段树或树状数组实现，本题同时提供两种实现方式

#include <iostream>
#include <vector>
#include <stdexcept>
#include <chrono>
using namespace std;
using namespace std::chrono;

/**
 * 使用线段树实现区间和查询与单点更新
 * 时间复杂度：
 * - 构建线段树：O(n)
 * - 单点更新：O(log n)
 * - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */
class Code15_RangeSumQueryMutable {
private:
    vector<int> tree; // 线段树数组
    int n;            // 原始数组长度

    /**
     * 构建线段树
     * @param nums 原始数组
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     */
    void buildTree(const vector<int>& nums, int node, int start, int end) {
        if (start == end) {
            // 叶子节点，直接赋值
            tree[node] = nums[start];
            return;
        }

        int mid = start + (end - start) / 2;
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;

        // 递归构建左右子树
        buildTree(nums, leftNode, start, mid);
        buildTree(nums, rightNode, mid + 1, end);

        // 当前节点的值为左右子节点之和
        tree[node] = tree[leftNode] + tree[rightNode];
    }

    /**
     * 单点更新（内部方法）
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param index 要更新的元素索引
     * @param val 新值
     */
    void updateTree(int node, int start, int end, int index, int val) {
        if (start == end) {
            // 到达叶子节点，更新值
            tree[node] = val;
            return;
        }

        int mid = start + (end - start) / 2;
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;

        // 根据index所在的区间决定递归左子树还是右子树
        if (index <= mid) {
            updateTree(leftNode, start, mid, index, val);
        } else {
            updateTree(rightNode, mid + 1, end, index, val);
        }

        // 更新当前节点的值
        tree[node] = tree[leftNode] + tree[rightNode];
    }

    /**
     * 区间查询（内部方法）
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param left 查询区间的左边界
     * @param right 查询区间的右边界
     * @return 查询区间的和
     */
    int queryTree(int node, int start, int end, int left, int right) {
        // 查询区间与当前区间无交集
        if (right < start || left > end) {
            return 0;
        }

        // 当前区间完全包含在查询区间内
        if (left <= start && end <= right) {
            return tree[node];
        }

        // 查询区间部分重叠，递归查询左右子树
        int mid = start + (end - start) / 2;
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;

        int leftSum = queryTree(leftNode, start, mid, left, right);
        int rightSum = queryTree(rightNode, mid + 1, end, left, right);

        return leftSum + rightSum;
    }

public:
    /**
     * 构造函数
     * @param nums 输入数组
     */
    Code15_RangeSumQueryMutable(const vector<int>& nums) {
        this->n = nums.size();
        // 线段树数组大小为4n，确保足够空间
        this->tree.resize(4 * this->n, 0);
        if (this->n > 0) {
            // 构建线段树
            buildTree(nums, 0, 0, this->n - 1);
        }
    }

    /**
     * 单点更新公共接口
     * @param index 要更新的元素索引
     * @param val 新值
     * @throws invalid_argument 当索引超出范围时
     */
    void update(int index, int val) {
        if (index < 0 || index >= n) {
            throw invalid_argument("索引超出范围");
        }
        updateTree(0, 0, n - 1, index, val);
    }

    /**
     * 区间和查询公共接口
     * @param left 查询区间的左边界
     * @param right 查询区间的右边界
     * @return 查询区间的和
     * @throws invalid_argument 当查询区间无效时
     */
    int sumRange(int left, int right) {
        if (left < 0 || right >= n || left > right) {
            throw invalid_argument("查询区间无效");
        }
        return queryTree(0, 0, n - 1, left, right);
    }
};

/**
 * 树状数组实现类
 * 树状数组(Binary Indexed Tree 或 Fenwick Tree)是一种高效处理前缀和查询和单点更新的数据结构
 * 时间复杂度：
 * - 构建树状数组：O(n log n)
 * - 单点更新：O(log n)
 * - 前缀和查询：O(log n)
 * - 区间和查询：O(log n)
 * 空间复杂度：O(n)
 */
class NumArrayBIT {
private:
    vector<int> bit;  // 树状数组
    vector<int> nums; // 原始数组
    int n;            // 数组长度

    /**
     * lowbit操作，获取x二进制表示中最低位的1所对应的值
     * @param x 输入整数
     * @return 最低位的1对应的值
     */
    int lowbit(int x) {
        return x & (-x);
    }

    /**
     * 单点更新（树状数组内部方法）
     * @param index 要更新的元素索引（0-based）
     * @param val 新值
     */
    void updateBIT(int index, int val) {
        // 将原数组中的增量累加到树状数组中
        int delta = val;
        index++; // 转换为1-based索引

        // 更新所有受影响的节点
        while (index <= n) {
            bit[index] += delta;
            index += lowbit(index);
        }
    }

    /**
     * 前缀和查询（树状数组内部方法）
     * @param index 查询到index的前缀和（0-based）
     * @return 前缀和
     */
    int prefixSum(int index) {
        index++; // 转换为1-based索引
        int sum = 0;

        // 累加所有包含的区间
        while (index > 0) {
            sum += bit[index];
            index -= lowbit(index);
        }

        return sum;
    }

public:
    /**
     * 构造函数
     * @param nums 输入数组
     */
    NumArrayBIT(const vector<int>& nums) {
        this->n = nums.size();
        this->nums = nums;
        this->bit.resize(this->n + 1, 0); // 树状数组从索引1开始

        // 初始化树状数组
        for (int i = 0; i < this->n; i++) {
            updateBIT(i, nums[i]);
        }
    }

    /**
     * 更新元素值（公共接口）
     * @param index 要更新的元素索引
     * @param val 新值
     * @throws invalid_argument 当索引超出范围时
     */
    void update(int index, int val) {
        if (index < 0 || index >= n) {
            throw invalid_argument("索引超出范围");
        }

        // 计算增量
        int delta = val - nums[index];
        nums[index] = val; // 更新原数组

        // 更新树状数组
        index++; // 转换为1-based索引
        while (index <= n) {
            bit[index] += delta;
            index += lowbit(index);
        }
    }

    /**
     * 区间和查询（公共接口）
     * @param left 区间左边界
     * @param right 区间右边界
     * @return 区间和
     * @throws invalid_argument 当查询区间无效时
     */
    int sumRange(int left, int right) {
        if (left < 0 || right >= n || left > right) {
            throw invalid_argument("查询区间无效");
        }

        // 区间和 = [0,right]的前缀和 - [0,left-1]的前缀和
        if (left == 0) {
            return prefixSum(right);
        } else {
            return prefixSum(right) - prefixSum(left - 1);
        }
    }
};

/**
 * 测试线段树实现
 */
void testSegmentTree() {
    cout << "=== 线段树实现测试 ===" << endl;

    // 测试用例1：基本操作
    vector<int> nums1 = {1, 3, 5, 7, 9, 11};
    Code15_RangeSumQueryMutable segTree(nums1);

    cout << "原始数组: [1, 3, 5, 7, 9, 11]" << endl;
    cout << "sumRange(0, 2) = " << segTree.sumRange(0, 2) << endl;  // 应为9 (1+3+5)
    segTree.update(1, 10);  // 将索引1的值从3更新为10
    cout << "更新索引1为10后" << endl;
    cout << "sumRange(0, 2) = " << segTree.sumRange(0, 2) << endl;  // 应为16 (1+10+5)
    cout << "sumRange(1, 5) = " << segTree.sumRange(1, 5) << endl;  // 应为42 (10+5+7+9+11)

    // 测试用例2：边界情况
    vector<int> nums2 = {5};
    Code15_RangeSumQueryMutable segTree2(nums2);
    cout << "\n测试边界情况" << endl;
    cout << "sumRange(0, 0) = " << segTree2.sumRange(0, 0) << endl;  // 应为5
    segTree2.update(0, 10);
    cout << "更新索引0为10后" << endl;
    cout << "sumRange(0, 0) = " << segTree2.sumRange(0, 0) << endl;  // 应为10

    // 测试用例3：空数组
    vector<int> nums3 = {};
    Code15_RangeSumQueryMutable segTree3(nums3);
    cout << "\n测试空数组" << endl;
    try {
        segTree3.sumRange(0, 0);
    } catch (const invalid_argument& e) {
        cout << "正确处理空数组异常: " << e.what() << endl;
    }
}

/**
 * 测试树状数组实现
 */
void testBIT() {
    cout << "\n=== 树状数组实现测试 ===" << endl;

    // 测试用例1：基本操作
    vector<int> nums1 = {1, 3, 5, 7, 9, 11};
    NumArrayBIT bit(nums1);

    cout << "原始数组: [1, 3, 5, 7, 9, 11]" << endl;
    cout << "sumRange(0, 2) = " << bit.sumRange(0, 2) << endl;  // 应为9 (1+3+5)
    bit.update(1, 10);  // 将索引1的值从3更新为10
    cout << "更新索引1为10后" << endl;
    cout << "sumRange(0, 2) = " << bit.sumRange(0, 2) << endl;  // 应为16 (1+10+5)
    cout << "sumRange(1, 5) = " << bit.sumRange(1, 5) << endl;  // 应为42 (10+5+7+9+11)

    // 测试用例2：边界情况
    vector<int> nums2 = {5};
    NumArrayBIT bit2(nums2);
    cout << "\n测试边界情况" << endl;
    cout << "sumRange(0, 0) = " << bit2.sumRange(0, 0) << endl;  // 应为5
    bit2.update(0, 10);
    cout << "更新索引0为10后" << endl;
    cout << "sumRange(0, 0) = " << bit2.sumRange(0, 0) << endl;  // 应为10
}

/**
 * 性能对比测试
 */
void performanceTest() {
    cout << "\n=== 性能对比测试 ===" << endl;

    // 创建较大的测试数组
    int size = 100000;
    vector<int> nums(size);
    for (int i = 0; i < size; i++) {
        nums[i] = i % 100;
    }

    // 测试线段树性能
    auto start = high_resolution_clock::now();
    Code15_RangeSumQueryMutable segTree(nums);
    auto end = high_resolution_clock::now();
    auto buildTime = duration_cast<milliseconds>(end - start).count();

    start = high_resolution_clock::now();
    for (int i = 0; i < 10000; i++) {
        segTree.update(i % size, (i % size) + 100);
        segTree.sumRange((i * 2) % size, ((i * 3) % size + 100) % size);
    }
    end = high_resolution_clock::now();
    auto segTreeTime = duration_cast<milliseconds>(end - start).count();

    // 测试树状数组性能
    start = high_resolution_clock::now();
    NumArrayBIT bit(nums);
    end = high_resolution_clock::now();
    auto bitBuildTime = duration_cast<milliseconds>(end - start).count();

    start = high_resolution_clock::now();
    for (int i = 0; i < 10000; i++) {
        bit.update(i % size, (i % size) + 100);
        bit.sumRange((i * 2) % size, ((i * 3) % size + 100) % size);
    }
    end = high_resolution_clock::now();
    auto bitTime = duration_cast<milliseconds>(end - start).count();

    cout << "数组大小: " << size << endl;
    cout << "线段树构建时间: " << buildTime << "ms" << endl;
    cout << "树状数组构建时间: " << bitBuildTime << "ms" << endl;
    cout << "线段树10000次操作时间: " << segTreeTime << "ms" << endl;
    cout << "树状数组10000次操作时间: " << bitTime << "ms" << endl;
}

/**
 * 主函数
 */
int main() {
    testSegmentTree();
    testBIT();
    performanceTest();
    
    return 0;
}

/**
 * 算法总结与比较
 * 
 * 1. 线段树 vs 树状数组:
 *    - 线段树功能更强大，可以处理更复杂的区间操作（如区间最大值、区间最小值等）
 *    - 树状数组代码更简洁，常数因子更小，空间效率更高
 *    - 树状数组更适合处理前缀和查询和单点更新
 *    - 线段树更适合处理多种类型的区间查询和更新
 * 
 * 2. 应用场景:
 *    - 树状数组适合: 前缀和查询、单点更新、逆序对计算等
 *    - 线段树适合: 区间最大值/最小值查询、区间和查询、区间更新等
 * 
 * 3. 时间复杂度分析:
 *    - 线段树和树状数组的单点更新和查询操作都是O(log n)
 *    - 线段树的区间更新可以是O(log n)（使用懒惰传播），而树状数组只能高效处理特定类型的区间更新
 * 
 * 4. 空间复杂度:
 *    - 线段树需要O(4n)的空间
 *    - 树状数组只需要O(n)的空间
 */
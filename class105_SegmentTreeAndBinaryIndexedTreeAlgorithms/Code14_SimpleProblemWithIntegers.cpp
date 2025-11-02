// POJ 3468 A Simple Problem with Integers
// 题目描述：给定一个长度为N的整数序列，执行以下操作：
// 1. C a b c: 将区间 [a,b] 中的每个数都加上c
// 2. Q a b: 查询区间 [a,b] 中所有数的和
// 题目链接：http://poj.org/problem?id=3468
// 解题思路：使用线段树 + 懒惰标记实现区间加法和区间求和查询

#include <iostream>
#include <vector>
#include <string>
using namespace std;

/**
 * 线段树实现区间加法和区间求和查询
 * 时间复杂度：
 * - 构建线段树：O(n)
 * - 区间更新：O(log n)
 * - 区间查询：O(log n)
 * 空间复杂度：O(n) - 线段树数组大小为4n
 */
class Code14_SimpleProblemWithIntegers {
private:
    vector<long long> tree_sum;  // 存储区间和的线段树数组
    vector<long long> tree_add;  // 存储懒惰标记的数组
    vector<long long> arr;       // 原始数组
    int n;                       // 数组长度

    /**
     * 构建线段树
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     */
    void _build(int node, int start, int end) {
        if (start == end) {
            // 叶子节点，直接赋值
            tree_sum[node] = arr[start];
            tree_add[node] = 0;
            return;
        }

        int mid = start + (end - start) / 2;
        int left_node = 2 * node + 1;
        int right_node = 2 * node + 2;

        // 递归构建左右子树
        _build(left_node, start, mid);
        _build(right_node, mid + 1, end);

        // 合并左右子树信息
        tree_sum[node] = tree_sum[left_node] + tree_sum[right_node];
        // 非叶子节点初始懒惰标记为0
        tree_add[node] = 0;
    }

    /**
     * 下传懒惰标记
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     */
    void _push_down(int node, int start, int end) {
        if (tree_add[node] != 0) {
            // 只有当懒惰标记不为0时需要下传
            int left_node = 2 * node + 1;
            int right_node = 2 * node + 2;
            int mid = start + (end - start) / 2;

            // 更新左子节点的区间和和懒惰标记
            tree_sum[left_node] += tree_add[node] * (mid - start + 1);
            tree_add[left_node] += tree_add[node];

            // 更新右子节点的区间和和懒惰标记
            tree_sum[right_node] += tree_add[node] * (end - mid);
            tree_add[right_node] += tree_add[node];

            // 清除当前节点的懒惰标记
            tree_add[node] = 0;
        }
    }

    /**
     * 区间更新（加法）
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param l 需要更新的区间左边界
     * @param r 需要更新的区间右边界
     * @param val 要增加的值
     */
    void _update_range(int node, int start, int end, int l, int r, long long val) {
        // 当前区间与目标区间无交集
        if (start > r || end < l) {
            return;
        }

        // 当前区间完全包含在目标区间内
        if (start >= l && end <= r) {
            // 更新区间和
            tree_sum[node] += val * (end - start + 1);
            // 更新懒惰标记
            tree_add[node] += val;
            return;
        }

        // 下传懒惰标记到子节点
        _push_down(node, start, end);

        int mid = start + (end - start) / 2;
        int left_node = 2 * node + 1;
        int right_node = 2 * node + 2;

        // 递归更新左右子树
        _update_range(left_node, start, mid, l, r, val);
        _update_range(right_node, mid + 1, end, l, r, val);

        // 更新当前节点的区间和
        tree_sum[node] = tree_sum[left_node] + tree_sum[right_node];
    }

    /**
     * 区间查询
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param l 查询的区间左边界
     * @param r 查询的区间右边界
     * @return 查询区间的和
     */
    long long _query_range(int node, int start, int end, int l, int r) {
        // 当前区间与查询区间无交集
        if (start > r || end < l) {
            return 0;
        }

        // 当前区间完全包含在查询区间内
        if (start >= l && end <= r) {
            return tree_sum[node];
        }

        // 下传懒惰标记到子节点
        _push_down(node, start, end);

        int mid = start + (end - start) / 2;
        int left_node = 2 * node + 1;
        int right_node = 2 * node + 2;

        // 递归查询左右子树
        long long left_sum = _query_range(left_node, start, mid, l, r);
        long long right_sum = _query_range(right_node, mid + 1, end, l, r);

        // 返回左右子树查询结果的和
        return left_sum + right_sum;
    }

public:
    /**
     * 构造函数
     * @param nums 输入数组
     */
    Code14_SimpleProblemWithIntegers(const vector<long long>& nums) {
        this->n = nums.size();
        this->arr = nums;
        // 线段树数组大小为4n，保证足够空间
        this->tree_sum.resize(4 * n, 0);
        this->tree_add.resize(4 * n, 0);
        // 构建线段树
        _build(0, 0, n - 1);
    }

    /**
     * 公共接口：区间更新
     * @param l 区间左边界（注意：这里是1-based索引）
     * @param r 区间右边界（注意：这里是1-based索引）
     * @param val 要增加的值
     */
    void update_range(int l, int r, long long val) {
        // 转换为0-based索引
        _update_range(0, 0, n - 1, l - 1, r - 1, val);
    }

    /**
     * 公共接口：区间查询
     * @param l 区间左边界（注意：这里是1-based索引）
     * @param r 区间右边界（注意：这里是1-based索引）
     * @return 查询区间的和
     */
    long long query_range(int l, int r) {
        // 转换为0-based索引
        return _query_range(0, 0, n - 1, l - 1, r - 1);
    }
};

/**
 * 主方法，用于处理输入输出
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, q;
    cin >> n >> q;

    vector<long long> arr(n);
    for (int i = 0; i < n; ++i) {
        cin >> arr[i];
    }

    Code14_SimpleProblemWithIntegers solution(arr);

    while (q--) {
        char op;
        int a, b;
        cin >> op >> a >> b;

        if (op == 'Q') {
            // 查询操作
            cout << solution.query_range(a, b) << '\n';
        } else if (op == 'C') {
            // 更新操作
            long long c;
            cin >> c;
            solution.update_range(a, b, c);
        }
    }

    return 0;
}

/**
 * 测试方法
 * 注意：在POJ上提交时应注释掉此函数，仅保留main函数
 */
void test() {
    // 测试用例1：基本操作测试
    vector<long long> nums1 = {1, 2, 3, 4, 5};
    Code14_SimpleProblemWithIntegers solution1(nums1);
    cout << "测试用例1:\n";
    cout << "初始数组: [1, 2, 3, 4, 5]\n";
    cout << "查询区间[1, 5]的和: " << solution1.query_range(1, 5) << endl;  // 应为15
    solution1.update_range(2, 4, 2);
    cout << "更新区间[2, 4]每个元素加2后，数组变为: [1, 4, 5, 6, 5]\n";
    cout << "查询区间[1, 5]的和: " << solution1.query_range(1, 5) << endl;  // 应为21
    cout << "查询区间[2, 4]的和: " << solution1.query_range(2, 4) << endl;  // 应为15

    // 测试用例2：边界情况测试
    vector<long long> nums2 = {10};
    Code14_SimpleProblemWithIntegers solution2(nums2);
    cout << "\n测试用例2:\n";
    cout << "初始数组: [10]\n";
    cout << "查询区间[1, 1]的和: " << solution2.query_range(1, 1) << endl;  // 应为10
    solution2.update_range(1, 1, -5);
    cout << "更新区间[1, 1]每个元素加-5后，数组变为: [5]\n";
    cout << "查询区间[1, 1]的和: " << solution2.query_range(1, 1) << endl;  // 应为5

    // 测试用例3：多次更新和查询测试
    vector<long long> nums3 = {0, 0, 0, 0, 0};
    Code14_SimpleProblemWithIntegers solution3(nums3);
    cout << "\n测试用例3:\n";
    cout << "初始数组: [0, 0, 0, 0, 0]\n";
    solution3.update_range(1, 5, 1);  // 所有元素加1
    solution3.update_range(2, 4, 2);  // 中间三个元素再加2
    solution3.update_range(3, 3, 3);  // 中间元素再加3
    cout << "多次更新后，数组变为: [1, 3, 6, 3, 1]\n";
    cout << "查询区间[1, 5]的和: " << solution3.query_range(1, 5) << endl;  // 应为14
    cout << "查询区间[1, 3]的和: " << solution3.query_range(1, 3) << endl;  // 应为10
    cout << "查询区间[3, 5]的和: " << solution3.query_range(3, 5) << endl;  // 应为10
}
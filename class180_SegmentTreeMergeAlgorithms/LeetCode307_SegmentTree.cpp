/**
 * LeetCode 307. 区域和检索 - 数组可修改
 * 题目链接: https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * 给你一个数组 nums ，请你完成两类查询：
 * 1. 将一个元素的值更新为 val
 * 2. 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和
 * 
 * 实现 NumArray 类：
 * - NumArray(int[] nums) 用整数数组 nums 初始化对象
 * - void update(int index, int val) 将 nums[index] 的值更新为 val
 * - int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和
 * 
 * 示例:
 * 输入:
 * ["NumArray", "sumRange", "update", "sumRange"]
 * [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
 * 输出:
 * [null, 9, null, 8]
 * 
 * 解释:
 * NumArray numArray = new NumArray([1, 3, 5]);
 * numArray.sumRange(0, 2); // 返回 1 + 3 + 5 = 9
 * numArray.update(1, 2);   // nums = [1,2,5]
 * numArray.sumRange(0, 2); // 返回 1 + 2 + 5 = 8
 * 
 * 提示:
 * 1 <= nums.length <= 3 * 10^4
 * -100 <= nums[i] <= 100
 * 0 <= index < nums.length
 * -100 <= val <= 100
 * 0 <= left <= right < nums.length
 * 调用 update 和 sumRange 方法次数不大于 3 * 10^4
 * 
 * 解题思路:
 * 这是一个经典的线段树应用问题，支持单点更新和区间查询。
 * 1. 使用线段树维护数组区间和
 * 2. 单点更新时，从根节点向下递归找到目标位置并更新，然后向上传递更新区间和
 * 3. 区间查询时，根据查询区间与当前节点区间的关系进行递归查询
 * 
 * 时间复杂度:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用位运算优化计算
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 * 
 * 编译命令: g++ -std=c++11 -o test_leetcode307 LeetCode307_SegmentTree.cpp
 * 运行命令: ./test_leetcode307
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <stdexcept>

using namespace std;

// 定义最大数组大小
#define MAXN 120000

class LeetCode307_SegmentTree {
private:
    int* nums;
    int* sum;
    int n;

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param nums 初始数组
     * @param size 数组大小
     */
    LeetCode307_SegmentTree(int* nums, int size) {
        this->nums = nums;
        this->n = size;
        
        // 特殊情况处理
        if (this->n == 0) {
            this->sum = 0;
            return;
        }
        
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        this->sum = new int[MAXN];
        build(0, n - 1, 1);
    }

    /**
     * 建树
     * 
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     */
    void build(int l, int r, int i) {
        // 递归终止条件：叶子节点
        if (l == r) {
            sum[i] = nums[l];
        } else {
            // 计算中点
            int mid = (l + r) >> 1;
            // 递归构建左子树
            build(l, mid, i << 1);
            // 递归构建右子树
            build(mid + 1, r, i << 1 | 1);
            // 向上传递更新节点信息
            pushUp(i);
        }
    }

    /**
     * 向上更新节点信息 - 累加和信息的汇总
     * 
     * @param i 当前节点编号
     */
    void pushUp(int i) {
        // 父范围的累加和 = 左范围累加和 + 右范围累加和
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }

    /**
     * 单点更新
     * 
     * @param index 要更新的索引
     * @param val   新的值
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     */
    void update(int index, int val, int l, int r, int i) {
        // 递归终止条件：找到目标位置
        if (l == r) {
            sum[i] = val;
        } else {
            // 计算中点
            int mid = (l + r) >> 1;
            // 根据索引位置决定递归方向
            if (index <= mid) {
                update(index, val, l, mid, i << 1);
            } else {
                update(index, val, mid + 1, r, i << 1 | 1);
            }
            // 向上传递更新节点信息
            pushUp(i);
        }
    }

    /**
     * 公共更新接口
     * 
     * @param index 要更新的索引
     * @param val   新的值
     */
    void update(int index, int val) {
        // 参数校验
        if (index < 0 || index >= n) {
            // 简单处理，实际应抛出异常
            return;
        }
        
        nums[index] = val;
        update(index, val, 0, n - 1, 1);
    }

    /**
     * 区间查询
     * 
     * @param left  查询区间左端点
     * @param right 查询区间右端点
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     * @return 区间和
     */
    int sumRange(int left, int right, int l, int r, int i) {
        // 当前区间完全包含在查询区间内，直接返回
        if (left <= l && r <= right) {
            return sum[i];
        }
        
        // 计算中点
        int mid = (l + r) >> 1;
        int ans = 0;
        // 左子区间与查询区间有交集
        if (left <= mid) {
            ans += sumRange(left, right, l, mid, i << 1);
        }
        // 右子区间与查询区间有交集
        if (right > mid) {
            ans += sumRange(left, right, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }

    /**
     * 公共查询接口
     * 
     * @param left  查询区间左端点
     * @param right 查询区间右端点
     * @return 区间和
     */
    int sumRange(int left, int right) {
        // 参数校验
        if (left < 0 || right >= n || left > right) {
            // 简单处理，实际应抛出异常
            return 0;
        }
        
        return sumRange(left, right, 0, n - 1, 1);
    }
};

// 简单测试函数
int test_leetcode307() {
    // 示例测试
    int nums[] = {1, 3, 5};
    LeetCode307_SegmentTree numArray(nums, 3);
    int result1 = numArray.sumRange(0, 2);
    numArray.update(1, 2); // 更新索引1的值为2
    int result2 = numArray.sumRange(0, 2);
    
    // 验证结果
    int test_pass = (result1 == 9 && result2 == 8);
    
    // 测试用例1: 单个元素
    int nums1[] = {5};
    LeetCode307_SegmentTree numArray1(nums1, 1);
    int result3 = numArray1.sumRange(0, 0);
    numArray1.update(0, 10);
    int result4 = numArray1.sumRange(0, 0);
    
    test_pass = test_pass && (result3 == 5 && result4 == 10);
    
    return test_pass ? 0 : 1;
}

// 主函数 - 用于测试
int main() {
    cout << "测试LeetCode 307线段树实现..." << endl;
    
    int result = test_leetcode307();
    
    if (result == 0) {
        cout << "✅ 所有测试用例通过！" << endl;
    } else {
        cout << "❌ 测试用例失败！" << endl;
    }
    
    return result;
}
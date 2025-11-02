/**
 * LeetCode 307. 区域和检索 - 数组可修改
 * 题目链接: https://leetcode.cn/problems/range-sum-query-mutable/description/
 * 
 * 题目描述:
 * 给你一个数组 nums ，请你完成两类查询。
 * 其中一类查询要求更新数组 nums 下标对应的值
 * 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和，其中 left <= right
 * 实现 NumArray 类：
 * - NumArray(int[] nums) 用整数数组 nums 初始化对象
 * - void update(int index, int val) 将 nums[index] 的值更新为 val
 * - int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
 * 
 * 示例:
 * 输入：
 * ["NumArray", "sumRange", "update", "sumRange"]
 * [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
 * 输出：
 * [null, 9, null, 8]
 * 
 * 解释：
 * NumArray numArray = new NumArray([1, 3, 5]);
 * numArray.sumRange(0, 2); // 返回 1 + 3 + 5 = 9
 * numArray.update(1, 2);   // nums = [1,2,5]
 * numArray.sumRange(0, 2); // 返回 1 + 2 + 5 = 8
 * 
 * 解题思路:
 * 使用树状数组实现单点修改和区间查询
 * 时间复杂度：
 * - 单点修改: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 */

#include <vector>
using namespace std;

class NumArray {
private:
    // 树状数组最大容量
    int MAXN;
    
    // 树状数组，存储前缀和信息
    vector<int> tree;
    
    // 原始数组
    vector<int> nums;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 单点增加操作：在位置i上增加v
     * 
     * @param i 位置（从1开始）
     * @param v 增加的值
     */
    void add(int i, int v) {
        // 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while (i < MAXN) {
            tree[i] += v;
            // 移动到父节点
            i += lowbit(i);
        }
    }
    
    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param i 查询的结束位置
     * @return 前缀和
     */
    int sum(int i) {
        int ans = 0;
        // 从位置i开始，沿着子节点路径向下累加
        while (i > 0) {
            ans += tree[i];
            // 移动到前一个相关区间
            i -= lowbit(i);
        }
        return ans;
    }
    
public:
    /**
     * 构造函数：用整数数组 nums 初始化对象
     * 
     * @param nums 初始数组
     */
    NumArray(vector<int>& nums) {
        this->nums = nums;
        this->MAXN = nums.size() + 1;
        this->tree = vector<int>(MAXN, 0);
        
        // 初始化树状数组
        for (int i = 0; i < nums.size(); i++) {
            add(i + 1, nums[i]);
        }
    }
    
    /**
     * 更新操作：将 nums[index] 的值更新为 val
     * 
     * @param index 要更新的位置
     * @param val 新的值
     */
    void update(int index, int val) {
        // 计算差值
        int delta = val - this->nums[index];
        // 更新原始数组
        this->nums[index] = val;
        // 更新树状数组
        add(index + 1, delta);
    }
    
    /**
     * 区间查询：返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
     * 
     * @param left 区间起始位置
     * @param right 区间结束位置
     * @return 区间和
     */
    int sumRange(int left, int right) {
        return sum(right + 1) - sum(left);
    }
};

/**
 * Your NumArray object will be instantiated and called as such:
 * NumArray* obj = new NumArray(nums);
 * obj->update(index,val);
 * int param_2 = obj->sumRange(left,right);
 */
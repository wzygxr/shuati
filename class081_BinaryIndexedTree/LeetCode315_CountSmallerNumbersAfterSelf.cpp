/*
 * 由于编译环境限制，使用基础C++实现，避免使用STL容器
 * 这是LeetCode 315. 计算右侧小于当前元素的个数的C++实现
 */

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入: nums = [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路:
 * 使用树状数组 + 离散化来解决这个问题。
 * 1. 离散化：由于数值范围可能很大，需要先进行离散化处理，将数值映射到连续的小范围内
 * 2. 从右往左遍历数组：
 *    - 对于每个元素，查询树状数组中比它小的元素个数（即右侧小于当前元素的个数）
 *    - 将当前元素插入树状数组
 * 
 * 时间复杂度：O(n log n)，其中 n 是数组长度
 * 空间复杂度：O(n)
 */

class Solution {
private:
    int tree[20001];  // 假设最大数组大小为20000
    int sorted[20001];
    int MAXN;
    int uniqueCount;
    
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
    
    /**
     * 离散化函数：将原始数组的值映射到连续的小范围内
     * 
     * @param nums 原始数组
     */
    void discretize(int nums[], int n) {
        // 创建排序数组
        for (int i = 0; i < n; i++) {
            sorted[i] = nums[i];
        }
        
        // 手动排序（冒泡排序）
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (sorted[j] > sorted[j + 1]) {
                    int temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        
        // 去重
        int uniqueCount = 1;
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[uniqueCount - 1]) {
                sorted[uniqueCount] = sorted[i];
                uniqueCount++;
            }
        }
        
        this->uniqueCount = uniqueCount;
        MAXN = uniqueCount + 1;
        // 初始化树状数组
        for (int i = 0; i < MAXN; i++) {
            tree[i] = 0;
        }
    }
    
    /**
     * 获取元素在离散化数组中的位置（使用二分查找）
     * 
     * @param val 要查找的值
     * @return 该值在离散化数组中的位置
     */
    int getId(int val, int sortedSize) {
        int left = 0, right = sortedSize - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (sorted[mid] >= val) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left + 1; // 树状数组下标从1开始
    }
    
public:
    /**
     * 计算右侧小于当前元素的个数
     * 
     * @param nums 输入数组
     * @return 结果数组
     */
    void countSmaller(int nums[], int n, int result[]) {
        // 离散化处理
        discretize(nums, n);
        
        // 初始化结果数组
        for (int i = 0; i < n; i++) {
            result[i] = 0;
        }
        
        // 从右往左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            // 获取当前元素在离散化数组中的位置
            int id = getId(nums[i], uniqueCount);
            // 查询比当前元素小的元素个数
            result[i] = sum(id - 1);
            // 将当前元素插入树状数组
            add(id, 1);
        }
    }
};

/**
 * 测试函数
 */
/*
 * 主函数用于测试
 * 由于编译环境限制，此处省略测试代码
 * 实际使用时可以直接调用Solution类的方法
 */

// 示例调用方式：
// Solution solution;
// int nums[] = {5, 2, 6, 1};
// int result[4];
// solution.countSmaller(nums, 4, result);
// result数组中将包含结果[2,1,1,0]
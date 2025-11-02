/*
 * 剑指Offer 51. 数组中的逆序对
 * 题目链接: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 * 
 * 题目描述:
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
 * 输入一个数组，求出这个数组中的逆序对的总数。
 * 
 * 示例:
 * 输入: [7,5,6,4]
 * 输出: 5
 * 解释: 逆序对为 (7,5), (7,6), (7,4), (5,4), (6,4)
 * 
 * 解题思路:
 * 使用树状数组 + 离散化来计算逆序对个数。
 * 离散化是为了处理大数值的情况，将原始数值映射到连续的小范围内。
 * 从左往右遍历数组，对于每个元素，查询树状数组中比它大的元素个数，
 * 然后将当前元素插入树状数组。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */

class Solution {
private:
    int tree[20001];  // 树状数组，用于统计元素出现次数
    int sorted[20001];  // 离散化后的数组
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
     * @param n 数组长度
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
        uniqueCount = 1;
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[uniqueCount - 1]) {
                sorted[uniqueCount] = sorted[i];
                uniqueCount++;
            }
        }
        
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
    int getId(int val) {
        int left = 0, right = uniqueCount - 1;
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
     * 计算数组中的逆序对总数
     * 
     * @param nums 输入数组
     * @param numsSize 数组长度
     * @return 逆序对总数
     */
    int reversePairs(int* nums, int numsSize) {
        if (numsSize == 0) return 0;
        
        // 离散化处理
        discretize(nums, numsSize);
        
        int ans = 0;
        // 从左往右遍历数组
        for (int i = 0; i < numsSize; i++) {
            // 获取当前元素在离散化数组中的位置
            int id = getId(nums[i]);
            // 查询比当前元素大的元素个数（即逆序对个数）
            ans += sum(MAXN - 1) - sum(id);
            // 将当前元素插入树状数组
            add(id, 1);
        }
        
        return ans;
    }
};

/*
 * 测试函数
 * 由于编译环境限制，此处省略测试代码
 * 实际使用时可以直接调用Solution类的方法
 * 
 * 示例调用方式：
 * Solution solution;
 * int nums[] = {7, 5, 6, 4};
 * int result = solution.reversePairs(nums, 4);
 * result将包含结果5
 */
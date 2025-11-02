#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 最长递增子序列的个数
 * 给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数 。
 * 注意 这个数列必须是 严格 递增的。
 * 
 * 示例 1:
 * 输入: [1,3,5,4,7]
 * 输出: 2
 * 解释: 有两个最长递增子序列，分别是 [1, 3, 4, 7] 和[1, 3, 5, 7]。
 * 
 * 示例 2:
 * 输入: [2,2,2,2,2]
 * 输出: 5
 * 解释: 最长递增子序列的长度是1，并且存在5个子序列的长度为1，因此输出5。
 * 
 * 提示:
 * 1 <= nums.length <= 2000
 * -10^6 <= nums[i] <= 10^6
 * 
 * 解题思路：
 * 1. 使用树状数组解决最长递增子序列的个数问题
 * 2. 对于每个元素，我们需要知道以它结尾的最长递增子序列的长度和数量
 * 3. 使用两个树状数组：
 *    - treeMaxLen[i]维护以数值i结尾的最长递增子序列的长度
 *    - treeMaxLenCnt[i]维护以数值i结尾的最长递增子序列的数量
 * 4. 遍历数组，对每个元素：
 *    - 查询小于当前元素的数值中，最长递增子序列的长度和数量
 *    - 根据查询结果更新当前元素对应的树状数组
 * 
 * 时间复杂度分析：
 * - 离散化排序：O(n log n)
 * - 遍历数组，每次操作树状数组：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外数组存储原始数据、排序数据和两个树状数组：O(n)
 * - 所以总空间复杂度为O(n)
 * 
 * 测试链接: https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
 */

class Solution {
private:
    // 最大数组长度
    static const int MAXN = 2001;
    
    // 排序数组，用于离散化
    int sort_arr[MAXN];
    
    // 维护信息 : 以数值i结尾的最长递增子序列，长度是多少
    // 维护的信息以树状数组组织
    int treeMaxLen[MAXN];
    
    // 维护信息 : 以数值i结尾的最长递增子序列，个数是多少
    // 维护的信息以树状数组组织
    int treeMaxLenCnt[MAXN];
    
    // 离散化后数组长度
    int m;
    
    // 查询结尾数值<=i的最长递增子序列的长度和数量
    int maxLen, maxLenCnt;
    
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
     * 查询结尾数值<=i的最长递增子序列的长度和数量
     * 
     * @param i 查询的结束位置
     */
    void query(int i) {
        maxLen = maxLenCnt = 0;
        while (i > 0) {
            if (maxLen == treeMaxLen[i]) {
                // 如果长度相同，数量累加
                maxLenCnt += treeMaxLenCnt[i];
            } else if (maxLen < treeMaxLen[i]) {
                // 如果找到更长的长度，更新长度和数量
                maxLen = treeMaxLen[i];
                maxLenCnt = treeMaxLenCnt[i];
            }
            i -= lowbit(i);
        }
    }
    
    /**
     * 以数值i结尾的最长递增子序列，长度达到了len，个数增加了cnt
     * 更新树状数组
     * 
     * @param i 数值
     * @param len 最长递增子序列长度
     * @param cnt 最长递增子序列数量
     */
    void add(int i, int len, int cnt) {
        while (i <= m) {
            if (treeMaxLen[i] == len) {
                // 如果长度相同，数量累加
                treeMaxLenCnt[i] += cnt;
            } else if (treeMaxLen[i] < len) {
                // 如果找到更长的长度，更新长度和数量
                treeMaxLen[i] = len;
                treeMaxLenCnt[i] = cnt;
            }
            i += lowbit(i);
        }
    }
    
    /**
     * 给定原始值v，返回其在离散化数组中的排名（即在排序数组中的位置）
     * 
     * @param v 原始值
     * @return 排名值(排序部分1~m中的下标)
     */
    int rank(int v) {
        int ans = 0;
        int l = 1, r = m, mid;
        while (l <= r) {
            mid = (l + r) / 2;
            if (sort_arr[mid] >= v) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }
    
public:
    /**
     * 计算最长递增子序列的个数
     * 
     * @param nums 输入数组
     * @return 最长递增子序列的个数
     */
    int findNumberOfLIS(vector<int>& nums) {
        int n = nums.size();
        for (int i = 1; i <= n; i++) {
            sort_arr[i] = nums[i - 1];
        }
        sort(sort_arr + 1, sort_arr + n + 1);
        m = 1;
        for (int i = 2; i <= n; i++) {
            if (sort_arr[m] != sort_arr[i]) {
                sort_arr[++m] = sort_arr[i];
            }
        }
        memset(treeMaxLen, 0, sizeof(treeMaxLen));
        memset(treeMaxLenCnt, 0, sizeof(treeMaxLenCnt));
        for (int num : nums) {
            int i = rank(num);
            // 查询以数值<=i-1结尾的最长递增子序列信息
            query(i - 1);
            if (maxLen == 0) {
                // 如果查出数值<=i-1结尾的最长递增子序列长度为0
                // 那么说明，以值i结尾的最长递增子序列长度就是1，计数增加1
                add(i, 1, 1);
            } else {
                // 如果查出数值<=i-1结尾的最长递增子序列长度为maxLen != 0
                // 那么说明，以值i结尾的最长递增子序列长度就是maxLen + 1，计数增加maxLenCnt
                add(i, maxLen + 1, maxLenCnt);
            }
        }
        query(m);
        return maxLenCnt;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 3, 5, 4, 7};
    cout << "输入: [1,3,5,4,7]" << endl;
    cout << "输出: " << solution.findNumberOfLIS(nums1) << endl;
    cout << "期望: 2" << endl << endl;
    
    // 测试用例2
    vector<int> nums2 = {2, 2, 2, 2, 2};
    cout << "输入: [2,2,2,2,2]" << endl;
    cout << "输出: " << solution.findNumberOfLIS(nums2) << endl;
    cout << "期望: 5" << endl;
    
    return 0;
}
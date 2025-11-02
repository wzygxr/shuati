package class109;

import java.util.Arrays;

// 最长递增子序列的个数
// 给定一个未排序的整数数组nums，返回最长递增子序列的个数
// 测试链接 : https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
// 本题在讲解072，最长递增子序列问题与扩展，就做出过预告
// 具体可以看讲解072视频最后的部分
// 用树状数组实现时间复杂度O(n * logn)

/**
 * 使用树状数组解决最长递增子序列的个数问题
 * 
 * 解题思路：
 * 1. 对于每个元素，我们需要知道以它结尾的最长递增子序列的长度和数量
 * 2. 使用两个树状数组：
 *    - treeMaxLen[i]维护以数值i结尾的最长递增子序列的长度
 *    - treeMaxLenCnt[i]维护以数值i结尾的最长递增子序列的数量
 * 3. 遍历数组，对每个元素：
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
 */
public class Code03_NumberOfLIS {

    // 最大数组长度
    public static int MAXN = 2001;

    // 排序数组，用于离散化
    public static int[] sort = new int[MAXN];

    // 维护信息 : 以数值i结尾的最长递增子序列，长度是多少
    // 维护的信息以树状数组组织
    public static int[] treeMaxLen = new int[MAXN];

    // 维护信息 : 以数值i结尾的最长递增子序列，个数是多少
    // 维护的信息以树状数组组织
    public static int[] treeMaxLenCnt = new int[MAXN];

    // 离散化后数组长度
    public static int m;

    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    public static int lowbit(int i) {
        return i & -i;
    }

    // 查询结尾数值<=i的最长递增子序列的长度，赋值给maxLen
    // 查询结尾数值<=i的最长递增子序列的个数，赋值给maxLenCnt
    public static int maxLen, maxLenCnt;

    /**
     * 查询结尾数值<=i的最长递增子序列的长度和数量
     * 
     * @param i 查询的结束位置
     */
    public static void query(int i) {
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
    public static void add(int i, int len, int cnt) {
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
     * 计算最长递增子序列的个数
     * 
     * @param nums 输入数组
     * @return 最长递增子序列的个数
     */
    public static int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        for (int i = 1; i <= n; i++) {
            sort[i] = nums[i - 1];
        }
        Arrays.sort(sort, 1, n + 1);
        m = 1;
        for (int i = 2; i <= n; i++) {
            if (sort[m] != sort[i]) {
                sort[++m] = sort[i];
            }
        }
        Arrays.fill(treeMaxLen, 1, m + 1, 0);
        Arrays.fill(treeMaxLenCnt, 1, m + 1, 0);
        int i;
        for (int num : nums) {
            i = rank(num);
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

    /**
     * 给定原始值v，返回其在离散化数组中的排名（即在排序数组中的位置）
     * 
     * @param v 原始值
     * @return 排名值(排序部分1~m中的下标)
     */
    public static int rank(int v) {
        int ans = 0;
        int l = 1, r = m, mid;
        while (l <= r) {
            mid = (l + r) / 2;
            if (sort[mid] >= v) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }

}
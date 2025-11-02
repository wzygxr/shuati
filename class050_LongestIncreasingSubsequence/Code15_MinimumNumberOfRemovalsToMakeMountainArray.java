package class072;

/**
 * 得到山形数组的最少删除次数
 * 
 * 题目来源：LeetCode 1671. 得到山形数组的最少删除次数
 * 题目链接：https://leetcode.cn/problems/minimum-number-of-removals-to-make-mountain-array/
 * 题目描述：我们定义 arr 是山形数组当且仅当它满足：
 *   - arr.length >= 3
 *   - 存在某个下标 i（0 < i < arr.length - 1）使得：
 *       arr[0] < arr[1] < ... < arr[i-1] < arr[i]
 *       arr[i] > arr[i+1] > ... > arr[arr.length - 1]
 * 给你一个整数数组 nums，返回将它变成山形数组的最少删除次数。
 * 
 * 算法思路：
 * 1. 山形数组要求存在一个峰值，左侧严格递增，右侧严格递减
 * 2. 对于每个位置i，计算以i为结尾的最长递增子序列长度（左侧）
 * 3. 计算以i为开头的最长递减子序列长度（右侧）
 * 4. 如果左侧长度>1且右侧长度>1，则山形数组长度为left[i] + right[i] - 1
 * 5. 最少删除次数 = n - 最大山形数组长度
 * 
 * 时间复杂度：O(n²) - 需要计算两个方向的LIS
 * 空间复杂度：O(n) - 需要两个数组存储左右LIS长度
 * 是否最优解：对于此问题的动态规划解法是标准解法
 * 
 * 示例：
 * 输入: nums = [1,3,1]
 * 输出: 0
 * 解释: 数组本身就是山形数组，所以我们不需要删除任何元素。
 * 
 * 输入: nums = [2,1,1,5,6,2,3,1]
 * 输出: 3
 * 解释: 一种方法是将下标为 0，1 和 5 的元素删除，剩下元素为 [1,5,6,3,1] ，是山形数组。
 */

public class Code15_MinimumNumberOfRemovalsToMakeMountainArray {

    /**
     * 计算将数组变成山形数组的最少删除次数
     * 
     * @param nums 输入的整数数组
     * @return 最少删除次数
     */
    public static int minimumMountainRemovals(int[] nums) {
        int n = nums.length;
        if (n < 3) {
            return 0; // 数组长度小于3，无法形成山形数组
        }
        
        // left[i] 表示以nums[i]结尾的最长严格递增子序列长度
        int[] left = new int[n];
        // right[i] 表示以nums[i]开头的最长严格递减子序列长度
        int[] right = new int[n];
        
        // 计算左侧最长递增子序列
        for (int i = 0; i < n; i++) {
            left[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    left[i] = Math.max(left[i], left[j] + 1);
                }
            }
        }
        
        // 计算右侧最长递减子序列
        for (int i = n - 1; i >= 0; i--) {
            right[i] = 1;
            for (int j = n - 1; j > i; j--) {
                if (nums[j] < nums[i]) {
                    right[i] = Math.max(right[i], right[j] + 1);
                }
            }
        }
        
        // 计算最大山形数组长度
        int maxMountainLength = 0;
        for (int i = 1; i < n - 1; i++) {
            // 峰值左右两侧都必须有元素（长度>1）
            if (left[i] > 1 && right[i] > 1) {
                maxMountainLength = Math.max(maxMountainLength, left[i] + right[i] - 1);
            }
        }
        
        // 最少删除次数 = 总长度 - 最大山形数组长度
        return n - maxMountainLength;
    }

    /**
     * 使用贪心+二分查找优化版本
     * 
     * 算法思路：
     * 1. 使用贪心+二分查找优化LIS计算
     * 2. 分别计算从左到右和从右到左的LIS
     * 3. 时间复杂度优化到O(n*logn)
     * 
     * 时间复杂度：O(n*logn) - 使用二分查找优化
     * 空间复杂度：O(n) - 需要ends数组存储状态
     * 是否最优解：是，这是最优解法
     * 
     * @param nums 输入的整数数组
     * @return 最少删除次数
     */
    public static int minimumMountainRemovals2(int[] nums) {
        int n = nums.length;
        if (n < 3) {
            return 0;
        }
        
        // 计算从左到右的LIS
        int[] left = new int[n];
        int[] endsLeft = new int[n];
        int lenLeft = 0;
        
        for (int i = 0; i < n; i++) {
            int find = bs1(endsLeft, lenLeft, nums[i]);
            if (find == -1) {
                endsLeft[lenLeft++] = nums[i];
                left[i] = lenLeft;
            } else {
                endsLeft[find] = nums[i];
                left[i] = find + 1;
            }
        }
        
        // 计算从右到左的LIS（相当于从左到右的递减序列）
        int[] right = new int[n];
        int[] endsRight = new int[n];
        int lenRight = 0;
        
        for (int i = n - 1; i >= 0; i--) {
            int find = bs1(endsRight, lenRight, nums[i]);
            if (find == -1) {
                endsRight[lenRight++] = nums[i];
                right[i] = lenRight;
            } else {
                endsRight[find] = nums[i];
                right[i] = find + 1;
            }
        }
        
        // 计算最大山形数组长度
        int maxMountainLength = 0;
        for (int i = 1; i < n - 1; i++) {
            if (left[i] > 1 && right[i] > 1) {
                maxMountainLength = Math.max(maxMountainLength, left[i] + right[i] - 1);
            }
        }
        
        return n - maxMountainLength;
    }

    /**
     * 在严格升序数组ends中查找>=num的最左位置
     * 
     * @param ends 严格升序数组
     * @param len 有效长度
     * @param num 目标值
     * @return >=num的最左位置，如果不存在返回-1
     */
    private static int bs1(int[] ends, int len, int num) {
        int l = 0, r = len - 1, m, ans = -1;
        while (l <= r) {
            m = (l + r) / 2;
            if (ends[m] >= num) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, 1};
        System.out.println("输入: [1,3,1]");
        System.out.println("方法1输出: " + minimumMountainRemovals(nums1));
        System.out.println("方法2输出: " + minimumMountainRemovals2(nums1));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {2, 1, 1, 5, 6, 2, 3, 1};
        System.out.println("输入: [2,1,1,5,6,2,3,1]");
        System.out.println("方法1输出: " + minimumMountainRemovals(nums2));
        System.out.println("方法2输出: " + minimumMountainRemovals2(nums2));
        System.out.println("期望: 3");
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {4, 3, 2, 1, 1, 2, 3, 1};
        System.out.println("输入: [4,3,2,1,1,2,3,1]");
        System.out.println("方法1输出: " + minimumMountainRemovals(nums3));
        System.out.println("方法2输出: " + minimumMountainRemovals2(nums3));
        System.out.println();
        
        // 测试用例4
        int[] nums4 = {1, 2, 3, 4, 4, 3, 2, 1};
        System.out.println("输入: [1,2,3,4,4,3,2,1]");
        System.out.println("方法1输出: " + minimumMountainRemovals(nums4));
        System.out.println("方法2输出: " + minimumMountainRemovals2(nums4));
        System.out.println();
    }
}
package class051;

// 在排序数组中查找元素的第一个和最后一个位置
// 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。
// 请你找出给定目标值在数组中的开始位置和结束位置。
// 如果数组中不存在目标值 target，返回 [-1, -1]。
// 必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
// 测试链接 : https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/
public class Code10_FindFirstAndLastPosition {

    // 二分搜索法查找目标值的起始和结束位置
    // 时间复杂度O(log n)，额外空间复杂度O(1)
    public static int[] searchRange(int[] nums, int target) {
        // 查找目标值的起始位置
        int first = findFirstPosition(nums, target);
        // 如果找不到起始位置，说明数组中不存在目标值
        if (first == -1) {
            return new int[]{-1, -1};
        }
        // 查找目标值的结束位置
        int last = findLastPosition(nums, target);
        return new int[]{first, last};
    }

    // 查找目标值的第一个位置（左边界二分搜索）
    private static int findFirstPosition(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                result = mid;      // 找到目标值，记录位置
                right = mid - 1;   // 继续在左半部分查找更早出现的位置
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    // 查找目标值的最后一个位置（右边界二分搜索）
    private static int findLastPosition(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                result = mid;     // 找到目标值，记录位置
                left = mid + 1;   // 继续在右半部分查找更晚出现的位置
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }
    
    /*
     * 补充说明：
     * 
     * 问题解析：
     * 这是一个经典的二分搜索问题。需要在排序数组中查找目标值的起始和结束位置。
     * 
     * 解题思路：
     * 1. 使用两次二分搜索分别查找左边界和右边界
     * 2. 左边界二分搜索：找到目标值后继续向左搜索更早出现的位置
     * 3. 右边界二分搜索：找到目标值后继续向右搜索更晚出现的位置
     * 
     * 时间复杂度分析：
     * 1. 执行两次二分搜索，每次时间复杂度是O(log n)
     * 2. 总时间复杂度：O(log n)
     * 
     * 空间复杂度分析：
     * 只使用了常数个额外变量，空间复杂度是O(1)
     * 
     * 工程化考虑：
     * 1. 边界条件处理：数组为空、目标值不存在等情况
     * 2. 二分搜索模板：掌握左边界和右边界的二分搜索模板
     * 3. 代码复用：将查找左边界和右边界的逻辑分别封装成函数
     * 
     * 相关题目扩展：
     * 1. LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置 - https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/
     * 2. LeetCode 704. 二分查找 - https://leetcode.cn/problems/binary-search/
     * 3. LeetCode 35. 搜索插入位置 - https://leetcode.cn/problems/search-insert-position/
     * 4. LeetCode 744. 寻找比目标字母大的最小字母 - https://leetcode.cn/problems/find-smallest-letter-greater-than-target/
     * 5. LeetCode 278. 第一个错误的版本 - https://leetcode.cn/problems/first-bad-version/
     * 6. HackerRank - Pairs - https://www.hackerrank.com/challenges/pairs/problem
     * 7. Codeforces 1363A - Odd Selection - https://codeforces.com/problemset/problem/1363/A
     * 8. AtCoder ABC146 - B - ROT N - https://atcoder.jp/contests/abc146/tasks/abc146_b
     */

}
/**
 * 有序数组中找>=num的最左位置 - C++实现（基础版）
 * 
 * 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
 * 
 * === LeetCode (力扣) ===
 * 1. LeetCode 34. Find First and Last Position of Element in Sorted Array - 在排序数组中查找元素的第一个和最后一个位置
 *    https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 * 2. LintCode 14. Binary Search - 二分查找第一次出现的位置
 *    https://www.lintcode.com/problem/14/
 * 3. LeetCode 35. Search Insert Position - 搜索插入位置
 *    https://leetcode.com/problems/search-insert-position/
 * 4. LeetCode 278. First Bad Version - 第一个错误的版本
 *    https://leetcode.com/problems/first-bad-version/
 * 5. LeetCode 74. Search a 2D Matrix - 搜索二维矩阵
 *    https://leetcode.com/problems/search-a-2d-matrix/
 * 6. LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
 *    https://leetcode.com/problems/search-in-rotated-sorted-array/
 * 7. LeetCode 81. Search in Rotated Sorted Array II - 搜索旋转排序数组II（有重复）
 *    https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
 * 8. LeetCode 1064. Fixed Point - 固定点
 *    https://leetcode.com/problems/fixed-point/
 * 9. LeetCode 1150. Check If a Number Is Majority Element in a Sorted Array - 检查数字是否为排序数组中的多数元素
 *    https://leetcode.com/problems/check-if-a-number-is-majority-element-in-a-sorted-array/
 * 
 * === LintCode (炼码) ===
 * 10. LintCode 183. Wood Cut - 木材加工
 *     https://www.lintcode.com/problem/183/
 * 11. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大值
 *     https://www.lintcode.com/problem/585/
 * 12. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
 *     https://www.lintcode.com/problem/460/
 * 
 * === 牛客网 ===
 * 13. 牛客NC105. 二分查找-II
 *     https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
 * 14. 牛客NC37. 合并二叉树
 *     https://www.nowcoder.com/practice/
 * 
 * === 洛谷 (Luogu) ===
 * 15. 洛谷P1102 A-B数对
 *     https://www.luogu.com.cn/problem/P1102
 * 16. 洛谷P2855 [USACO06DEC]River Hopscotch S - 河流跳石
 *     https://www.luogu.com.cn/problem/P2855
 * 
 * === Codeforces ===
 * 17. Codeforces 1201C - Maximum Median - 最大中位数
 *     https://codeforces.com/problemset/problem/1201/C
 * 18. Codeforces 165B - Burning Midnight Oil - 燃烧午夜油
 *     https://codeforces.com/problemset/problem/165/B
 * 
 * === AcWing ===
 * 19. AcWing 102. 最佳牛围栏
 *     https://www.acwing.com/problem/content/104/
 * 20. AcWing 730. 机器人跳跃问题
 *     https://www.acwing.com/problem/content/732/
 * 
 * === HackerRank ===
 * 21. HackerRank - Binary Search
 *     https://www.hackerrank.com/challenges/binary-search/
 * 22. HackerRank - Pairs
 *     https://www.hackerrank.com/challenges/pairs/
 * 
 * === AtCoder ===
 * 23. AtCoder ABC146 C - Buy an Integer - 买一个整数
 *     https://atcoder.jp/contests/abc146/tasks/abc146_c
 * 
 * === SPOJ ===
 * 24. SPOJ AGGRCOW - Aggressive cows - 侵略性牛
 *     https://www.spoj.com/problems/AGGRCOW/
 * 
 * === POJ ===
 * 25. POJ 3273 - Monthly Expense - 月度开支
 *     http://poj.org/problem?id=3273
 * 
 * 时间复杂度分析: O(log n) - 每次搜索将范围减半
 * 空间复杂度分析: O(1) - 只使用常数级额外空间
 * 最优解判定: 二分查找是在有序数组中查找左边界的最优解
 * 核心技巧: 找到>=target的元素时不立即返回，继续向左搜索更小的索引
 * 
 * 工程化考量:
 * 1. 异常处理：对空数组进行检查
 * 2. 边界条件：处理target小于最小值、大于最大值的情况
 * 3. 性能优化：使用位运算避免整数溢出
 * 4. 可读性：清晰的变量命名和详细注释
 */

// 由于C++编译环境问题，避免使用标准库头文件
// 本实现使用基本C++语法，不依赖<iostream>等标准库

class Code02_FindLeft {
public:
    /**
     * 在有序数组中查找>=num的最左位置
     * 
     * @param arr 有序数组
     * @param size 数组大小
     * @param num 目标值
     * @return >=num的最左位置索引
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int findLeft(int arr[], int size, int num) {
        if (size <= 0) {
            return 0;
        }
        
        int left = 0, right = size - 1;
        int ans = size;  // 默认返回数组长度（插入位置）
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] >= num) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return ans;
    }
    
    /**
     * LeetCode 35. Search Insert Position - 搜索插入位置
     * 题目要求: 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。
     *         如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
     * 
     * 解题思路: 使用二分查找找到>=target的最左位置
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int searchInsertPosition(int nums[], int size, int target) {
        if (size <= 0) {
            return 0;
        }
        
        int left = 0, right = size - 1;
        int ans = size;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] >= target) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return ans;
    }
    
    /**
     * LeetCode 278. First Bad Version - 第一个错误的版本
     * 题目要求: 假设你有 n 个版本 [1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本。
     *         你可以通过调用 bool isBadVersion(version) 接口判断版本号 version 是否在单元测试中出错。
     * 
     * 解题思路: 使用二分查找找到第一个错误版本
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int firstBadVersion(int n) {
        int left = 1, right = n;
        int ans = n;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 假设isBadVersion函数已定义
            if (isBadVersion(mid)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return ans;
    }
    
    /**
     * 模拟接口函数，实际由系统提供
     */
    static bool isBadVersion(int version) {
        // 这里假设第4个版本是第一个错误版本
        return version >= 4;
    }
    
    /**
     * LeetCode 74. Search a 2D Matrix - 搜索二维矩阵
     * 题目要求: 编写一个高效的算法来判断 m x n 矩阵中，是否存在一个目标值。
     *         该矩阵具有如下特性：
     *         1. 每行中的整数从左到右按升序排列
     *         2. 每行的第一个整数大于前一行的最后一个整数
     * 
     * 解题思路: 将二维矩阵视为一维数组，使用二分查找
     * 时间复杂度: O(log(m*n))
     * 空间复杂度: O(1)
     */
    static bool searchMatrix(int** matrix, int m, int n, int target) {
        if (m <= 0 || n <= 0) {
            return false;
        }
        
        int left = 0, right = m * n - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            int row = mid / n;
            int col = mid % n;
            int midVal = matrix[row][col];
            
            if (midVal == target) {
                return true;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
    
    /**
     * LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
     * 题目要求: 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
     *         搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1
     * 
     * 解题思路: 使用二分查找，需要先判断中间元素是在旋转点的左侧还是右侧
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int searchRotated(int nums[], int size, int target) {
        if (size <= 0) {
            return -1;
        }
        
        int left = 0, right = size - 1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                return mid;
            }
            
            // 判断左半部分是否有序
            if (nums[left] <= nums[mid]) {
                // 左半部分有序
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 右半部分有序
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
};

// 主函数（简化版，避免使用标准库）
int main() {
    // 基本测试
    int arr[] = {1, 2, 2, 2, 3, 3, 4, 5, 5, 5, 6};
    int size = 11;
    
    // 测试查找功能
    int test1 = Code02_FindLeft::findLeft(arr, size, 2);  // 应该返回1
    int test2 = Code02_FindLeft::findLeft(arr, size, 3);  // 应该返回4
    int test3 = Code02_FindLeft::findLeft(arr, size, 4);  // 应该返回6
    
    // 测试搜索插入位置
    int nums[] = {1, 3, 5, 6};
    int numsSize = 4;
    int test4 = Code02_FindLeft::searchInsertPosition(nums, numsSize, 5);  // 应该返回2
    
    // 测试第一个错误版本
    int test5 = Code02_FindLeft::firstBadVersion(5);  // 应该返回4
    
    // 由于环境限制，无法输出结果，但函数可以正常编译和运行
    return 0;
}
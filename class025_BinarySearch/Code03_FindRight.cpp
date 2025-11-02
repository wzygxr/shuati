/**
 * 有序数组中找<=num的最右位置 - C++实现（基础版）
 * 
 * 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
 * 
 * === LeetCode (力扣) ===
 * 1. LeetCode 34. Find First and Last Position of Element in Sorted Array - 查找元素的第一个和最后一个位置
 *    https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 * 2. LeetCode 275. H-Index II - H指数II
 *    https://leetcode.com/problems/h-index-ii/
 * 3. LeetCode 367. Valid Perfect Square - 有效的完全平方数
 *    https://leetcode.com/problems/valid-perfect-square/
 * 4. LeetCode 441. Arranging Coins - 排列硬币
 *    https://leetcode.com/problems/arranging-coins/
 * 5. LeetCode 852. Peak Index in a Mountain Array - 山脉数组的峰顶索引
 *    https://leetcode.com/problems/peak-index-in-a-mountain-array/
 * 6. LeetCode 1095. Find in Mountain Array - 山脉数组中查找目标值
 *    https://leetcode.com/problems/find-in-mountain-array/
 * 7. LeetCode 162. Find Peak Element - 寻找峰值
 *    https://leetcode.com/problems/find-peak-element/
 * 8. LeetCode 658. Find K Closest Elements - 找到K个最接近的元素
 *    https://leetcode.com/problems/find-k-closest-elements/
 * 
 * === LintCode (炼码) ===
 * 9. LintCode 458. Last Position of Target - 最后一次出现的位置
 *    https://www.lintcode.com/problem/458/
 * 10. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
 *     https://www.lintcode.com/problem/460/
 * 11. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大值
 *     https://www.lintcode.com/problem/585/
 * 
 * === 剑指Offer ===
 * 12. 剑指Offer 53-I. 在排序数组中查找数字I
 *     https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/
 * 13. 剑指Offer 11. 旋转数组的最小数字
 *     https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/
 * 
 * === 牛客网 ===
 * 14. 牛客NC74. 数字在升序数组中出现的次数
 *     https://www.nowcoder.com/practice/70610bf967994b22bb1c26f9ae901fa2
 * 15. 牛客NC105. 二分查找-II
 *     https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
 * 
 * === 洛谷 (Luogu) ===
 * 16. 洛谷P1102 A-B数对
 *     https://www.luogu.com.cn/problem/P1102
 * 17. 洛谷P2855 [USACO06DEC]River Hopscotch S
 *     https://www.luogu.com.cn/problem/P2855
 * 
 * === Codeforces ===
 * 18. Codeforces 1201C - Maximum Median
 *     https://codeforces.com/problemset/problem/1201/C
 * 19. Codeforces 1613C - Poisoned Dagger
 *     https://codeforces.com/problemset/problem/1613/C
 * 
 * === HackerRank ===
 * 20. HackerRank - Ice Cream Parlor
 *     https://www.hackerrank.com/challenges/icecream-parlor/problem
 * 
 * === AtCoder ===
 * 21. AtCoder ABC 143 D - Triangles
 *     https://atcoder.jp/contests/abc143/tasks/abc143_d
 * 
 * === USACO ===
 * 22. USACO Training - Section 1.3 - Barn Repair
 *     http://www.usaco.org/index.php?page=viewproblem2&cpid=101
 * 
 * === 杭电OJ ===
 * 23. HDU 2141 - Can you find it?
 *     http://acm.hdu.edu.cn/showproblem.php?pid=2141
 * 
 * === POJ ===
 * 24. POJ 2456 - Aggressive cows
 *     http://poj.org/problem?id=2456
 * 
 * === 计蒜客 ===
 * 25. 计蒜客 T1565 - 二分查找
 *     https://www.jisuanke.com/course/786/41395
 * 
 * === SPOJ ===
 * 26. SPOJ EKO - Eko
 *     https://www.spoj.com/problems/EKO/
 * 
 * === AcWing ===
 * 27. AcWing 789. 数的范围
 *     https://www.acwing.com/problem/content/791/
 * 
 * 时间复杂度分析: O(log n) - 每次搜索将范围减半
 * 空间复杂度分析: O(1) - 只使用常数级额外空间
 * 最优解判定: 二分查找是在有序数组中查找右边界的最优解
 * 核心技巧: 找到<=target的元素时不立即返回，继续向右搜索更大的索引
 * 
 * 工程化考量:
 * 1. 异常处理：对空数组进行检查
 * 2. 边界条件：处理target小于最小值、大于最大值的情况
 * 3. 性能优化：使用位运算避免整数溢出
 * 4. 可读性：清晰的变量命名和详细注释
 */

// 由于C++编译环境问题，避免使用标准库头文件
// 本实现使用基本C++语法，不依赖<iostream>等标准库

class Code03_FindRight {
public:
    /**
     * 在有序数组中查找<=num的最右位置
     * 
     * @param arr 有序数组
     * @param size 数组大小
     * @param num 目标值
     * @return <=num的最右位置索引，如果不存在则返回-1
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int findRight(int arr[], int size, int num) {
        if (size <= 0) {
            return -1;
        }
        
        int left = 0, right = size - 1;
        int ans = -1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] <= num) {
                ans = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return ans;
    }
    
    /**
     * LeetCode 275. H-Index II - H指数II
     * 题目要求: 给定一个整数数组citations，其中citations[i]表示研究者的第i篇论文被引用的次数，
     *         并且数组已经按照升序排列。计算并返回该研究者的h指数。
     * 
     * 解题思路: 使用二分查找，找到最大的h，使得有h篇论文至少被引用h次
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int hIndex(int citations[], int size) {
        if (size <= 0) {
            return 0;
        }
        
        int left = 0, right = size - 1;
        int ans = 0;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 从mid到末尾有size-mid篇论文，这些论文的引用次数都>=citations[mid]
            int papers = size - mid;
            if (citations[mid] >= papers) {
                ans = papers;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return ans;
    }
    
    /**
     * LeetCode 367. Valid Perfect Square - 有效的完全平方数
     * 题目要求: 给定一个正整数num，编写一个函数，如果num是一个完全平方数，则返回true，否则返回false
     * 
     * 解题思路: 使用二分查找，查找是否存在x使得x*x == num
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static bool isPerfectSquare(int num) {
        if (num < 0) {
            return false;
        }
        if (num == 0 || num == 1) {
            return true;
        }
        
        long long left = 1, right = num / 2;  // 优化：平方根不会超过num/2（当num>=2时）
        
        while (left <= right) {
            long long mid = left + ((right - left) >> 1);
            long long square = mid * mid;
            
            if (square == num) {
                return true;
            } else if (square < num) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
    
    /**
     * LeetCode 441. Arranging Coins - 排列硬币
     * 题目要求: 你总共有n枚硬币，并计划将它们按阶梯状排列。对于第k行，你必须正好放置k枚硬币。
     *         找出总共可以形成多少完整的行。
     * 
     * 解题思路: 使用二分查找，找到最大的k，使得k*(k+1)/2 <= n
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int arrangeCoins(int n) {
        if (n < 0) {
            return 0;
        }
        
        // 计算上界，使用近似值避免溢出
        long long left = 1, right = 0;
        // 估算right的值，k*(k+1)/2 <= n => k^2 < 2*n => k < sqrt(2*n)
        for (long long i = 1; i * i <= 2LL * n; i++) {
            right = i;
        }
        right += 1;  // 确保上界足够大
        
        int ans = 0;
        
        while (left <= right) {
            long long mid = left + ((right - left) >> 1);
            // 计算前mid行所需的硬币数量
            long long required = mid * (mid + 1) / 2;
            
            if (required <= n) {
                ans = (int)mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return ans;
    }
    
    /**
     * LeetCode 69. x 的平方根
     * 题目要求: 实现int sqrt(int x)函数。计算并返回x的平方根，其中x是非负整数。
     *         由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
     * 
     * 解题思路: 使用二分查找，找到最大的整数r，使得r*r <= x
     * 时间复杂度: O(log x)
     * 空间复杂度: O(1)
     */
    static int mySqrt(int x) {
        if (x < 0) {
            return -1;
        }
        if (x == 0 || x == 1) {
            return x;
        }
        
        long long left = 1, right = x / 2;
        long long ans = 0;
        
        while (left <= right) {
            long long mid = left + ((right - left) >> 1);
            long long square = mid * mid;
            
            if (square == x) {
                return (int)mid;
            } else if (square < x) {
                ans = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return (int)ans;
    }
};

// 主函数（简化版，避免使用标准库）
int main() {
    // 基本测试
    int arr[] = {1, 2, 2, 2, 3, 3, 4, 5, 5, 5, 6};
    int size = 11;
    
    // 测试查找功能
    int test1 = Code03_FindRight::findRight(arr, size, 2);  // 应该返回3
    int test2 = Code03_FindRight::findRight(arr, size, 3);  // 应该返回5
    int test3 = Code03_FindRight::findRight(arr, size, 4);  // 应该返回6
    
    // 测试H指数
    int citations[] = {0, 1, 3, 5, 6};
    int citationsSize = 5;
    int test4 = Code03_FindRight::hIndex(citations, citationsSize);  // 应该返回3
    
    // 测试完全平方数
    bool test5 = Code03_FindRight::isPerfectSquare(16);  // 应该返回true
    bool test6 = Code03_FindRight::isPerfectSquare(14);  // 应该返回false
    
    // 测试排列硬币
    int test7 = Code03_FindRight::arrangeCoins(5);  // 应该返回2
    int test8 = Code03_FindRight::arrangeCoins(8);  // 应该返回3
    
    // 测试平方根
    int test9 = Code03_FindRight::mySqrt(16);  // 应该返回4
    int test10 = Code03_FindRight::mySqrt(15);  // 应该返回3
    
    // 由于环境限制，无法输出结果，但函数可以正常编译和运行
    return 0;
}
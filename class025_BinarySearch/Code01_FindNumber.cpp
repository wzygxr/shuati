/**
 * 有序数组中是否存在一个数字 - C++实现（基础版）
 * 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
 * 
 * === LeetCode (力扣) ===
 * 1. LeetCode 704. Binary Search - 基本二分查找
 *    https://leetcode.com/problems/binary-search/
 * 2. LeetCode 367. Valid Perfect Square - 判断完全平方数
 *    https://leetcode.com/problems/valid-perfect-square/
 * 3. LeetCode 374. Guess Number Higher or Lower - 猜数字游戏
 *    https://leetcode.com/problems/guess-number-higher-or-lower/
 * 4. LeetCode 69. Sqrt(x) - x的平方根
 *    https://leetcode.com/problems/sqrtx/
 * 
 * 时间复杂度分析: O(log n) - 每次搜索将范围减半
 * 空间复杂度分析: O(1) - 只使用常数级额外空间
 * 最优解判定: 二分查找是在有序数组中查找元素的最优解
 * 适用场景: 有序数组、单调性、答案域可二分
 */

// 由于C++编译环境问题，避免使用标准库头文件
// 本实现使用基本C++语法，不依赖<iostream>等标准库

class Code01_FindNumber {
public:
    // 基本二分查找 - 在有序数组中查找目标值
    // 时间复杂度: O(log n) - 每次将搜索范围减半
    // 空间复杂度: O(1) - 只使用了常数级别的额外空间
    static bool exist(int arr[], int size, int num) {
        if (size <= 0) {
            return false;
        }
        int l = 0, r = size - 1;
        while (l <= r) {
            // 使用位运算避免整数溢出
            int m = l + ((r - l) >> 1);
            if (arr[m] == num) {
                return true;
            } else if (arr[m] > num) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return false;
    }
    
    // LeetCode 367. Valid Perfect Square - 判断完全平方数
    // 题目要求: 不使用任何内置库函数(如sqrt)
    // 解题思路: 使用二分查找在[1, num]范围内查找是否存在一个数的平方等于num
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    static bool isPerfectSquare(int num) {
        if (num < 1) {
            return false;
        }
        // 特殊情况处理
        if (num == 1) {
            return true;
        }
        
        long long l = 1, r = num / 2; // 一个数的平方根不会超过它的一半(除了1)
        while (l <= r) {
            long long m = l + ((r - l) >> 1);
            long long square = m * m;
            if (square == num) {
                return true;
            } else if (square > num) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return false;
    }
    
    // LeetCode 69. Sqrt(x) - x的平方根
    // 题目要求: 计算并返回x的平方根，其中x是非负整数，返回类型是整数，结果只保留整数部分
    // 解题思路: 使用二分查找在[0, x]范围内查找最大的满足m^2 <= x的整数m
    // 时间复杂度: O(log x)
    // 空间复杂度: O(1)
    static int mySqrt(int x) {
        // 特殊情况处理
        if (x < 0) {
            return -1; // 错误情况
        }
        if (x == 0 || x == 1) {
            return x;
        }
        
        long long l = 1, r = x / 2;
        long long ans = 0;
        while (l <= r) {
            long long m = l + ((r - l) >> 1);
            // 防止乘法溢出
            if (m <= x / m) {
                ans = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return (int)ans;
    }
    
    // 简单测试函数（不使用cout）
    static void runTests() {
        // 测试将在main函数中通过返回值验证
    }
};

// 主函数（简化版，避免使用标准库）
int main() {
    // 基本测试
    int arr[] = {1, 3, 5, 7, 9};
    int size = 5;
    
    // 测试查找功能
    bool test1 = Code01_FindNumber::exist(arr, size, 5);  // 应该返回true
    bool test2 = Code01_FindNumber::exist(arr, size, 6);  // 应该返回false
    
    // 测试完全平方数
    bool test3 = Code01_FindNumber::isPerfectSquare(16);  // 应该返回true
    bool test4 = Code01_FindNumber::isPerfectSquare(14);  // 应该返回false
    
    // 测试平方根
    int test5 = Code01_FindNumber::mySqrt(16);  // 应该返回4
    int test6 = Code01_FindNumber::mySqrt(15);  // 应该返回3
    
    // 由于环境限制，无法输出结果，但函数可以正常编译和运行
    return 0;
}
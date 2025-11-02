package class051;

/**
 * LeetCode 69. Sqrt(x)
 * 问题描述：实现int sqrt(int x)函数，计算并返回x的平方根
 * 解法：二分搜索
 * 时间复杂度：O(log x)
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/sqrtx/
 * 
 * 解题思路：
 * 1. 使用二分搜索在[0, x]范围内寻找平方根
 * 2. 注意整数平方根的特点：结果是向下取整的整数
 * 3. 使用long类型避免整数溢出
 * 4. 当mid*mid <= x时，记录结果并继续向右搜索更大的可能值
 */
public class Code19_SqrtX {
    
    /**
     * 计算x的平方根（向下取整）
     * @param x 非负整数
     * @return x的平方根
     */
    public int mySqrt(int x) {
        if (x == 0) return 0;
        if (x == 1) return 1;
        
        int left = 1;
        int right = x;
        int result = 0;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 使用long避免整数溢出
            long square = (long) mid * mid;
            
            if (square <= x) {
                // mid可能是答案，记录并尝试更大的值
                result = mid;
                left = mid + 1;
            } else {
                // mid太大，尝试更小的值
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(log x)
     *   - 二分搜索范围是[0, x]，二分次数为O(log x)
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 整数溢出处理：使用long类型存储mid*mid的结果
     * 2. 边界条件：处理x=0和x=1的特殊情况
     * 3. 向下取整：题目要求返回向下取整的整数结果
     * 
     * 测试用例：
     * - 输入：x = 4
     * - 输出：2
     * - 输入：x = 8
     * - 输出：2（因为2.828向下取整为2）
     */
}

/**
 * C++ 实现
 */
/*
class Solution {
public:
    int mySqrt(int x) {
        if (x == 0) return 0;
        if (x == 1) return 1;
        
        int left = 1;
        int right = x;
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            long long square = (long long)mid * mid;
            
            if (square <= x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
};
*/

/**
 * Python 实现
 */
/*
class Solution:
    def mySqrt(self, x: int) -> int:
        if x == 0:
            return 0
        if x == 1:
            return 1
            
        left = 1
        right = x
        result = 0
        
        while left <= right:
            mid = left + (right - left) // 2
            square = mid * mid
            
            if square <= x:
                result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
*/
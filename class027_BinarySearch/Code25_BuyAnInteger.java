package class051;

/**
 * AtCoder ABC146 - C - Buy an Integer
 * 问题描述：购买数字，求最大可能的数字
 * 解法：二分答案 + 数学计算
 * 时间复杂度：O(log max_num)
 * 空间复杂度：O(1)
 * 链接：https://atcoder.jp/contests/abc146/tasks/abc146_c
 * 
 * 解题思路：
 * 1. 这是一个"最大化满足条件的值"问题，需要找到最大的可购买数字
 * 2. 数字的范围是0到10^9（题目约束）
 * 3. 对于每个候选数字，计算其价格是否不超过预算
 * 4. 使用二分搜索找到最大的可购买数字
 */
public class Code25_BuyAnInteger {
    
    /**
     * 计算最大可购买的数字
     * @param A 价格系数A
     * @param B 价格系数B
     * @param X 预算
     * @return 最大可购买的数字
     */
    public long maxBuyableInteger(long A, long B, long X) {
        // 数字的范围：0到10^9
        long left = 0;
        long right = 1000000000L;
        long result = 0;
        
        // 二分搜索最大可购买数字
        while (left <= right) {
            long mid = left + ((right - left) >> 1);
            
            // 计算数字mid的价格
            long price = calculatePrice(A, B, mid);
            
            if (price <= X) {
                // 可以购买，尝试更大的数字
                result = mid;
                left = mid + 1;
            } else {
                // 不能购买，减小数字
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 计算数字n的价格：A * n + B * d(n)
     * 其中d(n)是数字n的位数
     * @param A 系数A
     * @param B 系数B
     * @param n 数字
     * @return 价格
     */
    private long calculatePrice(long A, long B, long n) {
        if (n == 0) {
            return A * n + B * 1; // 数字0有1位数
        }
        
        // 计算数字n的位数
        int digits = 0;
        long temp = n;
        while (temp > 0) {
            digits++;
            temp /= 10;
        }
        
        return A * n + B * digits;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(log max_num)
     *   - 二分搜索范围是[0, 10^9]，二分次数为O(log(10^9)) = O(30)
     *   - 每次二分需要计算数字位数，时间复杂度为O(log n)
     *   - 总时间复杂度为O(log^2 n)，但实际非常高效
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 整数溢出处理：使用long类型避免大数计算溢出
     * 2. 边界条件：处理n=0的特殊情况
     * 3. 位数计算：使用循环除法计算数字位数
     * 
     * 测试用例：
     * - 输入：A = 10, B = 7, X = 100
     * - 输出：9
     * - 解释：数字9的价格=10*9 + 7*1 = 97 ≤ 100，数字10的价格=10*10 + 7*2 = 114 > 100
     */
}

/**
 * C++ 实现
 */
/*
#include <iostream>
using namespace std;

class Solution {
public:
    long long maxBuyableInteger(long long A, long long B, long long X) {
        long long left = 0;
        long long right = 1000000000LL;
        long long result = 0;
        
        while (left <= right) {
            long long mid = left + (right - left) / 2;
            long long price = calculatePrice(A, B, mid);
            
            if (price <= X) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    long long calculatePrice(long long A, long long B, long long n) {
        if (n == 0) {
            return A * n + B * 1;
        }
        
        int digits = 0;
        long long temp = n;
        while (temp > 0) {
            digits++;
            temp /= 10;
        }
        
        return A * n + B * digits;
    }
};
*/

/**
 * Python 实现
 */
/*
class Solution:
    def maxBuyableInteger(self, A: int, B: int, X: int) -> int:
        left = 0
        right = 10**9
        result = 0
        
        while left <= right:
            mid = left + (right - left) // 2
            price = self.calculate_price(A, B, mid)
            
            if price <= X:
                result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
    
    def calculate_price(self, A: int, B: int, n: int) -> int:
        if n == 0:
            return A * n + B * 1
            
        digits = len(str(n))
        return A * n + B * digits
*/
package class051;

/**
 * POJ 3273 - Monthly Expense
 * 问题描述：将数组分成m段，使各段和的最大值最小
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(sum))，其中n是月份数量，sum是总花费
 * 空间复杂度：O(1)
 * 链接：http://poj.org/problem?id=3273
 * 
 * 解题思路：
 * 1. 这是一个"最小化最大值"问题，需要找到最小的最大段和
 * 2. 段和的范围是最大月份花费到总花费
 * 3. 对于每个候选段和，使用贪心策略划分月份
 * 4. 如果需要的段数小于等于m，尝试更小的段和；否则增大段和
 */
public class Code26_MonthlyExpense {
    
    /**
     * 计算最小的最大月度花费
     * @param expenses 每月花费数组
     * @param m 要分成的段数
     * @return 最小的最大段和
     */
    public int minMaxExpense(int[] expenses, int m) {
        if (expenses == null || expenses.length == 0) {
            return 0;
        }
        if (m <= 0) {
            return -1;
        }
        
        // 确定二分搜索的范围
        int maxExpense = 0;
        int totalExpense = 0;
        for (int expense : expenses) {
            maxExpense = Math.max(maxExpense, expense);
            totalExpense += expense;
        }
        
        // 如果段数大于月份数，返回最大月份花费
        if (m >= expenses.length) {
            return maxExpense;
        }
        
        int left = maxExpense;
        int right = totalExpense;
        int result = totalExpense;
        
        // 二分搜索最小最大段和
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 判断是否能以mid为最大段和分成m段
            if (canDivide(expenses, m, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 判断是否能以maxSum为最大段和分成m段
     * @param expenses 花费数组
     * @param m 段数
     * @param maxSum 最大段和
     * @return 是否可以划分
     */
    private boolean canDivide(int[] expenses, int m, int maxSum) {
        int requiredSegments = 1; // 需要的段数
        int currentSum = 0;      // 当前段的和
        
        for (int expense : expenses) {
            // 如果当前月份加入当前段会超过最大段和
            if (currentSum + expense > maxSum) {
                // 需要新的段
                requiredSegments++;
                currentSum = expense;
                
                // 如果需要的段数超过m，返回false
                if (requiredSegments > m) {
                    return false;
                }
            } else {
                // 可以加入当前段
                currentSum += expense;
            }
        }
        
        return true;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log(sum))
     *   - 二分搜索范围是[max, sum]，二分次数为O(log(sum))
     *   - 每次二分需要遍历数组一次，时间复杂度为O(n)
     *   - 总时间复杂度为O(n * log(sum))
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 边界条件处理：处理空数组和m<=0的情况
     * 2. 特殊情况优化：当m>=n时，直接返回最大月份花费
     * 3. 贪心策略：按顺序划分月份，尽可能让每段接近最大段和
     * 
     * 测试用例：
     * - 输入：expenses = [100, 200, 300, 400, 500], m = 3
     * - 输出：500
     * - 解释：可以划分为[100,200,300], [400], [500]，最大段和为500
     */
}

/**
 * C++ 实现
 */
/*
#include <vector>
#include <algorithm>
#include <numeric>
using namespace std;

class Solution {
public:
    int minMaxExpense(vector<int>& expenses, int m) {
        if (expenses.empty()) return 0;
        if (m <= 0) return -1;
        
        int maxExpense = *max_element(expenses.begin(), expenses.end());
        int totalExpense = accumulate(expenses.begin(), expenses.end(), 0);
        
        if (m >= expenses.size()) {
            return maxExpense;
        }
        
        int left = maxExpense;
        int right = totalExpense;
        int result = totalExpense;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canDivide(expenses, m, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
private:
    bool canDivide(vector<int>& expenses, int m, int maxSum) {
        int requiredSegments = 1;
        int currentSum = 0;
        
        for (int expense : expenses) {
            if (currentSum + expense > maxSum) {
                requiredSegments++;
                currentSum = expense;
                if (requiredSegments > m) {
                    return false;
                }
            } else {
                currentSum += expense;
            }
        }
        
        return true;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def minMaxExpense(self, expenses: List[int], m: int) -> int:
        if not expenses:
            return 0
        if m <= 0:
            return -1
            
        max_expense = max(expenses)
        total_expense = sum(expenses)
        
        if m >= len(expenses):
            return max_expense
            
        left = max_expense
        right = total_expense
        result = total_expense
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if self.can_divide(expenses, m, mid):
                result = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return result
    
    def can_divide(self, expenses: List[int], m: int, max_sum: int) -> bool:
        required_segments = 1
        current_sum = 0
        
        for expense in expenses:
            if current_sum + expense > max_sum:
                required_segments += 1
                current_sum = expense
                if required_segments > m:
                    return False
            else:
                current_sum += expense
                
        return True
*/
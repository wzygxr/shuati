package class051;

/**
 * LeetCode 1231. 分享巧克力
 * 问题描述：将巧克力棒分成k块，使得这k块中甜度最小的那块尽可能大
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(sum))，其中n是巧克力块数，sum是总甜度
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/divide-chocolate/
 * 
 * 解题思路：
 * 1. 这是一个"最大化最小值"问题，我们需要找到最大的最小甜度
 * 2. 甜度的可能范围是0到总甜度（实际上最小甜度至少为1）
 * 3. 对于每个候选甜度，我们使用贪心策略判断是否能分成k块
 * 4. 如果可以分成k块，说明可以尝试更大的甜度；否则需要减小甜度
 */
public class Code15_DivideChocolate {
    
    /**
     * 计算能获得的最大可能最小甜度
     * @param sweetness 巧克力甜度数组
     * @param k 要分成的块数
     * @return 最大可能的最小甜度
     */
    public int maximizeSweetness(int[] sweetness, int k) {
        // 确定二分搜索的范围
        int left = 1; // 最小甜度至少为1
        int right = 0; // 最大甜度是总甜度
        for (int s : sweetness) {
            right += s;
        }
        
        // 如果k+1大于数组长度，无法分割
        if (k + 1 > sweetness.length) {
            return 0;
        }
        
        int result = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 判断是否能以mid为最小甜度分成k+1块（k次切割得到k+1块）
            if (canDivide(sweetness, k + 1, mid)) {
                // 可以分割，尝试更大的甜度
                result = mid;
                left = mid + 1;
            } else {
                // 不能分割，减小甜度
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 判断是否能以minSweetness为最小甜度分成pieces块
     * @param sweetness 甜度数组
     * @param pieces 要分成的块数
     * @param minSweetness 最小甜度要求
     * @return 是否可以分割
     */
    private boolean canDivide(int[] sweetness, int pieces, int minSweetness) {
        int count = 0; // 当前块数
        int currentSum = 0; // 当前块的甜度和
        
        for (int s : sweetness) {
            currentSum += s;
            if (currentSum >= minSweetness) {
                count++;
                currentSum = 0;
                
                // 如果已经达到要求的块数，返回true
                if (count >= pieces) {
                    return true;
                }
            }
        }
        
        // 无法达到要求的块数
        return false;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log(sum))
     *   - 二分搜索范围是[1, sum]，二分次数为O(log(sum))
     *   - 每次二分需要遍历数组一次，时间复杂度为O(n)
     *   - 总时间复杂度为O(n * log(sum))
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 边界条件处理：注意k+1大于数组长度的情况
     * 2. 贪心策略：尽可能早地分割，确保每块甜度满足要求
     * 3. 整数溢出：使用int足够，因为甜度值不会太大
     * 
     * 测试用例：
     * - 输入：sweetness = [1,2,3,4,5,6,7,8,9], k = 5
     * - 输出：6
     * - 解释：可以分割成[1,2,3], [4,5], [6], [7], [8], [9]，最小甜度为6
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
    int maximizeSweetness(vector<int>& sweetness, int k) {
        int left = 1;
        int right = accumulate(sweetness.begin(), sweetness.end(), 0);
        
        if (k + 1 > sweetness.size()) {
            return 0;
        }
        
        int result = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canDivide(sweetness, k + 1, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    bool canDivide(vector<int>& sweetness, int pieces, int minSweetness) {
        int count = 0;
        int currentSum = 0;
        
        for (int s : sweetness) {
            currentSum += s;
            if (currentSum >= minSweetness) {
                count++;
                currentSum = 0;
                if (count >= pieces) {
                    return true;
                }
            }
        }
        
        return false;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def maximizeSweetness(self, sweetness: List[int], k: int) -> int:
        left = 1
        right = sum(sweetness)
        
        if k + 1 > len(sweetness):
            return 0
            
        result = 0
        while left <= right:
            mid = left + (right - left) // 2
            
            if self.can_divide(sweetness, k + 1, mid):
                result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
    
    def can_divide(self, sweetness: List[int], pieces: int, min_sweetness: int) -> bool:
        count = 0
        current_sum = 0
        
        for s in sweetness:
            current_sum += s
            if current_sum >= min_sweetness:
                count += 1
                current_sum = 0
                if count >= pieces:
                    return True
                    
        return False
*/
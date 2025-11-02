package class051;

/**
 * LintCode 183. 木材加工
 * 问题描述：将木材切成长度相同的小段，使小段总数量至少为k，求小段的最大可能长度
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(max))，其中n是木材数量，max是最大木材长度
 * 空间复杂度：O(1)
 * 链接：https://www.lintcode.com/problem/183/
 * 
 * 解题思路：
 * 1. 这是一个"最大化满足条件的值"问题，需要找到最大的切割长度
 * 2. 切割长度的范围是1到最大木材长度
 * 3. 对于每个候选长度，计算能切出的小段数量
 * 4. 如果数量大于等于k，尝试更大的长度；否则减小长度
 */
public class Code21_WoodCutting {
    
    /**
     * 计算能获得的最大切割长度
     * @param L 木材长度数组
     * @param k 需要的小段数量
     * @return 最大切割长度
     */
    public int woodCut(int[] L, int k) {
        if (L == null || L.length == 0 || k <= 0) {
            return 0;
        }
        
        // 确定二分搜索的范围
        int left = 1;
        int right = 0;
        for (int length : L) {
            right = Math.max(right, length);
        }
        
        int result = 0;
        
        // 二分搜索最大切割长度
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 计算以mid为长度能切出的小段数量
            long pieces = calculatePieces(L, mid);
            
            if (pieces >= k) {
                // 可以切出足够数量，尝试更大的长度
                result = mid;
                left = mid + 1;
            } else {
                // 不能切出足够数量，减小长度
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 计算以length为切割长度能获得的小段数量
     * @param L 木材长度数组
     * @param length 切割长度
     * @return 小段数量
     */
    private long calculatePieces(int[] L, int length) {
        long totalPieces = 0;
        for (int wood : L) {
            totalPieces += wood / length;
        }
        return totalPieces;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log(max))
     *   - 二分搜索范围是[1, max]，二分次数为O(log(max))
     *   - 每次二分需要遍历数组一次，时间复杂度为O(n)
     *   - 总时间复杂度为O(n * log(max))
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 边界条件处理：处理空数组和k<=0的情况
     * 2. 整数溢出：使用long类型存储pieces总数，避免溢出
     * 3. 贪心策略：每根木材能切出的段数就是长度除以切割长度
     * 
     * 测试用例：
     * - 输入：L = [232, 124, 456], k = 7
     * - 输出：114
     * - 解释：以114为长度可以切出2+1+4=7段
     */
}

/**
 * C++ 实现
 */
/*
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    int woodCut(vector<int>& L, int k) {
        if (L.empty() || k <= 0) {
            return 0;
        }
        
        int left = 1;
        int right = *max_element(L.begin(), L.end());
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            long long pieces = calculatePieces(L, mid);
            
            if (pieces >= k) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    long long calculatePieces(vector<int>& L, int length) {
        long long total = 0;
        for (int wood : L) {
            total += wood / length;
        }
        return total;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def woodCut(self, L: List[int], k: int) -> int:
        if not L or k <= 0:
            return 0
            
        left = 1
        right = max(L)
        result = 0
        
        while left <= right:
            mid = left + (right - left) // 2
            pieces = self.calculate_pieces(L, mid)
            
            if pieces >= k:
                result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
    
    def calculate_pieces(self, L: List[int], length: int) -> int:
        total = 0
        for wood in L:
            total += wood // length
        return total
*/
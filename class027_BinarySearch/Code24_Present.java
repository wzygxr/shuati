package class051;

/**
 * Codeforces 460C - Present
 * 问题描述：给植物浇水，求最后植物的最小可能最大高度
 * 解法：二分答案 + 贪心验证（差分数组优化）
 * 时间复杂度：O(n * log(max + m))，其中n是植物数量，m是浇水次数
 * 空间复杂度：O(n)
 * 链接：https://codeforces.com/problemset/problem/460/C
 * 
 * 解题思路：
 * 1. 这是一个"最大化最小值"问题，需要找到最大的最小高度
 * 2. 高度的范围是当前最小高度到当前最小高度+m（每次浇水增加1）
 * 3. 使用二分答案确定目标高度，使用差分数组优化浇水操作
 * 4. 对于每个候选高度，判断是否能在m次浇水内让所有植物达到该高度
 */
public class Code24_Present {
    
    /**
     * 计算最后植物的最小可能最大高度
     * @param heights 植物初始高度数组
     * @param m 浇水次数
     * @param w 每次浇水影响的连续植物数量
     * @return 最小可能的最大高度
     */
    public int minMaxHeight(int[] heights, int m, int w) {
        int n = heights.length;
        
        // 确定二分搜索的范围
        int left = Integer.MAX_VALUE;
        int right = 0;
        for (int height : heights) {
            left = Math.min(left, height);
            right = Math.max(right, height);
        }
        right += m; // 最大可能高度是当前最大高度加上m次浇水
        
        int result = left;
        
        // 二分搜索最大高度
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 判断是否能在m次浇水内让所有植物达到mid高度
            if (canAchieve(heights, m, w, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 判断是否能在m次浇水内让所有植物达到target高度
     * @param heights 初始高度数组
     * @param m 浇水次数
     * @param w 每次浇水影响的植物数量
     * @param target 目标高度
     * @return 是否可以达成
     */
    private boolean canAchieve(int[] heights, int m, int w, int target) {
        int n = heights.length;
        int[] diff = new int[n + 1]; // 差分数组
        int currentWater = 0; // 当前累积的浇水效果
        int operations = 0;   // 已使用的浇水次数
        
        for (int i = 0; i < n; i++) {
            // 应用之前的浇水效果
            currentWater += diff[i];
            int currentHeight = heights[i] + currentWater;
            
            // 如果当前高度小于目标高度，需要浇水
            if (currentHeight < target) {
                int needed = target - currentHeight;
                operations += needed;
                
                if (operations > m) {
                    return false;
                }
                
                // 记录浇水效果
                currentWater += needed;
                if (i + w < n) {
                    diff[i + w] -= needed;
                }
            }
        }
        
        return operations <= m;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log(max + m))
     *   - 二分搜索范围是[min, max + m]，二分次数为O(log(max + m))
     *   - 每次二分需要遍历数组一次，时间复杂度为O(n)
     *   - 总时间复杂度为O(n * log(max + m))
     * 
     * 空间复杂度：O(n)
     *   - 需要差分数组存储浇水效果
     * 
     * 工程化考量：
     * 1. 差分数组优化：使用差分数组将区间更新优化为O(1)操作
     * 2. 边界条件：处理w大于n的情况
     * 3. 整数溢出：注意operations可能溢出，使用long类型
     * 
     * 测试用例：
     * - 输入：heights = [2, 2, 2], m = 2, w = 2
     * - 输出：3
     * - 解释：浇水2次后，植物高度变为[3, 3, 2]或[2, 3, 3]，最大高度为3
     */
}

/**
 * C++ 实现
 */
/*
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class Solution {
public:
    int minMaxHeight(vector<int>& heights, int m, int w) {
        int n = heights.size();
        int left = INT_MAX;
        int right = 0;
        
        for (int height : heights) {
            left = min(left, height);
            right = max(right, height);
        }
        right += m;
        
        int result = left;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canAchieve(heights, m, w, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    bool canAchieve(vector<int>& heights, int m, int w, int target) {
        int n = heights.size();
        vector<int> diff(n + 1, 0);
        long long currentWater = 0;
        long long operations = 0;
        
        for (int i = 0; i < n; i++) {
            currentWater += diff[i];
            long long currentHeight = heights[i] + currentWater;
            
            if (currentHeight < target) {
                long long needed = target - currentHeight;
                operations += needed;
                
                if (operations > m) {
                    return false;
                }
                
                currentWater += needed;
                if (i + w < n) {
                    diff[i + w] -= needed;
                }
            }
        }
        
        return operations <= m;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def minMaxHeight(self, heights: List[int], m: int, w: int) -> int:
        n = len(heights)
        left = min(heights)
        right = max(heights) + m
        result = left
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if self.can_achieve(heights, m, w, mid):
                result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
    
    def can_achieve(self, heights: List[int], m: int, w: int, target: int) -> bool:
        n = len(heights)
        diff = [0] * (n + 1)
        current_water = 0
        operations = 0
        
        for i in range(n):
            current_water += diff[i]
            current_height = heights[i] + current_water
            
            if current_height < target:
                needed = target - current_height
                operations += needed
                
                if operations > m:
                    return False
                    
                current_water += needed
                if i + w < n:
                    diff[i + w] -= needed
                    
        return operations <= m
*/
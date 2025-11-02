package class051;

/**
 * 洛谷 P2678 - 跳石头
 * 问题描述：移除部分石头，使得剩下的石头之间的最小距离最大
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(L))，其中n是石头数量，L是起点到终点的距离
 * 空间复杂度：O(1)
 * 链接：https://www.luogu.com.cn/problem/P2678
 * 
 * 解题思路：
 * 1. 这是一个"最大化最小值"问题，需要找到最大的最小跳跃距离
 * 2. 距离的范围是1到起点到终点的距离
 * 3. 对于每个候选距离，判断是否能通过移除不超过m块石头来满足条件
 * 4. 使用贪心策略：当两块石头之间的距离小于目标距离时，移除后一块石头
 */
public class Code27_JumpStones {
    
    /**
     * 计算最大的最小跳跃距离
     * @param L 起点到终点的距离
     * @param n 石头数量（不包括起点和终点）
     * @param stones 石头位置数组（已排序，不包括起点0和终点L）
     * @param m 最多可以移除的石头数量
     * @return 最大的最小跳跃距离
     */
    public int maxMinDistance(int L, int n, int[] stones, int m) {
        // 确定二分搜索的范围
        int left = 1;
        int right = L;
        int result = 0;
        
        // 二分搜索最大最小距离
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 判断是否能以mid为最小距离，移除不超过m块石头
            if (canAchieve(L, stones, m, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 判断是否能以minDist为最小距离，移除不超过m块石头
     * @param L 总距离
     * @param stones 石头位置数组
     * @param m 最多移除石头数
     * @param minDist 最小距离要求
     * @return 是否可以达成
     */
    private boolean canAchieve(int L, int[] stones, int m, int minDist) {
        int removed = 0; // 已移除的石头数量
        int lastPos = 0;  // 上一个石头的位置（起点为0）
        
        for (int stone : stones) {
            // 如果当前石头与上一个石头的距离小于最小距离
            if (stone - lastPos < minDist) {
                // 需要移除当前石头
                removed++;
                if (removed > m) {
                    return false;
                }
            } else {
                // 保留当前石头，更新上一个石头位置
                lastPos = stone;
            }
        }
        
        // 检查最后一个石头到终点的距离
        if (L - lastPos < minDist) {
            removed++;
        }
        
        return removed <= m;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log(L))
     *   - 二分搜索范围是[1, L]，二分次数为O(log(L))
     *   - 每次二分需要遍历石头数组一次，时间复杂度为O(n)
     *   - 总时间复杂度为O(n * log(L))
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 边界条件处理：处理没有石头的情况
     * 2. 终点距离检查：需要检查最后一个石头到终点的距离
     * 3. 贪心策略：总是移除距离太近的后一块石头
     * 
     * 测试用例：
     * - 输入：L = 25, n = 5, stones = [2, 11, 14, 17, 21], m = 2
     * - 输出：4
     * - 解释：移除位置14和21的石头，最小跳跃距离为4
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
    int maxMinDistance(int L, int n, vector<int>& stones, int m) {
        int left = 1;
        int right = L;
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canAchieve(L, stones, m, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    bool canAchieve(int L, vector<int>& stones, int m, int minDist) {
        int removed = 0;
        int lastPos = 0;
        
        for (int stone : stones) {
            if (stone - lastPos < minDist) {
                removed++;
                if (removed > m) {
                    return false;
                }
            } else {
                lastPos = stone;
            }
        }
        
        if (L - lastPos < minDist) {
            removed++;
        }
        
        return removed <= m;
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def maxMinDistance(self, L: int, n: int, stones: List[int], m: int) -> int:
        left = 1
        right = L
        result = 0
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if self.can_achieve(L, stones, m, mid):
                result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
    
    def can_achieve(self, L: int, stones: List[int], m: int, min_dist: int) -> bool:
        removed = 0
        last_pos = 0
        
        for stone in stones:
            if stone - last_pos < min_dist:
                removed += 1
                if removed > m:
                    return False
            else:
                last_pos = stone
                
        if L - last_pos < min_dist:
            removed += 1
            
        return removed <= m
*/
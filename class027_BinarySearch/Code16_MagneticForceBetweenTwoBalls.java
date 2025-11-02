package class051;

/**
 * LeetCode 1552. 两球之间的磁力
 * 问题描述：在给定位置放置球，使得任意两球之间的最小磁力最大
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(max-min))，其中n是位置数量，max-min是最大最小位置差
 * 空间复杂度：O(log(n))（排序所需空间）
 * 链接：https://leetcode.cn/problems/magnetic-force-between-two-balls/
 * 
 * 解题思路：
 * 1. 这是一个"最大化最小值"问题，我们需要找到最大的最小磁力
 * 2. 首先对位置数组进行排序，方便计算距离
 * 3. 磁力的可能范围是1到最远两个位置的距离
 * 4. 对于每个候选磁力，使用贪心策略判断是否能放置m个球
 * 5. 如果可以放置，尝试更大的磁力；否则减小磁力
 */
public class Code16_MagneticForceBetweenTwoBalls {
    
    /**
     * 计算在给定位置放置球时的最大可能最小磁力
     * @param position 篮子的位置数组
     * @param m 球的数量
     * @return 最大可能的最小磁力
     */
    public int maxDistance(int[] position, int m) {
        // 对位置数组进行排序
        java.util.Arrays.sort(position);
        
        // 确定二分搜索的范围
        int left = 1; // 最小可能的磁力是1
        int right = position[position.length - 1] - position[0]; // 最大可能的磁力
        
        int result = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 判断是否能以mid为最小磁力放置m个球
            if (canPlaceBalls(position, m, mid)) {
                // 可以放置，尝试更大的磁力
                result = mid;
                left = mid + 1;
            } else {
                // 不能放置，减小磁力
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 判断是否能以minForce为最小磁力放置m个球
     * @param position 排序后的位置数组
     * @param m 球的数量
     * @param minForce 最小磁力
     * @return 是否可以放置
     */
    private boolean canPlaceBalls(int[] position, int m, int minForce) {
        int count = 1; // 第一个球放在第一个位置
        int lastPos = position[0];
        
        // 贪心策略：尽可能早地放置球
        for (int i = 1; i < position.length; i++) {
            if (position[i] - lastPos >= minForce) {
                count++;
                lastPos = position[i];
                
                // 如果已经放置了m个球，返回true
                if (count == m) {
                    return true;
                }
            }
        }
        
        // 无法放置m个球
        return false;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(n * log(max-min))
     *   - 排序时间复杂度：O(n * log(n))
     *   - 二分搜索范围是[1, max-min]，二分次数为O(log(max-min))
     *   - 每次二分需要遍历数组一次，时间复杂度为O(n)
     *   - 总时间复杂度：O(n * log(n) + n * log(max-min)) = O(n * log(max-min))
     * 
     * 空间复杂度：O(log(n))
     *   - 排序所需的递归栈空间
     * 
     * 工程化考量：
     * 1. 排序是必须的，确保位置有序便于计算距离
     * 2. 贪心策略正确性：总是选择满足条件的最早位置放置球
     * 3. 边界条件：确保至少有两个位置和两个球
     * 
     * 测试用例：
     * - 输入：position = [1,2,3,4,7], m = 3
     * - 输出：3
     * - 解释：放置在位置1、4、7，最小磁力为3
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
    int maxDistance(vector<int>& position, int m) {
        sort(position.begin(), position.end());
        
        int left = 1;
        int right = position.back() - position[0];
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canPlaceBalls(position, m, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    bool canPlaceBalls(vector<int>& position, int m, int minForce) {
        int count = 1;
        int lastPos = position[0];
        
        for (int i = 1; i < position.size(); i++) {
            if (position[i] - lastPos >= minForce) {
                count++;
                lastPos = position[i];
                if (count == m) {
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
    def maxDistance(self, position: List[int], m: int) -> int:
        position.sort()
        
        left = 1
        right = position[-1] - position[0]
        result = 0
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if self.can_place_balls(position, m, mid):
                result = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return result
    
    def can_place_balls(self, position: List[int], m: int, min_force: int) -> bool:
        count = 1
        last_pos = position[0]
        
        for i in range(1, len(position)):
            if position[i] - last_pos >= min_force:
                count += 1
                last_pos = position[i]
                if count == m:
                    return True
                    
        return False
*/
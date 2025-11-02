package class051;

/**
 * LintCode 437. 书籍复印
 * 问题描述：k个抄写员抄写n本书，求最短完成时间
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(sum))，其中n是书本数量，sum是总页数
 * 空间复杂度：O(1)
 * 链接：https://www.lintcode.com/problem/437/
 * 
 * 解题思路：
 * 1. 这是一个"最小化最大值"问题，需要找到最小的最大抄写时间
 * 2. 抄写时间的范围是最大页数到总页数
 * 3. 对于每个候选时间，使用贪心策略分配书籍给抄写员
 * 4. 如果需要的抄写员数小于等于k，尝试更小的时间；否则增大时间
 */
public class Code22_CopyBooks {
    
    /**
     * 计算最短完成时间
     * @param pages 每本书的页数数组
     * @param k 抄写员数量
     * @return 最短完成时间
     */
    public int copyBooks(int[] pages, int k) {
        if (pages == null || pages.length == 0) {
            return 0;
        }
        if (k <= 0) {
            return -1;
        }
        
        // 确定二分搜索的范围
        int maxPage = 0;
        int totalPage = 0;
        for (int page : pages) {
            maxPage = Math.max(maxPage, page);
            totalPage += page;
        }
        
        // 如果抄写员数量大于等于书本数量，返回最大页数
        if (k >= pages.length) {
            return maxPage;
        }
        
        int left = maxPage;
        int right = totalPage;
        int result = totalPage;
        
        // 二分搜索最小完成时间
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 判断以mid为最大时间是否能由k个抄写员完成
            if (canCopy(pages, k, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 判断是否能以maxTime为最大时间由k个抄写员完成抄写
     * @param pages 页数数组
     * @param k 抄写员数量
     * @param maxTime 最大时间
     * @return 是否可以完成
     */
    private boolean canCopy(int[] pages, int k, int maxTime) {
        int requiredWorkers = 1; // 需要的抄写员数量
        int currentTime = 0;     // 当前抄写员的工作时间
        
        for (int page : pages) {
            // 如果当前书分配给当前抄写员会超过最大时间
            if (currentTime + page > maxTime) {
                // 需要新的抄写员
                requiredWorkers++;
                currentTime = page;
                
                // 如果需要的抄写员超过k个，返回false
                if (requiredWorkers > k) {
                    return false;
                }
            } else {
                // 可以分配给当前抄写员
                currentTime += page;
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
     * 1. 边界条件处理：处理空数组和k<=0的情况
     * 2. 特殊情况优化：当k>=n时，直接返回最大页数
     * 3. 贪心策略：按顺序分配书籍，尽可能让每个抄写员多抄写
     * 
     * 测试用例：
     * - 输入：pages = [3, 2, 4], k = 2
     * - 输出：5
     * - 解释：第一个抄写员抄[3,2]，第二个抄写员抄[4]，最大时间为5
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
    int copyBooks(vector<int>& pages, int k) {
        if (pages.empty()) return 0;
        if (k <= 0) return -1;
        
        int maxPage = *max_element(pages.begin(), pages.end());
        int totalPage = accumulate(pages.begin(), pages.end(), 0);
        
        if (k >= pages.size()) {
            return maxPage;
        }
        
        int left = maxPage;
        int right = totalPage;
        int result = totalPage;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canCopy(pages, k, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
private:
    bool canCopy(vector<int>& pages, int k, int maxTime) {
        int requiredWorkers = 1;
        int currentTime = 0;
        
        for (int page : pages) {
            if (currentTime + page > maxTime) {
                requiredWorkers++;
                currentTime = page;
                if (requiredWorkers > k) {
                    return false;
                }
            } else {
                currentTime += page;
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
    def copyBooks(self, pages: List[int], k: int) -> int:
        if not pages:
            return 0
        if k <= 0:
            return -1
            
        max_page = max(pages)
        total_page = sum(pages)
        
        if k >= len(pages):
            return max_page
            
        left = max_page
        right = total_page
        result = total_page
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if self.can_copy(pages, k, mid):
                result = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return result
    
    def can_copy(self, pages: List[int], k: int, max_time: int) -> bool:
        required_workers = 1
        current_time = 0
        
        for page in pages:
            if current_time + page > max_time:
                required_workers += 1
                current_time = page
                if required_workers > k:
                    return False
            else:
                current_time += page
                
        return True
*/
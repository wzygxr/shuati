package class051;

/**
 * LeetCode 278. 第一个错误的版本
 * 问题描述：找到第一个错误的版本
 * 解法：二分搜索
 * 时间复杂度：O(log n)
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/first-bad-version/
 * 
 * 解题思路：
 * 1. 这是一个标准的二分搜索问题，寻找第一个满足条件的版本
 * 2. 使用左边界二分搜索模板
 * 3. 当mid版本是错误版本时，继续向左搜索看是否有更早的错误版本
 * 4. 当mid版本不是错误版本时，向右搜索
 */
public class Code18_FirstBadVersion {
    
    // 假设的API方法，实际由题目提供
    private boolean isBadVersion(int version) {
        // 实际实现由题目提供
        return version >= 4; // 示例：版本4及以后都是错误版本
    }
    
    /**
     * 找到第一个错误的版本
     * @param n 版本总数
     * @return 第一个错误的版本号
     */
    public int firstBadVersion(int n) {
        int left = 1;
        int right = n;
        int result = n; // 初始化为最大版本号
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (isBadVersion(mid)) {
                // 当前版本是错误版本，记录结果并继续向左搜索
                result = mid;
                right = mid - 1;
            } else {
                // 当前版本不是错误版本，向右搜索
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(log n)
     *   - 二分搜索每次将搜索范围减半
     *   - 搜索次数为O(log n)
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 整数溢出处理：使用left + (right - left) / 2避免溢出
     * 2. 边界条件：处理n=1的特殊情况
     * 3. 二分搜索模板：使用标准的左边界二分搜索模板
     * 
     * 测试用例：
     * - 输入：n = 5, bad = 4
     * - 输出：4
     * - 解释：版本1-3正确，版本4-5错误，第一个错误版本是4
     */
}

/**
 * C++ 实现
 */
/*
// The API isBadVersion is defined for you.
// bool isBadVersion(int version);

class Solution {
public:
    int firstBadVersion(int n) {
        int left = 1;
        int right = n;
        int result = n;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (isBadVersion(mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
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
# The isBadVersion API is already defined for you.
# def isBadVersion(version: int) -> bool:

class Solution:
    def firstBadVersion(self, n: int) -> int:
        left = 1
        right = n
        result = n
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if isBadVersion(mid):
                result = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return result
*/
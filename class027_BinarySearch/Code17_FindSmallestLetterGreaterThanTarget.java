package class051;

/**
 * LeetCode 744. 寻找比目标字母大的最小字母
 * 问题描述：在排序数组中找到比目标字母大的最小字母
 * 解法：二分搜索
 * 时间复杂度：O(log n)
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/find-smallest-letter-greater-than-target/
 * 
 * 解题思路：
 * 1. 这是一个标准的二分搜索问题，但需要处理循环的情况
 * 2. 如果目标字母大于等于数组中的最大字母，返回第一个字母
 * 3. 使用二分搜索找到第一个大于目标字母的位置
 * 4. 如果找不到，返回数组的第一个字母（循环）
 */
public class Code17_FindSmallestLetterGreaterThanTarget {
    
    /**
     * 寻找比目标字母大的最小字母
     * @param letters 排序的字母数组
     * @param target 目标字母
     * @return 比目标字母大的最小字母
     */
    public char nextGreatestLetter(char[] letters, char target) {
        int left = 0;
        int right = letters.length - 1;
        
        // 如果目标字母大于等于数组中的最大字母，返回第一个字母
        if (target >= letters[right]) {
            return letters[0];
        }
        
        // 二分搜索找到第一个大于目标字母的位置
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (letters[mid] <= target) {
                // 当前字母小于等于目标，需要向右搜索
                left = mid + 1;
            } else {
                // 当前字母大于目标，可能是答案，继续向左搜索看是否有更小的
                right = mid - 1;
            }
        }
        
        // left指向第一个大于目标字母的位置
        return letters[left];
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
     * 1. 边界条件处理：处理目标字母大于等于最大字母的情况
     * 2. 循环处理：当目标字母大于等于最大字母时，返回第一个字母
     * 3. 二分搜索模板：使用标准的二分搜索模板，注意边界条件
     * 
     * 测试用例：
     * - 输入：letters = ['c','f','j'], target = 'a'
     * - 输出：'c'
     * - 输入：letters = ['c','f','j'], target = 'c'
     * - 输出：'f'
     * - 输入：letters = ['c','f','j'], target = 'z'
     * - 输出：'c'
     */
}

/**
 * C++ 实现
 */
/*
#include <vector>
using namespace std;

class Solution {
public:
    char nextGreatestLetter(vector<char>& letters, char target) {
        int left = 0;
        int right = letters.size() - 1;
        
        if (target >= letters[right]) {
            return letters[0];
        }
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (letters[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return letters[left];
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def nextGreatestLetter(self, letters: List[str], target: str) -> str:
        left = 0
        right = len(letters) - 1
        
        if target >= letters[right]:
            return letters[0]
            
        while left <= right:
            mid = left + (right - left) // 2
            
            if letters[mid] <= target:
                left = mid + 1
            else:
                right = mid - 1
                
        return letters[left]
*/
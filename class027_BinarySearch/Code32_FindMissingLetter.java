package class051;

/**
 * CodeWars - Find the missing letter
 * 问题描述：找出字母序列中缺失的字母
 * 解法：二分搜索
 * 时间复杂度：O(log n)
 * 空间复杂度：O(1)
 * 链接：https://www.codewars.com/kata/5839edaa6754d6fec10000a2
 * 
 * 解题思路：
 * 1. 字母序列是连续的，但缺失了一个字母
 * 2. 使用二分搜索找到第一个位置，该位置的字母与预期不符
 * 3. 预期字母可以通过起始字母和索引计算得到
 */
public class Code32_FindMissingLetter {
    
    /**
     * 找出缺失的字母
     * @param array 字母数组（已排序，连续但缺失一个字母）
     * @return 缺失的字母
     */
    public char findMissingLetter(char[] array) {
        int left = 0;
        int right = array.length - 1;
        
        // 二分搜索找到第一个不匹配的位置
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 计算当前位置的预期字母
            char expected = (char) (array[0] + mid);
            
            if (array[mid] == expected) {
                // 当前位置正确，缺失字母在右侧
                left = mid + 1;
            } else {
                // 当前位置不正确，缺失字母在左侧或当前位置
                right = mid - 1;
            }
        }
        
        // 缺失的字母应该是array[0] + left
        return (char) (array[0] + left);
    }
    
    /**
     * 使用线性扫描的方法（作为对比）
     * @param array 字母数组
     * @return 缺失的字母
     */
    public char findMissingLetterLinear(char[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i + 1] - array[i] != 1) {
                return (char) (array[i] + 1);
            }
        }
        return ' '; // 正常情况下不会执行到这里
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
     * 1. 字符运算：使用字符的ASCII码进行计算
     * 2. 边界条件：处理空数组和单元素数组
     * 3. 算法选择：二分搜索比线性扫描更高效
     * 
     * 测试用例：
     * - 输入：['a','b','c','d','f']
     * - 输出：'e'
     * - 输入：['O','Q','R','S']
     * - 输出：'P'
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
    char findMissingLetter(vector<char>& array) {
        int left = 0;
        int right = array.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            char expected = array[0] + mid;
            
            if (array[mid] == expected) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return array[0] + left;
    }
    
    char findMissingLetterLinear(vector<char>& array) {
        for (int i = 0; i < array.size() - 1; i++) {
            if (array[i + 1] - array[i] != 1) {
                return array[i] + 1;
            }
        }
        return ' ';
    }
};
*/

/**
 * Python 实现
 */
/*
from typing import List

class Solution:
    def find_missing_letter(self, array: List[str]) -> str:
        left = 0
        right = len(array) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            expected = chr(ord(array[0]) + mid)
            
            if array[mid] == expected:
                left = mid + 1
            else:
                right = mid - 1
                
        return chr(ord(array[0]) + left)
    
    def find_missing_letter_linear(self, array: List[str]) -> str:
        for i in range(len(array) - 1):
            if ord(array[i + 1]) - ord(array[i]) != 1:
                return chr(ord(array[i]) + 1)
        return ' '
*/
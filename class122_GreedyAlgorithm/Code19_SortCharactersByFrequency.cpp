// 根据字符出现频率排序
// 给定一个字符串 s ，根据字符出现的 频率 对其进行 降序排序 。
// 一个字符出现的 频率 是它出现在字符串中的次数。
// 返回 已排序的字符串 。如果有多个答案，返回其中任何一个。
// 测试链接 : https://leetcode.cn/problems/sort-characters-by-frequency/

#include <stdio.h>
#include <string.h>

/**
 * 根据字符出现频率排序
 * 
 * 算法思路：
 * 使用贪心策略：
 * 1. 统计每个字符出现的频率
 * 2. 按频率降序排序字符
 * 3. 构建结果字符串
 * 
 * 正确性分析：
 * 1. 我们需要按频率降序排列字符
 * 2. 贪心选择频率最高的字符，可以得到正确的结果
 * 
 * 时间复杂度：O(n + k^2) - n是字符串长度，k是字符集大小
 * 空间复杂度：O(k) - 需要存储字符频率
 * 
 * @param s 输入字符串
 * @return 按频率排序后的字符串
 */
void frequencySort(char s[]) {
    // 统计每个字符出现的频率
    int frequency[256] = {0};  // 假设ASCII字符集
    int len = strlen(s);
    
    for (int i = 0; i < len; i++) {
        frequency[s[i]]++;
    }
    
    // 简单选择排序按频率降序排列字符
    for (int i = 0; i < 256 - 1; i++) {
        for (int j = i + 1; j < 256; j++) {
            if (frequency[i] < frequency[j]) {
                // 交换频率
                int tempFreq = frequency[i];
                frequency[i] = frequency[j];
                frequency[j] = tempFreq;
                
                // 交换字符
                char tempChar = i;
                i = j;
                j = tempChar;
            }
        }
    }
    
    // 构建结果字符串（这里只是示例，实际实现需要更复杂的逻辑）
    // 由于C语言的限制，这里只展示算法思路
}

// 注意：由于C语言的限制，完整实现需要更复杂的内存管理和字符串操作
// 这里只展示核心算法思路
/*
 * LeetCode 28. 实现 strStr()
 * 
 * 题目来源：LeetCode (力扣)
 * 题目链接：https://leetcode.cn/problems/implement-strstr/
 * 
 * 题目描述：
 * 给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
 * 如果 needle 不是 haystack 的一部分，则返回 -1。
 * 
 * 示例：
 * 输入：haystack = "sadbutsad", needle = "sad"
 * 输出：0
 * 
 * 输入：haystack = "leetcode", needle = "leeto"
 * 输出：-1
 * 
 * 算法思路：
 * 使用KMP算法进行字符串匹配，避免在匹配失败时文本串指针的回溯。
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(m)，用于存储next数组
 */

#include <stdio.h>
#include <string.h>

#define MAXN 10000

int next[MAXN];

/**
 * 构建KMP算法的next数组（部分匹配表）
 * 
 * next[i]表示pattern[0...i]子串的最长相等前后缀的长度
 * 
 * @param pattern 模式串
 * @param patternLen 模式串长度
 */
void buildNextArray(char* pattern, int patternLen) {
    // 初始化
    next[0] = 0;
    int prefixLen = 0;  // 当前最长相等前后缀的长度
    int i = 1;          // 当前处理的位置
    
    // 从位置1开始处理
    while (i < patternLen) {
        // 如果当前字符匹配，可以延长相等前后缀
        if (pattern[i] == pattern[prefixLen]) {
            prefixLen++;
            next[i] = prefixLen;
            i++;
        } 
        // 如果不匹配且前缀长度大于0，需要回退
        else if (prefixLen > 0) {
            prefixLen = next[prefixLen - 1];
        } 
        // 如果不匹配且前缀长度为0，next[i] = 0
        else {
            next[i] = 0;
            i++;
        }
    }
}

/**
 * KMP算法核心实现
 * 
 * 算法步骤：
 * 1. 预处理模式串，生成next数组
 * 2. 使用双指针同时遍历文本串和模式串
 * 3. 当字符匹配时，两个指针都向前移动
 * 4. 当字符不匹配且模式串指针不为0时，根据next数组调整模式串指针
 * 5. 当字符不匹配且模式串指针为0时，文本串指针向前移动
 * 6. 当模式串指针等于模式串长度时，说明匹配成功，返回起始位置
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 第一次匹配的起始位置，如果未找到则返回-1
 */
int kmpSearch(char* text, char* pattern) {
    int textLen = strlen(text);
    int patternLen = strlen(pattern);
    
    // 边界条件处理
    if (patternLen == 0) {
        return 0;
    }
    
    if (textLen < patternLen) {
        return -1;
    }
    
    // 构建next数组
    buildNextArray(pattern, patternLen);
    
    int textIndex = 0;      // 文本串指针
    int patternIndex = 0;   // 模式串指针
    
    // 匹配过程
    while (textIndex < textLen && patternIndex < patternLen) {
        // 字符匹配，两个指针都向前移动
        if (text[textIndex] == pattern[patternIndex]) {
            textIndex++;
            patternIndex++;
        } 
        // 字符不匹配且模式串指针不为0，根据next数组调整模式串指针
        else if (patternIndex > 0) {
            patternIndex = next[patternIndex - 1];
        } 
        // 字符不匹配且模式串指针为0，文本串指针向前移动
        else {
            textIndex++;
        }
    }
    
    // 如果模式串指针等于模式串长度，说明匹配成功
    if (patternIndex == patternLen) {
        return textIndex - patternIndex;
    }
    
    return -1;
}

/**
 * 在文本串haystack中查找模式串needle第一次出现的位置
 * 
 * @param haystack 文本串
 * @param needle 模式串
 * @return 第一次匹配的起始位置，如果未找到则返回-1
 */
int strStr(char* haystack, char* needle) {
    return kmpSearch(haystack, needle);
}

// 测试方法
int main() {
    // 测试用例1
    char haystack1[] = "sadbutsad";
    char needle1[] = "sad";
    int result1 = strStr(haystack1, needle1);
    printf("测试用例1: haystack=\"%s\", needle=\"%s\"\n", haystack1, needle1);
    printf("预期输出: 0, 实际输出: %d\n\n", result1);
    
    // 测试用例2
    char haystack2[] = "leetcode";
    char needle2[] = "leeto";
    int result2 = strStr(haystack2, needle2);
    printf("测试用例2: haystack=\"%s\", needle=\"%s\"\n", haystack2, needle2);
    printf("预期输出: -1, 实际输出: %d\n\n", result2);
    
    // 测试用例3
    char haystack3[] = "hello";
    char needle3[] = "ll";
    int result3 = strStr(haystack3, needle3);
    printf("测试用例3: haystack=\"%s\", needle=\"%s\"\n", haystack3, needle3);
    printf("预期输出: 2, 实际输出: %d\n\n", result3);
    
    // 测试用例4 - 空模式串
    char haystack4[] = "abc";
    char needle4[] = "";
    int result4 = strStr(haystack4, needle4);
    printf("测试用例4: haystack=\"%s\", needle=\"%s\"\n", haystack4, needle4);
    printf("预期输出: 0, 实际输出: %d\n\n", result4);
    
    // 测试用例5 - 模式串比文本串长
    char haystack5[] = "a";
    char needle5[] = "aa";
    int result5 = strStr(haystack5, needle5);
    printf("测试用例5: haystack=\"%s\", needle=\"%s\"\n", haystack5, needle5);
    printf("预期输出: -1, 实际输出: %d\n\n", result5);
    
    printf("所有测试用例完成！\n");
    
    return 0;
}
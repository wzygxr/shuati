/*
 * SPOJ PERIOD - Period
 * 
 * 题目来源：SPOJ (Sphere Online Judge)
 * 题目链接：https://www.spoj.com/problems/PERIOD/
 * 
 * 题目描述：
 * 对于给定字符串S的每个前缀，我们需要知道它是否是周期字符串。
 * 也就是说，对于每个i (2 <= i <= N) 我们要找到满足条件的最小的K (K > 1)，
 * 使得长度为i的前缀可以写成某个字符串重复K次的形式。
 * 如果不存在这样的K，则输出0。
 * 
 * 例如：对于字符串 "aabaab"，长度为6的前缀 "aabaab" 可以写成 "aab" 重复2次，
 * 所以K=2。
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 对于长度为i的前缀，如果i % (i - next[i]) == 0且next[i] > 0，
 * 则该前缀是周期字符串，周期长度为i - next[i]，周期数为i / (i - next[i])。
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(N)，用于存储next数组
 * 
 * KMP算法的核心思想：
 * 1. 利用已经匹配过的信息，避免重复的字符比较
 * 2. next数组（部分匹配表）记录了每个位置的最长相等前后缀长度
 * 3. 这使得在匹配失败时，可以知道模式串应该向右移动多少位，而不是简单地回退一位
 */

#include <iostream>
#include <vector>
#include <string>
#include <cassert>
#include <chrono>

#define MAXN 1000001

int next[MAXN];
int periods[MAXN];

/*
 * 构建KMP算法的next数组（部分匹配表）
 * 
 * next[i]表示str[0...i]子串的最长相等前后缀的长度
 */
void buildNextArray(char* str, int length) {
    // 初始化
    next[0] = 0;
    int prefixLen = 0;  // 当前最长相等前后缀的长度
    int i = 1;          // 当前处理的位置
    
    // 从位置1开始处理
    while (i < length) {
        // 如果当前字符匹配，可以延长相等前后缀
        if (str[i] == str[prefixLen]) {
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
 * 验证周期数计算是否正确的辅助方法
 * @param str 输入字符串
 * @param length 前缀长度
 * @param period 周期数
 * @return 是否确实可以通过重复period次某个子串得到该前缀
 */
bool verifyPeriod(char* str, int length, int period) {
    if (period == 0) return true; // 非周期字符串
    
    int subLength = length / period;
    for (int i = 0; i < length; i++) {
        if (str[i] != str[i % subLength]) {
            return false;
        }
    }
    return true;
}

/*
 * 计算字符串每个前缀的周期
 * 
 * @param str 输入字符串
 * @param length 字符串长度
 */
void calculatePeriods(char* str, int length) {
    // 构建next数组
    buildNextArray(str, length);
    
    // 对于每个前缀长度i (2 <= i <= n)
    for (int i = 2; i <= length; i++) {
        int periodLength = i - next[i - 1]; // 周期长度
        
        // 如果周期长度小于前缀长度且前缀长度能被周期长度整除
        if (periodLength < i && i % periodLength == 0) {
            periods[i] = i / periodLength; // 周期数
        } else {
            periods[i] = 0; // 不是周期字符串
        }
    }
}

/**
 * 测试calculatePeriods函数的正确性
 */
void testCalculatePeriods() {
    // 测试用例1: "aabaab" 预期结果：对于长度为6的前缀，K=2
    char s1[] = "aabaab";
    int len1 = 6;
    std::cout << "测试用例1:" << std::endl;
    std::cout << "输入字符串: " << s1 << std::endl;
    calculatePeriods(s1, len1);
    for (int i = 2; i <= len1; i++) {
        std::cout << "前缀长度 " << i << ": " << periods[i] << std::endl;
        // 验证结果正确性
        assert(verifyPeriod(s1, i, periods[i]) && "测试用例1失败！");
    }
    std::cout << std::endl;
    
    // 测试用例2: "abababab" 预期结果：每个前缀的周期数都是其长度/2
    char s2[] = "abababab";
    int len2 = 8;
    std::cout << "测试用例2:" << std::endl;
    std::cout << "输入字符串: " << s2 << std::endl;
    calculatePeriods(s2, len2);
    for (int i = 2; i <= len2; i++) {
        std::cout << "前缀长度 " << i << ": " << periods[i] << std::endl;
        // 验证结果正确性
        assert(verifyPeriod(s2, i, periods[i]) && "测试用例2失败！");
    }
    std::cout << std::endl;
    
    // 测试用例3: "abcdef" 预期结果：所有前缀都不是周期字符串，输出0
    char s3[] = "abcdef";
    int len3 = 6;
    std::cout << "测试用例3:" << std::endl;
    std::cout << "输入字符串: " << s3 << std::endl;
    calculatePeriods(s3, len3);
    for (int i = 2; i <= len3; i++) {
        std::cout << "前缀长度 " << i << ": " << periods[i] << std::endl;
        // 验证结果正确性
        assert(verifyPeriod(s3, i, periods[i]) && "测试用例3失败！");
    }
    std::cout << std::endl;
    
    // 测试用例4: "aaaaa" 预期结果：每个前缀都有周期，周期数等于其长度
    char s4[] = "aaaaa";
    int len4 = 5;
    std::cout << "测试用例4:" << std::endl;
    std::cout << "输入字符串: " << s4 << std::endl;
    calculatePeriods(s4, len4);
    for (int i = 2; i <= len4; i++) {
        std::cout << "前缀长度 " << i << ": " << periods[i] << std::endl;
        // 验证结果正确性
        assert(verifyPeriod(s4, i, periods[i]) && "测试用例4失败！");
    }
}

int main() {
    // 运行测试用例
    testCalculatePeriods();
    
    // 极端测试用例
    std::cout << "极端测试用例:" << std::endl;
    // 空字符串
    char emptyStr[] = "";
    int lenEmpty = 0;
    std::cout << "空字符串: \"\"" << std::endl;
    calculatePeriods(emptyStr, lenEmpty);
    std::cout << "结果: 无有效前缀" << std::endl;
    
    // 单个字符
    char singleChar[] = "a";
    int lenSingle = 1;
    std::cout << "单个字符: \"a\"" << std::endl;
    calculatePeriods(singleChar, lenSingle);
    std::cout << "结果: 无有效前缀" << std::endl;
    
    // 长字符串性能测试
    char longStr[1001];
    memset(longStr, 'a', 1000);
    longStr[1000] = '\0';
    int lenLong = 1000;
    std::cout << "长字符串 (1000个'a')" << std::endl;
    auto start = std::chrono::high_resolution_clock::now();
    calculatePeriods(longStr, lenLong);
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> elapsed = end - start;
    std::cout << "执行时间: " << elapsed.count() << " 秒" << std::endl;
    std::cout << "长度为1000的前缀周期数: " << periods[1000] << std::endl;
    
    return 0;
}
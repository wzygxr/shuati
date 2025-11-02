/**
 * POJ 2406 Power Strings
 * 题目链接: http://poj.org/problem?id=2406
 * 
 * 题目描述:
 * 给定一个字符串s，找到最大的n使得s = a^n（即字符串a重复n次等于s）。
 * 
 * 算法思路:
 * 使用KMP算法的next数组性质。对于一个字符串s，如果s的长度能被(长度-next[长度])整除，
 * 且next[长度]不为0，则s是由其前(长度-next[长度])个字符重复组成的。
 * 重复次数为长度/(长度-next[长度])。
 * 
 * 时间复杂度: O(m)，其中m是字符串长度
 * 空间复杂度: O(m)，用于存储next数组
 * 
 * 示例:
 * 输入:
 * abcd
 * aaaa
 * ababab
 * 
 * 输出:
 * 1
 * 4
 * 3
 */

// 手动实现字符串长度计算函数
int strLen(const char* str) {
    int len = 0;
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

/**
 * 构建next数组（部分匹配表）
 * next[i]表示模式串中以i-1位置字符结尾的子串，其前缀和后缀匹配的最大长度
 * 
 * 算法思路：
 * 1. next[0] = -1，next[1] = 0（根据定义）
 * 2. 从i=2开始，使用双指针技术计算next[i]
 * 3. cn指针表示当前要和前一个字符比对的下标
 * 4. 如果s[i-1] == s[cn]，说明匹配成功，next[i] = ++cn
 * 5. 如果不匹配且cn > 0，cn回退到next[cn]
 * 6. 如果不匹配且cn == 0，next[i] = 0
 * 
 * @param s 字符串
 * @param n 字符串长度
 * @param next next数组
 */
void nextArray(const char* s, int n, int* next) {
    if (n == 1) {
        next[0] = -1;
        return;
    }
    
    next[0] = -1;
    next[1] = 0;
    
    // i表示当前要求next值的位置
    // cn表示当前要和前一个字符比对的下标
    int i = 2, cn = 0;
    while (i <= n) {
        if (s[i - 1] == s[cn]) {
            next[i++] = ++cn;
        } else if (cn > 0) {
            cn = next[cn];
        } else {
            next[i++] = 0;
        }
    }
}

/**
 * 计算字符串能由其子串重复的最大次数
 * 
 * 算法思路:
 * 1. 使用KMP算法构建next数组
 * 2. 根据next数组的性质判断字符串是否由子串重复组成
 * 3. 如果是，则计算重复次数
 * 
 * @param s 输入字符串
 * @return 字符串能由其子串重复的最大次数
 */
int power(const char* s) {
    if (s == 0) {
        return 0;
    }
    
    int n = strLen(s);
    if (n == 0) {
        return 0;
    }
    
    // 构建next数组
    int next[1000001]; // 假设字符串最大长度为1000000
    nextArray(s, n, next);
    
    // 根据next数组判断是否由子串重复组成
    int period = n - next[n];
    
    // 如果n能被period整除，则说明字符串由长度为period的子串重复组成
    if (period != 0 && n % period == 0) {
        return n / period;
    }
    
    // 否则字符串不能由子串重复组成，返回1
    return 1;
}

// 简单的输出函数
void printStr(const char* str) {
    // 实现根据具体编译器而定
}

// 简单的数字输出函数
void printInt(int num) {
    // 实现根据具体编译器而定
}

#include <iostream>
#include <cstring>
using namespace std;

// 主函数
int main() {
    // 测试用例1: 无重复模式
    const char* s1 = "abcd";
    int result1 = power(s1);
    cout << "测试用例1:" << endl;
    cout << "字符串: " << s1 << endl;
    cout << "重复次数: " << result1 << endl; // 期望输出: 1
    cout << endl;

    // 测试用例2: 全部相同字符
    const char* s2 = "aaaa";
    int result2 = power(s2);
    cout << "测试用例2:" << endl;
    cout << "字符串: " << s2 << endl;
    cout << "重复次数: " << result2 << endl; // 期望输出: 4
    cout << endl;

    // 测试用例3: 重复模式
    const char* s3 = "ababab";
    int result3 = power(s3);
    cout << "测试用例3:" << endl;
    cout << "字符串: " << s3 << endl;
    cout << "重复次数: " << result3 << endl; // 期望输出: 3
    cout << endl;

    // 测试用例4: 空字符串
    const char* s4 = "";
    int result4 = power(s4);
    cout << "测试用例4:" << endl;
    cout << "字符串: " << s4 << endl;
    cout << "重复次数: " << result4 << endl; // 期望输出: 0
    cout << endl;

    // 测试用例5: 单字符
    const char* s5 = "a";
    int result5 = power(s5);
    cout << "测试用例5:" << endl;
    cout << "字符串: " << s5 << endl;
    cout << "重复次数: " << result5 << endl; // 期望输出: 1
    
    return 0;
}

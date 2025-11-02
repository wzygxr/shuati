// 找到所有好字符串
// 给你两个长度为 n 的字符串 s1 和 s2，以及一个字符串 evil。请你返回好字符串的数目。
// 好字符串的定义是：它的长度为 n，字典序大于等于 s1，字典序小于等于 s2，且不包含 evil 为子字符串。
// 由于答案可能很大，返回答案对 10^9 + 7 取余的结果。
// 测试链接 : https://leetcode.cn/problems/find-all-good-strings/

#include <stdio.h>
#include <string.h>

// 定义最大长度
#define MAX_N 505
#define MOD 1000000007

// 全局记忆化数组
int dp[MAX_N][2][MAX_N];
int next_arr[MAX_N];

/**
 * 数位DP + KMP解法
 * 时间复杂度: O(n * m * 2 * 2 * 26) 其中n是字符串长度，m是evil字符串长度
 * 空间复杂度: O(n * m * 2 * 2)
 * 
 * 解题思路:
 * 1. 使用数位DP框架，逐位确定字符
 * 2. 结合KMP算法避免包含evil字符串
 * 3. 状态需要记录：
 *    - 当前处理到第几位
 *    - 是否受到上下界限制
 *    - 当前已匹配evil字符串的前缀长度（使用KMP的next数组）
 * 4. 通过记忆化搜索避免重复计算
 * 
 * 最优解分析:
 * 该解法结合了数位DP和KMP算法，是解决此类问题的最优通用方法。
 */

// 计算KMP的next数组
void getNext(char* pattern, int patternLen) {
    next_arr[0] = -1;
    int i = 0, j = -1;
    
    while (i < patternLen) {
        if (j == -1 || pattern[i] == pattern[j]) {
            i++;
            j++;
            next_arr[i] = j;
        } else {
            j = next_arr[j];
        }
    }
}

// 初始化记忆化数组
void initDp(int n, int evilLen) {
    int i, j, k;
    for (i = 0; i < n; i++) {
        for (j = 0; j < 2; j++) {
            for (k = 0; k < evilLen; k++) {
                dp[i][j][k] = -1;
            }
        }
    }
}

// 数位DP递归函数
int dfs(char* s, char* evil, int pos, int isLimit, int matchLen, int sLen, int evilLen) {
    // 递归终止条件
    if (pos == sLen) {
        return 1;
    }
    
    // 记忆化搜索
    if (!isLimit && dp[pos][0][matchLen] != -1) {
        return dp[pos][0][matchLen];
    }
    
    int ans = 0;
    
    // 确定当前位可以填入的字符范围
    char up = isLimit ? s[pos] : 'z';
    
    // 枚举当前位可以填入的字符
    char c;
    for (c = 'a'; c <= up; c++) {
        // 使用KMP算法计算填入字符c后匹配evil的长度
        int newMatchLen = matchLen;
        while (newMatchLen > 0 && evil[newMatchLen] != c) {
            newMatchLen = next_arr[newMatchLen];
        }
        if (newMatchLen < evilLen && evil[newMatchLen] == c) {
            newMatchLen++;
        }
        
        // 如果已经完全匹配evil，则不能填入这个字符
        if (newMatchLen < evilLen) {
            // 递归处理下一位
            ans = ((long long)ans + dfs(s, evil, pos + 1, isLimit && c == s[pos], newMatchLen, sLen, evilLen)) % MOD;
        }
    }
    
    // 记忆化存储
    if (!isLimit) {
        dp[pos][0][matchLen] = ans;
    }
    
    return ans;
}

// 检查字符串是否包含evil子串
int containsEvil(char* str, int strLen, char* evil, int evilLen) {
    int i, j;
    for (i = 0; i <= strLen - evilLen; i++) {
        int found = 1;
        for (j = 0; j < evilLen; j++) {
            if (str[i + j] != evil[j]) {
                found = 0;
                break;
            }
        }
        if (found) {
            return 1;
        }
    }
    return 0;
}

// 主函数：计算好字符串的数目
int findGoodStrings(int n, char* s1, char* s2, char* evil) {
    int evilLen = 0;
    while (evil[evilLen] != '\0') {
        evilLen++;
    }
    
    // 计算KMP的next数组
    getNext(evil, evilLen);
    
    // 计算[0, s2]中的好字符串个数
    initDp(n, evilLen);
    int count2 = dfs(s2, evil, 0, 1, 0, n, evilLen);
    
    // 计算[0, s1)中的好字符串个数
    initDp(n, evilLen);
    int count1 = dfs(s1, evil, 0, 1, 0, n, evilLen);
    
    // 检查s1本身是否是好字符串
    int s1Len = 0;
    while (s1[s1Len] != '\0') {
        s1Len++;
    }
    int s1IsGood = containsEvil(s1, s1Len, evil, evilLen) ? 0 : 1;
    
    // 返回结果
    return (int)((((long long)count2 - count1 + s1IsGood) % MOD) + MOD) % MOD;
}

// 测试函数
void testFindGoodStrings() {
    printf("=== 测试找到所有好字符串 ===\n");
    
    // 测试用例1
    int n1 = 2;
    char s11[] = "aa";
    char s21[] = "da";
    char evil1[] = "b";
    int result1 = findGoodStrings(n1, s11, s21, evil1);
    printf("n = %d, s1 = \"%s\", s2 = \"%s\", evil = \"%s\"\n", n1, s11, s21, evil1);
    printf("好字符串的数目: %d\n", result1);
    printf("预期输出: 51\n\n");
    
    // 测试用例2
    int n2 = 8;
    char s12[] = "leetcode";
    char s22[] = "leetgoes";
    char evil2[] = "leet";
    int result2 = findGoodStrings(n2, s12, s22, evil2);
    printf("n = %d, s1 = \"%s\", s2 = \"%s\", evil = \"%s\"\n", n2, s12, s22, evil2);
    printf("好字符串的数目: %d\n", result2);
    printf("预期输出: 0\n\n");
    
    // 测试用例3
    int n3 = 2;
    char s13[] = "gx";
    char s23[] = "gz";
    char evil3[] = "x";
    int result3 = findGoodStrings(n3, s13, s23, evil3);
    printf("n = %d, s1 = \"%s\", s2 = \"%s\", evil = \"%s\"\n", n3, s13, s23, evil3);
    printf("好字符串的数目: %d\n", result3);
    printf("预期输出: 2\n\n");
}

int main() {
    testFindGoodStrings();
    
    // 额外测试
    printf("=== 边界测试 ===\n");
    int n = 1;
    char s1[] = "a";
    char s2[] = "z";
    char evil[] = "a";
    printf("n = %d, s1 = \"%s\", s2 = \"%s\", evil = \"%s\": %d\n", 
           n, s1, s2, evil, findGoodStrings(n, s1, s2, evil));
    
    return 0;
}

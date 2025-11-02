// ZJOI2010 数字计数
// 给定两个正整数a和b，求在[a,b]中的所有整数中，每个数码（digit）各出现了多少次。
// 测试链接 : https://www.luogu.com.cn/problem/P2602

#include <stdio.h>
#include <string.h>

// 定义最大长度
#define MAX_LEN 20

// 全局记忆化数组
long long dp[MAX_LEN][2][2][10];
char num_str[MAX_LEN];

/**
 * 数位DP解法
 * 时间复杂度: O(log(b) * 2 * 2 * 10 * 10)
 * 空间复杂度: O(log(b) * 2 * 2 * 10)
 * 
 * 解题思路:
 * 1. 使用数位DP框架，逐位确定数字
 * 2. 对于每个数字0-9，分别计算其出现次数
 * 3. 状态需要记录：
 *    - 当前处理到第几位
 *    - 是否受到上界限制
 *    - 是否已开始填数字（处理前导零）
 *    - 当前数字的出现次数
 * 4. 通过记忆化搜索避免重复计算
 * 
 * 最优解分析:
 * 该解法是标准的数位DP解法，时间复杂度与状态数相关，是解决此类问题的最优通用方法。
 */

// 初始化记忆化数组
void initDp() {
    int i, j, k, l;
    for (i = 0; i < MAX_LEN; i++) {
        for (j = 0; j < 2; j++) {
            for (k = 0; k < 2; k++) {
                for (l = 0; l < 10; l++) {
                    dp[i][j][k][l] = -1;
                }
            }
        }
    }
}

// 将长整数转换为字符串
void longTostr(long long n, char* str) {
    int len = 0;
    long long temp = n;
    int i;
    
    // 特殊处理0
    if (n == 0) {
        str[0] = '0';
        str[1] = '\0';
        return;
    }
    
    // 计算数字位数
    while (temp > 0) {
        len++;
        temp /= 10;
    }
    
    // 从低位到高位填充字符串
    temp = n;
    for (i = len - 1; i >= 0; i--) {
        str[i] = (temp % 10) + '0';
        temp /= 10;
    }
    str[len] = '\0';
}

// 数位DP递归函数
long long dfs(int pos, int isLimit, int isNum, int targetDigit) {
    // 递归终止条件
    if (num_str[pos] == '\0') {
        return 0;
    }
    
    // 记忆化搜索
    if (!isLimit && isNum && dp[pos][0][0][targetDigit] != -1) {
        return dp[pos][0][0][targetDigit];
    }
    
    long long ans = 0;
    
    // 如果还没开始填数字，可以选择跳过当前位（处理前导零）
    if (!isNum) {
        ans += dfs(pos + 1, 0, 0, targetDigit);
    }
    
    // 确定当前位可以填入的数字范围
    int up = isLimit ? num_str[pos] - '0' : 9;
    
    // 枚举当前位可以填入的数字
    int d;
    for (d = isNum ? 0 : 1; d <= up; d++) {
        // 如果当前位填的是目标数字，则计数加1
        long long count = (d == targetDigit) ? 1 : 0;
        
        // 递归处理下一位
        ans += count + dfs(pos + 1, isLimit && d == up, 1, targetDigit);
    }
    
    // 记忆化存储
    if (!isLimit && isNum) {
        dp[pos][0][0][targetDigit] = ans;
    }
    
    return ans;
}

// 计算[0, n]中每个数字的出现次数
void countDigitsUpTo(long long n, long long result[]) {
    if (n < 0) {
        for (int i = 0; i < 10; i++) {
            result[i] = 0;
        }
        return;
    }
    
    // 将数字转换为字符串
    longTostr(n, num_str);
    
    // 对于每个数字0-9，分别计算其出现次数
    int digit;
    for (digit = 0; digit < 10; digit++) {
        // 初始化记忆化数组
        initDp();
        result[digit] = dfs(0, 1, 0, digit);
    }
}

// 主函数：计算区间中每个数字的出现次数
void countDigits(long long a, long long b, long long result[]) {
    long long countB[10];
    long long countA[10];
    
    // 计算[0, b]中的数字计数
    countDigitsUpTo(b, countB);
    
    // 计算[0, a-1]中的数字计数
    countDigitsUpTo(a - 1, countA);
    
    // 答案为[0, b]中的数字计数减去[0, a-1]中的数字计数
    for (int i = 0; i < 10; i++) {
        result[i] = countB[i] - countA[i];
    }
}

// 测试函数
void testDigitCount() {
    printf("=== 测试数字计数 ===\n");
    
    // 测试用例1
    long long a1 = 1, b1 = 9;
    long long result1[10];
    countDigits(a1, b1, result1);
    printf("a = %lld, b = %lld\n", a1, b1);
    printf("结果: ");
    for (int i = 0; i < 10; i++) {
        printf("%lld ", result1[i]);
    }
    printf("\n预期: 0 1 1 1 1 1 1 1 1 1\n\n");
    
    // 测试用例2
    long long a2 = 1, b2 = 99;
    long long result2[10];
    countDigits(a2, b2, result2);
    printf("a = %lld, b = %lld\n", a2, b2);
    printf("结果: ");
    for (int i = 0; i < 10; i++) {
        printf("%lld ", result2[i]);
    }
    printf("\n预期: 9 20 20 20 20 20 20 20 20 20\n\n");
    
    // 测试用例3
    long long a3 = 1, b3 = 1000;
    long long result3[10];
    countDigits(a3, b3, result3);
    printf("a = %lld, b = %lld\n", a3, b3);
    printf("结果: ");
    for (int i = 0; i < 10; i++) {
        printf("%lld ", result3[i]);
    }
    printf("\n");
}

int main() {
    testDigitCount();
    
    // 额外测试
    printf("=== 边界测试 ===\n");
    long long a = 0, b = 0;
    long long result[10];
    countDigits(a, b, result);
    printf("a = %lld, b = %lld: ", a, b);
    for (int i = 0; i < 10; i++) {
        printf("%lld ", result[i]);
    }
    printf("\n");
    
    return 0;
}

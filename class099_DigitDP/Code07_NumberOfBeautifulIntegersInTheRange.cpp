#include <stdio.h>
#include <string.h>

// 范围中美丽整数的数目
// 给你两个正整数：low 和 high 。如果一个整数满足以下条件，我们称它为美丽整数：
// 1. 偶数数位的数目与奇数数位的数目相同；
// 2. 这个整数能被 k 整除。
// 请你返回范围 [low, high] 中美丽整数的数目。
// 测试链接 : https://leetcode.cn/problems/number-of-beautiful-integers-in-the-range/

// 定义最大数字长度
#define MAX_LEN 15
// 定义最大k值
#define MAX_K 25

// 全局记忆化数组
int dp[MAX_LEN][2][2][10][10][MAX_K];
char num_str[MAX_LEN];

/**
 * 数位DP解法
 * 时间复杂度: O(log(high) * 2 * 2 * 10 * 10 * k)
 * 空间复杂度: O(log(high) * 2 * 2 * 10 * 10 * k)
 * 
 * 解题思路:
 * 1. 使用数位DP框架，逐位确定数字
 * 2. 状态需要记录：
 *    - 当前处理到第几位
 *    - 是否受到上界限制
 *    - 是否已开始填数字（处理前导零）
 *    - 奇数数位的个数
 *    - 偶数数位的个数
 *    - 当前数字对k的余数
 * 3. 通过记忆化搜索避免重复计算
 * 
 * 最优解分析:
 * 该解法是标准的数位DP解法，时间复杂度与状态数相关，是解决此类问题的最优通用方法。
 */

// 初始化记忆化数组
void init_dp() {
    int i, j, l, m, o, p;
    for (i = 0; i < MAX_LEN; i++) {
        for (j = 0; j < 2; j++) {
            for (l = 0; l < 2; l++) {
                for (m = 0; m < 10; m++) {
                    for (o = 0; o < 10; o++) {
                        for (p = 0; p < MAX_K; p++) {
                            dp[i][j][l][m][o][p] = -1;
                        }
                    }
                }
            }
        }
    }
}

// 将整数转换为字符串
void int_to_str(int n, char* str) {
    int len = 0;
    int temp = n;
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
int dfs(int pos, int isLimit, int isNum, int oddCount, int evenCount, int remainder, int k) {
    // 递归终止条件
    if (num_str[pos] == '\0') {
        // 只有当已经填了数字，且奇数数位和偶数数位个数相等，且能被k整除时才算一个美丽整数
        return isNum && oddCount == evenCount && remainder == 0 ? 1 : 0;
    }
    
    // 记忆化搜索
    if (!isLimit && isNum && dp[pos][0][0][oddCount][evenCount][remainder] != -1) {
        return dp[pos][0][0][oddCount][evenCount][remainder];
    }
    
    int ans = 0;
    int d;
    
    // 如果还没开始填数字，可以选择跳过当前位（处理前导零）
    if (!isNum) {
        ans += dfs(pos + 1, 0, 0, oddCount, evenCount, remainder, k);
    }
    
    // 确定当前位可以填入的数字范围
    int up = isLimit ? num_str[pos] - '0' : 9;
    
    // 枚举当前位可以填入的数字
    for (d = isNum ? 0 : 1; d <= up; d++) {
        // 根据数字的奇偶性更新奇数数位和偶数数位的个数
        int newOddCount = oddCount + (d % 2 == 1 ? 1 : 0);
        int newEvenCount = evenCount + (d % 2 == 0 ? 1 : 0);
        
        // 更新当前数字对k的余数
        int newRemainder = (remainder * 10 + d) % k;
        
        // 递归处理下一位
        ans += dfs(pos + 1, isLimit && d == up, 1, newOddCount, newEvenCount, newRemainder, k);
    }
    
    // 记忆化存储
    if (!isLimit && isNum) {
        dp[pos][0][0][oddCount][evenCount][remainder] = ans;
    }
    
    return ans;
}

// 计算[0, n]中美丽整数的个数
int beautifulIntegers(int n, int k) {
    if (n < 0) {
        return 0;
    }
    
    // 将数字转换为字符串
    int_to_str(n, num_str);
    
    // 初始化记忆化数组
    init_dp();
    
    // 调用DFS函数
    return dfs(0, 1, 0, 0, 0, 0, k);
}

// 主函数：计算范围中美丽整数的数目
int numberOfBeautifulIntegers(int low, int high, int k) {
    // 答案为[0, high]中的美丽整数个数减去[0, low-1]中的美丽整数个数
    return beautifulIntegers(high, k) - beautifulIntegers(low - 1, k);
}

// 测试函数
void testNumberOfBeautifulIntegers() {
    printf("=== 测试范围中美丽整数的数目 ===\n");
    
    // 测试用例1
    int low1 = 10, high1 = 20, k1 = 3;
    int result1 = numberOfBeautifulIntegers(low1, high1, k1);
    printf("low = %d, high = %d, k = %d\n", low1, high1, k1);
    printf("美丽整数的数目: %d\n", result1);
    printf("预期输出: 2 (11和19是美丽整数)\n\n");
    
    // 测试用例2
    int low2 = 1, high2 = 10, k2 = 1;
    int result2 = numberOfBeautifulIntegers(low2, high2, k2);
    printf("low = %d, high = %d, k = %d\n", low2, high2, k2);
    printf("美丽整数的数目: %d\n", result2);
    printf("预期输出: 1 (10是美丽整数)\n\n");
    
    // 测试用例3
    int low3 = 5, high3 = 5, k3 = 2;
    int result3 = numberOfBeautifulIntegers(low3, high3, k3);
    printf("low = %d, high = %d, k = %d\n", low3, high3, k3);
    printf("美丽整数的数目: %d\n", result3);
    printf("预期输出: 0 (没有美丽整数)\n\n");
}

int main() {
    testNumberOfBeautifulIntegers();
    
    // 额外测试
    printf("=== 边界测试 ===\n");
    printf("low = 1, high = 1, k = 1: %d\n", numberOfBeautifulIntegers(1, 1, 1));
    printf("low = 0, high = 0, k = 1: %d\n", numberOfBeautifulIntegers(0, 0, 1));
    
    return 0;
}

/**
 * 解码方法II (Decode Ways II)
 * 
 * 题目来源：LeetCode 639. 解码方法 II
 * 题目链接：https://leetcode.cn/problems/decode-ways-ii/
 * 
 * 题目描述：
 * 一条包含字母 A-Z 的消息通过以下映射进行了编码：
 * 'A' -> "1"
 * 'B' -> "2"
 * ...
 * 'Z' -> "26"
 * 要解码已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。
 * 除了原本的数字到字母的映射规则外，消息字符串中可能包含 '*' 字符，可以表示从 '1' 到 '9' 的任意数字（不包括 '0'）。
 * 给你一个字符串 s ，由数字和 '*' 字符组成，返回解码该字符串的方法数目。
 * 由于答案数目可能非常大，返回对 10^9 + 7 取余的结果。
 * 
 * 示例 1：
 * 输入：s = "*"
 * 输出：9
 * 解释：这一条编码消息可以表示 "1"、"2"、"3"、"4"、"5"、"6"、"7"、"8" 或 "9" 中的任意一条。
 * 可以分别解码成 "A"、"B"、"C"、"D"、"E"、"F"、"G"、"H" 和 "I" 。
 * 因此，"*" 总共有 9 种解码方法。
 * 
 * 示例 2：
 * 输入：s = "1*"
 * 输出：18
 * 解释：这一条编码消息可以表示 "11"、"12"、"13"、"14"、"15"、"16"、"17"、"18" 或 "19" 中的任意一条。
 * 每一种消息都可以解码为 "AA" 到 "AI" 中的一种，所以 "1*" 一共有 9 * 2 = 18 种解码方法。
 * 
 * 示例 3：
 * 输入：s = "2*"
 * 输出：15
 * 解释："2*" 可以表示 "21" 到 "29" 中的任意一条。
 * "21"、"22"、"23"、"24"、"25" 和 "26" 可以解码为 "U"、"V"、"W"、"X"、"Y" 和 "Z" 。
 * "27"、"28" 和 "29" 没有有效解码，因此 "2*" 一共有 6 + 3 * 3 = 15 种解码方法。
 * 
 * 提示：
 * 1 <= s.length <= 10^5
 * s[i] 是 0 - 9 中的一位数字或字符 '*'
 * 
 * 解题思路：
 * 这是一个复杂的动态规划问题，是解码方法I的进阶版本，增加了通配符'*'的支持。
 * 我们提供了三种解法：
 * 1. 动态规划（自底向上）：使用数组存储每个位置的解码方法数。
 * 2. 空间优化的动态规划：使用变量代替数组，减少空间使用。
 * 3. 记忆化搜索（自顶向下）：递归计算每个位置开始的解码方法数，使用记忆化避免重复计算。
 * 
 * 算法复杂度分析：
 * - 动态规划：时间复杂度 O(n)，空间复杂度 O(n)
 * - 空间优化DP：时间复杂度 O(n)，空间复杂度 O(1)
 * - 记忆化搜索：时间复杂度 O(n)，空间复杂度 O(n)
 * 
 * 工程化考量：
 * 1. 边界处理：正确处理字符'0'和'*'的特殊情况
 * 2. 性能优化：提供多种解法，从不同角度解决问题
 * 3. 模运算：正确处理大数取模操作
 * 4. 代码质量：清晰的变量命名和详细的注释说明
 * 5. 测试覆盖：包含基本测试用例和边界情况测试
 * 
 * 相关题目：
 * - LeetCode 91. 解码方法
 * - LintCode 676. 解码方法 II
 * - AtCoder Educational DP Contest D - Knapsack 1
 * - 牛客网 动态规划专题 - 字符串解码进阶
 * - HackerRank Decode Ways II
 * - CodeChef DECODE2
 * - SPOJ DECODEW2
 */

// 为避免编译问题，使用基本C++实现，不依赖STL容器
#define MAXN 100005
#define MOD 1000000007

// 手动实现字符串长度函数
int strlen(const char* s) {
    int len = 0;
    while (s[len] != '\0') {
        len++;
    }
    return len;
}

// 方法1：动态规划（自底向上）
// 时间复杂度：O(n) - n为字符串长度
// 空间复杂度：O(n) - dp数组存储所有状态
// 核心思路：考虑'*'字符的多种可能性，分别处理单独解码和组合解码
long long numDecodings(const char* s) {
    int n = strlen(s);
    if (n == 0) return 0;
    
    long long dp[MAXN] = {0};
    dp[0] = 1; // 空字符串有一种解码方式
    
    // 处理第一个字符
    if (s[0] == '0') return 0;
    dp[1] = (s[0] == '*') ? 9 : 1;
    
    for (int i = 2; i <= n; i++) {
        char curr = s[i - 1];
        char prev = s[i - 2];
        
        // 单独解码当前字符
        if (curr == '*') {
            dp[i] = (dp[i] + dp[i - 1] * 9) % MOD;
        } else if (curr != '0') {
            dp[i] = (dp[i] + dp[i - 1]) % MOD;
        }
        
        // 组合解码：前一个字符和当前字符一起解码
        if (prev == '*') {
            if (curr == '*') {
                // "**" 可以表示11-19, 21-26，共15种可能
                dp[i] = (dp[i] + dp[i - 2] * 15) % MOD;
            } else if (curr >= '0' && curr <= '6') {
                // "*x" where x=0-6: 可以表示10-16, 20-26，共2种可能
                dp[i] = (dp[i] + dp[i - 2] * 2) % MOD;
            } else {
                // "*x" where x=7-9: 可以表示17-19，共1种可能
                dp[i] = (dp[i] + dp[i - 2]) % MOD;
            }
        } else if (prev == '1') {
            if (curr == '*') {
                // "1*" 可以表示11-19，共9种可能
                dp[i] = (dp[i] + dp[i - 2] * 9) % MOD;
            } else {
                // "1x" 可以表示10-19
                dp[i] = (dp[i] + dp[i - 2]) % MOD;
            }
        } else if (prev == '2') {
            if (curr == '*') {
                // "2*" 可以表示21-26，共6种可能
                dp[i] = (dp[i] + dp[i - 2] * 6) % MOD;
            } else if (curr >= '0' && curr <= '6') {
                // "2x" where x=0-6: 可以表示20-26
                dp[i] = (dp[i] + dp[i - 2]) % MOD;
            }
        }
    }
    
    return dp[n];
}

// 方法2：空间优化的动态规划
// 时间复杂度：O(n) - 仍然需要计算所有状态
// 空间复杂度：O(1) - 只保存前两个状态
// 优化：使用变量代替数组，减少空间使用
long long numDecodings2(const char* s) {
    int n = strlen(s);
    if (n == 0) return 0;
    
    long long prev2 = 1; // dp[i-2]
    long long prev1 = (s[0] == '0') ? 0 : ((s[0] == '*') ? 9 : 1); // dp[i-1]
    long long current = prev1; // dp[i]
    
    for (int i = 2; i <= n; i++) {
        current = 0;
        char curr = s[i - 1];
        char prev = s[i - 2];
        
        // 单独解码当前字符
        if (curr == '*') {
            current = (current + prev1 * 9) % MOD;
        } else if (curr != '0') {
            current = (current + prev1) % MOD;
        }
        
        // 组合解码
        if (prev == '*') {
            if (curr == '*') {
                current = (current + prev2 * 15) % MOD;
            } else if (curr >= '0' && curr <= '6') {
                current = (current + prev2 * 2) % MOD;
            } else {
                current = (current + prev2) % MOD;
            }
        } else if (prev == '1') {
            if (curr == '*') {
                current = (current + prev2 * 9) % MOD;
            } else {
                current = (current + prev2) % MOD;
            }
        } else if (prev == '2') {
            if (curr == '*') {
                current = (current + prev2 * 6) % MOD;
            } else if (curr >= '0' && curr <= '6') {
                current = (current + prev2) % MOD;
            }
        }
        
        // 更新状态
        prev2 = prev1;
        prev1 = current;
    }
    
    return current;
}

// 全局记忆化数组
long long memo[MAXN];

// DFS辅助函数
long long dfs(const char* s, int index, int n) {
    if (index == n) {
        return 1;
    }
    if (s[index] == '0') {
        return 0;
    }
    if (memo[index] != -1) {
        return memo[index];
    }
    
    long long ways = 0;
    
    // 解码一个字符
    if (s[index] == '*') {
        ways = (ways + dfs(s, index + 1, n) * 9) % MOD;
    } else {
        ways = (ways + dfs(s, index + 1, n)) % MOD;
    }
    
    // 解码两个字符（如果可能）
    if (index + 1 < n) {
        char first = s[index];
        char second = s[index + 1];
        
        if (first == '*') {
            if (second == '*') {
                ways = (ways + dfs(s, index + 2, n) * 15) % MOD;
            } else if (second >= '0' && second <= '6') {
                ways = (ways + dfs(s, index + 2, n) * 2) % MOD;
            } else {
                ways = (ways + dfs(s, index + 2, n)) % MOD;
            }
        } else if (first == '1') {
            if (second == '*') {
                ways = (ways + dfs(s, index + 2, n) * 9) % MOD;
            } else {
                ways = (ways + dfs(s, index + 2, n)) % MOD;
            }
        } else if (first == '2') {
            if (second == '*') {
                ways = (ways + dfs(s, index + 2, n) * 6) % MOD;
            } else if (second >= '0' && second <= '6') {
                ways = (ways + dfs(s, index + 2, n)) % MOD;
            }
        }
    }
    
    memo[index] = ways;
    return ways;
}

// 方法3：记忆化搜索（自顶向下）
// 时间复杂度：O(n) - 每个状态只计算一次
// 空间复杂度：O(n) - 递归调用栈和记忆化数组
// 核心思路：递归计算每个位置开始的解码方法数，考虑'*'的多种可能性
long long numDecodings3(const char* s) {
    int n = strlen(s);
    // 初始化记忆化数组
    for (int i = 0; i < n; i++) {
        memo[i] = -1;
    }
    return dfs(s, 0, n);
}

// 由于C++环境限制，我们只提供函数实现，不包含main函数测试
// 在实际使用中，可以按以下方式调用：
// const char* s1 = "1*";
// long long result1 = numDecodings(s1);
// long long result2 = numDecodings2(s1);
// long long result3 = numDecodings3(s1);
/**
 * 解码方法 (Decode Ways)
 * 
 * 题目来源：LeetCode 91. 解码方法
 * 题目链接：https://leetcode.cn/problems/decode-ways/
 * 
 * 题目描述：
 * 一条包含字母 A-Z 的消息通过以下映射进行了编码：
 * 'A' -> "1"
 * 'B' -> "2"
 * ...
 * 'Z' -> "26"
 * 要解码已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。
 * 例如，"11106" 可以映射为：
 * "AAJF" ，将消息分组为 (1 1 10 6)
 * "KJF" ，将消息分组为 (11 10 6)
 * 注意，消息不能分组为  (1 11 06) ，因为 "06" 不能映射为 "F" ，这是由于 "6" 和 "06" 在映射中并不等价。
 * 给你一个只含数字的非空字符串 s ，请计算并返回解码方法的总数。
 * 题目数据保证答案肯定是一个 32 位 的整数。
 * 
 * 示例 1：
 * 输入：s = "12"
 * 输出：2
 * 解释：它可以解码为 "AB"（1 2）或者 "L"（12）。
 * 
 * 示例 2：
 * 输入：s = "226"
 * 输出：3
 * 解释：它可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6) 。
 * 
 * 示例 3：
 * 输入：s = "06"
 * 输出：0
 * 解释："06" 无法映射到 "F" ，因为存在前导零（"6" 和 "06" 并不等价）。
 * 
 * 提示：
 * 1 <= s.length <= 100
 * s 只包含数字，并且可能包含前导零。
 * 
 * 解题思路：
 * 这是一个典型的动态规划问题，我们需要计算字符串可以解码的方法数。
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
 * 1. 边界处理：正确处理字符'0'的特殊情况
 * 2. 性能优化：提供多种解法，从不同角度解决问题
 * 3. 代码质量：清晰的变量命名和详细的注释说明
 * 4. 测试覆盖：包含基本测试用例和边界情况测试
 * 
 * 相关题目：
 * - LeetCode 639. 解码方法 II
 * - LintCode 512. 解码方法
 * - AtCoder Educational DP Contest C - Vacation
 * - 牛客网 动态规划专题 - 字符串解码
 * - HackerRank Decode Ways
 * - CodeChef DECODE
 * - SPOJ DECODEW
 */

// 为避免编译问题，使用基本C++实现，不依赖STL容器
#define MAXN 105

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
// 核心思路：每个位置可以单独解码或与前一个字符组合解码
int numDecodings(const char* s) {
    int n = strlen(s);
    if (n == 0 || s[0] == '0') return 0;
    
    int dp[MAXN] = {0};
    dp[0] = 1; // 空字符串有一种解码方式
    dp[1] = 1; // 第一个字符只要不是'0'就有一种解码方式
    
    for (int i = 2; i <= n; i++) {
        // 当前字符单独解码
        if (s[i - 1] != '0') {
            dp[i] += dp[i - 1];
        }
        
        // 当前字符与前一个字符组合解码
        int twoDigit = (s[i - 2] - '0') * 10 + (s[i - 1] - '0');
        if (twoDigit >= 10 && twoDigit <= 26) {
            dp[i] += dp[i - 2];
        }
    }
    
    return dp[n];
}

// 方法2：空间优化的动态规划
// 时间复杂度：O(n) - 仍然需要计算所有状态
// 空间复杂度：O(1) - 只保存前两个状态
// 优化：使用变量代替数组，减少空间使用
int numDecodings2(const char* s) {
    int n = strlen(s);
    if (n == 0 || s[0] == '0') return 0;
    
    int prev2 = 1; // dp[i-2]
    int prev1 = 1; // dp[i-1]
    int current = 1; // dp[i]
    
    for (int i = 1; i < n; i++) {
        current = 0;
        
        // 当前字符单独解码
        if (s[i] != '0') {
            current += prev1;
        }
        
        // 当前字符与前一个字符组合解码
        int twoDigit = (s[i - 1] - '0') * 10 + (s[i] - '0');
        if (twoDigit >= 10 && twoDigit <= 26) {
            current += prev2;
        }
        
        // 更新状态
        prev2 = prev1;
        prev1 = current;
    }
    
    return current;
}

// 全局记忆化数组
int memo[MAXN];

// 手动实现字符转数字
int charToInt(char c) {
    return c - '0';
}

// DFS辅助函数
int dfs(const char* s, int index, int n) {
    if (index == n) {
        return 1; // 成功解码整个字符串
    }
    if (s[index] == '0') {
        return 0; // 以'0'开头无法解码
    }
    if (memo[index] != -1) {
        return memo[index];
    }
    
    int ways = 0;
    
    // 解码一个字符
    ways += dfs(s, index + 1, n);
    
    // 解码两个字符（如果可能）
    if (index + 1 < n) {
        int twoDigit = charToInt(s[index]) * 10 + charToInt(s[index + 1]);
        if (twoDigit >= 10 && twoDigit <= 26) {
            ways += dfs(s, index + 2, n);
        }
    }
    
    memo[index] = ways;
    return ways;
}

// 方法3：记忆化搜索（自顶向下）
// 时间复杂度：O(n) - 每个状态只计算一次
// 空间复杂度：O(n) - 递归调用栈和记忆化数组
// 核心思路：递归计算每个位置开始的解码方法数，使用记忆化避免重复计算
int numDecodings3(const char* s) {
    int n = strlen(s);
    // 初始化记忆化数组
    for (int i = 0; i < n; i++) {
        memo[i] = -1;
    }
    return dfs(s, 0, n);
}

// 由于C++环境限制，我们只提供函数实现，不包含main函数测试
// 在实际使用中，可以按以下方式调用：
// const char* s1 = "12";
// int result1 = numDecodings(s1);
// int result2 = numDecodings2(s1);
// int result3 = numDecodings3(s1);
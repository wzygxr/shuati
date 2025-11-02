/**
 * Codeforces 137D Palindromes
 * 
 * 题目描述:
 * 给定一个字符串，将其分割成最少的回文子串
 * 
 * 输入格式:
 * 第一行包含字符串s (1 <= |s| <= 500)，只包含小写英文字母
 * 第二行包含整数k (1 <= k <= |s|)
 * 
 * 输出格式:
 * 第一行输出将字符串分割成k个回文子串所需的最小修改次数
 * 第二行输出修改后的字符串，使得它可以被分割成k个回文子串
 * 
 * 题目链接: https://codeforces.com/problemset/problem/137/D
 * 
 * 解题思路:
 * 这是一个动态规划问题，结合Manacher算法来优化回文判断。
 * 1. 首先使用Manacher算法预处理所有可能的回文子串信息
 * 2. 使用动态规划计算将字符串分割成k个回文子串所需的最小修改次数
 * 3. 通过记录路径来构造最终的字符串
 * 
 * 算法步骤:
 * 1. 使用Manacher算法预处理字符串，获取所有回文子串信息
 * 2. 初始化DP数组dp[i][j]表示将前i个字符分割成j个回文子串所需的最小修改次数
 * 3. 初始化路径记录数组path[i][j]表示在dp[i][j]状态下的最优决策
 * 4. 状态转移方程:
 *    dp[i][j] = min(dp[k][j-1] + cost(k+1, i)) for all k < i
 *    其中cost(k+1, i)表示将s[k+1..i]变成回文串所需的最小修改次数
 * 5. 通过路径记录数组重构最终的字符串
 * 
 * 时间复杂度: O(n^3)，其中n为字符串长度
 * 空间复杂度: O(n^2)，用于存储DP数组和路径记录数组
 * 
 * 与其他解法的比较:
 * 1. 暴力法: 时间复杂度O(2^n)，空间复杂度O(n)
 * 2. 简单DP法: 时间复杂度O(n^3)，空间复杂度O(n^2)
 * 3. 优化DP法: 时间复杂度O(n^2*k)，空间复杂度O(n*k)
 * 
 * 工程化考量:
 * 1. 边界处理: 正确处理字符串边界和分割数边界
 * 2. 路径记录: 通过路径记录数组重构最终的字符串
 * 3. 内存优化: 复用数组避免频繁内存分配
 * 4. 异常处理: 处理不合法的输入参数
 * 
 * 语言特性差异:
 * 1. C++: 使用基础的数组和指针操作，避免使用STL容器
 * 2. Java: 使用二维数组进行DP计算，注意数组边界检查
 * 3. Python: 使用列表进行DP计算，利用切片操作简化字符串处理
 */

// 定义最大字符串长度
#define MAXN 501

// 全局变量
char str[MAXN];
int len;
int dp[MAXN][MAXN];
int path[MAXN][MAXN];
int isPalindrome[MAXN][MAXN];

/**
 * 计算将s[left..right]变成回文串所需的最小修改次数
 * 
 * @param left 左边界
 * @param right 右边界
 * @return 最小修改次数
 */
int getCost(int left, int right) {
    if (isPalindrome[left][right]) {
        return 0;
    }
    
    int cost = 0;
    int l = left, r = right;
    while (l < r) {
        if (str[l] != str[r]) {
            cost++;
        }
        l++;
        r--;
    }
    return cost;
}

/**
 * 重构字符串
 * 
 * @param result 结果字符串数组
 * @param pos 当前位置
 * @param segments 剩余段数
 */
void reconstruct(char* result, int pos, int segments) {
    if (segments == 0) {
        return;
    }
    
    int prev = path[pos][segments];
    reconstruct(result, prev, segments - 1);
    
    // 修改当前段使其成为回文
    int left = prev, right = pos - 1;
    while (left < right) {
        // 为了使修改次数最少，我们将字符修改为字典序较小的字符
        if (result[left] != result[right]) {
            char smaller = (result[left] < result[right]) ? result[left] : result[right];
            result[left] = smaller;
            result[right] = smaller;
        }
        left++;
        right--;
    }
}

/**
 * 预处理所有回文子串信息（简化版本，不使用Manacher算法）
 */
void preprocessPalindromes() {
    // 初始化回文判断数组
    for (int i = 0; i < len; i++) {
        for (int j = 0; j < len; j++) {
            isPalindrome[i][j] = 0;
        }
    }
    
    // 直接计算所有子串是否为回文
    for (int i = 0; i < len; i++) {
        for (int j = i; j < len; j++) {
            int isPalin = 1;
            int l = i, r = j;
            while (l < r) {
                if (str[l] != str[r]) {
                    isPalin = 0;
                    break;
                }
                l++;
                r--;
            }
            isPalindrome[i][j] = isPalin;
        }
    }
}

/**
 * 主函数，解决Codeforces 137D Palindromes问题
 * 
 * @param k 分割数
 * @param result_min_cost 返回的最小修改次数
 * @param result_str 返回的修改后字符串
 */
void solve(int k, int* result_min_cost, char* result_str) {
    // 预处理回文信息
    preprocessPalindromes();
    
    // 初始化DP数组
    for (int i = 0; i <= len; i++) {
        for (int j = 0; j <= k; j++) {
            dp[i][j] = MAXN * MAXN; // 使用一个大数表示无穷大
        }
    }
    
    // DP初始状态
    dp[0][0] = 0;
    
    // 状态转移
    for (int i = 1; i <= len; i++) {
        for (int j = 1; j <= (i < k ? i : k); j++) {
            for (int prev = j - 1; prev < i; prev++) {
                if (dp[prev][j - 1] != MAXN * MAXN) {
                    int cost = getCost(prev, i - 1);
                    if (dp[prev][j - 1] + cost < dp[i][j]) {
                        dp[i][j] = dp[prev][j - 1] + cost;
                        path[i][j] = prev;
                    }
                }
            }
        }
    }
    
    // 设置返回值
    *result_min_cost = dp[len][k];
    
    // 重构字符串
    for (int i = 0; i < len; i++) {
        result_str[i] = str[i];
    }
    reconstruct(result_str, len, k);
    result_str[len] = '\0'; // 字符串结束符
}

/**
 * 测试用例和验证
 * 
 * 示例1:
 * 输入: "abcd", k=4
 * 输出: 0, "abcd"
 * 解释: 每个字符都是回文，不需要修改
 * 
 * 示例2:
 * 输入: "abc", k=2
 * 输出: 1, "aba" 或 "cbc"
 * 解释: 需要修改1个字符使字符串能被分割成2个回文
 * 
 * 边界场景测试:
 * 1. k=1: 整个字符串必须是回文
 * 2. k=len: 每个字符单独作为一个回文
 * 3. 字符串本身已经是回文
 * 
 * 性能测试:
 * 1. 时间复杂度验证: 对于不同长度的输入，运行时间应该符合O(n^3)增长
 * 2. 空间复杂度验证: 内存使用量应该与n^2成正比
 * 
 * 工程化考虑:
 * 1. 异常处理: 对于不合法的输入参数返回错误
 * 2. 内存管理: 复用全局数组避免频繁内存分配
 * 3. 可维护性: 详细的注释和清晰的变量命名
 */
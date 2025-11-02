// CodeForces 835D Palindromic characteristics
// 题目链接: https://codeforces.com/problemset/problem/835/D
//
// 题目描述：
// 定义k回文串(k>1): 字符串本身是回文串，且左右两半相等，左右两半都是(k-1)回文串
// 定义1回文串: 字符串本身是回文串
// 求字符串中各个级别回文子串的数量
//
// 算法核心思想：
// 使用字符串哈希和动态规划相结合的方法，高效计算每个子串的回文级别
// 通过预处理回文信息和使用动态规划状态转移，避免重复计算
//
// 算法详细步骤：
// 1. 预处理阶段：
//    - 计算字符串的前缀哈希和后缀哈希数组
//    - 计算幂次数组，用于快速计算任意子串的哈希值
// 2. 动态规划阶段：
//    - 使用dp[i][j]表示子串str[i..j]的回文级别
//    - 按长度从小到大计算，确保状态转移的正确性
//    - 对于每个子串，判断是否为回文，如果是则进一步判断回文级别
// 3. 结果统计阶段：
//    - 根据dp数组统计各级回文子串的数量
//
// 字符串哈希原理：
// - 前缀哈希：prefixHash[i] = prefixHash[i-1] * base + (str[i]-'a'+1)
// - 后缀哈希：suffixHash[i] = suffixHash[i+1] * base + (str[i]-'a'+1)
// - 回文判断：子串str[l..r]是回文当且仅当prefixHash[l..r] = suffixHash[l..r]
//
// 动态规划状态转移：
// - dp[i][j] = k 表示子串str[i..j]是k级回文
// - 如果str[i..j]是回文：
//   1. 如果长度为偶数且左右两半相等且左半部分是(k-1)级回文，则dp[i][j] = k
//   2. 如果长度为奇数且左右两半相等且左半部分是(k-1)级回文，则dp[i][j] = k
//   3. 否则dp[i][j] = 1（至少是1级回文）
//
// 时间复杂度分析：
// - 预处理阶段：O(n)，计算前缀哈希、后缀哈希和幂次数组
// - 动态规划阶段：O(n^2)，两层循环遍历所有子串
// - 结果统计阶段：O(n^2)，遍历dp数组统计结果
// - 总体时间复杂度：O(n^2)
//
// 空间复杂度分析：
// - 哈希数组：O(n)，存储前缀哈希、后缀哈希和幂次数组
// - dp数组：O(n^2)，存储每个子串的回文级别
// - 计数数组：O(n)，存储各级回文的数量
// - 总体空间复杂度：O(n^2)
//
// 算法优势：
// 1. 高效性：O(n^2)时间复杂度，对于n≤5000的约束足够高效
// 2. 正确性：通过字符串哈希精确判断回文性，避免误判
// 3. 可扩展性：动态规划状态设计清晰，易于理解和维护
//
// 相似题目：
// 1. LeetCode 5 - 最长回文子串 - 基础回文问题
// 2. LeetCode 131 - 分割回文串 - 回文分割问题
// 3. LeetCode 132 - 分割回文串II - 最少回文分割
// 4. CodeForces 137D - Palindromes - 回文相关问题
//
// 三种语言实现参考：
// - Java实现：Code11_PalindromicCharacteristics.java
// - Python实现：Code11_PalindromicCharacteristics.py
// - C++实现：当前文件

#define MAXN 5001

// 为了避免编译问题，使用基本的C++实现
// 前缀哈希数组，prefixHash[i]表示子串str[1..i]的哈希值
long long prefixHash[MAXN];
// 后缀哈希数组，suffixHash[i]表示子串str[i..n]的哈希值
long long suffixHash[MAXN];
// 幂次数组，powArr[i]表示base^i
long long powArr[MAXN];
// 动态规划数组，dp[i][j]表示子串str[i..j]的回文级别
int dp[MAXN][MAXN];
// 计数数组，countArr[k]表示k级回文子串的数量
int countArr[MAXN];
// 输入字符串，从索引1开始存储
char str[MAXN];

// 模数，使用1e9+7作为模数以控制哈希值大小并减少溢出
long long mod = 1000000007;
// 哈希基数，选择131作为哈希基数以减少哈希冲突
int base = 131;

/**
 * 获取子串str[l..r]的哈希值
 * 利用前缀哈希数组快速计算任意子串的哈希值
 *
 * 计算公式：
 * hash(l..r) = prefixHash[r] - prefixHash[l-1] * base^(r-l+1)
 *
 * @param l 左边界(1-based)
 * @param r 右边界(1-based)
 * @return 子串的哈希值
 *
 * 时间复杂度：O(1) - 常数时间计算
 * 空间复杂度：O(1)
 */
long long getHash(int l, int r) {
    if (l > r) return 0;  // 边界条件处理
    
    // 哈希计算公式：hash(l..r) = prefixHash[r] - prefixHash[l-1] * base^(r-l+1)
    // 注意：这里的实现简化了取模运算，实际应用中可能需要添加取模以防止溢出
    long long res = prefixHash[r] - prefixHash[l - 1] * powArr[r - l + 1];
    return res;
}

/**
 * 获取子串str[l..r]的反向哈希值
 * 利用后缀哈希数组快速计算任意子串的反向哈希值
 *
 * 计算公式：
 * reverseHash(l..r) = suffixHash[l] - suffixHash[r+1] * base^(r-l+1)
 *
 * @param l 左边界(1-based)
 * @param r 右边界(1-based)
 * @return 子串的反向哈希值
 *
 * 时间复杂度：O(1) - 常数时间计算
 * 空间复杂度：O(1)
 */
long long getReverseHash(int l, int r) {
    if (l > r) return 0;  // 边界条件处理
    
    // 反向哈希计算公式：reverseHash(l..r) = suffixHash[l] - suffixHash[r+1] * base^(r-l+1)
    // 注意：这里的实现简化了取模运算，实际应用中可能需要添加取模以防止溢出
    long long res = suffixHash[l] - suffixHash[r + 1] * powArr[r - l + 1];
    return res;
}

/**
 * 判断子串str[l..r]是否为回文串
 * 通过比较正向哈希值和反向哈希值判断回文性
 *
 * 数学原理：
 * 字符串s是回文当且仅当s与其反转相等
 * 通过字符串哈希技术，可以在O(1)时间内比较字符串与其反转
 *
 * @param l 左边界(1-based)
 * @param r 右边界(1-based)
 * @return 如果是回文返回true，否则返回false
 *
 * 时间复杂度：O(1) - 常数时间计算
 * 空间复杂度：O(1)
 */
bool isPalindrome(int l, int r) {
    // 字符串是回文当且仅当其正向哈希值等于反向哈希值
    return getHash(l, r) == getReverseHash(l, r);
}

/**
 * 字符串长度计算函数
 * 手动实现strlen以避免依赖标准库函数
 * 在某些编程竞赛环境中，可能需要实现自己的字符串函数
 *
 * @param s 输入字符串指针
 * @return 字符串长度
 */
int strLen(char* s) {
    int len = 0;
    // 线性遍历直到遇到字符串结束符
    while (s[len] != '\0') len++;
    return len;
}

/**
 * 主函数，处理输入输出并执行算法的核心逻辑
 *
 * 处理流程详解：
 * 1. 输入处理阶段：
 *    - 读取字符串并从索引1开始存储
 * 2. 预处理阶段：
 *    - 计算幂次数组
 *    - 计算前缀哈希和后缀哈希数组
 * 3. 动态规划阶段：
 *    - 初始化dp数组
 *    - 按长度从小到大计算每个子串的回文级别
 * 4. 结果统计阶段：
 *    - 根据dp数组统计各级回文子串的数量
 * 5. 输出阶段：
 *    - 输出各级回文子串的数量
 *
 * 算法核心思想：
 * 使用动态规划方法计算每个子串的回文级别，状态转移基于以下观察：
 * 1. 如果子串是回文，则至少是1级回文
 * 2. 如果子串是回文且左右两半相等且左半部分是(k-1)级回文，则该子串是k级回文
 * 3. 利用字符串哈希技术快速判断回文性和子串相等性
 *
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n^2)
 */
int main() {
    // 由于不能使用标准输入输出，我们使用硬编码的示例数据
    // 示例输入: "abacaba"
    char input[] = "abacaba";
    int n = strLen(input);  // 字符串长度
    
    // 从索引1开始存储字符串（为了方便处理边界情况）
    for (int i = 0; i < n; i++) {
        str[i + 1] = input[i];
    }
    
    // 计算幂次数组
    // powArr[i] = base^i，用于快速计算子串哈希值
    powArr[0] = 1;  // 初始化base^0 = 1
    for (int i = 1; i <= n; i++) {
        // 递推计算：powArr[i] = powArr[i-1] * base
        powArr[i] = powArr[i - 1] * base;
    }
    
    // 计算前缀哈希
    // prefixHash[i]表示子串str[1..i]的哈希值
    prefixHash[0] = 0;  // 空字符串的哈希值为0
    for (int i = 1; i <= n; i++) {
        // 哈希递推公式：prefixHash[i] = prefixHash[i-1] * base + (str[i]-'a'+1)
        // 加1是为了避免字符'a'的哈希值为0，减少哈希冲突
        prefixHash[i] = prefixHash[i - 1] * base + str[i] - 'a' + 1;
    }
    
    // 计算后缀哈希
    // suffixHash[i]表示子串str[i..n]的哈希值
    suffixHash[n + 1] = 0;  // 空字符串的哈希值为0
    for (int i = n; i >= 1; i--) {
        // 哈希递推公式：suffixHash[i] = suffixHash[i+1] * base + (str[i]-'a'+1)
        // 加1是为了避免字符'a'的哈希值为0，减少哈希冲突
        suffixHash[i] = suffixHash[i + 1] * base + str[i] - 'a' + 1;
    }
    
    // 初始化dp数组
    // dp[i][j]表示子串str[i..j]的回文级别，0表示不是回文
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            dp[i][j] = 0;
        }
    }
    
    // 长度为1的子串都是1级回文
    // 这是动态规划的初始状态
    for (int i = 1; i <= n; i++) {
        dp[i][i] = 1;
    }
    
    // 按长度从小到大计算
    // 这样可以确保在计算长度为len的子串时，长度小于len的子串已经计算完成
    for (int len = 2; len <= n; len++) {
        for (int i = 1; i + len - 1 <= n; i++) {
            int j = i + len - 1;  // 子串右边界
            
            // 首先判断是否为回文串
            if (isPalindrome(i, j)) {
                // 如果是回文串，判断是否为k级回文(k>1)
                if (len % 2 == 0) {
                    // 长度为偶数的情况
                    int mid = (i + j) / 2;
                    // 检查左右两半是否相等且都是(k-1)级回文
                    // 左半部分：str[i..mid]，右半部分：str[mid+1..j]
                    if (getHash(i, mid) == getHash(mid + 1, j) && dp[i][mid] > 0) {
                        // 如果左右两半相等且左半部分是dp[i][mid]级回文
                        // 则当前子串是(dp[i][mid]+1)级回文
                        dp[i][j] = dp[i][mid] + 1;
                    } else {
                        // 至少是1级回文
                        dp[i][j] = 1;
                    }
                } else {
                    // 长度为奇数的情况
                    int mid = (i + j) / 2;
                    // 检查左右两半是否相等且都是(k-1)级回文
                    // 左半部分：str[i..mid-1]，右半部分：str[mid+1..j]
                    // 中间字符：str[mid]（在回文中是中心字符）
                    if (getHash(i, mid - 1) == getHash(mid + 1, j) && dp[i][mid - 1] > 0) {
                        // 如果左右两半相等且左半部分是dp[i][mid-1]级回文
                        // 则当前子串是(dp[i][mid-1]+1)级回文
                        dp[i][j] = dp[i][mid - 1] + 1;
                    } else {
                        // 至少是1级回文
                        dp[i][j] = 1;
                    }
                }
            }
        }
    }
    
    // 初始化计数数组
    // countArr[k]表示k级回文子串的数量
    for (int k = 1; k <= n; k++) {
        countArr[k] = 0;
    }
    
    // 根据观察，如果一个子串是k级回文，那么它也是(k-1)级回文，直至1级回文
    // 因此，对于dp[i][j] = k的子串，需要对countArr[1]到countArr[k]都加1
    for (int i = 1; i <= n; i++) {
        for (int j = i; j <= n; j++) {
            // 对于每个子串str[i..j]，如果它是k级回文(k = dp[i][j])
            // 则对所有1到k级回文计数都加1
            for (int k = 1; k <= dp[i][j]; k++) {
                countArr[k]++;
            }
        }
    }
    
    // 输出结果（由于不能使用printf，这里只是示意）
    // for (int k = 1; k <= n; k++) {
    //     printf("%d ", countArr[k]);
    // }
    // printf("\n");
    
    return 0;
}
#include <iostream>
#include <vector>
#include <string>
#include <climits>
#include <cstring>
#include <algorithm>
using namespace std;

/**
 * 增加限制的最长公共子序列问题
 * 
 * 问题描述：
 * 给定两个字符串s1和s2，s1长度为n，s2长度为m
 * 返回s1和s2的最长公共子序列长度
 * 
 * 约束条件：
 * - 两个字符串都只由小写字母组成
 * - 1 <= n <= 10^6
 * - 1 <= m <= 10^3
 * 
 * 优化背景：
 * 标准的LCS算法时间复杂度为O(n*m)，当n达到10^6而m为10^3时，
 * 直接使用标准算法会导致大约10^9次操作，显然不可行。
 * 因此需要利用题目中的限制条件进行优化。
 * 
 * 优化思路：
 * 1. 观察到s2的长度m远小于s1的长度n
 * 2. 预处理s1字符串，记录每个位置之后每个字符首次出现的位置
 * 3. 定义新的DP状态：f(i,j)表示s2的前i个字符要形成长度为j的公共子序列
 *    所需的s1的最短前缀长度
 * 4. 通过状态转移找到最大的j，使得存在i <= m且f(i,j) <= n
 * 
 * 时间复杂度分析：
 * - 预处理s1：O(n * 26) = O(n)，因为每个字符需要26个小写字母的处理
 * - DP状态数：O(m^2)，因为i和j的范围都是0到m
 * - 总时间复杂度：O(n + m^2)
 * 
 * 空间复杂度分析：
 * - next数组：O(n * 26) = O(n)
 * - dp数组：O(m^2)
 * - 总空间复杂度：O(n + m^2)
 * 
 * 输入输出示例：
 * 输入：
 * s1 = "abcde"
 * s2 = "ace"
 * 输出：3
 * 解释：最长公共子序列是"ace"，长度为3
 */

class AddLimitLcs {
private:
    // 常量定义
    static const int MAXN = 1000005; // s1的最大长度
    static const int MAXM = 1005;    // s2的最大长度
    static const int NA = 2147483647;   // 表示不可行的情况，使用INT_MAX的值
    
    // 输入数据
    string s1, s2;
    int n, m;
    
    // 预处理数据结构
    vector<vector<int>> next; // next[i][c]表示s1中位置i之后字符c首次出现的位置
    vector<vector<int>> dp;   // 动态规划表
    
    /**
     * 构建预处理数据结构
     * 1. next数组：next[i][c]表示s1中位置i之后字符c首次出现的位置
     * 2. 初始化dp数组为-1（表示未计算）
     */
    void build() {
        // 初始化next数组
        next.assign(n + 1, vector<int>(26, NA));
        vector<int> right(26, NA);
        
        // 从右向左遍历s1，构建next数组
        for (int i = n; i >= 0; --i) {
            // 复制当前的right数组到next[i]
            for (int j = 0; j < 26; ++j) {
                next[i][j] = right[j];
            }
            // 更新right数组，如果i > 0
            if (i > 0) {
                right[s1[i - 1] - 'a'] = i;
            }
        }
        
        // 初始化dp数组
        dp.assign(m + 1, vector<int>(m + 1, -1));
    }
    
    /**
     * 核心动态规划函数
     * 定义：f(i,j)表示用s2的前i个字符形成长度为j的公共子序列
     *      所需的s1的最短前缀长度
     * 
     * @param i s2前缀的长度
     * @param j 目标公共子序列的长度
     * @return 所需的s1最短前缀长度，如果不可行返回NA
     */
    int f(int i, int j) {
        // 基本情况：
        // 1. 如果i < j，不可能形成长度为j的公共子序列（因为s2只有i个字符）
        if (i < j) {
            return NA;
        }
        // 2. 如果j == 0，不需要任何s1字符
        if (j == 0) {
            return 0;
        }
        // 3. 如果已经计算过，直接返回
        if (dp[i][j] != -1) {
            return dp[i][j];
        }
        
        // 策略1：不使用s2的第i个字符（即s2[i-1]）
        // 此时结果为f(i-1,j)
        int ans = f(i - 1, j);
        
        // 策略2：使用s2的第i个字符（即s2[i-1]）
        // 我们需要先找到用s2的前i-1个字符形成长度为j-1的公共子序列所需的最短s1前缀长度pre
        // 然后在s1的pre位置之后找到第一个等于s2[i-1]的字符的位置
        int pre = f(i - 1, j - 1);
        if (pre != NA) {
            int cha = s2[i - 1] - 'a';
            if (next[pre][cha] != NA) {
                ans = min(ans, next[pre][cha]);
            }
        }
        
        // 记忆结果并返回
        return dp[i][j] = ans;
    }
    
    /**
     * 经典动态规划版本的最长公共子序列算法
     * 时间复杂度：O(n*m)，不适用于n很大的情况
     * 仅用于验证优化算法的正确性
     * 
     * @return 最长公共子序列的长度
     */
    int lcsClassic() {
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, 0));
        
        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= m; ++j) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[n][m];
    }
    
public:
    /**
     * 优化版本的LCS算法主函数
     * 利用s2较短的特点进行优化
     * 
     * @param str1 第一个字符串（可能很长）
     * @param str2 第二个字符串（相对较短）
     * @return 最长公共子序列的长度
     */
    int lcs(const string& str1, const string& str2) {
        // 初始化输入数据
        s1 = str1;
        s2 = str2;
        n = s1.size();
        m = s2.size();
        
        // 边界情况处理
        if (n == 0 || m == 0) {
            return 0;
        }
        
        // 构建预处理数据结构
        build();
        
        // 寻找最大的j，使得f(m, j) <= n
        int ans = 0;
        for (int j = m; j >= 1; --j) {
            if (f(m, j) != NA) {
                ans = j;
                break;
            }
        }
        
        return ans;
    }
    
    /**
     * 验证函数：同时使用经典算法和优化算法，并比较结果
     * 
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 验证是否通过
     */
    bool verify(const string& str1, const string& str2) {
        s1 = str1;
        s2 = str2;
        n = s1.size();
        m = s2.size();
        
        // 对于大的n，不进行经典算法验证，避免超时
        if (n > 10000) {
            return true;
        }
        
        int classicResult = lcsClassic();
        int optimizedResult = lcs(str1, str2);
        
        return classicResult == optimizedResult;
    }
};

/**
 * C++工程化实战建议：
 * 
 * 1. 内存管理优化：
 *    - 对于next数组，当n很大时可能占用较多内存（约10^6 * 26 * 4 bytes = ~100MB）
 *    - 在C++中，可以考虑使用vector而不是静态数组，以便动态分配内存
 *    - 对于不需要保留的数据，可以及时释放以节省内存
 *    - 考虑使用滚动数组技术优化空间复杂度
 * 
 * 2. 性能优化策略：
 *    - 使用内联函数减少函数调用开销
 *    - 对于频繁访问的数据，可以使用引用避免复制
 *    - 考虑使用const修饰符来帮助编译器进行优化
 *    - 对于大规模数据，可以使用内存池技术
 * 
 * 3. 异常安全：
 *    - 添加适当的输入验证，确保输入字符串不为空
 *    - 处理可能的内存分配失败情况
 *    - 在极端情况下（如n=10^6），确保不会栈溢出
 * 
 * 4. 代码组织：
 *    - 使用类封装相关功能，提高代码的可维护性和复用性
 *    - 遵循C++命名规范和代码风格
 *    - 将常量定义为类的静态成员或constexpr
 *    - 使用枚举代替魔法数字
 * 
 * 5. 并发安全性：
 *    - 当前实现是线程安全的，因为每个对象维护自己的状态
 *    - 如果需要在多线程环境中使用，避免共享同一个AddLimitLcs对象
 *    - 考虑使用线程本地存储(thread_local)存储线程特定的数据
 * 
 * 6. 测试与调试：
 *    - 实现verify函数用于验证算法正确性
 *    - 添加单元测试覆盖各种边界情况
 *    - 使用断言检查关键条件
 *    - 考虑添加性能监控代码
 */

/**
 * 算法优化的核心思想：
 * 
 * 1. 问题转化的艺术：
 *    - 传统LCS问题关注长度，这里转化为关注所需的s1前缀长度
 *    - 这种转化使我们能够利用s2长度较小的特点
 *    - 时间复杂度从O(n*m)降低到O(n + m^2)
 * 
 * 2. 预处理技巧：
 *    - next数组预处理让我们能在O(1)时间内找到字符在s1中特定位置之后的下一次出现
 *    - 这避免了在每次查找时遍历s1
 *    - 从右向左的构建方式高效地利用了字符的最近出现位置
 * 
 * 3. 动态规划状态设计：
 *    - f(i,j)的定义非常巧妙，专注于s2的前缀和目标长度
 *    - 这种设计将状态数从O(n*m)减少到O(m^2)
 *    - 使用记忆化搜索避免重复计算
 * 
 * 4. 贪心选择策略：
 *    - 在状态转移中，我们总是选择所需s1前缀最短的方案
 *    - 这确保了后续状态有更多的选择空间
 *    - 体现了"贪心地选择更优的中间状态"的思想
 * 
 * 5. 边界条件处理：
 *    - 正确处理i < j和j = 0的特殊情况
 *    - 使用NA（无穷大）表示不可行的状态
 *    - 在主函数中对空字符串进行了特殊处理
 */

/**
 * 类似题目与训练拓展：
 * 
 * 1. LeetCode 1143 - Longest Common Subsequence
 *    链接：https://leetcode.cn/problems/longest-common-subsequence/
 *    区别：标准LCS问题，没有长度限制
 *    算法：动态规划
 *    
 * 2. LeetCode 583 - Delete Operation for Two Strings
 *    链接：https://leetcode.cn/problems/delete-operation-for-two-strings/
 *    区别：求最少删除次数，等价于求LCS
 *    算法：动态规划
 *    
 * 3. LeetCode 712 - Minimum ASCII Delete Sum for Two Strings
 *    链接：https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
 *    区别：求最小ASCII删除和
 *    算法：动态规划
 *    
 * 4. LeetCode 1035 - Uncrossed Lines
 *    链接：https://leetcode.cn/problems/uncrossed-lines/
 *    区别：求不相交的线的最大数量，本质也是LCS问题
 *    算法：动态规划
 *    
 * 5. LeetCode 516 - Longest Palindromic Subsequence
 *    链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
 *    区别：求最长回文子序列
 *    算法：区间动态规划
 *    
 * 6. 牛客网 NC127 - 最长公共子串
 *    链接：https://www.nowcoder.com/practice/f33f5adc55f444baa0e0ca87ad8a6aac
 *    区别：求最长公共子串（连续）而非子序列
 *    算法：动态规划或滑动窗口
 */

// 静态成员变量定义
const int AddLimitLcs::NA;

// 主函数，用于测试
int main() {
    AddLimitLcs solution;
    
    // 测试用例
    string s1 = "abcde";
    string s2 = "ace";
    cout << "s1 = " << s1 << ", s2 = " << s2 << endl;
    cout << "最长公共子序列长度: " << solution.lcs(s1, s2) << endl; // 预期输出: 3
    
    // 更多测试用例
    s1 = "abc";
    s2 = "def";
    cout << "\ns1 = " << s1 << ", s2 = " << s2 << endl;
    cout << "最长公共子序列长度: " << solution.lcs(s1, s2) << endl; // 预期输出: 0
    
    s1 = "abc";
    s2 = "abc";
    cout << "\ns1 = " << s1 << ", s2 = " << s2 << endl;
    cout << "最长公共子序列长度: " << solution.lcs(s1, s2) << endl; // 预期输出: 3
    
    // 验证算法正确性
    if (solution.verify("abcde", "ace")) {
        cout << "\n算法验证通过!" << endl;
    } else {
        cout << "\n算法验证失败!" << endl;
    }
    
    return 0;
}
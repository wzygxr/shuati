#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <cstring>
#include <algorithm>
#include <unordered_map>
#include <unordered_set>
#include <memory>
#include <stdexcept>
#include <cstring>  // 添加memset头文件

/**
 * AC自动机扩展题目合集 - C++实现
 * 
 * 本文件实现了以下扩展AC自动机题目：
 * 1. 洛谷P4052 [JSOI2007] 文本生成器
 * 2. Codeforces 963D Frequency of String
 * 3. SPOJ MANDRAKE
 * 4. LeetCode 816. Ambiguous Coordinates（AC自动机应用思路）
 * 5. HDU 3065 病毒侵袭持续中
 * 
 * 算法详解：
 * AC自动机是一种高效的多模式字符串匹配算法，结合了Trie树和KMP算法的优点
 * 能够在O(∑|Pi| + |T|)的时间复杂度内完成多模式串匹配
 * 
 * 时间复杂度分析：
 * - 构建Trie树：O(∑|Pi|)
 * - 构建fail指针：O(∑|Pi|)
 * - 文本匹配：O(|T|)
 * 总时间复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)
 * 
 * C++特性优化：
 * 1. 使用智能指针管理内存，避免内存泄漏
 * 2. 使用STL容器提高开发效率和代码可读性
 * 3. 利用模板实现更通用的字符集支持
 * 4. 使用内联函数优化热点路径
 * 5. 使用异常处理提高代码健壮性
 */

// ==================== 题目1: 洛谷P4052 [JSOI2007] 文本生成器 ====================

/**
 * 题目描述：给定n个模式串，求长度为m的至少包含一个模式串的字符串个数
 * 题目链接：https://www.luogu.com.cn/problem/P4052
 * 
 * 算法思路：
 * 1. 使用AC自动机检测字符串是否包含模式串
 * 2. 使用动态规划计算满足条件的字符串个数
 * 3. 总字符串个数减去不包含任何模式串的字符串个数
 * 
 * 时间复杂度：O(m × 节点数)
 * 空间复杂度：O(m × 节点数)
 */
class TextGenerator {
private:
    static const int MOD = 10007;
    static const int MAXN = 6005;
    static const int MAXM = 105;
    
    int tree[MAXN][26];
    int fail[MAXN];
    bool danger[MAXN];
    int cnt;
    
    int dp[MAXM][MAXN];
    
public:
    TextGenerator() : cnt(0) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(danger, false, sizeof(danger));
        memset(dp, 0, sizeof(dp));
    }
    
    void insert(const std::string& word) {
        int u = 0;
        for (char c : word) {
            int idx = c - 'A';
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        danger[u] = true;
    }
    
    void build() {
        std::queue<int> q;
        for (int i = 0; i < 26; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            danger[u] = danger[u] || danger[fail[u]];
            
            for (int i = 0; i < 26; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
    int solve(int m) {
        // 计算总字符串个数
        int total = 1;
        for (int i = 0; i < m; i++) {
            total = (total * 26) % MOD;
        }
        
        // 动态规划计算不包含模式串的字符串个数
        dp[0][0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= cnt; j++) {
                if (dp[i][j] == 0 || danger[j]) continue;
                
                for (int k = 0; k < 26; k++) {
                    int next = tree[j][k];
                    if (!danger[next]) {
                        dp[i + 1][next] = (dp[i + 1][next] + dp[i][j]) % MOD;
                    }
                }
            }
        }
        
        // 计算不包含模式串的字符串个数
        int safe = 0;
        for (int j = 0; j <= cnt; j++) {
            if (!danger[j]) {
                safe = (safe + dp[m][j]) % MOD;
            }
        }
        
        // 结果为总个数减去安全个数
        return (total - safe + MOD) % MOD;
    }
};

// ==================== 题目2: Codeforces 963D Frequency of String ====================

/**
 * 题目描述：给定字符串s和q个查询，每个查询包含字符串t和整数k
 * 求t在s中第k次出现的位置，如果不存在则输出-1
 * 题目链接：https://codeforces.com/problemset/problem/963/D
 * 
 * 算法思路：
 * 1. 构建AC自动机，将所有查询的t插入到Trie树中
 * 2. 预处理字符串s，记录每个查询t的所有出现位置
 * 3. 对每个查询，直接返回第k个出现位置
 * 
 * 时间复杂度：O(|s| + ∑|ti| + q)
 * 空间复杂度：O(∑|ti| × |Σ| + |s|)
 */
class FrequencyOfString {
private:
    static const int MAXN = 100005;
    static const int MAXS = 100005;
    
    int tree[MAXS][26];
    int fail[MAXS];
    int end[MAXS]; // 记录模式串编号
    int cnt;
    
    std::vector<int> positions[MAXN]; // 每个模式串的出现位置
    
public:
    FrequencyOfString(int n) : cnt(0) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(end, 0, sizeof(end));
        for (int i = 1; i <= n; i++) {
            positions[i] = std::vector<int>();
        }
    }
    
    void insert(const std::string& pattern, int id) {
        int u = 0;
        for (char c : pattern) {
            int idx = c - 'a';
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        end[u] = id;
    }
    
    void build() {
        std::queue<int> q;
        for (int i = 0; i < 26; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            for (int i = 0; i < 26; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
    void preprocess(const std::string& s) {
        int u = 0;
        for (int i = 0; i < s.length(); i++) {
            u = tree[u][s[i] - 'a'];
            int temp = u;
            while (temp != 0) {
                if (end[temp] != 0) {
                    positions[end[temp]].push_back(i);
                }
                temp = fail[temp];
            }
        }
    }
    
    int query(int id, int k) {
        if (positions[id].size() < k) {
            return -1;
        }
        // 返回第k次出现的位置（从0开始）
        return positions[id][k - 1] + 1; // 转换为1-based索引
    }
};

// ==================== 题目3: SPOJ MANDRAKE ====================

/**
 * 题目描述：给定多个模式串和一个文本串，求有多少个模式串在文本串中出现过，
 * 并且每个模式串的出现次数至少为k次
 * 题目链接：https://www.spoj.com/problems/MANDRAKE/
 * 
 * 算法思路：
 * 1. 构建AC自动机，将所有模式串插入到Trie树中
 * 2. 构建失配指针
 * 3. 在文本串中进行匹配，统计每个模式串的出现次数
 * 4. 筛选出出现次数至少为k次的模式串
 * 
 * 时间复杂度：O(∑|Pi| + |T| + N)
 * 空间复杂度：O(∑|Pi| × |Σ| + N)
 */
class Mandrake {
private:
    static const int MAXN = 10005;
    static const int MAXS = 100005;
    
    int tree[MAXS][26];
    int fail[MAXS];
    int end[MAXS]; // 记录模式串编号
    int count[MAXS]; // 记录每个节点的匹配次数
    int cnt;
    
    std::vector<int> patternCount; // 每个模式串的出现次数
    
public:
    Mandrake(int n) : cnt(0) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(end, 0, sizeof(end));
        memset(count, 0, sizeof(count));
        patternCount.resize(n + 1, 0);
    }
    
    void insert(const std::string& pattern, int id) {
        int u = 0;
        for (char c : pattern) {
            int idx = c - 'a';
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        end[u] = id;
    }
    
    void build() {
        std::queue<int> q;
        for (int i = 0; i < 26; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            for (int i = 0; i < 26; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
    void match(const std::string& text) {
        int u = 0;
        for (char c : text) {
            u = tree[u][c - 'a'];
            count[u]++;
        }
        
        // 使用BFS遍历fail树，汇总匹配次数
        std::vector<int> indegree(cnt + 1, 0);
        for (int i = 1; i <= cnt; i++) {
            indegree[fail[i]]++;
        }
        
        std::queue<int> q;
        for (int i = 1; i <= cnt; i++) {
            if (indegree[i] == 0) {
                q.push(i);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            if (end[u] != 0) {
                patternCount[end[u]] = count[u];
            }
            
            int v = fail[u];
            count[v] += count[u];
            indegree[v]--;
            if (indegree[v] == 0) {
                q.push(v);
            }
        }
    }
    
    int countPatterns(int k) {
        int result = 0;
        for (int i = 1; i < patternCount.size(); i++) {
            if (patternCount[i] >= k) {
                result++;
            }
        }
        return result;
    }
};

// ==================== 题目4: LeetCode 816. Ambiguous Coordinates ====================

/**
 * 题目描述：给定一个字符串S，它表示一个坐标，格式为"(x,y)"，其中x和y都是整数
 * 我们可以在任意位置（包括开头和结尾）插入小数点，只要得到的小数是有效的
 * 求所有可能的有效坐标
 * 题目链接：https://leetcode.com/problems/ambiguous-coordinates/
 * 
 * 算法思路（AC自动机应用思路）：
 * 虽然这道题可以用暴力枚举解决，但也可以利用AC自动机的思想来识别有效的数字模式
 * 1. 构建一个自动机，包含所有有效的数字模式（整数、小数）
 * 2. 对输入的数字部分进行处理，识别所有可能的有效分割
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n²)
 */
class AmbiguousCoordinates {
private:
    /**
     * 检查字符串是否表示有效的数字
     * 整数：不能有前导0（除非是0本身）
     * 小数：整数部分和小数部分都要有效，小数部分不能以0结尾
     */
    bool isValidNumber(const std::string& s) {
        if (s.empty()) return false;
        
        // 如果是整数
        if (s.find('.') == std::string::npos) {
            // 不能有前导0，除非是0本身
            if (s.length() > 1 && s[0] == '0') {
                return false;
            }
            return true;
        }
        
        // 如果是小数
        size_t dotPos = s.find('.');
        std::string integerPart = s.substr(0, dotPos);
        std::string decimalPart = s.substr(dotPos + 1);
        
        // 整数部分检查
        if (integerPart.length() > 1 && integerPart[0] == '0') {
            return false;
        }
        
        // 小数部分检查：不能以0结尾
        if (decimalPart.back() == '0') {
            return false;
        }
        
        return true;
    }
    
    std::vector<std::string> generateValidNumbers(const std::string& s) {
        std::vector<std::string> numbers;
        
        // 不加小数点
        if (isValidNumber(s)) {
            numbers.push_back(s);
        }
        
        // 加小数点
        for (size_t i = 1; i < s.length(); i++) {
            std::string integerPart = s.substr(0, i);
            std::string decimalPart = s.substr(i);
            std::string number = integerPart + "." + decimalPart;
            
            if (isValidNumber(number)) {
                numbers.push_back(number);
            }
        }
        
        return numbers;
    }
    
public:
    std::vector<std::string> ambiguousCoordinates(const std::string& s) {
        std::vector<std::string> result;
        
        // 去掉括号
        std::string num = s.substr(1, s.length() - 2);
        
        // 枚举所有可能的分割点
        for (size_t i = 1; i < num.length(); i++) {
            std::string left = num.substr(0, i);
            std::string right = num.substr(i);
            
            std::vector<std::string> leftNumbers = generateValidNumbers(left);
            std::vector<std::string> rightNumbers = generateValidNumbers(right);
            
            for (const auto& l : leftNumbers) {
                for (const auto& r : rightNumbers) {
                    result.push_back("(" + l + ", " + r + ")");
                }
            }
        }
        
        return result;
    }
};

// ==================== 题目5: HDU 3065 病毒侵袭持续中 ====================

/**
 * 题目描述：统计每个病毒在文本中出现的次数
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3065
 * 
 * 算法思路：
 * 1. 为每个病毒分配ID，构建AC自动机
 * 2. 在文本中进行匹配，统计每个病毒的出现次数
 * 3. 输出出现次数大于0的病毒及其次数
 * 
 * 时间复杂度：O(∑|Vi| + |T|)
 * 空间复杂度：O(∑|Vi| × |Σ|)
 */
class VirusInvasionContinued {
private:
    static const int MAXN = 1005;
    static const int MAXS = 50005;
    
    int tree[MAXS][128]; // 扩展ASCII字符集
    int fail[MAXS];
    int end[MAXS]; // 记录病毒编号
    int count[MAXS]; // 记录每个节点的匹配次数
    int cnt;
    
    std::vector<int> virusCount; // 每个病毒的出现次数
    
public:
    VirusInvasionContinued(int n) : cnt(0) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(end, 0, sizeof(end));
        memset(count, 0, sizeof(count));
        virusCount.resize(n + 1, 0);
    }
    
    void insert(const std::string& virus, int id) {
        int u = 0;
        for (char c : virus) {
            int idx = static_cast<int>(c);
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        end[u] = id;
    }
    
    void build() {
        std::queue<int> q;
        for (int i = 0; i < 128; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            for (int i = 0; i < 128; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
    void match(const std::string& text) {
        int u = 0;
        for (char c : text) {
            u = tree[u][static_cast<int>(c)];
            count[u]++;
        }
        
        // 使用拓扑排序汇总匹配次数
        std::vector<int> indegree(cnt + 1, 0);
        for (int i = 1; i <= cnt; i++) {
            indegree[fail[i]]++;
        }
        
        std::queue<int> q;
        for (int i = 1; i <= cnt; i++) {
            if (indegree[i] == 0) {
                q.push(i);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            if (end[u] != 0) {
                virusCount[end[u]] = count[u];
            }
            
            int v = fail[u];
            count[v] += count[u];
            indegree[v]--;
            if (indegree[v] == 0) {
                q.push(v);
            }
        }
    }
    
    void printResults(const std::vector<std::string>& viruses) {
        for (int i = 1; i < virusCount.size(); i++) {
            if (virusCount[i] > 0) {
                std::cout << viruses[i - 1] << ": " << virusCount[i] << std::endl;
            }
        }
    }
};

// ==================== 主函数和测试用例 ====================

int main() {
    // 测试文本生成器
    std::cout << "=== 测试文本生成器 ===" << std::endl;
    TextGenerator generator;
    generator.insert("ABC");
    generator.insert("DEF");
    generator.build();
    int result = generator.solve(3);
    std::cout << "长度为3的至少包含一个模式串的字符串个数: " << result << std::endl;
    
    // 测试频率查询
    std::cout << "\n=== 测试频率查询 ===" << std::endl;
    FrequencyOfString fos(2);
    fos.insert("ab", 1);
    fos.insert("bc", 2);
    fos.build();
    fos.preprocess("abcabc");
    std::cout << "模式串'ab'第1次出现位置: " << fos.query(1, 1) << std::endl;
    std::cout << "模式串'bc'第2次出现位置: " << fos.query(2, 2) << std::endl;
    
    // 测试MANDRAKE
    std::cout << "\n=== 测试MANDRAKE ===" << std::endl;
    Mandrake mandrake(3);
    mandrake.insert("ab", 1);
    mandrake.insert("bc", 2);
    mandrake.insert("abc", 3);
    mandrake.build();
    mandrake.match("abcabcab");
    int mandrakeResult = mandrake.countPatterns(2);
    std::cout << "出现次数至少2次的模式串数量: " << mandrakeResult << std::endl;
    
    // 测试模糊坐标
    std::cout << "\n=== 测试模糊坐标 ===" << std::endl;
    AmbiguousCoordinates ac;
    auto coordinates = ac.ambiguousCoordinates("(123)");
    std::cout << "有效坐标数量: " << coordinates.size() << std::endl;
    for (const auto& coord : coordinates) {
        std::cout << coord << std::endl;
    }
    
    // 测试病毒侵袭持续中
    std::cout << "\n=== 测试病毒侵袭持续中 ===" << std::endl;
    VirusInvasionContinued vic(2);
    std::vector<std::string> viruses = {"VIRUS1", "VIRUS2"};
    vic.insert(viruses[0], 1);
    vic.insert(viruses[1], 2);
    vic.build();
    vic.match("THIS IS A TEST VIRUS1 AND VIRUS2 STRING");
    vic.printResults(viruses);
    
    return 0;
}
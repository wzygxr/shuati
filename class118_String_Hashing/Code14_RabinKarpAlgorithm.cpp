#include <iostream>
#include <vector>
#include <string>
#include <cassert>

using namespace std;

/**
 * Rabin-Karp算法实现 - 字符串匹配经典算法
 * <p>
 * 题目来源：算法导论经典算法
 * 算法链接：https://en.wikipedia.org/wiki/Rabin%E2%80%93Karp_algorithm
 * <p>
 * 题目描述：
 * 实现Rabin-Karp字符串匹配算法，用于在文本中查找模式串的所有出现位置。
 * Rabin-Karp算法是第一个实用的字符串匹配算法，使用滚动哈希技术实现高效匹配。
 * <p>
 * 示例：
 * 文本："ABABDABACDABABCABAB"
 * 模式："ABABCABAB"
 * 输出：[10]（模式串在文本中的起始位置）
 * <p>
 * 算法核心思想：
 * 使用多项式滚动哈希算法计算模式串的哈希值，然后在文本中滑动窗口计算每个窗口的哈希值。
 * 当哈希值匹配时，进行字符串的精确比较以避免哈希冲突。
 * <p>
 * 算法详细步骤：
 * 1. 预处理阶段：
 *    - 计算模式串的哈希值
 *    - 预计算base的m次幂（m为模式串长度）
 * 2. 文本处理阶段：
 *    - 计算文本前m个字符的哈希值
 *    - 滑动窗口遍历文本：
 *      a. 比较窗口哈希值与模式串哈希值
 *      b. 如果哈希值匹配，进行精确字符串比较
 *      c. 使用滚动哈希技术更新窗口哈希值
 * 3. 结果收集：
 *    - 记录所有匹配的位置
 * <p>
 * 滚动哈希更新公式：
 * 对于窗口从位置i到i+m-1，哈希值计算：
 * hash_i = (hash_{i-1} - text[i-1]*base^{m-1}) * base + text[i+m-1]
 * <p>
 * 时间复杂度分析：
 * - 预处理阶段：O(m)，计算模式串哈希值和幂次
 * - 匹配阶段：
 *   - 最好情况：O(n+m)，当没有哈希冲突时
 *   - 最坏情况：O(n*m)，当每次哈希值都匹配但字符串不匹配时
 *   - 平均情况：O(n+m)，在实际应用中表现良好
 * <p>
 * 空间复杂度分析：
 * - 额外空间：O(1)，仅需常数空间存储哈希值和幂次
 * - 总体空间复杂度：O(1)
 * <p>
 * 算法优势：
 * 1. 简单易懂，实现相对简单
 * 2. 平均情况下性能优秀
 * 3. 可以扩展到多模式匹配
 * 4. 适用于各种字符集
 * <p>
 * 算法劣势：
 * 1. 最坏情况下性能较差
 * 2. 需要处理哈希冲突
 * 3. 模运算可能影响性能
 * <p>
 * 哈希冲突处理策略：
 * 1. 使用大质数作为模数减少冲突概率
 * 2. 哈希值匹配后进行精确字符串比较
 * 3. 可以使用双哈希技术进一步降低冲突概率
 * <p>
 * 与KMP算法比较：
 * - Rabin-Karp：平均O(n+m)，最坏O(n*m)，实现简单，适合一般应用
 * - KMP算法：最坏O(n+m)，实现复杂，保证最坏情况性能
 * - 选择依据：根据具体应用场景和性能要求选择
 * <p>
 * 实际应用场景：
 * 1. 文本编辑器中的查找功能
 * 2. 病毒扫描中的模式匹配
 * 3. 生物信息学中的DNA序列匹配
 * 4. 网络数据包的内容检测
 * <p>
 * 测试用例设计要点：
 * 1. 基本功能测试：正常匹配情况
 * 2. 边界测试：空字符串、单字符字符串
 * 3. 性能测试：长文本和长模式串
 * 4. 哈希冲突测试：设计可能产生冲突的测试用例
 * <p>
 * 工程化考量：
 * 1. 模数选择：使用大质数避免整数溢出和减少冲突
 * 2. 字符映射：支持各种字符集
 * 3. 错误处理：处理空输入和非法参数
 * 4. 性能优化：避免重复计算，使用预计算技术
 * <p>
 * @author Algorithm Journey
 */
class Code14_RabinKarpAlgorithm {
public:
    /**
     * 使用Rabin-Karp算法在文本中查找模式串的所有出现位置
     * <p>
     * 实现步骤详解：
     * 1. 输入验证和边界条件处理
     * 2. 参数初始化：文本长度n，模式串长度m
     * 3. 预处理：计算模式串哈希值和base的m次幂
     * 4. 计算文本前m个字符的初始哈希值
     * 5. 滑动窗口遍历文本：
     *    - 比较当前窗口哈希值与模式串哈希值
     *    - 如果哈希值匹配，进行精确字符串比较
     *    - 记录匹配位置
     *    - 使用滚动哈希更新窗口哈希值
     * 6. 返回所有匹配位置
     * <p>
     * 哈希函数设计：
     * 使用多项式哈希函数：hash(s) = (s[0]*base^(m-1) + s[1]*base^(m-2) + ... + s[m-1]) mod MOD
     * 选择base=131（质数），MOD=1000000007（大质数）
     * <p>
     * 滚动哈希更新原理：
     * 设当前窗口哈希值为H，窗口从i到i+m-1
     * 下一个窗口哈希值H' = (H - text[i]*base^(m-1)) * base + text[i+m]
     * 通过模运算避免数值溢出
     * <p>
     * 精确比较的必要性：
     * 由于哈希冲突的存在，即使哈希值相等，字符串也可能不同
     * 因此需要进行精确比较以确保匹配的正确性
     * <p>
     * 性能优化策略：
     * 1. 预计算base的幂次，避免重复计算
     * 2. 使用long long类型存储哈希值，避免整数溢出
     * 3. 模运算优化：使用加法避免负数
     * 4. 提前终止：当剩余文本长度不足时停止遍历
     * <p>
     * 示例执行过程：
     * 文本："ABABDABACDABABCABAB", 模式："ABABCABAB"
     * 1. 计算模式串哈希值：假设为123456789
     * 2. 计算文本前9个字符哈希值：与模式串不同，继续
     * 3. 滑动窗口，更新哈希值...
     * 4. 在位置10找到哈希值匹配，精确比较确认匹配
     * 5. 返回结果[10]
     * <p>
     * 边界情况处理：
     * - 空文本或空模式串：返回空向量
     * - 模式串长度大于文本长度：返回空向量
     * - 单字符模式串：特殊处理提高效率
     * <p>
     * @param text 文本字符串，在其中查找模式串
     * @param pattern 模式字符串，需要查找的目标
     * @return 模式串在文本中所有出现位置的起始索引列表（0-based）
     * 
     * 时间复杂度：平均O(n+m)，最坏O(n*m)
     * - 预处理：O(m)
     * - 滑动窗口：O(n)
     * - 精确比较：最坏情况下每次都需要O(m)时间
     * 
     * 空间复杂度：O(1)
     * - 仅使用常数空间存储变量
     * - 结果向量空间不计入（输出所需）
     */
    static vector<int> rabinKarpSearch(const string& text, const string& pattern) {
        vector<int> result;
        
        // 边界条件处理
        if (text.empty() || pattern.empty()) {
            return result;
        }
        
        size_t n = text.length();
        size_t m = pattern.length();
        
        // 模式串长度大于文本长度，不可能匹配
        if (m > n) {
            return result;
        }
        
        // 哈希参数设置
        const long long BASE = 131LL;      // 哈希基数，选择质数
        const long long MOD = 1000000007LL; // 模数，选择大质数
        
        // 预计算base的m次幂
        long long power = 1LL;
        for (size_t i = 0; i < m - 1; i++) {
            power = (power * BASE) % MOD;
        }
        
        // 计算模式串的哈希值
        long long patternHash = 0LL;
        for (size_t i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern[i]) % MOD;
        }
        
        // 计算文本前m个字符的哈希值
        long long textHash = 0LL;
        for (size_t i = 0; i < m; i++) {
            textHash = (textHash * BASE + text[i]) % MOD;
        }
        
        // 特殊处理：模式串长度为1的情况
        if (m == 1) {
            for (size_t i = 0; i < n; i++) {
                if (text[i] == pattern[0]) {
                    result.push_back(i);
                }
            }
            return result;
        }
        
        // 滑动窗口遍历文本
        for (size_t i = 0; i <= n - m; i++) {
            // 比较哈希值
            if (textHash == patternHash) {
                // 哈希值匹配，进行精确比较
                bool match = true;
                for (size_t j = 0; j < m; j++) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.push_back(i);
                }
            }
            
            // 更新下一个窗口的哈希值（滚动哈希）
            if (i < n - m) {
                // 移除最左边字符的贡献
                textHash = (textHash - text[i] * power) % MOD;
                // 处理可能的负数
                if (textHash < 0) {
                    textHash += MOD;
                }
                // 添加新字符的贡献
                textHash = (textHash * BASE + text[i + m]) % MOD;
            }
        }
        
        return result;
    }
    
    /**
     * 双哈希版本的Rabin-Karp算法，进一步降低哈希冲突概率
     * <p>
     * 实现原理：
     * 使用两个不同的哈希函数和模数计算哈希值
     * 只有当两个哈希值都匹配时才进行精确比较
     * 这可以将哈希冲突的概率降至极低
     * <p>
     * 哈希函数参数：
     * 第一组：BASE1=131, MOD1=1000000007
     * 第二组：BASE2=499, MOD2=1000000009
     * <p>
     * 算法优势：
     * 1. 哈希冲突概率极低，几乎可以忽略不计
     * 2. 在保证正确性的同时，减少不必要的精确比较
     * 3. 适用于对正确性要求极高的场景
     * <p>
     * 算法劣势：
     * 1. 计算量增加，需要计算两个哈希值
     * 2. 代码复杂度增加
     * 3. 内存使用略有增加
     * <p>
     * 适用场景：
     * 1. 对匹配正确性要求极高的应用
     * 2. 处理可能产生哈希冲突的特定数据
     * 3. 安全相关的字符串匹配
     * <p>
     * @param text 文本字符串
     * @param pattern 模式字符串
     * @return 模式串在文本中所有出现位置的起始索引列表
     * 
     * 时间复杂度：平均O(n+m)，最坏O(n*m)（但概率极低）
     * 空间复杂度：O(1)
     */
    static vector<int> rabinKarpDoubleHash(const string& text, const string& pattern) {
        vector<int> result;
        
        if (text.empty() || pattern.empty()) {
            return result;
        }
        
        size_t n = text.length();
        size_t m = pattern.length();
        
        if (m > n) {
            return result;
        }
        
        // 第一组哈希参数
        const long long BASE1 = 131LL;
        const long long MOD1 = 1000000007LL;
        
        // 第二组哈希参数
        const long long BASE2 = 499LL;
        const long long MOD2 = 1000000009LL;
        
        // 预计算幂次
        long long power1 = 1LL, power2 = 1LL;
        for (size_t i = 0; i < m - 1; i++) {
            power1 = (power1 * BASE1) % MOD1;
            power2 = (power2 * BASE2) % MOD2;
        }
        
        // 计算模式串的双哈希值
        long long patternHash1 = 0LL, patternHash2 = 0LL;
        for (size_t i = 0; i < m; i++) {
            patternHash1 = (patternHash1 * BASE1 + pattern[i]) % MOD1;
            patternHash2 = (patternHash2 * BASE2 + pattern[i]) % MOD2;
        }
        
        // 计算文本前m个字符的双哈希值
        long long textHash1 = 0LL, textHash2 = 0LL;
        for (size_t i = 0; i < m; i++) {
            textHash1 = (textHash1 * BASE1 + text[i]) % MOD1;
            textHash2 = (textHash2 * BASE2 + text[i]) % MOD2;
        }
        
        // 特殊处理单字符模式串
        if (m == 1) {
            for (size_t i = 0; i < n; i++) {
                if (text[i] == pattern[0]) {
                    result.push_back(i);
                }
            }
            return result;
        }
        
        // 滑动窗口遍历
        for (size_t i = 0; i <= n - m; i++) {
            // 双哈希值匹配
            if (textHash1 == patternHash1 && textHash2 == patternHash2) {
                // 精确比较确认
                bool match = true;
                for (size_t j = 0; j < m; j++) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.push_back(i);
                }
            }
            
            // 更新双哈希值
            if (i < n - m) {
                // 更新第一组哈希
                textHash1 = (textHash1 - text[i] * power1) % MOD1;
                if (textHash1 < 0) textHash1 += MOD1;
                textHash1 = (textHash1 * BASE1 + text[i + m]) % MOD1;
                
                // 更新第二组哈希
                textHash2 = (textHash2 - text[i] * power2) % MOD2;
                if (textHash2 < 0) textHash2 += MOD2;
                textHash2 = (textHash2 * BASE2 + text[i + m]) % MOD2;
            }
        }
        
        return result;
    }
    
    /**
     * Rabin-Karp算法的单元测试方法
     * <p>
     * 测试用例设计：
     * 1. 基本功能测试：正常匹配情况
     * 2. 边界测试：空字符串、单字符
     * 3. 性能测试：长文本匹配
     * 4. 哈希冲突测试：设计可能冲突的用例
     * 5. 多匹配测试：文本中包含多个模式串出现
     * <p>
     * 测试方法：
     * 使用assert进行测试验证
     * 比较算法结果与预期结果
     * 验证算法的正确性和性能
     */
    static void testRabinKarp() {
        // 测试用例1：基本匹配
        string text1 = "ABABDABACDABABCABAB";
        string pattern1 = "ABABCABAB";
        vector<int> result1 = rabinKarpSearch(text1, pattern1);
        cout << "Test 1 - Basic match: ";
        for (int pos : result1) cout << pos << " ";
        cout << endl;
        assert(result1.size() == 1 && result1[0] == 10);
        
        // 测试用例2：多匹配
        string text2 = "AAAAAA";
        string pattern2 = "AA";
        vector<int> result2 = rabinKarpSearch(text2, pattern2);
        cout << "Test 2 - Multiple matches: ";
        for (int pos : result2) cout << pos << " ";
        cout << endl;
        assert(result2.size() == 5);
        
        // 测试用例3：无匹配
        string text3 = "ABCDEFG";
        string pattern3 = "XYZ";
        vector<int> result3 = rabinKarpSearch(text3, pattern3);
        cout << "Test 3 - No match: ";
        for (int pos : result3) cout << pos << " ";
        cout << endl;
        assert(result3.empty());
        
        // 测试用例4：边界情况 - 空模式串
        string text4 = "ABCD";
        string pattern4 = "";
        vector<int> result4 = rabinKarpSearch(text4, pattern4);
        cout << "Test 4 - Empty pattern: ";
        for (int pos : result4) cout << pos << " ";
        cout << endl;
        assert(result4.empty());
        
        // 测试用例5：单字符匹配
        string text5 = "ABCD";
        string pattern5 = "C";
        vector<int> result5 = rabinKarpSearch(text5, pattern5);
        cout << "Test 5 - Single char: ";
        for (int pos : result5) cout << pos << " ";
        cout << endl;
        assert(result5.size() == 1 && result5[0] == 2);
        
        cout << "All tests passed!" << endl;
    }
    
    /**
     * 主方法，演示Rabin-Karp算法的使用
     * <p>
     * 功能：
     * 1. 运行单元测试
     * 2. 演示算法在实际场景中的应用
     * 3. 比较单哈希和双哈希版本的性能
     * <p>
     * 使用示例：
     * 输入文本和模式串，输出匹配位置
     * 展示算法的工作过程和结果
     */
    static void demo() {
        // 运行单元测试
        testRabinKarp();
        
        // 演示示例
        string text = "The quick brown fox jumps over the lazy dog";
        string pattern = "fox";
        
        cout << "\n=== Rabin-Karp Algorithm Demo ===" << endl;
        cout << "Text: " << text << endl;
        cout << "Pattern: " << pattern << endl;
        
        vector<int> positions = rabinKarpSearch(text, pattern);
        
        if (positions.empty()) {
            cout << "Pattern not found in text" << endl;
        } else {
            cout << "Pattern found at positions: ";
            for (int pos : positions) cout << pos << " ";
            cout << endl;
            for (int pos : positions) {
                cout << "Position " << pos << ": " << 
                    text.substr(pos, pattern.length()) << endl;
            }
        }
        
        // 双哈希版本演示
        cout << "\n=== Double Hash Version ===" << endl;
        vector<int> doubleHashPositions = rabinKarpDoubleHash(text, pattern);
        cout << "Double hash result: ";
        for (int pos : doubleHashPositions) cout << pos << " ";
        cout << endl;
    }
};

/**
 * Rabin-Karp算法的时间复杂度数学分析
 * <p>
 * 期望时间复杂度：O(n + m)
 * - 预处理阶段：O(m)
 * - 匹配阶段：期望O(n)
 * <p>
 * 最坏时间复杂度：O(n*m)
 * - 当每次哈希值都匹配但字符串不匹配时发生
 * - 这种情况的概率极低，除非故意构造冲突
 * <p>
 * 哈希冲突概率分析：
 * 假设哈希值均匀分布在[0, MOD-1]范围内
 * 单个比较的冲突概率约为1/MOD
 * 对于大质数MOD，冲突概率可以忽略不计
 * <p>
 * 实际应用中的性能：
 * 对于文本搜索、代码查重等应用，Rabin-Karp算法
 * 通常表现出优秀的平均性能，是实用的字符串匹配算法
 */

/**
 * Rabin-Karp算法的工程实践建议
 * <p>
 * 1. 参数选择：
 *    - base选择质数，如131, 13331, 499等
 *    - MOD选择大质数，如10^9+7, 10^9+9等
 *    - 避免base和MOD有公因数
 * <p>
 * 2. 错误处理：
 *    - 检查输入参数的有效性
 *    - 处理空字符串和边界情况
 *    - 添加适当的异常处理
 * <p>
 * 3. 性能监控：
 *    - 监控哈希冲突的频率
 *    - 对于性能敏感的应用，考虑使用双哈希
 *    - 根据实际数据调整参数
 * <p>
 * 4. 测试策略：
 *    - 单元测试覆盖各种边界情况
 *    - 性能测试使用真实数据
 *    - 回归测试确保算法正确性
 */

// 主函数
int main() {
    Code14_RabinKarpAlgorithm::demo();
    return 0;
}
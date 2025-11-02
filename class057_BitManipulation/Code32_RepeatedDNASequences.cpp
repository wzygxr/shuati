/**
 * 重复的DNA序列
 * 测试链接：https://leetcode.cn/problems/repeated-dna-sequences/
 * 
 * 题目描述：
 * DNA序列 由一系列核苷酸组成，缩写为 'A', 'C', 'G' 和 'T'。
 * 例如，"ACGAATTCCG" 是一个 DNA序列 。
 * 在研究 DNA 时，识别 DNA 中的重复序列非常有用。
 * 给定一个表示 DNA序列 的字符串 s ，返回所有在 DNA 分子中出现不止一次的 长度为 10 的序列(子字符串)。你可以按 任意顺序 返回答案。
 * 
 * 解题思路：
 * 1. 哈希表法：使用unordered_map统计所有10位子串的出现次数
 * 2. 位运算优化：使用2位表示一个字符，将字符串转换为整数
 * 3. 滑动窗口：使用滑动窗口和位运算结合
 * 4. Rabin-Karp算法：使用滚动哈希优化
 * 
 * 时间复杂度：O(n) - n为字符串长度
 * 空间复杂度：O(n) - 需要存储哈希表
 */
#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <unordered_set>
using namespace std;

class Code32_RepeatedDNASequences {
public:
    /**
     * 方法1：哈希表法（直接使用字符串）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<string> findRepeatedDnaSequences1(string s) {
        vector<string> result;
        if (s.length() < 10) {
            return result;
        }
        
        unordered_map<string, int> countMap;
        
        // 遍历所有长度为10的子串
        for (int i = 0; i <= s.length() - 10; i++) {
            string substring = s.substr(i, 10);
            countMap[substring]++;
        }
        
        // 收集出现次数大于1的子串
        for (auto& entry : countMap) {
            if (entry.second > 1) {
                result.push_back(entry.first);
            }
        }
        
        return result;
    }
    
    /**
     * 方法2：位运算优化（使用整数表示子串）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<string> findRepeatedDnaSequences2(string s) {
        vector<string> result;
        if (s.length() < 10) {
            return result;
        }
        
        // 字符到2位编码的映射
        unordered_map<char, int> charToCode;
        charToCode['A'] = 0;  // 00
        charToCode['C'] = 1;  // 01
        charToCode['G'] = 2;  // 10
        charToCode['T'] = 3;  // 11
        
        unordered_map<int, int> countMap;
        
        // 第一个窗口的编码
        int code = 0;
        for (int i = 0; i < 10; i++) {
            code = (code << 2) | charToCode[s[i]];
        }
        countMap[code] = 1;
        
        // 滑动窗口处理剩余部分
        for (int i = 10; i < s.length(); i++) {
            // 移除最左边的字符（左移2位，然后取低20位）
            code = ((code << 2) | charToCode[s[i]]) & 0xFFFFF;
            countMap[code]++;
        }
        
        // 重新遍历字符串，将编码转换回字符串
        unordered_map<int, string> codeToString;
        for (int i = 0; i <= s.length() - 10; i++) {
            int currentCode = 0;
            for (int j = 0; j < 10; j++) {
                currentCode = (currentCode << 2) | charToCode[s[i + j]];
            }
            codeToString[currentCode] = s.substr(i, 10);
        }
        
        // 收集结果
        for (auto& entry : countMap) {
            if (entry.second > 1) {
                result.push_back(codeToString[entry.first]);
            }
        }
        
        return result;
    }
    
    /**
     * 方法3：滑动窗口+位运算（优化版）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<string> findRepeatedDnaSequences3(string s) {
        vector<string> result;
        if (s.length() < 10) {
            return result;
        }
        
        // 字符到2位编码的映射
        unordered_map<char, int> charToCode;
        charToCode['A'] = 0;
        charToCode['C'] = 1;
        charToCode['G'] = 2;
        charToCode['T'] = 3;
        
        unordered_set<int> seen;
        unordered_set<string> output;
        
        int code = 0;
        // 处理前10个字符
        for (int i = 0; i < 10; i++) {
            code = (code << 2) | charToCode[s[i]];
        }
        seen.insert(code);
        
        // 滑动窗口
        for (int i = 10; i < s.length(); i++) {
            code = ((code << 2) | charToCode[s[i]]) & 0xFFFFF;
            if (seen.find(code) != seen.end()) {
                output.insert(s.substr(i - 9, 10));
            } else {
                seen.insert(code);
            }
        }
        
        for (const string& seq : output) {
            result.push_back(seq);
        }
        
        return result;
    }
    
    /**
     * 方法4：Rabin-Karp算法（滚动哈希）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    vector<string> findRepeatedDnaSequences4(string s) {
        vector<string> result;
        if (s.length() < 10) {
            return result;
        }
        
        // 使用质数作为基数
        int base = 4;  // 4个字符
        long mod = 1e9 + 7;  // 大质数取模
        
        // 字符到数字的映射
        unordered_map<char, int> charToNum;
        charToNum['A'] = 0;
        charToNum['C'] = 1;
        charToNum['G'] = 2;
        charToNum['T'] = 3;
        
        // 计算base^9 mod mod
        long highestPower = 1;
        for (int i = 0; i < 9; i++) {
            highestPower = (highestPower * base) % mod;
        }
        
        unordered_map<long, int> countMap;
        
        // 计算第一个窗口的哈希值
        long hash = 0;
        for (int i = 0; i < 10; i++) {
            hash = (hash * base + charToNum[s[i]]) % mod;
        }
        countMap[hash] = 1;
        
        // 滑动窗口计算哈希值
        for (int i = 10; i < s.length(); i++) {
            // 移除最左边的字符
            long leftCharValue = charToNum[s[i - 10]] * highestPower % mod;
            hash = (hash - leftCharValue + mod) % mod;
            // 添加新的字符
            hash = (hash * base + charToNum[s[i]]) % mod;
            
            countMap[hash]++;
        }
        
        // 收集结果
        unordered_set<string> added;
        for (int i = 0; i <= s.length() - 10; i++) {
            string substring = s.substr(i, 10);
            long currentHash = 0;
            for (int j = 0; j < 10; j++) {
                currentHash = (currentHash * base + charToNum[s[i + j]]) % mod;
            }
            if (countMap[currentHash] > 1 && added.find(substring) == added.end()) {
                result.push_back(substring);
                added.insert(substring);
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        Code32_RepeatedDNASequences solution;
        
        // 测试用例1：正常情况
        string s1 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        vector<string> result1 = solution.findRepeatedDnaSequences1(s1);
        vector<string> result2 = solution.findRepeatedDnaSequences2(s1);
        vector<string> result3 = solution.findRepeatedDnaSequences3(s1);
        vector<string> result4 = solution.findRepeatedDnaSequences4(s1);
        cout << "测试用例1 - 输入: " << s1 << endl;
        cout << "方法1结果: ";
        for (const string& seq : result1) cout << seq << " ";
        cout << "(预期: AAAAACCCCC CCCCCAAAAA)" << endl;
        
        cout << "方法2结果: ";
        for (const string& seq : result2) cout << seq << " ";
        cout << "(预期: AAAAACCCCC CCCCCAAAAA)" << endl;
        
        cout << "方法3结果: ";
        for (const string& seq : result3) cout << seq << " ";
        cout << "(预期: AAAAACCCCC CCCCCAAAAA)" << endl;
        
        cout << "方法4结果: ";
        for (const string& seq : result4) cout << seq << " ";
        cout << "(预期: AAAAACCCCC CCCCCAAAAA)" << endl;
        
        // 测试用例2：边界情况（无重复）
        string s2 = "AAAAAAAAAA";
        vector<string> result5 = solution.findRepeatedDnaSequences2(s2);
        cout << "测试用例2 - 输入: " << s2 << endl;
        cout << "方法2结果: ";
        for (const string& seq : result5) cout << seq << " ";
        cout << "(预期: 空)" << endl;
        
        // 复杂度分析
        cout << "\n=== 复杂度分析 ===" << endl;
        cout << "方法1 - 哈希表法:" << endl;
        cout << "  时间复杂度: O(n)" << endl;
        cout << "  空间复杂度: O(n)" << endl;
        
        cout << "方法2 - 位运算优化:" << endl;
        cout << "  时间复杂度: O(n)" << endl;
        cout << "  空间复杂度: O(n)" << endl;
        
        cout << "方法3 - 滑动窗口+位运算:" << endl;
        cout << "  时间复杂度: O(n)" << endl;
        cout << "  空间复杂度: O(n)" << endl;
        
        cout << "方法4 - Rabin-Karp算法:" << endl;
        cout << "  时间复杂度: O(n)" << endl;
        cout << "  空间复杂度: O(n)" << endl;
        
        // C++特性考量
        cout << "\n=== C++特性考量 ===" << endl;
        cout << "1. 标准库：使用unordered_map和unordered_set" << endl;
        cout << "2. 字符串操作：使用substr获取子串" << endl;
        cout << "3. 内存管理：自动管理容器内存" << endl;
        cout << "4. 性能优化：避免不必要的字符串拷贝" << endl;
        
        // 算法技巧总结
        cout << "\n=== 算法技巧总结 ===" << endl;
        cout << "1. 位编码：使用2位表示一个字符，节省空间" << endl;
        cout << "2. 滑动窗口：高效处理固定长度子串" << endl;
        cout << "3. 哈希优化：使用整数编码替代字符串比较" << endl;
        cout << "4. 滚动哈希：Rabin-Karp算法处理字符串匹配" << endl;
    }
};

int main() {
    Code32_RepeatedDNASequences::test();
    return 0;
}
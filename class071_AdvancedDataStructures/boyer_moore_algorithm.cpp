/**
 * BM (Boyer-Moore) 字符串匹配算法实现
 * 包含坏字符规则和好后缀规则
 * 时间复杂度：
 *   - 最好情况: O(n/m)，其中n是文本长度，m是模式长度
 *   - 最坏情况: O(n*m)
 *   - 平均情况: O(n)
 * 空间复杂度：O(k)，其中k是字符集大小
 */

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <stdexcept>
using namespace std;

class BoyerMooreAlgorithm {
private:
    static const int ALPHABET_SIZE = 256; // ASCII字符集大小

    /**
     * 构建坏字符规则表
     * @param pattern 模式串
     * @return 坏字符表，badChar[c]表示字符c在模式串中最右边出现的位置
     */
    static vector<int> buildBadCharTable(const string& pattern) {
        int m = pattern.length();
        vector<int> badChar(ALPHABET_SIZE, -1);
        
        // 记录每个字符最右边出现的位置
        for (int i = 0; i < m; i++) {
            badChar[static_cast<unsigned char>(pattern[i])] = i;
        }
        
        return badChar;
    }

    /**
     * 计算后缀数组：suffix[i]表示模式串从位置i开始的后缀与模式串本身的最长公共后缀长度
     */
    static vector<int> computeSuffixArray(const string& pattern) {
        int m = pattern.length();
        vector<int> suffix(m, 0);
        
        // 最后一个位置的后缀就是整个模式串，长度为m
        suffix[m - 1] = m;
        
        // 从倒数第二个位置开始向前计算
        for (int i = m - 2; i >= 0; i--) {
            int j = 0;  // 从模式串开头开始比较
            int k = i;  // 从位置i开始比较
            
            // 比较pattern[i:]和pattern[0:]
            while (j < m && k < m && pattern[k] == pattern[j]) {
                j++;
                k++;
            }
            
            suffix[i] = j;
        }
        
        return suffix;
    }

    // 已删除computePrefixArray函数，因为我们使用了更简单的实现方式

    /**
     * 构建好后缀规则表
     * @param pattern 模式串
     * @return 好后缀表，goodSuffix[j]表示当j位置出现不匹配时的移动距离
     */
    static vector<int> buildGoodSuffixTable(const string& pattern) {
        int m = pattern.length();
        // 初始化好后缀表
        vector<int> goodSuffix(m, m);
        
        // 计算后缀数组
        vector<int> suffix = computeSuffixArray(pattern);
        
        // case 1: 模式串的某一个后缀匹配了模式串的前缀
        for (int i = m - 1; i >= 0; i--) {
            // 如果从位置i开始的后缀等于整个模式串的前缀
            if (suffix[i] == m - i) {
                // 对于所有可能的位置，设置移动距离
                for (int j = 0; j < m - 1 - i; j++) {
                    if (goodSuffix[j] == m) {
                        goodSuffix[j] = m - 1 - i;
                    }
                }
            }
        }
        
        // case 2: 模式串的某一个子串等于以j为边界的后缀
        for (int i = 0; i <= m - 2; i++) {
            // 当在位置i发生不匹配时，应该移动的距离
            if (suffix[i] > 0) {
                goodSuffix[m - 1 - suffix[i]] = m - 1 - i;
            }
        }
        
        return goodSuffix;
    }

public:
    /**
     * BM字符串匹配算法
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     * @throws invalid_argument 如果输入参数为nullptr
     */
    static int search(const string& text, const string& pattern) {
        // 边界条件检查
        if (pattern.empty()) {
            return 0;  // 空模式串匹配任何位置的开始
        }
        if (text.empty()) {
            return -1;  // 空文本串不可能匹配非空模式串
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (n < m) {
            return -1;  // 文本串比模式串短，不可能匹配
        }
        
        // 构建坏字符规则表
        vector<int> badChar = buildBadCharTable(pattern);
        
        // 构建好后缀规则表
        vector<int> goodSuffix = buildGoodSuffixTable(pattern);
        
        // 开始匹配
        int i = 0; // 文本串中的位置
        while (i <= n - m) {
            int j = m - 1; // 从模式串的最后一个字符开始匹配
            
            // 从右向左匹配
            while (j >= 0 && pattern[j] == text[i + j]) {
                j--;
            }
            
            // 找到完全匹配
            if (j < 0) {
                return i;
            }
            
            // 计算坏字符规则的移动距离
            int badCharShift = max(1, j - badChar[static_cast<unsigned char>(text[i + j])]);
            
            // 计算好后缀规则的移动距离
            int goodSuffixShift = goodSuffix[j];
            
            // 取两个规则中的最大移动距离
            i += max(badCharShift, goodSuffixShift);
        }
        
        return -1; // 未找到匹配
    }

    /**
     * 查找模式串在文本串中所有出现的位置
     * @param text 文本串
     * @param pattern 模式串
     * @return 包含所有匹配位置的vector
     */
    static vector<int> searchAll(const string& text, const string& pattern) {
        vector<int> matches;
        
        // 边界条件检查
        if (pattern.empty()) {
            // 空模式串匹配每个位置的开始
            for (int i = 0; i <= static_cast<int>(text.length()); i++) {
                matches.push_back(i);
            }
            return matches;
        }
        
        if (text.empty()) {
            return matches;  // 空文本串不可能匹配非空模式串
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (n < m) {
            return matches;  // 无匹配
        }
        
        // 构建坏字符规则表和好后缀规则表
        vector<int> badChar = buildBadCharTable(pattern);
        vector<int> goodSuffix = buildGoodSuffixTable(pattern);
        
        int i = 0; // 文本串中的位置
        while (i <= n - m) {
            int j = m - 1; // 从模式串的最后一个字符开始匹配
            
            // 从右向左匹配
            while (j >= 0 && pattern[j] == text[i + j]) {
                j--;
            }
            
            // 找到完全匹配
            if (j < 0) {
                matches.push_back(i);
                // 移动一个位置继续查找
                i++;
            } else {
                // 计算坏字符规则的移动距离
                int badCharShift = max(1, j - badChar[static_cast<unsigned char>(text[i + j])]);
                
                // 计算好后缀规则的移动距离
                int goodSuffixShift = goodSuffix[j];
                
                // 取两个规则中的最大移动距离
                i += max(badCharShift, goodSuffixShift);
            }
        }
        
        return matches;
    }
};

int main() {
    // 测试用例1：基本匹配
    string text1 = "hello world";
    string pattern1 = "world";
    cout << "测试1 - 查找'world'在'hello world'中的位置: " << BoyerMooreAlgorithm::search(text1, pattern1) << endl;  // 应该是6
    
    // 测试用例2：多次匹配
    string text2 = "abababa";
    string pattern2 = "aba";
    vector<int> results2 = BoyerMooreAlgorithm::searchAll(text2, pattern2);
    cout << "测试2 - 查找所有'aba'在'abababa'中的位置: ";
    for (int pos : results2) {
        cout << pos << " ";  // 应该是0 2 4
    }
    cout << endl;
    
    // 测试用例3：无匹配
    string text3 = "hello";
    string pattern3 = "world";
    cout << "测试3 - 查找'world'在'hello'中的位置: " << BoyerMooreAlgorithm::search(text3, pattern3) << endl;  // 应该是-1
    
    // 测试用例4：边界情况
    string text4 = "test";
    string pattern4 = "";
    vector<int> results4 = BoyerMooreAlgorithm::searchAll(text4, pattern4);
    cout << "测试4 - 查找空串在'test'中的位置: ";
    for (int pos : results4) {
        cout << pos << " ";  // 应该是0 1 2 3 4
    }
    cout << endl;
    
    // 测试用例5：BM算法优势场景
    string text5 = "GCATCGCAGAGAGTATACAGTACG";
    string pattern5 = "GCAGAGAG";
    cout << "测试5 - 查找模式串在文本串中的位置: " << BoyerMooreAlgorithm::search(text5, pattern5) << endl;  // 应该是5
    
    // 测试用例6：好后缀规则测试
    string text6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    string pattern6 = "XYZ";
    cout << "测试6 - 查找'XYZ'在字母表中的位置: " << BoyerMooreAlgorithm::search(text6, pattern6) << endl;  // 应该是23
    
    return 0;
}
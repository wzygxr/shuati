#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
using namespace std;

/**
 * Trie树扩展题目合集 - 简化版本
 * 避免使用可能引起编译问题的现代C++特性
 */

class ExtendedTrieProblemsSimple {
public:
    /*
     * 题目1: LeetCode 745. 前缀和后缀搜索
     */
    class WordFilter {
    private:
        struct TrieNode {
            TrieNode* children[27]; // 26个字母 + 1个分隔符
            int weight;
            
            TrieNode() : weight(0) {
                for (int i = 0; i < 27; i++) children[i] = nullptr;
            }
        };
        
        TrieNode* root;
        
    public:
        WordFilter(vector<string>& words) {
            root = new TrieNode();
            for (int weight = 0; weight < words.size(); weight++) {
                string word = words[weight];
                for (int i = 0; i <= word.length(); i++) {
                    string key = word.substr(i) + "{" + word;
                    TrieNode* node = root;
                    for (char c : key) {
                        int index = c - 'a';
                        if (c == '{') index = 26;
                        if (!node->children[index]) {
                            node->children[index] = new TrieNode();
                        }
                        node = node->children[index];
                        node->weight = weight;
                    }
                }
            }
        }
        
        int f(string prefix, string suffix) {
            string key = suffix + "{" + prefix;
            TrieNode* node = root;
            for (char c : key) {
                int index = c - 'a';
                if (c == '{') index = 26;
                if (!node->children[index]) return -1;
                node = node->children[index];
            }
            return node->weight;
        }
    };

    /*
     * 题目2: 最长公共前缀
     */
    class LongestCommonPrefix {
    public:
        string longestCommonPrefix(vector<string>& strs) {
            if (strs.empty()) return "";
            if (strs.size() == 1) return strs[0];
            
            string prefix = "";
            // 直接比较法，避免Trie树的内存问题
            for (int i = 0; i < strs[0].length(); i++) {
                char c = strs[0][i];
                for (int j = 1; j < strs.size(); j++) {
                    if (i >= strs[j].length() || strs[j][i] != c) {
                        return prefix;
                    }
                }
                prefix += c;
            }
            return prefix;
        }
    };

    /*
     * 题目3: 电话号码列表检查
     */
    class PhoneListChecker {
    private:
        struct TrieNode {
            TrieNode* children[10];
            bool isEnd;
            
            TrieNode() : isEnd(false) {
                for (int i = 0; i < 10; i++) children[i] = nullptr;
            }
        };
        
    public:
        bool hasConsistentList(vector<string>& phoneNumbers) {
            // 按长度排序
            sort(phoneNumbers.begin(), phoneNumbers.end(), 
                 [](const string& a, const string& b) { return a.length() < b.length(); });
            
            TrieNode* root = new TrieNode();
            
            for (const string& phone : phoneNumbers) {
                TrieNode* node = root;
                bool createdNew = false;
                
                for (int i = 0; i < phone.length(); i++) {
                    int digit = phone[i] - '0';
                    
                    if (!node->children[digit]) {
                        node->children[digit] = new TrieNode();
                        createdNew = true;
                    }
                    
                    node = node->children[digit];
                    
                    if (node->isEnd) {
                        return false;
                    }
                }
                
                if (!createdNew) return false;
                node->isEnd = true;
            }
            
            return true;
        }
    };

    /*
     * 题目4: 统计前缀数量
     */
    class StatisticalProblem {
    private:
        struct TrieNode {
            TrieNode* children[26];
            int count;
            
            TrieNode() : count(0) {
                for (int i = 0; i < 26; i++) children[i] = nullptr;
            }
        };
        
        TrieNode* root;
        
        void insert(const string& word) {
            TrieNode* node = root;
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = new TrieNode();
                }
                node = node->children[index];
                node->count++;
            }
        }
        
    public:
        StatisticalProblem(vector<string>& words) {
            root = new TrieNode();
            for (const string& word : words) {
                insert(word);
            }
        }
        
        int prefixCount(const string& prefix) {
            TrieNode* node = root;
            for (char c : prefix) {
                int index = c - 'a';
                if (!node->children[index]) return 0;
                node = node->children[index];
            }
            return node->count;
        }
    };

    /*
     * 题目5: 把数组排成最小的数
     */
    class MinNumber {
    public:
        string minNumber(vector<int>& nums) {
            vector<string> strNums;
            for (int num : nums) {
                strNums.push_back(to_string(num));
            }
            
            sort(strNums.begin(), strNums.end(), [](const string& a, const string& b) {
                return a + b < b + a;
            });
            
            string result;
            for (const string& str : strNums) {
                result += str;
            }
            return result;
        }
    };

    // 测试方法
    static void test() {
        ExtendedTrieProblemsSimple solution;
        
        cout << "=== 测试最长公共前缀 ===" << endl;
        vector<string> words1 = {"flower", "flow", "flight"};
        auto lcp = LongestCommonPrefix();
        cout << "最长公共前缀: " << lcp.longestCommonPrefix(words1) << endl;
        
        cout << "\n=== 测试电话号码列表 ===" << endl;
        vector<string> phones1 = {"911", "97625999", "91125426"};
        vector<string> phones2 = {"113", "12340", "123440", "12345", "98346"};
        auto plc = PhoneListChecker();
        cout << "列表1是否一致: " << (plc.hasConsistentList(phones1) ? "是" : "否") << endl;
        cout << "列表2是否一致: " << (plc.hasConsistentList(phones2) ? "是" : "否") << endl;
        
        cout << "\n=== 测试统计前缀数量 ===" << endl;
        vector<string> words2 = {"banana", "band", "bee", "absolute", "acm"};
        auto stat = StatisticalProblem(words2);
        cout << "前缀'ba'的数量: " << stat.prefixCount("ba") << endl;
        cout << "前缀'b'的数量: " << stat.prefixCount("b") << endl;
        
        cout << "\n=== 测试最小数字 ===" << endl;
        vector<int> nums = {3, 30, 34, 5, 9};
        auto mn = MinNumber();
        cout << "最小数字: " << mn.minNumber(nums) << endl;
    }
};

int main() {
    ExtendedTrieProblemsSimple::test();
    return 0;
}
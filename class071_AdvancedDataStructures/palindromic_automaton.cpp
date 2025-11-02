#include <iostream>
#include <vector>
#include <map>
#include <string>
#include <unordered_set>
#include <algorithm>

using namespace std;

/**
 * 回文自动机节点类
 */
struct Node {
    map<char, int> next;  // 转移函数
    int len;              // 回文子串的长度
    int link;             // 后缀链接
    int count;            // 该回文子串在当前字符串中的出现次数
    int occurCount;       // 该回文子串在原字符串中的总出现次数
    
    Node(int l) : len(l), link(-1), count(0), occurCount(0) {}
};

/**
 * 回文自动机实现
 */
class PalindromicAutomaton {
private:
    vector<Node> tree;    // 所有节点
    string text;          // 原始文本
    int size;             // 节点数量
    int last;             // 当前最长回文后缀的节点索引
    
    /**
     * 找到当前节点的后缀链接中，其对应的回文子串前添加字符c后仍是回文的节点
     */
    int getFail(int p, int pos, char c) {
        while (true) {
            int len = tree[p].len;
            if (pos - len - 1 >= 0 && text[pos - len - 1] == c) {
                break;
            }
            p = tree[p].link;
        }
        return p;
    }
    
public:
    /**
     * 构造函数，初始化回文自动机
     */
    PalindromicAutomaton() {
        tree.emplace_back(-1);  // root1: 长度为-1的虚拟回文
        tree.emplace_back(0);   // root2: 长度为0的空回文
        size = 2;
        last = 1;
        text = "";
    }
    
    /**
     * 构造函数，从字符串构建回文自动机
     */
    PalindromicAutomaton(const string& s) : PalindromicAutomaton() {
        text = s;
        for (char c : s) {
            extend(c);
        }
        calculateOccurCount();
    }
    
    /**
     * 扩展回文自动机，添加一个字符
     */
    void extend(char c) {
        text += c;
        int pos = text.length() - 1;
        
        int p = getFail(last, pos, c);
        
        if (tree[p].next.find(c) == tree[p].next.end()) {
            int newNode = size++;
            tree.emplace_back(tree[p].len + 2);
            
            if (tree[newNode].len == 1) {
                tree[newNode].link = 1;
            } else {
                int failNode = getFail(tree[p].link, pos, c);
                tree[newNode].link = tree[failNode].next[c];
            }
            
            tree[p].next[c] = newNode;
        }
        
        last = tree[p].next[c];
        tree[last].count++;
    }
    
    /**
     * 计算每个回文子串在原字符串中的总出现次数
     */
    void calculateOccurCount() {
        vector<int> order;
        for (int i = 2; i < size; i++) {
            order.push_back(i);
        }
        
        sort(order.begin(), order.end(), [&](int a, int b) {
            return tree[a].len > tree[b].len;
        });
        
        for (int node : order) {
            tree[tree[node].link].occurCount += tree[node].count;
        }
        
        for (int i = 2; i < size; i++) {
            tree[i].occurCount += tree[i].count;
        }
    }
    
    /**
     * 获取不同回文子串的数量
     */
    int getDistinctPalindromeCount() {
        return size - 2;
    }
    
    /**
     * 获取最长回文子串
     */
    string getLongestPalindrome() {
        int maxLen = 0;
        int maxNode = -1;
        
        for (int i = 2; i < size; i++) {
            if (tree[i].len > maxLen) {
                maxLen = tree[i].len;
                maxNode = i;
            }
        }
        
        if (maxNode != -1) {
            return reconstructPalindrome(maxNode);
        }
        return "";
    }
    
    /**
     * 重建回文子串
     */
    string reconstructPalindrome(int node) {
        if (node == 0 || node == 1) {
            return "";
        }
        
        string result;
        int current = node;
        int parent = tree[current].link;
        
        char firstChar = 0;
        for (auto& entry : tree[parent].next) {
            if (entry.second == current) {
                firstChar = entry.first;
                break;
            }
        }
        
        if (tree[current].len == 1) {
            return string(1, firstChar);
        }
        
        string parentPalindrome = reconstructPalindrome(parent);
        result = string(1, firstChar) + parentPalindrome + string(1, firstChar);
        return result;
    }
    
    /**
     * 获取回文自动机的节点数量
     */
    int getNodeCount() {
        return size;
    }
    
    /**
     * 获取所有回文子串的总出现次数
     */
    long getTotalPalindromeOccurrences() {
        long total = 0;
        for (int i = 2; i < size; i++) {
            total += tree[i].occurCount;
        }
        return total;
    }
    
    /**
     * 获取文本
     */
    string getText() {
        return text;
    }
};

/**
 * 最长回文子串求解器
 */
class LPSSolver {
private:
    PalindromicAutomaton pam;
    
public:
    LPSSolver(const string& text) : pam(text) {}
    
    string getLongestPalindrome() {
        return pam.getLongestPalindrome();
    }
    
    int getLongestPalindromeLength() {
        string longest = pam.getLongestPalindrome();
        return longest.length();
    }
};

/**
 * 回文数量求解器
 */
class NumberOfPalindromes {
private:
    PalindromicAutomaton pam;
    
public:
    NumberOfPalindromes(const string& text) : pam(text) {}
    
    int getDistinctCount() {
        return pam.getDistinctPalindromeCount();
    }
    
    long getTotalOccurrences() {
        return pam.getTotalPalindromeOccurrences();
    }
};

/**
 * 测试函数
 */
int main() {
    cout << "=== 测试回文自动机 ===" << endl;
    
    // 测试最长回文子串
    cout << "\n=== 测试最长回文子串 ===" << endl;
    LPSSolver lps("abacabad");
    cout << "文本: abacabad" << endl;
    cout << "最长回文子串: " << lps.getLongestPalindrome() << endl;
    cout << "最长回文子串长度: " << lps.getLongestPalindromeLength() << endl;
    
    // 测试回文数量
    cout << "\n=== 测试回文数量 ===" << endl;
    NumberOfPalindromes nop("aabaa");
    cout << "文本: aabaa" << endl;
    cout << "不同回文子串数量: " << nop.getDistinctCount() << endl;
    cout << "回文子串总出现次数: " << nop.getTotalOccurrences() << endl;
    
    // 测试更多字符串
    cout << "\n=== 测试更多字符串 ===" << endl;
    vector<string> testStrings = {"racecar", "abba", "abcba", "a", ""};
    
    for (const string& s : testStrings) {
        PalindromicAutomaton pam(s);
        cout << "文本: '" << s << "'" << endl;
        cout << "  不同回文数: " << pam.getDistinctPalindromeCount() << endl;
        cout << "  最长回文: " << pam.getLongestPalindrome() << endl;
        cout << "  总出现次数: " << pam.getTotalPalindromeOccurrences() << endl;
        cout << endl;
    }
    
    return 0;
}
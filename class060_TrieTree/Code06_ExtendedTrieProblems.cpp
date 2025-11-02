#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
#include <memory>
#include <set>
#include <functional>
using namespace std;

/**
 * Trie树扩展题目合集 - 从各大算法平台收集的Trie树相关题目
 * 
 * 本文件包含从LeetCode、POJ、HDU、牛客网、洛谷等各大算法平台收集的Trie树相关题目
 * 每个题目都包含详细的解题思路、时间复杂度分析、空间复杂度分析和工程化考量
 */

class ExtendedTrieProblems {
public:
    /*
     * 题目1: LeetCode 745. 前缀和后缀搜索
     * 题目来源：LeetCode
     * 题目链接：https://leetcode.cn/problems/prefix-and-suffix-search/
     * 
     * 题目描述：
     * 设计一个包含一些单词的词典，支持前缀和后缀搜索。
     * WordFilter(string[] words) 使用给定的单词初始化对象。
     * int f(string prefix, string suffix) 返回词典中具有前缀 prefix 和后缀 suffix 的单词的下标。
     * 如果存在多个满足条件的单词，返回下标最大的单词。如果没有满足条件的单词，返回 -1。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词，每个节点记录经过该节点的最大下标
     * 2. 对于每个单词，将其所有后缀+分隔符+单词本身插入Trie树
     * 3. 查询时，将后缀+分隔符+前缀作为查询字符串
     * 
     * 时间复杂度分析：
     * 1. 构造函数：O(N*L^2)，其中N是单词数量，L是单词最大长度
     * 2. f函数：O(P+S)，其中P是前缀长度，S是后缀长度
     * 空间复杂度分析：
     * 1. O(N*L^2)，需要存储所有单词的所有后缀组合
     * 是否为最优解：是，这是解决此类问题的经典方法
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用更紧凑的数据结构
     * 2. 性能优化：对于大量查询，可以考虑缓存结果
     * 3. 异常处理：处理空输入和边界情况
     */
    class WordFilter {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            int weight; // 存储经过该节点的最大下标
            
            TrieNode() : children(27), weight(0) {}
        };
        
        unique_ptr<TrieNode> root;
        
    public:
        WordFilter(vector<string>& words) {
            root = make_unique<TrieNode>();
            for (int weight = 0; weight < words.size(); weight++) {
                string word = words[weight];
                // 对于每个单词，插入所有后缀+分隔符+单词的组合
                for (int i = 0; i <= word.length(); i++) {
                    string key = word.substr(i) + "{" + word;
                    TrieNode* node = root.get();
                    for (char c : key) {
                        int index = c - 'a';
                        if (c == '{') index = 26;
                        if (!node->children[index]) {
                            node->children[index] = make_unique<TrieNode>();
                        }
                        node = node->children[index].get();
                        node->weight = weight; // 更新最大下标
                    }
                }
            }
        }
        
        int f(string prefix, string suffix) {
            string key = suffix + "{" + prefix;
            TrieNode* node = root.get();
            for (char c : key) {
                int index = c - 'a';
                if (c == '{') index = 26;
                if (!node->children[index]) {
                    return -1;
                }
                node = node->children[index].get();
            }
            return node->weight;
        }
    };

    /*
     * 题目2: LeetCode 336. 回文对
     * 题目来源：LeetCode
     * 题目链接：https://leetcode.cn/problems/palindrome-pairs/
     * 
     * 题目描述：
     * 给定一组互不相同的单词，找出所有不同的索引对 (i, j)，使得列表中的两个单词，words[i] + words[j] ，可拼接成回文串。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词的逆序
     * 2. 对于每个单词，在Trie树中查找能与之形成回文串的单词
     * 3. 分情况讨论：当前单词是较长部分、当前单词是较短部分
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(N*L)，其中N是单词数量，L是单词平均长度
     * 2. 查询过程：O(N*L^2)，需要检查每个单词的所有前缀和后缀
     * 空间复杂度分析：
     * 1. O(N*L)，Trie树存储空间
     * 是否为最优解：是，Trie树是解决此类问题的高效方法
     * 
     * 工程化考量：
     * 1. 性能优化：可以使用哈希表预计算回文信息
     * 2. 内存优化：对于长单词，可以优化存储方式
     * 3. 去重处理：确保索引对不重复
     */
    class PalindromePairs {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            int index; // 单词在数组中的下标
            vector<int> list; // 存储经过该节点且剩余部分是回文的单词下标
            
            TrieNode() : children(26), index(-1) {}
        };
        
        bool isPalindrome(const string& word, int i, int j) {
            while (i < j) {
                if (word[i++] != word[j--]) return false;
            }
            return true;
        }
        
        void addWord(unique_ptr<TrieNode>& root, const string& word, int index) {
            // 逆序插入单词
            TrieNode* node = root.get();
            for (int i = word.length() - 1; i >= 0; i--) {
                int j = word[i] - 'a';
                if (!node->children[j]) {
                    node->children[j] = make_unique<TrieNode>();
                }
                // 如果单词的前缀是回文，记录当前下标
                if (isPalindrome(word, 0, i)) {
                    node->list.push_back(index);
                }
                node = node->children[j].get();
            }
            node->list.push_back(index);
            node->index = index;
        }
        
        void search(const vector<string>& words, int i, TrieNode* root, vector<vector<int>>& res) {
            // 正序匹配单词
            for (int j = 0; j < words[i].length(); j++) {
                if (root->index >= 0 && root->index != i && isPalindrome(words[i], j, words[i].length() - 1)) {
                    res.push_back({i, root->index});
                }
                root = root->children[words[i][j] - 'a'].get();
                if (!root) return;
            }
            
            // 处理Trie树中剩余的匹配
            for (int j : root->list) {
                if (i == j) continue;
                res.push_back({i, j});
            }
        }
        
    public:
        vector<vector<int>> palindromePairs(vector<string>& words) {
            vector<vector<int>> res;
            auto root = make_unique<TrieNode>();
            
            // 构建Trie树，存储单词的逆序
            for (int i = 0; i < words.size(); i++) {
                addWord(root, words[i], i);
            }
            
            // 对于每个单词，在Trie树中查找匹配
            for (int i = 0; i < words.size(); i++) {
                search(words, i, root.get(), res);
            }
            
            return res;
        }
    };

    /*
     * 题目3: POJ 2001 Shortest Prefixes
     * 题目来源：POJ
     * 题目链接：http://poj.org/problem?id=2001
     * 
     * 题目描述：
     * 给定一组单词，为每个单词找到最短的唯一前缀。也就是说，找到每个单词的最短前缀，使得这个前缀不是其他任何单词的前缀。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词
     * 2. 记录每个节点被经过的次数
     * 3. 对于每个单词，找到第一个出现次数为1的节点，该节点之前的前缀就是最短唯一前缀
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(∑len(s))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树是解决此类问题的最优方法
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用更紧凑的节点结构
     * 2. 性能优化：预处理可以进一步提高查询效率
     * 3. 异常处理：处理空单词和重复单词的情况
     */
    class ShortestPrefixes {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            int count; // 经过该节点的单词数量
            
            TrieNode() : children(26), count(0) {}
        };
        
    public:
        unordered_map<string, string> findShortestPrefixes(vector<string>& words) {
            unordered_map<string, string> result;
            auto root = make_unique<TrieNode>();
            
            // 构建Trie树
            for (const string& word : words) {
                TrieNode* node = root.get();
                for (char c : word) {
                    int index = c - 'a';
                    if (!node->children[index]) {
                        node->children[index] = make_unique<TrieNode>();
                    }
                    node = node->children[index].get();
                    node->count++;
                }
            }
            
            // 为每个单词寻找最短唯一前缀
            for (const string& word : words) {
                TrieNode* node = root.get();
                string prefix;
                for (int i = 0; i < word.length(); i++) {
                    char c = word[i];
                    int index = c - 'a';
                    prefix += c;
                    node = node->children[index].get();
                    // 如果当前节点只被当前单词经过，则找到最短唯一前缀
                    if (node->count == 1) {
                        break;
                    }
                }
                result[word] = prefix;
            }
            
            return result;
        }
    };

    /*
     * 题目4: HDU 1247 Hat's Words
     * 题目来源：HDU
     * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1247
     * 
     * 题目描述：
     * 一个"hat's word"是一个单词，可以恰好由字典中其他两个单词连接而成。
     * 给你一个字典，找出所有的hat's words。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词
     * 2. 对于每个单词，检查它是否能被拆分成两个都在字典中的单词
     * 3. 使用Trie树快速检查每个前缀和后缀是否在字典中
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 检查过程：O(N*L^2)，其中N是单词数量，L是单词最大长度
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树提供高效的字符串查找
     * 
     * 工程化考量：
     * 1. 性能优化：可以预处理单词长度信息
     * 2. 内存优化：使用合适的Trie树实现
     * 3. 去重处理：确保结果不重复
     */
    class HatsWords {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            bool isEnd;
            
            TrieNode() : children(26), isEnd(false) {}
        };
        
        void insert(unique_ptr<TrieNode>& root, const string& word) {
            TrieNode* node = root.get();
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = make_unique<TrieNode>();
                }
                node = node->children[index].get();
            }
            node->isEnd = true;
        }
        
        bool search(TrieNode* root, const string& word) {
            TrieNode* node = root;
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    return false;
                }
                node = node->children[index].get();
            }
            return node->isEnd;
        }
        
        bool isHatsWord(TrieNode* root, const string& word) {
            // 尝试所有可能的分割点
            for (int i = 1; i < word.length(); i++) {
                string prefix = word.substr(0, i);
                string suffix = word.substr(i);
                if (search(root, prefix) && search(root, suffix)) {
                    return true;
                }
            }
            return false;
        }
        
    public:
        vector<string> findHatsWords(vector<string>& words) {
            vector<string> result;
            auto root = make_unique<TrieNode>();
            
            // 构建Trie树
            for (const string& word : words) {
                insert(root, word);
            }
            
            // 检查每个单词是否是hat's word
            for (const string& word : words) {
                if (isHatsWord(root.get(), word)) {
                    result.push_back(word);
                }
            }
            
            return result;
        }
    };

    /*
     * 题目5: 牛客网 最长公共前缀
     * 题目来源：牛客网
     * 题目链接：https://www.nowcoder.com/practice/28eb3175488f4434a4a6207f6f484f47
     * 
     * 题目描述：
     * 编写一个函数来查找字符串数组中的最长公共前缀。
     * 如果不存在公共前缀，返回空字符串 ""。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有字符串
     * 2. 从根节点开始，找到第一个有多个子节点的节点
     * 3. 该节点之前的前缀就是最长公共前缀
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查找过程：O(min(len(s)))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树提供直观的解决方案
     * 
     * 工程化考量：
     * 1. 性能优化：对于少量字符串，直接比较可能更快
     * 2. 内存优化：可以使用更紧凑的Trie树实现
     * 3. 异常处理：处理空数组和空字符串的情况
     */
    class LongestCommonPrefix {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            int count; // 经过该节点的字符串数量
            
            TrieNode() : children(26), count(0) {}
        };
        
    public:
        string longestCommonPrefix(vector<string>& strs) {
            if (strs.empty()) return "";
            if (strs.size() == 1) return strs[0];
            
            auto root = make_unique<TrieNode>();
            
            // 构建Trie树
            for (const string& str : strs) {
                if (str.empty()) return "";
                TrieNode* node = root.get();
                for (char c : str) {
                    int index = c - 'a';
                    if (!node->children[index]) {
                        node->children[index] = make_unique<TrieNode>();
                    }
                    node = node->children[index].get();
                    node->count++;
                }
            }
            
            // 查找最长公共前缀
            string prefix;
            TrieNode* node = root.get();
            while (true) {
                // 检查当前节点的子节点
                int childCount = 0;
                TrieNode* nextNode = nullptr;
                char nextChar = '\0';
                
                for (int i = 0; i < 26; i++) {
                    if (node->children[i] && node->children[i]->count == strs.size()) {
                        childCount++;
                        nextNode = node->children[i].get();
                        nextChar = 'a' + i;
                    }
                }
                
                // 如果子节点数量不为1，结束查找
                if (childCount != 1) {
                    break;
                }
                prefix += nextChar;
                node = nextNode;
            }
            
            return prefix;
        }
    };

    /*
     * 题目6: 洛谷 P2580 于是他错误的点名开始了
     * 题目来源：洛谷
     * 题目链接：https://www.luogu.com.cn/problem/P2580
     * 
     * 题目描述：
     * 老师点名，第一次点到的输出"OK"，重复点到的输出"REPEAT"，点到不存在的名字输出"WRONG"。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有学生姓名
     * 2. 每个节点记录点名状态
     * 3. 根据点名状态输出相应结果
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(∑len(s))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树提供高效的姓名查找
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用哈希表作为替代方案
     * 2. 性能优化：Trie树在大量相似姓名时更高效
     * 3. 异常处理：处理非法字符和超长姓名
     */
    class RollCallSystem {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            int status; // 0: 未点名, 1: 已点名, 2: 不存在
            
            TrieNode() : children(26), status(0) {}
        };
        
        unique_ptr<TrieNode> root;
        
        void insert(const string& name) {
            TrieNode* node = root.get();
            for (char c : name) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = make_unique<TrieNode>();
                }
                node = node->children[index].get();
            }
            node->status = 0; // 初始状态为未点名
        }
        
    public:
        RollCallSystem(vector<string>& names) {
            root = make_unique<TrieNode>();
            // 构建Trie树，插入所有学生姓名
            for (const string& name : names) {
                insert(name);
            }
        }
        
        string call(const string& name) {
            TrieNode* node = root.get();
            for (char c : name) {
                int index = c - 'a';
                if (!node->children[index]) {
                    return "WRONG"; // 姓名不存在
                }
                node = node->children[index].get();
            }
            
            if (node->status == 0) {
                node->status = 1; // 标记为已点名
                return "OK";
            } else if (node->status == 1) {
                return "REPEAT";
            } else {
                return "WRONG";
            }
        }
    };

    /*
     * 题目7: CodeChef DICT - Dictionary
     * 题目来源：CodeChef
     * 题目链接：https://www.codechef.com/problems/DICT
     * 
     * 题目描述：
     * 给定一个字典和一组查询，对于每个查询，输出字典中所有以该查询字符串为前缀的单词。
     * 如果存在多个单词，按字典序输出。
     * 
     * 解题思路：
     * 1. 使用Trie树存储字典中的所有单词
     * 2. 每个节点维护以该节点为前缀的所有单词
     * 3. 查询时找到前缀对应的节点，输出该节点存储的所有单词
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(P + K)，其中P是前缀长度，K是输出单词数量
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是，Trie树是解决前缀查询的高效方法
     * 
     * 工程化考量：
     * 1. 内存优化：可以使用更紧凑的存储方式
     * 2. 性能优化：预处理可以加速查询
     * 3. 排序处理：需要按字典序输出结果
     */
    class DictionarySearch {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            vector<string> words; // 存储以该节点为前缀的所有单词
            
            TrieNode() : children(26) {}
        };
        
        unique_ptr<TrieNode> root;
        
        void insert(const string& word) {
            TrieNode* node = root.get();
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = make_unique<TrieNode>();
                }
                node = node->children[index].get();
                node->words.push_back(word);
            }
        }
        
    public:
        DictionarySearch(vector<string>& dictionary) {
            root = make_unique<TrieNode>();
            // 构建Trie树
            for (const string& word : dictionary) {
                insert(word);
            }
        }
        
        vector<string> search(const string& prefix) {
            TrieNode* node = root.get();
            for (char c : prefix) {
                int index = c - 'a';
                if (!node->children[index]) {
                    return {}; // 前缀不存在
                }
                node = node->children[index].get();
            }
            // 返回该前缀对应的所有单词，按字典序排序
            sort(node->words.begin(), node->words.end());
            return node->words;
        }
    };

    /*
     * 题目8: SPOJ PHONELST - Phone List
     * 题目来源：SPOJ
     * 题目链接：https://www.spoj.com/problems/PHONELIST/
     * 
     * 题目描述：
     * 与POJ 3630相同，判断电话号码列表中是否存在前缀关系。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有电话号码
     * 2. 在插入过程中检查前缀关系
     * 3. 优化实现，提高效率
     * 
     * 时间复杂度分析：
     * 1. O(∑len(s))
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是
     */
    class SPOJPhoneList {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            bool isEnd;
            
            TrieNode() : children(10), isEnd(false) {}
        };
        
    public:
        bool hasConsistentList(vector<string>& phoneNumbers) {
            auto root = make_unique<TrieNode>();
            
            // 按长度排序，先插入短的
            sort(phoneNumbers.begin(), phoneNumbers.end(), 
                 [](const string& a, const string& b) { return a.length() < b.length(); });
            
            for (const string& phone : phoneNumbers) {
                TrieNode* node = root.get();
                bool createdNew = false;
                
                for (int i = 0; i < phone.length(); i++) {
                    int digit = phone[i] - '0';
                    
                    if (!node->children[digit]) {
                        node->children[digit] = make_unique<TrieNode>();
                        createdNew = true;
                    }
                    
                    node = node->children[digit].get();
                    
                    // 如果在插入过程中遇到已标记的结尾，说明存在前缀关系
                    if (node->isEnd) {
                        return false;
                    }
                }
                
                // 如果当前节点有子节点，说明当前号码是其他号码的前缀
                if (!createdNew) {
                    return false;
                }
                
                node->isEnd = true;
            }
            
            return true;
        }
    };

    /*
     * 题目9: 剑指Offer 45. 把数组排成最小的数
     * 题目来源：剑指Offer
     * 题目链接：https://leetcode.cn/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/
     * 
     * 题目描述：
     * 输入一个非负整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
     * 
     * 解题思路：
     * 1. 使用Trie树思想进行字符串排序
     * 2. 自定义比较器，比较a+b和b+a的大小
     * 3. 按特定顺序拼接字符串
     * 
     * 时间复杂度分析：
     * 1. O(NlogN)
     * 空间复杂度分析：
     * 1. O(N)
     * 是否为最优解：是
     */
    class MinNumber {
    public:
        string minNumber(vector<int>& nums) {
            // 将数字转换为字符串
            vector<string> strNums;
            for (int num : nums) {
                strNums.push_back(to_string(num));
            }
            
            // 自定义排序：比较a+b和b+a的大小
            sort(strNums.begin(), strNums.end(), [](const string& a, const string& b) {
                return a + b < b + a;
            });
            
            // 拼接结果
            string result;
            for (const string& str : strNums) {
                result += str;
            }
            
            return result;
        }
    };

    /*
     * 题目10: 杭电OJ 1251 统计难题
     * 题目来源：杭电OJ
     * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1251
     * 
     * 题目描述：
     * Ignatius最近遇到一个难题，老师交给他很多单词(只有小写字母组成，不会有重复的单词出现)，
     * 现在老师要他统计出以某个字符串为前缀的单词数量(单词本身也是自己的前缀)。
     * 
     * 解题思路：
     * 1. 使用Trie树存储所有单词
     * 2. 每个节点记录经过该节点的单词数量
     * 3. 查询时找到前缀对应的节点，返回该节点的计数
     * 
     * 时间复杂度分析：
     * 1. 构建Trie树：O(∑len(s))
     * 2. 查询过程：O(P)，其中P是前缀长度
     * 空间复杂度分析：
     * 1. O(∑len(s))
     * 是否为最优解：是
     */
    class StatisticalProblem {
    private:
        struct TrieNode {
            vector<unique_ptr<TrieNode>> children;
            int count; // 经过该节点的单词数量
            
            TrieNode() : children(26), count(0) {}
        };
        
        unique_ptr<TrieNode> root;
        
        void insert(const string& word) {
            TrieNode* node = root.get();
            for (char c : word) {
                int index = c - 'a';
                if (!node->children[index]) {
                    node->children[index] = make_unique<TrieNode>();
                }
                node = node->children[index].get();
                node->count++;
            }
        }
        
    public:
        StatisticalProblem(vector<string>& words) {
            root = make_unique<TrieNode>();
            for (const string& word : words) {
                insert(word);
            }
        }
        
        int prefixCount(const string& prefix) {
            TrieNode* node = root.get();
            for (char c : prefix) {
                int index = c - 'a';
                if (!node->children[index]) {
                    return 0;
                }
                node = node->children[index].get();
            }
            return node->count;
        }
    };

    // 测试方法
    static void test() {
        ExtendedTrieProblems solution;
        
        // 测试WordFilter
        cout << "=== 测试WordFilter ===" << endl;
        vector<string> words1 = {"apple", "application", "apply"};
        auto wf = WordFilter(words1);
        cout << "f(\"a\", \"e\"): " << wf.f("a", "e") << endl; // 应该返回2（apply的下标）
        
        // 测试PalindromePairs
        cout << "\n=== 测试PalindromePairs ===" << endl;
        vector<string> words2 = {"abcd", "dcba", "lls", "s", "sssll"};
        auto pp = PalindromePairs();
        auto pairs = pp.palindromePairs(words2);
        cout << "回文对数量: " << pairs.size() << endl;
        
        // 测试ShortestPrefixes
        cout << "\n=== 测试ShortestPrefixes ===" << endl;
        vector<string> words3 = {"z", "dog", "duck", "dove"};
        auto sp = ShortestPrefixes();
        auto prefixes = sp.findShortestPrefixes(words3);
        cout << "最短唯一前缀数量: " << prefixes.size() << endl;
        
        // 测试HatsWords
        cout << "\n=== 测试HatsWords ===" << endl;
        vector<string> words4 = {"a", "hat", "hats", "word", "words", "hatword"};
        auto hw = HatsWords();
        auto hatsWords = hw.findHatsWords(words4);
        cout << "Hat's words数量: " << hatsWords.size() << endl;
        
        // 测试LongestCommonPrefix
        cout << "\n=== 测试LongestCommonPrefix ===" << endl;
        vector<string> words5 = {"flower", "flow", "flight"};
        auto lcp = LongestCommonPrefix();
        auto commonPrefix = lcp.longestCommonPrefix(words5);
        cout << "最长公共前缀: " << commonPrefix << endl;
        
        // 测试RollCallSystem
        cout << "\n=== 测试RollCallSystem ===" << endl;
        vector<string> names = {"alice", "bob", "charlie"};
        auto rcs = RollCallSystem(names);
        cout << "点名alice: " << rcs.call("alice") << endl; // OK
        cout << "点名alice: " << rcs.call("alice") << endl; // REPEAT
        cout << "点名david: " << rcs.call("david") << endl; // WRONG
        
        // 测试DictionarySearch
        cout << "\n=== 测试DictionarySearch ===" << endl;
        vector<string> dictionary = {"apple", "application", "apply", "banana", "band"};
        auto ds = DictionarySearch(dictionary);
        auto results = ds.search("app");
        cout << "前缀'app'的单词数量: " << results.size() << endl;
        
        // 测试SPOJPhoneList
        cout << "\n=== 测试SPOJPhoneList ===" << endl;
        vector<string> phones1 = {"911", "97625999", "91125426"};
        vector<string> phones2 = {"113", "12340", "123440", "12345", "98346"};
        auto spl = SPOJPhoneList();
        cout << "电话号码列表1是否一致: " << spl.hasConsistentList(phones1) << endl; // false
        cout << "电话号码列表2是否一致: " << spl.hasConsistentList(phones2) << endl; // true
        
        // 测试MinNumber
        cout << "\n=== 测试MinNumber ===" << endl;
        vector<int> nums = {3, 30, 34, 5, 9};
        auto mn = MinNumber();
        auto minNum = mn.minNumber(nums);
        cout << "最小数字: " << minNum << endl; // "3033459"
        
        // 测试StatisticalProblem
        cout << "\n=== 测试StatisticalProblem ===" << endl;
        vector<string> words6 = {"banana", "band", "bee", "absolute", "acm"};
        auto stat = StatisticalProblem(words6);
        cout << "前缀'ba'的数量: " << stat.prefixCount("ba") << endl; // 2
        cout << "前缀'b'的数量: " << stat.prefixCount("b") << endl; // 3
        cout << "前缀'abc'的数量: " << stat.prefixCount("abc") << endl; // 0
    }
};

int main() {
    ExtendedTrieProblems::test();
    return 0;
}
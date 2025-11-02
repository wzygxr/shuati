#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <set>
#include <cstring>
#include <algorithm>
using namespace std;

// Trie树（前缀树）算法详解与实战
// 本文件包含多个Trie树相关题目的C++实现

/*
 * 题目1: LeetCode 208. 实现 Trie (前缀树)
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/implement-trie-prefix-tree/
 * 
 * 题目描述：
 * Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。
 * 这一数据结构有相当多的应用情景，例如自动补全和拼写检查。
 * 请你实现 Trie 类：
 * Trie() 初始化前缀树对象。
 * void insert(String word) 向前缀树中插入字符串 word 。
 * boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
 * boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 1. Trie树是一种专门处理字符串前缀的数据结构
 * 2. 每个节点包含若干子节点（对应不同字符）和一个标记（表示是否为单词结尾）
 * 3. 插入操作：从根节点开始，逐字符查找，若不存在则创建新节点
 * 4. 搜索操作：从根节点开始，逐字符查找，若路径存在且终点为单词结尾则返回true
 * 5. 前缀搜索：从根节点开始，逐字符查找，若路径存在则返回true
 * 
 * 时间复杂度分析：
 * 1. insert操作：O(m)，m为插入字符串的长度
 * 2. search操作：O(m)，m为搜索字符串的长度
 * 3. startsWith操作：O(m)，m为前缀字符串的长度
 * 空间复杂度分析：
 * 1. O(ALPHABET_SIZE * N * M)，其中N是插入的字符串数量，M是字符串的平均长度
 * 2. 最坏情况下，没有公共前缀，每个字符都需要一个节点
 * 是否为最优解：是，这是Trie树的标准实现，时间复杂度已达到理论最优
 */

// LeetCode 208. 实现 Trie (前缀树)
// 数组实现的Trie树（适用于固定字符集，如小写字母a-z）
class Trie {
private:
    struct TrieNode {
        bool isEnd; // 标记该节点是否为某个单词的结尾
        TrieNode* children[26]; // 子节点数组，对应26个小写字母
        
        // 构造函数，初始化节点
        TrieNode() : isEnd(false) {
            memset(children, 0, sizeof(children)); // 将子节点指针数组初始化为NULL
        }
        
        // 析构函数，递归释放子节点内存
        ~TrieNode() {
            for (int i = 0; i < 26; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    TrieNode* root; // 根节点
    
public:
    // 构造函数
    Trie() {
        // 初始化根节点，根节点不存储字符
        root = new TrieNode();
    }
    
    // 析构函数
    ~Trie() {
        // 释放根节点及其子节点占用的内存
        delete root;
        root = nullptr;
    }
    
    // 插入字符串到Trie树中
    void insert(string word) {
        if (word.empty()) {
            return;
        }
        
        TrieNode* node = root;
        // 逐字符插入到Trie树中
        for (char c : word) {
            int index = c - 'a'; // 计算字符对应的索引
            // 如果当前字符对应的子节点不存在，则创建
            if (!node->children[index]) {
                node->children[index] = new TrieNode();
            }
            // 移动到下一个节点
            node = node->children[index];
        }
        // 标记单词结尾
        node->isEnd = true;
    }
    
    // 搜索字符串是否存在于Trie树中
    bool search(string word) {
        if (word.empty()) {
            return false;
        }
        
        TrieNode* node = root;
        // 逐字符查找
        for (char c : word) {
            int index = c - 'a';
            // 如果当前字符对应的子节点不存在，则单词不存在
            if (!node->children[index]) {
                return false;
            }
            node = node->children[index];
        }
        // 只有当到达单词结尾且该节点被标记为单词结尾时，才返回true
        return node->isEnd;
    }
    
    // 检查是否存在以指定前缀开头的单词
    bool startsWith(string prefix) {
        if (prefix.empty()) {
            return false;
        }
        
        TrieNode* node = root;
        // 逐字符查找前缀
        for (char c : prefix) {
            int index = c - 'a';
            // 如果当前字符对应的子节点不存在，则前缀不存在
            if (!node->children[index]) {
                return false;
            }
            node = node->children[index];
        }
        // 只要前缀存在，就返回true
        return true;
    }
};

/*
 * 题目2: LeetCode 211. 添加与搜索单词 - 数据结构设计
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/design-add-and-search-words-data-structure/
 * 
 * 题目描述：
 * 请你设计一个数据结构，支持 添加新单词 和 查找字符串是否与任何先前添加的字符串匹配 。
 * 实现词典类WordDictionary：
 * WordDictionary() 初始化词典对象
 * void addWord(word) 将word添加到数据结构中，之后可以对它进行匹配
 * bool search(word) 如果数据结构中存在字符串与word匹配，则返回true；否则，返回false。word中可能包含一些'.'，每个'.'都可以表示任何一个字母
 * 
 * 解题思路：
 * 1. 使用Trie树存储添加的单词
 * 2. 搜索时遇到'.'字符，需要递归搜索所有子节点
 * 3. 使用DFS实现模糊匹配
 * 
 * 时间复杂度分析：
 * 1. addWord操作：O(m)，m为单词长度
 * 2. search操作：最坏情况O(26^m)，其中m为搜索字符串长度，当所有字符都是'.'时达到最坏情况
 * 空间复杂度分析：
 * 1. O(ALPHABET_SIZE * N * M)，其中N是插入的字符串数量，M是字符串的平均长度
 * 是否为最优解：是，对于模糊匹配问题，这是标准的解决方案
 */

// LeetCode 211. 添加与搜索单词 - 数据结构设计
class WordDictionary {
private:
    struct WordNode {
        bool isEnd; // 标记该节点是否为某个单词的结尾
        WordNode* children[26]; // 子节点数组，对应26个小写字母
        
        // 构造函数，初始化节点
        WordNode() : isEnd(false) {
            memset(children, 0, sizeof(children));
        }
        
        // 析构函数，递归释放子节点内存
        ~WordNode() {
            for (int i = 0; i < 26; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    WordNode* root; // 根节点
    
    // DFS搜索辅助函数
    bool dfsSearch(const string& word, int index, WordNode* node) {
        // 递归终止条件：已遍历完整个单词
        if (index == word.size()) {
            return node->isEnd;
        }
        
        char c = word[index];
        if (c == '.') {
            // 遇到通配符'.'，需要尝试所有可能的子节点
            for (int i = 0; i < 26; i++) {
                if (node->children[i] && dfsSearch(word, index + 1, node->children[i])) {
                    return true;
                }
            }
            return false;
        } else {
            // 普通字符，直接查找对应的子节点
            int childIndex = c - 'a';
            if (!node->children[childIndex]) {
                return false;
            }
            return dfsSearch(word, index + 1, node->children[childIndex]);
        }
    }
    
public:
    // 构造函数
    WordDictionary() {
        root = new WordNode();
    }
    
    // 析构函数
    ~WordDictionary() {
        delete root;
        root = nullptr;
    }
    
    // 添加单词到词典中
    void addWord(string word) {
        if (word.empty()) {
            return;
        }
        
        WordNode* node = root;
        for (char c : word) {
            int index = c - 'a';
            if (!node->children[index]) {
                node->children[index] = new WordNode();
            }
            node = node->children[index];
        }
        node->isEnd = true;
    }
    
    // 搜索单词，支持通配符'.'
    bool search(string word) {
        if (word.empty()) {
            return false;
        }
        // 使用递归DFS进行搜索
        return dfsSearch(word, 0, root);
    }
};

/*
 * 题目3: LeetCode 677. 键值映射
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/map-sum-pairs/
 * 
 * 题目描述：
 * 实现一个 MapSum 类，支持两个方法，insert 和 sum：
 * MapSum() 初始化 MapSum 对象
 * void insert(String key, int val) 插入 key-val 键值对，字符串表示键 key ，整数表示值 val 。如果键 key 已经存在，那么原来的键值对将被替代成新的键值对。
 * int sum(string prefix) 返回所有以该前缀 prefix 开头的键 key 的值的总和。
 * 
 * 解题思路：
 * 1. 使用Trie树存储键值对
 * 2. 每个节点存储经过该节点的键的值总和
 * 3. 插入时需要处理键已存在的情况，先减去旧值再加上新值
 * 4. 求和时找到前缀对应的节点，返回该节点存储的值总和
 * 
 * 时间复杂度分析：
 * 1. insert操作：O(m)，m为键的长度
 * 2. sum操作：O(m)，m为前缀的长度
 * 空间复杂度分析：
 * 1. O(ALPHABET_SIZE * N * M)，其中N是插入的键数量，M是键的平均长度
 * 是否为最优解：是，利用Trie树的前缀特性可以高效实现键值映射
 */

// LeetCode 677. 键值映射
class MapSum {
private:
    struct SumNode {
        int sum; // 存储经过该节点的所有键的值总和
        SumNode* children[26]; // 子节点数组，对应26个小写字母
        
        // 构造函数，初始化节点
        SumNode() : sum(0) {
            memset(children, 0, sizeof(children));
        }
        
        // 析构函数，递归释放子节点内存
        ~SumNode() {
            for (int i = 0; i < 26; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    SumNode* root; // 根节点
    unordered_map<string, int> keyMap; // 用于存储键值对，处理更新操作
    
public:
    // 构造函数
    MapSum() {
        root = new SumNode();
    }
    
    // 析构函数
    ~MapSum() {
        delete root;
        root = nullptr;
    }
    
    // 插入键值对，支持更新操作
    void insert(string key, int val) {
        if (key.empty()) {
            return;
        }
        
        // 计算值的变化量
        int delta = val;
        auto it = keyMap.find(key);
        if (it != keyMap.end()) {
            delta -= it->second;
        }
        keyMap[key] = val;
        
        // 更新Trie树中的值总和
        SumNode* node = root;
        for (char c : key) {
            int index = c - 'a';
            if (!node->children[index]) {
                node->children[index] = new SumNode();
            }
            node = node->children[index];
            node->sum += delta; // 累加变化量
        }
    }
    
    // 计算所有以指定前缀开头的键的值总和
    int sum(string prefix) {
        if (prefix.empty()) {
            return 0;
        }
        
        SumNode* node = root;
        for (char c : prefix) {
            int index = c - 'a';
            if (!node->children[index]) {
                return 0; // 前缀不存在
            }
            node = node->children[index];
        }
        return node->sum; // 返回前缀对应节点的值总和
    }
};

/*
 * 题目4: LeetCode 212. 单词搜索 II
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/word-search-ii/
 * 
 * 题目描述：
 * 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words，找出所有同时在二维网格和单词列表中出现的单词。
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母在一个单词中不允许被重复使用。
 * 
 * 解题思路：
 * 1. 构建包含所有待查单词的Trie树
 * 2. 对二维网格中的每个位置进行DFS搜索
 * 3. 搜索过程中维护当前路径构成的前缀，在Trie树中查找
 * 4. 若当前前缀对应一个完整单词，则将其加入结果集
 * 5. 使用回溯法避免重复访问同一单元格
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(K*L)，其中K为单词数量，L为单词平均长度
 * 2. DFS搜索：O(M*N*4^L)，其中M、N为网格行列数，L为最长单词长度
 * 空间复杂度分析：
 * 1. Trie树：O(K*L)
 * 2. DFS递归栈：O(M*N)
 * 是否为最优解：是，结合Trie树和DFS是解决此类问题的经典方法
 */

// LeetCode 212. 单词搜索 II
class WordSearchII {
private:
    struct SearchNode {
        string word; // 存储完整单词，仅在单词结尾节点有效
        SearchNode* children[26]; // 子节点数组，对应26个小写字母
        
        // 构造函数，初始化节点
        SearchNode() {
            memset(children, 0, sizeof(children));
        }
        
        // 析构函数，递归释放子节点内存
        ~SearchNode() {
            for (int i = 0; i < 26; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    SearchNode* root; // 根节点
    vector<vector<int>> directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右四个方向
    
    // DFS搜索辅助函数
    void dfs(vector<vector<char>>& board, int i, int j, SearchNode* node, vector<vector<bool>>& visited, set<string>& result) {
        // 检查边界条件和访问状态
        if (i < 0 || i >= board.size() || j < 0 || j >= board[0].size() || visited[i][j]) {
            return;
        }
        
        char c = board[i][j];
        int index = c - 'a';
        
        // 如果当前字符不在Trie树的当前节点的子节点中，直接返回
        if (!node->children[index]) {
            return;
        }
        
        // 移动到下一个Trie节点
        node = node->children[index];
        
        // 如果找到一个完整单词，加入结果集
        if (!node->word.empty()) {
            result.insert(node->word);
            // 注意：不要在这里return，因为可能还有更长的单词
        }
        
        // 标记当前位置为已访问
        visited[i][j] = true;
        
        // 向四个方向递归搜索
        for (auto& dir : directions) {
            dfs(board, i + dir[0], j + dir[1], node, visited, result);
        }
        
        // 回溯：恢复访问状态
        visited[i][j] = false;
    }
    
public:
    // 构造函数
    WordSearchII() {
        root = new SearchNode();
    }
    
    // 析构函数
    ~WordSearchII() {
        delete root;
        root = nullptr;
    }
    
    // 插入单词到Trie树
    void insertWord(string word) {
        SearchNode* node = root;
        for (char c : word) {
            int index = c - 'a';
            if (!node->children[index]) {
                node->children[index] = new SearchNode();
            }
            node = node->children[index];
        }
        node->word = word; // 在单词结尾节点存储完整单词
    }
    
    // 在二维网格中查找所有单词
    vector<string> findWords(vector<vector<char>>& board, vector<string>& words) {
        set<string> resultSet; // 用于存储找到的单词，避免重复
        if (board.empty() || board[0].empty() || words.empty()) {
            return {};
        }
        
        // 构建Trie树
        for (string& word : words) {
            insertWord(word);
        }
        
        // 从网格的每个位置开始搜索
        int m = board.size();
        int n = board[0].size();
        vector<vector<bool>> visited(m, vector<bool>(n, false));
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 剪枝：如果当前字符不在Trie树的根节点的子节点中，直接跳过
                int index = board[i][j] - 'a';
                if (root->children[index]) {
                    dfs(board, i, j, root, visited, resultSet);
                }
            }
        }
        
        return vector<string>(resultSet.begin(), resultSet.end());
    }
};

/*
 * 题目5: LeetCode 421. 数组中两个数的最大异或值
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
 * 
 * 解题思路：
 * 1. 使用二进制Trie树存储数组中所有数字的二进制表示（从最高位到最低位）
 * 2. 对于每个数字，从高位到低位在Trie树中贪心查找与其异或结果最大的数字
 * 3. 贪心策略：在Trie树中尽量走与当前位相反的路径（0走1，1走0）
 * 4. 这样可以保证从高位开始尽可能多地获得1，从而得到最大异或值
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(N*32) = O(N)，其中N为数组长度
 * 2. 查找最大异或值：O(N*32) = O(N)
 * 空间复杂度分析：
 * 1. O(N*32) = O(N)，Trie树最多存储N个32位整数
 * 是否为最优解：是，这是解决此类问题的经典方法，时间复杂度已达到线性
 */

// LeetCode 421. 数组中两个数的最大异或值
class MaximumXOR {
private:
    static const int MAX_BIT = 30; // 假设整数最多31位（包括符号位），这里处理到第30位
    
    struct XORNode {
        XORNode* children[2]; // 0和1两个子节点
        
        // 构造函数，初始化节点
        XORNode() {
            children[0] = nullptr;
            children[1] = nullptr;
        }
        
        // 析构函数，递归释放子节点内存
        ~XORNode() {
            for (int i = 0; i < 2; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    XORNode* root; // 根节点
    
    // 插入数字到二进制Trie树
    void insert(int num) {
        XORNode* node = root;
        // 从最高位到最低位插入
        for (int i = MAX_BIT; i >= 0; i--) {
            int bit = (num >> i) & 1; // 获取第i位的值
            if (!node->children[bit]) {
                node->children[bit] = new XORNode();
            }
            node = node->children[bit];
        }
    }
    
    // 查找与给定数字异或结果最大的数
    int searchMaxXOR(int num) {
        XORNode* node = root;
        int maxXOR = 0;
        
        for (int i = MAX_BIT; i >= 0; i--) {
            int currentBit = (num >> i) & 1;
            int targetBit = 1 - currentBit; // 寻找相反的位
            
            // 如果存在相反的位，选择该路径并更新异或结果
            if (node->children[targetBit]) {
                maxXOR |= (1 << i); // 第i位可以得到1
                node = node->children[targetBit];
            } else {
                // 否则只能选择相同的位
                node = node->children[currentBit];
            }
        }
        
        return maxXOR;
    }
    
public:
    // 构造函数
    MaximumXOR() {
        root = new XORNode();
    }
    
    // 析构函数
    ~MaximumXOR() {
        delete root;
        root = nullptr;
    }
    
    // 查找数组中两个数的最大异或值
    int findMaximumXOR(vector<int>& nums) {
        if (nums.size() <= 1) {
            return 0;
        }
        
        int maxXOR = 0;
        
        // 先插入第一个数，然后依次处理每个数
        insert(nums[0]);
        for (int i = 1; i < nums.size(); i++) {
            // 对于当前数，查找能得到最大异或值的已插入数
            maxXOR = max(maxXOR, searchMaxXOR(nums[i]));
            // 将当前数插入Trie树
            insert(nums[i]);
        }
        
        return maxXOR;
    }
};

/*
 * 题目6: POJ 3630 / HDU 1671 Phone List
 * 题目来源：POJ / HDU
 * 题目链接：http://poj.org/problem?id=3630
 *           http://acm.hdu.edu.cn/showproblem.php?pid=1671
 * 
 * 题目描述：
 * 给定n个电话号码，判断是否存在一个电话号码是另一个电话号码的前缀。
 * 如果存在输出NO，否则输出YES。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有电话号码
 * 2. 在插入过程中检查是否存在前缀关系
 * 3. 如果在插入过程中遇到已经标记为结尾的节点，说明当前字符串是之前某个字符串的前缀
 * 4. 如果在插入完成后，当前节点还有子节点，说明之前某个字符串是当前字符串的前缀
 * 
 * 时间复杂度分析：
 * 构建Trie树：O(∑len(s))，其中∑len(s)是所有电话号码长度之和
 * 空间复杂度分析：
 * O(∑len(s) * 10)，每个节点最多有10个子节点(0-9)
 * 是否为最优解：是，使用Trie树可以在线性时间内检测前缀关系
 */

// POJ 3630 / HDU 1671 Phone List
class PhoneListChecker {
private:
    struct PhoneNode {
        bool isEnd; // 标记该节点是否为某个电话号码的结尾
        PhoneNode* children[10]; // 子节点数组，对应数字0-9
        
        // 构造函数，初始化节点
        PhoneNode() : isEnd(false) {
            memset(children, 0, sizeof(children));
        }
        
        // 析构函数，递归释放子节点内存
        ~PhoneNode() {
            for (int i = 0; i < 10; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    PhoneNode* root; // 根节点
    
    // 检查电话号码是否是其他号码的前缀或有其他号码是它的前缀
    bool hasPrefix(string phone) {
        PhoneNode* node = root;
        for (char c : phone) {
            int index = c - '0';
            
            // 如果当前节点已经是某个电话号码的结尾，说明存在前缀
            if (node->isEnd) {
                return true;
            }
            
            // 如果路径不存在，说明没有前缀
            if (!node->children[index]) {
                return false;
            }
            
            node = node->children[index];
        }
        
        // 如果当前路径存在其他子节点，说明当前字符串是其他字符串的前缀
        return node != root;
    }
    
    // 插入电话号码到Trie树
    void insertPhone(string phone) {
        PhoneNode* node = root;
        for (char c : phone) {
            int index = c - '0';
            if (!node->children[index]) {
                node->children[index] = new PhoneNode();
            }
            node = node->children[index];
        }
        node->isEnd = true;
    }
    
public:
    // 构造函数
    PhoneListChecker() {
        root = new PhoneNode();
    }
    
    // 析构函数
    ~PhoneListChecker() {
        delete root;
        root = nullptr;
    }
    
    // 检查电话号码列表中是否存在前缀冲突
    bool hasPrefixConflict(vector<string>& phoneNumbers) {
        if (phoneNumbers.size() <= 1) {
            return false;
        }
        
        // 按照电话号码长度排序，先插入短的
        sort(phoneNumbers.begin(), phoneNumbers.end(), 
             [](const string& a, const string& b) { return a.size() < b.size(); });
        
        for (string& phone : phoneNumbers) {
            if (hasPrefix(phone)) {
                return true; // 存在前缀冲突
            }
            insertPhone(phone);
        }
        
        return false;
    }
};

/*
 * 题目7: 敏感词过滤
 * 题目来源：常见面试题
 * 
 * 题目描述：
 * 给定一个敏感词库，和一段文本，要求将文本中的所有敏感词替换为***。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有敏感词
 * 2. 遍历文本，从每个位置开始匹配敏感词
 * 3. 使用多模式匹配算法，提高效率
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑len(s))，其中∑len(s)是所有敏感词长度之和
 * 2. 匹配过程：O(N + M)，其中N是文本长度，M是匹配到的敏感词总长度
 * 空间复杂度分析：
 * O(∑len(s) * ALPHABET_SIZE)
 * 是否为最优解：是，使用Trie树可以高效实现多模式匹配
 */

// 敏感词过滤器
class SensitiveWordFilter {
private:
    struct FilterNode {
        bool isEnd; // 是否为敏感词结尾
        unordered_map<char, FilterNode*> children; // 使用unordered_map存储子节点，支持任意字符
        
        // 构造函数，初始化节点
        FilterNode() : isEnd(false) {}
        
        // 析构函数，递归释放子节点内存
        ~FilterNode() {
            for (auto& pair : children) {
                delete pair.second;
                pair.second = nullptr;
            }
        }
    };
    
    FilterNode* root; // 根节点
    
    // 插入敏感词到Trie树
    void insertSensitiveWord(const string& word) {
        if (word.empty()) {
            return;
        }
        
        FilterNode* node = root;
        for (char c : word) {
            if (node->children.find(c) == node->children.end()) {
                node->children[c] = new FilterNode();
            }
            node = node->children[c];
        }
        node->isEnd = true;
    }
    
    // 查找从start位置开始的最长敏感词的结束位置
    int findLongestSensitiveWord(const string& text, int start) {
        FilterNode* node = root;
        int maxEnd = start;
        
        for (int i = start; i < text.size(); i++) {
            char c = text[i];
            auto it = node->children.find(c);
            if (it == node->children.end()) {
                break;
            }
            
            node = it->second;
            if (node->isEnd) {
                maxEnd = i + 1; // 更新最长敏感词的结束位置
            }
        }
        
        return maxEnd;
    }
    
public:
    // 构造函数
    SensitiveWordFilter(const vector<string>& sensitiveWords) {
        root = new FilterNode();
        // 构建敏感词Trie树
        for (const string& word : sensitiveWords) {
            insertSensitiveWord(word);
        }
    }
    
    // 析构函数
    ~SensitiveWordFilter() {
        delete root;
        root = nullptr;
    }
    
    // 过滤文本中的敏感词
    string filter(const string& text) {
        if (text.empty()) {
            return text;
        }
        
        string result;
        int start = 0;
        
        while (start < text.size()) {
            int end = findLongestSensitiveWord(text, start);
            if (end > start) {
                // 找到敏感词，替换为***
                result.append(end - start, '*');
            } else {
                // 不是敏感词，保留原字符
                result.push_back(text[start]);
            }
            start = end > start ? end : start + 1;
        }
        
        return result;
    }
};

/*
 * 题目8: LeetCode 1032. 字符流
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/stream-of-characters/
 * 
 * 题目描述：
 * 设计一个算法：接收一个字符流，并检查这些字符的后缀是否是字符串列表中某个字符串。
 * 实现 StreamChecker 类：
 * StreamChecker(String[] words) 构造函数，用字符串数组 words 初始化数据结构
 * boolean query(char letter) 从字符流中接收一个新字符，如果存在一个单词，它是字符流中某些字符的后缀（即字符串的最后几个字符刚好匹配该单词），返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 1. 将单词反转并构建Trie树
 * 2. 维护一个字符流的历史记录
 * 3. 查询时，从当前字符开始向前匹配Trie树
 * 
 * 时间复杂度分析：
 * 1. 构造函数：O(∑len(s))
 * 2. query函数：O(min(L, maxWordLength))，其中L是字符流长度，maxWordLength是单词最大长度
 * 空间复杂度分析：
 * O(∑len(s) * ALPHABET_SIZE)
 * 是否为最优解：是，使用反转Trie树是解决此类问题的高效方法
 */

// LeetCode 1032. 字符流
class StreamChecker {
private:
    struct StreamNode {
        bool isEnd; // 标记该节点是否为某个单词的结尾
        StreamNode* children[26]; // 子节点数组，对应26个小写字母
        
        // 构造函数，初始化节点
        StreamNode() : isEnd(false) {
            memset(children, 0, sizeof(children));
        }
        
        // 析构函数，递归释放子节点内存
        ~StreamNode() {
            for (int i = 0; i < 26; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    StreamNode* root; // 根节点
    string stream; // 存储字符流历史
    int maxLength; // 记录最长单词的长度，用于优化查询
    
    // 插入反转的单词到Trie树
    void insertReversedWord(const string& word) {
        StreamNode* node = root;
        // 反转单词并插入
        for (int i = word.size() - 1; i >= 0; i--) {
            char c = word[i];
            int index = c - 'a';
            if (!node->children[index]) {
                node->children[index] = new StreamNode();
            }
            node = node->children[index];
        }
        node->isEnd = true;
    }
    
public:
    // 构造函数
    StreamChecker(vector<string>& words) {
        root = new StreamNode();
        maxLength = 0;
        
        // 构建反转Trie树
        for (const string& word : words) {
            insertReversedWord(word);
            maxLength = max(maxLength, (int)word.size());
        }
    }
    
    // 析构函数
    ~StreamChecker() {
        delete root;
        root = nullptr;
    }
    
    // 查询当前字符流的后缀是否匹配任何单词
    bool query(char letter) {
        // 将当前字符添加到流中
        stream.push_back(letter);
        
        StreamNode* node = root;
        // 从当前字符开始向前匹配，最多匹配maxLength个字符
        int start = max(0, (int)(stream.size() - maxLength));
        
        for (int i = stream.size() - 1; i >= start; i--) {
            char c = stream[i];
            int index = c - 'a';
            
            if (!node->children[index]) {
                return false;
            }
            
            node = node->children[index];
            if (node->isEnd) {
                return true; // 找到匹配的后缀
            }
        }
        
        return false;
    }
};

/*
 * 题目9: SPOJ DICT - Search in the dictionary!
 * 题目来源：SPOJ
 * 题目链接：https://www.spoj.com/problems/DICT/
 * 相关题目：
 * - CodeChef DICT - Dictionary
 * - 牛客网 最长公共前缀
 * - 杭电OJ 1251 统计难题
 * - SPOJ ADAINDEX - Ada and Indexing
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

// SPOJ DICT - Search in the dictionary!
class DictionarySearchSPOJ {
private:
    struct DictNode {
        bool isEnd; // 标记是否为单词结尾
        vector<string> words; // 存储以该节点为前缀的所有单词
        DictNode* children[26]; // 子节点数组，对应26个小写字母
        
        // 构造函数，初始化节点
        DictNode() : isEnd(false) {
            memset(children, 0, sizeof(children));
        }
        
        // 析构函数，递归释放子节点内存
        ~DictNode() {
            for (int i = 0; i < 26; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    DictNode* root; // 根节点
    
    // 插入单词到Trie树
    void insertWord(const string& word) {
        DictNode* node = root;
        for (char c : word) {
            int index = c - 'a';
            if (!node->children[index]) {
                node->children[index] = new DictNode();
            }
            node = node->children[index];
            node->words.push_back(word);
        }
        node->isEnd = true;
    }
    
public:
    // 构造函数
    DictionarySearchSPOJ(const vector<string>& dictionary) {
        root = new DictNode();
        // 构建Trie树
        for (const string& word : dictionary) {
            insertWord(word);
        }
    }
    
    // 析构函数
    ~DictionarySearchSPOJ() {
        delete root;
        root = nullptr;
    }
    
    // 搜索以指定前缀开头的所有单词
    vector<string> search(const string& prefix) {
        DictNode* node = root;
        for (char c : prefix) {
            int index = c - 'a';
            if (!node->children[index]) {
                return {}; // 前缀不存在
            }
            node = node->children[index];
        }
        // 返回该前缀对应的所有单词，按字典序排序
        vector<string> result = node->words;
        sort(result.begin(), result.end());
        return result;
    }
};

/*
 * 题目10: HDU 1251 统计难题
 * 题目来源：HDU
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1251
 * 相关题目：
 * - 牛客网 最长公共前缀
 * - CodeChef DICT - Dictionary
 * - POJ 2001 Shortest Prefixes
 * - SPOJ ADAINDEX - Ada and Indexing
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
 * 
 * 工程化考量：
 * 1. 内存优化：对于大量单词，可以考虑压缩Trie树
 * 2. 性能优化：可以缓存常用查询结果
 * 3. 异常处理：处理空查询和边界情况
 */

// HDU 1251 统计难题
class StatisticalProblemHDU {
private:
    struct StatNode {
        int count; // 经过该节点的单词数量
        StatNode* children[26]; // 子节点数组，对应26个小写字母
        
        // 构造函数，初始化节点
        StatNode() : count(0) {
            memset(children, 0, sizeof(children));
        }
        
        // 析构函数，递归释放子节点内存
        ~StatNode() {
            for (int i = 0; i < 26; i++) {
                if (children[i]) {
                    delete children[i];
                    children[i] = nullptr;
                }
            }
        }
    };
    
    StatNode* root; // 根节点
    
    // 插入单词到Trie树
    void insertWord(const string& word) {
        StatNode* node = root;
        for (char c : word) {
            int index = c - 'a';
            if (!node->children[index]) {
                node->children[index] = new StatNode();
            }
            node = node->children[index];
            node->count++;
        }
    }
    
public:
    // 构造函数
    StatisticalProblemHDU(const vector<string>& words) {
        root = new StatNode();
        for (const string& word : words) {
            insertWord(word);
        }
    }
    
    // 析构函数
    ~StatisticalProblemHDU() {
        delete root;
        root = nullptr;
    }
    
    // 统计以指定前缀开头的单词数量
    int prefixCount(const string& prefix) {
        StatNode* node = root;
        for (char c : prefix) {
            int index = c - 'a';
            if (!node->children[index]) {
                return 0;
            }
            node = node->children[index];
        }
        return node->count;
    }
};

/*
 * 总结：Trie树的核心思想与应用场景
 * 
 * 核心思想：
 * 1. 空间换时间：利用字符串的公共前缀来减少查询时间
 * 2. 树形结构：每个节点代表一个字符，从根节点到任意节点的路径表示一个字符串前缀
 * 3. 前缀共享：具有相同前缀的字符串共享存储空间
 * 
 * 应用场景：
 * 1. 自动补全：搜索引擎、IDE代码补全等
 * 2. 拼写检查：快速查找字典中的单词
 * 3. 词频统计：统计文本中单词出现次数
 * 4. IP路由：最长前缀匹配用于网络路由
 * 5. 敏感词过滤：快速匹配文本中的敏感词
 * 6. 数据压缩：霍夫曼编码等压缩算法的基础
 * 7. 最大异或值：二进制Trie树用于查找最大异或对
 * 8. 单词搜索：在字符网格中查找单词
 * 
 * 设计要点：
 * 1. 节点结构：根据需求设计包含适当信息的节点
 * 2. 字符集支持：根据字符集特点选择数组或哈希表实现子节点
 * 3. 内存优化：对于稀疏字符集，哈希表更节省空间
 * 4. 性能优化：预分配内存、剪枝等技术提升性能
 * 
 * 时间与空间复杂度：
 * 1. 插入操作：时间复杂度O(m)，m为字符串长度
 * 2. 搜索操作：时间复杂度O(m)，m为字符串长度
 * 3. 空间复杂度：O(ALPHABET_SIZE * N * M)，N为字符串数量，M为平均长度
 * 
 * 工程化考虑：
 * 1. 异常处理：输入校验和边界条件处理
 * 2. 线程安全：根据使用场景决定是否需要同步机制
 * 3. 内存管理：在C++中需要注意内存泄漏，正确实现析构函数
 * 4. 性能监控：添加统计信息，监控内存使用和查询性能
 * 5. 可配置性：支持不同字符集和配置参数
 * 6. 测试覆盖：编写全面的单元测试，覆盖各种边界情况
 */

// 测试代码
int main() {
    cout << "Trie树算法测试" << endl;
    
    // 测试LeetCode 208
    cout << "\n测试LeetCode 208. 实现 Trie (前缀树)" << endl;
    Trie trie;
    trie.insert("apple");
    cout << "search(\"apple\"): " << (trie.search("apple") ? "true" : "false") << endl;    // true
    cout << "search(\"app\"): " << (trie.search("app") ? "true" : "false") << endl;        // false
    cout << "startsWith(\"app\"): " << (trie.startsWith("app") ? "true" : "false") << endl; // true
    trie.insert("app");
    cout << "search(\"app\"): " << (trie.search("app") ? "true" : "false") << endl;        // true
    
    // 测试LeetCode 211
    cout << "\n测试LeetCode 211. 添加与搜索单词 - 数据结构设计" << endl;
    WordDictionary wordDict;
    wordDict.addWord("bad");
    wordDict.addWord("dad");
    wordDict.addWord("mad");
    cout << "search(\"pad\"): " << (wordDict.search("pad") ? "true" : "false") << endl;     // false
    cout << "search(\"bad\"): " << (wordDict.search("bad") ? "true" : "false") << endl;     // true
    cout << "search(\".ad\"): " << (wordDict.search(".ad") ? "true" : "false") << endl;       // true
    cout << "search(\"b..\"): " << (wordDict.search("b..") ? "true" : "false") << endl;       // true
    
    // 测试LeetCode 677
    cout << "\n测试LeetCode 677. 键值映射" << endl;
    MapSum mapSum;
    mapSum.insert("apple", 3);
    cout << "sum(\"ap\"): " << mapSum.sum("ap") << endl;               // 3
    mapSum.insert("app", 2);
    cout << "sum(\"ap\"): " << mapSum.sum("ap") << endl;               // 5
    
    // 测试SPOJ DICT
    cout << "\n测试SPOJ DICT - Search in the dictionary!" << endl;
    vector<string> dictionary = {"abc", "abcd", "abcde", "bcd", "bcde"};
    DictionarySearchSPOJ dictSearch(dictionary);
    vector<string> dictResults = dictSearch.search("abc");
    cout << "前缀\"abc\"的单词: ";
    for (const string& word : dictResults) {
        cout << word << " ";
    }
    cout << endl;
    
    // 测试HDU 1251
    cout << "\n测试HDU 1251 统计难题" << endl;
    vector<string> wordsHDU = {"abc", "abcde", "abcdef", "bcd", "bcde"};
    StatisticalProblemHDU statProblem(wordsHDU);
    cout << "前缀\"abc\"的数量: " << statProblem.prefixCount("abc") << endl;  // 3
    cout << "前缀\"bc\"的数量: " << statProblem.prefixCount("bc") << endl;    // 2
    cout << "前缀\"xyz\"的数量: " << statProblem.prefixCount("xyz") << endl;  // 0
    
    return 0;
}
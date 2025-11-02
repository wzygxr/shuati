# Trie (前缀树) 算法题目汇总与解答

## 目录
1. [简介](#简介)
2. [核心概念](#核心概念)
3. [题目列表](#题目列表)
4. [题目详解](#题目详解)
   - [LeetCode 208. Implement Trie (Prefix Tree)](#leetcode-208-implement-trie-prefix-tree)
   - [LeetCode 211. Design Add and Search Words Data Structure](#leetcode-211-design-add-and-search-words-data-structure)
   - [LeetCode 212. Word Search II](#leetcode-212-word-search-ii)
   - [LeetCode 1707. Maximum XOR With an Element From Array](#leetcode-1707-maximum-xor-with-an-element-from-array)
   - [LeetCode 1803. Count Pairs With XOR in a Range](#leetcode-1803-count-pairs-with-xor-in-a-range)
   - [LeetCode 677. Map Sum Pairs](#leetcode-677-map-sum-pairs)
   - [LeetCode 1268. Search Suggestions System](#leetcode-1268-search-suggestions-system)
   - [HackerRank Contacts](#hackerrank-contacts)
   - [SPOJ PHONELST](#spoj-phonelst)
   - [SPOJ DICT](#spoj-dict)
5. [总结](#总结)

---

## 简介

Trie（前缀树）是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。它在许多应用场景中都非常有用，如自动补全、拼写检查、IP路由等。

## 核心概念

1. **节点结构**：每个节点包含指向子节点的指针数组（通常为26个字母）和一个标记表示是否为单词结尾
2. **插入操作**：逐字符遍历字符串，创建路径上的节点
3. **搜索操作**：逐字符遍历字符串，检查路径是否存在
4. **前缀搜索**：检查是否存在以指定前缀开头的字符串

## 题目列表

### LeetCode 题目
1. [208. Implement Trie (Prefix Tree)](#leetcode-208-implement-trie-prefix-tree)
2. [211. Design Add and Search Words Data Structure](#leetcode-211-design-add-and-search-words-data-structure)
3. [212. Word Search II](#leetcode-212-word-search-ii)
4. [1707. Maximum XOR With an Element From Array](#leetcode-1707-maximum-xor-with-an-element-from-array)
5. [1803. Count Pairs With XOR in a Range](#leetcode-1803-count-pairs-with-xor-in-a-range)
6. [677. Map Sum Pairs](#leetcode-677-map-sum-pairs)
7. [1268. Search Suggestions System](#leetcode-1268-search-suggestions-system)
8. [421. Maximum XOR of Two Numbers in an Array](#相关题目扩展)
9. [648. Replace Words](#相关题目扩展)
10. [642. Design Search Autocomplete System](#相关题目扩展)

### 其他平台题目
1. [HackerRank Contacts](#hackerrank-contacts)
2. [SPOJ PHONELST](#spoj-phonelst)
3. [SPOJ DICT](#spoj-dict)
4. [LintCode 442. Implement Trie](#相关题目扩展)
5. [LintCode 3729. Implement Trie II](#相关题目扩展)

---

## 题目详解

### LeetCode 208. Implement Trie (Prefix Tree)

#### 题目描述
实现一个 Trie 类，包含插入、搜索和前缀检查功能。

#### 解题思路
1. 设计TrieNode类，包含子节点数组和结束标记
2. 实现insert方法，逐字符创建路径
3. 实现search方法，检查完整单词是否存在
4. 实现startsWith方法，检查前缀是否存在

#### 时间复杂度
- 插入操作：O(m)，其中m为字符串长度
- 搜索操作：O(m)，其中m为字符串长度

#### 空间复杂度
- O(ALPHABET_SIZE × N × M)，其中N为插入的字符串数量，M为平均字符串长度

#### Java实现
```java
class TrieNode {
    TrieNode[] children;
    boolean isEnd;
    
    public TrieNode() {
        children = new TrieNode[26];
        isEnd = false;
    }
}

class Trie {
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }
    
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                return false;
            }
            node = node.children[index];
        }
        return node.isEnd;
    }
    
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                return false;
            }
            node = node.children[index];
        }
        return true;
    }
}
```

#### Python实现
```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
    
    def search(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.is_end
    
    def startsWith(self, prefix):
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True
```

#### C++实现
```cpp
#include <iostream>
#include <vector>
#include <string>
using namespace std;

class TrieNode {
public:
    vector<TrieNode*> children;
    bool isEnd;
    
    TrieNode() : children(26, nullptr), isEnd(false) {}
};

class Trie {
private:
    TrieNode* root;
    
public:
    Trie() {
        root = new TrieNode();
    }
    
    void insert(string word) {
        TrieNode* node = root;
        for (char c : word) {
            int index = c - 'a';
            if (node->children[index] == nullptr) {
                node->children[index] = new TrieNode();
            }
            node = node->children[index];
        }
        node->isEnd = true;
    }
    
    bool search(string word) {
        TrieNode* node = root;
        for (char c : word) {
            int index = c - 'a';
            if (node->children[index] == nullptr) {
                return false;
            }
            node = node->children[index];
        }
        return node->isEnd;
    }
    
    bool startsWith(string prefix) {
        TrieNode* node = root;
        for (char c : prefix) {
            int index = c - 'a';
            if (node->children[index] == nullptr) {
                return false;
            }
            node = node->children[index];
        }
        return true;
    }
};
```

### LeetCode 211. Design Add and Search Words Data Structure

#### 题目描述
设计一个数据结构，支持添加单词和搜索单词（支持通配符 '.'）。

#### 解题思路
1. 基于Trie的实现
2. 对于通配符'.'，需要递归搜索所有子节点

#### 时间复杂度
- 插入操作：O(m)
- 搜索操作：O(26^m)（最坏情况）

#### 空间复杂度
- O(ALPHABET_SIZE × N × M)

#### Java实现
```java
class WordDictionary {
    class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        
        public TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
        }
    }
    
    private TrieNode root;
    
    public WordDictionary() {
        root = new TrieNode();
    }
    
    public void addWord(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (c == '.') continue;
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }
    
    public boolean search(String word) {
        return searchHelper(word, 0, root);
    }
    
    private boolean searchHelper(String word, int index, TrieNode node) {
        if (index == word.length()) {
            return node.isEnd;
        }
        
        char c = word.charAt(index);
        if (c != '.') {
            int childIndex = c - 'a';
            return node.children[childIndex] != null && 
                   searchHelper(word, index + 1, node.children[childIndex]);
        } else {
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null && 
                    searchHelper(word, index + 1, node.children[i])) {
                    return true;
                }
            }
            return false;
        }
    }
}
```

### LeetCode 212. Word Search II

#### 题目描述
在二维字符网格中查找所有存在的单词。

#### 解题思路
1. 构建包含所有单词的Trie
2. 使用DFS遍历网格，同时在Trie中搜索

#### 时间复杂度
- O(M×N×4^L)，其中M和N是网格的行数和列数，L是最长单词的长度

#### 空间复杂度
- O(K×L)，其中K是单词数量，L是平均单词长度

### LeetCode 1707. Maximum XOR With an Element From Array

#### 题目描述
给定一个数组和查询列表，对每个查询找出与指定元素异或的最大值。

#### 解题思路
1. 使用二进制Trie存储数组元素
2. 对每个查询，在Trie中寻找最大异或值

#### 时间复杂度
- O(N×32 + Q×32)，其中N是数组长度，Q是查询数量

#### 空间复杂度
- O(N×32)

### LeetCode 1803. Count Pairs With XOR in a Range

#### 题目描述
统计数组中异或值在指定范围内的数对数量。

#### 解题思路
1. 使用二进制Trie存储数组元素
2. 对每个元素，在Trie中统计满足条件的数对数量

#### 时间复杂度
- O(N×32×log(N))

#### 空间复杂度
- O(N×32)

### LeetCode 677. Map Sum Pairs

#### 题目描述
实现一个键值映射类，支持插入键值对和查询指定前缀的所有键值之和。

#### 解题思路
1. 在Trie节点中存储累积值
2. 插入时更新路径上所有节点的值

#### 时间复杂度
- 插入操作：O(m)
- 求和操作：O(m)

#### 空间复杂度
- O(ALPHABET_SIZE × N × M)

### LeetCode 1268. Search Suggestions System

#### 题目描述
设计一个搜索推荐系统，根据输入的每个字母推荐产品。

#### 解题思路
1. 构建包含所有产品的Trie
2. 对每个前缀，DFS收集最多3个推荐产品

#### 时间复杂度
- 构建Trie：O(N×M)
- 查询：O(M×K)，其中K是结果数量

#### 空间复杂度
- O(ALPHABET_SIZE × N × M)

### HackerRank Contacts

#### 题目描述
实现一个联系人管理系统，支持添加联系人和查询指定前缀的联系人数量。

#### 解题思路
1. 在Trie节点中存储以该节点为前缀的单词数量
2. 插入时更新路径上所有节点的计数

### SPOJ PHONELST

#### 题目描述
判断给定的电话号码列表是否一致（没有号码是另一个号码的前缀）。

#### 解题思路
1. 构建Trie存储所有电话号码
2. 检查是否存在一个单词是另一个单词的前缀

### SPOJ DICT

#### 题目描述
在字典中查找具有指定前缀的所有单词。

#### 解题思路
1. 构建Trie存储所有单词
2. 找到前缀对应的节点，DFS收集所有单词

---

## 相关题目扩展

### 字符串相关
1. **LeetCode 421. Maximum XOR of Two Numbers in an Array** - 数组中两个数的最大异或值
2. **LeetCode 648. Replace Words** - 单词替换
3. **LeetCode 642. Design Search Autocomplete System** - 设计搜索自动补全系统

### 设计类
1. **LintCode 442. Implement Trie** - 实现 Trie 结构
2. **LintCode 3729. Implement Trie II** - 实现 Trie II

### 搜索类
1. **LeetCode 211. Design Add and Search Words Data Structure** - 添加与搜索单词 - 数据结构设计
2. **LeetCode 642. Design Search Autocomplete System** - 设计搜索自动补全系统

---

## 总结

Trie是一种非常重要的数据结构，在处理字符串相关问题时具有独特优势：

1. **优势**：
   - 高效的前缀匹配
   - 节省空间（共享前缀）
   - 支持按字典序遍历

2. **适用场景**：
   - 自动补全系统
   - 拼写检查
   - IP路由
   - 单词游戏

3. **变体**：
   - 压缩Trie（Patricia Trie）
   - 后缀Trie
   - 二进制Trie（用于异或运算）

通过掌握Trie的基本操作和应用场景，可以解决大量字符串处理问题。
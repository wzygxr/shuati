// LintCode 442. Implement Trie (Prefix Tree)
// 题目描述：
// 实现一个Trie（前缀树），包含insert, search, 和startsWith这三个操作。
// 测试链接：https://www.lintcode.com/problem/442/
//
// 相关题目扩展：
// 1. LeetCode 208. 实现 Trie (前缀树) (本题与LeetCode 208相同)
// 2. LeetCode 212. 单词搜索 II
// 3. LeetCode 421. 数组中两个数的最大异或值
// 4. HackerRank Contacts
// 5. SPOJ DICT
// 6. SPOJ PHONELST
// 7. LintCode 442. 实现 Trie (前缀树)
// 8. 牛客网 NC105. 二分查找-II
// 9. 牛客网 NC138. 字符串匹配
// 10. CodeChef - ANAGRAMS

#include <iostream>
#include <string>
#include <vector>
using namespace std;

/**
 * 实现Trie（前缀树）
 * 
 * 算法思路：
 * 1. 设计TrieNode类，包含子节点数组和单词结尾标记
 * 2. 实现insert方法：逐个字符遍历单词，创建节点并建立连接
 * 3. 实现search方法：逐个字符遍历单词，查找节点并检查是否为单词结尾
 * 4. 实现startsWith方法：逐个字符遍历前缀，查找节点
 * 
 * 时间复杂度分析：
 * - insert操作：O(len(word))，其中len(word)是单词长度
 * - search操作：O(len(word))，其中len(word)是单词长度
 * - startsWith操作：O(len(prefix))，其中len(prefix)是前缀长度
 * 
 * 空间复杂度分析：
 * - Trie空间：O(∑len(words))，用于存储所有插入的单词
 * - 总体空间复杂度：O(∑len(words))
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效地处理字符串的插入、搜索和前缀匹配操作
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或字符串包含非法字符的情况
 * 2. 边界情况：空字符串的情况
 * 3. 极端输入：大量操作或极长字符串的情况
 * 4. 鲁棒性：处理大小写敏感和特殊字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：使用指针实现前缀树节点，更节省空间
 * Python：使用字典实现前缀树，代码更简洁
 */

class TrieNode {
public:
    // 子节点指针数组
    TrieNode* children[26];
    // 单词结尾标记
    bool isEnd;
    
    /**
     * 初始化前缀树节点
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    TrieNode() {
        isEnd = false;
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
    }
    
    /**
     * 析构函数，释放子节点内存
     * 
     * 时间复杂度：O(n)，其中n是节点数量
     * 空间复杂度：O(1)
     */
    ~TrieNode() {
        for (int i = 0; i < 26; i++) {
            if (children[i]) {
                delete children[i];
            }
        }
    }
};

class Trie {
private:
    // 根节点
    TrieNode* root;
    
    /**
     * 将字符映射到路径索引
     * 
     * 'a' 映射到 0
     * 'b' 映射到 1
     * ...
     * 'z' 映射到 25
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * @param cha 字符
     * @return 路径索引
     */
    int path(char cha) {
        return cha - 'a';
    }
    
public:
    /**
     * 初始化前缀树
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    Trie() {
        root = new TrieNode();
    }
    
    /**
     * 析构函数，释放根节点内存
     * 
     * 时间复杂度：O(n)，其中n是所有节点数量
     * 空间复杂度：O(1)
     */
    ~Trie() {
        delete root;
    }
    
    /**
     * 向前缀树中插入单词
     * 
     * 算法思路：
     * 1. 从根节点开始遍历单词
     * 2. 对于每个字符，计算路径索引
     * 3. 如果子节点不存在，则创建新节点
     * 4. 移动到子节点，继续遍历
     * 5. 遍历完成后，标记单词结尾
     * 
     * 时间复杂度：O(len(word))，其中len(word)是单词长度
     * 空间复杂度：O(len(word))，最坏情况下需要创建新节点
     * 
     * @param word 待插入的单词
     */
    void insert(string word) {
        TrieNode* node = root;
        for (char c : word) {
            int p = path(c);
            if (!node->children[p]) {
                node->children[p] = new TrieNode();
            }
            node = node->children[p];
        }
        node->isEnd = true;
    }
    
    /**
     * 在前缀树中搜索单词
     * 
     * 算法思路：
     * 1. 从根节点开始遍历单词
     * 2. 对于每个字符，计算路径索引
     * 3. 如果子节点不存在，返回false
     * 4. 移动到子节点，继续遍历
     * 5. 遍历完成后，检查当前节点是否为单词结尾
     * 
     * 时间复杂度：O(len(word))，其中len(word)是单词长度
     * 空间复杂度：O(1)
     * 
     * @param word 待搜索的单词
     * @return 如果单词存在返回true，否则返回false
     */
    bool search(string word) {
        TrieNode* node = root;
        for (char c : word) {
            int p = path(c);
            if (!node->children[p]) {
                return false;
            }
            node = node->children[p];
        }
        return node->isEnd;
    }
    
    /**
     * 检查前缀树中是否存在以prefix为前缀的单词
     * 
     * 算法思路：
     * 1. 从根节点开始遍历前缀
     * 2. 对于每个字符，计算路径索引
     * 3. 如果子节点不存在，返回false
     * 4. 移动到子节点，继续遍历
     * 5. 遍历完成后，返回true
     * 
     * 时间复杂度：O(len(prefix))，其中len(prefix)是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param prefix 待检查的前缀
     * @return 如果存在以prefix为前缀的单词返回true，否则返回false
     */
    bool startsWith(string prefix) {
        TrieNode* node = root;
        for (char c : prefix) {
            int p = path(c);
            if (!node->children[p]) {
                return false;
            }
            node = node->children[p];
        }
        return true;
    }
    
    /**
     * 清空前缀树
     * 
     * 算法思路：
     * 1. 删除旧的根节点
     * 2. 创建新的根节点
     * 
     * 时间复杂度：O(n)，其中n是所有节点数量
     * 空间复杂度：O(1)
     */
    void clear() {
        delete root;
        root = new TrieNode();
    }
};

// 测试代码
int main() {
    Trie trie;
    
    // 测试插入和搜索
    trie.insert("apple");
    cout << "Search 'apple': " << (trie.search("apple") ? "true" : "false") << endl;  // 返回 true
    cout << "Search 'app': " << (trie.search("app") ? "true" : "false") << endl;      // 返回 false
    cout << "StartsWith 'app': " << (trie.startsWith("app") ? "true" : "false") << endl;  // 返回 true
    
    // 测试插入新单词
    trie.insert("app");
    cout << "Search 'app' after insert: " << (trie.search("app") ? "true" : "false") << endl;  // 返回 true
    
    return 0;
}
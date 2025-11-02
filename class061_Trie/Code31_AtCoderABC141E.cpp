#include <bits/stdc++.h>

using namespace std;

/**
 * AtCoder ABC141 E - Who Says a Pun?
 * 
 * 题目描述：
 * 给定一个字符串，求最长的出现至少两次的不重叠子串长度。
 * 
 * 解题思路：
 * 1. 使用二分答案，对于每个长度，检查是否存在出现至少两次的不重叠子串
 * 2. 使用前缀树或哈希来检查是否存在重复子串
 * 3. 对于每个长度，枚举所有子串并在前缀树中查找是否已存在
 * 
 * 时间复杂度：O(N^2 * log N)，其中N是字符串长度
 * 空间复杂度：O(N^2)
 */

// Trie树节点结构
struct TrieNode {
    map<char, TrieNode*> children; // 子节点映射
    vector<int> positions;         // 存储子串出现的位置
};

TrieNode* root;

/**
 * 初始化Trie树
 */
void init() {
    root = new TrieNode();
}

/**
 * 向Trie树中插入子串并检查是否已存在不重叠的子串
 * @param substr 子串
 * @param pos 子串起始位置
 * @param len 子串长度
 * @return 是否存在不重叠的重复子串
 */
bool insertAndCheck(const string& substr, int pos, int len) {
    TrieNode* node = root;
    for (char c : substr) {
        // 如果子节点不存在，创建新节点
        if (node->children.find(c) == node->children.end()) {
            node->children[c] = new TrieNode();
        }
        node = node->children[c];
    }
    
    // 检查是否存在不重叠的子串
    for (int prevPos : node->positions) {
        if (abs(pos - prevPos) >= len) {
            return true; // 找到不重叠的重复子串
        }
    }
    
    // 记录当前位置
    node->positions.push_back(pos);
    return false;
}

/**
 * 检查是否存在长度为len的重复不重叠子串
 * @param str 字符串
 * @param len 子串长度
 * @return 是否存在重复不重叠子串
 */
bool hasDuplicateNonOverlappingSubstring(const string& str, int len) {
    init(); // 初始化Trie树
    
    // 枚举所有长度为len的子串
    for (int i = 0; i <= (int)str.length() - len; i++) {
        string substr = str.substr(i, len);
        if (insertAndCheck(substr, i, len)) {
            return true;
        }
    }
    
    return false;
}

/**
 * 二分查找最长的重复不重叠子串长度
 * @param str 字符串
 * @return 最长重复不重叠子串长度
 */
int findLongestDuplicateNonOverlappingSubstring(const string& str) {
    int left = 0;
    int right = str.length() / 2;
    int result = 0;
    
    // 二分答案
    while (left <= right) {
        int mid = (left + right) / 2;
        if (hasDuplicateNonOverlappingSubstring(str, mid)) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

int main() {
    int n;
    cin >> n; // 字符串长度
    cin.ignore(); // 消费换行符
    
    string str;
    getline(cin, str); // 字符串
    
    int result = findLongestDuplicateNonOverlappingSubstring(str);
    cout << result << endl;
    
    return 0;
}
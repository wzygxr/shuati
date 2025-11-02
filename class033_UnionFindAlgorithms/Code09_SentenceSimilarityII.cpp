// C++标准库头文件
#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
using namespace std;

/**
 * 句子相似性II (C++版本)
 * 我们可以将一个句子表示为一个单词数组，例如，句子 "I am happy with leetcode" 可以表示为 arr = ["I","am","happy","with","leetcode"]。
 * 给定两个句子 sentence1 和 sentence2 分别表示为一个字符串数组，并给定一个字符串对的列表 similarPairs，其中 similarPairs[i] = [xi, yi] 表示两个单词 xi 和 yi 是相似的。
 * 如果两个句子句子长度相同，且在对应位置上的单词要么相同，要么通过相似单词对列表中的相似关系（可能是间接的相似关系）变得相似，则认为这两个句子是相似的。
 * 返回 true 如果 sentence1 和 sentence2 是相似的。
 * 
 * 示例 1:
 * 输入:
 * sentence1 = ["great","acting","skills"]
 * sentence2 = ["fine","drama","talent"]
 * similarPairs = [["great","good"],["fine","good"],["drama","acting"],["skills","talent"]]
 * 输出: true
 * 解释: "great" 和 "fine" 相似, "acting" 和 "drama" 相似, "skills" 和 "talent" 相似。
 * 
 * 示例 2:
 * 输入:
 * sentence1 = ["great"]
 * sentence2 = ["great"]
 * similarPairs = []
 * 输出: true
 * 解释: 句子长度相同且单词相同。
 * 
 * 示例 3:
 * 输入:
 * sentence1 = ["great"]
 * sentence2 = ["doubleplus","good"]
 * similarPairs = [["great","doubleplus"]]
 * 输出: false
 * 解释: 句子长度不同。
 * 
 * 约束条件:
 * 1 <= sentence1.length, sentence2.length <= 1000
 * 1 <= sentence1[i].length, sentence2[i].length <= 20
 * sentence1[i] 和 sentence2[i] 仅包含大小写英文字母
 * 0 <= similarPairs.length <= 2000
 * similarPairs[i].length == 2
 * 1 <= xi.length, yi.length <= 20
 * xi 和 yi 仅包含大小写英文字母
 * 所有相似单词对都是不同的
 * 
 * 测试链接: https://leetcode.cn/problems/sentence-similarity-ii/
 * 相关平台: LeetCode 737
 */

class UnionFind {
private:
    unordered_map<string, string> parent;  // 使用哈希表存储单词到其父节点的映射

public:
    /**
     * 查找单词所在集合的根节点
     * 使用路径压缩优化
     * @param word 要查找的单词
     * @return 单词所在集合的根节点
     */
    string find(const string& word) {
        // 如果单词不存在于parent中，将其添加到parent中，并将其父节点设为自身
        if (parent.find(word) == parent.end()) {
            parent[word] = word;
        }
        // 路径压缩：将路径上的所有节点直接连接到根节点
        if (parent[word] != word) {
            parent[word] = find(parent[word]);
        }
        return parent[word];
    }
    
    /**
     * 合并两个单词所在的集合
     * @param word1 第一个单词
     * @param word2 第二个单词
     */
    void unionSets(const string& word1, const string& word2) {
        string root1 = find(word1);
        string root2 = find(word2);
        // 如果两个单词不在同一个集合中，则合并它们
        if (root1 != root2) {
            parent[root1] = root2;
        }
    }
    
    /**
     * 判断两个单词是否在同一个集合中
     * @param word1 第一个单词
     * @param word2 第二个单词
     * @return 如果在同一个集合中返回true，否则返回false
     */
    bool isConnected(const string& word1, const string& word2) {
        return find(word1) == find(word2);
    }
};

/**
 * 判断两个句子是否相似
 * 
 * 解题思路：
 * 1. 首先检查两个句子的长度是否相同，如果不同则直接返回false
 * 2. 使用并查集将相似单词对中的单词合并到同一个集合中
 * 3. 遍历两个句子的对应位置单词，检查它们是否相同或者属于同一个集合
 * 
 * 时间复杂度：O(P + N)，其中P是similarPairs的长度，N是句子的长度，
 *          并查集操作的时间复杂度接近O(1)（路径压缩优化）
 * 空间复杂度：O(P)，用于存储并查集
 * 
 * @param sentence1 第一个句子
 * @param sentence2 第二个句子
 * @param similarPairs 相似单词对列表
 * @return 如果两个句子相似返回true，否则返回false
 */
bool areSentencesSimilarTwo(vector<string>& sentence1, vector<string>& sentence2, vector<vector<string>>& similarPairs) {
    // 检查两个句子的长度是否相同
    if (sentence1.size() != sentence2.size()) {
        return false;
    }
    
    // 初始化并查集
    UnionFind unionFind;
    
    // 将所有相似单词对合并到同一个集合中
    for (const auto& pair : similarPairs) {
        unionFind.unionSets(pair[0], pair[1]);
    }
    
    // 遍历两个句子的对应位置单词进行比较
    for (int i = 0; i < sentence1.size(); i++) {
        const string& word1 = sentence1[i];
        const string& word2 = sentence2[i];
        
        // 如果单词相同，或者属于同一个集合，则继续比较下一对单词
        if (word1 == word2 || unionFind.isConnected(word1, word2)) {
            continue;
        }
        // 否则，两个句子不相似
        return false;
    }
    
    // 所有对应位置的单词都相似，返回true
    return true;
}

// 主函数用于测试
int main() {
    // 测试用例1
    vector<string> sentence1 = {"great", "acting", "skills"};
    vector<string> sentence2 = {"fine", "drama", "talent"};
    vector<vector<string>> similarPairs1 = {{"great", "good"}, {"fine", "good"}, {"drama", "acting"}, {"skills", "talent"}};
    cout << "测试用例1结果: " << (areSentencesSimilarTwo(sentence1, sentence2, similarPairs1) ? "true" : "false") << endl;
    
    // 测试用例2
    vector<string> sentence3 = {"great"};
    vector<string> sentence4 = {"great"};
    vector<vector<string>> similarPairs2 = {};
    cout << "测试用例2结果: " << (areSentencesSimilarTwo(sentence3, sentence4, similarPairs2) ? "true" : "false") << endl;
    
    // 测试用例3
    vector<string> sentence5 = {"great"};
    vector<string> sentence6 = {"doubleplus", "good"};
    vector<vector<string>> similarPairs3 = {{"great", "doubleplus"}};
    cout << "测试用例3结果: " << (areSentencesSimilarTwo(sentence5, sentence6, similarPairs3) ? "true" : "false") << endl;
    
    return 0;
}
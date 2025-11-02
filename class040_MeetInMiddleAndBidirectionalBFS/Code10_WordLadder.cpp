// Word Ladder (LeetCode 127)
// 题目来源：LeetCode
// 题目描述：
// 给定两个单词（beginWord 和 endWord）和一个字典 wordList，找出从 beginWord 到 endWord 的最短转换序列的长度。
// 转换需遵循如下规则：
// 1. 每次转换只能改变一个字母。
// 2. 转换后的单词必须是字典中的单词。
// 3. 如果不存在这样的转换序列，返回 0。
// 测试链接：https://leetcode.com/problems/word-ladder/
// 
// 算法思路：
// 使用双向BFS算法，从起点和终点同时开始搜索，当两个搜索前沿相遇时，找到最短路径
// 时间复杂度：O(M*N^2)，其中M是单词长度，N是字典大小
// 空间复杂度：O(N)
// 
// 工程化考量：
// 1. 预处理：将单词按模式分组，提高生成邻接节点的效率
// 2. 优化：始终从较小的集合开始扩展，减少搜索空间
// 3. 边界检查：处理特殊情况（如endWord不在字典中）
// 
// 语言特性差异：
// C++中使用unordered_set存储访问过的节点，使用unordered_map存储单词模式映射

#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <queue>
using namespace std;

/**
 * 计算从beginWord到endWord的最短转换序列长度
 * @param beginWord 起始单词
 * @param endWord 目标单词
 * @param wordList 单词列表
 * @return 最短转换序列的长度，如果不存在返回0
 */
int ladderLength(string beginWord, string endWord, vector<string>& wordList) {
    // 边界条件检查
    unordered_set<string> wordSet(wordList.begin(), wordList.end());
    if (wordSet.find(endWord) == wordSet.end()) {
        return 0; // 如果endWord不在字典中，无法转换
    }
    
    // 创建双向BFS所需的集合
    unordered_set<string> beginSet;
    unordered_set<string> endSet;
    unordered_set<string> visited;
    
    // 初始化
    beginSet.insert(beginWord);
    endSet.insert(endWord);
    int length = 1; // 初始长度为1（包含beginWord）
    
    // 开始双向BFS
    while (!beginSet.empty() && !endSet.empty()) {
        // 优化：始终从较小的集合开始扩展，减少搜索空间
        if (beginSet.size() > endSet.size()) {
            // 交换beginSet和endSet
            swap(beginSet, endSet);
        }
        
        // 存储当前层的下一层节点
        unordered_set<string> nextLevel;
        
        // 遍历当前层的所有节点
        for (string word : beginSet) {
            // 生成所有可能的转换
            for (int i = 0; i < word.size(); i++) {
                char originalChar = word[i];
                
                // 尝试将当前字符替换为其他25个小写字母
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == originalChar) {
                        continue;
                    }
                    
                    word[i] = c;
                    
                    // 如果在另一个集合中找到，则找到了路径
                    if (endSet.find(word) != endSet.end()) {
                        return length + 1;
                    }
                    
                    // 如果单词在字典中且未被访问过，则加入下一层
                    if (wordSet.find(word) != wordSet.end() && visited.find(word) == visited.end()) {
                        nextLevel.insert(word);
                        visited.insert(word);
                    }
                }
                
                // 恢复原字符
                word[i] = originalChar;
            }
        }
        
        // 更新当前层
        beginSet = nextLevel;
        length++;
    }
    
    // 如果两个集合不再相交，表示没有找到路径
    return 0;
}

/**
 * 优化版本：使用单词模式映射进行优化
 */
int ladderLengthOptimized(string beginWord, string endWord, vector<string>& wordList) {
    // 边界条件检查
    unordered_set<string> wordSet(wordList.begin(), wordList.end());
    if (wordSet.find(endWord) == wordSet.end()) {
        return 0;
    }
    
    int L = beginWord.size();
    
    // 预处理：将单词按模式分组，例如：h*t -> [hot, hit, hat...]
    unordered_map<string, vector<string>> patternToWords;
    for (string word : wordList) {
        for (int i = 0; i < L; i++) {
            string pattern = word.substr(0, i) + "*" + word.substr(i + 1);
            patternToWords[pattern].push_back(word);
        }
    }
    
    // 添加beginWord到模式映射
    for (int i = 0; i < L; i++) {
        string pattern = beginWord.substr(0, i) + "*" + beginWord.substr(i + 1);
        if (patternToWords.find(pattern) == patternToWords.end()) {
            patternToWords[pattern] = vector<string>();
        }
    }
    
    // 双向BFS
    unordered_set<string> beginSet;
    unordered_set<string> endSet;
    unordered_set<string> visited;
    
    beginSet.insert(beginWord);
    endSet.insert(endWord);
    int length = 1;
    
    while (!beginSet.empty() && !endSet.empty()) {
        if (beginSet.size() > endSet.size()) {
            swap(beginSet, endSet);
        }
        
        unordered_set<string> nextLevel;
        
        for (string word : beginSet) {
            for (int i = 0; i < L; i++) {
                string pattern = word.substr(0, i) + "*" + word.substr(i + 1);
                
                // 获取所有匹配该模式的单词
                for (string neighbor : patternToWords[pattern]) {
                    if (endSet.find(neighbor) != endSet.end()) {
                        return length + 1;
                    }
                    
                    if (visited.find(neighbor) == visited.end()) {
                        nextLevel.insert(neighbor);
                        visited.insert(neighbor);
                    }
                }
            }
        }
        
        beginSet = nextLevel;
        length++;
    }
    
    return 0;
}

// 测试方法
int main() {
    // 测试用例1
    string beginWord1 = "hit";
    string endWord1 = "cog";
    vector<string> wordList1 = {"hot", "dot", "dog", "lot", "log", "cog"};
    cout << "测试用例1：" << endl;
    cout << "beginWord: " << beginWord1 << ", endWord: " << endWord1 << endl;
    cout << "wordList: [hot, dot, dog, lot, log, cog]" << endl;
    cout << "期望输出：5" << endl; // hit -> hot -> dot -> dog -> cog
    cout << "实际输出（普通版）：" << ladderLength(beginWord1, endWord1, wordList1) << endl;
    cout << "实际输出（优化版）：" << ladderLengthOptimized(beginWord1, endWord1, wordList1) << endl;
    
    // 测试用例2
    string beginWord2 = "hit";
    string endWord2 = "cog";
    vector<string> wordList2 = {"hot", "dot", "dog", "lot", "log"};
    cout << "\n测试用例2：" << endl;
    cout << "beginWord: " << beginWord2 << ", endWord: " << endWord2 << endl;
    cout << "wordList: [hot, dot, dog, lot, log]" << endl;
    cout << "期望输出：0" << endl; // endWord不在wordList中
    cout << "实际输出（普通版）：" << ladderLength(beginWord2, endWord2, wordList2) << endl;
    cout << "实际输出（优化版）：" << ladderLengthOptimized(beginWord2, endWord2, wordList2) << endl;
    
    // 测试用例3
    string beginWord3 = "a";
    string endWord3 = "c";
    vector<string> wordList3 = {"a", "b", "c"};
    cout << "\n测试用例3：" << endl;
    cout << "beginWord: " << beginWord3 << ", endWord: " << endWord3 << endl;
    cout << "wordList: [a, b, c]" << endl;
    cout << "期望输出：2" << endl; // a -> c
    cout << "实际输出（普通版）：" << ladderLength(beginWord3, endWord3, wordList3) << endl;
    cout << "实际输出（优化版）：" << ladderLengthOptimized(beginWord3, endWord3, wordList3) << endl;
    
    return 0;
}
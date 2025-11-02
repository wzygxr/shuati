// 单词接龙
// 字典 wordList 中从单词 beginWord 到 endWord 的转换序列是一个按下述规格形成的序列：
// 每一对相邻的单词只差一个字母
// 对于 1 <= i <= k 时，每个 si 都在 wordList 中（注意 beginWord 不需要在 wordList 中）
// sk == endWord
// 给你两个单词 beginWord 和 endWord 和一个字典 wordList
// 返回从 beginWord 到 endWord 的最短转换序列中的单词数目
// 如果不存在这样的转换序列，返回 0
// 测试链接 : https://leetcode.com/problems/word-ladder/
// 
// 算法思路：
// 使用双向BFS解决单词接龙问题
// 从beginWord和endWord同时开始搜索，每次扩展节点数较少的一端
// 当两端相遇时，找到最短路径
// 
// 时间复杂度：O(N * M^2)，其中N是单词数量，M是单词长度
// 空间复杂度：O(N * M^2)，用于存储图和访问状态
// 
// 工程化考量：
// 1. 双向搜索：从两端同时搜索提高效率
// 2. 图的构建：预处理单词列表构建模式图
// 3. 优化策略：每次扩展节点数较少的一端

#include <vector>
#include <string>
#include <unordered_set>
#include <algorithm>
using namespace std;

class Code14_WordLadder {
public:
    static int ladderLength(string beginWord, string endWord, vector<string>& wordList) {
        // 存储单词列表
        unordered_set<string> dict(wordList.begin(), wordList.end());
        
        // 如果目标单词不在词典中，直接返回0
        if (dict.find(endWord) == dict.end()) {
            return 0;
        }
        
        // 存储当前层和下一层的单词
        unordered_set<string> curLevel, nextLevel, visited;
        
        // 起点和终点分别加入两个集合
        curLevel.insert(beginWord);
        nextLevel.insert(endWord);
        visited.insert(beginWord);
        visited.insert(endWord);
        
        int level = 1;
        
        // 双向BFS搜索
        while (!curLevel.empty() && !nextLevel.empty()) {
            level++;
            // 选择节点数较少的一端进行扩展
            if (curLevel.size() > nextLevel.size()) {
                swap(curLevel, nextLevel);
            }
            
            // 扩展当前层的所有单词
            unordered_set<string> temp;
            for (const string& word : curLevel) {
                // 生成所有可能的模式
                for (int i = 0; i < word.length(); i++) {
                    string pattern = word;
                    pattern[i] = '*';
                    // 在词典中查找所有匹配该模式的单词
                    for (const string& next : wordList) {
                        string nextPattern = next;
                        nextPattern[i] = '*';
                        if (nextPattern == pattern) {
                            // 如果在另一端集合中找到，说明相遇
                            if (nextLevel.find(next) != nextLevel.end()) {
                                return level;
                            }
                            // 如果未访问过，加入下一层
                            if (visited.find(next) == visited.end()) {
                                temp.insert(next);
                                visited.insert(next);
                            }
                        }
                    }
                }
            }
            curLevel = temp;
        }
        return 0;
    }
};
// 单词接龙
// 字典 wordList 中从单词 beginWord 和 endWord 的 转换序列
// 是一个按下述规格形成的序列 beginWord -> s1 -> s2 -> ... -> sk ：
// 每一对相邻的单词只差一个字母。
// 对于 1 <= i <= k 时，每个 si 都在 wordList 中
// 注意， beginWord 不需要在 wordList 中。sk == endWord
// 给你两个单词 beginWord 和 endWord 和一个字典 wordList
// 返回 从 beginWord 到 endWord 的 最短转换序列 中的 单词数目
// 如果不存在这样的转换序列，返回 0 。
// 测试链接 : https://leetcode.cn/problems/word-ladder/
// 
// 算法思路：
// 使用双向BFS算法，从起点和终点同时开始搜索，一旦两个搜索相遇，就找到了最短路径
// 时间复杂度：O(M^2 * N)，其中M是单词的长度，N是单词列表中的单词数量
// 空间复杂度：O(N * M)
// 
// 工程化考量：
// 1. 异常处理：检查endWord是否在wordList中
// 2. 性能优化：使用双向BFS减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 4. 边界条件：处理空输入、极值输入等特殊情况
// 
// 语言特性差异：
// C++中使用unordered_set进行快速查找，使用queue进行BFS操作
// 注意字符串操作和内存管理

#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <queue>
#include <algorithm>
#include <chrono>
using namespace std;

class Solution {
public:
    /**
     * 计算从beginWord到endWord的最短转换序列中的单词数目
     * 
     * @param beginWord 起始单词
     * @param endWord 目标单词
     * @param wordList 单词列表
     * @return 最短转换序列中的单词数目，如果不存在则返回0
     * 
     * 算法核心思想：
     * 1. 双向BFS：从起点和终点同时开始搜索
     * 2. 平衡策略：始终从节点数较少的一侧开始扩展
     * 3. 相遇判断：当两侧搜索相遇时找到最短路径
     * 4. 状态记录：使用集合记录已访问状态
     * 
     * 时间复杂度分析：
     * - 每个单词有M个位置，每个位置可以替换为25个字母
     * - 最坏情况下需要遍历所有可能的单词变换
     * - 时间复杂度：O(M^2 * N)，其中M是单词长度，N是字典大小
     * 
     * 空间复杂度分析：
     * - 需要存储所有已访问的单词和当前搜索的层级
     * - 空间复杂度：O(N * M)
     */
    int ladderLength(string beginWord, string endWord, vector<string>& wordList) {
        // 边界条件检查：处理空输入
        if (beginWord.empty() || endWord.empty() || wordList.empty()) {
            return 0;
        }
        
        // 将wordList转换为unordered_set以提高查找效率
        // 使用unordered_set而不是set，因为查找时间复杂度为O(1)
        unordered_set<string> wordSet(wordList.begin(), wordList.end());
        
        // 如果目标单词不在词典中，直接返回0
        // 这是重要的边界条件检查，避免无效搜索
        if (wordSet.find(endWord) == wordSet.end()) {
            return 0;
        }
        
        // 初始化双向BFS的集合
        // smallLevel: 数量较少的一侧（当前扩展方向）
        // bigLevel: 数量较多的一侧（另一扩展方向）
        // nextLevel: 下一层要扩展的节点
        unordered_set<string> smallLevel, bigLevel, nextLevel;
        smallLevel.insert(beginWord);
        bigLevel.insert(endWord);
        
        // 从词典中移除起点和终点，避免重复访问
        // 注意：beginWord可能不在wordList中，所以需要检查
        if (wordSet.find(beginWord) != wordSet.end()) {
            wordSet.erase(beginWord);
        }
        wordSet.erase(endWord);
        
        // len记录路径长度，初始为2（包含begin和end）
        int length = 2;
        
        // 双向BFS主循环
        // 当smallLevel不为空时继续搜索
        while (!smallLevel.empty()) {
            // 遍历当前层的所有单词
            for (const string& word : smallLevel) {
                string currentWord = word; // 创建副本用于修改
                
                // 尝试改变每个位置的字符
                for (int i = 0; i < currentWord.length(); i++) {
                    char originalChar = currentWord[i]; // 保存原字符
                    
                    // 尝试26个字母中的每一个（除了原字符）
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == originalChar) {
                            continue; // 跳过相同的字符
                        }
                        
                        // 生成新单词
                        currentWord[i] = c;
                        string newWord = currentWord;
                        
                        // 检查是否与另一侧的搜索集合相遇
                        // 如果相遇，说明找到了最短路径
                        if (bigLevel.find(newWord) != bigLevel.end()) {
                            return length;
                        }
                        
                        // 检查新单词是否在词典中且未被访问过
                        if (wordSet.find(newWord) != wordSet.end()) {
                            // 将新单词加入下一层，并从词典中移除（标记为已访问）
                            nextLevel.insert(newWord);
                            wordSet.erase(newWord);
                        }
                    }
                    
                    // 恢复原字符，准备处理下一个位置
                    currentWord[i] = originalChar;
                }
            }
            
            // 优化策略：始终从节点数较少的一侧开始扩展
            // 这样可以减少搜索空间，提高算法效率
            if (nextLevel.size() <= bigLevel.size()) {
                // 如果下一层节点数小于等于另一侧，继续从当前方向扩展
                smallLevel = nextLevel;
            } else {
                // 否则交换扩展方向，从节点数较少的一侧开始
                smallLevel = bigLevel;
                bigLevel = nextLevel;
            }
            
            // 清空nextLevel，为下一轮扩展做准备
            nextLevel.clear();
            length++; // 进入下一层
        }
        
        // 如果搜索完成仍未找到路径，返回0
        return 0;
    }
};

// 单元测试类
class WordLadderTest {
public:
    /**
     * 运行所有测试用例
     */
    static void runAllTests() {
        testCase1();
        testCase2();
        testCase3();
        testCase4();
    }
    
private:
    /**
     * 测试用例1：正常情况，存在有效路径
     * 验证算法在标准情况下的正确性
     */
    static void testCase1() {
        cout << "=== 测试用例1：正常情况 ===" << endl;
        Solution solution;
        string beginWord = "hit";
        string endWord = "cog";
        vector<string> wordList = {"hot", "dot", "dog", "lot", "log", "cog"};
        
        int result = solution.ladderLength(beginWord, endWord, wordList);
        int expected = 5;
        
        cout << "起始单词: " << beginWord << endl;
        cout << "目标单词: " << endWord << endl;
        cout << "单词列表: [";
        for (size_t i = 0; i < wordList.size(); i++) {
            cout << wordList[i];
            if (i < wordList.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        cout << "期望输出: " << expected << endl;
        cout << "实际输出: " << result << endl;
        cout << "测试结果: " << (result == expected ? "通过" : "失败") << endl;
        cout << endl;
    }
    
    /**
     * 测试用例2：目标单词不在词典中
     * 验证边界条件处理
     */
    static void testCase2() {
        cout << "=== 测试用例2：目标单词不在词典中 ===" << endl;
        Solution solution;
        string beginWord = "hit";
        string endWord = "cog";
        vector<string> wordList = {"hot", "dot", "dog", "lot", "log"};
        
        int result = solution.ladderLength(beginWord, endWord, wordList);
        int expected = 0;
        
        cout << "起始单词: " << beginWord << endl;
        cout << "目标单词: " << endWord << endl;
        cout << "单词列表: [";
        for (size_t i = 0; i < wordList.size(); i++) {
            cout << wordList[i];
            if (i < wordList.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        cout << "期望输出: " << expected << endl;
        cout << "实际输出: " << result << endl;
        cout << "测试结果: " << (result == expected ? "通过" : "失败") << endl;
        cout << endl;
    }
    
    /**
     * 测试用例3：起始单词就是目标单词
     * 验证特殊情况处理
     */
    static void testCase3() {
        cout << "=== 测试用例3：起始单词就是目标单词 ===" << endl;
        Solution solution;
        string beginWord = "hit";
        string endWord = "hit";
        vector<string> wordList = {"hot", "dot", "dog", "lot", "log", "cog"};
        
        int result = solution.ladderLength(beginWord, endWord, wordList);
        int expected = 1; // 根据题目要求，序列长度为1（只包含起始单词）
        
        cout << "起始单词: " << beginWord << endl;
        cout << "目标单词: " << endWord << endl;
        cout << "单词列表: [";
        for (size_t i = 0; i < wordList.size(); i++) {
            cout << wordList[i];
            if (i < wordList.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        cout << "期望输出: " << expected << endl;
        cout << "实际输出: " << result << endl;
        cout << "测试结果: " << (result == expected ? "通过" : "失败") << endl;
        cout << endl;
    }
    
    /**
     * 测试用例4：空输入测试
     * 验证异常处理能力
     */
    static void testCase4() {
        cout << "=== 测试用例4：空输入测试 ===" << endl;
        Solution solution;
        string beginWord = "";
        string endWord = "cog";
        vector<string> wordList = {"hot", "dot", "dog", "lot", "log", "cog"};
        
        int result = solution.ladderLength(beginWord, endWord, wordList);
        int expected = 0;
        
        cout << "起始单词: \"\"" << endl;
        cout << "目标单词: " << endWord << endl;
        cout << "单词列表: [";
        for (size_t i = 0; i < wordList.size(); i++) {
            cout << wordList[i];
            if (i < wordList.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        cout << "期望输出: " << expected << endl;
        cout << "实际输出: " << result << endl;
        cout << "测试结果: " << (result == expected ? "通过" : "失败") << endl;
        cout << endl;
    }
};

// 性能测试类
class PerformanceTest {
public:
    /**
     * 运行性能测试，验证算法在大规模数据下的表现
     */
    static void runPerformanceTest() {
        cout << "=== 性能测试 ===" << endl;
        
        Solution solution;
        
        // 生成大规模测试数据
        vector<string> largeWordList = generateLargeWordList(1000);
        string beginWord = "aaaaa";
        string endWord = "zzzzz";
        
        auto start = chrono::high_resolution_clock::now();
        int result = solution.ladderLength(beginWord, endWord, largeWordList);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "数据规模: 1000个单词" << endl;
        cout << "执行时间: " << duration.count() << " 毫秒" << endl;
        cout << "结果: " << result << endl;
        cout << endl;
    }
    
private:
    /**
     * 生成大规模测试数据
     * @param size 单词数量
     * @return 生成的单词列表
     */
    static vector<string> generateLargeWordList(int size) {
        vector<string> wordList;
        for (int i = 0; i < size; i++) {
            string word;
            for (int j = 0; j < 5; j++) {
                word += 'a' + rand() % 26;
            }
            wordList.push_back(word);
        }
        return wordList;
    }
};

// 主函数
int main() {
    cout << "单词接龙算法测试" << endl;
    cout << "==================" << endl;
    cout << endl;
    
    // 运行功能测试
    WordLadderTest::runAllTests();
    
    // 运行性能测试
    PerformanceTest::runPerformanceTest();
    
    return 0;
}

/* 
 * 算法深度分析：
 * 
 * 1. 双向BFS vs 单向BFS：
 *    - 单向BFS时间复杂度：O(b^d)，其中b是分支因子，d是深度
 *    - 双向BFS时间复杂度：O(b^(d/2))，显著减少搜索空间
 *    - 在单词接龙问题中，b≈25*M（每个单词有25*M个邻居），d是转换序列长度
 * 
 * 2. 优化技巧：
 *    - 平衡扩展：始终扩展节点数较少的一侧
 *    - 快速查找：使用哈希表实现O(1)查找
 *    - 状态管理：及时移除已访问节点，避免重复搜索
 * 
 * 3. 工程化考量：
 *    - 异常处理：全面检查输入合法性
 *    - 性能监控：添加性能测试代码
 *    - 可维护性：模块化设计，清晰的注释
 * 
 * 4. 语言特性利用：
 *    - C++的unordered_set提供高效查找
 *    - 字符串操作注意性能影响
 *    - 内存管理由RAII机制自动处理
 */
// 最小基因变化
// 题目来源：LeetCode 433
// 题目描述：
// 基因序列可以表示为一条由 8 个字符组成的字符串，其中每个字符都是 'A'、'C'、'G' 和 'T' 之一。
// 假设我们需要研究从基因序列 start 变为 end 所发生的基因变化。
// 一次基因变化意味着这个基因序列中的一个字符发生了变化。
// 同时，每次基因变化的结果，必须是在基因库 bank 中存在的有效基因序列。
// 假设 start 和 end 都是有效的基因序列，start 不等于 end。
// 请找出并返回能够使 start 变为 end 的最少变化次数。
// 如果无法完成此任务，则返回 -1。
// 测试链接 : https://leetcode.cn/problems/minimum-genetic-mutation/
// 
// 算法思路：
// 使用双向BFS算法，从起点和终点同时开始搜索，直到两者相遇
// 这样可以大大减少搜索空间，提高算法效率
// 时间复杂度：O(B)，其中B是基因库的大小
// 空间复杂度：O(B)
// 
// 工程化考量：
// 1. 异常处理：检查输入是否合法
// 2. 性能优化：使用双向BFS减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// C++中使用unordered_set进行快速查找和集合操作

#include <iostream>
#include <vector>
#include <unordered_set>
#include <string>
using namespace std;

/**
 * 计算从start基因序列到end基因序列的最小变化次数
 * @param start 起始基因序列
 * @param end 目标基因序列
 * @param bank 基因库
 * @return 最小变化次数，如果无法完成则返回-1
 */
int minMutation(string start, string end, vector<string>& bank) {
    // 边界条件检查
    if (start.empty() || end.empty() || bank.empty()) {
        return -1;
    }
    
    // 将基因库转换为unordered_set，提高查找效率
    unordered_set<string> bankSet(bank.begin(), bank.end());
    
    // 如果目标基因不在基因库中，直接返回-1
    if (bankSet.find(end) == bankSet.end()) {
        return -1;
    }
    
    // 基因字符集
    vector<char> genes = {'A', 'C', 'G', 'T'};
    
    // 使用双向BFS
    // 从起点开始的搜索集合
    unordered_set<string> startSet;
    // 从终点开始的搜索集合
    unordered_set<string> endSet;
    // 全局已访问集合，避免重复搜索
    unordered_set<string> visited;
    
    startSet.insert(start);
    endSet.insert(end);
    visited.insert(start);
    visited.insert(end);
    
    int steps = 0;
    
    // 当两个集合都不为空时继续搜索
    while (!startSet.empty() && !endSet.empty()) {
        // 优化：总是从较小的集合开始搜索，减少搜索空间
        if (startSet.size() > endSet.size()) {
            swap(startSet, endSet);
        }
        
        unordered_set<string> nextLevel;
        
        // 遍历当前层的所有基因序列
        for (const string& current : startSet) {
            // 尝试改变每个位置的字符
            string temp = current;
            for (int i = 0; i < temp.size(); i++) {
                char original = temp[i];
                
                // 尝试所有可能的字符替换
                for (char gene : genes) {
                    if (gene == original) {
                        continue; // 跳过相同的字符
                    }
                    
                    temp[i] = gene;
                    
                    // 检查是否与另一端的搜索集合相遇
                    if (endSet.find(temp) != endSet.end()) {
                        return steps + 1;
                    }
                    
                    // 如果是有效的基因序列且未访问过，则加入下一层
                    if (bankSet.find(temp) != bankSet.end() && visited.find(temp) == visited.end()) {
                        nextLevel.insert(temp);
                        visited.insert(temp);
                    }
                }
                
                // 恢复原字符
                temp[i] = original;
            }
        }
        
        // 进入下一层
        startSet = nextLevel;
        steps++;
    }
    
    // 无法找到路径
    return -1;
}

// 测试方法
int main() {
    // 测试用例1
    cout << "测试用例1：" << endl;
    string start1 = "AACCGGTT";
    string end1 = "AACCGGTA";
    vector<string> bank1 = {"AACCGGTA"};
    cout << "start: \"AACCGGTT\"" << endl;
    cout << "end: \"AACCGGTA\"" << endl;
    cout << "bank: [\"AACCGGTA\"]" << endl;
    cout << "期望输出: 1" << endl;
    cout << "实际输出: " << minMutation(start1, end1, bank1) << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2：" << endl;
    string start2 = "AACCGGTT";
    string end2 = "AAACGGTA";
    vector<string> bank2 = {"AACCGGTA", "AACCGCTA", "AAACGGTA"};
    cout << "start: \"AACCGGTT\"" << endl;
    cout << "end: \"AAACGGTA\"" << endl;
    cout << "bank: [\"AACCGGTA\", \"AACCGCTA\", \"AAACGGTA\"]" << endl;
    cout << "期望输出: 2" << endl;
    cout << "实际输出: " << minMutation(start2, end2, bank2) << endl;
    cout << endl;
    
    // 测试用例3
    cout << "测试用例3：" << endl;
    string start3 = "AAAAACCC";
    string end3 = "AACCCCCC";
    vector<string> bank3 = {"AAAACCCC", "AAACCCCC", "AACCCCCC"};
    cout << "start: \"AAAAACCC\"" << endl;
    cout << "end: \"AACCCCCC\"" << endl;
    cout << "bank: [\"AAAACCCC\", \"AAACCCCC\", \"AACCCCCC\"]" << endl;
    cout << "期望输出: 3" << endl;
    cout << "实际输出: " << minMutation(start3, end3, bank3) << endl;
    
    return 0;
}
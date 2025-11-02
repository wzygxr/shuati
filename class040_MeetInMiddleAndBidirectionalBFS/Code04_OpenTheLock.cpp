// 打开转盘锁
// 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。
// 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。
// 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
// 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
// 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1 。
// 测试链接 : https://leetcode.cn/problems/open-the-lock/
// 
// 算法思路：
// 使用双向BFS算法，从起点"0000"和终点target同时开始搜索，一旦两个搜索相遇，就找到了最短路径
// 时间复杂度：O(10^4 * 8 + D)，其中D是deadends的长度，10^4是状态数，8是每个状态的邻居数
// 空间复杂度：O(10^4 + D)
// 
// 工程化考量：
// 1. 异常处理：检查初始状态是否在deadends中
// 2. 性能优化：使用双向BFS减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// C++中使用unordered_set进行快速查找和去重，使用queue进行BFS操作

#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <queue>
#include <algorithm>
using namespace std;

/**
 * 生成当前状态的所有邻居状态
 * @param s 当前状态
 * @return 邻居状态列表
 */
vector<string> getNeighbors(string s) {
    vector<string> neighbors;
    
    // 对每个位置尝试向上和向下旋转
    for (int i = 0; i < 4; i++) {
        char original = s[i];
        
        // 向上旋转
        s[i] = (char) ((original - '0' + 1) % 10 + '0');
        neighbors.push_back(s);
        
        // 向下旋转
        s[i] = (char) ((original - '0' + 9) % 10 + '0');
        neighbors.push_back(s);
        
        // 恢复原字符
        s[i] = original;
    }
    
    return neighbors;
}

/**
 * 计算打开转盘锁所需的最小旋转次数
 * @param deadends 死亡数字列表
 * @param target 目标数字
 * @return 最小旋转次数，如果无法解锁返回-1
 */
int openLock(vector<string>& deadends, string target) {
    // 将deadends转换为unordered_set以提高查找效率
    unordered_set<string> deadSet(deadends.begin(), deadends.end());
    
    // 如果初始状态"0000"在deadends中，直接返回-1
    if (deadSet.count("0000")) {
        return -1;
    }
    
    // 如果目标就是初始状态，返回0
    if (target == "0000") {
        return 0;
    }
    
    // 初始化双向BFS的队列和访问集合
    queue<string> queue1, queue2;  // 从起点和终点开始的队列
    unordered_set<string> visited1, visited2;  // 从起点和终点开始的访问集合
    
    queue1.push("0000");
    queue2.push(target);
    visited1.insert("0000");
    visited2.insert(target);
    
    int steps = 0;
    
    // 双向BFS
    while (!queue1.empty() && !queue2.empty()) {
        // 总是从较小的队列开始扩展，优化性能
        if (queue1.size() > queue2.size()) {
            swap(queue1, queue2);
            swap(visited1, visited2);
        }
        
        steps++;
        int size = queue1.size();
        
        // 扩展当前层的所有节点
        for (int i = 0; i < size; i++) {
            string current = queue1.front();
            queue1.pop();
            
            // 生成当前状态的所有邻居状态
            for (string next : getNeighbors(current)) {
                // 如果邻居状态在deadends中，跳过
                if (deadSet.count(next)) {
                    continue;
                }
                
                // 如果邻居状态已经被访问过，跳过
                if (visited1.count(next)) {
                    continue;
                }
                
                // 如果邻居状态在另一侧的访问集合中，说明两路相遇，返回步数
                if (visited2.count(next)) {
                    return steps;
                }
                
                // 将邻居状态加入队列和访问集合
                queue1.push(next);
                visited1.insert(next);
            }
        }
    }
    
    return -1; // 无法解锁
}

// 测试方法
int main() {
    // 测试用例1
    vector<string> deadends1 = {"0201", "0101", "0102", "1212", "2002"};
    string target1 = "0202";
    cout << "测试用例1:" << endl;
    cout << "deadends: [\"0201\",\"0101\",\"0102\",\"1212\",\"2002\"]" << endl;
    cout << "target: \"0202\"" << endl;
    cout << "期望输出: 6" << endl;
    cout << "实际输出: " << openLock(deadends1, target1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<string> deadends2 = {"8888"};
    string target2 = "0009";
    cout << "测试用例2:" << endl;
    cout << "deadends: [\"8888\"]" << endl;
    cout << "target: \"0009\"" << endl;
    cout << "期望输出: 1" << endl;
    cout << "实际输出: " << openLock(deadends2, target2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<string> deadends3 = {"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"};
    string target3 = "8888";
    cout << "测试用例3:" << endl;
    cout << "deadends: [\"8887\",\"8889\",\"8878\",\"8898\",\"8788\",\"8988\",\"7888\",\"9888\"]" << endl;
    cout << "target: \"8888\"" << endl;
    cout << "期望输出: -1" << endl;
    cout << "实际输出: " << openLock(deadends3, target3) << endl;
    
    return 0;
}
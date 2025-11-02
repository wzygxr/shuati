#include <iostream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <string>
#include <algorithm>
using namespace std;

// 打开转盘锁
// 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'。
// 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9'。每次旋转都只能旋转一个拨轮的一个数字。
// 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
// 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
// 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1。
// 测试链接 : https://leetcode.cn/problems/open-the-lock/
// 
// 算法思路：
// 使用BFS搜索从"0000"到target的最短路径。每个状态可以旋转8个方向（每个拨轮可以向上或向下旋转）。
// 使用哈希集合记录死亡数字和已访问状态，避免重复访问。
// 
// 时间复杂度：O(10^4 * 8) = O(80000)，因为有10000个可能的状态，每个状态有8个邻居
// 空间复杂度：O(10000)，用于存储队列和访问状态
// 
// 工程化考量：
// 1. 使用unordered_set提高查找效率
// 2. 字符串操作优化
// 3. 边界情况处理
class Solution {
public:
    int openLock(vector<string>& deadends, string target) {
        unordered_set<string> deadSet(deadends.begin(), deadends.end());
        string start = "0000";
        
        // 边界情况：初始状态就是死亡数字
        if (deadSet.count(start)) {
            return -1;
        }
        
        // 边界情况：初始状态就是目标状态
        if (start == target) {
            return 0;
        }
        
        queue<string> q;
        unordered_set<string> visited;
        
        q.push(start);
        visited.insert(start);
        int steps = 0;
        
        while (!q.empty()) {
            steps++;
            int size = q.size();
            
            // 处理当前层的所有状态
            for (int i = 0; i < size; i++) {
                string current = q.front();
                q.pop();
                
                // 生成所有可能的邻居状态
                for (string neighbor : getNeighbors(current)) {
                    // 跳过死亡数字和已访问状态
                    if (deadSet.count(neighbor) || visited.count(neighbor)) {
                        continue;
                    }
                    
                    // 如果找到目标状态
                    if (neighbor == target) {
                        return steps;
                    }
                    
                    // 加入队列并标记为已访问
                    visited.insert(neighbor);
                    q.push(neighbor);
                }
            }
        }
        
        return -1;
    }
    
private:
    vector<string> getNeighbors(const string& current) {
        vector<string> neighbors;
        
        // 对每个位置进行向上和向下旋转
        for (int i = 0; i < 4; i++) {
            string next = current;
            
            // 向上旋转（数字增加）
            next[i] = (current[i] - '0' + 1) % 10 + '0';
            neighbors.push_back(next);
            
            // 向下旋转（数字减少）
            next[i] = (current[i] - '0' + 9) % 10 + '0';
            neighbors.push_back(next);
        }
        
        return neighbors;
    }
};

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1：标准情况
    vector<string> deadends1 = {"0201","0101","0102","1212","2002"};
    string target1 = "0202";
    cout << "测试用例1 - 最小步数: " << solution.openLock(deadends1, target1) << endl; // 期望输出: 6
    
    // 测试用例2：无法解锁
    vector<string> deadends2 = {"8888"};
    string target2 = "0009";
    cout << "测试用例2 - 最小步数: " << solution.openLock(deadends2, target2) << endl; // 期望输出: 1
    
    // 测试用例3：初始状态就是死亡数字
    vector<string> deadends3 = {"0000"};
    string target3 = "8888";
    cout << "测试用例3 - 最小步数: " << solution.openLock(deadends3, target3) << endl; // 期望输出: -1
    
    // 测试用例4：初始状态就是目标状态
    vector<string> deadends4 = {"8888","9999"};
    string target4 = "0000";
    cout << "测试用例4 - 最小步数: " << solution.openLock(deadends4, target4) << endl; // 期望输出: 0
    
    // 测试用例5：复杂情况
    vector<string> deadends5 = {"1000","0100","0010","0001","9000","0900","0090","0009"};
    string target5 = "0002";
    cout << "测试用例5 - 最小步数: " << solution.openLock(deadends5, target5) << endl; // 期望输出: 2
    
    return 0;
}
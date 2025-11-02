#include <iostream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <string>
using namespace std;

/**
 * LeetCode 773 - 滑动谜题
 * 题目描述：
 * 在一个 2x3 的板上（board）有 5 个砖块，以及一个空的格子。
 * 一次移动定义为选择空的格子与一个相邻的数字（上下左右）进行交换。
 * 最后当板 board 的结果是 [[1,2,3],[4,5,0]] 时，返回最少的移动次数；如果不存在这样的结果，返回 -1。
 * 
 * 算法：01-BFS算法
 * 时间复杂度：O(6! * 4) = O(720 * 4) = O(2880)，因为2x3的板共有6个位置，所以状态总数为6!
 * 空间复杂度：O(6!) = O(720)
 */

class Solution {
private:
    // 目标状态
    const string TARGET = "123450";
    // 记录每个位置可以移动到的位置（0-5对应board中的每个位置）
    const vector<vector<int>> DIRECTIONS = {
        {1, 3},       // 位置0可以移动到位置1和3
        {0, 2, 4},    // 位置1可以移动到位置0、2和4
        {1, 5},       // 位置2可以移动到位置1和5
        {0, 4},       // 位置3可以移动到位置0和4
        {1, 3, 5},    // 位置4可以移动到位置1、3和5
        {2, 4}        // 位置5可以移动到位置2和4
    };
    
    /**
     * 交换字符串中两个字符的位置
     * @param s 原始字符串
     * @param i 第一个位置
     * @param j 第二个位置
     * @return 交换后的新字符串
     */
    string swapChars(string s, int i, int j) {
        char temp = s[i];
        s[i] = s[j];
        s[j] = temp;
        return s;
    }

public:
    /**
     * 解决滑动谜题问题的主函数
     * @param board 2x3的棋盘
     * @return 最少的移动次数，如果无解则返回-1
     */
    int slidingPuzzle(vector<vector<int>>& board) {
        // 将棋盘转换为字符串表示
        string start = "";
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                start += to_string(board[i][j]);
            }
        }
        
        // 如果初始状态就是目标状态，直接返回0
        if (TARGET == start) {
            return 0;
        }
        
        // 使用队列实现BFS
        queue<string> q;
        // 记录已经访问过的状态，避免重复访问
        unordered_set<string> visited;
        
        q.push(start);
        visited.insert(start);
        
        int steps = 0;
        
        while (!q.empty()) {
            int size = q.size();
            steps++;
            
            // 处理当前层的所有状态
            for (int i = 0; i < size; i++) {
                string current = q.front();
                q.pop();
                
                // 找到空格（0）的位置
                size_t zeroPos = current.find('0');
                
                // 尝试所有可能的移动方向
                for (int dir : DIRECTIONS[zeroPos]) {
                    // 生成新的状态
                    string next = swapChars(current, zeroPos, dir);
                    
                    // 如果是目标状态，返回步数
                    if (TARGET == next) {
                        return steps;
                    }
                    
                    // 如果是新状态，加入队列
                    if (visited.find(next) == visited.end()) {
                        visited.insert(next);
                        q.push(next);
                    }
                }
            }
        }
        
        // 如果无法到达目标状态，返回-1
        return -1;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> board1 = {{1, 2, 3}, {4, 0, 5}};
    cout << "测试用例1结果: " << solution.slidingPuzzle(board1) << endl; // 预期输出: 1
    
    // 测试用例2
    vector<vector<int>> board2 = {{1, 2, 3}, {5, 4, 0}};
    cout << "测试用例2结果: " << solution.slidingPuzzle(board2) << endl; // 预期输出: -1
    
    // 测试用例3
    vector<vector<int>> board3 = {{4, 1, 2}, {5, 0, 3}};
    cout << "测试用例3结果: " << solution.slidingPuzzle(board3) << endl; // 预期输出: 5
    
    return 0;
}
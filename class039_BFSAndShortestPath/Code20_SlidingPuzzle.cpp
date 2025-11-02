#include <iostream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <string>
#include <algorithm>
using namespace std;

// 滑动谜题
// 在一个 2 x 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示, 以及一块空缺用 0 来表示。
// 一次移动定义为选择 0 与一个相邻的数字（上下左右）进行交换。
// 最终当板 board 的结果是 [[1,2,3],[4,5,0]] 谜板被解开。
// 给出一个谜板的初始状态，返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1 。
// 测试链接 : https://leetcode.cn/problems/sliding-puzzle/
// 
// 算法思路：
// 使用BFS搜索从初始状态到目标状态的最短路径。将2x3的板状态表示为字符串进行状态搜索。
// 每个状态可以生成最多4个邻居状态（0可以向4个方向移动）。
// 
// 时间复杂度：O(6! * 4) = O(2880)，因为有6! = 720种可能的状态，每个状态最多有4个邻居
// 空间复杂度：O(720)，用于存储队列和访问状态
// 
// 工程化考量：
// 1. 状态表示：将2x3矩阵转换为字符串进行状态搜索
// 2. 邻居生成：根据0的位置生成可能的移动方向
// 3. 预计算移动方向：提高代码可读性和性能
// 4. 边界情况：初始状态就是目标状态
class Solution {
public:
    int slidingPuzzle(vector<vector<int>>& board) {
        string target = "123450";
        string start = boardToString(board);
        
        // 边界情况：初始状态就是目标状态
        if (start == target) {
            return 0;
        }
        
        // 预计算每个位置可以移动到的邻居位置
        vector<vector<int>> neighbors = {
            {1, 3},     // 位置0的邻居：1, 3
            {0, 2, 4},  // 位置1的邻居：0, 2, 4
            {1, 5},     // 位置2的邻居：1, 5
            {0, 4},     // 位置3的邻居：0, 4
            {1, 3, 5},  // 位置4的邻居：1, 3, 5
            {2, 4}      // 位置5的邻居：2, 4
        };
        
        queue<string> q;
        unordered_set<string> visited;
        
        q.push(start);
        visited.insert(start);
        int steps = 0;
        
        while (!q.empty()) {
            steps++;
            int size = q.size();
            
            for (int i = 0; i < size; i++) {
                string current = q.front();
                q.pop();
                
                // 找到0的位置
                int zeroPos = current.find('0');
                
                // 生成所有可能的邻居状态
                for (int neighborPos : neighbors[zeroPos]) {
                    string next = current;
                    // 交换0和邻居位置
                    swap(next[zeroPos], next[neighborPos]);
                    
                    if (visited.count(next)) {
                        continue;
                    }
                    
                    if (next == target) {
                        return steps;
                    }
                    
                    visited.insert(next);
                    q.push(next);
                }
            }
        }
        
        return -1;
    }
    
private:
    string boardToString(const vector<vector<int>>& board) {
        string result;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                result += to_string(board[i][j]);
            }
        }
        return result;
    }
};

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1：标准情况
    vector<vector<int>> board1 = {{1,2,3},{4,0,5}};
    cout << "测试用例1 - 最小步数: " << solution.slidingPuzzle(board1) << endl; // 期望输出: 1
    
    // 测试用例2：需要多步
    vector<vector<int>> board2 = {{1,2,3},{5,4,0}};
    cout << "测试用例2 - 最小步数: " << solution.slidingPuzzle(board2) << endl; // 期望输出: -1
    
    // 测试用例3：初始状态就是目标状态
    vector<vector<int>> board3 = {{1,2,3},{4,5,0}};
    cout << "测试用例3 - 最小步数: " << solution.slidingPuzzle(board3) << endl; // 期望输出: 0
    
    // 测试用例4：复杂情况
    vector<vector<int>> board4 = {{4,1,2},{5,0,3}};
    cout << "测试用例4 - 最小步数: " << solution.slidingPuzzle(board4) << endl; // 期望输出: 5
    
    return 0;
}
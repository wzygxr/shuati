// 最短路径（二进制矩阵）
// 给你一个 n x n 的二进制矩阵 grid ，返回矩阵中最短 畅通路径 的长度
// 如果不存在这样的路径，返回 -1
// 二进制矩阵中的 畅通路径 是一条从 左上角 单元格（即，(0, 0)）到 右下角 单元格（即，(n - 1, n - 1)）的路径
// 该路径同时满足以下要求：
// 路径途经的所有单元格的值都是 0
// 路径中所有相邻的单元格应当在 8 个方向之一 上连通（即，相邻两单元之间彼此不同且共享一条边或者一个角）
// 畅通路径的长度 是该路径途经的单元格总数
// 测试链接 : https://leetcode.com/problems/shortest-path-in-binary-matrix/
// 
// 算法思路：
// 使用标准BFS解决最短路径问题
// 由于是8方向连通，需要考虑8个方向的移动
// 从起点(0,0)开始BFS搜索，直到到达终点(n-1,n-1)
// 
// 时间复杂度：O(n^2)，其中n是矩阵的边长，每个单元格最多被访问一次
// 空间复杂度：O(n^2)，用于存储队列和访问状态
// 
// 工程化考量：
// 1. 边界检查：确保移动后的位置在矩阵范围内
// 2. 特殊情况处理：起点或终点为1时直接返回-1
// 3. 8方向移动：需要考虑8个方向而不是4个方向

#include <vector>
#include <queue>
#include <utility>
using namespace std;

class Code09_ShortestPathInBinaryMatrix {
public:
    // 8个方向的移动：上、右上、右、右下、下、左下、左、左上
    static const int move[10];
    
    static int shortestPathBinaryMatrix(vector<vector<int>>& grid) {
        int n = grid.size();
        // 特殊情况：起点或终点为1
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
            return -1;
        }
        // 特殊情况：只有一个单元格
        if (n == 1) {
            return 1;
        }
        
        // 访问状态数组
        vector<vector<bool>> visited(n, vector<bool>(n, false));
        
        // 队列用于BFS
        queue<pair<int, int> > q;
        
        // 起点入队
        visited[0][0] = true;
        q.push(make_pair(0, 0));
        int level = 1;
        
        // BFS搜索
        while (!q.empty()) {
            level++;
            int size = q.size();
            // 处理当前层的所有节点
            for (int k = 0; k < size; k++) {
                pair<int, int> cur = q.front();
                int x = cur.first;
                int y = cur.second;
                q.pop();
                
                // 向8个方向扩展
                for (int i = 0; i < 8; i++) {
                    int nx = x + move[i];
                    int ny = y + move[i + 1];
                    
                    // 检查边界、是否已访问、是否为畅通路径
                    if (nx >= 0 && nx < n && ny >= 0 && ny < n && 
                        !visited[nx][ny] && grid[nx][ny] == 0) {
                        // 如果到达终点
                        if (nx == n - 1 && ny == n - 1) {
                            return level;
                        }
                        visited[nx][ny] = true;
                        q.push(make_pair(nx, ny));
                    }
                }
            }
        }
        return -1;
    }
};

// 8个方向的移动：上、右上、右、右下、下、左下、左、左上
const int Code09_ShortestPathInBinaryMatrix::move[10] = { -1, -1, 0, 1, 1, 1, 0, -1, -1, -1 };
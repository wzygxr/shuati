// 腐烂的橘子
// 在给定的 m x n 网格 grid 中，每个单元格可以有以下三个值之一：
// 值 0 代表空单元格；
// 值 1 代表新鲜橘子；
// 值 2 代表腐烂的橘子。
// 每分钟，腐烂的橘子 四个方向上相邻 的新鲜橘子都会腐烂。
// 返回直到单元格中没有新鲜橘子为止所必须经过的最小分钟数
// 如果不可能，返回 -1
// 测试链接 : https://leetcode.com/problems/rotting-oranges/
// 
// 算法思路：
// 使用多源BFS解决腐烂过程模拟问题
// 初始时将所有腐烂的橘子加入队列，作为BFS的起始点
// 每一轮BFS代表一分钟，将相邻的新鲜橘子腐烂并加入队列
// 最后检查是否还有新鲜橘子未被腐烂
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数，每个单元格最多被访问一次
// 空间复杂度：O(m * n)，用于存储队列
// 
// 工程化考量：
// 1. 特殊情况处理：初始时就没有新鲜橘子直接返回0
// 2. 边界检查：确保移动后的位置在网格范围内
// 3. 结果验证：最后检查是否所有新鲜橘子都被腐烂

#include <vector>
#include <queue>
using namespace std;

class Code10_RottingOranges {
public:
    // 四个方向的移动：上、右、下、左
    static const int move[5];
    
    static int orangesRotting(vector<vector<int>>& grid) {
        int n = grid.size();
        int m = grid[0].size();
        
        // 队列用于BFS
        queue<pair<int, int>> q;
        int fresh = 0; // 新鲜橘子数量
        
        // 初始化队列，将所有腐烂的橘子加入队列
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 2) {
                    q.push(pair<int, int>(i, j));
                } else if (grid[i][j] == 1) {
                    fresh++;
                }
            }
        }
        
        // 特殊情况：没有新鲜橘子
        if (fresh == 0) {
            return 0;
        }
        
        int minutes = 0;
        // 多源BFS模拟腐烂过程
        while (!q.empty()) {
            minutes++;
            int size = q.size();
            // 处理当前层的所有腐烂橘子
            for (int k = 0; k < size; k++) {
                pair<int, int> cur = q.front();
                int x = cur.first;
                int y = cur.second;
                q.pop();
                
                // 向四个方向扩展
                for (int i = 0; i < 4; i++) {
                    int nx = x + move[i];
                    int ny = y + move[i + 1];
                    // 检查边界和是否为新鲜橘子
                    if (nx >= 0 && nx < n && ny >= 0 && ny < m && grid[nx][ny] == 1) {
                        grid[nx][ny] = 2; // 腐烂
                        fresh--; // 新鲜橘子减少
                        q.push(pair<int, int>(nx, ny));
                    }
                }
            }
        }
        
        // 如果还有新鲜橘子未被腐烂，返回-1
        return fresh == 0 ? minutes - 1 : -1;
    }
};

// 四个方向的移动：上、右、下、左
const int Code10_RottingOranges::move[5] = { -1, 0, 1, 0, -1 };
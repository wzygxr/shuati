// 墙与门
// 你被给定一个 m × n 的二维网格 rooms ，网格中有以下三种可能的初始化值：
// -1 表示墙或是障碍物
// 0 表示一扇门
// INF 无限表示一个空的房间。然后，我们用 2^31 - 1 = 2147483647 代表 INF
// 请你给每个空房间填上该房间到 最近门的距离 ，如果无法到达门，则填 INF
// 测试链接 : https://leetcode.com/problems/walls-and-gates/
// 
// 算法思路：
// 使用多源BFS解决距离填充问题
// 初始时将所有门（值为0的单元格）加入队列，作为BFS的起始点
// 从门开始向外扩展，每一轮BFS代表距离增加1
// 将空房间（值为INF）更新为其到最近门的距离
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数，每个单元格最多被访问一次
// 空间复杂度：O(m * n)，用于存储队列
// 
// 工程化考量：
// 1. 特殊值处理：正确处理墙(-1)、门(0)、空房间(INF)
// 2. 边界检查：确保移动后的位置在网格范围内
// 3. 距离更新：只更新空房间的距离值

#include <vector>
#include <queue>
using namespace std;

class Code11_WallsAndGates {
public:
    // 四个方向的移动：上、右、下、左
    static const int move[5];
    static const int INF;
    
    static void wallsAndGates(vector<vector<int>>& rooms) {
        if (rooms.empty() || rooms[0].empty()) {
            return;
        }
        
        int n = rooms.size();
        int m = rooms[0].size();
        
        // 队列用于BFS
        queue<pair<int, int> > q;
        
        // 初始化队列，将所有门加入队列
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (rooms[i][j] == 0) {
                    q.push(make_pair(i, j));
                }
            }
        }
        
        int distance = 0;
        // 多源BFS填充距离
        while (!q.empty()) {
            distance++;
            int size = q.size();
            // 处理当前层的所有节点
            for (int k = 0; k < size; k++) {
                pair<int, int> cur = q.front();
                int x = cur.first;
                int y = cur.second;
                q.pop();
                
                // 向四个方向扩展
                for (int i = 0; i < 4; i++) {
                    int nx = x + move[i];
                    int ny = y + move[i + 1];
                    // 检查边界和是否为空房间
                    if (nx >= 0 && nx < n && ny >= 0 && ny < m && rooms[nx][ny] == INF) {
                        rooms[nx][ny] = distance;
                        q.push(make_pair(nx, ny));
                    }
                }
            }
        }
    }
};

// 四个方向的移动：上、右、下、左
const int Code11_WallsAndGates::move[5] = { -1, 0, 1, 0, -1 };
const int Code11_WallsAndGates::INF = 2147483647;
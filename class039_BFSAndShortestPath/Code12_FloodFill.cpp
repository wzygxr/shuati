// 图像渲染（洪水填充）
// 有一幅以 m x n 二维整数数组表示的图画 image ，其中 image[i][j] 表示该图画的像素值大小
// 你也被给予三个整数 sr , sc 和 newColor 。你应该从像素 image[sr][sc] 开始对图像进行上色填充
// 为了完成上色工作，从初始像素开始，记录初始坐标的上下左右四个方向上像素值与初始坐标相同的相连像素点
// 接着再记录这四个方向上符合条件的像素点与他们对应的四个方向上像素值与初始坐标相同的相连像素点，……，重复该过程
// 将所有有记录的像素点的颜色值改为 newColor
// 最后返回经过上色渲染后的图像
// 测试链接 : https://leetcode.com/problems/flood-fill/
// 
// 算法思路：
// 使用标准BFS解决图像填充问题
// 从起始点(sr, sc)开始，将所有与起始点像素值相同且相连的像素点颜色改为newColor
// 使用BFS遍历所有相连的像素点
// 
// 时间复杂度：O(m * n)，其中m和n分别是图像的行数和列数，最坏情况下需要访问所有像素点
// 空间复杂度：O(m * n)，用于存储队列
// 
// 工程化考量：
// 1. 特殊情况处理：新颜色与原颜色相同时直接返回原图像
// 2. 边界检查：确保移动后的位置在图像范围内
// 3. 连通性判断：只处理与起始点像素值相同的像素点

#include <vector>
#include <queue>
using namespace std;

class Code12_FloodFill {
public:
    // 四个方向的移动：上、右、下、左
    static const int move[5];
    
    static vector<vector<int> > floodFill(vector<vector<int> >& image, int sr, int sc, int newColor) {
        int n = image.size();
        int m = image[0].size();
        int oldColor = image[sr][sc];
        
        // 特殊情况：新颜色与原颜色相同
        if (oldColor == newColor) {
            return image;
        }
        
        // 队列用于BFS
        queue<pair<int, int> > q;
        
        // 起点入队
        q.push(make_pair(sr, sc));
        image[sr][sc] = newColor;
        
        // BFS填充颜色
        while (!q.empty()) {
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
                    // 检查边界和是否为相同颜色的像素点
                    if (nx >= 0 && nx < n && ny >= 0 && ny < m && image[nx][ny] == oldColor) {
                        image[nx][ny] = newColor;
                        q.push(make_pair(nx, ny));
                    }
                }
            }
        }
        return image;
    }
};

// 四个方向的移动：上、右、下、左
const int Code12_FloodFill::move[5] = { -1, 0, 1, 0, -1 };
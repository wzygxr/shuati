/**
 * 获取所有钥匙的最短路径
 *
 * 题目链接：https://leetcode.cn/problems/shortest-path-to-get-all-keys
 *
 * 题目描述：
 * 给定一个二维网格 grid ，其中：
 * '.' 代表一个空房间、'#' 代表一堵墙、'@' 是起点
 * 小写字母代表钥匙、大写字母代表锁
 * 从起点开始出发，一次移动是指向四个基本方向之一行走一个单位空间
 * 不能在网格外面行走，也无法穿过一堵墙
 * 如果途经一个钥匙，我们就把它捡起来。除非我们手里有对应的钥匙，否则无法通过锁。
 * 假设 k 为 钥匙/锁 的个数，且满足 1 <= k <= 6，
 * 字母表中的前 k 个字母在网格中都有自己对应的一个小写和一个大写字母
 * 换言之，每个锁有唯一对应的钥匙，每个钥匙也有唯一对应的锁
 * 另外，代表钥匙和锁的字母互为大小写并按字母顺序排列
 * 返回获取所有钥匙所需要的移动的最少次数。如果无法获取所有钥匙，返回 -1 。
 *
 * 解题思路：
 * 这是一个状态空间搜索问题，可以使用BFS解决。
 * 与传统BFS不同的是，这里的状态不仅包括位置(x,y)，还包括收集钥匙的状态。
 * 我们用位运算来表示钥匙收集状态，第i位为1表示已收集第i把钥匙。
 * 使用三维visited数组visited[x][y][state]来记录状态是否已访问。
 *
 * 算法应用场景：
 * - 游戏中的寻路问题
 * - 机器人路径规划
 * - 状态空间搜索问题
 *
 * 时间复杂度分析：
 * O(n*m*2^k)，其中n和m是网格的行数和列数，k是钥匙的数量
 *
 * 空间复杂度分析：
 * O(n*m*2^k)，存储访问状态数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用BFS求解最短路径
    // 时间复杂度: O(n*m*2^k)
    // 空间复杂度: O(n*m*2^k)
    int shortestPathAllKeys(vector<string>& grid) {
        int n = grid.size();
        int m = grid[0].size();
        
        // 寻找起点和统计所有钥匙
        int start_x = 0, start_y = 0;
        int key_count = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '@') {
                    start_x = i;
                    start_y = j;
                } else if (grid[i][j] >= 'a' && grid[i][j] <= 'f') {
                    key_count |= 1 << (grid[i][j] - 'a');
                }
            }
        }
        
        // BFS队列，存储(行坐标, 列坐标, 钥匙状态)
        queue<tuple<int, int, int>> q;
        q.push({start_x, start_y, 0});
        
        // visited[x][y][state]表示在位置(x,y)且钥匙收集状态为state时是否已访问
        vector<vector<vector<bool>>> visited(n, vector<vector<bool>>(m, vector<bool>(1 << 6, false)));
        visited[start_x][start_y][0] = true;
        
        // 方向数组：上、右、下、左
        vector<pair<int, int>> move = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        // level表示移动的步数
        int level = 0;
        
        // BFS主循环
        while (!q.empty()) {
            // 按层遍历
            int size = q.size();
            for (int i = 0; i < size; i++) {
                auto [x, y, s] = q.front();
                q.pop();
                
                // 向四个方向扩展
                for (auto [dx, dy] : move) {
                    int nx = x + dx;
                    int ny = y + dy;
                    int ns = s;
                    
                    // 越界或者遇到墙，跳过
                    if (nx < 0 || nx >= n || ny < 0 || ny >= m || grid[nx][ny] == '#') {
                        continue;
                    }
                    
                    // 遇到锁但没有对应的钥匙，跳过
                    if (grid[nx][ny] >= 'A' && grid[nx][ny] <= 'F' && 
                        ((ns & (1 << (grid[nx][ny] - 'A'))) == 0)) {
                        continue;
                    }
                    
                    // 遇到钥匙，收集钥匙
                    if (grid[nx][ny] >= 'a' && grid[nx][ny] <= 'f') {
                        ns |= (1 << (grid[nx][ny] - 'a'));
                    }
                    
                    // 如果收集到了所有钥匙，返回步数
                    if (ns == key_count) {
                        return level + 1;
                    }
                    
                    // 如果该状态未访问过，加入队列
                    if (!visited[nx][ny][ns]) {
                        visited[nx][ny][ns] = true;
                        q.push({nx, ny, ns});
                    }
                }
            }
            level++;
        }
        
        // 无法收集所有钥匙
        return -1;
    }
};
*/

// 算法核心思想总结：
// 1. 这是一个状态空间搜索问题，状态包括位置和钥匙收集情况
// 2. 使用BFS保证第一次到达目标状态时步数最少
// 3. 用位运算优化钥匙状态的表示和处理，提高效率
// 4. 通过visited数组避免重复访问相同状态，剪枝优化
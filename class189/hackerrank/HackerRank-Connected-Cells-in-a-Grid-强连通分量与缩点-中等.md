# HackerRank - Connected Cells in a Grid

## 题目信息
- **平台**: HackerRank
- **题目**: Connected Cells in a Grid（网格中的连通区域）
- **难度**: 中等
- **类型**: 图论/连通分量/网格遍历
- **分类**: Algorithms → Graph Theory

## 题目链接
- https://www.hackerrank.com/challenges/connected-cell-in-a-grid/problem

## 题目描述
给定一个 n × m 的二维网格，每个格子包含0或1。

两个值为1的格子如果共享一条边或一个角（即8连通），则它们属于同一个区域。

请找出最大的区域包含的格子数。

## 输入格式
第一行包含一个整数n，表示网格的行数。

第二行包含一个整数m，表示网格的列数。

接下来的n行，每行m个整数（0或1），表示网格。

## 输出格式
输出一个整数，表示最大区域的大小（包含的1的个数）。

## 样例输入
```
4
4
1 1 0 0
0 1 1 0
0 0 1 0
1 0 0 0
```

## 样例输出
```
5
```

## 解释
最大的区域由以下5个1组成：
```
(0,0) (0,1)
      (1,1) (1,2)
            (2,2)
```

## 笔试/面试考察点分析

### 核心考察点
1. **网格图遍历**：将二维网格视为图进行遍历
2. **8连通定义**：不仅考虑上下左右，还要考虑对角线
3. **连通分量计数**：统计每个连通区域的大小
4. **DFS/BFS在网格中的应用**：网格图遍历的标准做法

### 与强连通分量的关系
- 本题是无向图连通分量，不是有向图强连通分量
- 但核心思想一致：找出图中的连通块
- 可用于对比学习：理解无向图连通 vs 有向图强连通

### 面试高频提问
1. **4连通 vs 8连通**？
   - 4连通：上下左右
   - 8连通：加上四个对角线方向
   - 根据题目要求选择，本题是8连通

2. **如何优化网格图遍历**？
   - 并查集：在线处理动态连通性
   - DFS/BFS：离线处理静态图
   - 标记访问：避免重复访问

3. **如果网格是10^6 × 10^6，如何优化**？
   - 使用稀疏存储，只存储值为1的格子
   - 使用哈希表代替二维数组
   - 考虑分块处理

## 解题思路

### 核心步骤
1. **遍历网格**：对每个未访问的值为1的格子启动DFS/BFS
2. **8方向扩展**：对每个格子，检查8个方向的邻居
3. **计数**：统计每个连通区域的大小
4. **更新最大值**：记录所有区域中的最大值

### 8方向定义
```
(-1,-1) (-1,0) (-1,1)
( 0,-1) ( 0,0) ( 0,1)
( 1,-1) ( 1,0) ( 1,1)
```

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 15; // 最大网格尺寸，根据题目约束n,m ≤ 10设定

int n, m; // 网格行数和列数
int grid[MAXN][MAXN]; // 网格数据
bool visited[MAXN][MAXN]; // 访问标记数组

// 8个方向的偏移量：上、下、左、右、四个对角线
int dx[8] = {-1, -1, -1, 0, 0, 1, 1, 1}; // 行偏移
int dy[8] = {-1, 0, 1, -1, 1, -1, 0, 1}; // 列偏移

/**
 * DFS遍历连通区域
 * @param x 当前行坐标
 * @param y 当前列坐标
 * @return 当前连通区域的大小
 * HackerRank要点：网格图遍历是基础技能
 * ML关联：图像分割中的连通区域标记
 */
int dfs(int x, int y) {
    // 标记当前格子已访问，避免重复计数
    visited[x][y] = true;
    
    int count = 1; // 当前格子计数为1
    
    // 遍历8个方向
    for (int i = 0; i < 8; i++) {
        int nx = x + dx[i]; // 邻居的行坐标
        int ny = y + dy[i]; // 邻居的列坐标
        
        // 检查邻居坐标是否合法
        // 条件1：在网格范围内（0 ≤ nx < n, 0 ≤ ny < m）
        // 条件2：未访问过
        // 条件3：值为1（是区域的一部分）
        if (nx >= 0 && nx < n && ny >= 0 && ny < m && 
            !visited[nx][ny] && grid[nx][ny] == 1) {
            // 递归访问邻居，累加区域大小
            count += dfs(nx, ny);
        }
    }
    
    return count; // 返回当前连通区域的大小
}

int main() {
    // 读取网格行数
    cin >> n;
    // 读取网格列数
    cin >> m;
    
    // 读取网格数据
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            cin >> grid[i][j]; // 读取第i行第j列的值
            visited[i][j] = false; // 初始化访问标记为未访问
        }
    }
    
    int maxRegion = 0; // 最大区域大小，初始为0
    
    // 遍历整个网格
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            // 对每个未访问的值为1的格子启动DFS
            if (grid[i][j] == 1 && !visited[i][j]) {
                // 计算当前连通区域的大小
                int regionSize = dfs(i, j);
                // 更新最大区域大小
                maxRegion = max(maxRegion, regionSize);
            }
        }
    }
    
    // 输出最大区域大小
    cout << maxRegion << endl;
    
    return 0;
}
```

## Python实现

```python
def dfs(x, y, grid, visited, n, m):
    """
    DFS遍历连通区域
    :param x, y: 当前坐标
    :param grid: 网格
    :param visited: 访问标记
    :param n, m: 网格尺寸
    :return: 区域大小
    """
    visited[x][y] = True  # 标记已访问
    count = 1  # 当前格子计数
    
    # 8个方向
    directions = [(-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1)]
    
    for dx, dy in directions:
        nx, ny = x + dx, y + dy
        # 检查边界和条件
        if 0 <= nx < n and 0 <= ny < m and not visited[nx][ny] and grid[nx][ny] == 1:
            count += dfs(nx, ny, grid, visited, n, m)  # 递归访问
    
    return count

def max_region(grid, n, m):
    """
    找出最大连通区域
    """
    visited = [[False] * m for _ in range(n)]
    max_size = 0
    
    for i in range(n):
        for j in range(m):
            if grid[i][j] == 1 and not visited[i][j]:
                size = dfs(i, j, grid, visited, n, m)
                max_size = max(max_size, size)
    
    return max_size

# 读取输入
n = int(input())
m = int(input())
grid = []
for _ in range(n):
    row = list(map(int, input().split()))
    grid.append(row)

# 输出结果
print(max_region(grid, n, m))
```

## BFS实现（非递归，避免栈溢出）

```cpp
#include <bits/stdc++.h>
using namespace std;

int bfs(int sx, int sy, int n, int m, int grid[][15], bool visited[][15]) {
    queue<pair<int, int>> q; // BFS队列
    q.push({sx, sy}); // 起点入队
    visited[sx][sy] = true; // 标记起点已访问
    
    int dx[8] = {-1, -1, -1, 0, 0, 1, 1, 1};
    int dy[8] = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    int count = 0; // 区域大小计数
    
    while (!q.empty()) {
        auto [x, y] = q.front();
        q.pop();
        count++; // 当前格子计数
        
        // 遍历8个方向
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            
            // 检查合法性
            if (nx >= 0 && nx < n && ny >= 0 && ny < m && 
                !visited[nx][ny] && grid[nx][ny] == 1) {
                visited[nx][ny] = true; // 标记已访问
                q.push({nx, ny}); // 邻居入队
            }
        }
    }
    
    return count;
}
```

## 时间/空间复杂度分析

### 时间复杂度
- **遍历网格**：O(n × m)
- **DFS/BFS**：每个格子访问一次，总共O(n × m)
- **总时间复杂度**：O(n × m)

### 空间复杂度
- **访问标记数组**：O(n × m)
- **递归栈/队列**：最坏情况O(n × m)
- **总空间复杂度**：O(n × m)

## HackerRank平台特点

### 编程环境
- **多语言支持**：C, C++, Java, Python, JavaScript等
- **在线IDE**：提供代码编辑和运行环境
- **即时反馈**：提交后立即知道测试用例通过情况

### 题目分类
- **Algorithms**：基础算法
- **Data Structures**：数据结构
- **Mathematics**：数学
- **AI**：人工智能（部分题目）

### 与ML相关的题目
- **BotClean**：强化学习基础
- **PacMan-DFS**：搜索算法
- **Satisfiability**：逻辑推理

## 同类题目拓展

### HackerRank平台
- **HackerRank - Roads and Libraries**：连通分量应用
- **HackerRank - Journey to the Moon**：并查集
- **HackerRank - Components in a Graph**：连通分量计数

### 其他平台同类题
- **LeetCode 200 - Number of Islands**：经典岛屿问题
- **LeetCode 695 - Max Area of Island**：最大岛屿面积
- **LeetCode 1254 - Number of Closed Islands**：封闭岛屿

## ML/DL关联思考

### 1. 图像分割中的应用
```python
# 连通区域标记在图像分割中的应用
import cv2
import numpy as np

# 二值图像
binary_image = np.array([...])

# 连通区域标记
num_labels, labels, stats, centroids = cv2.connectedComponentsWithStats(
    binary_image, connectivity=8
)

# 提取最大区域
largest_region = np.argmax(stats[1:, cv2.CC_STAT_AREA]) + 1
```

### 2. 医学图像处理
- **细胞分割**：统计细胞数量
- **肿瘤检测**：找出病变区域
- **病理分析**：连通区域特征提取

### 3. 计算机视觉
- **目标检测**：连通区域作为候选框
- **图像预处理**：去噪后的区域分析
- **文档分析**：文字区域提取

### 4. 与GNN的结合
```python
# 将网格转换为图结构
import torch
from torch_geometric.data import Data

def grid_to_graph(grid):
    # 提取值为1的格子作为节点
    nodes = []
    edges = []
    
    for i in range(n):
        for j in range(m):
            if grid[i][j] == 1:
                node_id = len(nodes)
                nodes.append((i, j))
                
                # 检查8邻居，添加边
                for dx, dy in directions:
                    ni, nj = i + dx, j + dy
                    if valid(ni, nj) and grid[ni][nj] == 1:
                        edges.append((node_id, neighbor_id))
    
    return Data(x=features, edge_index=edges)
```

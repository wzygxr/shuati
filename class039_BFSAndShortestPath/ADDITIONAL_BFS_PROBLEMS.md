# 补充BFS题目集

本文件包含从各大OJ平台收集的BFS经典题目，涵盖LeetCode、LintCode、HackerRank、AtCoder、洛谷、Codeforces等平台。

## 1. 二进制矩阵中的最短路径 (Shortest Path in Binary Matrix)

### 题目描述
给定一个 n x n 的二进制矩阵 grid，返回矩阵中最短畅通路径的长度。如果不存在这样的路径，返回 -1。

畅通路径是从左上角单元格 (0, 0) 到右下角单元格 (n - 1, n - 1) 的路径，路径上的所有单元格的值都是 0。路径可以向8个方向移动。

### 来源
- LeetCode: https://leetcode.cn/problems/shortest-path-in-binary-matrix/
- 难度: 中等

### 解题思路
这是一个典型的BFS最短路径问题。使用标准的BFS算法，从起点开始逐层扩展，直到到达终点。

### Java实现
```java
import java.util.*;

public class ShortestPathInBinaryMatrix {
    public int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0] == 1) return -1;
        
        int n = grid.length;
        if (n == 1) return 1;
        
        // 8个方向
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        grid[0][0] = 1; // 标记为已访问
        
        int pathLength = 1;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                
                // 检查是否到达终点
                if (x == n - 1 && y == n - 1) {
                    return pathLength;
                }
                
                // 向8个方向扩展
                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    
                    if (nx >= 0 && nx < n && ny >= 0 && ny < n && grid[nx][ny] == 0) {
                        queue.offer(new int[]{nx, ny});
                        grid[nx][ny] = 1; // 标记为已访问
                    }
                }
            }
            pathLength++;
        }
        
        return -1;
    }
}
```

### Python实现
```python
from collections import deque

def shortestPathBinaryMatrix(grid):
    """
    二进制矩阵中的最短路径
    
    Args:
        grid: List[List[int]] - n x n 的二进制矩阵
    
    Returns:
        int - 最短路径长度，如果不存在则返回-1
    """
    if grid[0][0] == 1:
        return -1
    
    n = len(grid)
    if n == 1:
        return 1
    
    # 8个方向
    directions = [
        (-1, -1), (-1, 0), (-1, 1),
        (0, -1),           (0, 1),
        (1, -1),  (1, 0),  (1, 1)
    ]
    
    queue = deque([(0, 0)])
    grid[0][0] = 1  # 标记为已访问
    
    path_length = 1
    
    while queue:
        size = len(queue)
        for _ in range(size):
            x, y = queue.popleft()
            
            # 检查是否到达终点
            if x == n - 1 and y == n - 1:
                return path_length
            
            # 向8个方向扩展
            for dx, dy in directions:
                nx, ny = x + dx, y + dy
                
                if 0 <= nx < n and 0 <= ny < n and grid[nx][ny] == 0:
                    queue.append((nx, ny))
                    grid[nx][ny] = 1  # 标记为已访问
        
        path_length += 1
    
    return -1
```

### C++实现
``cpp
#include <vector>
#include <queue>
using namespace std;

int shortestPathBinaryMatrix(vector<vector<int>>& grid) {
    if (grid[0][0] == 1) return -1;
    
    int n = grid.size();
    if (n == 1) return 1;
    
    // 8个方向
    int directions[8][2] = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1},           {0, 1},
        {1, -1},  {1, 0},  {1, 1}
    };
    
    queue<pair<int, int>> q;
    q.push({0, 0});
    grid[0][0] = 1; // 标记为已访问
    
    int pathLength = 1;
    
    while (!q.empty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            auto current = q.front();
            q.pop();
            int x = current.first;
            int y = current.second;
            
            // 检查是否到达终点
            if (x == n - 1 && y == n - 1) {
                return pathLength;
            }
            
            // 向8个方向扩展
            for (int j = 0; j < 8; j++) {
                int nx = x + directions[j][0];
                int ny = y + directions[j][1];
                
                if (nx >= 0 && nx < n && ny >= 0 && ny < n && grid[nx][ny] == 0) {
                    q.push({nx, ny});
                    grid[nx][ny] = 1; // 标记为已访问
                }
            }
        }
        pathLength++;
    }
    
    return -1;
}
```

## 2. 打开转盘锁 (Open the Lock)

### 题目描述
你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。

锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。

列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。

字符串 target 代表可以解锁的数字，你需要给出最小的旋转次数，如果无论如何不能解锁，返回 -1。

### 来源
- LeetCode: https://leetcode.cn/problems/open-the-lock/
- 难度: 中等

### 解题思路
这是一个BFS最短路径问题。从起点"0000"开始，每次可以向8个方向扩展（每个拨轮可以向上或向下旋转），使用BFS找到最短路径。需要注意避免访问死亡数字。

### Java实现
```java
import java.util.*;

public class OpenTheLock {
    public int openLock(String[] deadends, String target) {
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        if (deadSet.contains("0000")) return -1;
        if ("0000".equals(target)) return 0;
        
        Queue<String> queue = new LinkedList<>();
        queue.offer("0000");
        Set<String> visited = new HashSet<>();
        visited.add("0000");
        
        int steps = 0;
        
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                
                // 生成所有可能的下一步状态
                for (String next : getNextStates(current)) {
                    if (next.equals(target)) {
                        return steps;
                    }
                    
                    if (!deadSet.contains(next) && !visited.contains(next)) {
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
        }
        
        return -1;
    }
    
    private List<String> getNextStates(String s) {
        List<String> res = new ArrayList<>();
        char[] chars = s.toCharArray();
        
        for (int i = 0; i < 4; i++) {
            char original = chars[i];
            
            // 向上旋转
            chars[i] = (char) ((original - '0' + 1) % 10 + '0');
            res.add(new String(chars));
            
            // 向下旋转
            chars[i] = (char) ((original - '0' + 9) % 10 + '0');
            res.add(new String(chars));
            
            // 恢复原状
            chars[i] = original;
        }
        
        return res;
    }
}
```

### Python实现
```
from collections import deque

def openLock(deadends, target):
    """
    打开转盘锁
    
    Args:
        deadends: List[str] - 死亡数字列表
        target: str - 目标数字
    
    Returns:
        int - 最小旋转次数，如果无法解锁则返回-1
    """
    dead_set = set(deadends)
    if "0000" in dead_set:
        return -1
    if target == "0000":
        return 0
    
    queue = deque(["0000"])
    visited = set(["0000"])
    
    steps = 0
    
    while queue:
        steps += 1
        size = len(queue)
        
        for _ in range(size):
            current = queue.popleft()
            
            # 生成所有可能的下一步状态
            for next_state in get_next_states(current):
                if next_state == target:
                    return steps
                
                if next_state not in dead_set and next_state not in visited:
                    queue.append(next_state)
                    visited.add(next_state)
    
    return -1

def get_next_states(s):
    """
    生成当前状态的所有可能下一步状态
    
    Args:
        s: str - 当前状态
    
    Returns:
        List[str] - 所有可能的下一步状态
    """
    res = []
    chars = list(s)
    
    for i in range(4):
        original = chars[i]
        
        # 向上旋转
        chars[i] = str((int(original) + 1) % 10)
        res.append("".join(chars))
        
        # 向下旋转
        chars[i] = str((int(original) + 9) % 10)
        res.append("".join(chars))
        
        # 恢复原状
        chars[i] = original
    
    return res
```

### C++实现
```
#include <vector>
#include <string>
#include <queue>
#include <unordered_set>
using namespace std;

int openLock(vector<string>& deadends, string target) {
    unordered_set<string> deadSet(deadends.begin(), deadends.end());
    if (deadSet.count("0000")) return -1;
    if (target == "0000") return 0;
    
    queue<string> q;
    q.push("0000");
    unordered_set<string> visited;
    visited.insert("0000");
    
    int steps = 0;
    
    while (!q.empty()) {
        steps++;
        int size = q.size();
        
        for (int i = 0; i < size; i++) {
            string current = q.front();
            q.pop();
            
            // 生成所有可能的下一步状态
            for (string next : getNextStates(current)) {
                if (next == target) {
                    return steps;
                }
                
                if (!deadSet.count(next) && !visited.count(next)) {
                    q.push(next);
                    visited.insert(next);
                }
            }
        }
    }
    
    return -1;
}

vector<string> getNextStates(string s) {
    vector<string> res;
    for (int i = 0; i < 4; i++) {
        char original = s[i];
        
        // 向上旋转
        s[i] = (original - '0' + 1) % 10 + '0';
        res.push_back(s);
        
        // 向下旋转
        s[i] = (original - '0' + 9) % 10 + '0';
        res.push_back(s);
        
        // 恢复原状
        s[i] = original;
    }
    
    return res;
}
```

## 3. 滑动谜题 (Sliding Puzzle)

### 题目描述
在一个 2 x 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示，以及一块空缺用 0 来表示。一次移动定义为选择 0 与一个相邻的数字（上下左右）进行交换。

最终当板 board 的结果是 [[1,2,3],[4,5,0]] 谜板被解开。

给出一个谜板的初始状态 board，返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1。

### 来源
- LeetCode: https://leetcode.cn/problems/sliding-puzzle/
- 难度: 困难

### 解题思路
这是一个经典的BFS问题。将2x3的矩阵转换为字符串表示状态，使用BFS搜索从初始状态到目标状态的最短路径。需要预处理每个位置可以移动到的相邻位置。

### Java实现
```java
import java.util.*;

public class SlidingPuzzle {
    public int slidingPuzzle(int[][] board) {
        String target = "123450";
        String start = "";
        
        // 将二维数组转换为字符串
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                start += board[i][j];
            }
        }
        
        if (start.equals(target)) return 0;
        
        // 预处理每个位置的相邻位置
        int[][] neighbors = {
            {1, 3},         // 位置0的相邻位置
            {0, 2, 4},      // 位置1的相邻位置
            {1, 5},         // 位置2的相邻位置
            {0, 4},         // 位置3的相邻位置
            {1, 3, 5},      // 位置4的相邻位置
            {2, 4}          // 位置5的相邻位置
        };
        
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(start);
        visited.add(start);
        
        int steps = 0;
        
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                int zeroIndex = current.indexOf('0');
                
                // 尝试与每个相邻位置交换
                for (int neighbor : neighbors[zeroIndex]) {
                    String next = swap(current, zeroIndex, neighbor);
                    if (next.equals(target)) {
                        return steps;
                    }
                    
                    if (!visited.contains(next)) {
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
        }
        
        return -1;
    }
    
    private String swap(String s, int i, int j) {
        char[] chars = s.toCharArray();
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        return new String(chars);
    }
}
```

### Python实现
```
from collections import deque

def slidingPuzzle(board):
    """
    滑动谜题
    
    Args:
        board: List[List[int]] - 2x3的初始状态
    
    Returns:
        int - 最少移动次数，如果无法解开则返回-1
    """
    target = "123450"
    start = ""
    
    # 将二维数组转换为字符串
    for i in range(2):
        for j in range(3):
            start += str(board[i][j])
    
    if start == target:
        return 0
    
    # 预处理每个位置的相邻位置
    neighbors = [
        [1, 3],         # 位置0的相邻位置
        [0, 2, 4],      # 位置1的相邻位置
        [1, 5],         # 位置2的相邻位置
        [0, 4],         # 位置3的相邻位置
        [1, 3, 5],      # 位置4的相邻位置
        [2, 4]          # 位置5的相邻位置
    ]
    
    queue = deque([start])
    visited = set([start])
    
    steps = 0
    
    while queue:
        steps += 1
        size = len(queue)
        
        for _ in range(size):
            current = queue.popleft()
            zero_index = current.index('0')
            
            # 尝试与每个相邻位置交换
            for neighbor in neighbors[zero_index]:
                next_state = swap(current, zero_index, neighbor)
                if next_state == target:
                    return steps
                
                if next_state not in visited:
                    queue.append(next_state)
                    visited.add(next_state)
    
    return -1

def swap(s, i, j):
    """
    交换字符串中两个位置的字符
    
    Args:
        s: str - 原字符串
        i: int - 第一个位置
        j: int - 第二个位置
    
    Returns:
        str - 交换后的字符串
    """
    chars = list(s)
    chars[i], chars[j] = chars[j], chars[i]
    return "".join(chars)
```

### C++实现
```
#include <vector>
#include <string>
#include <queue>
#include <unordered_set>
using namespace std;

int slidingPuzzle(vector<vector<int>>& board) {
    string target = "123450";
    string start = "";
    
    // 将二维数组转换为字符串
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 3; j++) {
            start += to_string(board[i][j]);
        }
    }
    
    if (start == target) return 0;
    
    // 预处理每个位置的相邻位置
    vector<vector<int>> neighbors = {
        {1, 3},         // 位置0的相邻位置
        {0, 2, 4},      // 位置1的相邻位置
        {1, 5},         // 位置2的相邻位置
        {0, 4},         // 位置3的相邻位置
        {1, 3, 5},      // 位置4的相邻位置
        {2, 4}          // 位置5的相邻位置
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
            int zeroIndex = current.find('0');
            
            // 尝试与每个相邻位置交换
            for (int neighbor : neighbors[zeroIndex]) {
                string next = swap(current, zeroIndex, neighbor);
                if (next == target) {
                    return steps;
                }
                
                if (!visited.count(next)) {
                    q.push(next);
                    visited.insert(next);
                }
            }
        }
    }
    
    return -1;
}

string swap(string s, int i, int j) {
    char temp = s[i];
    s[i] = s[j];
    s[j] = temp;
    return s;
}
```

## 4. 岛屿数量 (Number of Islands)

### 题目描述
给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。

岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。

此外，你可以假设该网格的四条边均被水包围。

### 来源
- LeetCode: https://leetcode.cn/problems/number-of-islands/
- 难度: 中等

### 解题思路
这是一个经典的BFS/DFS问题。遍历网格，当遇到'1'时，使用BFS将与它相连的所有'1'标记为已访问，同时岛屿数量加1。

### Java实现
```java
import java.util.*;

public class NumberOfIslands {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        
        // 四个方向
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    // 使用BFS将整个岛屿标记为已访问
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{i, j});
                    grid[i][j] = '0'; // 标记为已访问
                    
                    while (!queue.isEmpty()) {
                        int[] current = queue.poll();
                        int x = current[0];
                        int y = current[1];
                        
                        // 向四个方向扩展
                        for (int[] dir : directions) {
                            int nx = x + dir[0];
                            int ny = y + dir[1];
                            
                            if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && grid[nx][ny] == '1') {
                                queue.offer(new int[]{nx, ny});
                                grid[nx][ny] = '0'; // 标记为已访问
                            }
                        }
                    }
                }
            }
        }
        
        return count;
    }
}
```

### Python实现
```
from collections import deque

def numIslands(grid):
    """
    岛屿数量
    
    Args:
        grid: List[List[str]] - 二维网格，'1'表示陆地，'0'表示水
    
    Returns:
        int - 岛屿数量
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    count = 0
    
    # 四个方向
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    
    for i in range(rows):
        for j in range(cols):
            if grid[i][j] == '1':
                count += 1
                # 使用BFS将整个岛屿标记为已访问
                queue = deque([(i, j)])
                grid[i][j] = '0'  # 标记为已访问
                
                while queue:
                    x, y = queue.popleft()
                    
                    # 向四个方向扩展
                    for dx, dy in directions:
                        nx, ny = x + dx, y + dy
                        
                        if 0 <= nx < rows and 0 <= ny < cols and grid[nx][ny] == '1':
                            queue.append((nx, ny))
                            grid[nx][ny] = '0'  # 标记为已访问
    
    return count
```

### C++实现
```
#include <vector>
#include <queue>
using namespace std;

int numIslands(vector<vector<char>>& grid) {
    if (grid.empty() || grid[0].empty()) return 0;
    
    int rows = grid.size();
    int cols = grid[0].size();
    int count = 0;
    
    // 四个方向
    int directions[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (grid[i][j] == '1') {
                count++;
                // 使用BFS将整个岛屿标记为已访问
                queue<pair<int, int>> q;
                q.push({i, j});
                grid[i][j] = '0'; // 标记为已访问
                
                while (!q.empty()) {
                    auto current = q.front();
                    q.pop();
                    int x = current.first;
                    int y = current.second;
                    
                    // 向四个方向扩展
                    for (int k = 0; k < 4; k++) {
                        int nx = x + directions[k][0];
                        int ny = y + directions[k][1];
                        
                        if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && grid[nx][ny] == '1') {
                            q.push({nx, ny});
                            grid[nx][ny] = '0'; // 标记为已访问
                        }
                    }
                }
            }
        }
    }
    
    return count;
}
```

## 5. 腐烂的橘子 (Rotting Oranges)

### 题目描述
在给定的 m x n 网格 grid 中，每个单元格可以有以下三个值之一：

- 值 0 代表空单元格；
- 值 1 代表新鲜橘子；
- 值 2 代表腐烂的橘子。

每分钟，腐烂的橘子周围 4 个方向上相邻的新鲜橘子都会腐烂。

返回直到单元格中没有新鲜橘子为止所必须经过的最小分钟数。如果不可能，返回 -1。

### 来源
- LeetCode: https://leetcode.cn/problems/rotting-oranges/
- 难度: 中等

### 解题思路
这是一个多源BFS问题。首先将所有腐烂的橘子加入队列，然后同时开始BFS，模拟腐烂过程。记录所需的时间，最后检查是否还有新鲜橘子。

### Java实现
```
import java.util.*;

public class RottingOranges {
    public int orangesRotting(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int rows = grid.length;
        int cols = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int freshCount = 0;
        
        // 四个方向
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        // 统计新鲜橘子数量，并将腐烂橘子加入队列
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    freshCount++;
                } else if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                }
            }
        }
        
        // 如果没有新鲜橘子，直接返回0
        if (freshCount == 0) return 0;
        
        int minutes = 0;
        
        // 多源BFS
        while (!queue.isEmpty() && freshCount > 0) {
            minutes++;
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                
                // 向四个方向扩展
                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    
                    if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && grid[nx][ny] == 1) {
                        grid[nx][ny] = 2; // 腐烂
                        freshCount--;
                        queue.offer(new int[]{nx, ny});
                    }
                }
            }
        }
        
        return freshCount == 0 ? minutes : -1;
    }
}

```

### Python实现
```
from collections import deque

def orangesRotting(grid):
    """
    腐烂的橘子
    
    Args:
        grid: List[List[int]] - 网格，0表示空单元格，1表示新鲜橘子，2表示腐烂橘子
    
    Returns:
        int - 腐烂所有橘子所需的最小分钟数，如果不可能则返回-1
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    queue = deque()
    fresh_count = 0
    
    # 四个方向
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    
    # 统计新鲜橘子数量，并将腐烂橘子加入队列
    for i in range(rows):
        for j in range(cols):
            if grid[i][j] == 1:
                fresh_count += 1
            elif grid[i][j] == 2:
                queue.append((i, j))
    
    # 如果没有新鲜橘子，直接返回0
    if fresh_count == 0:
        return 0
    
    minutes = 0
    
    # 多源BFS
    while queue and fresh_count > 0:
        minutes += 1
        size = len(queue)
        
        for _ in range(size):
            x, y = queue.popleft()
            
            # 向四个方向扩展
            for dx, dy in directions:
                nx, ny = x + dx, y + dy
                
                if 0 <= nx < rows and 0 <= ny < cols and grid[nx][ny] == 1:
                    grid[nx][ny] = 2  # 腐烂
                    fresh_count -= 1
                    queue.append((nx, ny))
    
    return fresh_count == 0 else -1

```

### C++实现
```
#include <vector>
#include <queue>
using namespace std;

int orangesRotting(vector<vector<int>>& grid) {
    if (grid.empty() || grid[0].empty()) return 0;
    
    int rows = grid.size();
    int cols = grid[0].size();
    queue<pair<int, int>> q;
    int freshCount = 0;
    
    // 四个方向
    int directions[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    
    // 统计新鲜橘子数量，并将腐烂橘子加入队列
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (grid[i][j] == 1) {
                freshCount++;
            } else if (grid[i][j] == 2) {
                q.push({i, j});
            }
        }
    }
    
    // 如果没有新鲜橘子，直接返回0
    if (freshCount == 0) return 0;
    
    int minutes = 0;
    
    // 多源BFS
    while (!q.empty() && freshCount > 0) {
        minutes++;
        int size = q.size();
        
        for (int i = 0; i < size; i++) {
            auto current = q.front();
            q.pop();
            int x = current.first;
            int y = current.second;
            
            // 向四个方向扩展
            for (int k = 0; k < 4; k++) {
                int nx = x + directions[k][0];
                int ny = y + directions[k][1];
                
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && grid[nx][ny] == 1) {
                    grid[nx][ny] = 2; // 腐烂
                    freshCount--;
                    q.push({nx, ny});
                }
            }
        }
    }
    
    return freshCount == 0 ? minutes : -1;
}

```


## 6. 01 矩阵 (01 Matrix)

### 题目描述
给定一个由 0 和 1 组成的矩阵 mat，请输出一个大小相同的矩阵，其中每一个格子是 mat 中对应位置元素到最近的 0 的距离。

两个相邻元素间的距离为 1。

### 来源
- LeetCode: https://leetcode.cn/problems/01-matrix/
- 难度: 中等

### 解题思路
这是一个典型的多源BFS问题。将所有0的位置作为起始点加入队列，然后进行BFS，逐步更新每个1到最近0的距离。

### Java实现
```java
import java.util.*;

public class UpdateMatrix {
    public int[][] updateMatrix(int[][] mat) {
        if (mat == null || mat.length == 0) return mat;
        
        int rows = mat.length;
        int cols = mat[0].length;
        int[][] dist = new int[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        
        // 四个方向
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        // 初始化：将所有0的位置加入队列，1的位置设为最大值
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mat[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                } else {
                    dist[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        
        // 多源BFS
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            
            // 向四个方向扩展
            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                
                // 检查边界和是否可以更新距离
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                    if (dist[nx][ny] > dist[x][y] + 1) {
                        dist[nx][ny] = dist[x][y] + 1;
                        queue.offer(new int[]{nx, ny});
                    }
                }
            }
        }
        
        return dist;
    }
}

```

### Python实现
```
from collections import deque

def updateMatrix(mat):
    """
    01 矩阵
    
    Args:
        mat: List[List[int]] - 由0和1组成的矩阵
    
    Returns:
        List[List[int]] - 每个位置到最近0的距离矩阵
    """
    if not mat or not mat[0]:
        return mat
    
    rows, cols = len(mat), len(mat[0])
    dist = [[float('inf')] * cols for _ in range(rows)]
    queue = deque()
    
    # 四个方向
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    
    # 初始化：将所有0的位置加入队列，1的位置设为无穷大
    for i in range(rows):
        for j in range(cols):
            if mat[i][j] == 0:
                queue.append((i, j))
                dist[i][j] = 0
    
    # 多源BFS
    while queue:
        x, y = queue.popleft()
        
        # 向四个方向扩展
        for dx, dy in directions:
            nx, ny = x + dx, y + dy
            
            # 检查边界和是否可以更新距离
            if 0 <= nx < rows and 0 <= ny < cols:
                if dist[nx][ny] > dist[x][y] + 1:
                    dist[nx][ny] = dist[x][y] + 1
                    queue.append((nx, ny))
    
    return dist

```

### C++实现
```
#include <vector>
#include <queue>
#include <climits>
using namespace std;

vector<vector<int>> updateMatrix(vector<vector<int>>& mat) {
    if (mat.empty() || mat[0].empty()) return mat;
    
    int rows = mat.size();
    int cols = mat[0].size();
    vector<vector<int>> dist(rows, vector<int>(cols, INT_MAX));
    queue<pair<int, int>> q;
    
    // 四个方向
    int directions[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    
    // 初始化：将所有0的位置加入队列，1的位置设为最大值
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (mat[i][j] == 0) {
                q.push({i, j});
                dist[i][j] = 0;
            }
        }
    }
    
    // 多源BFS
    while (!q.empty()) {
        auto current = q.front();
        q.pop();
        int x = current.first;
        int y = current.second;
        
        // 向四个方向扩展
        for (int k = 0; k < 4; k++) {
            int nx = x + directions[k][0];
            int ny = y + directions[k][1];
            
            // 检查边界和是否可以更新距离
            if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                if (dist[nx][ny] > dist[x][y] + 1) {
                    dist[nx][ny] = dist[x][y] + 1;
                    q.push({nx, ny});
                }
            }
        }
    }
    
    return dist;
}

```


## 7. KATHTHI (01BFS经典题)

### 题目描述
给定一个字符矩阵，从左上角(0,0)走到右下角(n-1,m-1)。每次可以向上下左右四个方向移动。

如果移动到的字符与当前位置字符相同，则移动代价为0；否则代价为1。

求从起点到终点的最小代价。

### 来源
- SPOJ: https://www.spoj.com/problems/KATHTHI/
- 洛谷: https://www.luogu.com.cn/problem/SP22393
- 难度: 中等

### 解题思路
这是一个经典的01BFS问题。使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾。

### Java实现
```
import java.util.*;

public class KATHTHI {
    public static int minChanges(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        
        // 四个方向的移动：上、右、下、左
        int[] move = {-1, 0, 1, 0, -1};
        
        // distance[i][j]表示从起点(0,0)到(i,j)的最小变化次数
        int[][] distance = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }
        
        // 双端队列，用于0-1 BFS
        Deque<int[]> deque = new ArrayDeque<>();
        deque.addFirst(new int[]{0, 0});
        distance[0][0] = 0;
        
        while (!deque.isEmpty()) {
            // 从队首取出节点
            int[] current = deque.pollFirst();
            int x = current[0];
            int y = current[1];
            
            // 如果到达终点
            if (x == n - 1 && y == m - 1) {
                return distance[x][y];
            }
            
            // 向四个方向扩展
            for (int i = 0; i < 4; i++) {
                int nx = x + move[i];
                int ny = y + move[i + 1];
                
                // 检查边界
                if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                    // 如果字符相同，权重为0；否则权重为1
                    int weight = (grid[x][y] != grid[nx][ny]) ? 1 : 0;
                    
                    // 如果新路径更优
                    if (distance[x][y] + weight < distance[nx][ny]) {
                        distance[nx][ny] = distance[x][y] + weight;
                        // 根据权重决定放在队首还是队尾
                        if (weight == 0) {
                            deque.addFirst(new int[]{nx, ny});
                        } else {
                            deque.addLast(new int[]{nx, ny});
                        }
                    }
                }
            }
        }
        
        return -1;
    }
}

```

### Python实现
```
from collections import deque

def minChanges(grid):
    """
    KATHTHI - 01BFS经典题
    
    Args:
        grid: List[List[str]] - 字符矩阵
    
    Returns:
        int - 从起点到终点的最小代价
    """
    n, m = len(grid), len(grid[0])
    
    # 四个方向的移动：上、右、下、左
    move = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # distance[i][j]表示从起点(0,0)到(i,j)的最小变化次数
    distance = [[float('inf')] * m for _ in range(n)]
    
    # 双端队列，用于0-1 BFS
    dq = deque()
    dq.appendleft((0, 0))
    distance[0][0] = 0
    
    while dq:
        # 从队首取出节点
        x, y = dq.popleft()
        
        # 如果到达终点
        if x == n - 1 and y == m - 1:
            return distance[x][y]
        
        # 向四个方向扩展
        for dx, dy in move:
            nx, ny = x + dx, y + dy
            
            # 检查边界
            if 0 <= nx < n and 0 <= ny < m:
                # 如果字符相同，权重为0；否则权重为1
                weight = 1 if grid[x][y] != grid[nx][ny] else 0
                
                # 如果新路径更优
                if distance[x][y] + weight < distance[nx][ny]:
                    distance[nx][ny] = distance[x][y] + weight
                    # 根据权重决定放在队首还是队尾
                    if weight == 0:
                        dq.appendleft((nx, ny))
                    else:
                        dq.append((nx, ny))
    
    return -1

```

### C++实现
```
#include <vector>
#include <deque>
#include <climits>
using namespace std;

int minChanges(vector<vector<char>>& grid) {
    int n = grid.size();
    int m = grid[0].size();
    
    // 四个方向的移动：上、右、下、左
    int move[5] = {-1, 0, 1, 0, -1};
    
    // distance[i][j]表示从起点(0,0)到(i,j)的最小变化次数
    vector<vector<int>> distance(n, vector<int>(m, INT_MAX));
    
    // 双端队列，用于0-1 BFS
    deque<pair<int, int>> dq;
    dq.push_front({0, 0});
    distance[0][0] = 0;
    
    while (!dq.empty()) {
        // 从队首取出节点
        auto current = dq.front();
        dq.pop_front();
        int x = current.first;
        int y = current.second;
        
        // 如果到达终点
        if (x == n - 1 && y == m - 1) {
            return distance[x][y];
        }
        
        // 向四个方向扩展
        for (int i = 0; i < 4; i++) {
            int nx = x + move[i];
            int ny = y + move[i + 1];
            
            // 检查边界
            if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                // 如果字符相同，权重为0；否则权重为1
                int weight = (grid[x][y] != grid[nx][ny]) ? 1 : 0;
                
                // 如果新路径更优
                if (distance[x][y] + weight < distance[nx][ny]) {
                    distance[nx][ny] = distance[x][y] + weight;
                    // 根据权重决定放在队首还是队尾
                    if (weight == 0) {
                        dq.push_front({nx, ny});
                    } else {
                        dq.push_back({nx, ny});
                    }
                }
            }
        }
    }
    
    return -1;
}

```


## 总结

以上是7道经典的BFS题目，涵盖了BFS的不同应用场景：

1. **最短路径问题** - 二进制矩阵中的最短路径
2. **状态搜索问题** - 打开转盘锁
3. **状态转换问题** - 滑动谜题
4. **图遍历问题** - 岛屿数量
5. **多源BFS问题** - 腐烂的橘子
6. **距离计算问题** - 01矩阵
7. **01BFS问题** - KATHTHI

这些题目展示了BFS在解决各种问题时的灵活性和强大能力。掌握这些经典题目的解法有助于更好地理解和应用BFS算法。
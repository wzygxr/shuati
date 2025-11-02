package class126;

// URAL 1519 Formula 1 (插头DP - 哈密顿回路)
// 在n×m的网格中，求经过所有非障碍格子的哈密顿回路数
// 1 <= n, m <= 12
// 测试链接 : https://vjudge.net/problem/URAL-1519
//
// 题目大意：
// 给定一个n×m的网格，其中一些格子是障碍物（用'*'表示），其他格子是可通行的（用'.'表示）。
// 要求找到一条哈密顿回路，即经过所有可通行格子恰好一次的闭合路径。
// 求满足条件的哈密顿回路数。
//
// 解题思路：
// 使用插头DP解决哈密顿回路问题。
// 状态表示：用括号表示法表示轮廓线上的连通性状态。
// 状态转移：根据插头的连通性进行合并、创建等操作。
// 使用哈希表优化状态存储。
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/URAL1519_Formula1.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/URAL1519_Formula1.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/URAL1519_Formula1.py

public class URAL1519_Formula1 {
    
    public static int MAXN = 15;
    public static int MAX_STATES = 300000; // 足够存储所有状态
    
    // 使用哈希表存储状态，因为状态数太多无法直接用数组
    public static class HashTable {
        public int[] head = new int[MAX_STATES];
        public int[] next = new int[MAX_STATES];
        public int[] state = new int[MAX_STATES];
        public long[] value = new long[MAX_STATES];
        public int size;
        
        public void init() {
            for (int i = 0; i < size; i++) {
                head[i] = -1;
            }
            size = 1;
        }
        
        public int getHash(int st) {
            return st % MAX_STATES;
        }
        
        public int find(int st) {
            int h = getHash(st);
            for (int i = head[h]; i != -1; i = next[i]) {
                if (state[i] == st) {
                    return i;
                }
            }
            return -1;
        }
        
        public int insert(int st, long val) {
            int h = getHash(st);
            int pos = find(st);
            if (pos != -1) {
                value[pos] += val;
                return pos;
            }
            state[size] = st;
            value[size] = val;
            next[size] = head[h];
            head[h] = size++;
            return size - 1;
        }
    }
    
    public static HashTable[] dp = new HashTable[2];
    public static char[][] grid = new char[MAXN][MAXN];
    public static int n, m;
    
    /**
     * 计算经过所有非障碍格子的哈密顿回路数
     * 
     * 算法思路：
     * 使用插头DP解决哈密顿回路问题
     * 状态表示：用括号表示法表示轮廓线上的连通性状态
     * 状态转移：根据插头的连通性进行合并、创建等操作
     * 使用哈希表优化状态存储
     * 
     * 时间复杂度：O(n * m * 状态数)
     * 空间复杂度：O(状态数)
     * 
     * @param rows 行数
     * @param cols 列数
     * @param maze 网格，'.'表示可经过，'*'表示障碍
     * @return 哈密顿回路数
     */
    public static long solve(int rows, int cols, char[][] maze) {
        n = rows;
        m = cols;
        
        // 复制网格
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = maze[i][j];
            }
        }
        
        // 初始化哈希表
        dp[0] = new HashTable();
        dp[1] = new HashTable();
        
        // 初始状态
        dp[0].init();
        dp[1].init();
        dp[0].insert(0, 1);
        
        int cur = 0;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            // 行间转移
            for (int j = 0; j < dp[cur].size; j++) {
                if (dp[cur].value[j] > 0) {
                    // 将状态转移到下一行的开始
                    dp[1-cur].insert(dp[cur].state[j] << 2, dp[cur].value[j]);
                }
            }
            
            cur = 1 - cur;
            dp[1-cur].init();
            
            // 行内转移
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < dp[cur].size; k++) {
                    int state = dp[cur].state[k];
                    long value = dp[cur].value[k];
                    if (value == 0) continue;
                    
                    // 获取当前格子左边和上面的插头状态
                    int left = j > 0 ? ((state >> (2 * (j - 1))) & 3) : 0;
                    int up = (state >> (2 * j)) & 3;
                    
                    // 如果是障碍格子
                    if (grid[i][j] == '*') {
                        // 只能在没有插头的情况下转移
                        if (left == 0 && up == 0) {
                            int newState = state & (~((3 << (2 * (j - 1))) | (3 << (2 * j))));
                            dp[1-cur].insert(newState, value);
                        }
                    } else {
                        // 可通行格子
                        
                        // 1. 不放置插头（合并两个插头）
                        if (left != 0 && up != 0) {
                            int newState = state;
                            newState &= ~((3 << (2 * (j - 1))) | (3 << (2 * j)));
                            
                            // 如果两个插头属于不同连通分量，则合并
                            // 如果两个插头属于相同连通分量，则形成哈密顿回路
                            if (left == up) {
                                // 检查是否所有格子都已访问
                                if (newState == 0 && i == n-1 && j == m-1) {
                                    dp[1-cur].insert(newState, value);
                                }
                            } else {
                                // 合并连通分量
                                // 需要重新编号保持括号表示法
                                newState = renumber(newState, j-1, j, left, up);
                                dp[1-cur].insert(newState, value);
                            }
                        }
                        
                        // 2. 延续插头
                        if (left != 0 && up == 0) {
                            // 延续左插头到上方
                            int newState = state;
                            newState &= ~(3 << (2 * (j - 1)));
                            newState |= (left << (2 * j));
                            dp[1-cur].insert(newState, value);
                        }
                        
                        if (left == 0 && up != 0) {
                            // 延续上插头到左方
                            int newState = state;
                            newState &= ~(3 << (2 * j));
                            newState |= (up << (2 * (j - 1)));
                            dp[1-cur].insert(newState, value);
                        }
                        
                        // 3. 创建新插头对（如果左右和上方都没有插头）
                        if (left == 0 && up == 0) {
                            // 创建一对新插头（左插头和上插头）
                            int newState = state | (1 << (2 * (j - 1))) | (1 << (2 * j));
                            dp[1-cur].insert(newState, value);
                        }
                    }
                }
                
                cur = 1 - cur;
                dp[1-cur].init();
            }
        }
        
        // 统计哈密顿回路数
        long result = 0;
        for (int i = 0; i < dp[cur].size; i++) {
            if (dp[cur].state[i] == 0) {
                result += dp[cur].value[i];
            }
        }
        return result;
    }
    
    /**
     * 重新编号以保持括号表示法
     */
    public static int renumber(int state, int pos1, int pos2, int id1, int id2) {
        // 合并两个连通分量，将id2的编号改为id1
        int minId = Math.min(id1, id2);
        int maxId = Math.max(id1, id2);
        
        for (int i = 0; i < m; i++) {
            int m = (state >> (2 * i)) & 3;
            if (m == maxId) {
                state &= ~(3 << (2 * i));
                state |= (minId << (2 * i));
            }
        }
        
        return state;
    }
    
    // 测试用例
    public static void main(String[] args) {
        char[][] maze1 = {
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'}
        };
        System.out.println(solve(4, 4, maze1)); // 输出哈密顿回路数
    }
}
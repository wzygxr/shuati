/**
 * 状态工程 (State Engineering)
 * 
 * 技术原理：
 * 状态工程是一种通过状态压缩和状态管理来优化算法性能的技术。
 * 主要包括位压缩、Zobrist哈希、状态去重等方法，用于减少状态表示的空间
 * 和提高状态比较的效率。
 * 
 * 技术特点：
 * 1. 位压缩：使用位运算表示状态，节省空间
 * 2. 状态哈希：快速比较和查找状态
 * 3. 状态去重：避免重复计算相同状态
 * 4. 高效状态转移：快速生成后继状态
 * 
 * 应用场景：
 * - 棋盘游戏状态表示
 * - 动态规划状态压缩
 * - 搜索算法状态管理
 * - 游戏AI状态评估
 * 
 * 核心技术：
 * 1. 位压缩(bitset/uint128)：用位表示状态
 * 2. Zobrist哈希：状态去重和快速比较
 * 3. 状态缓存：避免重复计算
 * 
 * 时间复杂度：取决于具体应用
 * 空间复杂度：通常比传统表示方法更优
 */

#include <iostream>
#include <vector>
#include <map>
#include <random>
#include <bitset>
#include <chrono>
#include <cstring>
#include <queue>
#include <climits>
#include <cstdint>

using namespace std;

class StateEngineering {
public:
    /**
     * 位压缩工具类 - 使用uint64_t类型进行位操作
     */
    class BitCompression {
    public:
        /**
         * 设置指定位为1
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 更新后的状态
         */
        static unsigned long long setBit(unsigned long long state, int bit) {
            return state | (1ULL << bit);
        }
        
        /**
         * 清除指定位（设为0）
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 更新后的状态
         */
        static unsigned long long clearBit(unsigned long long state, int bit) {
            return state & ~(1ULL << bit);
        }
        
        /**
         * 检查指定位是否为1
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 是否为1
         */
        static bool isBitSet(unsigned long long state, int bit) {
            return (state & (1ULL << bit)) != 0;
        }
        
        /**
         * 翻转指定位
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 更新后的状态
         */
        static unsigned long long toggleBit(unsigned long long state, int bit) {
            return state ^ (1ULL << bit);
        }
        
        /**
         * 计算状态中1的个数（汉明重量）
         * 
         * @param state 状态
         * @return 1的个数
         */
        static int countBits(unsigned long long state) {
            return __builtin_popcountll(state);
        }
        
        /**
         * 获取最低位的1的位置
         * 
         * @param state 状态
         * @return 最低位1的位置，如果没有1则返回-1
         */
        static int getLowestBitPosition(unsigned long long state) {
            if (state == 0) return -1;
            return __builtin_ctzll(state);
        }
        
        /**
         * 打印二进制表示
         * 
         * @param state 状态
         */
        static void printBinary(unsigned long long state) {
            std::bitset<64> bits(state);
            cout << bits << endl;
        }
    };
    
    /**
     * Zobrist哈希工具类
     */
    class ZobristHashing {
    private:
        static const int BOARD_SIZE = 64; // 假设64位棋盘
        static const int MAX_PIECE_TYPES = 16; // 最多16种棋子类型
        static unsigned long long zobristTable[BOARD_SIZE][MAX_PIECE_TYPES];
        static bool initialized;
        
        /**
         * 初始化Zobrist哈希表
         */
        static void initializeZobristTable() {
            if (initialized) return;
            
            mt19937_64 rng(12345); // 固定种子以保证一致性
            
            // 为每个位置和每种棋子类型生成随机数
            for (int pos = 0; pos < BOARD_SIZE; pos++) {
                for (int piece = 0; piece < MAX_PIECE_TYPES; piece++) {
                    zobristTable[pos][piece] = rng();
                }
            }
            
            initialized = true;
        }
        
    public:
        /**
         * 计算棋盘状态的Zobrist哈希值
         * 
         * @param board 棋盘状态数组，board[i]表示位置i的棋子类型
         * @return 哈希值
         */
        static unsigned long long calculateHash(const std::vector<int>& board) {
            if (!initialized) initializeZobristTable();
            
            unsigned long long hash = 0;
            
            for (size_t pos = 0; pos < board.size() && pos < BOARD_SIZE; pos++) {
                int piece = board[pos];
                if (piece >= 0 && piece < MAX_PIECE_TYPES) {
                    hash ^= zobristTable[pos][piece];
                }
            }
            
            return hash;
        }
        
        /**
         * 更新哈希值（当某个位置的棋子发生变化时）
         * 
         * @param currentHash 当前哈希值
         * @param position 位置
         * @param oldPiece 旧棋子类型
         * @param newPiece 新棋子类型
         * @return 更新后的哈希值
         */
        static unsigned long long updateHash(unsigned long long currentHash, int position, 
                                 int oldPiece, int newPiece) {
            if (!initialized) initializeZobristTable();
            
            unsigned long long newHash = currentHash;
            
            // 移除旧棋子的贡献
            if (oldPiece >= 0 && oldPiece < MAX_PIECE_TYPES) {
                newHash ^= zobristTable[position][oldPiece];
            }
            
            // 添加新棋子的贡献
            if (newPiece >= 0 && newPiece < MAX_PIECE_TYPES) {
                newHash ^= zobristTable[position][newPiece];
            }
            
            return newHash;
        }
        
        /**
         * 获取Zobrist表中的值（用于测试）
         * 
         * @param position 位置
         * @param piece 棋子类型
         * @return Zobrist值
         */
        static unsigned long long getZobristValue(int position, int piece) {
            if (!initialized) initializeZobristTable();
            
            if (position >= 0 && position < BOARD_SIZE && 
                piece >= 0 && piece < MAX_PIECE_TYPES) {
                return zobristTable[position][piece];
            }
            return 0;
        }
    };
    
    /**
     * 状态缓存类 - 用于避免重复计算相同状态
     */
    template<typename T>
    class StateCache {
    private:
        std::map<unsigned long long, T> cache;
        int hitCount;
        int missCount;
        
    public:
        StateCache() : hitCount(0), missCount(0) {}
        
        /**
         * 获取状态对应的值
         * 
         * @param hash 状态哈希值
         * @return 状态值的指针，如果不存在则返回nullptr
         */
        T* get(unsigned long long hash) {
            auto it = cache.find(hash);
            if (it != cache.end()) {
                hitCount++;
                return &(it->second);
            } else {
                missCount++;
                return nullptr;
            }
        }
        
        /**
         * 存储状态值
         * 
         * @param hash 状态哈希值
         * @param value 状态值
         */
        void put(unsigned long long hash, const T& value) {
            cache[hash] = value;
        }
        
        /**
         * 检查状态是否存在
         * 
         * @param hash 状态哈希值
         * @return 是否存在
         */
        bool contains(unsigned long long hash) {
            return cache.find(hash) != cache.end();
        }
        
        /**
         * 获取缓存大小
         * 
         * @return 缓存大小
         */
        size_t size() const {
            return cache.size();
        }
        
        /**
         * 获取命中率
         * 
         * @return 命中率
         */
        double getHitRate() const {
            int total = hitCount + missCount;
            return total == 0 ? 0 : (double)hitCount / total;
        }
        
        /**
         * 清空缓存
         */
        void clear() {
            cache.clear();
            hitCount = 0;
            missCount = 0;
        }
        
        /**
         * 获取统计信息
         * 
         * @return 统计信息字符串
         */
        std::string getStats() const {
            char buffer[200];
            sprintf(buffer, "缓存大小: %zu, 命中: %d, 未命中: %d, 命中率: %.2f%%", 
                   size(), hitCount, missCount, getHitRate() * 100);
            return std::string(buffer);
        }
    };
    
    /**
     * 128位整数模拟类（用于更大规模的状态压缩）
     */
    class UInt128 {
    private:
        unsigned long long high; // 高64位
        unsigned long long low;  // 低64位
        
    public:
        UInt128() : high(0), low(0) {}
        
        UInt128(unsigned long long high, unsigned long long low) : high(high), low(low) {}
        
        /**
         * 设置指定位为1
         * 
         * @param bit 位索引(0-127)
         * @return 更新后的值
         */
        UInt128 setBit(int bit) const {
            if (bit < 64) {
                return UInt128(high, low | (1ULL << bit));
            } else {
                return UInt128(high | (1ULL << (bit - 64)), low);
            }
        }
        
        /**
         * 检查指定位是否为1
         * 
         * @param bit 位索引(0-127)
         * @return 是否为1
         */
        bool isBitSet(int bit) const {
            if (bit < 64) {
                return (low & (1ULL << bit)) != 0;
            } else {
                return (high & (1ULL << (bit - 64))) != 0;
            }
        }
        
        /**
         * 与操作
         * 
         * @param other 另一个UInt128
         * @return 结果
         */
        UInt128 andOp(const UInt128& other) const {
            return UInt128(high & other.high, low & other.low);
        }
        
        /**
         * 或操作
         * 
         * @param other 另一个UInt128
         * @return 结果
         */
        UInt128 orOp(const UInt128& other) const {
            return UInt128(high | other.high, low | other.low);
        }
        
        /**
         * 异或操作
         * 
         * @param other 另一个UInt128
         * @return 结果
         */
        UInt128 xorOp(const UInt128& other) const {
            return UInt128(high ^ other.high, low ^ other.low);
        }
        
        /**
         * 左移操作
         * 
         * @param shift 移位数
         * @return 结果
         */
        UInt128 leftShift(int shift) const {
            if (shift == 0) return UInt128(high, low);
            if (shift >= 128) return UInt128(0, 0);
            
            if (shift < 64) {
                uint64_t newHigh = (high << shift) | (low >> (64 - shift));
                uint64_t newLow = low << shift;
                return UInt128(newHigh, newLow);
            } else {
                uint64_t newHigh = low << (shift - 64);
                return UInt128(newHigh, 0);
            }
        }
        
        /**
         * 右移操作
         * 
         * @param shift 移位数
         * @return 结果
         */
        UInt128 rightShift(int shift) const {
            if (shift == 0) return UInt128(high, low);
            if (shift >= 128) return UInt128(0, 0);
            
            if (shift < 64) {
                uint64_t newLow = (low >> shift) | (high << (64 - shift));
                uint64_t newHigh = high >> shift;
                return UInt128(newHigh, newLow);
            } else {
                uint64_t newLow = high >> (shift - 64);
                return UInt128(0, newLow);
            }
        }
        
        bool operator==(const UInt128& other) const {
            return high == other.high && low == other.low;
        }
        
        bool operator!=(const UInt128& other) const {
            return !(*this == other);
        }
        
        bool operator<(const UInt128& other) const {
            if (high != other.high) return high < other.high;
            return low < other.low;
        }
        
        friend std::ostream& operator<<(std::ostream& os, const UInt128& value) {
            os << std::hex << value.high << value.low << std::dec;
            return os;
        }
        
        // Getter方法
        uint64_t getHigh() const { return high; }
        uint64_t getLow() const { return low; }
    };
};

// 静态成员初始化
bool StateEngineering::ZobristHashing::initialized = false;
uint64_t StateEngineering::ZobristHashing::zobristTable[64][16];

/**
 * 测试示例
 */
/**
 * N皇后问题 - 使用位运算优化的状态压缩回溯算法
 * 题目来源: LeetCode 51. N-Queens, LeetCode 52. N-Queens II
 * 题目链接: https://leetcode.cn/problems/n-queens/
 * 题目链接: https://leetcode.cn/problems/n-queens-ii/
 * 
 * 题目描述:
 * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 给你一个整数 n ，返回 n 皇后问题 不同的解决方案的数量。
 * 
 * 解题思路:
 * 使用位运算优化的回溯算法，通过位掩码表示列、主对角线和副对角线的占用情况。
 * 1. 使用三个整数变量分别表示列、主对角线和副对角线的占用情况
 * 2. 使用位运算快速找到可用位置
 * 3. 通过位运算快速更新状态
 * 
 * 时间复杂度: O(N!)
 * 空间复杂度: O(N) - 递归栈空间
 * 
 * 工程化考量:
 * 1. 使用位运算优化性能
 * 2. 通过状态压缩减少内存使用
 * 3. 适用于n <= 16的情况
 * 4. 处理边界情况（n=1, n=2等）
 */
static int solveNQueens(int n, int row, uint64_t columns, uint64_t diagonals1, uint64_t diagonals2) {
    // 基线条件：所有皇后都已放置
    if (row == n) {
        return 1;
    }
    
    int count = 0;
    // 获取可用的位置（位为0表示可用）
    uint64_t availablePositions = ((1ULL << n) - 1) & ~(columns | diagonals1 | diagonals2);
    
    while (availablePositions != 0) {
        // 获取最低位的1（选择一个可用位置）
        uint64_t position = availablePositions & -availablePositions;
        // 清除最低位的1
        availablePositions &= availablePositions - 1;
        
        // 递归处理下一行，更新列和对角线的占用情况
        count += solveNQueens(n, row + 1, 
                            columns | position,
                            (diagonals1 | position) << 1,
                            (diagonals2 | position) >> 1);
    }
    
    return count;
}

static int totalNQueens(int n) {
    return solveNQueens(n, 0, 0, 0, 0);
}

/**
 * 旅行商问题(TSP) - 使用状态压缩动态规划
 * 题目来源: 经典算法问题
 * 
 * 题目描述:
 * 给定n个城市和它们之间的距离，找到一条最短的路径，访问每个城市恰好一次并回到起点。
 * 
 * 解题思路:
 * 使用状态压缩DP，dp[mask][last]表示在mask状态下最后访问城市last时的最短距离。
 * 1. 使用位掩码表示已访问的城市集合
 * 2. 状态转移：从当前状态转移到新状态
 * 3. 最终结果：访问所有城市后回到起点
 * 
 * 时间复杂度: O(2^N * N^2)
 * 空间复杂度: O(2^N * N)
 * 
 * 工程化考量:
 * 1. 适用于n <= 20的情况
 * 2. 对于更大规模问题需要使用近似算法
 * 3. 处理对称图的优化
 * 4. 内存优化：使用滚动数组等技术
 */
static int tspDP(const vector<vector<int>>& graph) {
    int n = graph.size();
    if (n <= 1) return 0;
    
    int totalStates = 1 << n;
    vector<vector<int>> dp(totalStates, vector<int>(n, INT_MAX));
    
    // 起点状态：只访问了城市0
    dp[1][0] = 0;
    
    // 遍历所有状态
    for (int mask = 1; mask < totalStates; mask++) {
        for (int last = 0; last < n; last++) {
            // 如果last不在mask中，跳过
            if ((mask & (1 << last)) == 0) continue;
            
            // 如果当前状态不可达，跳过
            if (dp[mask][last] == INT_MAX) continue;
            
            // 尝试访问新城市
            for (int next = 0; next < n; next++) {
                // 如果next已经在mask中，跳过
                if ((mask & (1 << next)) != 0) continue;
                
                int newMask = mask | (1 << next);
                int newDistance = dp[mask][last] + graph[last][next];
                
                if (newDistance < dp[newMask][next]) {
                    dp[newMask][next] = newDistance;
                }
            }
        }
    }
    
    // 找到最短回路：访问所有城市后回到起点
    int finalMask = (1 << n) - 1;
    int minDistance = INT_MAX;
    
    for (int last = 0; last < n; last++) {
        if (dp[finalMask][last] != INT_MAX) {
            minDistance = min(minDistance, 
                dp[finalMask][last] + graph[last][0]);
        }
    }
    
    return minDistance;
}

/**
 * 推箱子游戏 - 使用Zobrist哈希进行状态管理
 * 题目来源: LeetCode 1263. 推箱子
 * 题目链接: https://leetcode.cn/problems/minimum-moves-to-move-a-box-to-their-target-location/
 * 
 * 题目描述:
 * 「推箱子」是一款风靡全球的益智小游戏，玩家需要将箱子推到仓库中的目标位置。
 * 游戏地图用大小为 m x n 的网格 grid 表示，其中每个元素可以是墙、地板、箱子、玩家和目标。
 * 
 * 解题思路:
 * 使用BFS搜索最短路径，结合Zobrist哈希进行状态去重。
 * 1. 使用Zobrist哈希表示游戏状态（玩家位置+箱子位置）
 * 2. 使用BFS搜索最短推动次数
 * 3. 使用状态缓存避免重复访问相同状态
 * 
 * 时间复杂度: O(N*M*2^(N*M)) - 最坏情况
 * 空间复杂度: O(N*M*2^(N*M)) - 状态存储
 * 
 * 工程化考量:
 * 1. 使用Zobrist哈希进行状态压缩和快速比较
 * 2. 使用状态缓存避免重复计算
 * 3. 处理边界情况（无法到达、无解等）
 * 4. 优化搜索顺序，优先搜索更有可能的路径
 */
static int minPushBox(const vector<vector<char>>& grid) {
    int m = grid.size(), n = grid[0].size();
    int playerX = -1, playerY = -1, boxX = -1, boxY = -1, targetX = -1, targetY = -1;
    
    // 找到初始位置
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (grid[i][j] == 'S') {
                playerX = i;
                playerY = j;
            } else if (grid[i][j] == 'B') {
                boxX = i;
                boxY = j;
            } else if (grid[i][j] == 'T') {
                targetX = i;
                targetY = j;
            }
        }
    }
    
    // 使用Zobrist哈希进行状态管理
    StateEngineering::StateCache<bool> visited;
    
    // BFS队列：[玩家x, 玩家y, 箱子x, 箱子y, 推动次数]
    queue<vector<int>> queue;
    
    // 初始状态
    vector<int> initialState = {playerX, playerY, boxX, boxY};
    uint64_t initialStateHash = StateEngineering::ZobristHashing::calculateHash(initialState);
    queue.push({playerX, playerY, boxX, boxY, 0});
    visited.put(initialStateHash, true);
    
    // 四个方向：上、右、下、左
    vector<vector<int>> directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    
    while (!queue.empty()) {
        vector<int> current = queue.front();
        queue.pop();
        int px = current[0], py = current[1], bx = current[2], by = current[3], pushes = current[4];
        
        // 到达目标位置
        if (bx == targetX && by == targetY) {
            return pushes;
        }
        
        // 尝试四个方向移动
        for (const auto& dir : directions) {
            int newX = px + dir[0];
            int newY = py + dir[1];
            
            // 检查边界和墙
            if (newX < 0 || newX >= m || newY < 0 || newY >= n || grid[newX][newY] == '#') {
                continue;
            }
            
            // 如果移动到箱子位置，尝试推动箱子
            if (newX == bx && newY == by) {
                int newBoxX = bx + dir[0];
                int newBoxY = by + dir[1];
                
                // 检查箱子推动后的位置是否合法
                if (newBoxX < 0 || newBoxX >= m || newBoxY < 0 || newBoxY >= n || 
                    grid[newBoxX][newBoxY] == '#') {
                    continue;
                }
                
                // 新状态
                vector<int> newState = {newX, newY, newBoxX, newBoxY};
                uint64_t newStateHash = StateEngineering::ZobristHashing::calculateHash(newState);
                if (!visited.contains(newStateHash)) {
                    visited.put(newStateHash, true);
                    queue.push({newX, newY, newBoxX, newBoxY, pushes + 1});
                }
            } else {
                // 玩家移动但不推动箱子
                vector<int> newState = {newX, newY, bx, by};
                uint64_t newStateHash = StateEngineering::ZobristHashing::calculateHash(newState);
                if (!visited.contains(newStateHash)) {
                    visited.put(newStateHash, true);
                    queue.push({newX, newY, bx, by, pushes});
                }
            }
        }
    }
    
    return -1; // 无法到达目标
}

/**
 * 获取所有钥匙的最短路径 - 使用状态压缩BFS
 * 题目来源: LeetCode 864. Shortest Path to Get All Keys
 * 题目链接: https://leetcode.cn/problems/shortest-path-to-get-all-keys/
 * 
 * 题目描述:
 * 给定一个二维网格，其中包含：
 * '.' - 空房间
 * '#' - 墙壁
 * '@' - 起点
 * 小写字母 - 钥匙
 * 大写字母 - 锁
 * 
 * 解题思路:
 * 使用BFS搜索最短路径，结合状态压缩表示钥匙收集情况。
 * 1. 使用位掩码表示已收集的钥匙
 * 2. 状态表示：(x, y, keys)
 * 3. BFS搜索最短路径
 * 
 * 时间复杂度: O(M*N*2^K) - M,N为网格大小，K为钥匙数量
 * 空间复杂度: O(M*N*2^K)
 * 
 * 工程化考量:
 * 1. 使用状态压缩减少状态表示空间
 * 2. 使用距离数组避免重复访问
 * 3. 处理边界情况（无法获取所有钥匙等）
 * 4. 优化搜索顺序
 */
static int shortestPathAllKeys(const vector<string>& grid) {
    int m = grid.size(), n = grid[0].size();
    int allKeys = 0;
    int startX = -1, startY = -1;
    
    // 找到起点和所有钥匙
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            char c = grid[i][j];
            if (c == '@') {
                startX = i;
                startY = j;
            } else if (c >= 'a' && c <= 'f') {
                allKeys |= (1 << (c - 'a'));
            }
        }
    }
    
    // BFS搜索
    vector<vector<vector<int>>> dist(m, vector<vector<int>>(n, vector<int>(1 << 6, -1)));
    
    dist[startX][startY][0] = 0;
    queue<vector<int>> q;
    q.push({startX, startY, 0});
    
    vector<vector<int>> directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    
    while (!q.empty()) {
        vector<int> current = q.front();
        q.pop();
        int x = current[0], y = current[1], keys = current[2];
        int distance = dist[x][y][keys];
        
        if (keys == allKeys) {
            return distance;
        }
        
        for (const auto& dir : directions) {
            int nx = x + dir[0], ny = y + dir[1];
            if (nx < 0 || nx >= m || ny < 0 || ny >= n) continue;
            
            char c = grid[nx][ny];
            if (c == '#') continue; // 墙
            
            int newKeys = keys;
            if (c >= 'A' && c <= 'F') {
                // 遇到锁，检查是否有对应的钥匙
                int lock = c - 'A';
                if ((keys & (1 << lock)) == 0) continue; // 没有钥匙
            } else if (c >= 'a' && c <= 'f') {
                // 捡到钥匙
                newKeys |= (1 << (c - 'a'));
            }
            
            if (dist[nx][ny][newKeys] == -1) {
                dist[nx][ny][newKeys] = distance + 1;
                q.push({nx, ny, newKeys});
            }
        }
    }
    
    return -1;
}

int main() {
    cout << "=== 状态工程技术测试 ===" << endl;
    
    // 测试位压缩
    cout << "\n1. 位压缩测试:" << endl;
    uint64_t state = 0ULL;
    cout << "初始状态: ";
    StateEngineering::BitCompression::printBinary(state);
    
    // 设置一些位
    state = StateEngineering::BitCompression::setBit(state, 3);
    state = StateEngineering::BitCompression::setBit(state, 7);
    state = StateEngineering::BitCompression::setBit(state, 15);
    cout << "设置位3,7,15后: ";
    StateEngineering::BitCompression::printBinary(state);
    
    // 检查位
    cout << "位3是否为1: " << (StateEngineering::BitCompression::isBitSet(state, 3) ? "是" : "否") << endl;
    cout << "位5是否为1: " << (StateEngineering::BitCompression::isBitSet(state, 5) ? "是" : "否") << endl;
    
    // 计算1的个数
    cout << "1的个数: " << StateEngineering::BitCompression::countBits(state) << endl;
    
    // 翻转位
    state = StateEngineering::BitCompression::toggleBit(state, 3);
    cout << "翻转位3后: ";
    StateEngineering::BitCompression::printBinary(state);
    cout << "位3是否为1: " << (StateEngineering::BitCompression::isBitSet(state, 3) ? "是" : "否") << endl;
    
    // 测试Zobrist哈希
    cout << "\n2. Zobrist哈希测试:" << endl;
    vector<int> board(8, -1); // -1表示空位
    board[0] = 1; // 位置0放置类型1的棋子
    board[3] = 2; // 位置3放置类型2的棋子
    board[7] = 3; // 位置7放置类型3的棋子
    
    uint64_t hash1 = StateEngineering::ZobristHashing::calculateHash(board);
    printf("初始哈希值: %llu\n", hash1);
    
    // 移动棋子
    uint64_t hash2 = StateEngineering::ZobristHashing::updateHash(hash1, 0, 1, -1); // 移走位置0的棋子
    hash2 = StateEngineering::ZobristHashing::updateHash(hash2, 1, -1, 1); // 在位置1放置棋子
    printf("移动后哈希值: %llu\n", hash2);
    
    // 验证一致性
    board[0] = -1;
    board[1] = 1;
    uint64_t hash3 = StateEngineering::ZobristHashing::calculateHash(board);
    printf("重新计算哈希值: %llu\n", hash3);
    cout << "一致性验证: " << (hash2 == hash3 ? "通过" : "失败") << endl;
    
    // 测试状态缓存
    cout << "\n3. 状态缓存测试:" << endl;
    StateEngineering::StateCache<string> cache;
    
    // 添加一些状态
    cache.put(hash1, "状态1");
    cache.put(hash2, "状态2");
    cache.put(12345ULL, "状态3");
    
    printf("缓存大小: %zu\n", cache.size());
    string* result1 = cache.get(hash1);
    cout << "查找存在的状态: " << (result1 ? *result1 : "未找到") << endl;
    string* result2 = cache.get(99999ULL);
    cout << "查找不存在的状态: " << (result2 ? *result2 : "未找到") << endl;
    
    // 测试命中率
    cache.get(hash1); // 命中
    cache.get(99999ULL); // 未命中
    cache.get(hash2); // 命中
    cout << cache.getStats() << endl;
    
    // 测试128位整数
    cout << "\n4. 128位整数测试:" << endl;
    StateEngineering::UInt128 uint128;
    cout << "初始值: " << uint128 << endl;
    
    // 设置一些位
    uint128 = uint128.setBit(3).setBit(67).setBit(127);
    cout << "设置位3,67,127后: " << uint128 << endl;
    
    // 检查位
    cout << "位3是否为1: " << (uint128.isBitSet(3) ? "是" : "否") << endl;
    cout << "位64是否为1: " << (uint128.isBitSet(64) ? "是" : "否") << endl;
    
    // 位运算测试
    StateEngineering::UInt128 a(0x1234567890ABCDEFULL, 0xFEDCBA0987654321ULL);
    StateEngineering::UInt128 b(0xAAAAAAAAAAAAAAAAULL, 0x5555555555555555ULL);
    cout << "a: " << a << endl;
    cout << "b: " << b << endl;
    cout << "a & b: " << a.andOp(b) << endl;
    cout << "a | b: " << a.orOp(b) << endl;
    cout << "a ^ b: " << a.xorOp(b) << endl;
    
    // 移位测试
    cout << "a << 5: " << a.leftShift(5) << endl;
    cout << "a >> 5: " << a.rightShift(5) << endl;
    
    // 测试N皇后问题
    cout << "\n5. N皇后问题测试:" << endl;
    for (int n = 1; n <= 8; n++) {
        printf("%d皇后问题的解决方案数量: %d\n", n, totalNQueens(n));
    }
    
    // 测试旅行商问题
    cout << "\n6. 旅行商问题测试:" << endl;
    vector<vector<int>> graph = {
        {0, 10, 15, 20},
        {10, 0, 35, 25},
        {15, 35, 0, 30},
        {20, 25, 30, 0}
    };
    printf("4城市TSP最短路径长度: %d\n", tspDP(graph));
    
    // 测试获取所有钥匙的最短路径
    cout << "\n7. 获取所有钥匙的最短路径测试:" << endl;
    vector<string> grid = {"@.a.#", "###.#", "b.A.B"};
    printf("网格的最短路径长度: %d\n", shortestPathAllKeys(grid));
    
    return 0;
}
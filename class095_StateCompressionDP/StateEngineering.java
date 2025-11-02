import java.util.*;

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

public class StateEngineering {
    
    /**
     * 位压缩工具类 - 使用long类型进行位操作
     */
    public static class BitCompression {
        /**
         * 设置指定位为1
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 更新后的状态
         */
        public static long setBit(long state, int bit) {
            return state | (1L << bit);
        }
        
        /**
         * 清除指定位（设为0）
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 更新后的状态
         */
        public static long clearBit(long state, int bit) {
            return state & ~(1L << bit);
        }
        
        /**
         * 检查指定位是否为1
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 是否为1
         */
        public static boolean isBitSet(long state, int bit) {
            return (state & (1L << bit)) != 0;
        }
        
        /**
         * 翻转指定位
         * 
         * @param state 当前状态
         * @param bit 位索引
         * @return 更新后的状态
         */
        public static long toggleBit(long state, int bit) {
            return state ^ (1L << bit);
        }
        
        /**
         * 计算状态中1的个数（汉明重量）
         * 
         * @param state 状态
         * @return 1的个数
         */
        public static int countBits(long state) {
            return Long.bitCount(state);
        }
        
        /**
         * 获取最低位的1的位置
         * 
         * @param state 状态
         * @return 最低位1的位置，如果没有1则返回-1
         */
        public static int getLowestBitPosition(long state) {
            if (state == 0) return -1;
            return Long.numberOfTrailingZeros(state);
        }
        
        /**
         * 打印二进制表示
         * 
         * @param state 状态
         */
        public static void printBinary(long state) {
            System.out.println(Long.toBinaryString(state));
        }
    }
    
    /**
     * Zobrist哈希工具类
     */
    public static class ZobristHashing {
        private static final int BOARD_SIZE = 64; // 假设64位棋盘
        private static final int MAX_PIECE_TYPES = 16; // 最多16种棋子类型
        private static long[][] zobristTable;
        private static Random random;
        
        static {
            initializeZobristTable();
        }
        
        /**
         * 初始化Zobrist哈希表
         */
        private static void initializeZobristTable() {
            zobristTable = new long[BOARD_SIZE][MAX_PIECE_TYPES];
            random = new Random(12345); // 固定种子以保证一致性
            
            // 为每个位置和每种棋子类型生成随机数
            for (int pos = 0; pos < BOARD_SIZE; pos++) {
                for (int piece = 0; piece < MAX_PIECE_TYPES; piece++) {
                    zobristTable[pos][piece] = random.nextLong();
                }
            }
        }
        
        /**
         * 计算棋盘状态的Zobrist哈希值
         * 
         * @param board 棋盘状态数组，board[i]表示位置i的棋子类型
         * @return 哈希值
         */
        public static long calculateHash(int[] board) {
            long hash = 0;
            
            for (int pos = 0; pos < board.length && pos < BOARD_SIZE; pos++) {
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
        public static long updateHash(long currentHash, int position, int oldPiece, int newPiece) {
            long newHash = currentHash;
            
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
        public static long getZobristValue(int position, int piece) {
            if (position >= 0 && position < BOARD_SIZE && 
                piece >= 0 && piece < MAX_PIECE_TYPES) {
                return zobristTable[position][piece];
            }
            return 0;
        }
    }
    
    /**
     * 状态缓存类 - 用于避免重复计算相同状态
     */
    public static class StateCache<T> {
        private Map<Long, T> cache;
        private int hitCount;
        private int missCount;
        
        public StateCache() {
            this.cache = new HashMap<>();
            this.hitCount = 0;
            this.missCount = 0;
        }
        
        /**
         * 获取状态对应的值
         * 
         * @param hash 状态哈希值
         * @return 状态值，如果不存在则返回null
         */
        public T get(long hash) {
            T value = cache.get(hash);
            if (value != null) {
                hitCount++;
            } else {
                missCount++;
            }
            return value;
        }
        
        /**
         * 存储状态值
         * 
         * @param hash 状态哈希值
         * @param value 状态值
         */
        public void put(long hash, T value) {
            cache.put(hash, value);
        }
        
        /**
         * 检查状态是否存在
         * 
         * @param hash 状态哈希值
         * @return 是否存在
         */
        public boolean contains(long hash) {
            return cache.containsKey(hash);
        }
        
        /**
         * 获取缓存大小
         * 
         * @return 缓存大小
         */
        public int size() {
            return cache.size();
        }
        
        /**
         * 获取命中率
         * 
         * @return 命中率
         */
        public double getHitRate() {
            int total = hitCount + missCount;
            return total == 0 ? 0 : (double) hitCount / total;
        }
        
        /**
         * 清空缓存
         */
        public void clear() {
            cache.clear();
            hitCount = 0;
            missCount = 0;
        }
        
        /**
         * 获取统计信息
         * 
         * @return 统计信息字符串
         */
        public String getStats() {
            return String.format("缓存大小: %d, 命中: %d, 未命中: %d, 命中率: %.2f%%", 
                               size(), hitCount, missCount, getHitRate() * 100);
        }
    }
    
    /**
     * 128位整数模拟类（用于更大规模的状态压缩）
     */
    public static class UInt128 {
        private long high; // 高64位
        private long low;  // 低64位
        
        public UInt128() {
            this.high = 0;
            this.low = 0;
        }
        
        public UInt128(long high, long low) {
            this.high = high;
            this.low = low;
        }
        
        /**
         * 设置指定位为1
         * 
         * @param bit 位索引(0-127)
         * @return 更新后的值
         */
        public UInt128 setBit(int bit) {
            if (bit < 64) {
                return new UInt128(high, low | (1L << bit));
            } else {
                return new UInt128(high | (1L << (bit - 64)), low);
            }
        }
        
        /**
         * 检查指定位是否为1
         * 
         * @param bit 位索引(0-127)
         * @return 是否为1
         */
        public boolean isBitSet(int bit) {
            if (bit < 64) {
                return (low & (1L << bit)) != 0;
            } else {
                return (high & (1L << (bit - 64))) != 0;
            }
        }
        
        /**
         * 与操作
         * 
         * @param other 另一个UInt128
         * @return 结果
         */
        public UInt128 and(UInt128 other) {
            return new UInt128(high & other.high, low & other.low);
        }
        
        /**
         * 或操作
         * 
         * @param other 另一个UInt128
         * @return 结果
         */
        public UInt128 or(UInt128 other) {
            return new UInt128(high | other.high, low | other.low);
        }
        
        /**
         * 异或操作
         * 
         * @param other 另一个UInt128
         * @return 结果
         */
        public UInt128 xor(UInt128 other) {
            return new UInt128(high ^ other.high, low ^ other.low);
        }
        
        /**
         * 左移操作
         * 
         * @param shift 移位数
         * @return 结果
         */
        public UInt128 leftShift(int shift) {
            if (shift == 0) return new UInt128(high, low);
            if (shift >= 128) return new UInt128(0, 0);
            
            if (shift < 64) {
                long newHigh = (high << shift) | (low >>> (64 - shift));
                long newLow = low << shift;
                return new UInt128(newHigh, newLow);
            } else {
                long newHigh = low << (shift - 64);
                return new UInt128(newHigh, 0);
            }
        }
        
        /**
         * 右移操作
         * 
         * @param shift 移位数
         * @return 结果
         */
        public UInt128 rightShift(int shift) {
            if (shift == 0) return new UInt128(high, low);
            if (shift >= 128) return new UInt128(0, 0);
            
            if (shift < 64) {
                long newLow = (low >>> shift) | (high << (64 - shift));
                long newHigh = high >>> shift;
                return new UInt128(newHigh, newLow);
            } else {
                long newLow = high >>> (shift - 64);
                return new UInt128(0, newLow);
            }
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            UInt128 uint128 = (UInt128) obj;
            return high == uint128.high && low == uint128.low;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(high, low);
        }
        
        @Override
        public String toString() {
            return String.format("%016X%016X", high, low);
        }
        
        // Getter方法
        public long getHigh() { return high; }
        public long getLow() { return low; }
    }
    
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
    public static int totalNQueens(int n) {
        return solveNQueens(n, 0, 0, 0, 0);
    }
    
    private static int solveNQueens(int n, int row, int columns, int diagonals1, int diagonals2) {
        // 基线条件：所有皇后都已放置
        if (row == n) {
            return 1;
        }
        
        int count = 0;
        // 获取可用的位置（位为0表示可用）
        int availablePositions = ((1 << n) - 1) & ~(columns | diagonals1 | diagonals2);
        
        while (availablePositions != 0) {
            // 获取最低位的1（选择一个可用位置）
            int position = availablePositions & -availablePositions;
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
    public static int tspDP(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        int totalStates = 1 << n;
        int[][] dp = new int[totalStates][n];
        
        // 初始化DP数组
        for (int i = 0; i < totalStates; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        
        // 起点状态：只访问了城市0
        dp[1][0] = 0;
        
        // 遍历所有状态
        for (int mask = 1; mask < totalStates; mask++) {
            for (int last = 0; last < n; last++) {
                // 如果last不在mask中，跳过
                if ((mask & (1 << last)) == 0) continue;
                
                // 如果当前状态不可达，跳过
                if (dp[mask][last] == Integer.MAX_VALUE) continue;
                
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
        int minDistance = Integer.MAX_VALUE;
        
        for (int last = 0; last < n; last++) {
            if (dp[finalMask][last] != Integer.MAX_VALUE) {
                minDistance = Math.min(minDistance, 
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
    public static int minPushBox(char[][] grid) {
        int m = grid.length, n = grid[0].length;
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
        StateCache<Boolean> visited = new StateCache<>();
        
        // BFS队列：[玩家x, 玩家y, 箱子x, 箱子y, 推动次数]
        Queue<int[]> queue = new LinkedList<>();
        
        // 初始状态
        long initialStateHash = ZobristHashing.calculateHash(new int[]{playerX, playerY, boxX, boxY});
        queue.offer(new int[]{playerX, playerY, boxX, boxY, 0});
        visited.put(initialStateHash, true);
        
        // 四个方向：上、右、下、左
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int px = current[0], py = current[1], bx = current[2], by = current[3], pushes = current[4];
            
            // 到达目标位置
            if (bx == targetX && by == targetY) {
                return pushes;
            }
            
            // 尝试四个方向移动
            for (int[] dir : directions) {
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
                    long newStateHash = ZobristHashing.calculateHash(new int[]{newX, newY, newBoxX, newBoxY});
                    if (!visited.contains(newStateHash)) {
                        visited.put(newStateHash, true);
                        queue.offer(new int[]{newX, newY, newBoxX, newBoxY, pushes + 1});
                    }
                } else {
                    // 玩家移动但不推动箱子
                    long newStateHash = ZobristHashing.calculateHash(new int[]{newX, newY, bx, by});
                    if (!visited.contains(newStateHash)) {
                        visited.put(newStateHash, true);
                        queue.offer(new int[]{newX, newY, bx, by, pushes});
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
    public static int shortestPathAllKeys(String[] grid) {
        int m = grid.length, n = grid[0].length();
        int allKeys = 0;
        int startX = -1, startY = -1;
        
        // 找到起点和所有钥匙
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i].charAt(j);
                if (c == '@') {
                    startX = i;
                    startY = j;
                } else if (c >= 'a' && c <= 'f') {
                    allKeys |= (1 << (c - 'a'));
                }
            }
        }
        
        // BFS搜索
        int[][][] dist = new int[m][n][1 << 6];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < (1 << 6); k++) {
                    dist[i][j][k] = -1;
                }
            }
        }
        
        dist[startX][startY][0] = 0;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startX, startY, 0});
        
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], keys = current[2];
            int distance = dist[x][y][keys];
            
            if (keys == allKeys) {
                return distance;
            }
            
            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n) continue;
                
                char c = grid[nx].charAt(ny);
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
                    queue.offer(new int[]{nx, ny, newKeys});
                }
            }
        }
        
        return -1;
    }
    
    public static void main(String[] args) {
        System.out.println("=== 状态工程技术测试 ===");
        
        // 测试位压缩
        System.out.println("\n1. 位压缩测试:");
        long state = 0L;
        System.out.println("初始状态: " + Long.toBinaryString(state));
        
        // 设置一些位
        state = BitCompression.setBit(state, 3);
        state = BitCompression.setBit(state, 7);
        state = BitCompression.setBit(state, 15);
        System.out.println("设置位3,7,15后: " + Long.toBinaryString(state));
        
        // 检查位
        System.out.println("位3是否为1: " + BitCompression.isBitSet(state, 3));
        System.out.println("位5是否为1: " + BitCompression.isBitSet(state, 5));
        
        // 计算1的个数
        System.out.println("1的个数: " + BitCompression.countBits(state));
        
        // 翻转位
        state = BitCompression.toggleBit(state, 3);
        System.out.println("翻转位3后: " + Long.toBinaryString(state));
        System.out.println("位3是否为1: " + BitCompression.isBitSet(state, 3));
        
        // 测试Zobrist哈希
        System.out.println("\n2. Zobrist哈希测试:");
        int[] board = new int[8];
        Arrays.fill(board, -1); // -1表示空位
        board[0] = 1; // 位置0放置类型1的棋子
        board[3] = 2; // 位置3放置类型2的棋子
        board[7] = 3; // 位置7放置类型3的棋子
        
        long hash1 = ZobristHashing.calculateHash(board);
        System.out.println("初始哈希值: " + hash1);
        
        // 移动棋子
        long hash2 = ZobristHashing.updateHash(hash1, 0, 1, -1); // 移走位置0的棋子
        hash2 = ZobristHashing.updateHash(hash2, 1, -1, 1); // 在位置1放置棋子
        System.out.println("移动后哈希值: " + hash2);
        
        // 验证一致性
        board[0] = -1;
        board[1] = 1;
        long hash3 = ZobristHashing.calculateHash(board);
        System.out.println("重新计算哈希值: " + hash3);
        System.out.println("一致性验证: " + (hash2 == hash3 ? "通过" : "失败"));
        
        // 测试状态缓存
        System.out.println("\n3. 状态缓存测试:");
        StateCache<String> cache = new StateCache<>();
        
        // 添加一些状态
        cache.put(hash1, "状态1");
        cache.put(hash2, "状态2");
        cache.put(12345L, "状态3");
        
        System.out.println("缓存大小: " + cache.size());
        System.out.println("查找存在的状态: " + cache.get(hash1));
        System.out.println("查找不存在的状态: " + cache.get(99999L));
        
        // 测试命中率
        cache.get(hash1); // 命中
        cache.get(99999L); // 未命中
        cache.get(hash2); // 命中
        System.out.println(cache.getStats());
        
        // 测试128位整数
        System.out.println("\n4. 128位整数测试:");
        UInt128 uint128 = new UInt128();
        System.out.println("初始值: " + uint128);
        
        // 设置一些位
        uint128 = uint128.setBit(3).setBit(67).setBit(127);
        System.out.println("设置位3,67,127后: " + uint128);
        
        // 检查位
        System.out.println("位3是否为1: " + uint128.isBitSet(3));
        System.out.println("位64是否为1: " + uint128.isBitSet(64));
        
        // 位运算测试
        UInt128 a = new UInt128(0x1234567890ABCDEFL, 0xFEDCBA0987654321L);
        UInt128 b = new UInt128(0xAAAAAAAAAAAAAAAAL, 0x5555555555555555L);
        System.out.println("a: " + a);
        System.out.println("b: " + b);
        System.out.println("a & b: " + a.and(b));
        System.out.println("a | b: " + a.or(b));
        System.out.println("a ^ b: " + a.xor(b));
        
        // 移位测试
        System.out.println("a << 5: " + a.leftShift(5));
        System.out.println("a >> 5: " + a.rightShift(5));
        
        // 测试N皇后问题
        System.out.println("\n5. N皇后问题测试:");
        for (int n = 1; n <= 8; n++) {
            System.out.printf("%d皇后问题的解决方案数量: %d\n", n, totalNQueens(n));
        }
        
        // 测试旅行商问题
        System.out.println("\n6. 旅行商问题测试:");
        int[][] graph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        System.out.printf("4城市TSP最短路径长度: %d\n", tspDP(graph));
        
        // 测试获取所有钥匙的最短路径
        System.out.println("\n7. 获取所有钥匙的最短路径测试:");
        String[] grid = {"@.a.#", "###.#", "b.A.B"};
        System.out.printf("网格%s的最短路径长度: %d\n", Arrays.toString(grid), shortestPathAllKeys(grid));
    }
}
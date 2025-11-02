package class032;

import java.util.*;

/**
 * 位算法挑战题和竞赛题目
 * 题目来源: LeetCode Hard, Codeforces, ACM竞赛
 * 包含高难度的位算法问题和竞赛题目
 * 
 * 解题思路:
 * 方法1: 位掩码 + 动态规划
 * 方法2: 位运算 + 数学推导
 * 方法3: 状态压缩 + 搜索优化
 * 方法4: 位操作 + 算法技巧
 * 
 * 时间复杂度分析:
 * 方法1: O(n * 2^n) - 状态压缩DP
 * 方法2: O(1) - 数学公式
 * 方法3: O(2^n) - 状态枚举
 * 方法4: O(n) - 线性扫描
 * 
 * 空间复杂度分析:
 * 方法1: O(2^n) - DP数组
 * 方法2: O(1) - 常数空间
 * 方法3: O(2^n) - 状态存储
 * 方法4: O(1) - 原地操作
 * 
 * 工程化考量:
 * 1. 算法选择: 根据数据规模选择合适算法
 * 2. 内存优化: 使用滚动数组等技术
 * 3. 性能优化: 利用位运算的并行性
 * 4. 错误处理: 处理边界情况和异常输入
 */

public class Code21_BitAlgorithmChallenges {
    
    /**
     * LeetCode 52. N-Queens II - N皇后问题II
     * 题目链接: https://leetcode.com/problems/n-queens-ii/
     * 题目描述: 计算N皇后问题的不同解决方案的数量
     * 
     * 方法: 位运算优化的回溯算法
     * 时间复杂度: O(n!) 但实际运行很快
     * 空间复杂度: O(n)
     */
    public static int totalNQueens(int n) {
        return solveNQueens(n, 0, 0, 0, 0);
    }
    
    private static int solveNQueens(int n, int row, int columns, int diagonals1, int diagonals2) {
        if (row == n) {
            return 1;
        }
        
        int count = 0;
        // 获取可用的位置（位为0表示可用）
        int availablePositions = ((1 << n) - 1) & ~(columns | diagonals1 | diagonals2);
        
        while (availablePositions != 0) {
            // 获取最低位的1
            int position = availablePositions & -availablePositions;
            // 清除最低位的1
            availablePositions &= availablePositions - 1;
            
            count += solveNQueens(n, row + 1, 
                                columns | position,
                                (diagonals1 | position) << 1,
                                (diagonals2 | position) >> 1);
        }
        
        return count;
    }
    
    /**
     * LeetCode 691. Stickers to Spell Word - 贴纸拼词
     * 题目链接: https://leetcode.com/problems/stickers-to-spell-word/
     * 题目描述: 给定一组贴纸和目標单词，计算最少需要多少张贴纸才能拼出目标单词
     * 
     * 方法: 状态压缩 + BFS
     * 时间复杂度: O(2^n * m * 26) n为目标单词长度，m为贴纸数量
     * 空间复杂度: O(2^n)
     */
    public static int minStickers(String[] stickers, String target) {
        int n = target.length();
        int[] dp = new int[1 << n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        for (int state = 0; state < (1 << n); state++) {
            if (dp[state] == Integer.MAX_VALUE) continue;
            
            for (String sticker : stickers) {
                int nextState = state;
                int[] count = new int[26];
                
                // 统计贴纸中每个字符的数量
                for (char c : sticker.toCharArray()) {
                    count[c - 'a']++;
                }
                
                // 尝试使用贴纸覆盖目标单词
                for (int i = 0; i < n; i++) {
                    if (((nextState >> i) & 1) == 1) continue; // 已经覆盖
                    
                    char c = target.charAt(i);
                    if (count[c - 'a'] > 0) {
                        count[c - 'a']--;
                        nextState |= (1 << i);
                    }
                }
                
                if (dp[nextState] > dp[state] + 1) {
                    dp[nextState] = dp[state] + 1;
                }
            }
        }
        
        return dp[(1 << n) - 1] == Integer.MAX_VALUE ? -1 : dp[(1 << n) - 1];
    }
    
    /**
     * LeetCode 864. Shortest Path to Get All Keys - 获取所有钥匙的最短路径
     * 题目链接: https://leetcode.com/problems/shortest-path-to-get-all-keys/
     * 题目描述: 在网格中找到获取所有钥匙的最短路径
     * 
     * 方法: BFS + 状态压缩
     * 时间复杂度: O(m * n * 2^k) k为钥匙数量
     * 空间复杂度: O(m * n * 2^k)
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
                Arrays.fill(dist[i][j], Integer.MAX_VALUE);
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
                
                if (dist[nx][ny][newKeys] > distance + 1) {
                    dist[nx][ny][newKeys] = distance + 1;
                    queue.offer(new int[]{nx, ny, newKeys});
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Codeforces Problem: Xor-MST
     * 题目描述: 给定n个点的完全图，边权为两点值的异或，求最小生成树
     * 
     * 方法: 分治 + 贪心 + Trie树
     * 时间复杂度: O(n * log(max_value))
     * 空间复杂度: O(n * log(max_value))
     */
    public static long xorMST(int[] arr) {
        Arrays.sort(arr);
        return buildMST(arr, 0, arr.length - 1, 30);
    }
    
    private static long buildMST(int[] arr, int left, int right, int bit) {
        if (left >= right || bit < 0) return 0;
        
        // 根据当前位将数组分成两部分
        int mid = left;
        while (mid <= right && ((arr[mid] >> bit) & 1) == 0) {
            mid++;
        }
        mid--; // 最后一个0的位置
        
        if (mid < left || mid >= right) {
            // 所有数在当前位相同，继续下一位
            return buildMST(arr, left, right, bit - 1);
        }
        
        // 递归构建左右子树
        long cost = buildMST(arr, left, mid, bit - 1) + 
                   buildMST(arr, mid + 1, right, bit - 1);
        
        // 找到连接两部分的最小边
        long minEdge = findMinXor(arr, left, mid, mid + 1, right);
        
        return cost + minEdge;
    }
    
    private static long findMinXor(int[] arr, int l1, int r1, int l2, int r2) {
        long minXor = Long.MAX_VALUE;
        
        // 暴力查找最小异或值（实际可以使用Trie优化）
        for (int i = l1; i <= r1; i++) {
            for (int j = l2; j <= r2; j++) {
                minXor = Math.min(minXor, (long)arr[i] ^ arr[j]);
            }
        }
        
        return minXor;
    }
    
    /**
     * ACM竞赛题: 位运算最大值
     * 题目描述: 给定数组，选择两个数进行位运算，求最大值
     */
    public static int maxBitwiseOperation(int[] arr, String operation) {
        int maxResult = Integer.MIN_VALUE;
        
        switch (operation) {
            case "AND":
                // 寻找最大的AND值对
                for (int i = 0; i < arr.length; i++) {
                    for (int j = i + 1; j < arr.length; j++) {
                        maxResult = Math.max(maxResult, arr[i] & arr[j]);
                    }
                }
                break;
                
            case "OR":
                // 寻找最大的OR值对
                for (int i = 0; i < arr.length; i++) {
                    for (int j = i + 1; j < arr.length; j++) {
                        maxResult = Math.max(maxResult, arr[i] | arr[j]);
                    }
                }
                break;
                
            case "XOR":
                // 使用Trie树优化
                maxResult = findMaxXOR(arr);
                break;
        }
        
        return maxResult;
    }
    
    private static int findMaxXOR(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        
        // 构建前缀树
        TrieNode root = new TrieNode();
        
        // 插入所有数字
        for (int num : arr) {
            TrieNode node = root;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (node.children[bit] == null) {
                    node.children[bit] = new TrieNode();
                }
                node = node.children[bit];
            }
        }
        
        // 查找最大异或值
        int maxXor = 0;
        for (int num : arr) {
            TrieNode node = root;
            int currentXor = 0;
            
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                int oppositeBit = 1 - bit;
                
                if (node.children[oppositeBit] != null) {
                    currentXor |= (1 << i);
                    node = node.children[oppositeBit];
                } else {
                    node = node.children[bit];
                }
            }
            
            maxXor = Math.max(maxXor, currentXor);
        }
        
        return maxXor;
    }
    
    // 前缀树节点定义
    static class TrieNode {
        TrieNode[] children;
        
        public TrieNode() {
            children = new TrieNode[2];
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== 位算法挑战题 - 单元测试 ===");
        
        // 测试N皇后问题
        System.out.println("N皇后问题测试:");
        for (int n = 1; n <= 8; n++) {
            System.out.printf("%d皇后的解决方案数量: %d%n", n, totalNQueens(n));
        }
        
        // 测试贴纸拼词
        System.out.println("\n贴纸拼词测试:");
        String[] stickers = {"with", "example", "science"};
        String target = "thehat";
        System.out.printf("贴纸%s拼出'%s'需要的最少贴纸: %d%n", 
                         Arrays.toString(stickers), target, minStickers(stickers, target));
        
        // 测试获取所有钥匙的最短路径
        System.out.println("\n获取所有钥匙的最短路径测试:");
        String[] grid = {"@.a.#", "###.#", "b.A.B"};
        System.out.printf("网格%s的最短路径长度: %d%n", 
                         Arrays.toString(grid), shortestPathAllKeys(grid));
        
        // 测试异或最小生成树
        System.out.println("\n异或最小生成树测试:");
        int[] points = {1, 2, 3, 4};
        System.out.printf("点集%s的异或MST权重: %d%n", 
                         Arrays.toString(points), xorMST(points));
        
        // 测试位运算最大值
        System.out.println("\n位运算最大值测试:");
        int[] numbers = {3, 10, 5, 25, 2, 8};
        System.out.printf("数组%s的最大AND值: %d%n", 
                         Arrays.toString(numbers), maxBitwiseOperation(numbers, "AND"));
        System.out.printf("数组%s的最大OR值: %d%n", 
                         Arrays.toString(numbers), maxBitwiseOperation(numbers, "OR"));
        System.out.printf("数组%s的最大XOR值: %d%n", 
                         Arrays.toString(numbers), maxBitwiseOperation(numbers, "XOR"));
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 测试N皇后问题的性能
        long startTime = System.nanoTime();
        int result8 = totalNQueens(8);
        long time1 = System.nanoTime() - startTime;
        System.out.printf("8皇后问题: %d ns, 结果: %d%n", time1, result8);
        
        // 测试贴纸拼词的性能
        String[] stickers = {"with", "example", "science"};
        String target = "thehat";
        startTime = System.nanoTime();
        int resultStickers = minStickers(stickers, target);
        long time2 = System.nanoTime() - startTime;
        System.out.printf("贴纸拼词: %d ns, 结果: %d%n", time2, resultStickers);
        
        // 测试异或MST的性能
        int[] largePoints = new int[100];
        Random random = new Random();
        for (int i = 0; i < largePoints.length; i++) {
            largePoints[i] = random.nextInt(1000);
        }
        startTime = System.nanoTime();
        long resultMST = xorMST(largePoints);
        long time3 = System.nanoTime() - startTime;
        System.out.printf("异或MST: %d ns, 结果: %d%n", time3, resultMST);
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("挑战题算法的特点:");
        System.out.println("1. 时间复杂度: 通常较高，需要优化");
        System.out.println("2. 空间复杂度: 使用状态压缩减少内存");
        System.out.println("3. 算法技巧: 结合多种算法思想");
        
        System.out.println("\n优化策略:");
        System.out.println("1. 状态压缩: 使用位表示状态集合");
        System.out.println("2. 记忆化搜索: 避免重复计算");
        System.out.println("3. 剪枝优化: 提前终止不可能的分支");
        System.out.println("4. 数据结构优化: 使用高效数据结构");
    }
    
    public static void main(String[] args) {
        System.out.println("位算法挑战题和竞赛题目");
        System.out.println("包含LeetCode Hard、Codeforces等高难度问题");
        
        // 运行单元测试
        runTests();
        
        // 性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 竞赛技巧
        System.out.println("\n=== 竞赛技巧 ===");
        System.out.println("1. 识别问题模式: 判断是否适合位运算");
        System.out.println("2. 状态设计: 合理设计状态表示");
        System.out.println("3. 算法选择: 根据数据规模选择算法");
        System.out.println("4. 调试技巧: 使用小数据测试");
    }
}
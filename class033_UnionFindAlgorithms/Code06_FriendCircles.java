package class056;

/**
 * 朋友圈
 * 班上有 N 名学生。其中有些人是朋友，有些则不是。他们的友谊具有是传递性。
 * 如果已知 A 是 B 的朋友，B 是 C 的朋友，那么我们可以认为 A 也是 C 的朋友。
 * 所谓的朋友圈，是指所有朋友都属于同一个圈子里。
 * 给定一个 N * N 的矩阵 M，表示班级中学生之间的朋友关系。
 * 如果M[i][j] = 1，表示已知第 i 个和 j 个学生互为朋友关系，否则为不知道他们是否为朋友。
 * 返回所有朋友圈的数量。
 * 
 * 示例 1:
 * 输入: 
 * [[1,1,0],
 *  [1,1,0],
 *  [0,0,1]]
 * 输出: 2 
 * 说明：已知学生0和学生1互为朋友，他们在一个朋友圈。
 * 第2个学生自己在一个朋友圈。所以返回2。
 * 
 * 示例 2:
 * 输入: 
 * [[1,1,0],
 *  [1,1,1],
 *  [0,1,1]]
 * 输出: 1
 * 说明：已知学生0和学生1互为朋友，学生1和学生2互为朋友，
 * 所以学生0和学生2也是朋友，所以他们三个人在一个朋友圈，返回1。
 * 
 * 约束条件：
 * 1 <= n <= 200
 * M[i][i] == 1
 * M[i][j] == M[j][i]
 * 
 * 测试链接: https://leetcode.cn/problems/friend-circles/
 * 相关平台: LeetCode 547, LintCode 1045, 牛客网, HackerRank
 */
public class Code06_FriendCircles {
    
    /**
     * 使用并查集解决朋友圈问题
     * 
     * 解题思路：
     * 1. 初始化并查集，每个学生初始时都是独立的集合
     * 2. 遍历朋友关系矩阵，如果两个人是朋友，则将他们所在的集合合并
     * 3. 最终集合的数量就是朋友圈的数量
     * 
     * 时间复杂度：O(N^2 * α(N))，其中N是学生数量，α是阿克曼函数的反函数，近似为常数
     * 空间复杂度：O(N)
     * 
     * @param M 朋友关系矩阵
     * @return 朋友圈数量
     */
    public static int findCircleNum(int[][] M) {
        if (M == null || M.length == 0) {
            return 0;
        }
        
        int n = M.length;
        UnionFind unionFind = new UnionFind(n);
        
        // 遍历矩阵的上三角部分（因为矩阵是对称的）
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 如果i和j是朋友，则合并他们的集合
                if (M[i][j] == 1) {
                    unionFind.union(i, j);
                }
            }
        }
        
        // 返回集合数量，即朋友圈数量
        return unionFind.getSetCount();
    }
    
    /**
     * 并查集数据结构实现
     * 包含路径压缩和按秩合并优化
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界
        private int setCount;  // 当前集合数量
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            setCount = n;
            
            // 初始时每个节点都是自己的父节点
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;  // 初始时每个树的秩为1
            }
        }
        
        /**
         * 查找节点的根节点（代表元素）
         * 使用路径压缩优化
         * @param x 要查找的节点
         * @return 节点x所在集合的根节点
         */
        public int find(int x) {
            if (parent[x] != x) {
                // 路径压缩：将路径上的所有节点直接连接到根节点
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        /**
         * 合并两个集合
         * 使用按秩合并优化
         * @param x 第一个节点
         * @param y 第二个节点
         */
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一个集合中，则无需合并
            if (rootX != rootY) {
                // 按秩合并：将秩小的树合并到秩大的树下
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    // 秩相等时，任选一个作为根，并将其秩加1
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
                // 集合数量减1
                setCount--;
            }
        }
        
        /**
         * 判断两个节点是否在同一个集合中
         * @param x 第一个节点
         * @param y 第二个节点
         * @return 如果在同一个集合中返回true，否则返回false
         */
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
        
        /**
         * 获取当前集合数量
         * @return 集合数量
         */
        public int getSetCount() {
            return setCount;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] M1 = {
            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 1}
        };
        System.out.println("测试用例1结果: " + findCircleNum(M1)); // 预期输出: 2
        
        // 测试用例2
        int[][] M2 = {
            {1, 1, 0},
            {1, 1, 1},
            {0, 1, 1}
        };
        System.out.println("测试用例2结果: " + findCircleNum(M2)); // 预期输出: 1
        
        // 测试用例3：单个学生
        int[][] M3 = {{1}};
        System.out.println("测试用例3结果: " + findCircleNum(M3)); // 预期输出: 1
        
        // 测试用例4：所有学生都互为朋友
        int[][] M4 = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        System.out.println("测试用例4结果: " + findCircleNum(M4)); // 预期输出: 1
    }
}
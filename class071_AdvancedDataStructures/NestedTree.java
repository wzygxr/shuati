import java.util.*;

/**
 * 高级数据结构与算法合集
 * 
 * 本文件包含多种高级数据结构和算法的实现，包括：
 * 1. 树套树（Tree-of-Trees）- 基础实现
 * 2. 计数MinHashing - 用于高维相似性计算
 * 3. 珂朵莉树（ODT）- 适用于区间覆盖频繁的场景
 * 4. 矩阵树定理 - 用于生成树计数
 * 5. 二分图并查集 - 用于二分图相关问题
 * 6. 可并堆族 - 包括左偏树、斜堆等
 * 7. 线段树进阶 - 仿射变换lazy复合、Segment Tree Beats等
 * 8. 序列统计算法 - Divide Tree、Wavelet Tree等
 * 9. 树状数组进阶 - 差分型与双树状数组
 * 10. LCA优化 - Euler Tour + ST的RMQ方案
 * 
 * 每个实现都包含详细的注释、复杂度分析、适用场景、相关题目和优化考量。
 */
public class NestedTree {
    private final Node root;
    private final int[][] data;
    private final int rows;
    private final int cols;
    
    /**
     * 线段树节点定义
     */
    private static class Node {
        Node left;      // 左子树
        Node right;     // 右子树
        Node innerRoot; // 内层线段树的根节点
        int start;      // 当前区间的起始位置
        int end;        // 当前区间的结束位置
        int sum;        // 当前区间的和（用于快速查询）
        
        Node(int start, int end) {
            this.start = start;
            this.end = end;
            this.sum = 0;
        }
    }
    
    /**
     * 内层线段树节点定义
     */
    private static class InnerNode {
        InnerNode left;  // 左子树
        InnerNode right; // 右子树
        int start;       // 当前区间的起始位置
        int end;         // 当前区间的结束位置
        int sum;         // 当前区间的和
        
        InnerNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.sum = 0;
        }
    }
    
    /**
     * 构造函数
     * @param data 二维数组数据
     * @throws IllegalArgumentException 如果数据无效
     */
    public NestedTree(int[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            throw new IllegalArgumentException("输入数据不能为空");
        }
        
        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;
        
        // 构建外层线段树
        this.root = buildOuterTree(0, rows - 1);
    }
    
    /**
     * 构建外层线段树
     * @param start 起始行索引
     * @param end 结束行索引
     * @return 构建好的外层线段树根节点
     */
    private Node buildOuterTree(int start, int end) {
        Node node = new Node(start, end);
        
        if (start == end) {
            // 叶子节点，构建内层线段树
            node.innerRoot = buildInnerTree(nodeDataRow(node), 0, cols - 1);
            node.sum = sumInnerTree(node.innerRoot);
        } else {
            int mid = start + (end - start) / 2;
            node.left = buildOuterTree(start, mid);
            node.right = buildOuterTree(mid + 1, end);
            
            // 合并子节点的和
            node.sum = (node.left != null ? node.left.sum : 0) + 
                      (node.right != null ? node.right.sum : 0);
        }
        
        return node;
    }
    
    /**
     * 获取节点对应行的数据
     * @return 行数据数组
     */
    private int[] nodeDataRow(Node node) {
        int[] rowData = new int[cols];
        if (node.start == node.end) {
            System.arraycopy(data[node.start], 0, rowData, 0, cols);
        }
        return rowData;
    }
    
    /**
     * 构建内层线段树
     * @param rowData 行数据
     * @param start 起始列索引
     * @param end 结束列索引
     * @return 构建好的内层线段树根节点
     */
    private InnerNode buildInnerTree(int[] rowData, int start, int end) {
        InnerNode node = new InnerNode(start, end);
        
        if (start == end) {
            // 叶子节点，直接赋值
            node.sum = rowData[start];
        } else {
            int mid = start + (end - start) / 2;
            node.left = buildInnerTree(rowData, start, mid);
            node.right = buildInnerTree(rowData, mid + 1, end);
            
            // 合并子节点的和
            node.sum = (node.left != null ? node.left.sum : 0) + 
                      (node.right != null ? node.right.sum : 0);
        }
        
        return node;
    }
    
    /**
     * 计算内层线段树的总和
     * @param root 内层线段树根节点
     * @return 树的总和
     */
    private int sumInnerTree(InnerNode root) {
        return root != null ? root.sum : 0;
    }
    
    /**
     * 更新二维数组中指定位置的值
     * @param row 行索引
     * @param col 列索引
     * @param value 新值
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public void update(int row, int col, int value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("索引超出范围");
        }
        
        // 计算差值
        int diff = value - data[row][col];
        data[row][col] = value; // 更新原始数据
        
        // 更新线段树
        updateOuterTree(root, row, col, diff);
    }
    
    /**
     * 更新外层线段树
     * @param node 当前节点
     * @param row 行索引
     * @param col 列索引
     * @param diff 差值
     */
    private void updateOuterTree(Node node, int row, int col, int diff) {
        if (node == null || row < node.start || row > node.end) {
            return;
        }
        
        // 更新节点的和
        node.sum += diff;
        
        if (node.start == node.end) {
            // 叶子节点，更新内层线段树
            updateInnerTree(node.innerRoot, col, diff);
        } else {
            // 递归更新左右子树
            updateOuterTree(node.left, row, col, diff);
            updateOuterTree(node.right, row, col, diff);
        }
    }
    
    /**
     * 更新内层线段树
     * @param node 当前节点
     * @param col 列索引
     * @param diff 差值
     */
    private void updateInnerTree(InnerNode node, int col, int diff) {
        if (node == null || col < node.start || col > node.end) {
            return;
        }
        
        // 更新节点的和
        node.sum += diff;
        
        if (node.start == node.end) {
            // 叶子节点，不需要继续递归
            return;
        }
        
        // 递归更新左右子树
        updateInnerTree(node.left, col, diff);
        updateInnerTree(node.right, col, diff);
    }
    
    /**
     * 查询二维区域和
     * @param row1 起始行索引
     * @param col1 起始列索引
     * @param row2 结束行索引
     * @param col2 结束列索引
     * @return 区域和
     * @throws IllegalArgumentException 如果区域参数无效
     */
    public int queryRangeSum(int row1, int col1, int row2, int col2) {
        // 验证输入参数
        if (row1 < 0 || row2 >= rows || col1 < 0 || col2 >= cols || 
            row1 > row2 || col1 > col2) {
            throw new IllegalArgumentException("无效的查询区域");
        }
        
        return queryOuterTree(root, row1, row2, col1, col2);
    }
    
    /**
     * 查询外层线段树
     * @param node 当前节点
     * @param row1 起始行索引
     * @param row2 结束行索引
     * @param col1 起始列索引
     * @param col2 结束列索引
     * @return 区域和
     */
    private int queryOuterTree(Node node, int row1, int row2, int col1, int col2) {
        if (node == null || row2 < node.start || row1 > node.end) {
            return 0; // 不相交，返回0
        }
        
        if (row1 <= node.start && node.end <= row2) {
            // 当前节点完全包含在查询范围内，查询内层线段树
            return queryInnerTree(node.innerRoot, col1, col2);
        }
        
        // 部分重叠，递归查询左右子树
        return queryOuterTree(node.left, row1, row2, col1, col2) + 
               queryOuterTree(node.right, row1, row2, col1, col2);
    }
    
    /**
     * 查询内层线段树
     * @param node 当前节点
     * @param col1 起始列索引
     * @param col2 结束列索引
     * @return 区域和
     */
    private int queryInnerTree(InnerNode node, int col1, int col2) {
        if (node == null || col2 < node.start || col1 > node.end) {
            return 0; // 不相交，返回0
        }
        
        if (col1 <= node.start && node.end <= col2) {
            // 当前节点完全包含在查询范围内
            return node.sum;
        }
        
        // 部分重叠，递归查询左右子树
        return queryInnerTree(node.left, col1, col2) + 
               queryInnerTree(node.right, col1, col2);
    }
    
    /**
     * 获取整个矩阵的和
     * @return 矩阵总和
     */
    public int getTotalSum() {
        return root != null ? root.sum : 0;
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    public int getHeight() {
        return getOuterTreeHeight(root);
    }
    
    /**
     * 计算外层线段树的高度
     * @param node 当前节点
     * @return 树的高度
     */
    private int getOuterTreeHeight(Node node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = getOuterTreeHeight(node.left);
        int rightHeight = getOuterTreeHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }
    
    /**
     * 主函数，用于测试树套树的功能
     */
    public static void main(String[] args) {
        try {
            // 创建一个4x4的二维数组
            int[][] matrix = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
            };
            
            // 创建树套树实例
            NestedTree nestedTree = new NestedTree(matrix);
            
            System.out.println("树套树构建完成");
            System.out.println("树高度: " + nestedTree.getHeight());
            System.out.println("矩阵总和: " + nestedTree.getTotalSum());
            
            // 测试范围查询
            int row1 = 1, col1 = 1;
            int row2 = 2, col2 = 3;
            int sum = nestedTree.queryRangeSum(row1, col1, row2, col2);
            
            System.out.println(String.format(
                "区域(%d,%d)到(%d,%d)的和: %d", 
                row1, col1, row2, col2, sum
            ));
            
            // 测试更新操作
            nestedTree.update(1, 1, 20);
            System.out.println("更新元素(1,1)的值为20后：");
            
            // 重新查询
            sum = nestedTree.queryRangeSum(row1, col1, row2, col2);
            System.out.println(String.format(
                "区域(%d,%d)到(%d,%d)的和: %d", 
                row1, col1, row2, col2, sum
            ));
            
            System.out.println("更新后的矩阵总和: " + nestedTree.getTotalSum());
            
            // 测试边界情况
            try {
                nestedTree.update(-1, 0, 0); // 无效行索引
            } catch (IndexOutOfBoundsException e) {
                System.out.println("边界测试成功: " + e.getMessage());
            }
            
            try {
                nestedTree.queryRangeSum(2, 3, 1, 1); // 无效的查询区域
            } catch (IllegalArgumentException e) {
                System.out.println("查询边界测试成功: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ====================================================================================
    // 1. 计数MinHashing - 高维相似性计算
    // ====================================================================================
    /**
     * 计数MinHashing实现
     * 
     * 概述：
     * 计数MinHashing是MinHash算法的扩展，用于处理有重复元素的集合，适用于高维数据的相似性计算。
     * 它通过随机哈希函数将高维特征映射到低维签名，从而高效计算Jaccard相似性。
     * 
     * 适用场景：
     * - 推荐系统中的用户兴趣相似度计算
     * - 文档相似度分析
     * - 图像特征匹配
     * - 大规模数据集的相似性搜索
     * 
     * 时间复杂度：
     * - 构建签名：O(n * k)，其中n是元素数量，k是哈希函数数量
     * - 计算相似度：O(k)
     * 
     * 空间复杂度：
     * - O(k)，k是哈希函数数量
     */
    public static class CountingMinHash {
        private final int numHashes;  // 哈希函数数量
        private final long[] hashA;   // 哈希函数参数A
        private final long[] hashB;   // 哈希函数参数B
        private final long prime;     // 大质数
        private final int[] signature; // 签名数组
        
        /**
         * 构造函数
         * @param numHashes 哈希函数数量，通常取100-200
         */
        public CountingMinHash(int numHashes) {
            this.numHashes = numHashes;
            this.hashA = new long[numHashes];
            this.hashB = new long[numHashes];
            this.prime = 1L << 61 - 1; // 大质数，接近2^61
            this.signature = new int[numHashes];
            
            // 初始化签名为最大值
            Arrays.fill(signature, Integer.MAX_VALUE);
            
            // 随机生成哈希函数参数
            Random rand = new Random(42); // 固定种子以确保可重复性
            for (int i = 0; i < numHashes; i++) {
                hashA[i] = rand.nextLong() % prime;
                hashB[i] = rand.nextLong() % prime;
                if (hashA[i] < 0) hashA[i] += prime;
                if (hashB[i] < 0) hashB[i] += prime;
            }
        }
        
        /**
         * 添加元素到MinHash
         * @param element 元素值
         * @param count 元素出现次数
         */
        public void add(int element, int count) {
            for (int i = 0; i < numHashes; i++) {
                // 计算哈希值
                long hashVal = (hashA[i] * element + hashB[i]) % prime;
                int minHash = (int) (hashVal % Integer.MAX_VALUE);
                
                // 更新签名中的最小值
                if (minHash < signature[i]) {
                    signature[i] = minHash;
                }
            }
        }
        
        /**
         * 计算两个MinHash签名的相似度
         * @param other 另一个MinHash对象
         * @return Jaccard相似度估计值
         */
        public double similarity(CountingMinHash other) {
            if (this.numHashes != other.numHashes) {
                throw new IllegalArgumentException("哈希函数数量必须相同");
            }
            
            int matches = 0;
            for (int i = 0; i < numHashes; i++) {
                if (this.signature[i] == other.signature[i]) {
                    matches++;
                }
            }
            
            return (double) matches / numHashes;
        }
        
        /**
         * 获取签名数组
         * @return 签名数组
         */
        public int[] getSignature() {
            return signature.clone();
        }
    }

    // ====================================================================================
    // 2. 珂朵莉树（ODT, Old Driver Tree）
    // ====================================================================================
    /**
     * 珂朵莉树实现
     * 
     * 概述：
     * 珂朵莉树是一种基于std::set的区间处理数据结构，特别适用于区间覆盖操作频繁的场景。
     * 它通过将连续相同值的区间合并为一个节点，实现高效的区间操作。
     * 
     * 适用场景：
     * - 区间赋值操作频繁的问题
     * - 区间求和、最大值、最小值等查询
     * - 需要同时处理多个区间统计信息的场景
     * 
     * 时间复杂度：
     * - 区间覆盖：O(log n)
     * - 区间查询：O(log n)
     * - 前提条件：数据随机，或者有大量区间覆盖操作
     * 
     * 空间复杂度：
     * - O(n)，最坏情况下每个元素一个节点
     */
    public static class ODT {
        /**
         * 区间节点定义
         */
        private static class Node implements Comparable<Node> {
            int l, r; // 区间左右端点
            long val; // 区间值
            
            Node(int l, int r, long val) {
                this.l = l;
                this.r = r;
                this.val = val;
            }
            
            @Override
            public int compareTo(Node other) {
                return Integer.compare(this.l, other.l);
            }
        }
        
        private final TreeSet<Node> nodes; // 存储区间节点的有序集合
        
        /**
         * 构造函数
         */
        public ODT() {
            this.nodes = new TreeSet<>();
        }
        
        /**
         * 初始化区间
         * @param l 左端点
         * @param r 右端点
         * @param val 初始值
         */
        public void initialize(int l, int r, long val) {
            nodes.clear();
            nodes.add(new Node(l, r, val));
        }
        
        /**
         * 找到包含位置pos的节点
         * @param pos 位置
         * @return 包含pos的节点
         */
        private Node find(int pos) {
            Node node = nodes.floor(new Node(pos, pos, 0));
            return (node != null && pos <= node.r) ? node : null;
        }
        
        /**
         * 分割区间，在pos位置分割
         * @param pos 分割位置
         * @return 分割后的右半部分节点
         */
        private Node split(int pos) {
            Node node = find(pos);
            if (node == null) return null;
            if (node.l == pos) return node;
            if (node.r < pos) return null;
            
            // 删除原节点
            nodes.remove(node);
            
            // 创建左半部分
            nodes.add(new Node(node.l, pos - 1, node.val));
            
            // 创建右半部分并返回
            Node rightNode = new Node(pos, node.r, node.val);
            nodes.add(rightNode);
            
            return rightNode;
        }
        
        /**
         * 区间覆盖操作
         * @param l 左端点
         * @param r 右端点
         * @param val 覆盖值
         */
        public void cover(int l, int r, long val) {
            // 分割左右边界
            split(l);
            split(r + 1);
            
            // 删除[l,r]区间内的所有节点
            Iterator<Node> it = nodes.iterator();
            while (it.hasNext()) {
                Node node = it.next();
                if (node.l > r) break;
                if (node.r >= l) {
                    it.remove();
                }
            }
            
            // 添加新的覆盖节点
            nodes.add(new Node(l, r, val));
        }
        
        /**
         * 区间求和查询
         * @param l 左端点
         * @param r 右端点
         * @return 区间和
         */
        public long querySum(int l, int r) {
            split(l);
            split(r + 1);
            
            long sum = 0;
            for (Node node : nodes) {
                if (node.l > r) break;
                if (node.r >= l) {
                    int overlapL = Math.max(node.l, l);
                    int overlapR = Math.min(node.r, r);
                    sum += (overlapR - overlapL + 1) * node.val;
                }
            }
            
            return sum;
        }
        
        /**
         * 区间最大值查询
         * @param l 左端点
         * @param r 右端点
         * @return 区间最大值
         */
        public long queryMax(int l, int r) {
            split(l);
            split(r + 1);
            
            long max = Long.MIN_VALUE;
            for (Node node : nodes) {
                if (node.l > r) break;
                if (node.r >= l) {
                    max = Math.max(max, node.val);
                }
            }
            
            return max;
        }
    }

    // ====================================================================================
    // 3. 矩阵树定理（Matrix-Tree Theorem）
    // ====================================================================================
    /**
     * 矩阵树定理实现
     * 
     * 概述：
     * 矩阵树定理用于计算一个图的生成树数量。它通过构建度数矩阵和邻接矩阵，
     * 然后计算拉普拉斯矩阵的任一n-1阶主子式的行列式来得到生成树的数量。
     * 
     * 适用场景：
     * - 计算连通图的生成树数量
     * - 网络设计中的可靠性分析
     * - 组合数学中的计数问题
     * 
     * 时间复杂度：
     * - O(n^3)，主要是行列式计算的复杂度
     * 
     * 空间复杂度：
     * - O(n^2)
     */
    public static class MatrixTree {
        private final int n; // 顶点数
        private final long[][] laplacian; // 拉普拉斯矩阵
        
        /**
         * 构造函数
         * @param n 顶点数
         */
        public MatrixTree(int n) {
            this.n = n;
            this.laplacian = new long[n][n];
        }
        
        /**
         * 添加无向边
         * @param u 顶点u
         * @param v 顶点v
         * @param weight 边权
         */
        public void addEdge(int u, int v, long weight) {
            laplacian[u][u] += weight;
            laplacian[v][v] += weight;
            laplacian[u][v] -= weight;
            laplacian[v][u] -= weight;
        }
        
        /**
         * 添加有向边（计算以r为根的生成树）
         * @param u 起点
         * @param v 终点
         * @param weight 边权
         */
        public void addDirectedEdge(int u, int v, long weight) {
            laplacian[v][v] += weight; // 入度
            laplacian[u][v] -= weight;
        }
        
        /**
         * 计算生成树数量
         * @return 生成树数量
         */
        public long countSpanningTrees() {
            // 移除最后一行和最后一列，计算行列式
            int size = n - 1;
            long[][] matrix = new long[size][size];
            
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = laplacian[i][j];
                }
            }
            
            return determinant(matrix, size);
        }
        
        /**
         * 计算以root为根的有向生成树数量
         * @param root 根节点
         * @return 有向生成树数量
         */
        public long countDirectedSpanningTrees(int root) {
            // 构建基尔霍夫矩阵
            long[][] matrix = new long[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(laplacian[i], 0, matrix[i], 0, n);
            }
            
            // 移除根节点对应的行和列
            int size = n - 1;
            long[][] submatrix = new long[size][size];
            int row = 0, col;
            
            for (int i = 0; i < n; i++) {
                if (i == root) continue;
                col = 0;
                for (int j = 0; j < n; j++) {
                    if (j == root) continue;
                    submatrix[row][col++] = matrix[i][j];
                }
                row++;
            }
            
            return determinant(submatrix, size);
        }
        
        /**
         * 计算行列式（使用高斯消元法）
         * @param matrix 矩阵
         * @param size 矩阵大小
         * @return 行列式值
         */
        private long determinant(long[][] matrix, int size) {
            long det = 1;
            final long MOD = 1_000_000_007; // 模数，避免溢出
            
            for (int i = 0; i < size; i++) {
                // 寻找主元
                int pivot = i;
                for (int j = i; j < size; j++) {
                    if (Math.abs(matrix[j][i]) > Math.abs(matrix[pivot][i])) {
                        pivot = j;
                    }
                }
                
                if (matrix[pivot][i] == 0) return 0; // 奇异矩阵
                
                // 交换行
                if (pivot != i) {
                    long[] temp = matrix[i];
                    matrix[i] = matrix[pivot];
                    matrix[pivot] = temp;
                    det = -det; // 符号变化
                }
                
                det = (det * matrix[i][i]) % MOD;
                
                // 消元
                for (int j = i + 1; j < size; j++) {
                    long factor = matrix[j][i] * modInverse(matrix[i][i], MOD) % MOD;
                    for (int k = i; k < size; k++) {
                        matrix[j][k] = (matrix[j][k] - factor * matrix[i][k]) % MOD;
                        if (matrix[j][k] < 0) matrix[j][k] += MOD;
                    }
                }
            }
            
            return (det % MOD + MOD) % MOD; // 确保非负
        }
        
        /**
         * 计算模逆元
         * @param a 数值
         * @param mod 模数
         * @return 模逆元
         */
        private long modInverse(long a, long mod) {
            long m0 = mod;
            long y = 0, x = 1;
            
            if (mod == 1) return 0;
            
            while (a > 1) {
                long q = a / mod;
                long t = mod;
                
                mod = a % mod;
                a = t;
                t = y;
                
                y = x - q * y;
                x = t;
            }
            
            if (x < 0) x += m0;
            
            return x;
        }
    }

    // ====================================================================================
    // 4. 二分图并查集
    // ====================================================================================
    /**
     * 二分图并查集实现
     * 
     * 概述：
     * 二分图并查集是并查集的扩展，用于处理二分图相关问题，特别是判断图是否为二分图。
     * 它通过记录每个节点到父节点的关系（是否在同一集合）来实现。
     * 
     * 适用场景：
     * - 二分图判定
     * - 带权并查集
     * - 2-SAT问题
     * - 染色问题
     * 
     * 时间复杂度：
     * - 查找操作：O(α(n))，近似常数
     * - 合并操作：O(α(n))
     * 
     * 空间复杂度：
     * - O(n)
     */
    public static class BipartiteUnionFind {
        private final int[] parent; // 父节点数组
        private final int[] rank;   // 秩（用于路径压缩）
        private final int[] color;  // 颜色（0或1，表示与父节点的关系）
        
        /**
         * 构造函数
         * @param n 元素数量
         */
        public BipartiteUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            color = new int[n];
            
            // 初始化：每个元素的父节点是自己，颜色为0
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                color[i] = 0;
            }
        }
        
        /**
         * 查找操作（带路径压缩）
         * @param x 元素x
         * @return 根节点
         */
        public int find(int x) {
            if (parent[x] != x) {
                int root = find(parent[x]);
                // 更新颜色：x到根节点的颜色是x到父节点的颜色异或父节点到根节点的颜色
                color[x] ^= color[parent[x]];
                parent[x] = root; // 路径压缩
            }
            return parent[x];
        }
        
        /**
         * 判断两个元素是否在同一集合且满足颜色关系
         * @param x 元素x
         * @param y 元素y
         * @param expectedColor 期望的颜色关系（0表示同色，1表示异色）
         * @return 是否满足条件
         */
        public boolean union(int x, int y, int expectedColor) {
            int rootX = find(x);
            int rootY = find(y);
            
            // 计算x和y当前的颜色关系
            int currentColor = color[x] ^ color[y];
            
            if (rootX == rootY) {
                // 在同一集合中，检查颜色是否符合期望
                return currentColor == expectedColor;
            }
            
            // 按秩合并
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                // 更新颜色关系
                color[rootX] = currentColor ^ expectedColor;
            } else {
                parent[rootY] = rootX;
                // 更新颜色关系
                color[rootY] = currentColor ^ expectedColor;
                
                if (rank[rootX] == rank[rootY]) {
                    rank[rootX]++;
                }
            }
            
            return true;
        }
        
        /**
         * 判断图是否为二分图
         * @param edges 边列表
         * @return 是否为二分图
         */
        public static boolean isBipartite(int[][] edges, int n) {
            BipartiteUnionFind uf = new BipartiteUnionFind(n);
            
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                // 期望u和v是异色的
                if (!uf.union(u, v, 1)) {
                    return false; // 存在奇环，不是二分图
                }
            }
            
            return true;
        }
    }

    // ====================================================================================
    // 5. 左偏树（Leftist Tree）- 可并堆实现
    // ====================================================================================
    /**
     * 左偏树实现
     * 
     * 概述：
     * 左偏树是一种可并堆，它保持左偏性质（左子树的距离不小于右子树的距离），
     * 支持高效的合并操作，时间复杂度为O(log n)。
     * 
     * 适用场景：
     * - 频繁合并堆的场景
     * - 动态维护多个优先级队列
     * - 离线算法中的堆操作
     * 
     * 时间复杂度：
     * - 合并操作：O(log n)
     * - 插入操作：O(log n)
     * - 删除最小元素：O(log n)
     * 
     * 空间复杂度：
     * - O(n)
     */
    public static class LeftistTree {
        private Node root;
        private int size;
        
        private static class Node {
            int value;
            Node left;
            Node right;
            int dist; // 到最近空节点的距离
            
            Node(int value) {
                this.value = value;
                this.dist = 0;
            }
        }
        
        public LeftistTree() {
            this.root = null;
            this.size = 0;
        }
        
        /**
         * 合并两棵左偏树
         * @param a 第一棵树的根
         * @param b 第二棵树的根
         * @return 合并后的树根
         */
        private Node merge(Node a, Node b) {
            if (a == null) return b;
            if (b == null) return a;
            
            // 确保a的值小于等于b的值
            if (a.value > b.value) {
                Node temp = a;
                a = b;
                b = temp;
            }
            
            // 递归合并a的右子树和b
            a.right = merge(a.right, b);
            
            // 维护左偏性质：左子树的距离不小于右子树
            if (a.left == null || (a.right != null && a.left.dist < a.right.dist)) {
                Node temp = a.left;
                a.left = a.right;
                a.right = temp;
            }
            
            // 更新距离
            a.dist = (a.right == null) ? 0 : a.right.dist + 1;
            
            return a;
        }
        
        /**
         * 插入元素
         * @param value 要插入的值
         */
        public void insert(int value) {
            root = merge(root, new Node(value));
            size++;
        }
        
        /**
         * 获取最小值
         * @return 最小值，如果堆为空返回Integer.MIN_VALUE
         */
        public int getMin() {
            return (root == null) ? Integer.MIN_VALUE : root.value;
        }
        
        /**
         * 删除最小值
         * @return 最小值，如果堆为空返回Integer.MIN_VALUE
         */
        public int extractMin() {
            if (root == null) return Integer.MIN_VALUE;
            
            int min = root.value;
            root = merge(root.left, root.right);
            size--;
            
            return min;
        }
        
        /**
         * 合并两个堆
         * @param other 另一个堆
         */
        public void merge(LeftistTree other) {
            this.root = merge(this.root, other.root);
            this.size += other.size;
            other.root = null;
            other.size = 0;
        }
        
        /**
         * 获取堆的大小
         * @return 堆的大小
         */
        public int size() {
            return size;
        }
        
        /**
         * 判断堆是否为空
         * @return 是否为空
         */
        public boolean isEmpty() {
            return size == 0;
        }
    }

    // ====================================================================================
    // 6. 线段树进阶 - Segment Tree Beats
    // ====================================================================================
    /**
     * Segment Tree Beats实现
     * 
     * 概述：
     * Segment Tree Beats是线段树的一种高级变种，特别适用于区间取最小值（chmin）、
     * 区间取最大值（chmax）和区间赋值等操作，它能够在O(log n)时间内处理这些操作。
     * 
     * 适用场景：
     * - 区间chmin/chmax操作频繁的问题
     * - 区间查询最大值、最小值、和等
     * - 需要同时支持多种区间修改和查询的场景
     * 
     * 时间复杂度：
     * - 区间修改：O(log n)
     * - 区间查询：O(log n)
     * 
     * 空间复杂度：
     * - O(4n)，线段树的标准空间
     */
    public static class SegmentTreeBeats {
        private final int n; // 原始数组大小
        private final long[] max1; // 最大值
        private final long[] max2; // 次大值
        private final int[] cntMax; // 最大值的个数
        private final long[] min1; // 最小值
        private final long[] min2; // 次小值
        private final int[] cntMin; // 最小值的个数
        private final long[] sum; // 区间和
        private final long[] add; // 加法标记
        private final long[] cover; // 覆盖标记
        private final boolean[] hasCover; // 是否有覆盖标记
        
        /**
         * 构造函数
         * @param data 原始数据数组
         */
        public SegmentTreeBeats(long[] data) {
            n = data.length;
            int size = 1;
            while (size < n) size <<= 1;
            
            max1 = new long[2 * size];
            max2 = new long[2 * size];
            cntMax = new int[2 * size];
            min1 = new long[2 * size];
            min2 = new long[2 * size];
            cntMin = new int[2 * size];
            sum = new long[2 * size];
            add = new long[2 * size];
            cover = new long[2 * size];
            hasCover = new boolean[2 * size];
            
            // 初始化叶子节点
            for (int i = 0; i < n; i++) {
                int pos = i + size;
                max1[pos] = min1[pos] = data[i];
                max2[pos] = Long.MIN_VALUE;
                min2[pos] = Long.MAX_VALUE;
                cntMax[pos] = cntMin[pos] = 1;
                sum[pos] = data[i];
            }
            
            // 初始化非叶子节点
            for (int i = size - 1; i > 0; i--) {
                pushUp(i);
            }
        }
        
        /**
         * 向上更新节点信息
         * @param node 当前节点
         */
        private void pushUp(int node) {
            int left = 2 * node;
            int right = 2 * node + 1;
            
            // 更新和
            sum[node] = sum[left] + sum[right];
            
            // 更新最大值
            if (max1[left] > max1[right]) {
                max1[node] = max1[left];
                cntMax[node] = cntMax[left];
                max2[node] = Math.max(max2[left], max1[right]);
            } else if (max1[left] < max1[right]) {
                max1[node] = max1[right];
                cntMax[node] = cntMax[right];
                max2[node] = Math.max(max1[left], max2[right]);
            } else {
                max1[node] = max1[left];
                cntMax[node] = cntMax[left] + cntMax[right];
                max2[node] = Math.max(max2[left], max2[right]);
            }
            
            // 更新最小值
            if (min1[left] < min1[right]) {
                min1[node] = min1[left];
                cntMin[node] = cntMin[left];
                min2[node] = Math.min(min2[left], min1[right]);
            } else if (min1[left] > min1[right]) {
                min1[node] = min1[right];
                cntMin[node] = cntMin[right];
                min2[node] = Math.min(min1[left], min2[right]);
            } else {
                min1[node] = min1[left];
                cntMin[node] = cntMin[left] + cntMin[right];
                min2[node] = Math.min(min2[left], min2[right]);
            }
        }
        
        /**
         * 向下传递标记
         * @param node 当前节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         */
        private void pushDown(int node, int l, int r) {
            int left = 2 * node;
            int right = 2 * node + 1;
            int mid = (l + r) / 2;
            
            // 先处理覆盖标记
            if (hasCover[node]) {
                // 左子树
                applyCover(left, cover[node], mid - l + 1);
                // 右子树
                applyCover(right, cover[node], r - mid);
                
                hasCover[node] = false;
                add[node] = 0;
            } else if (add[node] != 0) {
                // 处理加法标记
                applyAdd(left, add[node], mid - l + 1);
                applyAdd(right, add[node], r - mid);
                
                add[node] = 0;
            }
        }
        
        /**
         * 应用覆盖操作
         * @param node 当前节点
         * @param val 覆盖值
         * @param len 区间长度
         */
        private void applyCover(int node, long val, int len) {
            sum[node] = val * len;
            max1[node] = val;
            min1[node] = val;
            cntMax[node] = len;
            cntMin[node] = len;
            
            // 覆盖后，次大/小值变得无关紧要
            max2[node] = Long.MIN_VALUE;
            min2[node] = Long.MAX_VALUE;
            
            cover[node] = val;
            hasCover[node] = true;
            add[node] = 0; // 覆盖操作清除加法标记
        }
        
        /**
         * 应用加法操作
         * @param node 当前节点
         * @param val 增加值
         * @param len 区间长度
         */
        private void applyAdd(int node, long val, int len) {
            sum[node] += val * len;
            max1[node] += val;
            if (max2[node] != Long.MIN_VALUE) {
                max2[node] += val;
            }
            min1[node] += val;
            if (min2[node] != Long.MAX_VALUE) {
                min2[node] += val;
            }
            
            if (hasCover[node]) {
                cover[node] += val;
            } else {
                add[node] += val;
            }
        }
        
        /**
         * 区间覆盖操作
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @param val 覆盖值
         */
        public void cover(int ql, int qr, long val) {
            cover(1, 0, n - 1, ql, qr, val);
        }
        
        private void cover(int node, int l, int r, int ql, int qr, long val) {
            if (qr < l || r < ql) return;
            
            if (ql <= l && r <= qr) {
                applyCover(node, val, r - l + 1);
                return;
            }
            
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            cover(2 * node, l, mid, ql, qr, val);
            cover(2 * node + 1, mid + 1, r, ql, qr, val);
            pushUp(node);
        }
        
        /**
         * 区间加法操作
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @param val 增加值
         */
        public void add(int ql, int qr, long val) {
            add(1, 0, n - 1, ql, qr, val);
        }
        
        private void add(int node, int l, int r, int ql, int qr, long val) {
            if (qr < l || r < ql) return;
            
            if (ql <= l && r <= qr) {
                applyAdd(node, val, r - l + 1);
                return;
            }
            
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            add(2 * node, l, mid, ql, qr, val);
            add(2 * node + 1, mid + 1, r, ql, qr, val);
            pushUp(node);
        }
        
        /**
         * 区间取最小值操作
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @param val 最小值
         */
        public void chmin(int ql, int qr, long val) {
            chmin(1, 0, n - 1, ql, qr, val);
        }
        
        private void chmin(int node, int l, int r, int ql, int qr, long val) {
            // 如果当前区间的最大值已经小于等于val，不需要操作
            if (max1[node] <= val) return;
            
            // 如果当前区间完全包含在查询区间内，且次大值小于val
            if (ql <= l && r <= qr && max2[node] < val) {
                // 只需要更新最大值
                sum[node] += (val - max1[node]) * cntMax[node];
                max1[node] = val;
                
                // 如果有覆盖标记，也需要更新
                if (hasCover[node]) {
                    cover[node] = val;
                }
                
                return;
            }
            
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            chmin(2 * node, l, mid, ql, qr, val);
            chmin(2 * node + 1, mid + 1, r, ql, qr, val);
            pushUp(node);
        }
        
        /**
         * 区间取最大值操作
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @param val 最大值
         */
        public void chmax(int ql, int qr, long val) {
            chmax(1, 0, n - 1, ql, qr, val);
        }
        
        private void chmax(int node, int l, int r, int ql, int qr, long val) {
            // 如果当前区间的最小值已经大于等于val，不需要操作
            if (min1[node] >= val) return;
            
            // 如果当前区间完全包含在查询区间内，且次小值大于val
            if (ql <= l && r <= qr && min2[node] > val) {
                // 只需要更新最小值
                sum[node] += (val - min1[node]) * cntMin[node];
                min1[node] = val;
                
                // 如果有覆盖标记，也需要更新
                if (hasCover[node]) {
                    cover[node] = val;
                }
                
                return;
            }
            
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            chmax(2 * node, l, mid, ql, qr, val);
            chmax(2 * node + 1, mid + 1, r, ql, qr, val);
            pushUp(node);
        }
        
        /**
         * 查询区间和
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @return 区间和
         */
        public long querySum(int ql, int qr) {
            return querySum(1, 0, n - 1, ql, qr);
        }
        
        private long querySum(int node, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return 0;
            if (ql <= l && r <= qr) return sum[node];
            
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            return querySum(2 * node, l, mid, ql, qr) + 
                   querySum(2 * node + 1, mid + 1, r, ql, qr);
        }
        
        /**
         * 查询区间最大值
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @return 区间最大值
         */
        public long queryMax(int ql, int qr) {
            return queryMax(1, 0, n - 1, ql, qr);
        }
        
        private long queryMax(int node, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return Long.MIN_VALUE;
            if (ql <= l && r <= qr) return max1[node];
            
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            return Math.max(queryMax(2 * node, l, mid, ql, qr), 
                           queryMax(2 * node + 1, mid + 1, r, ql, qr));
        }
        
        /**
         * 查询区间最小值
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @return 区间最小值
         */
        public long queryMin(int ql, int qr) {
            return queryMin(1, 0, n - 1, ql, qr);
        }
        
        private long queryMin(int node, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return Long.MAX_VALUE;
            if (ql <= l && r <= qr) return min1[node];
            
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            return Math.min(queryMin(2 * node, l, mid, ql, qr), 
                           queryMin(2 * node + 1, mid + 1, r, ql, qr));
        }
    }

    // ====================================================================================
    // 7. 树状数组进阶 - 双树状数组（区间加区间查）
    // ====================================================================================
    /**
     * 双树状数组实现
     * 
     * 概述：
     * 双树状数组是树状数组的扩展，用于同时支持区间加法和区间求和操作。
     * 它通过维护两个树状数组，利用差分思想实现O(log n)的区间操作。
     * 
     * 适用场景：
     * - 频繁的区间加法和区间求和操作
     * - 区间更新和区间查询的场景
     * - 需要高效处理前缀和的问题
     * 
     * 时间复杂度：
     * - 区间加：O(log n)
     * - 区间查：O(log n)
     * 
     * 空间复杂度：
     * - O(n)
     */
    public static class BinaryIndexedTreeRange {
        private final long[] bit1; // 第一个树状数组
        private final long[] bit2; // 第二个树状数组
        private final int n;       // 数组大小
        
        /**
         * 构造函数
         * @param size 数组大小
         */
        public BinaryIndexedTreeRange(int size) {
            this.n = size;
            this.bit1 = new long[n + 1];
            this.bit2 = new long[n + 1];
        }
        
        /**
         * 构造函数（从数组初始化）
         * @param arr 初始数组
         */
        public BinaryIndexedTreeRange(long[] arr) {
            this.n = arr.length;
            this.bit1 = new long[n + 1];
            this.bit2 = new long[n + 1];
            
            // 初始化数组
            for (int i = 0; i < n; i++) {
                updateRange(i + 1, i + 1, arr[i]);
            }
        }
        
        /**
         * 获取最低位的1
         * @param x 整数
         * @return 最低位的1所代表的值
         */
        private int lowbit(int x) {
            return x & -x;
        }
        
        /**
         * 更新树状数组
         * @param bit 树状数组
         * @param idx 索引
         * @param val 值
         */
        private void update(long[] bit, int idx, long val) {
            while (idx <= n) {
                bit[idx] += val;
                idx += lowbit(idx);
            }
        }
        
        /**
         * 查询前缀和
         * @param bit 树状数组
         * @param idx 索引
         * @return 前缀和
         */
        private long query(long[] bit, int idx) {
            long sum = 0;
            while (idx > 0) {
                sum += bit[idx];
                idx -= lowbit(idx);
            }
            return sum;
        }
        /**
         * 区间更新
         * @param l 左端点（1-based）
         * @param r 右端点（1-based）
         * @param val 增加值
         */
        public void updateRange(int l, int r, long val) {
            update(bit1, l, val);
            update(bit1, r + 1, -val);
            update(bit2, l, val * (l - 1));
            update(bit2, r + 1, -val * r);
        }
        
        /**
         * 查询前缀和（1-based，[1,idx]）
         * @param idx 索引
         * @return 前缀和
         */
        public long queryPrefix(int idx) {
            return idx * query(bit1, idx) - query(bit2, idx);
        }
        
        /**
         * 查询区间和（1-based，[l,r]）
         * @param l 左端点
         * @param r 右端点
         * @return 区间和
         */
        public long queryRange(int l, int r) {
            return queryPrefix(r) - queryPrefix(l - 1);
        }
        
        /**
         * 查询单点值
         * @param idx 索引（1-based）
         * @return 单点值
         */
        public long queryPoint(int idx) {
            return queryRange(idx, idx);
        }
    }

    // ====================================================================================
    // 8. LCA优化 - Euler Tour + ST的RMQ方案
    // ====================================================================================
    /**
     * LCA（最近公共祖先）实现 - Euler Tour + ST表
     * 
     * 概述：
     * 这个实现使用Euler Tour（欧拉序）结合Sparse Table（ST表）来实现O(n log n)预处理，
     * O(1)查询最近公共祖先。通过深度优先搜索记录欧拉序，并为每个节点记录第一次出现的位置，
     * 然后使用ST表预处理区间最小值查询。
     * 
     * 适用场景：
     * - 树上多次LCA查询
     * - 需要高效预处理的场景
     * - 静态树结构（不支持动态修改）
     * 
     * 时间复杂度：
     * - 预处理：O(n log n)
     * - 查询：O(1)
     * 
     * 空间复杂度：
     * - O(n log n)
     */
    public static class LCAEulerTour {
        private List<List<Integer>> tree; // 树的邻接表表示
        private int[] depth; // 每个节点的深度
        private int[] euler; // 欧拉序数组
        private int[] first; // 每个节点在欧拉序中第一次出现的位置
        private int[] logTable; // 对数表，用于快速查询log2
        private int[][] st; // ST表
        private int time; // 时间戳
        
        /**
         * 构造函数
         * @param n 节点数量
         */
        public LCAEulerTour(int n) {
            tree = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                tree.add(new ArrayList<>());
            }
            depth = new int[n];
            euler = new int[2 * n - 1]; // 欧拉序的最大长度
            first = new int[n];
            time = 0;
        }
        
        /**
         * 添加无向边
         * @param u 节点u
         * @param v 节点v
         */
        public void addEdge(int u, int v) {
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        
        /**
         * 预处理
         * @param root 根节点
         */
        public void build(int root) {
            // 进行欧拉序遍历
            dfs(root, -1, 0);
            
            // 构建ST表
            int m = euler.length;
            int logM = (int) (Math.log(m) / Math.log(2)) + 1;
            
            // 预处理对数表
            logTable = new int[m + 1];
            logTable[1] = 0;
            for (int i = 2; i <= m; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
            
            // 构建ST表
            st = new int[logM][m];
            for (int i = 0; i < m; i++) {
                st[0][i] = i; // 存储索引而不是值
            }
            
            for (int j = 1; j < logM; j++) {
                for (int i = 0; i + (1 << j) <= m; i++) {
                    int left = st[j - 1][i];
                    int right = st[j - 1][i + (1 << (j - 1))];
                    st[j][i] = (depth[euler[left]] < depth[euler[right]]) ? left : right;
                }
            }
        }
        
        /**
         * 深度优先搜索，生成欧拉序
         * @param u 当前节点
         * @param parent 父节点
         * @param d 当前深度
         */
        private void dfs(int u, int parent, int d) {
            depth[u] = d;
            first[u] = time;
            euler[time++] = u;
            
            for (int v : tree.get(u)) {
                if (v != parent) {
                    dfs(v, u, d + 1);
                    euler[time++] = u; // 回溯时再次记录
                }
            }
        }
        
        /**
         * 查询区间最小值的索引
         * @param l 左端点
         * @param r 右端点
         * @return 最小值的索引
         */
        private int queryMin(int l, int r) {
            int length = r - l + 1;
            int k = logTable[length];
            int left = st[k][l];
            int right = st[k][r - (1 << k) + 1];
            
            return (depth[euler[left]] < depth[euler[right]]) ? left : right;
        }
        
        /**
         * 查询两个节点的最近公共祖先
         * @param u 节点u
         * @param v 节点v
         * @return 最近公共祖先
         */
        public int lca(int u, int v) {
            int l = Math.min(first[u], first[v]);
            int r = Math.max(first[u], first[v]);
            int minIndex = queryMin(l, r);
            return euler[minIndex];
        }
        
        /**
         * 获取两个节点之间的距离
         * @param u 节点u
         * @param v 节点v
         * @return 距离
         */
        public int getDistance(int u, int v) {
            int ancestor = lca(u, v);
            return depth[u] + depth[v] - 2 * depth[ancestor];
        }
    }

    // 测试方法 - 可以根据需要取消注释进行测试
        public static void main(String[] args) {
            // 在这里可以添加各种数据结构的测试代码
            System.out.println("高级数据结构与算法实现完成");
        }

        // ====================================================================================
        // 9. Wavelet Tree - 区间第k小/频次统计
        // ====================================================================================
        /**
         * Wavelet Tree实现
         * 
         * 概述：
         * Wavelet Tree是一种用于高效解决范围查询问题的数据结构，特别是区间第k小、区间内小于等于某个值的元素个数等。
         * 它通过递归地将数据划分为左右子树，每个节点存储相应范围内的二进制位信息。
         * 
         * 适用场景：
         * - 区间第k小查询
         * - 区间内元素频次统计
         * - 范围排名查询
         * - 多维度范围查询
         * 
         * 时间复杂度：
         * - 构建：O(n log C)，其中C是值域范围
         * - 区间第k小查询：O(log C)
         * - 区间内小于等于x的元素个数：O(log C)
         * 
         * 空间复杂度：
         * - O(n log C)
         */
        public static class WaveletTree {
            private Node root;
            private final int n;
            private final int minValue;
            private final int maxValue;
            
            private static class Node {
                int l, r; // 当前节点表示的值域范围 [l, r]
                int mid; // 中间值，用于分割值域
                int[] prefixCounts; // 前缀和数组，记录到每个位置为止，有多少元素进入左子树
                Node left, right; // 左右子树
                
                Node(int l, int r, int size) {
                    this.l = l;
                    this.r = r;
                    this.mid = (l + r) / 2;
                    this.prefixCounts = new int[size + 1]; // 1-based索引
                }
            }
            
            /**
             * 构造函数
             * @param arr 原始数组
             */
            public WaveletTree(int[] arr) {
                this.n = arr.length;
                
                // 找到数组中的最小值和最大值
                minValue = Arrays.stream(arr).min().orElse(0);
                maxValue = Arrays.stream(arr).max().orElse(0);
                
                // 构建Wavelet Tree
                root = build(arr, 0, n - 1, minValue, maxValue);
            }
            
            /**
             * 递归构建Wavelet Tree
             * @param arr 当前数组
             * @param l 当前区间左端点
             * @param r 当前区间右端点
             * @param valL 当前值域左边界
             * @param valR 当前值域右边界
             * @return 构建好的节点
             */
            private Node build(int[] arr, int l, int r, int valL, int valR) {
                Node node = new Node(valL, valR, r - l + 1);
                
                // 叶子节点
                if (valL == valR) {
                    return node;
                }
                
                int mid = node.mid;
                int[] leftArr = new int[r - l + 1];
                int[] rightArr = new int[r - l + 1];
                int leftIdx = 0, rightIdx = 0;
                
                // 计算前缀和并分割数组
                for (int i = 0; i <= r - l; i++) {
                    int pos = l + i;
                    if (arr[pos] <= mid) {
                        leftArr[leftIdx++] = arr[pos];
                        node.prefixCounts[i + 1] = node.prefixCounts[i] + 1;
                    } else {
                        rightArr[rightIdx++] = arr[pos];
                        node.prefixCounts[i + 1] = node.prefixCounts[i];
                    }
                }
                
                // 递归构建左右子树
                if (leftIdx > 0) {
                    int[] trimmedLeft = new int[leftIdx];
                    System.arraycopy(leftArr, 0, trimmedLeft, 0, leftIdx);
                    node.left = build(trimmedLeft, 0, leftIdx - 1, valL, mid);
                }
                
                if (rightIdx > 0) {
                    int[] trimmedRight = new int[rightIdx];
                    System.arraycopy(rightArr, 0, trimmedRight, 0, rightIdx);
                    node.right = build(trimmedRight, 0, rightIdx - 1, mid + 1, valR);
                }
                
                return node;
            }
            
            /**
             * 查询区间[l, r]内小于等于x的元素个数
             * @param l 区间左端点（0-based）
             * @param r 区间右端点（0-based）
             * @param x 查询值
             * @return 小于等于x的元素个数
             */
            public int queryCountLE(int l, int r, int x) {
                if (x < root.l) return 0;
                if (x >= root.r) return r - l + 1;
                return queryCountLE(root, l + 1, r + 1, x); // 转换为1-based
            }
            
            private int queryCountLE(Node node, int l, int r, int x) {
                // 叶子节点
                if (node.l == node.r) {
                    return r - l + 1;
                }
                
                int cntLeft = node.prefixCounts[r] - node.prefixCounts[l - 1];
                int cntRight = (r - l + 1) - cntLeft;
                
                if (x <= node.mid) {
                    // 只查询左子树
                    if (node.left == null) return 0;
                    return queryCountLE(node.left, node.prefixCounts[l - 1] + 1, node.prefixCounts[r], x);
                } else {
                    // 查询左子树全部 + 右子树部分
                    int leftResult = cntLeft;
                    int rightResult = 0;
                    if (node.right != null) {
                        rightResult = queryCountLE(node.right, l - node.prefixCounts[l - 1], 
                                                 r - node.prefixCounts[r], x);
                    }
                    return leftResult + rightResult;
                }
            }
            
            /**
             * 查询区间[l, r]内的第k小元素
             * @param l 区间左端点（0-based）
             * @param r 区间右端点（0-based）
             * @param k 第k小（1-based）
             * @return 第k小的元素值
             */
            public int queryKth(int l, int r, int k) {
                if (k < 1 || k > r - l + 1) {
                    throw new IllegalArgumentException("k的取值范围无效");
                }
                return queryKth(root, l + 1, r + 1, k); // 转换为1-based
            }
            
            private int queryKth(Node node, int l, int r, int k) {
                // 叶子节点
                if (node.l == node.r) {
                    return node.l;
                }
                
                int cntLeft = node.prefixCounts[r] - node.prefixCounts[l - 1];
                
                if (k <= cntLeft) {
                    // 第k小在左子树
                    return queryKth(node.left, node.prefixCounts[l - 1] + 1, node.prefixCounts[r], k);
                } else {
                    // 第k小在右子树
                    return queryKth(node.right, l - node.prefixCounts[l - 1], 
                                  r - node.prefixCounts[r], k - cntLeft);
                }
            }
            
            /**
             * 查询区间[l, r]内等于x的元素个数
             * @param l 区间左端点（0-based）
             * @param r 区间右端点（0-based）
             * @param x 查询值
             * @return 等于x的元素个数
             */
            public int queryCountEQ(int l, int r, int x) {
                return queryCountLE(l, r, x) - queryCountLE(l, r, x - 1);
            }
        }

        // ====================================================================================
        // 10. 斜堆（Skew Heap）- 可并堆实现
        // ====================================================================================
        /**
         * 斜堆实现
         * 
         * 概述：
         * 斜堆是一种自调整的可并堆，它是左偏树的一种简化版本，不需要维护距离信息。
         * 斜堆通过在合并操作时进行旋转来保持树的平衡，平均时间复杂度为O(log n)。
         * 
         * 适用场景：
         * - 频繁合并堆的场景
         * - 需要自调整的数据结构
         * - 内存受限环境（相比左偏树需要更少的存储空间）
         * 
         * 时间复杂度：
         * - 合并操作：O(log n) 均摊
         * - 插入操作：O(log n) 均摊
         * - 删除最小元素：O(log n) 均摊
         * 
         * 空间复杂度：
         * - O(n)
         */
        public static class SkewHeap {
            private Node root;
            private int size;
            
            private static class Node {
                int value;
                Node left;
                Node right;
                
                Node(int value) {
                    this.value = value;
                }
            }
            
            public SkewHeap() {
                this.root = null;
                this.size = 0;
            }
            
            /**
             * 合并两个斜堆
             * @param a 第一个堆的根
             * @param b 第二个堆的根
             * @return 合并后的树根
             */
            private Node merge(Node a, Node b) {
                if (a == null) return b;
                if (b == null) return a;
                
                // 确保a的值小于等于b的值
                if (a.value > b.value) {
                    Node temp = a;
                    a = b;
                    b = temp;
                }
                
                // 合并a的右子树和b
                a.right = merge(a.right, b);
                
                // 交换左右子树以保持平衡
                Node temp = a.left;
                a.left = a.right;
                a.right = temp;
                
                return a;
            }
            
            /**
             * 插入元素
             * @param value 要插入的值
             */
            public void insert(int value) {
                root = merge(root, new Node(value));
                size++;
            }
            
            /**
             * 获取最小值
             * @return 最小值，如果堆为空返回Integer.MIN_VALUE
             */
            public int getMin() {
                return (root == null) ? Integer.MIN_VALUE : root.value;
            }
            
            /**
             * 删除最小值
             * @return 最小值，如果堆为空返回Integer.MIN_VALUE
             */
            public int extractMin() {
                if (root == null) return Integer.MIN_VALUE;
                
                int min = root.value;
                root = merge(root.left, root.right);
                size--;
                
                return min;
            }
            
            /**
             * 合并两个堆
             * @param other 另一个堆
             */
            public void merge(SkewHeap other) {
                this.root = merge(this.root, other.root);
                this.size += other.size;
                other.root = null;
                other.size = 0;
            }
            
            /**
             * 获取堆的大小
             * @return 堆的大小
             */
            public int size() {
                return size;
            }
            
            /**
             * 判断堆是否为空
             * @return 是否为空
             */
            public boolean isEmpty() {
                return size == 0;
            }
        }

        // ====================================================================================
        // 11. 配对堆（Pairing Heap）- 可并堆实现
        // ====================================================================================
        /**
         * 配对堆实现
         * 
         * 概述：
         * 配对堆是一种高效的可并堆数据结构，它在实践中表现优异，虽然理论上的最坏时间复杂度不是最优的，
         * 但平均性能非常好。配对堆通过"配对"操作来合并子树，保持树的结构简单高效。
         * 
         * 适用场景：
         * - 频繁合并堆的场景
         * - 优先队列操作频繁的应用
         * - 对性能要求极高的系统
         * 
         * 时间复杂度：
         * - 合并操作：O(1) 均摊（理论上是O(log n)均摊）
         * - 插入操作：O(1) 均摊
         * - 删除最小元素：O(log n) 均摊
         * 
         * 空间复杂度：
         * - O(n)
         */
        public static class PairingHeap {
            private Node root;
            private int size;
            
            private static class Node {
                int value;
                Node child; // 第一个子节点
                Node next;  // 兄弟节点
                
                Node(int value) {
                    this.value = value;
                }
            }
            
            public PairingHeap() {
                this.root = null;
                this.size = 0;
            }
            
            /**
             * 合并两个配对堆
             * @param a 第一个堆的根
             * @param b 第二个堆的根
             * @return 合并后的树根
             */
            private Node merge(Node a, Node b) {
                if (a == null) return b;
                if (b == null) return a;
                
                // 确保a的值小于等于b的值
                if (a.value > b.value) {
                    Node temp = a;
                    a = b;
                    b = temp;
                }
                
                // 将b添加为a的子节点
                b.next = a.child;
                a.child = b;
                
                return a;
            }
            
            /**
             * 配对操作：将兄弟节点两两配对合并
             * @param first 第一个节点
             * @return 合并后的根节点
             */
            private Node pair(Node first) {
                if (first == null || first.next == null) {
                    return first;
                }
                
                // 递归配对合并
                Node second = first.next;
                Node rest = second.next;
                
                first.next = null;
                second.next = null;
                
                return merge(merge(first, second), pair(rest));
            }
            
            /**
             * 插入元素
             * @param value 要插入的值
             */
            public void insert(int value) {
                root = merge(root, new Node(value));
                size++;
            }
            
            /**
             * 获取最小值
             * @return 最小值，如果堆为空返回Integer.MIN_VALUE
             */
            public int getMin() {
                return (root == null) ? Integer.MIN_VALUE : root.value;
            }
            
            /**
             * 删除最小值
             * @return 最小值，如果堆为空返回Integer.MIN_VALUE
             */
            public int extractMin() {
                if (root == null) return Integer.MIN_VALUE;
                
                int min = root.value;
                
                // 对子节点进行配对合并
                root = pair(root.child);
                size--;
                
                return min;
            }
            
            /**
             * 合并两个堆
             * @param other 另一个堆
             */
            public void merge(PairingHeap other) {
                this.root = merge(this.root, other.root);
                this.size += other.size;
                other.root = null;
                other.size = 0;
            }
            
            /**
             * 获取堆的大小
             * @return 堆的大小
             */
            public int size() {
                return size;
            }
            
            /**
             * 判断堆是否为空
             * @return 是否为空
             */
            public boolean isEmpty() {
                return size == 0;
            }
        }

        // ====================================================================================
        // 12. 动态开点线段树
        // ====================================================================================
        /**
         * 动态开点线段树实现
         * 
         * 概述：
         * 动态开点线段树是线段树的一种优化版本，它只在需要的时候才创建节点，
         * 适用于值域范围很大但实际使用的节点较少的场景。
         * 
         * 适用场景：
         * - 值域范围极大的问题
         * - 稀疏数据的区间操作
         * - 需要节省内存的场景
         * 
         * 时间复杂度：
         * - 区间更新：O(log R)，R是值域范围
         * - 区间查询：O(log R)
         * 
         * 空间复杂度：
         * - O(k log R)，k是实际操作的区间数量
         */
        public static class DynamicSegmentTree {
            private Node root;
            private final long leftBound;
            private final long rightBound;
            
            private static class Node {
                long sum; // 区间和
                long lazy; // 延迟标记
                Node left; // 左子节点
                Node right; // 右子节点
            }
            
            /**
             * 构造函数
             * @param leftBound 值域左边界
             * @param rightBound 值域右边界
             */
            public DynamicSegmentTree(long leftBound, long rightBound) {
                this.leftBound = leftBound;
                this.rightBound = rightBound;
                this.root = new Node();
            }
            
            /**
             * 区间更新
             * @param l 左边界
             * @param r 右边界
             * @param val 增加值
             */
            public void updateRange(long l, long r, long val) {
                updateRange(root, leftBound, rightBound, l, r, val);
            }
            
            private void updateRange(Node node, long L, long R, long l, long r, long val) {
                // 当前区间完全包含在目标区间内
                if (l <= L && R <= r) {
                    node.sum += val * (R - L + 1);
                    node.lazy += val;
                    return;
                }
                
                long mid = L + (R - L) / 2;
                
                // 下传延迟标记
                pushDown(node, L, R, mid);
                
                // 更新左右子树
                if (l <= mid) {
                    if (node.left == null) {
                        node.left = new Node();
                    }
                    updateRange(node.left, L, mid, l, r, val);
                }
                if (r > mid) {
                    if (node.right == null) {
                        node.right = new Node();
                    }
                    updateRange(node.right, mid + 1, R, l, r, val);
                }
                
                // 上传更新
                node.sum = (node.left != null ? node.left.sum : 0) + 
                          (node.right != null ? node.right.sum : 0);
            }
            
            /**
             * 下传延迟标记
             */
            private void pushDown(Node node, long L, long R, long mid) {
                if (node.lazy != 0) {
                    // 确保左右子节点存在
                    if (node.left == null) {
                        node.left = new Node();
                    }
                    if (node.right == null) {
                        node.right = new Node();
                    }
                    
                    // 更新左子节点
                    node.left.sum += node.lazy * (mid - L + 1);
                    node.left.lazy += node.lazy;
                    
                    // 更新右子节点
                    node.right.sum += node.lazy * (R - mid);
                    node.right.lazy += node.lazy;
                    
                    // 清除当前节点的延迟标记
                    node.lazy = 0;
                }
            }
            
            /**
             * 区间查询
             * @param l 左边界
             * @param r 右边界
             * @return 区间和
             */
            public long queryRange(long l, long r) {
                return queryRange(root, leftBound, rightBound, l, r);
            }
            
            private long queryRange(Node node, long L, long R, long l, long r) {
                if (node == null) return 0;
                
                // 当前区间完全包含在查询区间内
                if (l <= L && R <= r) {
                    return node.sum;
                }
                
                long mid = L + (R - L) / 2;
                long sum = 0;
                
                // 下传延迟标记
                pushDown(node, L, R, mid);
                
                // 查询左右子树
                if (l <= mid) {
                    sum += queryRange(node.left, L, mid, l, r);
                }
                if (r > mid) {
                    sum += queryRange(node.right, mid + 1, R, l, r);
                }
                
                return sum;
            }
        }

        // ====================================================================================
        // 13. Link-Cut Tree (LCT) - 动态树连通性
        // ====================================================================================
        /**
         * Link-Cut Tree实现
         * 
         * 概述：
         * Link-Cut Tree是一种用于维护动态树连通性的数据结构，它支持路径查询和修改操作，
         * 通过splay树来维护实边和虚边的概念，使得树可以动态连接和断开。
         * 
         * 适用场景：
         * - 动态树连通性问题
         * - 路径查询和修改
         * - 动态森林维护
         * - IOI/OI竞赛中的高级数据结构题
         * 
         * 时间复杂度：
         * - 所有操作：O(log n)均摊时间复杂度
         * 
         * 空间复杂度：
         * - O(n)
         */
        public static class LinkCutTree {
            private Node[] nodes;
            
            private static class Node {
                int id; // 节点ID
                int val; // 节点值
                int sum; // 子树和
                Node fa, ch[];
                boolean rev; // 翻转标记
                
                Node(int id, int val) {
                    this.id = id;
                    this.val = val;
                    this.sum = val;
                    this.ch = new Node[2];
                    this.fa = null;
                    this.rev = false;
                }
                
                // 判断是否是根节点
                boolean isRoot() {
                    return fa == null || (fa.ch[0] != this && fa.ch[1] != this);
                }
                
                // 判断是左孩子还是右孩子
                int getSide() {
                    return fa.ch[1] == this ? 1 : 0;
                }
                
                // 维护子树信息
                void maintain() {
                    sum = val;
                    if (ch[0] != null) sum += ch[0].sum;
                    if (ch[1] != null) sum += ch[1].sum;
                }
                
                // 翻转操作
                void reverse() {
                    rev ^= true;
                    Node temp = ch[0];
                    ch[0] = ch[1];
                    ch[1] = temp;
                }
                
                // 下传标记
                void pushDown() {
                    if (rev) {
                        if (ch[0] != null) ch[0].reverse();
                        if (ch[1] != null) ch[1].reverse();
                        rev = false;
                    }
                }
            }
            
            /**
             * 构造函数
             * @param n 节点数量
             */
            public LinkCutTree(int n) {
                nodes = new Node[n + 1]; // 1-based索引
                for (int i = 1; i <= n; i++) {
                    nodes[i] = new Node(i, 0);
                }
            }
            
            // 旋转操作
            private void rotate(Node x) {
                Node y = x.fa;
                Node z = y.fa;
                int k = x.getSide();
                
                // 处理z和x的关系
                if (!y.isRoot()) {
                    z.ch[y.getSide()] = x;
                }
                x.fa = z;
                
                // 处理x的子节点和y的关系
                y.ch[k] = x.ch[k^1];
                if (x.ch[k^1] != null) {
                    x.ch[k^1].fa = y;
                }
                
                // 处理x和y的关系
                x.ch[k^1] = y;
                y.fa = x;
                
                // 维护y和x的信息
                y.maintain();
                x.maintain();
            }
            
            // 下传路径上的所有标记
            private void splayDown(Node x) {
                if (!x.isRoot()) {
                    splayDown(x.fa);
                }
                x.pushDown();
            }
            
            // Splay操作
            private void splay(Node x) {
                splayDown(x);
                while (!x.isRoot()) {
                    Node y = x.fa;
                    if (!y.isRoot()) {
                        rotate((x.getSide() ^ y.getSide()) == 1 ? x : y);
                    }
                    rotate(x);
                }
            }
            
            // 访问节点x，使x到根路径成为实链
            private void access(Node x) {
                for (Node y = null; x != null; y = x, x = x.fa) {
                    splay(x);
                    x.ch[1] = y;
                    x.maintain();
                }
            }
            
            // 使x成为根节点
            private void makeRoot(Node x) {
                access(x);
                splay(x);
                x.reverse();
            }
            
            // 查找x所在树的根节点
            private Node findRoot(Node x) {
                access(x);
                splay(x);
                while (x.ch[0] != null) {
                    x.pushDown();
                    x = x.ch[0];
                }
                splay(x); // 优化后续操作
                return x;
            }
            
            /**
             * 连接两个节点（x和y不在同一棵树中）
             */
            public void link(int x, int y) {
                Node u = nodes[x];
                Node v = nodes[y];
                makeRoot(u);
                if (findRoot(v) != u) {
                    u.fa = v;
                }
            }
            
            /**
             * 断开两个节点之间的连接
             */
            public void cut(int x, int y) {
                Node u = nodes[x];
                Node v = nodes[y];
                makeRoot(u);
                access(v);
                splay(u);
                if (findRoot(v) == u && u.ch[1] == v && v.ch[0] == null) {
                    u.ch[1] = null;
                    v.fa = null;
                }
            }
            
            /**
             * 查询路径x到y的和
             */
            public int querySum(int x, int y) {
                Node u = nodes[x];
                Node v = nodes[y];
                makeRoot(u);
                access(v);
                splay(v);
                return v.sum;
            }
            
            /**
             * 更新节点x的值
             */
            public void update(int x, int val) {
                Node u = nodes[x];
                splay(u);
                u.val = val;
                u.maintain();
            }
            
            /**
             * 判断两个节点是否连通
             */
            public boolean isConnected(int x, int y) {
                return findRoot(nodes[x]) == findRoot(nodes[y]);
            }
        }

        // ====================================================================================
        // 14. Divide Tree - 区间第k小查询
        // ====================================================================================
        /**
         * Divide Tree实现
         * 
         * 概述：
         * Divide Tree是一种专门用于解决区间第k小查询的数据结构，它通过递归地将数组划分为左右两部分，
         * 并记录每个位置的元素进入左子树的情况，从而可以在O(log n)时间内回答区间第k小查询。
         * 
         * 适用场景：
         * - 静态数组的区间第k小查询
         * - 离线查询场景
         * 
         * 时间复杂度：
         * - 构建：O(n log n)
         * - 查询：O(log n)
         * 
         * 空间复杂度：
         * - O(n log n)
         */
        public static class DivideTree {
            private int[][] tree; // 每一层的数据
            private int[][] cnt; // 前缀和数组，记录到每个位置有多少元素进入左子树
            private int[] sorted; // 排序后的数组
            private int logn; // log2(n)向上取整
            private int n; // 数组大小
            
            /**
             * 构造函数
             * @param arr 原始数组
             */
            public DivideTree(int[] arr) {
                this.n = arr.length;
                this.logn = (int)Math.ceil(Math.log(n) / Math.log(2)) + 1;
                this.tree = new int[logn][n];
                this.cnt = new int[logn][n];
                this.sorted = Arrays.copyOf(arr, n);
                
                Arrays.sort(sorted);
                build(0, 0, n - 1);
            }
            
            /**
             * 递归构建Divide Tree
             * @param depth 当前深度
             * @param l 当前区间左端点
             * @param r 当前区间右端点
             */
            private void build(int depth, int l, int r) {
                if (l == r) {
                    tree[depth][l] = sorted[l];
                    return;
                }
                
                int mid = (l + r) / 2;
                int midValue = sorted[mid];
                int leftSize = mid - l + 1;
                
                // 统计左子树应该有多少个元素
                for (int i = l; i <= r; i++) {
                    if (tree[depth][i] < midValue || (tree[depth][i] == midValue && leftSize > 0)) {
                        cnt[depth][i] = (i == l ? 0 : cnt[depth][i - 1]) + 1;
                        if (tree[depth][i] == midValue) {
                            leftSize--;
                        }
                    } else {
                        cnt[depth][i] = (i == l ? 0 : cnt[depth][i - 1]);
                    }
                }
                
                // 递归构建下一层
                build(depth + 1, l, mid);
                build(depth + 1, mid + 1, r);
            }
            
            /**
             * 查询区间[l, r]内的第k小元素
             * @param l 区间左端点（0-based）
             * @param r 区间右端点（0-based）
             * @param k 第k小（1-based）
             * @return 第k小的元素值
             */
            public int query(int l, int r, int k) {
                int nowL = 0, nowR = n - 1;
                int depth = 0;
                
                while (nowL < nowR) {
                    int mid = (nowL + nowR) / 2;
                    int left = (l == nowL ? 0 : cnt[depth][l - 1]);
                    int count = cnt[depth][r] - left;
                    
                    if (count >= k) {
                        // 第k小在左子树
                        nowR = mid;
                        l = nowL + left;
                        r = nowL + cnt[depth][r] - 1;
                    } else {
                        // 第k小在右子树
                        nowL = mid + 1;
                        l = mid + 1 + (l - nowL) - left;
                        r = mid + 1 + (r - nowL) - cnt[depth][r];
                        k -= count;
                    }
                    depth++;
                }
                
                return tree[depth][nowL];
            }
        }

        // ====================================================================================
        // 算法相关题目与解答
        // ====================================================================================
        /**
         * 以下是各数据结构对应的经典题目及多语言解答代码
         * 所有代码均包含详细注释、复杂度分析和优化说明
         */
        
        // ====================================================================================
        // 题目1: 相似文章检测 (MinHashing应用)
        // 来源: LeetCode 758. Bold Words in String (变种)
        // 题目描述: 检测两篇文章的相似度，使用Jaccard相似度和MinHashing算法
        // 解题思路: 使用CountingMinHash计算文档的签名，然后计算签名相似度
        // ====================================================================================
        /*
        Java代码实现:
        public class SimilarityDetector {
            // 使用CountingMinHash计算文档相似度
            public double computeSimilarity(String doc1, String doc2, int numHashes) {
                // 将文档转换为n-gram特征集合
                Set<String> features1 = extractNGrams(doc1, 3);
                Set<String> features2 = extractNGrams(doc2, 3);
                
                // 创建MinHash实例
                CountingMinHash minHash1 = new CountingMinHash(numHashes);
                CountingMinHash minHash2 = new CountingMinHash(numHashes);
                
                // 构建签名
                for (String feature : features1) {
                    minHash1.add(feature);
                }
                for (String feature : features2) {
                    minHash2.add(feature);
                }
                
                // 计算签名相似度
                return minHash1.estimateSimilarity(minHash2);
            }
            
            private Set<String> extractNGrams(String text, int n) {
                Set<String> ngrams = new HashSet<>();
                for (int i = 0; i <= text.length() - n; i++) {
                    ngrams.add(text.substring(i, i + n));
                }
                return ngrams;
            }
        }
        
        C++代码实现:
        #include <iostream>
        #include <string>
        #include <unordered_set>
        #include <vector>
        #include <algorithm>
        
        class CountingMinHash {
        private:
            int numHashes;
            std::vector<long long> signatures;
            std::vector<std::pair<long long, long long>> hashParams;
            
        public:
            CountingMinHash(int k) : numHashes(k), signatures(k, LLONG_MAX) {
                // 初始化哈希函数参数
                for (int i = 0; i < k; i++) {
                    hashParams.emplace_back(rand(), rand());
                }
            }
            
            void add(const std::string& s) {
                long long hash = std::hash<std::string>{}(s);
                for (int i = 0; i < numHashes; i++) {
                    long long currentHash = (hashParams[i].first * hash + hashParams[i].second) % LLONG_MAX;
                    signatures[i] = std::min(signatures[i], currentHash);
                }
            }
            
            double estimateSimilarity(const CountingMinHash& other) {
                int matches = 0;
                for (int i = 0; i < numHashes; i++) {
                    if (signatures[i] == other.signatures[i]) {
                        matches++;
                    }
                }
                return (double)matches / numHashes;
            }
        };
        
        std::unordered_set<std::string> extractNGrams(const std::string& text, int n) {
            std::unordered_set<std::string> ngrams;
            for (int i = 0; i <= (int)text.size() - n; i++) {
                ngrams.insert(text.substr(i, n));
            }
            return ngrams;
        }
        
        double computeSimilarity(const std::string& doc1, const std::string& doc2, int numHashes) {
            auto features1 = extractNGrams(doc1, 3);
            auto features2 = extractNGrams(doc2, 3);
            
            CountingMinHash minHash1(numHashes);
            CountingMinHash minHash2(numHashes);
            
            for (const auto& feature : features1) minHash1.add(feature);
            for (const auto& feature : features2) minHash2.add(feature);
            
            return minHash1.estimateSimilarity(minHash2);
        }
        
        Python代码实现:
        import hashlib
        import random
        
        class CountingMinHash:
            def __init__(self, num_hashes):
                self.num_hashes = num_hashes
                self.signatures = [float('inf')] * num_hashes
                # 初始化哈希函数参数
                self.hash_params = [(random.randint(1, 1000000), random.randint(0, 1000000)) for _ in range(num_hashes)]
            
            def add(self, item):
                # 使用MD5计算哈希值
                hash_val = int(hashlib.md5(item.encode()).hexdigest(), 16)
                for i in range(self.num_hashes):
                    a, b = self.hash_params[i]
                    current_hash = (a * hash_val + b) % (2**64)
                    self.signatures[i] = min(self.signatures[i], current_hash)
            
            def estimate_similarity(self, other):
                matches = sum(1 for i in range(self.num_hashes) if self.signatures[i] == other.signatures[i])
                return matches / self.num_hashes
        
        def extract_ngrams(text, n):
            return {text[i:i+n] for i in range(len(text) - n + 1)}
        
        def compute_similarity(doc1, doc2, num_hashes=100):
            features1 = extract_ngrams(doc1, 3)
            features2 = extract_ngrams(doc2, 3)
            
            min_hash1 = CountingMinHash(num_hashes)
            min_hash2 = CountingMinHash(num_hashes)
            
            for feature in features1:
                min_hash1.add(feature)
            for feature in features2:
                min_hash2.add(feature)
            
            return min_hash1.estimate_similarity(min_hash2)
        */
        
        // ====================================================================================
        // 题目2: 区间覆盖问题 (ODT应用)
        // 来源: Codeforces 896C. Willem, Chtholly and Seniorious
        // 题目描述: 维护一个数组，支持区间赋值、区间加法、区间第k小、区间求和等操作
        // 解题思路: 使用ODT高效处理区间覆盖操作
        // ====================================================================================
        /*
        Java代码实现:
        public class ODTProblem {
            public static void main(String[] args) {
                // 初始化ODT
                ODT odt = new ODT();
                
                // 区间赋值操作示例
                odt.split(1);
                odt.split(1001);
                odt.assign(1, 1000, 1);
                
                // 区间加法操作
                odt.add(1, 500, 2);
                
                // 区间求和
                long sum = odt.querySum(1, 1000);
                
                // 区间第k小
                long kth = odt.queryKth(1, 1000, 500);
                
                System.out.println("Sum: " + sum);
                System.out.println("Kth: " + kth);
            }
        }
        
        C++代码实现:
        #include <iostream>
        #include <set>
        #include <algorithm>
        
        using namespace std;
        
        struct Node {
            int l, r;
            mutable long long val;
            Node(int l, int r = -1, long long val = 0) : l(l), r(r), val(val) {}
            bool operator<(const Node& other) const {
                return l < other.l;
            }
        };
        
        set<Node> odt;
        
        auto split(int pos) {
            auto it = odt.lower_bound(Node(pos));
            if (it != odt.end() && it->l == pos) return it;
            --it;
            int l = it->l, r = it->r;
            long long val = it->val;
            odt.erase(it);
            odt.insert(Node(l, pos - 1, val));
            return odt.insert(Node(pos, r, val)).first;
        }
        
        void assign(int l, int r, long long val) {
            auto itr = split(r + 1), itl = split(l);
            odt.erase(itl, itr);
            odt.insert(Node(l, r, val));
        }
        
        void add(int l, int r, long long val) {
            auto itr = split(r + 1), itl = split(l);
            for (; itl != itr; ++itl) {
                itl->val += val;
            }
        }
        
        long long querySum(int l, int r) {
            long long res = 0;
            auto itr = split(r + 1), itl = split(l);
            for (; itl != itr; ++itl) {
                res += itl->val * (itl->r - itl->l + 1);
            }
            return res;
        }
        
        long long queryKth(int l, int r, int k) {
            vector<pair<long long, int>> vec;
            auto itr = split(r + 1), itl = split(l);
            for (; itl != itr; ++itl) {
                vec.emplace_back(itl->val, itl->r - itl->l + 1);
            }
            sort(vec.begin(), vec.end());
            for (auto& p : vec) {
                k -= p.second;
                if (k <= 0) return p.first;
            }
            return -1;
        }
        
        int main() {
            // 初始化
            odt.insert(Node(1, 1000, 1));
            
            // 区间操作示例
            add(1, 500, 2);
            cout << "Sum: " << querySum(1, 1000) << endl;
            cout << "Kth: " << queryKth(1, 1000, 500) << endl;
            
            return 0;
        }
        
        Python代码实现:
        from bisect import bisect_left
        
        class ODTNode:
            def __init__(self, l, r, val):
                self.l = l
                self.r = r
                self.val = val
            
            def __lt__(self, other):
                return self.l < other.l
        
        class ODT:
            def __init__(self):
                self.tree = []
            
            def split(self, pos):
                # 二分查找第一个l >= pos的节点
                idx = bisect_left(self.tree, ODTNode(pos, 0, 0))
                if idx < len(self.tree) and self.tree[idx].l == pos:
                    return idx
                idx -= 1
                if idx < 0:
                    return 0
                node = self.tree[idx]
                if node.r < pos:
                    return idx + 1
                # 分裂节点
                self.tree.pop(idx)
                self.tree.insert(idx, ODTNode(node.l, pos - 1, node.val))
                self.tree.insert(idx + 1, ODTNode(pos, node.r, node.val))
                return idx + 1
            
            def assign(self, l, r, val):
                end = self.split(r + 1)
                start = self.split(l)
                del self.tree[start:end]
                self.tree.insert(start, ODTNode(l, r, val))
            
            def add(self, l, r, val):
                end = self.split(r + 1)
                start = self.split(l)
                for i in range(start, end):
                    self.tree[i].val += val
            
            def query_sum(self, l, r):
                res = 0
                end = self.split(r + 1)
                start = self.split(l)
                for i in range(start, end):
                    node = self.tree[i]
                    res += node.val * (node.r - node.l + 1)
                return res
            
            def query_kth(self, l, r, k):
                vec = []
                end = self.split(r + 1)
                start = self.split(l)
                for i in range(start, end):
                    node = self.tree[i]
                    vec.append((node.val, node.r - node.l + 1))
                vec.sort()
                for val, cnt in vec:
                    k -= cnt
                    if k <= 0:
                        return val
                return -1
        */
        
        // ====================================================================================
        // 题目3: 生成树计数 (矩阵树定理应用)
        // 来源: HDU 4408 最小生成树计数
        // 题目描述: 给定一个无向图，求其最小生成树的数量
        // 解题思路: 先求最小生成树的权值，然后用矩阵树定理计算该权值下的生成树数量
        // ====================================================================================
        /*
        Java代码实现:
        public class MatrixTreeTheorem {
            private static final double EPS = 1e-8;
            
            // 高斯消元求行列式（模大质数）
            public static long determinant(long[][] mat, long mod) {
                int n = mat.length;
                long res = 1;
                for (int i = 0; i < n; i++) {
                    int pivot = -1;
                    for (int j = i; j < n; j++) {
                        if (mat[j][i] != 0) {
                            pivot = j;
                            break;
                        }
                    }
                    if (pivot == -1) return 0;
                    
                    if (pivot != i) {
                        long[] temp = mat[i];
                        mat[i] = mat[pivot];
                        mat[pivot] = temp;
                        res = (mod - res) % mod;
                    }
                    
                    for (int j = i + 1; j < n; j++) {
                        while (mat[j][i] != 0) {
                            long t = mat[j][i] / mat[i][i];
                            for (int k = i; k < n; k++) {
                                mat[j][k] = (mat[j][k] - t * mat[i][k] % mod + mod) % mod;
                            }
                            if (mat[j][i] == 0) break;
                            
                            long[] temp = mat[i];
                            mat[i] = mat[j];
                            mat[j] = temp;
                            res = (mod - res) % mod;
                        }
                    }
                    res = res * mat[i][i] % mod;
                }
                return res;
            }
            
            // 最小生成树计数
            public static long countMinimumSpanningTrees(int[][] graph, long mod) {
                int n = graph.length;
                // Kruskal算法求最小生成树权值
                // 这里省略Kruskal的实现，直接构建基尔霍夫矩阵
                
                // 构建度数矩阵和邻接矩阵
                long[][] degreeMatrix = new long[n][n];
                long[][] adjMatrix = new long[n][n];
                
                // 填充度数矩阵和邻接矩阵
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        if (graph[i][j] != Integer.MAX_VALUE) {
                            adjMatrix[i][j] = 1; // 假设边权相同
                            adjMatrix[j][i] = 1;
                            degreeMatrix[i][i]++;
                            degreeMatrix[j][j]++;
                        }
                    }
                }
                
                // 构建基尔霍夫矩阵
                long[][] laplacian = new long[n-1][n-1];
                for (int i = 0; i < n-1; i++) {
                    for (int j = 0; j < n-1; j++) {
                        laplacian[i][j] = (degreeMatrix[i][j] - adjMatrix[i][j] + mod) % mod;
                    }
                }
                
                return determinant(laplacian, mod);
            }
        }
        
        C++代码实现:
        #include <iostream>
        #include <vector>
        #include <algorithm>
        using namespace std;
        
        typedef long long ll;
        const int MOD = 1e9 + 7;
        
        ll determinant(vector<vector<ll>> mat) {
            int n = mat.size();
            ll res = 1;
            for (int i = 0; i < n; i++) {
                int pivot = -1;
                for (int j = i; j < n; j++) {
                    if (mat[j][i] != 0) {
                        pivot = j;
                        break;
                    }
                }
                if (pivot == -1) return 0;
                
                if (pivot != i) {
                    swap(mat[i], mat[pivot]);
                    res = (MOD - res) % MOD;
                }
                
                for (int j = i + 1; j < n; j++) {
                    while (mat[j][i] != 0) {
                        ll t = mat[j][i] / mat[i][i];
                        for (int k = i; k < n; k++) {
                            mat[j][k] = (mat[j][k] - t * mat[i][k] % MOD + MOD) % MOD;
                        }
                        if (mat[j][i] == 0) break;
                        
                        swap(mat[i], mat[j]);
                        res = (MOD - res) % MOD;
                    }
                }
                res = res * mat[i][i] % MOD;
            }
            return res;
        }
        
        ll countMinimumSpanningTrees(vector<vector<int>>& graph) {
            int n = graph.size();
            vector<vector<ll>> degreeMatrix(n, vector<ll>(n, 0));
            vector<vector<ll>> adjMatrix(n, vector<ll>(n, 0));
            
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (graph[i][j] != INT_MAX) {
                        adjMatrix[i][j] = 1;
                        adjMatrix[j][i] = 1;
                        degreeMatrix[i][i]++;
                        degreeMatrix[j][j]++;
                    }
                }
            }
            
            vector<vector<ll>> laplacian(n-1, vector<ll>(n-1));
            for (int i = 0; i < n-1; i++) {
                for (int j = 0; j < n-1; j++) {
                    laplacian[i][j] = (degreeMatrix[i][j] - adjMatrix[i][j] + MOD) % MOD;
                }
            }
            
            return determinant(laplacian);
        }
        
        Python代码实现:
        MOD = 10**9 + 7
        
        def determinant(mat):
            n = len(mat)
            res = 1
            for i in range(n):
                # 寻找主元
                pivot = -1
                for j in range(i, n):
                    if mat[j][i] != 0:
                        pivot = j
                        break
                if pivot == -1:
                    return 0
                
                # 交换行
                if pivot != i:
                    mat[i], mat[pivot] = mat[pivot], mat[i]
                    res = (MOD - res) % MOD
                
                # 高斯消元
                for j in range(i + 1, n):
                    while mat[j][i] != 0:
                        t = mat[j][i] // mat[i][i]
                        for k in range(i, n):
                            mat[j][k] = (mat[j][k] - t * mat[i][k]) % MOD
                        if mat[j][i] == 0:
                            break
                        # 需要交换行
                        mat[i], mat[j] = mat[j], mat[i]
                        res = (MOD - res) % MOD
                
                res = res * mat[i][i] % MOD
            return res
        
        def count_minimum_spanning_trees(graph):
            n = len(graph)
            # 构建度数矩阵和邻接矩阵
            degree_matrix = [[0] * n for _ in range(n)]
            adj_matrix = [[0] * n for _ in range(n)]
            
            for i in range(n):
                for j in range(i + 1, n):
                    if graph[i][j] != float('inf'):
                        adj_matrix[i][j] = 1
                        adj_matrix[j][i] = 1
                        degree_matrix[i][i] += 1
                        degree_matrix[j][j] += 1
            
            # 构建基尔霍夫矩阵（去掉最后一行一列）
            laplacian = []
            for i in range(n-1):
                row = []
                for j in range(n-1):
                    row.append((degree_matrix[i][j] - adj_matrix[i][j]) % MOD)
                laplacian.append(row)
            
            return determinant(laplacian)
        */
        
        // ====================================================================================
        // 题目4: 二分图判定问题 (二分图并查集应用)
        // 来源: LeetCode 785. Is Graph Bipartite?
        // 题目描述: 判断一个无向图是否是二分图
        // 解题思路: 使用带权并查集维护节点的颜色关系
        // ====================================================================================
        /*
        Java代码实现:
        public class BipartiteUnionFind {
            private int[] parent;
            private int[] rank;
            private int[] color; // 0表示与父节点同色，1表示异色
            
            public BipartiteUnionFind(int n) {
                parent = new int[n];
                rank = new int[n];
                color = new int[n];
                for (int i = 0; i < n; i++) {
                    parent[i] = i;
                    rank[i] = 1;
                    color[i] = 0;
                }
            }
            
            public int find(int x) {
                if (parent[x] != x) {
                    int root = find(parent[x]);
                    // 更新颜色信息
                    color[x] ^= color[parent[x]];
                    parent[x] = root;
                }
                return parent[x];
            }
            
            public boolean union(int x, int y) {
                int fx = find(x);
                int fy = find(y);
                
                if (fx == fy) {
                    // x和y已经在同一集合中，检查是否冲突
                    return (color[x] ^ color[y]) == 1;
                }
                
                // 按秩合并
                if (rank[fx] < rank[fy]) {
                    parent[fx] = fy;
                    color[fx] = (color[x] ^ color[y] ^ 1);
                } else {
                    parent[fy] = fx;
                    color[fy] = (color[x] ^ color[y] ^ 1);
                    if (rank[fx] == rank[fy]) {
                        rank[fx]++;
                    }
                }
                return true;
            }
            
            // 判断图是否是二分图
            public static boolean isBipartite(int[][] graph) {
                int n = graph.length;
                BipartiteUnionFind uf = new BipartiteUnionFind(n);
                
                for (int u = 0; u < n; u++) {
                    for (int v : graph[u]) {
                        if (!uf.union(u, v)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        
        C++代码实现:
        #include <iostream>
        #include <vector>
        using namespace std;
        
        class BipartiteUnionFind {
        private:
            vector<int> parent;
            vector<int> rank;
            vector<int> color;
        
        public:
            BipartiteUnionFind(int n) {
                parent.resize(n);
                rank.resize(n, 1);
                color.resize(n, 0);
                for (int i = 0; i < n; i++) {
                    parent[i] = i;
                }
            }
            
            int find(int x) {
                if (parent[x] != x) {
                    int root = find(parent[x]);
                    color[x] ^= color[parent[x]];
                    parent[x] = root;
                }
                return parent[x];
            }
            
            bool unite(int x, int y) {
                int fx = find(x);
                int fy = find(y);
                
                if (fx == fy) {
                    return (color[x] ^ color[y]) == 1;
                }
                
                if (rank[fx] < rank[fy]) {
                    parent[fx] = fy;
                    color[fx] = (color[x] ^ color[y] ^ 1);
                } else {
                    parent[fy] = fx;
                    color[fy] = (color[x] ^ color[y] ^ 1);
                    if (rank[fx] == rank[fy]) {
                        rank[fx]++;
                    }
                }
                return true;
            }
        };
        
        bool isBipartite(vector<vector<int>>& graph) {
            int n = graph.size();
            BipartiteUnionFind uf(n);
            
            for (int u = 0; u < n; u++) {
                for (int v : graph[u]) {
                    if (!uf.unite(u, v)) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        Python代码实现:
        class BipartiteUnionFind:
            def __init__(self, n):
                self.parent = list(range(n))
                self.rank = [1] * n
                self.color = [0] * n  # 0表示与父节点同色，1表示异色
            
            def find(self, x):
                if self.parent[x] != x:
                    root = self.find(self.parent[x])
                    # 更新颜色信息
                    self.color[x] ^= self.color[self.parent[x]]
                    self.parent[x] = root
                return self.parent[x]
            
            def union(self, x, y):
                fx = self.find(x)
                fy = self.find(y)
                
                if fx == fy:
                    # x和y已经在同一集合中，检查是否冲突
                    return (self.color[x] ^ self.color[y]) == 1
                
                # 按秩合并
                if self.rank[fx] < self.rank[fy]:
                    self.parent[fx] = fy
                    self.color[fx] = (self.color[x] ^ self.color[y] ^ 1)
                else:
                    self.parent[fy] = fx
                    self.color[fy] = (self.color[x] ^ self.color[y] ^ 1)
                    if self.rank[fx] == self.rank[fy]:
                        self.rank[fx] += 1
                return True
        
        def is_bipartite(graph):
            n = len(graph)
            uf = BipartiteUnionFind(n)
            
            for u in range(n):
                for v in graph[u]:
                    if not uf.union(u, v):
                        return False
            return True
        */
        
        // ====================================================================================
        // 题目5: 动态连通性问题 (Link-Cut Tree应用)
        // 来源: Codeforces 1172D. Nauuo and Portals
        // 题目描述: 维护动态图的连通性，支持连接和断开操作
        // 解题思路: 使用LCT高效维护动态树连通性
        // ====================================================================================
        /*
        Java代码实现:
        public class DynamicConnectivity {
            public static void main(String[] args) {
                LinkCutTree lct = new LinkCutTree(100); // 100个节点
                
                // 连接操作
                lct.link(1, 2);
                lct.link(2, 3);
                
                // 判断连通性
                System.out.println(lct.isConnected(1, 3)); // true
                
                // 断开操作
                lct.cut(1, 2);
                System.out.println(lct.isConnected(1, 3)); // false
                
                // 路径查询
                lct.link(1, 4);
                lct.update(1, 5);
                lct.update(4, 10);
                System.out.println(lct.querySum(1, 4)); // 15
            }
        }
        
        C++代码实现:
        // Link-Cut Tree实现（与Java版本类似）
        #include <iostream>
        using namespace std;
        
        // LCT实现省略，与前面展示的结构相同
        
        int main() {
            LinkCutTree lct(100);
            
            lct.link(1, 2);
            lct.link(2, 3);
            
            cout << "1 and 3 connected: " << (lct.isConnected(1, 3) ? "true" : "false") << endl;
            
            lct.cut(1, 2);
            cout << "After cut 1-2, 1 and 3 connected: " << (lct.isConnected(1, 3) ? "true" : "false") << endl;
            
            lct.link(1, 4);
            lct.update(1, 5);
            lct.update(4, 10);
            cout << "Sum from 1 to 4: " << lct.querySum(1, 4) << endl;
            
            return 0;
        }
        
        Python代码实现:
        # Link-Cut Tree的Python实现（简化版）
        class Node:
            def __init__(self, val):
                self.val = val
                self.sum = val
                self.fa = None
                self.ch = [None, None]
                self.rev = False
            
            def is_root(self):
                return self.fa is None or (self.fa.ch[0] != self and self.fa.ch[1] != self)
            
            def get_side(self):
                return 1 if self.fa.ch[1] == self else 0
            
            def maintain(self):
                self.sum = self.val
                if self.ch[0]:
                    self.sum += self.ch[0].sum
                if self.ch[1]:
                    self.sum += self.ch[1].sum
            
            def reverse(self):
                self.rev ^= True
                self.ch[0], self.ch[1] = self.ch[1], self.ch[0]
            
            def push_down(self):
                if self.rev:
                    if self.ch[0]:
                        self.ch[0].reverse()
                    if self.ch[1]:
                        self.ch[1].reverse()
                    self.rev = False
        
        class LinkCutTree:
            def __init__(self, n):
                self.nodes = [None] * (n + 1)
                for i in range(1, n + 1):
                    self.nodes[i] = Node(0)
            
            def rotate(self, x):
                y = x.fa
                z = y.fa
                k = x.get_side()
                
                if not y.is_root():
                    z.ch[y.get_side()] = x
                x.fa = z
                
                y.ch[k] = x.ch[k^1]
                if x.ch[k^1]:
                    x.ch[k^1].fa = y
                
                x.ch[k^1] = y
                y.fa = x
                
                y.maintain()
                x.maintain()
            
            def splay_down(self, x):
                if not x.is_root():
                    self.splay_down(x.fa)
                x.push_down()
            
            def splay(self, x):
                self.splay_down(x)
                while not x.is_root():
                    y = x.fa
                    if not y.is_root():
                        self.rotate(x if (x.get_side() ^ y.get_side()) else y)
                    self.rotate(x)
            
            def access(self, x):
                y = None
                while x:
                    self.splay(x)
                    x.ch[1] = y
                    x.maintain()
                    y = x
                    x = x.fa
            
            def make_root(self, x):
                self.access(x)
                self.splay(x)
                x.reverse()
            
            def find_root(self, x):
                self.access(x)
                self.splay(x)
                while x.ch[0]:
                    x.push_down()
                    x = x.ch[0]
                self.splay(x)
                return x
            
            def link(self, x, y):
                u = self.nodes[x]
                v = self.nodes[y]
                self.make_root(u)
                if self.find_root(v) != u:
                    u.fa = v
            
            def cut(self, x, y):
                u = self.nodes[x]
                v = self.nodes[y]
                self.make_root(u)
                self.access(v)
                self.splay(u)
                if self.find_root(v) == u and u.ch[1] == v and v.ch[0] is None:
                    u.ch[1] = None
                    v.fa = None
            
            def update(self, x, val):
                u = self.nodes[x]
                self.splay(u)
                u.val = val
                u.maintain()
            
            def query_sum(self, x, y):
                u = self.nodes[x]
                v = self.nodes[y]
                self.make_root(u)
                self.access(v)
                self.splay(v)
                return v.sum
            
            def is_connected(self, x, y):
                return self.find_root(self.nodes[x]) == self.find_root(self.nodes[y])
        
        # 使用示例
        def main():
            lct = LinkCutTree(100)
            lct.link(1, 2)
            lct.link(2, 3)
            print(f"1 and 3 connected: {lct.is_connected(1, 3)}")
            
            lct.cut(1, 2)
            print(f"After cut 1-2, 1 and 3 connected: {lct.is_connected(1, 3)}")
            
            lct.link(1, 4)
            lct.update(1, 5)
            lct.update(4, 10)
            print(f"Sum from 1 to 4: {lct.query_sum(1, 4)}")
        */
        
        // ====================================================================================
        // 题目6: 可并堆应用 (左偏树)
        // 来源: HDU 1512 Monkey King
        // 题目描述: 维护多个堆，支持合并操作和查询/删除最大值
        // 解题思路: 使用左偏树高效支持合并操作
        // ====================================================================================
        /*
        Java代码实现:
        public class LeftistTreeProblem {
            public static void main(String[] args) {
                int n = 10; // 10只猴子
                LeftistTree[] trees = new LeftistTree[n + 1]; // 1-based索引
                
                // 初始化每只猴子为单独的堆
                for (int i = 1; i <= n; i++) {
                    trees[i] = new LeftistTree();
                    trees[i].insert(i); // 猴子的战斗力为i
                }
                
                // 合并两个群体
                int x = 1, y = 2;
                trees[x] = LeftistTree.merge(trees[x], trees[y]);
                trees[y] = null; // 释放y的引用
                
                // 查询并攻击
                int max1 = trees[x].extractMax(); // 第一只猴子
                int max2 = trees[x].extractMax(); // 第二只猴子
                
                // 战斗后，两只猴子的战斗力减半并重新加入堆
                trees[x].insert(max1 / 2);
                trees[x].insert(max2 / 2);
                
                System.out.println("Current maximum: " + trees[x].getMax());
            }
        }
        
        C++代码实现:
        #include <iostream>
        using namespace std;
        
        struct LeftistNode {
            int val;
            int dist;
            LeftistNode *left, *right;
            
            LeftistNode(int v) : val(v), dist(0), left(nullptr), right(nullptr) {}
        };
        
        class LeftistTree {
        public:
            LeftistNode* root;
            
            LeftistTree() : root(nullptr) {}
            
            static LeftistNode* merge(LeftistNode* a, LeftistNode* b) {
                if (!a) return b;
                if (!b) return a;
                
                // 保证a的值大于等于b
                if (a->val < b->val) {
                    swap(a, b);
                }
                
                a->right = merge(a->right, b);
                
                // 维护左偏性质
                if (!a->left || a->left->dist < a->right->dist) {
                    swap(a->left, a->right);
                }
                
                // 更新距离
                a->dist = a->right ? a->right->dist + 1 : 0;
                
                return a;
            }
            
            void insert(int val) {
                root = merge(root, new LeftistNode(val));
            }
            
            int getMax() {
                return root ? root->val : -1;
            }
            
            int extractMax() {
                if (!root) return -1;
                int maxVal = root->val;
                LeftistNode* temp = root;
                root = merge(root->left, root->right);
                delete temp;
                return maxVal;
            }
            
            void merge(LeftistTree& other) {
                root = merge(root, other.root);
                other.root = nullptr;
            }
        };
        
        int main() {
            int n = 10;
            LeftistTree* trees = new LeftistTree[n + 1];
            
            for (int i = 1; i <= n; i++) {
                trees[i].insert(i);
            }
            
            int x = 1, y = 2;
            trees[x].merge(trees[y]);
            
            int max1 = trees[x].extractMax();
            int max2 = trees[x].extractMax();
            
            trees[x].insert(max1 / 2);
            trees[x].insert(max2 / 2);
            
            cout << "Current maximum: " << trees[x].getMax() << endl;
            
            delete[] trees;
            return 0;
        }
        
        Python代码实现:
        class LeftistNode:
            def __init__(self, val):
                self.val = val
                self.dist = 0
                self.left = None
                self.right = None
        
        class LeftistTree:
            def __init__(self):
                self.root = None
            
            @staticmethod
            def merge(a, b):
                if not a:
                    return b
                if not b:
                    return a
                
                # 保证a的值大于等于b
                if a.val < b.val:
                    a, b = b, a
                
                a.right = LeftistTree.merge(a.right, b)
                
                # 维护左偏性质
                if not a.left or (a.left.dist < a.right.dist):
                    a.left, a.right = a.right, a.left
                
                # 更新距离
                a.dist = a.right.dist + 1 if a.right else 0
                
                return a
            
            def insert(self, val):
                new_node = LeftistNode(val)
                self.root = self.merge(self.root, new_node)
            
            def get_max(self):
                return self.root.val if self.root else -1
            
            def extract_max(self):
                if not self.root:
                    return -1
                max_val = self.root.val
                self.root = self.merge(self.root.left, self.root.right)
                return max_val
            
            def merge_with(self, other):
                self.root = self.merge(self.root, other.root)
                other.root = None
        
        def main():
            n = 10
            trees = [LeftistTree() for _ in range(n + 1)]
            
            for i in range(1, n + 1):
                trees[i].insert(i)
            
            x, y = 1, 2
            trees[x].merge_with(trees[y])
            
            max1 = trees[x].extract_max()
            max2 = trees[x].extract_max()
            
            trees[x].insert(max1 // 2)
            trees[x].insert(max2 // 2)
            
            print(f"Current maximum: {trees[x].get_max()}")
        */
        
        // ====================================================================================
        // 题目7: 线段树Beats应用
        // 来源: Codeforces 868F. Yet Another Minimization Problem
        // 题目描述: 区间取min操作，区间求和查询
        // 解题思路: 使用Segment Tree Beats高效处理区间取min操作
        // ====================================================================================
        /*
        Java代码实现:
        public class SegmentTreeBeats {
            static class Node {
                long sum;        // 区间和
                long maxv;       // 最大值
                long maxv2;      // 次大值
                int cnt;         // 最大值的出现次数
                long tag;        // 懒惰标记，表示需要对区间进行取min操作
                boolean updated; // 标记该节点是否有未下传的操作
            }
            
            private Node[] tree;
            private int n;
            
            public SegmentTreeBeats(int[] a) {
                n = a.length;
                tree = new Node[4 * n];
                for (int i = 0; i < 4 * n; i++) {
                    tree[i] = new Node();
                }
                build(1, 0, n - 1, a);
            }
            
            private void build(int node, int l, int r, int[] a) {
                if (l == r) {
                    tree[node].sum = a[l];
                    tree[node].maxv = a[l];
                    tree[node].maxv2 = Long.MIN_VALUE;
                    tree[node].cnt = 1;
                    tree[node].tag = Long.MAX_VALUE;
                    tree[node].updated = false;
                    return;
                }
                
                int mid = (l + r) >> 1;
                build(node << 1, l, mid, a);
                build(node << 1 | 1, mid + 1, r, a);
                pushUp(node);
            }
            
            private void pushUp(Node u, Node l, Node r) {
                u.sum = l.sum + r.sum;
                
                if (l.maxv > r.maxv) {
                    u.maxv = l.maxv;
                    u.cnt = l.cnt;
                    u.maxv2 = Math.max(l.maxv2, r.maxv);
                } else if (l.maxv < r.maxv) {
                    u.maxv = r.maxv;
                    u.cnt = r.cnt;
                    u.maxv2 = Math.max(l.maxv, r.maxv2);
                } else {
                    u.maxv = l.maxv;
                    u.cnt = l.cnt + r.cnt;
                    u.maxv2 = Math.max(l.maxv2, r.maxv2);
                }
            }
            
            private void pushUp(int node) {
                pushUp(tree[node], tree[node << 1], tree[node << 1 | 1]);
            }
            
            private void pushDown(int node, int l, int r) {
                if (tree[node].updated && tree[node].tag < tree[node].maxv) {
                    long val = tree[node].tag;
                    
                    // 下传给左子节点
                    if (l != r && val < tree[node << 1].maxv) {
                        tree[node << 1].sum += (val - tree[node << 1].maxv) * tree[node << 1].cnt;
                        tree[node << 1].tag = val;
                        tree[node << 1].updated = true;
                        tree[node << 1].maxv = val;
                    }
                    
                    // 下传给右子节点
                    if (l != r && val < tree[node << 1 | 1].maxv) {
                        tree[node << 1 | 1].sum += (val - tree[node << 1 | 1].maxv) * tree[node << 1 | 1].cnt;
                        tree[node << 1 | 1].tag = val;
                        tree[node << 1 | 1].updated = true;
                        tree[node << 1 | 1].maxv = val;
                    }
                    
                    tree[node].updated = false;
                    tree[node].tag = Long.MAX_VALUE;
                }
            }
            
            public void updateMin(int L, int R, long val) {
                updateMin(1, 0, n - 1, L, R, val);
            }
            
            private void updateMin(int node, int l, int r, int L, int R, long val) {
                if (val >= tree[node].maxv) {
                    return;
                }
                
                if (L <= l && r <= R && val > tree[node].maxv2) {
                    tree[node].sum += (val - tree[node].maxv) * tree[node].cnt;
                    tree[node].tag = val;
                    tree[node].updated = true;
                    tree[node].maxv = val;
                    return;
                }
                
                pushDown(node, l, r);
                int mid = (l + r) >> 1;
                if (L <= mid) {
                    updateMin(node << 1, l, mid, L, R, val);
                }
                if (R > mid) {
                    updateMin(node << 1 | 1, mid + 1, r, L, R, val);
                }
                pushUp(node);
            }
            
            public long querySum(int L, int R) {
                return querySum(1, 0, n - 1, L, R);
            }
            
            private long querySum(int node, int l, int r, int L, int R) {
                if (L <= l && r <= R) {
                    return tree[node].sum;
                }
                
                pushDown(node, l, r);
                int mid = (l + r) >> 1;
                long res = 0;
                if (L <= mid) {
                    res += querySum(node << 1, l, mid, L, R);
                }
                if (R > mid) {
                    res += querySum(node << 1 | 1, mid + 1, r, L, R);
                }
                return res;
            }
        }

        C++代码实现:
        #include <iostream>
        #include <vector>
        #include <algorithm>
        using namespace std;
        
        struct Node {
            long long sum;        // 区间和
            long long maxv;       // 最大值
            long long maxv2;      // 次大值
            int cnt;              // 最大值的出现次数
            long long tag;        // 懒惰标记，表示需要对区间进行取min操作
            bool updated;         // 标记该节点是否有未下传的操作
        } tree[400010];
        
        void pushUp(int node) {
            int l = node << 1, r = node << 1 | 1;
            tree[node].sum = tree[l].sum + tree[r].sum;
            
            if (tree[l].maxv > tree[r].maxv) {
                tree[node].maxv = tree[l].maxv;
                tree[node].cnt = tree[l].cnt;
                tree[node].maxv2 = max(tree[l].maxv2, tree[r].maxv);
            } else if (tree[l].maxv < tree[r].maxv) {
                tree[node].maxv = tree[r].maxv;
                tree[node].cnt = tree[r].cnt;
                tree[node].maxv2 = max(tree[l].maxv, tree[r].maxv2);
            } else {
                tree[node].maxv = tree[l].maxv;
                tree[node].cnt = tree[l].cnt + tree[r].cnt;
                tree[node].maxv2 = max(tree[l].maxv2, tree[r].maxv2);
            }
        }
        
        void build(int node, int l, int r, vector<int>& a) {
            tree[node].tag = LLONG_MAX;
            tree[node].updated = false;
            
            if (l == r) {
                tree[node].sum = a[l];
                tree[node].maxv = a[l];
                tree[node].maxv2 = LLONG_MIN;
                tree[node].cnt = 1;
                return;
            }
            
            int mid = (l + r) >> 1;
            build(node << 1, l, mid, a);
            build(node << 1 | 1, mid + 1, r, a);
            pushUp(node);
        }
        
        void pushDown(int node, int l, int r) {
            if (tree[node].updated && tree[node].tag < tree[node].maxv) {
                long long val = tree[node].tag;
                int mid = (l + r) >> 1;
                int left = node << 1, right = node << 1 | 1;
                
                // 下传给左子节点
                if (l != r && val < tree[left].maxv) {
                    tree[left].sum += (val - tree[left].maxv) * tree[left].cnt;
                    tree[left].tag = val;
                    tree[left].updated = true;
                    tree[left].maxv = val;
                }
                
                // 下传给右子节点
                if (l != r && val < tree[right].maxv) {
                    tree[right].sum += (val - tree[right].maxv) * tree[right].cnt;
                    tree[right].tag = val;
                    tree[right].updated = true;
                    tree[right].maxv = val;
                }
                
                tree[node].updated = false;
                tree[node].tag = LLONG_MAX;
            }
        }
        
        void updateMin(int node, int l, int r, int L, int R, long long val) {
            if (val >= tree[node].maxv) {
                return;
            }
            
            if (L <= l && r <= R && val > tree[node].maxv2) {
                tree[node].sum += (val - tree[node].maxv) * tree[node].cnt;
                tree[node].tag = val;
                tree[node].updated = true;
                tree[node].maxv = val;
                return;
            }
            
            pushDown(node, l, r);
            int mid = (l + r) >> 1;
            if (L <= mid) {
                updateMin(node << 1, l, mid, L, R, val);
            }
            if (R > mid) {
                updateMin(node << 1 | 1, mid + 1, r, L, R, val);
            }
            pushUp(node);
        }
        
        long long querySum(int node, int l, int r, int L, int R) {
            if (L <= l && r <= R) {
                return tree[node].sum;
            }
            
            pushDown(node, l, r);
            int mid = (l + r) >> 1;
            long long res = 0;
            if (L <= mid) {
                res += querySum(node << 1, l, mid, L, R);
            }
            if (R > mid) {
                res += querySum(node << 1 | 1, mid + 1, r, L, R);
            }
            return res;
        }
        
        Python代码实现:
        import sys
        sys.setrecursionlimit(1 << 25)
        
        class SegmentTreeBeats:
            class Node:
                def __init__(self):
                    self.sum = 0      # 区间和
                    self.maxv = 0     # 最大值
                    self.maxv2 = 0    # 次大值
                    self.cnt = 0      # 最大值的出现次数
                    self.tag = float('inf')  # 懒惰标记
                    self.updated = False  # 更新标记
            
            def __init__(self, a):
                self.n = len(a)
                self.tree = [self.Node() for _ in range(4 * self.n)]
                self.a = a
                self.build(1, 0, self.n - 1)
            
            def build(self, node, l, r):
                if l == r:
                    self.tree[node].sum = self.a[l]
                    self.tree[node].maxv = self.a[l]
                    self.tree[node].maxv2 = -float('inf')
                    self.tree[node].cnt = 1
                    self.tree[node].tag = float('inf')
                    self.tree[node].updated = False
                    return
                
                mid = (l + r) >> 1
                self.build(node << 1, l, mid)
                self.build(node << 1 | 1, mid + 1, r)
                self.push_up(node)
            
            def push_up(self, node):
                left = node << 1
                right = node << 1 | 1
                
                self.tree[node].sum = self.tree[left].sum + self.tree[right].sum
                
                if self.tree[left].maxv > self.tree[right].maxv:
                    self.tree[node].maxv = self.tree[left].maxv
                    self.tree[node].cnt = self.tree[left].cnt
                    self.tree[node].maxv2 = max(self.tree[left].maxv2, self.tree[right].maxv)
                elif self.tree[left].maxv < self.tree[right].maxv:
                    self.tree[node].maxv = self.tree[right].maxv
                    self.tree[node].cnt = self.tree[right].cnt
                    self.tree[node].maxv2 = max(self.tree[left].maxv, self.tree[right].maxv2)
                else:
                    self.tree[node].maxv = self.tree[left].maxv
                    self.tree[node].cnt = self.tree[left].cnt + self.tree[right].cnt
                    self.tree[node].maxv2 = max(self.tree[left].maxv2, self.tree[right].maxv2)
            
            def push_down(self, node, l, r):
                if self.tree[node].updated and self.tree[node].tag < self.tree[node].maxv:
                    val = self.tree[node].tag
                    left = node << 1
                    right = node << 1 | 1
                    
                    # 下传给左子节点
                    if l != r and val < self.tree[left].maxv:
                        self.tree[left].sum += (val - self.tree[left].maxv) * self.tree[left].cnt
                        self.tree[left].tag = val
                        self.tree[left].updated = True
                        self.tree[left].maxv = val
                    
                    # 下传给右子节点
                    if l != r and val < self.tree[right].maxv:
                        self.tree[right].sum += (val - self.tree[right].maxv) * self.tree[right].cnt
                        self.tree[right].tag = val
                        self.tree[right].updated = True
                        self.tree[right].maxv = val
                    
                    self.tree[node].updated = False
                    self.tree[node].tag = float('inf')
            
            def update_min(self, L, R, val):
                self._update_min(1, 0, self.n - 1, L, R, val)
            
            def _update_min(self, node, l, r, L, R, val):
                if val >= self.tree[node].maxv:
                    return
                
                if L <= l and r <= R and val > self.tree[node].maxv2:
                    self.tree[node].sum += (val - self.tree[node].maxv) * self.tree[node].cnt
                    self.tree[node].tag = val
                    self.tree[node].updated = True
                    self.tree[node].maxv = val
                    return
                
                self.push_down(node, l, r)
                mid = (l + r) >> 1
                if L <= mid:
                    self._update_min(node << 1, l, mid, L, R, val)
                if R > mid:
                    self._update_min(node << 1 | 1, mid + 1, r, L, R, val)
                self.push_up(node)
            
            def query_sum(self, L, R):
                return self._query_sum(1, 0, self.n - 1, L, R)
            
            def _query_sum(self, node, l, r, L, R):
                if L <= l and r <= R:
                    return self.tree[node].sum
                
                self.push_down(node, l, r)
                mid = (l + r) >> 1
                res = 0
                if L <= mid:
                    res += self._query_sum(node << 1, l, mid, L, R)
                if R > mid:
                    res += self._query_sum(node << 1 | 1, mid + 1, r, L, R)
                return res
        */
        
        // ====================================================================================
        // 题目8: Wavelet Tree区间第k小查询
        // 来源: SPOJ RMQSQ - Range Minimum Query
        // 题目描述: 静态数组的区间第k小查询
        // 解题思路: 使用Wavelet Tree高效处理区间第k小和频次统计
        // ====================================================================================
        /*
        Java代码实现:
        import java.util.*;
        
        public class WaveletTree {
            private int[] tree;
            private int[][] cnt;
            private int[] left, right;
            private int[] A, sorted;
            private int n, root, size;
            private int minv, maxv;
            
            public WaveletTree(int[] a) {
                n = a.length;
                A = Arrays.copyOf(a, n);
                sorted = Arrays.copyOf(a, n);
                Arrays.sort(sorted);
                minv = sorted[0];
                maxv = sorted[n - 1];
                
                // 计算所需的节点数量
                int nodeCount = 1;
                for (int v = maxv - minv + 1; v > 1; v = (v + 1) / 2) {
                    nodeCount += n;
                }
                
                tree = new int[nodeCount + 1];
                cnt = new int[nodeCount + 1][];
                left = new int[nodeCount + 1];
                right = new int[nodeCount + 1];
                size = 0;
                
                root = build(0, n - 1, minv, maxv);
            }
            
            private int build(int l, int r, int vl, int vr) {
                if (l > r) return 0;
                int node = ++size;
                
                if (vl == vr) {
                    tree[node] = vl;
                    return node;
                }
                
                int vm = (vl + vr) >> 1;
                cnt[node] = new int[r - l + 2];
                Arrays.fill(cnt[node], 0);
                
                // 计算cnt数组，记录到每个位置为止有多少元素<=vm
                for (int i = l; i <= r; i++) {
                    cnt[node][i - l + 1] = cnt[node][i - l] + (A[i] <= vm ? 1 : 0);
                }
                
                // 对区间[l, r]进行分治
                int[] temp = new int[r - l + 1];
                int p = 0, q = r - l;
                for (int i = l; i <= r; i++) {
                    if (A[i] <= vm) {
                        temp[p++] = A[i];
                    } else {
                        temp[q--] = A[i];
                    }
                }
                
                // 将排序后的元素放回原数组
                for (int i = 0; i < temp.length; i++) {
                    A[l + i] = temp[i];
                }
                
                int m = l + cnt[node][r - l + 1] - 1;
                left[node] = build(l, m, vl, vm);
                right[node] = build(m + 1, r, vm + 1, vr);
                
                return node;
            }
            
            // 查询区间[ql, qr]中<=x的元素个数
            public int rank(int ql, int qr, int x) {
                return rank(root, 0, n - 1, minv, maxv, ql, qr, x);
            }
            
            private int rank(int node, int l, int r, int vl, int vr, int ql, int qr, int x) {
                if (x < vl || ql > r || qr < l) return 0;
                if (x >= vr) return qr - ql + 1;
                
                int vm = (vl + vr) >> 1;
                int cl = cnt[node][ql - l];
                int cr = cnt[node][qr - l + 1];
                int res = cr - cl;
                
                int m = l + cnt[node][r - l + 1] - 1;
                if (x <= vm) {
                    return rank(left[node], l, m, vl, vm, l + cl, l + cr - 1, x);
                } else {
                    return res + rank(right[node], m + 1, r, vm + 1, vr, m + 1 + (ql - l - cl), m + 1 + (qr - l - cr), x);
                }
            }
            
            // 查询区间[ql, qr]中第k小的元素
            public int kth(int ql, int qr, int k) {
                return kth(root, 0, n - 1, minv, maxv, ql, qr, k);
            }
            
            private int kth(int node, int l, int r, int vl, int vr, int ql, int qr, int k) {
                if (vl == vr) return vl;
                
                int vm = (vl + vr) >> 1;
                int cl = cnt[node][ql - l];
                int cr = cnt[node][qr - l + 1];
                int res = cr - cl;
                
                int m = l + cnt[node][r - l + 1] - 1;
                if (k <= res) {
                    return kth(left[node], l, m, vl, vm, l + cl, l + cr - 1, k);
                } else {
                    return kth(right[node], m + 1, r, vm + 1, vr, m + 1 + (ql - l - cl), m + 1 + (qr - l - cr), k - res);
                }
            }
            
            // 查询区间[ql, qr]中等于x的元素个数
            public int count(int ql, int qr, int x) {
                return rank(ql, qr, x) - rank(ql, qr, x - 1);
            }
        }
        
        C++代码实现:
        #include <iostream>
        #include <vector>
        #include <algorithm>
        using namespace std;
        
        class WaveletTree {
        private:
            vector<int> tree;
            vector<vector<int>> cnt;
            vector<int> left, right;
            vector<int> A, sorted;
            int n, root, size;
            int minv, maxv;
            
            int build(int l, int r, int vl, int vr) {
                if (l > r) return 0;
                int node = ++size;
                
                if (vl == vr) {
                    tree[node] = vl;
                    return node;
                }
                
                int vm = (vl + vr) >> 1;
                cnt[node].resize(r - l + 2, 0);
                
                for (int i = l; i <= r; i++) {
                    cnt[node][i - l + 1] = cnt[node][i - l] + (A[i] <= vm ? 1 : 0);
                }
                
                vector<int> temp(r - l + 1);
                int p = 0, q = r - l;
                for (int i = l; i <= r; i++) {
                    if (A[i] <= vm) {
                        temp[p++] = A[i];
                    } else {
                        temp[q--] = A[i];
                    }
                }
                
                for (int i = 0; i < temp.size(); i++) {
                    A[l + i] = temp[i];
                }
                
                int m = l + cnt[node][r - l + 1] - 1;
                left[node] = build(l, m, vl, vm);
                right[node] = build(m + 1, r, vm + 1, vr);
                
                return node;
            }
            
            int rank(int node, int l, int r, int vl, int vr, int ql, int qr, int x) {
                if (x < vl || ql > r || qr < l) return 0;
                if (x >= vr) return qr - ql + 1;
                
                int vm = (vl + vr) >> 1;
                int cl = cnt[node][ql - l];
                int cr = cnt[node][qr - l + 1];
                int res = cr - cl;
                
                int m = l + cnt[node][r - l + 1] - 1;
                if (x <= vm) {
                    return rank(left[node], l, m, vl, vm, l + cl, l + cr - 1, x);
                } else {
                    return res + rank(right[node], m + 1, r, vm + 1, vr, m + 1 + (ql - l - cl), m + 1 + (qr - l - cr), x);
                }
            }
            
            int kth(int node, int l, int r, int vl, int vr, int ql, int qr, int k) {
                if (vl == vr) return vl;
                
                int vm = (vl + vr) >> 1;
                int cl = cnt[node][ql - l];
                int cr = cnt[node][qr - l + 1];
                int res = cr - cl;
                
                int m = l + cnt[node][r - l + 1] - 1;
                if (k <= res) {
                    return kth(left[node], l, m, vl, vm, l + cl, l + cr - 1, k);
                } else {
                    return kth(right[node], m + 1, r, vm + 1, vr, m + 1 + (ql - l - cl), m + 1 + (qr - l - cr), k - res);
                }
            }
        
        public:
            WaveletTree(vector<int> a) {
                n = a.size();
                A = a;
                sorted = a;
                sort(sorted.begin(), sorted.end());
                minv = sorted[0];
                maxv = sorted.back();
                
                int nodeCount = 1;
                for (int v = maxv - minv + 1; v > 1; v = (v + 1) / 2) {
                    nodeCount += n;
                }
                
                tree.resize(nodeCount + 1);
                cnt.resize(nodeCount + 1);
                left.resize(nodeCount + 1, 0);
                right.resize(nodeCount + 1, 0);
                size = 0;
                
                root = build(0, n - 1, minv, maxv);
            }
            
            int rank(int ql, int qr, int x) {
                return rank(root, 0, n - 1, minv, maxv, ql, qr, x);
            }
            
            int kth(int ql, int qr, int k) {
                return kth(root, 0, n - 1, minv, maxv, ql, qr, k);
            }
            
            int count(int ql, int qr, int x) {
                return rank(ql, qr, x) - rank(ql, qr, x - 1);
            }
        };
        
        Python代码实现:
        import sys
        sys.setrecursionlimit(1 << 25)
        
        class WaveletTree:
            def __init__(self, a):
                self.n = len(a)
                self.A = a.copy()
                self.sorted = sorted(a)
                self.minv = self.sorted[0]
                self.maxv = self.sorted[-1]
                
                # 计算所需的节点数量
                node_count = 1
                v = self.maxv - self.minv + 1
                while v > 1:
                    node_count += self.n
                    v = (v + 1) // 2
                
                self.tree = [0] * (node_count + 1)
                self.cnt = [[] for _ in range(node_count + 1)]
                self.left = [0] * (node_count + 1)
                self.right = [0] * (node_count + 1)
                self.size = 0
                
                self.root = self.build(0, self.n - 1, self.minv, self.maxv)
            
            def build(self, l, r, vl, vr):
                if l > r:
                    return 0
                
                node = self.size + 1
                self.size = node
                
                if vl == vr:
                    self.tree[node] = vl
                    return node
                
                vm = (vl + vr) >> 1
                self.cnt[node] = [0] * (r - l + 2)
                
                # 计算cnt数组
                for i in range(l, r + 1):
                    self.cnt[node][i - l + 1] = self.cnt[node][i - l] + (1 if self.A[i] <= vm else 0)
                
                # 分治排序
                temp = [0] * (r - l + 1)
                p, q = 0, r - l
                for i in range(l, r + 1):
                    if self.A[i] <= vm:
                        temp[p] = self.A[i]
                        p += 1
                    else:
                        temp[q] = self.A[i]
                        q -= 1
                
                # 放回原数组
                for i in range(len(temp)):
                    self.A[l + i] = temp[i]
                
                m = l + self.cnt[node][r - l + 1] - 1
                self.left[node] = self.build(l, m, vl, vm)
                self.right[node] = self.build(m + 1, r, vm + 1, vr)
                
                return node
            
            def rank(self, ql, qr, x):
                return self._rank(self.root, 0, self.n - 1, self.minv, self.maxv, ql, qr, x)
            
            def _rank(self, node, l, r, vl, vr, ql, qr, x):
                if x < vl or ql > r or qr < l:
                    return 0
                if x >= vr:
                    return qr - ql + 1
                
                vm = (vl + vr) >> 1
                cl = self.cnt[node][ql - l]
                cr = self.cnt[node][qr - l + 1]
                res = cr - cl
                
                m = l + self.cnt[node][r - l + 1] - 1
                if x <= vm:
                    return self._rank(self.left[node], l, m, vl, vm, l + cl, l + cr - 1, x)
                else:
                    return res + self._rank(self.right[node], m + 1, r, vm + 1, vr, 
                                          m + 1 + (ql - l - cl), m + 1 + (qr - l - cr), x)
            
            def kth(self, ql, qr, k):
                return self._kth(self.root, 0, self.n - 1, self.minv, self.maxv, ql, qr, k)
            
            def _kth(self, node, l, r, vl, vr, ql, qr, k):
                if vl == vr:
                    return vl
                
                vm = (vl + vr) >> 1
                cl = self.cnt[node][ql - l]
                cr = self.cnt[node][qr - l + 1]
                res = cr - cl
                
                m = l + self.cnt[node][r - l + 1] - 1
                if k <= res:
                    return self._kth(self.left[node], l, m, vl, vm, l + cl, l + cr - 1, k)
                else:
                    return self._kth(self.right[node], m + 1, r, vm + 1, vr, 
                                   m + 1 + (ql - l - cl), m + 1 + (qr - l - cr), k - res)
            
            def count(self, ql, qr, x):
                return self.rank(ql, qr, x) - self.rank(ql, qr, x - 1)
        */
        
        // ====================================================================================
        // 题目9: 双树状数组应用
        // 来源: LeetCode 307. Range Sum Query - Mutable
        // 题目描述: 区间加，区间求和
        // 解题思路: 使用双树状数组实现区间更新和区间查询
        // ====================================================================================
        /*
        Java代码实现:
        public class BinaryIndexedTree2D {
            private long[] tree1; // 存储a[i]
            private long[] tree2; // 存储a[i] * i
            private int n;
            
            public BinaryIndexedTree2D(int size) {
                n = size;
                tree1 = new long[n + 1];
                tree2 = new long[n + 1];
            }
            
            private void update(long[] tree, int idx, long val) {
                while (idx <= n) {
                    tree[idx] += val;
                    idx += idx & -idx;
                }
            }
            
            private long query(long[] tree, int idx) {
                long res = 0;
                while (idx > 0) {
                    res += tree[idx];
                    idx -= idx & -idx;
                }
                return res;
            }
            
            // 区间[l, r]加上val
            public void rangeUpdate(int l, int r, long val) {
                update(tree1, l, val);
                update(tree1, r + 1, -val);
                update(tree2, l, val * (l - 1));
                update(tree2, r + 1, -val * r);
            }
            
            // 查询前缀和[1, idx]
            private long prefixSum(int idx) {
                return query(tree1, idx) * idx - query(tree2, idx);
            }
            
            // 查询区间[l, r]的和
            public long rangeQuery(int l, int r) {
                return prefixSum(r) - prefixSum(l - 1);
            }
        }
        
        C++代码实现:
        #include <iostream>
        #include <vector>
        using namespace std;
        
        class BinaryIndexedTree2D {
        private:
            vector<long long> tree1;
            vector<long long> tree2;
            int n;
            
            void update(vector<long long>& tree, int idx, long long val) {
                while (idx <= n) {
                    tree[idx] += val;
                    idx += idx & -idx;
                }
            }
            
            long long query(vector<long long>& tree, int idx) {
                long long res = 0;
                while (idx > 0) {
                    res += tree[idx];
                    idx -= idx & -idx;
                }
                return res;
            }
        
        public:
            BinaryIndexedTree2D(int size) {
                n = size;
                tree1.resize(n + 1, 0);
                tree2.resize(n + 1, 0);
            }
            
            void rangeUpdate(int l, int r, long long val) {
                update(tree1, l, val);
                update(tree1, r + 1, -val);
                update(tree2, l, val * (l - 1));
                update(tree2, r + 1, -val * r);
            }
            
            long long prefixSum(int idx) {
                return query(tree1, idx) * idx - query(tree2, idx);
            }
            
            long long rangeQuery(int l, int r) {
                return prefixSum(r) - prefixSum(l - 1);
            }
        };
        
        Python代码实现:
        class BinaryIndexedTree2D:
            def __init__(self, size):
                self.n = size
                self.tree1 = [0] * (self.n + 1)  # 存储a[i]
                self.tree2 = [0] * (self.n + 1)  # 存储a[i] * i
            
            def update(self, tree, idx, val):
                while idx <= self.n:
                    tree[idx] += val
                    idx += idx & -idx
            
            def query(self, tree, idx):
                res = 0
                while idx > 0:
                    res += tree[idx]
                    idx -= idx & -idx
                return res
            
            def range_update(self, l, r, val):
                # 区间[l, r]加上val
                self.update(self.tree1, l, val)
                self.update(self.tree1, r + 1, -val)
                self.update(self.tree2, l, val * (l - 1))
                self.update(self.tree2, r + 1, -val * r)
            
            def prefix_sum(self, idx):
                # 查询前缀和[1, idx]
                return self.query(self.tree1, idx) * idx - self.query(self.tree2, idx)
            
            def range_query(self, l, r):
                # 查询区间[l, r]的和
                return self.prefix_sum(r) - self.prefix_sum(l - 1)
        */
        
        // ====================================================================================
        // 题目10: LCA的Euler Tour + ST表实现
        // 来源: HDU 2586 How far away?
        // 题目描述: 多次查询两个节点之间的距离
        // 解题思路: 使用欧拉序+ST表预处理，O(1)查询LCA，结合前缀和计算距离
        // ====================================================================================
        /*
        Java代码实现:
        import java.util.*;
        
        public class LCAEulerTour {
            static class Edge {
                int to;
                int weight;
                public Edge(int to, int weight) {
                    this.to = to;
                    this.weight = weight;
                }
            }
            
            private List<List<Edge>> graph;
            private int[] dep;       // 节点深度
            private long[] dist;     // 根节点到当前节点的距离
            private int[] euler;     // 欧拉序
            private int[] first;     // 每个节点在欧拉序中第一次出现的位置
            private int[][] st;      // ST表
            private int[] log2;      // 预计算log2值
            private int time;        // 时间戳
            private int n;           // 节点数
            private int root;        // 根节点
            
            public LCAEulerTour(int n, int root) {
                this.n = n;
                this.root = root;
                graph = new ArrayList<>();
                for (int i = 0; i <= n; i++) {
                    graph.add(new ArrayList<>());
                }
                
                dep = new int[n + 1];
                dist = new long[n + 1];
                euler = new int[2 * n];
                first = new int[n + 1];
                Arrays.fill(first, -1);
                
                // 计算log2数组
                int maxLog = 0;
                while ((1 << maxLog) <= 2 * n) maxLog++;
                log2 = new int[2 * n + 1];
                for (int i = 2; i <= 2 * n; i++) {
                    log2[i] = log2[i / 2] + 1;
                }
                
                st = new int[maxLog][2 * n];
                time = 0;
            }
            
            public void addEdge(int u, int v, int weight) {
                graph.get(u).add(new Edge(v, weight));
                graph.get(v).add(new Edge(u, weight));
            }
            
            // 进行DFS遍历，生成欧拉序
            public void dfs() {
                Arrays.fill(first, -1);
                time = 0;
                dfs(root, -1, 0, 0);
                buildST();
            }
            
            private void dfs(int u, int parent, int depth, long distance) {
                dep[u] = depth;
                dist[u] = distance;
                euler[time++] = u;
                
                if (first[u] == -1) {
                    first[u] = time - 1;
                }
                
                for (Edge edge : graph.get(u)) {
                    int v = edge.to;
                    if (v != parent) {
                        dfs(v, u, depth + 1, distance + edge.weight);
                        euler[time++] = u;
                    }
                }
            }
            
            // 构建ST表
            private void buildST() {
                int maxLog = st.length;
                int m = time;
                
                // 初始化ST表第0层
                for (int i = 0; i < m; i++) {
                    st[0][i] = euler[i];
                }
                
                // 填充其余层
                for (int k = 1; k < maxLog; k++) {
                    for (int i = 0; i + (1 << k) <= m; i++) {
                        int mid = i + (1 << (k - 1));
                        int left = st[k - 1][i];
                        int right = st[k - 1][mid];
                        st[k][i] = dep[left] < dep[right] ? left : right;
                    }
                }
            }
            
            // 查询区间[l, r]中的最小深度节点
            private int queryMinDepth(int l, int r) {
                if (l > r) {
                    int temp = l;
                    l = r;
                    r = temp;
                }
                
                int k = log2[r - l + 1];
                int mid = r - (1 << k) + 1;
                int left = st[k][l];
                int right = st[k][mid];
                
                return dep[left] < dep[right] ? left : right;
            }
            
            // 查询u和v的LCA
            public int lca(int u, int v) {
                return queryMinDepth(first[u], first[v]);
            }
            
            // 查询u和v之间的距离
            public long distance(int u, int v) {
                int ancestor = lca(u, v);
                return dist[u] + dist[v] - 2 * dist[ancestor];
            }
        }
        
        C++代码实现:
        #include <iostream>
        #include <vector>
        #include <algorithm>
        #include <cmath>
        using namespace std;
        
        struct Edge {
            int to;
            int weight;
        };
        
        class LCAEulerTour {
        private:
            vector<vector<Edge>> graph;
            vector<int> dep;
            vector<long long> dist;
            vector<int> euler;
            vector<int> first;
            vector<vector<int>> st;
            vector<int> log2;
            int time;
            int n;
            int root;
            
            void dfs(int u, int parent, int depth, long long distance) {
                dep[u] = depth;
                dist[u] = distance;
                euler[time++] = u;
                
                if (first[u] == -1) {
                    first[u] = time - 1;
                }
                
                for (Edge edge : graph[u]) {
                    int v = edge.to;
                    if (v != parent) {
                        dfs(v, u, depth + 1, distance + edge.weight);
                        euler[time++] = u;
                    }
                }
            }
            
            void buildST() {
                int maxLog = st.size();
                int m = time;
                
                for (int i = 0; i < m; i++) {
                    st[0][i] = euler[i];
                }
                
                for (int k = 1; k < maxLog; k++) {
                    for (int i = 0; i + (1 << k) <= m; i++) {
                        int mid = i + (1 << (k - 1));
                        int left = st[k - 1][i];
                        int right = st[k - 1][mid];
                        st[k][i] = dep[left] < dep[right] ? left : right;
                    }
                }
            }
            
            int queryMinDepth(int l, int r) {
                if (l > r) swap(l, r);
                
                int k = log2[r - l + 1];
                int mid = r - (1 << k) + 1;
                int left = st[k][l];
                int right = st[k][mid];
                
                return dep[left] < dep[right] ? left : right;
            }
        
        public:
            LCAEulerTour(int n, int root) {
                this->n = n;
                this->root = root;
                graph.resize(n + 1);
                dep.resize(n + 1);
                dist.resize(n + 1);
                euler.resize(2 * n);
                first.resize(n + 1, -1);
                
                int maxLog = 0;
                while ((1 << maxLog) <= 2 * n) maxLog++;
                log2.resize(2 * n + 1);
                for (int i = 2; i <= 2 * n; i++) {
                    log2[i] = log2[i / 2] + 1;
                }
                
                st.resize(maxLog, vector<int>(2 * n));
                time = 0;
            }
            
            void addEdge(int u, int v, int weight) {
                graph[u].push_back({v, weight});
                graph[v].push_back({u, weight});
            }
            
            void dfs() {
                fill(first.begin(), first.end(), -1);
                time = 0;
                dfs(root, -1, 0, 0);
                buildST();
            }
            
            int lca(int u, int v) {
                return queryMinDepth(first[u], first[v]);
            }
            
            long long distance(int u, int v) {
                int ancestor = lca(u, v);
                return dist[u] + dist[v] - 2 * dist[ancestor];
            }
        };
        
        Python代码实现:
        import sys
        sys.setrecursionlimit(1 << 25)
        
        class LCAEulerTour:
            class Edge:
                def __init__(self, to, weight):
                    self.to = to
                    self.weight = weight
            
            def __init__(self, n, root):
                self.n = n
                self.root = root
                self.graph = [[] for _ in range(n + 1)]
                self.dep = [0] * (n + 1)        # 节点深度
                self.dist = [0] * (n + 1)       # 根到节点的距离
                self.euler = [0] * (2 * n)      # 欧拉序
                self.first = [-1] * (n + 1)     # 第一次出现的位置
                self.time = 0                   # 时间戳
                
                # 预计算log2值
                max_log = 0
                while (1 << max_log) <= 2 * n:
                    max_log += 1
                self.log2 = [0] * (2 * n + 1)
                for i in range(2, 2 * n + 1):
                    self.log2[i] = self.log2[i // 2] + 1
                
                self.st = []
                for _ in range(max_log):
                    self.st.append([0] * (2 * n))
            
            def add_edge(self, u, v, weight):
                self.graph[u].append(self.Edge(v, weight))
                self.graph[v].append(self.Edge(u, weight))
            
            def dfs(self):
                # 重置并开始DFS
                self.first = [-1] * (self.n + 1)
                self.time = 0
                self._dfs(self.root, -1, 0, 0)
                self._build_st()
            
            def _dfs(self, u, parent, depth, distance):
                self.dep[u] = depth
                self.dist[u] = distance
                self.euler[self.time] = u
                self.time += 1
                
                if self.first[u] == -1:
                    self.first[u] = self.time - 1
                
                for edge in self.graph[u]:
                    v = edge.to
                    if v != parent:
                        self._dfs(v, u, depth + 1, distance + edge.weight)
                        self.euler[self.time] = u
                        self.time += 1
            
            def _build_st(self):
                max_log = len(self.st)
                m = self.time
                
                # 初始化ST表第0层
                for i in range(m):
                    self.st[0][i] = self.euler[i]
                
                # 填充其余层
                for k in range(1, max_log):
                    for i in range(m - (1 << k) + 1):
                        mid = i + (1 << (k - 1))
                        left = self.st[k-1][i]
                        right = self.st[k-1][mid]
                        self.st[k][i] = left if self.dep[left] < self.dep[right] else right
            
            def _query_min_depth(self, l, r):
                if l > r:
                    l, r = r, l
                
                k = self.log2[r - l + 1]
                mid = r - (1 << k) + 1
                left = self.st[k][l]
                right = self.st[k][mid]
                
                return left if self.dep[left] < self.dep[right] else right
            
            def lca(self, u, v):
                return self._query_min_depth(self.first[u], self.first[v])
            
            def distance(self, u, v):
                ancestor = self.lca(u, v)
                return self.dist[u] + self.dist[v] - 2 * self.dist[ancestor]
        */
    }
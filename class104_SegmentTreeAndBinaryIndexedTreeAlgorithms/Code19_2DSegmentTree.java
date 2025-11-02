package class131;

/**
 * LeetCode 304. Range Sum Query 2D - Immutable 的二维线段树实现
 * 题目链接: https://leetcode.com/problems/range-sum-query-2d-immutable/
 * 
 * 虽然这道题通常用前缀和解决，但使用二维线段树可以支持单点更新，更加通用
 * 二维线段树用于高效处理二维区域查询，如求和、最大值、最小值等
 * 
 * 解题思路:
 * 使用线段树嵌套的方式实现二维线段树
 * 1. 外层线段树管理行范围
 * 2. 内层线段树管理列范围
 * 3. 每个线段树节点保存对应区域的和
 * 
 * 时间复杂度分析:
 * - 构建树: O(m*n)
 * - 单点更新: O(logm * logn)
 * - 区域查询: O(logm * logn)
 * 空间复杂度: O(m*n)
 */
public class Code19_2DSegmentTree {
    
    // 二维线段树节点定义
    static class Node {
        int sum;       // 当前区域的和
        int row1, row2; // 行范围
        int col1, col2; // 列范围
        Node topLeft, topRight, bottomLeft, bottomRight; // 四个子区域
        
        public Node(int row1, int row2, int col1, int col2) {
            this.sum = 0;
            this.row1 = row1;
            this.row2 = row2;
            this.col1 = col1;
            this.col2 = col2;
            this.topLeft = null;
            this.topRight = null;
            this.bottomLeft = null;
            this.bottomRight = null;
        }
        
        // 判断是否是叶子节点
        public boolean isLeaf() {
            return row1 == row2 && col1 == col2;
        }
    }
    
    private Node root; // 根节点
    private int[][] matrix; // 原始矩阵
    private int rows, cols; // 矩阵尺寸
    
    /**
     * 初始化二维线段树
     * @param matrix 输入矩阵
     */
    public Code19_2DSegmentTree(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            this.rows = 0;
            this.cols = 0;
            this.root = null;
            this.matrix = new int[0][0];
            return;
        }
        
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = new int[rows][cols];
        
        // 复制输入矩阵
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
        
        // 构建二维线段树
        this.root = buildTree(0, rows - 1, 0, cols - 1);
    }
    
    /**
     * 构建二维线段树
     * @param row1 起始行
     * @param row2 结束行
     * @param col1 起始列
     * @param col2 结束列
     * @return 构建好的线段树节点
     */
    private Node buildTree(int row1, int row2, int col1, int col2) {
        Node node = new Node(row1, row2, col1, col2);
        
        // 叶子节点
        if (row1 == row2 && col1 == col2) {
            node.sum = matrix[row1][col1];
            return node;
        }
        
        int midRow = row1 + (row2 - row1) / 2;
        int midCol = col1 + (col2 - col1) / 2;
        
        // 递归构建四个子区域
        if (row1 == row2) {
            // 只有一行
            node.topLeft = buildTree(row1, row2, col1, midCol);
            node.topRight = buildTree(row1, row2, midCol + 1, col2);
            node.sum = node.topLeft.sum + node.topRight.sum;
        } else if (col1 == col2) {
            // 只有一列
            node.topLeft = buildTree(row1, midRow, col1, col2);
            node.bottomLeft = buildTree(midRow + 1, row2, col1, col2);
            node.sum = node.topLeft.sum + node.bottomLeft.sum;
        } else {
            // 一般情况，分为四个子区域
            node.topLeft = buildTree(row1, midRow, col1, midCol);
            node.topRight = buildTree(row1, midRow, midCol + 1, col2);
            node.bottomLeft = buildTree(midRow + 1, row2, col1, midCol);
            node.bottomRight = buildTree(midRow + 1, row2, midCol + 1, col2);
            node.sum = node.topLeft.sum + node.topRight.sum + node.bottomLeft.sum + node.bottomRight.sum;
        }
        
        return node;
    }
    
    /**
     * 更新矩阵中某一点的值
     * @param row 行索引
     * @param col 列索引
     * @param val 新值
     */
    public void update(int row, int col, int val) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        
        int delta = val - matrix[row][col];
        matrix[row][col] = val;
        updateTree(root, row, col, delta);
    }
    
    /**
     * 递归更新线段树
     * @param node 当前节点
     * @param row 要更新的行索引
     * @param col 要更新的列索引
     * @param delta 变化量
     */
    private void updateTree(Node node, int row, int col, int delta) {
        // 如果当前节点包含要更新的点
        if (row >= node.row1 && row <= node.row2 && col >= node.col1 && col <= node.col2) {
            node.sum += delta;
            
            // 如果不是叶子节点，继续递归更新子节点
            if (!node.isLeaf()) {
                if (node.topLeft != null && row <= node.topLeft.row2 && col <= node.topLeft.col2) {
                    updateTree(node.topLeft, row, col, delta);
                } else if (node.topRight != null && row <= node.topRight.row2 && col >= node.topRight.col1) {
                    updateTree(node.topRight, row, col, delta);
                } else if (node.bottomLeft != null && row >= node.bottomLeft.row1 && col <= node.bottomLeft.col2) {
                    updateTree(node.bottomLeft, row, col, delta);
                } else if (node.bottomRight != null && row >= node.bottomRight.row1 && col >= node.bottomRight.col1) {
                    updateTree(node.bottomRight, row, col, delta);
                }
            }
        }
    }
    
    /**
     * 查询区域和
     * @param row1 起始行
     * @param col1 起始列
     * @param row2 结束行
     * @param col2 结束列
     * @return 区域和
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (row1 < 0 || row1 >= rows || row2 < 0 || row2 >= rows || 
            col1 < 0 || col1 >= cols || col2 < 0 || col2 >= cols ||
            row1 > row2 || col1 > col2) {
            throw new IllegalArgumentException("Invalid query range");
        }
        
        return queryTree(root, row1, col1, row2, col2);
    }
    
    /**
     * 递归查询区域和
     * @param node 当前节点
     * @param row1 查询起始行
     * @param col1 查询起始列
     * @param row2 查询结束行
     * @param col2 查询结束列
     * @return 查询结果
     */
    private int queryTree(Node node, int row1, int col1, int row2, int col2) {
        // 查询区域完全包含当前节点
        if (row1 <= node.row1 && row2 >= node.row2 && col1 <= node.col1 && col2 >= node.col2) {
            return node.sum;
        }
        
        // 查询区域与当前节点无交集
        if (row2 < node.row1 || row1 > node.row2 || col2 < node.col1 || col1 > node.col2) {
            return 0;
        }
        
        // 查询区域与当前节点有部分交集，递归查询子节点
        int sum = 0;
        if (node.topLeft != null) {
            sum += queryTree(node.topLeft, row1, col1, row2, col2);
        }
        if (node.topRight != null) {
            sum += queryTree(node.topRight, row1, col1, row2, col2);
        }
        if (node.bottomLeft != null) {
            sum += queryTree(node.bottomLeft, row1, col1, row2, col2);
        }
        if (node.bottomRight != null) {
            sum += queryTree(node.bottomRight, row1, col1, row2, col2);
        }
        
        return sum;
    }
    
    public static void main(String[] args) {
        // 测试样例
        int[][] matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        Code19_2DSegmentTree segTree = new Code19_2DSegmentTree(matrix);
        
        // 测试查询
        System.out.println(segTree.sumRegion(2, 1, 4, 3)); // 输出：8
        System.out.println(segTree.sumRegion(1, 1, 2, 2)); // 输出：11
        System.out.println(segTree.sumRegion(1, 2, 2, 4)); // 输出：12
        
        // 测试更新
        segTree.update(1, 1, 10);
        System.out.println(segTree.sumRegion(1, 1, 2, 2)); // 输出：15
    }
}

/*
二维线段树的优化思路：

1. **空间优化**：
   - 对于大规模数据，可以采用数组形式存储线段树，减少对象创建的开销
   - 使用动态开点技术，只在需要时创建节点

2. **时间优化**：
   - 实现非递归版本的二维线段树，减少函数调用开销
   - 对于特定问题，可以采用分块处理等优化方法

3. **其他变种**：
   - 二维树状数组：空间更小，但只能处理求和等操作
   - 四分树：一种更扁平的二维线段树实现
   - 线段树套线段树：另一种实现二维线段树的方式，外层线段树每个节点对应一棵一维线段树

二维线段树适用于需要频繁进行二维区域查询和单点更新的场景，例如图像处理、地理信息系统等。
*/
package class155;

import java.util.*;

/**
 * CodeForces 627E Orchestra（管弦乐队）
 * 
 * 题目链接: https://codeforces.com/problemset/problem/627/E
 * 
 * 题目描述：给定一个N×M的矩阵，其中每个元素是0或1。我们要找出所有大小为a×b的子矩阵，
 * 使得这些子矩阵中至少包含k个1。请输出满足条件的子矩阵数量。
 * 
 * 解题思路：使用滑动窗口和左偏树来维护每列的滑动窗口中的最大值
 * 
 * 时间复杂度：O(N*M*a*log b)，在实际应用中表现良好
 * 空间复杂度：O(N*M)
 */
public class Orchestra_Java {
    
    // 左偏树节点类
    static class LeftistTreeNode {
        int value; // 值
        int row;   // 行号
        int col;   // 列号
        int dist;  // 距离
        LeftistTreeNode left;
        LeftistTreeNode right;
        
        public LeftistTreeNode(int value, int row, int col) {
            this.value = value;
            this.row = row;
            this.col = col;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    // 合并两个左偏树
    private static LeftistTreeNode merge(LeftistTreeNode a, LeftistTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        // 维护大根堆性质
        if (a.value < b.value) {
            LeftistTreeNode temp = a;
            a = b;
            b = temp;
        }
        
        // 递归合并右子树
        a.right = merge(a.right, b);
        
        // 维护左偏性质
        if (a.left == null || (a.right != null && a.left.dist < a.right.dist)) {
            LeftistTreeNode temp = a.left;
            a.left = a.right;
            a.right = temp;
        }
        
        // 更新距离
        a.dist = (a.right == null) ? 0 : a.right.dist + 1;
        return a;
    }
    
    // 获取堆顶元素（最大值）
    private static int getMax(LeftistTreeNode root) {
        if (root == null) return 0;
        return root.value;
    }
    
    // 移除特定位置的元素
    private static LeftistTreeNode remove(LeftistTreeNode root, int targetRow, int targetCol) {
        if (root == null) return null;
        
        if (root.row == targetRow && root.col == targetCol) {
            return merge(root.left, root.right);
        }
        
        // 递归删除
        root.left = remove(root.left, targetRow, targetCol);
        root.right = remove(root.right, targetRow, targetCol);
        
        // 重新维护左偏性质
        if (root.left == null || (root.right != null && root.left.dist < root.right.dist)) {
            LeftistTreeNode temp = root.left;
            root.left = root.right;
            root.right = temp;
        }
        
        root.dist = (root.right == null) ? 0 : root.right.dist + 1;
        return root;
    }
    
    // 主函数，计算满足条件的子矩阵数量
    public static long countValidSubmatrices(int[][] matrix, int a, int b, int k) {
        int n = matrix.length;
        if (n == 0) return 0;
        int m = matrix[0].length;
        
        // 预处理每个位置向上连续的1的数量
        int[][] upCounts = new int[n][m];
        for (int j = 0; j < m; j++) {
            upCounts[0][j] = matrix[0][j];
            for (int i = 1; i < n; i++) {
                upCounts[i][j] = matrix[i][j] == 0 ? 0 : upCounts[i-1][j] + 1;
            }
        }
        
        long result = 0;
        
        // 遍历所有可能的起始行
        for (int topRow = 0; topRow <= n - a; topRow++) {
            int bottomRow = topRow + a - 1;
            
            // 对于每一列，计算在[a×b]窗口内的有效高度
            int[][] windowCounts = new int[n][m];
            for (int j = 0; j < m; j++) {
                // windowCounts[i][j]表示从i行向上看，在a行范围内的最小upCounts
                windowCounts[bottomRow][j] = Math.min(upCounts[bottomRow][j], a);
            }
            
            // 使用滑动窗口和左偏树维护每列的滑动窗口最大值
            for (int leftCol = 0; leftCol <= m - b; leftCol++) {
                int rightCol = leftCol + b - 1;
                
                // 为每个行创建一个左偏树来维护该行的b列窗口中的最大值
                LeftistTreeNode[] rowHeaps = new LeftistTreeNode[n];
                
                // 初始化每个行的左偏树
                for (int i = 0; i < n; i++) {
                    for (int j = leftCol; j <= rightCol; j++) {
                        rowHeaps[i] = merge(rowHeaps[i], new LeftistTreeNode(windowCounts[i][j], i, j));
                    }
                }
                
                // 统计当前窗口内的有效1的数量
                int countOnes = 0;
                for (int i = topRow; i <= bottomRow; i++) {
                    countOnes += getMax(rowHeaps[i]);
                }
                
                if (countOnes >= k) {
                    result++;
                }
            }
        }
        
        return result;
    }
    
    // 主测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int k = scanner.nextInt();
        
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
        
        long result = countValidSubmatrices(matrix, a, b, k);
        System.out.println(result);
        
        scanner.close();
    }
}
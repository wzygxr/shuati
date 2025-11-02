/**
 * 二维线段树实现
 * 
 * 题目描述:
 * 支持二维矩阵的区间查询和区间更新操作
 * 1. 区间求和：查询子矩阵的和
 * 2. 区间更新：将子矩阵内的所有元素加上一个值
 * 
 * 解题思路:
 * 使用二维线段树，外层线段树维护行区间，内层线段树维护列区间
 * 每个外层节点包含一个内层线段树
 * 
 * 时间复杂度分析:
 * - 建树: O(n * m)
 * - 区间查询: O(log n * log m)
 * - 区间更新: O(log n * log m)
 * 
 * 空间复杂度: O(n * m)
 */

public class TwoDimensionalSegmentTree {
    private int n, m;
    private int[][] matrix;
    private int[][] tree;
    private int[][] lazy;
    
    public TwoDimensionalSegmentTree(int[][] matrix) {
        this.n = matrix.length;
        this.m = matrix[0].length;
        this.matrix = matrix;
        this.tree = new int[4 * n][4 * m];
        this.lazy = new int[4 * n][4 * m];
        build(1, 0, n - 1, 1, 0, m - 1);
    }
    
    private void build(int nodeX, int startX, int endX, int nodeY, int startY, int endY) {
        if (startX == endX && startY == endY) {
            tree[nodeX][nodeY] = matrix[startX][startY];
        } else if (startX == endX) {
            // 只在Y方向递归
            int midY = (startY + endY) / 2;
            build(nodeX, startX, endX, 2 * nodeY, startY, midY);
            build(nodeX, startX, endX, 2 * nodeY + 1, midY + 1, endY);
            tree[nodeX][nodeY] = tree[nodeX][2 * nodeY] + tree[nodeX][2 * nodeY + 1];
        } else {
            // 在X方向递归
            int midX = (startX + endX) / 2;
            build(2 * nodeX, startX, midX, nodeY, startY, endY);
            build(2 * nodeX + 1, midX + 1, endX, nodeY, startY, endY);
            
            // 合并X方向的子节点
            if (startY == endY) {
                tree[nodeX][nodeY] = tree[2 * nodeX][nodeY] + tree[2 * nodeX + 1][nodeY];
            } else {
                // 需要递归构建Y方向
                int midY = (startY + endY) / 2;
                build(nodeX, startX, endX, 2 * nodeY, startY, midY);
                build(nodeX, startX, endX, 2 * nodeY + 1, midY + 1, endY);
                tree[nodeX][nodeY] = tree[nodeX][2 * nodeY] + tree[nodeX][2 * nodeY + 1];
            }
        }
    }
    
    public void update(int x1, int y1, int x2, int y2, int value) {
        update(1, 0, n - 1, 1, 0, m - 1, x1, y1, x2, y2, value);
    }
    
    private void update(int nodeX, int startX, int endX, int nodeY, int startY, int endY, 
                       int x1, int y1, int x2, int y2, int value) {
        if (x1 > x2 || y1 > y2) return;
        
        // 处理懒惰标记
        pushDown(nodeX, startX, endX, nodeY, startY, endY);
        
        if (x1 <= startX && endX <= x2 && y1 <= startY && endY <= y2) {
            // 当前区间完全包含在更新区间内
            lazy[nodeX][nodeY] += value;
            tree[nodeX][nodeY] += value * (endX - startX + 1) * (endY - startY + 1);
            return;
        }
        
        int midX = (startX + endX) / 2;
        int midY = (startY + endY) / 2;
        
        // 递归更新四个子区间
        if (x1 <= midX && y1 <= midY) {
            update(2 * nodeX, startX, midX, 2 * nodeY, startY, midY, 
                   x1, y1, Math.min(x2, midX), Math.min(y2, midY), value);
        }
        if (x1 <= midX && y2 > midY) {
            update(2 * nodeX, startX, midX, 2 * nodeY + 1, midY + 1, endY, 
                   x1, Math.max(y1, midY + 1), Math.min(x2, midX), y2, value);
        }
        if (x2 > midX && y1 <= midY) {
            update(2 * nodeX + 1, midX + 1, endX, 2 * nodeY, startY, midY, 
                   Math.max(x1, midX + 1), y1, x2, Math.min(y2, midY), value);
        }
        if (x2 > midX && y2 > midY) {
            update(2 * nodeX + 1, midX + 1, endX, 2 * nodeY + 1, midY + 1, endY, 
                   Math.max(x1, midX + 1), Math.max(y1, midY + 1), x2, y2, value);
        }
        
        // 更新当前节点
        tree[nodeX][nodeY] = tree[2 * nodeX][nodeY] + tree[2 * nodeX + 1][nodeY];
    }
    
    private void pushDown(int nodeX, int startX, int endX, int nodeY, int startY, int endY) {
        if (lazy[nodeX][nodeY] != 0) {
            int lazyVal = lazy[nodeX][nodeY];
            lazy[nodeX][nodeY] = 0;
            
            if (startX != endX) {
                lazy[2 * nodeX][nodeY] += lazyVal;
                lazy[2 * nodeX + 1][nodeY] += lazyVal;
            }
            if (startY != endY) {
                lazy[nodeX][2 * nodeY] += lazyVal;
                lazy[nodeX][2 * nodeY + 1] += lazyVal;
            }
            
            tree[nodeX][nodeY] += lazyVal * (endX - startX + 1) * (endY - startY + 1);
        }
    }
    
    public int query(int x1, int y1, int x2, int y2) {
        return query(1, 0, n - 1, 1, 0, m - 1, x1, y1, x2, y2);
    }
    
    private int query(int nodeX, int startX, int endX, int nodeY, int startY, int endY, 
                     int x1, int y1, int x2, int y2) {
        if (x1 > x2 || y1 > y2) return 0;
        
        // 处理懒惰标记
        pushDown(nodeX, startX, endX, nodeY, startY, endY);
        
        if (x1 <= startX && endX <= x2 && y1 <= startY && endY <= y2) {
            return tree[nodeX][nodeY];
        }
        
        int midX = (startX + endX) / 2;
        int midY = (startY + endY) / 2;
        int result = 0;
        
        // 递归查询四个子区间
        if (x1 <= midX && y1 <= midY) {
            result += query(2 * nodeX, startX, midX, 2 * nodeY, startY, midY, 
                           x1, y1, Math.min(x2, midX), Math.min(y2, midY));
        }
        if (x1 <= midX && y2 > midY) {
            result += query(2 * nodeX, startX, midX, 2 * nodeY + 1, midY + 1, endY, 
                           x1, Math.max(y1, midY + 1), Math.min(x2, midX), y2);
        }
        if (x2 > midX && y1 <= midY) {
            result += query(2 * nodeX + 1, midX + 1, endX, 2 * nodeY, startY, midY, 
                           Math.max(x1, midX + 1), y1, x2, Math.min(y2, midY));
        }
        if (x2 > midX && y2 > midY) {
            result += query(2 * nodeX + 1, midX + 1, endX, 2 * nodeY + 1, midY + 1, endY, 
                           Math.max(x1, midX + 1), Math.max(y1, midY + 1), x2, y2);
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        // 测试用例
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        TwoDimensionalSegmentTree segTree = new TwoDimensionalSegmentTree(matrix);
        
        // 测试查询
        System.out.println("查询整个矩阵: " + segTree.query(0, 0, 2, 2)); // 期望: 45
        System.out.println("查询子矩阵 [0,0]到[1,1]: " + segTree.query(0, 0, 1, 1)); // 期望: 12
        
        // 测试更新
        segTree.update(0, 0, 1, 1, 2);
        System.out.println("更新后查询 [0,0]到[1,1]: " + segTree.query(0, 0, 1, 1)); // 期望: 12 + 2*4 = 20
        System.out.println("更新后查询整个矩阵: " + segTree.query(0, 0, 2, 2)); // 期望: 45 + 2*4 = 53
    }
}
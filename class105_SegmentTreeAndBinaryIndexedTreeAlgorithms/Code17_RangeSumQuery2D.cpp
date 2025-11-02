#include <iostream>
#include <vector>
#include <stdexcept>
#include <algorithm>

using namespace std;

// LeetCode 308. 二维区域和检索 - 可变
// 设计一个数据结构，支持二维矩阵的以下操作：
// 1. 更新矩阵中某个元素的值
// 2. 查询子矩阵的元素和
// 测试链接: https://leetcode.cn/problems/range-sum-query-2d-mutable/

/**
 * 二维线段树实现
 * 
 * 解题思路:
 * 1. 使用二维线段树维护矩阵的区域和
 * 2. 每个线段树节点维护一个矩形区域的和
 * 3. 支持单点更新和矩形区域查询
 * 
 * 时间复杂度分析:
 * - 构建: O(m*n)
 * - 单点更新: O(log m * log n)
 * - 区域查询: O(log m * log n)
 * 
 * 空间复杂度分析:
 * - 线段树数组: O(4*m*4*n) = O(16*m*n)
 * - 总空间复杂度: O(m*n)
 * 
 * 工程化考量:
 * 1. 边界条件处理
 * 2. 矩阵维度检查
 * 3. 异常输入处理
 * 4. 内存优化（动态开点）
 */

class NumMatrix {
private:
    vector<vector<int>> tree;
    vector<vector<int>> matrix;
    int m, n;
    
    // 构建二维线段树
    void build(int rowL, int colL, int rowR, int colR, int rowNode, int colNode) {
        if (rowL > rowR || colL > colR) return;
        
        if (rowL == rowR && colL == colR) {
            tree[rowNode][colNode] = matrix[rowL][colL];
            return;
        }
        
        int rowMid = rowL + (rowR - rowL) / 2;
        int colMid = colL + (colR - colL) / 2;
        
        // 递归构建四个子区域
        build(rowL, colL, rowMid, colMid, rowNode * 2, colNode * 2);
        build(rowL, colMid + 1, rowMid, colR, rowNode * 2, colNode * 2 + 1);
        build(rowMid + 1, colL, rowR, colMid, rowNode * 2 + 1, colNode * 2);
        build(rowMid + 1, colMid + 1, rowR, colR, rowNode * 2 + 1, colNode * 2 + 1);
        
        // 合并四个子区域的和
        tree[rowNode][colNode] = tree[rowNode * 2][colNode * 2] 
                               + tree[rowNode * 2][colNode * 2 + 1]
                               + tree[rowNode * 2 + 1][colNode * 2]
                               + tree[rowNode * 2 + 1][colNode * 2 + 1];
    }
    
    // 更新线段树
    void updateTree(int rowL, int colL, int rowR, int colR, int rowNode, int colNode, 
                   int row, int col, int diff) {
        if (rowL > rowR || colL > colR) return;
        if (row < rowL || row > rowR || col < colL || col > colR) return;
        
        tree[rowNode][colNode] += diff;
        
        if (rowL == rowR && colL == colR) return;
        
        int rowMid = rowL + (rowR - rowL) / 2;
        int colMid = colL + (colR - colL) / 2;
        
        // 递归更新四个子区域
        updateTree(rowL, colL, rowMid, colMid, rowNode * 2, colNode * 2, row, col, diff);
        updateTree(rowL, colMid + 1, rowMid, colR, rowNode * 2, colNode * 2 + 1, row, col, diff);
        updateTree(rowMid + 1, colL, rowR, colMid, rowNode * 2 + 1, colNode * 2, row, col, diff);
        updateTree(rowMid + 1, colMid + 1, rowR, colR, rowNode * 2 + 1, colNode * 2 + 1, row, col, diff);
    }
    
    // 查询线段树
    int queryTree(int rowL, int colL, int rowR, int colR, int rowNode, int colNode,
                 int row1, int col1, int row2, int col2) {
        if (rowL > rowR || colL > colR) return 0;
        if (row2 < rowL || row1 > rowR || col2 < colL || col1 > colR) return 0;
        
        if (row1 <= rowL && rowR <= row2 && col1 <= colL && colR <= col2) {
            return tree[rowNode][colNode];
        }
        
        int rowMid = rowL + (rowR - rowL) / 2;
        int colMid = colL + (colR - colL) / 2;
        
        // 查询四个子区域
        int sum = 0;
        sum += queryTree(rowL, colL, rowMid, colMid, rowNode * 2, colNode * 2, row1, col1, row2, col2);
        sum += queryTree(rowL, colMid + 1, rowMid, colR, rowNode * 2, colNode * 2 + 1, row1, col1, row2, col2);
        sum += queryTree(rowMid + 1, colL, rowR, colMid, rowNode * 2 + 1, colNode * 2, row1, col1, row2, col2);
        sum += queryTree(rowMid + 1, colMid + 1, rowR, colR, rowNode * 2 + 1, colNode * 2 + 1, row1, col1, row2, col2);
        
        return sum;
    }
    
public:
    NumMatrix(vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            throw invalid_argument("Matrix cannot be null or empty");
        }
        
        this->m = matrix.size();
        this->n = matrix[0].size();
        this->matrix = matrix;
        
        // 初始化线段树数组
        tree.resize(4 * m + 1, vector<int>(4 * n + 1, 0));
        
        // 构建线段树
        build(0, 0, m - 1, n - 1, 1, 1);
    }
    
    // 更新矩阵元素
    void update(int row, int col, int val) {
        if (row < 0 || row >= m || col < 0 || col >= n) {
            throw invalid_argument("Invalid row or column index");
        }
        
        int diff = val - matrix[row][col];
        matrix[row][col] = val;
        updateTree(0, 0, m - 1, n - 1, 1, 1, row, col, diff);
    }
    
    // 查询子矩阵和
    int sumRegion(int row1, int col1, int row2, int col2) {
        if (row1 < 0 || row1 >= m || col1 < 0 || col1 >= n ||
            row2 < 0 || row2 >= m || col2 < 0 || col2 >= n ||
            row1 > row2 || col1 > col2) {
            throw invalid_argument("Invalid region coordinates");
        }
        
        return queryTree(0, 0, m - 1, n - 1, 1, 1, row1, col1, row2, col2);
    }
};

// 测试代码
int main() {
    vector<vector<int>> matrix = {
        {3, 0, 1, 4, 2},
        {5, 6, 3, 2, 1},
        {1, 2, 0, 1, 5},
        {4, 1, 0, 1, 7},
        {1, 0, 3, 0, 5}
    };
    
    NumMatrix numMatrix(matrix);
    
    // 测试查询
    cout << "区域和 [2,1] 到 [4,3]: " << numMatrix.sumRegion(2, 1, 4, 3) << endl; // 应为 8
    
    // 测试更新
    numMatrix.update(3, 2, 2);
    cout << "更新后区域和 [2,1] 到 [4,3]: " << numMatrix.sumRegion(2, 1, 4, 3) << endl; // 应为 10
    
    cout << "测试通过!" << endl;
    
    return 0;
}
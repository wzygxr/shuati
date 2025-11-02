#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * LeetCode 308 - Range Sum Query 2D - Mutable
 * 题目：二维区域和检索 - 可变
 * 来源：LeetCode
 * 网址：https://leetcode.cn/problems/range-sum-query-2d-mutable/
 * 
 * 给定一个二维矩阵，支持以下操作：
 * 1. 更新矩阵中某个位置的值
 * 2. 查询子矩阵的和
 * 
 * 解题思路：
 * 使用二维线段树（四叉树）来处理二维区域查询和更新问题。
 * 每个节点代表一个矩形区域，维护该区域的和。
 * 
 * 时间复杂度：
 *   - 建树：O(m*n)
 *   - 更新：O(log m * log n)
 *   - 查询：O(log m * log n)
 * 空间复杂度：O(m*n)
 */

struct TreeNode {
    int row1, col1, row2, col2;  // 矩形区域的坐标
    int sum;                     // 区域和
    TreeNode* children[4];       // 四个子节点
    
    TreeNode(int r1, int c1, int r2, int c2) {
        this->row1 = r1;
        this->col1 = c1;
        this->row2 = r2;
        this->col2 = c2;
        this->sum = 0;
        for (int i = 0; i < 4; i++) {
            this->children[i] = nullptr;
        }
    }
    
    ~TreeNode() {
        for (int i = 0; i < 4; i++) {
            if (children[i]) {
                delete children[i];
            }
        }
    }
};

class RangeSumQuery2D {
private:
    TreeNode* root;
    vector<vector<int>> matrix;
    
public:
    RangeSumQuery2D(vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            this->root = nullptr;
            return;
        }
        
        this->matrix = matrix;
        this->root = buildTree(0, 0, matrix.size() - 1, matrix[0].size() - 1);
    }
    
private:
    TreeNode* buildTree(int row1, int col1, int row2, int col2) {
        if (row1 > row2 || col1 > col2) {
            return nullptr;
        }
        
        TreeNode* node = new TreeNode(row1, col1, row2, col2);
        
        // 叶子节点
        if (row1 == row2 && col1 == col2) {
            node->sum = matrix[row1][col1];
            return node;
        }
        
        int rowMid = (row1 + row2) / 2;
        int colMid = (col1 + col2) / 2;
        
        // 构建四个子节点
        node->children[0] = buildTree(row1, col1, rowMid, colMid);           // 左上
        node->children[1] = buildTree(row1, colMid + 1, rowMid, col2);       // 右上
        node->children[2] = buildTree(rowMid + 1, col1, row2, colMid);       // 左下
        node->children[3] = buildTree(rowMid + 1, colMid + 1, row2, col2);   // 右下
        
        // 计算当前节点的和
        for (int i = 0; i < 4; i++) {
            if (node->children[i]) {
                node->sum += node->children[i]->sum;
            }
        }
        
        return node;
    }
    
    void update(TreeNode* node, int row, int col, int delta) {
        if (!node) {
            return;
        }
        
        // 检查目标点是否在当前节点的区域内
        if (row < node->row1 || row > node->row2 || col < node->col1 || col > node->col2) {
            return;
        }
        
        node->sum += delta;
        
        // 如果是叶子节点，直接返回
        if (node->row1 == node->row2 && node->col1 == node->col2) {
            return;
        }
        
        // 递归更新子节点
        for (int i = 0; i < 4; i++) {
            if (node->children[i]) {
                update(node->children[i], row, col, delta);
            }
        }
    }
    
    int sumRegion(TreeNode* node, int row1, int col1, int row2, int col2) {
        if (!node) {
            return 0;
        }
        
        // 没有交集
        if (row1 > node->row2 || row2 < node->row1 || col1 > node->col2 || col2 < node->col1) {
            return 0;
        }
        
        // 完全包含
        if (row1 <= node->row1 && node->row2 <= row2 && col1 <= node->col1 && node->col2 <= col2) {
            return node->sum;
        }
        
        // 部分重叠，递归查询子节点
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            if (node->children[i]) {
                sum += sumRegion(node->children[i], row1, col1, row2, col2);
            }
        }
        
        return sum;
    }
    
    ~RangeSumQuery2D() {
        if (root) {
            delete root;
        }
    }
    
    void update(int row, int col, int val) {
        if (!root) {
            return;
        }
        
        int delta = val - matrix[row][col];
        matrix[row][col] = val;
        update(root, row, col, delta);
    }
    
    int sumRegion(int row1, int col1, int row2, int col2) {
        if (!root) {
            return 0;
        }
        return sumRegion(root, row1, col1, row2, col2);
    }
};

int main() {
    // 测试样例
    vector<vector<int>> matrix = {
        {3, 0, 1, 4, 2},
        {5, 6, 3, 2, 1},
        {1, 2, 0, 1, 5},
        {4, 1, 0, 1, 7},
        {1, 0, 3, 0, 5}
    };
    
    RangeSumQuery2D st(matrix);
    
    // 查询区域和
    cout << "区域[2,1,4,3]的和: " << st.sumRegion(2, 1, 4, 3) << endl; // 8
    
    // 更新矩阵
    st.update(3, 2, 2);
    
    // 查询更新后的区域和
    cout << "更新后区域[2,1,4,3]的和: " << st.sumRegion(2, 1, 4, 3) << endl; // 10
    
    // 查询其他区域
    cout << "区域[1,1,2,2]的和: " << st.sumRegion(1, 1, 2, 2) << endl; // 11
    cout << "区域[1,2,2,4]的和: " << st.sumRegion(1, 2, 2, 4) << endl; // 12
    
    return 0;
}
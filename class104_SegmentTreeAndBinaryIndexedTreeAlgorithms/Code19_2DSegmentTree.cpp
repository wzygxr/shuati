// class131/Code19_2DSegmentTree.cpp
// LeetCode 304. Range Sum Query 2D - Immutable 的二维线段树实现
// 题目链接: https://leetcode.com/problems/range-sum-query-2d-immutable/

#include <iostream>
#include <vector>
#include <stdexcept>
using namespace std;

/**
 * 二维线段树节点定义
 */
struct Node {
    int sum;       // 当前区域的和
    int row1, row2; // 行范围
    int col1, col2; // 列范围
    Node *topLeft, *topRight, *bottomLeft, *bottomRight; // 四个子区域
    
    Node(int r1, int r2, int c1, int c2) : 
        sum(0), row1(r1), row2(r2), col1(c1), col2(c2),
        topLeft(nullptr), topRight(nullptr), bottomLeft(nullptr), bottomRight(nullptr) {}
    
    // 判断是否是叶子节点
    bool isLeaf() const {
        return row1 == row2 && col1 == col2;
    }
    
    // 析构函数，释放子节点内存
    ~Node() {
        delete topLeft;
        delete topRight;
        delete bottomLeft;
        delete bottomRight;
    }
};

class Code19_2DSegmentTree {
private:
    Node* root;     // 根节点
    vector<vector<int>> matrix; // 原始矩阵
    int rows, cols; // 矩阵尺寸
    
    /**
     * 构建二维线段树
     * @param row1 起始行
     * @param row2 结束行
     * @param col1 起始列
     * @param col2 结束列
     * @return 构建好的线段树节点
     */
    Node* buildTree(int row1, int row2, int col1, int col2) {
        Node* node = new Node(row1, row2, col1, col2);
        
        // 叶子节点
        if (row1 == row2 && col1 == col2) {
            node->sum = matrix[row1][col1];
            return node;
        }
        
        int midRow = row1 + (row2 - row1) / 2;
        int midCol = col1 + (col2 - col1) / 2;
        
        // 递归构建四个子区域
        if (row1 == row2) {
            // 只有一行
            node->topLeft = buildTree(row1, row2, col1, midCol);
            node->topRight = buildTree(row1, row2, midCol + 1, col2);
            node->sum = node->topLeft->sum + node->topRight->sum;
        } else if (col1 == col2) {
            // 只有一列
            node->topLeft = buildTree(row1, midRow, col1, col2);
            node->bottomLeft = buildTree(midRow + 1, row2, col1, col2);
            node->sum = node->topLeft->sum + node->bottomLeft->sum;
        } else {
            // 一般情况，分为四个子区域
            node->topLeft = buildTree(row1, midRow, col1, midCol);
            node->topRight = buildTree(row1, midRow, midCol + 1, col2);
            node->bottomLeft = buildTree(midRow + 1, row2, col1, midCol);
            node->bottomRight = buildTree(midRow + 1, row2, midCol + 1, col2);
            node->sum = node->topLeft->sum + node->topRight->sum + 
                      node->bottomLeft->sum + node->bottomRight->sum;
        }
        
        return node;
    }
    
    /**
     * 递归更新线段树
     * @param node 当前节点
     * @param row 要更新的行索引
     * @param col 要更新的列索引
     * @param delta 变化量
     */
    void updateTree(Node* node, int row, int col, int delta) {
        // 如果当前节点包含要更新的点
        if (row >= node->row1 && row <= node->row2 && 
            col >= node->col1 && col <= node->col2) {
            node->sum += delta;
            
            // 如果不是叶子节点，继续递归更新子节点
            if (!node->isLeaf()) {
                if (node->topLeft && row <= node->topLeft->row2 && col <= node->topLeft->col2) {
                    updateTree(node->topLeft, row, col, delta);
                } else if (node->topRight && row <= node->topRight->row2 && col >= node->topRight->col1) {
                    updateTree(node->topRight, row, col, delta);
                } else if (node->bottomLeft && row >= node->bottomLeft->row1 && col <= node->bottomLeft->col2) {
                    updateTree(node->bottomLeft, row, col, delta);
                } else if (node->bottomRight && row >= node->bottomRight->row1 && col >= node->bottomRight->col1) {
                    updateTree(node->bottomRight, row, col, delta);
                }
            }
        }
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
    int queryTree(Node* node, int row1, int col1, int row2, int col2) const {
        // 查询区域完全包含当前节点
        if (row1 <= node->row1 && row2 >= node->row2 && 
            col1 <= node->col1 && col2 >= node->col2) {
            return node->sum;
        }
        
        // 查询区域与当前节点无交集
        if (row2 < node->row1 || row1 > node->row2 || 
            col2 < node->col1 || col1 > node->col2) {
            return 0;
        }
        
        // 查询区域与当前节点有部分交集，递归查询子节点
        int sum = 0;
        if (node->topLeft) {
            sum += queryTree(node->topLeft, row1, col1, row2, col2);
        }
        if (node->topRight) {
            sum += queryTree(node->topRight, row1, col1, row2, col2);
        }
        if (node->bottomLeft) {
            sum += queryTree(node->bottomLeft, row1, col1, row2, col2);
        }
        if (node->bottomRight) {
            sum += queryTree(node->bottomRight, row1, col1, row2, col2);
        }
        
        return sum;
    }
public:
    /**
     * 初始化二维线段树
     * @param matrix 输入矩阵
     */
    Code19_2DSegmentTree(const vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            this->rows = 0;
            this->cols = 0;
            this->root = nullptr;
            return;
        }
        
        this->rows = matrix.size();
        this->cols = matrix[0].size();
        this->matrix = matrix;
        
        // 构建二维线段树
        this->root = buildTree(0, rows - 1, 0, cols - 1);
    }
    
    /**
     * 析构函数，释放内存
     */
    ~Code19_2DSegmentTree() {
        delete root;
    }
    
    /**
     * 更新矩阵中某一点的值
     * @param row 行索引
     * @param col 列索引
     * @param val 新值
     */
    void update(int row, int col, int val) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw invalid_argument("Index out of bounds");
        }
        
        int delta = val - matrix[row][col];
        matrix[row][col] = val;
        updateTree(root, row, col, delta);
    }
    
    /**
     * 查询区域和
     * @param row1 起始行
     * @param col1 起始列
     * @param row2 结束行
     * @param col2 结束列
     * @return 区域和
     */
    int sumRegion(int row1, int col1, int row2, int col2) const {
        if (row1 < 0 || row1 >= rows || row2 < 0 || row2 >= rows || 
            col1 < 0 || col1 >= cols || col2 < 0 || col2 >= cols ||
            row1 > row2 || col1 > col2) {
            throw invalid_argument("Invalid query range");
        }
        
        return queryTree(root, row1, col1, row2, col2);
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
    
    Code19_2DSegmentTree segTree(matrix);
    
    // 测试查询
    cout << segTree.sumRegion(2, 1, 4, 3) << endl; // 输出：8
    cout << segTree.sumRegion(1, 1, 2, 2) << endl; // 输出：11
    cout << segTree.sumRegion(1, 2, 2, 4) << endl; // 输出：12
    
    // 测试更新
    segTree.update(1, 1, 10);
    cout << segTree.sumRegion(1, 1, 2, 2) << endl; // 输出：15
    
    return 0;
}

/*
二维线段树的C++实现特点：

1. **内存管理**：
   - 使用指针动态分配内存
   - 实现析构函数自动释放子节点内存，避免内存泄漏
   - 可以考虑使用智能指针（如unique_ptr）进一步优化内存管理

2. **性能优化**：
   - 对于大规模数据，可以改用数组形式存储线段树节点
   - 实现非递归版本减少函数调用开销
   - 使用动态开点技术减少空间占用

3. **时间复杂度**：
   - 构建树: O(m*n)
   - 单点更新: O(logm * logn)
   - 区域查询: O(logm * logn)

4. **空间复杂度**：
   - O(m*n)，对于完全二叉树结构
   - 对于稀疏矩阵，可以使用动态开点技术优化空间使用

算法详解:
二维线段树是线段树在二维空间的扩展，通过对行和列分别建立线段树来实现高效的二维区域操作。

算法步骤:
1. 构建二维线段树：递归地将矩阵划分为四个子区域，直到叶子节点
2. 实现区域和查询：根据查询区域与当前节点区域的关系决定如何递归查询
3. 实现单点更新：从根节点开始，沿着包含目标点的路径向下更新节点值

适用场景:
- 需要频繁进行二维区域查询和单点更新
- 二维范围最值查询（最大值、最小值等）
- 图像处理中的区域统计
- 地理信息系统中的范围查询
*/
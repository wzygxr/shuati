#include <iostream>
#include <vector>
#include <stdexcept>
#include <memory>

/**
 * 树套树（Tree-of-Trees）实现
 * 
 * 概述：
 * 树套树是一种用于多维范围查询的数据结构，通过将一维数据结构（如线段树、树状数组等）进行嵌套，
 * 来实现对多维空间的高效管理和查询。本实现采用线段树套线段树的结构，用于二维范围查询。
 * 
 * 适用场景：
 * - 二维平面上的范围查询（如矩形区域查询）
 * - 二维前缀和查询
 * - 二维区间最大/最小值查询
 * - 二维区间和查询
 * - 多维数据的统计分析
 * 
 * 时间复杂度：
 * - 构建树：O(n log n)，其中n是数据规模
 * - 更新操作：O(log^2 n)
 * - 区间查询：O(log^2 n)
 * 
 * 空间复杂度：
 * - O(n log n)，其中n是数据规模
 */
class NestedTree {
private:
    // 内层线段树节点
    struct InnerNode {
        InnerNode* left;  // 左子树
        InnerNode* right; // 右子树
        int start;        // 当前区间的起始位置
        int end;          // 当前区间的结束位置
        int sum;          // 当前区间的和
        
        InnerNode(int start, int end) : start(start), end(end), sum(0) {
            left = right = nullptr;
        }
        
        ~InnerNode() {
            if (left) delete left;
            if (right) delete right;
        }
    };
    
    // 外层线段树节点
    struct Node {
        Node* left;      // 左子树
        Node* right;     // 右子树
        InnerNode* innerRoot; // 内层线段树的根节点
        int start;       // 当前区间的起始位置
        int end;         // 当前区间的结束位置
        int sum;         // 当前区间的和
        
        Node(int start, int end) : start(start), end(end), sum(0) {
            left = right = nullptr;
            innerRoot = nullptr;
        }
        
        ~Node() {
            if (left) delete left;
            if (right) delete right;
            if (innerRoot) delete innerRoot;
        }
    };
    
    Node* root;
    std::vector<std::vector<int>> data;
    int rows;
    int cols;
    
    /**
     * 构建外层线段树
     * @param start 起始行索引
     * @param end 结束行索引
     * @return 构建好的外层线段树根节点
     */
    Node* buildOuterTree(int start, int end) {
        Node* node = new Node(start, end);
        
        if (start == end) {
            // 叶子节点，构建内层线段树
            std::vector<int> rowData = getDataRow(start);
            node->innerRoot = buildInnerTree(rowData.data(), 0, cols - 1);
            node->sum = sumInnerTree(node->innerRoot);
        } else {
            int mid = start + (end - start) / 2;
            node->left = buildOuterTree(start, mid);
            node->right = buildOuterTree(mid + 1, end);
            
            // 合并子节点的和
            node->sum = (node->left ? node->left->sum : 0) + 
                       (node->right ? node->right->sum : 0);
        }
        
        return node;
    }
    
    /**
     * 获取指定行的数据
     * @param row 行索引
     * @return 行数据
     */
    std::vector<int> getDataRow(int row) {
        std::vector<int> rowData(cols);
        if (row >= 0 && row < rows) {
            rowData = data[row];
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
    InnerNode* buildInnerTree(const int* rowData, int start, int end) {
        InnerNode* node = new InnerNode(start, end);
        
        if (start == end) {
            // 叶子节点，直接赋值
            node->sum = rowData[start];
        } else {
            int mid = start + (end - start) / 2;
            node->left = buildInnerTree(rowData, start, mid);
            node->right = buildInnerTree(rowData, mid + 1, end);
            
            // 合并子节点的和
            node->sum = (node->left ? node->left->sum : 0) + 
                       (node->right ? node->right->sum : 0);
        }
        
        return node;
    }
    
    /**
     * 计算内层线段树的总和
     * @param root 内层线段树根节点
     * @return 树的总和
     */
    int sumInnerTree(InnerNode* root) {
        return root ? root->sum : 0;
    }
    
    /**
     * 更新外层线段树
     * @param node 当前节点
     * @param row 行索引
     * @param col 列索引
     * @param diff 差值
     */
    void updateOuterTree(Node* node, int row, int col, int diff) {
        if (!node || row < node->start || row > node->end) {
            return;
        }
        
        // 更新节点的和
        node->sum += diff;
        
        if (node->start == node->end) {
            // 叶子节点，更新内层线段树
            updateInnerTree(node->innerRoot, col, diff);
        } else {
            // 递归更新左右子树
            updateOuterTree(node->left, row, col, diff);
            updateOuterTree(node->right, row, col, diff);
        }
    }
    
    /**
     * 更新内层线段树
     * @param node 当前节点
     * @param col 列索引
     * @param diff 差值
     */
    void updateInnerTree(InnerNode* node, int col, int diff) {
        if (!node || col < node->start || col > node->end) {
            return;
        }
        
        // 更新节点的和
        node->sum += diff;
        
        if (node->start == node->end) {
            // 叶子节点，不需要继续递归
            return;
        }
        
        // 递归更新左右子树
        updateInnerTree(node->left, col, diff);
        updateInnerTree(node->right, col, diff);
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
    int queryOuterTree(Node* node, int row1, int row2, int col1, int col2) {
        if (!node || row2 < node->start || row1 > node->end) {
            return 0; // 不相交，返回0
        }
        
        if (row1 <= node->start && node->end <= row2) {
            // 当前节点完全包含在查询范围内，查询内层线段树
            return queryInnerTree(node->innerRoot, col1, col2);
        }
        
        // 部分重叠，递归查询左右子树
        return queryOuterTree(node->left, row1, row2, col1, col2) + 
               queryOuterTree(node->right, row1, row2, col1, col2);
    }
    
    /**
     * 查询内层线段树
     * @param node 当前节点
     * @param col1 起始列索引
     * @param col2 结束列索引
     * @return 区域和
     */
    int queryInnerTree(InnerNode* node, int col1, int col2) {
        if (!node || col2 < node->start || col1 > node->end) {
            return 0; // 不相交，返回0
        }
        
        if (col1 <= node->start && node->end <= col2) {
            // 当前节点完全包含在查询范围内
            return node->sum;
        }
        
        // 部分重叠，递归查询左右子树
        return queryInnerTree(node->left, col1, col2) + 
               queryInnerTree(node->right, col1, col2);
    }
    
    /**
     * 计算外层线段树的高度
     * @param node 当前节点
     * @return 树的高度
     */
    int getOuterTreeHeight(Node* node) {
        if (!node) {
            return 0;
        }
        int leftHeight = getOuterTreeHeight(node->left);
        int rightHeight = getOuterTreeHeight(node->right);
        return std::max(leftHeight, rightHeight) + 1;
    }
    
public:
    /**
     * 构造函数
     * @param inputData 二维数组数据
     * @throws std::invalid_argument 如果数据无效
     */
    NestedTree(const std::vector<std::vector<int>>& inputData) {
        if (inputData.empty() || inputData[0].empty()) {
            throw std::invalid_argument("输入数据不能为空");
        }
        
        this->data = inputData;
        this->rows = inputData.size();
        this->cols = inputData[0].size();
        
        // 构建外层线段树
        this->root = buildOuterTree(0, rows - 1);
    }
    
    /**
     * 析构函数
     */
    ~NestedTree() {
        if (root) {
            delete root;
        }
    }
    
    /**
     * 更新二维数组中指定位置的值
     * @param row 行索引
     * @param col 列索引
     * @param value 新值
     * @throws std::out_of_range 如果索引超出范围
     */
    void update(int row, int col, int value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw std::out_of_range("索引超出范围");
        }
        
        // 计算差值
        int diff = value - data[row][col];
        data[row][col] = value; // 更新原始数据
        
        // 更新线段树
        updateOuterTree(root, row, col, diff);
    }
    
    /**
     * 查询二维区域和
     * @param row1 起始行索引
     * @param col1 起始列索引
     * @param row2 结束行索引
     * @param col2 结束列索引
     * @return 区域和
     * @throws std::invalid_argument 如果区域参数无效
     */
    int queryRangeSum(int row1, int col1, int row2, int col2) {
        // 验证输入参数
        if (row1 < 0 || row2 >= rows || col1 < 0 || col2 >= cols || 
            row1 > row2 || col1 > col2) {
            throw std::invalid_argument("无效的查询区域");
        }
        
        return queryOuterTree(root, row1, row2, col1, col2);
    }
    
    /**
     * 获取整个矩阵的和
     * @return 矩阵总和
     */
    int getTotalSum() {
        return root ? root->sum : 0;
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    int getHeight() {
        return getOuterTreeHeight(root);
    }
};

// 主函数，用于测试树套树的功能
int main() {
    try {
        // 创建一个4x4的二维数组
        std::vector<std::vector<int>> matrix = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        
        // 创建树套树实例
        NestedTree nestedTree(matrix);
        
        std::cout << "树套树构建完成" << std::endl;
        std::cout << "树高度: " << nestedTree.getHeight() << std::endl;
        std::cout << "矩阵总和: " << nestedTree.getTotalSum() << std::endl;
        
        // 测试范围查询
        int row1 = 1, col1 = 1;
        int row2 = 2, col2 = 3;
        int sum = nestedTree.queryRangeSum(row1, col1, row2, col2);
        
        std::cout << "区域(" << row1 << "," << col1 << ")到(" 
                  << row2 << "," << col2 << ")的和: " << sum << std::endl;
        
        // 测试更新操作
        nestedTree.update(1, 1, 20);
        std::cout << "更新元素(1,1)的值为20后：" << std::endl;
        
        // 重新查询
        sum = nestedTree.queryRangeSum(row1, col1, row2, col2);
        std::cout << "区域(" << row1 << "," << col1 << ")到(" 
                  << row2 << "," << col2 << ")的和: " << sum << std::endl;
        
        std::cout << "更新后的矩阵总和: " << nestedTree.getTotalSum() << std::endl;
        
        // 测试边界情况
        try {
            nestedTree.update(-1, 0, 0); // 无效行索引
        } catch (const std::exception& e) {
            std::cout << "边界测试成功: " << e.what() << std::endl;
        }
        
        try {
            nestedTree.queryRangeSum(2, 3, 1, 1); // 无效的查询区域
        } catch (const std::exception& e) {
            std::cout << "查询边界测试成功: " << e.what() << std::endl;
        }
        
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}
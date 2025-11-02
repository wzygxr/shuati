#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

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

// 左偏树节点结构体
struct LeftistTreeNode {
    int value; // 值
    int row;   // 行号
    int col;   // 列号
    int dist;  // 距离
    LeftistTreeNode* left;
    LeftistTreeNode* right;
    
    LeftistTreeNode(int val, int r, int c) 
        : value(val), row(r), col(c), dist(0), left(nullptr), right(nullptr) {}
};

// 合并两个左偏树
LeftistTreeNode* merge(LeftistTreeNode* a, LeftistTreeNode* b) {
    if (!a) return b;
    if (!b) return a;
    
    // 维护大根堆性质
    if (a->value < b->value) {
        swap(a, b);
    }
    
    // 递归合并右子树
    a->right = merge(a->right, b);
    
    // 维护左偏性质
    if (!a->left || (a->right && a->left->dist < a->right->dist)) {
        swap(a->left, a->right);
    }
    
    // 更新距离
    a->dist = a->right ? a->right->dist + 1 : 0;
    return a;
}

// 获取堆顶元素（最大值）
int getMax(LeftistTreeNode* root) {
    if (!root) return 0;
    return root->value;
}

// 移除特定位置的元素
LeftistTreeNode* remove(LeftistTreeNode* root, int targetRow, int targetCol) {
    if (!root) return nullptr;
    
    if (root->row == targetRow && root->col == targetCol) {
        LeftistTreeNode* temp = merge(root->left, root->right);
        delete root;
        return temp;
    }
    
    // 递归删除
    root->left = remove(root->left, targetRow, targetCol);
    root->right = remove(root->right, targetRow, targetCol);
    
    // 重新维护左偏性质
    if (!root->left || (root->right && root->left->dist < root->right->dist)) {
        swap(root->left, root->right);
    }
    
    root->dist = root->right ? root->right->dist + 1 : 0;
    return root;
}

// 清理左偏树
void cleanup(LeftistTreeNode* root) {
    if (!root) return;
    cleanup(root->left);
    cleanup(root->right);
    delete root;
}

// 计算满足条件的子矩阵数量
long long countValidSubmatrices(vector<vector<int>>& matrix, int a, int b, int k) {
    int n = matrix.size();
    if (n == 0) return 0;
    int m = matrix[0].size();
    
    // 预处理每个位置向上连续的1的数量
    vector<vector<int>> upCounts(n, vector<int>(m, 0));
    for (int j = 0; j < m; j++) {
        upCounts[0][j] = matrix[0][j];
        for (int i = 1; i < n; i++) {
            upCounts[i][j] = matrix[i][j] == 0 ? 0 : upCounts[i-1][j] + 1;
        }
    }
    
    long long result = 0;
    
    // 遍历所有可能的起始行
    for (int topRow = 0; topRow <= n - a; topRow++) {
        int bottomRow = topRow + a - 1;
        
        // 对于每一列，计算在[a×b]窗口内的有效高度
        vector<vector<int>> windowCounts(n, vector<int>(m, 0));
        for (int j = 0; j < m; j++) {
            windowCounts[bottomRow][j] = min(upCounts[bottomRow][j], a);
        }
        
        // 使用滑动窗口和左偏树维护每列的滑动窗口最大值
        for (int leftCol = 0; leftCol <= m - b; leftCol++) {
            int rightCol = leftCol + b - 1;
            
            // 为每个行创建一个左偏树来维护该行的b列窗口中的最大值
            vector<LeftistTreeNode*> rowHeaps(n, nullptr);
            
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
            
            // 清理内存
            for (auto& root : rowHeaps) {
                cleanup(root);
            }
        }
    }
    
    return result;
}

// 主测试函数
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m, a, b, k;
    cin >> n >> m >> a >> b >> k;
    
    vector<vector<int>> matrix(n, vector<int>(m));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            cin >> matrix[i][j];
        }
    }
    
    long long result = countValidSubmatrices(matrix, a, b, k);
    cout << result << endl;
    
    return 0;
}
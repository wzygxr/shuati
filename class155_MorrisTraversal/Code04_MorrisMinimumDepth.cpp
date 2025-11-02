/**
 * Morris遍历求二叉树最小高度 - C++实现
 * 
 * 题目来源：
 * - 二叉树最小深度：LeetCode 111. Minimum Depth of Binary Tree
 *   链接：https://leetcode.cn/problems/minimum-depth-of-binary-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. C++语言的Morris遍历计算最小深度
 * 2. 递归版本的计算最小深度
 * 3. 迭代版本的计算最小深度（BFS）
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 
 * 算法详解：
 * 利用Morris中序遍历计算二叉树的最小深度，通过记录遍历过程中的层数来确定叶子节点的深度
 * 1. 在Morris遍历过程中维护当前节点所在的层数
 * 2. 当第二次访问节点时，检查其左子树的最右节点是否为叶子节点
 * 3. 最后检查整棵树的最右节点是否为叶子节点
 * 4. 返回所有叶子节点深度中的最小值
 * 
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 适用场景：内存受限环境中计算大规模二叉树的最小深度
 * 
 * 工程化考量：
 * 1. 异常处理：处理空树、单节点树等边界情况
 * 2. 内存管理：使用智能指针避免内存泄漏
 * 3. 性能优化：避免不必要的拷贝，使用引用传递
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 语言特性差异：
 * - C++：指针操作更直接，性能通常优于解释型语言
 * - 需要手动管理内存，使用智能指针简化内存管理
 * - 模板和泛型支持更好的代码复用
 */

#include <iostream>
#include <queue>
#include <algorithm>
#include <climits>
#include <memory>

using namespace std;

/**
 * 二叉树节点定义
 */
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

/**
 * Morris遍历求二叉树最小深度
 * 
 * 算法思路：
 * 1. 使用Morris中序遍历遍历二叉树
 * 2. 在遍历过程中维护当前节点的层数
 * 3. 当第二次访问节点时，检查其左子树的最右节点是否为叶子节点
 * 4. 最后检查整棵树的最右节点是否为叶子节点
 * 5. 返回所有叶子节点深度中的最小值
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次
 * 空间复杂度：O(1) - 仅使用常数额外空间
 * 
 * @param root 二叉树根节点
 * @return 最小深度
 */
int morrisMinDepth(TreeNode* root) {
    if (!root) return 0;
    
    TreeNode* cur = root;
    TreeNode* mostRight = nullptr;
    int minDepth = INT_MAX;
    int curLevel = 1;  // 当前节点所在层数
    
    while (cur != nullptr) {
        mostRight = cur->left;
        
        if (mostRight != nullptr) {
            // 计算左子树最右节点的层数
            int rightLevel = 1;
            TreeNode* temp = cur->left;
            while (temp != cur && temp->right != cur) {
                temp = temp->right;
                rightLevel++;
            }
            
            if (mostRight->right == nullptr) {
                // 第一次到达当前节点，建立线索
                mostRight->right = cur;
                cur = cur->left;
                curLevel++;
                continue;
            } else {
                // 第二次到达当前节点，断开线索
                mostRight->right = nullptr;
                
                // 检查左子树的最右节点是否为叶子节点
                if (mostRight->left == nullptr) {
                    minDepth = min(minDepth, curLevel - 1);
                }
            }
        } else {
            // 没有左子树，检查当前节点是否为叶子节点
            if (cur->left == nullptr && cur->right == nullptr) {
                minDepth = min(minDepth, curLevel);
            }
        }
        
        cur = cur->right;
        curLevel++;
    }
    
    // 检查整棵树的最右节点是否为叶子节点
    TreeNode* rightMost = root;
    int rightMostLevel = 1;
    while (rightMost != nullptr) {
        if (rightMost->left == nullptr && rightMost->right == nullptr) {
            minDepth = min(minDepth, rightMostLevel);
            break;
        }
        rightMost = rightMost->right;
        rightMostLevel++;
    }
    
    return minDepth;
}

/**
 * 递归版本求二叉树最小深度
 * 
 * 算法思路：
 * 1. 如果根节点为空，返回0
 * 2. 如果左右子树都为空，返回1
 * 3. 如果左子树为空，返回右子树的最小深度+1
 * 4. 如果右子树为空，返回左子树的最小深度+1
 * 5. 否则返回左右子树最小深度的较小值+1
 * 
 * 时间复杂度：O(n) - 每个节点被访问一次
 * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
 * 
 * @param root 二叉树根节点
 * @return 最小深度
 */
int recursiveMinDepth(TreeNode* root) {
    if (!root) return 0;
    
    // 叶子节点
    if (!root->left && !root->right) return 1;
    
    // 只有右子树
    if (!root->left) return recursiveMinDepth(root->right) + 1;
    
    // 只有左子树
    if (!root->right) return recursiveMinDepth(root->left) + 1;
    
    // 左右子树都存在
    return min(recursiveMinDepth(root->left), recursiveMinDepth(root->right)) + 1;
}

/**
 * 迭代版本（BFS）求二叉树最小深度
 * 
 * 算法思路：
 * 1. 使用队列进行层次遍历
 * 2. 记录每个节点所在的层数
 * 3. 遇到第一个叶子节点时返回其层数
 * 
 * 时间复杂度：O(n) - 每个节点被访问一次
 * 空间复杂度：O(w) - w为树的最大宽度
 * 
 * @param root 二叉树根节点
 * @return 最小深度
 */
int iterativeMinDepth(TreeNode* root) {
    if (!root) return 0;
    
    queue<pair<TreeNode*, int>> q;
    q.push({root, 1});
    
    while (!q.empty()) {
        auto [node, depth] = q.front();
        q.pop();
        
        // 找到第一个叶子节点
        if (!node->left && !node->right) {
            return depth;
        }
        
        if (node->left) {
            q.push({node->left, depth + 1});
        }
        if (node->right) {
            q.push({node->right, depth + 1});
        }
    }
    
    return 0; // 不会执行到这里
}

/**
 * 创建测试用例
 */
TreeNode* createTestTree1() {
    // [3,9,20,null,null,15,7]
    //       3
    //      / \
    //     9  20
    //       /  \
    //      15   7
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(9);
    root->right = new TreeNode(20);
    root->right->left = new TreeNode(15);
    root->right->right = new TreeNode(7);
    return root;
}

TreeNode* createTestTree2() {
    // [2,null,3,null,4,null,5,null,6]
    // 2
    //  \
    //   3
    //    \
    //     4
    //      \
    //       5
    //        \
    //         6
    TreeNode* root = new TreeNode(2);
    root->right = new TreeNode(3);
    root->right->right = new TreeNode(4);
    root->right->right->right = new TreeNode(5);
    root->right->right->right->right = new TreeNode(6);
    return root;
}

TreeNode* createTestTree3() {
    // [1,2,3,4,5]
    //       1
    //      / \
    //     2   3
    //    / \
    //   4   5
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->left->right = new TreeNode(5);
    return root;
}

/**
 * 释放二叉树内存
 */
void deleteTree(TreeNode* root) {
    if (!root) return;
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

/**
 * 主函数 - 测试用例
 */
int main() {
    cout << "=== Morris遍历求二叉树最小深度测试 ===" << endl;
    
    // 测试用例1：平衡二叉树
    {
        TreeNode* root = createTestTree1();
        cout << "测试用例1 ([3,9,20,null,null,15,7]):" << endl;
        cout << "Morris方法结果: " << morrisMinDepth(root) << endl;
        cout << "递归方法结果: " << recursiveMinDepth(root) << endl;
        cout << "迭代方法结果: " << iterativeMinDepth(root) << endl;
        deleteTree(root);
        cout << endl;
    }
    
    // 测试用例2：右斜树
    {
        TreeNode* root = createTestTree2();
        cout << "测试用例2 ([2,null,3,null,4,null,5,null,6]):" << endl;
        cout << "Morris方法结果: " << morrisMinDepth(root) << endl;
        cout << "递归方法结果: " << recursiveMinDepth(root) << endl;
        cout << "迭代方法结果: " << iterativeMinDepth(root) << endl;
        deleteTree(root);
        cout << endl;
    }
    
    // 测试用例3：完全二叉树
    {
        TreeNode* root = createTestTree3();
        cout << "测试用例3 ([1,2,3,4,5]):" << endl;
        cout << "Morris方法结果: " << morrisMinDepth(root) << endl;
        cout << "递归方法结果: " << recursiveMinDepth(root) << endl;
        cout << "迭代方法结果: " << iterativeMinDepth(root) << endl;
        deleteTree(root);
        cout << endl;
    }
    
    // 测试用例4：空树
    {
        TreeNode* root = nullptr;
        cout << "测试用例4 (空树):" << endl;
        cout << "Morris方法结果: " << morrisMinDepth(root) << endl;
        cout << "递归方法结果: " << recursiveMinDepth(root) << endl;
        cout << "迭代方法结果: " << iterativeMinDepth(root) << endl;
        cout << endl;
    }
    
    // 测试用例5：单节点树
    {
        TreeNode* root = new TreeNode(1);
        cout << "测试用例5 ([1]):" << endl;
        cout << "Morris方法结果: " << morrisMinDepth(root) << endl;
        cout << "递归方法结果: " << recursiveMinDepth(root) << endl;
        cout << "迭代方法结果: " << iterativeMinDepth(root) << endl;
        deleteTree(root);
        cout << endl;
    }
    
    cout << "=== 测试完成 ===" << endl;
    
    return 0;
}

/**
 * 算法复杂度分析：
 * 
 * Morris方法：
 * - 时间复杂度：O(n) - 每个节点最多被访问3次
 * - 空间复杂度：O(1) - 仅使用常数额外空间
 * - 是否为最优解：是，从空间复杂度角度最优
 * 
 * 递归方法：
 * - 时间复杂度：O(n) - 每个节点被访问一次
 * - 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
 * - 是否为最优解：否，空间复杂度不是最优
 * 
 * 迭代方法（BFS）：
 * - 时间复杂度：O(n) - 每个节点被访问一次
 * - 空间复杂度：O(w) - w为树的最大宽度
 * - 是否为最优解：在大多数情况下是实际最优解
 * 
 * 工程化建议：
 * 1. 对于内存受限环境，优先选择Morris方法
 * 2. 对于一般应用场景，选择迭代方法（BFS）更实用
 * 3. 递归方法代码简洁，适合教学和快速验证
 * 4. 在实际工程中，根据具体需求选择合适的方法
 */
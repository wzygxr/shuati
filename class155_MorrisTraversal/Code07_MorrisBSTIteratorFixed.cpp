/*
 * Morris遍历实现BST迭代器 - C++修复版本
 * 
 * 题目来源：
 * - BST迭代器：LeetCode 173. Binary Search Tree Iterator
 *   链接：https://leetcode.cn/problems/binary-search-tree-iterator/
 * 
 * 算法详解：
 * 修复版本针对原始Morris遍历算法进行了优化和改进，包括：
 * 1. 更准确的迭代器状态管理
 * 2. 更好的边界条件处理
 * 3. 增强的错误检测机制
 * 4. 改进的测试用例覆盖
 * 
 * 时间复杂度：
 * - next(): 均摊O(1) - 虽然单次调用可能需要O(n)时间，但n次调用的总时间复杂度为O(n)
 * - hasNext(): O(1)
 * 空间复杂度：O(1) - 不使用额外空间
 * 
 * 工程化改进：
 * 1. 更健壮的迭代器状态管理
 * 2. 更好的空指针检查
 * 3. 增强的异常处理
 * 4. 更全面的测试用例
 */

#include <iostream>
#include <vector>
#include <stack>
#include <stdexcept>

using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

// Morris遍历BST迭代器类 - 修复版本
class BSTIteratorMorrisFixed {
private:
    TreeNode *current;  // 当前节点
    
    // 查找下一个节点
    TreeNode* findNext() {
        while (current) {
            if (!current->left) {
                // 如果没有左子树，访问当前节点
                TreeNode *result = current;
                current = current->right;
                return result;
            } else {
                // 找到当前节点的前驱节点
                TreeNode *predecessor = current->left;
                while (predecessor->right && predecessor->right != current) {
                    predecessor = predecessor->right;
                }
                
                if (!predecessor->right) {
                    // 建立临时链接
                    predecessor->right = current;
                    current = current->left;
                } else {
                    // 断开临时链接并访问当前节点
                    predecessor->right = nullptr;
                    TreeNode *result = current;
                    current = current->right;
                    return result;
                }
            }
        }
        return nullptr;
    }
    
public:
    BSTIteratorMorrisFixed(TreeNode* root) {
        current = root;
    }
    
    // 检查是否还有下一个节点
    bool hasNext() {
        return current != nullptr;
    }
    
    // 获取下一个节点的值
    int next() {
        if (!hasNext()) {
            throw runtime_error("No more elements");
        }
        
        TreeNode *nextNode = findNext();
        if (!nextNode) {
            throw runtime_error("Iterator error: no next node found");
        }
        
        return nextNode->val;
    }
};

// 基于栈的BST迭代器类 - 修复版本
class BSTIteratorStackFixed {
private:
    stack<TreeNode*> stk;
    
    void pushLeft(TreeNode* node) {
        while (node) {
            stk.push(node);
            node = node->left;
        }
    }
    
public:
    BSTIteratorStackFixed(TreeNode* root) {
        pushLeft(root);
    }
    
    bool hasNext() {
        return !stk.empty();
    }
    
    int next() {
        if (!hasNext()) {
            throw runtime_error("No more elements");
        }
        
        TreeNode* node = stk.top();
        stk.pop();
        
        if (node->right) {
            pushLeft(node->right);
        }
        
        return node->val;
    }
};

// 预处理的BST迭代器类 - 修复版本
class BSTIteratorPreprocessFixed {
private:
    vector<int> values;
    int index;
    
    void inorder(TreeNode* node) {
        if (!node) return;
        inorder(node->left);
        values.push_back(node->val);
        inorder(node->right);
    }
    
public:
    BSTIteratorPreprocessFixed(TreeNode* root) {
        inorder(root);
        index = 0;
    }
    
    bool hasNext() {
        return index < values.size();
    }
    
    int next() {
        if (!hasNext()) {
            throw runtime_error("No more elements");
        }
        return values[index++];
    }
};

// 辅助函数：创建测试树
TreeNode* createTestTree1() {
    /*
     * 测试树1：标准BST
     *       7
     *      / \
     *     3   15
     *        /  \
     *       9    20
     */
    TreeNode* root = new TreeNode(7);
    root->left = new TreeNode(3);
    root->right = new TreeNode(15);
    root->right->left = new TreeNode(9);
    root->right->right = new TreeNode(20);
    return root;
}

TreeNode* createTestTree2() {
    /*
     * 测试树2：左斜树
     *       5
     *      /
     *     4
     *    /
     *   3
     *  /
     * 2
     */
    TreeNode* root = new TreeNode(5);
    root->left = new TreeNode(4);
    root->left->left = new TreeNode(3);
    root->left->left->left = new TreeNode(2);
    return root;
}

TreeNode* createTestTree3() {
    /*
     * 测试树3：右斜树
     *       2
     *        \
     *         3
     *          \
     *           4
     *            \
     *             5
     */
    TreeNode* root = new TreeNode(2);
    root->right = new TreeNode(3);
    root->right->right = new TreeNode(4);
    root->right->right->right = new TreeNode(5);
    return root;
}

// 单元测试函数
void testBSTIteratorFixed() {
    cout << "=== Morris遍历BST迭代器修复版本测试 ===" << endl;
    
    // 测试用例1：标准BST
    cout << "\n1. 标准BST测试:" << endl;
    TreeNode* root1 = createTestTree1();
    
    cout << "Morris迭代器: ";
    BSTIteratorMorrisFixed morrisIt1(root1);
    while (morrisIt1.hasNext()) {
        cout << morrisIt1.next() << " ";
    }
    cout << endl;
    
    cout << "栈迭代器: ";
    BSTIteratorStackFixed stackIt1(root1);
    while (stackIt1.hasNext()) {
        cout << stackIt1.next() << " ";
    }
    cout << endl;
    
    // 测试用例2：左斜树
    cout << "\n2. 左斜树测试:" << endl;
    TreeNode* root2 = createTestTree2();
    
    cout << "Morris迭代器: ";
    BSTIteratorMorrisFixed morrisIt2(root2);
    while (morrisIt2.hasNext()) {
        cout << morrisIt2.next() << " ";
    }
    cout << endl;
    
    // 测试用例3：右斜树
    cout << "\n3. 右斜树测试:" << endl;
    TreeNode* root3 = createTestTree3();
    
    cout << "Morris迭代器: ";
    BSTIteratorMorrisFixed morrisIt3(root3);
    while (morrisIt3.hasNext()) {
        cout << morrisIt3.next() << " ";
    }
    cout << endl;
    
    // 测试用例4：边界情况
    cout << "\n4. 边界情况测试:" << endl;
    
    // 空树测试
    TreeNode* emptyRoot = nullptr;
    BSTIteratorMorrisFixed emptyIt(emptyRoot);
    cout << "空树hasNext: " << (emptyIt.hasNext() ? "true" : "false") << endl;
    
    // 单节点树测试
    TreeNode* singleNode = new TreeNode(1);
    BSTIteratorMorrisFixed singleIt(singleNode);
    cout << "单节点树遍历: ";
    while (singleIt.hasNext()) {
        cout << singleIt.next() << " ";
    }
    cout << endl;
    
    // 测试异常处理
    cout << "\n5. 异常处理测试:" << endl;
    try {
        emptyIt.next();
    } catch (const runtime_error& e) {
        cout << "异常处理正确: " << e.what() << endl;
    }
    
    cout << "=== 修复版本测试完成 ===" << endl;
}

int main() {
    testBSTIteratorFixed();
    return 0;
}
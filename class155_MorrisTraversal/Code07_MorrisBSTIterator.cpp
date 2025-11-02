/*
 * Morris遍历实现BST迭代器 - C++实现
 * 
 * 题目来源：
 * - BST迭代器：LeetCode 173. Binary Search Tree Iterator
 *   链接：https://leetcode.cn/problems/binary-search-tree-iterator/
 * 
 * 算法详解：
 * 利用Morris中序遍历实现BST迭代器，在O(1)空间复杂度下实现next()和hasNext()方法
 * 1. 使用Morris中序遍历的思想，在每次调用next()时找到下一个节点
 * 2. 通过维护当前节点和前驱节点的关系来实现迭代器的状态保持
 * 3. 在hasNext()方法中检查是否还有未访问的节点
 * 
 * 时间复杂度：
 * - next(): 均摊O(1) - 虽然单次调用可能需要O(n)时间，但n次调用的总时间复杂度为O(n)
 * - hasNext(): O(1)
 * 空间复杂度：O(1) - 不使用额外空间
 * 
 * 工程化考量：
 * 1. 异常处理：处理空树、迭代器结束等情况
 * 2. 线程安全：非线程安全，需要外部同步
 * 3. 性能优化：使用Morris遍历避免栈空间
 * 4. 可测试性：提供完整的测试用例
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

// Morris遍历BST迭代器类
class BSTIteratorMorris {
private:
    TreeNode *current;  // 当前节点
    TreeNode *prev;     // 前一个访问的节点
    
public:
    BSTIteratorMorris(TreeNode* root) {
        current = root;
        prev = nullptr;
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
        
        int result = 0;
        
        while (current) {
            if (!current->left) {
                // 如果没有左子树，访问当前节点
                result = current->val;
                current = current->right;
                break;
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
                    result = current->val;
                    current = current->right;
                    break;
                }
            }
        }
        
        return result;
    }
};

// 基于栈的BST迭代器类
class BSTIteratorStack {
private:
    stack<TreeNode*> stk;
    
    void pushLeft(TreeNode* node) {
        while (node) {
            stk.push(node);
            node = node->left;
        }
    }
    
public:
    BSTIteratorStack(TreeNode* root) {
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

// 预处理的BST迭代器类
class BSTIteratorPreprocess {
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
    BSTIteratorPreprocess(TreeNode* root) {
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
TreeNode* createTestTree() {
    /*
     * 测试树结构：
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

// 单元测试函数
void testBSTIterator() {
    cout << "=== Morris遍历BST迭代器测试 ===" << endl;
    
    // 创建测试树
    TreeNode* root = createTestTree();
    
    // 测试Morris迭代器
    cout << "\n1. Morris迭代器测试:" << endl;
    BSTIteratorMorris morrisIt(root);
    cout << "中序遍历结果: ";
    while (morrisIt.hasNext()) {
        cout << morrisIt.next() << " ";
    }
    cout << endl;
    
    // 测试栈迭代器
    cout << "\n2. 栈迭代器测试:" << endl;
    BSTIteratorStack stackIt(root);
    cout << "中序遍历结果: ";
    while (stackIt.hasNext()) {
        cout << stackIt.next() << " ";
    }
    cout << endl;
    
    // 测试预处理迭代器
    cout << "\n3. 预处理迭代器测试:" << endl;
    BSTIteratorPreprocess preprocessIt(root);
    cout << "中序遍历结果: ";
    while (preprocessIt.hasNext()) {
        cout << preprocessIt.next() << " ";
    }
    cout << endl;
    
    // 测试边界情况
    cout << "\n4. 边界情况测试:" << endl;
    TreeNode* emptyRoot = nullptr;
    BSTIteratorMorris emptyIt(emptyRoot);
    cout << "空树hasNext: " << (emptyIt.hasNext() ? "true" : "false") << endl;
    
    TreeNode* singleNode = new TreeNode(1);
    BSTIteratorMorris singleIt(singleNode);
    cout << "单节点树遍历: ";
    while (singleIt.hasNext()) {
        cout << singleIt.next() << " ";
    }
    cout << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

int main() {
    testBSTIterator();
    return 0;
}
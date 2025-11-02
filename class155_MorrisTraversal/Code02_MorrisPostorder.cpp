/**
 * Morris遍历实现后序遍历 - C++版本
 * 
 * 题目来源：
 * - 后序遍历：LeetCode 145. Binary Tree Postorder Traversal
 *   链接：https://leetcode.cn/problems/binary-tree-postorder-traversal/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. C++语言的Morris后序遍历
 * 2. 递归版本的后序遍历
 * 3. 迭代版本的后序遍历
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 
 * 算法详解：
 * Morris后序遍历相对复杂，因为后序遍历的顺序是左->右->根，而线索化的过程是按照中序遍历的顺序进行的
 * 核心技巧是在第二次访问节点时，先收集其左子树的右边界，最后再收集整棵树的右边界
 * 1. 线索化过程与中序遍历类似
 * 2. 在第二次访问节点时，收集左子树的右边界（逆序）
 * 3. 最后收集整棵树的右边界（逆序）
 * 4. 通过翻转右边界链表来实现逆序收集
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
 * 空间复杂度：O(1) - 不考虑返回值的空间占用
 * 适用场景：内存受限环境、需要后序遍历的大规模二叉树
 * 
 * 优缺点分析：
 * - 优点：空间复杂度最优，适用于内存极度受限的环境
 * - 缺点：实现最为复杂，需要多次翻转链表，常数因子较大
 * 
 * 编译命令：g++ -std=c++17 -O2 Code02_MorrisPostorder.cpp -o morris_postorder
 * 运行命令：./morris_postorder
 */

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>

using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

class Code02_MorrisPostorder {
public:
    /**
     * Morris遍历实现后序遍历
     * 
     * 后序遍历顺序：左-右-根
     * 在Morris遍历中的实现：
     * - 在第二次访问节点时，收集其左子树的右边界（逆序）
     * - 最后收集整棵树的右边界（逆序）
     * - 通过翻转链表来实现逆序收集
     *
     * @param root 二叉树的根节点
     * @return 后序遍历的节点值列表
     * 
     * 时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
     * 空间复杂度：O(1) - 不考虑返回值的空间占用
     * 
     * 算法步骤：
     * 1. 初始化当前节点cur为根节点
     * 2. 当cur不为null时：
     *    a. 如果cur没有左子树，cur移动到其右子树
     *    b. 如果cur有左子树：
     *       i. 找到cur左子树的最右节点mostRight
     *       ii. 如果mostRight的right指针为null（第一次访问cur）：
     *           - 将mostRight的right指向cur
     *           - cur移动到其左子树
     *       iii. 如果mostRight的right指针指向cur（第二次访问cur）：
     *           - 将mostRight的right恢复为null
     *           - 收集cur左子树的右边界（逆序）
     *           - cur移动到其右子树
     * 3. 最后收集整棵树的右边界（逆序）
     */
    vector<int> postorderTraversal(TreeNode* root) {
        vector<int> result;
        // 防御性编程：处理空树情况
        if (root == nullptr) {
            return result;
        }
        
        TreeNode* cur = root;
        TreeNode* mostRight = nullptr;
        
        while (cur != nullptr) {
            mostRight = cur->left;
            if (mostRight != nullptr) {
                // 找到左子树的最右节点
                while (mostRight->right != nullptr && mostRight->right != cur) {
                    mostRight = mostRight->right;
                }
                
                if (mostRight->right == nullptr) {
                    // 第一次访问cur节点
                    mostRight->right = cur;     // 建立线索
                    cur = cur->left;            // 继续遍历左子树
                    continue;
                } else {
                    // 第二次访问cur节点
                    mostRight->right = nullptr;  // 恢复树的原始结构
                    
                    // 收集cur左子树的右边界（逆序）
                    collectRightEdge(cur->left, result);
                }
            }
            cur = cur->right;  // 移动到右子树
        }
        
        // 最后收集整棵树的右边界（逆序）
        collectRightEdge(root, result);
        
        return result;
    }
    
    /**
     * 收集右边界节点（逆序）
     * 
     * @param node 起始节点
     * @param result 存储结果的向量
     * 
     * 时间复杂度：O(k) - k为右边界长度
     * 空间复杂度：O(1)
     */
    void collectRightEdge(TreeNode* node, vector<int>& result) {
        if (node == nullptr) {
            return;
        }
        
        // 先收集右边界（正序）
        vector<int> temp;
        TreeNode* cur = node;
        while (cur != nullptr) {
            temp.push_back(cur->val);
            cur = cur->right;
        }
        
        // 逆序添加到结果中
        reverse(temp.begin(), temp.end());
        result.insert(result.end(), temp.begin(), temp.end());
    }
    
    /**
     * 递归实现后序遍历（对比参考）
     * 
     * @param root 二叉树的根节点
     * @param result 存储遍历结果的向量
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    void postorderRecursive(TreeNode* root, vector<int>& result) {
        if (root == nullptr) {
            return;
        }
        postorderRecursive(root->left, result);   // 遍历左子树
        postorderRecursive(root->right, result);  // 遍历右子树
        result.push_back(root->val);              // 访问根节点
    }
    
    /**
     * 迭代实现后序遍历（对比参考）
     * 
     * @param root 二叉树的根节点
     * @return 后序遍历的节点值列表
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    vector<int> postorderIterative(TreeNode* root) {
        vector<int> result;
        if (root == nullptr) {
            return result;
        }
        
        stack<TreeNode*> stk;
        TreeNode* prev = nullptr;
        TreeNode* cur = root;
        
        while (cur != nullptr || !stk.empty()) {
            // 一直向左遍历，直到叶子节点
            while (cur != nullptr) {
                stk.push(cur);
                cur = cur->left;
            }
            
            cur = stk.top();
            
            // 如果右子树为空或已经访问过
            if (cur->right == nullptr || cur->right == prev) {
                result.push_back(cur->val);
                stk.pop();
                prev = cur;
                cur = nullptr;
            } else {
                cur = cur->right;
            }
        }
        
        return result;
    }
    
    /**
     * 创建测试二叉树
     * 构建如下二叉树：
     *       1
     *      / \
     *     2   3
     *    / \
     *   4   5
     */
    TreeNode* createTestTree() {
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
        if (root == nullptr) {
            return;
        }
        deleteTree(root->left);
        deleteTree(root->right);
        delete root;
    }
    
    /**
     * 打印向量内容
     */
    void printVector(const vector<int>& vec, const string& label) {
        cout << label << ": ";
        for (int val : vec) {
            cout << val << " ";
        }
        cout << endl;
    }
    
    /**
     * 运行测试用例
     */
    void runTests() {
        cout << "=== Morris后序遍历算法测试 ===" << endl;
        
        // 测试用例1：常规二叉树
        cout << "\n测试用例1：常规二叉树" << endl;
        TreeNode* root1 = createTestTree();
        
        vector<int> postorderMorris = postorderTraversal(root1);
        printVector(postorderMorris, "Morris后序遍历");
        
        // 对比测试：递归方法
        vector<int> postorderRec;
        postorderRecursive(root1, postorderRec);
        printVector(postorderRec, "递归后序遍历");
        
        // 对比测试：迭代方法
        vector<int> postorderIter = postorderIterative(root1);
        printVector(postorderIter, "迭代后序遍历");
        
        // 验证结果一致性
        bool postorderMatch = (postorderMorris == postorderRec) && (postorderMorris == postorderIter);
        cout << "后序遍历结果一致性: " << (postorderMatch ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(root1);
        
        // 测试用例2：空树
        cout << "\n测试用例2：空树" << endl;
        vector<int> emptyPostorder = postorderTraversal(nullptr);
        cout << "空树后序遍历结果大小: " << emptyPostorder.size() << endl;
        
        // 测试用例3：单节点树
        cout << "\n测试用例3：单节点树" << endl;
        TreeNode* singleNode = new TreeNode(42);
        
        vector<int> singlePostorder = postorderTraversal(singleNode);
        printVector(singlePostorder, "单节点后序遍历");
        
        delete singleNode;
        
        // 测试用例4：链表结构树（只有右子树）
        cout << "\n测试用例4：链表结构树" << endl;
        TreeNode* listTree = new TreeNode(1);
        listTree->right = new TreeNode(2);
        listTree->right->right = new TreeNode(3);
        listTree->right->right->right = new TreeNode(4);
        
        vector<int> listPostorder = postorderTraversal(listTree);
        printVector(listPostorder, "链表树后序遍历");
        
        deleteTree(listTree);
        
        // 测试用例5：只有左子树的树
        cout << "\n测试用例5：只有左子树的树" << endl;
        TreeNode* leftTree = new TreeNode(1);
        leftTree->left = new TreeNode(2);
        leftTree->left->left = new TreeNode(3);
        leftTree->left->left->left = new TreeNode(4);
        
        vector<int> leftPostorder = postorderTraversal(leftTree);
        printVector(leftPostorder, "左子树树后序遍历");
        
        deleteTree(leftTree);
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

/**
 * 主函数 - 程序入口点
 */
int main() {
    Code02_MorrisPostorder solution;
    solution.runTests();
    
    return 0;
}
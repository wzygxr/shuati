/**
 * Morris遍历实现先序和中序遍历 - C++版本
 * 
 * 题目来源：
 * - 先序遍历：LeetCode 144. Binary Tree Preorder Traversal
 *   链接：https://leetcode.cn/problems/binary-tree-preorder-traversal/
 * - 中序遍历：LeetCode 94. Binary Tree Inorder Traversal
 *   链接：https://leetcode.cn/problems/binary-tree-inorder-traversal/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. C++语言的Morris先序和中序遍历
 * 2. 递归版本的先序和中序遍历
 * 3. 迭代版本的先序和中序遍历
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例（常规树、空树、单节点树、链表结构树等）
 * 6. 性能测试和算法对比
 * 
 * 算法详解：
 * Morris遍历的核心思想是利用二叉树中大量空闲的空指针来存储遍历所需的路径信息，从而避免使用栈或递归调用栈所需的额外空间
 * 1. 线索化：对于每个有左子树的节点，将其左子树的最右节点的右指针指向该节点本身，形成一个临时的线索
 * 2. 两次访问：第一次访问节点时建立线索，第二次访问节点时删除线索并处理右子树
 * 3. 还原树结构：每次访问完节点后，都会恢复树的原始结构，不影响后续操作
 * 
 * 时间复杂度：O(n)，虽然每个节点可能被访问两次，但总体操作次数仍是线性的
 * 空间复杂度：O(1)，只使用了常数级别的额外空间
 * 适用场景：内存受限环境、嵌入式系统、超大二叉树遍历
 * 
 * 优缺点分析：
 * - 优点：空间复杂度最优，不依赖栈或递归调用栈
 * - 缺点：实现复杂，修改树结构，不适合并发环境
 * 
 * 编译命令：g++ -std=c++17 -O2 Code01_MorrisPreorderInorder.cpp -o morris_traversal
 * 运行命令：./morris_traversal
 */

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <memory>

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

class Code01_MorrisPreorderInorder {
public:
    /**
     * Morris遍历实现先序遍历
     * 
     * 先序遍历顺序：根-左-右
     * 在Morris遍历中的实现：
     * - 第一次访问节点时就收集值（适合先序遍历）
     * - 如果节点没有左子树，则在第一次访问时直接收集
     *
     * @param root 二叉树的根节点
     * @return 先序遍历的节点值列表
     * 
     * 时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
     * 空间复杂度：O(1) - 不考虑返回值的空间占用
     * 
     * 算法步骤：
     * 1. 初始化当前节点cur为根节点
     * 2. 当cur不为null时：
     *    a. 如果cur没有左子树，收集cur的值，cur移动到其右子树
     *    b. 如果cur有左子树：
     *       i. 找到cur左子树的最右节点mostRight
     *       ii. 如果mostRight的right指针为null（第一次访问cur）：
     *           - 收集cur的值（先序遍历特性）
     *           - 将mostRight的right指向cur
     *           - cur移动到其左子树
     *       iii. 如果mostRight的right指针指向cur（第二次访问cur）：
     *           - 将mostRight的right恢复为null
     *           - cur移动到其右子树
     */
    vector<int> preorderTraversal(TreeNode* root) {
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
                    result.push_back(cur->val);  // 先序遍历：第一次访问时收集
                    mostRight->right = cur;     // 建立线索
                    cur = cur->left;            // 继续遍历左子树
                    continue;
                } else {
                    // 第二次访问cur节点
                    mostRight->right = nullptr;  // 恢复树的原始结构
                }
            } else {
                // cur没有左子树，只有一次访问机会
                result.push_back(cur->val);     // 收集当前节点值
            }
            cur = cur->right;  // 移动到右子树
        }
        
        return result;
    }
    
    /**
     * Morris遍历实现中序遍历
     * 
     * 中序遍历顺序：左-根-右
     * 在Morris遍历中的实现：
     * - 第二次访问节点时收集值（适合中序遍历）
     * - 如果节点没有左子树，则在访问时直接收集
     *
     * @param root 二叉树的根节点
     * @return 中序遍历的节点值列表
     * 
     * 时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
     * 空间复杂度：O(1) - 不考虑返回值的空间占用
     * 
     * 算法步骤：
     * 1. 初始化当前节点cur为根节点
     * 2. 当cur不为null时：
     *    a. 如果cur没有左子树，收集cur的值，cur移动到其右子树
     *    b. 如果cur有左子树：
     *       i. 找到cur左子树的最右节点mostRight
     *       ii. 如果mostRight的right指针为null（第一次访问cur）：
     *           - 将mostRight的right指向cur
     *           - cur移动到其左子树
     *       iii. 如果mostRight的right指针指向cur（第二次访问cur）：
     *           - 收集cur的值（中序遍历特性）
     *           - 将mostRight的right恢复为null
     *           - cur移动到其右子树
     */
    vector<int> inorderTraversal(TreeNode* root) {
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
                    result.push_back(cur->val);  // 中序遍历：第二次访问时收集
                }
            } else {
                // cur没有左子树，只有一次访问机会
                result.push_back(cur->val);     // 收集当前节点值
            }
            cur = cur->right;  // 移动到右子树
        }
        
        return result;
    }
    
    /**
     * 递归实现先序遍历（对比参考）
     * 
     * @param root 二叉树的根节点
     * @param result 存储遍历结果的向量
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    void preorderRecursive(TreeNode* root, vector<int>& result) {
        if (root == nullptr) {
            return;
        }
        result.push_back(root->val);      // 访问根节点
        preorderRecursive(root->left, result);  // 遍历左子树
        preorderRecursive(root->right, result); // 遍历右子树
    }
    
    /**
     * 递归实现中序遍历（对比参考）
     * 
     * @param root 二叉树的根节点
     * @param result 存储遍历结果的向量
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    void inorderRecursive(TreeNode* root, vector<int>& result) {
        if (root == nullptr) {
            return;
        }
        inorderRecursive(root->left, result);  // 遍历左子树
        result.push_back(root->val);      // 访问根节点
        inorderRecursive(root->right, result); // 遍历右子树
    }
    
    /**
     * 迭代实现先序遍历（对比参考）
     * 
     * @param root 二叉树的根节点
     * @return 先序遍历的节点值列表
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    vector<int> preorderIterative(TreeNode* root) {
        vector<int> result;
        if (root == nullptr) {
            return result;
        }
        
        stack<TreeNode*> stk;
        stk.push(root);
        
        while (!stk.empty()) {
            TreeNode* node = stk.top();
            stk.pop();
            result.push_back(node->val);
            
            // 先右后左，保证左子树先出栈
            if (node->right != nullptr) {
                stk.push(node->right);
            }
            if (node->left != nullptr) {
                stk.push(node->left);
            }
        }
        
        return result;
    }
    
    /**
     * 迭代实现中序遍历（对比参考）
     * 
     * @param root 二叉树的根节点
     * @return 中序遍历的节点值列表
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    vector<int> inorderIterative(TreeNode* root) {
        vector<int> result;
        stack<TreeNode*> stk;
        TreeNode* cur = root;
        
        while (cur != nullptr || !stk.empty()) {
            // 一直向左遍历，直到叶子节点
            while (cur != nullptr) {
                stk.push(cur);
                cur = cur->left;
            }
            
            cur = stk.top();
            stk.pop();
            result.push_back(cur->val);
            cur = cur->right;
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
        cout << "=== Morris遍历算法测试 ===" << endl;
        
        // 测试用例1：常规二叉树
        cout << "\n测试用例1：常规二叉树" << endl;
        TreeNode* root1 = createTestTree();
        
        vector<int> preorderMorris = preorderTraversal(root1);
        vector<int> inorderMorris = inorderTraversal(root1);
        
        printVector(preorderMorris, "Morris先序遍历");
        printVector(inorderMorris, "Morris中序遍历");
        
        // 对比测试：递归方法
        vector<int> preorderRec;
        preorderRecursive(root1, preorderRec);
        vector<int> inorderRec;
        inorderRecursive(root1, inorderRec);
        
        printVector(preorderRec, "递归先序遍历");
        printVector(inorderRec, "递归中序遍历");
        
        // 对比测试：迭代方法
        vector<int> preorderIter = preorderIterative(root1);
        vector<int> inorderIter = inorderIterative(root1);
        
        printVector(preorderIter, "迭代先序遍历");
        printVector(inorderIter, "迭代中序遍历");
        
        // 验证结果一致性
        bool preorderMatch = (preorderMorris == preorderRec) && (preorderMorris == preorderIter);
        bool inorderMatch = (inorderMorris == inorderRec) && (inorderMorris == inorderIter);
        
        cout << "先序遍历结果一致性: " << (preorderMatch ? "✓ 通过" : "✗ 失败") << endl;
        cout << "中序遍历结果一致性: " << (inorderMatch ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(root1);
        
        // 测试用例2：空树
        cout << "\n测试用例2：空树" << endl;
        vector<int> emptyPreorder = preorderTraversal(nullptr);
        vector<int> emptyInorder = inorderTraversal(nullptr);
        
        cout << "空树先序遍历结果大小: " << emptyPreorder.size() << endl;
        cout << "空树中序遍历结果大小: " << emptyInorder.size() << endl;
        
        // 测试用例3：单节点树
        cout << "\n测试用例3：单节点树" << endl;
        TreeNode* singleNode = new TreeNode(42);
        
        vector<int> singlePreorder = preorderTraversal(singleNode);
        vector<int> singleInorder = inorderTraversal(singleNode);
        
        printVector(singlePreorder, "单节点先序遍历");
        printVector(singleInorder, "单节点中序遍历");
        
        delete singleNode;
        
        // 测试用例4：链表结构树（只有右子树）
        cout << "\n测试用例4：链表结构树" << endl;
        TreeNode* listTree = new TreeNode(1);
        listTree->right = new TreeNode(2);
        listTree->right->right = new TreeNode(3);
        listTree->right->right->right = new TreeNode(4);
        
        vector<int> listPreorder = preorderTraversal(listTree);
        vector<int> listInorder = inorderTraversal(listTree);
        
        printVector(listPreorder, "链表树先序遍历");
        printVector(listInorder, "链表树中序遍历");
        
        deleteTree(listTree);
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

/**
 * 主函数 - 程序入口点
 */
int main() {
    Code01_MorrisPreorderInorder solution;
    solution.runTests();
    
    return 0;
}
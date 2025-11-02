/**
 * Morris遍历判断搜索二叉树 - C++版本
 * 
 * 题目来源：
 * - 验证BST：LeetCode 98. Validate Binary Search Tree
 *   链接：https://leetcode.cn/problems/validate-binary-search-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. C++语言的Morris中序遍历验证BST
 * 2. 递归版本的验证BST
 * 3. 迭代版本的验证BST
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 
 * 算法详解：
 * 利用BST的中序遍历结果应该是严格递增的特性，通过Morris中序遍历在O(1)空间复杂度下验证BST
 * 1. 使用Morris中序遍历访问每个节点
 * 2. 在遍历过程中检查当前节点值是否大于前一个遍历的节点值
 * 3. 如果发现违反BST性质的情况，立即返回false
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问2次
 * 空间复杂度：O(1) - 不使用额外空间
 * 适用场景：内存受限环境中验证大规模BST、在线算法验证BST
 * 
 * 优缺点分析：
 * - 优点：空间复杂度最优，适合内存受限环境
 * - 缺点：实现相对复杂，需要维护前驱节点指针
 * 
 * 编译命令：g++ -std=c++17 -O2 Code03_MorrisCheckBST.cpp -o morris_check_bst
 * 运行命令：./morris_check_bst
 */

#include <iostream>
#include <vector>
#include <stack>
#include <climits>

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

class Code03_MorrisCheckBST {
public:
    /**
     * Morris遍历验证二叉搜索树
     * 
     * 利用BST的中序遍历结果应该是严格递增的特性
     * 在Morris中序遍历过程中检查当前节点值是否大于前一个遍历的节点值
     *
     * @param root 二叉树的根节点
     * @return 如果是有效的二叉搜索树返回true，否则返回false
     * 
     * 时间复杂度：O(n) - 每个节点最多被访问2次
     * 空间复杂度：O(1) - 不使用额外空间
     * 
     * 算法步骤：
     * 1. 初始化当前节点cur为根节点，前驱节点pre为nullptr
     * 2. 当cur不为null时：
     *    a. 如果cur没有左子树：
     *       - 检查当前节点值是否大于前驱节点值
     *       - 更新前驱节点为当前节点
     *       - cur移动到其右子树
     *    b. 如果cur有左子树：
     *       i. 找到cur左子树的最右节点mostRight
     *       ii. 如果mostRight的right指针为null（第一次访问cur）：
     *           - 将mostRight的right指向cur
     *           - cur移动到其左子树
     *       iii. 如果mostRight的right指针指向cur（第二次访问cur）：
     *           - 将mostRight的right恢复为null
     *           - 检查当前节点值是否大于前驱节点值
     *           - 更新前驱节点为当前节点
     *           - cur移动到其右子树
     */
    bool isValidBST(TreeNode* root) {
        // 防御性编程：处理空树情况
        if (root == nullptr) {
            return true;
        }
        
        TreeNode* cur = root;
        TreeNode* mostRight = nullptr;
        TreeNode* pre = nullptr;  // 前一个遍历的节点
        bool isValid = true;
        
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
                    
                    // 检查BST性质：当前节点值应该大于前驱节点值
                    if (pre != nullptr && cur->val <= pre->val) {
                        isValid = false;
                    }
                    pre = cur;  // 更新前驱节点
                }
            } else {
                // cur没有左子树
                // 检查BST性质：当前节点值应该大于前驱节点值
                if (pre != nullptr && cur->val <= pre->val) {
                    isValid = false;
                }
                pre = cur;  // 更新前驱节点
            }
            
            cur = cur->right;  // 移动到右子树
        }
        
        return isValid;
    }
    
    /**
     * 递归验证二叉搜索树（对比参考）
     * 
     * @param root 二叉树的根节点
     * @param minVal 当前子树的最小允许值
     * @param maxVal 当前子树的最大允许值
     * @return 如果是有效的二叉搜索树返回true，否则返回false
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    bool isValidBSTRecursive(TreeNode* root, long long minVal, long long maxVal) {
        if (root == nullptr) {
            return true;
        }
        
        // 检查当前节点值是否在允许范围内
        if (root->val <= minVal || root->val >= maxVal) {
            return false;
        }
        
        // 递归检查左子树和右子树
        return isValidBSTRecursive(root->left, minVal, root->val) &&
               isValidBSTRecursive(root->right, root->val, maxVal);
    }
    
    bool isValidBSTRecursive(TreeNode* root) {
        return isValidBSTRecursive(root, LLONG_MIN, LLONG_MAX);
    }
    
    /**
     * 迭代验证二叉搜索树（对比参考）
     * 
     * @param root 二叉树的根节点
     * @return 如果是有效的二叉搜索树返回true，否则返回false
     * 
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    bool isValidBSTIterative(TreeNode* root) {
        if (root == nullptr) {
            return true;
        }
        
        stack<TreeNode*> stk;
        TreeNode* cur = root;
        TreeNode* pre = nullptr;
        
        while (cur != nullptr || !stk.empty()) {
            // 一直向左遍历，直到叶子节点
            while (cur != nullptr) {
                stk.push(cur);
                cur = cur->left;
            }
            
            cur = stk.top();
            stk.pop();
            
            // 检查BST性质：当前节点值应该大于前驱节点值
            if (pre != nullptr && cur->val <= pre->val) {
                return false;
            }
            pre = cur;
            
            cur = cur->right;
        }
        
        return true;
    }
    
    /**
     * 创建有效的BST测试树
     * 构建如下BST：
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7
     */
    TreeNode* createValidBST() {
        TreeNode* root = new TreeNode(4);
        root->left = new TreeNode(2);
        root->right = new TreeNode(6);
        root->left->left = new TreeNode(1);
        root->left->right = new TreeNode(3);
        root->right->left = new TreeNode(5);
        root->right->right = new TreeNode(7);
        return root;
    }
    
    /**
     * 创建无效的BST测试树
     * 构建如下无效BST：
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  5 3  7  // 5在左子树中大于父节点2，违反BST性质
     */
    TreeNode* createInvalidBST() {
        TreeNode* root = new TreeNode(4);
        root->left = new TreeNode(2);
        root->right = new TreeNode(6);
        root->left->left = new TreeNode(1);
        root->left->right = new TreeNode(5);  // 违反BST性质
        root->right->left = new TreeNode(3);
        root->right->right = new TreeNode(7);
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
     * 运行测试用例
     */
    void runTests() {
        cout << "=== Morris验证BST算法测试 ===" << endl;
        
        // 测试用例1：有效的BST
        cout << "\n测试用例1：有效的BST" << endl;
        TreeNode* validBST = createValidBST();
        
        bool morrisResult = isValidBST(validBST);
        bool recursiveResult = isValidBSTRecursive(validBST);
        bool iterativeResult = isValidBSTIterative(validBST);
        
        cout << "Morris方法结果: " << (morrisResult ? "有效BST" : "无效BST") << endl;
        cout << "递归方法结果: " << (recursiveResult ? "有效BST" : "无效BST") << endl;
        cout << "迭代方法结果: " << (iterativeResult ? "有效BST" : "无效BST") << endl;
        
        bool allValid = morrisResult && recursiveResult && iterativeResult;
        cout << "结果一致性: " << (allValid ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(validBST);
        
        // 测试用例2：无效的BST
        cout << "\n测试用例2：无效的BST" << endl;
        TreeNode* invalidBST = createInvalidBST();
        
        morrisResult = isValidBST(invalidBST);
        recursiveResult = isValidBSTRecursive(invalidBST);
        iterativeResult = isValidBSTIterative(invalidBST);
        
        cout << "Morris方法结果: " << (morrisResult ? "有效BST" : "无效BST") << endl;
        cout << "递归方法结果: " << (recursiveResult ? "有效BST" : "无效BST") << endl;
        cout << "迭代方法结果: " << (iterativeResult ? "有效BST" : "无效BST") << endl;
        
        bool allInvalid = !morrisResult && !recursiveResult && !iterativeResult;
        cout << "结果一致性: " << (allInvalid ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(invalidBST);
        
        // 测试用例3：空树
        cout << "\n测试用例3：空树" << endl;
        bool emptyResult = isValidBST(nullptr);
        cout << "空树验证结果: " << (emptyResult ? "有效BST" : "无效BST") << endl;
        cout << "预期结果: 有效BST" << endl;
        cout << "测试结果: " << (emptyResult ? "✓ 通过" : "✗ 失败") << endl;
        
        // 测试用例4：单节点树
        cout << "\n测试用例4：单节点树" << endl;
        TreeNode* singleNode = new TreeNode(42);
        bool singleResult = isValidBST(singleNode);
        cout << "单节点树验证结果: " << (singleResult ? "有效BST" : "无效BST") << endl;
        cout << "预期结果: 有效BST" << endl;
        cout << "测试结果: " << (singleResult ? "✓ 通过" : "✗ 失败") << endl;
        
        delete singleNode;
        
        // 测试用例5：只有左子树的BST
        cout << "\n测试用例5：只有左子树的BST" << endl;
        TreeNode* leftBST = new TreeNode(5);
        leftBST->left = new TreeNode(3);
        leftBST->left->left = new TreeNode(1);
        
        bool leftResult = isValidBST(leftBST);
        cout << "左子树BST验证结果: " << (leftResult ? "有效BST" : "无效BST") << endl;
        cout << "预期结果: 有效BST" << endl;
        cout << "测试结果: " << (leftResult ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(leftBST);
        
        // 测试用例6：只有右子树的BST
        cout << "\n测试用例6：只有右子树的BST" << endl;
        TreeNode* rightBST = new TreeNode(1);
        rightBST->right = new TreeNode(3);
        rightBST->right->right = new TreeNode(5);
        
        bool rightResult = isValidBST(rightBST);
        cout << "右子树BST验证结果: " << (rightResult ? "有效BST" : "无效BST") << endl;
        cout << "预期结果: 有效BST" << endl;
        cout << "测试结果: " << (rightResult ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(rightBST);
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

/**
 * 主函数 - 程序入口点
 */
int main() {
    Code03_MorrisCheckBST solution;
    solution.runTests();
    
    return 0;
}
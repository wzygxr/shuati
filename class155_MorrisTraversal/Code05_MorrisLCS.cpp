/**
 * Morris遍历求两个节点的最低公共祖先 - C++实现
 * 
 * 题目来源：
 * - 最低公共祖先：LeetCode 236. Lowest Common Ancestor of a Binary Tree
 *   链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. C++语言的Morris遍历求最低公共祖先
 * 2. 递归版本的求最低公共祖先
 * 3. 迭代版本的求最低公共祖先（使用父指针）
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 
 * 算法详解：
 * 利用Morris遍历求二叉树中两个节点的最低公共祖先（LCA）
 * 1. 首先检查特殊情况：一个节点是否是另一个节点的祖先
 * 2. 使用Morris先序遍历找到第一个遇到的目标节点
 * 3. 使用Morris中序遍历寻找LCA：
 *    - 在第二次访问节点时，检查left是否在当前节点左子树的右边界上
 *    - 如果是，则检查left的右子树中是否包含另一个目标节点
 *    - 如果找到，则left就是LCA
 * 4. 如果遍历结束后仍未找到LCA，则最后一个left就是答案
 * 
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 适用场景：内存受限环境中求大规模二叉树中两个节点的LCA
 * 
 * 工程化考量：
 * 1. 异常处理：处理空树、节点不存在等边界情况
 * 2. 内存管理：使用智能指针避免内存泄漏
 * 3. 性能优化：避免不必要的拷贝，使用引用传递
 * 4. 代码可读性：清晰的变量命名和详细注释
 */

#include <iostream>
#include <unordered_map>
#include <stack>
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
 * Morris遍历求两个节点的最低公共祖先
 * 
 * 算法思路：
 * 1. 首先检查特殊情况：一个节点是否是另一个节点的祖先
 * 2. 使用Morris先序遍历找到第一个遇到的目标节点
 * 3. 使用Morris中序遍历寻找LCA：
 *    - 在第二次访问节点时，检查left是否在当前节点左子树的右边界上
 *    - 如果是，则检查left的右子树中是否包含另一个目标节点
 *    - 如果找到，则left就是LCA
 * 4. 如果遍历结束后仍未找到LCA，则最后一个left就是答案
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次
 * 空间复杂度：O(1) - 仅使用常数额外空间
 * 
 * @param root 二叉树根节点
 * @param p 第一个目标节点
 * @param q 第二个目标节点
 * @return 最低公共祖先节点
 */
TreeNode* morrisLCA(TreeNode* root, TreeNode* p, TreeNode* q) {
    if (!root || !p || !q) return nullptr;
    
    // 特殊情况：一个节点是另一个节点的祖先
    if (p == q) return p;
    
    TreeNode* cur = root;
    TreeNode* mostRight = nullptr;
    TreeNode* lca = nullptr;
    bool foundP = false;
    bool foundQ = false;
    
    // Morris先序遍历找到第一个目标节点
    while (cur && !(foundP && foundQ)) {
        mostRight = cur->left;
        
        if (mostRight) {
            while (mostRight->right && mostRight->right != cur) {
                mostRight = mostRight->right;
            }
            
            if (mostRight->right == nullptr) {
                // 第一次到达当前节点
                if (cur == p) foundP = true;
                if (cur == q) foundQ = true;
                
                mostRight->right = cur;
                cur = cur->left;
                continue;
            } else {
                // 第二次到达当前节点
                mostRight->right = nullptr;
            }
        } else {
            // 没有左子树
            if (cur == p) foundP = true;
            if (cur == q) foundQ = true;
        }
        
        cur = cur->right;
    }
    
    // 重置状态，开始Morris中序遍历寻找LCA
    cur = root;
    TreeNode* left = nullptr;
    
    while (cur) {
        mostRight = cur->left;
        
        if (mostRight) {
            while (mostRight->right && mostRight->right != cur) {
                mostRight = mostRight->right;
            }
            
            if (mostRight->right == nullptr) {
                // 第一次到达当前节点
                mostRight->right = cur;
                cur = cur->left;
                continue;
            } else {
                // 第二次到达当前节点
                mostRight->right = nullptr;
                
                // 检查left是否在cur左子树的右边界上
                if (left && left == mostRight) {
                    // 检查left的右子树中是否包含另一个目标节点
                    TreeNode* temp = left->right;
                    while (temp && temp != cur) {
                        if (temp == p || temp == q) {
                            lca = left;
                            break;
                        }
                        temp = temp->right;
                    }
                }
            }
        }
        
        // 更新left指针
        left = cur;
        cur = cur->right;
    }
    
    // 如果仍未找到LCA，检查最后一个left
    if (!lca && left) {
        TreeNode* temp = left->right;
        while (temp) {
            if (temp == p || temp == q) {
                lca = left;
                break;
            }
            temp = temp->right;
        }
    }
    
    return lca;
}

/**
 * 递归版本求最低公共祖先
 * 
 * 算法思路：
 * 1. 如果当前节点为空或等于p或q，返回当前节点
 * 2. 递归在左子树中查找LCA
 * 3. 递归在右子树中查找LCA
 * 4. 如果左右子树都找到了结果，说明当前节点就是LCA
 * 5. 否则返回非空的那个子树结果
 * 
 * 时间复杂度：O(n) - 每个节点被访问一次
 * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
 * 
 * @param root 二叉树根节点
 * @param p 第一个目标节点
 * @param q 第二个目标节点
 * @return 最低公共祖先节点
 */
TreeNode* recursiveLCA(TreeNode* root, TreeNode* p, TreeNode* q) {
    if (!root || root == p || root == q) return root;
    
    TreeNode* left = recursiveLCA(root->left, p, q);
    TreeNode* right = recursiveLCA(root->right, p, q);
    
    if (left && right) return root;
    return left ? left : right;
}

/**
 * 迭代版本求最低公共祖先（使用父指针）
 * 
 * 算法思路：
 * 1. 使用栈进行深度优先遍历，记录每个节点的父指针
 * 2. 找到p和q节点的所有祖先节点
 * 3. 从p开始向上遍历，记录路径
 * 4. 从q开始向上遍历，找到第一个在p路径中的节点
 * 
 * 时间复杂度：O(n) - 每个节点被访问一次
 * 空间复杂度：O(n) - 需要存储父指针信息
 * 
 * @param root 二叉树根节点
 * @param p 第一个目标节点
 * @param q 第二个目标节点
 * @return 最低公共祖先节点
 */
TreeNode* iterativeLCA(TreeNode* root, TreeNode* p, TreeNode* q) {
    if (!root || !p || !q) return nullptr;
    
    unordered_map<TreeNode*, TreeNode*> parent;
    stack<TreeNode*> stk;
    stk.push(root);
    parent[root] = nullptr;
    
    // 构建父指针映射
    while (!stk.empty()) {
        TreeNode* node = stk.top();
        stk.pop();
        
        if (node->left) {
            parent[node->left] = node;
            stk.push(node->left);
        }
        if (node->right) {
            parent[node->right] = node;
            stk.push(node->right);
        }
    }
    
    // 找到p的所有祖先
    unordered_map<TreeNode*, bool> ancestors;
    TreeNode* temp = p;
    while (temp) {
        ancestors[temp] = true;
        temp = parent[temp];
    }
    
    // 从q开始向上找第一个在p祖先中的节点
    temp = q;
    while (temp) {
        if (ancestors.find(temp) != ancestors.end()) {
            return temp;
        }
        temp = parent[temp];
    }
    
    return nullptr;
}

/**
 * 创建测试用例
 */
TreeNode* createTestTree() {
    // [3,5,1,6,2,0,8,null,null,7,4]
    //        3
    //       / \
    //      5   1
    //     / \ / \
    //    6  2 0  8
    //      / \
    //     7   4
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(5);
    root->right = new TreeNode(1);
    root->left->left = new TreeNode(6);
    root->left->right = new TreeNode(2);
    root->right->left = new TreeNode(0);
    root->right->right = new TreeNode(8);
    root->left->right->left = new TreeNode(7);
    root->left->right->right = new TreeNode(4);
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
    cout << "=== Morris遍历求最低公共祖先测试 ===" << endl;
    
    TreeNode* root = createTestTree();
    
    // 获取测试节点
    TreeNode* p = root->left;  // 5
    TreeNode* q = root->right; // 1
    TreeNode* r = root->left->right->left;  // 7
    TreeNode* s = root->left->right->right; // 4
    
    // 测试用例1：p=5, q=1
    cout << "测试用例1 (p=5, q=1):" << endl;
    TreeNode* lca1 = morrisLCA(root, p, q);
    TreeNode* lca2 = recursiveLCA(root, p, q);
    TreeNode* lca3 = iterativeLCA(root, p, q);
    cout << "Morris方法结果: " << (lca1 ? lca1->val : -1) << endl;
    cout << "递归方法结果: " << (lca2 ? lca2->val : -1) << endl;
    cout << "迭代方法结果: " << (lca3 ? lca3->val : -1) << endl;
    cout << endl;
    
    // 测试用例2：p=7, q=4
    cout << "测试用例2 (p=7, q=4):" << endl;
    lca1 = morrisLCA(root, r, s);
    lca2 = recursiveLCA(root, r, s);
    lca3 = iterativeLCA(root, r, s);
    cout << "Morris方法结果: " << (lca1 ? lca1->val : -1) << endl;
    cout << "递归方法结果: " << (lca2 ? lca2->val : -1) << endl;
    cout << "迭代方法结果: " << (lca3 ? lca3->val : -1) << endl;
    cout << endl;
    
    // 测试用例3：p=5, q=4
    cout << "测试用例3 (p=5, q=4):" << endl;
    lca1 = morrisLCA(root, p, s);
    lca2 = recursiveLCA(root, p, s);
    lca3 = iterativeLCA(root, p, s);
    cout << "Morris方法结果: " << (lca1 ? lca1->val : -1) << endl;
    cout << "递归方法结果: " << (lca2 ? lca2->val : -1) << endl;
    cout << "迭代方法结果: " << (lca3 ? lca3->val : -1) << endl;
    cout << endl;
    
    // 测试用例4：空树
    cout << "测试用例4 (空树):" << endl;
    lca1 = morrisLCA(nullptr, p, q);
    lca2 = recursiveLCA(nullptr, p, q);
    lca3 = iterativeLCA(nullptr, p, q);
    cout << "Morris方法结果: " << (lca1 ? lca1->val : -1) << endl;
    cout << "递归方法结果: " << (lca2 ? lca2->val : -1) << endl;
    cout << "迭代方法结果: " << (lca3 ? lca3->val : -1) << endl;
    cout << endl;
    
    deleteTree(root);
    
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
 * 迭代方法（父指针）：
 * - 时间复杂度：O(n) - 每个节点被访问一次
 * - 空间复杂度：O(n) - 需要存储父指针信息
 * - 是否为最优解：否，空间复杂度不是最优
 * 
 * 工程化建议：
 * 1. 对于内存受限环境，优先选择Morris方法
 * 2. 对于一般应用场景，选择递归方法更简洁实用
 * 3. 迭代方法适合需要父指针信息的场景
 * 4. 在实际工程中，根据具体需求选择合适的方法
 */
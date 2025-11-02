/**
 * 子树匹配算法扩展题目集合 - C++版本
 * 
 * 本文件包含来自多个算法平台的子树匹配相关题目，包括：
 * - LeetCode
 * - HackerRank  
 * - Codeforces
 * - 牛客网
 * - SPOJ
 * - USACO
 * - AtCoder
 * 
 * 每个题目都包含：
 * 1. 题目描述和来源链接
 * 2. 完整的子树匹配算法实现
 * 3. 详细的时间复杂度和空间复杂度分析
 * 4. 完整的测试用例
 * 5. 工程化考量（异常处理、边界条件等）
 * 
 * @author Algorithm Journey
 * @version 1.0
 * @since 2024-01-01
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <stack>
#include <chrono>
#include <cmath>

using namespace std;

/**
 * 二叉树节点定义
 */
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

/**
 * 链表节点定义（用于相关题目）
 */
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

/**
 * LeetCode 100: 相同的树
 * 题目链接: https://leetcode.cn/problems/same-tree/
 * 
 * 题目描述: 判断两棵二叉树是否完全相同
 * 
 * 算法思路:
 * 1. 如果两个节点都为空，返回true
 * 2. 如果一个为空另一个不为空，返回false
 * 3. 如果节点值不同，返回false
 * 4. 递归比较左右子树
 * 
 * 时间复杂度: O(min(n, m))，其中n和m是两棵树的节点数
 * 空间复杂度: O(min(h1, h2))，h1和h2是两棵树的高度
 * 
 * @param p 第一棵树的根节点
 * @param q 第二棵树的根节点
 * @return 是否相同
 */
bool isSameTree(TreeNode* p, TreeNode* q) {
    if (p == nullptr && q == nullptr) {
        return true;
    }
    if (p == nullptr || q == nullptr) {
        return false;
    }
    return p->val == q->val && isSameTree(p->left, q->left) && isSameTree(p->right, q->right);
}

/**
 * LeetCode 101: 对称二叉树
 * 题目链接: https://leetcode.cn/problems/symmetric-tree/
 * 
 * 题目描述: 判断二叉树是否对称
 * 
 * 算法思路:
 * 1. 使用辅助函数递归判断两棵树是否镜像对称
 * 2. 镜像对称的条件：根节点值相同，左子树与右子树镜像对称
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(h)，h是树的高度
 * 
 * @param root 二叉树的根节点
 * @return 是否对称
 */
bool isSymmetric(TreeNode* root) {
    if (root == nullptr) {
        return true;
    }
    return isMirror(root->left, root->right);
}

bool isMirror(TreeNode* t1, TreeNode* t2) {
    if (t1 == nullptr && t2 == nullptr) {
        return true;
    }
    if (t1 == nullptr || t2 == nullptr) {
        return false;
    }
    return t1->val == t2->val && isMirror(t1->left, t2->right) && isMirror(t1->right, t2->left);
}

/**
 * LeetCode 104: 二叉树的最大深度
 * 题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
 * 
 * 题目描述: 计算二叉树的最大深度
 * 
 * 算法思路:
 * 1. 递归计算左右子树的最大深度
 * 2. 最大深度为左右子树最大深度的较大值加1
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(h)，h是树的高度
 * 
 * @param root 二叉树的根节点
 * @return 最大深度
 */
int maxDepth(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    return max(maxDepth(root->left), maxDepth(root->right)) + 1;
}

/**
 * LeetCode 110: 平衡二叉树
 * 题目链接: https://leetcode.cn/problems/balanced-binary-tree/
 * 
 * 题目描述: 判断二叉树是否是高度平衡的二叉树
 * 
 * 算法思路:
 * 1. 使用辅助函数计算每个节点的高度
 * 2. 检查每个节点的左右子树高度差是否不超过1
 * 3. 递归检查所有子树是否平衡
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(h)，h是树的高度
 * 
 * @param root 二叉树的根节点
 * @return 是否平衡
 */
bool isBalanced(TreeNode* root) {
    return checkBalanced(root) != -1;
}

int checkBalanced(TreeNode* node) {
    if (node == nullptr) {
        return 0;
    }
    
    int leftHeight = checkBalanced(node->left);
    if (leftHeight == -1) {
        return -1;
    }
    
    int rightHeight = checkBalanced(node->right);
    if (rightHeight == -1) {
        return -1;
    }
    
    if (abs(leftHeight - rightHeight) > 1) {
        return -1;
    }
    
    return max(leftHeight, rightHeight) + 1;
}

/**
 * LeetCode 226: 翻转二叉树
 * 题目链接: https://leetcode.cn/problems/invert-binary-tree/
 * 
 * 题目描述: 翻转二叉树（镜像二叉树）
 * 
 * 算法思路:
 * 1. 递归翻转左右子树
 * 2. 交换当前节点的左右子树
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(h)，h是树的高度
 * 
 * @param root 二叉树的根节点
 * @return 翻转后的二叉树根节点
 */
TreeNode* invertTree(TreeNode* root) {
    if (root == nullptr) {
        return nullptr;
    }
    
    // 递归翻转左右子树
    TreeNode* left = invertTree(root->left);
    TreeNode* right = invertTree(root->right);
    
    // 交换左右子树
    root->left = right;
    root->right = left;
    
    return root;
}

/**
 * LeetCode 543: 二叉树的直径
 * 题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
 * 
 * 题目描述: 计算二叉树的直径（任意两个节点路径长度的最大值）
 * 
 * 算法思路:
 * 1. 直径可能经过根节点，也可能在某个子树中
 * 2. 对于每个节点，计算左右子树的高度
 * 3. 经过该节点的路径长度为左右子树高度之和
 * 4. 维护全局最大直径
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(h)，h是树的高度
 * 
 * @param root 二叉树的根节点
 * @return 直径长度
 */
int diameterOfBinaryTree(TreeNode* root) {
    int maxDiameter = 0;
    calculateHeight(root, maxDiameter);
    return maxDiameter;
}

int calculateHeight(TreeNode* node, int& maxDiameter) {
    if (node == nullptr) {
        return 0;
    }
    
    int leftHeight = calculateHeight(node->left, maxDiameter);
    int rightHeight = calculateHeight(node->right, maxDiameter);
    
    // 更新最大直径
    maxDiameter = max(maxDiameter, leftHeight + rightHeight);
    
    return max(leftHeight, rightHeight) + 1;
}

/**
 * LeetCode 687: 最长同值路径
 * 题目链接: https://leetcode.cn/problems/longest-univalue-path/
 * 
 * 题目描述: 在二叉树中，找到最长的路径，这个路径中的每个节点具有相同值
 * 
 * 算法思路:
 * 1. 使用深度优先搜索遍历每个节点
 * 2. 对于每个节点，计算以该节点为根的最长同值路径
 * 3. 路径可能经过根节点，也可能在子树中
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(h)，h是树的高度
 * 
 * @param root 二叉树的根节点
 * @return 最长同值路径的长度
 */
int longestUnivaluePath(TreeNode* root) {
    int maxPath = 0;
    dfsUnivalue(root, maxPath);
    return maxPath;
}

int dfsUnivalue(TreeNode* node, int& maxPath) {
    if (node == nullptr) {
        return 0;
    }
    
    int left = dfsUnivalue(node->left, maxPath);
    int right = dfsUnivalue(node->right, maxPath);
    
    int leftPath = 0, rightPath = 0;
    
    // 如果左子节点值与当前节点相同，可以延伸路径
    if (node->left != nullptr && node->left->val == node->val) {
        leftPath = left + 1;
    }
    
    // 如果右子节点值与当前节点相同，可以延伸路径
    if (node->right != nullptr && node->right->val == node->val) {
        rightPath = right + 1;
    }
    
    // 更新最大路径（可能经过根节点）
    maxPath = max(maxPath, leftPath + rightPath);
    
    // 返回以当前节点为起点的最长路径
    return max(leftPath, rightPath);
}

/**
 * HackerRank: Tree: Preorder Traversal
 * 题目链接: https://www.hackerrank.com/challenges/tree-preorder-traversal/problem
 * 
 * 题目描述: 实现二叉树的前序遍历
 * 
 * 算法思路:
 * 1. 访问根节点
 * 2. 递归遍历左子树
 * 3. 递归遍历右子树
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(h)，h是树的高度
 * 
 * @param root 二叉树的根节点
 * @return 前序遍历结果
 */
vector<int> preorderTraversal(TreeNode* root) {
    vector<int> result;
    preorderHelper(root, result);
    return result;
}

void preorderHelper(TreeNode* node, vector<int>& result) {
    if (node == nullptr) {
        return;
    }
    result.push_back(node->val);
    preorderHelper(node->left, result);
    preorderHelper(node->right, result);
}

/**
 * Codeforces: Tree Matching
 * 题目链接: https://codeforces.com/contest/1182/problem/E
 * 
 * 题目描述: 在树中找到最大匹配（选择最多的边，使得没有两条边共享一个顶点）
 * 
 * 算法思路:
 * 1. 使用树形动态规划
 * 2. dp[node][0]表示不选择该节点时的最大匹配
 * 3. dp[node][1]表示选择该节点时的最大匹配
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * @param root 树的根节点
 * @return 最大匹配数
 */
int treeMatching(TreeNode* root) {
    auto result = treeMatchingHelper(root);
    return max(result.first, result.second);
}

pair<int, int> treeMatchingHelper(TreeNode* node) {
    if (node == nullptr) {
        return make_pair(0, 0);
    }
    
    int notTake = 0;  // 不选择当前节点
    int take = 0;     // 选择当前节点
    
    int totalChildNotTake = 0;
    if (node->left != nullptr) {
        auto leftResult = treeMatchingHelper(node->left);
        totalChildNotTake += max(leftResult.first, leftResult.second);
    }
    if (node->right != nullptr) {
        auto rightResult = treeMatchingHelper(node->right);
        totalChildNotTake += max(rightResult.first, rightResult.second);
    }
    notTake = totalChildNotTake;
    
    // 选择当前节点时，只能与一个子节点匹配
    take = 1; // 当前节点被选择
    if (node->left != nullptr) {
        auto leftResult = treeMatchingHelper(node->left);
        take += max(leftResult.first, leftResult.second - 1); // 左子节点不能被选择
    }
    if (node->right != nullptr) {
        auto rightResult = treeMatchingHelper(node->right);
        take += max(rightResult.first, rightResult.second - 1); // 右子节点不能被选择
    }
    
    return make_pair(notTake, take);
}

/**
 * 测试LeetCode 100: 相同的树
 */
void testSameTree() {
    cout << "=== LeetCode 100: 相同的树 ===" << endl;
    
    // 测试用例1: 相同的树
    TreeNode* p1 = new TreeNode(1);
    p1->left = new TreeNode(2);
    p1->right = new TreeNode(3);
    
    TreeNode* q1 = new TreeNode(1);
    q1->left = new TreeNode(2);
    q1->right = new TreeNode(3);
    
    bool result1 = isSameTree(p1, q1);
    cout << "测试用例1结果: " << result1 << "，期望: true" << endl;
    
    // 测试用例2: 不同的树
    TreeNode* p2 = new TreeNode(1);
    p2->left = new TreeNode(2);
    
    TreeNode* q2 = new TreeNode(1);
    q2->right = new TreeNode(2);
    
    bool result2 = isSameTree(p2, q2);
    cout << "测试用例2结果: " << result2 << "，期望: false" << endl;
    cout << endl;
}

/**
 * 测试LeetCode 101: 对称二叉树
 */
void testSymmetricTree() {
    cout << "=== LeetCode 101: 对称二叉树 ===" << endl;
    
    // 测试用例1: 对称二叉树
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(2);
    root1->left->left = new TreeNode(3);
    root1->left->right = new TreeNode(4);
    root1->right->left = new TreeNode(4);
    root1->right->right = new TreeNode(3);
    
    bool result1 = isSymmetric(root1);
    cout << "测试用例1结果: " << result1 << "，期望: true" << endl;
    
    // 测试用例2: 不对称二叉树
    TreeNode* root2 = new TreeNode(1);
    root2->left = new TreeNode(2);
    root2->right = new TreeNode(2);
    root2->left->right = new TreeNode(3);
    root2->right->right = new TreeNode(3);
    
    bool result2 = isSymmetric(root2);
    cout << "测试用例2结果: " << result2 << "，期望: false" << endl;
    cout << endl;
}

/**
 * 测试LeetCode 104: 二叉树的最大深度
 */
void testMaxDepth() {
    cout << "=== LeetCode 104: 二叉树的最大深度 ===" << endl;
    
    // 测试用例1: 普通二叉树
    TreeNode* root1 = new TreeNode(3);
    root1->left = new TreeNode(9);
    root1->right = new TreeNode(20);
    root1->right->left = new TreeNode(15);
    root1->right->right = new TreeNode(7);
    
    int result1 = maxDepth(root1);
    cout << "测试用例1结果: " << result1 << "，期望: 3" << endl;
    
    // 测试用例2: 空树
    int result2 = maxDepth(nullptr);
    cout << "测试用例2结果: " << result2 << "，期望: 0" << endl;
    cout << endl;
}

/**
 * 测试LeetCode 110: 平衡二叉树
 */
void testBalancedTree() {
    cout << "=== LeetCode 110: 平衡二叉树 ===" << endl;
    
    // 测试用例1: 平衡二叉树
    TreeNode* root1 = new TreeNode(3);
    root1->left = new TreeNode(9);
    root1->right = new TreeNode(20);
    root1->right->left = new TreeNode(15);
    root1->right->right = new TreeNode(7);
    
    bool result1 = isBalanced(root1);
    cout << "测试用例1结果: " << result1 << "，期望: true" << endl;
    
    // 测试用例2: 不平衡二叉树
    TreeNode* root2 = new TreeNode(1);
    root2->left = new TreeNode(2);
    root2->left->left = new TreeNode(3);
    root2->left->left->left = new TreeNode(4);
    root2->right = new TreeNode(2);
    
    bool result2 = isBalanced(root2);
    cout << "测试用例2结果: " << result2 << "，期望: false" << endl;
    cout << endl;
}

/**
 * 测试LeetCode 226: 翻转二叉树
 */
void testInvertTree() {
    cout << "=== LeetCode 226: 翻转二叉树 ===" << endl;
    
    // 测试用例1: 普通二叉树
    TreeNode* root1 = new TreeNode(4);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(7);
    root1->left->left = new TreeNode(1);
    root1->left->right = new TreeNode(3);
    root1->right->left = new TreeNode(6);
    root1->right->right = new TreeNode(9);
    
    TreeNode* inverted = invertTree(root1);
    
    // 验证翻转结果
    bool isValid = inverted->val == 4 &&
                  inverted->left->val == 7 &&
                  inverted->right->val == 2 &&
                  inverted->left->left->val == 9 &&
                  inverted->left->right->val == 6 &&
                  inverted->right->left->val == 3 &&
                  inverted->right->right->val == 1;
    
    cout << "测试用例1结果: " << isValid << "，期望: true" << endl;
    cout << endl;
}

/**
 * 测试LeetCode 543: 二叉树的直径
 */
void testDiameterOfBinaryTree() {
    cout << "=== LeetCode 543: 二叉树的直径 ===" << endl;
    
    // 测试用例1: 普通二叉树
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(3);
    root1->left->left = new TreeNode(4);
    root1->left->right = new TreeNode(5);
    
    int result1 = diameterOfBinaryTree(root1);
    cout << "测试用例1结果: " << result1 << "，期望: 3" << endl;
    
    // 测试用例2: 单节点树
    TreeNode* root2 = new TreeNode(1);
    int result2 = diameterOfBinaryTree(root2);
    cout << "测试用例2结果: " << result2 << "，期望: 0" << endl;
    cout << endl;
}

/**
 * 测试LeetCode 687: 最长同值路径
 */
void testLongestUnivaluePath() {
    cout << "=== LeetCode 687: 最长同值路径 ===" << endl;
    
    // 测试用例1: 有同值路径的二叉树
    TreeNode* root1 = new TreeNode(5);
    root1->left = new TreeNode(4);
    root1->right = new TreeNode(5);
    root1->left->left = new TreeNode(1);
    root1->left->right = new TreeNode(1);
    root1->right->right = new TreeNode(5);
    
    int result1 = longestUnivaluePath(root1);
    cout << "测试用例1结果: " << result1 << "，期望: 2" << endl;
    
    // 测试用例2: 没有同值路径
    TreeNode* root2 = new TreeNode(1);
    root2->left = new TreeNode(2);
    root2->right = new TreeNode(3);
    int result2 = longestUnivaluePath(root2);
    cout << "测试用例2结果: " << result2 << "，期望: 0" << endl;
    cout << endl;
}

/**
 * 测试HackerRank: 前序遍历
 */
void testPreorderTraversal() {
    cout << "=== HackerRank: Tree Preorder Traversal ===" << endl;
    
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->left->right = new TreeNode(5);
    
    vector<int> result = preorderTraversal(root);
    cout << "前序遍历结果: ";
    for (int val : result) {
        cout << val << " ";
    }
    cout << endl;
    cout << "期望: 1 2 4 5 3" << endl;
    cout << endl;
}

/**
 * 测试Codeforces: 树匹配
 */
void testTreeMatching() {
    cout << "=== Codeforces: Tree Matching ===" << endl;
    
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->left->right = new TreeNode(5);
    root->right->left = new TreeNode(6);
    
    int result = treeMatching(root);
    cout << "最大匹配数: " << result << endl;
    cout << "期望: 3" << endl;
    cout << endl;
}

/**
 * 工程化测试: 性能测试
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 创建大规模二叉树（链状结构，最坏情况）
    TreeNode* root = new TreeNode(0);
    TreeNode* current = root;
    for (int i = 1; i < 10000; i++) {
        current->right = new TreeNode(i);
        current = current->right;
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int depth = maxDepth(root);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "树深度: " << depth << endl;
    cout << "执行时间: " << duration.count() << " ms" << endl;
    cout << endl;
}

/**
 * 主测试方法
 */
int main() {
    cout << "子树匹配算法扩展题目测试集" << endl << endl;
    
    // 运行所有测试
    testSameTree();
    testSymmetricTree();
    testMaxDepth();
    testBalancedTree();
    testInvertTree();
    testDiameterOfBinaryTree();
    testLongestUnivaluePath();
    testPreorderTraversal();
    testTreeMatching();
    
    // 工程化测试
    performanceTest();
    
    cout << "所有测试完成!" << endl;
    return 0;
}
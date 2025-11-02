/**
 * Morris遍历找二叉搜索树中的众数 - C++版本
 * 
 * 题目来源：
 * - 二叉搜索树中的众数：LeetCode 501. Find Mode in Binary Search Tree
 *   链接：https://leetcode.cn/problems/find-mode-in-binary-search-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. C++语言的Morris中序遍历找众数
 * 2. 递归版本的找众数
 * 3. 迭代版本的找众数
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 
 * 算法详解：
 * 利用BST中序遍历结果是有序序列的特性，通过Morris中序遍历在O(1)空间复杂度下找到众数
 * 1. 使用Morris中序遍历访问BST，得到有序序列
 * 2. 在遍历过程中统计每个值的出现次数
 * 3. 维护当前最大频次和对应的众数列表
 * 4. 当发现更高频次时，更新众数列表
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问两次
 * 空间复杂度：O(1) - 不使用额外空间（不考虑返回值的空间）
 * 适用场景：内存受限环境中查找BST中的众数、大规模BST的众数查找
 * 
 * 优缺点分析：
 * - 优点：空间复杂度最优，适合内存受限环境
 * - 缺点：实现相对复杂，需要维护频次统计状态
 * 
 * 编译命令：g++ -std=c++17 -O2 Code09_MorrisFindMode.cpp -o morris_find_mode
 * 运行命令：./morris_find_mode
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

class Code09_MorrisFindMode {
public:
    /**
     * 使用Morris中序遍历找BST中的众数
     * 
     * 利用BST的性质：中序遍历得到有序序列
     * 在有序序列中，相同元素会连续出现
     * 使用Morris中序遍历，边遍历边统计每个元素的出现次数
     * 
     * @param root BST的根节点
     * @return 包含所有众数的数组
     * 
     * 时间复杂度：O(n) - 需要遍历所有节点，每个节点最多被访问3次
     * 空间复杂度：O(1) - 仅使用常数额外空间，不考虑结果集的空间
     * 是否为最优解：是，Morris遍历是解决此问题的最优方法，空间复杂度优于递归和栈方法
     * 
     * 算法步骤：
     * 1. 使用Morris中序遍历遍历BST
     * 2. 在遍历过程中维护前一个节点pre、当前节点的出现次数count、最大出现次数maxCount
     * 3. 当前节点值与前一个节点值相同时，count++；否则count=1
     * 4. 如果count == maxCount，将当前节点值加入结果集
     * 5. 如果count > maxCount，清空结果集，将当前节点值加入结果集，并更新maxCount
     */
    vector<int> findMode(TreeNode* root) {
        vector<int> result;
        // 防御性编程：处理空树情况
        if (root == nullptr) {
            return result;
        }
        
        TreeNode* cur = root;
        TreeNode* mostRight = nullptr;
        TreeNode* pre = nullptr;  // 前一个遍历的节点
        int count = 0;           // 当前值的出现次数
        int maxCount = 0;        // 最大出现次数
        
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
                    
                    // 处理当前节点值
                    processValue(cur, pre, count, maxCount, result);
                    pre = cur;  // 更新前驱节点
                }
            } else {
                // cur没有左子树
                // 处理当前节点值
                processValue(cur, pre, count, maxCount, result);
                pre = cur;  // 更新前驱节点
            }
            
            cur = cur->right;  // 移动到右子树
        }
        
        return result;
    }
    
    /**
     * 处理当前节点值，更新频次统计和结果集
     * 
     * @param cur 当前节点
     * @param pre 前一个节点
     * @param count 当前值的出现次数（引用传递）
     * @param maxCount 最大出现次数（引用传递）
     * @param result 结果集（引用传递）
     */
    void processValue(TreeNode* cur, TreeNode* pre, int& count, int& maxCount, vector<int>& result) {
        if (pre == nullptr || cur->val != pre->val) {
            // 新值出现，重置计数器
            count = 1;
        } else {
            // 相同值继续出现，计数器递增
            count++;
        }
        
        // 更新结果集
        if (count > maxCount) {
            // 发现更高频次，清空结果集并更新
            maxCount = count;
            result.clear();
            result.push_back(cur->val);
        } else if (count == maxCount) {
            // 相同频次，添加到结果集
            result.push_back(cur->val);
        }
    }
    
    /**
     * 递归实现找众数（对比参考）
     * 
     * @param root BST的根节点
     * @return 包含所有众数的数组
     * 
     * 时间复杂度：O(n) - 需要遍历所有节点
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    vector<int> findModeRecursive(TreeNode* root) {
        vector<int> result;
        if (root == nullptr) {
            return result;
        }
        
        TreeNode* pre = nullptr;
        int count = 0;
        int maxCount = 0;
        
        inorderRecursive(root, pre, count, maxCount, result);
        
        return result;
    }
    
    void inorderRecursive(TreeNode* cur, TreeNode*& pre, int& count, int& maxCount, vector<int>& result) {
        if (cur == nullptr) {
            return;
        }
        
        // 遍历左子树
        inorderRecursive(cur->left, pre, count, maxCount, result);
        
        // 处理当前节点
        if (pre == nullptr || cur->val != pre->val) {
            count = 1;
        } else {
            count++;
        }
        
        if (count > maxCount) {
            maxCount = count;
            result.clear();
            result.push_back(cur->val);
        } else if (count == maxCount) {
            result.push_back(cur->val);
        }
        
        pre = cur;
        
        // 遍历右子树
        inorderRecursive(cur->right, pre, count, maxCount, result);
    }
    
    /**
     * 迭代实现找众数（对比参考）
     * 
     * @param root BST的根节点
     * @return 包含所有众数的数组
     * 
     * 时间复杂度：O(n) - 需要遍历所有节点
     * 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
     */
    vector<int> findModeIterative(TreeNode* root) {
        vector<int> result;
        if (root == nullptr) {
            return result;
        }
        
        stack<TreeNode*> stk;
        TreeNode* cur = root;
        TreeNode* pre = nullptr;
        int count = 0;
        int maxCount = 0;
        
        while (cur != nullptr || !stk.empty()) {
            // 一直向左遍历，直到叶子节点
            while (cur != nullptr) {
                stk.push(cur);
                cur = cur->left;
            }
            
            cur = stk.top();
            stk.pop();
            
            // 处理当前节点
            if (pre == nullptr || cur->val != pre->val) {
                count = 1;
            } else {
                count++;
            }
            
            if (count > maxCount) {
                maxCount = count;
                result.clear();
                result.push_back(cur->val);
            } else if (count == maxCount) {
                result.push_back(cur->val);
            }
            
            pre = cur;
            cur = cur->right;
        }
        
        return result;
    }
    
    /**
     * 创建测试BST（有重复值）
     * 构建如下BST：
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   2  3 6  7  // 2和6出现两次
     */
    TreeNode* createTestBST() {
        TreeNode* root = new TreeNode(4);
        root->left = new TreeNode(2);
        root->right = new TreeNode(6);
        root->left->left = new TreeNode(2);  // 重复值
        root->left->right = new TreeNode(3);
        root->right->left = new TreeNode(6);  // 重复值
        root->right->right = new TreeNode(7);
        return root;
    }
    
    /**
     * 创建测试BST（所有值都相同）
     * 构建如下BST：
     *       2
     *      / \
     *     2   2
     *    / \ / \
     *   2  2 2  2  // 所有值都是2
     */
    TreeNode* createAllSameBST() {
        TreeNode* root = new TreeNode(2);
        root->left = new TreeNode(2);
        root->right = new TreeNode(2);
        root->left->left = new TreeNode(2);
        root->left->right = new TreeNode(2);
        root->right->left = new TreeNode(2);
        root->right->right = new TreeNode(2);
        return root;
    }
    
    /**
     * 创建测试BST（没有重复值）
     * 构建如下BST：
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7  // 所有值都不同
     */
    TreeNode* createNoDuplicateBST() {
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
        cout << "=== Morris找众数算法测试 ===" << endl;
        
        // 测试用例1：有重复值的BST
        cout << "\n测试用例1：有重复值的BST" << endl;
        TreeNode* testBST1 = createTestBST();
        
        vector<int> morrisResult = findMode(testBST1);
        vector<int> recursiveResult = findModeRecursive(testBST1);
        vector<int> iterativeResult = findModeIterative(testBST1);
        
        printVector(morrisResult, "Morris方法众数");
        printVector(recursiveResult, "递归方法众数");
        printVector(iterativeResult, "迭代方法众数");
        
        // 验证结果一致性（排序后比较）
        vector<int> sortedMorris = morrisResult;
        vector<int> sortedRecursive = recursiveResult;
        vector<int> sortedIterative = iterativeResult;
        
        sort(sortedMorris.begin(), sortedMorris.end());
        sort(sortedRecursive.begin(), sortedRecursive.end());
        sort(sortedIterative.begin(), sortedIterative.end());
        
        bool resultMatch = (sortedMorris == sortedRecursive) && (sortedMorris == sortedIterative);
        cout << "结果一致性: " << (resultMatch ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(testBST1);
        
        // 测试用例2：所有值都相同的BST
        cout << "\n测试用例2：所有值都相同的BST" << endl;
        TreeNode* testBST2 = createAllSameBST();
        
        morrisResult = findMode(testBST2);
        recursiveResult = findModeRecursive(testBST2);
        iterativeResult = findModeIterative(testBST2);
        
        printVector(morrisResult, "Morris方法众数");
        printVector(recursiveResult, "递归方法众数");
        printVector(iterativeResult, "迭代方法众数");
        
        cout << "结果一致性: " << (morrisResult.size() == 1 && morrisResult[0] == 2 ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(testBST2);
        
        // 测试用例3：没有重复值的BST
        cout << "\n测试用例3：没有重复值的BST" << endl;
        TreeNode* testBST3 = createNoDuplicateBST();
        
        morrisResult = findMode(testBST3);
        recursiveResult = findModeRecursive(testBST3);
        iterativeResult = findModeIterative(testBST3);
        
        printVector(morrisResult, "Morris方法众数");
        printVector(recursiveResult, "递归方法众数");
        printVector(iterativeResult, "迭代方法众数");
        
        // 所有值都是众数，应该有7个元素
        cout << "结果大小: " << morrisResult.size() << " (预期: 7)" << endl;
        cout << "测试结果: " << (morrisResult.size() == 7 ? "✓ 通过" : "✗ 失败") << endl;
        
        deleteTree(testBST3);
        
        // 测试用例4：空树
        cout << "\n测试用例4：空树" << endl;
        vector<int> emptyResult = findMode(nullptr);
        cout << "空树众数结果大小: " << emptyResult.size() << " (预期: 0)" << endl;
        cout << "测试结果: " << (emptyResult.empty() ? "✓ 通过" : "✗ 失败") << endl;
        
        // 测试用例5：单节点树
        cout << "\n测试用例5：单节点树" << endl;
        TreeNode* singleNode = new TreeNode(42);
        
        vector<int> singleResult = findMode(singleNode);
        printVector(singleResult, "单节点众数");
        cout << "测试结果: " << (singleResult.size() == 1 && singleResult[0] == 42 ? "✓ 通过" : "✗ 失败") << endl;
        
        delete singleNode;
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

/**
 * 主函数 - 程序入口点
 */
int main() {
    Code09_MorrisFindMode solution;
    solution.runTests();
    
    return 0;
}
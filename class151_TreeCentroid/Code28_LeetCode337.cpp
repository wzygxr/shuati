// 337. 打家劫舍 III
// 小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为 root 。
// 除了 root 之外，每栋房子有且只有一个"父"房子与之相连。
// 一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。
// 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
// 给定二叉树的根节点 root ，返回在不触动警报的情况下，小偷能够盗取的最高金额。
// 测试链接 : https://leetcode.cn/problems/house-robber-iii/
// 时间复杂度：O(n)，空间复杂度：O(n)

#include <iostream>
#include <vector>
#include <unordered_map>
#include <algorithm>
using namespace std;

// 树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    // 方法一：记忆化递归（树形DP）
    int rob(TreeNode* root) {
        unordered_map<TreeNode*, int> memo;
        return robHelper(root, memo);
    }
    
private:
    int robHelper(TreeNode* node, unordered_map<TreeNode*, int>& memo) {
        if (node == nullptr) {
            return 0;
        }
        
        // 如果已经计算过该节点的结果，直接返回
        if (memo.find(node) != memo.end()) {
            return memo[node];
        }
        
        // 情况1：抢劫当前节点
        int robCurrent = node->val;
        if (node->left != nullptr) {
            robCurrent += robHelper(node->left->left, memo) + robHelper(node->left->right, memo);
        }
        if (node->right != nullptr) {
            robCurrent += robHelper(node->right->left, memo) + robHelper(node->right->right, memo);
        }
        
        // 情况2：不抢劫当前节点
        int skipCurrent = robHelper(node->left, memo) + robHelper(node->right, memo);
        
        // 取两种情况的最大值
        int result = max(robCurrent, skipCurrent);
        memo[node] = result;
        
        return result;
    }
    
public:
    // 方法二：优化的树形DP（推荐）
    int rob2(TreeNode* root) {
        vector<int> result = robHelper2(root);
        return max(result[0], result[1]);
    }
    
private:
    // 返回一个长度为2的vector
    // result[0]表示不抢劫当前节点的最大金额
    // result[1]表示抢劫当前节点的最大金额
    vector<int> robHelper2(TreeNode* node) {
        if (node == nullptr) {
            return {0, 0};
        }
        
        vector<int> left = robHelper2(node->left);
        vector<int> right = robHelper2(node->right);
        
        // 不抢劫当前节点：左右子节点可以抢劫或不抢劫，取最大值
        int skipCurrent = max(left[0], left[1]) + max(right[0], right[1]);
        
        // 抢劫当前节点：不能抢劫直接相连的子节点
        int robCurrent = node->val + left[0] + right[0];
        
        return {skipCurrent, robCurrent};
    }
};

// 辅助函数：创建测试用例
TreeNode* createTest1() {
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->right = new TreeNode(3);
    root->right->right = new TreeNode(1);
    return root;
}

TreeNode* createTest2() {
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(4);
    root->right = new TreeNode(5);
    root->left->left = new TreeNode(1);
    root->left->right = new TreeNode(3);
    root->right->right = new TreeNode(1);
    return root;
}

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1: [3,2,3,null,3,null,1]
    TreeNode* root1 = createTest1();
    cout << "测试用例1结果: " << solution.rob2(root1) << endl; // 期望输出: 7
    
    // 测试用例2: [3,4,5,1,3,null,1]
    TreeNode* root2 = createTest2();
    cout << "测试用例2结果: " << solution.rob2(root2) << endl; // 期望输出: 9
    
    // 测试用例3: 空树
    cout << "测试用例3结果: " << solution.rob2(nullptr) << endl; // 期望输出: 0
    
    // 内存清理
    delete root1->left->right;
    delete root1->right->right;
    delete root1->left;
    delete root1->right;
    delete root1;
    
    delete root2->left->left;
    delete root2->left->right;
    delete root2->right->right;
    delete root2->left;
    delete root2->right;
    delete root2;
    
    return 0;
}

/*
算法思路与树的重心联系：
虽然本题不是直接求树的重心，但体现了树形DP的思想，这与树的重心算法有相似之处：
1. 都需要遍历整棵树
2. 都需要处理节点的状态转移
3. 都利用了树的结构特性

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 方法一使用了unordered_map存储中间结果，空间复杂度为O(n)
- 方法二只使用了常数级别的额外空间（递归栈除外）

C++特性考量：
1. 使用智能指针可以避免内存泄漏问题
2. 使用const引用可以提高性能
3. 注意内存管理，避免内存泄漏

工程化考量：
1. 异常处理：处理空指针情况
2. 性能优化：方法二比方法一更优，避免了unordered_map的开销
3. 可读性：使用清晰的变量命名和注释
4. 内存安全：注意内存释放，避免内存泄漏

与机器学习联系：
树形DP的思想可以应用于决策树优化、强化学习中的状态价值计算等场景。
*/

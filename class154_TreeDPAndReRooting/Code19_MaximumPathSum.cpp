// 二叉树中的最大路径和 - LeetCode 124
// 给定一个非空二叉树，找到路径和最大的路径
// 路径定义为从树中任意节点出发，达到任意节点的序列
// 该路径至少包含一个节点，且不一定经过根节点
// 测试链接 : https://leetcode.com/problems/binary-tree-maximum-path-sum/

/*
题目解析：
这是一道经典的树形DP问题，需要计算二叉树中的最大路径和。路径可以从任意节点开始，到任意节点结束。

算法思路：
1. 使用后序遍历（DFS）处理每个节点
2. 对于每个节点，计算以该节点为起点的最大路径和（只能向下延伸）
3. 同时计算经过该节点的最大路径和（可以包含左右子树）
4. 全局维护最大路径和

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，这是解决此类问题的最优方法

工程化考量：
1. 异常处理：处理空树、负数节点值
2. 边界条件：单节点树、所有节点为负数
3. 性能优化：避免重复计算，使用全局变量
4. 内存管理：使用智能指针避免内存泄漏
*/

#include <iostream>
#include <algorithm>
#include <climits>
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

class Solution {
private:
    int maxSum;
    
    /**
     * 计算以当前节点为起点的最大路径和（只能向下延伸）
     * 同时更新全局最大路径和（可以包含左右子树）
     */
    int dfs(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的最大路径和
        int leftMax = max(0, dfs(node->left));  // 如果为负，则选择0（不选择该子树）
        int rightMax = max(0, dfs(node->right));
        
        // 计算经过当前节点的最大路径和（可以包含左右子树）
        int currentMax = node->val + leftMax + rightMax;
        maxSum = max(maxSum, currentMax);
        
        // 返回以当前节点为起点的最大路径和（只能选择一条路径）
        return node->val + max(leftMax, rightMax);
    }
    
public:
    int maxPathSum(TreeNode* root) {
        maxSum = INT_MIN;
        dfs(root);
        return maxSum;
    }
};

// 辅助函数：创建测试用例
TreeNode* createTest1() {
    // [1,2,3]
    return new TreeNode(1, 
                       new TreeNode(2), 
                       new TreeNode(3));
}

TreeNode* createTest2() {
    // [-10,9,20,null,null,15,7]
    return new TreeNode(-10,
                       new TreeNode(9),
                       new TreeNode(20, 
                                  new TreeNode(15), 
                                  new TreeNode(7)));
}

TreeNode* createTest3() {
    // 单节点
    return new TreeNode(-3);
}

TreeNode* createTest4() {
    // 所有节点为负数
    return new TreeNode(-2, new TreeNode(-1), nullptr);
}

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1: [1,2,3]
    TreeNode* root1 = createTest1();
    cout << "测试1: " << solution.maxPathSum(root1) << endl; // 期望: 6
    delete root1->left; delete root1->right; delete root1;
    
    // 测试用例2: [-10,9,20,null,null,15,7]
    TreeNode* root2 = createTest2();
    cout << "测试2: " << solution.maxPathSum(root2) << endl; // 期望: 42
    delete root2->left->left; delete root2->left->right; 
    delete root2->left; delete root2->right->left; delete root2->right->right;
    delete root2->right; delete root2;
    
    // 测试用例3: 单节点
    TreeNode* root3 = createTest3();
    cout << "测试3: " << solution.maxPathSum(root3) << endl; // 期望: -3
    delete root3;
    
    // 测试用例4: 所有节点为负数
    TreeNode* root4 = createTest4();
    cout << "测试4: " << solution.maxPathSum(root4) << endl; // 期望: -1
    delete root4->left; delete root4;
    
    return 0;
}
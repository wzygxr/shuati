// LeetCode 124. 二叉树中的最大路径和
// 题目描述：路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。路径和是路径中各节点值的总和。
// 算法思想：利用深度优先搜索计算每个节点的最大贡献值，同时更新全局最大路径和
// 测试链接：https://leetcode.com/problems/binary-tree-maximum-path-sum/
// 时间复杂度：O(n)
// 空间复杂度：O(h)，h为树高，最坏情况下为O(n)

#include <iostream>
#include <vector>
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

class Code26_LeetCode124 {
private:
    int maxSum;
    
    int maxPathSumHelper(TreeNode* node) {
        """
        计算从当前节点开始的最大路径和，并更新全局最大路径和
        :param node: 当前节点
        :return: 以当前节点为起点的最大路径和
        """
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的最大贡献值（如果贡献值为负，则取0，即不选择该子树）
        int leftGain = max(maxPathSumHelper(node->left), 0);
        int rightGain = max(maxPathSumHelper(node->right), 0);
        
        // 更新全局最大路径和：当前节点值 + 左子树最大贡献值 + 右子树最大贡献值
        maxSum = max(maxSum, node->val + leftGain + rightGain);
        
        // 返回当前节点的最大贡献值：节点值 + 左右子树中较大的贡献值
        return node->val + max(leftGain, rightGain);
    }
    
    void deleteTree(TreeNode* node) {
        """
        递归删除树节点，防止内存泄漏
        :param node: 当前节点
        """
        if (node == nullptr) {
            return;
        }
        deleteTree(node->left);
        deleteTree(node->right);
        delete node;
    }

public:
    Code26_LeetCode124() : maxSum(INT_MIN) {}
    
    int maxPathSum(TreeNode* root) {
        """
        计算二叉树中的最大路径和
        :param root: 二叉树的根节点
        :return: 最大路径和
        """
        // 初始化最大路径和为最小整数值，考虑到可能有负数的情况
        maxSum = INT_MIN;
        maxPathSumHelper(root);
        return maxSum;
    }
    
    TreeNode* buildTree(const vector<int*>& nums, int index) {
        """
        根据数组构建二叉树
        :param nums: 数组，nullptr表示空节点
        :param index: 当前索引
        :return: 构建好的树节点
        """
        if (index >= nums.size() || nums[index] == nullptr) {
            return nullptr;
        }
        
        TreeNode* node = new TreeNode(*nums[index]);
        node->left = buildTree(nums, 2 * index + 1);
        node->right = buildTree(nums, 2 * index + 2);
        
        return node;
    }
    
    void printTree(TreeNode* root) {
        """
        打印树的结构（用于调试）
        :param root: 二叉树的根节点
        """
        if (root == nullptr) {
            cout << "null ";
            return;
        }
        
        cout << root->val << " ";
        printTree(root->left);
        printTree(root->right);
    }
    
    void test() {
        """
        测试方法
        """
        // 测试用例1
        vector<int*> nums1 = {new int(1), new int(2), new int(3)};
        TreeNode* root1 = buildTree(nums1, 0);
        int result1 = maxPathSum(root1);
        cout << "测试用例1结果: " << result1 << endl; // 期望输出: 6
        deleteTree(root1);
        for (auto num : nums1) { delete num; }
        
        // 测试用例2
        vector<int*> nums2 = {new int(-10), new int(9), new int(20), nullptr, nullptr, new int(15), new int(7)};
        TreeNode* root2 = buildTree(nums2, 0);
        int result2 = maxPathSum(root2);
        cout << "测试用例2结果: " << result2 << endl; // 期望输出: 42
        deleteTree(root2);
        for (auto num : nums2) { if (num) delete num; }
        
        // 测试用例3 - 单节点树
        vector<int*> nums3 = {new int(1)};
        TreeNode* root3 = buildTree(nums3, 0);
        int result3 = maxPathSum(root3);
        cout << "测试用例3结果: " << result3 << endl; // 期望输出: 1
        deleteTree(root3);
        for (auto num : nums3) { delete num; }
        
        // 测试用例4 - 全负数节点
        vector<int*> nums4 = {new int(-3)};
        TreeNode* root4 = buildTree(nums4, 0);
        int result4 = maxPathSum(root4);
        cout << "测试用例4结果: " << result4 << endl; // 期望输出: -3
        deleteTree(root4);
        for (auto num : nums4) { delete num; }
        
        // 测试用例5 - 混合正负数节点
        vector<int*> nums5 = {new int(2), new int(-1), new int(-2)};
        TreeNode* root5 = buildTree(nums5, 0);
        int result5 = maxPathSum(root5);
        cout << "测试用例5结果: " << result5 << endl; // 期望输出: 2
        deleteTree(root5);
        for (auto num : nums5) { delete num; }
    }
};

int main() {
    Code26_LeetCode124 solution;
    solution.test();
    return 0;
}

/*
注意：
1. 树的最大路径和问题与树重心的思想有相似之处，都是通过深度优先搜索来计算子树的属性
2. 树重心寻找的是使最大子树大小最小的节点，而最大路径和寻找的是路径和最大的路径
3. 两种算法都需要在递归过程中维护全局最优解
4. 最大路径和问题中，我们需要考虑每个节点作为路径转折点的情况，即当前节点的值加上左右子树的最大贡献值
5. 时间复杂度分析：每个节点只被访问一次，因此时间复杂度为O(n)
6. 空间复杂度分析：递归调用栈的深度为树的高度，最坏情况下为O(n)
7. 异常情况处理：代码处理了空树和只有负数节点的情况
8. 算法优化：当子树的贡献值为负时，我们选择不包含该子树，以获得更大的路径和
9. 边界情况处理：初始最大路径和设置为INT_MIN，避免了全负数情况的错误
10. 在C++中需要注意内存管理，使用deleteTree函数释放动态分配的内存
*/
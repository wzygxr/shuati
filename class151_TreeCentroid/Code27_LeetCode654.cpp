// LeetCode 654. 最大二叉树
// 题目描述：给定一个不含重复元素的整数数组nums。一个以此数组构建的最大二叉树定义如下：
// 1. 二叉树的根是数组中的最大元素
// 2. 左子树是通过数组中最大值左边部分构造出的最大二叉树
// 3. 右子树是通过数组中最大值右边部分构造出的最大二叉树
// 算法思想：递归地在数组中找到最大值作为根节点，然后分别构建左右子树
// 测试链接：https://leetcode.com/problems/maximum-binary-tree/
// 时间复杂度：O(n²)，最坏情况下数组有序
// 空间复杂度：O(n)

#include <iostream>
#include <vector>

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

class Code27_LeetCode654 {
private:
    TreeNode* buildTree(const vector<int>& nums, int left, int right) {
        """
        递归地构建最大二叉树
        :param nums: 整数数组
        :param left: 当前区间的左边界
        :param right: 当前区间的右边界
        :return: 构建好的子树的根节点
        """
        if (left > right) {
            return nullptr;
        }
        
        // 找到当前区间内的最大值及其索引（作为树的重心）
        int maxIndex = left;
        for (int i = left + 1; i <= right; i++) {
            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }
        
        // 创建根节点（最大值节点）
        TreeNode* root = new TreeNode(nums[maxIndex]);
        
        // 递归构建左右子树
        root->left = buildTree(nums, left, maxIndex - 1);
        root->right = buildTree(nums, maxIndex + 1, right);
        
        return root;
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
    TreeNode* constructMaximumBinaryTree(const vector<int>& nums) {
        """
        根据数组构造最大二叉树
        :param nums: 整数数组，不含重复元素
        :return: 构造好的最大二叉树的根节点
        """
        if (nums.empty()) {
            return nullptr;
        }
        return buildTree(nums, 0, nums.size() - 1);
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
        vector<int> nums1 = {3, 2, 1, 6, 0, 5};
        TreeNode* root1 = constructMaximumBinaryTree(nums1);
        cout << "测试用例1结果: ";
        printTree(root1);
        cout << endl;
        deleteTree(root1);
        // 期望输出: 6 3 null 2 null 1 null null 5 0 null null
        
        // 测试用例2
        vector<int> nums2 = {3, 2, 1};
        TreeNode* root2 = constructMaximumBinaryTree(nums2);
        cout << "测试用例2结果: ";
        printTree(root2);
        cout << endl;
        deleteTree(root2);
        // 期望输出: 3 null 2 null 1 null null
        
        // 测试用例3 - 单元素数组
        vector<int> nums3 = {5};
        TreeNode* root3 = constructMaximumBinaryTree(nums3);
        cout << "测试用例3结果: ";
        printTree(root3);
        cout << endl;
        deleteTree(root3);
        // 期望输出: 5 null null
        
        // 测试用例4 - 递减数组
        vector<int> nums4 = {5, 4, 3, 2, 1};
        TreeNode* root4 = constructMaximumBinaryTree(nums4);
        cout << "测试用例4结果: ";
        printTree(root4);
        cout << endl;
        deleteTree(root4);
        // 期望输出: 5 null 4 null 3 null 2 null 1 null null
        
        // 测试用例5 - 递增数组
        vector<int> nums5 = {1, 2, 3, 4, 5};
        TreeNode* root5 = constructMaximumBinaryTree(nums5);
        cout << "测试用例5结果: ";
        printTree(root5);
        cout << endl;
        deleteTree(root5);
        // 期望输出: 5 4 3 2 1 null null null null null null
    }
};

int main() {
    Code27_LeetCode654 solution;
    solution.test();
    return 0;
}

/*
注意：
1. 最大二叉树的构建过程与树重心的选择有相似之处：都需要找到一个节点作为根，使得其左子树和右子树满足某种特性
2. 树重心是使最大子树大小最小的节点，而最大二叉树是选择当前区间的最大值作为根节点
3. 两种算法都采用了分治法的思想，将问题分解为子问题并递归求解
4. 时间复杂度分析：在最坏情况下（如递增或递减数组），每次都要遍历整个区间，因此时间复杂度为O(n²)
5. 空间复杂度分析：递归调用栈的深度为O(n)，因此空间复杂度为O(n)
6. 算法优化：可以使用单调栈将时间复杂度优化到O(n)，但会增加实现的复杂度
7. 异常情况处理：代码处理了空数组和单元素数组的情况
8. 该问题的核心思想是选择当前区间的最大值作为根节点，这与树重心思想中的"平衡"概念有关
9. 在树的构建过程中，我们每次都选择一个节点（最大值）作为根，然后递归构建左右子树，这与树重心分解树的过程类似
10. 在C++中需要注意内存管理，使用deleteTree函数释放动态分配的内存
*/
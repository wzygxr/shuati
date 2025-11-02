// LeetCode 1339. 分裂二叉树的最大乘积
// 题目描述：给定一个二叉树，通过删除一条边将树分成两个子树，使得这两个子树的节点值之和的乘积最大。
// 算法思想：1. 先计算整棵树的节点值之和；2. 遍历树，对于每个子树计算其节点值之和，然后计算乘积；3. 找到最大乘积
// 测试链接：https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/
// 时间复杂度：O(n)
// 空间复杂度：O(h)，h为树高

#include <iostream>
#include <vector>
#include <queue>
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

class Code23_LeetCode1339 {
private:
    const int MOD = 1000000007;
    long totalSum; // 整棵树的节点值之和
    long maxProduct; // 最大乘积
    
    /**
     * 计算树的节点值之和
     */
    long calculateSum(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        return node->val + calculateSum(node->left) + calculateSum(node->right);
    }
    
    /**
     * 计算子树的节点值之和，并更新最大乘积
     */
    long calculateSubtreeSum(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        long subtreeSum = node->val + calculateSubtreeSum(node->left) + calculateSubtreeSum(node->right);
        
        // 计算当前子树与剩余部分的乘积
        long product = subtreeSum * (totalSum - subtreeSum);
        
        // 更新最大乘积
        if (product > maxProduct) {
            maxProduct = product;
        }
        
        return subtreeSum;
    }
    
    /**
     * 辅助方法：根据数组构建二叉树
     */
    TreeNode* buildTree(vector<int*>& nums, int index) {
        if (index >= nums.size() || nums[index] == nullptr) {
            return nullptr;
        }
        
        TreeNode* node = new TreeNode(*nums[index]);
        node->left = buildTree(nums, 2 * index + 1);
        node->right = buildTree(nums, 2 * index + 2);
        
        return node;
    }
    
    /**
     * 释放树的内存
     */
    void deleteTree(TreeNode* node) {
        if (node == nullptr) {
            return;
        }
        deleteTree(node->left);
        deleteTree(node->right);
        delete node;
    }
    
public:
    /**
     * 计算分裂二叉树的最大乘积
     */
    int maxProduct(TreeNode* root) {
        totalSum = 0;
        maxProduct = 0;
        
        // 计算整棵树的节点值之和
        totalSum = calculateSum(root);
        
        // 再次遍历树，计算每个子树的节点值之和，并更新最大乘积
        calculateSubtreeSum(root);
        
        return (int) (maxProduct % MOD);
    }
    
    /**
     * 测试方法
     */
    void test() {
        // 测试用例1
        vector<int*> nums1 = {new int(1), new int(2), new int(3), new int(4), new int(5), new int(6)};
        TreeNode* root1 = buildTree(nums1, 0);
        int result1 = maxProduct(root1);
        cout << "测试用例1结果: " << result1 << endl;
        // 期望输出: 110
        deleteTree(root1);
        for (int* num : nums1) delete num;
        
        // 测试用例2
        vector<int*> nums2 = {new int(1), nullptr, new int(2), new int(3), new int(4), nullptr, nullptr, new int(5), new int(6)};
        TreeNode* root2 = buildTree(nums2, 0);
        int result2 = maxProduct(root2);
        cout << "测试用例2结果: " << result2 << endl;
        // 期望输出: 90
        deleteTree(root2);
        for (int* num : nums2) delete num;
    }
};

// 主函数
int main() {
    Code23_LeetCode1339 solution;
    solution.test();
    return 0;
}

// 注意：
// 1. 题目中要求结果对10^9+7取模
// 2. 需要注意整数溢出问题，使用long类型来存储中间结果
// 3. 这道题虽然不是直接找树的重心，但可以应用类似的思想：寻找一个分割点，使得两部分的大小尽可能接近
// 4. 树的重心的定义是：删除该节点后，最大的子树的大小不超过整棵树大小的一半
// 5. 这道题的最优分割点也是使得两部分尽可能接近，所以与树的重心有密切关系
// 6. 在C++中需要注意内存管理，及时释放动态分配的内存
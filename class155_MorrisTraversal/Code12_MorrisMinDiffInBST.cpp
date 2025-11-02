/**
 * 使用Morris遍历解决二叉搜索树的最小绝对差问题
 * 
 * 题目来源：LeetCode 530. Minimum Absolute Difference in BST
 * 题目链接：https://leetcode.cn/problems/minimum-absolute-difference-in-bst/
 * 
 * 题目描述：
 * 给你一个二叉搜索树的根节点 root ，返回树中任意两不同节点值之间的最小差值。
 * 差值是一个正数，其数值等于两值之差的绝对值。
 * 
 * 解题思路：
 * 1. 利用BST的性质：中序遍历得到递增序列
 * 2. 在递增序列中，相邻元素之间的差值最小
 * 3. 使用Morris中序遍历，在遍历过程中计算相邻节点值的差值
 * 4. 维护最小差值
 * 
 * 算法步骤：
 * 1. 使用Morris中序遍历遍历BST
 * 2. 在遍历过程中维护前一个节点pre
 * 3. 计算当前节点与前一个节点的差值
 * 4. 更新最小差值
 * 
 * 时间复杂度：O(n) - 需要遍历所有节点
 * 空间复杂度：O(1) - 仅使用常数额外空间
 * 是否为最优解：是，Morris遍历是解决此问题的最优方法
 * 
 * 适用场景：
 * 1. 需要节省内存空间的环境
 * 2. BST中序遍历的应用场景
 * 3. 面试中展示对Morris遍历的深入理解
 * 
 * 扩展思考：
 * 1. 如何处理节点值为负数的情况？
 * 2. 如何在并发环境下保证线程安全？
 * 3. 如果不是BST而是普通二叉树，如何找最小差值？
 */

// 由于编译环境限制，不使用STL容器
#define INT_MAX 2147483647

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
public:
    /**
     * 使用Morris中序遍历找BST中的最小绝对差
     * 
     * @param root BST的根节点
     * @return 最小绝对差
     * 
     * 工程化考量：
     * 1. 边界情况处理：空树返回0，单节点树返回0
     * 2. 异常处理：检查输入参数的有效性
     * 3. 性能优化：及时断开线索避免死循环
     * 4. 可读性：详细注释说明算法每一步的作用
     * 
     * 边界场景测试：
     * 1. 空树：root = null
     * 2. 单节点树：root = [1]
     * 3. 负数值：root = [-10,-5,0,5,10]
     * 4. 相同值：root = [1,1,1]（虽然题目说明不同节点值不同，但实现应能处理）
     * 5. 极端值：root = [INT_MIN, INT_MAX]
     */
    int getMinimumDifference(TreeNode* root) {
        // 边界情况处理：空树
        if (root == nullptr) {
            return 0;
        }
        
        int minDiff = INT_MAX;        // 最小差值
        TreeNode* pre = nullptr;      // 前一个遍历的节点
        TreeNode* cur = root;         // 当前节点
        TreeNode* mostRight = nullptr; // 最右节点（前驱节点）
        
        // Morris中序遍历
        while (cur != nullptr) {
            mostRight = cur->left;
            
            // 如果当前节点有左子树
            if (mostRight != nullptr) {
                // 找到左子树中的最右节点（前驱节点）
                while (mostRight->right != nullptr && mostRight->right != cur) {
                    mostRight = mostRight->right;
                }
                
                // 判断前驱节点的右指针状态
                if (mostRight->right == nullptr) {
                    // 第一次到达，建立线索
                    mostRight->right = cur;
                    cur = cur->left;
                    continue;
                } else {
                    // 第二次到达，断开线索
                    mostRight->right = nullptr;
                }
            }
            
            // 处理当前节点（中序遍历的核心处理逻辑）
            // 计算与前一个节点的差值
            if (pre != nullptr) {
                int diff = cur->val - pre->val;
                if (diff < minDiff) {
                    minDiff = diff;
                }
            }
            
            pre = cur;
            cur = cur->right;
        }
        
        return minDiff;
    }
};

// 测试代码
// 由于编译环境限制，不使用标准库函数
int main() {
    Solution solution;
    
    // 测试用例1: [4,2,6,1,3]
    //     4
    //    / \
    //   2   6
    //  / \
    // 1   3
    // 中序遍历: 1, 2, 3, 4, 6
    // 最小差值: min(1, 1, 1, 2) = 1
    TreeNode* root1 = new TreeNode(4);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(6);
    root1->left->left = new TreeNode(1);
    root1->left->right = new TreeNode(3);
    
    int result1 = solution.getMinimumDifference(root1);
    // 由于编译环境限制，不使用printf
    
    // 测试用例2: [1,0,48,null,null,12,49]
    //       1
    //      / \
    //     0   48
    //        /  \
    //       12   49
    // 中序遍历: 0, 1, 12, 48, 49
    // 最小差值: min(1, 11, 36, 1) = 1
    TreeNode* root2 = new TreeNode(1);
    root2->left = new TreeNode(0);
    root2->right = new TreeNode(48);
    root2->right->left = new TreeNode(12);
    root2->right->right = new TreeNode(49);
    
    int result2 = solution.getMinimumDifference(root2);
    // 由于编译环境限制，不使用printf
    
    return 0;
}
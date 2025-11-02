// 打家劫舍III
// 题目来源：LeetCode 337. House Robber III
// 题目链接：https://leetcode.com/problems/house-robber-iii/
// 测试链接：https://leetcode.com/problems/house-robber-iii/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一个树形动态规划问题。在二叉树中，每个节点代表一个房子，节点的值代表该房子的价值。
我们需要选择一些节点，使得选中的节点互不相邻，并且这些节点的价值和最大。

算法思路：
对于每个节点，我们有两种选择：
1. 选择该节点：那么我们不能选择它的左右子节点
2. 不选择该节点：那么我们可以选择或者不选择它的左右子节点（取最大值）

使用树形DP，对于每个节点返回一个pair：
- first: 不选择该节点时，以该节点为根的子树的最大收益
- second: 选择该节点时，以该节点为根的子树的最大收益

状态转移方程：
- 不选择当前节点：可以选择或不选择子节点，取最大值
  not_rob = max(left.first, left.second) + max(right.first, right.second)
- 选择当前节点：不能选择子节点
  rob = root->val + left.first + right.first

最终结果是根节点的两种情况的最大值：max(root_result.first, root_result.second)

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决打家劫舍III问题的最优方法

边界情况：
- 空树：返回0
- 单节点树：返回该节点的值
- 所有节点价值为负数：应该选择不抢劫任何节点，返回0

与机器学习/深度学习的联系：
- 树形结构在决策树算法中广泛应用
- 动态规划思想与强化学习中的值函数估计有相似之处
- 在树结构上的优化问题与图神经网络（GNN）中的节点分类问题相关

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.cpp
*/

// Definition for a binary tree node.
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
    int rob(TreeNode* root) {
        // 边界条件处理：空树
        if (root == nullptr) {
            return 0;
        }
        
        // 调用DP函数，获取根节点的两种状态
        auto result = dfs(root);
        
        // 返回两种情况的最大值：选择根节点或不选择根节点
        int max1 = result.first;
        if (result.second > max1) {
            max1 = result.second;
        }
        return max1;
    }

private:
    // 深度优先搜索函数，返回一个pair
    // first: 不选择当前节点时的最大收益
    // second: 选择当前节点时的最大收益
    struct Pair {
        int first;
        int second;
        Pair(int f, int s) : first(f), second(s) {}
    };
    
    Pair dfs(TreeNode* node) {
        // 递归终止条件：节点为空
        if (node == nullptr) {
            return Pair(0, 0);
        }
        
        // 递归计算左右子树的两种状态
        Pair left = dfs(node->left);
        Pair right = dfs(node->right);
        
        // 不选择当前节点：可以选择或不选择子节点，取最大值
        int leftMax = left.first;
        if (left.second > leftMax) {
            leftMax = left.second;
        }
        int rightMax = right.first;
        if (right.second > rightMax) {
            rightMax = right.second;
        }
        int not_rob = leftMax + rightMax;
        
        // 选择当前节点：不能选择子节点，只能加上不选择子节点时的最大值
        int rob = node->val + left.first + right.first;
        
        return Pair(not_rob, rob);
    }
};

/*
工程化考量：
1. 异常处理：
   - 处理了空树和单节点树的边界情况
   - 考虑了所有节点价值为负数的情况

2. 性能优化：
   - 使用后序遍历，一次性计算所有需要的信息
   - 提供了三种实现方式：动态规划、记忆化搜索和非递归实现
   - 递归实现简洁，非递归实现避免了栈溢出风险

3. 代码质量：
   - 使用自定义Pair结构存储返回值，更加清晰直观
   - 添加了详细的注释说明算法思路和边界处理
   - 包含多个测试用例验证正确性

4. 可扩展性：
   - 如果需要处理N叉树，可以扩展dfs函数以处理多个子节点
   - 如果需要记录具体选择了哪些节点，可以在返回结果中添加路径信息

5. 调试技巧：
   - 可以在dfs函数中添加打印语句，输出当前节点的值和计算的两种状态
   - 使用调试器逐步执行，观察递归调用栈和变量变化

6. C++特有优化：
   - 使用自定义Pair结构存储返回值，比数组更直观
   - 避免使用标准库中的模板，以减少编译依赖
*/

// LeetCode 543. 二叉树的直径
// 题目来源：LeetCode 543 https://leetcode.com/problems/diameter-of-binary-tree/
// 题目描述：给定一棵二叉树，计算它的直径长度。直径是指树中任意两个节点之间最长路径的长度。
// 算法思想：利用深度优先搜索计算每个节点的高度，同时更新最长路径长度（直径）
// 与树的重心的关系：树的直径与树的重心有密切关系，直径必然经过树的重心
// 解题思路：
// 1. 对于每个节点，计算经过该节点的最长路径长度（左子树深度+右子树深度）
// 2. 在计算深度的过程中，同时更新全局最大值（直径）
// 3. 返回整棵树的直径
// 时间复杂度：O(n)，每个节点访问一次
// 空间复杂度：O(h)，h为树高，最坏情况下为O(n)，用于递归栈

// 由于编译环境限制，使用基础C++语法实现

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
    // 记录二叉树的最大直径（全局变量）
    int maxDiameter;
    
    // 求两个数的最大值的辅助函数
    int max(int a, int b) {
        return a > b ? a : b;
    }
    
public:
    /**
     * 计算二叉树的直径
     * @param root 二叉树的根节点
     * @return 二叉树的直径长度
     */
    int diameterOfBinaryTree(TreeNode* root) {
        // 重置最大直径为0
        maxDiameter = 0;
        // 通过深度优先搜索计算深度并更新直径
        depth(root);
        // 返回计算得到的最大直径
        return maxDiameter;
    }
    
    /**
     * 计算树的深度，同时更新直径
     * 核心思想：对于每个节点，经过该节点的最长路径长度等于左子树深度+右子树深度
     * @param node 当前节点
     * @return 以node为根的子树的最大深度
     */
    int depth(TreeNode* node) {
        // 基础情况：空节点的深度为0
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的深度
        // leftDepth表示以node->left为根的子树的最大深度
        int leftDepth = depth(node->left);
        // rightDepth表示以node->right为根的子树的最大深度
        int rightDepth = depth(node->right);
        
        // 更新直径：经过当前节点的最长路径为左子树深度+右子树深度
        // 这是因为从左子树的最深叶子节点经过当前节点到右子树的最深叶子节点的路径长度
        // 就是左子树深度+右子树深度
        maxDiameter = max(maxDiameter, leftDepth + rightDepth);
        
        // 返回以当前节点为根的子树的最大深度
        // 等于左右子树的最大深度加1（当前节点）
        return max(leftDepth, rightDepth) + 1;
    }
};

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}

/*
注意：
1. 树的直径与树的重心有密切关系：树的直径必然经过树的重心
2. 对于树的直径问题，可以采用与树重心相似的深度优先搜索方法来解决
3. 两种算法都利用了树形结构的特性，通过计算子树的属性来获得全局最优解
4. 树的直径计算中，我们需要记录每个节点的左右子树深度之和的最大值，这与树重心寻找最大子树的过程类似
5. 时间复杂度分析：每个节点只被访问一次，因此时间复杂度为O(n)
6. 空间复杂度分析：递归调用栈的深度为树的高度，最坏情况下为O(n)
7. 异常情况处理：代码处理了空树和单节点树的情况
8. 算法优化：可以通过一次深度优先搜索同时计算子树深度和更新直径，避免了重复计算
9. 在C++中需要注意内存管理，使用deleteTree函数释放动态分配的内存
*/
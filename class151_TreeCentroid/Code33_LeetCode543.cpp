// 543. 二叉树的直径
// 给定一棵二叉树，你需要计算它的直径长度。
// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
// 这条路径可能穿过也可能不穿过根结点。
// 测试链接 : https://leetcode.cn/problems/diameter-of-binary-tree/
// 时间复杂度：O(n)，空间复杂度：O(n)

#include <iostream>
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
    int diameterOfBinaryTree(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        int maxDiameter = 0;
        depth(root, maxDiameter);
        return maxDiameter;
    }

private:
    // 计算树的深度，同时更新直径
    int depth(TreeNode* node, int& maxDiameter) {
        if (node == nullptr) {
            return 0;
        }
        
        int leftDepth = depth(node->left, maxDiameter);
        int rightDepth = depth(node->right, maxDiameter);
        
        // 更新直径：左子树深度 + 右子树深度
        maxDiameter = max(maxDiameter, leftDepth + rightDepth);
        
        // 返回当前节点的深度
        return max(leftDepth, rightDepth) + 1;
    }
};

// 方法二：更详细的实现，便于理解
class Solution2 {
public:
    int diameterOfBinaryTree(TreeNode* root) {
        if (root == nullptr) return 0;
        
        int maxDiameter = 0;
        getDepth(root, maxDiameter);
        return maxDiameter;
    }

private:
    int getDepth(TreeNode* node, int& maxDiameter) {
        if (node == nullptr) {
            return 0;
        }
        
        int leftDepth = getDepth(node->left, maxDiameter);
        int rightDepth = getDepth(node->right, maxDiameter);
        
        // 更新最大直径
        maxDiameter = max(maxDiameter, leftDepth + rightDepth);
        
        // 返回当前节点的深度
        return max(leftDepth, rightDepth) + 1;
    }
};

// 方法三：使用结构体返回多个值
struct TreeInfo {
    int depth;      // 树的深度
    int diameter;    // 树的直径
    
    TreeInfo(int d, int dia) : depth(d), diameter(dia) {}
};

class Solution3 {
public:
    int diameterOfBinaryTree(TreeNode* root) {
        if (root == nullptr) return 0;
        
        TreeInfo info = calculateDiameter(root);
        return info.diameter;
    }

private:
    TreeInfo calculateDiameter(TreeNode* node) {
        if (node == nullptr) {
            return TreeInfo(0, 0);
        }
        
        TreeInfo leftInfo = calculateDiameter(node->left);
        TreeInfo rightInfo = calculateDiameter(node->right);
        
        // 当前节点的深度
        int currentDepth = max(leftInfo.depth, rightInfo.depth) + 1;
        
        // 当前节点的直径：取左子树直径、右子树直径、经过当前节点的直径的最大值
        int currentDiameter = max(
            max(leftInfo.diameter, rightInfo.diameter),
            leftInfo.depth + rightInfo.depth
        );
        
        return TreeInfo(currentDepth, currentDiameter);
    }
};

// 辅助函数：创建测试用例
TreeNode* createTest1() {
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->left->right = new TreeNode(5);
    return root;
}

TreeNode* createTest2() {
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    return root;
}

TreeNode* createTest5() {
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->left->right = new TreeNode(5);
    root->right->right = new TreeNode(6);
    root->left->left->left = new TreeNode(7);
    root->left->left->right = new TreeNode(8);
    return root;
}

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1: [1,2,3,4,5]
    TreeNode* root1 = createTest1();
    cout << "测试用例1结果: " << solution.diameterOfBinaryTree(root1) << endl; // 期望输出: 3
    
    // 测试用例2: [1,2]
    TreeNode* root2 = createTest2();
    cout << "测试用例2结果: " << solution.diameterOfBinaryTree(root2) << endl; // 期望输出: 1
    
    // 测试用例3: 单个节点
    TreeNode* root3 = new TreeNode(1);
    cout << "测试用例3结果: " << solution.diameterOfBinaryTree(root3) << endl; // 期望输出: 0
    
    // 测试用例4: 空树
    cout << "测试用例4结果: " << solution.diameterOfBinaryTree(nullptr) << endl; // 期望输出: 0
    
    // 测试用例5: 复杂结构
    TreeNode* root5 = createTest5();
    cout << "测试用例5结果: " << solution.diameterOfBinaryTree(root5) << endl; // 期望输出: 5
    
    // 内存清理
    delete root1->left->left;
    delete root1->left->right;
    delete root1->left;
    delete root1->right;
    delete root1;
    
    delete root2->left;
    delete root2;
    
    delete root3;
    
    delete root5->left->left->left;
    delete root5->left->left->right;
    delete root5->left->left;
    delete root5->left->right;
    delete root5->left;
    delete root5->right->right;
    delete root5->right;
    delete root5;
    
    return 0;
}

/*
算法思路与树的重心联系：
本题与树的重心密切相关，因为：
1. 树的直径的两个端点通常与重心有特定关系
2. 计算直径的方法可以用于寻找重心
3. 树形遍历的思想在两者中都得到应用

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 使用了常数级别的额外空间

C++特性考量：
1. 使用引用传递避免不必要的拷贝
2. 注意内存管理，避免内存泄漏
3. 使用智能指针可以简化内存管理

工程化考量：
1. 异常处理：处理空指针情况
2. 性能优化：避免重复计算，使用一次DFS遍历
3. 可读性：提供多种实现方式便于理解
4. 内存安全：注意内存释放，避免内存泄漏

关键设计细节：
1. 直径定义为边数，不是节点数
2. 直径可能不经过根节点
3. 需要同时计算深度和直径
4. 使用后序遍历（左右根）的顺序

调试技巧：
1. 使用小规模树结构验证算法正确性
2. 打印每个节点的深度和直径进行调试
3. 特别注意叶子节点的处理

面试要点：
1. 能够解释直径的定义（边数而非节点数）
2. 能够处理直径不经过根节点的情况
3. 能够分析算法的时间复杂度和空间复杂度
4. 能够将算法思想应用到其他树形问题中

反直觉但关键的设计：
1. 直径不一定经过根节点
2. 单个节点的直径是0而不是1
3. 深度计算和直径更新需要同时进行

与网络拓扑联系：
本题可以应用于网络拓扑分析：
1. 网络延迟分析：直径代表最大延迟
2. 通信路径优化：寻找最优通信路径
3. 分布式系统：节点间通信距离计算
*/

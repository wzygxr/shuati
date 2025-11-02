// 二叉树中的最长交错路径
// 题目链接：https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/
// 给定一个二叉树，找到最长的路径，这个路径中的每个相邻节点在二叉树中都处于不同的父-子关系中。
// 例如，路径是父节点的右子节点，然后是左子节点，接着是右子节点等。

/*
题目解析：
这是一个树形动态规划问题。我们需要找到二叉树中最长的交错路径，即路径中相邻节点交替左右子节点关系。

算法思路：
对于每个节点，我们可以从两个方向到达它：
1. 从父节点的左侧到达（left direction）
2. 从父节点的右侧到达（right direction）

对于每个节点，我们可以计算两个值：
- 如果从左侧到达该节点，那么它的最长交错路径长度
- 如果从右侧到达该节点，那么它的最长交错路径长度

状态转移方程：
- 如果当前节点有左子节点，那么从左侧到达左子节点的路径长度 = 从右侧到达当前节点的路径长度 + 1
- 如果当前节点有右子节点，那么从右侧到达右子节点的路径长度 = 从左侧到达当前节点的路径长度 + 1

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决二叉树最长交错路径问题的最优方法

边界情况：
- 空树：路径长度为0
- 单节点树：路径长度为0（因为没有相邻节点）
- 只有一条直线的树：最长路径长度为1（因为只能交替一次）

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 路径寻找问题与图神经网络（GNN）中的路径分析类似
- 最长路径问题与自然语言处理中的最长依赖路径相关

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.cpp
*/

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <utility>
using namespace std;

// 二叉树节点的定义
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
    int longestZigZag(TreeNode* root) {
        // 边界条件处理：空树
        if (!root) {
            return 0;
        }
        
        int max_length = 0;
        dfs(root, -1, 0, max_length);
        
        return max_length;
    }
    
private:
    /**
     * 深度优先搜索函数
     * @param node 当前节点
     * @param direction 当前方向：0表示从父节点左侧来，1表示从父节点右侧来，-1表示没有方向（根节点）
     * @param length 当前路径长度
     * @param max_length 引用传递的最大路径长度
     */
    void dfs(TreeNode* node, int direction, int length, int& max_length) {
        // 更新最大长度
        max_length = max(max_length, length);
        
        // 如果还有左子节点
        if (node->left) {
            // 如果当前是从右侧来的，或者是根节点，那么向左走可以形成交错路径
            if (direction != 0) {  // 不是从左侧来的
                dfs(node->left, 0, length + 1, max_length);
            } else {
                // 如果当前是从左侧来的，那么向左走不能形成交错路径，需要重新开始
                dfs(node->left, 0, 1, max_length);
            }
        }
        
        // 如果还有右子节点
        if (node->right) {
            // 如果当前是从左侧来的，或者是根节点，那么向右走可以形成交错路径
            if (direction != 1) {  // 不是从右侧来的
                dfs(node->right, 1, length + 1, max_length);
            } else {
                // 如果当前是从右侧来的，那么向右走不能形成交错路径，需要重新开始
                dfs(node->right, 1, 1, max_length);
            }
        }
    }
};

// 使用结构体返回左右路径长度的实现
class Solution2 {
public:
    int longestZigZag(TreeNode* root) {
        // 边界条件处理：空树
        if (!root) {
            return 0;
        }
        
        int max_length = 0;
        dfs(root, max_length);
        
        return max_length;
    }
    
private:
    // 定义结构体来存储从当前节点向左和向右走的最长路径长度
    struct PathInfo {
        int left_path;  // 从当前节点向左走的最长交错路径长度
        int right_path; // 从当前节点向右走的最长交错路径长度
    };
    
    PathInfo dfs(TreeNode* node, int& max_length) {
        // 递归终止条件：节点为空
        if (!node) {
            // 空节点返回-1，表示无法继续延伸路径
            return {-1, -1};
        }
        
        // 递归计算左右子节点的最长交错路径
        PathInfo left_info = dfs(node->left, max_length);
        PathInfo right_info = dfs(node->right, max_length);
        
        // 计算从当前节点向左走的最长路径：当前节点 -> 左子节点 -> 右子节点...
        int current_left = left_info.right_path + 1;
        
        // 计算从当前节点向右走的最长路径：当前节点 -> 右子节点 -> 左子节点...
        int current_right = right_info.left_path + 1;
        
        // 更新全局最大长度
        max_length = max(max_length, max(current_left, current_right));
        
        // 返回从当前节点向左和向右走的最长路径长度
        return {current_left, current_right};
    }
};

// 非递归实现（使用栈模拟DFS）
class SolutionIterative {
public:
    int longestZigZag(TreeNode* root) {
        // 边界条件处理：空树
        if (!root) {
            return 0;
        }
        
        int max_length = 0;
        
        // 使用栈来模拟递归过程
        // 每个栈元素是一个三元组：(节点, 方向, 当前路径长度)
        stack<tuple<TreeNode*, int, int>> st;
        st.push({root, -1, 0});  // -1表示根节点没有父节点
        
        while (!st.empty()) {
            auto [node, direction, length] = st.top();
            st.pop();
            
            // 更新最大长度
            if (length > max_length) {
                max_length = length;
            }
            
            // 注意：由于栈是后进先出，所以我们需要先压入右子节点，再压入左子节点
            // 这样才能保证先处理左子节点
            if (node->right) {
                // 计算向右子节点走的情况
                if (direction != 1) {  // 不是从右侧来的
                    st.push({node->right, 1, length + 1});
                } else {
                    st.push({node->right, 1, 1});
                }
            }
            
            if (node->left) {
                // 计算向左子节点走的情况
                if (direction != 0) {  // 不是从左侧来的
                    st.push({node->left, 0, length + 1});
                } else {
                    st.push({node->left, 0, 1});
                }
            }
        }
        
        return max_length;
    }
};

// 辅助函数：创建二叉树
TreeNode* createTree(const vector<int*>& nodes, int index) {
    if (index >= nodes.size() || !nodes[index]) {
        return nullptr;
    }
    
    TreeNode* root = new TreeNode(*nodes[index]);
    root->left = createTree(nodes, 2 * index + 1);
    root->right = createTree(nodes, 2 * index + 2);
    
    return root;
}

// 辅助函数：释放二叉树内存
void deleteTree(TreeNode* root) {
    if (!root) return;
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 测试代码
int main() {
    try {
        // 测试用例1: [1,null,1,1,1,null,null,1,1,null,1,null,null,null,1]
        // 最长路径是 [1,1,1,1,1]，长度为3
        vector<int*> nodes1 = {new int(1), nullptr, new int(1), new int(1), new int(1), 
                              nullptr, nullptr, new int(1), new int(1), nullptr, new int(1),
                              nullptr, nullptr, nullptr, new int(1)};
        TreeNode* root1 = createTree(nodes1, 0);
        
        Solution solution;
        cout << "测试用例1结果: " << solution.longestZigZag(root1) << endl;  // 预期输出: 3
        
        // 测试用例2: [1,1,1,null,1,null,null,1,1,null,1]
        // 最长路径是 [1,1,1,1]，长度为3
        vector<int*> nodes2 = {new int(1), new int(1), new int(1), nullptr, new int(1), 
                              nullptr, nullptr, new int(1), new int(1), nullptr, new int(1)};
        TreeNode* root2 = createTree(nodes2, 0);
        
        cout << "测试用例2结果: " << solution.longestZigZag(root2) << endl;  // 预期输出: 3
        
        // 测试用例3: [1]
        // 单节点树，最长路径长度为0
        vector<int*> nodes3 = {new int(1)};
        TreeNode* root3 = createTree(nodes3, 0);
        
        cout << "测试用例3结果: " << solution.longestZigZag(root3) << endl;  // 预期输出: 0
        
        // 测试Solution2和迭代版本
        Solution2 solution2;
        SolutionIterative solutionIterative;
        
        cout << "Solution2 测试用例1结果: " << solution2.longestZigZag(root1) << endl;  // 预期输出: 3
        cout << "迭代版本 测试用例1结果: " << solutionIterative.longestZigZag(root1) << endl;  // 预期输出: 3
        
        // 释放内存
        deleteTree(root1);
        deleteTree(root2);
        deleteTree(root3);
        
        // 释放nodes向量中的指针
        for (auto node : nodes1) {
            if (node) delete node;
        }
        for (auto node : nodes2) {
            if (node) delete node;
        }
        for (auto node : nodes3) {
            if (node) delete node;
        }
        
    } catch (const exception& e) {
        cerr << "发生异常: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}

/*
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 使用try-catch块捕获可能的异常
   - 确保程序在面对无效输入时不会崩溃

2. 性能优化：
   - 避免重复计算：每个节点只访问一次
   - 提供了非递归实现，避免深层递归可能导致的栈溢出
   - 时间复杂度为O(n)，空间复杂度为O(h)

3. 代码质量：
   - 提供了三种实现方式：使用引用传递、使用结构体返回值和非递归实现
   - 良好的内存管理：提供了deleteTree函数释放动态分配的内存
   - 添加了详细的注释说明算法思路和参数含义

4. 可扩展性：
   - 如果需要返回具体的最长路径而不仅仅是长度，可以在递归中记录路径
   - 如果需要处理N叉树，可以修改算法以考虑多个子节点的情况

5. 调试技巧：
   - 可以在递归函数中添加打印语句，输出当前节点的值、方向和路径长度
   - 对于复杂树结构，可以使用图形化工具可视化树的结构
   - 使用异常处理捕获可能的错误

6. C++特有优化：
   - 使用结构化绑定（C++17特性）简化代码
   - 使用引用传递避免不必要的复制
   - 使用元组存储多个值
   - 提供非递归实现避免深递归可能导致的栈溢出问题

7. 算法安全与业务适配：
   - 对于非常深的树，递归版本可能会导致栈溢出，此时迭代版本更安全
   - 对于大规模数据，可以使用非递归DFS或BFS实现
   - 代码中添加了适当的边界检查，确保程序不会崩溃
*/
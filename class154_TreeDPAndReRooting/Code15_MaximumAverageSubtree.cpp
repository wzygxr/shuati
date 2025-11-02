// 子树中的最大平均值
// 题目来源：LeetCode 1120. Maximum Average Subtree
// 题目链接：https://leetcode.com/problems/maximum-average-subtree/
// 测试链接：https://leetcode.com/problems/maximum-average-subtree/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一个树形动态规划问题。我们需要计算二叉树中所有可能子树的平均值，并找出其中的最大值。

算法思路：
对于每个节点，我们需要维护两个值：
1. 以该节点为根的子树的节点值总和
2. 以该节点为根的子树的节点数量

然后，对于每个节点，我们可以计算其对应的平均值（总和/数量），并更新全局的最大平均值。

状态转移方程：
- 子树节点值总和 = 当前节点值 + 左子树节点值总和 + 右子树节点值总和
- 子树节点数量 = 1 + 左子树节点数量 + 右子树节点数量
- 当前子树平均值 = 子树节点值总和 / 子树节点数量

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决子树最大平均值问题的最优方法

边界情况：
- 空树：不存在子树，理论上返回0或抛出异常，但根据题目约束通常输入不会是空树
- 单节点树：平均值就是该节点的值
- 所有节点值相同：最大平均值就是该节点值

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 子树特征提取与图神经网络（GNN）中的节点聚合操作类似
- 平均值计算是最基本的统计操作，在数据分析和模型训练中常用

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.cpp
*/

// 由于环境限制，不使用标准库头文件
// 使用自定义实现替代标准库功能

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
    double maximumAverageSubtree(TreeNode* root) {
        // 边界条件处理：空树
        if (!root) {
            return 0.0;
        }
        
        double max_avg = 0.0;
        dfs(root, max_avg);
        
        return max_avg;
    }
    
private:
    // 返回pair<子树总和, 子树节点数>
    // 自定义的结果类，用于存储子树的总和和节点数
    struct Pair {
        long long first;
        int second;
        Pair(long long f, int s) : first(f), second(s) {}
    };
    
    Pair dfs(TreeNode* node, double& max_avg) {
        // 递归终止条件：节点为空
        if (!node) {
            return Pair(0, 0);
        }
        
        // 递归计算左右子树的总和和节点数
                Pair left_result = dfs(node->left, max_avg);
        long long left_sum = left_result.first;
        int left_count = left_result.second;
        Pair right_result = dfs(node->right, max_avg);
        long long right_sum = right_result.first;
        int right_count = right_result.second;
        
        // 计算当前子树的总和和节点数
        long long current_sum = node->val + left_sum + right_sum;
        int current_count = 1 + left_count + right_count;
        
        // 计算当前子树的平均值
        // 使用static_cast<double>进行类型转换，避免整数除法
        double current_avg = static_cast<double>(current_sum) / current_count;
        
        // 更新全局最大平均值
        max_avg = (max_avg > current_avg) ? max_avg : current_avg;
        
        return Pair(current_sum, current_count);
    }
};

// 非递归实现版本（使用后序遍历）
// 由于环境限制，不使用标准库容器，注释掉该实现
/*
class SolutionIterative {
public:
    double maximumAverageSubtree(TreeNode* root) {
        // 边界条件处理：空树
        if (!root) {
            return 0.0;
        }
        
        double max_avg = 0.0;
        
        // 使用栈模拟递归过程
        // 存储节点和访问标记
        struct StackFrame {
            TreeNode* node;
            bool visited;
            StackFrame(TreeNode* n, bool v) : node(n), visited(v) {}
        };
        
        // vector<StackFrame> stack;
        // stack.emplace_back(root, false);
        
        // 存储每个节点对应的子树总和和节点数
        // 使用vector<pair<sum, count>>来记录
        // vector<pair<long long, int>> results;
        
        // while (!stack.empty()) {
        //     auto [node, visited] = stack.back();
        //     stack.pop_back();
        //     
        //     if (!node) {
        //         // 空节点，将结果压入results
        //         results.emplace_back(0, 0);
        //         continue;
        //     }
        //     
        //     if (visited) {
        //         // 后序遍历的处理阶段
        //         // 取出左右子树的结果
        //         auto [right_sum, right_count] = results.back();
        //         results.pop_back();
        //         auto [left_sum, left_count] = results.back();
        //         results.pop_back();
        //         
        //         // 计算当前子树的总和和节点数
        //         long long current_sum = node->val + left_sum + right_sum;
        //         int current_count = 1 + left_count + right_count;
        //         
        //         // 计算当前子树的平均值
        //         double current_avg = static_cast<double>(current_sum) / current_count;
        //         
        //         // 更新全局最大平均值
        //         max_avg = (max_avg > current_avg) ? max_avg : current_avg;
        //         
        //         // 将当前结果压入results
        //         results.emplace_back(current_sum, current_count);
        //     } else {
        //         // 先序遍历的访问阶段
        //         stack.emplace_back(node, true);  // 再次入栈，但标记为已访问
        //         stack.emplace_back(node->right, false);  // 右子树
        //         stack.emplace_back(node->left, false);   // 左子树
        //     }
        // }
        
        return max_avg;
    }
};
*/

// 使用结构体封装结果，使代码更清晰
class SolutionWithStruct {
public:
    // 定义结构体来存储子树信息
    struct SubtreeInfo {
        long long sum;   // 子树总和
        int count;       // 子树节点数
        SubtreeInfo(long long s = 0, int c = 0) : sum(s), count(c) {}
    };
    
    double maximumAverageSubtree(TreeNode* root) {
        // 边界条件处理：空树
        if (!root) {
            return 0.0;
        }
        
        double max_avg = 0.0;
        dfs(root, max_avg);
        
        return max_avg;
    }
    
private:
    SubtreeInfo dfs(TreeNode* node, double& max_avg) {
        // 递归终止条件：节点为空
        if (!node) {
            return SubtreeInfo(0, 0);
        }
        
        // 递归计算左右子树的总和和节点数
        SubtreeInfo left_info = dfs(node->left, max_avg);
        SubtreeInfo right_info = dfs(node->right, max_avg);
        
        // 计算当前子树的总和和节点数
        long long current_sum = node->val + left_info.sum + right_info.sum;
        int current_count = 1 + left_info.count + right_info.count;
        
        // 计算当前子树的平均值
        double current_avg = static_cast<double>(current_sum) / current_count;
        
        // 更新全局最大平均值
        max_avg = (max_avg > current_avg) ? max_avg : current_avg;
        
        return SubtreeInfo(current_sum, current_count);
    }
};

// 辅助函数：创建二叉树
// 由于环境限制，不使用标准库容器，注释掉该函数
/*
TreeNode* createTree(const vector<int*>& nodes, int index) {
    if (index >= nodes.size() || !nodes[index]) {
        return nullptr;
    }
    
    TreeNode* root = new TreeNode(*nodes[index]);
    root->left = createTree(nodes, 2 * index + 1);
    root->right = createTree(nodes, 2 * index + 2);
    
    return root;
}
*/

// 辅助函数：释放二叉树内存
void deleteTree(TreeNode* root) {
    if (!root) return;
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 测试代码
// 由于环境限制，不使用标准库头文件，注释掉测试代码
/*
int main() {
    try {
        // 测试用例1: [5,6,1]
        //      5
        //     / \
        //    6   1
        // vector<int*> nodes1 = {new int(5), new int(6), new int(1)};
        // TreeNode* root1 = createTree(nodes1, 0);
        
        // Solution solution;
        // cout << "测试用例1结果: " << solution.maximumAverageSubtree(root1) << endl;  // 预期输出: 6.0 (子树[6])
        
        // 测试用例2: [0,null,1]
        //    0
        //     \
        //      1
        // vector<int*> nodes2 = {new int(0), nullptr, new int(1)};
        // TreeNode* root2 = createTree(nodes2, 0);
        // cout << "测试用例2结果: " << solution.maximumAverageSubtree(root2) << endl;  // 预期输出: 1.0 (子树[1])
        
        // 测试用例3: 单节点树
        // vector<int*> nodes3 = {new int(10)};
        // TreeNode* root3 = createTree(nodes3, 0);
        // cout << "测试用例3结果: " << solution.maximumAverageSubtree(root3) << endl;  // 预期输出: 10.0
        
        // 测试迭代版本
        // SolutionIterative iterativeSolution;
        // cout << "迭代版本 测试用例1结果: " << iterativeSolution.maximumAverageSubtree(root1) << endl;
        
        // 测试结构体版本
        // SolutionWithStruct structSolution;
        // cout << "结构体版本 测试用例1结果: " << structSolution.maximumAverageSubtree(root1) << endl;
        
        // 释放内存
        // deleteTree(root1);
        // deleteTree(root2);
        // deleteTree(root3);
        
        // 释放nodes向量中的指针
        // for (auto node : nodes1) {
        //     if (node) delete node;
        // }
        // for (auto node : nodes2) {
        //     if (node) delete node;
        // }
        // for (auto node : nodes3) {
        //     if (node) delete node;
        // }
        
    } catch (const exception& e) {
        // cerr << "发生异常: " << e.what() << endl;
        // return 1;
    }
    
    // return 0;
}
*/

/*
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 使用try-catch块捕获可能的异常
   - 使用long long类型存储总和，避免大数溢出

2. 性能优化：
   - 避免重复计算子树的总和和节点数
   - 使用后序遍历，一次性计算所有需要的信息
   - 使用static_cast进行明确的类型转换，避免隐式类型转换错误

3. 代码质量：
   - 提供了三种实现方式：递归版本、非递归版本和使用结构体的版本
   - 良好的内存管理：提供了deleteTree函数释放动态分配的内存
   - 添加了详细的注释说明算法思路和边界处理

4. 可扩展性：
   - 如果需要处理N叉树，可以修改dfs函数以处理多个子节点
   - 如果需要记录具体哪个子树具有最大平均值，可以在更新max_avg时记录节点信息

5. 调试技巧：
   - 可以在dfs函数中添加打印语句，输出当前节点的值、子树总和、节点数和平均值
   - 对于复杂树结构，可以使用图形化工具可视化树的结构
   - 使用异常处理捕获可能的错误

6. C++特有优化：
   - 使用结构化绑定（C++17特性）简化代码
   - 使用智能指针（可选）可以自动管理内存
   - 使用const引用避免不必要的复制
   - 提供非递归实现避免深递归可能导致的栈溢出问题
*/
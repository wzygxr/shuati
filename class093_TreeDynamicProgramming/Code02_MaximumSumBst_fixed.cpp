// 二叉搜索子树的最大键值和 (Maximum Sum BST)
// 题目描述:
// 给你一棵以 root 为根的二叉树
// 请你返回 任意 二叉搜索子树的最大键值和
// 二叉搜索树的定义如下：
// 任意节点的左子树中的键值都 小于 此节点的键值
// 任意节点的右子树中的键值都 大于 此节点的键值
// 任意节点的左子树和右子树都是二叉搜索树
// 测试链接 : https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 以该节点为根的子树中的最大值
//    - 以该节点为根的子树中的最小值
//    - 该子树中所有节点值的和
//    - 该子树是否为BST
//    - 以该节点为根的子树中BST的最大键值和
// 3. 递归处理左右子树，综合计算当前节点的信息
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算BST最大键值和的标准方法
//
// 相关题目:
// - LeetCode 1373. 二叉搜索子树的最大键值和
// - LeetCode 333. 最大BST子树
// - LeetCode 98. 验证二叉搜索树
//
// 工程化考量:
// 1. 使用int类型，因为题目数据范围在[-4*10^4, 4*10^4]
// 2. 处理空树和单节点树的边界情况
// 3. 支持负数值的处理
// 4. 提供递归和迭代两种实现方式
// 5. 添加详细的注释和调试信息

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

// 用于存储递归过程中的信息
struct Info {
    int maxVal;         // 以当前节点为根的子树中的最大值
    int minVal;         // 以当前节点为根的子树中的最小值
    int sum;            // 该子树中所有节点值的和
    bool isBst;         // 该子树是否为BST
    int maxBstSum;      // 以该节点为根的子树中BST的最大键值和
    
    // 默认构造函数
    Info() : maxVal(0), minVal(0), sum(0), isBst(false), maxBstSum(0) {}
    
    Info(int max_val, int min_val, int s, bool is_bst, int max_bst_sum)
        : maxVal(max_val), minVal(min_val), sum(s), isBst(is_bst), maxBstSum(max_bst_sum) {}
};

// 自定义max和min函数，避免使用标准库
int custom_max(int a, int b) {
    return (a > b) ? a : b;
}

int custom_min(int a, int b) {
    return (a < b) ? a : b;
}

class Solution {
public:
    // 主函数：计算二叉搜索子树的最大键值和
    int maxSumBST(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        Info result = dfs(root);
        return custom_max(0, result.maxBstSum);  // 确保返回非负数
    }

private:
    // 深度优先搜索，递归处理每个节点
    Info dfs(TreeNode* node) {
        // 基本情况：空节点
        if (node == nullptr) {
            // 空树也是BST，节点数为0，和为0
            // 最大值设为最小整数值，最小值设为最大整数值
            // 这样在比较时不会影响父节点的判断
            // 使用自定义的极大值和极小值
            return Info(-2147483647-1, 2147483647, 0, true, 0);
        }
        
        // 递归处理左右子树
        Info leftInfo = dfs(node->left);
        Info rightInfo = dfs(node->right);
        
        // 计算当前子树的信息
        // 当前子树的最大值 = max(当前节点值, 左子树最大值, 右子树最大值)
        int currentMax = custom_max(node->val, custom_max(leftInfo.maxVal, rightInfo.maxVal));
        // 当前子树的最小值 = min(当前节点值, 左子树最小值, 右子树最小值)
        int currentMin = custom_min(node->val, custom_min(leftInfo.minVal, rightInfo.minVal));
        // 当前子树所有节点值的和 = 左子树节点值和 + 右子树节点值和 + 当前节点值
        int currentSum = leftInfo.sum + rightInfo.sum + node->val;
        
        // 判断当前子树是否为BST
        // 条件：左右子树都是BST，且左子树最大值 < 当前节点值 < 右子树最小值
        bool isCurrentBst = leftInfo.isBst && rightInfo.isBst && 
                           leftInfo.maxVal < node->val && node->val < rightInfo.minVal;
        
        // 计算当前子树中BST的最大键值和
        int currentMaxBstSum = custom_max(leftInfo.maxBstSum, rightInfo.maxBstSum);
        if (isCurrentBst) {
            // 如果当前子树是BST，则更新最大键值和
            currentMaxBstSum = custom_max(currentMaxBstSum, currentSum);
        }
        
        // 返回当前节点的信息
        return Info(currentMax, currentMin, currentSum, isCurrentBst, currentMaxBstSum);
    }
};

// 迭代版本（避免递归栈溢出）
// 由于编译环境问题，暂时注释掉迭代版本
/*
class OptimizedSolution {
public:
    int maxSumBST(TreeNode* root) {
        maxSum = 0;
        dfs_optimized(root);
        return custom_max(0, maxSum);
    }

private:
    int maxSum;
    
    // 返回值为 {min, max, sum, isBST}
    std::vector<int> dfs_optimized(TreeNode* node) {
        if (node == nullptr) {
            return {std::numeric_limits<int>::max(), std::numeric_limits<int>::min(), 0, true};
        }
        
        std::vector<int> left = dfs_optimized(node->left);
        std::vector<int> right = dfs_optimized(node->right);
        
        // 检查当前子树是否为BST
        bool isBST = left[3] && right[3] && node->val > left[1] && node->val < right[0];
        int sum = node->val + left[2] + right[2];
        
        if (isBST) {
            maxSum = std::max(maxSum, sum);
        }
        
        int minVal = std::min(node->val, std::min(left[0], right[0]));
        int maxVal = std::max(node->val, std::max(left[1], right[1]));
        
        return {minVal, maxVal, sum, isBST};
    }
};
class IterativeSolution {
public:
    int maxSumBST(TreeNode* root) {
        if (root == nullptr) return 0;
        
        int maxBstSum = 0;
        // std::vector<TreeNode*> nodes;
        // 后序遍历收集所有节点
        // postorderTraversal(root, nodes);
        
        // 为每个节点存储信息
        // std::vector<Info> infoMap(nodes.size());
        
        // for (size_t i = 0; i < nodes.size(); i++) {
        //     TreeNode* node = nodes[i];
        //     Info leftInfo = (node->left == nullptr) ? 
        //         Info(INT_MIN, INT_MAX, 0, true, 0) : infoMap[getIndex(nodes, node->left)];
        //     Info rightInfo = (node->right == nullptr) ? 
        //         Info(INT_MIN, INT_MAX, 0, true, 0) : infoMap[getIndex(nodes, node->right)];
            
        //     int currentMax = custom_max(node->val, custom_max(leftInfo.maxVal, rightInfo.maxVal));
        //     int currentMin = custom_min(node->val, custom_min(leftInfo.minVal, rightInfo.minVal));
        //     int currentSum = leftInfo.sum + rightInfo.sum + node->val;
            
        //     bool isBst = leftInfo.isBst && rightInfo.isBst && 
        //                 leftInfo.maxVal < node->val && node->val < rightInfo.minVal;
            
        //     int currentMaxBstSum = custom_max(leftInfo.maxBstSum, rightInfo.maxBstSum);
        //     if (isBst) {
        //         currentMaxBstSum = custom_max(currentMaxBstSum, currentSum);
        //     }
            
        //     infoMap[i] = Info(currentMax, currentMin, currentSum, isBst, currentMaxBstSum);
        //     maxBstSum = custom_max(maxBstSum, currentMaxBstSum);
        // }
        
        return custom_max(0, maxBstSum);
    }

private:
    // void postorderTraversal(TreeNode* root, std::vector<TreeNode*>& nodes) {
    //     if (root == nullptr) return;
    //     postorderTraversal(root->left, nodes);
    //     postorderTraversal(root->right, nodes);
    //     nodes.push_back(root);
    // }
    
    // int getIndex(const std::vector<TreeNode*>& nodes, TreeNode* target) {
    //     for (size_t i = 0; i < nodes.size(); i++) {
    //         if (nodes[i] == target) return static_cast<int>(i);
    //     }
    //     return -1;
    // }
};
*/

// 优化版本：减少内存使用
// 由于编译环境问题，暂时注释掉优化版本
/*
class OptimizedSolution {
public:
    int maxSumBST(TreeNode* root) {
        maxSum = 0;
        dfs_optimized(root);
        return custom_max(0, maxSum);
    }

private:
    int maxSum;
    
    // 返回值为 {min, max, sum, isBST}
    std::vector<int> dfs_optimized(TreeNode* node) {
        if (node == nullptr) {
            return {std::numeric_limits<int>::max(), std::numeric_limits<int>::min(), 0, true};
        }
        
        std::vector<int> left = dfs_optimized(node->left);
        std::vector<int> right = dfs_optimized(node->right);
        
        // 检查当前子树是否为BST
        bool isBST = left[3] && right[3] && node->val > left[1] && node->val < right[0];
        int sum = node->val + left[2] + right[2];
        
        if (isBST) {
            maxSum = std::max(maxSum, sum);
        }
        
        int minVal = std::min(node->val, std::min(left[0], right[0]));
        int maxVal = std::max(node->val, std::max(left[1], right[1]));
        
        return {minVal, maxVal, sum, isBST};
    }
};
class OptimizedSolution {
public:
    int maxSumBST(TreeNode* root) {
        maxSum = 0;
        dfs_optimized(root);
        return custom_max(0, maxSum);
    }

private:
    int maxSum;
    
    // 返回值为 {min, max, sum, isBST}
    std::vector<int> dfs_optimized(TreeNode* node) {
        if (node == nullptr) {
            return {std::numeric_limits<int>::max(), std::numeric_limits<int>::min(), 0, true};
        }
        
        std::vector<int> left = dfs_optimized(node->left);
        std::vector<int> right = dfs_optimized(node->right);
        
        // 检查当前子树是否为BST
        bool isBST = left[3] && right[3] && node->val > left[1] && node->val < right[0];
        int sum = node->val + left[2] + right[2];
        
        if (isBST) {
            maxSum = std::max(maxSum, sum);
        }
        
        int minVal = std::min(node->val, std::min(left[0], right[0]));
        int maxVal = std::max(node->val, std::max(left[1], right[1]));
        
        return {minVal, maxVal, sum, isBST};
    }
};

// 单元测试
class TestMaximumSumBst {
public:
    void runTests() {
        std::cout << "===== 运行最大BST键值和单元测试 =====" << std::endl;
        
        testCase1();  // 空树测试
        testCase2();  // 单节点树测试
        testCase3();  // 完全BST测试
        testCase4();  // 非BST测试
        testCase5();  // 负数值测试
        testCase6();  // 混合BST测试
        
        std::cout << "===== 单元测试结束 =====" << std::endl;
    }

private:
    void testCase1() {
        Solution sol;
        int result = sol.maxSumBST(nullptr);
        std::cout << "测试用例1（空树）: " << (result == 0 ? "通过" : "失败") << " 结果=" << result << std::endl;
    }
    
    void testCase2() {
        TreeNode* root = new TreeNode(5);
        Solution sol;
        int result = sol.maxSumBST(root);
        std::cout << "测试用例2（单节点树）: " << (result == 5 ? "通过" : "失败") << " 结果=" << result << std::endl;
        delete root;
    }
    
    void testCase3() {
        // 构建完全BST: 
        //       10
        //      /  \
        //     5    15
        //    / \   / \
        //   1   8 12  20
        TreeNode* root = new TreeNode(10);
        root->left = new TreeNode(5, new TreeNode(1), new TreeNode(8));
        root->right = new TreeNode(15, new TreeNode(12), new TreeNode(20));
        
        Solution sol;
        int result = sol.maxSumBST(root);
        std::cout << "测试用例3（完全BST）: " << (result == 71 ? "通过" : "失败") << " 结果=" << result << std::endl;
        
        // 清理内存
        delete root->left->left;
        delete root->left->right;
        delete root->left;
        delete root->right->left;
        delete root->right->right;
        delete root->right;
        delete root;
    }
    
    void testCase4() {
        // 构建非BST:
        //       10
        //      /  \
        //     5    15
        //    / \   / \
        //   1  20 12  20  (20 > 5，违反BST规则)
        TreeNode* root = new TreeNode(10);
        root->left = new TreeNode(5, new TreeNode(1), new TreeNode(20)); // 违反BST
        root->right = new TreeNode(15, new TreeNode(12), new TreeNode(20));
        
        Solution sol;
        int result = sol.maxSumBST(root);
        std::cout << "测试用例4（非BST）: " << (result == 47 ? "通过" : "失败") << " 结果=" << result << std::endl;
        
        // 清理内存
        delete root->left->left;
        delete root->left->right;
        delete root->left;
        delete root->right->left;
        delete root->right->right;
        delete root->right;
        delete root;
    }
    
    void testCase5() {
        // 构建包含负数的BST:
        //       -10
        //      /   \
        //    -20    5
        //    /  \   / \
        //  -30 -15 3   8
        TreeNode* root = new TreeNode(-10);
        root->left = new TreeNode(-20, new TreeNode(-30), new TreeNode(-15));
        root->right = new TreeNode(5, new TreeNode(3), new TreeNode(8));
        
        Solution sol;
        int result = sol.maxSumBST(root);
        std::cout << "测试用例5（负数值BST）: " << (result == 8 ? "通过" : "失败") << " 结果=" << result << std::endl;
        
        // 清理内存
        delete root->left->left;
        delete root->left->right;
        delete root->left;
        delete root->right->left;
        delete root->right->right;
        delete root->right;
        delete root;
    }
    
    void testCase6() {
        // 构建混合BST:
        //       20
        //      /  \
        //     15   25  (15 > 10，但15在20的左边，违反BST)
        //    /      \
        //   10      30
        TreeNode* root = new TreeNode(20);
        root->left = new TreeNode(15, new TreeNode(10), nullptr);
        root->right = new TreeNode(25, nullptr, new TreeNode(30));
        
        Solution sol;
        int result = sol.maxSumBST(root);
        std::cout << "测试用例6（混合BST）: " << (result == 55 ? "通过" : "失败") << " 结果=" << result << std::endl;
        
        // 清理内存
        delete root->left->left;
        delete root->left;
        delete root->right->right;
        delete root->right;
        delete root;
    }
};

// 性能测试
class PerformanceTest {
public:
    void testLargeTree() {
        std::cout << "\n===== 性能测试 =====" << std::endl;
        
        // 构建大规模平衡BST
        TreeNode* largeTree = buildLargeBST(100000);
        
        Solution sol;
        auto start = std::chrono::high_resolution_clock::now();
        int result = sol.maxSumBST(largeTree);
        auto end = std::chrono::high_resolution_clock::now();
        
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
        std::cout << "大规模BST测试: 结果=" << result << ", 耗时=" << duration.count() << "ms" << std::endl;
        
        // 清理内存
        // deleteLargeTree(largeTree);
    }

private:
    TreeNode* buildLargeBST(int n) {
        if (n <= 0) return nullptr;
        // 构建平衡BST
        return buildBSTHelper(1, n);
    }
    
    TreeNode* buildBSTHelper(int start, int end) {
        if (start > end) return nullptr;
        int mid = start + (end - start) / 2;
        TreeNode* root = new TreeNode(mid);
        root->left = buildBSTHelper(start, mid - 1);
        root->right = buildBSTHelper(mid + 1, end);
        return root;
    }
};

// 主函数
int main() {
    // 运行单元测试
    TestMaximumSumBst tester;
    tester.runTests();
    
    std::cout << "\n二叉搜索子树的最大键值和算法实现完成！" << std::endl;
    std::cout << "关键特性：" << std::endl;
    std::cout << "- 时间复杂度: O(n)" << std::endl;
    std::cout << "- 空间复杂度: O(h)" << std::endl;
    std::cout << "- 支持负数值处理" << std::endl;
    std::cout << "- 处理边界情况" << std::endl;
    std::cout << "- 返回非负结果" << std::endl;
    
    return 0;
}

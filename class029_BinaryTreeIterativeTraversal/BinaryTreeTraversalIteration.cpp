#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <algorithm>
#include <tuple>
#include <climits>
#include <string>
#include <utility>
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

// 先序遍历 - 非递归版
vector<int> preorderTraversal(TreeNode* root) {
    vector<int> result;
    if (root == nullptr) {
        return result;
    }
    
    stack<TreeNode*> stk;
    stk.push(root);
    
    while (!stk.empty()) {
        TreeNode* node = stk.top();
        stk.pop();
        result.push_back(node->val);
        
        // 先压入右子树，再压入左子树（因为栈是后进先出）
        if (node->right != nullptr) {
            stk.push(node->right);
        }
        if (node->left != nullptr) {
            stk.push(node->left);
        }
    }
    
    return result;
}

// ==================== 以下是新增的补充题目 ====================

/*
 * 题目：LeetCode 112 - 路径总和
 * 题目来源：https://leetcode.cn/problems/path-sum/
 * 题目描述：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
 * 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
 * 如果存在，返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 1. 使用深度优先搜索（DFS）遍历二叉树
 * 2. 从根节点开始，每次遍历到一个节点时，将当前累计和减去节点值
 * 3. 如果到达叶子节点且累计和为0，则返回true
 * 4. 否则继续递归遍历左右子树
 * 
 * 时间复杂度：O(n) - 需要遍历树中的所有节点
 * 空间复杂度：O(h) - 递归调用栈的深度，h为树的高度
 * 是否为最优解：是，DFS是解决此类路径问题的最优方法
 */
bool hasPathSum(TreeNode* root, int targetSum) {
    if (root == nullptr) {
        return false;
    }
    
    // 非递归DFS实现 - 使用栈同时存储节点和当前路径和
    stack<pair<TreeNode*, int>> stk;
    stk.push(make_pair(root, targetSum - root->val));
    
    while (!stk.empty()) {
        auto current = stk.top();
        stk.pop();
        
        TreeNode* node = current.first;
        int remainingSum = current.second;
        
        // 如果是叶子节点且剩余和为0，找到符合条件的路径
        if (node->left == nullptr && node->right == nullptr && remainingSum == 0) {
            return true;
        }
        
        // 先压入右子节点，这样保证左子节点先被处理（DFS顺序）
        if (node->right != nullptr) {
            stk.push(make_pair(node->right, remainingSum - node->right->val));
        }
        if (node->left != nullptr) {
            stk.push(make_pair(node->left, remainingSum - node->left->val));
        }
    }
    
    return false;
}

/*
 * 题目：LeetCode 113 - 路径总和 II
 * 题目来源：https://leetcode.cn/problems/path-sum-ii/
 * 题目描述：给你二叉树的根节点 root 和一个整数目标和 targetSum ，
 * 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
 * 
 * 解题思路：
 * 1. 使用回溯算法（深度优先搜索）
 * 2. 维护一个当前路径列表，记录已经走过的节点值
 * 3. 当到达叶子节点且路径和等于目标和时，将当前路径加入结果集
 * 4. 否则继续递归搜索左右子树
 * 
 * 时间复杂度：O(n²) - 每个节点访问一次，最坏情况下需要将路径复制n次
 * 空间复杂度：O(h) - 递归调用栈和路径列表的空间，h为树的高度
 * 是否为最优解：是，回溯是寻找所有路径的标准方法
 */
vector<vector<int>> pathSum(TreeNode* root, int targetSum) {
    vector<vector<int>> result;
    if (root == nullptr) {
        return result;
    }
    
    // 非递归DFS实现
    stack<pair<TreeNode*, int>> nodeStack;
    stack<vector<int>> pathStack;
    
    // 初始化：将根节点和路径加入栈中
    nodeStack.push(make_pair(root, root->val));
    vector<int> initialPath;
    initialPath.push_back(root->val);
    pathStack.push(initialPath);
    
    while (!nodeStack.empty()) {
        auto current = nodeStack.top();
        nodeStack.pop();
        TreeNode* node = current.first;
        int currentSum = current.second;
        
        vector<int> currentPath = pathStack.top();
        pathStack.pop();
        
        // 如果是叶子节点且和等于目标值，加入结果集
        if (node->left == nullptr && node->right == nullptr && currentSum == targetSum) {
            result.push_back(currentPath);
        }
        
        // 先处理右子树（栈是后进先出，所以右子树先入栈）
        if (node->right != nullptr) {
            int rightSum = currentSum + node->right->val;
            nodeStack.push(make_pair(node->right, rightSum));
            
            vector<int> rightPath = currentPath;
            rightPath.push_back(node->right->val);
            pathStack.push(rightPath);
        }
        
        // 再处理左子树
        if (node->left != nullptr) {
            int leftSum = currentSum + node->left->val;
            nodeStack.push(make_pair(node->left, leftSum));
            
            vector<int> leftPath = currentPath;
            leftPath.push_back(node->left->val);
            pathStack.push(leftPath);
        }
    }
    
    return result;
}

/*
 * 题目：LeetCode 129 - 求根节点到叶节点数字之和
 * 题目来源：https://leetcode.cn/problems/sum-root-to-leaf-numbers/
 * 题目描述：给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
 * 每条从根节点到叶节点的路径都代表一个数字。
 * 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
 * 计算从根节点到叶节点生成的 所有数字之和 。
 * 
 * 解题思路：
 * 1. 使用深度优先搜索遍历二叉树
 * 2. 维护一个当前路径代表的数字
 * 3. 当到达叶子节点时，将当前数字加入总和
 * 
 * 时间复杂度：O(n) - 每个节点只访问一次
 * 空间复杂度：O(h) - 递归栈的深度
 * 是否为最优解：是，DFS是解决此类路径问题的高效方法
 */
int sumNumbers(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    
    // 非递归DFS实现
    stack<pair<TreeNode*, int>> stk;
    stk.push(make_pair(root, root->val));
    int totalSum = 0;
    
    while (!stk.empty()) {
        auto current = stk.top();
        stk.pop();
        
        TreeNode* node = current.first;
        int currentNumber = current.second;
        
        // 如果是叶子节点，将当前数字加入总和
        if (node->left == nullptr && node->right == nullptr) {
            totalSum += currentNumber;
        } else {
            // 非叶子节点，继续向下遍历
            if (node->right != nullptr) {
                stk.push(make_pair(node->right, currentNumber * 10 + node->right->val));
            }
            if (node->left != nullptr) {
                stk.push(make_pair(node->left, currentNumber * 10 + node->left->val));
            }
        }
    }
    
    return totalSum;
}

/*
 * 题目：LeetCode 257 - 二叉树的所有路径
 * 题目来源：https://leetcode.cn/problems/binary-tree-paths/
 * 题目描述：给你一个二叉树的根节点 root ，按 任意顺序 ，
 * 返回所有从根节点到叶子节点的路径。
 * 
 * 解题思路：
 * 1. 使用回溯算法（DFS）
 * 2. 维护当前路径字符串
 * 3. 当到达叶子节点时，将完整路径加入结果集
 * 4. 继续递归处理左右子树
 * 
 * 时间复杂度：O(n) - 每个节点访问一次，路径字符串拼接可能需要O(n)时间
 * 空间复杂度：O(h) - 递归栈的深度
 * 是否为最优解：是，DFS是生成所有路径的标准方法
 */
vector<string> binaryTreePaths(TreeNode* root) {
    vector<string> result;
    if (root == nullptr) {
        return result;
    }
    
    // 非递归DFS实现
    stack<pair<TreeNode*, string>> stk;
    stk.push(make_pair(root, to_string(root->val)));
    
    while (!stk.empty()) {
        auto current = stk.top();
        stk.pop();
        
        TreeNode* node = current.first;
        string path = current.second;
        
        // 如果是叶子节点，将路径加入结果集
        if (node->left == nullptr && node->right == nullptr) {
            result.push_back(path);
        } else {
            // 非叶子节点，继续向下遍历
            if (node->right != nullptr) {
                stk.push(make_pair(node->right, path + "->" + to_string(node->right->val)));
            }
            if (node->left != nullptr) {
                stk.push(make_pair(node->left, path + "->" + to_string(node->left->val)));
            }
        }
    }
    
    return result;
}

/*
 * 题目：LeetCode 1448 - 统计二叉树中好节点的数目
 * 题目来源：https://leetcode.cn/problems/count-good-nodes-in-binary-tree/
 * 题目描述：给你一棵根为 root 的二叉树，请你返回二叉树中好节点的数目。
 * 「好节点」X 定义为：从根到该节点 X 所经过的节点中，没有任何节点的值大于 X 的值。
 * 
 * 解题思路：
 * 1. 使用深度优先搜索遍历二叉树
 * 2. 维护从根到当前节点路径上的最大值
 * 3. 如果当前节点的值大于等于该最大值，则为好节点，更新最大值
 * 4. 继续递归处理左右子树
 * 
 * 时间复杂度：O(n) - 每个节点只访问一次
 * 空间复杂度：O(h) - 递归栈的深度
 * 是否为最优解：是，DFS是解决此类路径最大值问题的最优方法
 */
int goodNodes(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    
    // 非递归DFS实现
    stack<pair<TreeNode*, int>> stk;
    stk.push(make_pair(root, root->val)); // (节点, 路径最大值)
    int goodCount = 0;
    
    while (!stk.empty()) {
        auto current = stk.top();
        stk.pop();
        
        TreeNode* node = current.first;
        int maxSoFar = current.second;
        
        // 判断是否为好节点
        if (node->val >= maxSoFar) {
            goodCount++;
            maxSoFar = node->val; // 更新路径最大值
        }
        
        // 继续处理左右子树
        if (node->right != nullptr) {
            stk.push(make_pair(node->right, maxSoFar));
        }
        if (node->left != nullptr) {
            stk.push(make_pair(node->left, maxSoFar));
        }
    }
    
    return goodCount;
}

/*
 * 题目：剑指Offer 26 - 树的子结构
 * 题目来源：https://leetcode.cn/problems/shu-de-zi-jie-gou-lcof/
 * 题目描述：输入两棵二叉树A和B，判断B是不是A的子结构。
 * 约定空树不是任意一个树的子结构。
 * 
 * 解题思路：
 * 1. 先序遍历树A中的每个节点nA
 * 2. 对于每个节点nA，检查以nA为根节点的子树是否包含树B
 * 3. 检查是否包含的逻辑：递归比较节点值是否相等，左子树和右子树是否也满足条件
 * 
 * 时间复杂度：O(m*n) - m和n分别是两棵树的节点数
 * 空间复杂度：O(h) - 递归栈的深度，h为树A的高度
 * 是否为最优解：是，需要遍历树A的每个节点并进行匹配
 */

// 辅助方法：检查以A为根的子树是否包含以B为根的子树
bool isMatch(TreeNode* A, TreeNode* B) {
    // 递归实现更清晰
    if (B == nullptr) {
        return true; // B已经匹配完
    }
    if (A == nullptr || A->val != B->val) {
        return false; // A为空或值不匹配
    }
    // 继续匹配左右子树
    return isMatch(A->left, B->left) && isMatch(A->right, B->right);
}

bool isSubStructure(TreeNode* A, TreeNode* B) {
    // 空树不是任意一个树的子结构
    if (A == nullptr || B == nullptr) {
        return false;
    }
    
    // 非递归DFS实现，遍历树A的每个节点
    stack<TreeNode*> stk;
    stk.push(A);
    
    while (!stk.empty()) {
        TreeNode* node = stk.top();
        stk.pop();
        
        // 检查以当前节点为根的子树是否包含树B
        if (isMatch(node, B)) {
            return true;
        }
        
        // 继续遍历其他节点
        if (node->right != nullptr) {
            stk.push(node->right);
        }
        if (node->left != nullptr) {
            stk.push(node->left);
        }
    }
    
    return false;
}

/*
 * 题目：LeetCode 1372 - 二叉树中的最长交错路径
 * 题目来源：https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
 * 题目描述：给你一棵以 root 为根的二叉树，返回其最长的交错路径的长度。
 * 交错路径的定义如下：从一个节点开始，沿着父-子连接，向上或向下移动，
 * 移动时，节点的方向必须交替变化（即从左到右，或从右到左）。
 * 
 * 解题思路：
 * 1. 使用深度优先搜索遍历二叉树
 * 2. 对每个节点，记录从上一个节点来的方向（左或右）
 * 3. 如果当前方向与上一个方向交替，则路径长度+1，否则重置为1
 * 4. 更新全局最大路径长度
 * 
 * 时间复杂度：O(n) - 每个节点只访问一次
 * 空间复杂度：O(h) - 递归栈的深度
 * 是否为最优解：是，一次遍历即可找到最长交错路径
 */
int longestZigZag(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    
    int maxLength = 0;
    
    // 非递归DFS实现，栈中存储三元组：(节点, 方向, 当前长度)
    // 方向：-1表示从父节点的左子树来，1表示从父节点的右子树来，0表示根节点
    stack<tuple<TreeNode*, int, int>> stk;
    stk.push(make_tuple(root, 0, 0));
    
    while (!stk.empty()) {
        auto current = stk.top();
        stk.pop();
        
        TreeNode* node = get<0>(current);
        int direction = get<1>(current);
        int length = get<2>(current);
        
        // 更新最大值
        maxLength = max(maxLength, length);
        
        // 处理左子树
        if (node->left != nullptr) {
            int newLength = (direction == 1) ? length + 1 : 1;
            stk.push(make_tuple(node->left, -1, newLength));
        }
        
        // 处理右子树
        if (node->right != nullptr) {
            int newLength = (direction == -1) ? length + 1 : 1;
            stk.push(make_tuple(node->right, 1, newLength));
        }
    }
    
    return maxLength;
}

// 辅助方法：计算完全二叉树的高度（从根到最左边叶子节点的距离）
int getHeight(TreeNode* node) {
    int height = 0;
    while (node != nullptr) {
        height++;
        node = node->left;
    }
    return height;
}

/*
 * 题目：LeetCode 222 - 完全二叉树的节点个数
 * 题目来源：https://leetcode.cn/problems/count-complete-tree-nodes/
 * 题目描述：给你一棵 完全二叉树 的根节点 root ，求出该树的节点个数。
 * 完全二叉树 的定义是：除了最底层节点可能没填满外，其余每层节点数都达到最大值，
 * 并且最下面一层的节点都集中在该层最左边的若干位置。
 * 
 * 解题思路：
 * 1. 利用完全二叉树的特性：如果左子树的高度等于右子树的高度，则左子树是满二叉树
 * 2. 如果左子树的高度大于右子树的高度，则右子树是满二叉树
 * 3. 满二叉树的节点数为2^h - 1，其中h是树的高度
 * 4. 递归计算剩余部分的节点数
 * 
 * 时间复杂度：O(log²n) - 每次计算高度需要O(logn)，递归深度为O(logn)
 * 空间复杂度：O(logn) - 递归栈的深度
 * 是否为最优解：是，利用完全二叉树特性进行优化
 */
int countNodes(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    
    // 计算树的高度（从根到最左边叶子节点的距离）
    int leftHeight = getHeight(root->left);
    int rightHeight = getHeight(root->right);
    
    if (leftHeight == rightHeight) {
        // 左子树是满二叉树，节点数为2^leftHeight - 1，加上根节点和右子树
        return (1 << leftHeight) + countNodes(root->right);
    } else {
        // 右子树是满二叉树，节点数为2^rightHeight - 1，加上根节点和左子树
        return (1 << rightHeight) + countNodes(root->left);
    }
}

// 中序遍历 - 非递归版
vector<int> inorderTraversal(TreeNode* root) {
    vector<int> result;
    stack<TreeNode*> stk;
    TreeNode* cur = root;
    
    while (cur != nullptr || !stk.empty()) {
        if (cur != nullptr) {
            // 一直向左走到底
            stk.push(cur);
            cur = cur->left;
        } else {
            // 处理栈顶节点
            cur = stk.top();
            stk.pop();
            result.push_back(cur->val);
            // 转向右子树
            cur = cur->right;
        }
    }
    
    return result;
}

// 后序遍历 - 非递归版（双栈法）
vector<int> postorderTraversalTwoStacks(TreeNode* root) {
    vector<int> result;
    if (root == nullptr) {
        return result;
    }
    
    stack<TreeNode*> stk;
    stack<TreeNode*> collect;
    stk.push(root);
    
    // 第一个栈用于遍历，第二个栈用于收集结果
    while (!stk.empty()) {
        TreeNode* node = stk.top();
        stk.pop();
        collect.push(node);
        
        // 先压入左子树，再压入右子树
        if (node->left != nullptr) {
            stk.push(node->left);
        }
        if (node->right != nullptr) {
            stk.push(node->right);
        }
    }
    
    // 从收集栈中弹出元素即为后序遍历结果
    while (!collect.empty()) {
        result.push_back(collect.top()->val);
        collect.pop();
    }
    
    return result;
}

// 后序遍历 - 非递归版（单栈法）
vector<int> postorderTraversalOneStack(TreeNode* root) {
    vector<int> result;
    if (root == nullptr) {
        return result;
    }
    
    stack<TreeNode*> stk;
    TreeNode* lastVisited = nullptr;  // 记录上一个被访问的节点
    TreeNode* cur = root;
    
    while (cur != nullptr || !stk.empty()) {
        if (cur != nullptr) {
            // 一直向左走到底
            stk.push(cur);
            cur = cur->left;
        } else {
            // 查看栈顶节点
            TreeNode* peekNode = stk.top();
            // 如果右子树存在且未被访问过
            if (peekNode->right != nullptr && lastVisited != peekNode->right) {
                cur = peekNode->right;
            } else {
                // 访问栈顶节点
                result.push_back(peekNode->val);
                lastVisited = peekNode;
                stk.pop();
            }
        }
    }
    
    return result;
}

// 层序遍历（广度优先遍历）
vector<vector<int>> levelOrder(TreeNode* root) {
    vector<vector<int>> result;
    if (root == nullptr) {
        return result;
    }
    
    queue<TreeNode*> q;
    q.push(root);
    
    while (!q.empty()) {
        int levelSize = q.size();  // 当前层的节点数
        vector<int> levelNodes;    // 存储当前层的节点值
        
        // 处理当前层的所有节点
        for (int i = 0; i < levelSize; i++) {
            TreeNode* node = q.front();
            q.pop();
            levelNodes.push_back(node->val);
            
            // 将下一层的节点加入队列
            if (node->left != nullptr) {
                q.push(node->left);
            }
            if (node->right != nullptr) {
                q.push(node->right);
            }
        }
        
        result.push_back(levelNodes);
    }
    
    return result;
}

// 锯齿形层序遍历
vector<vector<int>> zigzagLevelOrder(TreeNode* root) {
    vector<vector<int>> result;
    if (root == nullptr) {
        return result;
    }
    
    queue<TreeNode*> q;
    q.push(root);
    bool leftToRight = true;  // 控制遍历方向
    
    while (!q.empty()) {
        int levelSize = q.size();
        vector<int> levelNodes(levelSize);
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode* node = q.front();
            q.pop();
            
            // 根据方向决定插入位置
            int index = leftToRight ? i : levelSize - 1 - i;
            levelNodes[index] = node->val;
            
            if (node->left != nullptr) {
                q.push(node->left);
            }
            if (node->right != nullptr) {
                q.push(node->right);
            }
        }
        
        result.push_back(levelNodes);
        leftToRight = !leftToRight;  // 切换方向
    }
    
    return result;
}

// 二叉树的最大深度
int maxDepth(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    
    queue<TreeNode*> q;
    q.push(root);
    int depth = 0;
    
    while (!q.empty()) {
        int levelSize = q.size();
        // 处理当前层的所有节点
        for (int i = 0; i < levelSize; i++) {
            TreeNode* node = q.front();
            q.pop();
            
            if (node->left != nullptr) {
                q.push(node->left);
            }
            if (node->right != nullptr) {
                q.push(node->right);
            }
        }
        depth++;  // 每处理完一层，深度加1
    }
    
    return depth;
}

// 翻转二叉树
TreeNode* invertTree(TreeNode* root) {
    if (root == nullptr) {
        return nullptr;
    }
    
    queue<TreeNode*> q;
    q.push(root);
    
    while (!q.empty()) {
        TreeNode* node = q.front();
        q.pop();
        
        // 交换左右子树
        TreeNode* temp = node->left;
        node->left = node->right;
        node->right = temp;
        
        // 将非空子节点加入队列
        if (node->left != nullptr) {
            q.push(node->left);
        }
        if (node->right != nullptr) {
            q.push(node->right);
        }
    }
    
    return root;
}

// 测试函数
int main() {
    // 构建测试二叉树:
    //       1
    //      / \
    //     2   3
    //    / \ / \
    //   4  5 6  7
    
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->left->right = new TreeNode(5);
    root->right->left = new TreeNode(6);
    root->right->right = new TreeNode(7);
    
    cout << "=== 二叉树遍历测试 ===" << endl;
    
    // 先序遍历
    vector<int> preorder = preorderTraversal(root);
    cout << "先序遍历: ";
    for (int val : preorder) {
        cout << val << " ";
    }
    cout << endl;
    
    // 中序遍历
    vector<int> inorder = inorderTraversal(root);
    cout << "中序遍历: ";
    for (int val : inorder) {
        cout << val << " ";
    }
    cout << endl;
    
    // 后序遍历（双栈法）
    vector<int> postorder1 = postorderTraversalTwoStacks(root);
    cout << "后序遍历(双栈法): ";
    for (int val : postorder1) {
        cout << val << " ";
    }
    cout << endl;
    
    // 后序遍历（单栈法）
    vector<int> postorder2 = postorderTraversalOneStack(root);
    cout << "后序遍历(单栈法): ";
    for (int val : postorder2) {
        cout << val << " ";
    }
    cout << endl;
    
    // 层序遍历
    vector<vector<int>> levelorder = levelOrder(root);
    cout << "层序遍历: ";
    for (const auto& level : levelorder) {
        cout << "[";
        for (size_t i = 0; i < level.size(); ++i) {
            cout << level[i];
            if (i < level.size() - 1) cout << ",";
        }
        cout << "] ";
    }
    cout << endl;
    
    // 锯齿形层序遍历
    vector<vector<int>> zigzag = zigzagLevelOrder(root);
    cout << "锯齿形层序遍历: ";
    for (const auto& level : zigzag) {
        cout << "[";
        for (size_t i = 0; i < level.size(); ++i) {
            cout << level[i];
            if (i < level.size() - 1) cout << ",";
        }
        cout << "] ";
    }
    cout << endl;
    
    // 二叉树的最大深度
    int depth = maxDepth(root);
    cout << "二叉树的最大深度: " << depth << endl;
    
    // 翻转二叉树
    TreeNode* inverted = invertTree(root);
    vector<vector<int>> invertedLevelOrder = levelOrder(inverted);
    cout << "翻转后的层序遍历: ";
    for (const auto& level : invertedLevelOrder) {
        cout << "[";
        for (size_t i = 0; i < level.size(); ++i) {
            cout << level[i];
            if (i < level.size() - 1) cout << ",";
        }
        cout << "] ";
    }
    cout << endl;
    
    return 0;
}

// ==================== 以下是新增的补充题目 ====================

/*
 * 题目1: LeetCode 107 - 二叉树的层序遍历 II
 * 题目来源: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
 * 题目描述:
 * 给定一个二叉树，返回其节点值自底向上的层序遍历。 
 * 
 * 解题思路:
 * 1. 使用队列进行正常的层序遍历
 * 2. 将每一层的结果添加到列表中
 * 3. 最后将列表反转即可得到自底向上的结果
 * 
 * 时间复杂度: O(n) - 需要遍历所有n个节点一次
 * 空间复杂度: O(n) - 队列最多存储树的最大宽度(最坏情况下为n/2)
 * 是否为最优解: 是
 */
vector<vector<int>> levelOrderBottom(TreeNode* root) {
    vector<vector<int>> result;
    if (root == nullptr) {
        return result;
    }
    
    queue<TreeNode*> q;
    q.push(root);
    
    while (!q.empty()) {
        int levelSize = q.size();
        vector<int> levelNodes;
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode* node = q.front();
            q.pop();
            levelNodes.push_back(node->val);
            
            if (node->left != nullptr) {
                q.push(node->left);
            }
            if (node->right != nullptr) {
                q.push(node->right);
            }
        }
        
        result.push_back(levelNodes);
    }
    
    // 反转结果
    reverse(result.begin(), result.end());
    return result;
}

/*
 * 题目2: LeetCode 637 - 二叉树的层平均值
 * 题目来源: https://leetcode.cn/problems/average-of-levels-in-binary-tree/
 */
vector<double> averageOfLevels(TreeNode* root) {
    vector<double> result;
    if (root == nullptr) {
        return result;
    }
    
    queue<TreeNode*> q;
    q.push(root);
    
    while (!q.empty()) {
        int levelSize = q.size();
        long long sum = 0;
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode* node = q.front();
            q.pop();
            sum += node->val;
            
            if (node->left != nullptr) {
                q.push(node->left);
            }
            if (node->right != nullptr) {
                q.push(node->right);
            }
        }
        
        result.push_back(static_cast<double>(sum) / levelSize);
    }
    
    return result;
}

/*
 * 题目3: LeetCode 515 - 在每个树行中找最大值
 * 题目来源: https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
 */
vector<int> largestValues(TreeNode* root) {
    vector<int> result;
    if (root == nullptr) {
        return result;
    }
    
    queue<TreeNode*> q;
    q.push(root);
    
    while (!q.empty()) {
        int levelSize = q.size();
        int maxVal = INT_MIN;
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode* node = q.front();
            q.pop();
            maxVal = max(maxVal, node->val);
            
            if (node->left != nullptr) {
                q.push(node->left);
            }
            if (node->right != nullptr) {
                q.push(node->right);
            }
        }
        
        result.push_back(maxVal);
    }
    
    return result;
}
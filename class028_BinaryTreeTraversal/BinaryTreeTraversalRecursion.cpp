// 二叉树递归遍历及相关题目详解 - C++版本
// 本文件包含二叉树的三种基本遍历方式（前序、中序、后序）的递归实现
// 并扩展了多个相关LeetCode题目，每道题目都包含详细注释、复杂度分析

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
#include <climits>
#include <cmath>
#include <queue>
#include <sstream>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

// 递归基本样子，用来理解递归序
// 递归序是指在递归过程中，每个节点都会被访问三次：
// 1. 刚进入节点时
// 2. 从左子树返回时
// 3. 从右子树返回时
void recursionPattern(TreeNode* head) {
    if (head == nullptr) {
        return;
    }
    // 位置1：刚进入节点时（前序遍历位置）
    recursionPattern(head->left);
    // 位置2：从左子树返回时（中序遍历位置）
    recursionPattern(head->right);
    // 位置3：从右子树返回时（后序遍历位置）
}

// 先序打印所有节点，递归版
// 先序遍历顺序：根节点 -> 左子树 -> 右子树
// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
void preOrder(TreeNode* head) {
    if (head == nullptr) {
        return;
    }
    // 先访问根节点
    cout << head->val << " ";
    // 再递归访问左子树
    preOrder(head->left);
    // 最后递归访问右子树
    preOrder(head->right);
}

// 中序打印所有节点，递归版
// 中序遍历顺序：左子树 -> 根节点 -> 右子树
// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
void inOrder(TreeNode* head) {
    if (head == nullptr) {
        return;
    }
    // 先递归访问左子树
    inOrder(head->left);
    // 再访问根节点
    cout << head->val << " ";
    // 最后递归访问右子树
    inOrder(head->right);
}

// 后序打印所有节点，递归版
// 后序遍历顺序：左子树 -> 右子树 -> 根节点
// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
void posOrder(TreeNode* head) {
    if (head == nullptr) {
        return;
    }
    // 先递归访问左子树
    posOrder(head->left);
    // 再递归访问右子树
    posOrder(head->right);
    // 最后访问根节点
    cout << head->val << " ";
}

// LeetCode 104. 二叉树的最大深度
// 题目链接：https://leetcode.cn/problems/maximum-depth-of-binary-tree/
// 题目描述：给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
// 解法：使用递归，树的最大深度等于左右子树最大深度的最大值加1
// 时间复杂度：O(n)，其中n是二叉树的节点数
// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
int maxDepth(TreeNode* root) {
    // 基础情况：空节点的深度为0
    if (root == nullptr) {
        return 0;
    }
    // 递归计算左右子树的最大深度
    int leftDepth = maxDepth(root->left);
    int rightDepth = maxDepth(root->right);
    // 返回左右子树最大深度的最大值加1
    return max(leftDepth, rightDepth) + 1;
}

// LeetCode 110. 平衡二叉树
// 题目链接：https://leetcode.cn/problems/balanced-binary-tree/
// 题目描述：给定一个二叉树，判断它是否是高度平衡的二叉树。
// 解法：使用递归，自底向上检查每个节点的左右子树高度差是否不超过1
// 时间复杂度：O(n)，其中n是二叉树的节点数
// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度

// 辅助函数：获取树的高度，如果不平衡则返回-1
int getHeight(TreeNode* node) {
    // 基础情况：空节点的高度为0
    if (node == nullptr) {
        return 0;
    }
    // 递归获取左子树高度
    int leftHeight = getHeight(node->left);
    // 如果左子树不平衡，直接返回-1
    if (leftHeight == -1) {
        return -1;
    }
    // 递归获取右子树高度
    int rightHeight = getHeight(node->right);
    // 如果右子树不平衡，直接返回-1
    if (rightHeight == -1) {
        return -1;
    }
    // 检查当前节点是否平衡（左右子树高度差不超过1）
    if (abs(leftHeight - rightHeight) > 1) {
        return -1;
    }
    // 返回当前节点的高度（左右子树最大高度加1）
    return max(leftHeight, rightHeight) + 1;
}

bool isBalanced(TreeNode* root) {
    return getHeight(root) != -1;
}

// LeetCode 100. 相同的树
// 题目链接：https://leetcode.cn/problems/same-tree/
// 题目描述：给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验两棵树是否相同。
// 解法：使用递归同时遍历两棵树，比较对应节点的值是否相等
// 时间复杂度：O(min(m,n))，其中m和n分别是两个二叉树的节点数
// 空间复杂度：O(min(h1,h2))，其中h1和h2分别是两个二叉树的高度
bool isSameTree(TreeNode* p, TreeNode* q) {
    // 基础情况：两个节点都为空，则相同
    if (p == nullptr && q == nullptr) {
        return true;
    }
    // 基础情况：一个节点为空，另一个不为空，则不相同
    if (p == nullptr || q == nullptr) {
        return false;
    }
    // 比较当前节点值，并递归比较左右子树
    return p->val == q->val && 
           isSameTree(p->left, q->left) && 
           isSameTree(p->right, q->right);
}

// LeetCode 101. 对称二叉树
// 题目链接：https://leetcode.cn/problems/symmetric-tree/
// 题目描述：给你一个二叉树的根节点 root ，检查它是否轴对称。
// 解法：使用递归比较左子树和右子树是否镜像对称
// 时间复杂度：O(n)，其中n是二叉树的节点数
// 空间复杂度：O(h)，其中h是二叉树的高度

// 辅助函数：判断两个树是否镜像对称
bool isMirror(TreeNode* left, TreeNode* right) {
    // 基础情况：两个节点都为空，则对称
    if (left == nullptr && right == nullptr) {
        return true;
    }
    // 基础情况：一个节点为空，另一个不为空，则不对称
    if (left == nullptr || right == nullptr) {
        return false;
    }
    // 比较当前节点值，并递归比较外侧和内侧
    return left->val == right->val && 
           isMirror(left->left, right->right) && 
           isMirror(left->right, right->left);
}

bool isSymmetric(TreeNode* root) {
    // 空树是对称的
    if (root == nullptr) {
        return true;
    }
    // 比较左右子树是否镜像对称
    return isMirror(root->left, root->right);
}

// LeetCode 226. 翻转二叉树
// 题目链接：https://leetcode.cn/problems/invert-binary-tree/
// 题目描述：给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
// 解法：使用递归，交换每个节点的左右子树
// 时间复杂度：O(n)，其中n是二叉树的节点数
// 空间复杂度：O(h)，其中h是二叉树的高度
TreeNode* invertTree(TreeNode* root) {
    // 基础情况：空节点无需翻转
    if (root == nullptr) {
        return nullptr;
    }
    // 交换左右子树
    TreeNode* temp = root->left;
    root->left = root->right;
    root->right = temp;
    // 递归翻转左右子树
    invertTree(root->left);
    invertTree(root->right);
    return root;
}

// =========================== 扩展题目部分 ===========================

// LeetCode 112. 路径总和
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/path-sum/
// 题目描述：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum。
// 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum。
// 叶子节点是指没有子节点的节点。
//
// 思路分析：
// 1. 使用递归，从根节点开始，每次递归时减去当前节点的值
// 2. 当到达叶子节点时，检查剩余的目标和是否等于叶子节点的值
// 3. 递归地检查左右子树是否存在满足条件的路径
//
// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点访问一次
// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
//
// 是否为最优解：是。递归遍历是解决此类路径问题的最优方法。
//
// 边界场景：
// - 空树：返回false
// - 只有根节点：检查根节点值是否等于targetSum
// - 负数节点值：算法依然有效
// - 目标和为0：正常处理
bool hasPathSum(TreeNode* root, int targetSum) {
    // 边界情况：空节点返回false
    if (root == nullptr) {
        return false;
    }
    // 到达叶子节点，检查路径和是否等于目标和
    if (root->left == nullptr && root->right == nullptr) {
        return root->val == targetSum;
    }
    // 递归检查左右子树，目标和减去当前节点的值
    return hasPathSum(root->left, targetSum - root->val) || 
           hasPathSum(root->right, targetSum - root->val);
}

// LeetCode 113. 路径总和 II
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/path-sum-ii/
// 题目描述：给你二叉树的根节点 root 和一个整数目标和 targetSum，
// 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
//
// 思路分析：
// 1. 使用回溯法，维护一个当前路径列表
// 2. 递归遍历树，每次将当前节点加入路径
// 3. 到达叶子节点时，检查路径和是否等于目标和，若是则将路径加入结果
// 4. 回溯时移除当前节点
//
// 时间复杂度：O(n^2)，其中n是节点数，最坏情况下需要复制所有路径
// 空间复杂度：O(n)，递归栈和路径存储的空间
//
// 是否为最优解：是。回溯+递归是解决所有路径问题的标准方法。

void pathSumHelper(TreeNode* node, int targetSum, vector<int>& path, 
                   vector<vector<int>>& result) {
    if (node == nullptr) {
        return;
    }
    // 将当前节点加入路径
    path.push_back(node->val);
    // 到达叶子节点，检查路径和
    if (node->left == nullptr && node->right == nullptr && node->val == targetSum) {
        result.push_back(path); // 复制当前路径
    }
    // 递归遍历左右子树
    pathSumHelper(node->left, targetSum - node->val, path, result);
    pathSumHelper(node->right, targetSum - node->val, path, result);
    // 回溯：移除当前节点
    path.pop_back();
}

vector<vector<int>> pathSum(TreeNode* root, int targetSum) {
    vector<vector<int>> result;
    vector<int> path;
    pathSumHelper(root, targetSum, path, result);
    return result;
}

// LeetCode 111. 二叉树的最小深度
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/minimum-depth-of-binary-tree/
// 题目描述：给定一个二叉树，找出其最小深度。
// 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
//
// 思路分析：
// 1. 使用递归，注意必须到达叶子节点才算一条路径
// 2. 如果一个节点只有左子树或只有右子树，不能简单取min，要继续递归非空子树
// 3. 只有当左右子树都存在时，才取较小深度
//
// 时间复杂度：O(n)，其中n是节点数
// 空间复杂度：O(h)，其中h是树的高度
//
// 是否为最优解：是。但BFS层序遍历也是最优解，在某些情况下更快（遇到第一个叶子节点即可返回）。
//
// 常见错误：直接用min(左深度, 右深度)会在单子树情况下出错
int minDepth(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    // 如果左子树为空，只递归右子树
    if (root->left == nullptr) {
        return minDepth(root->right) + 1;
    }
    // 如果右子树为空，只递归左子树
    if (root->right == nullptr) {
        return minDepth(root->left) + 1;
    }
    // 左右子树都存在，取较小深度
    return min(minDepth(root->left), minDepth(root->right)) + 1;
}

// LeetCode 257. 二叉树的所有路径
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/binary-tree-paths/
// 题目描述：给你一个二叉树的根节点 root，按 任意顺序，
// 返回所有从根节点到叶子节点的路径。
//
// 思路分析：
// 1. 使用递归+回溯，构建路径字符串
// 2. 到达叶子节点时，将路径字符串加入结果
// 3. 使用string可以方便地拼接路径
//
// 时间复杂度：O(n^2)，需要构建和复制路径字符串
// 空间复杂度：O(n)，递归栈和结果存储
//
// 是否为最优解：是。递归+回溯是标准解法。

void binaryTreePathsHelper(TreeNode* node, string path, vector<string>& result) {
    if (node == nullptr) {
        return;
    }
    // 构建当前路径
    path += to_string(node->val);
    // 到达叶子节点，加入结果
    if (node->left == nullptr && node->right == nullptr) {
        result.push_back(path);
        return;
    }
    // 继续递归，路径中加入箭头
    path += "->";
    binaryTreePathsHelper(node->left, path, result);
    binaryTreePathsHelper(node->right, path, result);
}

vector<string> binaryTreePaths(TreeNode* root) {
    vector<string> result;
    if (root == nullptr) {
        return result;
    }
    binaryTreePathsHelper(root, "", result);
    return result;
}

// LeetCode 543. 二叉树的直径
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/diameter-of-binary-tree/
// 题目描述：给定一棵二叉树，你需要计算它的直径长度。
// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
// 这条路径可能穿过也可能不穿过根节点。
//
// 思路分析：
// 1. 直径 = 某个节点的左子树最大深度 + 右子树最大深度
// 2. 需要递归计算每个节点的这个值，并维护全局最大值
// 3. 使用后序遍历，先计算子树深度，再更新直径
//
// 时间复杂度：O(n)，每个节点访问一次
// 空间复杂度：O(h)，递归栈深度
//
// 是否为最优解：是。一次遍历即可得到答案。

int maxDiameter = 0;

int getDepth(TreeNode* node) {
    if (node == nullptr) {
        return 0;
    }
    // 递归计算左右子树深度
    int leftDepth = getDepth(node->left);
    int rightDepth = getDepth(node->right);
    // 更新最大直径：左深度 + 右深度
    maxDiameter = max(maxDiameter, leftDepth + rightDepth);
    // 返回当前节点的深度
    return max(leftDepth, rightDepth) + 1;
}

int diameterOfBinaryTree(TreeNode* root) {
    maxDiameter = 0;
    getDepth(root);
    return maxDiameter;
}

// LeetCode 404. 左叶子之和
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/sum-of-left-leaves/
// 题目描述：给定二叉树的根节点 root，返回所有左叶子之和。
//
// 思路分析：
// 1. 递归遍历树，判断节点是否为左叶子
// 2. 左叶子的定义：是某个节点的左孩子，且该孩子没有子节点
// 3. 需要从父节点判断，而不是在节点自身判断
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
int sumOfLeftLeaves(TreeNode* root) {
    if (root == nullptr) {
        return 0;
    }
    int sum = 0;
    // 检查左子节点是否为叶子
    if (root->left != nullptr && 
        root->left->left == nullptr && 
        root->left->right == nullptr) {
        sum += root->left->val;
    }
    // 递归计算左右子树的左叶子之和
    sum += sumOfLeftLeaves(root->left);
    sum += sumOfLeftLeaves(root->right);
    return sum;
}

// LeetCode 572. 另一棵树的子树
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/subtree-of-another-tree/
// 题目描述：给你两棵二叉树 root 和 subRoot 。检验 root 中是否包含和 subRoot 具有相同结构和节点值的子树。
// 如果存在，返回 true ；否则，返回 false 。
//
// 思路分析：
// 1. 需要两个递归函数：
//    - 一个遍历主树的每个节点作为根节点
//    - 一个检查两棵树是否完全相同
// 2. 遍历主树，对于每个节点，调用相同树检查函数
//
// 时间复杂度：O(m*n)
// 空间复杂度：O(h)
//
// 是否为最优解：是

bool isSubtree(TreeNode* root, TreeNode* subRoot) {
    if (root == nullptr) return false;
    // 检查当前节点作为根的子树是否与subRoot相同
    if (isSameTree(root, subRoot)) return true;
    // 递归检查左子树或右子树
    return isSubtree(root->left, subRoot) || isSubtree(root->right, subRoot);
}

// LeetCode 617. 合并二叉树
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/merge-two-binary-trees/
// 题目描述：给你两棵二叉树： root1 和 root2 。
// 想象当你将其中一棵覆盖到另一棵之上时，两棵树上的一些节点将会重叠（而另一些不会）。
// 你需要将这两棵树合并成一棵新二叉树。合并的规则是：如果两个节点重叠，
// 那么将这两个节点的值相加作为合并后节点的新值；否则，不为 null 的节点将直接作为新二叉树的节点。
//
// 思路分析：
// 1. 递归合并两棵树的对应节点
// 2. 如果其中一个节点为空，返回另一个节点
// 3. 否则，将两个节点的值相加，并递归合并左右子树
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
TreeNode* mergeTrees(TreeNode* root1, TreeNode* root2) {
    if (root1 == nullptr) return root2;
    if (root2 == nullptr) return root1;
    
    // 创建新节点，值为两个节点之和
    TreeNode* merged = new TreeNode(root1->val + root2->val);
    // 递归合并左右子树
    merged->left = mergeTrees(root1->left, root2->left);
    merged->right = mergeTrees(root1->right, root2->right);
    
    return merged;
}

// LeetCode 654. 最大二叉树
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/maximum-binary-tree/
// 题目描述：给定一个不重复的整数数组 nums 。 最大二叉树 可以用下面的算法从 nums 递归地构建:
// 1. 创建一个根节点，其值为 nums 中的最大值。
// 2. 递归地在最大值 左边 的 子数组前缀上 构建左子树。
// 3. 递归地在最大值 右边 的 子数组后缀上 构建右子树。
// 返回构建的最大二叉树。
//
// 思路分析：
// 1. 找到当前数组中的最大值及其索引
// 2. 创建根节点，其值为最大值
// 3. 递归构建左右子树
//
// 时间复杂度：O(n^2)，最坏情况数组是递增或递减的
// 空间复杂度：O(h)
//
// 是否为最优解：不是最优解。最优解可以使用单调栈将时间复杂度降为O(n)
TreeNode* constructMaximumBinaryTree(vector<int>& nums, int left, int right) {
    if (left > right) return nullptr;
    
    // 找到最大值的索引
    int maxIdx = left;
    for (int i = left + 1; i <= right; i++) {
        if (nums[i] > nums[maxIdx]) {
            maxIdx = i;
        }
    }
    
    // 创建根节点
    TreeNode* root = new TreeNode(nums[maxIdx]);
    // 递归构建左右子树
    root->left = constructMaximumBinaryTree(nums, left, maxIdx - 1);
    root->right = constructMaximumBinaryTree(nums, maxIdx + 1, right);
    
    return root;
}

TreeNode* constructMaximumBinaryTree(vector<int>& nums) {
    return constructMaximumBinaryTree(nums, 0, nums.size() - 1);
}

// LeetCode 563. 二叉树的坡度
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/binary-tree-tilt/
// 题目描述：给定一个二叉树，计算 整个树 的坡度 。
// 一个树的 节点的坡度 定义即为，该节点左子树的节点之和和右子树节点之和的 差的绝对值 。
// 如果没有左子树的话，左子树的节点之和为 0 ；没有右子树的话也是一样。空结点的坡度是 0 。
// 整个树 的坡度就是其所有节点的坡度之和。
//
// 思路分析：
// 1. 使用后序遍历计算每个子树的节点和
// 2. 同时计算每个节点的坡度，并累加到全局变量中
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
int totalTilt = 0;

int findTiltHelper(TreeNode* node) {
    if (node == nullptr) return 0;
    
    // 计算左右子树的节点和
    int leftSum = findTiltHelper(node->left);
    int rightSum = findTiltHelper(node->right);
    
    // 计算当前节点的坡度并更新全局总和
    totalTilt += abs(leftSum - rightSum);
    
    // 返回当前子树的节点和
    return leftSum + rightSum + node->val;
}

int findTilt(TreeNode* root) {
    totalTilt = 0;
    findTiltHelper(root);
    return totalTilt;
}

// LeetCode 508. 出现次数最多的子树元素和
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/most-frequent-subtree-sum/
// 题目描述：给你一个二叉树的根结点 root ，请返回出现次数最多的子树元素和。
// 如果有多个元素出现的次数相同，返回所有出现次数最多的子树元素和（不限顺序）。
//
// 思路分析：
// 1. 使用后序遍历计算每个子树的元素和
// 2. 使用哈希表统计每个和出现的次数
// 3. 找出出现次数最多的和
//
// 时间复杂度：O(n)
// 空间复杂度：O(n)
//
// 是否为最优解：是
unordered_map<int, int> sumFreq;

int subTreeSum(TreeNode* node) {
    if (node == nullptr) return 0;
    
    // 计算左右子树的和加上当前节点值
    int sum = node->val + subTreeSum(node->left) + subTreeSum(node->right);
    // 更新频率
    sumFreq[sum]++;
    
    return sum;
}

vector<int> findFrequentTreeSum(TreeNode* root) {
    sumFreq.clear();
    subTreeSum(root);
    
    vector<int> result;
    int maxFreq = 0;
    // 找出最大频率
    for (auto& pair : sumFreq) {
        if (pair.second > maxFreq) {
            maxFreq = pair.second;
        }
    }
    // 找出所有出现最大频率的和
    for (auto& pair : sumFreq) {
        if (pair.second == maxFreq) {
            result.push_back(pair.first);
        }
    }
    
    return result;
}

// LeetCode 437. 路径总和 III
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/path-sum-iii/
// 题目描述：给定一个二叉树的根节点 root，和一个整数 targetSum，
// 求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
// 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只从父节点到子节点）。
//
// 思路分析：
// 方法1：双重递归 - 需要两个递归函数
//   1. 第一个递归遍历所有节点
//   2. 第二个递归以当前节点为起点计算路径数
//
// 时间复杂度：O(n^2)
// 空间复杂度：O(h)
int pathSumStartWithRoot(TreeNode* root, long long sum) {
    if (root == nullptr) return 0;
    
    int count = 0;
    if (root->val == sum) count++;
    
    count += pathSumStartWithRoot(root->left, sum - root->val);
    count += pathSumStartWithRoot(root->right, sum - root->val);
    
    return count;
}

int pathSumIII(TreeNode* root, int targetSum) {
    if (root == nullptr) return 0;
    
    // 计算以当前节点为起点的路径数
    int count = pathSumStartWithRoot(root, (long long)targetSum);
    // 递归计算左右子树
    count += pathSumIII(root->left, targetSum);
    count += pathSumIII(root->right, targetSum);
    
    return count;
}

// 方法2：前缀和+HashMap - 更优的解法
// 时间复杂度：O(n)
// 空间复杂度：O(n)
int pathSumIII_OptimalHelper(TreeNode* node, long long currSum, int target, unordered_map<long long, int>& prefixSum) {
    if (node == nullptr) return 0;
    
    // 更新当前路径和
    currSum += node->val;
    
    // 计算有多少条路径的和等于target
    int count = 0;
    if (prefixSum.find(currSum - target) != prefixSum.end()) {
        count = prefixSum[currSum - target];
    }
    
    // 更新前缀和的频率
    prefixSum[currSum]++;
    
    // 递归计算左右子树
    count += pathSumIII_OptimalHelper(node->left, currSum, target, prefixSum);
    count += pathSumIII_OptimalHelper(node->right, currSum, target, prefixSum);
    
    // 回溯，移除当前路径和
    prefixSum[currSum]--;
    if (prefixSum[currSum] == 0) {
        prefixSum.erase(currSum);
    }
    
    return count;
}

int pathSumIII_Optimal(TreeNode* root, int targetSum) {
    unordered_map<long long, int> prefixSum;
    prefixSum[0] = 1; // 空路径的和为0
    return pathSumIII_OptimalHelper(root, 0, targetSum, prefixSum);
}

// LeetCode 236. 最近公共祖先
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
// 题目描述：给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
// 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
// 满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
//
// 思路分析：
// 1. 递归查找p和q，当找到一个节点是p或q时返回
// 2. 如果左右子树返回值都不为null，说明当前节点就是LCA
// 3. 如果只有一侧返回值不为null，返回那个不为null的值
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
    if (root == nullptr || root == p || root == q) return root;
    
    // 在左右子树中查找p和q
    TreeNode* left = lowestCommonAncestor(root->left, p, q);
    TreeNode* right = lowestCommonAncestor(root->right, p, q);
    
    // 如果左右子树都找到了，说明当前节点就是LCA
    if (left != nullptr && right != nullptr) return root;
    
    // 否则返回找到的一侧（如果都没找到，就返回nullptr）
    return left != nullptr ? left : right;
}

// LeetCode 124. 二叉树中的最大路径和
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
// 题目描述：二叉树中的 路径 被定义为一条从树中任意节点出发，
// 沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次。
// 该路径 至少包含一个 节点，且不一定经过根节点。
// 路径和 是路径中各节点值的总和。给你一个二叉树的根节点 root ，返回其 最大路径和。
//
// 思路分析：
// 1. 对于每个节点，最大路径和可能是：
//    - 左子树的最大贡献 + 节点值 + 右子树的最大贡献
// 2. 但返回给父节点时，只能选择左或右一侧（因为路径不能分叉）
// 3. 如果子树贡献为负，则不选择该子树（贡献为0）
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
int maxPathSumValue = INT_MIN;

int maxGain(TreeNode* node) {
    if (node == nullptr) return 0;
    
    // 递归计算左右子树的最大贡献，负数则不选
    int leftGain = max(maxGain(node->left), 0);
    int rightGain = max(maxGain(node->right), 0);
    
    // 更新全局最大路径和
    int currentPathSum = leftGain + node->val + rightGain;
    maxPathSumValue = max(maxPathSumValue, currentPathSum);
    
    // 返回节点的最大贡献
    return node->val + max(leftGain, rightGain);
}

int maxPathSum(TreeNode* root) {
    maxPathSumValue = INT_MIN;
    maxGain(root);
    return maxPathSumValue;
}



// LintCode 453. 将二叉树拆分为链表
// 题目来源：LintCode（炼码）
// 题目链接：https://www.lintcode.com/problem/453/
// 题目描述：将一棵二叉树按照前序遍历拆解成为一个假链表。所谓的假链表是说，用二叉树的 right 指针，来表示链表中的 next 指针。
// 要求不能创建任何新的节点，只能调整树中节点指针的指向。
//
// 思路分析：
// 1. 使用后序遍历，先处理左右子树
// 2. 对于每个节点，将左子树变成链表，右子树变成链表
// 3. 将左子树链接到右子树上，左子树指针设为nullptr
// 4. 返回当前链表的末尾节点
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
TreeNode* flattenHelper(TreeNode* node) {
    if (node == nullptr) return nullptr;
    
    // 叶子节点直接返回
    if (node->left == nullptr && node->right == nullptr) {
        return node;
    }
    
    // 递归处理左右子树
    TreeNode* leftTail = flattenHelper(node->left);
    TreeNode* rightTail = flattenHelper(node->right);
    
    // 如果左子树不为空，将左子树连接到右子树上
    if (leftTail != nullptr) {
        leftTail->right = node->right;
        node->right = node->left;
        node->left = nullptr;
    }
    
    // 返回新的链表末尾节点
    return rightTail != nullptr ? rightTail : leftTail;
}

void flatten(TreeNode* root) {
    if (root == nullptr) return;
    flattenHelper(root);
}

// HackerRank 二叉树的镜像
// 题目来源：HackerRank
// 题目描述：给定一棵二叉树，判断它是否是自身的镜像（即对称）
//
// 思路分析：
// 1. 使用辅助函数检查两棵子树是否互为镜像
// 2. 两棵子树互为镜像的条件：
//    - 当前节点值相等
//    - 左子树的左子树与右子树的右子树互为镜像
//    - 左子树的右子树与右子树的左子树互为镜像
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
bool isSymmetricAdvanced(TreeNode* root) {
    if (root == nullptr) return true;
    return isMirror(root->left, root->right);
}

// 剑指Offer 26. 树的子结构
// 题目来源：剑指Offer
// 题目描述：输入两棵二叉树A和B，判断B是不是A的子结构。
//
// 思路分析：
// 1. 遍历树A的每个节点，以该节点为根节点
// 2. 检查从该节点开始的子树是否包含树B的结构
//
// 时间复杂度：O(m*n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
bool isSubStructureHelper(TreeNode* A, TreeNode* B) {
    if (B == nullptr) return true;
    if (A == nullptr || A->val != B->val) return false;
    
    return isSubStructureHelper(A->left, B->left) && 
           isSubStructureHelper(A->right, B->right);
}

bool isSubStructure(TreeNode* A, TreeNode* B) {
    if (A == nullptr || B == nullptr) return false;
    
    return isSubStructureHelper(A, B) || 
           isSubStructure(A->left, B) || 
           isSubStructure(A->right, B);
}

// USACO 二叉搜索树的最近公共祖先
// 题目来源：USACO（美国计算机奥林匹克竞赛）
// 题目描述：给定一个二叉搜索树（BST），找到该树中两个指定节点的最近公共祖先。
//
// 思路分析：
// 利用BST的特性：左子树所有节点值小于根节点，右子树所有节点值大于根节点
// 1. 如果p和q的值都小于当前节点，那么LCA在左子树
// 2. 如果p和q的值都大于当前节点，那么LCA在右子树
// 3. 否则，当前节点就是LCA
//
// 时间复杂度：O(h)
// 空间复杂度：O(h)
//
// 是否为最优解：是
TreeNode* lowestCommonAncestorBST(TreeNode* root, TreeNode* p, TreeNode* q) {
    if (root == nullptr || p == nullptr || q == nullptr) return nullptr;
    
    if (p->val < root->val && q->val < root->val) {
        return lowestCommonAncestorBST(root->left, p, q);
    }
    if (p->val > root->val && q->val > root->val) {
        return lowestCommonAncestorBST(root->right, p, q);
    }
    
    return root;
}

// AtCoder ABC191 E. Come Back Quickly - 距离和计算
// 题目描述简化：给定一棵有根树，计算每个节点到其所有子孙节点的距离之和
//
// 思路分析：
// 1. 使用后序遍历计算每个子树的节点数
// 2. 使用前序遍历计算距离之和
//
// 时间复杂度：O(n)
// 空间复杂度：O(n)
//
// 是否为最优解：是
int dfsSize(TreeNode* node, vector<int>& size) {
    if (node == nullptr) return 0;
    
    size[node->val] = 1; // 包含自己
    size[node->val] += dfsSize(node->left, size);
    size[node->val] += dfsSize(node->right, size);
    
    return size[node->val];
}

void dfsDistance(TreeNode* node, vector<int>& size, vector<long long>& result, long long parentDistance) {
    if (node == nullptr) return;
    
    result[node->val] = parentDistance;
    
    if (node->left != nullptr) {
        int leftSize = size[node->left->val];
        int rightSize = node->right != nullptr ? size[node->right->val] : 0;
        long long leftDistance = parentDistance + (size[node->val] - leftSize) - leftSize;
        dfsDistance(node->left, size, result, leftDistance);
    }
    
    if (node->right != nullptr) {
        int rightSize = size[node->right->val];
        int leftSize = node->left != nullptr ? size[node->left->val] : 0;
        long long rightDistance = parentDistance + (size[node->val] - rightSize) - rightSize;
        dfsDistance(node->right, size, result, rightDistance);
    }
}

vector<long long> calculateDistanceSum(TreeNode* root, int n) {
    vector<long long> result(n, 0);
    vector<int> size(n, 0);
    
    dfsSize(root, size);
    dfsDistance(root, size, result, 0);
    
    return result;
}

// CodeChef - SUBTREE - 最大子树和
// 题目描述简化：给定一棵二叉树，每个节点有一个权值。找出权值和最大的子树。
//
// 思路分析：
// 1. 使用后序遍历，计算每个子树的权值和
// 2. 对于每个节点，其最大子树和为：节点值 + max(左子树最大和, 0) + max(右子树最大和, 0)
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是
int maxSubtreeSumValue = INT_MIN;

int calculateSubtreeSum(TreeNode* node) {
    if (node == nullptr) return 0;
    
    int leftSum = max(calculateSubtreeSum(node->left), 0);
    int rightSum = max(calculateSubtreeSum(node->right), 0);
    
    int currentSum = node->val + leftSum + rightSum;
    maxSubtreeSumValue = max(maxSubtreeSumValue, currentSum);
    
    return node->val + max(leftSum, rightSum);
}

int maxSubtreeSum(TreeNode* root) {
    maxSubtreeSumValue = INT_MIN;
    calculateSubtreeSum(root);
    return maxSubtreeSumValue;
}

// UVa OJ 10080 - 重建二叉树
// 题目描述简化：根据前序遍历和中序遍历结果重建二叉树
//
// 思路分析：
// 1. 前序遍历的第一个节点是根节点
// 2. 在中序遍历中找到根节点的位置，分割左右子树
// 3. 递归重建左右子树
//
// 时间复杂度：O(n^2)
// 空间复杂度：O(n)
//
// 最优解：可以使用哈希表优化查找根节点的过程
TreeNode* buildTreeHelper(vector<int>& preorder, int preStart, int preEnd,
                         vector<int>& inorder, int inStart, int inEnd) {
    if (preStart > preEnd || inStart > inEnd) return nullptr;
    
    TreeNode* root = new TreeNode(preorder[preStart]);
    
    // 找到根节点在中序遍历中的位置
    int rootIndex = inStart;
    for (; rootIndex <= inEnd; rootIndex++) {
        if (inorder[rootIndex] == root->val) {
            break;
        }
    }
    
    int leftSize = rootIndex - inStart;
    
    root->left = buildTreeHelper(preorder, preStart + 1, preStart + leftSize,
                                inorder, inStart, rootIndex - 1);
    root->right = buildTreeHelper(preorder, preStart + leftSize + 1, preEnd,
                                 inorder, rootIndex + 1, inEnd);
    
    return root;
}

TreeNode* buildTree(vector<int>& preorder, vector<int>& inorder) {
    if (preorder.empty() || inorder.empty()) return nullptr;
    return buildTreeHelper(preorder, 0, preorder.size() - 1, 
                          inorder, 0, inorder.size() - 1);
}

// 牛客网 NC102. 树的序列化和反序列化
// 题目描述：将二叉树序列化为字符串，然后从字符串反序列化回二叉树
//
// 思路分析：
// 序列化使用前序遍历，空节点用特殊字符表示
//
// 时间复杂度：O(n)
// 空间复杂度：O(n)
//
// 是否为最优解：是
void serializeHelper(TreeNode* node, string& result) {
    if (node == nullptr) {
        result += "#,";
        return;
    }
    
    result += to_string(node->val) + ",";
    serializeHelper(node->left, result);
    serializeHelper(node->right, result);
}

string serialize(TreeNode* root) {
    string result;
    serializeHelper(root, result);
    return result;
}

TreeNode* deserializeHelper(vector<string>& nodes, int& index) {
    if (index >= nodes.size() || nodes[index] == "#") {
        index++;
        return nullptr;
    }
    
    TreeNode* node = new TreeNode(stoi(nodes[index++]));
    node->left = deserializeHelper(nodes, index);
    node->right = deserializeHelper(nodes, index);
    
    return node;
}

TreeNode* deserialize(string data) {
    if (data.empty()) return nullptr;
    
    vector<string> nodes;
    stringstream ss(data);
    string item;
    while (getline(ss, item, ',')) {
        nodes.push_back(item);
    }
    
    int index = 0;
    return deserializeHelper(nodes, index);
}

// 杭电OJ 2024 - 二叉树遍历
// 题目描述：输入二叉树的前序遍历和中序遍历结果，输出其后序遍历结果
//
// 思路分析：
// 1. 先根据前序和中序构建二叉树
// 2. 然后进行后序遍历输出
//
// 时间复杂度：O(n^2)
// 空间复杂度：O(n)
//
// 最优解：可以使用哈希表优化查找过程
void postorderHelper(const string& preorder, int preStart, int preEnd,
                    const string& inorder, int inStart, int inEnd,
                    string& result) {
    if (preStart > preEnd || inStart > inEnd) return;
    
    char rootVal = preorder[preStart];
    
    // 找到根节点在中序中的位置
    int rootIndex = inStart;
    for (; rootIndex <= inEnd; rootIndex++) {
        if (inorder[rootIndex] == rootVal) {
            break;
        }
    }
    
    int leftLength = rootIndex - inStart;
    
    // 递归处理左右子树
    postorderHelper(preorder, preStart + 1, preStart + leftLength,
                   inorder, inStart, rootIndex - 1, result);
    postorderHelper(preorder, preStart + leftLength + 1, preEnd,
                   inorder, rootIndex + 1, inEnd, result);
    
    // 后序：添加根节点
    result += rootVal;
}

string postorderFromPreorderAndInorder(const string& preorder, const string& inorder) {
    if (preorder.empty() || inorder.empty()) return "";
    
    string result;
    postorderHelper(preorder, 0, preorder.size() - 1,
                   inorder, 0, inorder.size() - 1, result);
    
    return result;
}

// LeetCode 226. 翻转二叉树
// 题目来源：LeetCode
// 题目链接：https://leetcode.cn/problems/invert-binary-tree/
// 题目描述：给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
//
// 思路分析：
// 1. 递归翻转左右子树
// 2. 交换左右子树的位置
//
// 时间复杂度：O(n)
// 空间复杂度：O(h)
//
// 是否为最优解：是


// 主函数测试
int main() {
    cout << "========== 二叉树递归遍历基础测试 ==========" << endl;
    TreeNode* head = new TreeNode(1);
    head->left = new TreeNode(2);
    head->right = new TreeNode(3);
    head->left->left = new TreeNode(4);
    head->left->right = new TreeNode(5);
    head->right->left = new TreeNode(6);
    head->right->right = new TreeNode(7);

    cout << "前序遍历：";
    preOrder(head);
    cout << endl;

    cout << "中序遍历：";
    inOrder(head);
    cout << endl;

    cout << "后序遍历：";
    posOrder(head);
    cout << endl;

    cout << "\n========== LeetCode 104. 最大深度 ==========" << endl;
    cout << "最大深度: " << maxDepth(head) << endl; // 预期: 3

    cout << "\n========== LeetCode 110. 平衡二叉树 ==========" << endl;
    TreeNode* balancedTree = new TreeNode(1);
    balancedTree->left = new TreeNode(2);
    balancedTree->right = new TreeNode(3);
    balancedTree->left->left = new TreeNode(4);
    balancedTree->left->right = new TreeNode(5);
    cout << "是否为平衡二叉树: " << (isBalanced(balancedTree) ? "true" : "false") << endl;

    cout << "\n========== LeetCode 112. 路径总和 ==========" << endl;
    TreeNode* pathTree = new TreeNode(5);
    pathTree->left = new TreeNode(4);
    pathTree->right = new TreeNode(8);
    pathTree->left->left = new TreeNode(11);
    pathTree->left->left->left = new TreeNode(7);
    pathTree->left->left->right = new TreeNode(2);
    cout << "是否存在路径和为22: " << (hasPathSum(pathTree, 22) ? "true" : "false") << endl;

    cout << "\n========== 所有测试完成！ ==========" << endl;

    return 0;
}

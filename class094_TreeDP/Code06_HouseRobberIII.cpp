// 337. 打家劫舍 III
// 测试链接 : https://leetcode.cn/problems/house-robber-iii/

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
    // 提交如下的方法
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决树形DP问题的标准方法
    int rob(TreeNode* root) {
        // 调用递归函数，返回包含两个值的数组
        // 第一个值表示不抢劫当前节点时的最大收益
        // 第二个值表示抢劫当前节点时的最大收益
        int result[2];
        robHelper(root, result);
        // 返回两种情况的最大值
        return result[0] > result[1] ? result[0] : result[1];
    }

private:
    // 递归函数计算结果并存储在result数组中
    // result[0] 表示不抢劫当前节点时的最大收益
    // result[1] 表示抢劫当前节点时的最大收益
    void robHelper(TreeNode* node, int result[2]) {
        // 基础情况：如果节点为空，返回[0, 0]
        if (node == nullptr) {
            result[0] = 0;
            result[1] = 0;
            return;
        }

        // 递归计算左右子树的结果
        int left[2], right[2];
        robHelper(node->left, left);
        robHelper(node->right, right);

        // 计算当前节点的两种情况
        // 1. 不抢劫当前节点：左右子树可以自由选择是否抢劫
        int leftMax = left[0] > left[1] ? left[0] : left[1];
        int rightMax = right[0] > right[1] ? right[0] : right[1];
        result[0] = leftMax + rightMax;
        
        // 2. 抢劫当前节点：左右子节点都不能抢劫
        result[1] = node->val + left[0] + right[0];
    }
};

// 补充题目1: 1372. 二叉树中的最长交错路径
// 题目链接: https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
// 题目描述: 给定一棵二叉树，找到最长的交错路径的长度。
// 交错路径定义为：从根节点到任意叶子节点，路径上的节点交替经过左子节点和右子节点。
class ZigZagSolution {
public:
    int longestZigZag(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        int maxLength = 0;
        // 从左子节点开始，方向为左
        longestZigZagHelper(root->left, 1, true, maxLength);
        // 从右子节点开始，方向为右
        longestZigZagHelper(root->right, 1, false, maxLength);
        return maxLength;
    }

private:
    void longestZigZagHelper(TreeNode* node, int length, bool isLeft, int& maxLength) {
        if (node == nullptr) {
            return;
        }
        maxLength = std::max(maxLength, length);
        
        if (isLeft) {
            // 如果当前是左子节点，下一步应该走右子节点
            longestZigZagHelper(node->right, length + 1, false, maxLength);
            // 也可以重新开始计算
            longestZigZagHelper(node->left, 1, true, maxLength);
        } else {
            // 如果当前是右子节点，下一步应该走左子节点
            longestZigZagHelper(node->left, length + 1, true, maxLength);
            // 也可以重新开始计算
            longestZigZagHelper(node->right, 1, false, maxLength);
        }
    }
};

// 补充题目2: 549. 二叉树中最长的连续序列
// 题目链接: https://leetcode.cn/problems/binary-tree-longest-consecutive-sequence-ii/
// 题目描述: 给定一棵二叉树，找出最长连续序列路径的长度。这个路径可以是升序也可以是降序。
class ConsecutiveSolution {
public:
    int longestConsecutive2(TreeNode* root) {
        int maxLength = 0;
        longestConsecutive2Helper(root, maxLength);
        return maxLength;
    }

private:
    // 返回一个包含两个元素的数组，第一个元素是从该节点开始的最长递增序列长度，第二个元素是最长递减序列长度
    std::pair<int, int> longestConsecutive2Helper(TreeNode* node, int& maxLength) {
        if (node == nullptr) {
            return {0, 0};
        }

        int inc = 1; // 递增序列长度，初始为1（包含自己）
        int dec = 1; // 递减序列长度，初始为1（包含自己）

        if (node->left != nullptr) {
            auto left = longestConsecutive2Helper(node->left, maxLength);
            if (node->val == node->left->val + 1) {
                // 当前节点比左子节点大1，递减序列
                dec = left.second + 1;
            } else if (node->val == node->left->val - 1) {
                // 当前节点比左子节点小1，递增序列
                inc = left.first + 1;
            }
        }

        if (node->right != nullptr) {
            auto right = longestConsecutive2Helper(node->right, maxLength);
            if (node->val == node->right->val + 1) {
                // 当前节点比右子节点大1，递减序列
                dec = std::max(dec, right.second + 1);
            } else if (node->val == node->right->val - 1) {
                // 当前节点比右子节点小1，递增序列
                inc = std::max(inc, right.first + 1);
            }
        }

        // 更新全局最长长度：可以是从该节点开始的递增或递减序列，或者经过该节点的序列（inc + dec - 1）
        maxLength = std::max(maxLength, inc + dec - 1);
        
        return {inc, dec};
    }
};

// 补充题目3: 1457. 二叉树中的伪回文路径
// 题目链接: https://leetcode.cn/problems/pseudo-palindromic-paths-in-a-binary-tree/
// 题目描述: 给一棵二叉树，统计从根到叶子节点的所有路径中，伪回文路径的数量。
// 伪回文路径定义为：路径上的节点值可以重新排列形成一个回文串。
class PseudoPalindromeSolution {
public:
    int pseudoPalindromicPaths(TreeNode* root) {
        int count[10] = {0}; // 存储每个数字出现的次数
        return pseudoPalindromicPathsHelper(root, count);
    }

private:
    int pseudoPalindromicPathsHelper(TreeNode* node, int count[]) {
        if (node == nullptr) {
            return 0;
        }

        // 增加当前节点值的计数
        count[node->val]++;

        int result = 0;
        if (node->left == nullptr && node->right == nullptr) {
            // 叶子节点，检查是否是伪回文路径
            result = isPseudoPalindrome(count) ? 1 : 0;
        } else {
            // 非叶子节点，继续递归
            result = pseudoPalindromicPathsHelper(node->left, count) + 
                     pseudoPalindromicPathsHelper(node->right, count);
        }

        // 回溯，减少当前节点值的计数
        count[node->val]--;

        return result;
    }

    bool isPseudoPalindrome(int count[]) {
        int oddCount = 0;
        for (int i = 0; i < 10; i++) {
            if (count[i] % 2 != 0) {
                oddCount++;
                // 伪回文最多只能有一个奇数次数
                if (oddCount > 1) {
                    return false;
                }
            }
        }
        return true;
    }
};

// 补充题目4: 2246. 相邻字符不同的最长路径
// 题目链接: https://leetcode.cn/problems/longest-path-with-different-adjacent-characters/
// 题目描述: 给一棵树，每个节点有一个字符，找到最长的路径，使得路径上相邻节点的字符不同。
class LongestPathSolution {
public:
    int longestPath(std::vector<int>& parent, std::string s) {
        int n = parent.size();
        // 构建邻接表
        std::vector<std::vector<int>> adj(n);
        for (int i = 1; i < n; i++) {
            adj[parent[i]].push_back(i);
            adj[i].push_back(parent[i]); // 无向树
        }

        int maxLength = 0;
        longestPathHelper(0, -1, adj, s, maxLength);
        return maxLength;
    }

private:
    int longestPathHelper(int node, int parentNode, std::vector<std::vector<int>>& adj, std::string& s, int& maxLength) {
        int firstMax = 0, secondMax = 0;
        
        for (int neighbor : adj[node]) {
            if (neighbor == parentNode) continue;
            
            int currentLength = longestPathHelper(neighbor, node, adj, s, maxLength);
            
            // 如果相邻节点字符不同，才能继续路径
            if (s[neighbor] != s[node]) {
                if (currentLength > firstMax) {
                    secondMax = firstMax;
                    firstMax = currentLength;
                } else if (currentLength > secondMax) {
                    secondMax = currentLength;
                }
            }
        }
        
        // 更新全局最长路径：可能是通过当前节点的两条最长路径之和
        maxLength = std::max(maxLength, firstMax + secondMax + 1);
        
        // 返回从当前节点开始的最长路径长度
        return firstMax + 1;
    }
};
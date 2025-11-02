// 543. 二叉树的直径
// 测试链接 : https://leetcode.cn/problems/diameter-of-binary-tree/

#include <vector>
#include <map>
#include <algorithm>
#include <string>

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
    // 是否为最优解: 是，这是计算二叉树直径的标准方法
    int diameterOfBinaryTree(TreeNode* root) {
        maxDiameter = 0;
        depth(root);
        return maxDiameter;
    }

private:
    // 全局变量，记录最大直径
    int maxDiameter;

    // 计算以node为根的子树的深度
    // 在计算过程中更新最大直径
    int depth(TreeNode* node) {
        // 基础情况：空节点的深度为0
        if (node == nullptr) {
            return 0;
        }

        // 递归计算左右子树的深度
        int leftDepth = depth(node->left);
        int rightDepth = depth(node->right);

        // 更新最大直径：左子树深度 + 右子树深度
        int currentDiameter = leftDepth + rightDepth;
        if (currentDiameter > maxDiameter) {
            maxDiameter = currentDiameter;
        }

        // 返回当前节点的深度：左右子树深度的最大值 + 1
        return (leftDepth > rightDepth ? leftDepth : rightDepth) + 1;
    }
};

// 补充题目1: 1245. 树的直径（N叉树/无向树版本）
// 题目链接: https://leetcode.cn/problems/tree-diameter/
// 题目描述: 给一棵无向树，找到树中最长路径的长度。
// 注意：树中的最长路径可能不经过根节点，这与二叉树的直径定义相同。
class TreeDiameterSolution {
public:
    int treeDiameter(std::vector<std::vector<int>>& edges) {
        if (edges.empty()) {
            return 0;
        }
        
        // 构建邻接表表示的图
        std::map<int, std::vector<int>> graph;
        for (const auto& edge : edges) {
            graph[edge[0]].push_back(edge[1]);
            graph[edge[1]].push_back(edge[0]);
        }
        
        int maxDistance = 0;
        // 第一次DFS找到离任意节点最远的节点
        std::pair<int, int> first = dfs(graph, -1, 0, maxDistance);
        maxDistance = 0;
        // 第二次DFS从最远节点出发找到真正的最长路径
        dfs(graph, -1, first.first, maxDistance);
        
        return maxDistance;
    }

private:
    // 返回最远节点和距离的pair
    std::pair<int, int> dfs(const std::map<int, std::vector<int>>& graph, int parent, int node, int& maxDistance) {
        std::pair<int, int> result = {node, 0}; // 默认最远节点是自己，距离为0
        
        const auto& neighbors = graph.at(node);
        for (int neighbor : neighbors) {
            if (neighbor != parent) { // 避免回到父节点
                int currentDistance = 0;
                std::pair<int, int> current = dfs(graph, node, neighbor, currentDistance);
                int distance = currentDistance + 1;
                
                if (distance > result.second) { // 更新最长距离和最远节点
                    result.first = current.first;
                    result.second = distance;
                }
            }
        }
        
        maxDistance = result.second; // 更新当前路径的最大距离
        return result;
    }
};

// 补充题目2: 1522. N叉树的直径
// 题目链接: https://leetcode.cn/problems/diameter-of-n-ary-tree/
// 题目描述: 给定一棵N叉树，找到树中最长路径的长度。
// 注意：这里的路径是两个节点之间的边数。
class Node {
public:
    int val;
    std::vector<Node*> children;

    Node() {}

    Node(int _val) {
        val = _val;
    }

    Node(int _val, std::vector<Node*> _children) {
        val = _val;
        children = _children;
    }
};

class NaryTreeDiameterSolution {
public:
    int diameter(Node* root) {
        maxDiameter = 0;
        if (root == nullptr) {
            return 0;
        }
        height(root);
        return maxDiameter;
    }

private:
    int maxDiameter; // 用于存储N叉树的最大直径
    
    int height(Node* node) {
        if (node == nullptr) {
            return 0;
        }
        
        int firstMax = 0, secondMax = 0; // 记录前两个最大的子树高度
        for (Node* child : node->children) {
            int h = height(child);
            if (h > firstMax) {
                secondMax = firstMax;
                firstMax = h;
            } else if (h > secondMax) {
                secondMax = h;
            }
        }
        
        // 最大直径是两个最深子树的高度之和
        maxDiameter = std::max(maxDiameter, firstMax + secondMax);
        
        // 返回当前节点的高度
        return firstMax + 1;
    }
};

// 补充题目3: 687. 最长同值路径
// 题目链接: https://leetcode.cn/problems/longest-univalue-path/
// 题目描述: 给定一棵二叉树，找出最长的路径，该路径上的每个节点都具有相同的值。
// 注意：这条路径可以经过也可以不经过根节点。
class LongestUnivaluePathSolution {
public:
    int longestUnivaluePath(TreeNode* root) {
        maxLength = 0;
        helper(root);
        return maxLength;
    }

private:
    int maxLength;
    
    int helper(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的最长同值路径长度
        int leftLength = 0;
        if (node->left != nullptr) {
            int left = helper(node->left);
            // 如果左子节点的值与当前节点相同，更新长度
            if (node->left->val == node->val) {
                leftLength = left + 1;
            }
        }
        
        int rightLength = 0;
        if (node->right != nullptr) {
            int right = helper(node->right);
            // 如果右子节点的值与当前节点相同，更新长度
            if (node->right->val == node->val) {
                rightLength = right + 1;
            }
        }
        
        // 更新最长同值路径长度，考虑经过当前节点的路径
        maxLength = std::max(maxLength, leftLength + rightLength);
        
        // 返回从当前节点出发的最长同值路径长度
        return std::max(leftLength, rightLength);
    }
};

// 补充题目4: 2222. 选择建筑的方案数
// 题目链接: https://leetcode.cn/problems/number-of-ways-to-select-buildings/
// 题目描述: 给定一个二进制字符串s，找出所有满足以下条件的三元组(i, j, k)：
// i < j < k，且s[i], s[j], s[k] 构成交替序列（即 "010" 或 "101"）
class BuildingSelectionSolution {
public:
    long long numberOfWays(std::string s) {
        // 0的总数，1的总数
        long long total0 = 0, total1 = 0;
        for (char c : s) {
            if (c == '0') total0++;
            else total1++;
        }
        
        // 当前已遍历的0和1的数量
        long long count0 = 0, count1 = 0;
        long long result = 0;
        
        for (char c : s) {
            if (c == '0') {
                // 选择当前0作为中间节点，左边的1的数量乘以右边的1的数量
                result += count1 * (total1 - count1);
                count0++;
            } else {
                // 选择当前1作为中间节点，左边的0的数量乘以右边的0的数量
                result += count0 * (total0 - count0);
                count1++;
            }
        }
        
        return result;
    }
};
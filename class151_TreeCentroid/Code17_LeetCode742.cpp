// LeetCode 742. 二叉树中最近的叶节点
// 题目描述：给定一个二叉树，其中每个节点都含有一个整数键，给定一个键 k，找出距离给定节点最近的叶节点
// 算法思想：将二叉树转换为无向图，然后进行广度优先搜索。对于大型树，可以先找到重心以优化搜索
// 测试链接：https://leetcode.cn/problems/closest-leaf-in-a-binary-tree/
// 时间复杂度：O(n)
// 空间复杂度：O(n)

#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <unordered_set>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

class Solution {
public:
    // 将二叉树转换为无向图
    void buildGraph(TreeNode* root, TreeNode* parent, 
                   unordered_map<int, vector<int>>& graph,
                   unordered_map<int, bool>& isLeaf) {
        if (!root) return;
        
        // 初始化邻接表
        graph[root->val] = vector<int>();
        
        // 检查是否为叶节点
        if (!root->left && !root->right) {
            isLeaf[root->val] = true;
        } else {
            isLeaf[root->val] = false;
        }
        
        // 添加与父节点的连接
        if (parent) {
            graph[root->val].push_back(parent->val);
            graph[parent->val].push_back(root->val);
        }
        
        // 递归处理左右子树
        buildGraph(root->left, root, graph, isLeaf);
        buildGraph(root->right, root, graph, isLeaf);
    }
    
    // 寻找最近的叶节点
    int findClosestLeaf(TreeNode* root, int k) {
        // 构建图和标记叶节点
        unordered_map<int, vector<int>> graph;
        unordered_map<int, bool> isLeaf;
        buildGraph(root, nullptr, graph, isLeaf);
        
        // 广度优先搜索
        queue<int> q;
        unordered_set<int> visited;
        
        q.push(k);
        visited.insert(k);
        
        while (!q.empty()) {
            int current = q.front();
            q.pop();
            
            // 如果是叶节点，返回
            if (isLeaf[current]) {
                return current;
            }
            
            // 遍历所有邻居
            for (int neighbor : graph[current]) {
                if (!visited.count(neighbor)) {
                    visited.insert(neighbor);
                    q.push(neighbor);
                }
            }
        }
        
        // 不应该到达这里
        return -1;
    }
};

// 辅助函数：释放树内存
void deleteTree(TreeNode* root) {
    if (!root) return;
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 示例1: [1, 3, 2]
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(3);
    root1->right = new TreeNode(2);
    cout << "Example 1: " << solution.findClosestLeaf(root1, 1) << endl; // Expected: 3
    deleteTree(root1);
    
    // 示例2: [1]
    TreeNode* root2 = new TreeNode(1);
    cout << "Example 2: " << solution.findClosestLeaf(root2, 1) << endl; // Expected: 1
    deleteTree(root2);
    
    // 示例3: [1,2,3,4,null,null,null,5,null,6]
    TreeNode* root3 = new TreeNode(1);
    root3->left = new TreeNode(2);
    root3->right = new TreeNode(3);
    root3->left->left = new TreeNode(4);
    root3->left->left->left = new TreeNode(5);
    root3->left->left->left->left = new TreeNode(6);
    cout << "Example 3: " << solution.findClosestLeaf(root3, 2) << endl; // Expected: 3
    deleteTree(root3);
    
    return 0;
}

// 注意：在LeetCode上提交时，需要将代码适配为LeetCode的格式
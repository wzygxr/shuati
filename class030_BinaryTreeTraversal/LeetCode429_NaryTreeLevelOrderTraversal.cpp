#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

// LeetCode 429. N 叉树的层序遍历
// 题目链接: https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
// 题目大意: 给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）
// 树的序列化输入是用层序遍历，每组子节点都由 null 值分隔

// N叉树节点定义
class Node {
public:
    int val;
    vector<Node*> children;

    Node() {}

    Node(int _val) {
        val = _val;
    }

    Node(int _val, vector<Node*> _children) {
        val = _val;
        children = _children;
    }
};

class Solution {
public:
    /**
     * 方法1: 使用BFS层序遍历
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    vector<vector<int>> levelOrder1(Node* root) {
        vector<vector<int>> result;
        if (root == nullptr) {
            return result;
        }
        
        queue<Node*> q;
        q.push(root);
        
        while (!q.empty()) {
            int size = q.size();
            vector<int> level;
            
            for (int i = 0; i < size; i++) {
                Node* node = q.front();
                q.pop();
                level.push_back(node->val);
                
                // 将所有子节点加入队列
                for (Node* child : node->children) {
                    if (child != nullptr) {
                        q.push(child);
                    }
                }
            }
            
            result.push_back(level);
        }
        
        return result;
    }
    
    /**
     * 方法2: 使用DFS递归遍历
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(h) - h是树的高度，递归调用栈的深度
     */
    vector<vector<int>> levelOrder2(Node* root) {
        vector<vector<int>> result;
        if (root == nullptr) {
            return result;
        }
        
        dfs(root, 0, result);
        return result;
    }
    
private:
    void dfs(Node* node, int level, vector<vector<int>>& result) {
        if (node == nullptr) {
            return;
        }
        
        // 如果当前层级还没有对应的列表，创建一个新的
        if (result.size() <= level) {
            result.push_back(vector<int>());
        }
        
        // 将当前节点值添加到对应层级的列表中
        result[level].push_back(node->val);
        
        // 递归处理所有子节点
        for (Node* child : node->children) {
            dfs(child, level + 1, result);
        }
    }
};
package class036;

import java.util.*;

/**
 * LintCode 69. 二叉树的层次遍历
 * 题目链接: https://www.lintcode.com/problem/69/
 * 题目大意: 给出一棵二叉树，返回其节点值的层次遍历（逐层从左到右访问所有节点）

 */
public class LintCode69_二叉树的层次遍历 {
    
    // 二叉树节点定义
    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }
    
    /**
     * 层次遍历实现
     * 思路:
     * 1. 使用队列进行层序遍历
     * 2. 从根节点开始，将节点加入队列
     * 3. 当队列不为空时，记录当前层的节点数量
     * 4. 处理当前层的所有节点，将它们的值加入当前层列表
     * 5. 将节点的左右子节点（如果存在）加入队列
     * 6. 重复步骤3-5直到队列为空
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 使用队列存储待访问的节点
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // 当队列不为空时继续遍历
        while (!queue.isEmpty()) {
            // 记录当前层的节点数量
            int size = queue.size();
            
            // 存储当前层的节点值
            List<Integer> level = new ArrayList<>();
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; i++) {
                // 取出队首节点
                TreeNode current = queue.poll();
                
                // 将当前节点的值加入当前层列表
                level.add(current.val);
                
                // 将左右子节点加入队列（如果存在）
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 将当前层的结果加入最终结果
            result.add(level);
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试二叉树:
        //     3
        //    / \
        //   9  20
        //     /  \
        //    15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        
        List<List<Integer>> result = levelOrder(root);
        System.out.println("层次遍历结果:");
        for (List<Integer> level : result) {
            System.out.println(level);
        }
        
        // 测试空树
        TreeNode emptyRoot = null;
        List<List<Integer>> emptyResult = levelOrder(emptyRoot);
        System.out.println("空树遍历结果: " + emptyResult);
    }
}

/*
Python实现:

class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left, self.right = None, None

class Solution:
    def levelOrder(self, root):
        if not root:
            return []
            
        from collections import deque
        result = []
        queue = deque([root])
        
        while queue:
            size = len(queue)
            level = []
            
            for i in range(size):
                current = queue.popleft()
                level.append(current.val)
                
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
                    
            result.append(level)
            
        return result

C++实现:

#include <iostream>
#include <vector>
#include <queue>
using namespace std;

class TreeNode {
public:
    int val;
    TreeNode *left, *right;
    TreeNode(int val) {
        this->val = val;
        this->left = this->right = NULL;
    }
};

class Solution {
public:
    vector<vector<int>> levelOrder(TreeNode *root) {
        vector<vector<int>> result;
        if (!root) return result;
        
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            int size = q.size();
            vector<int> level;
            
            for (int i = 0; i < size; ++i) {
                TreeNode* current = q.front();
                q.pop();
                level.push_back(current->val);
                
                if (current->left) q.push(current->left);
                if (current->right) q.push(current->right);
            }
            
            result.push_back(level);
        }
        
        return result;
    }
};
*/
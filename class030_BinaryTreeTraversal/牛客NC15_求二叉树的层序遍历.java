package class036;

import java.util.*;

/**
 * 牛客网 NC15. 求二叉树的层序遍历
 * 题目链接: https://www.nowcoder.com/practice/04a5560e43e24e9db4595865dc9c63a3
 * 题目描述: 给定一个二叉树，返回该二叉树层序遍历的结果（从左到右，一层一层地遍历）
 * 
 * 核心算法思想:
 * 1. 分层层序遍历: 使用队列进行层序遍历，分层收集节点值
 * 2. 数组优化: 使用数组实现队列，提升性能
 * 
 * 时间复杂度分析:
 * - 所有方法: O(N)，其中N是二叉树中的节点数
 * 
 * 空间复杂度分析:
 * - 方法1(层序遍历): O(W)，W为树的最大宽度
 * - 方法2(数组优化): O(W)，W为树的最大宽度
 * 
 * 相关题目:
 * 1. LeetCode 102. 二叉树的层序遍历 - 相同题目
 * 2. 剑指 Offer 32 - II. 从上到下打印二叉树 II - 相同题目
 * 3. LeetCode 107. 二叉树的层序遍历 II - 自底向上遍历
 * 
 * 工程化考量:
 * 1. 结果格式: 返回ArrayList<ArrayList<Integer>>格式
 * 2. 边界处理: 空树返回空列表
 * 3. 性能优化: 预分配内存空间
 */
public class 牛客NC15_求二叉树的层序遍历 {
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * 层序遍历实现
     * 思路:
     * 1. 使用队列进行层序遍历
     * 2. 从根节点开始，将节点加入队列
     * 3. 当队列不为空时，取出队首节点并将其值加入结果列表
     * 4. 将该节点的左右子节点（如果存在）加入队列
     * 5. 重复步骤3-4直到队列为空
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static ArrayList<ArrayList<Integer>> levelOrder(TreeNode root) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
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
            ArrayList<Integer> level = new ArrayList<>();
            
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
        
        ArrayList<ArrayList<Integer>> result = levelOrder(root);
        System.out.println("层序遍历结果:");
        for (ArrayList<Integer> level : result) {
            System.out.println(level);
        }
        
        // 测试空树
        TreeNode emptyRoot = null;
        ArrayList<ArrayList<Integer>> emptyResult = levelOrder(emptyRoot);
        System.out.println("空树遍历结果: " + emptyResult);
    }
}

/*
Python实现:

class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution:
    def levelOrder(self , root: TreeNode) -> List[List[int]]:
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

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(NULL), right(NULL) {}
};

class Solution {
public:
    vector<vector<int>> levelOrder(TreeNode* root) {
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
package class036;

import java.util.*;

// 剑指Offer 32-I. 从上到下打印二叉树
// 题目链接: https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-lcof/
// 题目大意: 从上到下打印出二叉树的每个节点，同一层的节点按照从左到右的顺序打印。

public class 剑指Offer32_I_从上到下打印二叉树 {
    
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
     * 层序遍历实现（从上到下打印二叉树）
     * 思路:
     * 1. 使用队列进行层序遍历
     * 2. 从根节点开始，将节点加入队列
     * 3. 当队列不为空时，取出队首节点并将其值加入结果列表
     * 4. 将该节点的左右子节点（如果存在）加入队列
     * 5. 重复步骤3-4直到队列为空
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static int[] levelOrder(TreeNode root) {
        if (root == null) {
            return new int[0];
        }
        
        // 使用列表存储结果，方便动态添加
        List<Integer> result = new ArrayList<>();
        
        // 使用队列存储待访问的节点
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // 当队列不为空时继续遍历
        while (!queue.isEmpty()) {
            // 取出队首节点
            TreeNode current = queue.poll();
            
            // 将当前节点的值加入结果列表
            result.add(current.val);
            
            // 将左右子节点加入队列（如果存在）
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        
        // 将列表转换为数组
        return result.stream().mapToInt(Integer::intValue).toArray();
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
        
        int[] result = levelOrder(root);
        System.out.println("层序遍历结果: " + Arrays.toString(result));
        
        // 测试空树
        TreeNode emptyRoot = null;
        int[] emptyResult = levelOrder(emptyRoot);
        System.out.println("空树遍历结果: " + Arrays.toString(emptyResult));
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
    def levelOrder(self, root: TreeNode) -> List[int]:
        if not root:
            return []
            
        from collections import deque
        result = []
        queue = deque([root])
        
        while queue:
            current = queue.popleft()
            result.append(current.val)
            
            if current.left:
                queue.append(current.left)
            if current.right:
                queue.append(current.right)
                
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
    vector<int> levelOrder(TreeNode* root) {
        vector<int> result;
        if (!root) return result;
        
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            TreeNode* current = q.front();
            q.pop();
            result.push_back(current->val);
            
            if (current->left) q.push(current->left);
            if (current->right) q.push(current->right);
        }
        
        return result;
    }
};
*/
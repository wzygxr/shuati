package class036;

import java.util.*;

/**
 * LeetCode 513. 找树左下角的值
 * 题目链接: https://leetcode.cn/problems/find-bottom-left-tree-value/
 * 题目描述: 给定一个二叉树的根节点 root，请找出该二叉树的最后一行最左边的值。
 * 
 * 核心算法思想:
 * 1. 层序遍历(BFS): 从右到左进行层序遍历，最后一个节点即为左下角值
 * 2. 深度优先遍历(DFS): 记录最大深度和对应值
 * 3. 优化的BFS: 使用数组实现队列，提升性能
 * 
 * 时间复杂度分析:
 * - 所有方法: O(N)，其中N是二叉树中的节点数
 * 
 * 空间复杂度分析:
 * - 方法1(层序遍历): O(W)，W为树的最大宽度
 * - 方法2(DFS递归): O(H)，H为树的高度
 * - 方法3(优化BFS): O(W)，W为树的最大宽度
 * 
 * 相关题目:
 * 1. LeetCode 199. 二叉树的右视图 - 类似的分层处理
 * 2. LeetCode 515. 在每个树行中找最大值 - 分层极值查找
 * 3. LeetCode 637. 二叉树的层平均值 - 分层统计
 * 
 * 工程化考量:
 * 1. 边界处理: 处理空树和单节点树
 * 2. 性能优化: 选择合适的数据结构
 * 3. 可读性: 代码结构清晰
 */
public class LeetCode513_FindBottomLeftTreeValue {
    
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
     * 方法1: 层序遍历法 - 从右到左遍历
     * 思路: 从右到左进行层序遍历，最后一个节点即为左下角值
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     */
    public static int findBottomLeftValue1(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        TreeNode result = root;
        
        while (!queue.isEmpty()) {
            result = queue.peek(); // 记录当前层第一个节点
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                
                // 先右后左加入队列
                if (current.right != null) {
                    queue.offer(current.right);
                }
                if (current.left != null) {
                    queue.offer(current.left);
                }
            }
        }
        
        return result.val;
    }
    
    /**
     * 方法2: 深度优先遍历(DFS) - 记录最大深度
     * 思路: 使用DFS记录最大深度和对应值
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(H) - H为树的高度
     */
    public static int findBottomLeftValue2(TreeNode root) {
        int[] maxDepth = {-1};
        int[] result = {root.val};
        dfs(root, 0, maxDepth, result);
        return result[0];
    }
    
    private static void dfs(TreeNode node, int depth, int[] maxDepth, int[] result) {
        if (node == null) {
            return;
        }
        
        // 如果是更深层的第一个节点
        if (depth > maxDepth[0]) {
            maxDepth[0] = depth;
            result[0] = node.val;
        }
        
        // 先左后右，保证找到的是最左边的值
        dfs(node.left, depth + 1, maxDepth, result);
        dfs(node.right, depth + 1, maxDepth, result);
    }
    
    /**
     * 方法3: 优化的BFS - 使用数组实现队列
     * 思路: 使用数组代替LinkedList，提升性能
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     */
    public static int findBottomLeftValue3(TreeNode root) {
        TreeNode[] queue = new TreeNode[1000];
        int l = 0, r = 0;
        queue[r++] = root;
        TreeNode result = root;
        
        while (l < r) {
            result = queue[l]; // 记录当前层第一个节点
            int size = r - l;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue[l++];
                
                if (current.right != null) {
                    queue[r++] = current.right;
                }
                if (current.left != null) {
                    queue[r++] = current.left;
                }
            }
        }
        
        return result.val;
    }
    
    /**
     * 辅助方法: 根据数组构建测试树
     */
    public static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) {
            return null;
        }
        
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        
        while (!queue.isEmpty() && i < arr.length) {
            TreeNode current = queue.poll();
            
            if (i < arr.length && arr[i] != null) {
                current.left = new TreeNode(arr[i]);
                queue.offer(current.left);
            }
            i++;
            
            if (i < arr.length && arr[i] != null) {
                current.right = new TreeNode(arr[i]);
                queue.offer(current.right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 测试方法: 包含多种测试用例
     */
    public static void main(String[] args) {
        System.out.println("========== LeetCode 513 测试 ==========");
        
        // 测试用例1: 标准二叉树 [2,1,3]
        Integer[] arr1 = {2, 1, 3};
        TreeNode root1 = buildTree(arr1);
        
        System.out.println("方法1结果: " + findBottomLeftValue1(root1));
        System.out.println("方法2结果: " + findBottomLeftValue2(root1));
        System.out.println("方法3结果: " + findBottomLeftValue3(root1));
        
        // 测试用例2: 复杂二叉树 [1,2,3,4,null,5,6,null,null,7]
        Integer[] arr2 = {1, 2, 3, 4, null, 5, 6, null, null, 7};
        TreeNode root2 = buildTree(arr2);
        System.out.println("方法1结果: " + findBottomLeftValue1(root2));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（层序遍历）: 通用性强，逻辑清晰");
        System.out.println("2. 方法2（DFS递归）: 空间复杂度优，适合平衡树");
        System.out.println("3. 方法3（优化BFS）: 性能最优，适合大数据量");
    }
}

/*
Python实现:

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def findBottomLeftValue(self, root: TreeNode) -> int:
        from collections import deque
        queue = deque([root])
        result = root
        
        while queue:
            result = queue[0]
            size = len(queue)
            
            for i in range(size):
                current = queue.popleft()
                
                if current.right:
                    queue.append(current.right)
                if current.left:
                    queue.append(current.left)
                    
        return result.val

C++实现:

#include <iostream>
#include <queue>
using namespace std;

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
    int findBottomLeftValue(TreeNode* root) {
        queue<TreeNode*> q;
        q.push(root);
        TreeNode* result = root;
        
        while (!q.empty()) {
            result = q.front();
            int size = q.size();
            
            for (int i = 0; i < size; ++i) {
                TreeNode* current = q.front();
                q.pop();
                
                if (current->right) q.push(current->right);
                if (current->left) q.push(current->left);
            }
        }
        
        return result->val;
    }
};
*/
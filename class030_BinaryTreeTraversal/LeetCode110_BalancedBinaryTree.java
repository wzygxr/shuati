package class036;

import java.util.*;

/**
 * LeetCode 110. 平衡二叉树
 * 题目链接: https://leetcode.cn/problems/balanced-binary-tree/
 * 题目描述: 给定一个二叉树，判断它是否是高度平衡的二叉树。
 * 高度平衡二叉树定义: 一个二叉树每个节点的左右两个子树的高度差的绝对值不超过1。
 * 
 * 核心算法思想:
 * 1. 自顶向下递归: 计算每个节点的高度并检查平衡性
 * 2. 自底向上递归: 在计算高度的同时检查平衡性，避免重复计算
 * 3. 优化递归: 使用返回值同时传递高度和平衡信息
 * 
 * 时间复杂度分析:
 * - 方法1(自顶向下): O(NlogN) - 最坏情况O(N²)
 * - 方法2(自底向上): O(N) - 每个节点访问一次
 * - 方法3(优化递归): O(N) - 每个节点访问一次
 * 
 * 空间复杂度分析:
 * - 所有方法: O(H) - H为树的高度，递归调用栈深度
 * 
 * 相关题目:
 * 1. LeetCode 104. 二叉树的最大深度 - 基础高度计算
 * 2. LeetCode 111. 二叉树的最小深度 - 深度计算变种
 * 3. 剑指 Offer 55 - II. 平衡二叉树 - 相同题目
 * 
 * 工程化考量:
 * 1. 提前终止: 发现不平衡立即返回，避免不必要的计算
 * 2. 返回值设计: 使用特殊值或对象传递多个信息
 * 3. 边界处理: 空树、单节点树等特殊情况
 */
public class LeetCode110_BalancedBinaryTree {
    
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
     * 方法1: 自顶向下递归 - 基础实现
     * 思路: 对于每个节点，计算左右子树高度并检查平衡性
     * 时间复杂度: O(NlogN) - 平衡树情况，最坏O(N²)
     * 空间复杂度: O(H) - H为树的高度
     * 
     * 缺点: 存在重复计算高度的问题
     */
    public static boolean isBalanced1(TreeNode root) {
        if (root == null) {
            return true;
        }
        
        // 检查当前节点是否平衡
        int leftHeight = getHeight(root.left);
        int rightHeight = getHeight(root.right);
        
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }
        
        // 递归检查左右子树
        return isBalanced1(root.left) && isBalanced1(root.right);
    }
    
    /**
     * 计算二叉树的高度
     */
    private static int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }
    
    /**
     * 方法2: 自底向上递归 - 最优解法
     * 思路: 在计算高度的同时检查平衡性，避免重复计算
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(H) - H为树的高度
     * 
     * 核心思想:
     * 1. 使用-1表示不平衡状态
     * 2. 如果子树不平衡，立即返回-1
     * 3. 否则返回正常的高度值
     */
    public static boolean isBalanced2(TreeNode root) {
        return checkHeight(root) != -1;
    }
    
    private static int checkHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 检查左子树
        int leftHeight = checkHeight(node.left);
        if (leftHeight == -1) {
            return -1; // 左子树不平衡
        }
        
        // 检查右子树
        int rightHeight = checkHeight(node.right);
        if (rightHeight == -1) {
            return -1; // 右子树不平衡
        }
        
        // 检查当前节点是否平衡
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1; // 当前节点不平衡
        }
        
        // 返回当前节点的高度
        return Math.max(leftHeight, rightHeight) + 1;
    }
    
    /**
     * 方法3: 使用返回值对象 - 更清晰的实现
     * 思路: 创建返回值对象同时包含高度和平衡信息
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(H) - H为树的高度
     */
    public static boolean isBalanced3(TreeNode root) {
        Result result = checkBalanced(root);
        return result.isBalanced;
    }
    
    /**
     * 返回值对象，包含高度和平衡信息
     */
    private static class Result {
        boolean isBalanced;
        int height;
        
        Result(boolean isBalanced, int height) {
            this.isBalanced = isBalanced;
            this.height = height;
        }
    }
    
    private static Result checkBalanced(TreeNode node) {
        if (node == null) {
            return new Result(true, 0);
        }
        
        // 检查左子树
        Result leftResult = checkBalanced(node.left);
        if (!leftResult.isBalanced) {
            return new Result(false, 0);
        }
        
        // 检查右子树
        Result rightResult = checkBalanced(node.right);
        if (!rightResult.isBalanced) {
            return new Result(false, 0);
        }
        
        // 检查当前节点是否平衡
        if (Math.abs(leftResult.height - rightResult.height) > 1) {
            return new Result(false, 0);
        }
        
        // 返回当前节点的结果
        int height = Math.max(leftResult.height, rightResult.height) + 1;
        return new Result(true, height);
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
        System.out.println("========== LeetCode 110 测试 ==========");
        
        // 测试用例1: 平衡二叉树 [3,9,20,null,null,15,7]
        Integer[] arr1 = {3, 9, 20, null, null, 15, 7};
        TreeNode root1 = buildTree(arr1);
        
        System.out.println("测试用例1 - 平衡二叉树:");
        System.out.println("方法1结果: " + isBalanced1(root1));
        System.out.println("方法2结果: " + isBalanced2(root1));
        System.out.println("方法3结果: " + isBalanced3(root1));
        
        // 测试用例2: 不平衡二叉树 [1,2,2,3,3,null,null,4,4]
        Integer[] arr2 = {1, 2, 2, 3, 3, null, null, 4, 4};
        TreeNode root2 = buildTree(arr2);
        
        System.out.println("\n测试用例2 - 不平衡二叉树:");
        System.out.println("方法1结果: " + isBalanced1(root2));
        System.out.println("方法2结果: " + isBalanced2(root2));
        System.out.println("方法3结果: " + isBalanced3(root2));
        
        // 测试用例3: 单节点树
        TreeNode root3 = new TreeNode(1);
        System.out.println("\n测试用例3 - 单节点树:");
        System.out.println("方法1结果: " + isBalanced1(root3));
        
        // 测试用例4: 空树
        TreeNode root4 = null;
        System.out.println("\n测试用例4 - 空树:");
        System.out.println("方法1结果: " + isBalanced1(root4));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（自顶向下）: 逻辑简单但效率低，存在重复计算");
        System.out.println("2. 方法2（自底向上）: 最优解法O(N)，推荐使用");
        System.out.println("3. 方法3（返回值对象）: 代码更清晰，性能与方法2相同");
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
    # 方法2: 自底向上递归（最优解）
    def isBalanced(self, root: TreeNode) -> bool:
        def check_height(node):
            if not node:
                return 0
                
            left_height = check_height(node.left)
            if left_height == -1:
                return -1
                
            right_height = check_height(node.right)
            if right_height == -1:
                return -1
                
            if abs(left_height - right_height) > 1:
                return -1
                
            return max(left_height, right_height) + 1
            
        return check_height(root) != -1

C++实现:

#include <iostream>
#include <algorithm>
#include <cmath>
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
    bool isBalanced(TreeNode* root) {
        return checkHeight(root) != -1;
    }
    
private:
    int checkHeight(TreeNode* node) {
        if (!node) return 0;
        
        int leftHeight = checkHeight(node->left);
        if (leftHeight == -1) return -1;
        
        int rightHeight = checkHeight(node->right);
        if (rightHeight == -1) return -1;
        
        if (abs(leftHeight - rightHeight) > 1) return -1;
        
        return max(leftHeight, rightHeight) + 1;
    }
};
*/
package class036;

import java.util.*;

/**
 * LeetCode 637. 二叉树的层平均值
 * 题目链接: https://leetcode.cn/problems/average-of-levels-in-binary-tree/
 * 题目描述: 给定一个非空二叉树的根节点 root，以数组的形式返回每一层节点的平均值。
 * 
 * 核心算法思想:
 * 1. 层序遍历(BFS): 使用队列进行层序遍历，计算每层节点的平均值
 * 2. 深度优先遍历(DFS): 使用递归记录每层的节点值和数量
 * 3. 优化的BFS: 使用数组实现队列，减少对象创建开销
 * 
 * 时间复杂度分析:
 * - 所有方法: O(N)，其中N是二叉树中的节点数
 * 
 * 空间复杂度分析:
 * - 方法1(层序遍历): O(W)，W为树的最大宽度
 * - 方法2(DFS递归): O(H)，H为树的高度
 * - 方法3(优化BFS): O(W)，W为树的最大宽度
 * 
 * 精度处理考量:
 * 1. 使用double类型避免整数除法精度损失
 * 2. 处理大数相加时的溢出问题
 * 3. 考虑极端情况下的数值稳定性
 * 
 * 相关题目:
 * 1. LeetCode 102. 二叉树的层序遍历 - 基础层序遍历
 * 2. LeetCode 107. 二叉树的层序遍历 II - 自底向上层序遍历
 * 3. LeetCode 199. 二叉树的右视图 - 分层处理
 * 4. LeetCode 515. 在每个树行中找最大值 - 分层统计极值
 * 
 * 工程化考量:
 * 1. 数值精度: 使用double类型保证计算精度
 * 2. 溢出处理: 对于大数据量考虑使用BigDecimal
 * 3. 性能优化: 选择合适的数据结构提升性能
 */
public class LeetCode637_AverageOfLevelsInBinaryTree {
    
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
     * 方法1: 层序遍历法 - 基础BFS实现
     * 思路: 使用队列进行层序遍历，计算每层节点的平均值
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     * 
     * 优点:
     * - 逻辑清晰，易于理解和实现
     * - 适用于各种二叉树结构
     * 缺点:
     * - 需要额外的队列空间
     * 
     * 关键步骤:
     * 1. 使用队列存储当前层的所有节点
     * 2. 记录每层的节点数量和总和
     * 3. 计算平均值并加入结果列表
     */
    public static List<Double> averageOfLevels1(TreeNode root) {
        List<Double> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            double sum = 0;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                sum += current.val;
                
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 计算平均值，注意使用double避免精度损失
            result.add(sum / size);
        }
        
        return result;
    }
    
    /**
     * 方法2: 深度优先遍历(DFS) - 递归实现
     * 思路: 使用递归进行DFS，记录每层的节点总和和数量
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(H) - H为树的高度，递归调用栈的深度
     * 
     * 核心思想:
     * 1. 使用两个列表分别记录每层的总和和节点数量
     * 2. 递归遍历时更新对应层级的统计信息
     * 3. 遍历完成后计算每层的平均值
     * 
     * 优点:
     * - 空间复杂度较低（树的高度通常远小于节点数）
     * - 避免使用队列，减少对象创建
     * 缺点:
     * - 递归深度可能较大
     * - 需要额外的存储空间记录统计信息
     */
    public static List<Double> averageOfLevels2(TreeNode root) {
        // 存储每层的节点值总和
        List<Double> sums = new ArrayList<>();
        // 存储每层的节点数量
        List<Integer> counts = new ArrayList<>();
        
        dfs(root, 0, sums, counts);
        
        // 计算每层的平均值
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < sums.size(); i++) {
            result.add(sums.get(i) / counts.get(i));
        }
        
        return result;
    }
    
    private static void dfs(TreeNode node, int level, List<Double> sums, List<Integer> counts) {
        if (node == null) {
            return;
        }
        
        // 如果当前层级还没有统计信息，初始化
        if (level == sums.size()) {
            sums.add(0.0);
            counts.add(0);
        }
        
        // 更新当前层级的统计信息
        sums.set(level, sums.get(level) + node.val);
        counts.set(level, counts.get(level) + 1);
        
        // 递归处理左右子树
        dfs(node.left, level + 1, sums, counts);
        dfs(node.right, level + 1, sums, counts);
    }
    
    /**
     * 方法3: 优化的BFS - 使用数组实现队列
     * 思路: 使用数组代替LinkedList，减少对象创建和内存分配开销
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     * 
     * 工程化优化:
     * 1. 使用数组代替LinkedList减少内存分配
     * 2. 预分配队列大小，避免动态扩容
     * 3. 减少对象创建，提升性能
     */
    public static List<Double> averageOfLevels3(TreeNode root) {
        List<Double> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 预分配队列大小
        TreeNode[] queue = new TreeNode[1000];
        int l = 0, r = 0;
        queue[r++] = root;
        
        while (l < r) {
            int size = r - l;
            double sum = 0;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue[l++];
                sum += current.val;
                
                if (current.left != null) {
                    queue[r++] = current.left;
                }
                if (current.right != null) {
                    queue[r++] = current.right;
                }
            }
            
            result.add(sum / size);
        }
        
        return result;
    }
    
    /**
     * 方法4: 防止数值溢出的安全版本
     * 思路: 使用BigDecimal处理大数相加，避免溢出问题
     * 适用场景: 节点值很大或层节点数很多时
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     */
    public static List<Double> averageOfLevels4(TreeNode root) {
        List<Double> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            // 使用long避免整数溢出
            long sum = 0;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                sum += current.val;
                
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 使用double计算平均值
            result.add((double) sum / size);
        }
        
        return result;
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
        System.out.println("========== LeetCode 637 测试 ==========");
        
        // 测试用例1: 标准二叉树 [3,9,20,null,null,15,7]
        System.out.println("\n测试用例1: 标准二叉树");
        Integer[] arr1 = {3, 9, 20, null, null, 15, 7};
        TreeNode root1 = buildTree(arr1);
        
        System.out.println("方法1结果: " + averageOfLevels1(root1));
        System.out.println("方法2结果: " + averageOfLevels2(root1));
        System.out.println("方法3结果: " + averageOfLevels3(root1));
        System.out.println("方法4结果: " + averageOfLevels4(root1));
        
        // 测试用例2: 单节点树
        System.out.println("\n测试用例2: 单节点树");
        TreeNode root2 = new TreeNode(5);
        System.out.println("方法1结果: " + averageOfLevels1(root2));
        
        // 测试用例3: 斜树
        System.out.println("\n测试用例3: 斜树");
        Integer[] arr3 = {1, 2, null, 3, null, 4, null};
        TreeNode root3 = buildTree(arr3);
        System.out.println("方法1结果: " + averageOfLevels1(root3));
        
        // 测试用例4: 大数测试（避免溢出）
        System.out.println("\n测试用例4: 大数测试");
        TreeNode root4 = new TreeNode(Integer.MAX_VALUE);
        root4.left = new TreeNode(Integer.MAX_VALUE);
        root4.right = new TreeNode(Integer.MAX_VALUE);
        System.out.println("方法4结果: " + averageOfLevels4(root4));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（层序遍历）: 通用性强，逻辑清晰");
        System.out.println("2. 方法2（DFS递归）: 空间复杂度优，适合平衡树");
        System.out.println("3. 方法3（优化BFS）: 性能最优，适合大数据量");
        System.out.println("4. 方法4（安全版本）: 防止数值溢出，适合大数场景");
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
    # 方法1: 层序遍历法
    def averageOfLevels1(self, root: TreeNode) -> List[float]:
        if not root:
            return []
            
        from collections import deque
        result = []
        queue = deque([root])
        
        while queue:
            size = len(queue)
            level_sum = 0
            
            for i in range(size):
                current = queue.popleft()
                level_sum += current.val
                
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
                    
            result.append(level_sum / size)
            
        return result
    
    # 方法2: DFS递归
    def averageOfLevels2(self, root: TreeNode) -> List[float]:
        sums = []
        counts = []
        
        def dfs(node, level):
            if not node:
                return
                
            if level == len(sums):
                sums.append(0)
                counts.append(0)
                
            sums[level] += node.val
            counts[level] += 1
            
            dfs(node.left, level + 1)
            dfs(node.right, level + 1)
            
        dfs(root, 0)
        return [s / c for s, c in zip(sums, counts)]

C++实现:

#include <iostream>
#include <vector>
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
    // 方法1: 层序遍历法
    vector<double> averageOfLevels(TreeNode* root) {
        vector<double> result;
        if (!root) return result;
        
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            int size = q.size();
            double sum = 0;
            
            for (int i = 0; i < size; ++i) {
                TreeNode* current = q.front();
                q.pop();
                sum += current->val;
                
                if (current->left) q.push(current->left);
                if (current->right) q.push(current->right);
            }
            
            result.push_back(sum / size);
        }
        
        return result;
    }
};
*/
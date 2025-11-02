package class036;

import java.util.*;

/**
 * LeetCode 515. 在每个树行中找最大值
 * 题目链接: https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
 * 题目描述: 给定一棵二叉树的根节点 root，请找出该二叉树中每一层的最大值。
 * 
 * 核心算法思想:
 * 1. 层序遍历(BFS): 使用队列进行层序遍历，记录每层的最大值
 * 2. 深度优先遍历(DFS): 使用递归记录每层的最大值
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
 * 极值处理考量:
 * 1. 处理空树和单节点树的边界情况
 * 2. 考虑节点值为Integer.MIN_VALUE的情况
 * 3. 处理大数比较的边界条件
 * 
 * 相关题目:
 * 1. LeetCode 102. 二叉树的层序遍历 - 基础层序遍历
 * 2. LeetCode 199. 二叉树的右视图 - 分层处理
 * 3. LeetCode 637. 二叉树的层平均值 - 分层统计
 * 4. LeetCode 513. 找树左下角的值 - 分层极值查找
 * 
 * 工程化考量:
 * 1. 边界处理: 完善各种边界情况的处理
 * 2. 性能优化: 选择合适的数据结构
 * 3. 可读性: 代码结构清晰，注释完整
 */
public class LeetCode515_FindLargestValueInEachTreeRow {
    
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
     * 思路: 使用队列进行层序遍历，记录每层的最大值
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     */
    public static List<Integer> largestValues1(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            int maxVal = Integer.MIN_VALUE;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                maxVal = Math.max(maxVal, current.val);
                
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            result.add(maxVal);
        }
        
        return result;
    }
    
    /**
     * 方法2: 深度优先遍历(DFS) - 递归实现
     * 思路: 使用递归进行DFS，记录每层的最大值
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(H) - H为树的高度
     */
    public static List<Integer> largestValues2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }
    
    private static void dfs(TreeNode node, int level, List<Integer> result) {
        if (node == null) {
            return;
        }
        
        if (level == result.size()) {
            result.add(node.val);
        } else {
            result.set(level, Math.max(result.get(level), node.val));
        }
        
        dfs(node.left, level + 1, result);
        dfs(node.right, level + 1, result);
    }
    
    /**
     * 方法3: 优化的BFS - 使用数组实现队列
     * 思路: 使用数组代替LinkedList，提升性能
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     */
    public static List<Integer> largestValues3(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        TreeNode[] queue = new TreeNode[1000];
        int l = 0, r = 0;
        queue[r++] = root;
        
        while (l < r) {
            int size = r - l;
            int maxVal = Integer.MIN_VALUE;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue[l++];
                maxVal = Math.max(maxVal, current.val);
                
                if (current.left != null) {
                    queue[r++] = current.left;
                }
                if (current.right != null) {
                    queue[r++] = current.right;
                }
            }
            
            result.add(maxVal);
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
        System.out.println("========== LeetCode 515 测试 ==========");
        
        // 测试用例1: 标准二叉树 [1,3,2,5,3,null,9]
        Integer[] arr1 = {1, 3, 2, 5, 3, null, 9};
        TreeNode root1 = buildTree(arr1);
        
        System.out.println("方法1结果: " + largestValues1(root1));
        System.out.println("方法2结果: " + largestValues2(root1));
        System.out.println("方法3结果: " + largestValues3(root1));
        
        // 测试用例2: 单节点树
        TreeNode root2 = new TreeNode(5);
        System.out.println("方法1结果: " + largestValues1(root2));
        
        // 测试用例3: 斜树
        Integer[] arr3 = {1, 2, null, 3, null, 4, null};
        TreeNode root3 = buildTree(arr3);
        System.out.println("方法1结果: " + largestValues1(root3));
        
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
    def largestValues(self, root: TreeNode) -> List[int]:
        if not root:
            return []
            
        from collections import deque
        result = []
        queue = deque([root])
        
        while queue:
            size = len(queue)
            max_val = float('-inf')
            
            for i in range(size):
                current = queue.popleft()
                max_val = max(max_val, current.val)
                
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
                    
            result.append(max_val)
            
        return result

C++实现:

#include <iostream>
#include <vector>
#include <queue>
#include <climits>
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
    vector<int> largestValues(TreeNode* root) {
        vector<int> result;
        if (!root) return result;
        
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            int size = q.size();
            int maxVal = INT_MIN;
            
            for (int i = 0; i < size; ++i) {
                TreeNode* current = q.front();
                q.pop();
                maxVal = max(maxVal, current->val);
                
                if (current->left) q.push(current->left);
                if (current->right) q.push(current->right);
            }
            
            result.push_back(maxVal);
        }
        
        return result;
    }
};
*/
package class036;

import java.util.*;

/**
 * LeetCode 199. 二叉树的右视图
 * 题目链接: https://leetcode.cn/problems/binary-tree-right-side-view/
 * 题目描述: 给定一个二叉树的根节点 root，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
 * 
 * 核心算法思想:
 * 1. 层序遍历(BFS): 使用队列进行层序遍历，记录每层的最右节点
 * 2. 深度优先遍历(DFS): 使用递归进行深度优先遍历，优先访问右子树
 * 3. 反向层序遍历: 从右到左进行层序遍历，记录每层的第一个节点
 * 
 * 时间复杂度分析:
 * - 所有方法: O(N)，其中N是二叉树中的节点数，每个节点只被访问一次
 * 
 * 空间复杂度分析:
 * - 方法1(层序遍历): O(W)，W为树的最大宽度
 * - 方法2(DFS递归): O(H)，H为树的高度，递归调用栈的深度
 * - 方法3(反向层序): O(W)，W为树的最大宽度
 * 
 * 相关题目:
 * 1. LeetCode 102. 二叉树的层序遍历 - 基础层序遍历
 * 2. LeetCode 116/117. 填充下一个右侧节点指针 - 类似的分层处理
 * 3. LeetCode 637. 二叉树的层平均值 - 分层统计
 * 4. LeetCode 515. 在每个树行中找最大值 - 分层找极值
 * 5. 剑指 Offer 32 - III. 从上到下打印二叉树 III - 锯齿形层序遍历
 * 
 * 工程化考量:
 * 1. 鲁棒性: 处理空树、单节点树、斜树等各种边界情况
 * 2. 性能优化: 对于大数据量选择空间复杂度更低的方法
 * 3. 可读性: 代码结构清晰，注释完整
 */
public class LeetCode199_BinaryTreeRightSideView {
    
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
     * 方法1: 层序遍历法 - 记录每层的最右节点
     * 思路: 使用队列进行层序遍历，在每层遍历时记录最后一个节点（最右节点）
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度，最坏情况为O(N/2)≈O(N)
     * 
     * 优点:
     * - 逻辑清晰，易于理解和实现
     * - 适用于各种二叉树结构
     * 缺点:
     * - 需要额外的队列空间
     * 
     * 关键步骤:
     * 1. 使用队列存储当前层的所有节点
     * 2. 记录每层的节点数量
     * 3. 遍历当前层，记录最后一个节点
     */
    public static List<Integer> rightSideView1(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            TreeNode rightmost = null;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                rightmost = current; // 记录当前节点
                
                // 先左后右加入队列（保证同一层从左到右）
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 当前层的最右节点加入结果
            if (rightmost != null) {
                result.add(rightmost.val);
            }
        }
        
        return result;
    }
    
    /**
     * 方法2: 深度优先遍历(DFS) - 优先访问右子树
     * 思路: 使用递归进行DFS，优先访问右子树，每层第一个访问到的节点就是右视图节点
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(H) - H为树的高度，递归调用栈的深度
     * 
     * 核心思想:
     * 1. 优先访问右子树，再访问左子树
     * 2. 当深度等于结果列表大小时，说明是当前层第一个访问的节点（最右节点）
     * 3. 将该节点加入结果列表
     * 
     * 优点:
     * - 空间复杂度较低（树的高度通常远小于节点数）
     * - 递归代码简洁
     * 缺点:
     * - 递归深度可能较大（斜树）
     * - 对于大树可能栈溢出
     */
    public static List<Integer> rightSideView2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }
    
    private static void dfs(TreeNode node, int depth, List<Integer> result) {
        if (node == null) {
            return;
        }
        
        // 如果当前深度等于结果列表大小，说明是当前层第一个访问的节点
        if (depth == result.size()) {
            result.add(node.val);
        }
        
        // 优先访问右子树（保证先看到右边的节点）
        dfs(node.right, depth + 1, result);
        // 再访问左子树
        dfs(node.left, depth + 1, result);
    }
    
    /**
     * 方法3: 反向层序遍历 - 从右到左遍历，记录每层第一个节点
     * 思路: 层序遍历时从右到左处理节点，每层的第一个节点就是右视图节点
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     * 
     * 优点:
     * - 逻辑直观，易于理解
     * - 避免递归的栈溢出风险
     * 缺点:
     * - 需要额外的队列空间
     * 
     * 关键优化:
     * - 从右到左处理节点，可以立即确定右视图节点
     */
    public static List<Integer> rightSideView3(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            // 每层的第一个节点（从右开始）就是右视图节点
            result.add(queue.peek().val);
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                
                // 先右后左加入队列（保证下一层从右开始）
                if (current.right != null) {
                    queue.offer(current.right);
                }
                if (current.left != null) {
                    queue.offer(current.left);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 方法4: 优化的BFS - 只记录必要的节点信息
     * 思路: 使用数组实现队列，减少对象创建开销
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度
     * 
     * 工程化优化:
     * 1. 使用数组代替LinkedList减少内存分配
     * 2. 预分配队列大小，避免动态扩容
     * 3. 减少对象创建，提升性能
     */
    public static List<Integer> rightSideView4(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 预分配队列大小
        TreeNode[] queue = new TreeNode[1000];
        int l = 0, r = 0;
        queue[r++] = root;
        
        while (l < r) {
            int size = r - l;
            TreeNode rightmost = null;
            
            for (int i = 0; i < size; i++) {
                TreeNode current = queue[l++];
                rightmost = current;
                
                if (current.left != null) {
                    queue[r++] = current.left;
                }
                if (current.right != null) {
                    queue[r++] = current.right;
                }
            }
            
            if (rightmost != null) {
                result.add(rightmost.val);
            }
        }
        
        return result;
    }
    
    /**
     * 辅助方法: 根据数组构建测试树
     * 数组格式: 层序遍历，null表示空节点
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
     * 覆盖各种边界情况和典型场景
     */
    public static void main(String[] args) {
        System.out.println("========== LeetCode 199 测试 ==========");
        
        // 测试用例1: 标准二叉树 [1,2,3,null,5,null,4]
        System.out.println("\n测试用例1: 标准二叉树");
        Integer[] arr1 = {1, 2, 3, null, 5, null, 4};
        TreeNode root1 = buildTree(arr1);
        
        System.out.println("方法1结果: " + rightSideView1(root1));
        System.out.println("方法2结果: " + rightSideView2(root1));
        System.out.println("方法3结果: " + rightSideView3(root1));
        System.out.println("方法4结果: " + rightSideView4(root1));
        
        // 测试用例2: 只有左子树 [1,2,null,3,null,4]
        System.out.println("\n测试用例2: 斜树（左）");
        Integer[] arr2 = {1, 2, null, 3, null, 4, null};
        TreeNode root2 = buildTree(arr2);
        System.out.println("方法1结果: " + rightSideView1(root2));
        
        // 测试用例3: 只有右子树 [1,null,2,null,3,null,4]
        System.out.println("\n测试用例3: 斜树（右）");
        Integer[] arr3 = {1, null, 2, null, 3, null, 4};
        TreeNode root3 = buildTree(arr3);
        System.out.println("方法1结果: " + rightSideView1(root3));
        
        // 测试用例4: 单节点树
        System.out.println("\n测试用例4: 单节点树");
        TreeNode root4 = new TreeNode(1);
        System.out.println("方法1结果: " + rightSideView1(root4));
        
        // 测试用例5: 空树
        System.out.println("\n测试用例5: 空树");
        TreeNode root5 = null;
        System.out.println("方法1结果: " + rightSideView1(root5));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（层序遍历）: 通用性强，逻辑清晰");
        System.out.println("2. 方法2（DFS递归）: 空间复杂度优，适合平衡树");
        System.out.println("3. 方法3（反向层序）: 逻辑直观，避免递归风险");
        System.out.println("4. 方法4（优化BFS）: 性能最优，适合大数据量");
        System.out.println("推荐: 根据具体场景选择合适的方法");
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
    def rightSideView1(self, root: TreeNode) -> List[int]:
        if not root:
            return []
            
        from collections import deque
        result = []
        queue = deque([root])
        
        while queue:
            size = len(queue)
            rightmost = None
            
            for i in range(size):
                current = queue.popleft()
                rightmost = current
                
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
                    
            if rightmost:
                result.append(rightmost.val)
                
        return result
    
    # 方法2: DFS递归
    def rightSideView2(self, root: TreeNode) -> List[int]:
        result = []
        
        def dfs(node, depth):
            if not node:
                return
                
            if depth == len(result):
                result.append(node.val)
                
            dfs(node.right, depth + 1)
            dfs(node.left, depth + 1)
            
        dfs(root, 0)
        return result

# 测试代码
if __name__ == "__main__":
    # 构建测试树
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.right = TreeNode(5)
    root.right.right = TreeNode(4)
    
    solution = Solution()
    print(solution.rightSideView1(root))

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
    vector<int> rightSideView(TreeNode* root) {
        vector<int> result;
        if (!root) return result;
        
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            int size = q.size();
            TreeNode* rightmost = nullptr;
            
            for (int i = 0; i < size; ++i) {
                TreeNode* current = q.front();
                q.pop();
                rightmost = current;
                
                if (current->left) q.push(current->left);
                if (current->right) q.push(current->right);
            }
            
            if (rightmost) {
                result.push_back(rightmost->val);
            }
        }
        
        return result;
    }
};

// 测试代码
int main() {
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->right = new TreeNode(5);
    root->right->right = new TreeNode(4);
    
    Solution solution;
    vector<int> result = solution.rightSideView(root);
    
    for (int val : result) {
        cout << val << " ";
    }
    
    // 释放内存...
    return 0;
}
*/
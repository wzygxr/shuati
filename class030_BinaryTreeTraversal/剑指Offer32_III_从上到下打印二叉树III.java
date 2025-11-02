package class036;

import java.util.*;

// 剑指Offer 32-III. 从上到下打印二叉树 III
// 题目链接: https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-iii-lcof/
// 题目大意: 请实现一个函数按照之字形顺序打印二叉树，即第一行按照从左到右的顺序打印，
// 第二层按照从右到左的顺序打印，第三行再按照从左到右的顺序打印，其他行以此类推。

public class 剑指Offer32_III_从上到下打印二叉树III {
    
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
     * 之字形层序遍历实现
     * 思路:
     * 1. 使用队列进行层序遍历
     * 2. 使用一个布尔变量记录当前层的打印方向
     * 3. 对于需要从右到左打印的层，将节点值插入到列表的开头
     * 4. 每层处理完后切换打印方向
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
        
        // true表示从左到右，false表示从右到左
        boolean leftToRight = true;
        
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
                
                // 根据打印方向决定节点值的插入位置
                if (leftToRight) {
                    // 从左到右：添加到列表末尾
                    level.add(current.val);
                } else {
                    // 从右到左：添加到列表开头
                    level.add(0, current.val);
                }
                
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
            
            // 切换打印方向
            leftToRight = !leftToRight;
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试二叉树:
        //       3
        //      / \
        //     9  20
        //       /  \
        //      15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        
        List<List<Integer>> result = levelOrder(root);
        System.out.println("之字形遍历结果:");
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
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution:
    # 方法2: 双端队列法
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        if not root:
            return []
            
        from collections import deque
        result = []
        queue = deque([root])
        left_to_right = True
        
        while queue:
            size = len(queue)
            level = deque()
            
            for i in range(size):
                current = queue.popleft()
                
                if left_to_right:
                    level.append(current.val)
                else:
                    level.appendleft(current.val)
                    
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
                    
            result.append(list(level))
            left_to_right = not left_to_right
            
        return result

C++实现:

#include <iostream>
#include <vector>
#include <queue>
#include <deque>
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
        bool leftToRight = true;
        
        while (!q.empty()) {
            int size = q.size();
            deque<int> level;
            
            for (int i = 0; i < size; ++i) {
                TreeNode* current = q.front();
                q.pop();
                
                if (leftToRight) {
                    level.push_back(current->val);
                } else {
                    level.push_front(current->val);
                }
                
                if (current->left) q.push(current->left);
                if (current->right) q.push(current->right);
            }
            
            result.push_back(vector<int>(level.begin(), level.end()));
            leftToRight = !leftToRight;
        }
        
        return result;
    }
};
*/
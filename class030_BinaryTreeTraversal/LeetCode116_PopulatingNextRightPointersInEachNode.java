package class036;

import java.util.*;

/**
 * LeetCode 116. 填充每个节点的下一个右侧节点指针
 * 题目链接: https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/
 * 题目描述: 给定一个完美二叉树，其所有叶子节点都在同一层，每个父节点都有两个子节点。
 * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 null。
 * 初始状态下，所有 next 指针都被设置为 null。
 * 
 * 完美二叉树定义: 所有叶子节点都在同一层，每个非叶子节点都有两个子节点
 * 
 * 核心算法思想:
 * 1. 层序遍历(BFS): 使用队列进行层序遍历，在每层中连接相邻节点的next指针
 * 2. 利用已建立的next指针: 利用上一层的next指针来连接当前层的节点，避免使用队列
 * 
 * 时间复杂度分析:
 * - 方法1(层序遍历): O(N)，每个节点访问一次
 * - 方法2(利用next指针): O(N)，每个节点访问一次
 * 
 * 空间复杂度分析:
 * - 方法1(层序遍历): O(W)，W为树的最大宽度，最坏情况为O(N/2)≈O(N)
 * - 方法2(利用next指针): O(1)，只使用常数级别的额外空间
 * 
 * 相关题目:
 * 1. LeetCode 117. 填充每个节点的下一个右侧节点指针 II - 非完美二叉树版本
 * 2. LeetCode 199. 二叉树的右视图 - 类似的分层处理思想
 * 3. LeetCode 102. 二叉树的层序遍历 - 基础层序遍历
 * 
 * 工程化考量:
 * 1. 线程安全: 多线程环境下需要同步机制
 * 2. 内存管理: 对于大树需要注意内存使用
 * 3. 异常处理: 处理空指针和边界情况
 */
public class LeetCode116_PopulatingNextRightPointersInEachNode {
    
    // 完美二叉树节点定义
    static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;
        
        public Node() {}
        
        public Node(int _val) {
            val = _val;
        }
        
        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
        
        @Override
        public String toString() {
            return "Node{" + val + "}";
        }
    }
    
    /**
     * 方法1: 层序遍历法 - 使用队列进行BFS遍历
     * 思路: 使用队列进行层序遍历，在每层遍历时连接相邻节点的next指针
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(W) - W为树的最大宽度，最坏情况为O(N/2)≈O(N)
     * 
     * 优点:
     * - 逻辑清晰，易于理解
     * - 适用于各种二叉树结构
     * 缺点:
     * - 需要额外的队列空间
     * - 对于完美二叉树有更优的空间复杂度解法
     * 
     * 关键步骤:
     * 1. 使用队列存储当前层的所有节点
     * 2. 遍历当前层，连接每个节点到下一个节点
     * 3. 将下一层的节点加入队列
     */
    public static Node connect1(Node root) {
        if (root == null) {
            return null;
        }
        
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            Node prev = null; // 前一个节点
            
            for (int i = 0; i < size; i++) {
                Node current = queue.poll();
                
                // 连接前一个节点到当前节点
                if (prev != null) {
                    prev.next = current;
                }
                prev = current;
                
                // 将子节点加入队列
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 每层最后一个节点的next为null（默认值）
        }
        
        return root;
    }
    
    /**
     * 方法2: 利用已建立的next指针 - 最优解法
     * 思路: 利用完美二叉树的特性，通过上一层的next指针来连接当前层的节点
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(1) - 只使用常数级别的额外空间
     * 
     * 核心思想:
     * 1. 使用两个指针: leftmost指向每层的最左节点，current用于遍历当前层
     * 2. 对于每个节点，连接其左子节点到右子节点
     * 3. 连接相邻子树的节点（通过next指针）
     * 
     * 优点:
     * - 空间复杂度最优
     * - 充分利用完美二叉树的特性
     * 缺点:
     * - 只适用于完美二叉树
     * 
     * 关键步骤:
     * 1. leftmost指针始终指向当前层的最左节点
     * 2. current指针遍历当前层，连接下一层的节点
     * 3. 当leftmost没有左子节点时，说明到达叶子层，遍历结束
     */
    public static Node connect2(Node root) {
        if (root == null) {
            return null;
        }
        
        // leftmost指针始终指向当前层的最左节点
        Node leftmost = root;
        
        // 当leftmost还有下一层时继续循环
        while (leftmost.left != null) {
            // current指针用于遍历当前层
            Node current = leftmost;
            
            while (current != null) {
                // 连接当前节点的左子节点到右子节点
                current.left.next = current.right;
                
                // 如果当前节点有下一个右侧节点，连接当前节点的右子节点到下一个节点的左子节点
                if (current.next != null) {
                    current.right.next = current.next.left;
                }
                
                // 移动到当前层的下一个节点
                current = current.next;
            }
            
            // 移动到下一层的最左节点
            leftmost = leftmost.left;
        }
        
        return root;
    }
    
    /**
     * 方法3: 递归解法 - 深度优先遍历
     * 思路: 使用递归连接相邻节点，先处理同一父节点的子节点，再处理跨父节点的子节点
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(logN) - 递归调用栈的深度，对于完美二叉树为树的高度
     * 
     * 优点:
     * - 代码简洁，递归思想清晰
     * - 不需要显式使用队列
     * 缺点:
     * - 递归调用栈可能较深
     * - 对于大树可能栈溢出
     * 
     * 关键递归关系:
     * 1. 连接左子节点到右子节点
     * 2. 连接右子节点到下一个左子节点（如果存在）
     * 3. 递归处理左右子树
     */
    public static Node connect3(Node root) {
        if (root == null) {
            return null;
        }
        
        connectNodes(root.left, root.right);
        return root;
    }
    
    private static void connectNodes(Node node1, Node node2) {
        if (node1 == null || node2 == null) {
            return;
        }
        
        // 连接两个节点
        node1.next = node2;
        
        // 递归连接同一父节点的子节点
        connectNodes(node1.left, node1.right);
        connectNodes(node2.left, node2.right);
        
        // 递归连接跨父节点的子节点
        connectNodes(node1.right, node2.left);
    }
    
    /**
     * 辅助方法: 根据数组构建完美二叉树
     * 数组格式: 层序遍历，完美二叉树
     * 例如: [1,2,3,4,5,6,7] 构建3层的完美二叉树
     */
    public static Node buildPerfectBinaryTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) {
            return null;
        }
        
        Node root = new Node(arr[0]);
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        
        while (!queue.isEmpty() && i < arr.length) {
            Node current = queue.poll();
            
            // 构建左子节点
            if (i < arr.length && arr[i] != null) {
                current.left = new Node(arr[i]);
                queue.offer(current.left);
            }
            i++;
            
            // 构建右子节点
            if (i < arr.length && arr[i] != null) {
                current.right = new Node(arr[i]);
                queue.offer(current.right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 辅助方法: 打印树的层序遍历结果（包含next指针信息）
     * 用于验证连接结果
     */
    public static void printTreeWithNext(Node root) {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }
        
        Node levelStart = root;
        
        while (levelStart != null) {
            Node current = levelStart;
            StringBuilder level = new StringBuilder();
            boolean first = true;
            
            while (current != null) {
                if (!first) {
                    level.append(" -> ");
                }
                level.append(current.val);
                first = false;
                current = current.next;
            }
            
            System.out.println(level.toString());
            levelStart = levelStart.left; // 完美二叉树，最左节点一定有左子节点
        }
    }
    
    /**
     * 测试方法: 包含多种测试用例
     * 覆盖边界情况和典型场景
     */
    public static void main(String[] args) {
        System.out.println("========== LeetCode 116 测试 ==========");
        
        // 测试用例1: 3层完美二叉树 [1,2,3,4,5,6,7]
        System.out.println("\n测试用例1: 3层完美二叉树");
        Integer[] arr1 = {1, 2, 3, 4, 5, 6, 7};
        Node root1 = buildPerfectBinaryTree(arr1);
        
        System.out.println("方法1结果:");
        printTreeWithNext(connect1(buildPerfectBinaryTree(arr1)));
        
        System.out.println("\n方法2结果:");
        printTreeWithNext(connect2(buildPerfectBinaryTree(arr1)));
        
        System.out.println("\n方法3结果:");
        printTreeWithNext(connect3(buildPerfectBinaryTree(arr1)));
        
        // 测试用例2: 单节点树
        System.out.println("\n测试用例2: 单节点树");
        Integer[] arr2 = {1};
        Node root2 = buildPerfectBinaryTree(arr2);
        
        System.out.println("方法2结果:");
        printTreeWithNext(connect2(root2));
        
        // 测试用例3: 空树
        System.out.println("\n测试用例3: 空树");
        Node root3 = null;
        System.out.println("方法2结果: " + connect2(root3));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（层序遍历）: 通用性强，但空间复杂度较高");
        System.out.println("2. 方法2（利用next指针）: 空间复杂度最优，只适用于完美二叉树");
        System.out.println("3. 方法3（递归）: 代码简洁，但递归深度可能较大");
        System.out.println("推荐: 对于完美二叉树使用方法2，对于一般二叉树使用LeetCode 117的解法");
    }
}

/*
Python实现:

class Node:
    def __init__(self, val=0, left=None, right=None, next=None):
        self.val = val
        self.left = left
        self.right = right
        self.next = next

class Solution:
    # 方法1: 层序遍历法
    def connect1(self, root: 'Node') -> 'Node':
        if not root:
            return None
            
        from collections import deque
        queue = deque([root])
        
        while queue:
            size = len(queue)
            prev = None
            
            for i in range(size):
                current = queue.popleft()
                
                if prev:
                    prev.next = current
                prev = current
                
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
                    
        return root
    
    # 方法2: 利用next指针（最优解）
    def connect2(self, root: 'Node') -> 'Node':
        if not root:
            return None
            
        leftmost = root
        
        while leftmost.left:
            current = leftmost
            
            while current:
                # 连接同一父节点的子节点
                current.left.next = current.right
                
                # 连接相邻父节点的子节点
                if current.next:
                    current.right.next = current.next.left
                    
                current = current.next
                
            leftmost = leftmost.left
            
        return root
    
    # 方法3: 递归解法
    def connect3(self, root: 'Node') -> 'Node':
        if not root:
            return None
            
        self._connect_nodes(root.left, root.right)
        return root
    
    def _connect_nodes(self, node1: 'Node', node2: 'Node'):
        if not node1 or not node2:
            return
            
        node1.next = node2
        
        self._connect_nodes(node1.left, node1.right)
        self._connect_nodes(node2.left, node2.right)
        self._connect_nodes(node1.right, node2.left)

# 测试代码
if __name__ == "__main__":
    # 构建测试树
    root = Node(1)
    root.left = Node(2)
    root.right = Node(3)
    root.left.left = Node(4)
    root.left.right = Node(5)
    root.right.left = Node(6)
    root.right.right = Node(7)
    
    solution = Solution()
    
    print("方法1结果:")
    result1 = solution.connect1(root)
    # 打印结果...
    
    print("方法2结果:")
    result2 = solution.connect2(root)
    # 打印结果...

C++实现:

#include <iostream>
#include <queue>
#include <vector>
using namespace std;

class Node {
public:
    int val;
    Node* left;
    Node* right;
    Node* next;
    
    Node() : val(0), left(NULL), right(NULL), next(NULL) {}
    Node(int _val) : val(_val), left(NULL), right(NULL), next(NULL) {}
    Node(int _val, Node* _left, Node* _right, Node* _next)
        : val(_val), left(_left), right(_right), next(_next) {}
};

class Solution {
public:
    // 方法1: 层序遍历法
    Node* connect1(Node* root) {
        if (!root) return nullptr;
        
        queue<Node*> q;
        q.push(root);
        
        while (!q.empty()) {
            int size = q.size();
            Node* prev = nullptr;
            
            for (int i = 0; i < size; ++i) {
                Node* current = q.front();
                q.pop();
                
                if (prev) {
                    prev->next = current;
                }
                prev = current;
                
                if (current->left) q.push(current->left);
                if (current->right) q.push(current->right);
            }
        }
        
        return root;
    }
    
    // 方法2: 利用next指针（最优解）
    Node* connect2(Node* root) {
        if (!root) return nullptr;
        
        Node* leftmost = root;
        
        while (leftmost->left) {
            Node* current = leftmost;
            
            while (current) {
                // 连接同一父节点的子节点
                current->left->next = current->right;
                
                // 连接相邻父节点的子节点
                if (current->next) {
                    current->right->next = current->next->left;
                }
                
                current = current->next;
            }
            
            leftmost = leftmost->left;
        }
        
        return root;
    }
    
    // 方法3: 递归解法
    Node* connect3(Node* root) {
        if (!root) return nullptr;
        
        connectNodes(root->left, root->right);
        return root;
    }
    
private:
    void connectNodes(Node* node1, Node* node2) {
        if (!node1 || !node2) return;
        
        node1->next = node2;
        
        connectNodes(node1->left, node1->right);
        connectNodes(node2->left, node2->right);
        connectNodes(node1->right, node2->left);
    }
};

// 测试代码
int main() {
    // 构建测试树
    Node* root = new Node(1);
    root->left = new Node(2);
    root->right = new Node(3);
    root->left->left = new Node(4);
    root->left->right = new Node(5);
    root->right->left = new Node(6);
    root->right->right = new Node(7);
    
    Solution solution;
    
    cout << "方法2结果:" << endl;
    Node* result = solution.connect2(root);
    // 打印结果...
    
    // 释放内存
    delete root->right->right;
    delete root->right->left;
    delete root->right;
    delete root->left;
    delete root;
    
    return 0;
}
*/
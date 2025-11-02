package class036;

import java.util.*;

/**
 * LeetCode 117. 填充每个节点的下一个右侧节点指针 II
 * 题目链接: https://leetcode.cn/problems/populating-next-right-pointers-in-each-node-ii/
 * 题目描述: 给定一个二叉树，填充它的每个 next 指针，让这个指针指向其下一个右侧节点。
 * 如果找不到下一个右侧节点，则将 next 指针设置为 null。
 * 初始状态下，所有 next 指针都被设置为 null。
 * 
 * 与LeetCode 116的区别: 本题的二叉树不一定是完美二叉树，可能缺少某些节点
 * 
 * 核心算法思想:
 * 1. 层序遍历(BFS): 使用队列进行层序遍历，在每层中连接相邻节点的next指针
 * 2. 利用虚拟头节点: 使用虚拟头节点来简化每层的连接逻辑
 * 3. 空间优化遍历: 利用已建立的next指针进行层序遍历，避免使用队列
 * 
 * 时间复杂度分析:
 * - 所有方法: O(N)，每个节点访问一次
 * 
 * 空间复杂度分析:
 * - 方法1(层序遍历): O(W)，W为树的最大宽度
 * - 方法2(虚拟头节点): O(1)，只使用常数级别的额外空间
 * - 方法3(空间优化): O(1)，只使用常数级别的额外空间
 * 
 * 相关题目:
 * 1. LeetCode 116. 填充每个节点的下一个右侧节点指针 - 完美二叉树版本
 * 2. LeetCode 199. 二叉树的右视图 - 类似的分层处理思想
 * 3. LeetCode 102. 二叉树的层序遍历 - 基础层序遍历
 * 
 * 工程化考量:
 * 1. 通用性: 适用于任意二叉树结构
 * 2. 鲁棒性: 处理各种边界情况（空树、单节点、斜树等）
 * 3. 性能优化: 对于大数据量需要考虑空间效率
 */
public class LeetCode117_PopulatingNextRightPointersInEachNodeII {
    
    // 二叉树节点定义
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
     * 空间复杂度: O(W) - W为树的最大宽度，最坏情况为O(N)
     * 
     * 优点:
     * - 逻辑清晰，易于理解和实现
     * - 适用于各种二叉树结构
     * 缺点:
     * - 需要额外的队列空间
     * - 空间复杂度不是最优
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
                
                // 将子节点加入队列（注意：可能缺少某些子节点）
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
     * 方法2: 使用虚拟头节点 - 最优解法（空间复杂度O(1)）
     * 思路: 使用虚拟头节点来简化每层的连接逻辑，利用已建立的next指针遍历下一层
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(1) - 只使用常数级别的额外空间
     * 
     * 核心思想:
     * 1. 使用虚拟头节点dummy来记录下一层的起始位置
     * 2. 使用current指针遍历当前层，同时构建下一层的连接
     * 3. 使用tail指针来跟踪下一层的最后一个节点
     * 
     * 优点:
     * - 空间复杂度最优
     * - 适用于任意二叉树结构
     * 缺点:
     * - 逻辑相对复杂，需要仔细处理指针连接
     * 
     * 关键步骤:
     * 1. 外层循环: 从根节点开始，逐层向下处理
     * 2. 内层循环: 遍历当前层的所有节点，连接下一层的节点
     * 3. 虚拟头节点: 简化下一层起始位置的处理
     */
    public static Node connect2(Node root) {
        if (root == null) {
            return null;
        }
        
        Node current = root;
        
        while (current != null) {
            // 虚拟头节点，用于记录下一层的起始位置
            Node dummy = new Node(0);
            // tail指针用于跟踪下一层的最后一个节点
            Node tail = dummy;
            
            // 遍历当前层的所有节点
            while (current != null) {
                // 处理左子节点
                if (current.left != null) {
                    tail.next = current.left;
                    tail = tail.next;
                }
                
                // 处理右子节点
                if (current.right != null) {
                    tail.next = current.right;
                    tail = tail.next;
                }
                
                // 移动到当前层的下一个节点
                current = current.next;
            }
            
            // 移动到下一层的第一个节点（通过虚拟头节点的next）
            current = dummy.next;
        }
        
        return root;
    }
    
    /**
     * 方法3: 空间优化遍历 - 另一种O(1)空间复杂度的解法
     * 思路: 使用两个指针分别跟踪当前层和下一层，避免使用虚拟头节点
     * 时间复杂度: O(N) - 每个节点访问一次
     * 空间复杂度: O(1) - 只使用常数级别的额外空间
     * 
     * 核心思想:
     * 1. head指针指向下一层的起始节点
     * 2. prev指针跟踪下一层的最后一个节点
     * 3. current指针遍历当前层
     * 
     * 优点:
     * - 空间复杂度最优
     * - 不使用虚拟节点，更直观
     * 缺点:
     * - 需要处理头节点的特殊情况
     * 
     * 关键步骤:
     * 1. 找到下一层的第一个非空节点作为head
     * 2. 使用prev连接下一层的节点
     * 3. 当前层遍历完成后，移动到下一层
     */
    public static Node connect3(Node root) {
        if (root == null) {
            return null;
        }
        
        Node current = root;
        
        while (current != null) {
            Node head = null; // 下一层的头节点
            Node prev = null; // 下一层的前一个节点
            
            // 遍历当前层，构建下一层的连接
            while (current != null) {
                // 处理左子节点
                if (current.left != null) {
                    if (head == null) {
                        head = current.left; // 设置下一层的头节点
                    }
                    if (prev != null) {
                        prev.next = current.left;
                    }
                    prev = current.left;
                }
                
                // 处理右子节点
                if (current.right != null) {
                    if (head == null) {
                        head = current.right; // 设置下一层的头节点
                    }
                    if (prev != null) {
                        prev.next = current.right;
                    }
                    prev = current.right;
                }
                
                // 移动到当前层的下一个节点
                current = current.next;
            }
            
            // 移动到下一层
            current = head;
        }
        
        return root;
    }
    
    /**
     * 辅助方法: 根据数组构建任意二叉树（不一定是完美二叉树）
     * 数组格式: 层序遍历，null表示空节点
     * 例如: [1,2,3,4,null,null,5] 构建非完美二叉树
     */
    public static Node buildBinaryTree(Integer[] arr) {
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
     * 用于验证连接结果，支持非完美二叉树
     */
    public static void printTreeWithNext(Node root) {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }
        
        // 找到每层的最左节点
        Node levelStart = root;
        
        while (levelStart != null) {
            Node current = levelStart;
            StringBuilder level = new StringBuilder();
            boolean first = true;
            
            // 找到下一层的最左节点
            Node nextLevelStart = null;
            Node nextLevelPrev = null;
            
            while (current != null) {
                if (!first) {
                    level.append(" -> ");
                }
                level.append(current.val);
                first = false;
                
                // 同时构建下一层的最左节点
                if (nextLevelStart == null) {
                    if (current.left != null) {
                        nextLevelStart = current.left;
                        nextLevelPrev = nextLevelStart;
                    } else if (current.right != null) {
                        nextLevelStart = current.right;
                        nextLevelPrev = nextLevelStart;
                    }
                } else {
                    if (current.left != null) {
                        nextLevelPrev.next = current.left;
                        nextLevelPrev = nextLevelPrev.next;
                    }
                    if (current.right != null) {
                        nextLevelPrev.next = current.right;
                        nextLevelPrev = nextLevelPrev.next;
                    }
                }
                
                current = current.next;
            }
            
            System.out.println(level.toString());
            levelStart = nextLevelStart;
        }
    }
    
    /**
     * 测试方法: 包含多种测试用例
     * 覆盖各种边界情况和典型场景
     */
    public static void main(String[] args) {
        System.out.println("========== LeetCode 117 测试 ==========");
        
        // 测试用例1: 非完美二叉树 [1,2,3,4,null,null,5]
        System.out.println("\n测试用例1: 非完美二叉树");
        Integer[] arr1 = {1, 2, 3, 4, null, null, 5};
        Node root1 = buildBinaryTree(arr1);
        
        System.out.println("方法1结果:");
        printTreeWithNext(connect1(buildBinaryTree(arr1)));
        
        System.out.println("\n方法2结果:");
        printTreeWithNext(connect2(buildBinaryTree(arr1)));
        
        System.out.println("\n方法3结果:");
        printTreeWithNext(connect3(buildBinaryTree(arr1)));
        
        // 测试用例2: 单节点树
        System.out.println("\n测试用例2: 单节点树");
        Integer[] arr2 = {1};
        Node root2 = buildBinaryTree(arr2);
        
        System.out.println("方法2结果:");
        printTreeWithNext(connect2(root2));
        
        // 测试用例3: 斜树（只有左子树）
        System.out.println("\n测试用例3: 斜树");
        Integer[] arr3 = {1, 2, null, 3, null, 4, null};
        Node root3 = buildBinaryTree(arr3);
        
        System.out.println("方法2结果:");
        printTreeWithNext(connect2(root3));
        
        // 测试用例4: 空树
        System.out.println("\n测试用例4: 空树");
        Node root4 = null;
        System.out.println("方法2结果: " + connect2(root4));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（层序遍历）: 通用性强，逻辑清晰，但空间复杂度较高");
        System.out.println("2. 方法2（虚拟头节点）: 空间复杂度最优，推荐使用");
        System.out.println("3. 方法3（空间优化）: 空间复杂度最优，逻辑相对复杂");
        System.out.println("推荐: 对于一般二叉树使用方法2，平衡代码可读性和空间效率");
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
    
    # 方法2: 使用虚拟头节点（最优解）
    def connect2(self, root: 'Node') -> 'Node':
        if not root:
            return None
            
        current = root
        
        while current:
            dummy = Node(0)  # 虚拟头节点
            tail = dummy
            
            # 遍历当前层
            while current:
                if current.left:
                    tail.next = current.left
                    tail = tail.next
                if current.right:
                    tail.next = current.right
                    tail = tail.next
                    
                current = current.next
                
            # 移动到下一层
            current = dummy.next
            
        return root

# 测试代码
if __name__ == "__main__":
    # 构建测试树
    root = Node(1)
    root.left = Node(2)
    root.right = Node(3)
    root.left.left = Node(4)
    root.right.right = Node(5)
    
    solution = Solution()
    result = solution.connect2(root)

C++实现:

#include <iostream>
#include <queue>
using namespace std;

class Node {
public:
    int val;
    Node* left;
    Node* right;
    Node* next;
    
    Node() : val(0), left(NULL), right(NULL), next(NULL) {}
    Node(int _val) : val(_val), left(NULL), right(NULL), next(NULL) {}
};

class Solution {
public:
    // 方法2: 使用虚拟头节点（最优解）
    Node* connect(Node* root) {
        if (!root) return nullptr;
        
        Node* current = root;
        
        while (current) {
            Node* dummy = new Node(0);  // 虚拟头节点
            Node* tail = dummy;
            
            // 遍历当前层
            while (current) {
                if (current->left) {
                    tail->next = current->left;
                    tail = tail->next;
                }
                if (current->right) {
                    tail->next = current->right;
                    tail = tail->next;
                }
                
                current = current->next;
            }
            
            // 移动到下一层
            current = dummy->next;
            delete dummy;  // 释放虚拟头节点
        }
        
        return root;
    }
};

// 测试代码
int main() {
    // 构建测试树
    Node* root = new Node(1);
    root->left = new Node(2);
    root->right = new Node(3);
    root->left->left = new Node(4);
    root->right->right = new Node(5);
    
    Solution solution;
    Node* result = solution.connect(root);
    
    // 释放内存...
    return 0;
}
*/
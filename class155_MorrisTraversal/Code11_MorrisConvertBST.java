package class124;

import java.util.Stack;

/**
 * 使用Morris遍历解决把二叉搜索树转换为累加树问题
 * 
 * 题目来源：
 * - 把二叉搜索树转换为累加树：LeetCode 538. Convert BST to Greater Tree
 *   链接：https://leetcode.cn/problems/convert-bst-to-greater-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris反向中序遍历转换BST
 * 2. 递归版本的转换BST
 * 3. 迭代版本的转换BST
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/python-morris-fan-xiang-zhong-xu-bian-li-zhuan-huan-by-xxx/
 * - C++: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/c-morris-fan-xiang-zhong-xu-bian-li-zhuan-huan-by-xxx/
 */
public class Code11_MorrisConvertBST {

    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 使用Morris反向中序遍历将BST转换为累加树
     * 这是空间最优的实现，仅使用O(1)的额外空间
     * 
     * @param root BST的根节点
     * @return 转换后的累加树的根节点
     * @throws NullPointerException 如果root为null（但代码已处理null情况，此处仅作文档说明）
     * 
     * 题目描述：
     * 给出二叉搜索树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），
     * 使每个节点 node 的新值等于原树中大于或等于 node.val 的值之和。
     * 
     * 解题思路：
     * 1. 利用BST的性质：中序遍历得到递增序列
     * 2. 累加树需要的是大于等于当前节点值的所有节点值之和
     * 3. 可以通过反向中序遍历（右-根-左）来实现
     * 4. 在反向中序遍历过程中维护累加和
     * 5. 本实现提供三种方法：Morris反向中序遍历、递归DFS、迭代DFS
     * 
     * 算法步骤（Morris反向中序遍历）：
     * 1. 使用Morris反向中序遍历遍历BST（右-根-左）
     * 2. 在遍历过程中维护累加和sum
     * 3. 每个节点的值更新为累加和
     * 
     * Morris反向中序遍历的实现要点：
     * 1. 与标准中序遍历相反，先处理右子树
     * 2. 找前驱节点时，是在右子树中找最左节点
     * 3. 线索建立和断开的逻辑与标准中序遍历对称
     * 
     * 时间复杂度：
     * - Morris方法：O(n) - 需要遍历所有节点，每个节点最多被访问3次
     * - 递归方法：O(n) - 每个节点被访问一次
     * - 迭代方法：O(n) - 每个节点被访问一次
     * 
     * 空间复杂度：
     * - Morris方法：O(1) - 仅使用常数额外空间
     * - 递归方法：O(h) - h为树高，最坏情况下为O(n)
     * - 迭代方法：O(h) - 栈的空间复杂度，最坏情况下为O(n)
     * 
     * 是否为最优解：
     * - 从空间复杂度角度，Morris方法最优
     * - 从代码简洁性角度，递归方法更直观
     * - 实际应用中可根据空间限制选择合适的方法
     * 
     * 适用场景：
     * 1. 需要节省内存空间的环境
     * 2. BST反向遍历的应用场景
     * 3. 面试中展示对Morris遍历的深入理解
     * 4. 大规模二叉搜索树的转换，内存受限场景
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/python-morris-fan-xiang-zhong-xu-bian-li-zhuan-huan-by-xxx/
     * - C++: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/c-morris-fan-xiang-zhong-xu-bian-li-zhuan-huan-by-xxx/
     */
    public TreeNode convertBST(TreeNode root) {
        // 防御性编程：处理空树情况
        if (root == null) {
            return null;
        }
        
        int sum = 0;                   // 累加和
        TreeNode cur = root;           // 当前节点
        TreeNode mostLeft = null;      // 最左节点（前驱节点）
        
        // Morris反向中序遍历（右-根-左）的核心循环
        while (cur != null) {
            mostLeft = cur.right;
            
            // 如果当前节点有右子树
            if (mostLeft != null) {
                // 找到右子树中的最左节点（前驱节点）
                // 这是与标准中序遍历的关键区别之一
                while (mostLeft.left != null && mostLeft.left != cur) {
                    mostLeft = mostLeft.left;
                }
                
                // 判断前驱节点的左指针状态
                if (mostLeft.left == null) {
                    // 第一次到达，建立线索
                    // 线索指向当前节点，用于后续回溯
                    mostLeft.left = cur;
                    // 继续向右子树深入，保证先访问右子树
                    cur = cur.right;
                    continue;  // 跳过当前迭代的剩余部分
                } else {
                    // 第二次到达，断开线索
                    // 恢复树的原始结构
                    mostLeft.left = null;
                    // 此时需要处理当前节点（在第二次访问时）
                }
            }
            
            // 处理当前节点（反向中序遍历的核心处理逻辑）
            // 更新累加和并设置节点的新值
            sum += cur.val;
            cur.val = sum;
            
            // 处理完当前节点后，移动到左子树
            // 保证遍历顺序为：右-根-左
            cur = cur.left;
        }
        
        // 返回转换后的根节点
        return root;
    }
    
    /**
     * 使用递归DFS方法将BST转换为累加树
     * 递归实现更简洁直观，但空间复杂度为O(h)
     * 
     * @param root BST的根节点
     * @return 转换后的累加树的根节点
     * 
     * 算法步骤（递归DFS）：
     * 1. 递归进行反向中序遍历（右-根-左）
     * 2. 维护一个全局或引用类型的累加和
     * 3. 访问节点时更新其值为累加和，并更新累加和
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/python-di-gui-zhuan-huan-bst-by-xxx/
     * - C++: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/c-di-gui-zhuan-huan-bst-by-xxx/
     */
    public TreeNode convertBSTRecursive(TreeNode root) {
        // 使用整型数组作为可变引用，存储累加和
        // 也可以使用成员变量，但为了保持方法的独立性，使用数组
        int[] sum = new int[1];  // sum[0]存储累加和
        dfs(root, sum);
        return root;
    }
    
    /**
     * 递归DFS辅助函数，执行反向中序遍历（右-根-左）
     * 
     * @param node 当前节点
     * @param sum 累加和（作为可变引用传递）
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/python-di-gui-zhuan-huan-bst-by-xxx/
     * - C++: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/c-di-gui-zhuan-huan-bst-by-xxx/
     */
    private void dfs(TreeNode node, int[] sum) {
        // 基本情况：节点为空
        if (node == null) {
            return;
        }
        
        // 1. 递归处理右子树（先访问右子树）
        dfs(node.right, sum);
        
        // 2. 处理当前节点
        // 更新累加和并设置节点的新值
        sum[0] += node.val;
        node.val = sum[0];
        
        // 3. 递归处理左子树（最后访问左子树）
        dfs(node.left, sum);
    }
    
    /**
     * 使用迭代DFS方法将BST转换为累加树
     * 
     * @param root BST的根节点
     * @return 转换后的累加树的根节点
     * 
     * 算法步骤（迭代DFS）：
     * 1. 使用栈模拟递归的反向中序遍历
     * 2. 维护累加和变量
     * 3. 访问节点时更新其值和累加和
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/python-die-dai-zhuan-huan-bst-by-xxx/
     * - C++: https://leetcode.cn/problems/convert-bst-to-greater-tree/solution/c-die-dai-zhuan-huan-bst-by-xxx/
     */
    public TreeNode convertBSTIterative(TreeNode root) {
        // 防御性编程：处理空树情况
        if (root == null) {
            return null;
        }
        
        int sum = 0;  // 累加和
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        
        // 迭代反向中序遍历（右-根-左）
        while (cur != null || !stack.isEmpty()) {
            // 1. 一直向右遍历，将节点入栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.right;
            }
            
            // 2. 处理栈顶节点
            cur = stack.pop();
            
            // 3. 更新累加和并设置节点的新值
            sum += cur.val;
            cur.val = sum;
            
            // 4. 处理左子树
            cur = cur.left;
        }
        
        return root;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建测试树: [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(1);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);
        root.left.right.right = new TreeNode(3);
        root.right.right.right = new TreeNode(8);
        
        Code11_MorrisConvertBST solution = new Code11_MorrisConvertBST();
        
        System.out.println("测试用例: [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]");
        System.out.println("原始树中序遍历结果: ");
        printInOrder(root);
        
        // 使用Morris方法转换
        TreeNode convertedRoot = solution.convertBST(root);
        System.out.println("\nMorris方法转换后中序遍历结果: ");
        printInOrder(convertedRoot);
    }
    
    /**
     * 中序遍历打印树节点值
     * @param root 树的根节点
     */
    public static void printInOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        
        printInOrder(root.left);
        System.out.print(root.val + " ");
        printInOrder(root.right);
    }
}

/*
#include <iostream>
#include <stack>
using namespace std;

// 二叉树节点定义
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
    // Morris反向中序遍历方法
    TreeNode* convertBST(TreeNode* root) {
        if (!root) return nullptr;
        
        int sum = 0;
        TreeNode* cur = root;
        TreeNode* mostLeft = nullptr;
        
        while (cur) {
            mostLeft = cur->right;
            if (mostLeft) {
                // 找到右子树中的最左节点（前驱节点）
                while (mostLeft->left && mostLeft->left != cur) {
                    mostLeft = mostLeft->left;
                }
                
                if (!mostLeft->left) {
                    // 第一次到达，建立线索
                    mostLeft->left = cur;
                    cur = cur->right;
                    continue;
                } else {
                    // 第二次到达，断开线索
                    mostLeft->left = nullptr;
                }
            }
            
            // 处理当前节点
            sum += cur->val;
            cur->val = sum;
            
            cur = cur->left;
        }
        
        return root;
    }
    
    // 递归DFS方法
    TreeNode* convertBSTRecursive(TreeNode* root) {
        int sum = 0;
        dfs(root, sum);
        return root;
    }
    
private:
    void dfs(TreeNode* node, int& sum) {
        if (!node) return;
        
        // 先处理右子树
        dfs(node->right, sum);
        
        // 处理当前节点
        sum += node->val;
        node->val = sum;
        
        // 最后处理左子树
        dfs(node->left, sum);
    }
    
public:
    // 迭代DFS方法
    TreeNode* convertBSTIterative(TreeNode* root) {
        if (!root) return nullptr;
        
        int sum = 0;
        stack<TreeNode*> stk;
        TreeNode* cur = root;
        
        while (cur || !stk.empty()) {
            // 一直向右遍历
            while (cur) {
                stk.push(cur);
                cur = cur->right;
            }
            
            // 处理栈顶节点
            cur = stk.top();
            stk.pop();
            
            // 更新累加和和节点值
            sum += cur->val;
            cur->val = sum;
            
            // 处理左子树
            cur = cur->left;
        }
        
        return root;
    }
    
    // 深度克隆树的辅助方法
    TreeNode* deepClone(TreeNode* root) {
        if (!root) return nullptr;
        TreeNode* newRoot = new TreeNode(root->val);
        newRoot->left = deepClone(root->left);
        newRoot->right = deepClone(root->right);
        return newRoot;
    }
    
    // 打印树的辅助方法
    void printTreeInOrder(TreeNode* root) {
        if (!root) return;
        printTreeInOrder(root->left);
        cout << root->val << " ";
        printTreeInOrder(root->right);
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1: [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
    TreeNode* root1 = new TreeNode(4);
    root1->left = new TreeNode(1);
    root1->right = new TreeNode(6);
    root1->left->left = new TreeNode(0);
    root1->left->right = new TreeNode(2);
    root1->right->left = new TreeNode(5);
    root1->right->right = new TreeNode(7);
    root1->left->right->right = new TreeNode(3);
    root1->right->right->right = new TreeNode(8);
    
    // 克隆树用于不同方法的测试
    TreeNode* root1Clone1 = solution.deepClone(root1);
    TreeNode* root1Clone2 = solution.deepClone(root1);
    
    cout << "===== 测试用例1 =====" << endl;
    cout << "原始树中序遍历: " << endl;
    solution.printTreeInOrder(root1);
    cout << endl;
    
    TreeNode* result1Morris = solution.convertBST(root1Clone1);
    cout << "Morris方法转换后中序遍历: " << endl;
    solution.printTreeInOrder(result1Morris);
    cout << endl;
    
    // 释放内存...
    
    return 0;
}
*/

/*
 * Python版本实现（注释版）
 * 
 * # 二叉树节点定义
 * class TreeNode:
 *     def __init__(self, val=0, left=None, right=None):
 *         self.val = val
 *         self.left = left
 *         self.right = right
 * 
 * class Solution:
 *     # Morris反向中序遍历方法
 *     def convertBST(self, root):
 *         if not root:
 *             return None
 *         
 *         total_sum = 0
 *         cur = root
 *         
 *         while cur:
 *             most_left = cur.right
 *             if most_left:
 *                 # 找到右子树中的最左节点（前驱节点）
 *                 while most_left.left and most_left.left != cur:
 *                     most_left = most_left.left
 *                 
 *                 if not most_left.left:
 *                     # 第一次到达，建立线索
 *                     most_left.left = cur
 *                     cur = cur.right
 *                     continue
 *                 else:
 *                     # 第二次到达，断开线索
 *                     most_left.left = None
 *             
 *             # 处理当前节点
 *             total_sum += cur.val
 *             cur.val = total_sum
 *             
 *             cur = cur.left
 *         
 *         return root
 *     
 *     # 递归DFS方法
 *     def convertBSTRecursive(self, root):
 *         # 使用可变对象存储累加和
 *         total_sum = [0]
 *         
 *         def dfs(node):
 *             if not node:
 *                 return
 *             
 *             # 先处理右子树
 *             dfs(node.right)
 *             
 *             # 处理当前节点
 *             total_sum[0] += node.val
 *             node.val = total_sum[0]
 *             
 *             # 最后处理左子树
 *             dfs(node.left)
 *         
 *         dfs(root)
 *         return root
 *     
 *     # 迭代DFS方法
 *     def convertBSTIterative(self, root):
 *         if not root:
 *             return None
 *         
 *         total_sum = 0
 *         stack = []
 *         cur = root
 *         
 *         while cur or stack:
 *             # 一直向右遍历
 *             while cur:
 *                 stack.append(cur)
 *                 cur = cur.right
 *             
 *             # 处理栈顶节点
 *             cur = stack.pop()
 *             
 *             # 更新累加和和节点值
 *             total_sum += cur.val
 *             cur.val = total_sum
 *             
 *             # 处理左子树
 *             cur = cur.left
 *         
 *         return root
 *     
 *     # 深度克隆树的辅助方法
 *     def deepClone(self, root):
 *         if not root:
 *             return None
 *         new_root = TreeNode(root.val)
 *         new_root.left = self.deepClone(root.left)
 *         new_root.right = self.deepClone(root.right)
 *         return new_root
 *     
 *     # 打印树的辅助方法
 *     def printTreeInOrder(self, root):
 *         result = []
 *         
 *         def inOrder(node):
 *             if node:
 *                 inOrder(node.left)
 *                 result.append(str(node.val))
 *                 inOrder(node.right)
 *         
 *         inOrder(root)
 *         return " ".join(result)
 * 
 * # 测试代码
 * def test():
 *     solution = Solution()
 *     
 *     # 测试用例1: [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
 *     root1 = TreeNode(4)
 *     root1.left = TreeNode(1)
 *     root1.right = TreeNode(6)
 *     root1.left.left = TreeNode(0)
 *     root1.left.right = TreeNode(2)
 *     root1.right.left = TreeNode(5)
 *     root1.right.right = TreeNode(7)
 *     root1.left.right.right = TreeNode(3)
 *     root1.right.right.right = TreeNode(8)
 *     
 *     # 克隆树用于不同方法的测试
 *     root1_clone1 = solution.deepClone(root1)
 *     root1_clone2 = solution.deepClone(root1)
 *     
 *     print("===== 测试用例1 =====")
 *     print("原始树中序遍历:")
 *     print(solution.printTreeInOrder(root1))
 *     
 *     result1_morris = solution.convertBST(root1_clone1)
 *     print("Morris方法转换后中序遍历:")
 *     print(solution.printTreeInOrder(result1_morris))
 *     
 *     result1_recursive = solution.convertBSTRecursive(root1_clone2)
 *     print("递归方法转换后中序遍历:")
 *     print(solution.printTreeInOrder(result1_recursive))
 *     
 *     result1_iterative = solution.convertBSTIterative(root1)
 *     print("迭代方法转换后中序遍历:")
 *     print(solution.printTreeInOrder(result1_iterative))
 * 
 * if __name__ == "__main__":
 *     test()
 */
}
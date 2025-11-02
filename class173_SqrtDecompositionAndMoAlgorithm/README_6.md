# 二叉搜索树查找算法专题

## 算法原理

二叉搜索树（Binary Search Tree，简称BST）是一种特殊的二叉树，它满足以下性质：
- 左子树上所有节点的值均小于根节点的值
- 右子树上所有节点的值均大于根节点的值
- 左右子树也分别为二叉搜索树

这种特性使得二叉搜索树可以高效地进行查找操作，平均时间复杂度为O(log n)。

## 基本操作

### 1. 基本二叉搜索树实现

**题目来源**：数据结构基础题

**题目描述**：实现二叉搜索树的基本结构和查找功能

**解题思路**：
- 定义二叉搜索树节点结构
- 实现查找、插入等基本操作
- 处理边界情况

**代码实现**：

**Java代码**：
```java
public class BinarySearchTree {
    // 定义树节点
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
        }
    }
    
    private TreeNode root;
    
    // 插入节点
    public void insert(int val) {
        root = insertRec(root, val);
    }
    
    private TreeNode insertRec(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        
        if (val < root.val) {
            root.left = insertRec(root.left, val);
        } else if (val > root.val) {
            root.right = insertRec(root.right, val);
        }
        
        return root;
    }
    
    // 查找节点
    public boolean search(int val) {
        return searchRec(root, val);
    }
    
    private boolean searchRec(TreeNode root, int val) {
        if (root == null) {
            return false;
        }
        
        if (val == root.val) {
            return true;
        }
        
        if (val < root.val) {
            return searchRec(root.left, val);
        } else {
            return searchRec(root.right, val);
        }
    }
    
    // 中序遍历（验证BST的正确性）
    public void inorderTraversal() {
        inorderRec(root);
        System.out.println();
    }
    
    private void inorderRec(TreeNode root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.val + " ");
            inorderRec(root.right);
        }
    }
    
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        
        for (int val : values) {
            bst.insert(val);
        }
        
        System.out.print("中序遍历结果: ");
        bst.inorderTraversal();
        
        System.out.println("查找值 40: " + bst.search(40));
        System.out.println("查找值 45: " + bst.search(45));
    }
}
```

**Python代码**：
```python
class TreeNode:
    def __init__(self, val=0):
        self.val = val
        self.left = None
        self.right = None

class BinarySearchTree:
    def __init__(self):
        self.root = None
    
    def insert(self, val):
        """插入节点到BST"""
        self.root = self._insert_recursive(self.root, val)
    
    def _insert_recursive(self, root, val):
        if root is None:
            return TreeNode(val)
        
        if val < root.val:
            root.left = self._insert_recursive(root.left, val)
        elif val > root.val:
            root.right = self._insert_recursive(root.right, val)
        
        return root
    
    def search(self, val):
        """在BST中查找值"""
        return self._search_recursive(self.root, val)
    
    def _search_recursive(self, root, val):
        if root is None:
            return False
        
        if val == root.val:
            return True
        
        if val < root.val:
            return self._search_recursive(root.left, val)
        else:
            return self._search_recursive(root.right, val)
    
    def inorder_traversal(self):
        """中序遍历BST"""
        result = []
        self._inorder_recursive(self.root, result)
        return result
    
    def _inorder_recursive(self, root, result):
        if root:
            self._inorder_recursive(root.left, result)
            result.append(root.val)
            self._inorder_recursive(root.right, result)

# 测试代码
if __name__ == "__main__":
    bst = BinarySearchTree()
    values = [50, 30, 70, 20, 40, 60, 80]
    
    for val in values:
        bst.insert(val)
    
    print("中序遍历结果:", bst.inorder_traversal())
    print("查找值 40:", bst.search(40))
    print("查找值 45:", bst.search(45))
```

**C++代码**：
```cpp
#include <iostream>
#include <vector>
using namespace std;

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

class BinarySearchTree {
private:
    TreeNode* root;
    
    TreeNode* insertRec(TreeNode* root, int val) {
        if (root == nullptr) {
            return new TreeNode(val);
        }
        
        if (val < root->val) {
            root->left = insertRec(root->left, val);
        } else if (val > root->val) {
            root->right = insertRec(root->right, val);
        }
        
        return root;
    }
    
    bool searchRec(TreeNode* root, int val) {
        if (root == nullptr) {
            return false;
        }
        
        if (val == root->val) {
            return true;
        }
        
        if (val < root->val) {
            return searchRec(root->left, val);
        } else {
            return searchRec(root->right, val);
        }
    }
    
    void inorderRec(TreeNode* root, vector<int>& result) {
        if (root != nullptr) {
            inorderRec(root->left, result);
            result.push_back(root->val);
            inorderRec(root->right, result);
        }
    }
    
    void deleteTree(TreeNode* root) {
        if (root != nullptr) {
            deleteTree(root->left);
            deleteTree(root->right);
            delete root;
        }
    }
    
public:
    BinarySearchTree() : root(nullptr) {}
    
    ~BinarySearchTree() {
        deleteTree(root);
    }
    
    void insert(int val) {
        root = insertRec(root, val);
    }
    
    bool search(int val) {
        return searchRec(root, val);
    }
    
    vector<int> inorderTraversal() {
        vector<int> result;
        inorderRec(root, result);
        return result;
    }
};

int main() {
    BinarySearchTree bst;
    int values[] = {50, 30, 70, 20, 40, 60, 80};
    
    for (int val : values) {
        bst.insert(val);
    }
    
    vector<int> result = bst.inorderTraversal();
    cout << "中序遍历结果: ";
    for (int val : result) {
        cout << val << " ";
    }
    cout << endl;
    
    cout << "查找值 40: " << (bst.search(40) ? "true" : "false") << endl;
    cout << "查找值 45: " << (bst.search(45) ? "true" : "false") << endl;
    
    return 0;
}
```

### 2. 二叉搜索树的高级查找操作

**题目来源**：数据结构进阶题

**题目描述**：实现二叉搜索树的高级查找功能，如查找最小值、最大值、前驱节点、后继节点等

**解题思路**：
- 利用BST的性质实现各种高级查找操作
- 注意处理边界情况

**代码实现**：

**Java代码**：
```java
public class BSTAdvancedOperations {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
        }
    }
    
    // 查找最小值
    public static TreeNode findMin(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    
    // 查找最大值
    public static TreeNode findMax(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }
    
    // 查找给定值的节点
    public static TreeNode findNode(TreeNode root, int val) {
        if (root == null || root.val == val) {
            return root;
        }
        
        if (val < root.val) {
            return findNode(root.left, val);
        } else {
            return findNode(root.right, val);
        }
    }
    
    // 查找后继节点（大于给定值的最小节点）
    public static TreeNode findSuccessor(TreeNode root, int val) {
        TreeNode current = root;
        TreeNode successor = null;
        
        while (current != null) {
            if (current.val > val) {
                successor = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        
        return successor;
    }
    
    // 查找前驱节点（小于给定值的最大节点）
    public static TreeNode findPredecessor(TreeNode root, int val) {
        TreeNode current = root;
        TreeNode predecessor = null;
        
        while (current != null) {
            if (current.val < val) {
                predecessor = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        
        return predecessor;
    }
    
    // 查找第k小的元素
    public static int findKthSmallest(TreeNode root, int k) {
        int[] result = new int[2]; // result[0]存储计数，result[1]存储结果
        inorderKth(root, k, result);
        return result[1];
    }
    
    private static void inorderKth(TreeNode root, int k, int[] result) {
        if (root == null || result[0] >= k) {
            return;
        }
        
        inorderKth(root.left, k, result);
        
        result[0]++;
        if (result[0] == k) {
            result[1] = root.val;
            return;
        }
        
        inorderKth(root.right, k, result);
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 构建测试树
        TreeNode root = new TreeNode(50);
        root.left = new TreeNode(30);
        root.right = new TreeNode(70);
        root.left.left = new TreeNode(20);
        root.left.right = new TreeNode(40);
        root.right.left = new TreeNode(60);
        root.right.right = new TreeNode(80);
        
        System.out.println("最小值: " + findMin(root).val);
        System.out.println("最大值: " + findMax(root).val);
        System.out.println("查找值 40: " + (findNode(root, 40) != null ? findNode(root, 40).val : "不存在"));
        System.out.println("后继节点(40): " + findSuccessor(root, 40).val);
        System.out.println("前驱节点(40): " + findPredecessor(root, 40).val);
        System.out.println("第3小的元素: " + findKthSmallest(root, 3));
    }
}
```

**Python代码**：
```python
class TreeNode:
    def __init__(self, val=0):
        self.val = val
        self.left = None
        self.right = None

# 查找最小值
def find_min(root):
    if root is None:
        return None
    current = root
    while current.left:
        current = current.left
    return current

# 查找最大值
def find_max(root):
    if root is None:
        return None
    current = root
    while current.right:
        current = current.right
    return current

# 查找给定值的节点
def find_node(root, val):
    if root is None or root.val == val:
        return root
    
    if val < root.val:
        return find_node(root.left, val)
    else:
        return find_node(root.right, val)

# 查找后继节点（大于给定值的最小节点）
def find_successor(root, val):
    current = root
    successor = None
    
    while current:
        if current.val > val:
            successor = current
            current = current.left
        else:
            current = current.right
    
    return successor

# 查找前驱节点（小于给定值的最大节点）
def find_predecessor(root, val):
    current = root
    predecessor = None
    
    while current:
        if current.val < val:
            predecessor = current
            current = current.right
        else:
            current = current.left
    
    return predecessor

# 查找第k小的元素
def find_kth_smallest(root, k):
    result = [0, 0]  # [计数, 结果]
    
    def inorder_kth(node):
        if node is None or result[0] >= k:
            return
        
        inorder_kth(node.left)
        
        result[0] += 1
        if result[0] == k:
            result[1] = node.val
            return
        
        inorder_kth(node.right)
    
    inorder_kth(root)
    return result[1]

# 测试代码
if __name__ == "__main__":
    # 构建测试树
    root = TreeNode(50)
    root.left = TreeNode(30)
    root.right = TreeNode(70)
    root.left.left = TreeNode(20)
    root.left.right = TreeNode(40)
    root.right.left = TreeNode(60)
    root.right.right = TreeNode(80)
    
    print("最小值:", find_min(root).val if find_min(root) else "None")
    print("最大值:", find_max(root).val if find_max(root) else "None")
    node = find_node(root, 40)
    print("查找值 40:", node.val if node else "不存在")
    succ = find_successor(root, 40)
    print("后继节点(40):", succ.val if succ else "不存在")
    pred = find_predecessor(root, 40)
    print("前驱节点(40):", pred.val if pred else "不存在")
    print("第3小的元素:", find_kth_smallest(root, 3))
```

**C++代码**：
```cpp
#include <iostream>
using namespace std;

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

// 查找最小值
TreeNode* findMin(TreeNode* root) {
    if (root == nullptr) {
        return nullptr;
    }
    TreeNode* current = root;
    while (current->left != nullptr) {
        current = current->left;
    }
    return current;
}

// 查找最大值
TreeNode* findMax(TreeNode* root) {
    if (root == nullptr) {
        return nullptr;
    }
    TreeNode* current = root;
    while (current->right != nullptr) {
        current = current->right;
    }
    return current;
}

// 查找给定值的节点
TreeNode* findNode(TreeNode* root, int val) {
    if (root == nullptr || root->val == val) {
        return root;
    }
    
    if (val < root->val) {
        return findNode(root->left, val);
    } else {
        return findNode(root->right, val);
    }
}

// 查找后继节点（大于给定值的最小节点）
TreeNode* findSuccessor(TreeNode* root, int val) {
    TreeNode* current = root;
    TreeNode* successor = nullptr;
    
    while (current != nullptr) {
        if (current->val > val) {
            successor = current;
            current = current->left;
        } else {
            current = current->right;
        }
    }
    
    return successor;
}

// 查找前驱节点（小于给定值的最大节点）
TreeNode* findPredecessor(TreeNode* root, int val) {
    TreeNode* current = root;
    TreeNode* predecessor = nullptr;
    
    while (current != nullptr) {
        if (current->val < val) {
            predecessor = current;
            current = current->right;
        } else {
            current = current->left;
        }
    }
    
    return predecessor;
}

// 查找第k小的元素
void inorderKth(TreeNode* root, int k, int& count, int& result) {
    if (root == nullptr || count >= k) {
        return;
    }
    
    inorderKth(root->left, k, count, result);
    
    count++;
    if (count == k) {
        result = root->val;
        return;
    }
    
    inorderKth(root->right, k, count, result);
}

int findKthSmallest(TreeNode* root, int k) {
    int count = 0;
    int result = -1;
    inorderKth(root, k, count, result);
    return result;
}

// 释放树内存
void deleteTree(TreeNode* root) {
    if (root != nullptr) {
        deleteTree(root->left);
        deleteTree(root->right);
        delete root;
    }
}

int main() {
    // 构建测试树
    TreeNode* root = new TreeNode(50);
    root->left = new TreeNode(30);
    root->right = new TreeNode(70);
    root->left->left = new TreeNode(20);
    root->left->right = new TreeNode(40);
    root->right->left = new TreeNode(60);
    root->right->right = new TreeNode(80);
    
    TreeNode* minNode = findMin(root);
    cout << "最小值: " << (minNode ? minNode->val : -1) << endl;
    
    TreeNode* maxNode = findMax(root);
    cout << "最大值: " << (maxNode ? maxNode->val : -1) << endl;
    
    TreeNode* targetNode = findNode(root, 40);
    cout << "查找值 40: " << (targetNode ? to_string(targetNode->val) : "不存在") << endl;
    
    TreeNode* succNode = findSuccessor(root, 40);
    cout << "后继节点(40): " << (succNode ? succNode->val : -1) << endl;
    
    TreeNode* predNode = findPredecessor(root, 40);
    cout << "前驱节点(40): " << (predNode ? predNode->val : -1) << endl;
    
    cout << "第3小的元素: " << findKthSmallest(root, 3) << endl;
    
    // 释放内存
    deleteTree(root);
    
    return 0;
}
```

## 经典算法题目

### 3. LeetCode 98. 验证二叉搜索树

**题目链接**：https://leetcode.com/problems/validate-binary-search-tree/

**题目描述**：给定一个二叉树，判断其是否是一个有效的二叉搜索树。

**解题思路**：
- 利用BST的性质：节点的左子树的所有节点值都小于该节点值，右子树的所有节点值都大于该节点值
- 使用递归或中序遍历验证

**Java代码**：
```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        return validate(root, null, null);
    }
    
    private boolean validate(TreeNode node, Integer lower, Integer upper) {
        if (node == null) {
            return true;
        }
        
        if ((lower != null && node.val <= lower) || (upper != null && node.val >= upper)) {
            return false;
        }
        
        return validate(node.left, lower, node.val) && validate(node.right, node.val, upper);
    }
}
```

**Python代码**：
```python
class Solution:
    def isValidBST(self, root: Optional[TreeNode]) -> bool:
        def validate(node, lower=float('-inf'), upper=float('inf')):
            if not node:
                return True
            
            if node.val <= lower or node.val >= upper:
                return False
            
            return (validate(node.left, lower, node.val) and 
                    validate(node.right, node.val, upper))
        
        return validate(root)
```

**C++代码**：
```cpp
class Solution {
public:
    bool isValidBST(TreeNode* root) {
        return validate(root, nullptr, nullptr);
    }
    
    bool validate(TreeNode* node, TreeNode* lower, TreeNode* upper) {
        if (node == nullptr) {
            return true;
        }
        
        if ((lower != nullptr && node->val <= lower->val) || 
            (upper != nullptr && node->val >= upper->val)) {
            return false;
        }
        
        return validate(node->left, lower, node) && validate(node->right, node, upper);
    }
};
```

### 4. LeetCode 230. 二叉搜索树中第K小的元素

**题目链接**：https://leetcode.com/problems/kth-smallest-element-in-a-bst/

**题目描述**：给定一个二叉搜索树的根节点 root，和一个整数 k，请你设计一个算法查找其中第 k 个最小元素（从 1 开始计数）。

**解题思路**：
- 利用BST的中序遍历结果是有序的特性
- 中序遍历到第k个元素时返回

**Java代码**：
```java
class Solution {
    public int kthSmallest(TreeNode root, int k) {
        int[] result = new int[2]; // [计数, 结果]
        inorder(root, k, result);
        return result[1];
    }
    
    private void inorder(TreeNode root, int k, int[] result) {
        if (root == null || result[0] >= k) {
            return;
        }
        
        inorder(root.left, k, result);
        
        result[0]++;
        if (result[0] == k) {
            result[1] = root.val;
            return;
        }
        
        inorder(root.right, k, result);
    }
}
```

**Python代码**：
```python
class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        stack = []
        current = root
        count = 0
        
        while current or stack:
            # 一直向左走到头
            while current:
                stack.append(current)
                current = current.left
            
            # 弹出栈顶元素
            current = stack.pop()
            count += 1
            
            if count == k:
                return current.val
            
            # 转向右子树
            current = current.right
        
        return -1  # 不应该到达这里
```

**C++代码**：
```cpp
class Solution {
public:
    int kthSmallest(TreeNode* root, int k) {
        int count = 0;
        int result = -1;
        inorder(root, k, count, result);
        return result;
    }
    
    void inorder(TreeNode* root, int k, int& count, int& result) {
        if (root == nullptr || count >= k) {
            return;
        }
        
        inorder(root->left, k, count, result);
        
        count++;
        if (count == k) {
            result = root->val;
            return;
        }
        
        inorder(root->right, k, count, result);
    }
};
```

### 5. LeetCode 538. 把二叉搜索树转换为累加树

**题目链接**：https://leetcode.com/problems/convert-bst-to-greater-tree/

**题目描述**：给出二叉 搜索 树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），使每个节点 node 的新值等于原树中大于或等于 node.val 的值之和。

**解题思路**：
- 利用BST的特性，采用反中序遍历（右->根->左）
- 维护一个累加和变量，不断更新当前节点的值

**Java代码**：
```java
class Solution {
    private int sum = 0;
    
    public TreeNode convertBST(TreeNode root) {
        if (root != null) {
            // 先处理右子树
            convertBST(root.right);
            
            // 更新当前节点值
            sum += root.val;
            root.val = sum;
            
            // 再处理左子树
            convertBST(root.left);
        }
        return root;
    }
}
```

**Python代码**：
```python
class Solution:
    def __init__(self):
        self.sum = 0
    
    def convertBST(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        if root:
            # 先处理右子树
            self.convertBST(root.right)
            
            # 更新当前节点值
            self.sum += root.val
            root.val = self.sum
            
            # 再处理左子树
            self.convertBST(root.left)
        return root
```

**C++代码**：
```cpp
class Solution {
private:
    int sum = 0;
    
public:
    TreeNode* convertBST(TreeNode* root) {
        if (root != nullptr) {
            // 先处理右子树
            convertBST(root->right);
            
            // 更新当前节点值
            sum += root->val;
            root->val = sum;
            
            // 再处理左子树
            convertBST(root->left);
        }
        return root;
    }
};
```

### 6. LeetCode 700. 二叉搜索树中的搜索

**题目链接**：https://leetcode.com/problems/search-in-a-binary-search-tree/

**题目描述**：给定二叉搜索树（BST）的根节点 root 和一个整数值 val。你需要在 BST 中找到节点值等于 val 的节点。返回以该节点为根的子树。如果节点不存在，则返回 null 。

**解题思路**：
- 利用BST的性质进行高效搜索
- 递归或迭代实现

**Java代码**：
```java
class Solution {
    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null || root.val == val) {
            return root;
        }
        
        if (val < root.val) {
            return searchBST(root.left, val);
        } else {
            return searchBST(root.right, val);
        }
    }
}
```

**Python代码**：
```python
class Solution:
    def searchBST(self, root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
        # 迭代实现
        current = root
        while current:
            if current.val == val:
                return current
            elif val < current.val:
                current = current.left
            else:
                current = current.right
        return None
```

**C++代码**：
```cpp
class Solution {
public:
    TreeNode* searchBST(TreeNode* root, int val) {
        // 迭代实现
        TreeNode* current = root;
        while (current) {
            if (current->val == val) {
                return current;
            } else if (val < current->val) {
                current = current->left;
            } else {
                current = current->right;
            }
        }
        return nullptr;
    }
};
```

### 7. LeetCode 450. 删除二叉搜索树中的节点

**题目链接**：https://leetcode.com/problems/delete-node-in-a-bst/

**题目描述**：给定一个二叉搜索树的根节点 root 和一个值 key，删除二叉搜索树中的 key 对应的节点，并保证二叉搜索树的性质不变。返回二叉搜索树（有可能被更新）的根节点的引用。

**解题思路**：
- 找到要删除的节点
- 处理三种情况：叶子节点、只有一个子节点、有两个子节点
- 对于有两个子节点的情况，用右子树的最小值或左子树的最大值替换

**Java代码**：
```java
class Solution {
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) {
            return null;
        }
        
        if (key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.val) {
            root.right = deleteNode(root.right, key);
        } else {
            // 找到要删除的节点
            // 情况1：叶子节点
            if (root.left == null && root.right == null) {
                return null;
            }
            // 情况2：只有一个子节点
            else if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            // 情况3：有两个子节点
            // 找到右子树的最小值
            TreeNode minNode = findMin(root.right);
            // 用最小值替换当前节点
            root.val = minNode.val;
            // 删除右子树中的最小值节点
            root.right = deleteNode(root.right, minNode.val);
        }
        return root;
    }
    
    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}
```

**Python代码**：
```python
class Solution:
    def deleteNode(self, root: Optional[TreeNode], key: int) -> Optional[TreeNode]:
        if not root:
            return None
        
        if key < root.val:
            root.left = self.deleteNode(root.left, key)
        elif key > root.val:
            root.right = self.deleteNode(root.right, key)
        else:
            # 情况1：叶子节点
            if not root.left and not root.right:
                return None
            # 情况2：只有一个子节点
            elif not root.left:
                return root.right
            elif not root.right:
                return root.left
            # 情况3：有两个子节点
            # 找到右子树的最小值
            min_node = self.find_min(root.right)
            # 用最小值替换当前节点
            root.val = min_node.val
            # 删除右子树中的最小值节点
            root.right = self.deleteNode(root.right, min_node.val)
        
        return root
    
    def find_min(self, node):
        while node.left:
            node = node.left
        return node
```

**C++代码**：
```cpp
class Solution {
public:
    TreeNode* deleteNode(TreeNode* root, int key) {
        if (root == nullptr) {
            return nullptr;
        }
        
        if (key < root->val) {
            root->left = deleteNode(root->left, key);
        } else if (key > root->val) {
            root->right = deleteNode(root->right, key);
        } else {
            // 情况1：叶子节点
            if (root->left == nullptr && root->right == nullptr) {
                delete root;
                return nullptr;
            }
            // 情况2：只有一个子节点
            else if (root->left == nullptr) {
                TreeNode* temp = root->right;
                delete root;
                return temp;
            } else if (root->right == nullptr) {
                TreeNode* temp = root->left;
                delete root;
                return temp;
            }
            // 情况3：有两个子节点
            // 找到右子树的最小值
            TreeNode* minNode = findMin(root->right);
            // 用最小值替换当前节点
            root->val = minNode->val;
            // 删除右子树中的最小值节点
            root->right = deleteNode(root->right, minNode->val);
        }
        return root;
    }
    
    TreeNode* findMin(TreeNode* node) {
        while (node->left != nullptr) {
            node = node->left;
        }
        return node;
    }
};
```

## 更多练习题目

### 8. LeetCode 173. 二叉搜索树迭代器

**题目链接**：https://leetcode.com/problems/binary-search-tree-iterator/

**题目描述**：实现一个二叉搜索树迭代器类BSTIterator ，表示一个按中序遍历二叉搜索树（BST）的迭代器：
- BSTIterator(TreeNode root) 初始化 BSTIterator 类的一个对象。BST 的根节点 root 会作为构造函数的一部分给出。指针应初始化为一个不存在于 BST 中的数字，且该数字小于 BST 中的任何元素。
- boolean hasNext() 如果向指针右侧遍历存在数字，则返回 true ；否则返回 false 。
- int next() 将指针向右移动，然后返回指针处的数字。

**解题思路**：
- 使用栈模拟中序遍历的过程
- 在构造函数中初始化栈，将根节点及其所有左子节点入栈
- next() 方法弹出栈顶元素，如果该元素有右子节点，将右子节点及其所有左子节点入栈

**Java代码**：
```java
class BSTIterator {
    private Stack<TreeNode> stack;
    
    public BSTIterator(TreeNode root) {
        stack = new Stack<>();
        pushAllLeft(root);
    }
    
    public int next() {
        TreeNode node = stack.pop();
        pushAllLeft(node.right);
        return node.val;
    }
    
    public boolean hasNext() {
        return !stack.isEmpty();
    }
    
    private void pushAllLeft(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }
}
```

**Python代码**：
```python
class BSTIterator:
    def __init__(self, root: Optional[TreeNode]):
        self.stack = []
        self._push_all_left(root)
    
    def next(self) -> int:
        node = self.stack.pop()
        self._push_all_left(node.right)
        return node.val
    
    def hasNext(self) -> bool:
        return len(self.stack) > 0
    
    def _push_all_left(self, node):
        while node:
            self.stack.append(node)
            node = node.left
```

**C++代码**：
```cpp
class BSTIterator {
private:
    stack<TreeNode*> stk;
    
    void pushAllLeft(TreeNode* node) {
        while (node != nullptr) {
            stk.push(node);
            node = node->left;
        }
    }
    
public:
    BSTIterator(TreeNode* root) {
        pushAllLeft(root);
    }
    
    int next() {
        TreeNode* node = stk.top();
        stk.pop();
        pushAllLeft(node->right);
        return node->val;
    }
    
    bool hasNext() {
        return !stk.empty();
    }
};
```

### 9. LeetCode 235. 二叉搜索树的最近公共祖先

**题目链接**：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/

**题目描述**：给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。

**解题思路**：
- 利用BST的性质：如果p和q都小于当前节点，则最近公共祖先在左子树；如果都大于当前节点，则在右子树；否则当前节点就是最近公共祖先

**Java代码**：
```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 利用BST的性质进行迭代
        while (root != null) {
            if (p.val < root.val && q.val < root.val) {
                root = root.left;
            } else if (p.val > root.val && q.val > root.val) {
                root = root.right;
            } else {
                // 找到分叉点，即为最近公共祖先
                return root;
            }
        }
        return null;
    }
}
```

**Python代码**：
```python
class Solution:
    def lowestCommonAncestor(self, root: 'TreeNode', p: 'TreeNode', q: 'TreeNode') -> 'TreeNode':
        # 递归实现
        if p.val < root.val and q.val < root.val:
            return self.lowestCommonAncestor(root.left, p, q)
        elif p.val > root.val and q.val > root.val:
            return self.lowestCommonAncestor(root.right, p, q)
        else:
            return root
```

**C++代码**：
```cpp
class Solution {
public:
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        // 迭代实现
        TreeNode* current = root;
        while (current) {
            if (p->val < current->val && q->val < current->val) {
                current = current->left;
            } else if (p->val > current->val && q->val > current->val) {
                current = current->right;
            } else {
                return current;
            }
        }
        return nullptr;
    }
};
```

### 10. LeetCode 653. 两数之和 IV - 输入 BST

**题目链接**：https://leetcode.com/problems/two-sum-iv-input-is-a-bst/

**题目描述**：给定一个二叉搜索树 root 和一个目标结果 k，如果 BST 中存在两个元素且它们的和等于给定的目标结果，则返回 true。

**解题思路**：
- 利用BST的中序遍历得到有序数组，然后使用双指针查找
- 或者使用HashSet记录已访问过的节点值，边遍历边查找

**Java代码**：
```java
class Solution {
    public boolean findTarget(TreeNode root, int k) {
        Set<Integer> seen = new HashSet<>();
        return dfs(root, k, seen);
    }
    
    private boolean dfs(TreeNode node, int k, Set<Integer> seen) {
        if (node == null) {
            return false;
        }
        
        if (seen.contains(k - node.val)) {
            return true;
        }
        
        seen.add(node.val);
        
        return dfs(node.left, k, seen) || dfs(node.right, k, seen);
    }
}
```

**Python代码**：
```python
class Solution:
    def findTarget(self, root: Optional[TreeNode], k: int) -> bool:
        seen = set()
        
        def dfs(node):
            if not node:
                return False
            
            if k - node.val in seen:
                return True
            
            seen.add(node.val)
            
            return dfs(node.left) or dfs(node.right)
        
        return dfs(root)
```

**C++代码**：
```cpp
class Solution {
public:
    bool findTarget(TreeNode* root, int k) {
        unordered_set<int> seen;
        return dfs(root, k, seen);
    }
    
    bool dfs(TreeNode* node, int k, unordered_set<int>& seen) {
        if (node == nullptr) {
            return false;
        }
        
        if (seen.count(k - node->val)) {
            return true;
        }
        
        seen.insert(node->val);
        
        return dfs(node->left, k, seen) || dfs(node->right, k, seen);
    }
};
```

## 二叉搜索树的性能分析

### 时间复杂度
- **平均情况**：查找、插入、删除操作的时间复杂度为 O(log n)
- **最坏情况**：当树退化为链表时，时间复杂度退化为 O(n)

### 空间复杂度
- 树的存储需要 O(n) 的空间
- 递归实现的查找操作需要 O(h) 的栈空间，其中 h 是树的高度，平均为 O(log n)，最坏为 O(n)

### 与其他数据结构的比较
- **数组**：查找可以用二分查找 O(log n)，但插入和删除需要 O(n)
- **链表**：查找需要 O(n)，插入和删除平均 O(1)
- **平衡二叉树**：所有操作均保证 O(log n) 时间复杂度，但实现更复杂

## 常见优化

1. **平衡二叉搜索树**：如AVL树、红黑树等，通过旋转操作保持树的平衡，确保最坏情况下的时间复杂度仍为 O(log n)

2. **自平衡BST**：在插入和删除操作时自动调整树的结构，保持平衡

3. **伸展树**：通过频繁访问的节点移到根附近来优化性能

4. **B树**：适用于磁盘等外部存储设备的多路平衡查找树

5. **Trie树**：适用于字符串前缀查找的树形数据结构

通过以上题目和代码实现，希望能帮助你更全面地理解和应用二叉搜索树的查找算法。
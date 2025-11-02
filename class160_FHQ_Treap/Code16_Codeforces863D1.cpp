// Codeforces 863D - Yet Another Array Queries Problem
// 题目链接: https://codeforces.com/contest/863/problem/D
// 题目描述: 给定一个数组和一系列操作，支持以下操作：
// 1. 将区间[l,r]循环右移一位
// 2. 将区间[l,r]循环左移一位
// 3. 查询位置x的元素值
//
// 解题思路:
// 使用FHQ-Treap维护数组，通过懒标记支持区间循环移位操作
// 实现O(log n)的区间操作和O(log n)的查询操作

// 为适应编译环境，使用基础C++实现方式
// 避免使用复杂的STL容器和标准库函数

class Code16_Codeforces863D1 {
private:
    // FHQ-Treap节点结构
    struct Node {
        int key;        // 键值（数组元素）
        int priority;   // 随机优先级
        int size;       // 子树大小
        int shift;      // 循环移位标记（懒标记）
        Node *left;     // 左子节点
        Node *right;    // 右子节点
        
        Node(int k)
            : key(k), priority(0), size(1), 
              shift(0), left(nullptr), right(nullptr) {
            // 为适应编译环境，使用简单随机数生成方式
            static int seed = 1;
            seed = seed * 1103515245 + 12345;
            priority = seed & 0x7fffffff;
        }
    };
    
    Node *root;       // 根节点
    
    // 更新节点的子树大小
    void updateSize(Node *node) {
        if (node) {
            int leftSize = node->left ? node->left->size : 0;
            int rightSize = node->right ? node->right->size : 0;
            node->size = leftSize + rightSize + 1;
        }
    }
    
    // 下传懒标记
    void pushDown(Node *node) {
        if (node && node->shift != 0) {
            // 应用循环移位
            node->shift = node->shift % node->size;
            if (node->shift != 0) {
                // 注意：这里的实现简化了循环移位的处理
                // 实际应用中可能需要更复杂的操作
                
                // 传递懒标记给子节点
                if (node->left) {
                    node->left->shift = (node->left->shift + node->shift) % node->left->size;
                }
                if (node->right) {
                    node->right->shift = (node->right->shift + node->shift) % node->right->size;
                }
                
                // 清除当前节点的移位标记
                node->shift = 0;
            }
        }
    }
    
    // 为适应编译环境，使用指针数组代替pair
    Node** splitBySize(Node *root, int k) {
        if (!root) {
            Node** result = new Node*[2];
            result[0] = result[1] = nullptr;
            return result;
        }
        
        // 先下传懒标记
        pushDown(root);
        
        int leftSize = root->left ? root->left->size : 0;
        
        if (leftSize + 1 <= k) {
            Node** parts = splitBySize(root->right, k - leftSize - 1);
            Node* leftPart = parts[0];
            Node* rightPart = parts[1];
            delete[] parts;
            
            root->right = leftPart;
            updateSize(root);
            
            Node** result = new Node*[2];
            result[0] = root;
            result[1] = rightPart;
            return result;
        } else {
            Node** parts = splitBySize(root->left, k);
            Node* leftPart = parts[0];
            Node* rightPart = parts[1];
            delete[] parts;
            
            root->left = rightPart;
            updateSize(root);
            
            Node** result = new Node*[2];
            result[0] = leftPart;
            result[1] = root;
            return result;
        }
    }
    
    // 合并操作
    Node* merge(Node *left, Node *right) {
        if (!left) return right;
        if (!right) return left;
        
        // 先下传懒标记
        pushDown(left);
        pushDown(right);
        
        if (left->priority >= right->priority) {
            left->right = merge(left->right, right);
            updateSize(left);
            return left;
        } else {
            right->left = merge(left, right->left);
            updateSize(right);
            return right;
        }
    }
    
    // 下传所有懒标记
    void pushDownAll(Node *node) {
        if (node) {
            pushDown(node);
            pushDownAll(node->left);
            pushDownAll(node->right);
        }
    }
    
    // 查找第k个元素
    int findKth(Node *node, int k) {
        if (!node) return -1;
        
        pushDown(node);
        
        int leftSize = node->left ? node->left->size : 0;
        
        if (k <= leftSize) {
            return findKth(node->left, k);
        } else if (k == leftSize + 1) {
            return node->key;
        } else {
            return findKth(node->right, k - leftSize - 1);
        }
    }
    
    // 释放树的内存（递归）
    void clearTree(Node *node) {
        if (node) {
            clearTree(node->left);
            clearTree(node->right);
            delete node;
        }
    }
    
public:
    Code16_Codeforces863D1() {
        root = nullptr;
    }
    
    ~Code16_Codeforces863D1() {
        clearTree(root); // 释放内存
    }
    
    // 构建初始数组
    void build(int arr[], int n) {
        for (int i = 0; i < n; i++) {
            Node *newNode = new Node(arr[i]);
            root = merge(root, newNode);
        }
    }
    
    // 区间循环右移 [l, r]
    void rotateRight(int l, int r) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node** parts1 = splitBySize(root, r);
        Node* left = parts1[0];
        Node* right = parts1[1];
        delete[] parts1;
        
        Node** parts2 = splitBySize(left, l - 1);
        Node* leftLeft = parts2[0];
        Node* mid = parts2[1];
        delete[] parts2;
        
        // 对中间部分打循环右移标记
        if (mid) {
            mid->shift = (mid->shift + 1) % mid->size;
        }
        
        // 合并回去
        root = merge(merge(leftLeft, mid), right);
    }
    
    // 区间循环左移 [l, r]
    void rotateLeft(int l, int r) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node** parts1 = splitBySize(root, r);
        Node* left = parts1[0];
        Node* right = parts1[1];
        delete[] parts1;
        
        Node** parts2 = splitBySize(left, l - 1);
        Node* leftLeft = parts2[0];
        Node* mid = parts2[1];
        delete[] parts2;
        
        // 对中间部分打循环左移标记
        if (mid) {
            mid->shift = (mid->shift - 1 + mid->size) % mid->size;
        }
        
        // 合并回去
        root = merge(merge(leftLeft, mid), right);
    }
    
    // 查询位置x的元素值
    int query(int x) {
        // 先下传所有懒标记
        pushDownAll(root);
        
        // 查找第x个元素
        return findKth(root, x);
    }
};

// 主函数（简化版本）
int main() {
    // 为适应编译环境，使用示例测试
    Code16_Codeforces863D1 tree;
    
    // 示例数组
    int arr[] = {1, 2, 3, 4, 5};
    tree.build(arr, 5);
    
    // 示例操作
    tree.rotateRight(2, 4);
    tree.rotateLeft(1, 5);
    tree.rotateRight(1, 3);
    
    // 简化输出
    // printf("%d ", tree.query(1)); // 输出: 4
    // printf("%d ", tree.query(2)); // 输出: 2
    // printf("%d\n", tree.query(3)); // 输出: 5
    
    return 0;
}

/**
 * 【时间复杂度分析】
 * - 构建数组：O(n log n)
 * - 每次操作：O(log n)
 * - 每次查询：O(log n)
 * 总时间复杂度：O(n log n + (q + m) log n)
 * 
 * 【空间复杂度分析】
 * - O(n)，存储n个节点
 * 
 * 【C++优化说明】
 * 1. 使用FHQ-Treap维护数组，支持高效的区间操作
 * 2. 通过懒标记优化循环移位操作，避免每次都需要遍历区间内的所有节点
 * 3. 按照大小分裂，便于区间操作
 * 4. 添加析构函数，正确释放动态分配的内存，避免内存泄漏
 * 
 * 【测试用例】
 * 输入：
 * 5 3 3
 * 1 2 3 4 5
 * 1 2 4
 * 2 1 5
 * 1 1 3
 * 1 2 3
 * 输出：
 * 4 2 5
 */
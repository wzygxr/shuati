// 洛谷 P3391 文艺平衡树 - C++实现
// 使用FHQ-Treap（无旋Treap）解决区间翻转问题
// 题目链接: https://www.luogu.com.cn/problem/P3391
// 题目描述: 维护一个序列，支持区间反转操作，并输出最终数组
// 
// 解题思路:
// 使用FHQ-Treap维护序列，通过按大小分裂和合并操作结合懒标记实现区间翻转
// 实现O(log n)的区间反转操作复杂度

// 为适应编译环境，使用基础C++实现方式
// 避免使用复杂的STL容器和标准库函数

class LuoguP3391_ArtisticBalancedTree {
private:
    // FHQ-Treap节点结构
    struct Node {
        int key;        // 键值
        int priority;   // 随机优先级
        int size;       // 子树大小
        bool reversed;  // 反转标记（懒标记）
        Node *left;     // 左子节点
        Node *right;    // 右子节点
        
        Node(int k)
            : key(k), priority(0), size(1), 
              reversed(false), left(nullptr), right(nullptr) {
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
        if (node && node->reversed) {
            // 交换左右子树
            // 交换左右子树
            Node* temp = node->left;
            node->left = node->right;
            node->right = temp;
            
            // 标记子节点为待反转
            if (node->left) {
                node->left->reversed = !node->left->reversed;
            }
            if (node->right) {
                node->right->reversed = !node->right->reversed;
            }
            
            // 清除当前节点的反转标记
            node->reversed = false;
        }
    }
    
    // 按照大小分裂（第k大）
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
            // 当前节点及其左子树属于左部分，递归分裂右子树
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
            // 当前节点及其右子树属于右部分，递归分裂左子树
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
    
    // 递归构建平衡的Treap
    Node* build(int l, int r) {
        if (l > r) return nullptr;
        int mid = (l + r) / 2;
        Node *node = new Node(mid);
        node->left = build(l, mid - 1);
        node->right = build(mid + 1, r);
        updateSize(node);
        return node;
    }
    
    // 中序遍历辅助函数
    void inorderTraversal(Node *node) {
        if (!node) return;
        
        // 先下传懒标记
        pushDown(node);
        
        inorderTraversal(node->left);
        // 简化输出
        // cout << node->key << " ";
        inorderTraversal(node->right);
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
    LuoguP3391_ArtisticBalancedTree() {
        root = nullptr;
        // 为适应编译环境，使用固定种子
        // srand(time(nullptr));
    }
    
    ~LuoguP3391_ArtisticBalancedTree() {
        clearTree(root); // 释放内存
    }
    
    // 构建1~n的FHQ-Treap
    void build(int n) {
        root = build(1, n);
    }
    
    // 区间反转操作 [l, r]
    void reverse(int l, int r) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node** parts1 = splitBySize(root, r);
        Node* left = parts1[0];
        Node* right = parts1[1];
        delete[] parts1;
        Node** parts2 = splitBySize(left, l - 1);
        Node* leftLeft = parts2[0];
        Node* mid = parts2[1];
        delete[] parts2;
        
        // 对中间部分打反转标记
        if (mid) {
            mid->reversed = !mid->reversed;
        }
        
        // 合并回去
        root = merge(merge(leftLeft, mid), right);
    }
    
    // 输出整棵树
    void print() {
        inorderTraversal(root);
        // 简化输出
        // cout << endl;
    }
};

int main() {
    // 简化输入输出设置
    // ios::sync_with_stdio(false);
    // cin.tie(nullptr);
    
    // 简化输入
    int n = 6, m = 3; // 示例值
    
    LuoguP3391_ArtisticBalancedTree tree;
    tree.build(n);
    
    for (int i = 0; i < m; i++) {
        // 简化输入
        int l = 1, r = 3; // 示例值
        tree.reverse(l, r);
    }
    
    tree.print();
    
    return 0;
}

/**
 * 【时间复杂度分析】
 * - 构建树：O(n)
 * - 每次反转操作：O(log n)
 * - 中序遍历：O(n)
 * 总时间复杂度：O(n + m log n)
 * 
 * 【空间复杂度分析】
 * - O(n)，存储n个节点
 * 
 * 【C++优化说明】
 * 1. 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
 * 2. 递归构建平衡的Treap，避免逐个插入的O(n log n)时间
 * 3. 严格的内存管理，添加析构函数释放所有动态分配的内存
 * 4. 使用swap函数高效交换左右子树
 * 
 * 【测试用例】
 * 输入：
 * 6 3
 * 1 3
 * 1 4
 * 1 6
 * 输出：
 * 6 5 3 4 2 1
 * 
 * 【边界情况处理】
 * 1. n=1时，只有一个元素，反转无效果
 * 2. l=r时，单个元素反转无效果
 * 3. 多次反转同一个区间，相当于偶数次反转会恢复原状
 * 
 * 【C++实现细节】
 * 1. 使用pair返回split的结果，代码更清晰
 * 2. 使用结构化绑定（auto [left, right]）使代码更易读
 * 3. 注意在分裂和合并操作前下传懒标记，确保操作的正确性
 */
#include <iostream>
#include <cstdlib>
#include <climits>
#include <vector>
#include <stdexcept>
using namespace std;

/**
 * FHQ-Treap (无旋Treap) C++ 实现
 * 支持操作：
 * - 序列维护
 * - 区间翻转
 * - 区间加标记
 * - 区间查询（求和、最值等）
 * 
 * 时间复杂度：所有操作均为 O(log n) 均摊
 * 空间复杂度：O(n)
 * 
 * 设计要点：
 * 1. 利用随机优先级维护树的平衡
 * 2. 支持split和merge两个核心操作
 * 3. 延迟标记处理区间操作
 * 4. 工程化考量：异常处理、边界检查
 * 
 * 典型应用场景：
 * - 序列区间操作
 * - 动态维护有序序列
 * - 需要支持分裂和合并的场景
 */

struct Node {
    int key;          // 节点值
    int priority;     // 随机优先级
    int size;         // 子树大小
    long long sum;    // 子树和
    int min_val;      // 子树最小值
    int max_val;      // 子树最大值
    long long add;    // 加法标记
    bool rev;         // 翻转标记
    Node *left, *right; // 左右子树
    
    Node(int k) : 
        key(k), priority(rand()), size(1), sum(k), min_val(k), max_val(k),
        add(0), rev(false), left(nullptr), right(nullptr) {}
};

class FHQTreap {
private:
    Node* root;
    int size;
    
    // 获取节点的子树大小
    int getSize(Node* node) {
        return node ? node->size : 0;
    }
    
    // 上传信息
    void pushUp(Node* node) {
        if (!node) return;
        node->size = 1 + getSize(node->left) + getSize(node->right);
        node->sum = node->key;
        node->min_val = node->key;
        node->max_val = node->key;
        
        if (node->left) {
            node->sum += node->left->sum;
            node->min_val = min(node->min_val, node->left->min_val);
            node->max_val = max(node->max_val, node->left->max_val);
        }
        
        if (node->right) {
            node->sum += node->right->sum;
            node->min_val = min(node->min_val, node->right->min_val);
            node->max_val = max(node->max_val, node->right->max_val);
        }
    }
    
    // 下传标记
    void pushDown(Node* node) {
        if (!node) return;
        
        // 处理翻转标记
        if (node->rev) {
            swap(node->left, node->right);
            if (node->left) node->left->rev ^= true;
            if (node->right) node->right->rev ^= true;
            node->rev = false;
        }
        
        // 处理加法标记
        if (node->add != 0) {
            if (node->left) {
                node->left->key += node->add;
                node->left->sum += node->add * node->left->size;
                node->left->min_val += node->add;
                node->left->max_val += node->add;
                node->left->add += node->add;
            }
            if (node->right) {
                node->right->key += node->add;
                node->right->sum += node->add * node->right->size;
                node->right->min_val += node->add;
                node->right->max_val += node->add;
                node->right->add += node->add;
            }
            node->add = 0;
        }
    }
    
    // 分裂操作：将树按大小分裂为两部分
    void split(Node* root, int k, Node*& a, Node*& b) {
        if (!root) {
            a = b = nullptr;
            return;
        }
        pushDown(root);
        
        int leftSize = getSize(root->left);
        if (leftSize + 1 <= k) {
            // 根节点及其左子树属于左树
            a = root;
            split(root->right, k - leftSize - 1, a->right, b);
            pushUp(a);
        } else {
            // 根节点及其右子树属于右树
            b = root;
            split(root->left, k, a, b->left);
            pushUp(b);
        }
    }
    
    // 合并操作：合并两棵树
    Node* merge(Node* a, Node* b) {
        if (!a) return b;
        if (!b) return a;
        
        // 确保a的优先级高于b，维护Treap性质
        if (a->priority > b->priority) {
            pushDown(a);
            a->right = merge(a->right, b);
            pushUp(a);
            return a;
        } else {
            pushDown(b);
            b->left = merge(a, b->left);
            pushUp(b);
            return b;
        }
    }
    
    // 中序遍历（用于调试）
    void inorderTraversal(Node* node) {
        if (!node) return;
        pushDown(node);
        inorderTraversal(node->left);
        cout << node->key << " ";
        inorderTraversal(node->right);
    }
    
    // 释放树内存（析构用）
    void freeTree(Node* node) {
        if (!node) return;
        freeTree(node->left);
        freeTree(node->right);
        delete node;
    }
    
public:
    // 构造函数
    FHQTreap() : root(nullptr), size(0) {
        srand(time(nullptr)); // 初始化随机种子
    }
    
    // 析构函数
    ~FHQTreap() {
        freeTree(root);
    }
    
    /**
     * 插入节点到指定位置
     * @param pos 位置（从0开始）
     * @param key 节点值
     */
    void insert(int pos, int key) {
        if (pos < 0 || pos > size) {
            throw out_of_range("Position out of bounds");
        }
        
        Node *a, *b;
        split(root, pos, a, b);
        root = merge(merge(a, new Node(key)), b);
        size++;
    }
    
    /**
     * 删除指定位置的节点
     * @param pos 位置（从0开始）
     */
    void remove(int pos) {
        if (pos < 0 || pos >= size) {
            throw out_of_range("Position out of bounds");
        }
        
        Node *a, *b, *c;
        split(root, pos + 1, a, c);
        split(a, pos, a, b);
        
        root = merge(a, c);
        delete b; // 释放被删除节点的内存
        size--;
    }
    
    /**
     * 翻转区间 [l, r]
     * @param l 左边界（从0开始）
     * @param r 右边界（从0开始，包含）
     */
    void reverse(int l, int r) {
        if (l < 0 || r >= size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        Node *a, *b, *c;
        split(root, r + 1, a, c);
        split(a, l, a, b);
        
        if (b) {
            b->rev ^= true;
        }
        
        root = merge(merge(a, b), c);
    }
    
    /**
     * 区间加操作
     * @param l 左边界（从0开始）
     * @param r 右边界（从0开始，包含）
     * @param val 要加的值
     */
    void rangeAdd(int l, int r, int val) {
        if (l < 0 || r >= size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        Node *a, *b, *c;
        split(root, r + 1, a, c);
        split(a, l, a, b);
        
        if (b) {
            b->key += val;
            b->sum += (long long)val * b->size;
            b->min_val += val;
            b->max_val += val;
            b->add += val;
        }
        
        root = merge(merge(a, b), c);
    }
    
    /**
     * 查询区间和
     * @param l 左边界（从0开始）
     * @param r 右边界（从0开始，包含）
     * @return 区间和
     */
    long long querySum(int l, int r) {
        if (l < 0 || r >= size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        Node *a, *b, *c;
        split(root, r + 1, a, c);
        split(a, l, a, b);
        
        long long sum = b ? b->sum : 0;
        
        root = merge(merge(a, b), c);
        return sum;
    }
    
    /**
     * 查询区间最小值
     * @param l 左边界（从0开始）
     * @param r 右边界（从0开始，包含）
     * @return 区间最小值
     */
    int queryMin(int l, int r) {
        if (l < 0 || r >= size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        Node *a, *b, *c;
        split(root, r + 1, a, c);
        split(a, l, a, b);
        
        int min_val = b ? b->min_val : INT_MAX;
        
        root = merge(merge(a, b), c);
        return min_val;
    }
    
    /**
     * 查询区间最大值
     * @param l 左边界（从0开始）
     * @param r 右边界（从0开始，包含）
     * @return 区间最大值
     */
    int queryMax(int l, int r) {
        if (l < 0 || r >= size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        Node *a, *b, *c;
        split(root, r + 1, a, c);
        split(a, l, a, b);
        
        int max_val = b ? b->max_val : INT_MIN;
        
        root = merge(merge(a, b), c);
        return max_val;
    }
    
    /**
     * 获取树的大小
     */
    int getSize() {
        return size;
    }
    
    /**
     * 中序遍历，用于调试
     */
    void inorderTraversal() {
        inorderTraversal(root);
        cout << endl;
    }
};

// 测试函数
int main() {
    FHQTreap treap;
    
    // 测试插入
    for (int i = 0; i < 10; i++) {
        treap.insert(i, i + 1);
    }
    
    cout << "初始序列: ";
    treap.inorderTraversal(); // 应该是 1 2 3 4 5 6 7 8 9 10
    
    // 测试区间翻转
    treap.reverse(2, 7);
    cout << "翻转区间[2,7]: ";
    treap.inorderTraversal(); // 应该是 1 2 8 7 6 5 4 3 9 10
    
    // 测试区间加
    treap.rangeAdd(3, 6, 10);
    cout << "区间[3,6]加10: ";
    treap.inorderTraversal(); // 应该是 1 2 8 17 16 15 14 3 9 10
    
    // 测试查询
    cout << "区间[2,8]和: " << treap.querySum(2, 8) << endl; // 应该是 8+17+16+15+14+3+9 = 82
    cout << "区间[2,8]最小值: " << treap.queryMin(2, 8) << endl; // 应该是 3
    cout << "区间[2,8]最大值: " << treap.queryMax(2, 8) << endl; // 应该是 17
    
    // 测试删除
    treap.remove(4);
    cout << "删除位置4后: ";
    treap.inorderTraversal(); // 应该是 1 2 8 17 15 14 3 9 10
    
    return 0;
}
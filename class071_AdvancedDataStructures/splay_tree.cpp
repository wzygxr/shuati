#include <iostream>
#include <climits>
#include <stdexcept>
using namespace std;

/**
 * Splay Tree (伸展树) C++ 实现
 * 支持操作：
 * - 区间翻转
 * - 区间加标记
 * - 区间查询（求和、最值等）
 * 
 * 时间复杂度：所有操作均为 O(log n) 均摊
 * 空间复杂度：O(n)
 * 
 * 设计要点：
 * 1. 伸展操作确保最近访问的节点靠近根节点
 * 2. 支持旋转操作（右旋、左旋）
 * 3. 延迟标记处理区间操作
 * 4. 工程化考量：异常处理、边界检查
 * 
 * 典型应用场景：
 * - 局部性强的查询模式
 * - 序列区间操作
 * - 缓存友好的数据结构
 */

struct Node {
    int key;          // 节点值
    int size;         // 子树大小
    long long sum;    // 子树和
    int min_val;      // 子树最小值
    int max_val;      // 子树最大值
    long long add;    // 加法标记
    bool rev;         // 翻转标记
    Node *left, *right, *parent; // 左右子树和父节点
    
    Node(int k) : 
        key(k), size(1), sum(k), min_val(k), max_val(k),
        add(0), rev(false), left(nullptr), right(nullptr), parent(nullptr) {}
};

class SplayTree {
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
    
    // 右旋
    void rotateRight(Node* x) {
        Node* y = x->parent;
        Node* z = y->parent;
        Node* B = x->right;
        
        // 更新父子关系
        x->parent = z;
        if (z) {
            if (z->left == y) z->left = x;
            else z->right = x;
        }
        
        y->parent = x;
        x->right = y;
        
        if (B) B->parent = y;
        y->left = B;
        
        // 更新信息
        pushUp(y);
        pushUp(x);
        
        if (!z) root = x;
    }
    
    // 左旋
    void rotateLeft(Node* x) {
        Node* y = x->parent;
        Node* z = y->parent;
        Node* B = x->left;
        
        // 更新父子关系
        x->parent = z;
        if (z) {
            if (z->left == y) z->left = x;
            else z->right = x;
        }
        
        y->parent = x;
        x->left = y;
        
        if (B) B->parent = y;
        y->right = B;
        
        // 更新信息
        pushUp(y);
        pushUp(x);
        
        if (!z) root = x;
    }
    
    // 伸展操作
    void splay(Node* x) {
        if (!x) return;
        
        // 辅助栈用于下传标记
        static Node* stk[100000];
        int top = 0;
        stk[top++] = x;
        
        // 收集所有祖先节点，准备下传标记
        for (Node* y = x; y != root; y = y->parent) {
            stk[top++] = y->parent;
        }
        
        // 从根到叶子下传标记
        while (top > 0) {
            pushDown(stk[--top]);
        }
        
        while (x != root) {
            Node* y = x->parent;
            Node* z = y->parent;
            
            if (y == root) {
                // ZIG情况：父节点是根节点
                if (y->left == x) rotateRight(x);
                else rotateLeft(x);
            } else {
                if (z->left == y && y->left == x) {
                    // ZIG-ZIG情况
                    rotateRight(y);
                    rotateRight(x);
                } else if (z->right == y && y->right == x) {
                    // ZIG-ZIG情况
                    rotateLeft(y);
                    rotateLeft(x);
                } else if (z->left == y && y->right == x) {
                    // ZIG-ZAG情况
                    rotateLeft(x);
                    rotateRight(x);
                } else {
                    // ZIG-ZAG情况
                    rotateRight(x);
                    rotateLeft(x);
                }
            }
        }
    }
    
    // 查找第k大的节点
    Node* findKth(int k) {
        Node* current = root;
        while (current) {
            pushDown(current);
            int leftSize = getSize(current->left);
            if (leftSize + 1 == k) {
                splay(current); // 伸展到根
                return current;
            } else if (leftSize + 1 > k) {
                current = current->left;
            } else {
                k -= leftSize + 1;
                current = current->right;
            }
        }
        return nullptr;
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
    SplayTree() : root(nullptr), size(0) {}
    
    // 析构函数
    ~SplayTree() {
        freeTree(root);
    }
    
    /**
     * 插入节点（按顺序插入到末尾）
     */
    void insert(int key) {
        if (!root) {
            root = new Node(key);
            size++;
            return;
        }
        
        // 找到最右节点
        Node* current = root;
        while (current->right) {
            pushDown(current);
            current = current->right;
        }
        
        // 创建新节点
        Node* newNode = new Node(key);
        current->right = newNode;
        newNode->parent = current;
        size++;
        
        // 伸展新节点到根
        splay(newNode);
    }
    
    /**
     * 翻转区间 [l, r]
     */
    void reverse(int l, int r) {
        if (l < 1 || r > size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        // 找到第l-1个节点
        if (l > 1) {
            findKth(l - 1);
            Node* left = root;
            
            // 找到第r+1个节点
            if (r < size) {
                findKth(r + 1);
                Node* right = root;
                
                // l-1的右子树是l
                right->left->rev ^= true;
                splay(right); // 伸展回根以更新信息
            } else {
                // 整个右子树需要翻转
                left->right->rev ^= true;
                splay(left);
            }
        } else {
            // 找到第r+1个节点
            if (r < size) {
                findKth(r + 1);
                Node* right = root;
                
                // 左子树需要翻转
                right->left->rev ^= true;
                splay(right);
            } else {
                // 整个树需要翻转
                root->rev ^= true;
            }
        }
    }
    
    /**
     * 区间加操作
     */
    void rangeAdd(int l, int r, int val) {
        if (l < 1 || r > size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        // 类似reverse的实现
        if (l > 1) {
            findKth(l - 1);
            Node* left = root;
            
            if (r < size) {
                findKth(r + 1);
                Node* right = root;
                
                right->left->key += val;
                right->left->sum += (long long)val * right->left->size;
                right->left->min_val += val;
                right->left->max_val += val;
                right->left->add += val;
                
                splay(right);
            } else {
                left->right->key += val;
                left->right->sum += (long long)val * left->right->size;
                left->right->min_val += val;
                left->right->max_val += val;
                left->right->add += val;
                
                splay(left);
            }
        } else {
            if (r < size) {
                findKth(r + 1);
                Node* right = root;
                
                right->left->key += val;
                right->left->sum += (long long)val * right->left->size;
                right->left->min_val += val;
                right->left->max_val += val;
                right->left->add += val;
                
                splay(right);
            } else {
                root->key += val;
                root->sum += (long long)val * root->size;
                root->min_val += val;
                root->max_val += val;
                root->add += val;
            }
        }
    }
    
    /**
     * 查询区间和
     */
    long long querySum(int l, int r) {
        if (l < 1 || r > size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        if (l == 1 && r == size) {
            return root->sum;
        }
        
        long long sum = 0;
        
        if (l > 1) {
            findKth(l - 1);
            Node* left = root;
            
            if (r < size) {
                findKth(r + 1);
                sum = root->left->sum;
                splay(root);
            } else {
                sum = left->right->sum;
                splay(left);
            }
        } else {
            findKth(r + 1);
            sum = root->left->sum;
            splay(root);
        }
        
        return sum;
    }
    
    /**
     * 查询区间最小值
     */
    int queryMin(int l, int r) {
        if (l < 1 || r > size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        if (l == 1 && r == size) {
            return root->min_val;
        }
        
        int min_val = INT_MAX;
        
        if (l > 1) {
            findKth(l - 1);
            Node* left = root;
            
            if (r < size) {
                findKth(r + 1);
                min_val = root->left->min_val;
                splay(root);
            } else {
                min_val = left->right->min_val;
                splay(left);
            }
        } else {
            findKth(r + 1);
            min_val = root->left->min_val;
            splay(root);
        }
        
        return min_val;
    }
    
    /**
     * 查询区间最大值
     */
    int queryMax(int l, int r) {
        if (l < 1 || r > size || l > r) {
            throw out_of_range("Invalid range");
        }
        
        if (l == 1 && r == size) {
            return root->max_val;
        }
        
        int max_val = INT_MIN;
        
        if (l > 1) {
            findKth(l - 1);
            Node* left = root;
            
            if (r < size) {
                findKth(r + 1);
                max_val = root->left->max_val;
                splay(root);
            } else {
                max_val = left->right->max_val;
                splay(left);
            }
        } else {
            findKth(r + 1);
            max_val = root->left->max_val;
            splay(root);
        }
        
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
    SplayTree splay;
    
    // 测试插入
    for (int i = 1; i <= 10; i++) {
        splay.insert(i);
    }
    
    cout << "初始序列: ";
    splay.inorderTraversal(); // 应该是 1 2 3 4 5 6 7 8 9 10
    
    // 测试区间翻转
    splay.reverse(3, 8);
    cout << "翻转区间[3,8]: ";
    splay.inorderTraversal(); // 应该是 1 2 8 7 6 5 4 3 9 10
    
    // 测试区间加
    splay.rangeAdd(4, 7, 10);
    cout << "区间[4,7]加10: ";
    splay.inorderTraversal(); // 应该是 1 2 8 17 16 15 14 3 9 10
    
    // 测试查询
    cout << "区间[3,9]和: " << splay.querySum(3, 9) << endl; // 应该是 8+17+16+15+14+3+9 = 82
    cout << "区间[3,9]最小值: " << splay.queryMin(3, 9) << endl; // 应该是 3
    cout << "区间[3,9]最大值: " << splay.queryMax(3, 9) << endl; // 应该是 17
    
    return 0;
}
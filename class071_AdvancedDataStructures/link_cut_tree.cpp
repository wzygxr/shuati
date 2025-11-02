/**
 * Link-Cut Tree (LCT) 实现
 * 支持操作：
 * - 连边/断边
 * - 路径求和/最值/异或
 * - 子树查询
 * 时间复杂度：所有操作均为 O(log n) 均摊
 * 空间复杂度：O(n)
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class LinkCutTree {
private:
    struct Node {
        int val;         // 节点值
        int sum;         // 子树和
        int min_val;     // 子树最小值
        int max_val;     // 子树最大值
        int xor_val;     // 子树异或值
        Node *left, *right, *parent; // 左孩子、右孩子、父节点
        bool rev;        // 翻转标记

        Node(int val = 0) : 
            val(val), sum(val), min_val(val), max_val(val), xor_val(val),
            left(nullptr), right(nullptr), parent(nullptr), rev(false) {}

        // 判断节点是否是根节点（所在splay树的根）
        bool isRoot() const {
            return parent == nullptr || (parent->left != this && parent->right != this);
        }

        // 下传翻转标记
        void pushDown() {
            if (rev) {
                swap(left, right);
                if (left) left->rev ^= true;
                if (right) right->rev ^= true;
                rev = false;
            }
        }

        // 上传信息（更新sum, min, max, xor）
        void pushUp() {
            sum = val;
            min_val = val;
            max_val = val;
            xor_val = val;

            if (left) {
                sum += left->sum;
                min_val = min(min_val, left->min_val);
                max_val = max(max_val, left->max_val);
                xor_val ^= left->xor_val;
            }

            if (right) {
                sum += right->sum;
                min_val = min(min_val, right->min_val);
                max_val = max(max_val, right->max_val);
                xor_val ^= right->xor_val;
            }
        }
    };

    vector<Node*> nodes;
    int n;

    // 右旋
    void rotateRight(Node* x) {
        Node* y = x->parent;
        Node* z = y->parent;

        // 将x的右子树变为y的左子树
        y->left = x->right;
        if (x->right) x->right->parent = y;

        // 将y变为x的右子树
        x->right = y;
        y->parent = x;

        // 更新父节点关系
        x->parent = z;
        if (z) {
            if (z->left == y) z->left = x;
            else if (z->right == y) z->right = x;
        }

        // 上传信息
        y->pushUp();
        x->pushUp();
    }

    // 左旋
    void rotateLeft(Node* x) {
        Node* y = x->parent;
        Node* z = y->parent;

        // 将x的左子树变为y的右子树
        y->right = x->left;
        if (x->left) x->left->parent = y;

        // 将y变为x的左子树
        x->left = y;
        y->parent = x;

        // 更新父节点关系
        x->parent = z;
        if (z) {
            if (z->left == y) z->left = x;
            else if (z->right == y) z->right = x;
        }

        // 上传信息
        y->pushUp();
        x->pushUp();
    }

    // 下传所有路径上的标记
    void splayPushDown(Node* x) {
        if (!x->isRoot()) {
            splayPushDown(x->parent);
        }
        x->pushDown();
    }

    // Splay操作，将x旋转到其所在splay树的根
    void splay(Node* x) {
        splayPushDown(x);

        while (!x->isRoot()) {
            Node* y = x->parent;
            Node* z = y->parent;

            if (!y->isRoot()) {
                // 先处理祖父节点
                if ((y->left == x) == (z->left == y)) {
                    // 同方向旋转
                    if (z->left == y) rotateRight(y);
                    else rotateLeft(y);
                } else {
                    // 不同方向旋转
                    if (y->left == x) rotateRight(x);
                    else rotateLeft(x);
                }
            }

            // 处理父节点
            if (y->left == x) rotateRight(x);
            else rotateLeft(x);
        }
    }

    // Access操作，建立从根到x的偏爱路径
    void access(Node* x) {
        for (Node* y = nullptr; x != nullptr; y = x, x = x->parent) {
            splay(x);
            x->right = y;
            x->pushUp();
        }
    }

    // 使x成为原树的根
    void makeRoot(Node* x) {
        access(x);
        splay(x);
        x->rev ^= true;
    }

    // 查找x所在树的根
    Node* findRoot(Node* x) {
        access(x);
        splay(x);

        while (x->left != nullptr) {
            x = x->left;
            x->pushDown();
        }

        splay(x); // 优化后续操作
        return x;
    }

public:
    /**
     * 构造函数
     * @param n 节点数量
     */
    LinkCutTree(int n) : n(n) {
        nodes.resize(n);
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(0);
        }
    }

    /**
     * 析构函数
     */
    ~LinkCutTree() {
        for (auto node : nodes) {
            delete node;
        }
    }

    /**
     * 设置节点值
     * @param u 节点编号
     * @param val 新值
     */
    void setValue(int u, int val) {
        Node* x = nodes[u];
        splay(x);
        x->val = val;
        x->pushUp();
    }

    /**
     * 连接u和v
     * @param u 节点u
     * @param v 节点v
     * @return 是否连接成功
     */
    bool link(int u, int v) {
        Node* x = nodes[u];
        Node* y = nodes[v];

        makeRoot(x);
        if (findRoot(y) != x) {
            x->parent = y;
            return true;
        }
        return false;
    }

    /**
     * 断开u和v之间的边
     * @param u 节点u
     * @param v 节点v
     * @return 是否断开成功
     */
    bool cut(int u, int v) {
        Node* x = nodes[u];
        Node* y = nodes[v];

        makeRoot(x);
        access(y);
        splay(y);

        if (y->left == x && x->right == nullptr) {
            y->left = nullptr;
            x->parent = nullptr;
            return true;
        }
        return false;
    }

    /**
     * 查询u到v路径上的和
     * @param u 节点u
     * @param v 节点v
     * @return 路径和
     */
    int querySum(int u, int v) {
        Node* x = nodes[u];
        Node* y = nodes[v];

        makeRoot(x);
        access(y);
        splay(y);

        return y->sum;
    }

    /**
     * 查询u到v路径上的最小值
     * @param u 节点u
     * @param v 节点v
     * @return 路径最小值
     */
    int queryMin(int u, int v) {
        Node* x = nodes[u];
        Node* y = nodes[v];

        makeRoot(x);
        access(y);
        splay(y);

        return y->min_val;
    }

    /**
     * 查询u到v路径上的最大值
     * @param u 节点u
     * @param v 节点v
     * @return 路径最大值
     */
    int queryMax(int u, int v) {
        Node* x = nodes[u];
        Node* y = nodes[v];

        makeRoot(x);
        access(y);
        splay(y);

        return y->max_val;
    }

    /**
     * 查询u到v路径上的异或值
     * @param u 节点u
     * @param v 节点v
     * @return 路径异或值
     */
    int queryXor(int u, int v) {
        Node* x = nodes[u];
        Node* y = nodes[v];

        makeRoot(x);
        access(y);
        splay(y);

        return y->xor_val;
    }

    /**
     * 判断u和v是否连通
     * @param u 节点u
     * @param v 节点v
     * @return 是否连通
     */
    bool isConnected(int u, int v) {
        return findRoot(nodes[u]) == findRoot(nodes[v]);
    }
};

int main() {
    // 测试用例
    LinkCutTree lct(5);

    // 设置节点值
    lct.setValue(0, 1);
    lct.setValue(1, 2);
    lct.setValue(2, 3);
    lct.setValue(3, 4);
    lct.setValue(4, 5);

    // 连接节点
    lct.link(0, 1);
    lct.link(1, 2);
    lct.link(2, 3);
    lct.link(3, 4);

    cout << "节点0到4的路径和: " << lct.querySum(0, 4) << endl; // 应该是15
    cout << "节点0到4的最小值: " << lct.queryMin(0, 4) << endl; // 应该是1
    cout << "节点0到4的最大值: " << lct.queryMax(0, 4) << endl; // 应该是5
    cout << "节点0到4的异或值: " << lct.queryXor(0, 4) << endl; // 应该是1^2^3^4^5 = 1

    // 断开边
    lct.cut(2, 3);
    cout << "断开边2-3后，0和4是否连通: " << (lct.isConnected(0, 4) ? "true" : "false") << endl; // 应该是false

    // 重新连接
    lct.link(2, 3);
    cout << "重新连接后，0和4是否连通: " << (lct.isConnected(0, 4) ? "true" : "false") << endl; // 应该是true

    return 0;
}
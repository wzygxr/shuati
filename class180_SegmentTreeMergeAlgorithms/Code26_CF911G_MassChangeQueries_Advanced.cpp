#include <iostream>
#include <vector>
#include <memory>
#include <algorithm>
using namespace std;

/**
 * Code26: CF911G Mass Change Queries (Advanced) - C++版本
 * 线段树分裂 + 值域线段树 + 懒标记
 */
class SegmentTree {
private:
    struct Node {
        int l, r;
        long long sum;
        int lazy;
        unique_ptr<Node> left, right;
        
        Node(int l, int r) : l(l), r(r), sum(0), lazy(-1) {}
    };
    
    vector<unique_ptr<Node>> roots;
    int n;
    
    void pushDown(Node* node) {
        if (node->l == node->r) return;
        
        int mid = (node->l + node->r) >> 1;
        if (!node->left) {
            node->left = make_unique<Node>(node->l, mid);
            node->right = make_unique<Node>(mid + 1, node->r);
        }
        
        if (node->lazy != -1) {
            node->left->lazy = node->lazy;
            node->right->lazy = node->lazy;
            node->left->sum = (mid - node->l + 1) * node->lazy;
            node->right->sum = (node->r - mid) * node->lazy;
            node->lazy = -1;
        }
    }
    
    void updateValue(Node* node, int L, int R, int val) {
        if (L <= node->l && node->r <= R) {
            node->sum += val;
            return;
        }
        
        pushDown(node);
        int mid = (node->l + node->r) >> 1;
        
        if (L <= mid) {
            if (!node->left) node->left = make_unique<Node>(node->l, mid);
            updateValue(node->left.get(), L, R, val);
        }
        if (R > mid) {
            if (!node->right) node->right = make_unique<Node>(mid + 1, node->r);
            updateValue(node->right.get(), L, R, val);
        }
        
        node->sum = (node->left ? node->left->sum : 0) + 
                   (node->right ? node->right->sum : 0);
    }
    
    unique_ptr<Node> split(Node* node, int L, int R) {
        if (!node || R < node->l || L > node->r) return nullptr;
        
        if (L <= node->l && node->r <= R) {
            auto result = make_unique<Node>(node->l, node->r);
            result->sum = node->sum;
            result->lazy = node->lazy;
            result->left = move(node->left);
            result->right = move(node->right);
            
            node->sum = 0;
            node->lazy = -1;
            node->left.reset();
            node->right.reset();
            
            return result;
        }
        
        pushDown(node);
        int mid = (node->l + node->r) >> 1;
        
        unique_ptr<Node> leftSplit, rightSplit;
        if (L <= mid) {
            leftSplit = split(node->left ? node->left.get() : nullptr, L, R);
        }
        if (R > mid) {
            rightSplit = split(node->right ? node->right.get() : nullptr, L, R);
        }
        
        node->sum = (node->left ? node->left->sum : 0) + 
                   (node->right ? node->right->sum : 0);
        
        if (leftSplit || rightSplit) {
            auto result = make_unique<Node>(node->l, node->r);
            result->left = move(leftSplit);
            result->right = move(rightSplit);
            result->sum = (result->left ? result->left->sum : 0) + 
                         (result->right ? result->right->sum : 0);
            return result;
        }
        return nullptr;
    }
    
    unique_ptr<Node> merge(unique_ptr<Node> a, unique_ptr<Node> b) {
        if (!a) return move(b);
        if (!b) return move(a);
        
        if (a->l == a->r) {
            a->sum += b->sum;
            return a;
        }
        
        pushDown(a.get());
        pushDown(b.get());
        
        a->left = merge(move(a->left), move(b->left));
        a->right = merge(move(a->right), move(b->right));
        a->sum = (a->left ? a->left->sum : 0) + 
                (a->right ? a->right->sum : 0);
        
        return a;
    }
    
    unique_ptr<Node> subtract(unique_ptr<Node> a, unique_ptr<Node> b) {
        if (!b) return move(a);
        if (!a) return nullptr;
        
        if (a->l == a->r) {
            a->sum -= b->sum;
            if (a->sum <= 0) return nullptr;
            return a;
        }
        
        pushDown(a.get());
        pushDown(b.get());
        
        a->left = subtract(move(a->left), move(b->left));
        a->right = subtract(move(a->right), move(b->right));
        a->sum = (a->left ? a->left->sum : 0) + 
                (a->right ? a->right->sum : 0);
        
        if (a->sum == 0) return nullptr;
        return a;
    }
    
    long long queryRange(Node* node, int L, int R) {
        if (!node || R < node->l || L > node->r) return 0;
        if (L <= node->l && node->r <= R) {
            return node->sum;
        }
        
        pushDown(node);
        int mid = (node->l + node->r) >> 1;
        long long res = 0;
        
        if (L <= mid && node->left) {
            res += queryRange(node->left.get(), L, R);
        }
        if (R > mid && node->right) {
            res += queryRange(node->right.get(), L, R);
        }
        
        return res;
    }
    
public:
    SegmentTree(const vector<int>& arr) : n(100) {
        roots.resize(101);
        for (int i = 1; i <= 100; i++) {
            roots[i] = make_unique<Node>(1, n);
        }
        
        for (int i = 0; i < arr.size(); i++) {
            updateValue(roots[arr[i]].get(), i + 1, i + 1, 1);
        }
    }
    
    void massChange(int l, int r, int x, int y) {
        if (x == y) return;
        
        auto splitTree = split(roots[x].get(), l, r);
        if (splitTree) {
            roots[y] = merge(move(roots[y]), move(splitTree));
            roots[x] = subtract(move(roots[x]), move(splitTree));
        }
    }
    
    long long querySum(int l, int r) {
        long long sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += queryRange(roots[i].get(), l, r) * i;
        }
        return sum;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    vector<int> arr(n);
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }
    
    SegmentTree stree(arr);
    
    int q;
    cin >> q;
    while (q--) {
        int type;
        cin >> type;
        if (type == 1) {
            int l, r, x, y;
            cin >> l >> r >> x >> y;
            stree.massChange(l, r, x, y);
        } else {
            int l, r;
            cin >> l >> r;
            cout << stree.querySum(l, r) << '\n';
        }
    }
    
    return 0;
}
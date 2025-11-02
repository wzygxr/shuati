#include <iostream>
#include <random>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * FHQ Treap算法实现 - C++版本
 * 
 * 本文件实现了FHQ Treap数据结构，支持以下操作：
 * 1. 插入、删除、查询
 * 2. 区间翻转、区间加
 * 3. 区间和、区间最小值、区间最大值查询
 * 4. 动态顺序统计
 * 
 * 时间复杂度：所有操作均为O(log n)
 * 空间复杂度：O(n)
 */

struct Node {
    int value;          // 节点值
    int priority;       // 随机优先级
    int size;          // 子树大小
    long long sum;     // 子树和
    int min;           // 子树最小值
    int max;           // 子树最大值
    long long add;     // 加法标记
    bool rev;          // 翻转标记
    Node *left, *right; // 左右子树
    
    Node(int v) : value(v), priority(rand()), size(1), sum(v), min(v), max(v),
                  add(0), rev(false), left(nullptr), right(nullptr) {}
    
    void pushDown() {
        // 处理翻转标记
        if (rev) {
            swap(left, right);
            if (left) left->rev ^= true;
            if (right) right->rev ^= true;
            rev = false;
        }
        
        // 处理加法标记
        if (add != 0) {
            if (left) {
                left->value += add;
                left->sum += add * left->size;
                left->min += add;
                left->max += add;
                left->add += add;
            }
            if (right) {
                right->value += add;
                right->sum += add * right->size;
                right->min += add;
                right->max += add;
                right->add += add;
            }
            add = 0;
        }
    }
    
    void pushUp() {
        size = 1;
        sum = value;
        min = max = value;
        
        if (left) {
            size += left->size;
            sum += left->sum;
            min = std::min(min, left->min);
            max = std::max(max, left->max);
        }
        
        if (right) {
            size += right->size;
            sum += right->sum;
            min = std::min(min, right->min);
            max = std::max(max, right->max);
        }
    }
};

class ImplicitFHQTreap {
private:
    Node* root;
    
    int getSize(Node* node) {
        return node ? node->size : 0;
    }
    
    void split(Node* root, int k, Node*& l, Node*& r) {
        if (!root) {
            l = r = nullptr;
            return;
        }
        
        root->pushDown();
        int leftSize = getSize(root->left);
        if (leftSize + 1 <= k) {
            l = root;
            split(root->right, k - leftSize - 1, root->right, r);
            l->pushUp();
        } else {
            r = root;
            split(root->left, k, l, root->left);
            r->pushUp();
        }
    }
    
    Node* merge(Node* a, Node* b) {
        if (!a) return b;
        if (!b) return a;
        
        if (a->priority > b->priority) {
            a->pushDown();
            a->right = merge(a->right, b);
            a->pushUp();
            return a;
        } else {
            b->pushDown();
            b->left = merge(a, b->left);
            b->pushUp();
            return b;
        }
    }
    
public:
    ImplicitFHQTreap() : root(nullptr) {}
    
    void insert(int pos, int value) {
        Node *l, *r;
        split(root, pos, l, r);
        root = merge(merge(l, new Node(value)), r);
    }
    
    void erase(int pos) {
        Node *l, *m, *r;
        split(root, pos + 1, m, r);
        split(m, pos, l, m);
        root = merge(l, r);
    }
    
    void reverse(int l, int r) {
        Node *l1, *r1, *l2, *r2;
        split(root, r + 1, l1, r1);
        split(l1, l, l2, r2);
        
        if (r2) {
            r2->rev ^= true;
        }
        
        root = merge(merge(l2, r2), r1);
    }
    
    void rangeAdd(int l, int r, int val) {
        Node *l1, *r1, *l2, *r2;
        split(root, r + 1, l1, r1);
        split(l1, l, l2, r2);
        
        if (r2) {
            r2->value += val;
            r2->sum += (long long)val * r2->size;
            r2->min += val;
            r2->max += val;
            r2->add += val;
        }
        
        root = merge(merge(l2, r2), r1);
    }
    
    long long querySum(int l, int r) {
        Node *l1, *r1, *l2, *r2;
        split(root, r + 1, l1, r1);
        split(l1, l, l2, r2);
        long long result = r2 ? r2->sum : 0;
        root = merge(merge(l2, r2), r1);
        return result;
    }
    
    int queryMin(int l, int r) {
        Node *l1, *r1, *l2, *r2;
        split(root, r + 1, l1, r1);
        split(l1, l, l2, r2);
        int result = r2 ? r2->min : INT_MAX;
        root = merge(merge(l2, r2), r1);
        return result;
    }
    
    int queryMax(int l, int r) {
        Node *l1, *r1, *l2, *r2;
        split(root, r + 1, l1, r1);
        split(l1, l, l2, r2);
        int result = r2 ? r2->max : INT_MIN;
        root = merge(merge(l2, r2), r1);
        return result;
    }
    
    int size() {
        return getSize(root);
    }
    
    void inorderTraversal() {
        inorderTraversal(root);
        cout << endl;
    }
    
private:
    void inorderTraversal(Node* node) {
        if (!node) return;
        
        node->pushDown();
        inorderTraversal(node->left);
        cout << node->value << " ";
        inorderTraversal(node->right);
    }
};

// 动态顺序统计类
class DynamicOrderStatistics {
private:
    ImplicitFHQTreap treap;
    
public:
    DynamicOrderStatistics() {}
    
    void insert(int pos, int value) {
        treap.insert(pos, value);
    }
    
    void erase(int pos) {
        treap.erase(pos);
    }
    
    long long querySum(int l, int r) {
        return treap.querySum(l, r);
    }
    
    int queryMin(int l, int r) {
        return treap.queryMin(l, r);
    }
    
    int queryMax(int l, int r) {
        return treap.queryMax(l, r);
    }
    
    void reverse(int l, int r) {
        treap.reverse(l, r);
    }
    
    void rangeAdd(int l, int r, int val) {
        treap.rangeAdd(l, r, val);
    }
    
    void printSequence() {
        treap.inorderTraversal();
    }
};

// 测试函数
int main() {
    cout << "=== 测试FHQ Treap算法 ===" << endl;
    
    // 测试动态顺序统计
    cout << "\n=== 测试动态顺序统计 ===" << endl;
    DynamicOrderStatistics dos;
    
    // 插入元素
    dos.insert(0, 1);
    dos.insert(1, 3);
    dos.insert(2, 5);
    dos.insert(3, 7);
    dos.insert(4, 9);
    
    cout << "初始序列: ";
    dos.printSequence();
    
    cout << "序列和 [0,4]: " << dos.querySum(0, 4) << endl;
    cout << "序列最小值 [0,4]: " << dos.queryMin(0, 4) << endl;
    cout << "序列最大值 [0,4]: " << dos.queryMax(0, 4) << endl;
    
    // 测试区间操作
    cout << "\n=== 测试区间操作 ===" << endl;
    dos.rangeAdd(1, 3, 10);
    cout << "区间[1,3]加10后序列: ";
    dos.printSequence();
    
    cout << "区间[1,3]加10后序列和: " << dos.querySum(0, 4) << endl;
    
    // 测试翻转操作
    cout << "\n=== 测试翻转操作 ===" << endl;
    dos.reverse(1, 3);
    cout << "翻转区间[1,3]后序列: ";
    dos.printSequence();
    
    // 测试删除操作
    cout << "\n=== 测试删除操作 ===" << endl;
    dos.erase(2);
    cout << "删除位置2后序列: ";
    dos.printSequence();
    
    cout << "删除后序列和 [0,3]: " << dos.querySum(0, 3) << endl;
    
    return 0;
}
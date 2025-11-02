/*
 * 题目：区间翻转 (Interval Reversal)
 * 来源：POJ 3580
 * 网址：http://poj.org/problem?id=3580
 * 
 * 问题描述：
 * 维护一个序列，支持以下操作：
 * 1. ADD x y D: 将区间[x,y]中的每个元素加上D
 * 2. REVERSE x y: 将区间[x,y]翻转
 * 3. REVOLVE x y T: 将区间[x,y]循环右移T次
 * 4. INSERT x P: 在位置x后插入P
 * 5. DELETE x: 删除位置x的元素
 * 6. MIN x y: 查询区间[x,y]的最小值
 * 
 * 时间复杂度：每个操作平均O(log n)
 * 空间复杂度：O(n)
 * 
 * 解题思路：
 * 使用Splay树维护序列，每个节点存储子树大小、最小值、懒标记
 * 通过splay操作实现高效的区间操作
 */

#include <iostream>
#include <cstdio>
#include <cstring>
#include <algorithm>
#include <climits>
using namespace std;

const int INF = 0x3f3f3f3f;

struct SplayNode {
    int key;           // 节点值
    int size;          // 子树大小
    int minVal;        // 子树最小值
    int addLazy;       // 加法懒标记
    bool revLazy;      // 翻转懒标记
    SplayNode *left;   // 左子树
    SplayNode *right;  // 右子树
    SplayNode *parent; // 父节点
    
    SplayNode(int k) : key(k), size(1), minVal(k), addLazy(0), revLazy(false), 
                      left(nullptr), right(nullptr), parent(nullptr) {}
};

SplayNode* root = nullptr;

// 维护子树信息
void maintain(SplayNode* x) {
    if (x != nullptr) {
        x->size = 1;
        x->minVal = x->key;
        
        if (x->left != nullptr) {
            x->size += x->left->size;
            x->minVal = min(x->minVal, x->left->minVal);
        }
        if (x->right != nullptr) {
            x->size += x->right->size;
            x->minVal = min(x->minVal, x->right->minVal);
        }
    }
}

// 下传懒标记
void pushDown(SplayNode* x) {
    if (x != nullptr) {
        if (x->addLazy != 0) {
            x->key += x->addLazy;
            if (x->left != nullptr) {
                x->left->addLazy += x->addLazy;
                x->left->minVal += x->addLazy;
            }
            if (x->right != nullptr) {
                x->right->addLazy += x->addLazy;
                x->right->minVal += x->addLazy;
            }
            x->addLazy = 0;
        }
        
        if (x->revLazy) {
            swap(x->left, x->right);
            if (x->left != nullptr) x->left->revLazy = !x->left->revLazy;
            if (x->right != nullptr) x->right->revLazy = !x->right->revLazy;
            x->revLazy = false;
        }
    }
}

// 左旋操作
void leftRotate(SplayNode* x) {
    SplayNode* y = x->right;
    pushDown(x);
    pushDown(y);
    
    if (y != nullptr) {
        x->right = y->left;
        if (y->left != nullptr) y->left->parent = x;
        y->parent = x->parent;
    }
    
    if (x->parent == nullptr) {
        root = y;
    } else if (x == x->parent->left) {
        x->parent->left = y;
    } else {
        x->parent->right = y;
    }
    
    if (y != nullptr) y->left = x;
    x->parent = y;
    
    maintain(x);
    maintain(y);
}

// 右旋操作
void rightRotate(SplayNode* x) {
    SplayNode* y = x->left;
    pushDown(x);
    pushDown(y);
    
    if (y != nullptr) {
        x->left = y->right;
        if (y->right != nullptr) y->right->parent = x;
        y->parent = x->parent;
    }
    
    if (x->parent == nullptr) {
        root = y;
    } else if (x == x->parent->left) {
        x->parent->left = y;
    } else {
        x->parent->right = y;
    }
    
    if (y != nullptr) y->right = x;
    x->parent = y;
    
    maintain(x);
    maintain(y);
}

// Splay操作
void splay(SplayNode* x) {
    while (x->parent != nullptr) {
        if (x->parent->parent == nullptr) {
            if (x == x->parent->left) {
                rightRotate(x->parent);
            } else {
                leftRotate(x->parent);
            }
        } else {
            SplayNode* parent = x->parent;
            SplayNode* grandParent = parent->parent;
            
            if (parent->left == x && grandParent->left == parent) {
                rightRotate(grandParent);
                rightRotate(parent);
            } else if (parent->right == x && grandParent->right == parent) {
                leftRotate(grandParent);
                leftRotate(parent);
            } else if (parent->left == x && grandParent->right == parent) {
                rightRotate(parent);
                leftRotate(grandParent);
            } else {
                leftRotate(parent);
                rightRotate(grandParent);
            }
        }
    }
}

// 获取第k小的节点
SplayNode* getKth(int k) {
    if (root == nullptr || k <= 0 || k > root->size) {
        return nullptr;
    }
    
    SplayNode* current = root;
    while (current != nullptr) {
        pushDown(current);
        int leftSize = (current->left != nullptr) ? current->left->size : 0;
        
        if (k == leftSize + 1) {
            return current;
        } else if (k <= leftSize) {
            current = current->left;
        } else {
            k -= leftSize + 1;
            current = current->right;
        }
    }
    return nullptr;
}

// 分割区间[l, r]
void split(int l, int r, SplayNode* &left, SplayNode* &mid, SplayNode* &right) {
    if (l > 1) {
        SplayNode* leftPart = getKth(l - 1);
        splay(leftPart);
        SplayNode* rightPart = leftPart->right;
        leftPart->right = nullptr;
        if (rightPart != nullptr) rightPart->parent = nullptr;
        maintain(leftPart);
        
        if (rightPart != nullptr) {
            SplayNode* midPart = getKth(r - l + 1);
            splay(midPart);
            SplayNode* remaining = midPart->right;
            midPart->right = nullptr;
            if (remaining != nullptr) remaining->parent = nullptr;
            maintain(midPart);
            
            left = leftPart;
            mid = midPart;
            right = remaining;
        }
    } else {
        SplayNode* midPart = getKth(r);
        splay(midPart);
        SplayNode* remaining = midPart->right;
        midPart->right = nullptr;
        if (remaining != nullptr) remaining->parent = nullptr;
        maintain(midPart);
        
        left = nullptr;
        mid = midPart;
        right = remaining;
    }
}

// 合并子树
SplayNode* merge(SplayNode* left, SplayNode* right) {
    if (left == nullptr) return right;
    if (right == nullptr) return left;
    
    SplayNode* maxNode = left;
    while (maxNode->right != nullptr) {
        maxNode = maxNode->right;
    }
    splay(maxNode);
    maxNode->right = right;
    right->parent = maxNode;
    maintain(maxNode);
    
    return maxNode;
}

// 区间加法
void addInterval(int l, int r, int d) {
    SplayNode *left, *mid, *right;
    split(l, r, left, mid, right);
    if (mid != nullptr) {
        mid->addLazy += d;
        mid->minVal += d;
    }
    root = merge(merge(left, mid), right);
}

// 区间翻转
void reverseInterval(int l, int r) {
    SplayNode *left, *mid, *right;
    split(l, r, left, mid, right);
    if (mid != nullptr) {
        mid->revLazy = !mid->revLazy;
    }
    root = merge(merge(left, mid), right);
}

// 区间循环右移
void revolveInterval(int l, int r, int t) {
    int len = r - l + 1;
    t %= len;
    if (t == 0) return;
    
    SplayNode *left, *mid, *right;
    split(l, r, left, mid, right);
    if (mid != nullptr) {
        SplayNode *subLeft, *subMid, *subRight;
        split(1, len - t, subLeft, subMid, subRight);
        mid = merge(subRight, subMid);
    }
    root = merge(merge(left, mid), right);
}

// 插入节点
void insert(int pos, int val) {
    SplayNode* newNode = new SplayNode(val);
    if (pos == 0) {
        if (root == nullptr) {
            root = newNode;
        } else {
            SplayNode* minNode = root;
            while (minNode->left != nullptr) {
                minNode = minNode->left;
            }
            splay(minNode);
            minNode->left = newNode;
            newNode->parent = minNode;
            maintain(minNode);
        }
    } else {
        SplayNode* node = getKth(pos);
        splay(node);
        newNode->right = node->right;
        if (node->right != nullptr) node->right->parent = newNode;
        node->right = newNode;
        newNode->parent = node;
        maintain(newNode);
        maintain(node);
    }
}

// 删除节点
void deleteNode(int pos) {
    SplayNode* node = getKth(pos);
    splay(node);
    
    if (node->left == nullptr) {
        root = node->right;
        if (root != nullptr) root->parent = nullptr;
    } else if (node->right == nullptr) {
        root = node->left;
        if (root != nullptr) root->parent = nullptr;
    } else {
        SplayNode* leftTree = node->left;
        leftTree->parent = nullptr;
        SplayNode* rightTree = node->right;
        rightTree->parent = nullptr;
        
        SplayNode* maxNode = leftTree;
        while (maxNode->right != nullptr) {
            maxNode = maxNode->right;
        }
        splay(maxNode);
        maxNode->right = rightTree;
        rightTree->parent = maxNode;
        maintain(maxNode);
        root = maxNode;
    }
    delete node;
}

// 查询区间最小值
int queryMin(int l, int r) {
    SplayNode *left, *mid, *right;
    split(l, r, left, mid, right);
    int minVal = (mid != nullptr) ? mid->minVal : INF;
    root = merge(merge(left, mid), right);
    return minVal;
}

int main() {
    int n;
    scanf("%d", &n);
    
    // 初始化序列
    root = nullptr;
    for (int i = 0; i < n; i++) {
        int val;
        scanf("%d", &val);
        insert(i, val);
    }
    
    int m;
    scanf("%d", &m);
    
    for (int i = 0; i < m; i++) {
        char op[10];
        scanf("%s", op);
        
        if (strcmp(op, "ADD") == 0) {
            int x, y, d;
            scanf("%d%d%d", &x, &y, &d);
            addInterval(x, y, d);
        } else if (strcmp(op, "REVERSE") == 0) {
            int x, y;
            scanf("%d%d", &x, &y);
            reverseInterval(x, y);
        } else if (strcmp(op, "REVOLVE") == 0) {
            int x, y, t;
            scanf("%d%d%d", &x, &y, &t);
            revolveInterval(x, y, t);
        } else if (strcmp(op, "INSERT") == 0) {
            int pos, val;
            scanf("%d%d", &pos, &val);
            insert(pos, val);
        } else if (strcmp(op, "DELETE") == 0) {
            int pos;
            scanf("%d", &pos);
            deleteNode(pos);
        } else if (strcmp(op, "MIN") == 0) {
            int x, y;
            scanf("%d%d", &x, &y);
            printf("%d\n", queryMin(x, y));
        }
    }
    
    return 0;
}
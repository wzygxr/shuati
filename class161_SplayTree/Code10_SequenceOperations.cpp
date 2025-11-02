/*
 * 题目：序列操作 (Sequence Operations)
 * 来源：HDU 3436
 * 网址：http://acm.hdu.edu.cn/showproblem.php?pid=3436
 * 
 * 问题描述：
 * 维护一个序列，支持以下操作：
 * 1. TOP x: 将元素x移动到序列开头
 * 2. QUERY x: 查询元素x在序列中的位置
 * 3. RANK x: 查询序列中第x个位置的元素
 * 
 * 时间复杂度：每个操作平均O(log n)
 * 空间复杂度：O(n)
 * 
 * 解题思路：
 * 使用Splay树维护序列，每个节点存储子树大小用于快速定位
 * 通过splay操作实现高效的区间操作和位置查询
 */

#include <iostream>
#include <cstdio>
#include <cstring>
#include <map>
#include <string>
using namespace std;

struct SplayNode {
    int key;           // 节点值
    int size;          // 子树大小
    SplayNode *left;   // 左子树
    SplayNode *right;  // 右子树
    SplayNode *parent; // 父节点
    
    SplayNode(int k) : key(k), size(1), left(nullptr), right(nullptr), parent(nullptr) {}
};

SplayNode* root = nullptr;
map<int, SplayNode*> nodeMap;

// 维护子树大小
void maintain(SplayNode* x) {
    if (x != nullptr) {
        x->size = 1;
        if (x->left != nullptr) x->size += x->left->size;
        if (x->right != nullptr) x->size += x->right->size;
    }
}

// 左旋操作
void leftRotate(SplayNode* x) {
    SplayNode* y = x->right;
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

// Splay操作：将节点x旋转到根
void splay(SplayNode* x) {
    while (x->parent != nullptr) {
        if (x->parent->parent == nullptr) {
            // 父节点是根节点
            if (x == x->parent->left) {
                rightRotate(x->parent);
            } else {
                leftRotate(x->parent);
            }
        } else {
            SplayNode* parent = x->parent;
            SplayNode* grandParent = parent->parent;
            
            if (parent->left == x && grandParent->left == parent) {
                // LL情况
                rightRotate(grandParent);
                rightRotate(parent);
            } else if (parent->right == x && grandParent->right == parent) {
                // RR情况
                leftRotate(grandParent);
                leftRotate(parent);
            } else if (parent->left == x && grandParent->right == parent) {
                // LR情况
                rightRotate(parent);
                leftRotate(grandParent);
            } else {
                // RL情况
                leftRotate(parent);
                rightRotate(grandParent);
            }
        }
    }
}

// 插入节点
void insert(int key) {
    SplayNode* newNode = new SplayNode(key);
    nodeMap[key] = newNode;
    
    if (root == nullptr) {
        root = newNode;
        return;
    }
    
    SplayNode* current = root;
    SplayNode* parent = nullptr;
    
    while (current != nullptr) {
        parent = current;
        if (key < current->key) {
            current = current->left;
        } else {
            current = current->right;
        }
    }
    
    if (key < parent->key) {
        parent->left = newNode;
    } else {
        parent->right = newNode;
    }
    newNode->parent = parent;
    
    splay(newNode);
}

// 查找节点
SplayNode* find(int key) {
    SplayNode* current = root;
    while (current != nullptr) {
        if (key == current->key) {
            splay(current);
            return current;
        } else if (key < current->key) {
            current = current->left;
        } else {
            current = current->right;
        }
    }
    return nullptr;
}

// 获取第k小的元素
SplayNode* getKth(int k) {
    if (root == nullptr || k <= 0 || k > root->size) {
        return nullptr;
    }
    
    SplayNode* current = root;
    while (current != nullptr) {
        int leftSize = (current->left != nullptr) ? current->left->size : 0;
        
        if (k == leftSize + 1) {
            splay(current);
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

// 获取节点的排名
int getRank(SplayNode* x) {
    if (x == nullptr) return -1;
    splay(x);
    return (x->left != nullptr) ? x->left->size + 1 : 1;
}

// 将节点移动到开头
void moveToFront(int key) {
    SplayNode* node = find(key);
    if (node == nullptr) return;
    
    // 如果已经是第一个节点，不需要移动
    if (node->left == nullptr) return;
    
    // 分离左子树
    SplayNode* leftTree = node->left;
    node->left = nullptr;
    leftTree->parent = nullptr;
    maintain(node);
    
    // 找到左子树的最大节点
    SplayNode* maxNode = leftTree;
    while (maxNode->right != nullptr) {
        maxNode = maxNode->right;
    }
    splay(maxNode);
    
    // 将原节点插入到左子树最大节点的右侧
    maxNode->right = node;
    node->parent = maxNode;
    maintain(maxNode);
    
    root = maxNode;
}

int main() {
    int T;
    scanf("%d", &T);
    
    for (int t = 1; t <= T; t++) {
        int n, m;
        scanf("%d%d", &n, &m);
        
        // 初始化Splay树
        root = nullptr;
        nodeMap.clear();
        
        // 插入初始序列
        for (int i = 1; i <= n; i++) {
            insert(i);
        }
        
        printf("Case %d:\n", t);
        
        for (int i = 0; i < m; i++) {
            char op[10];
            int x;
            scanf("%s%d", op, &x);
            
            if (strcmp(op, "TOP") == 0) {
                moveToFront(x);
            } else if (strcmp(op, "QUERY") == 0) {
                SplayNode* node = find(x);
                if (node != nullptr) {
                    printf("%d\n", getRank(node));
                }
            } else if (strcmp(op, "RANK") == 0) {
                SplayNode* kthNode = getKth(x);
                if (kthNode != nullptr) {
                    printf("%d\n", kthNode->key);
                }
            }
        }
    }
    
    return 0;
}
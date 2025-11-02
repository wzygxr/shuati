// 文本编辑器，能通过的C++版本
// 一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作
// Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
// Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
// Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
// Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
// Prev       : 光标前移一个字符，操作保证光标不会到非法位置
// Next       : 光标后移一个字符，操作保证光标不会到非法位置
// Insert操作时，字符串s中ASCII码在[32,126]范围上的字符一定有n个，其他字符请过滤掉
// 测试链接 : https://www.luogu.com.cn/problem/P4008
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
// 一个能通过的版本，连数组都自己写扩容逻辑，IO彻底重写，看看就好
// 讲解172，讲解块状链表时，本题又讲了一遍，分块的方法，可以通过所有测试用例，更有学习意义

#include <iostream>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <ctime>
#include <vector>
using namespace std;

// FHQ Treap节点结构
struct Node {
    char key;
    int size;
    double priority;
    Node *left, *right;
    
    Node(char k) : key(k), size(1), priority((double)rand() / RAND_MAX), left(nullptr), right(nullptr) {}
};

// 更新节点大小
void updateSize(Node* node) {
    if (node == nullptr) return;
    node->size = 1;
    if (node->left != nullptr) node->size += node->left->size;
    if (node->right != nullptr) node->size += node->right->size;
}

// 分裂操作
void split(Node* root, int k, Node* &left, Node* &right) {
    if (root == nullptr) {
        left = right = nullptr;
        return;
    }
    
    int leftSize = (root->left == nullptr) ? 0 : root->left->size;
    if (leftSize + 1 <= k) {
        left = root;
        split(root->right, k - leftSize - 1, root->right, right);
    } else {
        right = root;
        split(root->left, k, left, root->left);
    }
    updateSize(root);
}

// 合并操作
Node* merge(Node* left, Node* right) {
    if (left == nullptr) return right;
    if (right == nullptr) return left;
    
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

// 中序遍历输出
void inorder(Node* root, vector<char> &result) {
    if (root == nullptr) return;
    inorder(root->left, result);
    result.push_back(root->key);
    inorder(root->right, result);
}

int main() {
    srand(time(0));
    
    Node* root = nullptr;
    int cursor = 0; // 光标位置
    int op;
    cin >> op;
    
    while (op--) {
        string command;
        cin >> command;
        
        if (command == "Move") {
            int k;
            cin >> k;
            cursor = k;
        } else if (command == "Insert") {
            int n;
            cin >> n;
            string s;
            cin >> s;
            
            // 过滤有效字符
            vector<char> validChars;
            for (char c : s) {
                if (c >= 32 && c <= 126) {
                    validChars.push_back(c);
                }
            }
            
            // 分裂树
            Node *left, *right;
            split(root, cursor, left, right);
            
            // 插入新节点
            for (char c : validChars) {
                Node* newNode = new Node(c);
                left = merge(left, newNode);
            }
            
            root = merge(left, right);
        } else if (command == "Delete") {
            int n;
            cin >> n;
            
            Node *left, *mid, *right;
            split(root, cursor, left, right);
            split(right, n, mid, right);
            
            root = merge(left, right);
        } else if (command == "Get") {
            int n;
            cin >> n;
            
            Node *left, *mid, *right;
            split(root, cursor, left, right);
            split(right, n, mid, right);
            
            vector<char> result;
            inorder(mid, result);
            for (char c : result) {
                cout << c;
            }
            cout << endl;
            
            root = merge(merge(left, mid), right);
        } else if (command == "Prev") {
            cursor--;
        } else if (command == "Next") {
            cursor++;
        }
    }
    
    return 0;
}
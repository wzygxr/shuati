// 洛谷 P2596 [ZJOI2006]书架 - C++版本
// 题目链接: https://www.luogu.com.cn/problem/P2596
// 题目描述: 维护一个书架，支持以下操作：
// 1. 将某本书置于顶部 (Top x)
// 2. 将某本书置于底部 (Bottom x)
// 3. 将某本书置于指定位置 (Insert x y)
// 4. 询问某本书在书架上的位置 (Ask x)
// 5. 从顶部取书 (Query Top)
// 6. 从底部取书 (Query Bottom)
//
// 【算法原理】
// 使用FHQ-Treap维护书架上的书籍顺序，通过分裂和合并操作实现书籍的移动
// 每个节点存储书的编号和随机优先级，通过子树大小维护位置信息
//
// 【时间复杂度分析】
// 每个操作的期望时间复杂度：O(log n)，其中n为书籍数量
// 最坏情况下可能退化为O(n)，但概率极低
//
// 【空间复杂度分析】
// O(n)，存储所有书籍节点
//
// 【适用场景】
// - 需要动态维护序列顺序的场景
// - 支持高效插入、删除和位置查询的数据结构
// - 需要支持复杂位置操作的应用

#include <iostream>
#include <cstdlib>
#include <ctime>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

// FHQ-Treap节点结构
struct Node {
    int key;        // 书的编号
    int priority;   // 随机优先级
    int size;       // 子树大小
    int position;   // 书在书架上的位置
    Node* left;     // 左子节点
    Node* right;    // 右子节点
    
    Node(int k) : key(k), priority(rand()), size(1), position(0), left(nullptr), right(nullptr) {}
};

class Code17_Bookshelf2 {
private:
    Node* root;     // 根节点
    int nodeCnt;    // 节点计数
    
    // 更新节点信息
    void update(Node* node) {
        if (node != nullptr) {
            int leftSize = (node->left != nullptr) ? node->left->size : 0;
            int rightSize = (node->right != nullptr) ? node->right->size : 0;
            node->size = leftSize + rightSize + 1;
        }
    }
    
    // 按位置分裂，将树按照位置pos分裂为两棵树
    pair<Node*, Node*> splitByPosition(Node* root, int pos) {
        if (root == nullptr) {
            return {nullptr, nullptr};
        }
        
        int leftSize = (root->left != nullptr) ? root->left->size : 0;
        
        if (leftSize + 1 <= pos) {
            auto [leftTree, rightTree] = splitByPosition(root->right, pos - leftSize - 1);
            root->right = leftTree;
            update(root);
            return {root, rightTree};
        } else {
            auto [leftTree, rightTree] = splitByPosition(root->left, pos);
            root->left = rightTree;
            update(root);
            return {leftTree, root};
        }
    }
    
    // 合并两棵树
    Node* merge(Node* leftTree, Node* rightTree) {
        if (leftTree == nullptr) return rightTree;
        if (rightTree == nullptr) return leftTree;
        
        if (leftTree->priority > rightTree->priority) {
            leftTree->right = merge(leftTree->right, rightTree);
            update(leftTree);
            return leftTree;
        } else {
            rightTree->left = merge(leftTree, rightTree->left);
            update(rightTree);
            return rightTree;
        }
    }
    
    // 按值查找节点
    Node* find(Node* root, int key) {
        if (root == nullptr) return nullptr;
        if (root->key == key) return root;
        if (key < root->key) return find(root->left, key);
        return find(root->right, key);
    }
    
    // 获取节点的位置
    int getPosition(Node* root, int key) {
        if (root == nullptr) return 0;
        if (root->key == key) {
            return (root->left != nullptr ? root->left->size : 0) + 1;
        }
        if (key < root->key) {
            return getPosition(root->left, key);
        } else {
            return (root->left != nullptr ? root->left->size : 0) + 1 + getPosition(root->right, key);
        }
    }
    
    // 中序遍历，用于调试
    void inorder(Node* root, vector<int>& result) {
        if (root == nullptr) return;
        inorder(root->left, result);
        result.push_back(root->key);
        inorder(root->right, result);
    }
    
public:
    Code17_Bookshelf2() : root(nullptr), nodeCnt(0) {
        srand(time(nullptr)); // 初始化随机种子
    }
    
    // 将书置于顶部
    void top(int x) {
        int pos = getPosition(root, x);
        if (pos == 0) return; // 书不存在
        
        auto [left, right] = splitByPosition(root, pos - 1);
        auto [target, rest] = splitByPosition(right, 1);
        
        root = merge(target, merge(left, rest));
    }
    
    // 将书置于底部
    void bottom(int x) {
        int pos = getPosition(root, x);
        if (pos == 0) return; // 书不存在
        
        auto [left, right] = splitByPosition(root, pos - 1);
        auto [target, rest] = splitByPosition(right, 1);
        
        root = merge(merge(left, rest), target);
    }
    
    // 将书插入到指定位置
    void insert(int x, int y) {
        int pos = getPosition(root, x);
        if (pos == 0) return; // 书不存在
        
        // 先删除原位置的书籍
        auto [left1, right1] = splitByPosition(root, pos - 1);
        auto [target, right2] = splitByPosition(right1, 1);
        
        // 计算新位置
        int newPos;
        if (y == 0) {
            newPos = 1; // 顶部
        } else if (y == 1) {
            newPos = (left1 != nullptr ? left1->size : 0) + 1; // 原位置
        } else if (y == -1) {
            newPos = (left1 != nullptr ? left1->size : 0); // 原位置前一个
        } else {
            newPos = y; // 指定位置
        }
        
        // 插入到新位置
        auto [left2, right3] = splitByPosition(merge(left1, right2), newPos - 1);
        root = merge(merge(left2, target), right3);
    }
    
    // 询问书的位置
    int ask(int x) {
        return getPosition(root, x);
    }
    
    // 查询顶部书籍
    int queryTop() {
        if (root == nullptr) return 0;
        
        Node* current = root;
        while (current->left != nullptr) {
            current = current->left;
        }
        return current->key;
    }
    
    // 查询底部书籍
    int queryBottom() {
        if (root == nullptr) return 0;
        
        Node* current = root;
        while (current->right != nullptr) {
            current = current->right;
        }
        return current->key;
    }
    
    // 添加书籍
    void add(int x) {
        Node* newNode = new Node(x);
        root = merge(root, newNode);
        nodeCnt++;
    }
    
    // 获取书架上的所有书籍（用于调试）
    vector<int> getAllBooks() {
        vector<int> result;
        inorder(root, result);
        return result;
    }
    
    // 获取书籍数量
    int getBookCount() {
        return nodeCnt;
    }
};

// 测试函数
int main() {
    Code17_Bookshelf2 bookshelf;
    
    // 测试用例：添加5本书
    for (int i = 1; i <= 5; i++) {
        bookshelf.add(i);
    }
    
    cout << "初始书架: ";
    vector<int> books = bookshelf.getAllBooks();
    for (int book : books) {
        cout << book << " ";
    }
    cout << endl;
    
    // 测试将书3置于顶部
    bookshelf.top(3);
    cout << "将书3置于顶部后: ";
    books = bookshelf.getAllBooks();
    for (int book : books) {
        cout << book << " ";
    }
    cout << endl;
    
    // 测试将书2置于底部
    bookshelf.bottom(2);
    cout << "将书2置于底部后: ";
    books = bookshelf.getAllBooks();
    for (int book : books) {
        cout << book << " ";
    }
    cout << endl;
    
    // 测试查询书4的位置
    int pos = bookshelf.ask(4);
    cout << "书4的位置: " << pos << endl;
    
    // 测试查询顶部书籍
    int topBook = bookshelf.queryTop();
    cout << "顶部书籍: " << topBook << endl;
    
    // 测试查询底部书籍
    int bottomBook = bookshelf.queryBottom();
    cout << "底部书籍: " << bottomBook << endl;
    
    return 0;
}
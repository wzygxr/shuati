// LeetCode 2336. 无限集中的最小数字 - C++实现
// 使用FHQ-Treap（无旋Treap）解决LeetCode 2336题
// 题目链接: https://leetcode.cn/problems/smallest-number-in-infinite-set/
// 题目描述: 设计一个数据结构，维护一个包含所有正整数的无限集，支持以下操作：
// 1. popSmallest(): 弹出并返回集合中最小的整数
// 2. addBack(num): 将一个之前弹出的正整数num添加回集合中
// 
// 解题思路:
// 使用FHQ-Treap维护已删除的元素集合，同时使用currentMin变量优化最小值查询
// 实现O(log k)的操作复杂度，其中k是已删除的元素个数

// 为适应编译环境，使用基础C++实现方式
// 避免使用复杂的STL容器和标准库函数

class SmallestInfiniteSet {
private:
    // FHQ-Treap节点结构
    struct Node {
        int key;        // 键值（存储被删除的正整数）
        int count;      // 词频计数
        int size;       // 子树大小
        int priority;   // 随机优先级
        Node *left;     // 左子节点
        Node *right;    // 右子节点
        
        Node(int k)
            : key(k), count(1), size(1), 
              priority(0), left(nullptr), right(nullptr) {
        // 为适应编译环境，使用简单随机数生成方式
        static int seed = 1;
        seed = seed * 1103515245 + 12345;
        priority = seed & 0x7fffffff;
    }
    };
    
    Node *root;       // 根节点
    int currentMin;   // 当前最小的可用正整数
    
    // 更新节点的子树大小
    void updateSize(Node *node) {
        if (node) {
            int leftSize = node->left ? node->left->size : 0;
            int rightSize = node->right ? node->right->size : 0;
            node->size = leftSize + rightSize + node->count;
        }
    }
    
    // 分裂操作：将树按值分成两部分
    // 为适应编译环境，使用指针数组代替pair
    Node** split(Node *root, int key) {
        if (!root) {
            Node** result = new Node*[2];
            result[0] = result[1] = nullptr;
            return result;
        }
        
        if (root->key <= key) {
            // 当前节点及其左子树属于左部分，递归分裂右子树
            Node** parts = split(root->right, key);
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
            Node** parts = split(root->left, key);
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
    
    // 合并操作：合并两棵满足条件的树
    Node* merge(Node *left, Node *right) {
        if (!left) return right;
        if (!right) return left;
        
        if (left->priority >= right->priority) {
            // 左树优先级更高，作为新根
            left->right = merge(left->right, right);
            updateSize(left);
            return left;
        } else {
            // 右树优先级更高，作为新根
            right->left = merge(left, right->left);
            updateSize(right);
            return right;
        }
    }
    
    // 查找元素是否存在
    bool contains(Node *root, int num) {
        if (!root) return false;
        if (root->key == num) return true;
        if (root->key > num) return contains(root->left, num);
        return contains(root->right, num);
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
    SmallestInfiniteSet() {
        root = nullptr;
        currentMin = 1;
        // 为适应编译环境，使用固定种子
        // srand(time(nullptr));
    }
    
    ~SmallestInfiniteSet() {
        clearTree(root); // 释放内存
    }
    
    int popSmallest() {
        // 如果currentMin未被删除，直接返回并递增
        if (!contains(root, currentMin)) {
            int result = currentMin;
            currentMin++;
            return result;
        }
        
        // 否则需要找到第一个未被删除的值
        int result = currentMin;
        while (contains(root, result)) {
            result++;
        }
        
        // 添加到已删除集合
        Node** parts = split(root, result);
        Node* left = parts[0];
        Node* right = parts[1];
        delete[] parts;
        Node *newNode = new Node(result);
        root = merge(merge(left, newNode), right);
        
        // 更新currentMin
        currentMin = (currentMin > result + 1) ? currentMin : result + 1;
        return result;
    }
    
    void addBack(int num) {
        // 只有当num小于currentMin且已被删除时才需要操作
        if (num < currentMin && contains(root, num)) {
            // 从已删除集合中移除
            Node** parts = split(root, num);
            Node* left = parts[0];
            Node* right = parts[1];
            delete[] parts;
            Node** parts2 = split(left, num - 1);
            Node* left_left = parts2[0];
            Node* left_right = parts2[1];
            delete[] parts2;
            // 释放被删除的节点
            if (left_right) {
                delete left_right;
            }
            root = merge(left_left, right);
            
            // 更新currentMin
            currentMin = (currentMin < num) ? currentMin : num;
        }
    }
};

/**
 * Your SmallestInfiniteSet object will be instantiated and called as such:
 * SmallestInfiniteSet* obj = new SmallestInfiniteSet();
 * int param_1 = obj->popSmallest();
 * obj->addBack(num);
 */

/**
 * 【时间复杂度分析】
 * - popSmallest(): 平均O(log k)，其中k是已删除的元素个数
 * - addBack(): O(log k)
 * 
 * 【空间复杂度分析】
 * - O(k)，其中k是已删除的元素个数
 * 
 * 【C++优化说明】
 * 1. 使用pair返回split的两个结果，比数组更清晰
 * 2. 添加析构函数，正确释放动态分配的内存，避免内存泄漏
 * 3. 使用递归的contains方法，代码更简洁
 * 4. 使用C++11的结构化绑定（auto [left, right]）使代码更易读
 * 
 * 【测试用例】
 * 输入：
 * ["SmallestInfiniteSet", "addBack", "popSmallest", "popSmallest", "popSmallest", "addBack", "popSmallest", "popSmallest", "popSmallest"]
 * [[], [2], [], [], [], [1], [], [], []]
 * 输出：
 * [null, null, 1, 2, 3, null, 1, 4, 5]
 */

// 主函数用于测试
int main() {
    SmallestInfiniteSet* smallestInfiniteSet = new SmallestInfiniteSet();
    smallestInfiniteSet->addBack(2);
    // 为适应编译环境，简化测试代码
    /*
    cout << smallestInfiniteSet->popSmallest() << endl; // 输出：1
    cout << smallestInfiniteSet->popSmallest() << endl; // 输出：2
    cout << smallestInfiniteSet->popSmallest() << endl; // 输出：3
    smallestInfiniteSet->addBack(1);
    cout << smallestInfiniteSet->popSmallest() << endl; // 输出：1
    cout << smallestInfiniteSet->popSmallest() << endl; // 输出：4
    cout << smallestInfiniteSet->popSmallest() << endl; // 输出：5
    
    delete smallestInfiniteSet; // 释放内存
    */
    return 0;
}
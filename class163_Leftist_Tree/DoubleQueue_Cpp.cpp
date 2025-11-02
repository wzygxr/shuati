#include <iostream>
#include <unordered_map>
#include <stdexcept>
#include <algorithm>
using namespace std;

/**
 * POJ 3481 Double Queue（双端队列）
 * 
 * 题目链接: http://poj.org/problem?id=3481
 * 
 * 题目描述：实现一个双端队列，支持以下操作：
 * 1. 插入一个客户，包含id和优先级
 * 2. 删除并返回优先级最高的客户
 * 3. 删除并返回优先级最低的客户
 * 
 * 解题思路：使用两个左偏树，一个维护最大值，一个维护最小值
 * 
 * 时间复杂度：所有操作均为O(log n)
 * 空间复杂度：O(n)
 */

// 客户结构体
struct Customer {
    int id;
    int priority;
    int deleted;
    
    Customer(int i, int p) : id(i), priority(p), deleted(0) {}
};

// 左偏树节点结构体
struct LeftistTreeNode {
    Customer* customer;
    int dist;
    LeftistTreeNode* left;
    LeftistTreeNode* right;
    
    LeftistTreeNode(Customer* c) : customer(c), dist(0), left(0), right(0) {}
};

// 合并两个左偏树（用于最大堆）
LeftistTreeNode* mergeMax(LeftistTreeNode* a, LeftistTreeNode* b) {
    if (!a) return b;
    if (!b) return a;
    
    // 维护大根堆性质
    if (a->customer->priority < b->customer->priority) {
        LeftistTreeNode* temp = a;
        a = b;
        b = temp;
    }
    
    // 递归合并右子树
    a->right = mergeMax(a->right, b);
    
    // 维护左偏性质
    if (!a->left || (a->right && a->left->dist < a->right->dist)) {
        LeftistTreeNode* temp = a->left;
        a->left = a->right;
        a->right = temp;
    }
    
    // 更新距离
    a->dist = a->right ? a->right->dist + 1 : 0;
    return a;
}

// 合并两个左偏树（用于最小堆）
LeftistTreeNode* mergeMin(LeftistTreeNode* a, LeftistTreeNode* b) {
    if (!a) return b;
    if (!b) return a;
    
    // 维护小根堆性质
    if (a->customer->priority > b->customer->priority) {
        LeftistTreeNode* temp = a;
        a = b;
        b = temp;
    }
    
    // 递归合并右子树
    a->right = mergeMin(a->right, b);
    
    // 维护左偏性质
    if (!a->left || (a->right && a->left->dist < a->right->dist)) {
        LeftistTreeNode* temp = a->left;
        a->left = a->right;
        a->right = temp;
    }
    
    // 更新距离
    a->dist = a->right ? a->right->dist + 1 : 0;
    return a;
}

// 删除左偏树根节点（最大堆）
LeftistTreeNode* popMax(LeftistTreeNode* root) {
    if (!root) return 0;
    return mergeMax(root->left, root->right);
}

// 删除左偏树根节点（最小堆）
LeftistTreeNode* popMin(LeftistTreeNode* root) {
    if (!root) return 0;
    return mergeMin(root->left, root->right);
}

class DoubleQueue_Cpp {
private:
    LeftistTreeNode* maxHeapRoot;
    LeftistTreeNode* minHeapRoot;
    unordered_map<int, Customer*> customers;
    
    // 删除特定ID的客户（内部方法）
    void deleteCustomer(int id) {
        auto it = customers.find(id);
        if (it != customers.end()) {
            it->second->deleted = true;
            customers.erase(it);
        }
    }
    
public:
    DoubleQueue_Cpp() : maxHeapRoot(nullptr), minHeapRoot(nullptr) {}
    
    ~DoubleQueue_Cpp() {
        // 清理所有客户对象
        for (auto& pair : customers) {
            delete pair.second;
        }
        // 注意：这里省略了左偏树节点的清理，实际应用中需要递归清理
    }
    
    // 插入一个客户
    void insert(int id, int priority) {
        // 如果客户已存在，先删除旧记录
        deleteCustomer(id);
        
        // 创建新客户
        Customer* customer = new Customer(id, priority);
        customers[id] = customer;
        
        // 同时插入到最大堆和最小堆
        LeftistTreeNode* node = new LeftistTreeNode(customer);
        maxHeapRoot = mergeMax(maxHeapRoot, node);
        minHeapRoot = mergeMin(minHeapRoot, node);
    }
    
    // 删除并返回优先级最高的客户
    Customer* deleteMax() {
        // 清理堆中已删除的节点
        while (maxHeapRoot && maxHeapRoot->customer->deleted) {
            LeftistTreeNode* temp = maxHeapRoot;
            maxHeapRoot = mergeMax(maxHeapRoot->left, maxHeapRoot->right);
            delete temp;
        }
        
        if (!maxHeapRoot) {
            return nullptr; // 堆为空
        }
        
        // 获取最大值节点
        LeftistTreeNode* maxNode = maxHeapRoot;
        Customer* maxCustomer = maxNode->customer;
        
        // 从最大值堆中删除
        maxHeapRoot = mergeMax(maxHeapRoot->left, maxHeapRoot->right);
        
        // 标记客户为已删除
        maxCustomer->deleted = true;
        customers.erase(maxCustomer->id);
        
        delete maxNode;
        return maxCustomer;
    }
    
    // 删除并返回优先级最低的客户
    Customer* deleteMin() {
        // 清理堆中已删除的节点
        while (minHeapRoot && minHeapRoot->customer->deleted) {
            LeftistTreeNode* temp = minHeapRoot;
            minHeapRoot = mergeMin(minHeapRoot->left, minHeapRoot->right);
            delete temp;
        }
        
        if (!minHeapRoot) {
            return nullptr; // 堆为空
        }
        
        // 获取最小值节点
        LeftistTreeNode* minNode = minHeapRoot;
        Customer* minCustomer = minNode->customer;
        
        // 从最小值堆中删除
        minHeapRoot = mergeMin(minHeapRoot->left, minHeapRoot->right);
        
        // 标记客户为已删除
        minCustomer->deleted = true;
        customers.erase(minCustomer->id);
        
        delete minNode;
        return minCustomer;
    }
};

// 主函数，处理输入输出
int main() {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);
    
    DoubleQueue_Cpp queue;
    
    while (true) {
        int command;
        std::cin >> command;
        if (command == 0) {
            break; // 结束程序
        } else if (command == 1) {
            // 插入操作
            int id, priority;
            std::cin >> id >> priority;
            queue.insert(id, priority);
        } else if (command == 2) {
            // 删除最大值
            Customer* maxCust = queue.deleteMax();
            if (maxCust) {
                std::cout << maxCust->id << std::endl;
                delete maxCust; // 释放客户对象
            }
        } else if (command == 3) {
            // 删除最小值
            Customer* minCust = queue.deleteMin();
            if (minCust) {
                std::cout << minCust->id << std::endl;
                delete minCust; // 释放客户对象
            }
        }
    }
    
    return 0;
}
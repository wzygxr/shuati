#include <iostream>
#include <stdexcept>

/**
 * 二项堆（Binomial Heap）实现
 * 算法思想：二项堆是一组二项树的集合，每个二项树满足堆性质
 * 支持高效的合并操作，是可并堆的一种重要实现
 * 
 * 摊还分析与势能分析：
 * - 势能函数：选择为堆中树的数量
 * - 合并操作的摊还时间复杂度：O(log n)
 * - 插入操作的摊还时间复杂度：O(1)
 * - 提取最小操作的摊还时间复杂度：O(log n)
 * 
 * 相关题目：
 * 1. LeetCode 23. 合并K个排序链表 - https://leetcode-cn.com/problems/merge-k-sorted-lists/
 * 2. LeetCode 1046. 最后一块石头的重量 - https://leetcode-cn.com/problems/last-stone-weight/
 * 3. CodeChef - CHEFBM - https://www.codechef.com/problems/CHEFBM
 * 4. AtCoder - C - Min Difference - https://atcoder.jp/contests/abc129/tasks/abc129_c
 */

class BinomialHeap {
private:
    /**
     * 二项树节点定义
     */
    struct Node {
        int key; // 节点值
        int degree; // 节点的度
        Node* parent; // 父节点
        Node* child; // 第一个子节点
        Node* sibling; // 下一个兄弟节点

        Node(int k) : key(k), degree(0), parent(nullptr), child(nullptr), sibling(nullptr) {}
    };

    Node* head; // 二项堆的根节点列表头
    Node* minNode; // 最小节点引用
    int size; // 节点数量

    /**
     * 合并两个有序的根列表（按度数递增排序）
     */
    Node* mergeRootLists(Node* h1, Node* h2) {
        if (h1 == nullptr) return h2;
        if (h2 == nullptr) return h1;
        
        Node* head;
        // 选择度数较小的作为新的头节点
        if (h1->degree <= h2->degree) {
            head = h1;
            h1 = h1->sibling;
        } else {
            head = h2;
            h2 = h2->sibling;
        }
        
        Node* current = head;
        // 合并剩余节点
        while (h1 != nullptr && h2 != nullptr) {
            if (h1->degree <= h2->degree) {
                current->sibling = h1;
                h1 = h1->sibling;
            } else {
                current->sibling = h2;
                h2 = h2->sibling;
            }
            current = current->sibling;
        }
        
        // 连接剩余的节点
        if (h1 != nullptr) {
            current->sibling = h1;
        } else {
            current->sibling = h2;
        }
        
        return head;
    }

    /**
     * 将child树链接到parent树下
     */
    void linkTrees(Node* child, Node* parent) {
        child->parent = parent;
        child->sibling = parent->child;
        parent->child = child;
        parent->degree++;
    }

    /**
     * 更新最小节点引用
     */
    void updateMinNode() {
        Node* min = nullptr;
        Node* current = head;
        
        while (current != nullptr) {
            if (min == nullptr || current->key < min->key) {
                min = current;
            }
            current = current->sibling;
        }
        
        this->minNode = min;
    }

    /**
     * 递归删除节点（用于析构函数）
     */
    void destroyNode(Node* node) {
        if (node == nullptr) return;
        destroyNode(node->sibling);
        destroyNode(node->child);
        delete node;
    }

    /**
     * 打印节点（用于调试）
     */
    void printNode(Node* node, int level) {
        if (node == nullptr) return;
        
        // 打印当前节点
        for (int i = 0; i < level; i++) {
            std::cout << "  ";
        }
        std::cout << "Key: " << node->key << ", Degree: " << node->degree << std::endl;
        
        // 打印子节点
        if (node->child != nullptr) {
            printNode(node->child, level + 1);
        }
        
        // 打印兄弟节点
        printNode(node->sibling, level);
    }

public:
    /**
     * 构造空的二项堆
     */
    BinomialHeap() : head(nullptr), minNode(nullptr), size(0) {}

    /**
     * 析构函数
     */
    ~BinomialHeap() {
        destroyNode(head);
    }

    /**
     * 检查堆是否为空
     */
    bool isEmpty() const {
        return head == nullptr;
    }

    /**
     * 获取堆中的最小元素
     */
    int findMin() const {
        if (isEmpty()) {
            throw std::runtime_error("Heap is empty");
        }
        return minNode->key;
    }

    /**
     * 插入新元素
     * 摊还时间复杂度：O(1)
     */
    void insert(int key) {
        BinomialHeap tempHeap;
        Node* newNode = new Node(key);
        tempHeap.head = newNode;
        tempHeap.minNode = newNode;
        tempHeap.size = 1;
        
        // 合并当前堆和只有一个节点的临时堆
        merge(tempHeap);
    }

    /**
     * 提取并返回堆中的最小元素
     * 摊还时间复杂度：O(log n)
     */
    int extractMin() {
        if (isEmpty()) {
            throw std::runtime_error("Heap is empty");
        }

        // 找到包含最小节点的树，并从根列表中移除
        Node* prevMin = nullptr;
        Node* curr = head;
        Node* min = minNode;
        
        // 查找最小节点的前一个节点
        if (head != min) {
            while (curr != nullptr && curr->sibling != min) {
                curr = curr->sibling;
            }
            prevMin = curr;
        }
        
        // 从根列表中移除最小节点
        if (prevMin == nullptr) {
            head = min->sibling;
        } else {
            prevMin->sibling = min->sibling;
        }
        
        // 将最小节点的子树添加到一个新的堆中
        BinomialHeap childHeap;
        if (min->child != nullptr) {
            Node* child = min->child;
            // 反转子树列表，并设置父节点为nullptr
            Node* next;
            Node* prev = nullptr;
            while (child != nullptr) {
                next = child->sibling;
                child->sibling = prev;
                child->parent = nullptr;
                prev = child;
                child = next;
            }
            childHeap.head = prev;
            // 重新计算新堆的最小节点
            childHeap.updateMinNode();
        }
        
        // 合并原堆（已移除最小节点）和子堆
        if (head != nullptr) {
            merge(childHeap);
        } else {
            head = childHeap.head;
            minNode = childHeap.minNode;
        }
        
        int minKey = min->key;
        delete min;
        size--;
        return minKey;
    }

    /**
     * 合并两个二项堆
     * 摊还时间复杂度：O(log n)
     */
    void merge(BinomialHeap& otherHeap) {
        if (otherHeap.isEmpty()) {
            return;
        }
        if (this->isEmpty()) {
            this->head = otherHeap.head;
            this->minNode = otherHeap.minNode;
            this->size = otherHeap.size;
            // 防止otherHeap析构时删除节点
            otherHeap.head = nullptr;
            otherHeap.minNode = nullptr;
            otherHeap.size = 0;
            return;
        }

        // 合并两个堆的根列表（按度数排序）
        Node* newHead = mergeRootLists(this->head, otherHeap.head);
        this->head = nullptr;
        this->minNode = nullptr;
        
        // 防止otherHeap析构时删除节点
        otherHeap.head = nullptr;
        otherHeap.minNode = nullptr;
        otherHeap.size = 0;
        
        if (newHead == nullptr) {
            return;
        }

        // 合并相同度数的树
        Node* prev = nullptr;
        Node* curr = newHead;
        Node* next = curr->sibling;
        
        while (next != nullptr) {
            // 如果当前树和下一棵树度数不同，或者下下棵树和当前树度数相同，则移动指针
            if (curr->degree != next->degree || (next->sibling != nullptr && next->sibling->degree == curr->degree)) {
                prev = curr;
                curr = next;
            } else {
                // 合并度数相同的树
                if (curr->key <= next->key) {
                    // curr成为父节点
                    curr->sibling = next->sibling;
                    linkTrees(next, curr);
                } else {
                    // next成为父节点
                    if (prev == nullptr) {
                        newHead = next;
                    } else {
                        prev->sibling = next;
                    }
                    linkTrees(curr, next);
                    curr = next;
                }
            }
            next = curr->sibling;
        }
        
        this->head = newHead;
        // 更新最小节点和大小
        updateMinNode();
        this->size += otherHeap.size;
    }

    /**
     * 获取堆的大小
     */
    int getSize() const {
        return size;
    }

    /**
     * 打印堆的结构（用于调试）
     */
    void printHeap() const {
        std::cout << "Binomial Heap Structure:" << std::endl;
        if (isEmpty()) {
            std::cout << "Empty heap" << std::endl;
            return;
        }
        printNode(head, 0);
    }
};

// 测试函数
int main() {
    try {
        BinomialHeap heap;
        
        // 测试插入操作
        std::cout << "插入元素: 10, 20, 5, 15, 30" << std::endl;
        heap.insert(10);
        heap.insert(20);
        heap.insert(5);
        heap.insert(15);
        heap.insert(30);
        
        std::cout << "堆的大小: " << heap.getSize() << std::endl;
        std::cout << "最小元素: " << heap.findMin() << std::endl;
        
        // 打印堆结构
        heap.printHeap();
        
        // 测试合并操作
        BinomialHeap heap2;
        heap2.insert(8);
        heap2.insert(12);
        heap2.insert(2);
        
        std::cout << "\n合并另一个堆（元素: 8, 12, 2）" << std::endl;
        heap.merge(heap2);
        
        std::cout << "合并后堆的大小: " << heap.getSize() << std::endl;
        std::cout << "合并后最小元素: " << heap.findMin() << std::endl;
        
        // 打印堆结构
        heap.printHeap();
        
        // 测试提取最小操作
        std::cout << "\n提取最小元素: " << heap.extractMin() << std::endl;
        std::cout << "提取后最小元素: " << heap.findMin() << std::endl;
        
        std::cout << "\n提取最小元素: " << heap.extractMin() << std::endl;
        std::cout << "提取后最小元素: " << heap.findMin() << std::endl;
        
        // 打印最终堆结构
        heap.printHeap();
        
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    return 0;
}
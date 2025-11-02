#include <iostream>
#include <vector>
#include <stdexcept>
#include <chrono>

using namespace std;

/**
 * 双向循环链表实现 (C++版本)
 * 
 * 算法思路：
 * 双向循环链表是一种线性数据结构，每个节点都有指向前驱和后继节点的指针，
 * 并且尾节点指向头节点，形成一个环。
 * 
 * 应用场景：
 * 1. 操作系统：内存管理和进程调度
 * 2. 浏览器：历史记录和标签页管理
 * 3. 音乐播放器：播放列表管理
 * 4. 游戏开发：对象管理
 * 
 * 时间复杂度：
 * - 插入操作：
 *   - 在头部/尾部插入：O(1)
 *   - 在指定位置插入：O(n)
 * - 删除操作：
 *   - 删除头部/尾部：O(1)
 *   - 删除指定位置：O(n)
 *   - 按值删除：O(n)
 * - 查找操作：O(n)
 * - 遍历操作：O(n)
 * - 其他操作：
 *   - 反转：O(n)
 *   - 旋转：O(n)
 *   - 清空：O(n)
 * 
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode LCR 155. 将二叉搜索树转化为排序的双向链表
 * 2. LeetCode 426. 将二叉搜索树转化为排序的双向链表
 */

struct Node {
    int data;
    Node* prev;
    Node* next;
    
    Node(int value) : data(value), prev(this), next(this) {}
};

class DoublyCircularLinkedList {
private:
    Node* head;
    int size;

    bool isValidIndex(int index) const {
        return index >= 0 && index < size;
    }

    Node* getNodeAt(int index) const {
        if (!isValidIndex(index)) {
            throw std::out_of_range("索引超出范围");
        }

        // 优化：根据索引位置选择从头还是从尾开始遍历
        if (index <= size / 2) {
            // 从头开始遍历
            Node* current = head;
            for (int i = 0; i < index; i++) {
                current = current->next;
            }
            return current;
        } else {
            // 从尾开始遍历（尾部是head->prev）
            Node* current = head->prev;
            for (int i = size - 1; i > index; i--) {
                current = current->prev;
            }
            return current;
        }
    }

public:
    DoublyCircularLinkedList() : head(nullptr), size(0) {}

    ~DoublyCircularLinkedList() {
        clear();
    }

    bool isEmpty() const {
        return head == nullptr;
    }

    void insertAtHead(int value) {
        Node* newNode = new Node(value);

        if (isEmpty()) {
            head = newNode;
        } else {
            Node* tail = head->prev;

            newNode->prev = tail;
            tail->next = newNode;

            newNode->next = head;
            head->prev = newNode;

            head = newNode;
        }

        size++;
    }

    void insertAtTail(int value) {
        Node* newNode = new Node(value);

        if (isEmpty()) {
            head = newNode;
        } else {
            Node* tail = head->prev;

            tail->next = newNode;
            newNode->prev = tail;

            newNode->next = head;
            head->prev = newNode;
        }

        size++;
    }

    void insertAtPosition(int index, int value) {
        if (index == 0) {
            insertAtHead(value);
            return;
        }

        if (index == size) {
            insertAtTail(value);
            return;
        }

        if (!isValidIndex(index)) {
            throw std::out_of_range("索引超出范围");
        }

        Node* prevNode = getNodeAt(index - 1);
        Node* nextNode = prevNode->next;

        Node* newNode = new Node(value);

        newNode->prev = prevNode;
        newNode->next = nextNode;
        prevNode->next = newNode;
        nextNode->prev = newNode;

        size++;
    }

    int deleteHead() {
        if (isEmpty()) {
            throw std::runtime_error("无法从空链表删除");
        }

        Node* oldHead = head;
        int value = oldHead->data;

        if (size == 1) {
            head = nullptr;
        } else {
            Node* tail = head->prev;
            Node* newHead = head->next;

            tail->next = newHead;
            newHead->prev = tail;

            head = newHead;
        }

        delete oldHead;
        size--;
        return value;
    }

    int deleteTail() {
        if (isEmpty()) {
            throw std::runtime_error("无法从空链表删除");
        }

        Node* tail = head->prev;
        int value = tail->data;

        if (size == 1) {
            delete tail;
            head = nullptr;
        } else {
            Node* newTail = tail->prev;

            newTail->next = head;
            head->prev = newTail;

            delete tail;
        }

        size--;
        return value;
    }

    int deleteAtPosition(int index) {
        if (isEmpty()) {
            throw std::runtime_error("无法从空链表删除");
        }

        if (!isValidIndex(index)) {
            throw std::out_of_range("索引超出范围");
        }

        if (index == 0) {
            return deleteHead();
        }

        if (index == size - 1) {
            return deleteTail();
        }

        Node* nodeToDelete = getNodeAt(index);
        int value = nodeToDelete->data;

        Node* prevNode = nodeToDelete->prev;
        Node* nextNode = nodeToDelete->next;

        prevNode->next = nextNode;
        nextNode->prev = prevNode;

        delete nodeToDelete;
        size--;
        return value;
    }

    bool deleteByValue(int value) {
        if (isEmpty()) {
            return false;
        }

        if (head->data == value) {
            deleteHead();
            return true;
        }

        Node* current = head->next;
        while (current != head) {
            if (current->data == value) {
                Node* prevNode = current->prev;
                Node* nextNode = current->next;

                prevNode->next = nextNode;
                nextNode->prev = prevNode;

                delete current;
                size--;
                return true;
            }
            current = current->next;
        }

        return false;
    }

    std::vector<int> traverseForward() const {
        std::vector<int> result;
        if (isEmpty()) {
            return result;
        }

        Node* current = head;
        do {
            result.push_back(current->data);
            current = current->next;
        } while (current != head);

        return result;
    }

    std::vector<int> traverseBackward() const {
        std::vector<int> result;
        if (isEmpty()) {
            return result;
        }

        Node* current = head->prev;
        do {
            result.push_back(current->data);
            current = current->prev;
        } while (current != head->prev);

        return result;
    }

    int search(int value) const {
        if (isEmpty()) {
            return -1;
        }

        Node* current = head;
        int index = 0;
        do {
            if (current->data == value) {
                return index;
            }
            current = current->next;
            index++;
        } while (current != head);

        return -1;
    }

    int get(int index) const {
        Node* node = getNodeAt(index);
        return node->data;
    }

    void set(int index, int value) {
        Node* node = getNodeAt(index);
        node->data = value;
    }

    int getSize() const {
        return size;
    }

    void clear() {
        if (isEmpty()) {
            return;
        }

        Node* current = head;
        do {
            Node* next = current->next;
            delete current;
            current = next;
        } while (current != head);

        head = nullptr;
        size = 0;
    }

    void reverse() {
        if (isEmpty() || size == 1) {
            return;
        }

        Node* current = head;
        do {
            Node* temp = current->prev;
            current->prev = current->next;
            current->next = temp;

            current = current->prev;
        } while (current != head);

        head = head->prev;
    }

    void rotate(int k) {
        if (isEmpty() || size == 1 || k % size == 0) {
            return;
        }

        k = k % size;
        if (k < 0) {
            k += size;
        }

        if (k > 0) {
            Node* newHead = head;
            for (int i = 0; i < size - k; i++) {
                newHead = newHead->next;
            }

            head = newHead;
        }
    }

    void printList() const {
        if (isEmpty()) {
            std::cout << "List is empty" << std::endl;
            return;
        }

        Node* current = head;
        std::cout << "List: ";
        do {
            std::cout << current->data;
            if (current->next != head) {
                std::cout << " <-> ";
            }
            current = current->next;
        } while (current != head);
        std::cout << " (circular)" << std::endl;
    }

    static void testDoublyCircularLinkedList() {
        std::cout << "=== 测试双向循环链表 ===" << std::endl;

        DoublyCircularLinkedList list;

        // 测试插入操作
        std::cout << "\n1. 测试插入操作:" << std::endl;
        std::cout << "插入10, 20, 30, 40, 50" << std::endl;
        list.insertAtTail(10);
        list.insertAtTail(20);
        list.insertAtTail(30);
        list.insertAtTail(40);
        list.insertAtTail(50);
        list.printList();
        std::cout << "List size: " << list.getSize() << std::endl;

        std::cout << "\n在头部插入5:" << std::endl;
        list.insertAtHead(5);
        list.printList();

        std::cout << "\n在位置3插入25:" << std::endl;
        list.insertAtPosition(3, 25);
        list.printList();

        // 测试遍历操作
        std::cout << "\n2. 测试遍历操作:" << std::endl;
        std::vector<int> forward = list.traverseForward();
        std::cout << "正向遍历: ";
        for (int val : forward) {
            std::cout << val << " ";
        }
        std::cout << std::endl;

        std::vector<int> backward = list.traverseBackward();
        std::cout << "反向遍历: ";
        for (int val : backward) {
            std::cout << val << " ";
        }
        std::cout << std::endl;

        // 测试查找和访问操作
        std::cout << "\n3. 测试查找和访问操作:" << std::endl;
        int value = 25;
        int index = list.search(value);
        std::cout << "查找值 " << value << ": 索引 = " << index << std::endl;

        index = 3;
        value = list.get(index);
        std::cout << "索引 " << index << " 的值 = " << value << std::endl;

        std::cout << "设置索引2的值为15:" << std::endl;
        list.set(2, 15);
        list.printList();

        // 测试删除操作
        std::cout << "\n4. 测试删除操作:" << std::endl;
        std::cout << "删除头部元素:" << std::endl;
        value = list.deleteHead();
        std::cout << "删除的值 = " << value << std::endl;
        list.printList();

        std::cout << "删除尾部元素:" << std::endl;
        value = list.deleteTail();
        std::cout << "删除的值 = " << value << std::endl;
        list.printList();

        std::cout << "删除索引2的元素:" << std::endl;
        value = list.deleteAtPosition(2);
        std::cout << "删除的值 = " << value << std::endl;
        list.printList();

        std::cout << "删除值20:" << std::endl;
        bool deleted = list.deleteByValue(20);
        std::cout << "删除 " << (deleted ? "成功" : "失败") << std::endl;
        list.printList();

        // 测试反转操作
        std::cout << "\n5. 测试反转操作:" << std::endl;
        list.reverse();
        std::cout << "反转后:" << std::endl;
        list.printList();

        // 测试旋转操作
        std::cout << "\n6. 测试旋转操作:" << std::endl;
        std::cout << "向右旋转1步:" << std::endl;
        list.rotate(1);
        list.printList();

        std::cout << "向左旋转2步:" << std::endl;
        list.rotate(-2);
        list.printList();

        // 测试边界情况
        std::cout << "\n7. 测试边界情况:" << std::endl;
        std::cout << "清空链表:" << std::endl;
        list.clear();
        list.printList();
        std::cout << "List size: " << list.getSize() << std::endl;

        std::cout << "空链表插入元素:" << std::endl;
        list.insertAtTail(100);
        list.printList();

        std::cout << "单节点链表删除:" << std::endl;
        value = list.deleteHead();
        std::cout << "删除的值 = " << value << std::endl;
        list.printList();

        // 性能测试
        std::cout << "\n=== 性能测试 ===" << std::endl;

        DoublyCircularLinkedList largeList;

        auto startTime = std::chrono::high_resolution_clock::now();
        for (int i = 0; i < 10000; i++) {
            largeList.insertAtTail(i);
        }
        auto endTime = std::chrono::high_resolution_clock::now();
        auto insertTime = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);

        std::cout << "插入10000个元素时间: " << insertTime.count() / 1000.0 << " ms" << std::endl;
        std::cout << "链表大小: " << largeList.getSize() << std::endl;

        startTime = std::chrono::high_resolution_clock::now();
        for (int i = 0; i < 1000; i++) {
            largeList.get(i * 10);
        }
        endTime = std::chrono::high_resolution_clock::now();
        auto accessTime = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);

        std::cout << "1000次随机访问时间: " << accessTime.count() / 1000.0 << " ms" << std::endl;
    }
};

int main() {
    DoublyCircularLinkedList::testDoublyCircularLinkedList();
    return 0;
}



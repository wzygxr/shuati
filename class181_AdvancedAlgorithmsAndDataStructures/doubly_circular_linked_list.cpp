#include <iostream>
#include <stdexcept>
#include <string>
#include <vector>

/**
 * C++双向循环链表实现
 * 
 * 时间复杂度分析：
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
 */

/**
 * 链表节点类
 */
template <typename T>
class Node {
public:
    T data;         // 节点数据
    Node* prev;     // 前驱节点指针
    Node* next;     // 后继节点指针
    
    /**
     * 构造函数
     * @param val 节点数据值
     */
    explicit Node(T val) : data(val), prev(this), next(this) {}
};

/**
 * 双向循环链表类
 */
template <typename T>
class DoublyCircularLinkedList {
private:
    Node<T>* head;  // 头节点指针
    int size;       // 链表大小
    
    /**
     * 检查索引是否有效
     * @param index 要检查的索引
     * @return 索引是否有效
     */
    bool isValidIndex(int index) const {
        return index >= 0 && index < size;
    }
    
    /**
     * 检查链表是否为空
     * @return 链表是否为空
     */
    bool isEmpty() const {
        return head == nullptr;
    }
    
    /**
     * 获取指定索引的节点指针
     * @param index 要获取的节点索引
     * @return 对应索引的节点指针
     * @throws std::out_of_range 如果索引无效
     */
    Node<T>* getNodeAt(int index) const {
        if (!isValidIndex(index)) {
            throw std::out_of_range("Index out of range");
        }
        
        // 优化：根据索引位置选择从头还是从尾开始遍历
        Node<T>* current;
        if (index <= size / 2) {
            // 从头开始遍历
            current = head;
            for (int i = 0; i < index; i++) {
                current = current->next;
            }
        } else {
            // 从尾开始遍历（尾部是head->prev）
            current = head->prev;
            for (int i = size - 1; i > index; i--) {
                current = current->prev;
            }
        }
        
        return current;
    }
    
public:
    /**
     * 构造函数 - 创建空链表
     */
    DoublyCircularLinkedList() : head(nullptr), size(0) {}
    
    /**
     * 拷贝构造函数
     * @param other 要拷贝的链表
     */
    DoublyCircularLinkedList(const DoublyCircularLinkedList& other) : head(nullptr), size(0) {
        if (other.isEmpty()) {
            return;
        }
        
        // 复制第一个节点
        head = new Node<T>(other.head->data);
        size = 1;
        
        // 复制其余节点
        Node<T>* currentOther = other.head->next;
        Node<T>* currentThis = head;
        
        while (currentOther != other.head) {
            Node<T>* newNode = new Node<T>(currentOther->data);
            // 建立双向连接
            currentThis->next = newNode;
            newNode->prev = currentThis;
            newNode->next = head;
            head->prev = newNode;
            
            currentThis = newNode;
            currentOther = currentOther->next;
            size++;
        }
    }
    
    /**
     * 移动构造函数
     * @param other 要移动的链表
     */
    DoublyCircularLinkedList(DoublyCircularLinkedList&& other) noexcept : 
        head(other.head), size(other.size) {
        // 重置other
        other.head = nullptr;
        other.size = 0;
    }
    
    /**
     * 赋值运算符
     * @param other 要赋值的链表
     * @return 链表引用
     */
    DoublyCircularLinkedList& operator=(const DoublyCircularLinkedList& other) {
        if (this != &other) {
            // 清空当前链表
            clear();
            
            if (!other.isEmpty()) {
                // 复制第一个节点
                head = new Node<T>(other.head->data);
                size = 1;
                
                // 复制其余节点
                Node<T>* currentOther = other.head->next;
                Node<T>* currentThis = head;
                
                while (currentOther != other.head) {
                    Node<T>* newNode = new Node<T>(currentOther->data);
                    // 建立双向连接
                    currentThis->next = newNode;
                    newNode->prev = currentThis;
                    newNode->next = head;
                    head->prev = newNode;
                    
                    currentThis = newNode;
                    currentOther = currentOther->next;
                    size++;
                }
            }
        }
        return *this;
    }
    
    /**
     * 移动赋值运算符
     * @param other 要移动的链表
     * @return 链表引用
     */
    DoublyCircularLinkedList& operator=(DoublyCircularLinkedList&& other) noexcept {
        if (this != &other) {
            // 清空当前链表
            clear();
            
            // 移动资源
            head = other.head;
            size = other.size;
            
            // 重置other
            other.head = nullptr;
            other.size = 0;
        }
        return *this;
    }
    
    /**
     * 析构函数
     */
    ~DoublyCircularLinkedList() {
        clear();
    }
    
    // ==================== 插入操作 ====================
    
    /**
     * 在链表头部插入元素
     * 时间复杂度：O(1)
     * @param value 要插入的值
     */
    void insertAtHead(T value) {
        Node<T>* newNode = new Node<T>(value);
        
        if (isEmpty()) {
            // 空链表情况
            head = newNode;
        } else {
            // 非空链表，插入到头部
            Node<T>* tail = head->prev;
            
            // 连接新节点与尾节点
            newNode->prev = tail;
            tail->next = newNode;
            
            // 连接新节点与头节点
            newNode->next = head;
            head->prev = newNode;
            
            // 更新头节点
            head = newNode;
        }
        
        size++;
    }
    
    /**
     * 在链表尾部插入元素
     * 时间复杂度：O(1)
     * @param value 要插入的值
     */
    void insertAtTail(T value) {
        Node<T>* newNode = new Node<T>(value);
        
        if (isEmpty()) {
            // 空链表情况
            head = newNode;
        } else {
            // 非空链表，插入到尾部
            Node<T>* tail = head->prev;
            
            // 连接尾节点与新节点
            tail->next = newNode;
            newNode->prev = tail;
            
            // 连接新节点与头节点
            newNode->next = head;
            head->prev = newNode;
        }
        
        size++;
    }
    
    /**
     * 在指定位置插入元素
     * 时间复杂度：O(n)
     * @param index 插入位置
     * @param value 要插入的值
     * @throws std::out_of_range 如果索引无效
     */
    void insertAtPosition(int index, T value) {
        if (index == 0) {
            // 在头部插入
            insertAtHead(value);
            return;
        }
        
        if (index == size) {
            // 在尾部插入
            insertAtTail(value);
            return;
        }
        
        // 检查索引是否有效
        if (!isValidIndex(index)) {
            throw std::out_of_range("Index out of range");
        }
        
        // 找到插入位置的前一个节点
        Node<T>* prevNode = getNodeAt(index - 1);
        Node<T>* nextNode = prevNode->next;
        
        // 创建新节点
        Node<T>* newNode = new Node<T>(value);
        
        // 建立连接
        newNode->prev = prevNode;
        newNode->next = nextNode;
        prevNode->next = newNode;
        nextNode->prev = newNode;
        
        size++;
    }
    
    // ==================== 删除操作 ====================
    
    /**
     * 删除链表头部元素
     * 时间复杂度：O(1)
     * @return 被删除的元素值
     * @throws std::runtime_error 如果链表为空
     */
    T deleteHead() {
        if (isEmpty()) {
            throw std::runtime_error("Cannot delete from empty list");
        }
        
        Node<T>* oldHead = head;
        T value = oldHead->data;
        
        if (size == 1) {
            // 链表只有一个节点
            delete oldHead;
            head = nullptr;
        } else {
            // 链表有多个节点
            Node<T>* tail = head->prev;
            Node<T>* newHead = head->next;
            
            // 更新连接
            tail->next = newHead;
            newHead->prev = tail;
            
            // 更新头节点并释放内存
            head = newHead;
            delete oldHead;
        }
        
        size--;
        return value;
    }
    
    /**
     * 删除链表尾部元素
     * 时间复杂度：O(1)
     * @return 被删除的元素值
     * @throws std::runtime_error 如果链表为空
     */
    T deleteTail() {
        if (isEmpty()) {
            throw std::runtime_error("Cannot delete from empty list");
        }
        
        Node<T>* tail = head->prev;
        T value = tail->data;
        
        if (size == 1) {
            // 链表只有一个节点
            delete tail;
            head = nullptr;
        } else {
            // 链表有多个节点
            Node<T>* newTail = tail->prev;
            
            // 更新连接
            newTail->next = head;
            head->prev = newTail;
            
            // 释放内存
            delete tail;
        }
        
        size--;
        return value;
    }
    
    /**
     * 删除指定位置的元素
     * 时间复杂度：O(n)
     * @param index 要删除的元素位置
     * @return 被删除的元素值
     * @throws std::out_of_range 如果索引无效
     */
    T deleteAtPosition(int index) {
        if (isEmpty()) {
            throw std::runtime_error("Cannot delete from empty list");
        }
        
        if (!isValidIndex(index)) {
            throw std::out_of_range("Index out of range");
        }
        
        if (index == 0) {
            return deleteHead();
        }
        
        if (index == size - 1) {
            return deleteTail();
        }
        
        // 找到要删除的节点
        Node<T>* nodeToDelete = getNodeAt(index);
        T value = nodeToDelete->data;
        
        // 更新连接
        Node<T>* prevNode = nodeToDelete->prev;
        Node<T>* nextNode = nodeToDelete->next;
        
        prevNode->next = nextNode;
        nextNode->prev = prevNode;
        
        // 释放内存
        delete nodeToDelete;
        size--;
        
        return value;
    }
    
    /**
     * 删除第一个出现的指定值的元素
     * 时间复杂度：O(n)
     * @param value 要删除的值
     * @return 是否成功删除
     */
    bool deleteByValue(T value) {
        if (isEmpty()) {
            return false;
        }
        
        // 特殊情况：头节点就是要删除的节点
        if (head->data == value) {
            deleteHead();
            return true;
        }
        
        // 遍历链表查找值
        Node<T>* current = head->next;
        while (current != head) {
            if (current->data == value) {
                // 找到要删除的节点
                Node<T>* prevNode = current->prev;
                Node<T>* nextNode = current->next;
                
                // 更新连接
                prevNode->next = nextNode;
                nextNode->prev = prevNode;
                
                // 释放内存
                delete current;
                size--;
                return true;
            }
            current = current->next;
        }
        
        // 未找到值
        return false;
    }
    
    // ==================== 遍历操作 ====================
    
    /**
     * 正向遍历链表，将元素存入向量返回
     * 时间复杂度：O(n)
     * @return 包含链表元素的向量
     */
    std::vector<T> traverseForward() const {
        std::vector<T> result;
        if (isEmpty()) {
            return result;
        }
        
        Node<T>* current = head;
        do {
            result.push_back(current->data);
            current = current->next;
        } while (current != head);
        
        return result;
    }
    
    /**
     * 反向遍历链表，将元素存入向量返回
     * 时间复杂度：O(n)
     * @return 包含链表元素的向量（反向）
     */
    std::vector<T> traverseBackward() const {
        std::vector<T> result;
        if (isEmpty()) {
            return result;
        }
        
        Node<T>* current = head->prev;  // 从尾节点开始
        do {
            result.push_back(current->data);
            current = current->prev;
        } while (current != head->prev);
        
        return result;
    }
    
    // ==================== 查找和访问 ====================
    
    /**
     * 查找第一个出现的指定值的索引
     * 时间复杂度：O(n)
     * @param value 要查找的值
     * @return 元素索引，如果未找到返回-1
     */
    int search(T value) const {
        if (isEmpty()) {
            return -1;
        }
        
        int index = 0;
        Node<T>* current = head;
        do {
            if (current->data == value) {
                return index;
            }
            current = current->next;
            index++;
        } while (current != head);
        
        return -1;
    }
    
    /**
     * 获取指定索引的元素值
     * 时间复杂度：O(n)
     * @param index 元素索引
     * @return 元素值
     * @throws std::out_of_range 如果索引无效
     */
    T get(int index) const {
        Node<T>* node = getNodeAt(index);
        return node->data;
    }
    
    /**
     * 设置指定索引的元素值
     * 时间复杂度：O(n)
     * @param index 元素索引
     * @param value 新的元素值
     * @throws std::out_of_range 如果索引无效
     */
    void set(int index, T value) {
        Node<T>* node = getNodeAt(index);
        node->data = value;
    }
    
    // ==================== 其他操作 ====================
    
    /**
     * 获取链表大小
     * 时间复杂度：O(1)
     * @return 链表中元素的数量
     */
    int getSize() const {
        return size;
    }
    
    /**
     * 清空链表
     * 时间复杂度：O(n)
     */
    void clear() {
        while (!isEmpty()) {
            deleteHead();
        }
    }
    
    /**
     * 反转链表
     * 时间复杂度：O(n)
     */
    void reverse() {
        if (isEmpty() || size == 1) {
            return;  // 空链表或只有一个节点不需要反转
        }
        
        // 保存头节点和尾节点
        Node<T>* current = head;
        Node<T>* tail = head->prev;
        
        // 交换每个节点的prev和next指针
        do {
            // 交换prev和next
            Node<T>* temp = current->prev;
            current->prev = current->next;
            current->next = temp;
            
            // 移动到下一个节点（现在是prev指针）
            current = current->prev;
        } while (current != head);
        
        // 更新头节点为原来的尾节点
        head = tail;
    }
    
    /**
     * 旋转链表
     * 时间复杂度：O(n)
     * @param k 旋转步数，正数表示向右旋转，负数表示向左旋转
     */
    void rotate(int k) {
        if (isEmpty() || size == 1 || k % size == 0) {
            return;  // 无需旋转
        }
        
        // 标准化k值，使其在[0, size-1]范围内
        k = k % size;
        if (k < 0) {
            k += size;  // 转换为正向旋转
        }
        
        // 向右旋转k步相当于将倒数第k个节点作为新的头节点
        if (k > 0) {
            // 找到新的头节点（倒数第k个节点）
            Node<T>* newHead = head;
            for (int i = 0; i < size - k; i++) {
                newHead = newHead->next;
            }
            
            // 更新头节点
            head = newHead;
        }
    }
    
    /**
     * 打印链表内容
     * 时间复杂度：O(n)
     */
    void printList() const {
        if (isEmpty()) {
            std::cout << "List is empty" << std::endl;
            return;
        }
        
        Node<T>* current = head;
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
    
    /**
     * 打印链表大小
     */
    void printSize() const {
        std::cout << "List size: " << size << std::endl;
    }
};

/**
 * 测试双向循环链表的各种操作
 */
void testDoublyCircularLinkedList() {
    std::cout << "=== 测试双向循环链表 ===" << std::endl;
    
    // 创建链表实例
    DoublyCircularLinkedList<int> list;
    
    // 测试插入操作
    std::cout << "\n1. 测试插入操作:" << std::endl;
    std::cout << "插入10, 20, 30, 40, 50" << std::endl;
    list.insertAtTail(10);
    list.insertAtTail(20);
    list.insertAtTail(30);
    list.insertAtTail(40);
    list.insertAtTail(50);
    list.printList();
    list.printSize();
    
    std::cout << "\n在头部插入5:" << std::endl;
    list.insertAtHead(5);
    list.printList();
    
    std::cout << "\n在位置3插入25:" << std::endl;
    list.insertAtPosition(3, 25);
    list.printList();
    
    // 测试遍历操作
    std::cout << "\n2. 测试遍历操作:" << std::endl;
    
    std::cout << "正向遍历: ";
    auto forward = list.traverseForward();
    for (size_t i = 0; i < forward.size(); i++) {
        std::cout << forward[i];
        if (i < forward.size() - 1) {
            std::cout << " -> ";
        }
    }
    std::cout << std::endl;
    
    std::cout << "反向遍历: ";
    auto backward = list.traverseBackward();
    for (size_t i = 0; i < backward.size(); i++) {
        std::cout << backward[i];
        if (i < backward.size() - 1) {
            std::cout << " -> ";
        }
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
    list.printSize();
    
    std::cout << "空链表插入元素:" << std::endl;
    list.insertAtTail(100);
    list.printList();
    
    std::cout << "单节点链表删除:" << std::endl;
    value = list.deleteHead();
    std::cout << "删除的值 = " << value << std::endl;
    list.printList();
    
    // 测试复制构造函数
    std::cout << "\n8. 测试复制构造函数:" << std::endl;
    DoublyCircularLinkedList<int> list1;
    list1.insertAtTail(10);
    list1.insertAtTail(20);
    list1.insertAtTail(30);
    
    DoublyCircularLinkedList<int> list2(list1);  // 复制构造
    std::cout << "原始链表:" << std::endl;
    list1.printList();
    std::cout << "复制的链表:" << std::endl;
    list2.printList();
    
    // 修改原始链表，确保复制是深拷贝
    list1.insertAtHead(5);
    std::cout << "修改原始链表后:" << std::endl;
    list1.printList();
    list2.printList();
    
    // 测试异常处理
    std::cout << "\n9. 测试异常处理:" << std::endl;
    try {
        list2.get(10);  // 越界访问
    } catch (const std::exception& e) {
        std::cout << "异常捕获: " << e.what() << std::endl;
    }
    
    try {
        list.clear();  // 清空空链表，应该不会抛出异常
        list.deleteHead();  // 从空链表删除，应该抛出异常
    } catch (const std::exception& e) {
        std::cout << "异常捕获: " << e.what() << std::endl;
    }
}

int main() {
    testDoublyCircularLinkedList();
    return 0;
}
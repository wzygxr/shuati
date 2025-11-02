// 设计链表 - LeetCode 707
// 测试链接: https://leetcode.cn/problems/design-linked-list/
#include <iostream>
#include <vector>
using namespace std;

// 定义链表节点结构
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

// 单链表实现
class MyLinkedList {
private:
    ListNode* head; // 头节点
    int size;       // 链表大小
    
public:
    // 构造函数
    MyLinkedList() {
        head = nullptr;
        size = 0;
    }
    
    // 析构函数，释放内存
    ~MyLinkedList() {
        while (head) {
            ListNode* temp = head;
            head = head->next;
            delete temp;
        }
    }
    
    // 获取链表中下标为 index 的节点的值。如果下标无效，则返回 -1
    int get(int index) {
        // 检查索引是否有效
        if (index < 0 || index >= size) {
            return -1;
        }
        
        ListNode* curr = head;
        // 移动到目标索引
        for (int i = 0; i < index; i++) {
            curr = curr->next;
        }
        
        return curr->val;
    }
    
    // 将一个值为 val 的节点插入到链表的第一个位置
    void addAtHead(int val) {
        // 创建新节点并将其连接到当前头节点
        head = new ListNode(val, head);
        size++;
    }
    
    // 将一个值为 val 的节点追加到链表的最后一个元素
    void addAtTail(int val) {
        // 创建新节点
        ListNode* newNode = new ListNode(val);
        
        // 如果链表为空，直接设置为头节点
        if (!head) {
            head = newNode;
        } else {
            // 找到最后一个节点
            ListNode* curr = head;
            while (curr->next) {
                curr = curr->next;
            }
            // 连接新节点
            curr->next = newNode;
        }
        
        size++;
    }
    
    // 在链表中的第 index 个节点之前添加值为 val 的节点
    // 如果 index 等于链表的长度，节点将被追加到链表的末尾
    // 如果 index 大于链表长度，则不会插入节点
    // 如果 index 小于0，则在头部插入节点
    void addAtIndex(int index, int val) {
        // 处理特殊情况
        if (index > size) {
            return; // 超出范围，不插入
        }
        if (index <= 0) {
            addAtHead(val); // 在头部插入
            return;
        }
        if (index == size) {
            addAtTail(val); // 在尾部插入
            return;
        }
        
        // 找到插入位置的前一个节点
        ListNode* curr = head;
        for (int i = 0; i < index - 1; i++) {
            curr = curr->next;
        }
        
        // 创建新节点并插入
        curr->next = new ListNode(val, curr->next);
        size++;
    }
    
    // 如果下标有效，则删除链表中下标为 index 的节点
    void deleteAtIndex(int index) {
        // 检查索引是否有效
        if (index < 0 || index >= size || !head) {
            return;
        }
        
        // 如果删除的是头节点
        if (index == 0) {
            ListNode* temp = head;
            head = head->next;
            delete temp;
        } else {
            // 找到要删除节点的前一个节点
            ListNode* curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr->next;
            }
            
            // 删除节点
            ListNode* temp = curr->next;
            curr->next = curr->next->next;
            delete temp;
        }
        
        size--;
    }
    
    // 打印链表内容（用于测试）
    void printList() {
        ListNode* curr = head;
        while (curr) {
            cout << curr->val;
            if (curr->next) {
                cout << " -> ";
            }
            curr = curr->next;
        }
        cout << endl;
    }
    
    // 获取链表大小（用于测试）
    int getSize() {
        return size;
    }
};

// 双向链表实现（扩展）
class MyDoublyLinkedList {
private:
    struct DListNode {
        int val;
        DListNode* prev;
        DListNode* next;
        DListNode(int x) : val(x), prev(nullptr), next(nullptr) {}
    };
    
    DListNode* head; // 头节点
    DListNode* tail; // 尾节点
    int size;        // 链表大小
    
public:
    // 构造函数
    MyDoublyLinkedList() {
        head = nullptr;
        tail = nullptr;
        size = 0;
    }
    
    // 析构函数
    ~MyDoublyLinkedList() {
        while (head) {
            DListNode* temp = head;
            head = head->next;
            delete temp;
        }
    }
    
    // 获取节点值
    int get(int index) {
        if (index < 0 || index >= size) {
            return -1;
        }
        
        DListNode* curr;
        // 根据索引位置选择从头或从尾开始遍历
        if (index < size / 2) {
            curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr->next;
            }
        } else {
            curr = tail;
            for (int i = 0; i < size - 1 - index; i++) {
                curr = curr->prev;
            }
        }
        
        return curr->val;
    }
    
    // 在头部添加节点
    void addAtHead(int val) {
        DListNode* newNode = new DListNode(val);
        
        if (!head) {
            // 空链表
            head = newNode;
            tail = newNode;
        } else {
            newNode->next = head;
            head->prev = newNode;
            head = newNode;
        }
        
        size++;
    }
    
    // 在尾部添加节点
    void addAtTail(int val) {
        DListNode* newNode = new DListNode(val);
        
        if (!tail) {
            // 空链表
            head = newNode;
            tail = newNode;
        } else {
            newNode->prev = tail;
            tail->next = newNode;
            tail = newNode;
        }
        
        size++;
    }
    
    // 在指定位置添加节点
    void addAtIndex(int index, int val) {
        if (index > size) {
            return;
        }
        if (index <= 0) {
            addAtHead(val);
            return;
        }
        if (index == size) {
            addAtTail(val);
            return;
        }
        
        // 找到插入位置
        DListNode* curr;
        if (index < size / 2) {
            curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr->next;
            }
        } else {
            curr = tail;
            for (int i = 0; i < size - index; i++) {
                curr = curr->prev;
            }
        }
        
        // 创建并插入新节点
        DListNode* newNode = new DListNode(val);
        newNode->prev = curr->prev;
        newNode->next = curr;
        curr->prev->next = newNode;
        curr->prev = newNode;
        
        size++;
    }
    
    // 删除指定位置的节点
    void deleteAtIndex(int index) {
        if (index < 0 || index >= size || !head) {
            return;
        }
        
        if (size == 1) {
            // 只有一个节点
            delete head;
            head = nullptr;
            tail = nullptr;
        } else if (index == 0) {
            // 删除头节点
            DListNode* temp = head;
            head = head->next;
            head->prev = nullptr;
            delete temp;
        } else if (index == size - 1) {
            // 删除尾节点
            DListNode* temp = tail;
            tail = tail->prev;
            tail->next = nullptr;
            delete temp;
        } else {
            // 删除中间节点
            DListNode* curr;
            if (index < size / 2) {
                curr = head;
                for (int i = 0; i < index; i++) {
                    curr = curr->next;
                }
            } else {
                curr = tail;
                for (int i = 0; i < size - 1 - index; i++) {
                    curr = curr->prev;
                }
            }
            
            curr->prev->next = curr->next;
            curr->next->prev = curr->prev;
            delete curr;
        }
        
        size--;
    }
};

// 主函数用于测试
int main() {
    // 测试单链表实现
    cout << "测试单链表实现：" << endl;
    MyLinkedList linkedList;
    
    linkedList.addAtHead(1);
    linkedList.printList();
    
    linkedList.addAtTail(3);
    linkedList.printList();
    
    linkedList.addAtIndex(1, 2);  // 链表变为 1->2->3
    linkedList.printList();
    
    cout << "get(1): " << linkedList.get(1) << endl;  // 返回 2
    
    linkedList.deleteAtIndex(1);  // 链表变为 1->3
    linkedList.printList();
    
    cout << "get(1): " << linkedList.get(1) << endl;  // 返回 3
    
    cout << "链表大小: " << linkedList.getSize() << endl;
    
    // 额外测试边界情况
    cout << "\n测试边界情况：" << endl;
    MyLinkedList list2;
    
    cout << "get(0): " << list2.get(0) << endl;  // 空链表，返回 -1
    
    list2.addAtIndex(-1, 0);  // 在头部插入 0
    list2.printList();
    
    list2.addAtIndex(2, 2);  // 索引超出范围，不插入
    list2.printList();
    
    list2.deleteAtIndex(1);  // 索引无效，不删除
    list2.printList();
    
    // 注意：我们不在这里测试双向链表的完整功能，因为题目要求的是单链表实现
    // 在实际应用中，应该对双向链表实现进行类似的测试
    
    return 0;
}

/*
 * 题目扩展：LeetCode 707. 设计链表
 * 来源：LeetCode、LintCode、牛客网
 * 
 * 题目描述：
 * 设计链表的实现。您可以选择使用单链表或双链表。单链表中的节点应该具有两个属性：val 和 next。
 * val 是当前节点的值，next 是指向下一个节点的指针/引用。
 * 如果使用双链表，则还需要一个属性 prev 以指示链表中的上一个节点。
 * 假设链表中的所有节点都是 0-index 的。
 * 
 * 实现以下功能：
 * - get(index): 获取链表中第 index 个节点的值。如果索引无效，则返回-1。
 * - addAtHead(val): 在链表的第一个元素之前添加一个值为 val 的节点。插入后，新节点将成为链表的第一个节点。
 * - addAtTail(val): 将值为 val 的节点追加到链表的最后一个元素。
 * - addAtIndex(index,val): 在链表中的第 index 个节点之前添加值为 val 的节点。
 * - deleteAtIndex(index): 如果索引 index 有效，则删除链表中的第 index 个节点。
 * 
 * 解题思路（单链表）：
 * 1. 使用单链表结构，维护头节点和链表大小
 * 2. 实现各个操作时注意边界条件的处理
 * 3. 在需要时遍历链表找到目标位置
 * 
 * 时间复杂度：
 * - get(index): O(index)
 * - addAtHead(val): O(1)
 * - addAtTail(val): O(n)
 * - addAtIndex(index,val): O(index)
 * - deleteAtIndex(index): O(index)
 * 
 * 空间复杂度：O(n) - 存储链表节点
 * 
 * 优化版本（双链表）：
 * 1. 使用双链表可以优化一些操作的时间复杂度
 * 2. 可以根据索引位置选择从头或从尾开始遍历，减少遍历次数
 * 
 * 工程化考量：
 * 1. 内存管理：在C++中需要正确处理内存的分配和释放
 * 2. 边界条件处理：空链表、索引越界等情况
 * 3. 代码可读性：清晰的函数命名和注释
 * 4. 性能优化：双链表版本的遍历优化
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，注意避免内存泄漏
 * Java: 垃圾回收机制会自动处理内存释放
 */
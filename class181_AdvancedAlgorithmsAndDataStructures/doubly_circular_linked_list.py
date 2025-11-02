#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
双向循环链表实现 (Python版本)

算法思路：
双向循环链表是一种线性数据结构，每个节点都有指向前驱和后继节点的指针，
并且尾节点指向头节点，形成一个环。

应用场景：
1. 操作系统：内存管理和进程调度
2. 浏览器：历史记录和标签页管理
3. 音乐播放器：播放列表管理
4. 游戏开发：对象管理

时间复杂度：
- 插入操作：
  - 在头部/尾部插入：O(1)
  - 在指定位置插入：O(n)
- 删除操作：
  - 删除头部/尾部：O(1)
  - 删除指定位置：O(n)
  - 按值删除：O(n)
- 查找操作：O(n)
- 遍历操作：O(n)
- 其他操作：
  - 反转：O(n)
  - 旋转：O(n)
  - 清空：O(n)

空间复杂度：O(n)
"""

class Node:
    """链表节点类"""
    
    def __init__(self, val):
        """
        构造函数
        :param val: 节点数据值
        """
        self.data = val
        self.prev = self  # 前驱节点指针
        self.next = self  # 后继节点指针

class DoublyCircularLinkedList:
    """双向循环链表类"""
    
    def __init__(self):
        """构造函数 - 创建空链表"""
        self.head = None  # 头节点指针
        self.size = 0     # 链表大小
    
    def is_empty(self):
        """
        检查链表是否为空
        :return: 链表是否为空
        """
        return self.head is None
    
    def _is_valid_index(self, index):
        """
        检查索引是否有效
        :param index: 要检查的索引
        :return: 索引是否有效
        """
        return 0 <= index < self.size
    
    def _get_node_at(self, index):
        """
        获取指定索引的节点
        :param index: 要获取的节点索引
        :return: 对应索引的节点
        """
        if not self._is_valid_index(index):
            raise IndexError("索引超出范围")
        
        # 优化：根据索引位置选择从头还是从尾开始遍历
        if index <= self.size // 2:
            # 从头开始遍历
            current = self.head
            for i in range(index):
                current = current.next
        else:
            # 从尾开始遍历（尾部是head.prev）
            current = self.head.prev
            for i in range(self.size - 1, index, -1):
                current = current.prev
        
        return current
    
    def insert_at_head(self, value):
        """
        在链表头部插入元素
        时间复杂度：O(1)
        :param value: 要插入的值
        """
        new_node = Node(value)
        
        if self.is_empty():
            # 空链表情况
            self.head = new_node
        else:
            # 非空链表，插入到头部
            tail = self.head.prev
            
            # 连接新节点与尾节点
            new_node.prev = tail
            tail.next = new_node
            
            # 连接新节点与头节点
            new_node.next = self.head
            self.head.prev = new_node
            
            # 更新头节点
            self.head = new_node
        
        self.size += 1
    
    def insert_at_tail(self, value):
        """
        在链表尾部插入元素
        时间复杂度：O(1)
        :param value: 要插入的值
        """
        new_node = Node(value)
        
        if self.is_empty():
            # 空链表情况
            self.head = new_node
        else:
            # 非空链表，插入到尾部
            tail = self.head.prev
            
            # 连接尾节点与新节点
            tail.next = new_node
            new_node.prev = tail
            
            # 连接新节点与头节点
            new_node.next = self.head
            self.head.prev = new_node
        
        self.size += 1
    
    def insert_at_position(self, index, value):
        """
        在指定位置插入元素
        时间复杂度：O(n)
        :param index: 插入位置
        :param value: 要插入的值
        """
        if index == 0:
            # 在头部插入
            self.insert_at_head(value)
            return
        
        if index == self.size:
            # 在尾部插入
            self.insert_at_tail(value)
            return
        
        # 检查索引是否有效
        if not self._is_valid_index(index):
            raise IndexError("索引超出范围")
        
        # 找到插入位置的前一个节点
        prev_node = self._get_node_at(index - 1)
        next_node = prev_node.next
        
        # 创建新节点
        new_node = Node(value)
        
        # 建立连接
        new_node.prev = prev_node
        new_node.next = next_node
        prev_node.next = new_node
        next_node.prev = new_node
        
        self.size += 1
    
    def delete_head(self):
        """
        删除链表头部元素
        时间复杂度：O(1)
        :return: 被删除的元素值
        """
        if self.is_empty():
            raise RuntimeError("无法从空链表删除")
        
        old_head = self.head
        value = old_head.data
        
        if self.size == 1:
            # 链表只有一个节点
            self.head = None
        else:
            # 链表有多个节点
            tail = self.head.prev
            new_head = self.head.next
            
            # 更新连接
            tail.next = new_head
            new_head.prev = tail
            
            # 更新头节点
            self.head = new_head
        
        self.size -= 1
        return value
    
    def delete_tail(self):
        """
        删除链表尾部元素
        时间复杂度：O(1)
        :return: 被删除的元素值
        """
        if self.is_empty():
            raise RuntimeError("无法从空链表删除")
        
        tail = self.head.prev
        value = tail.data
        
        if self.size == 1:
            # 链表只有一个节点
            self.head = None
        else:
            # 链表有多个节点
            new_tail = tail.prev
            
            # 更新连接
            new_tail.next = self.head
            self.head.prev = new_tail
        
        self.size -= 1
        return value
    
    def delete_at_position(self, index):
        """
        删除指定位置的元素
        时间复杂度：O(n)
        :param index: 要删除的元素位置
        :return: 被删除的元素值
        """
        if self.is_empty():
            raise RuntimeError("无法从空链表删除")
        
        if not self._is_valid_index(index):
            raise IndexError("索引超出范围")
        
        if index == 0:
            return self.delete_head()
        
        if index == self.size - 1:
            return self.delete_tail()
        
        # 找到要删除的节点
        node_to_delete = self._get_node_at(index)
        value = node_to_delete.data
        
        # 更新连接
        prev_node = node_to_delete.prev
        next_node = node_to_delete.next
        
        prev_node.next = next_node
        next_node.prev = prev_node
        
        self.size -= 1
        return value
    
    def delete_by_value(self, value):
        """
        删除第一个出现的指定值的元素
        时间复杂度：O(n)
        :param value: 要删除的值
        :return: 是否成功删除
        """
        if self.is_empty():
            return False
        
        # 特殊情况：头节点就是要删除的节点
        if self.head.data == value:
            self.delete_head()
            return True
        
        # 遍历链表查找值
        current = self.head.next
        while current != self.head:
            if current.data == value:
                # 找到要删除的节点
                prev_node = current.prev
                next_node = current.next
                
                # 更新连接
                prev_node.next = next_node
                next_node.prev = prev_node
                
                self.size -= 1
                return True
            current = current.next
        
        # 未找到值
        return False
    
    def traverse_forward(self):
        """
        正向遍历链表，将元素存入列表返回
        时间复杂度：O(n)
        :return: 包含链表元素的列表
        """
        result = []
        if self.is_empty():
            return result
        
        current = self.head
        while True:
            result.append(current.data)
            current = current.next
            if current == self.head:
                break
        
        return result
    
    def traverse_backward(self):
        """
        反向遍历链表，将元素存入列表返回
        时间复杂度：O(n)
        :return: 包含链表元素的列表（反向）
        """
        result = []
        if self.is_empty():
            return result
        
        current = self.head.prev  # 从尾节点开始
        while True:
            result.append(current.data)
            current = current.prev
            if current == self.head.prev:
                break
        
        return result
    
    def search(self, value):
        """
        查找第一个出现的指定值的索引
        时间复杂度：O(n)
        :param value: 要查找的值
        :return: 元素索引，如果未找到返回-1
        """
        if self.is_empty():
            return -1
        
        index = 0
        current = self.head
        while True:
            if current.data == value:
                return index
            current = current.next
            index += 1
            if current == self.head:
                break
        
        return -1
    
    def get(self, index):
        """
        获取指定索引的元素值
        时间复杂度：O(n)
        :param index: 元素索引
        :return: 元素值
        """
        node = self._get_node_at(index)
        return node.data
    
    def set(self, index, value):
        """
        设置指定索引的元素值
        时间复杂度：O(n)
        :param index: 元素索引
        :param value: 新的元素值
        """
        node = self._get_node_at(index)
        node.data = value
    
    def get_size(self):
        """
        获取链表大小
        时间复杂度：O(1)
        :return: 链表中元素的数量
        """
        return self.size
    
    def clear(self):
        """
        清空链表
        时间复杂度：O(1)
        """
        self.head = None
        self.size = 0
    
    def reverse(self):
        """
        反转链表
        时间复杂度：O(n)
        """
        if self.is_empty() or self.size == 1:
            return  # 空链表或只有一个节点不需要反转
        
        # 保存头节点和尾节点
        current = self.head
        tail = self.head.prev
        
        # 交换每个节点的prev和next指针
        while True:
            # 交换prev和next
            temp = current.prev
            current.prev = current.next
            current.next = temp
            
            # 移动到下一个节点（现在是prev指针）
            current = current.prev
            
            if current == self.head:
                break
        
        # 更新头节点为原来的尾节点
        self.head = tail
    
    def rotate(self, k):
        """
        旋转链表
        时间复杂度：O(n)
        :param k: 旋转步数，正数表示向右旋转，负数表示向左旋转
        """
        if self.is_empty() or self.size == 1 or k % self.size == 0:
            return  # 无需旋转
        
        # 标准化k值，使其在[0, size-1]范围内
        k = k % self.size
        if k < 0:
            k += self.size  # 转换为正向旋转
        
        # 向右旋转k步相当于将倒数第k个节点作为新的头节点
        if k > 0:
            # 找到新的头节点（倒数第k个节点）
            new_head = self.head
            for i in range(self.size - k):
                new_head = new_head.next
            
            # 更新头节点
            self.head = new_head
    
    def print_list(self):
        """
        打印链表内容
        时间复杂度：O(n)
        """
        if self.is_empty():
            print("List is empty")
            return
        
        current = self.head
        print("List: ", end="")
        while True:
            print(current.data, end="")
            if current.next != self.head:
                print(" <-> ", end="")
            current = current.next
            if current == self.head:
                break
        print(" (circular)")
    
    def print_size(self):
        """
        打印链表大小
        """
        print(f"List size: {self.size}")
    
    @staticmethod
    def test_doubly_circular_linked_list():
        """测试双向循环链表"""
        print("=== 测试双向循环链表 ===")
        
        # 创建链表实例
        list_obj = DoublyCircularLinkedList()
        
        # 测试插入操作
        print("\n1. 测试插入操作:")
        print("插入10, 20, 30, 40, 50")
        list_obj.insert_at_tail(10)
        list_obj.insert_at_tail(20)
        list_obj.insert_at_tail(30)
        list_obj.insert_at_tail(40)
        list_obj.insert_at_tail(50)
        list_obj.print_list()
        list_obj.print_size()
        
        print("\n在头部插入5:")
        list_obj.insert_at_head(5)
        list_obj.print_list()
        
        print("\n在位置3插入25:")
        list_obj.insert_at_position(3, 25)
        list_obj.print_list()
        
        # 测试遍历操作
        print("\n2. 测试遍历操作:")
        
        print("正向遍历:", list_obj.traverse_forward())
        print("反向遍历:", list_obj.traverse_backward())
        
        # 测试查找和访问操作
        print("\n3. 测试查找和访问操作:")
        
        value = 25
        index = list_obj.search(value)
        print(f"查找值 {value}: 索引 = {index}")
        
        index = 3
        value = list_obj.get(index)
        print(f"索引 {index} 的值 = {value}")
        
        print("设置索引2的值为15:")
        list_obj.set(2, 15)
        list_obj.print_list()
        
        # 测试删除操作
        print("\n4. 测试删除操作:")
        
        print("删除头部元素:")
        value = list_obj.delete_head()
        print(f"删除的值 = {value}")
        list_obj.print_list()
        
        print("删除尾部元素:")
        value = list_obj.delete_tail()
        print(f"删除的值 = {value}")
        list_obj.print_list()
        
        print("删除索引2的元素:")
        value = list_obj.delete_at_position(2)
        print(f"删除的值 = {value}")
        list_obj.print_list()
        
        print("删除值20:")
        deleted = list_obj.delete_by_value(20)
        print(f"删除 {'成功' if deleted else '失败'}")
        list_obj.print_list()
        
        # 测试反转操作
        print("\n5. 测试反转操作:")
        list_obj.reverse()
        print("反转后:")
        list_obj.print_list()
        
        # 测试旋转操作
        print("\n6. 测试旋转操作:")
        
        print("向右旋转1步:")
        list_obj.rotate(1)
        list_obj.print_list()
        
        print("向左旋转2步:")
        list_obj.rotate(-2)
        list_obj.print_list()
        
        # 测试边界情况
        print("\n7. 测试边界情况:")
        
        print("清空链表:")
        list_obj.clear()
        list_obj.print_list()
        list_obj.print_size()
        
        print("空链表插入元素:")
        list_obj.insert_at_tail(100)
        list_obj.print_list()
        
        print("单节点链表删除:")
        value = list_obj.delete_head()
        print(f"删除的值 = {value}")
        list_obj.print_list()
        
        # 性能测试
        print("\n=== 性能测试 ===")
        import time
        
        # 测试大量插入操作
        large_list = DoublyCircularLinkedList()
        
        start_time = time.time()
        for i in range(10000):
            large_list.insert_at_tail(i)
        insert_time = time.time() - start_time
        
        print(f"插入10000个元素时间: {insert_time*1000:.2f} ms")
        print(f"链表大小: {large_list.get_size()}")
        
        # 测试随机访问
        start_time = time.time()
        for i in range(1000):
            large_list.get(i * 10)
        access_time = time.time() - start_time
        
        print(f"1000次随机访问时间: {access_time*1000:.2f} ms")

if __name__ == "__main__":
    DoublyCircularLinkedList.test_doubly_circular_linked_list()

# ================================
# C++ 代码等效实现 (注释版)
'''
#include <iostream>
#include <stdexcept>
#include <vector>

class Node {
public:
    int data;
    Node* prev;
    Node* next;
    
    // 构造函数
    Node(int val) : data(val), prev(nullptr), next(nullptr) {}
};

class DoublyCircularLinkedList {
private:
    Node* head;
    int size;
    
public:
    // 构造函数
    DoublyCircularLinkedList() : head(nullptr), size(0) {}
    
    // 析构函数
    ~DoublyCircularLinkedList() {
        clear();
    }
    
    // 检查链表是否为空
    bool isEmpty() const {
        return size == 0;
    }
    
    // 获取链表大小
    int getSize() const {
        return size;
    }
    
    // 在头部插入节点
    void insertAtHead(int data) {
        Node* newNode = new Node(data);
        
        if (isEmpty()) {
            // 空链表
            newNode->prev = newNode;
            newNode->next = newNode;
            head = newNode;
        } else {
            Node* tail = head->prev;
            
            newNode->next = head;
            newNode->prev = tail;
            
            head->prev = newNode;
            tail->next = newNode;
            
            head = newNode;
        }
        
        size++;
    }
    
    // 在尾部插入节点
    void insertAtTail(int data) {
        if (isEmpty()) {
            insertAtHead(data);
            return;
        }
        
        Node* newNode = new Node(data);
        Node* tail = head->prev;
        
        newNode->next = head;
        newNode->prev = tail;
        
        tail->next = newNode;
        head->prev = newNode;
        
        size++;
    }
    
    // 在指定位置插入节点
    void insertAtPosition(int position, int data) {
        if (position < 0 || position > size) {
            throw std::invalid_argument("Invalid insertion position");
        }
        
        if (position == 0) {
            insertAtHead(data);
        } else if (position == size) {
            insertAtTail(data);
        } else {
            Node* newNode = new Node(data);
            Node* current = head;
            
            // 优化查找位置
            if (position <= size / 2) {
                for (int i = 0; i < position - 1; i++) {
                    current = current->next;
                }
            } else {
                current = head->prev;
                for (int i = 0; i < size - position; i++) {
                    current = current->prev;
                }
            }
            
            newNode->next = current->next;
            newNode->prev = current;
            current->next->prev = newNode;
            current->next = newNode;
            
            size++;
        }
    }
    
    // 删除头节点
    int deleteHead() {
        if (isEmpty()) {
            throw std::runtime_error("Cannot delete from empty list");
        }
        
        int data = head->data;
        Node* temp = head;
        
        if (size == 1) {
            head = nullptr;
        } else {
            Node* tail = head->prev;
            Node* newHead = head->next;
            
            tail->next = newHead;
            newHead->prev = tail;
            head = newHead;
        }
        
        delete temp;
        size--;
        return data;
    }
    
    // 删除尾节点
    int deleteTail() {
        if (isEmpty()) {
            throw std::runtime_error("Cannot delete from empty list");
        }
        
        if (size == 1) {
            return deleteHead();
        }
        
        Node* tail = head->prev;
        int data = tail->data;
        
        tail->prev->next = head;
        head->prev = tail->prev;
        
        delete tail;
        size--;
        return data;
    }
    
    // 删除指定位置的节点
    int deleteAtPosition(int position) {
        if (position < 0 || position >= size) {
            throw std::invalid_argument("Invalid deletion position");
        }
        
        if (position == 0) {
            return deleteHead();
        } else if (position == size - 1) {
            return deleteTail();
        } else {
            Node* current = head;
            
            // 优化查找位置
            if (position <= size / 2) {
                for (int i = 0; i < position; i++) {
                    current = current->next;
                }
            } else {
                current = head->prev;
                for (int i = 0; i < size - 1 - position; i++) {
                    current = current->prev;
                }
            }
            
            int data = current->data;
            current->prev->next = current->next;
            current->next->prev = current->prev;
            
            delete current;
            size--;
            return data;
        }
    }
    
    // 删除指定值的节点
    bool deleteByValue(int value) {
        if (isEmpty()) {
            return false;
        }
        
        Node* current = head;
        for (int i = 0; i < size; i++) {
            if (current->data == value) {
                if (size == 1) {
                    delete current;
                    head = nullptr;
                } else {
                    current->prev->next = current->next;
                    current->next->prev = current->prev;
                    
                    if (current == head) {
                        head = current->next;
                    }
                    
                    delete current;
                }
                
                size--;
                return true;
            }
            
            current = current->next;
        }
        
        return false;
    }
    
    // 查找值的位置
    int search(int value) const {
        if (isEmpty()) {
            return -1;
        }
        
        Node* current = head;
        for (int i = 0; i < size; i++) {
            if (current->data == value) {
                return i;
            }
            current = current->next;
        }
        
        return -1;
    }
    
    // 获取指定位置的值
    int get(int position) const {
        if (position < 0 || position >= size) {
            throw std::invalid_argument("Invalid position");
        }
        
        Node* current = head;
        if (position <= size / 2) {
            for (int i = 0; i < position; i++) {
                current = current->next;
            }
        } else {
            current = head->prev;
            for (int i = 0; i < size - 1 - position; i++) {
                current = current->prev;
            }
        }
        
        return current->data;
    }
    
    // 设置指定位置的值
    void set(int position, int data) {
        if (position < 0 || position >= size) {
            throw std::invalid_argument("Invalid position");
        }
        
        Node* current = head;
        if (position <= size / 2) {
            for (int i = 0; i < position; i++) {
                current = current->next;
            }
        } else {
            current = head->prev;
            for (int i = 0; i < size - 1 - position; i++) {
                current = current->prev;
            }
        }
        
        current->data = data;
    }
    
    // 正向遍历
    std::vector<int> traverseForward() const {
        std::vector<int> result;
        if (!isEmpty()) {
            Node* current = head;
            for (int i = 0; i < size; i++) {
                result.push_back(current->data);
                current = current->next;
            }
        }
        return result;
    }
    
    // 反向遍历
    std::vector<int> traverseBackward() const {
        std::vector<int> result;
        if (!isEmpty()) {
            Node* current = head->prev;
            for (int i = 0; i < size; i++) {
                result.push_back(current->data);
                current = current->prev;
            }
        }
        return result;
    }
    
    // 反转链表
    void reverse() {
        if (size <= 1) {
            return;
        }
        
        Node* current = head;
        for (int i = 0; i < size; i++) {
            // 交换prev和next
            std::swap(current->prev, current->next);
            // 移动到下一个节点
            current = current->prev;
        }
        
        head = head->prev;
    }
    
    // 旋转链表
    void rotate(int k) {
        if (size <= 1 || k % size == 0) {
            return;
        }
        
        k = k % size;
        
        Node* current = head;
        for (int i = 0; i < k - 1; i++) {
            current = current->next;
        }
        
        Node* newHead = current->next;
        Node* tail = head->prev;
        
        // 断开连接
        tail->next = head;
        head->prev = tail;
        
        // 更新头节点
        head = newHead;
    }
    
    // 清空链表
    void clear() {
        if (!isEmpty()) {
            Node* current = head;
            for (int i = 0; i < size; i++) {
                Node* next = current->next;
                delete current;
                current = next;
            }
        }
        
        head = nullptr;
        size = 0;
    }
};

// 测试函数
void testDoublyCircularLinkedList() {
    DoublyCircularLinkedList list;
    
    // 测试插入
    list.insertAtHead(10);
    list.insertAtTail(20);
    list.insertAtHead(5);
    
    // 测试遍历
    std::cout << "Forward traversal: ";
    for (int val : list.traverseForward()) {
        std::cout << val << " ";
    }
    std::cout << std::endl;
    
    // 其他测试...
}
'''

# ================================
# Java 代码等效实现 (注释版)
'''
import java.util.ArrayList;
import java.util.List;

public class DoublyCircularLinkedList {
    private class Node {
        int data;
        Node prev;
        Node next;
        
        Node(int data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }
    
    private Node head;
    private int size;
    
    public DoublyCircularLinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void insertAtHead(int data) {
        Node newNode = new Node(data);
        
        if (isEmpty()) {
            newNode.prev = newNode;
            newNode.next = newNode;
            head = newNode;
        } else {
            Node tail = head.prev;
            
            newNode.next = head;
            newNode.prev = tail;
            
            head.prev = newNode;
            tail.next = newNode;
            
            head = newNode;
        }
        
        size++;
    }
    
    public void insertAtTail(int data) {
        if (isEmpty()) {
            insertAtHead(data);
            return;
        }
        
        Node newNode = new Node(data);
        Node tail = head.prev;
        
        newNode.next = head;
        newNode.prev = tail;
        
        tail.next = newNode;
        head.prev = newNode;
        
        size++;
    }
    
    public void insertAtPosition(int position, int data) {
        if (position < 0 || position > size) {
            throw new IllegalArgumentException("Invalid insertion position: " + position);
        }
        
        if (position == 0) {
            insertAtHead(data);
        } else if (position == size) {
            insertAtTail(data);
        } else {
            Node newNode = new Node(data);
            Node current = head;
            
            if (position <= size / 2) {
                for (int i = 0; i < position - 1; i++) {
                    current = current.next;
                }
            } else {
                current = head.prev;
                for (int i = 0; i < size - position; i++) {
                    current = current.prev;
                }
            }
            
            newNode.next = current.next;
            newNode.prev = current;
            current.next.prev = newNode;
            current.next = newNode;
            
            size++;
        }
    }
    
    public int deleteHead() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot delete from empty list");
        }
        
        int data = head.data;
        
        if (size == 1) {
            head = null;
        } else {
            Node tail = head.prev;
            Node newHead = head.next;
            
            tail.next = newHead;
            newHead.prev = tail;
            head = newHead;
        }
        
        size--;
        return data;
    }
    
    public int deleteTail() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot delete from empty list");
        }
        
        if (size == 1) {
            return deleteHead();
        }
        
        Node tail = head.prev;
        int data = tail.data;
        
        tail.prev.next = head;
        head.prev = tail.prev;
        
        size--;
        return data;
    }
    
    public int deleteAtPosition(int position) {
        if (position < 0 || position >= size) {
            throw new IllegalArgumentException("Invalid deletion position: " + position);
        }
        
        if (position == 0) {
            return deleteHead();
        } else if (position == size - 1) {
            return deleteTail();
        } else {
            Node current = head;
            
            if (position <= size / 2) {
                for (int i = 0; i < position; i++) {
                    current = current.next;
                }
            } else {
                current = head.prev;
                for (int i = 0; i < size - 1 - position; i++) {
                    current = current.prev;
                }
            }
            
            int data = current.data;
            current.prev.next = current.next;
            current.next.prev = current.prev;
            
            size--;
            return data;
        }
    }
    
    public boolean deleteByValue(int value) {
        if (isEmpty()) {
            return false;
        }
        
        Node current = head;
        for (int i = 0; i < size; i++) {
            if (current.data == value) {
                if (size == 1) {
                    head = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    
                    if (current == head) {
                        head = current.next;
                    }
                }
                
                size--;
                return true;
            }
            
            current = current.next;
        }
        
        return false;
    }
    
    public int search(int value) {
        if (isEmpty()) {
            return -1;
        }
        
        Node current = head;
        for (int i = 0; i < size; i++) {
            if (current.data == value) {
                return i;
            }
            current = current.next;
        }
        
        return -1;
    }
    
    public int get(int position) {
        if (position < 0 || position >= size) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        
        Node current = head;
        if (position <= size / 2) {
            for (int i = 0; i < position; i++) {
                current = current.next;
            }
        } else {
            current = head.prev;
            for (int i = 0; i < size - 1 - position; i++) {
                current = current.prev;
            }
        }
        
        return current.data;
    }
    
    public void set(int position, int data) {
        if (position < 0 || position >= size) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        
        Node current = head;
        if (position <= size / 2) {
            for (int i = 0; i < position; i++) {
                current = current.next;
            }
        } else {
            current = head.prev;
            for (int i = 0; i < size - 1 - position; i++) {
                current = current.prev;
            }
        }
        
        current.data = data;
    }
    
    public List<Integer> traverseForward() {
        List<Integer> result = new ArrayList<>();
        if (!isEmpty()) {
            Node current = head;
            for (int i = 0; i < size; i++) {
                result.add(current.data);
                current = current.next;
            }
        }
        return result;
    }
    
    public List<Integer> traverseBackward() {
        List<Integer> result = new ArrayList<>();
        if (!isEmpty()) {
            Node current = head.prev;
            for (int i = 0; i < size; i++) {
                result.add(current.data);
                current = current.prev;
            }
        }
        return result;
    }
    
    public void reverse() {
        if (size <= 1) {
            return;
        }
        
        Node current = head;
        for (int i = 0; i < size; i++) {
            // 交换prev和next
            Node temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            
            current = current.prev;
        }
        
        head = head.prev;
    }
    
    public void rotate(int k) {
        if (size <= 1 || k % size == 0) {
            return;
        }
        
        k = k % size;
        
        Node current = head;
        for (int i = 0; i < k - 1; i++) {
            current = current.next;
        }
        
        Node newHead = current.next;
        Node tail = head.prev;
        
        tail.next = head;
        head.prev = tail;
        
        head = newHead;
    }
    
    public void clear() {
        head = null;
        size = 0;
    }
    
    @Override
    public String toString() {
        return traverseForward().toString();
    }
    
    public static void main(String[] args) {
        DoublyCircularLinkedList list = new DoublyCircularLinkedList();
        // 测试代码...
    }
}
'''






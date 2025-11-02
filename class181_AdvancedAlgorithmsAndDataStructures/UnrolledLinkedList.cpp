#include <iostream>
#include <vector>
#include <stdexcept>
#include <chrono>

using namespace std;

/**
 * 块状链表实现 (C++版本)
 * 
 * 算法思路：
 * 块状链表是一种结合了数组和链表优点的数据结构。它将元素分块存储在节点中，
 * 每个节点包含一个固定大小的数组和指向下一个节点的指针。
 * 
 * 应用场景：
 * 1. 大型序列维护：文本编辑器、数据库索引
 * 2. 数组操作优化：批量更新处理
 * 3. 文件系统：大文件的分块管理
 * 
 * 时间复杂度：
 * - 插入/删除：O(n/b) 均摊，其中b是块大小
 * - 查找：O(n/b)
 * - 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 641. 设计循环双端队列
 * 2. LeetCode 707. 设计链表
 * 3. LeetCode 146. LRU缓存机制
 */

template<typename T>
class Block {
private:
    int capacity;      // 块的最大容量
    vector<T> array;   // 块内的数组
    int size;          // 当前块中元素的数量
    Block* next;       // 指向下一个块
    Block* prev;       // 指向上一个块

public:
    Block(int capacity) : capacity(capacity), size(0), next(nullptr), prev(nullptr) {
        array.resize(capacity);
    }

    bool isFull() const {
        /**
         * 检查块是否已满
         * @return 块是否已满
         */
        return size == capacity;
    }

    bool isEmpty() const {
        /**
         * 检查块是否为空
         * @return 块是否为空
         */
        return size == 0;
    }

    int getSize() const {
        /**
         * 获取块的大小
         * @return 块中元素的数量
         */
        return size;
    }

    int getCapacity() const {
        /**
         * 获取块的容量
         * @return 块的最大容量
         */
        return capacity;
    }

    void add(const T& value) {
        /**
         * 在块的末尾添加元素
         * @param value 要添加的值
         * @throws runtime_error 如果块已满
         */
        if (isFull()) {
            throw runtime_error("Block is full");
        }
        array[size] = value;
        size++;
    }

    void insert(int index, const T& value) {
        /**
         * 在指定位置插入元素
         * @param index 插入位置
         * @param value 要插入的值
         * @throws out_of_range 如果索引无效
         * @throws runtime_error 如果块已满
         */
        if (isFull()) {
            throw runtime_error("Block is full");
        }

        if (index < 0 || index > size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        // 移动元素为新元素腾出空间
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = value;
        size++;
    }

    T deleteAt(int index) {
        /**
         * 删除指定位置的元素
         * @param index 要删除的元素位置
         * @return 被删除的元素
         * @throws out_of_range 如果索引无效
         */
        if (index < 0 || index >= size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        T value = array[index];

        // 移动元素覆盖被删除的元素
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        size--;

        return value;
    }

    T get(int index) const {
        /**
         * 获取指定位置的元素
         * @param index 元素位置
         * @return 元素值
         * @throws out_of_range 如果索引无效
         */
        if (index < 0 || index >= size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }
        return array[index];
    }

    T set(int index, const T& value) {
        /**
         * 设置指定位置的元素值
         * @param index 元素位置
         * @param value 新的元素值
         * @return 原来的元素值
         * @throws out_of_range 如果索引无效
         */
        if (index < 0 || index >= size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        T oldValue = array[index];
        array[index] = value;
        return oldValue;
    }

    Block* split(int splitIndex) {
        /**
         * 分割块
         * 将当前块从指定位置分割，返回包含后半部分元素的新块
         * @param splitIndex 分割位置
         * @return 包含后半部分元素的新块
         * @throws out_of_range 如果分割位置无效
         */
        if (splitIndex < 0 || splitIndex > size) {
            throw out_of_range("Split index out of bounds: " + to_string(splitIndex));
        }

        // 创建新块
        Block* newBlock = new Block(capacity);

        // 复制后半部分元素到新块
        int elementsToMove = size - splitIndex;
        for (int i = 0; i < elementsToMove; i++) {
            newBlock->array[i] = array[splitIndex + i];
        }

        // 更新块大小
        newBlock->size = elementsToMove;
        size = splitIndex;

        // 建立双向链接
        newBlock->next = next;
        if (next != nullptr) {
            next->prev = newBlock;
        }
        next = newBlock;
        newBlock->prev = this;

        return newBlock;
    }

    Block* mergeNext() {
        /**
         * 合并两个相邻块
         * 假设当前块和next块是相邻的
         * @return 合并后的块（即当前块）
         * @throws runtime_error 如果没有下一个块或合并后超出容量
         */
        if (next == nullptr) {
            throw runtime_error("No next block to merge");
        }

        if (size + next->size > capacity) {
            throw runtime_error("Merged size exceeds block capacity");
        }

        // 复制next块的元素到当前块
        for (int i = 0; i < next->size; i++) {
            array[size + i] = next->array[i];
        }
        size += next->size;

        // 更新链接，跳过next块
        Block* nextNext = next->next;
        delete next;
        next = nextNext;
        if (nextNext != nullptr) {
            nextNext->prev = this;
        }

        return this;
    }

    // 友元类声明
    template<typename U>
    friend class UnrolledLinkedList;
};

template<typename T>
class UnrolledLinkedList {
private:
    int blockCapacity;  // 块的最大容量
    Block<T>* head;     // 头块指针
    Block<T>* tail;     // 尾块指针
    int size;           // 链表元素总数

    // 内部辅助结构体
    struct BlockAndIndex {
        Block<T>* block;
        int index;

        BlockAndIndex(Block<T>* b, int i) : block(b), index(i) {}
    };

    BlockAndIndex findBlockAndIndex(int index) const {
        /**
         * 查找包含指定索引的块和块内索引
         * 时间复杂度：O(n/b)
         * @param index 元素索引
         * @return 包含块和块内索引的对象
         */
        if (isEmpty() || index < 0 || index >= size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        // 优化：根据索引位置选择从头还是从尾开始查找
        // 如果索引更靠近头部，从头开始
        if (index < size / 2) {
            Block<T>* current = head;
            int currentIndex = 0;

            while (current != nullptr) {
                if (index < currentIndex + current->getSize()) {
                    // 找到了包含索引的块
                    return BlockAndIndex(current, index - currentIndex);
                }
                currentIndex += current->getSize();
                current = current->next;
            }
        // 否则从尾开始
        } else {
            Block<T>* current = tail;
            int currentIndex = size - 1;
            int currentBlockSize = current->getSize();

            while (current != nullptr) {
                if (index >= currentIndex - currentBlockSize + 1) {
                    // 找到了包含索引的块
                    return BlockAndIndex(current, index - (currentIndex - currentBlockSize + 1));
                }
                currentIndex -= currentBlockSize;
                current = current->prev;
                currentBlockSize = (current != nullptr) ? current->getSize() : 0;
            }
        }

        // 不应该到达这里
        throw out_of_range("Index not found: " + to_string(index));
    }

public:
    UnrolledLinkedList(int blockCapacity = 16) : blockCapacity(blockCapacity), head(nullptr), tail(nullptr), size(0) {
        /**
         * 构造函数
         * @param blockCapacity 块的最大容量
         * @throws invalid_argument 如果块容量小于2
         */
        if (blockCapacity < 2) {
            throw invalid_argument("Block capacity must be at least 2");
        }
    }

    ~UnrolledLinkedList() {
        /**
         * 析构函数
         */
        clear();
    }

    bool isEmpty() const {
        /**
         * 检查链表是否为空
         * @return 链表是否为空
         */
        return size == 0;
    }

    int getSize() const {
        /**
         * 获取链表中元素的数量
         * @return 元素数量
         */
        return size;
    }

    void add(const T& value) {
        /**
         * 在链表末尾添加元素
         * 时间复杂度：O(n/b) 均摊，其中b是块容量
         * @param value 要添加的值
         */
        if (isEmpty()) {
            // 空链表，创建第一个块
            head = new Block<T>(blockCapacity);
            tail = head;
            head->add(value);
        } else {
            // 非空链表，检查尾块是否已满
            if (tail->isFull()) {
                // 尾块已满，分割为两个半满的块
                tail->split(tail->getSize() / 2);
                tail = tail->next; // 更新尾块指针
            }
            tail->add(value);
        }
        size++;
    }

    void insert(int index, const T& value) {
        /**
         * 在指定位置插入元素
         * 时间复杂度：O(n/b)
         * @param index 插入位置
         * @param value 要插入的值
         * @throws out_of_range 如果索引无效
         */
        if (index < 0 || index > size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        if (index == size) {
            // 在末尾插入，调用add方法
            add(value);
            return;
        }

        if (isEmpty()) {
            // 空链表，创建第一个块
            head = new Block<T>(blockCapacity);
            tail = head;
            head->add(value);
        } else {
            // 定位到包含插入位置的块和块内索引
            BlockAndIndex pos = findBlockAndIndex(index);
            Block<T>* block = pos.block;
            int blockIndex = pos.index;

            // 检查块是否已满
            if (block->isFull()) {
                // 块已满，分割为两个半满的块
                int splitIndex = block->getSize() / 2;
                Block<T>* newBlock = block->split(splitIndex);

                // 调整插入位置
                if (blockIndex >= splitIndex) {
                    block = newBlock;
                    blockIndex -= splitIndex;
                }
            }

            // 在块中插入元素
            block->insert(blockIndex, value);

            // 更新尾块指针（如果需要）
            Block<T>* current = head;
            while (current->next != nullptr) {
                current = current->next;
            }
            tail = current;
        }

        size++;
    }

    T deleteAt(int index) {
        /**
         * 删除指定位置的元素
         * 时间复杂度：O(n/b)
         * @param index 要删除的元素位置
         * @return 被删除的元素
         * @throws out_of_range 如果索引无效
         */
        if (isEmpty()) {
            throw runtime_error("Cannot delete from empty list");
        }

        if (index < 0 || index >= size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        // 定位到包含删除位置的块和块内索引
        BlockAndIndex pos = findBlockAndIndex(index);
        Block<T>* block = pos.block;
        int blockIndex = pos.index;

        // 保存要删除的元素值
        T value = block->deleteAt(blockIndex);

        // 如果删除后块的大小过小，尝试与相邻块合并（保持块的大小在合理范围）
        if ((block->getSize() < blockCapacity / 4 && block != head) ||
            (block->isEmpty() && size > 0)) {  // 特殊处理空块

            // 优先与前一个块合并
            if (block->prev != nullptr) {
                Block<T>* prevBlock = block->prev;
                // 确保合并后不会超出容量
                if (prevBlock->getSize() + block->getSize() <= blockCapacity) {
                    // 将要删除的索引调整为前一个块的末尾
                    if (block == tail) {
                        tail = prevBlock;
                    }
                    prevBlock->mergeNext();
                    // 如果当前删除的块是head，更新head指针
                    if (block == head) {
                        head = prevBlock;
                    }
                }
            // 否则与后一个块合并
            } else if (block->next != nullptr) {
                Block<T>* nextBlock = block->next;
                if (block->getSize() + nextBlock->getSize() <= blockCapacity) {
                    if (nextBlock == tail) {
                        tail = block;
                    }
                    block->mergeNext();
                }
            // 特殊情况：只剩一个空块
            } else if (block->isEmpty()) {
                head = nullptr;
                tail = nullptr;
            }
        }

        size--;
        return value;
    }

    T get(int index) const {
        /**
         * 获取指定位置的元素
         * 时间复杂度：O(n/b)
         * @param index 元素位置
         * @return 元素值
         * @throws out_of_range 如果索引无效
         */
        if (isEmpty()) {
            throw runtime_error("List is empty");
        }

        if (index < 0 || index >= size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        BlockAndIndex pos = findBlockAndIndex(index);
        return pos.block->get(pos.index);
    }

    T set(int index, const T& value) {
        /**
         * 设置指定位置的元素值
         * 时间复杂度：O(n/b)
         * @param index 元素位置
         * @param value 新的元素值
         * @return 原来的元素值
         * @throws out_of_range 如果索引无效
         */
        if (isEmpty()) {
            throw runtime_error("List is empty");
        }

        if (index < 0 || index >= size) {
            throw out_of_range("Index out of bounds: " + to_string(index));
        }

        BlockAndIndex pos = findBlockAndIndex(index);
        return pos.block->set(pos.index, value);
    }

    void clear() {
        /**
         * 清空链表
         * 时间复杂度：O(n)
         */
        Block<T>* current = head;
        while (current != nullptr) {
            Block<T>* next = current->next;
            delete current;
            current = next;
        }
        head = nullptr;
        tail = nullptr;
        size = 0;
    }

    vector<T> toArray() const {
        /**
         * 将链表内容转换为数组
         * 时间复杂度：O(n)
         * @return 包含链表所有元素的数组
         */
        if (isEmpty()) {
            return vector<T>();
        }

        vector<T> result(size);
        Block<T>* current = head;
        int resultIndex = 0;

        while (current != nullptr) {
            for (int i = 0; i < current->getSize(); i++) {
                result[resultIndex++] = current->get(i);
            }
            current = current->next;
        }

        return result;
    }

    int indexOf(const T& value) const {
        /**
         * 查找第一个出现的指定值的索引
         * 时间复杂度：O(n)
         * @param value 要查找的值
         * @return 元素索引，如果未找到返回-1
         */
        if (isEmpty()) {
            return -1;
        }

        int index = 0;
        Block<T>* current = head;

        while (current != nullptr) {
            for (int i = 0; i < current->getSize(); i++) {
                if (current->get(i) == value) {
                    return index + i;
                }
            }
            index += current->getSize();
            current = current->next;
        }

        return -1;
    }

    int lastIndexOf(const T& value) const {
        /**
         * 查找最后一个出现的指定值的索引
         * 时间复杂度：O(n)
         * @param value 要查找的值
         * @return 元素索引，如果未找到返回-1
         */
        if (isEmpty()) {
            return -1;
        }

        int index = size - 1;
        Block<T>* current = tail;
        int currentBlockSize = current->getSize();

        while (current != nullptr) {
            for (int i = currentBlockSize - 1; i >= 0; i--) {
                if (current->get(i) == value) {
                    return index - (currentBlockSize - 1 - i);
                }
            }

            index -= currentBlockSize;
            current = current->prev;
            currentBlockSize = (current != nullptr) ? current->getSize() : 0;
        }

        return -1;
    }

    bool contains(const T& value) const {
        /**
         * 检查链表是否包含指定值
         * 时间复杂度：O(n)
         * @param value 要检查的值
         * @return 是否包含该值
         */
        return indexOf(value) != -1;
    }

    UnrolledLinkedList<T> subList(int start, int end) const {
        /**
         * 范围查询：获取从start到end（不包含）的子列表
         * 时间复杂度：O(n/b + k)，其中k是子列表的大小
         * @param start 起始索引（包含）
         * @param end 结束索引（不包含）
         * @return 子列表
         * @throws out_of_range 如果索引无效
         */
        if (start < 0 || end > size || start > end) {
            throw out_of_range("Invalid range: [" + to_string(start) + ", " + to_string(end) + ")");
        }

        UnrolledLinkedList<T> sublist(blockCapacity);

        if (start == end) {
            return sublist; // 空的子列表
        }

        // 处理跨越多个块的情况
        int currentIndex = start;
        while (currentIndex < end) {
            sublist.add(get(currentIndex));
            currentIndex++;
        }

        return sublist;
    }

    void printList() const {
        /**
         * 打印链表内容
         * 时间复杂度：O(n)
         */
        if (isEmpty()) {
            cout << "List is empty" << endl;
            return;
        }

        cout << "UnrolledLinkedList: [";
        Block<T>* current = head;
        bool firstElement = true;

        while (current != nullptr) {
            for (int i = 0; i < current->getSize(); i++) {
                if (!firstElement) {
                    cout << ", ";
                } else {
                    firstElement = false;
                }
                cout << current->get(i);
            }
            current = current->next;
        }

        cout << "]" << endl;
    }

    void printBlockStructure() const {
        /**
         * 打印链表的块结构（用于调试）
         */
        if (isEmpty()) {
            cout << "List is empty" << endl;
            return;
        }

        cout << "Block Structure:" << endl;
        int blockIndex = 0;
        Block<T>* current = head;

        while (current != nullptr) {
            cout << "Block " << blockIndex << " (size=" << current->getSize() << "): [";

            for (int i = 0; i < current->getSize(); i++) {
                cout << current->get(i);
                if (i < current->getSize() - 1) {
                    cout << ", ";
                }
            }

            cout << "]" << endl;

            current = current->next;
            blockIndex++;
        }
    }

    static void testUnrolledLinkedList() {
        cout << "=== 测试块状链表 ===" << endl;
        // 使用较小的块容量以便更容易观察块分割和合并
        UnrolledLinkedList<int> list(4);

        // 测试添加操作
        cout << "\n1. 测试添加操作:" << endl;
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        cout << "添加1-10后的列表:" << endl;
        list.printList();
        list.printBlockStructure();

        // 测试获取和设置
        cout << "\n2. 测试获取和设置操作:" << endl;
        cout << "索引5的值: " << list.get(5) << endl; // 应该是 6
        int oldValue = list.set(5, 100);
        cout << "设置索引5的值为100，旧值: " << oldValue << endl;
        cout << "索引5的新值: " << list.get(5) << endl; // 应该是 100
        list.printList();

        // 测试插入操作
        cout << "\n3. 测试插入操作:" << endl;
        list.insert(3, 50); // 在索引3插入50
        cout << "在索引3插入50后:" << endl;
        list.printList();
        list.printBlockStructure();

        list.insert(0, 0); // 在头部插入0
        cout << "在头部插入0后:" << endl;
        list.printList();

        // 测试删除操作
        cout << "\n4. 测试删除操作:" << endl;
        int deletedValue = list.deleteAt(5); // 删除索引5的值
        cout << "删除索引5的值: " << deletedValue << endl;
        cout << "删除后:" << endl;
        list.printList();
        list.printBlockStructure();

        list.deleteAt(0); // 删除头部
        cout << "删除头部后:" << endl;
        list.printList();

        // 测试查找操作
        cout << "\n5. 测试查找操作:" << endl;
        cout << "值为100的索引: " << list.indexOf(100) << endl;
        cout << "值为99的索引: " << list.indexOf(99) << endl; // 应该是 -1
        cout << "列表是否包含50: " << (list.contains(50) ? "true" : "false") << endl;

        // 测试子列表
        cout << "\n6. 测试子列表:" << endl;
        UnrolledLinkedList<int> sublist = list.subList(2, 6);
        cout << "子列表[2,6):" << endl;
        sublist.printList();

        // 测试清空操作
        cout << "\n7. 测试清空操作:" << endl;
        list.clear();
        cout << "清空后:" << endl;
        list.printList();
        cout << "列表大小: " << list.getSize() << endl;

        // 测试边界情况
        cout << "\n8. 测试边界情况:" << endl;
        try {
            list.get(0); // 空列表访问
        } catch (const exception& e) {
            cout << "空列表访问异常: " << e.what() << endl;
        }

        list.add(1); // 添加一个元素
        list.add(2); // 添加第二个元素
        cout << "添加两个元素后:" << endl;
        list.printList();

        list.deleteAt(0); // 删除第一个元素
        list.deleteAt(0); // 删除第二个元素
        cout << "删除所有元素后:" << endl;
        list.printList();

        // 性能测试
        cout << "\n=== 性能测试 ===" << endl;

        UnrolledLinkedList<int> largeList(16);

        auto startTime = chrono::high_resolution_clock::now();
        for (int i = 0; i < 10000; i++) {
            largeList.add(i);
        }
        auto endTime = chrono::high_resolution_clock::now();
        auto insertTime = chrono::duration_cast<chrono::microseconds>(endTime - startTime);

        cout << "插入10000个元素时间: " << insertTime.count() / 1000.0 << " ms" << endl;
        cout << "链表大小: " << largeList.getSize() << endl;

        // 测试随机访问
        startTime = chrono::high_resolution_clock::now();
        for (int i = 0; i < 1000; i++) {
            largeList.get(i * 10);
        }
        endTime = chrono::high_resolution_clock::now();
        auto accessTime = chrono::duration_cast<chrono::microseconds>(endTime - startTime);

        cout << "1000次随机访问时间: " << accessTime.count() / 1000.0 << " ms" << endl;
    }
};

int main() {
    UnrolledLinkedList<int>::testUnrolledLinkedList();
    return 0;
}
package class185.unrolled_linked_list_problems;

import java.util.*;

/**
 * 块状链表实现 (Java版本)
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
class Block {
    int capacity;      // 块的最大容量
    Object[] array;    // 块内的数组
    int size;          // 当前块中元素的数量
    Block next;        // 指向下一个块
    Block prev;        // 指向上一个块

    Block(int capacity) {
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.size = 0;
        this.next = null;
        this.prev = null;
    }

    boolean isFull() {
        /**
         * 检查块是否已满
         * @return 块是否已满
         */
        return size == capacity;
    }

    boolean isEmpty() {
        /**
         * 检查块是否为空
         * @return 块是否为空
         */
        return size == 0;
    }

    int getSize() {
        /**
         * 获取块的大小
         * @return 块中元素的数量
         */
        return size;
    }

    int getCapacity() {
        /**
         * 获取块的容量
         * @return 块的最大容量
         */
        return capacity;
    }

    void add(Object value) {
        /**
         * 在块的末尾添加元素
         * @param value 要添加的值
         * @throws RuntimeException 如果块已满
         */
        if (isFull()) {
            throw new RuntimeException("Block is full");
        }
        array[size] = value;
        size++;
    }

    void insert(int index, Object value) {
        /**
         * 在指定位置插入元素
         * @param index 插入位置
         * @param value 要插入的值
         * @throws IndexOutOfBoundsException 如果索引无效
         * @throws RuntimeException 如果块已满
         */
        if (isFull()) {
            throw new RuntimeException("Block is full");
        }

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        // 移动元素为新元素腾出空间
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = value;
        size++;
    }

    Object delete(int index) {
        /**
         * 删除指定位置的元素
         * @param index 要删除的元素位置
         * @return 被删除的元素
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Object value = array[index];

        // 移动元素覆盖被删除的元素
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[size - 1] = null; // 清除引用
        size--;

        return value;
    }

    Object get(int index) {
        /**
         * 获取指定位置的元素
         * @param index 元素位置
         * @return 元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return array[index];
    }

    Object set(int index, Object value) {
        /**
         * 设置指定位置的元素值
         * @param index 元素位置
         * @param value 新的元素值
         * @return 原来的元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Object oldValue = array[index];
        array[index] = value;
        return oldValue;
    }

    Block split(int splitIndex) {
        /**
         * 分割块
         * 将当前块从指定位置分割，返回包含后半部分元素的新块
         * @param splitIndex 分割位置
         * @return 包含后半部分元素的新块
         * @throws IndexOutOfBoundsException 如果分割位置无效
         */
        if (splitIndex < 0 || splitIndex > size) {
            throw new IndexOutOfBoundsException("Split index out of bounds: " + splitIndex);
        }

        // 创建新块
        Block newBlock = new Block(capacity);

        // 复制后半部分元素到新块
        int elementsToMove = size - splitIndex;
        for (int i = 0; i < elementsToMove; i++) {
            newBlock.array[i] = array[splitIndex + i];
            array[splitIndex + i] = null; // 清除引用
        }

        // 更新块大小
        newBlock.size = elementsToMove;
        size = splitIndex;

        // 建立双向链接
        newBlock.next = next;
        if (next != null) {
            next.prev = newBlock;
        }
        next = newBlock;
        newBlock.prev = this;

        return newBlock;
    }

    Block mergeNext() {
        /**
         * 合并两个相邻块
         * 假设当前块和next块是相邻的
         * @return 合并后的块（即当前块）
         * @throws RuntimeException 如果没有下一个块或合并后超出容量
         */
        if (next == null) {
            throw new RuntimeException("No next block to merge");
        }

        if (size + next.size > capacity) {
            throw new RuntimeException("Merged size exceeds block capacity");
        }

        // 复制next块的元素到当前块
        for (int i = 0; i < next.size; i++) {
            array[size + i] = next.array[i];
            next.array[i] = null; // 清除引用
        }
        size += next.size;

        // 更新链接，跳过next块
        Block nextNext = next.next;
        next = nextNext;
        if (nextNext != null) {
            nextNext.prev = this;
        }

        return this;
    }
}

public class UnrolledLinkedList {
    private int blockCapacity;  // 块的最大容量
    private Block head;         // 头块指针
    private Block tail;         // 尾块指针
    private int size;           // 链表元素总数

    public UnrolledLinkedList(int blockCapacity) {
        /**
         * 构造函数
         * @param blockCapacity 块的最大容量
         * @throws IllegalArgumentException 如果块容量小于2
         */
        if (blockCapacity < 2) {
            throw new IllegalArgumentException("Block capacity must be at least 2");
        }

        this.blockCapacity = blockCapacity;
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public UnrolledLinkedList() {
        /**
         * 默认构造函数，使用默认块容量16
         */
        this(16);
    }

    public boolean isEmpty() {
        /**
         * 检查链表是否为空
         * @return 链表是否为空
         */
        return size == 0;
    }

    public int getSize() {
        /**
         * 获取链表中元素的数量
         * @return 元素数量
         */
        return size;
    }

    public void add(Object value) {
        /**
         * 在链表末尾添加元素
         * 时间复杂度：O(n/b) 均摊，其中b是块容量
         * @param value 要添加的值
         */
        if (isEmpty()) {
            // 空链表，创建第一个块
            head = new Block(blockCapacity);
            tail = head;
            head.add(value);
        } else {
            // 非空链表，检查尾块是否已满
            if (tail.isFull()) {
                // 尾块已满，分割为两个半满的块
                tail.split(tail.getSize() / 2);
                tail = tail.next; // 更新尾块指针
            }
            tail.add(value);
        }
        size++;
    }

    public void insert(int index, Object value) {
        /**
         * 在指定位置插入元素
         * 时间复杂度：O(n/b)
         * @param index 插入位置
         * @param value 要插入的值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        if (index == size) {
            // 在末尾插入，调用add方法
            add(value);
            return;
        }

        if (isEmpty()) {
            // 空链表，创建第一个块
            head = new Block(blockCapacity);
            tail = head;
            head.add(value);
        } else {
            // 定位到包含插入位置的块和块内索引
            BlockAndIndex pos = findBlockAndIndex(index);
            Block block = pos.block;
            int blockIndex = pos.index;

            // 检查块是否已满
            if (block.isFull()) {
                // 块已满，分割为两个半满的块
                int splitIndex = block.getSize() / 2;
                Block newBlock = block.split(splitIndex);

                // 调整插入位置
                if (blockIndex >= splitIndex) {
                    block = newBlock;
                    blockIndex -= splitIndex;
                }
            }

            // 在块中插入元素
            block.insert(blockIndex, value);

            // 更新尾块指针（如果需要）
            Block current = head;
            while (current.next != null) {
                current = current.next;
            }
            tail = current;
        }

        size++;
    }

    public Object delete(int index) {
        /**
         * 删除指定位置的元素
         * 时间复杂度：O(n/b)
         * @param index 要删除的元素位置
         * @return 被删除的元素
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (isEmpty()) {
            throw new RuntimeException("Cannot delete from empty list");
        }

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        // 定位到包含删除位置的块和块内索引
        BlockAndIndex pos = findBlockAndIndex(index);
        Block block = pos.block;
        int blockIndex = pos.index;

        // 保存要删除的元素值
        Object value = block.delete(blockIndex);

        // 如果删除后块的大小过小，尝试与相邻块合并（保持块的大小在合理范围）
        if ((block.getSize() < blockCapacity / 4 && block != head) ||
            (block.isEmpty() && size > 0)) {  // 特殊处理空块

            // 优先与前一个块合并
            if (block.prev != null) {
                Block prevBlock = block.prev;
                // 确保合并后不会超出容量
                if (prevBlock.getSize() + block.getSize() <= blockCapacity) {
                    // 将要删除的索引调整为前一个块的末尾
                    if (block == tail) {
                        tail = prevBlock;
                    }
                    prevBlock.mergeNext();
                    // 如果当前删除的块是head，更新head指针
                    if (block == head) {
                        head = prevBlock;
                    }
                }
            // 否则与后一个块合并
            } else if (block.next != null) {
                Block nextBlock = block.next;
                if (block.getSize() + nextBlock.getSize() <= blockCapacity) {
                    if (nextBlock == tail) {
                        tail = block;
                    }
                    block.mergeNext();
                }
            // 特殊情况：只剩一个空块
            } else if (block.isEmpty()) {
                head = null;
                tail = null;
            }
        }

        size--;
        return value;
    }

    public Object get(int index) {
        /**
         * 获取指定位置的元素
         * 时间复杂度：O(n/b)
         * @param index 元素位置
         * @return 元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        BlockAndIndex pos = findBlockAndIndex(index);
        return pos.block.get(pos.index);
    }

    public Object set(int index, Object value) {
        /**
         * 设置指定位置的元素值
         * 时间复杂度：O(n/b)
         * @param index 元素位置
         * @param value 新的元素值
         * @return 原来的元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        BlockAndIndex pos = findBlockAndIndex(index);
        return pos.block.set(pos.index, value);
    }

    public void clear() {
        /**
         * 清空链表
         * 时间复杂度：O(n)
         */
        head = null;
        tail = null;
        size = 0;
    }

    public Object[] toArray() {
        /**
         * 将链表内容转换为数组
         * 时间复杂度：O(n)
         * @return 包含链表所有元素的数组
         */
        if (isEmpty()) {
            return new Object[0];
        }

        Object[] result = new Object[size];
        Block current = head;
        int resultIndex = 0;

        while (current != null) {
            for (int i = 0; i < current.getSize(); i++) {
                result[resultIndex++] = current.get(i);
            }
            current = current.next;
        }

        return result;
    }

    public int indexOf(Object value) {
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
        Block current = head;

        while (current != null) {
            for (int i = 0; i < current.getSize(); i++) {
                if (Objects.equals(current.get(i), value)) {
                    return index + i;
                }
            }
            index += current.getSize();
            current = current.next;
        }

        return -1;
    }

    public int lastIndexOf(Object value) {
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
        Block current = tail;
        int currentBlockSize = current.getSize();

        while (current != null) {
            for (int i = currentBlockSize - 1; i >= 0; i--) {
                if (Objects.equals(current.get(i), value)) {
                    return index - (currentBlockSize - 1 - i);
                }
            }

            index -= currentBlockSize;
            current = current.prev;
            currentBlockSize = (current != null) ? current.getSize() : 0;
        }

        return -1;
    }

    public boolean contains(Object value) {
        /**
         * 检查链表是否包含指定值
         * 时间复杂度：O(n)
         * @param value 要检查的值
         * @return 是否包含该值
         */
        return indexOf(value) != -1;
    }

    public UnrolledLinkedList subList(int start, int end) {
        /**
         * 范围查询：获取从start到end（不包含）的子列表
         * 时间复杂度：O(n/b + k)，其中k是子列表的大小
         * @param start 起始索引（包含）
         * @param end 结束索引（不包含）
         * @return 子列表
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        if (start < 0 || end > size || start > end) {
            throw new IndexOutOfBoundsException("Invalid range: [" + start + ", " + end + ")");
        }

        UnrolledLinkedList sublist = new UnrolledLinkedList(blockCapacity);

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

    public void printList() {
        /**
         * 打印链表内容
         * 时间复杂度：O(n)
         */
        if (isEmpty()) {
            System.out.println("List is empty");
            return;
        }

        System.out.print("UnrolledLinkedList: [");
        Block current = head;
        boolean firstElement = true;

        while (current != null) {
            for (int i = 0; i < current.getSize(); i++) {
                if (!firstElement) {
                    System.out.print(", ");
                } else {
                    firstElement = false;
                }
                System.out.print(current.get(i));
            }
            current = current.next;
        }

        System.out.println("]");
    }

    public void printBlockStructure() {
        /**
         * 打印链表的块结构（用于调试）
         */
        if (isEmpty()) {
            System.out.println("List is empty");
            return;
        }

        System.out.println("Block Structure:");
        int blockIndex = 0;
        Block current = head;

        while (current != null) {
            System.out.print("Block " + blockIndex + " (size=" + current.getSize() + "): [");

            for (int i = 0; i < current.getSize(); i++) {
                System.out.print(current.get(i));
                if (i < current.getSize() - 1) {
                    System.out.print(", ");
                }
            }

            System.out.println("]");

            current = current.next;
            blockIndex++;
        }
    }

    // ==================== 内部辅助类和方法 ====================

    private static class BlockAndIndex {
        Block block;
        int index;

        BlockAndIndex(Block block, int index) {
            this.block = block;
            this.index = index;
        }
    }

    private BlockAndIndex findBlockAndIndex(int index) {
        /**
         * 查找包含指定索引的块和块内索引
         * 时间复杂度：O(n/b)
         * @param index 元素索引
         * @return 包含块和块内索引的对象
         */
        if (isEmpty() || index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        // 优化：根据索引位置选择从头还是从尾开始查找
        // 如果索引更靠近头部，从头开始
        if (index < size / 2) {
            Block current = head;
            int currentIndex = 0;

            while (current != null) {
                if (index < currentIndex + current.getSize()) {
                    // 找到了包含索引的块
                    return new BlockAndIndex(current, index - currentIndex);
                }
                currentIndex += current.getSize();
                current = current.next;
            }
        // 否则从尾开始
        } else {
            Block current = tail;
            int currentIndex = size - 1;
            int currentBlockSize = current.getSize();

            while (current != null) {
                if (index >= currentIndex - currentBlockSize + 1) {
                    // 找到了包含索引的块
                    return new BlockAndIndex(current, index - (currentIndex - currentBlockSize + 1));
                }
                currentIndex -= currentBlockSize;
                current = current.prev;
                currentBlockSize = (current != null) ? current.getSize() : 0;
            }
        }

        // 不应该到达这里
        throw new IndexOutOfBoundsException("Index not found: " + index);
    }

    public static void main(String[] args) {
        System.out.println("=== 测试块状链表 ===");
        // 使用较小的块容量以便更容易观察块分割和合并
        UnrolledLinkedList list = new UnrolledLinkedList(4);

        // 测试添加操作
        System.out.println("\n1. 测试添加操作:");
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        System.out.println("添加1-10后的列表:");
        list.printList();
        list.printBlockStructure();

        // 测试获取和设置
        System.out.println("\n2. 测试获取和设置操作:");
        System.out.println("索引5的值: " + list.get(5)); // 应该是 6
        Object oldValue = list.set(5, 100);
        System.out.println("设置索引5的值为100，旧值: " + oldValue);
        System.out.println("索引5的新值: " + list.get(5)); // 应该是 100
        list.printList();

        // 测试插入操作
        System.out.println("\n3. 测试插入操作:");
        list.insert(3, 50); // 在索引3插入50
        System.out.println("在索引3插入50后:");
        list.printList();
        list.printBlockStructure();

        list.insert(0, 0); // 在头部插入0
        System.out.println("在头部插入0后:");
        list.printList();

        // 测试删除操作
        System.out.println("\n4. 测试删除操作:");
        Object deletedValue = list.delete(5); // 删除索引5的值
        System.out.println("删除索引5的值: " + deletedValue);
        System.out.println("删除后:");
        list.printList();
        list.printBlockStructure();

        list.delete(0); // 删除头部
        System.out.println("删除头部后:");
        list.printList();

        // 测试查找操作
        System.out.println("\n5. 测试查找操作:");
        System.out.println("值为100的索引: " + list.indexOf(100));
        System.out.println("值为99的索引: " + list.indexOf(99)); // 应该是 -1
        System.out.println("列表是否包含50: " + list.contains(50));

        // 测试子列表
        System.out.println("\n6. 测试子列表:");
        UnrolledLinkedList sublist = list.subList(2, 6);
        System.out.println("子列表[2,6):");
        sublist.printList();

        // 测试清空操作
        System.out.println("\n7. 测试清空操作:");
        list.clear();
        System.out.println("清空后:");
        list.printList();
        System.out.println("列表大小: " + list.getSize());

        // 测试边界情况
        System.out.println("\n8. 测试边界情况:");
        try {
            list.get(0); // 空列表访问
        } catch (Exception e) {
            System.out.println("空列表访问异常: " + e.getMessage());
        }

        list.add(1); // 添加一个元素
        list.add(2); // 添加第二个元素
        System.out.println("添加两个元素后:");
        list.printList();

        list.delete(0); // 删除第一个元素
        list.delete(0); // 删除第二个元素
        System.out.println("删除所有元素后:");
        list.printList();

        // 性能测试
        System.out.println("\n=== 性能测试 ===");

        UnrolledLinkedList largeList = new UnrolledLinkedList(16);

        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            largeList.add(i);
        }
        long insertTime = System.nanoTime() - startTime;

        System.out.println("插入10000个元素时间: " + insertTime / 1_000_000.0 + " ms");
        System.out.println("链表大小: " + largeList.getSize());

        // 测试随机访问
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            largeList.get(i * 10);
        }
        long accessTime = System.nanoTime() - startTime;

        System.out.println("1000次随机访问时间: " + accessTime / 1_000_000.0 + " ms");
    }
}
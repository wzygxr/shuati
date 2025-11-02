package class185;

import java.util.*;

/**
 * 高级数据结构实现 - Java版本
 * 包含：
 * 1. 斐波那契堆 (Fibonacci Heap)
 * 2. 块状链表 (Block Linked List / Unrolled Linked List)
 * 
 * 算法复杂度分析：
 * - 斐波那契堆：
 *   - insert: O(1) 均摊时间复杂度
 *   - extractMin: O(log n) 均摊时间复杂度
 *   - decreaseKey: O(1) 均摊时间复杂度
 *   - delete: O(log n) 均摊时间复杂度
 *   - merge: O(1) 时间复杂度
 * 
 * - 块状链表：
 *   - insert/delete: O(n/b) 均摊时间复杂度，其中b是块大小
 *   - get/set: O(n/b) 时间复杂度
 *   - 空间复杂度：O(n)
 */
public class AdvancedDataStructures {
    
    // ================================
    // 1. 斐波那契堆 (Fibonacci Heap)
    // ================================
    
    /**
     * 斐波那契堆节点类
     */
    public static class FibonacciHeapNode<T> {
        T key;                 // 节点键值
        double priority;       // 优先级（用于排序）
        int degree;            // 节点的度数（子节点数量）
        boolean marked;        // 是否被标记（用于级联剪枝）
        FibonacciHeapNode<T> parent;      // 父节点
        FibonacciHeapNode<T> child;       // 第一个子节点
        FibonacciHeapNode<T> left;        // 左侧兄弟节点
        FibonacciHeapNode<T> right;       // 右侧兄弟节点
        
        /**
         * 构造函数
         * @param key 节点键值
         * @param priority 节点优先级
         */
        public FibonacciHeapNode(T key, double priority) {
            this.key = key;
            this.priority = priority;
            this.degree = 0;
            this.marked = false;
            this.parent = null;
            this.child = null;
            // 初始化为自环双向链表
            this.left = this;
            this.right = this;
        }
    }
    
    /**
     * 斐波那契堆实现
     * 支持高效的优先队列操作
     */
    public static class FibonacciHeap<T> {
        private FibonacciHeapNode<T> minNode;  // 指向最小节点
        private int size;                      // 堆中节点数量
        
        /**
         * 构造空堆
         */
        public FibonacciHeap() {
            this.minNode = null;
            this.size = 0;
        }
        
        /**
         * 检查堆是否为空
         * @return 堆是否为空
         */
        public boolean isEmpty() {
            return minNode == null;
        }
        
        /**
         * 获取堆中元素数量
         * @return 元素数量
         */
        public int size() {
            return size;
        }
        
        /**
         * 插入新节点到堆中
         * 时间复杂度：O(1) 均摊
         * @param key 节点键值
         * @param priority 节点优先级
         * @return 新插入的节点
         */
        public FibonacciHeapNode<T> insert(T key, double priority) {
            FibonacciHeapNode<T> newNode = new FibonacciHeapNode<>(key, priority);
            
            // 将新节点添加到根链表
            if (minNode == null) {
                // 空堆情况
                minNode = newNode;
            } else {
                // 将新节点插入到根链表的minNode旁边
                linkRootList(newNode, minNode);
                
                // 更新最小节点
                if (newNode.priority < minNode.priority) {
                    minNode = newNode;
                }
            }
            
            // 增加节点计数
            size++;
            return newNode;
        }
        
        /**
         * 合并两个斐波那契堆
         * 时间复杂度：O(1)
         * @param other 要合并的另一个堆
         */
        public void merge(FibonacciHeap<T> other) {
            if (other == null || other.isEmpty()) {
                return;  // 空堆无需合并
            }
            
            if (this.isEmpty()) {
                // 如果当前堆为空，直接接管other的minNode
                this.minNode = other.minNode;
                this.size = other.size;
                return;
            }
            
            // 合并两个根链表
            FibonacciHeapNode<T> thisRight = this.minNode.right;
            FibonacciHeapNode<T> otherLeft = other.minNode.left;
            
            this.minNode.right = other.minNode;
            other.minNode.left = this.minNode;
            
            thisRight.left = otherLeft;
            otherLeft.right = thisRight;
            
            // 更新最小节点
            if (other.minNode.priority < this.minNode.priority) {
                this.minNode = other.minNode;
            }
            
            // 更新节点数量
            this.size += other.size;
            
            // 重置other堆，避免悬空引用
            other.minNode = null;
            other.size = 0;
        }
        
        /**
         * 提取堆中的最小节点
         * 时间复杂度：O(log n) 均摊
         * @return 最小节点的键值，如果堆为空返回null
         */
        public T extractMin() {
            if (isEmpty()) {
                return null;
            }
            
            FibonacciHeapNode<T> min = minNode;
            
            // 将min的所有子节点提升到根链表
            if (min.child != null) {
                FibonacciHeapNode<T> child = min.child;
                do {
                    FibonacciHeapNode<T> nextChild = child.right;
                    
                    // 从子链表中移除child
                    removeFromChildList(child);
                    
                    // 添加到根链表
                    child.parent = null;
                    linkRootList(child, minNode);
                    
                    child = nextChild;
                } while (child != min.child);
                
                // 清除min的子节点引用
                min.child = null;
            }
            
            // 从根链表中移除min
            if (min.right == min) {
                // 根链表中只有一个节点
                minNode = null;
            } else {
                // 更新根链表
                minNode = min.right;  // 暂时将min的右侧设为新的minNode
                removeFromRootList(min);
                
                // 合并相同度数的树
                consolidate();
            }
            
            // 减少节点计数
            size--;
            
            return min.key;
        }
        
        /**
         * 减小节点的优先级
         * 时间复杂度：O(1) 均摊
         * @param node 要修改的节点
         * @param newPriority 新的优先级
         * @throws IllegalArgumentException 如果新优先级大于当前优先级
         */
        public void decreaseKey(FibonacciHeapNode<T> node, double newPriority) {
            if (newPriority > node.priority) {
                throw new IllegalArgumentException("New priority cannot be greater than current priority");
            }
            
            node.priority = newPriority;
            FibonacciHeapNode<T> parent = node.parent;
            
            // 如果节点在根链表中，或者父节点的优先级不大于当前节点，无需其他操作
            if (parent == null || parent.priority <= node.priority) {
                // 如果是根链表中的节点且优先级比当前minNode小，更新minNode
                if (parent == null && node.priority < minNode.priority) {
                    minNode = node;
                }
                return;
            }
            
            // 否则，需要进行级联剪枝操作
            cut(node, parent);
            cascadingCut(parent);
        }
        
        /**
         * 删除指定节点
         * 时间复杂度：O(log n) 均摊
         * @param node 要删除的节点
         */
        public void delete(FibonacciHeapNode<T> node) {
            // 将节点优先级设置为负无穷，使其成为新的最小节点
            decreaseKey(node, Double.NEGATIVE_INFINITY);
            
            // 提取最小节点（即刚刚被设置为负无穷的节点）
            extractMin();
        }
        
        /**
         * 获取最小节点（不移除）
         * 时间复杂度：O(1)
         * @return 最小节点的键值，如果堆为空返回null
         */
        public T getMin() {
            return isEmpty() ? null : minNode.key;
        }
        
        // ==================== 辅助方法 ====================
        
        /**
         * 将节点链接到根链表
         */
        private void linkRootList(FibonacciHeapNode<T> node, FibonacciHeapNode<T> root) {
            // 在根和根的右侧节点之间插入node
            node.right = root.right;
            node.left = root;
            root.right.left = node;
            root.right = node;
        }
        
        /**
         * 从根链表中移除节点
         */
        private void removeFromRootList(FibonacciHeapNode<T> node) {
            node.left.right = node.right;
            node.right.left = node.left;
        }
        
        /**
         * 从子链表中移除节点
         */
        private void removeFromChildList(FibonacciHeapNode<T> node) {
            if (node.parent.child == node) {
                // 如果是父节点的第一个子节点，更新父节点的child指针
                if (node.right != node) {
                    node.parent.child = node.right;
                } else {
                    node.parent.child = null;
                }
            }
            
            // 更新子链表中的双向链接
            node.left.right = node.right;
            node.right.left = node.left;
        }
        
        /**
         * 将一个节点作为另一个节点的子节点
         */
        private void linkAsChild(FibonacciHeapNode<T> child, FibonacciHeapNode<T> parent) {
            // 从根链表中移除child
            removeFromRootList(child);
            
            // 重置child的状态
            child.parent = parent;
            child.marked = false;
            
            // 将child添加到parent的子链表中
            if (parent.child == null) {
                // parent没有子节点
                parent.child = child;
                child.left = child;
                child.right = child;
            } else {
                // 将child插入到parent的第一个子节点旁边
                child.right = parent.child.right;
                child.left = parent.child;
                parent.child.right.left = child;
                parent.child.right = child;
            }
            
            // 增加parent的度数
            parent.degree++;
        }
        
        /**
         * 合并相同度数的树
         */
        private void consolidate() {
            // 计算最大可能的度数，理论上不会超过log_phi(n)，其中phi是黄金分割比
            int maxDegree = (int) Math.floor(Math.log(size) / Math.log((1 + Math.sqrt(5)) / 2)) + 1;
            
            // 用于存储不同度数的根节点
            @SuppressWarnings("unchecked")
            FibonacciHeapNode<T>[] degreeTable = new FibonacciHeapNode[maxDegree];
            
            // 遍历所有根节点
            FibonacciHeapNode<T> start = minNode;
            FibonacciHeapNode<T> current = start;
            boolean isVisited;
            
            do {
                isVisited = false;
                int degree = current.degree;
                FibonacciHeapNode<T> next = current.right;
                
                // 合并相同度数的树
                while (degreeTable[degree] != null) {
                    FibonacciHeapNode<T> other = degreeTable[degree];
                    
                    // 确保current的优先级不大于other
                    if (current.priority > other.priority) {
                        FibonacciHeapNode<T> temp = current;
                        current = other;
                        other = temp;
                    }
                    
                    // 将other作为current的子节点
                    linkAsChild(other, current);
                    
                    // 清除度数表中的条目
                    degreeTable[degree] = null;
                    degree++;
                }
                
                // 记录当前度数的根节点
                degreeTable[degree] = current;
                
                // 移动到下一个根节点
                current = next;
                
                // 检查是否已经遍历完所有根节点
                if (current == start) {
                    isVisited = true;
                }
            } while (!isVisited);
            
            // 重建根链表并找到新的最小节点
            minNode = null;
            
            for (int i = 0; i < maxDegree; i++) {
                if (degreeTable[i] != null) {
                    // 初始化根链表
                    if (minNode == null) {
                        minNode = degreeTable[i];
                        minNode.left = minNode;
                        minNode.right = minNode;
                    } else {
                        // 将节点添加到根链表
                        linkRootList(degreeTable[i], minNode);
                        
                        // 更新最小节点
                        if (degreeTable[i].priority < minNode.priority) {
                            minNode = degreeTable[i];
                        }
                    }
                }
            }
        }
        
        /**
         * 剪切操作：将节点从父节点的子树中移除并添加到根链表
         */
        private void cut(FibonacciHeapNode<T> node, FibonacciHeapNode<T> parent) {
            // 从父节点的子链表中移除node
            removeFromChildList(node);
            
            // 减少父节点的度数
            parent.degree--;
            
            // 将node添加到根链表
            node.parent = null;
            node.marked = false;
            linkRootList(node, minNode);
        }
        
        /**
         * 级联剪切操作
         */
        private void cascadingCut(FibonacciHeapNode<T> node) {
            FibonacciHeapNode<T> parent = node.parent;
            
            if (parent != null) {
                if (!node.marked) {
                    // 如果节点未被标记，标记它
                    node.marked = true;
                } else {
                    // 如果节点已被标记，进行剪切并继续级联
                    cut(node, parent);
                    cascadingCut(parent);
                }
            }
        }
        
        /**
         * 打印堆的结构（用于调试）
         */
        public void printHeap() {
            if (isEmpty()) {
                System.out.println("Heap is empty");
                return;
            }
            
            System.out.println("Fibonacci Heap Structure:");
            Set<FibonacciHeapNode<T>> visited = new HashSet<>();
            printNode(minNode, 0, visited);
        }
        
        /**
         * 递归打印节点及其子节点
         */
        private void printNode(FibonacciHeapNode<T> node, int level, Set<FibonacciHeapNode<T>> visited) {
            if (node == null || visited.contains(node)) {
                return;
            }
            
            visited.add(node);
            
            // 打印缩进
            for (int i = 0; i < level; i++) {
                System.out.print("  ");
            }
            
            // 打印节点信息
            System.out.println("Key: " + node.key + ", Priority: " + node.priority + ", Degree: " + node.degree + ", Marked: " + node.marked);
            
            // 递归打印子节点
            if (node.child != null) {
                FibonacciHeapNode<T> child = node.child;
                do {
                    printNode(child, level + 1, visited);
                    child = child.right;
                } while (child != node.child && !visited.contains(child));
            }
            
            // 递归打印根链表中的下一个节点
            FibonacciHeapNode<T> next = node.right;
            if (next != minNode && !visited.contains(next)) {
                printNode(next, level, visited);
            }
        }
    }
    
    // ================================
    // 2. 块状链表 (Unrolled Linked List)
    // ================================
    
    /**
     * 块状链表的块类
     */
    public static class Block<T> {
        private T[] array;      // 块内的数组
        private int size;       // 当前块中元素的数量
        private Block<T> next;  // 指向下一个块
        private Block<T> prev;  // 指向上一个块
        private final int capacity;  // 块的最大容量
        
        /**
         * 构造函数
         * @param capacity 块的最大容量
         */
        @SuppressWarnings("unchecked")
        public Block(int capacity) {
            this.capacity = capacity;
            this.array = (T[]) new Object[capacity];
            this.size = 0;
            this.next = null;
            this.prev = null;
        }
        
        /**
         * 检查块是否已满
         * @return 块是否已满
         */
        public boolean isFull() {
            return size == capacity;
        }
        
        /**
         * 检查块是否为空
         * @return 块是否为空
         */
        public boolean isEmpty() {
            return size == 0;
        }
        
        /**
         * 获取块的大小
         * @return 块中元素的数量
         */
        public int size() {
            return size;
        }
        
        /**
         * 获取块的容量
         * @return 块的最大容量
         */
        public int capacity() {
            return capacity;
        }
        
        /**
         * 在块的末尾添加元素
         * @param value 要添加的值
         * @throws IllegalStateException 如果块已满
         */
        public void add(T value) {
            if (isFull()) {
                throw new IllegalStateException("Block is full");
            }
            array[size++] = value;
        }
        
        /**
         * 在指定位置插入元素
         * @param index 插入位置
         * @param value 要插入的值
         * @throws IndexOutOfBoundsException 如果索引无效
         * @throws IllegalStateException 如果块已满
         */
        public void insert(int index, T value) {
            if (isFull()) {
                throw new IllegalStateException("Block is full");
            }
            
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            // 移动元素为新元素腾出空间
            System.arraycopy(array, index, array, index + 1, size - index);
            array[index] = value;
            size++;
        }
        
        /**
         * 删除指定位置的元素
         * @param index 要删除的元素位置
         * @return 被删除的元素
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public T delete(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            T value = array[index];
            
            // 移动元素覆盖被删除的元素
            System.arraycopy(array, index + 1, array, index, size - index - 1);
            array[--size] = null;  // 清除引用，便于GC
            
            return value;
        }
        
        /**
         * 获取指定位置的元素
         * @param index 元素位置
         * @return 元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            return array[index];
        }
        
        /**
         * 设置指定位置的元素值
         * @param index 元素位置
         * @param value 新的元素值
         * @return 原来的元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public T set(int index, T value) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            T oldValue = array[index];
            array[index] = value;
            return oldValue;
        }
        
        /**
         * 分割块
         * 将当前块从指定位置分割，返回包含后半部分元素的新块
         * @param splitIndex 分割位置
         * @return 包含后半部分元素的新块
         * @throws IndexOutOfBoundsException 如果分割位置无效
         */
        public Block<T> split(int splitIndex) {
            if (splitIndex < 0 || splitIndex > size) {
                throw new IndexOutOfBoundsException("Split index out of bounds: " + splitIndex);
            }
            
            // 创建新块
            Block<T> newBlock = new Block<>(capacity);
            
            // 复制后半部分元素到新块
            int elementsToMove = size - splitIndex;
            for (int i = 0; i < elementsToMove; i++) {
                newBlock.array[i] = array[splitIndex + i];
                array[splitIndex + i] = null;  // 清除引用
            }
            
            // 更新块大小
            newBlock.size = elementsToMove;
            this.size = splitIndex;
            
            // 建立双向链接
            newBlock.next = this.next;
            if (this.next != null) {
                this.next.prev = newBlock;
            }
            this.next = newBlock;
            newBlock.prev = this;
            
            return newBlock;
        }
        
        /**
         * 合并两个相邻块
         * 假设当前块和next块是相邻的
         * @return 合并后的块（即当前块）
         * @throws IllegalStateException 如果没有下一个块或合并后超出容量
         */
        public Block<T> mergeNext() {
            if (next == null) {
                throw new IllegalStateException("No next block to merge");
            }
            
            if (this.size + next.size > this.capacity) {
                throw new IllegalStateException("Merged size exceeds block capacity");
            }
            
            // 复制next块的元素到当前块
            System.arraycopy(next.array, 0, this.array, this.size, next.size);
            this.size += next.size;
            
            // 更新链接，跳过next块
            Block<T> nextNext = next.next;
            this.next = nextNext;
            if (nextNext != null) {
                nextNext.prev = this;
            }
            
            return this;
        }
    }
    
    /**
     * 块状链表实现
     * 使用块存储元素，优化内存使用和遍历性能
     */
    public static class UnrolledLinkedList<T> {
        private Block<T> head;  // 头块指针
        private Block<T> tail;  // 尾块指针
        private int size;       // 链表元素总数
        private final int blockCapacity;  // 块的最大容量
        
        /**
         * 构造函数，使用默认块容量（通常为sqrt(n)，这里使用16作为示例）
         */
        public UnrolledLinkedList() {
            this(16);  // 默认块容量为16
        }
        
        /**
         * 构造函数
         * @param blockCapacity 块的最大容量
         * @throws IllegalArgumentException 如果块容量小于2
         */
        public UnrolledLinkedList(int blockCapacity) {
            if (blockCapacity < 2) {
                throw new IllegalArgumentException("Block capacity must be at least 2");
            }
            
            this.blockCapacity = blockCapacity;
            this.head = null;
            this.tail = null;
            this.size = 0;
        }
        
        /**
         * 检查链表是否为空
         * @return 链表是否为空
         */
        public boolean isEmpty() {
            return size == 0;
        }
        
        /**
         * 获取链表中元素的数量
         * @return 元素数量
         */
        public int size() {
            return size;
        }
        
        /**
         * 在链表末尾添加元素
         * 时间复杂度：O(n/b) 均摊，其中b是块容量
         * @param value 要添加的值
         */
        public void add(T value) {
            if (isEmpty()) {
                // 空链表，创建第一个块
                head = new Block<>(blockCapacity);
                tail = head;
                head.add(value);
            } else {
                // 非空链表，检查尾块是否已满
                if (tail.isFull()) {
                    // 尾块已满，分割为两个半满的块
                    tail.split(tail.size() / 2);
                    tail = tail.next;  // 更新尾块指针
                }
                tail.add(value);
            }
            size++;
        }
        
        /**
         * 在指定位置插入元素
         * 时间复杂度：O(n/b)
         * @param index 插入位置
         * @param value 要插入的值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public void insert(int index, T value) {
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
                head = new Block<>(blockCapacity);
                tail = head;
                head.add(value);
            } else {
                // 定位到包含插入位置的块和块内索引
                BlockPosition<T> pos = findBlockAndIndex(index);
                Block<T> block = pos.block;
                int blockIndex = pos.index;
                
                // 检查块是否已满
                if (block.isFull()) {
                    // 块已满，分割为两个半满的块
                    int splitIndex = block.size() / 2;
                    Block<T> newBlock = block.split(splitIndex);
                    
                    // 调整插入位置
                    if (blockIndex >= splitIndex) {
                        block = newBlock;
                        blockIndex -= splitIndex;
                    }
                }
                
                // 在块中插入元素
                block.insert(blockIndex, value);
                
                // 更新尾块指针（如果需要）
                Block<T> lastBlock = head;
                while (lastBlock.next != null) {
                    lastBlock = lastBlock.next;
                }
                tail = lastBlock;
            }
            
            size++;
        }
        
        /**
         * 删除指定位置的元素
         * 时间复杂度：O(n/b)
         * @param index 要删除的元素位置
         * @return 被删除的元素
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public T delete(int index) {
            if (isEmpty()) {
                throw new IllegalStateException("Cannot delete from empty list");
            }
            
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            // 定位到包含删除位置的块和块内索引
            BlockPosition<T> pos = findBlockAndIndex(index);
            Block<T> block = pos.block;
            int blockIndex = pos.index;
            
            // 保存要删除的元素值
            T value = block.delete(blockIndex);
            
            // 如果删除后块的大小过小，尝试与相邻块合并（保持块的大小在合理范围）
            if (block.size() < blockCapacity / 4 && block != head || 
                block.isEmpty() && size > 0) {  // 特殊处理空块
                
                // 优先与前一个块合并
                if (block.prev != null) {
                    Block<T> prevBlock = block.prev;
                    // 确保合并后不会超出容量
                    if (prevBlock.size() + block.size() <= blockCapacity) {
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
                } 
                // 否则与后一个块合并
                else if (block.next != null) {
                    Block<T> nextBlock = block.next;
                    if (block.size() + nextBlock.size() <= blockCapacity) {
                        if (nextBlock == tail) {
                            tail = block;
                        }
                        block.mergeNext();
                    }
                }
                // 特殊情况：只剩一个空块
                else if (block.isEmpty()) {
                    head = null;
                    tail = null;
                }
            }
            
            size--;
            return value;
        }
        
        /**
         * 获取指定位置的元素
         * 时间复杂度：O(n/b)
         * @param index 元素位置
         * @return 元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public T get(int index) {
            if (isEmpty()) {
                throw new IllegalStateException("List is empty");
            }
            
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            BlockPosition<T> pos = findBlockAndIndex(index);
            return pos.block.get(pos.index);
        }
        
        /**
         * 设置指定位置的元素值
         * 时间复杂度：O(n/b)
         * @param index 元素位置
         * @param value 新的元素值
         * @return 原来的元素值
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public T set(int index, T value) {
            if (isEmpty()) {
                throw new IllegalStateException("List is empty");
            }
            
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            BlockPosition<T> pos = findBlockAndIndex(index);
            return pos.block.set(pos.index, value);
        }
        
        /**
         * 清空链表
         * 时间复杂度：O(n)
         */
        public void clear() {
            head = null;
            tail = null;
            size = 0;
        }
        
        /**
         * 将链表内容转换为数组
         * 时间复杂度：O(n)
         * @return 包含链表所有元素的数组
         */
        @SuppressWarnings("unchecked")
        public T[] toArray() {
            if (isEmpty()) {
                return (T[]) new Object[0];
            }
            
            T[] result = (T[]) new Object[size];
            int index = 0;
            Block<T> current = head;
            
            while (current != null) {
                for (int i = 0; i < current.size(); i++) {
                    result[index++] = current.get(i);
                }
                current = current.next;
            }
            
            return result;
        }
        
        /**
         * 查找第一个出现的指定值的索引
         * 时间复杂度：O(n)
         * @param value 要查找的值
         * @return 元素索引，如果未找到返回-1
         */
        public int indexOf(T value) {
            if (isEmpty()) {
                return -1;
            }
            
            int index = 0;
            Block<T> current = head;
            
            while (current != null) {
                for (int i = 0; i < current.size(); i++) {
                    if (Objects.equals(current.get(i), value)) {
                        return index + i;
                    }
                }
                index += current.size();
                current = current.next;
            }
            
            return -1;
        }
        
        /**
         * 查找最后一个出现的指定值的索引
         * 时间复杂度：O(n)
         * @param value 要查找的值
         * @return 元素索引，如果未找到返回-1
         */
        public int lastIndexOf(T value) {
            if (isEmpty()) {
                return -1;
            }
            
            int index = size - 1;
            Block<T> current = tail;
            int currentBlockSize = current.size();
            
            while (current != null) {
                for (int i = currentBlockSize - 1; i >= 0; i--) {
                    if (Objects.equals(current.get(i), value)) {
                        return index - (currentBlockSize - 1 - i);
                    }
                }
                
                index -= currentBlockSize;
                current = current.prev;
                currentBlockSize = (current != null) ? current.size() : 0;
            }
            
            return -1;
        }
        
        /**
         * 检查链表是否包含指定值
         * 时间复杂度：O(n)
         * @param value 要检查的值
         * @return 是否包含该值
         */
        public boolean contains(T value) {
            return indexOf(value) != -1;
        }
        
        /**
         * 范围查询：获取从start到end（不包含）的子列表
         * 时间复杂度：O(n/b + k)，其中k是子列表的大小
         * @param start 起始索引（包含）
         * @param end 结束索引（不包含）
         * @return 子列表
         * @throws IndexOutOfBoundsException 如果索引无效
         */
        public UnrolledLinkedList<T> subList(int start, int end) {
            if (start < 0 || end > size || start > end) {
                throw new IndexOutOfBoundsException("Invalid range: [" + start + ", " + end + ")");
            }
            
            UnrolledLinkedList<T> sublist = new UnrolledLinkedList<>(blockCapacity);
            
            if (start == end) {
                return sublist;  // 空的子列表
            }
            
            // 处理跨越多个块的情况
            int currentIndex = start;
            while (currentIndex < end) {
                sublist.add(get(currentIndex));
                currentIndex++;
            }
            
            return sublist;
        }
        
        /**
         * 打印链表内容
         * 时间复杂度：O(n)
         */
        public void printList() {
            if (isEmpty()) {
                System.out.println("List is empty");
                return;
            }
            
            System.out.print("UnrolledLinkedList: [");
            Block<T> current = head;
            boolean firstElement = true;
            
            while (current != null) {
                for (int i = 0; i < current.size(); i++) {
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
        
        /**
         * 打印链表的块结构（用于调试）
         */
        public void printBlockStructure() {
            if (isEmpty()) {
                System.out.println("List is empty");
                return;
            }
            
            System.out.println("Block Structure:");
            int blockIndex = 0;
            Block<T> current = head;
            
            while (current != null) {
                System.out.print("Block " + blockIndex + " (size=" + current.size() + "): [");
                
                for (int i = 0; i < current.size(); i++) {
                    System.out.print(current.get(i));
                    if (i < current.size() - 1) {
                        System.out.print(", ");
                    }
                }
                
                System.out.println("]");
                
                current = current.next;
                blockIndex++;
            }
        }
        
        // ==================== 内部辅助类和方法 ====================
        
        /**
         * 存储块指针和块内索引的内部类
         */
        private static class BlockPosition<T> {
            Block<T> block;
            int index;
            
            BlockPosition(Block<T> block, int index) {
                this.block = block;
                this.index = index;
            }
        }
        
        /**
         * 查找包含指定索引的块和块内索引
         * 时间复杂度：O(n/b)
         * @param index 元素索引
         * @return 包含块和块内索引的BlockPosition对象
         */
        private BlockPosition<T> findBlockAndIndex(int index) {
            if (isEmpty() || index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            // 优化：根据索引位置选择从头还是从尾开始查找
            // 如果索引更靠近头部，从头开始
            if (index < size / 2) {
                Block<T> current = head;
                int currentIndex = 0;
                
                while (current != null) {
                    if (index < currentIndex + current.size()) {
                        // 找到了包含索引的块
                        return new BlockPosition<>(current, index - currentIndex);
                    }
                    currentIndex += current.size();
                    current = current.next;
                }
            } 
            // 否则从尾开始
            else {
                Block<T> current = tail;
                int currentIndex = size - 1;
                int currentBlockSize = current.size();
                
                while (current != null) {
                    if (index >= currentIndex - currentBlockSize + 1) {
                        // 找到了包含索引的块
                        return new BlockPosition<>(current, index - (currentIndex - currentBlockSize + 1));
                    }
                    currentIndex -= currentBlockSize;
                    current = current.prev;
                    currentBlockSize = (current != null) ? current.size() : 0;
                }
            }
            
            // 不应该到达这里
            throw new IndexOutOfBoundsException("Index not found: " + index);
        }
    }
    
    // ================================
    // 主方法 - 测试数据结构
    // ================================
    
    public static void main(String[] args) {
        // 测试斐波那契堆
        testFibonacciHeap();
        
        // 测试块状链表
        testUnrolledLinkedList();
    }
    
    /**
     * 测试斐波那契堆的各种操作
     */
    private static void testFibonacciHeap() {
        System.out.println("=== 测试斐波那契堆 ===");
        FibonacciHeap<String> heap = new FibonacciHeap<>();
        
        // 测试插入操作
        System.out.println("\n1. 测试插入操作:");
        FibonacciHeapNode<String> node1 = heap.insert("Task 1", 5);
        FibonacciHeapNode<String> node2 = heap.insert("Task 2", 3);
        FibonacciHeapNode<String> node3 = heap.insert("Task 3", 8);
        FibonacciHeapNode<String> node4 = heap.insert("Task 4", 1);
        FibonacciHeapNode<String> node5 = heap.insert("Task 5", 10);
        
        System.out.println("插入5个节点后，最小节点: " + heap.getMin());  // 应该是 Task 4
        
        // 测试提取最小节点
        System.out.println("\n2. 测试提取最小节点:");
        String min1 = heap.extractMin();
        System.out.println("提取的最小节点: " + min1);  // 应该是 Task 4
        System.out.println("提取后，最小节点: " + heap.getMin());  // 应该是 Task 2
        
        // 测试减小键值
        System.out.println("\n3. 测试减小键值:");
        heap.decreaseKey(node3, 2);
        System.out.println("减小Task 3的优先级后，最小节点: " + heap.getMin());  // 应该是 Task 3
        
        // 测试删除节点
        System.out.println("\n4. 测试删除节点:");
        heap.delete(node5);
        System.out.println("删除Task 5后，最小节点: " + heap.getMin());  // 仍然是 Task 3
        
        // 测试合并操作
        System.out.println("\n5. 测试合并操作:");
        FibonacciHeap<String> heap2 = new FibonacciHeap<>();
        heap2.insert("Task A", 4);
        heap2.insert("Task B", 6);
        
        heap.merge(heap2);
        System.out.println("合并两个堆后，最小节点: " + heap.getMin());  // 仍然是 Task 3
        System.out.println("堆大小: " + heap.size());  // 应该是 5
        
        // 测试提取所有元素
        System.out.println("\n6. 测试提取所有元素:");
        System.out.print("按优先级提取顺序: ");
        while (!heap.isEmpty()) {
            System.out.print(heap.extractMin() + " ");
        }
        System.out.println();
        
        // 测试边界情况
        System.out.println("\n7. 测试边界情况:");
        System.out.println("空堆获取最小节点: " + heap.getMin());  // 应该是 null
        System.out.println("空堆提取最小节点: " + heap.extractMin());  // 应该是 null
    }
    
    /**
     * 测试块状链表的各种操作
     */
    private static void testUnrolledLinkedList() {
        System.out.println("\n=== 测试块状链表 ===");
        // 使用较小的块容量以便更容易观察块分割和合并
        UnrolledLinkedList<Integer> list = new UnrolledLinkedList<>(4);
        
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
        System.out.println("索引5的值: " + list.get(5));  // 应该是 6
        int oldValue = list.set(5, 100);
        System.out.println("设置索引5的值为100，旧值: " + oldValue);
        System.out.println("索引5的新值: " + list.get(5));  // 应该是 100
        list.printList();
        
        // 测试插入操作
        System.out.println("\n3. 测试插入操作:");
        list.insert(3, 50);  // 在索引3插入50
        System.out.println("在索引3插入50后:");
        list.printList();
        list.printBlockStructure();
        
        list.insert(0, 0);  // 在头部插入0
        System.out.println("在头部插入0后:");
        list.printList();
        
        // 测试删除操作
        System.out.println("\n4. 测试删除操作:");
        int deletedValue = list.delete(5);  // 删除索引5的值
        System.out.println("删除索引5的值: " + deletedValue);
        System.out.println("删除后:");
        list.printList();
        list.printBlockStructure();
        
        list.delete(0);  // 删除头部
        System.out.println("删除头部后:");
        list.printList();
        
        // 测试查找操作
        System.out.println("\n5. 测试查找操作:");
        System.out.println("值为100的索引: " + list.indexOf(100));
        System.out.println("值为99的索引: " + list.indexOf(99));  // 应该是 -1
        System.out.println("列表是否包含50: " + list.contains(50));
        
        // 测试子列表
        System.out.println("\n6. 测试子列表:");
        UnrolledLinkedList<Integer> sublist = list.subList(2, 6);
        System.out.println("子列表[2,6):");
        sublist.printList();
        
        // 测试清空操作
        System.out.println("\n7. 测试清空操作:");
        list.clear();
        System.out.println("清空后:");
        list.printList();
        System.out.println("列表大小: " + list.size());
        
        // 测试边界情况
        System.out.println("\n8. 测试边界情况:");
        try {
            list.get(0);  // 空列表访问
        } catch (Exception e) {
            System.out.println("空列表访问异常: " + e.getMessage());
        }
        
        list.add(1);  // 添加一个元素
        list.add(2);  // 添加第二个元素
        System.out.println("添加两个元素后:");
        list.printList();
        
        list.delete(0);  // 删除第一个元素
        list.delete(0);  // 删除第二个元素
        System.out.println("删除所有元素后:");
        list.printList();
    }
}
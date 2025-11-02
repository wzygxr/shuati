package class185.fibonacci_heap_problems;

import java.util.*;

/**
 * 斐波那契堆实现 (Java版本)
 * 
 * 算法思路：
 * 斐波那契堆是一种高效的优先队列数据结构，支持多种操作的均摊时间复杂度都很优秀。
 * 它通过延迟合并和标记机制来实现高效的性能。
 * 
 * 应用场景：
 * 1. 图算法：最短路径、最小生成树
 * 2. 网络优化：路由算法、流量调度
 * 3. 操作系统：任务调度
 * 
 * 时间复杂度：
 * - 插入：O(1) 均摊
 * - 提取最小：O(log n) 均摊
 * - 减小键值：O(1) 均摊
 * - 删除：O(log n) 均摊
 * - 合并：O(1)
 * 
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 743. 网络延迟时间
 * 2. LeetCode 1584. 连接所有点的最小费用
 * 3. LeetCode 1135. 最低成本联通所有城市
 */
class FibonacciHeapNode {
    Object key;           // 节点键值
    int priority;         // 优先级（用于排序）
    int degree;           // 节点的度数（子节点数量）
    boolean marked;       // 是否被标记（用于级联剪枝）
    FibonacciHeapNode parent;   // 父节点
    FibonacciHeapNode child;    // 第一个子节点
    FibonacciHeapNode left;     // 左侧兄弟节点
    FibonacciHeapNode right;    // 右侧兄弟节点

    FibonacciHeapNode(Object key, int priority) {
        this.key = key;
        this.priority = priority;
        this.degree = 0;
        this.marked = false;
        this.parent = null;
        this.child = null;
        this.left = this;
        this.right = this;
    }
}

public class FibonacciHeap {
    private FibonacciHeapNode minNode;  // 指向最小节点
    private int size;                   // 堆中节点数量

    public FibonacciHeap() {
        this.minNode = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return minNode == null;
    }

    public int getSize() {
        return size;
    }

    public FibonacciHeapNode insert(Object key, int priority) {
        /**
         * 插入新节点到堆中
         * 时间复杂度：O(1) 均摊
         * @param key: 节点键值
         * @param priority: 节点优先级
         * @return: 新插入的节点
         */
        FibonacciHeapNode newNode = new FibonacciHeapNode(key, priority);

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

    public void merge(FibonacciHeap other) {
        /**
         * 合并两个斐波那契堆
         * 时间复杂度：O(1)
         * @param other: 要合并的另一个堆
         */
        if (other == null || other.isEmpty()) {
            return;  // 空堆无需合并
        }

        if (isEmpty()) {
            // 如果当前堆为空，直接接管other的minNode
            minNode = other.minNode;
            size = other.size;
            return;
        }

        if (minNode != null && other.minNode != null) {
            // 合并两个根链表
            FibonacciHeapNode thisRight = minNode.right;
            FibonacciHeapNode otherLeft = other.minNode.left;

            minNode.right = other.minNode;
            other.minNode.left = minNode;

            thisRight.left = otherLeft;
            otherLeft.right = thisRight;

            // 更新最小节点
            if (other.minNode.priority < minNode.priority) {
                minNode = other.minNode;
            }

            // 更新节点数量
            size += other.size;

            // 重置other堆，避免悬空引用
            other.minNode = null;
            other.size = 0;
        }
    }

    public Object extractMin() {
        /**
         * 提取堆中的最小节点
         * 时间复杂度：O(log n) 均摊
         * @return: 最小节点的键值，如果堆为空返回null
         */
        if (isEmpty() || minNode == null) {
            return null;
        }

        FibonacciHeapNode minNodeCopy = minNode;

        // 将min的所有子节点提升到根链表
        if (minNodeCopy.child != null) {
            FibonacciHeapNode child = minNodeCopy.child;
            List<FibonacciHeapNode> children = new ArrayList<>();

            // 收集所有子节点
            FibonacciHeapNode current = child;
            do {
                children.add(current);
                current = current.right;
            } while (current != child);

            // 将所有子节点添加到根链表
            for (FibonacciHeapNode childNode : children) {
                // 从子链表中移除child
                removeFromChildList(childNode);

                // 添加到根链表
                childNode.parent = null;
                linkRootList(childNode, minNode);
            }

            // 清除min的子节点引用
            minNodeCopy.child = null;
        }

        // 从根链表中移除min
        if (minNodeCopy.right == minNodeCopy) {
            // 根链表中只有一个节点
            minNode = null;
        } else {
            // 更新根链表
            minNode = minNodeCopy.right;  // 暂时将min的右侧设为新的minNode
            removeFromRootList(minNodeCopy);

            // 合并相同度数的树
            consolidate();
        }

        // 减少节点计数
        size--;

        return minNodeCopy.key;
    }

    public void decreaseKey(FibonacciHeapNode node, int newPriority) {
        /**
         * 减小节点的优先级
         * 时间复杂度：O(1) 均摊
         * @param node: 要修改的节点
         * @param newPriority: 新的优先级
         * @throws IllegalArgumentException: 如果新优先级大于当前优先级
         */
        if (newPriority > node.priority) {
            throw new IllegalArgumentException("New priority cannot be greater than current priority");
        }

        node.priority = newPriority;
        FibonacciHeapNode parent = node.parent;

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

    public void delete(FibonacciHeapNode node) {
        /**
         * 删除指定节点
         * 时间复杂度：O(log n) 均摊
         * @param node: 要删除的节点
         */
        if (node == null) {
            return;
        }

        // 将节点优先级设置为负无穷，使其成为新的最小节点
        decreaseKey(node, Integer.MIN_VALUE);

        // 提取最小节点（即刚刚被设置为负无穷的节点）
        extractMin();
    }

    public Object getMin() {
        /**
         * 获取最小节点（不移除）
         * 时间复杂度：O(1)
         * @return: 最小节点的键值，如果堆为空返回null
         */
        return isEmpty() ? null : minNode.key;
    }

    // ==================== 辅助方法 ====================

    private void linkRootList(FibonacciHeapNode node, FibonacciHeapNode root) {
        /**
         * 将节点链接到根链表
         */
        // 在根和根的右侧节点之间插入node
        node.right = root.right;
        node.left = root;
        root.right.left = node;
        root.right = node;
    }

    private void removeFromRootList(FibonacciHeapNode node) {
        /**
         * 从根链表中移除节点
         */
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void removeFromChildList(FibonacciHeapNode node) {
        /**
         * 从子链表中移除节点
         */
        if (node.parent == null) {
            return;
        }

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

    private void linkAsChild(FibonacciHeapNode child, FibonacciHeapNode parent) {
        /**
         * 将一个节点作为另一个节点的子节点
         */
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

    private void consolidate() {
        /**
         * 合并相同度数的树
         */
        // 计算最大可能的度数，理论上不会超过log_phi(n)，其中phi是黄金分割比
        int maxDegree = (int) (Math.log(size) / Math.log((1 + Math.sqrt(5)) / 2)) + 1;

        // 用于存储不同度数的根节点
        FibonacciHeapNode[] degreeTable = new FibonacciHeapNode[maxDegree];

        // 遍历所有根节点
        List<FibonacciHeapNode> roots = new ArrayList<>();
        if (minNode != null) {
            FibonacciHeapNode start = minNode;
            FibonacciHeapNode current = start;
            do {
                roots.add(current);
                current = current.right;
            } while (current != start);
        }

        // 处理每个根节点
        for (FibonacciHeapNode current : roots) {
            int degree = current.degree;
            FibonacciHeapNode nextNode = current.right;

            // 合并相同度数的树
            while (degreeTable[degree] != null) {
                FibonacciHeapNode other = degreeTable[degree];

                // 确保current的优先级不大于other
                if (current.priority > other.priority) {
                    FibonacciHeapNode temp = current;
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
        }

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

    private void cut(FibonacciHeapNode node, FibonacciHeapNode parent) {
        /**
         * 剪切操作：将节点从父节点的子树中移除并添加到根链表
         */
        // 从父节点的子链表中移除node
        removeFromChildList(node);

        // 减少父节点的度数
        parent.degree--;

        // 将node添加到根链表
        node.parent = null;
        node.marked = false;
        linkRootList(node, minNode);
    }

    private void cascadingCut(FibonacciHeapNode node) {
        /**
         * 级联剪切操作
         */
        FibonacciHeapNode parent = node.parent;

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

    public void printHeap() {
        /**
         * 打印堆的结构（用于调试）
         */
        if (isEmpty()) {
            System.out.println("Heap is empty");
            return;
        }

        System.out.println("Fibonacci Heap Structure:");
        Set<FibonacciHeapNode> visited = new HashSet<>();
        printNode(minNode, 0, visited);
    }

    private void printNode(FibonacciHeapNode node, int level, Set<FibonacciHeapNode> visited) {
        /**
         * 递归打印节点及其子节点
         */
        if (node == null || visited.contains(node)) {
            return;
        }

        visited.add(node);

        // 打印缩进
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }

        // 打印节点信息
        System.out.println("Key: " + node.key + ", Priority: " + node.priority + 
                          ", Degree: " + node.degree + ", Marked: " + node.marked);

        // 递归打印子节点
        if (node.child != null) {
            FibonacciHeapNode child = node.child;
            do {
                printNode(child, level + 1, visited);
                child = child.right;
            } while (child != node.child);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 测试斐波那契堆 ===");
        FibonacciHeap heap = new FibonacciHeap();

        // 测试插入操作
        System.out.println("\n1. 测试插入操作:");
        FibonacciHeapNode node1 = heap.insert("Task 1", 5);
        FibonacciHeapNode node2 = heap.insert("Task 2", 3);
        FibonacciHeapNode node3 = heap.insert("Task 3", 8);
        FibonacciHeapNode node4 = heap.insert("Task 4", 1);
        FibonacciHeapNode node5 = heap.insert("Task 5", 10);

        System.out.println("插入5个节点后，最小节点: " + heap.getMin());  // 应该是 Task 4

        // 测试提取最小节点
        System.out.println("\n2. 测试提取最小节点:");
        Object min1 = heap.extractMin();
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
        FibonacciHeap heap2 = new FibonacciHeap();
        heap2.insert("Task A", 4);
        heap2.insert("Task B", 6);

        heap.merge(heap2);
        System.out.println("合并两个堆后，最小节点: " + heap.getMin());  // 仍然是 Task 3
        System.out.println("堆大小: " + heap.getSize());  // 应该是 5

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

        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        FibonacciHeap largeHeap = new FibonacciHeap();
        
        long startTime = System.nanoTime();
        List<FibonacciHeapNode> nodes = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int priority = random.nextInt(100000) + 1;
            FibonacciHeapNode node = largeHeap.insert("Task " + i, priority);
            nodes.add(node);
        }
        long insertTime = System.nanoTime() - startTime;
        
        System.out.println("插入10000个节点时间: " + insertTime / 1_000_000.0 + " ms");
        System.out.println("堆大小: " + largeHeap.getSize());
        
        // 测试随机减小键值操作
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            FibonacciHeapNode node = nodes.get(random.nextInt(nodes.size()));
            int newPriority = random.nextInt(100) + 1;
            try {
                heap.decreaseKey(node, newPriority);
            } catch (IllegalArgumentException e) {
                // 忽略无效操作
            }
        }
        long decreaseTime = System.nanoTime() - startTime;
        
        System.out.println("1000次减小键值操作时间: " + decreaseTime / 1_000_000.0 + " ms");
        
        // 测试提取最小操作
        startTime = System.nanoTime();
        int extractedCount = 0;
        while (!largeHeap.isEmpty() && extractedCount < 1000) {
            largeHeap.extractMin();
            extractedCount++;
        }
        long extractTime = System.nanoTime() - startTime;
        
        System.out.println("提取" + extractedCount + "个最小节点时间: " + extractTime / 1_000_000.0 + " ms");
    }
}
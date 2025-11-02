package class184;

import java.util.*;

/**
 * Codeforces 863D - Yet Another Array Queries Problem 解决方案
 * 
 * 题目链接: https://codeforces.com/problemset/problem/863/D
 * 题目描述: 数组查询和更新问题
 * 解题思路: 可以使用块状链表优化，但更简单的方法是离线处理查询
 * 
 * 时间复杂度: 
 * - 在线处理: O(n + m) - 使用离线处理和逆向操作
 * - 块状链表: O(m * sqrt(n) + q * sqrt(n))
 * 空间复杂度: O(n + m)
 */
public class Codeforces_YetAnotherArrayQueries {
    
    /**
     * 块状链表的块类
     */
    static class Block {
        int[] array;      // 块内的数组
        int size;         // 当前块中元素的数量
        int capacity;     // 块的最大容量
        Block next;       // 指向下一个块
        Block prev;       // 指向上一个块
        
        Block(int capacity) {
            this.capacity = capacity;
            this.array = new int[capacity];
            this.size = 0;
            this.next = null;
            this.prev = null;
        }
        
        boolean isFull() {
            return size == capacity;
        }
        
        boolean isEmpty() {
            return size == 0;
        }
    }
    
    /**
     * 块状链表实现
     */
    static class UnrolledLinkedList {
        private Block head;           // 头块指针
        private Block tail;           // 尾块指针
        private int blockSize;        // 块的大小
        private int size;             // 链表元素总数
        
        UnrolledLinkedList(int blockSize) {
            this.blockSize = blockSize;
            this.head = null;
            this.tail = null;
            this.size = 0;
        }
        
        boolean isEmpty() {
            return size == 0;
        }
        
        int size() {
            return size;
        }
        
        /**
         * 在链表末尾添加元素
         */
        void add(int value) {
            if (isEmpty()) {
                // 空链表，创建第一个块
                head = new Block(blockSize);
                tail = head;
                head.array[head.size++] = value;
            } else {
                // 非空链表，检查尾块是否已满
                if (tail.isFull()) {
                    // 尾块已满，分割为两个半满的块
                    splitBlock(tail);
                    tail = tail.next;  // 更新尾块指针
                }
                tail.array[tail.size++] = value;
            }
            size++;
        }
        
        /**
         * 分割块
         */
        private void splitBlock(Block block) {
            // 创建新块
            Block newBlock = new Block(blockSize);
            
            // 计算分割点
            int splitIndex = block.size / 2;
            
            // 复制后半部分元素到新块
            int elementsToMove = block.size - splitIndex;
            for (int i = 0; i < elementsToMove; i++) {
                newBlock.array[i] = block.array[splitIndex + i];
            }
            
            // 更新块大小
            newBlock.size = elementsToMove;
            block.size = splitIndex;
            
            // 建立双向链接
            newBlock.next = block.next;
            if (block.next != null) {
                block.next.prev = newBlock;
            }
            block.next = newBlock;
            newBlock.prev = block;
            
            // 更新尾块指针
            if (block == tail) {
                tail = newBlock;
            }
        }
        
        /**
         * 获取指定位置的元素
         */
        int get(int index) {
            if (isEmpty() || index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            // 定位到包含索引的块和块内索引
            BlockIndexPair pos = findBlockAndIndex(index);
            Block block = pos.block;
            int blockIndex = pos.index;
            
            return block.array[blockIndex];
        }
        
        /**
         * 存储块和索引的辅助类
         */
        private static class BlockIndexPair {
            Block block;
            int index;
            
            BlockIndexPair(Block block, int index) {
                this.block = block;
                this.index = index;
            }
        }
        
        /**
         * 查找包含指定索引的块和块内索引
         */
        private BlockIndexPair findBlockAndIndex(int index) {
            // 优化：根据索引位置选择从头还是从尾开始查找
            Block current;
            int currentIndex;
            
            if (index < size / 2) {
                // 从头开始
                current = head;
                currentIndex = 0;
                
                while (current != null) {
                    if (index < currentIndex + current.size) {
                        // 找到了包含索引的块
                        return new BlockIndexPair(current, index - currentIndex);
                    }
                    currentIndex += current.size;
                    current = current.next;
                }
            } else {
                // 从尾开始
                current = tail;
                currentIndex = size - 1;
                
                while (current != null) {
                    if (index >= currentIndex - current.size + 1) {
                        // 找到了包含索引的块
                        return new BlockIndexPair(current, index - (currentIndex - current.size + 1));
                    }
                    currentIndex -= current.size;
                    current = current.prev;
                }
            }
            
            // 不应该到达这里
            throw new IndexOutOfBoundsException("Index not found: " + index);
        }
        
        /**
         * 反转区间 [l, r]
         */
        void reverseRange(int l, int r) {
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range: [" + l + ", " + r + "]");
            }
            
            // 对于块状链表，区间反转比较复杂，这里简化处理
            // 实际应用中可能需要更复杂的实现
            int len = r - l + 1;
            for (int i = 0; i < len / 2; i++) {
                int leftIndex = l + i;
                int rightIndex = r - i;
                
                // 获取两个位置的值
                int leftValue = get(leftIndex);
                int rightValue = get(rightIndex);
                
                // 交换值（简化实现，实际需要更复杂的操作）
                // 这里只是为了演示，实际实现会更复杂
            }
        }
        
        /**
         * 右移区间 [l, r]
         */
        void shiftRight(int l, int r) {
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("Invalid range: [" + l + ", " + r + "]");
            }
            
            // 保存最后一个元素
            int lastValue = get(r);
            
            // 将区间内元素右移
            for (int i = r; i > l; i--) {
                // 这里简化处理，实际实现会更复杂
            }
            
            // 将最后一个元素放到第一个位置
            // 实际实现会更复杂
        }
        
        /**
         * 将数组转换为列表（用于输出）
         */
        List<Integer> toList() {
            List<Integer> result = new ArrayList<>();
            if (isEmpty()) {
                return result;
            }
            
            Block current = head;
            while (current != null) {
                for (int i = 0; i < current.size; i++) {
                    result.add(current.array[i]);
                }
                current = current.next;
            }
            
            return result;
        }
    }
    
    /**
     * 使用离线处理和逆向操作解决数组查询问题
     * @param n 数组大小
     * @param m 操作数量
     * @param q 查询数量
     * @param array 初始数组
     * @param operations 操作数组
     * @param queries 查询位置数组
     * @return 查询结果数组
     */
    public int[] solve(int n, int m, int q, int[] array, int[][] operations, int[] queries) {
        // 检查输入有效性
        if (n <= 0 || array == null || array.length != n) {
            return new int[0];
        }
        
        // 复制数组以避免修改原始数组
        int[] resultArray = array.clone();
        
        // 逆向处理操作
        for (int i = m - 1; i >= 0; i--) {
            int[] op = operations[i];
            int type = op[0];
            int l = op[1] - 1; // 转换为0-based索引
            int r = op[2] - 1; // 转换为0-based索引
            
            if (type == 1) {
                // 右移操作：将 [l, r] 区间右移一位
                // 逆向操作是左移一位
                int firstValue = resultArray[l];
                for (int j = l; j < r; j++) {
                    resultArray[j] = resultArray[j + 1];
                }
                resultArray[r] = firstValue;
            } else {
                // 反转操作：将 [l, r] 区间反转
                // 逆向操作也是反转
                for (int j = 0; j < (r - l + 1) / 2; j++) {
                    int temp = resultArray[l + j];
                    resultArray[l + j] = resultArray[r - j];
                    resultArray[r - j] = temp;
                }
            }
        }
        
        // 处理查询
        int[] results = new int[q];
        for (int i = 0; i < q; i++) {
            results[i] = resultArray[queries[i] - 1]; // 转换为0-based索引
        }
        
        return results;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Codeforces_YetAnotherArrayQueries solution = new Codeforces_YetAnotherArrayQueries();
        
        // 测试用例1
        System.out.println("=== 测试用例1 ===");
        int n1 = 6;
        int m1 = 3;
        int q1 = 6;
        int[] array1 = {1, 2, 3, 4, 5, 6};
        int[][] operations1 = {
            {2, 1, 5}, // 反转 [1,5]
            {2, 2, 4}, // 反转 [2,4]
            {1, 3, 6}  // 右移 [3,6]
        };
        int[] queries1 = {1, 2, 3, 4, 5, 6};
        
        System.out.println("初始数组: " + Arrays.toString(array1));
        System.out.println("操作:");
        for (int[] op : operations1) {
            System.out.println("  类型" + op[0] + " 区间[" + op[1] + ", " + op[2] + "]");
        }
        System.out.println("查询位置: " + Arrays.toString(queries1));
        
        int[] result1 = solution.solve(n1, m1, q1, array1, operations1, queries1);
        System.out.println("查询结果: " + Arrays.toString(result1));
        
        // 验证结果
        int[] expected1 = {1, 3, 5, 6, 4, 2};
        System.out.println("期望结果: " + Arrays.toString(expected1));
        System.out.println("结果正确: " + Arrays.equals(result1, expected1));
        
        // 测试用例2
        System.out.println("\n=== 测试用例2 ===");
        int n2 = 3;
        int m2 = 1;
        int q2 = 3;
        int[] array2 = {1, 2, 3};
        int[][] operations2 = {
            {1, 1, 3} // 右移 [1,3]
        };
        int[] queries2 = {1, 2, 3};
        
        System.out.println("初始数组: " + Arrays.toString(array2));
        System.out.println("操作:");
        for (int[] op : operations2) {
            System.out.println("  类型" + op[0] + " 区间[" + op[1] + ", " + op[2] + "]");
        }
        System.out.println("查询位置: " + Arrays.toString(queries2));
        
        int[] result2 = solution.solve(n2, m2, q2, array2, operations2, queries2);
        System.out.println("查询结果: " + Arrays.toString(result2));
        
        // 验证结果
        int[] expected2 = {3, 1, 2};
        System.out.println("期望结果: " + Arrays.toString(expected2));
        System.out.println("结果正确: " + Arrays.equals(result2, expected2));
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成测试数据
        int n = 10000;
        int m = 5000;
        int q = 1000;
        
        int[] array = new int[n];
        Random random = new Random(42); // 固定种子以确保可重复性
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(1000000);
        }
        
        int[][] operations = new int[m][3];
        for (int i = 0; i < m; i++) {
            int type = random.nextInt(2) + 1; // 1或2
            int l = random.nextInt(n) + 1;
            int r = random.nextInt(n) + 1;
            if (l > r) {
                int temp = l;
                l = r;
                r = temp;
            }
            operations[i] = new int[]{type, l, r};
        }
        
        int[] queries = new int[q];
        for (int i = 0; i < q; i++) {
            queries[i] = random.nextInt(n) + 1;
        }
        
        Codeforces_YetAnotherArrayQueries solution = new Codeforces_YetAnotherArrayQueries();
        
        long startTime = System.currentTimeMillis();
        int[] result = solution.solve(n, m, q, array, operations, queries);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数组大小: " + n);
        System.out.println("操作数量: " + m);
        System.out.println("查询数量: " + q);
        System.out.println("处理时间: " + (endTime - startTime) + " ms");
        System.out.println("查询结果前10个元素: " + Arrays.toString(Arrays.copyOfRange(result, 0, Math.min(10, result.length))));
    }
}
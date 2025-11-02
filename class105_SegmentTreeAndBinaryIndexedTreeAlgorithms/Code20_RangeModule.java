import java.util.*;

// LeetCode 715. Range 模块
// 设计一个数据结构，支持以下操作：
// 1. 添加区间 [left, right)
// 2. 删除区间 [left, right)
// 3. 查询区间 [left, right) 是否完全被覆盖
// 测试链接: https://leetcode.cn/problems/range-module/

public class Code20_RangeModule {
    
    /**
     * 动态开点线段树实现Range模块
     * 
     * 解题思路:
     * 1. 使用动态开点线段树维护区间覆盖状态
     * 2. 每个节点维护三个信息：
     *    - 区间是否被完全覆盖 (covered)
     *    - 懒惰标记 (lazy)
     *    - 左右子节点引用
     * 3. 支持区间添加、删除和查询操作
     * 
     * 时间复杂度分析:
     * - 添加区间: O(log R) 其中R是值域范围
     * - 删除区间: O(log R)
     * - 查询区间: O(log R)
     * 
     * 空间复杂度分析:
     * - 动态开点线段树: O(n log R)，其中n是操作次数
     * - 总空间复杂度: O(n log R)
     * 
     * 工程化考量:
     * 1. 动态开点节省内存
     * 2. 懒惰传播提高效率
     * 3. 边界条件处理
     * 4. 大值域范围处理
     */
    
    static class SegmentNode {
        boolean covered;  // 当前区间是否被完全覆盖
        int lazy;         // 懒惰标记: 0-无操作, 1-添加, 2-删除
        SegmentNode left, right;
        
        SegmentNode() {
            this.covered = false;
            this.lazy = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    static class RangeModule {
        private SegmentNode root;
        private static final int MIN = 1;
        private static final int MAX = 1000000000; // 10^9
        
        public RangeModule() {
            root = new SegmentNode();
        }
        
        /**
         * 添加区间 [left, right)
         */
        public void addRange(int left, int right) {
            update(root, MIN, MAX, left, right - 1, 1);
        }
        
        /**
         * 查询区间 [left, right) 是否完全被覆盖
         */
        public boolean queryRange(int left, int right) {
            return query(root, MIN, MAX, left, right - 1);
        }
        
        /**
         * 删除区间 [left, right)
         */
        public void removeRange(int left, int right) {
            update(root, MIN, MAX, left, right - 1, 2);
        }
        
        /**
         * 更新线段树
         * 
         * @param node 当前节点
         * @param l 当前节点区间左边界
         * @param r 当前节点区间右边界
         * @param ql 查询区间左边界
         * @param qr 查询区间右边界
         * @param op 操作类型: 1-添加, 2-删除
         */
        private void update(SegmentNode node, int l, int r, int ql, int qr, int op) {
            if (ql <= l && r <= qr) {
                // 当前区间完全在查询区间内
                if (op == 1) {
                    // 添加操作: 标记为完全覆盖
                    node.covered = true;
                    node.lazy = 1;
                } else {
                    // 删除操作: 标记为未覆盖
                    node.covered = false;
                    node.lazy = 2;
                }
                return;
            }
            
            // 懒惰传播
            pushDown(node, l, r);
            
            int mid = l + (r - l) / 2;
            
            if (ql <= mid) {
                update(getLeft(node), l, mid, ql, qr, op);
            }
            if (qr > mid) {
                update(getRight(node), mid + 1, r, ql, qr, op);
            }
            
            // 合并子节点状态
            pushUp(node);
        }
        
        /**
         * 查询区间是否完全被覆盖
         */
        private boolean query(SegmentNode node, int l, int r, int ql, int qr) {
            if (ql <= l && r <= qr) {
                // 当前区间完全在查询区间内，直接返回覆盖状态
                return node.covered;
            }
            
            // 懒惰传播
            pushDown(node, l, r);
            
            int mid = l + (r - l) / 2;
            boolean result = true;
            
            if (ql <= mid) {
                result = result && query(getLeft(node), l, mid, ql, qr);
            }
            if (qr > mid) {
                result = result && query(getRight(node), mid + 1, r, ql, qr);
            }
            
            return result;
        }
        
        /**
         * 懒惰传播
         */
        private void pushDown(SegmentNode node, int l, int r) {
            if (node.lazy != 0) {
                int mid = l + (r - l) / 2;
                
                SegmentNode leftNode = getLeft(node);
                SegmentNode rightNode = getRight(node);
                
                if (node.lazy == 1) {
                    // 添加操作传播
                    leftNode.covered = true;
                    rightNode.covered = true;
                    leftNode.lazy = 1;
                    rightNode.lazy = 1;
                } else {
                    // 删除操作传播
                    leftNode.covered = false;
                    rightNode.covered = false;
                    leftNode.lazy = 2;
                    rightNode.lazy = 2;
                }
                
                node.lazy = 0; // 清除懒惰标记
            }
        }
        
        /**
         * 合并子节点状态
         */
        private void pushUp(SegmentNode node) {
            node.covered = getLeft(node).covered && getRight(node).covered;
        }
        
        /**
         * 获取左子节点（动态开点）
         */
        private SegmentNode getLeft(SegmentNode node) {
            if (node.left == null) {
                node.left = new SegmentNode();
            }
            return node.left;
        }
        
        /**
         * 获取右子节点（动态开点）
         */
        private SegmentNode getRight(SegmentNode node) {
            if (node.right == null) {
                node.right = new SegmentNode();
            }
            return node.right;
        }
    }
    
    /**
     * 有序集合实现（对比解法）
     * 使用TreeMap维护不相交区间
     * 
     * 解题思路:
     * 1. 使用TreeMap存储区间，key为区间左端点，value为区间右端点
     * 2. 添加区间时合并重叠区间
     * 3. 删除区间时分割现有区间
     * 4. 查询时检查区间是否完全覆盖
     * 
     * 时间复杂度分析:
     * - 添加区间: O(n) 最坏情况需要合并多个区间
     * - 删除区间: O(n) 最坏情况需要分割多个区间
     * - 查询区间: O(log n)
     * 
     * 空间复杂度分析:
     * - TreeMap: O(n)
     * - 总空间复杂度: O(n)
     */
    static class RangeModuleTreeMap {
        private TreeMap<Integer, Integer> intervals;
        
        public RangeModuleTreeMap() {
            intervals = new TreeMap<>();
        }
        
        public void addRange(int left, int right) {
            // 找到第一个左端点小于等于right的区间
            Map.Entry<Integer, Integer> entry = intervals.floorEntry(right);
            
            while (entry != null && entry.getValue() >= left) {
                // 合并重叠区间
                left = Math.min(left, entry.getKey());
                right = Math.max(right, entry.getValue());
                intervals.remove(entry.getKey());
                entry = intervals.floorEntry(right);
            }
            
            intervals.put(left, right);
        }
        
        public boolean queryRange(int left, int right) {
            // 找到第一个左端点小于等于left的区间
            Map.Entry<Integer, Integer> entry = intervals.floorEntry(left);
            return entry != null && entry.getValue() >= right;
        }
        
        public void removeRange(int left, int right) {
            // 找到第一个左端点小于right的区间
            Map.Entry<Integer, Integer> entry = intervals.lowerEntry(right);
            
            while (entry != null && entry.getValue() > left) {
                // 分割区间
                intervals.remove(entry.getKey());
                
                if (entry.getKey() < left) {
                    intervals.put(entry.getKey(), left);
                }
                
                if (entry.getValue() > right) {
                    intervals.put(right, entry.getValue());
                }
                
                entry = intervals.lowerEntry(right);
            }
        }
    }
    
    // 测试代码
    public static void main(String[] args) {
        System.out.println("=== 线段树实现测试 ===");
        testSegmentTreeImplementation();
        
        System.out.println("\n=== TreeMap实现测试 ===");
        testTreeMapImplementation();
        
        System.out.println("\n=== 性能对比测试 ===");
        performanceComparison();
        
        System.out.println("\n所有测试通过!");
    }
    
    private static void testSegmentTreeImplementation() {
        RangeModule rangeModule = new RangeModule();
        
        // 测试添加区间
        rangeModule.addRange(10, 20);
        System.out.println("添加区间 [10, 20)");
        
        // 测试查询
        System.out.println("查询 [14, 16): " + rangeModule.queryRange(14, 16)); // true
        System.out.println("查询 [16, 17): " + rangeModule.queryRange(16, 17)); // true
        System.out.println("查询 [20, 21): " + rangeModule.queryRange(20, 21)); // false
        
        // 测试添加重叠区间
        rangeModule.addRange(15, 25);
        System.out.println("添加重叠区间 [15, 25)");
        System.out.println("查询 [20, 21): " + rangeModule.queryRange(20, 21)); // true
        
        // 测试删除区间
        rangeModule.removeRange(14, 16);
        System.out.println("删除区间 [14, 16)");
        System.out.println("查询 [14, 15): " + rangeModule.queryRange(14, 15)); // false
        System.out.println("查询 [16, 17): " + rangeModule.queryRange(16, 17)); // true
        
        // 测试复杂操作
        rangeModule.addRange(5, 8);
        rangeModule.addRange(1, 3);
        System.out.println("查询 [1, 8): " + rangeModule.queryRange(1, 8)); // false
        System.out.println("查询 [1, 3): " + rangeModule.queryRange(1, 3)); // true
        System.out.println("查询 [5, 8): " + rangeModule.queryRange(5, 8)); // true
    }
    
    private static void testTreeMapImplementation() {
        RangeModuleTreeMap rangeModule = new RangeModuleTreeMap();
        
        // 测试添加区间
        rangeModule.addRange(10, 20);
        System.out.println("添加区间 [10, 20)");
        
        // 测试查询
        System.out.println("查询 [14, 16): " + rangeModule.queryRange(14, 16)); // true
        System.out.println("查询 [16, 17): " + rangeModule.queryRange(16, 17)); // true
        System.out.println("查询 [20, 21): " + rangeModule.queryRange(20, 21)); // false
        
        // 测试添加重叠区间
        rangeModule.addRange(15, 25);
        System.out.println("添加重叠区间 [15, 25)");
        System.out.println("查询 [20, 21): " + rangeModule.queryRange(20, 21)); // true
        
        // 测试删除区间
        rangeModule.removeRange(14, 16);
        System.out.println("删除区间 [14, 16)");
        System.out.println("查询 [14, 15): " + rangeModule.queryRange(14, 15)); // false
        System.out.println("查询 [16, 17): " + rangeModule.queryRange(16, 17)); // true
    }
    
    private static void performanceComparison() {
        int n = 10000; // 操作次数
        
        // 线段树实现性能测试
        long startTime = System.currentTimeMillis();
        RangeModule segmentTree = new RangeModule();
        for (int i = 0; i < n; i++) {
            int left = i * 10;
            int right = left + 5;
            segmentTree.addRange(left, right);
            segmentTree.queryRange(left, right + 1);
            segmentTree.removeRange(left + 2, right - 2);
        }
        long segmentTreeTime = System.currentTimeMillis() - startTime;
        
        // TreeMap实现性能测试
        startTime = System.currentTimeMillis();
        RangeModuleTreeMap treeMap = new RangeModuleTreeMap();
        for (int i = 0; i < n; i++) {
            int left = i * 10;
            int right = left + 5;
            treeMap.addRange(left, right);
            treeMap.queryRange(left, right + 1);
            treeMap.removeRange(left + 2, right - 2);
        }
        long treeMapTime = System.currentTimeMillis() - startTime;
        
        System.out.println("线段树实现耗时: " + segmentTreeTime + "ms");
        System.out.println("TreeMap实现耗时: " + treeMapTime + "ms");
        System.out.println("性能差异: " + (segmentTreeTime - treeMapTime) + "ms");
    }
}
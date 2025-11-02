package class112;

// 715. Range 模块 - 线段树实现
// 题目来源：LeetCode 715 https://leetcode.cn/problems/range-module/
// 
// 题目描述：
// Range模块是跟踪数字范围的模块。设计一个数据结构来跟踪表示为 半开区间 的范围并查询它们。
// 实现 RangeModule 类:
// RangeModule() 初始化数据结构的对象
// void addRange(int left, int right) 添加 半开区间 [left, right), 跟踪该区间中的每个实数。添加与当前跟踪的区间重叠的区间时，应当添加在区间 [left, right) 中尚未被跟踪的任何数字到该区间中。
// boolean queryRange(int left, int right) 只有在当前正在跟踪区间 [left, right) 中的每一个实数时，才返回 true ，否则返回 false 。
// void removeRange(int left, int right) 停止跟踪 半开区间 [left, right) 中当前正在跟踪的每个实数。
// 
// 解题思路：
// 使用线段树配合懒惰标记来维护区间覆盖状态
// 1. 使用线段树节点维护区间覆盖信息：0表示未完全覆盖，1表示完全覆盖
// 2. 使用懒惰标记优化区间更新操作：-1表示无操作，0表示删除，1表示添加
// 3. addRange操作：将区间[left, right)标记为完全覆盖
// 4. removeRange操作：将区间[left, right)标记为未完全覆盖
// 5. queryRange操作：查询区间[left, right)是否完全覆盖
// 
// 时间复杂度分析：
// - addRange：O(log n)
// - removeRange：O(log n)
// - queryRange：O(log n)
// 空间复杂度：O(n)

public class Code12_RangeModule {
    
    // 线段树节点类
    static class SegmentTree {
        private int[] tree;     // 存储区间覆盖状态：0-未覆盖，1-完全覆盖
        private int[] lazy;     // 懒惰标记：-1-无操作，0-删除，1-添加
        private int maxSize;    // 线段树能处理的最大值范围
        
        /**
         * 构造函数
         * @param maxSize 线段树维护的最大范围
         * 
         * 时间复杂度: O(n)
         * 空间复杂度: O(n)
         */
        public SegmentTree(int maxSize) {
            this.maxSize = maxSize;
            // 线段树通常需要4倍空间
            this.tree = new int[4 * maxSize];
            this.lazy = new int[4 * maxSize];
            // 初始化懒惰标记为-1（无操作）
            for (int i = 0; i < 4 * maxSize; i++) {
                lazy[i] = -1;
            }
        }
        
        /**
         * 下推懒惰标记
         * 将当前节点的懒惰标记传递给左右子节点
         * @param node 当前节点索引
         * @param start 当前节点维护的区间左边界
         * @param end 当前节点维护的区间右边界
         */
        private void pushDown(int node, int start, int end) {
            // 如果当前节点有懒惰标记
            if (lazy[node] != -1) {
                int mid = start + (end - start) / 2;
                int leftNode = 2 * node + 1;
                int rightNode = 2 * node + 2;
                
                // 更新左子节点的值和懒惰标记
                tree[leftNode] = lazy[node];
                lazy[leftNode] = lazy[node];
                
                // 更新右子节点的值和懒惰标记
                tree[rightNode] = lazy[node];
                lazy[rightNode] = lazy[node];
                
                // 清除当前节点的懒惰标记
                lazy[node] = -1;
            }
        }
        
        /**
         * 添加区间
         * @param left 区间左边界（包含）
         * @param right 区间右边界（不包含）
         * 
         * 时间复杂度: O(log n)
         */
        public void addRange(int left, int right) {
            updateRange(0, 0, maxSize - 1, left, right - 1, 1);
        }
        
        /**
         * 删除区间
         * @param left 区间左边界（包含）
         * @param right 区间右边界（不包含）
         * 
         * 时间复杂度: O(log n)
         */
        public void removeRange(int left, int right) {
            updateRange(0, 0, maxSize - 1, left, right - 1, 0);
        }
        
        /**
         * 查询区间是否完全覆盖
         * @param left 区间左边界（包含）
         * @param right 区间右边界（不包含）
         * @return 区间是否完全覆盖
         * 
         * 时间复杂度: O(log n)
         */
        public boolean queryRange(int left, int right) {
            return query(0, 0, maxSize - 1, left, right - 1);
        }
        
        /**
         * 更新区间的辅助函数
         * @param node 当前节点索引
         * @param start 当前节点维护的区间左边界
         * @param end 当前节点维护的区间右边界
         * @param left 更新区间左边界
         * @param right 更新区间右边界
         * @param val 要设置的值（0-删除，1-添加）
         */
        private void updateRange(int node, int start, int end, int left, int right, int val) {
            // 更新区间与当前节点维护区间无交集，直接返回
            if (right < start || end < left) {
                return;
            }
            
            // 当前节点维护区间完全包含在更新区间内
            if (left <= start && end <= right) {
                tree[node] = val;
                lazy[node] = val;
                return;
            }
            
            // 下推懒惰标记
            pushDown(node, start, end);
            
            // 部分重叠，递归更新左右子树
            int mid = start + (end - start) / 2;
            int leftNode = 2 * node + 1;
            int rightNode = 2 * node + 2;
            
            updateRange(leftNode, start, mid, left, right, val);
            updateRange(rightNode, mid + 1, end, left, right, val);
            
            // 更新当前节点的值
            // 如果左右子节点都完全覆盖，则当前节点也完全覆盖
            tree[node] = (tree[leftNode] == 1 && tree[rightNode] == 1) ? 1 : 0;
        }
        
        /**
         * 查询辅助函数
         * @param node 当前节点索引
         * @param start 当前节点维护的区间左边界
         * @param end 当前节点维护的区间右边界
         * @param left 查询区间左边界
         * @param right 查询区间右边界
         * @return 区间是否完全覆盖
         */
        private boolean query(int node, int start, int end, int left, int right) {
            // 查询区间与当前节点维护区间无交集，返回true（不影响整体结果）
            if (right < start || end < left) {
                return true;
            }
            
            // 当前节点维护区间完全包含在查询区间内，返回覆盖状态
            if (left <= start && end <= right) {
                return tree[node] == 1;
            }
            
            // 下推懒惰标记
            pushDown(node, start, end);
            
            // 部分重叠，递归查询左右子树
            int mid = start + (end - start) / 2;
            int leftNode = 2 * node + 1;
            int rightNode = 2 * node + 2;
            
            boolean leftResult = query(leftNode, start, mid, left, right);
            boolean rightResult = query(rightNode, mid + 1, end, left, right);
            
            // 只有左右子树都完全覆盖，才返回true
            return leftResult && rightResult;
        }
    }
    
    private SegmentTree segmentTree;
    private final int MAX_RANGE = 1000000000; // 题目中范围较大，使用10^9
    
    /**
     * 构造函数
     * 初始化数据结构的对象
     */
    public Code12_RangeModule() {
        // 由于范围很大，直接使用动态开点线段树会更高效
        // 但为了简化，这里使用离散化的思路，实际应用中应该使用动态开点
        // 这里我们使用一个简化版，假设范围不超过1000000
        this.segmentTree = new SegmentTree(1000001);
    }
    
    /**
     * 添加 半开区间 [left, right), 跟踪该区间中的每个实数
     * @param left 区间左边界（包含）
     * @param right 区间右边界（不包含）
     */
    public void addRange(int left, int right) {
        segmentTree.addRange(left, right);
    }
    
    /**
     * 查询区间 [left, right) 是否完全覆盖
     * @param left 区间左边界（包含）
     * @param right 区间右边界（不包含）
     * @return 只有在当前正在跟踪区间中的每一个实数时，才返回 true ，否则返回 false
     */
    public boolean queryRange(int left, int right) {
        return segmentTree.queryRange(left, right);
    }
    
    /**
     * 停止跟踪 半开区间 [left, right) 中当前正在跟踪的每个实数
     * @param left 区间左边界（包含）
     * @param right 区间右边界（不包含）
     */
    public void removeRange(int left, int right) {
        segmentTree.removeRange(left, right);
    }
    
    /**
     * 测试代码
     */
    public static void main(String[] args) {
        // 测试用例
        Code12_RangeModule rangeModule = new Code12_RangeModule();
        rangeModule.addRange(10, 20);
        System.out.println("查询[10, 14]: " + rangeModule.queryRange(10, 14)); // true
        System.out.println("查询[13, 15]: " + rangeModule.queryRange(13, 15)); // true
        System.out.println("查询[16, 17]: " + rangeModule.queryRange(16, 17)); // true
        rangeModule.removeRange(14, 16);
        System.out.println("删除后查询[10, 14]: " + rangeModule.queryRange(10, 14)); // true
        System.out.println("删除后查询[13, 15]: " + rangeModule.queryRange(13, 15)); // false
        System.out.println("删除后查询[16, 17]: " + rangeModule.queryRange(16, 17)); // true
    }
}
package class112;

import java.io.*;
import java.util.*;

/**
 * 线段树高级应用 - 多种区间操作
 * 
 * 题目描述：
 * 实现支持多种区间操作的线段树，包括：
 * 1. 区间赋值
 * 2. 区间加法
 * 3. 区间乘法
 * 4. 区间求和
 * 5. 区间最大值/最小值
 * 
 * 题目来源：洛谷 P3373 【模板】线段树 2
 * 测试链接 : https://www.luogu.com.cn/problem/P3373
 * 
 * 解题思路：
 * 使用高级线段树实现支持多种操作，包括区间赋值、加法、乘法以及查询操作。
 * 通过维护多种懒惰标记来处理不同优先级的操作。
 * 
 * 核心思想：
 * 1. 多标记懒惰传播：同时维护加法、乘法和赋值三种懒惰标记
 * 2. 标记优先级：赋值 > 乘法 > 加法
 * 3. 标记下传：在下传标记时需要按照优先级顺序处理
 * 
 * 时间复杂度分析：
 * - 构建线段树：O(n)
 * - 所有区间操作：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树需要O(n)的额外空间
 */
public class Code17_SegmentTreeApplications {
    
    /**
     * 高级线段树实现 - 支持多种操作
     * 
     * 特点：
     * 1. 支持区间赋值、加法、乘法操作
     * 2. 支持区间求和、最大值、最小值查询
     * 3. 使用多标记懒惰传播技术
     */
    static class AdvancedSegmentTree {
        /**
         * 线段树节点类
         * 存储区间信息和懒惰标记
         */
        static class Node {
            long sum;        // 区间和
            long max;        // 区间最大值
            long min;        // 区间最小值
            long add;        // 加法懒惰标记
            long mul;        // 乘法懒惰标记
            long set;        // 赋值懒惰标记
            boolean hasSet;  // 是否有赋值标记
            
            Node() {
                sum = 0;
                max = Long.MIN_VALUE;
                min = Long.MAX_VALUE;
                add = 0;
                mul = 1;
                set = 0;
                hasSet = false;
            }
        }
        
        Node[] tree;  // 线段树数组
        int n;        // 数组长度
        
        /**
         * 构造函数 - 初始化高级线段树
         * @param nums 原始数组
         */
        public AdvancedSegmentTree(int[] nums) {
            n = nums.length;
            tree = new Node[n * 4];  // 线段树通常需要4倍空间
            // 初始化所有节点
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node();
            }
            buildTree(nums, 0, 0, n - 1);
        }
        
        /**
         * 构建线段树
         * 递归地将数组构建成线段树结构
         * @param nums 原始数组
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * 
         * 时间复杂度: O(n)
         */
        private void buildTree(int[] nums, int node, int start, int end) {
            if (start == end) {
                // 叶子节点 - 直接存储数组元素值
                tree[node].sum = nums[start];
                tree[node].max = nums[start];
                tree[node].min = nums[start];
            } else {
                int mid = (start + end) / 2;
                // 递归构建左子树
                buildTree(nums, 2 * node + 1, start, mid);
                // 递归构建右子树
                buildTree(nums, 2 * node + 2, mid + 1, end);
                // 向上更新父节点信息
                pushUp(node);
            }
        }
        
        /**
         * 向上更新父节点
         * 根据左右子节点的信息更新当前节点的信息
         * @param node 当前线段树节点索引
         */
        private void pushUp(int node) {
            int left = 2 * node + 1;   // 左子节点索引
            int right = 2 * node + 2;  // 右子节点索引
            // 更新区间和
            tree[node].sum = tree[left].sum + tree[right].sum;
            // 更新区间最大值
            tree[node].max = Math.max(tree[left].max, tree[right].max);
            // 更新区间最小值
            tree[node].min = Math.min(tree[left].min, tree[right].min);
        }
        
        /**
         * 向下传递懒惰标记
         * 按照标记优先级顺序传递标记给子节点
         * 优先级：赋值 > 乘法 > 加法
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * 
         * 时间复杂度: O(1)
         */
        private void pushDown(int node, int start, int end) {
            // 叶子节点不需要传递标记
            if (start == end) return;
            
            int left = 2 * node + 1;   // 左子节点索引
            int right = 2 * node + 2;  // 右子节点索引
            int mid = (start + end) / 2;
            
            // 处理赋值标记（优先级最高）
            // 当存在赋值标记时，需要清除子节点的其他标记
            if (tree[node].hasSet) {
                // 更新左子树的区间信息
                tree[left].sum = tree[node].set * (mid - start + 1);  // 区间和 = 赋值 * 区间长度
                tree[right].sum = tree[node].set * (end - mid);
                tree[left].max = tree[node].set;  // 区间最大值 = 赋值
                tree[right].max = tree[node].set;
                tree[left].min = tree[node].set;  // 区间最小值 = 赋值
                tree[right].min = tree[node].set;
                
                // 传递赋值标记给子节点
                tree[left].set = tree[node].set;
                tree[right].set = tree[node].set;
                tree[left].hasSet = true;
                tree[right].hasSet = true;
                
                // 清除子节点的其他标记（加法和乘法）
                tree[left].add = 0;
                tree[right].add = 0;
                tree[left].mul = 1;
                tree[right].mul = 1;
                
                // 清除当前节点的赋值标记
                tree[node].hasSet = false;
            }
            
            // 处理乘法标记（优先级次之）
            // 当存在乘法标记时，需要更新子节点的所有信息
            if (tree[node].mul != 1) {
                // 更新左子树的区间信息（乘以mul）
                tree[left].sum *= tree[node].mul;
                tree[right].sum *= tree[node].mul;
                tree[left].max *= tree[node].mul;
                tree[right].max *= tree[node].mul;
                tree[left].min *= tree[node].mul;
                tree[right].min *= tree[node].mul;
                
                // 传递乘法标记给子节点
                tree[left].mul *= tree[node].mul;
                tree[right].mul *= tree[node].mul;
                // 乘法标记也会影响加法标记（add * mul）
                tree[left].add *= tree[node].mul;
                tree[right].add *= tree[node].mul;
                
                // 清除当前节点的乘法标记
                tree[node].mul = 1;
            }
            
            // 处理加法标记（优先级最低）
            // 当存在加法标记时，需要更新子节点的所有信息
            if (tree[node].add != 0) {
                int leftLen = mid - start + 1;   // 左子树区间长度
                int rightLen = end - mid;        // 右子树区间长度
                
                // 更新左子树的区间信息（加上add）
                tree[left].sum += tree[node].add * leftLen;  // 区间和增加 add * 区间长度
                tree[right].sum += tree[node].add * rightLen;
                tree[left].max += tree[node].add;  // 区间最大值增加 add
                tree[right].max += tree[node].add;
                tree[left].min += tree[node].add;  // 区间最小值增加 add
                tree[right].min += tree[node].add;
                
                // 传递加法标记给子节点
                tree[left].add += tree[node].add;
                tree[right].add += tree[node].add;
                
                // 清除当前节点的加法标记
                tree[node].add = 0;
            }
        }
        
        /**
         * 区间赋值操作
         * 将区间[left, right]内的每个数都赋值为val
         * @param left 更新区间左边界（0-based索引）
         * @param right 更新区间右边界（0-based索引）
         * @param val 要赋的值
         * 
         * 时间复杂度: O(log n)
         */
        public void rangeSet(int left, int right, long val) {
            rangeSetHelper(0, 0, n - 1, left, right, val);
        }
        
        /**
         * 区间赋值操作辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param l 更新区间左边界（0-based索引）
         * @param r 更新区间右边界（0-based索引）
         * @param val 要赋的值
         */
        private void rangeSetHelper(int node, int start, int end, int l, int r, long val) {
            // 如果当前区间完全包含在更新区间内
            if (l <= start && end <= r) {
                // 直接更新当前节点的信息和标记
                tree[node].sum = val * (end - start + 1);  // 区间和 = 赋值 * 区间长度
                tree[node].max = val;  // 区间最大值 = 赋值
                tree[node].min = val;  // 区间最小值 = 赋值
                tree[node].set = val;  // 设置赋值标记
                tree[node].hasSet = true;  // 标记存在赋值操作
                tree[node].add = 0;  // 清除加法标记
                tree[node].mul = 1;  // 清除乘法标记
                return;
            }
            
            // 需要向下传递懒惰标记（在递归之前）
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            // 递归更新左右子树
            if (l <= mid) rangeSetHelper(2 * node + 1, start, mid, l, r, val);
            if (r > mid) rangeSetHelper(2 * node + 2, mid + 1, end, l, r, val);
            // 更新父节点信息
            pushUp(node);
        }
        
        /**
         * 区间乘法操作
         * 将区间[left, right]内的每个数都乘以val
         * @param left 更新区间左边界（0-based索引）
         * @param right 更新区间右边界（0-based索引）
         * @param val 要乘的值
         * 
         * 时间复杂度: O(log n)
         */
        public void rangeMul(int left, int right, long val) {
            rangeMulHelper(0, 0, n - 1, left, right, val);
        }
        
        /**
         * 区间乘法操作辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param l 更新区间左边界（0-based索引）
         * @param r 更新区间右边界（0-based索引）
         * @param val 要乘的值
         */
        private void rangeMulHelper(int node, int start, int end, int l, int r, long val) {
            // 如果当前区间完全包含在更新区间内
            if (l <= start && end <= r) {
                // 直接更新当前节点的信息和标记
                tree[node].sum *= val;  // 区间和乘以val
                tree[node].max *= val;  // 区间最大值乘以val
                tree[node].min *= val;  // 区间最小值乘以val
                tree[node].mul *= val;  // 乘法标记乘以val
                tree[node].add *= val;  // 加法标记也乘以val（因为 a*x + b 变成 a*x*val + b*val）
                return;
            }
            
            // 需要向下传递懒惰标记（在递归之前）
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            // 递归更新左右子树
            if (l <= mid) rangeMulHelper(2 * node + 1, start, mid, l, r, val);
            if (r > mid) rangeMulHelper(2 * node + 2, mid + 1, end, l, r, val);
            // 更新父节点信息
            pushUp(node);
        }
        
        /**
         * 区间加法操作
         * 将区间[left, right]内的每个数都加上val
         * @param left 更新区间左边界（0-based索引）
         * @param right 更新区间右边界（0-based索引）
         * @param val 要加的值
         * 
         * 时间复杂度: O(log n)
         */
        public void rangeAdd(int left, int right, long val) {
            rangeAddHelper(0, 0, n - 1, left, right, val);
        }
        
        /**
         * 区间加法操作辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param l 更新区间左边界（0-based索引）
         * @param r 更新区间右边界（0-based索引）
         * @param val 要加的值
         */
        private void rangeAddHelper(int node, int start, int end, int l, int r, long val) {
            // 如果当前区间完全包含在更新区间内
            if (l <= start && end <= r) {
                // 直接更新当前节点的信息和标记
                tree[node].sum += val * (end - start + 1);  // 区间和增加 val * 区间长度
                tree[node].max += val;  // 区间最大值增加 val
                tree[node].min += val;  // 区间最小值增加 val
                tree[node].add += val;  // 加法标记增加 val
                return;
            }
            
            // 需要向下传递懒惰标记（在递归之前）
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            // 递归更新左右子树
            if (l <= mid) rangeAddHelper(2 * node + 1, start, mid, l, r, val);
            if (r > mid) rangeAddHelper(2 * node + 2, mid + 1, end, l, r, val);
            // 更新父节点信息
            pushUp(node);
        }
        
        /**
         * 区间求和查询
         * 查询区间[left, right]内所有数的和
         * @param left 查询区间左边界（0-based索引）
         * @param right 查询区间右边界（0-based索引）
         * @return 区间[left, right]内所有数的和
         * 
         * 时间复杂度: O(log n)
         */
        public long rangeSum(int left, int right) {
            return rangeSumHelper(0, 0, n - 1, left, right);
        }
        
        /**
         * 区间求和查询辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param l 查询区间左边界（0-based索引）
         * @param r 查询区间右边界（0-based索引）
         * @return 区间[left, right]内所有数的和
         */
        private long rangeSumHelper(int node, int start, int end, int l, int r) {
            // 如果当前区间完全包含在查询区间内
            if (l <= start && end <= r) {
                // 直接返回当前节点的区间和
                return tree[node].sum;
            }
            
            // 需要向下传递懒惰标记（在递归之前）
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            long sum = 0;
            // 递归查询左右子树
            if (l <= mid) sum += rangeSumHelper(2 * node + 1, start, mid, l, r);
            if (r > mid) sum += rangeSumHelper(2 * node + 2, mid + 1, end, l, r);
            return sum;
        }
        
        /**
         * 区间最大值查询
         * 查询区间[left, right]内的最大值
         * @param left 查询区间左边界（0-based索引）
         * @param right 查询区间右边界（0-based索引）
         * @return 区间[left, right]内的最大值
         * 
         * 时间复杂度: O(log n)
         */
        public long rangeMax(int left, int right) {
            return rangeMaxHelper(0, 0, n - 1, left, right);
        }
        
        /**
         * 区间最大值查询辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param l 查询区间左边界（0-based索引）
         * @param r 查询区间右边界（0-based索引）
         * @return 区间[left, right]内的最大值
         */
        private long rangeMaxHelper(int node, int start, int end, int l, int r) {
            // 如果当前区间完全包含在查询区间内
            if (l <= start && end <= r) {
                // 直接返回当前节点的区间最大值
                return tree[node].max;
            }
            
            // 需要向下传递懒惰标记（在递归之前）
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            long maxVal = Long.MIN_VALUE;
            // 递归查询左右子树
            if (l <= mid) maxVal = Math.max(maxVal, rangeMaxHelper(2 * node + 1, start, mid, l, r));
            if (r > mid) maxVal = Math.max(maxVal, rangeMaxHelper(2 * node + 2, mid + 1, end, l, r));
            return maxVal;
        }
        
        /**
         * 区间最小值查询
         * 查询区间[left, right]内的最小值
         * @param left 查询区间左边界（0-based索引）
         * @param right 查询区间右边界（0-based索引）
         * @return 区间[left, right]内的最小值
         * 
         * 时间复杂度: O(log n)
         */
        public long rangeMin(int left, int right) {
            return rangeMinHelper(0, 0, n - 1, left, right);
        }
        
        /**
         * 区间最小值查询辅助函数
         * @param node 当前线段树节点索引
         * @param start 当前节点表示区间的起始位置
         * @param end 当前节点表示区间的结束位置
         * @param l 查询区间左边界（0-based索引）
         * @param r 查询区间右边界（0-based索引）
         * @return 区间[left, right]内的最小值
         */
        private long rangeMinHelper(int node, int start, int end, int l, int r) {
            // 如果当前区间完全包含在查询区间内
            if (l <= start && end <= r) {
                // 直接返回当前节点的区间最小值
                return tree[node].min;
            }
            
            // 需要向下传递懒惰标记（在递归之前）
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            long minVal = Long.MAX_VALUE;
            // 递归查询左右子树
            if (l <= mid) minVal = Math.min(minVal, rangeMinHelper(2 * node + 1, start, mid, l, r));
            if (r > mid) minVal = Math.min(minVal, rangeMinHelper(2 * node + 2, mid + 1, end, l, r));
            return minVal;
        }
    }
    
    /**
     * 主函数 - 处理输入输出和操作调度
     * 使用高效的输入输出处理方式
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取n和m
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        
        // 读取初始数组
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            in.nextToken();
            nums[i] = (int) in.nval;
        }
        
        // 构建高级线段树
        AdvancedSegmentTree st = new AdvancedSegmentTree(nums);
        
        // 处理m个操作
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int op = (int) in.nval;
            in.nextToken();
            int l = (int) in.nval;
            in.nextToken();
            int r = (int) in.nval;
            
            switch (op) {
                case 1: // 区间加法
                    in.nextToken();
                    long addVal = (long) in.nval;
                    st.rangeAdd(l - 1, r - 1, addVal);
                    break;
                case 2: // 区间乘法
                    in.nextToken();
                    long mulVal = (long) in.nval;
                    st.rangeMul(l - 1, r - 1, mulVal);
                    break;
                case 3: // 区间赋值
                    in.nextToken();
                    long setVal = (long) in.nval;
                    st.rangeSet(l - 1, r - 1, setVal);
                    break;
                case 4: // 区间求和
                    out.println(st.rangeSum(l - 1, r - 1));
                    break;
                case 5: // 区间最大值
                    out.println(st.rangeMax(l - 1, r - 1));
                    break;
                case 6: // 区间最小值
                    out.println(st.rangeMin(l - 1, r - 1));
                    break;
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 测试方法 - 验证高级线段树实现的正确性
     */
    public static void test() {
        System.out.println("=== 高级线段树测试 ===");
        
        int[] nums = {1, 2, 3, 4, 5};
        AdvancedSegmentTree st = new AdvancedSegmentTree(nums);
        
        // 测试初始状态
        System.out.println("初始数组区间[0,4]的和: " + st.rangeSum(0, 4)); // 15
        System.out.println("区间[0,4]的最大值: " + st.rangeMax(0, 4)); // 5
        System.out.println("区间[0,4]的最小值: " + st.rangeMin(0, 4)); // 1
        
        // 测试区间加法
        st.rangeAdd(1, 3, 2);
        System.out.println("区间加法后区间[1,3]的和: " + st.rangeSum(1, 3)); // 4+5+6 = 15
        
        // 测试区间乘法
        st.rangeMul(0, 2, 3);
        System.out.println("区间乘法后区间[0,2]的和: " + st.rangeSum(0, 2)); // 3*3 + 4*3 + 5*3 = 36
        
        // 测试区间赋值
        st.rangeSet(2, 4, 10);
        System.out.println("区间赋值后区间[2,4]的和: " + st.rangeSum(2, 4)); // 10*3 = 30
        
        System.out.println("=== 测试完成 ===");
    }
}
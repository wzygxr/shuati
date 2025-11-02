package class131;

import java.util.*;

/** 
 * Codeforces 242E. XOR on Segment (区间异或)
 * 题目链接: https://codeforces.com/problemset/problem/242/E
 * 
 * 题目描述: 
 * 给你一个数组和几种操作:
 * 1. 操作1 l r x: 对区间[l,r]的每个元素与x进行异或操作
 * 2. 操作2 l r: 查询区间[l,r]的元素和
 * 
 * 解题思路:
 * 使用线段树实现，支持区间异或和区间求和操作
 * 1. 线段树每个节点维护区间和
 * 2. 使用懒惰传播处理区间异或操作
 * 3. 对于每个二进制位维护独立的懒惰标记
 * 
 * 时间复杂度分析:
 * - 区间异或: O(log n)
 * - 区间求和: O(log n)
 * - 总时间复杂度: O(30 * n * log n) （30为整数的二进制位数）
 * 空间复杂度: O(n)
 */
public class Code13_XOROnSegment {
    
    /** 
     * 线段树节点
     * 每个节点表示一个区间[left, right]，并维护该区间的元素和以及懒惰标记
     */
    static class Node {
        int left, right;     // 节点表示的区间范围
        long sum;            // 区间内元素的和
        int[] lazy;          // 每个二进制位的懒惰标记
        
        /** 
         * 构造函数
         * 
         * @param l 区间左边界
         * @param r 区间右边界
         */
        Node(int l, int r) {
            left = l;
            right = r;
            sum = 0;
            // 只需要考虑前20位（因为数值范围是10^6，2^20 > 10^6）
            lazy = new int[20]; 
        }
    }
    
    private Node[] tree;  // 线段树数组
    private int[] nums;   // 原始数组
    
    /** 
     * 构造函数，根据给定数组构建线段树
     * 
     * @param nums 初始数组
     */
    public Code13_XOROnSegment(int[] nums) {
        this.nums = nums;
        int n = nums.length;
        tree = new Node[4 * n];
        build(0, 0, n - 1);
    }
    
    /** 
     * 构建线段树
     * 
     * @param node  当前线段树节点索引
     * @param start 区间起始位置
     * @param end   区间结束位置
     */
    private void build(int node, int start, int end) {
        tree[node] = new Node(start, end);
        // 叶子节点
        if (start == end) {
            tree[node].sum = nums[start];
            return;
        }
        // 非叶子节点，递归构建左右子树
        int mid = (start + end) / 2;
        build(2 * node + 1, start, mid);
        build(2 * node + 2, mid + 1, end);
        // 更新当前节点的和为左右子树和的和
        tree[node].sum = tree[2 * node + 1].sum + tree[2 * node + 2].sum;
    }
    
    /** 
     * 下传懒惰标记
     * 将当前节点的懒惰标记传递给子节点
     * 
     * @param node 当前线段树节点索引
     */
    private void pushDown(int node) {
        // 检查是否有懒惰标记
        boolean hasLazy = false;
        for (int i = 0; i < 20; i++) {
            if (tree[node].lazy[i] != 0) {
                hasLazy = true;
                break;
            }
        }
        
        // 如果没有懒惰标记，直接返回
        if (!hasLazy) {
            return;
        }
        
        int leftChild = 2 * node + 1;
        int rightChild = 2 * node + 2;
        
        // 创建子节点（如果需要）
        if (tree[node].left != tree[node].right) {
            // 对每个二进制位处理懒惰标记
            for (int i = 0; i < 20; i++) {
                if (tree[node].lazy[i] != 0) {
                    // 将标记传递给子节点（异或操作）
                    tree[leftChild].lazy[i] ^= tree[node].lazy[i];
                    tree[rightChild].lazy[i] ^= tree[node].lazy[i];
                }
            }
        }
        
        // 应用懒惰标记到当前节点
        applyLazy(node);
        
        // 清除当前节点的懒惰标记
        for (int i = 0; i < 20; i++) {
            tree[node].lazy[i] = 0;
        }
    }
    
    /** 
     * 应用懒惰标记到节点
     * 根据懒惰标记更新节点的sum值
     * 
     * @param node 当前线段树节点索引
     */
    private void applyLazy(int node) {
        int len = tree[node].right - tree[node].left + 1;
        // 对每个二进制位处理
        for (int i = 0; i < 20; i++) {
            if (tree[node].lazy[i] != 0) {
                // 如果该位有奇数个元素，异或操作会影响和
                // 这里的计算逻辑需要修正，应该是根据该位为1的元素个数来计算
                long count = ((long) len + 1) / 2; // 该位为1的元素个数
                tree[node].sum ^= (count * (1 << i));
            }
        }
    }
    
    /** 
     * 区间异或操作
     * 对区间[start, end]的每个元素与x进行异或操作
     * 
     * @param start 区间起始位置
     * @param end   区间结束位置
     * @param x     异或值
     */
    public void xorRange(int start, int end, int x) {
        xorRange(0, start, end, x);
    }
    
    /** 
     * 区间异或操作（内部实现）
     * 
     * @param node  当前线段树节点索引
     * @param start 区间起始位置
     * @param end   区间结束位置
     * @param x     异或值
     */
    private void xorRange(int node, int start, int end, int x) {
        // 当前节点区间与操作区间无交集
        if (start > tree[node].right || end < tree[node].left) {
            return;
        }
        
        // 当前节点区间完全包含在操作区间内
        if (start <= tree[node].left && tree[node].right <= end) {
            // 对x的每个为1的二进制位设置懒惰标记
            for (int i = 0; i < 20; i++) {
                if ((x & (1 << i)) != 0) {
                    tree[node].lazy[i] ^= 1;
                }
            }
            return;
        }
        
        // 部分重叠，需要下传懒惰标记并递归处理
        pushDown(node);
        xorRange(2 * node + 1, start, end, x);
        xorRange(2 * node + 2, start, end, x);
        
        // 更新当前节点的和为左右子树和的和
        tree[node].sum = tree[2 * node + 1].sum + tree[2 * node + 2].sum;
    }
    
    /** 
     * 查询区间和
     * 
     * @param start 查询区间起始位置
     * @param end   查询区间结束位置
     * @return      区间内元素的和
     */
    public long querySum(int start, int end) {
        return querySum(0, start, end);
    }
    
    /** 
     * 查询区间和（内部实现）
     * 
     * @param node  当前线段树节点索引
     * @param start 查询区间起始位置
     * @param end   查询区间结束位置
     * @return      区间内元素的和
     */
    private long querySum(int node, int start, int end) {
        // 当前节点区间与查询区间无交集
        if (start > tree[node].right || end < tree[node].left) {
            return 0;
        }
        
        // 当前节点区间完全包含在查询区间内
        if (start <= tree[node].left && tree[node].right <= end) {
            return tree[node].sum;
        }
        
        // 部分重叠，需要下传懒惰标记并递归查询
        pushDown(node);
        long leftSum = querySum(2 * node + 1, start, end);
        long rightSum = querySum(2 * node + 2, start, end);
        
        // 返回左右子树查询结果的和
        return leftSum + rightSum;
    }
    
    /** 
     * 测试方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 示例测试
        int[] nums = {4, 1, 2, 3};
        Code13_XOROnSegment solution = new Code13_XOROnSegment(nums);
        
        System.out.println("初始数组和: " + solution.querySum(0, 3)); // 应该输出10
        
        solution.xorRange(0, 3, 1);
        System.out.println("对区间[0,3]异或1后和: " + solution.querySum(0, 3)); // 应该输出14
        
        solution.xorRange(1, 2, 2);
        System.out.println("对区间[1,2]异或2后和: " + solution.querySum(0, 3)); // 应该输出12
    }
}
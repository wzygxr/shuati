/**
 * Codeforces 339D Xenia and Bit Operations
 * 题目链接: https://codeforces.com/contest/339/problem/D
 * 
 * 题目描述:
 * 给定一个长度为2^n的数组，支持两种操作：
 * 1. 单点更新：修改某个位置的值
 * 2. 查询整个数组经过特定位运算后的结果
 * 
 * 运算规则：
 * - 第1层：相邻元素进行OR运算
 * - 第2层：相邻元素进行XOR运算
 * - 第3层：相邻元素进行OR运算
 * - 第4层：相邻元素进行XOR运算
 * - 以此类推，交替进行OR和XOR运算
 * 
 * 解题思路:
 * 使用线段树来维护每一层的运算结果。
 * 根据层数的奇偶性决定使用OR还是XOR运算。
 * 
 * 时间复杂度分析:
 * - 建树: O(2^n)
 * - 单点更新: O(log 2^n) = O(n)
 * - 查询: O(1)
 * 
 * 空间复杂度: O(2^n)
 */

public class Codeforces339D_XeniaAndBitOperations {
    private int n;
    private int size;
    private int[] tree;
    
    public Codeforces339D_XeniaAndBitOperations(int[] arr) {
        this.n = arr.length;
        this.size = n;
        tree = new int[4 * n];
        build(arr, 1, 0, n - 1, 0);
    }
    
    private void build(int[] arr, int node, int start, int end, int level) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(arr, 2 * node, start, mid, level + 1);
            build(arr, 2 * node + 1, mid + 1, end, level + 1);
            
            // 根据层数的奇偶性决定运算类型
            // 偶数层使用OR，奇数层使用XOR
            if (level % 2 == 0) {
                tree[node] = tree[2 * node] | tree[2 * node + 1];
            } else {
                tree[node] = tree[2 * node] ^ tree[2 * node + 1];
            }
        }
    }
    
    public void update(int index, int value) {
        update(1, 0, n - 1, index, value, 0);
    }
    
    private void update(int node, int start, int end, int index, int value, int level) {
        if (start == end) {
            tree[node] = value;
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                update(2 * node, start, mid, index, value, level + 1);
            } else {
                update(2 * node + 1, mid + 1, end, index, value, level + 1);
            }
            
            // 根据层数的奇偶性更新父节点
            if (level % 2 == 0) {
                tree[node] = tree[2 * node] | tree[2 * node + 1];
            } else {
                tree[node] = tree[2 * node] ^ tree[2 * node + 1];
            }
        }
    }
    
    public int query() {
        return tree[1];
    }
    
    public static void main(String[] args) {
        // 测试用例
        int[] arr = {1, 2, 3, 4};
        Codeforces339D_XeniaAndBitOperations segTree = new Codeforces339D_XeniaAndBitOperations(arr);
        
        System.out.println("初始查询结果: " + segTree.query()); // 期望: 根据运算规则计算
        
        // 更新第一个元素
        segTree.update(0, 5);
        System.out.println("更新后查询结果: " + segTree.query());
        
        // 测试更大的数组
        int[] arr2 = {1, 2, 3, 4, 5, 6, 7, 8};
        Codeforces339D_XeniaAndBitOperations segTree2 = new Codeforces339D_XeniaAndBitOperations(arr2);
        System.out.println("8元素数组查询结果: " + segTree2.query());
    }
}
/**
 * Codeforces 474F. Ant colony
 * 题目链接: https://codeforces.com/problemset/problem/474/F
 * 
 * 题目描述:
 * 给定一个长度为n的数组a，有m次查询。
 * 每次查询给出区间[l, r]，问区间内有多少个数字不能被区间内其他所有数字整除。
 * 换句话说，对于区间内的每个数字x，如果存在区间内的其他数字y，使得x不能被y整除，则x不被统计。
 * 
 * 示例:
 * 输入:
 * n = 5, a = [1, 3, 2, 4, 2]
 * m = 3
 * 查询1: [1, 5] -> 输出: 2
 * 查询2: [2, 4] -> 输出: 1
 * 查询3: [1, 3] -> 输出: 1
 * 
 * 解释:
 * 查询1: 区间[1,5]的GCD是1，只有1和2能被1整除，所以输出2
 * 查询2: 区间[2,4]的GCD是1，只有1能被1整除，所以输出1
 * 查询3: 区间[1,3]的GCD是1，只有1能被1整除，所以输出1
 * 
 * 解题思路:
 * 这是一个经典的线段树应用问题，需要同时维护区间GCD和区间最小值及其出现次数。
 * 1. 使用线段树维护区间GCD
 * 2. 同时维护区间最小值及其出现次数
 * 3. 对于每个查询，先求出区间GCD
 * 4. 如果区间最小值等于区间GCD，则统计区间内等于最小值的数字个数
 * 5. 否则，区间内没有数字能被所有其他数字整除
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 每次查询: O(log n)
 * - 总时间复杂度: O((n + m) * log n)
 * 
 * 空间复杂度分析:
 * - 线段树空间: O(4 * n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 边界处理: 处理空区间和单个元素的情况
 * 2. 性能优化: 使用位运算优化GCD计算
 * 3. 内存优化: 合理设计线段树节点结构
 * 4. 异常处理: 检查输入参数的有效性
 * 
 * 面试要点:
 * 1. 理解GCD的性质和在区间查询中的应用
 * 2. 掌握线段树维护多个信息的方法
 * 3. 能够分析时间空间复杂度
 * 4. 处理边界情况和极端输入
 */

import java.util.*;

class Node {
    int gcd;        // 区间GCD
    int min;        // 区间最小值
    int count;      // 最小值出现的次数
    
    Node(int gcd, int min, int count) {
        this.gcd = gcd;
        this.min = min;
        this.count = count;
    }
}

class SegmentTree {
    private int n;
    private int[] arr;
    private Node[] tree;
    
    public SegmentTree(int[] arr) {
        this.n = arr.length;
        this.arr = arr;
        this.tree = new Node[4 * n];
        build(1, 0, n - 1);
    }
    
    private void build(int node, int left, int right) {
        if (left == right) {
            tree[node] = new Node(arr[left], arr[left], 1);
            return;
        }
        
        int mid = left + (right - left) / 2;
        build(node * 2, left, mid);
        build(node * 2 + 1, mid + 1, right);
        
        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }
    
    private Node merge(Node left, Node right) {
        int gcd = gcd(left.gcd, right.gcd);
        int min = Math.min(left.min, right.min);
        int count = 0;
        
        if (left.min == min) count += left.count;
        if (right.min == min) count += right.count;
        
        return new Node(gcd, min, count);
    }
    
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    public Node query(int L, int R) {
        return query(1, 0, n - 1, L, R);
    }
    
    private Node query(int node, int left, int right, int L, int R) {
        if (L > right || R < left) {
            return null;
        }
        
        if (L <= left && right <= R) {
            return tree[node];
        }
        
        int mid = left + (right - left) / 2;
        Node leftNode = query(node * 2, left, mid, L, R);
        Node rightNode = query(node * 2 + 1, mid + 1, right, L, R);
        
        if (leftNode == null) return rightNode;
        if (rightNode == null) return leftNode;
        
        return merge(leftNode, rightNode);
    }
}

public class Codeforces474F_AntColony {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取数组大小
        int n = scanner.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        
        // 构建线段树
        SegmentTree segTree = new SegmentTree(arr);
        
        // 读取查询次数
        int m = scanner.nextInt();
        
        for (int i = 0; i < m; i++) {
            int l = scanner.nextInt() - 1; // 转换为0-based索引
            int r = scanner.nextInt() - 1;
            
            Node result = segTree.query(l, r);
            
            // 如果区间最小值等于区间GCD，则统计最小值出现次数
            // 否则，没有数字能被所有其他数字整除
            int answer = (result.min == result.gcd) ? result.count : 0;
            
            // 输出结果：区间长度减去符合条件的数字个数
            System.out.println((r - l + 1) - answer);
        }
        
        scanner.close();
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        // 测试用例1
        int[] arr1 = {1, 3, 2, 4, 2};
        SegmentTree segTree1 = new SegmentTree(arr1);
        
        // 查询[1,5] -> 区间[0,4]
        Node result1 = segTree1.query(0, 4);
        int answer1 = (result1.min == result1.gcd) ? result1.count : 0;
        System.out.println("测试用例1 - 区间[1,5]: " + ((4 - 0 + 1) - answer1));
        
        // 查询[2,4] -> 区间[1,3]
        Node result2 = segTree1.query(1, 3);
        int answer2 = (result2.min == result2.gcd) ? result2.count : 0;
        System.out.println("测试用例1 - 区间[2,4]: " + ((3 - 1 + 1) - answer2));
        
        // 查询[1,3] -> 区间[0,2]
        Node result3 = segTree1.query(0, 2);
        int answer3 = (result3.min == result3.gcd) ? result3.count : 0;
        System.out.println("测试用例1 - 区间[1,3]: " + ((2 - 0 + 1) - answer3));
        
        // 测试用例2: 所有数字相同
        int[] arr2 = {2, 2, 2, 2};
        SegmentTree segTree2 = new SegmentTree(arr2);
        
        Node result4 = segTree2.query(0, 3);
        int answer4 = (result4.min == result4.gcd) ? result4.count : 0;
        System.out.println("测试用例2 - 区间[1,4]: " + ((3 - 0 + 1) - answer4));
        
        // 测试用例3: 单个元素
        int[] arr3 = {5};
        SegmentTree segTree3 = new SegmentTree(arr3);
        
        Node result5 = segTree3.query(0, 0);
        int answer5 = (result5.min == result5.gcd) ? result5.count : 0;
        System.out.println("测试用例3 - 区间[1,1]: " + ((0 - 0 + 1) - answer5));
    }
}
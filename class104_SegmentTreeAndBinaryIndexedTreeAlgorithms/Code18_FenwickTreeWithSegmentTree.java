package class131;

import java.util.*;

/**
 * 洛谷 P3380 【模板】树套树
 * 题目链接: https://www.luogu.com.cn/problem/P3380
 * 题目描述:
 * 您需要维护一个序列，支持以下操作：
 * 1. 查询区间[l,r]中数值k的排名（比k小的数的个数+1）
 * 2. 查询区间[l,r]中第k小的数
 * 3. 单点修改：将pos位置的值修改为k
 * 4. 查询区间[l,r]中数值k的前驱（最大的比k小的数）
 * 5. 查询区间[l,r]中数值k的后继（最小的比k大的数）
 * 
 * 解题思路:
 * 使用树状数组套权值线段树（Fenwick Tree + 动态开点线段树）
 * 1. 树状数组的每个节点对应一个权值线段树
 * 2. 树状数组用于处理区间查询（前缀和思想）
 * 3. 权值线段树用于处理数值范围的统计
 * 4. 离散化处理原始数据以压缩权值范围
 * 
 * 时间复杂度分析:
 * - 单点修改: O(log²n)
 * - 区间查询: O(log²n)
 * 空间复杂度: O(n log n)
 */
public class Code18_FenwickTreeWithSegmentTree {
    
    static class Node {
        int left, right; // 左右子节点索引
        int count;       // 该区间的元素个数
        
        public Node() {
            this.left = 0;
            this.right = 0;
            this.count = 0;
        }
    }
    
    private static final int MAXN = 50010;
    private static final int MAX_VAL = 1000000010;
    private static Node[] tree;  // 线段树节点数组
    private static int[] root;   // 树状数组每个节点对应的线段树根节点
    private static int[] a;      // 原始数组
    private static int[] sorted; // 离散化后的数组
    private static int cnt;      // 动态开点计数器
    private static int n, m;     // 数组大小和操作数量
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        
        a = new int[n + 1];
        sorted = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = scanner.nextInt();
            sorted[i] = a[i];
        }
        
        // 离散化处理
        Arrays.sort(sorted, 1, n + 1);
        int unique = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[i] != sorted[unique]) {
                sorted[++unique] = sorted[i];
            }
        }
        
        // 初始化树状数组和线段树
        int size = MAXN * 40; // 预估空间大小
        tree = new Node[size];
        for (int i = 0; i < size; i++) {
            tree[i] = new Node();
        }
        root = new int[MAXN];
        cnt = 1;
        
        // 构建初始树状数组
        for (int i = 1; i <= n; i++) {
            int idx = getIndex(a[i]);
            update(i, idx, 1);
        }
        
        // 处理操作
        while (m-- > 0) {
            int op = scanner.nextInt();
            if (op == 1) { // 查询排名
                int l = scanner.nextInt();
                int r = scanner.nextInt();
                int k = scanner.nextInt();
                int rank = query(r, getIndex(k) - 1) - query(l - 1, getIndex(k) - 1) + 1;
                System.out.println(rank);
            } else if (op == 2) { // 查询第k小
                int l = scanner.nextInt();
                int r = scanner.nextInt();
                int k = scanner.nextInt();
                int ans = kthSmallest(l, r, k);
                System.out.println(ans);
            } else if (op == 3) { // 单点修改
                int pos = scanner.nextInt();
                int k = scanner.nextInt();
                int oldIdx = getIndex(a[pos]);
                update(pos, oldIdx, -1);
                a[pos] = k;
                int newIdx = getIndex(k);
                update(pos, newIdx, 1);
            } else if (op == 4) { // 查询前驱
                int l = scanner.nextInt();
                int r = scanner.nextInt();
                int k = scanner.nextInt();
                int pre = getPredecessor(l, r, k);
                System.out.println(pre);
            } else if (op == 5) { // 查询后继
                int l = scanner.nextInt();
                int r = scanner.nextInt();
                int k = scanner.nextInt();
                int succ = getSuccessor(l, r, k);
                System.out.println(succ);
            }
        }
        
        scanner.close();
    }
    
    // 离散化：获取数值对应的索引
    private static int getIndex(int val) {
        int left = 1, right = sorted.length - 1;
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (sorted[mid] == val) {
                return mid;
            } else if (sorted[mid] < val) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    
    // 线段树更新
    private static void updateTree(int root, int l, int r, int pos, int val) {
        tree[root].count += val;
        if (l == r) {
            return;
        }
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (tree[root].left == 0) {
                tree[root].left = cnt++;
            }
            updateTree(tree[root].left, l, mid, pos, val);
        } else {
            if (tree[root].right == 0) {
                tree[root].right = cnt++;
            }
            updateTree(tree[root].right, mid + 1, r, pos, val);
        }
    }
    
    // 线段树查询
    private static int queryTree(int root, int l, int r, int ql, int qr) {
        if (root == 0 || r < ql || l > qr) {
            return 0;
        }
        if (ql <= l && r <= qr) {
            return tree[root].count;
        }
        int mid = (l + r) >> 1;
        return queryTree(tree[root].left, l, mid, ql, qr) + 
               queryTree(tree[root].right, mid + 1, r, ql, qr);
    }
    
    // 树状数组更新
    private static void update(int pos, int val, int delta) {
        while (pos <= n) {
            if (root[pos] == 0) {
                root[pos] = cnt++;
            }
            updateTree(root[pos], 1, sorted.length - 1, val, delta);
            pos += lowbit(pos);
        }
    }
    
    // 树状数组查询前缀和
    private static int query(int pos, int val) {
        int sum = 0;
        while (pos > 0) {
            sum += queryTree(root[pos], 1, sorted.length - 1, 1, val);
            pos -= lowbit(pos);
        }
        return sum;
    }
    
    // 计算区间[l,r]中<=k的元素个数
    private static int getCount(int l, int r, int k) {
        return query(r, getIndex(k)) - query(l - 1, getIndex(k));
    }
    
    // 查询区间第k小
    private static int kthSmallest(int l, int r, int k) {
        int left = 1, right = sorted.length - 1;
        while (left < right) {
            int mid = (left + right) >> 1;
            int count = query(r, mid) - query(l - 1, mid);
            if (count >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return sorted[left];
    }
    
    // 查询前驱
    private static int getPredecessor(int l, int r, int k) {
        int left = 1, right = sorted.length - 1;
        int ans = -MAX_VAL;
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (sorted[mid] >= k) {
                right = mid - 1;
            } else {
                int count = query(r, mid) - query(l - 1, mid);
                if (count > 0) {
                    ans = sorted[mid];
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return ans;
    }
    
    // 查询后继
    private static int getSuccessor(int l, int r, int k) {
        int left = 1, right = sorted.length - 1;
        int ans = MAX_VAL;
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (sorted[mid] <= k) {
                left = mid + 1;
            } else {
                int count = query(r, sorted.length - 1) - query(l - 1, sorted.length - 1) - 
                           (query(r, mid - 1) - query(l - 1, mid - 1));
                if (count > 0) {
                    ans = sorted[mid];
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        return ans;
    }
    
    // 计算lowbit
    private static int lowbit(int x) {
        return x & (-x);
    }
}
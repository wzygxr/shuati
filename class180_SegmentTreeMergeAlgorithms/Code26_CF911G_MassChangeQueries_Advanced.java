import java.io.*;
import java.util.*;

/**
 * Code26: CF911G Mass Change Queries (Advanced)
 * 线段树分裂 + 值域线段树 + 懒标记
 * 题目：给定一个数组，支持区间[l,r]中所有值为x的数改为y，查询区间和
 * 时间复杂度：O((n+q)log^2n)
 */
public class Code26_CF911G_MassChangeQueries_Advanced {
    
    static class SegmentTree {
        static class Node {
            int l, r;
            long sum;
            int lazy;
            Node left, right;
            
            Node(int l, int r) {
                this.l = l;
                this.r = r;
                this.lazy = -1;
            }
        }
        
        private Node[] roots;
        private int n;
        
        public SegmentTree(int[] arr) {
            this.n = 100;
            this.roots = new Node[101];
            for (int i = 1; i <= 100; i++) {
                roots[i] = new Node(1, n);
            }
            
            for (int i = 0; i < arr.length; i++) {
                updateValue(roots[arr[i]], i + 1, i + 1, 1);
            }
        }
        
        private void pushDown(Node node) {
            if (node.l == node.r) return;
            int mid = (node.l + node.r) >> 1;
            if (node.left == null) {
                node.left = new Node(node.l, mid);
                node.right = new Node(mid + 1, node.r);
            }
            if (node.lazy != -1) {
                node.left.lazy = node.lazy;
                node.right.lazy = node.lazy;
                node.left.sum = (mid - node.l + 1) * node.lazy;
                node.right.sum = (node.r - mid) * node.lazy;
                node.lazy = -1;
            }
        }
        
        private void updateValue(Node node, int L, int R, int val) {
            if (L <= node.l && node.r <= R) {
                node.sum += val;
                return;
            }
            pushDown(node);
            int mid = (node.l + node.r) >> 1;
            if (L <= mid) {
                if (node.left == null) node.left = new Node(node.l, mid);
                updateValue(node.left, L, R, val);
            }
            if (R > mid) {
                if (node.right == null) node.right = new Node(mid + 1, node.r);
                updateValue(node.right, L, R, val);
            }
            node.sum = (node.left != null ? node.left.sum : 0) + 
                      (node.right != null ? node.right.sum : 0);
        }
        
        public void massChange(int l, int r, int x, int y) {
            if (x == y) return;
            
            // 分裂出区间[l,r]对应的线段树
            Node splitTree = split(roots[x], l, r);
            
            if (splitTree != null) {
                // 将分裂出的线段树合并到y对应的线段树
                roots[y] = merge(roots[y], splitTree);
                
                // 更新x对应的线段树
                roots[x] = subtract(roots[x], splitTree);
            }
        }
        
        private Node split(Node node, int L, int R) {
            if (node == null || R < node.l || L > node.r) return null;
            if (L <= node.l && node.r <= R) {
                // 整个节点都在区间内，直接返回
                Node result = new Node(node.l, node.r);
                result.sum = node.sum;
                result.lazy = node.lazy;
                result.left = node.left;
                result.right = node.right;
                
                // 清空原节点
                node.sum = 0;
                node.lazy = -1;
                node.left = null;
                node.right = null;
                
                return result;
            }
            
            pushDown(node);
            int mid = (node.l + node.r) >> 1;
            
            Node leftSplit = null, rightSplit = null;
            if (L <= mid) {
                leftSplit = split(node.left != null ? node.left : new Node(node.l, mid), L, R);
            }
            if (R > mid) {
                rightSplit = split(node.right != null ? node.right : new Node(mid + 1, node.r), L, R);
            }
            
            // 更新当前节点
            node.sum = (node.left != null ? node.left.sum : 0) + 
                      (node.right != null ? node.right.sum : 0);
            
            // 合并分裂结果
            if (leftSplit != null || rightSplit != null) {
                Node result = new Node(node.l, node.r);
                result.left = leftSplit;
                result.right = rightSplit;
                result.sum = (leftSplit != null ? leftSplit.sum : 0) + 
                           (rightSplit != null ? rightSplit.sum : 0);
                return result;
            }
            return null;
        }
        
        private Node merge(Node a, Node b) {
            if (a == null) return b;
            if (b == null) return a;
            
            if (a.l == a.r) {
                // 叶子节点直接合并
                a.sum += b.sum;
                return a;
            }
            
            pushDown(a);
            pushDown(b);
            
            a.left = merge(a.left, b.left);
            a.right = merge(a.right, b.right);
            a.sum = (a.left != null ? a.left.sum : 0) + 
                   (a.right != null ? a.right.sum : 0);
            
            return a;
        }
        
        private Node subtract(Node a, Node b) {
            if (b == null) return a;
            if (a == null) return null;
            
            if (a.l == a.r) {
                a.sum -= b.sum;
                if (a.sum <= 0) return null;
                return a;
            }
            
            pushDown(a);
            pushDown(b);
            
            a.left = subtract(a.left, b.left);
            a.right = subtract(a.right, b.right);
            a.sum = (a.left != null ? a.left.sum : 0) + 
                   (a.right != null ? a.right.sum : 0);
            
            if (a.sum == 0) return null;
            return a;
        }
        
        public long querySum(int l, int r) {
            long sum = 0;
            for (int i = 1; i <= 100; i++) {
                sum += queryRange(roots[i], l, r) * i;
            }
            return sum;
        }
        
        private long queryRange(Node node, int L, int R) {
            if (node == null || R < node.l || L > node.r) return 0;
            if (L <= node.l && node.r <= R) {
                return node.sum;
            }
            pushDown(node);
            int mid = (node.l + node.r) >> 1;
            long res = 0;
            if (L <= mid && node.left != null) {
                res += queryRange(node.left, L, R);
            }
            if (R > mid && node.right != null) {
                res += queryRange(node.right, L, R);
            }
            return res;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        SegmentTree stree = new SegmentTree(arr);
        
        int q = Integer.parseInt(br.readLine());
        while (q-- > 0) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());
            if (type == 1) {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                stree.massChange(l, r, x, y);
            } else {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                pw.println(stree.querySum(l, r));
            }
        }
        
        pw.flush();
        pw.close();
    }
}
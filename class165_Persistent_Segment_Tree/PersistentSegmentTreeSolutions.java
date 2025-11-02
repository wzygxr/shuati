package class158;

import java.io.*;
import java.util.*;

/**
 * 持久化线段树（主席树）完整解决方案
 * 包含所有经典题目和详细实现
 * 时间复杂度分析：构建O(n log n)，单点更新O(log n)，区间查询O(log n)
 * 空间复杂度分析：O(n log n)
 * 最优解分析：对于区间第K小、历史版本查询等问题，持久化线段树是最优解之一
 */

public class PersistentSegmentTreeSolutions {
    
    // 题目1: SPOJ MKTHNUM - K-th Number
    // 题目链接: https://www.spoj.com/problems/MKTHNUM/
    // 题目描述: 给定一个数组，多次查询区间第K小的数
    // 最优解: 持久化线段树是该问题的最优解之一
    public static class MKTHNUM {
        static class Node {
            int left, right, count;
            public Node(int left, int right, int count) {
                this.left = left;
                this.right = right;
                this.count = count;
            }
        }
        
        static List<Node> nodes;
        static List<Integer> roots;
        
        public static void solve() throws IOException {
            Reader in = new Reader();
            PrintWriter out = new PrintWriter(System.out);
            
            int n = in.nextInt();
            int m = in.nextInt();
            
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = in.nextInt();
            }
            
            // 离散化
            int[] sortedArr = arr.clone();
            Arrays.sort(sortedArr);
            Map<Integer, Integer> rank = new HashMap<>();
            int idx = 1;
            for (int num : sortedArr) {
                if (!rank.containsKey(num)) {
                    rank.put(num, idx++);
                }
            }
            
            // 初始化
            nodes = new ArrayList<>();
            roots = new ArrayList<>();
            nodes.add(new Node(0, 0, 0)); // 哨兵节点
            roots.add(0);
            
            // 构建持久化线段树
            for (int i = 0; i < n; i++) {
                int r = update(roots.get(i), 1, idx - 1, rank.get(arr[i]), 1);
                roots.add(r);
            }
            
            // 处理查询
            for (int i = 0; i < m; i++) {
                int l = in.nextInt() - 1;
                int r = in.nextInt() - 1;
                int k = in.nextInt();
                int pos = query(roots.get(l), roots.get(r + 1), 1, idx - 1, k);
                // 找到对应的原始值
                int left = 0, right = sortedArr.length - 1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (rank.get(sortedArr[mid]) == pos) {
                        out.println(sortedArr[mid]);
                        break;
                    } else if (rank.get(sortedArr[mid]) < pos) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
            }
            
            out.flush();
            out.close();
        }
        
        /**
         * 更新线段树节点
         * @param preRoot 前一个版本的根节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param pos 要更新的位置
         * @param val 要增加的值
         * @return 新版本的根节点
         */
        private static int update(int preRoot, int l, int r, int pos, int val) {
            // 创建新节点
            nodes.add(new Node(
                nodes.get(preRoot).left,
                nodes.get(preRoot).right,
                nodes.get(preRoot).count
            ));
            int newRoot = nodes.size() - 1;
            
            if (l == r) {
                nodes.get(newRoot).count += val;
                return newRoot;
            }
            
            int mid = l + (r - l) / 2;
            if (pos <= mid) {
                nodes.get(newRoot).left = update(nodes.get(preRoot).left, l, mid, pos, val);
            } else {
                nodes.get(newRoot).right = update(nodes.get(preRoot).right, mid + 1, r, pos, val);
            }
            
            // 向上更新
            nodes.get(newRoot).count = nodes.get(nodes.get(newRoot).left).count 
                                     + nodes.get(nodes.get(newRoot).right).count;
            return newRoot;
        }
        
        /**
         * 查询区间第k小的数
         * @param root1 前一个版本的根节点
         * @param root2 当前版本的根节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param k 要查询的排名
         * @return 第k小的数在离散化数组中的位置
         */
        private static int query(int root1, int root2, int l, int r, int k) {
            if (l == r) {
                return l;
            }
            
            int mid = l + (r - l) / 2;
            int leftCount = nodes.get(nodes.get(root2).left).count 
                          - nodes.get(nodes.get(root1).left).count;
            
            if (k <= leftCount) {
                return query(nodes.get(root1).left, nodes.get(root2).left, l, mid, k);
            } else {
                return query(nodes.get(root1).right, nodes.get(root2).right, mid + 1, r, k - leftCount);
            }
        }
    }
    
    // 题目2: SPOJ COT - Count on a Tree
    // 题目链接: https://www.spoj.com/problems/COT/
    // 题目描述: 给定一棵树，多次查询两点之间路径上的第K小的数
    // 最优解: 树上持久化线段树（树链剖分+主席树）是该问题的最优解
    public static class COT {
        static class Node {
            int left, right, count;
            public Node(int left, int right, int count) {
                this.left = left;
                this.right = right;
                this.count = count;
            }
        }
        
        static List<Node> nodes;
        static int[] roots;
        static List<Integer>[] graph;
        static int[] values;
        static int[][] parent;
        static int[] depth;
        static int LOG = 20;
        
        public static void solve() throws IOException {
            Reader in = new Reader();
            PrintWriter out = new PrintWriter(System.out);
            
            int n = in.nextInt();
            int m = in.nextInt();
            
            values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = in.nextInt();
            }
            
            // 离散化
            int[] sortedVals = values.clone();
            Arrays.sort(sortedVals);
            Map<Integer, Integer> rank = new HashMap<>();
            int idx = 1;
            for (int val : sortedVals) {
                if (!rank.containsKey(val)) {
                    rank.put(val, idx++);
                }
            }
            
            // 构建图
            graph = new ArrayList[n + 1];
            for (int i = 1; i <= n; i++) {
                graph[i] = new ArrayList<>();
            }
            for (int i = 0; i < n - 1; i++) {
                int u = in.nextInt();
                int v = in.nextInt();
                graph[u].add(v);
                graph[v].add(u);
            }
            
            // 初始化LCA数组
            parent = new int[LOG][n + 1];
            depth = new int[n + 1];
            
            // 初始化持久化线段树
            nodes = new ArrayList<>();
            roots = new int[n + 1];
            nodes.add(new Node(0, 0, 0));
            
            // DFS构建
            dfs(1, 0, rank);
            
            // 构建LCA倍增表
            for (int k = 1; k < LOG; k++) {
                for (int i = 1; i <= n; i++) {
                    parent[k][i] = parent[k - 1][parent[k - 1][i]];
                }
            }
            
            // 处理查询
            for (int i = 0; i < m; i++) {
                int u = in.nextInt();
                int v = in.nextInt();
                int k = in.nextInt();
                int ancestor = lca(u, v);
                int res = query(u, v, ancestor, parent[0][ancestor], 1, idx - 1, k);
                // 找到对应的原始值
                int left = 0, right = sortedVals.length - 1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (rank.get(sortedVals[mid]) == res) {
                        out.println(sortedVals[mid]);
                        break;
                    } else if (rank.get(sortedVals[mid]) < res) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
            }
            
            out.flush();
            out.close();
        }
        
        /**
         * DFS遍历树并构建主席树
         * @param u 当前节点
         * @param p 父节点
         * @param rank 离散化映射
         */
        private static void dfs(int u, int p, Map<Integer, Integer> rank) {
            parent[0][u] = p;
            depth[u] = depth[p] + 1;
            
            // 继承父节点的版本并更新
            roots[u] = update(roots[p], 1, rank.size(), rank.get(values[u - 1]), 1);
            
            for (int v : graph[u]) {
                if (v != p) {
                    dfs(v, u, rank);
                }
            }
        }
        
        /**
         * 计算节点u和节点v的最近公共祖先(LCA)
         * @param u 节点u
         * @param v 节点v
         * @return LCA节点
         */
        private static int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 提升u到v的深度
            for (int k = LOG - 1; k >= 0; k--) {
                if (depth[u] - (1 << k) >= depth[v]) {
                    u = parent[k][u];
                }
            }
            
            if (u == v) return u;
            
            for (int k = LOG - 1; k >= 0; k--) {
                if (parent[k][u] != parent[k][v]) {
                    u = parent[k][u];
                    v = parent[k][v];
                }
            }
            
            return parent[0][u];
        }
        
        /**
         * 更新线段树节点
         * @param preRoot 前一个版本的根节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param pos 要更新的位置
         * @param val 要增加的值
         * @return 新版本的根节点
         */
        private static int update(int preRoot, int l, int r, int pos, int val) {
            nodes.add(new Node(
                nodes.get(preRoot).left,
                nodes.get(preRoot).right,
                nodes.get(preRoot).count
            ));
            int newRoot = nodes.size() - 1;
            
            if (l == r) {
                nodes.get(newRoot).count += val;
                return newRoot;
            }
            
            int mid = l + (r - l) / 2;
            if (pos <= mid) {
                nodes.get(newRoot).left = update(nodes.get(preRoot).left, l, mid, pos, val);
            } else {
                nodes.get(newRoot).right = update(nodes.get(preRoot).right, mid + 1, r, pos, val);
            }
            
            nodes.get(newRoot).count = nodes.get(nodes.get(newRoot).left).count 
                                     + nodes.get(nodes.get(newRoot).right).count;
            return newRoot;
        }
        
        /**
         * 查询树上路径第k小的数
         * @param u 节点u
         * @param v 节点v
         * @param ancestor LCA节点
         * @param ancestorParent LCA的父节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param k 要查询的排名
         * @return 第k小的数在离散化数组中的位置
         */
        private static int query(int u, int v, int ancestor, int ancestorParent, int l, int r, int k) {
            if (l == r) {
                return l;
            }
            
            int mid = l + (r - l) / 2;
            int leftCount = nodes.get(nodes.get(u).left).count + nodes.get(nodes.get(v).left).count
                          - nodes.get(nodes.get(ancestor).left).count - nodes.get(nodes.get(ancestorParent).left).count;
            
            if (k <= leftCount) {
                return query(nodes.get(u).left, nodes.get(v).left, 
                           nodes.get(ancestor).left, nodes.get(ancestorParent).left, 
                           l, mid, k);
            } else {
                return query(nodes.get(u).right, nodes.get(v).right, 
                           nodes.get(ancestor).right, nodes.get(ancestorParent).right, 
                           mid + 1, r, k - leftCount);
            }
        }
    }
    
    // 题目3: LeetCode 2276 - Count Integers in Intervals
    // 题目链接: https://leetcode.com/problems/count-integers-in-intervals/
    // 题目描述: 实现一个数据结构，支持添加区间和查询区间内整数的个数
    // 最优解: 动态开点线段树是该问题的最优解之一
    public static class CountIntervals {
        class Node {
            Node left, right;
            int cnt, cover;
        }
        
        private Node root;
        private int total;
        private final int MAX = 1000000000;
        
        public CountIntervals() {
            root = new Node();
            total = 0;
        }
        
        /**
         * 向上更新节点信息
         * @param node 节点
         * @param l 区间左端点
         * @param r 区间右端点
         */
        private void pushUp(Node node, int l, int r) {
            if (node.cover > 0) {
                node.cnt = r - l + 1;
            } else if (l == r) {
                node.cnt = 0;
            } else {
                node.cnt = (node.left != null ? node.left.cnt : 0) 
                         + (node.right != null ? node.right.cnt : 0);
            }
        }
        
        /**
         * 线段树区间更新操作
         * @param node 当前节点
         * @param l 当前节点表示的区间左端点
         * @param r 当前节点表示的区间右端点
         * @param ul 更新区间左端点
         * @param ur 更新区间右端点
         * @param val 要增加的值
         */
        private void update(Node node, int l, int r, int ul, int ur, int val) {
            if (ur < l || ul > r) {
                return;
            }
            
            if (ul <= l && r <= ur) {
                node.cover += val;
                pushUp(node, l, r);
                return;
            }
            
            // 动态开点
            if (node.left == null) node.left = new Node();
            if (node.right == null) node.right = new Node();
            
            int mid = l + (r - l) / 2;
            update(node.left, l, mid, ul, ur, val);
            update(node.right, mid + 1, r, ul, ur, val);
            pushUp(node, l, r);
        }
        
        /**
         * 添加区间[left, right]到集合中
         * @param left 区间左端点
         * @param right 区间右端点
         */
        public void add(int left, int right) {
            int before = root.cnt;
            update(root, 1, MAX, left, right, 1);
            total = root.cnt;
        }
        
        /**
         * 返回出现在至少一个区间中的整数个数
         * @return 整数个数
         */
        public int count() {
            return total;
        }
    }
    
    // 题目4: LeetCode 1970 - Smallest Missing Genetic Value in Each Subtree
    // 题目链接: https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/
    // 题目描述: 给定一棵树，每个节点有一个基因值，求每个子树中最小缺失的基因值
    public static class SmallestMissingGeneticValue {
        /**
         * 求每个子树中最小缺失的基因值
         * @param parents 父节点数组
         * @param nums 基因值数组
         * @return 每个子树中最小缺失的基因值
         */
        public static int[] smallestMissingValueSubtree(int[] parents, int[] nums) {
            int n = parents.length;
            int[] res = new int[n];
            Arrays.fill(res, 1);
            
            // 检查是否存在值为1的节点
            int posOfOne = -1;
            for (int i = 0; i < n; i++) {
                if (nums[i] == 1) {
                    posOfOne = i;
                    break;
                }
            }
            if (posOfOne == -1) {
                return res;
            }
            
            // 构建子树
            List<Integer>[] children = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                children[i] = new ArrayList<>();
            }
            for (int i = 0; i < n; i++) {
                if (parents[i] != -1) {
                    children[parents[i]].add(i);
                }
            }
            
            // 并查集
            int[] parent = new int[100002];
            for (int i = 0; i < parent.length; i++) {
                parent[i] = i;
            }
            
            // 后序遍历
            boolean[] visited = new boolean[n];
            dfs(posOfOne, children, nums, parent, res, visited);
            
            return res;
        }
        
        /**
         * 并查集查找操作
         * @param parent 并查集数组
         * @param u 节点
         * @return 根节点
         */
        private static int find(int[] parent, int u) {
            if (parent[u] != u) {
                parent[u] = find(parent, parent[u]);
            }
            return parent[u];
        }
        
        /**
         * DFS遍历树并计算结果
         * @param node 当前节点
         * @param children 子节点列表
         * @param nums 基因值数组
         * @param parent 并查集数组
         * @param res 结果数组
         * @param visited 访问标记数组
         */
        private static void dfs(int node, List<Integer>[] children, int[] nums, int[] parent, 
                               int[] res, boolean[] visited) {
            visited[node] = true;
            
            for (int child : children[node]) {
                if (!visited[child]) {
                    dfs(child, children, nums, parent, res, visited);
                }
            }
            
            // 合并当前节点的值
            int u = find(parent, nums[node]);
            parent[u] = u + 1;
            
            if (nums[node] == 1) {
                res[node] = find(parent, 1);
            }
            
            // 向上传递结果
            int curr = node;
            while (parent[curr] != -1) {
                curr = parent[curr];
                if (curr >= 0 && curr < res.length && res[curr] == 1) {
                    res[curr] = find(parent, 1);
                } else {
                    break;
                }
            }
        }
    }
    
    // 题目5: SPOJ DQUERY - D-query
    // 题目链接: https://www.spoj.com/problems/DQUERY/
    // 题目描述: 给定一个数组，多次查询区间内不同元素的个数
    public static class DQUERY {
        public static void solve() throws IOException {
            Reader in = new Reader();
            PrintWriter out = new PrintWriter(System.out);
            
            int n = in.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = in.nextInt();
            }
            
            int m = in.nextInt();
            int[][] queries = new int[m][3];
            for (int i = 0; i < m; i++) {
                queries[i][0] = in.nextInt() - 1;  // l
                queries[i][1] = in.nextInt() - 1;  // r
                queries[i][2] = i;                 // index
            }
            
            // 按右端点排序
            Arrays.sort(queries, Comparator.comparingInt(a -> a[1]));
            
            // 树状数组
            FenwickTree ft = new FenwickTree(n);
            int[] res = new int[m];
            Map<Integer, Integer> last = new HashMap<>();
            int ptr = 0;
            
            for (int i = 0; i < n; i++) {
                if (last.containsKey(arr[i])) {
                    ft.update(last.get(arr[i]), -1);
                }
                ft.update(i, 1);
                last.put(arr[i], i);
                
                while (ptr < m && queries[ptr][1] == i) {
                    int l = queries[ptr][0];
                    int qIdx = queries[ptr][2];
                    res[qIdx] = ft.query(i) - (l > 0 ? ft.query(l - 1) : 0);
                    ptr++;
                }
            }
            
            for (int num : res) {
                out.println(num);
            }
            
            out.flush();
            out.close();
        }
        
        /**
         * 树状数组实现
         */
        static class FenwickTree {
            int[] tree;
            int n;
            
            public FenwickTree(int size) {
                this.n = size;
                this.tree = new int[n + 1];
            }
            
            /**
             * 单点更新
             * @param idx 索引
             * @param val 值
             */
            public void update(int idx, int val) {
                idx++;
                while (idx <= n) {
                    tree[idx] += val;
                    idx += idx & -idx;
                }
            }
            
            /**
             * 前缀和查询
             * @param idx 索引
             * @return 前缀和
             */
            public int query(int idx) {
                idx++;
                int res = 0;
                while (idx > 0) {
                    res += tree[idx];
                    idx -= idx & -idx;
                }
                return res;
            }
        }
    }
    
    // 题目6: 第一次出现位置序列查询
    public static class FirstOccurrence {
        static class Node {
            int left, right, minVal;
            public Node(int left, int right, int minVal) {
                this.left = left;
                this.right = right;
                this.minVal = minVal;
            }
        }
        
        static List<Node> nodes;
        static List<Integer> roots;
        
        public static void solve() throws IOException {
            Reader in = new Reader();
            PrintWriter out = new PrintWriter(System.out);
            
            int n = in.nextInt();
            int q = in.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = in.nextInt();
            }
            
            // 初始化
            nodes = new ArrayList<>();
            roots = new ArrayList<>();
            nodes.add(new Node(0, 0, Integer.MAX_VALUE));
            roots.add(0);
            
            Map<Integer, Integer> lastOccurrence = new HashMap<>();
            
            // 从右往左构建
            for (int i = n - 1; i >= 0; i--) {
                int currentRoot = roots.get(roots.size() - 1);
                
                if (lastOccurrence.containsKey(arr[i])) {
                    // 更新之前的位置
                    currentRoot = update(currentRoot, 1, n, lastOccurrence.get(arr[i]) + 1, Integer.MAX_VALUE);
                }
                
                // 更新当前位置
                currentRoot = update(currentRoot, 1, n, i + 1, i + 1);
                lastOccurrence.put(arr[i], i);
                roots.add(currentRoot);
            }
            
            // 反转roots数组
            Collections.reverse(roots);
            
            // 处理查询
            for (int i = 0; i < q; i++) {
                int l = in.nextInt();
                int r = in.nextInt();
                int minPos = queryMin(roots.get(r), 1, n, l, r);
                out.println(minPos == Integer.MAX_VALUE ? -1 : minPos);
            }
            
            out.flush();
            out.close();
        }
        
        /**
         * 更新线段树节点
         * @param preRoot 前一个版本的根节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param pos 要更新的位置
         * @param val 要设置的值
         * @return 新版本的根节点
         */
        private static int update(int preRoot, int l, int r, int pos, int val) {
            nodes.add(new Node(
                nodes.get(preRoot).left,
                nodes.get(preRoot).right,
                nodes.get(preRoot).minVal
            ));
            int newRoot = nodes.size() - 1;
            
            if (l == r) {
                nodes.get(newRoot).minVal = val;
                return newRoot;
            }
            
            int mid = l + (r - l) / 2;
            if (pos <= mid) {
                nodes.get(newRoot).left = update(nodes.get(preRoot).left, l, mid, pos, val);
            } else {
                nodes.get(newRoot).right = update(nodes.get(preRoot).right, mid + 1, r, pos, val);
            }
            
            // 向上更新
            nodes.get(newRoot).minVal = Math.min(
                nodes.get(nodes.get(newRoot).left).minVal,
                nodes.get(nodes.get(newRoot).right).minVal
            );
            return newRoot;
        }
        
        /**
         * 查询区间最小值
         * @param root 根节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param ql 查询区间左端点
         * @param qr 查询区间右端点
         * @return 区间最小值
         */
        private static int queryMin(int root, int l, int r, int ql, int qr) {
            if (qr < l || ql > r) {
                return Integer.MAX_VALUE;
            }
            
            if (ql <= l && r <= qr) {
                return nodes.get(root).minVal;
            }
            
            int mid = l + (r - l) / 2;
            int leftMin = queryMin(nodes.get(root).left, l, mid, ql, qr);
            int rightMin = queryMin(nodes.get(root).right, mid + 1, r, ql, qr);
            return Math.min(leftMin, rightMin);
        }
    }
    
    // 题目7: 区间最小缺失自然数查询（区间Mex查询）
    public static class RangeMex {
        static class Node {
            int left, right, minPos;
            public Node(int left, int right, int minPos) {
                this.left = left;
                this.right = right;
                this.minPos = minPos;
            }
        }
        
        static List<Node> nodes;
        static List<Integer> roots;
        
        public static void solve() throws IOException {
            Reader in = new Reader();
            PrintWriter out = new PrintWriter(System.out);
            
            int n = in.nextInt();
            int q = in.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = in.nextInt();
            }
            
            // 初始化
            nodes = new ArrayList<>();
            roots = new ArrayList<>();
            nodes.add(new Node(0, 0, Integer.MAX_VALUE));
            roots.add(0);
            
            int maxVal = 0;
            for (int num : arr) {
                if (num >= 0) {
                    maxVal = Math.max(maxVal, num);
                }
            }
            maxVal = Math.max(maxVal, n);
            
            Map<Integer, Integer> lastOccurrence = new HashMap<>();
            
            for (int i = 0; i < n; i++) {
                int val = arr[i];
                int newRoot = roots.get(i);
                
                if (val >= 0) {
                    if (lastOccurrence.containsKey(val)) {
                        newRoot = update(newRoot, 0, maxVal, val, i);
                    } else {
                        newRoot = update(newRoot, 0, maxVal, val, i);
                    }
                    lastOccurrence.put(val, i);
                }
                
                roots.add(newRoot);
            }
            
            // 处理查询
            for (int i = 0; i < q; i++) {
                int l = in.nextInt() - 1;
                int r = in.nextInt() - 1;
                int mex = queryMex(roots.get(r + 1), 0, maxVal, l);
                out.println(mex);
            }
            
            out.flush();
            out.close();
        }
        
        /**
         * 更新线段树节点
         * @param preRoot 前一个版本的根节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param pos 要更新的位置
         * @param val 要设置的值
         * @return 新版本的根节点
         */
        private static int update(int preRoot, int l, int r, int pos, int val) {
            nodes.add(new Node(
                nodes.get(preRoot).left,
                nodes.get(preRoot).right,
                nodes.get(preRoot).minPos
            ));
            int newRoot = nodes.size() - 1;
            
            if (l == r) {
                nodes.get(newRoot).minPos = val;
                return newRoot;
            }
            
            int mid = l + (r - l) / 2;
            if (pos <= mid) {
                nodes.get(newRoot).left = update(nodes.get(preRoot).left, l, mid, pos, val);
            } else {
                nodes.get(newRoot).right = update(nodes.get(preRoot).right, mid + 1, r, pos, val);
            }
            
            nodes.get(newRoot).minPos = Math.min(
                nodes.get(nodes.get(newRoot).left).minPos,
                nodes.get(nodes.get(newRoot).right).minPos
            );
            return newRoot;
        }
        
        /**
         * 查询区间Mex
         * @param root 根节点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param currentL 当前查询左端点
         * @return Mex值
         */
        private static int queryMex(int root, int l, int r, int currentL) {
            if (l == r) {
                return l;
            }
            
            int mid = l + (r - l) / 2;
            int leftMin = nodes.get(nodes.get(root).left).minPos;
            
            if (leftMin < currentL) {
                return queryMex(nodes.get(root).left, l, mid, currentL);
            } else {
                return queryMex(nodes.get(root).right, mid + 1, r, currentL);
            }
        }
    }
    
    // 高效读写类
    static class Reader {
        private final InputStreamReader reader;
        private final BufferedReader br;
        private StringTokenizer st;
        
        public Reader() {
            reader = new InputStreamReader(System.in);
            br = new BufferedReader(reader, 32768);
            st = new StringTokenizer("");
        }
        
        public String nextLine() throws IOException {
            return br.readLine();
        }
        
        public String next() throws IOException {
            while (!st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return st.nextToken();
        }
        
        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
        
        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }
    
    public static void main(String[] args) throws IOException {
        // 测试各个问题
        // 可以根据需要取消注释相应的测试函数
        // MKTHNUM.solve();
        // COT.solve();
        // DQUERY.solve();
        // FirstOccurrence.solve();
        // RangeMex.solve();
        
        // 测试LeetCode 2276
        CountIntervals intervals = new CountIntervals();
        intervals.add(2, 3);
        intervals.add(7, 10);
        System.out.println(intervals.count());  // 输出: 6
        intervals.add(5, 8);
        System.out.println(intervals.count());  // 输出: 8
        
        // 测试LeetCode 1970
        int[] parents = {-1, 0, 0, 2};
        int[] nums = {1, 2, 3, 4};
        int[] res = SmallestMissingGeneticValue.smallestMissingValueSubtree(parents, nums);
        System.out.println(Arrays.toString(res));  // 输出: [5, 1, 1, 1]
    }
}
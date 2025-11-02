import java.io.*;
import java.util.*;

/**
 * P5298 [PKUWC2018] Minimax - 线段树合并加速概率DP转移
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P5298
 * 
 * 题目描述:
 * 给定一棵二叉树，每个叶子节点有一个权值，非叶子节点有概率取左右子树的最大值或最小值。
 * 求每个叶子节点成为最终根节点权值的概率。
 * 
 * 核心算法: 概率DP + 线段树合并优化
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 定义DP状态: dp[u][x] 表示以u为根的子树，最终权值为x的概率
 * 2. 状态转移:
 *    - 叶子节点: dp[u][w] = 1
 *    - 非叶子节点: 根据概率取左右子树的最大值或最小值
 * 3. 使用线段树合并来优化概率分布合并
 * 4. 维护每个节点的概率分布线段树
 * 
 * 线段树合并加速概率DP转移的关键:
 * 1. 动态开点线段树维护概率分布
 * 2. 合并子树概率分布时进行概率转移
 * 3. 支持区间乘、区间加操作
 * 4. 离散化权值范围
 */
public class Code22_P5298_Minimax_DP_Optimization {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(System.out);
        }
        
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        double nextDouble() { return Double.parseDouble(next()); }
        
        void println(Object obj) { out.println(obj); }
        void close() { out.close(); }
    }
    
    static class SegmentTree {
        static class Node {
            int left, right;
            double sum;      // 概率和
            double mul;      // 乘法懒标记
            double add;      // 加法懒标记
            
            Node() {
                this.left = -1;
                this.right = -1;
                this.sum = 0;
                this.mul = 1;
                this.add = 0;
            }
        }
        
        Node[] tree;
        int root;
        int cnt;
        int maxn;
        
        public SegmentTree(int maxn) {
            this.maxn = maxn;
            tree = new Node[2000000];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node();
            }
            root = -1;
            cnt = 0;
        }
        
        int newNode() {
            if (cnt >= tree.length) {
                Node[] newTree = new Node[tree.length * 2];
                System.arraycopy(tree, 0, newTree, 0, tree.length);
                for (int i = tree.length; i < newTree.length; i++) {
                    newTree[i] = new Node();
                }
                tree = newTree;
            }
            tree[cnt].sum = 0;
            tree[cnt].mul = 1;
            tree[cnt].add = 0;
            tree[cnt].left = -1;
            tree[cnt].right = -1;
            return cnt++;
        }
        
        void apply(int p, double mul, double add) {
            if (p == -1) return;
            tree[p].sum = tree[p].sum * mul + add;
            tree[p].mul *= mul;
            tree[p].add = tree[p].add * mul + add;
        }
        
        void pushDown(int p) {
            if (tree[p].mul != 1 || tree[p].add != 0) {
                if (tree[p].left == -1) tree[p].left = newNode();
                if (tree[p].right == -1) tree[p].right = newNode();
                
                apply(tree[p].left, tree[p].mul, tree[p].add);
                apply(tree[p].right, tree[p].mul, tree[p].add);
                
                tree[p].mul = 1;
                tree[p].add = 0;
            }
        }
        
        void update(int p, int l, int r, int pos, double val) {
            if (l == r) {
                tree[p].sum = val;
                return;
            }
            
            pushDown(p);
            int mid = (l + r) / 2;
            
            if (pos <= mid) {
                if (tree[p].left == -1) tree[p].left = newNode();
                update(tree[p].left, l, mid, pos, val);
            } else {
                if (tree[p].right == -1) tree[p].right = newNode();
                update(tree[p].right, mid + 1, r, pos, val);
            }
            
            tree[p].sum = (tree[p].left != -1 ? tree[tree[p].left].sum : 0) + 
                         (tree[p].right != -1 ? tree[tree[p].right].sum : 0);
        }
        
        double query(int p, int l, int r, int ql, int qr) {
            if (ql > qr || p == -1) return 0;
            if (ql <= l && r <= qr) {
                return tree[p].sum;
            }
            
            pushDown(p);
            int mid = (l + r) / 2;
            double res = 0;
            
            if (ql <= mid && tree[p].left != -1) {
                res += query(tree[p].left, l, mid, ql, qr);
            }
            if (qr > mid && tree[p].right != -1) {
                res += query(tree[p].right, mid + 1, r, ql, qr);
            }
            
            return res;
        }
        
        // 关键操作: 合并两棵概率分布线段树
        int merge(int p, int q, int l, int r, double pMul, double pAdd, double qMul, double qAdd) {
            if (p == -1 && q == -1) return -1;
            if (p == -1) {
                apply(q, qMul, qAdd);
                return q;
            }
            if (q == -1) {
                apply(p, pMul, pAdd);
                return p;
            }
            
            if (l == r) {
                int newP = newNode();
                tree[newP].sum = tree[p].sum * pMul + pAdd + tree[q].sum * qMul + qAdd;
                return newP;
            }
            
            pushDown(p);
            pushDown(q);
            
            int mid = (l + r) / 2;
            
            // 递归合并左右子树
            int leftMerge = merge(
                tree[p].left, tree[q].left, l, mid,
                pMul, pAdd, qMul, qAdd
            );
            int rightMerge = merge(
                tree[p].right, tree[q].right, mid + 1, r,
                pMul, pAdd, qMul, qAdd
            );
            
            int newP = newNode();
            tree[newP].left = leftMerge;
            tree[newP].right = rightMerge;
            tree[newP].sum = (leftMerge != -1 ? tree[leftMerge].sum : 0) + 
                           (rightMerge != -1 ? tree[rightMerge].sum : 0);
            
            return newP;
        }
        
        // 获取所有叶子节点的概率
        void getLeaves(int p, int l, int r, double[] result, int[] values) {
            if (p == -1) return;
            
            if (l == r) {
                result[values[l]] = tree[p].sum;
                return;
            }
            
            pushDown(p);
            int mid = (l + r) / 2;
            
            getLeaves(tree[p].left, l, mid, result, values);
            getLeaves(tree[p].right, mid + 1, r, result, values);
        }
    }
    
    static int n;
    static int[] parent, leftChild, rightChild;
    static double[] probability;
    static int[] leafValue;
    static boolean[] isLeaf;
    static SegmentTree[] dpTrees;
    static int[] values;
    static Map<Integer, Integer> valueToIndex;
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        parent = new int[n + 1];
        leftChild = new int[n + 1];
        rightChild = new int[n + 1];
        probability = new double[n + 1];
        leafValue = new int[n + 1];
        isLeaf = new boolean[n + 1];
        
        // 读取输入
        Set<Integer> valueSet = new TreeSet<>();
        for (int i = 1; i <= n; i++) {
            int p = io.nextInt();
            parent[i] = p;
            if (p != 0) {
                if (leftChild[p] == 0) {
                    leftChild[p] = i;
                } else {
                    rightChild[p] = i;
                }
            }
        }
        
        for (int i = 1; i <= n; i++) {
            if (leftChild[i] == 0) {
                // 叶子节点
                isLeaf[i] = true;
                leafValue[i] = io.nextInt();
                valueSet.add(leafValue[i]);
            } else {
                // 非叶子节点
                probability[i] = io.nextDouble();
            }
        }
        
        // 离散化权值
        values = new int[valueSet.size()];
        valueToIndex = new HashMap<>();
        int idx = 0;
        for (int val : valueSet) {
            values[idx] = val;
            valueToIndex.put(val, idx);
            idx++;
        }
        
        int maxn = valueSet.size();
        dpTrees = new SegmentTree[n + 1];
        for (int i = 1; i <= n; i++) {
            dpTrees[i] = new SegmentTree(maxn);
        }
        
        // 从叶子节点开始DFS
        for (int i = n; i >= 1; i--) {
            if (isLeaf[i]) {
                // 叶子节点: 概率为1
                int pos = valueToIndex.get(leafValue[i]);
                dpTrees[i].root = dpTrees[i].newNode();
                dpTrees[i].update(dpTrees[i].root, 0, maxn - 1, pos, 1.0);
            } else {
                // 非叶子节点: 合并左右子树
                int l = leftChild[i];
                int r = rightChild[i];
                double p = probability[i];
                
                if (dpTrees[l].root == -1) dpTrees[l].root = dpTrees[l].newNode();
                if (dpTrees[r].root == -1) dpTrees[r].root = dpTrees[r].newNode();
                
                // 计算概率转移系数
                double lSum = dpTrees[l].query(dpTrees[l].root, 0, maxn - 1, 0, maxn - 1);
                double rSum = dpTrees[r].query(dpTrees[r].root, 0, maxn - 1, 0, maxn - 1);
                
                // 合并概率分布
                dpTrees[i].root = dpTrees[i].merge(
                    dpTrees[l].root, dpTrees[r].root, 0, maxn - 1,
                    p, (1 - p) * rSum,  // 左子树转移系数
                    (1 - p), p * lSum    // 右子树转移系数
                );
            }
        }
        
        // 输出结果: 每个叶子节点的概率
        double[] result = new double[1000000];
        dpTrees[1].getLeaves(dpTrees[1].root, 0, maxn - 1, result, values);
        
        for (int i = 1; i <= n; i++) {
            if (isLeaf[i]) {
                io.println(String.format("%.6f", result[leafValue[i]]));
            }
        }
        
        io.close();
    }
}

/**
 * 线段树合并加速概率DP转移的核心优势:
 * 1. 高效合并概率分布: O(log n)时间复杂度合并两棵概率分布线段树
 * 2. 支持复杂概率运算: 支持乘法、加法等概率运算
 * 3. 动态开点: 节省空间，只维护必要的概率分布
 * 4. 离散化优化: 通过离散化减少线段树规模
 * 
 * 类似题目推荐:
 * 1. P6773 [NOI2020] 命运 - 树形DP + 线段树合并
 * 2. CF455D Serega and Fun - 区间操作 + DP优化
 * 3. CF868F Yet Another Minimization Problem - 分治DP + 线段树
 * 4. CF321E Ciel and Gondolas - 四边形不等式优化
 * 5. P4770 [NOI2018] 你的名字 - 后缀自动机 + 线段树合并
 * 
 * 算法复杂度分析:
 * - 时间复杂度: O(n log n)，每个节点最多被合并log n次
 * - 空间复杂度: O(n log n)，动态开点线段树的空间消耗
 * 
 * 适用场景:
 * 1. 概率DP问题，需要合并概率分布
 * 2. DP状态转移涉及复杂概率运算
 * 3. 需要支持动态修改和查询概率分布
 * 4. 数据规模较大，需要高效算法
 */
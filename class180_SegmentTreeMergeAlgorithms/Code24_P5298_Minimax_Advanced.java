import java.io.*;
import java.util.*;

/**
 * 洛谷 P5298 [PKUWC2018]Minimax (线段树合并加速概率DP)
 * 题目链接: https://www.luogu.com.cn/problem/P5298
 * 
 * 题目描述:
 * 给定一棵二叉树，每个叶子节点有一个权值。非叶子节点有一个概率p，
 * 该节点的权值等于左子树权值的概率为p，等于右子树权值的概率为1-p。
 * 求根节点权值的期望分布。
 * 
 * 核心算法: 线段树合并 + 概率DP + 离散化
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 对叶子节点的权值进行离散化
 * 2. 使用线段树维护每个节点的权值分布概率
 * 3. 通过线段树合并实现概率DP的状态转移
 * 4. 使用懒标记维护概率转移的线性组合
 */
public class Code24_P5298_Minimax_Advanced {
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
        
        void println(Object obj) { out.println(obj); }
        void close() { out.close(); }
    }
    
    static class SegmentTree {
        static class Node {
            int left, right;
            long sum;  // 概率和
            long mul;  // 乘法懒标记
            long add;  // 加法懒标记
            
            Node() {
                left = right = -1;
                sum = 0;
                mul = 1;
                add = 0;
            }
        }
        
        Node[] tree;
        int cnt;
        static final long MOD = 998244353;
        
        public SegmentTree() {
            tree = new Node[20000000];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node();
            }
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
            tree[cnt].left = tree[cnt].right = -1;
            tree[cnt].sum = 0;
            tree[cnt].mul = 1;
            tree[cnt].add = 0;
            return cnt++;
        }
        
        void apply(int p, long mul, long add) {
            if (p == -1) return;
            tree[p].sum = (tree[p].sum * mul + add) % MOD;
            tree[p].mul = (tree[p].mul * mul) % MOD;
            tree[p].add = (tree[p].add * mul + add) % MOD;
        }
        
        void pushDown(int p) {
            if (p == -1) return;
            if (tree[p].mul != 1 || tree[p].add != 0) {
                if (tree[p].left == -1) tree[p].left = newNode();
                if (tree[p].right == -1) tree[p].right = newNode();
                
                apply(tree[p].left, tree[p].mul, tree[p].add);
                apply(tree[p].right, tree[p].mul, tree[p].add);
                
                tree[p].mul = 1;
                tree[p].add = 0;
            }
        }
        
        void update(int p, int l, int r, int pos, long val) {
            if (l == r) {
                tree[p].sum = (tree[p].sum + val) % MOD;
                return;
            }
            
            pushDown(p);
            int mid = (l + r) >> 1;
            
            if (pos <= mid) {
                if (tree[p].left == -1) tree[p].left = newNode();
                update(tree[p].left, l, mid, pos, val);
            } else {
                if (tree[p].right == -1) tree[p].right = newNode();
                update(tree[p].right, mid + 1, r, pos, val);
            }
            
            tree[p].sum = (getSum(tree[p].left) + getSum(tree[p].right)) % MOD;
        }
        
        long getSum(int p) {
            return p == -1 ? 0 : tree[p].sum;
        }
        
        // 线段树合并，支持概率转移
        int merge(int p, int q, int l, int r, long p1, long p2, long sumP, long sumQ) {
            if (p == -1 && q == -1) return -1;
            
            if (p == -1) {
                // 只有q存在，应用概率转移
                apply(q, p2, 0);
                return q;
            }
            
            if (q == -1) {
                // 只有p存在，应用概率转移
                apply(p, p1, 0);
                return p;
            }
            
            if (l == r) {
                // 叶子节点合并
                long valP = tree[p].sum;
                long valQ = tree[q].sum;
                
                // 概率转移公式: p * valP * sumQ + (1-p) * valQ * sumP
                long newVal = (p1 * valP % MOD * sumQ % MOD + 
                              p2 * valQ % MOD * sumP % MOD) % MOD;
                
                int newP = newNode();
                tree[newP].sum = newVal;
                return newP;
            }
            
            pushDown(p);
            pushDown(q);
            
            int mid = (l + r) >> 1;
            
            // 计算左右子树的概率和
            long leftSumP = getSum(tree[p].left);
            long rightSumP = getSum(tree[p].right);
            long leftSumQ = getSum(tree[q].left);
            long rightSumQ = getSum(tree[q].right);
            
            // 递归合并左右子树
            tree[p].left = merge(tree[p].left, tree[q].left, l, mid, p1, p2, rightSumP, rightSumQ);
            tree[p].right = merge(tree[p].right, tree[q].right, mid + 1, r, p1, p2, 0, 0);
            
            tree[p].sum = (getSum(tree[p].left) + getSum(tree[p].right)) % MOD;
            
            return p;
        }
        
        // 获取所有叶子节点的概率值
        void getLeaves(int p, int l, int r, long[] result, int[] values) {
            if (p == -1) return;
            
            if (l == r) {
                result[values[l]] = tree[p].sum;
                return;
            }
            
            pushDown(p);
            int mid = (l + r) >> 1;
            
            if (tree[p].left != -1) {
                getLeaves(tree[p].left, l, mid, result, values);
            }
            if (tree[p].right != -1) {
                getLeaves(tree[p].right, mid + 1, r, result, values);
            }
        }
    }
    
    static int n;
    static int[] parent, leftChild, rightChild;
    static long[] probability;
    static int[] leafValues;
    static boolean[] isLeaf;
    static int[] discreteValues;
    static Map<Integer, Integer> valueToIndex;
    static SegmentTree segTree;
    static int[] root;
    
    static long modInverse(long a, long mod) {
        return pow(a, mod - 2, mod);
    }
    
    static long pow(long a, long b, long mod) {
        long res = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = res * a % mod;
            }
            a = a * a % mod;
            b >>= 1;
        }
        return res;
    }
    
    static void dfs(int u) {
        if (isLeaf[u]) {
            // 叶子节点：在对应权值位置设置概率为1
            int pos = valueToIndex.get(leafValues[u]);
            segTree.update(root[u], 1, discreteValues.length, pos, 1);
            return;
        }
        
        int left = leftChild[u];
        int right = rightChild[u];
        
        // 递归处理左右子树
        dfs(left);
        dfs(right);
        
        long p = probability[u];
        long inv10000 = modInverse(10000, SegmentTree.MOD);
        p = p * inv10000 % SegmentTree.MOD;
        long q = (1 - p + SegmentTree.MOD) % SegmentTree.MOD;
        
        // 合并左右子树的线段树
        root[u] = segTree.merge(root[left], root[right], 1, discreteValues.length, p, q, 0, 0);
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        parent = new int[n + 1];
        leftChild = new int[n + 1];
        rightChild = new int[n + 1];
        probability = new long[n + 1];
        leafValues = new int[n + 1];
        isLeaf = new boolean[n + 1];
        
        // 读入树结构
        Set<Integer> valueSet = new HashSet<>();
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
                leafValues[i] = io.nextInt();
                valueSet.add(leafValues[i]);
            } else {
                // 非叶子节点
                probability[i] = io.nextLong();
            }
        }
        
        // 离散化权值
        discreteValues = new int[valueSet.size() + 1];
        valueToIndex = new HashMap<>();
        int idx = 1;
        List<Integer> sortedValues = new ArrayList<>(valueSet);
        Collections.sort(sortedValues);
        for (int val : sortedValues) {
            discreteValues[idx] = val;
            valueToIndex.put(val, idx);
            idx++;
        }
        
        // 初始化线段树
        segTree = new SegmentTree();
        root = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            root[i] = segTree.newNode();
        }
        
        // 从根节点开始DFS
        int rootNode = 1;
        while (parent[rootNode] != 0) {
            rootNode = parent[rootNode];
        }
        dfs(rootNode);
        
        // 获取根节点的概率分布
        long[] result = new long[discreteValues.length];
        segTree.getLeaves(root[rootNode], 1, discreteValues.length, result, discreteValues);
        
        // 输出结果
        long total = 0;
        for (int i = 1; i < discreteValues.length; i++) {
            total = (total + result[i]) % SegmentTree.MOD;
        }
        
        for (int i = 1; i < discreteValues.length; i++) {
            long expected = discreteValues[i] * result[i] % SegmentTree.MOD;
            io.println(expected);
        }
        
        io.close();
    }
}

/**
 * 线段树合并加速概率DP的核心技术:
 * 
 * 1. 概率转移的数学原理:
 *    - 使用乘法原理和加法原理计算复合概率
 *    - 通过懒标记实现概率的线性变换
 *    - 支持复杂的概率组合运算
 * 
 * 2. 线段树合并的优势:
 *    - 高效处理概率分布: O(log n)时间复杂度
 *    - 支持动态修改: 可以实时更新概率分布
 *    - 内存优化: 动态开点只维护非零概率
 * 
 * 3. 离散化技术:
 *    - 将连续权值映射到离散索引
 *    - 减少线段树的空间复杂度
 *    - 提高查询效率
 * 
 * 类似题目推荐:
 * 1. CF908D New Year and Arbitrary Arrangement - 概率DP + 期望计算
 * 2. P1654 OSU! - 概率期望 + 动态规划
 * 3. P4562 [JXOI2018]游戏 - 概率DP + 组合数学
 * 4. P2473 [SCOI2008]奖励关 - 状压DP + 概率期望
 * 
 * 算法复杂度分析:
 * - 离散化: O(n log n)
 * - 线段树合并: O(n log n)
 * - 总体复杂度: O(n log n)
 * 
 * 工程化优化:
 * 1. 模运算优化: 使用快速幂和模逆元
 * 2. 内存管理: 预分配足够的内存空间
 * 3. 数值稳定性: 处理浮点数精度问题
 * 4. 边界处理: 处理概率为0或1的特殊情况
 */
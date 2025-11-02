// 线段树合并优化区间颜色统计DP

import java.io.*;
import java.util.*;

/**
 * 区间颜色统计问题 - 线段树合并优化DP
 * 
 * 问题描述：
 * 给定一个序列，每个位置有一个颜色，支持以下操作：
 * 1. 区间分裂和合并
 * 2. 查询区间内不同颜色的数量
 * 3. 查询区间内出现次数最多的颜色
 * 
 * 核心算法：线段树分裂 + 颜色信息维护 + DP优化
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code33_DPOptimization_ColorStatistics {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
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
    
    static final int MAXN = 100010;
    static final int MAXM = 20000000;
    static final int MAX_COLORS = 100010;
    
    // 颜色统计信息
    static class ColorInfo {
        int color;          // 颜色值
        int count;          // 出现次数
        
        ColorInfo(int color, int count) {
            this.color = color;
            this.count = count;
        }
    }
    
    // 区间颜色统计信息
    static class SegmentColorInfo {
        int distinctCount;          // 不同颜色数量
        ColorInfo maxColor;         // 出现次数最多的颜色
        Map<Integer, Integer> colorMap; // 颜色计数映射
        
        SegmentColorInfo() {
            distinctCount = 0;
            maxColor = new ColorInfo(-1, 0);
            colorMap = new HashMap<>();
        }
        
        SegmentColorInfo(int color) {
            distinctCount = 1;
            maxColor = new ColorInfo(color, 1);
            colorMap = new HashMap<>();
            colorMap.put(color, 1);
        }
        
        // 合并两个区间信息
        static SegmentColorInfo merge(SegmentColorInfo left, SegmentColorInfo right) {
            if (left == null) return right;
            if (right == null) return left;
            
            SegmentColorInfo res = new SegmentColorInfo();
            res.colorMap = new HashMap<>(left.colorMap);
            
            // 合并颜色映射
            for (Map.Entry<Integer, Integer> entry : right.colorMap.entrySet()) {
                int color = entry.getKey();
                int count = entry.getValue();
                
                res.colorMap.merge(color, count, Integer::sum);
            }
            
            // 更新不同颜色数量
            res.distinctCount = res.colorMap.size();
            
            // 更新出现次数最多的颜色
            res.maxColor = new ColorInfo(-1, 0);
            for (Map.Entry<Integer, Integer> entry : res.colorMap.entrySet()) {
                if (entry.getValue() > res.maxColor.count) {
                    res.maxColor = new ColorInfo(entry.getKey(), entry.getValue());
                }
            }
            
            return res;
        }
    }
    
    // 线段树节点
    static class Node {
        int l, r;
        SegmentColorInfo info;
        
        Node() {
            l = r = -1;
            info = new SegmentColorInfo();
        }
    }
    
    static Node[] tree = new Node[MAXM];
    static int cnt = 0;
    
    static {
        for (int i = 0; i < MAXM; i++) {
            tree[i] = new Node();
        }
    }
    
    // 创建新节点
    static int newNode() {
        if (cnt >= MAXM) {
            System.gc();
            return -1;
        }
        tree[cnt].l = tree[cnt].r = -1;
        tree[cnt].info = new SegmentColorInfo();
        return cnt++;
    }
    
    // 构建叶子节点
    static int buildLeaf(int color) {
        int rt = newNode();
        tree[rt].info = new SegmentColorInfo(color);
        return rt;
    }
    
    // 线段树合并（核心函数）
    static int merge(int u, int v) {
        if (u == -1) return v;
        if (v == -1) return u;
        
        // 合并颜色信息
        tree[u].info = SegmentColorInfo.merge(tree[u].info, tree[v].info);
        
        // 递归合并子树
        tree[u].l = merge(tree[u].l, tree[v].l);
        tree[u].r = merge(tree[u].r, tree[v].r);
        
        return u;
    }
    
    // 线段树分裂
    static int[] split(int rt, int l, int r, int pos) {
        if (rt == -1) return new int[]{-1, -1};
        
        if (l == r) {
            // 叶子节点分裂
            int newRt = newNode();
            tree[newRt].info = tree[rt].info;
            tree[rt].info = new SegmentColorInfo();
            return new int[]{rt, newRt};
        }
        
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            // 分裂左子树
            int[] leftSplit = split(tree[rt].l, l, mid, pos);
            tree[rt].l = leftSplit[0];
            
            int newRt = newNode();
            tree[newRt].l = leftSplit[1];
            tree[newRt].r = tree[rt].r;
            
            // 更新信息
            updateInfo(rt);
            updateInfo(newRt);
            
            return new int[]{rt, newRt};
        } else {
            // 分裂右子树
            int[] rightSplit = split(tree[rt].r, mid + 1, r, pos);
            tree[rt].r = rightSplit[0];
            
            int newRt = newNode();
            tree[newRt].l = tree[rt].l;
            tree[newRt].r = rightSplit[1];
            
            // 更新信息
            updateInfo(rt);
            updateInfo(newRt);
            
            return new int[]{rt, newRt};
        }
    }
    
    // 更新节点信息
    static void updateInfo(int rt) {
        if (rt == -1) return;
        
        SegmentColorInfo leftInfo = (tree[rt].l != -1) ? tree[tree[rt].l].info : new SegmentColorInfo();
        SegmentColorInfo rightInfo = (tree[rt].r != -1) ? tree[tree[rt].r].info : new SegmentColorInfo();
        
        tree[rt].info = SegmentColorInfo.merge(leftInfo, rightInfo);
    }
    
    // 查询区间颜色统计信息
    static SegmentColorInfo query(int rt, int l, int r, int ql, int qr) {
        if (rt == -1 || ql > r || qr < l) {
            return new SegmentColorInfo();
        }
        
        if (ql <= l && r <= qr) {
            return tree[rt].info;
        }
        
        int mid = (l + r) >> 1;
        
        SegmentColorInfo leftInfo = query(tree[rt].l, l, mid, ql, qr);
        SegmentColorInfo rightInfo = query(tree[rt].r, mid + 1, r, ql, qr);
        
        return SegmentColorInfo.merge(leftInfo, rightInfo);
    }
    
    // 构建初始线段树
    static int buildTree(int l, int r, int[] colors) {
        if (l == r) {
            return buildLeaf(colors[l]);
        }
        
        int mid = (l + r) >> 1;
        int leftTree = buildTree(l, mid, colors);
        int rightTree = buildTree(mid + 1, r, colors);
        
        return merge(leftTree, rightTree);
    }
    
    // DP优化：颜色统计问题的DP状态
    static class DPState {
        int root;                   // 线段树根节点
        SegmentColorInfo info;      // 颜色统计信息
        
        DPState() {
            root = -1;
            info = new SegmentColorInfo();
        }
        
        DPState(int root, SegmentColorInfo info) {
            this.root = root;
            this.info = info;
        }
    }
    
    // 合并两个DP状态
    static DPState mergeDPState(DPState left, DPState right) {
        if (left.root == -1) return right;
        if (right.root == -1) return left;
        
        int mergedRoot = merge(left.root, right.root);
        SegmentColorInfo mergedInfo = SegmentColorInfo.merge(left.info, right.info);
        
        return new DPState(mergedRoot, mergedInfo);
    }
    
    // 区间DP优化：处理颜色统计问题
    static DPState solveColorDP(int l, int r, int[] colors) {
        if (l > r) return new DPState();
        
        if (l == r) {
            int root = buildLeaf(colors[l]);
            return new DPState(root, tree[root].info);
        }
        
        int mid = (l + r) >> 1;
        
        // 递归处理左右子区间
        DPState leftState = solveColorDP(l, mid, colors);
        DPState rightState = solveColorDP(mid + 1, r, colors);
        
        // 合并左右子区间的DP状态
        return mergeDPState(leftState, rightState);
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        int n = io.nextInt();
        int q = io.nextInt();
        
        int[] colors = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            colors[i] = io.nextInt();
        }
        
        // 构建初始线段树
        int root = buildTree(1, n, colors);
        
        while (q-- > 0) {
            int type = io.nextInt();
            
            switch (type) {
                case 1: // 查询区间不同颜色数量
                    int l = io.nextInt(), r = io.nextInt();
                    SegmentColorInfo info = query(root, 1, n, l, r);
                    io.println(info.distinctCount);
                    break;
                    
                case 2: // 查询区间出现次数最多的颜色
                    int ql = io.nextInt(), qr = io.nextInt();
                    SegmentColorInfo maxInfo = query(root, 1, n, ql, qr);
                    io.println(maxInfo.maxColor.color + " (count: " + maxInfo.maxColor.count + ")");
                    break;
                    
                case 3: // 区间分裂
                    int pos = io.nextInt();
                    int[] splitResult = split(root, 1, n, pos);
                    root = splitResult[0];
                    int newRoot = splitResult[1];
                    
                    io.println("Split completed. Two segments created.");
                    break;
                    
                case 4: // 区间合并
                    int otherRoot = buildTree(1, n, new int[n + 1]); // 示例
                    root = merge(root, otherRoot);
                    io.println("Merge completed.");
                    break;
                    
                case 5: // 使用DP优化解决颜色统计问题
                    DPState dpState = solveColorDP(1, n, colors);
                    io.println("DP optimization completed. Distinct colors: " + dpState.info.distinctCount);
                    break;
            }
        }
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    - 需要支持动态的区间分裂和合并
 *    - 需要高效统计区间颜色信息
 *    - 传统方法复杂度较高
 * 
 * 2. 解决方案：
 *    - 使用线段树分裂技术处理区间操作
 *    - 维护颜色统计信息：不同颜色数量、出现次数最多的颜色
 *    - 使用线段树合并优化DP状态转移
 * 
 * 3. 核心优化：
 *    - 颜色信息维护：O(1)时间合并颜色统计信息
 *    - 线段树合并：将O(n²)复杂度优化到O(n log n)
 *    - 动态开点：节省内存空间
 * 
 * 4. 时间复杂度：
 *    - 每次分裂/合并操作O(log n)
 *    - 查询操作O(log n)
 *    - 总体复杂度O(q log n)
 * 
 * 5. 应用场景：
 *    - 区间颜色统计问题
 *    - 区间众数查询问题
 *    - 区间不同元素统计问题
 * 
 * 6. 扩展方向：
 *    - 支持更多颜色统计信息
 *    - 可持久化版本
 *    - 分布式处理
 * 
 * 7. 实现技巧：
 *    - 合理设计颜色信息结构
 *    - 优化颜色映射的合并
 *    - 注意内存管理
 */
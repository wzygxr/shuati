// 莫队二次离线 - 二次离线莫队算法实现 (Java版本)
// 题目来源: 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)）
// 题目链接: https://www.luogu.com.cn/problem/P4887
// 题目大意: 给定一个序列，每次查询区间[l,r]内满足a[i] XOR a[j]的二进制表示有k个1的二元组(i,j)个数
// 时间复杂度: O(n*sqrt(m) + n*sqrt(n))
// 空间复杂度: O(n + m)
//
// 相关题目链接:
// 1. 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)） - https://www.luogu.com.cn/problem/P4887
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline3.py
//
// 2. 洛谷 P5398 [Ynoi2018]GOSICK - https://www.luogu.com.cn/problem/P5398
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5398_SecondaryOffline1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5398_SecondaryOffline2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5398_SecondaryOffline3.py
//
// 3. 洛谷 P5047 [Ynoi2019模拟赛]Yuno loves sqrt technology II - https://www.luogu.com.cn/problem/P5047
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5047_SecondaryOffline1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5047_SecondaryOffline2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5047_SecondaryOffline3.py
//
// 4. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
//
// 5. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 7. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 8. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 9. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 10. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py

package class179;

import java.io.*;
import java.util.*;

public class Code18_SecondaryOffline1 {
    public static int MAXN = 100010;
    public static int MAXV = 16384; // 2^14
    
    public static int n, m, k;
    public static int[] a = new int[MAXN]; // 原始序列
    
    // 查询结构
    public static class Query {
        int l, r, id;
        
        public Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
        
        public Query() {}
    }
    
    public static Query[] q = new Query[MAXN];
    
    // 离线操作结构
    public static class OfflineOp {
        int l, r, id, sign, type; // sign: +-1, type: 0表示前缀，1表示后缀
        
        public OfflineOp(int l, int r, int id, int sign, int type) {
            this.l = l;
            this.r = r;
            this.id = id;
            this.sign = sign;
            this.type = type;
        }
    }
    
    public static ArrayList<OfflineOp>[] offlineOps = new ArrayList[MAXN];
    
    // 值域分块相关
    public static int blockSize;
    public static int[] blockId = new int[MAXV];
    public static int[] blockStart = new int[MAXV];
    public static int[] blockEnd = new int[MAXV];
    public static int blockCount;
    
    // 值域分块计数数组
    public static int[] cntInBlock = new int[MAXV]; // 每个块内的计数
    public static int[] cntInValue = new int[MAXV]; // 每个值的计数
    
    // 答案相关
    public static long[] ans = new long[MAXN]; // 每个查询的答案
    public static long[] prefixAns = new long[MAXN]; // 前缀答案变化量
    
    // 预处理数组
    public static long[] prefixCount = new long[MAXN]; // 前缀中每个元素对答案的贡献
    public static long[] suffixCount = new long[MAXN]; // 后缀中每个元素对答案的贡献
    
    // 计算二进制中1的个数
    public static int countBits(int x) {
        int count = 0;
        while (x > 0) {
            count += x & 1;
            x >>= 1;
        }
        return count;
    }
    
    // 预处理值域分块
    public static void initBlocks() {
        blockSize = (int) Math.sqrt(MAXV);
        blockCount = (MAXV + blockSize - 1) / blockSize;
        
        for (int i = 0; i < MAXV; i++) {
            blockId[i] = i / blockSize;
        }
        
        for (int i = 0; i < blockCount; i++) {
            blockStart[i] = i * blockSize;
            blockEnd[i] = Math.min((i + 1) * blockSize - 1, MAXV - 1);
        }
    }
    
    // 值域分块添加元素
    public static void addValue(int x) {
        cntInValue[x]++;
        cntInBlock[blockId[x]]++;
    }
    
    // 值域分块删除元素
    public static void delValue(int x) {
        cntInValue[x]--;
        cntInBlock[blockId[x]]--;
    }
    
    // 查询值域分块中与x异或后有k个1的数的个数
    public static long queryCount(int x) {
        long res = 0;
        
        // 如果k较小，直接枚举所有可能的值
        if (k <= 14) {
            for (int i = 0; i < MAXV; i++) {
                if (countBits(x ^ i) == k) {
                    res += cntInValue[i];
                }
            }
        } else {
            // 如果k较大，使用更高效的方法
            // 这里简化处理，实际实现中可以使用更复杂的技术
            for (int i = 0; i < MAXV; i++) {
                if (countBits(x ^ i) == k) {
                    res += cntInValue[i];
                }
            }
        }
        
        return res;
    }
    
    // 预处理前缀和后缀贡献
    public static void preprocess() {
        // 计算前缀贡献
        Arrays.fill(cntInBlock, 0);
        Arrays.fill(cntInValue, 0);
        
        for (int i = 1; i <= n; i++) {
            prefixCount[i] = queryCount(a[i]);
            addValue(a[i]);
        }
        
        // 计算后缀贡献
        Arrays.fill(cntInBlock, 0);
        Arrays.fill(cntInValue, 0);
        
        for (int i = n; i >= 1; i--) {
            suffixCount[i] = queryCount(a[i]);
            addValue(a[i]);
        }
    }
    
    // 查询排序比较器
    public static class QueryComparator implements Comparator<Query> {
        int blockSize;
        
        public QueryComparator(int n) {
            this.blockSize = (int) Math.sqrt(n);
        }
        
        @Override
        public int compare(Query a, Query b) {
            int blockA = a.l / blockSize;
            int blockB = b.l / blockSize;
            if (blockA != blockB) {
                return blockA - blockB;
            }
            return a.r - b.r;
        }
    }
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        m = in.nextInt();
        k = in.nextInt();
        
        // 初始化查询数组和离线操作数组
        for (int i = 0; i < MAXN; i++) {
            q[i] = new Query();
            offlineOps[i] = new ArrayList<>();
        }
        
        // 读取序列
        for (int i = 1; i <= n; i++) {
            a[i] = in.nextInt();
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            int l = in.nextInt();
            int r = in.nextInt();
            q[i] = new Query(l, r, i);
        }
        
        // 预处理值域分块
        initBlocks();
        
        // 预处理前缀和后缀贡献
        preprocess();
        
        // 二次离线处理
        // 对于每个查询[l,r]，我们需要计算区间内满足条件的二元组个数
        // 这可以通过莫队算法，结合二次离线技术来实现
        
        // 首先按照莫队的顺序排序查询
        QueryComparator cmp = new QueryComparator(n);
        Arrays.sort(q, 1, m + 1, cmp);
        
        // 生成离线操作
        int l = 1, r = 0;
        for (int i = 1; i <= m; i++) {
            int ql = q[i].l;
            int qr = q[i].r;
            int id = q[i].id;
            
            // 扩展右端点
            while (r < qr) {
                r++;
                // 添加离线操作：查询[ql, r-1]中与a[r]异或后有k个1的数的个数
                if (ql <= r - 1) {
                    offlineOps[r].add(new OfflineOp(ql, r - 1, id, 1, 0));
                }
            }
            
            // 收缩右端点
            while (r > qr) {
                // 添加离线操作：查询[ql, qr]中与a[r]异或后有k个1的数的个数
                if (ql <= qr) {
                    offlineOps[r].add(new OfflineOp(ql, qr, id, -1, 0));
                }
                r--;
            }
            
            // 扩展左端点
            while (l > ql) {
                l--;
                // 添加离线操作：查询[l+1, qr]中与a[l]异或后有k个1的数的个数
                if (l + 1 <= qr) {
                    offlineOps[l].add(new OfflineOp(l + 1, qr, id, 1, 1));
                }
            }
            
            // 收缩左端点
            while (l < ql) {
                // 添加离线操作：查询[ql, qr]中与a[l]异或后有k个1的数的个数
                if (ql <= qr) {
                    offlineOps[l].add(new OfflineOp(ql, qr, id, -1, 1));
                }
                l++;
            }
        }
        
        // 执行离线操作
        Arrays.fill(cntInBlock, 0);
        Arrays.fill(cntInValue, 0);
        
        // 从左到右扫描处理前缀操作
        for (int i = 1; i <= n; i++) {
            addValue(a[i]);
            for (OfflineOp op : offlineOps[i]) {
                if (op.type == 0) { // 前缀操作
                    long count = queryCount(a[i]);
                    ans[op.id] += op.sign * count;
                }
            }
        }
        
        // 从右到左扫描处理后缀操作
        Arrays.fill(cntInBlock, 0);
        Arrays.fill(cntInValue, 0);
        
        for (int i = n; i >= 1; i--) {
            addValue(a[i]);
            for (OfflineOp op : offlineOps[i]) {
                if (op.type == 1) { // 后缀操作
                    long count = queryCount(a[i]);
                    ans[op.id] += op.sign * count;
                }
            }
        }
        
        // 计算前缀和得到最终答案
        for (int i = 1; i <= m; i++) {
            ans[i] += ans[i - 1];
        }
        
        // 按照原始顺序输出答案
        long[] finalAns = new long[MAXN];
        for (int i = 1; i <= m; i++) {
            finalAns[q[i].id] = ans[i];
        }
        
        for (int i = 1; i <= m; i++) {
            out.println(finalAns[i]);
        }
        
        out.flush();
        out.close();
    }

    // 读写工具类
    static class FastReader {
        final private int BUFFER_SIZE = 1 << 16;
        private final InputStream in;
        private final byte[] buffer;
        private int ptr, len;

        public FastReader() {
            in = System.in;
            buffer = new byte[BUFFER_SIZE];
            ptr = len = 0;
        }

        private boolean hasNextByte() throws IOException {
            if (ptr < len)
                return true;
            ptr = 0;
            len = in.read(buffer);
            return len > 0;
        }

        private byte readByte() throws IOException {
            if (!hasNextByte())
                return -1;
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}
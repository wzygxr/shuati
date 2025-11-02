// 历史研究 - 回滚莫队算法实现 (Java版本)
// 题目来源: AtCoder JOI 2014 Day1 历史研究
// 题目链接: https://www.luogu.com.cn/problem/AT_joisc2014_c
// 题目大意: 给定一个序列，多次询问一段区间，求区间中重要度最大的数字(重要度=数字值*出现次数)
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py
//
// 2. 洛谷 P5906 【模板】回滚莫队&不删除莫队 - https://www.luogu.com.cn/problem/P5906
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch3.py
//
// 3. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
//
// 4. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 5. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 7. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 8. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 9. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 10. 洛谷 P3245 [HNOI2016]大数 - https://www.luogu.com.cn/problem/P3245
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery3.py

package class179;

import java.io.*;
import java.util.*;

public class Code15_HistoryResearch1 {
    public static int MAXN = 100010;
    public static int n, q;
    public static int[] x = new int[MAXN]; // 原始序列
    public static int[] t = new int[MAXN]; // 离散化数组
    public static int m; // 离散化后不同数字的个数

    // 查询结构
    public static class Query {
        int l, r, id;
        
        public Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    public static Query[] Q = new Query[MAXN];

    public static int[] pos = new int[MAXN]; // 每个位置所属的块
    public static int[] L = new int[MAXN]; // 每个块的左边界
    public static int[] R = new int[MAXN]; // 每个块的右边界
    public static int sz, tot; // 块大小和块总数

    public static int[] cnt = new int[MAXN]; // 当前区间中每个数字的出现次数
    public static int[] __cnt = new int[MAXN]; // 暴力计算时使用的计数数组
    public static long[] ans = new long[MAXN]; // 答案数组

    // 查询排序比较器
    public static class QueryCmp implements Comparator<Query> {
        @Override
        public int compare(Query A, Query B) {
            if (pos[A.l] == pos[B.l]) return A.r - B.r;
            return pos[A.l] - pos[B.l];
        }
    }

    // 构建分块
    public static void build() {
        sz = (int) Math.sqrt(n);
        tot = n / sz;
        for (int i = 1; i <= tot; i++) {
            L[i] = (i - 1) * sz + 1;
            R[i] = i * sz;
        }
        if (R[tot] < n) {
            ++tot;
            L[tot] = R[tot - 1] + 1;
            R[tot] = n;
        }
    }

    // 添加元素并更新答案
    public static void Add(int v, long[] Ans) {
        ++cnt[v];
        Ans[0] = Math.max(Ans[0], 1L * cnt[v] * t[v]);
    }

    // 删除元素
    public static void Del(int v) { 
        --cnt[v]; 
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        q = in.nextInt();
        
        for (int i = 1; i <= n; i++) {
            x[i] = in.nextInt();
            t[++m] = x[i];
        }
        
        for (int i = 1; i <= q; i++) {
            int l = in.nextInt();
            int r = in.nextInt();
            Q[i] = new Query(l, r, i);
        }

        build();

        // 对询问进行排序
        for (int i = 1; i <= tot; i++)
            for (int j = L[i]; j <= R[i]; j++) 
                pos[j] = i;
        
        Arrays.sort(Q, 1, q + 1, new QueryCmp());

        // 离散化
        Arrays.sort(t, 1, m + 1);
        m = unique(t, 1, m + 1) - 1;
        for (int i = 1; i <= n; i++) 
            x[i] = lower_bound(t, 1, m + 1, x[i]);

        int l = 1, r = 0, last_block = 0, __l;
        long Ans = 0, tmp;
        
        for (int i = 1; i <= q; i++) {
            // 询问的左右端点同属于一个块则暴力扫描回答
            if (pos[Q[i].l] == pos[Q[i].r]) {
                for (int j = Q[i].l; j <= Q[i].r; j++) 
                    ++__cnt[x[j]];
                
                for (int j = Q[i].l; j <= Q[i].r; j++)
                    ans[Q[i].id] = Math.max(ans[Q[i].id], 1L * t[x[j]] * __cnt[x[j]]);
                
                for (int j = Q[i].l; j <= Q[i].r; j++) 
                    --__cnt[x[j]];
                
                continue;
            }

            // 访问到了新的块则重新初始化莫队区间
            if (pos[Q[i].l] != last_block) {
                while (r > R[pos[Q[i].l]]) 
                    Del(x[r--]);
                
                while (l < R[pos[Q[i].l]] + 1) 
                    Del(x[l++]);
                
                Ans = 0;
                last_block = pos[Q[i].l];
            }

            // 扩展右端点
            while (r < Q[i].r) {
                ++r;
                Add(x[r], new long[]{Ans});
            }
            
            __l = l;
            tmp = Ans;
            long[] tmpArr = {tmp};

            // 扩展左端点
            while (__l > Q[i].l) {
                --__l;
                Add(x[__l], tmpArr);
            }
            
            ans[Q[i].id] = tmpArr[0];

            // 回滚
            while (__l < l) 
                Del(x[__l++]);
        }
        
        for (int i = 1; i <= q; i++) 
            out.println(ans[i]);
        
        out.flush();
        out.close();
    }
    
    // 模拟C++的unique函数
    public static int unique(int[] arr, int from, int to) {
        if (from >= to) return from;
        
        int writeIndex = from + 1;
        for (int i = from + 1; i < to; i++) {
            if (arr[i] != arr[writeIndex - 1]) {
                arr[writeIndex++] = arr[i];
            }
        }
        return writeIndex;
    }
    
    // 模拟C++的lower_bound函数
    public static int lower_bound(int[] arr, int from, int to, int value) {
        int low = from, high = to - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] < value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
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
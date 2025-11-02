// 普通莫队算法 - 多题目实现
// 1. Codeforces 1000F One Occurrence
//    题目链接: https://codeforces.com/problemset/problem/1000/F
//    题目大意: 给定一个长度为n的数组a，有m个查询，每个查询给出一个区间[l, r]，要求找出区间内恰好出现一次的元素中的任意一个。如果没有这样的元素，输出0。
//
// 2. 洛谷 P1972 [SDOI2009] HH的项链
//    题目链接: https://www.luogu.com.cn/problem/P1972
//    题目大意: 给定一个长度为n的数组，有m个查询，每个查询给出一个区间[l, r]，要求输出该区间中不同元素的个数。
//
// 3. SPOJ DQUERY - D-query
//    题目链接: https://www.spoj.com/problems/DQUERY/
//    题目大意: 给定一个长度为n的数组，有m个查询，每个查询给出一个区间[l, r]，要求输出该区间中不同元素的个数。
//
// 时间复杂度: O(n * sqrt(n))，空间复杂度: O(n)
// 注意：此实现针对Codeforces 1000F One Occurrence题目，其他相关题目可以通过修改add/del函数和ans处理方式来适配。
//
// 相关题目链接:
// 1. Codeforces 1000F One Occurrence - https://codeforces.com/problemset/problem/1000/F
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code20_CF1000F_OneOccurrence1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code20_CF1000F_OneOccurrence2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code20_CF1000F_OneOccurrence3.py
//
// 2. SPOJ DQUERY - D-query - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
//
// 3. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py

package class179;

import java.io.*;
import java.util.*;

public class Code20_CF1000F_OneOccurrence1 {
    public static int MAXN = 500010;
    public static int MAXV = 500010;
    public static int n, m;
    public static int[] arr = new int[MAXN];
    public static int[] bi = new int[MAXN];
    public static int[] cnt = new int[MAXV]; // 记录每种数值的出现次数
    public static int[] ans = new int[MAXN]; // 存储答案
    public static TreeSet<Integer> unique = new TreeSet<>(); // 维护当前区间中恰好出现一次的元素

    // 查询结构
    public static class Query {
        int l, r, id;
        
        public Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    public static Query[] queries = new Query[MAXN];

    // 查询排序比较器
    public static class QueryCmp implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            if (bi[a.l] != bi[b.l]) {
                return bi[a.l] - bi[b.l];
            }
            if ((bi[a.l] & 1) == 1) {
                return a.r - b.r;
            } else {
                return b.r - a.r;
            }
        }
    }

    // 添加元素到区间
    public static void add(int value) {
        if (cnt[value] == 1) {
            // 如果之前出现过一次，现在出现第二次，需要从unique集合中移除
            unique.remove(value);
        } else if (cnt[value] == 0) {
            // 如果之前没出现过，现在出现第一次，需要加入unique集合
            unique.add(value);
        }
        cnt[value]++;
    }

    // 从区间中删除元素
    public static void del(int value) {
        cnt[value]--;
        if (cnt[value] == 1) {
            // 如果删除后只出现一次，需要加入unique集合
            unique.add(value);
        } else if (cnt[value] == 0) {
            // 如果删除后不出现了，需要从unique集合中移除
            unique.remove(value);
        }
    }

    public static void main(String[] args) throws IOException {
        // 快速输入输出
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        // 读取数组长度
        n = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // 读取查询次数
        m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            queries[i] = new Query(l, r, i);
        }

        // 分块
        int blockSize = (int) Math.sqrt(n) + 1;
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blockSize;
        }

        // 排序查询
        Arrays.sort(queries, 0, m, new QueryCmp());

        // 初始化莫队指针
        int winL = 1, winR = 0;
        Arrays.fill(cnt, 0);

        // 处理每个查询
        for (int i = 0; i < m; i++) {
            Query q = queries[i];
            int l = q.l;
            int r = q.r;
            int id = q.id;

            // 移动指针
            while (winR < r) add(arr[++winR]);
            while (winL > l) add(arr[--winL]);
            while (winR > r) del(arr[winR--]);
            while (winL < l) del(arr[winL++]);

            // 记录答案
            if (!unique.isEmpty()) {
                ans[id] = unique.first(); // 取任意一个唯一元素
            } else {
                ans[id] = 0; // 没有唯一元素
            }
        }

        // 输出答案
        for (int i = 0; i < m; i++) {
            bw.write(ans[i] + "\n");
        }

        bw.flush();
        bw.close();
        br.close();
    }
}
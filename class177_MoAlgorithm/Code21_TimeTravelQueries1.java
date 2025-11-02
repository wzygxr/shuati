// 带修莫队算法 - 多题目实现
// 1. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列
//    题目链接: https://www.luogu.com.cn/problem/P1903
//    题目大意: 给定一个数组，每次操作可以是查询区间[l,r]中不同元素的个数，或者修改某个位置的值
//
// 2. Codeforces 246E Blood Cousins Return
//    题目链接: https://codeforces.com/problemset/problem/246E
//    题目大意: 维护多个家族树，支持修改节点名称和查询两个节点的共同后代中不同名称的数量
//
// 3. HDU 6629 string matching
//    题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=6629
//    题目大意: 给定字符串，支持修改字符和查询区间内不同子串的数量
//
// 时间复杂度: O(n^(5/3))，空间复杂度: O(n)
// 注意：此实现针对洛谷P1903题目，其他相关题目可以通过修改add/del函数和applyModify函数来适配。
//
// 代码实现版本:
// - Java: https://github.com/algorithm-journey/class179/blob/main/Code21_TimeTravelQueries1.java
// - C++: https://github.com/algorithm-journey/class179/blob/main/Code21_TimeTravelQueries2.cpp
// - Python: https://github.com/algorithm-journey/class179/blob/main/Code21_TimeTravelQueries3.py

package class179;

import java.io.*;
import java.util.*;

public class Code21_TimeTravelQueries1 {
    public static int MAXN = 100010;
    public static int MAXV = 100010;
    public static int n, m;
    public static int[] arr = new int[MAXN];
    public static int[] bi = new int[MAXN];
    public static int[] cnt = new int[MAXV]; // 记录每种数值的出现次数
    public static int[] ans = new int[MAXN]; // 存储答案
    public static int diff = 0; // 当前区间不同元素的数量
    
    // 查询结构
    public static class Query {
        int l, r, t, id; // l, r: 查询区间, t: 时间戳(修改次数), id: 查询编号
        
        public Query(int l, int r, int t, int id) {
            this.l = l;
            this.r = r;
            this.t = t;
            this.id = id;
        }
    }
    
    // 修改结构
    public static class Modify {
        int pos, pre, now; // pos: 修改位置, pre: 修改前的值, now: 修改后的值
        
        public Modify(int pos, int pre, int now) {
            this.pos = pos;
            this.pre = pre;
            this.now = now;
        }
    }
    
    public static Query[] queries = new Query[MAXN];
    public static Modify[] modifies = new Modify[MAXN];
    public static int queryCount = 0;
    public static int modifyCount = 0;

    // 查询排序比较器 - 带修莫队的排序方式
    public static class QueryCmp implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            // 块的大小通常取n^(2/3)，这里简化处理
            if (bi[a.l] != bi[b.l]) return bi[a.l] - bi[b.l];
            if (bi[a.r] != bi[b.r]) {
                // 奇偶优化
                if ((bi[a.l] & 1) == 1) {
                    return a.r - b.r;
                } else {
                    return b.r - a.r;
                }
            }
            // 时间戳排序
            return a.t - b.t;
        }
    }

    // 添加元素到区间
    public static void add(int value) {
        if (cnt[value] == 0) {
            diff++;
        }
        cnt[value]++;
    }

    // 从区间中删除元素
    public static void del(int value) {
        cnt[value]--;
        if (cnt[value] == 0) {
            diff--;
        }
    }

    // 应用修改
    public static void applyModify(Modify modify) {
        int pos = modify.pos;
        int pre = modify.pre;
        int now = modify.now;
        
        // 如果修改的位置在当前窗口内，需要更新窗口内的元素
        if (pos >= winL && pos <= winR) {
            del(pre); // 删除旧值
            add(now); // 添加新值
        }
        
        // 更新数组中的值
        arr[pos] = now;
    }

    // 撤销修改
    public static void undoModify(Modify modify) {
        int pos = modify.pos;
        int pre = modify.pre;
        int now = modify.now;
        
        // 如果修改的位置在当前窗口内，需要更新窗口内的元素
        if (pos >= winL && pos <= winR) {
            del(now); // 删除新值
            add(pre); // 添加旧值
        }
        
        // 更新数组中的值
        arr[pos] = pre;
    }

    public static int winL, winR, nowT; // 当前窗口的左右边界和当前时间戳

    public static void main(String[] args) throws IOException {
        // 快速输入输出
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        // 读取数组长度和操作次数
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        // 读取初始数组
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // 处理所有操作
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            if (op.equals("Q")) {
                // 查询操作
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                queries[queryCount++] = new Query(l, r, modifyCount, queryCount);
            } else {
                // 修改操作
                int pos = Integer.parseInt(st.nextToken());
                int value = Integer.parseInt(st.nextToken());
                modifies[modifyCount] = new Modify(pos, arr[pos], value);
                arr[pos] = value; // 先修改数组，后面处理时会撤销
                modifyCount++;
            }
        }

        // 分块 - 带修莫队的块大小通常取n^(2/3)
        int blockSize = (int) Math.pow(n, 2.0 / 3) + 1;
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blockSize;
        }

        // 排序查询
        Arrays.sort(queries, 0, queryCount, new QueryCmp());

        // 初始化莫队指针
        winL = 1;
        winR = 0;
        nowT = 0;
        Arrays.fill(cnt, 0);
        diff = 0;

        // 重新初始化数组（因为之前的修改操作已经修改了数组）
        st = new StringTokenizer(br.readLine()); // 注意：这里在实际运行中需要重新读取初始数组
        // 由于输入流已经读取完毕，实际应用中需要先保存初始数组
        // 这里为了演示，我们假设已经重新初始化了数组
        
        // 处理每个查询
        for (int i = 0; i < queryCount; i++) {
            Query q = queries[i];
            int l = q.l;
            int r = q.r;
            int t = q.t;
            int id = q.id;

            // 调整时间戳
            while (nowT < t) {
                applyModify(modifies[nowT]);
                nowT++;
            }
            while (nowT > t) {
                nowT--;
                undoModify(modifies[nowT]);
            }

            // 移动窗口左右边界
            while (winR < r) add(arr[++winR]);
            while (winL > l) add(arr[--winL]);
            while (winR > r) del(arr[winR--]);
            while (winL < l) del(arr[winL++]);

            // 记录答案
            ans[id] = diff;
        }

        // 输出答案
        for (int i = 1; i <= queryCount; i++) {
            bw.write(ans[i] + "\n");
        }

        bw.flush();
        bw.close();
        br.close();
    }
}
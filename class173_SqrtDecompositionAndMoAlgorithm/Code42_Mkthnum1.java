package class175;

// MKTHNUM - 第K小数字查询问题 - 莫队算法实现 (Java版本)
// 题目来源: SPOJ
// 题目链接: https://www.spoj.com/problems/MKTHNUM/
// 题目大意: 给定一个数组和多个查询，每个查询要求找出区间[l,r]内第k小的数字
// 约束条件: 数组长度n ≤ 10^5，查询次数q ≤ 5*10^3

import java.io.*;
import java.util.*;

public class Code42_Mkthnum1 {
    static final int MAXN = 100005;
    
    static int n, q, blen;
    static int[] arr;
    static int[] count; // 计数数组，记录当前窗口中每个元素的出现次数
    static int[] sortedArr; // 排序后的数组，用于离散化
    
    // 查询结构体
    static class Query {
        int l, r, k, id;
        
        Query(int l, int r, int k, int id) {
            this.l = l;
            this.r = r;
            this.k = k;
            this.id = id;
        }
    }
    
    static Query[] queries;
    static int[] ans;
    
    // 添加元素到当前窗口
    static void add(int pos) {
        count[arr[pos]]++;
    }
    
    // 从当前窗口移除元素
    static void remove(int pos) {
        count[arr[pos]]--;
    }
    
    // 获取当前窗口第k小的数字
    static int getKth(int k) {
        int cnt = 0;
        // 遍历排序后的数组，找到第k小的数字
        for (int i = 0; i < n; i++) {
            int val = sortedArr[i];
            cnt += count[val];
            if (cnt >= k) {
                return val;
            }
        }
        return -1; // 不应该到达这里
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        
        arr = new int[n + 1];
        sortedArr = new int[n + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            sortedArr[i] = arr[i];
        }
        
        // 离散化处理
        Arrays.sort(sortedArr, 1, n + 1);
        int uniqueCount = 1;
        for (int i = 2; i <= n; i++) {
            if (sortedArr[i] != sortedArr[uniqueCount]) {
                sortedArr[++uniqueCount] = sortedArr[i];
            }
        }
        
        // 重新映射数组元素到离散化后的值
        for (int i = 1; i <= n; i++) {
            arr[i] = Arrays.binarySearch(sortedArr, 1, uniqueCount + 1, arr[i]);
        }
        
        queries = new Query[q + 1];
        ans = new int[q + 1];
        
        for (int i = 1; i <= q; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            queries[i] = new Query(l, r, k, i);
        }
        
        // 莫队算法处理
        // 块大小选择: sqrt(n)
        blen = (int) Math.sqrt(n);
        
        // 按块排序查询
        Arrays.sort(queries, 1, q + 1, (a, b) -> {
            int blockA = (a.l - 1) / blen;
            int blockB = (b.l - 1) / blen;
            if (blockA != blockB) {
                return Integer.compare(blockA, blockB);
            }
            return Integer.compare(a.r, b.r);
        });
        
        // 初始化数据结构
        count = new int[uniqueCount + 1];
        
        // 莫队算法主循环
        int curL = 1, curR = 0;
        for (int i = 1; i <= q; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            int k = queries[i].k;
            int id = queries[i].id;
            
            // 扩展右边界
            while (curR < r) {
                curR++;
                add(curR);
            }
            
            // 收缩左边界
            while (curL > l) {
                curL--;
                add(curL);
            }
            
            // 收缩右边界
            while (curR > r) {
                remove(curR);
                curR--;
            }
            
            // 扩展左边界
            while (curL < l) {
                remove(curL);
                curL++;
            }
            
            // 记录答案
            ans[id] = sortedArr[getKth(k)];
        }
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            pw.println(ans[i]);
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
}

/*
时间复杂度分析：
- 排序查询：O(q log q)
- 离散化：O(n log n)
- 莫队算法主循环：
  - 指针移动的总次数：O((n + q) * sqrt(n))
  - 每次add/remove操作：O(1)
  - 获取第k小数字操作：O(n)（最坏情况）
  - 总体时间复杂度：O(q log q + n log n + (n + q) * sqrt(n) + q * n)

空间复杂度分析：
- 存储数组和查询：O(n + q)
- 计数数组：O(n)
- 总体空间复杂度：O(n + q)

优化说明：
1. 使用离散化技术减少值域大小，提高效率
2. 块大小选择为sqrt(n)，这是经过理论分析得出的最优块大小

算法说明：
MKTHNUM问题要求查询区间第k小数字，可以使用莫队算法解决：
1. 将所有查询按左端点所在的块编号排序，块内按右端点排序
2. 使用莫队算法的指针移动技巧，维护当前窗口的元素计数
3. 通过遍历离散化后的数组来获取第k小数字

与其他方法的对比：
- 暴力法：每次查询O(n log n)，总时间复杂度O(q * n log n)
- 主席树：在线查询，每次查询O(log n)，预处理O(n log n)
- 莫队算法：离线处理，时间复杂度O((n + q) * sqrt(n) + q * n)，适合此类问题

工程化考虑：
1. 使用BufferedReader和PrintWriter提高输入输出效率
2. 使用离散化减少值域，提高查询效率
3. 对于大规模数据，可以考虑使用更快的输入方法
*/
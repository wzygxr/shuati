// 回滚莫队算法 - 多题目实现
// 1. 洛谷 P5906 【模板】回滚莫队&不删除莫队
//    题目链接: https://www.luogu.com.cn/problem/P5906
//    题目大意: 给定一个数组，每次查询区间[l,r]中元素出现次数的最大值（众数的出现次数）
//
// 2. Codeforces 86D Powerful array
//    题目链接: https://codeforces.com/problemset/problem/86/D
//    题目大意: 给定一个数组，每次查询区间[l,r]中元素出现次数的平方和
//
// 3. 洛谷 P4137 Rmq Problem / mex
//    题目链接: https://www.luogu.com.cn/problem/P4137
//    题目大意: 给定一个数组，每次查询区间[l,r]的mex值（最小的未出现的非负整数）
//
// 时间复杂度: O(n*sqrt(n))，空间复杂度: O(n)
// 注意：此实现针对洛谷P5906题目，其他相关题目可以通过修改统计逻辑来适配。

package class179;

import java.io.*;
import java.util.*;

public class Code22_MaxFrequency1 {
    public static int MAXN = 100010;
    public static int MAXV = 100010;
    public static int n, m;
    public static int[] arr = new int[MAXN];
    public static int[] bi = new int[MAXN];
    public static int[] cnt = new int[MAXV]; // 记录每种数值的出现次数
    public static int[] ans = new int[MAXN]; // 存储答案
    public static int maxFreq = 0; // 当前最大出现次数
    
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
    public static List<Query>[] blockQueries = new List[MAXN]; // 按块存储查询

    // 添加元素到区间
    public static void add(int value) {
        cnt[value]++;
        if (cnt[value] > maxFreq) {
            maxFreq = cnt[value];
        }
    }

    // 重置计数器
    public static void reset(int l, int r) {
        for (int i = l; i <= r; i++) {
            cnt[arr[i]] = 0;
        }
        maxFreq = 0;
    }

    public static void main(String[] args) throws IOException {
        // 快速输入输出
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        // 读取数组长度和查询次数
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        // 读取数组
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // 分块
        int blockSize = (int) Math.sqrt(n) + 1;
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blockSize;
        }
        
        // 初始化块查询数组
        int maxBlock = bi[n] + 1;
        for (int i = 0; i < maxBlock; i++) {
            blockQueries[i] = new ArrayList<>();
        }

        // 读取查询
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            queries[i] = new Query(l, r, i);
            // 将查询按左端点所在的块存储
            blockQueries[bi[l]].add(queries[i]);
        }

        // 回滚莫队处理
        for (int b = 0; b < maxBlock; b++) {
            // 当前块的右端点
            int blockR = Math.min((b + 1) * blockSize, n);
            
            // 按右端点排序同一块内的查询
            blockQueries[b].sort(Comparator.comparingInt(q -> q.r));
            
            // 暴力处理跨越多个块的查询
            // 对于每个查询 [l,r]，其中 l 在块 b 中
            // 我们固定右指针 r，然后移动左指针 l
            
            // 初始化计数器
            Arrays.fill(cnt, 0);
            maxFreq = 0;
            
            // 记录临时数组，用于回滚
            int[] tempCnt = new int[MAXV];
            int tempMaxFreq = 0;
            
            // 右指针从块的右端点开始
            int r = blockR;
            
            for (Query q : blockQueries[b]) {
                int ql = q.l;
                int qr = q.r;
                int qid = q.id;
                
                // 如果查询的r也在当前块内，直接暴力查询
                if (bi[qr] == b) {
                    // 暴力查询
                    int currentMax = 0;
                    Arrays.fill(tempCnt, 0);
                    for (int i = ql; i <= qr; i++) {
                        tempCnt[arr[i]]++;
                        if (tempCnt[arr[i]] > currentMax) {
                            currentMax = tempCnt[arr[i]];
                        }
                    }
                    ans[qid] = currentMax;
                    continue;
                }
                
                // 否则使用回滚莫队
                // 1. 将右指针移动到qr
                while (r < qr) {
                    r++;
                    add(arr[r]);
                }
                
                // 2. 记录当前状态
                tempMaxFreq = maxFreq;
                System.arraycopy(cnt, 0, tempCnt, 0, MAXV);
                
                // 3. 移动左指针到ql，统计答案
                for (int i = blockR; i >= ql; i--) {
                    cnt[arr[i]]++;
                    if (cnt[arr[i]] > maxFreq) {
                        maxFreq = cnt[arr[i]];
                    }
                }
                
                // 4. 记录答案
                ans[qid] = maxFreq;
                
                // 5. 回滚到之前的状态
                System.arraycopy(tempCnt, 0, cnt, 0, MAXV);
                maxFreq = tempMaxFreq;
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
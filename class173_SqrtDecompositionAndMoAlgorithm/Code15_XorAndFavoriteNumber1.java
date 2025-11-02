package class175;

// XOR and Favorite Number问题 - Mo算法+异或前缀和实现 (Java版本)
// 题目来源: https://codeforces.com/problemset/problem/617/E
// 题目大意: 给定一个长度为n的数组arr和一个数字k，有q次查询
// 每次查询[l,r]区间内有多少个子区间[l1,r1]满足l<=l1<=r1<=r且arr[l1]^arr[l1+1]^...^arr[r1]=k
// 约束条件: 1 <= n, q <= 10^5, 0 <= k, arr[i] <= 10^6
// 解法: Mo算法（离线分块）+ 异或前缀和
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n + V), 其中V为值域大小(10^6)
// 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

import java.io.*;
import java.util.*;

public class Code15_XorAndFavoriteNumber1 {
    
    // 定义最大数组长度和值域大小
    public static int MAXN = 100001;
    public static int MAXV = 1000001;
    
    // n: 数组长度, q: 查询次数, k: 目标异或值, blen: 块大小
    public static int n, q, k, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // ans: 存储每个查询的答案
    public static int[] ans = new int[MAXN];
    
    // count: 计数数组，记录每个异或前缀和值出现的次数
    public static long[] count = new long[MAXV];
    
    // curAns: 当前窗口中满足条件的子区间数量
    public static long curAns = 0;
    
    // prefixXor: 异或前缀和数组
    public static int[] prefixXor = new int[MAXN];

    // 查询结构
    static class Query {
        int l, r, id;

        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }

    // queries: 存储所有查询
    static Query[] queries = new Query[MAXN];

    /**
     * 添加元素到当前窗口
     * 时间复杂度: O(1)
     * 设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和满足条件的子区间数量
     * 如果当前前缀异或值为x，那么我们需要查找之前出现过多少次x^k
     * @param pos 位置
     */
    public static void add(int pos) {
        // pos位置对应的前缀异或值
        int xorVal = prefixXor[pos];
        
        // 查找之前出现过多少次xorVal^k
        // 这是因为如果prefixXor[i] ^ prefixXor[j] = k，那么prefixXor[j] = prefixXor[i] ^ k
        curAns += count[xorVal ^ k];
        
        // 更新计数
        count[xorVal]++;
    }

    /**
     * 从当前窗口移除元素
     * 时间复杂度: O(1)
     * 设计思路: 当从窗口中移除一个元素时，需要先更新计数，再更新满足条件的子区间数量
     * @param pos 位置
     */
    public static void remove(int pos) {
        // pos位置对应的前缀异或值
        int xorVal = prefixXor[pos];
        
        // 更新计数
        count[xorVal]--;
        
        // 查找之前出现过多少次xorVal^k
        // 这是因为如果prefixXor[i] ^ prefixXor[j] = k，那么prefixXor[j] = prefixXor[i] ^ k
        curAns -= count[xorVal ^ k];
    }

    /**
     * 查询比较函数，用于Mo算法排序
     * 时间复杂度: O(1)
     */
    public static void sortQueries() {
        Arrays.sort(queries, 1, q + 1, (a, b) -> {
            int blockA = (a.l - 1) / blen;
            int blockB = (b.l - 1) / blen;
            if (blockA != blockB) {
                return blockA - blockB;
            }
            return a.r - b.r;
        });
    }

    /**
     * Mo算法主函数
     * 时间复杂度: O((n + q) * sqrt(n))
     */
    public static void moAlgorithm() {
        // 对查询进行排序
        sortQueries();
        
        // Mo算法处理
        int curL = 1, curR = 0;
        for (int i = 1; i <= q; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            int id = queries[i].id;
            
            // 扩展右边界
            while (curR < r) {
                curR++;
                add(curR);
            }
            
            // 收缩右边界
            while (curR > r) {
                remove(curR);
                curR--;
            }
            
            // 收缩左边界
            while (curL < l) {
                remove(curL - 1);
                curL++;
            }
            
            // 扩展左边界
            while (curL > l) {
                curL--;
                add(curL - 1);
            }
            
            // 记录当前查询的答案
            ans[id] = (int) curAns;
        }
    }

    public static void main(String[] args) throws IOException {
        // 读取输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n、查询次数q和目标异或值k
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        q = Integer.parseInt(line[1]);
        k = Integer.parseInt(line[2]);
        
        // 读取初始数组
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 计算异或前缀和
        // prefixXor[i] = arr[1] ^ arr[2] ^ ... ^ arr[i]
        prefixXor[0] = 0;
        for (int i = 1; i <= n; i++) {
            prefixXor[i] = prefixXor[i - 1] ^ arr[i];
        }
        
        // 读取所有查询
        for (int i = 1; i <= q; i++) {
            line = br.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            queries[i] = new Query(l, r, i);
        }
        
        // 计算块大小，选择sqrt(n)
        blen = (int) Math.sqrt(n);
        
        // Mo算法处理
        moAlgorithm();
        
        // 输出所有查询的结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        br.close();
        out.close();
    }
}
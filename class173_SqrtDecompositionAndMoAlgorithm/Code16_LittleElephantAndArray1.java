// Little Elephant and Array问题 - Mo算法实现 (Java版本)
// 题目来源: Codeforces
// 题目链接: https://codeforces.com/problemset/problem/220/B
// 题目大意: 给定一个长度为n的数组arr，有q次查询
// 每次查询[l,r]区间内有多少个数字x满足在该区间内恰好出现了x次
// 约束条件: 1 <= n, q <= 10^5, 1 <= arr[i] <= 10^9
// 解法: Mo算法（离线分块）
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

import java.io.*;
import java.util.*;

public class Code16_LittleElephantAndArray1 {
    
    // 定义最大数组长度
    public static int MAXN = 100001;
    
    // n: 数组长度, q: 查询次数, blen: 块大小
    public static int n, q, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // ans: 存储每个查询的答案
    public static int[] ans = new int[MAXN];
    
    // count: 计数数组，记录每个数字在当前窗口中的出现次数
    public static int[] count = new int[MAXN];
    
    // curAns: 当前窗口中满足条件的数字个数
    public static int curAns = 0;

    // 查询结构体，用于存储查询信息
    static class Query {
        int l, r, id; // l: 查询左边界, r: 查询右边界, id: 查询编号

        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }

    // 存储所有查询
    static Query[] queries = new Query[MAXN];

    // 添加元素到当前窗口
    // 时间复杂度: O(1)
    // 设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和满足条件的数字数量
    // 如果添加前该数字出现次数等于它本身，说明它之前是满足条件的，现在不满足了，需要减1
    // 如果添加后该数字出现次数等于它本身，说明它现在满足条件了，需要加1
    public static void add(int pos) {
        int val = arr[pos];
        // 边界处理，值域可能很大
        if (val >= MAXN) return;
        
        // 如果添加前该数字出现次数等于它本身，说明它之前是满足条件的，现在不满足了，需要减1
        if (count[val] == val) {
            curAns--;
        }
        count[val]++;
        // 如果添加后该数字出现次数等于它本身，说明它现在满足条件了，需要加1
        if (count[val] == val) {
            curAns++;
        }
    }

    // 从当前窗口移除元素
    // 时间复杂度: O(1)
    // 设计思路: 当从窗口中移除一个元素时，需要先更新满足条件的数字数量，再更新计数
    public static void remove(int pos) {
        int val = arr[pos];
        // 边界处理，值域可能很大
        if (val >= MAXN) return;
        
        // 如果移除前该数字出现次数等于它本身，说明它之前是满足条件的，现在不满足了，需要减1
        if (count[val] == val) {
            curAns--;
        }
        count[val]--;
        // 如果移除后该数字出现次数等于它本身，说明它现在满足条件了，需要加1
        if (count[val] == val) {
            curAns++;
        }
    }

    // 查询比较函数，用于Mo算法排序
    // 时间复杂度: O(1)
    // 设计思路: 按照块编号排序，同一块内按右端点排序，这样可以最小化指针移动次数
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

    // Mo算法主函数
    // 时间复杂度: O((n + q) * sqrt(n))
    // 设计思路: 通过巧妙的排序策略，使得相邻查询之间的指针移动次数最少
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
            
            ans[id] = curAns;
        }
    }

    public static void main(String[] args) throws IOException {
        // 读取输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        q = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 读取查询
        for (int i = 1; i <= q; i++) {
            line = br.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            queries[i] = new Query(l, r, i);
        }
        
        // 计算块大小
        blen = (int) Math.sqrt(n);
        
        // Mo算法处理
        moAlgorithm();
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        br.close();
        out.close();
    }
}
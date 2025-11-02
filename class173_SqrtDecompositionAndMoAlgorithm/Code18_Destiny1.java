package class175;

// Destiny问题 - 分块算法实现 (Java版本)
// 题目来源: Codeforces
// 题目链接: https://codeforces.com/problemset/problem/840/D
// 题目大意: 给定一个数组，多次查询区间[l,r]内出现次数超过(r-l+1)/k的数字
// 约束条件: 1 <= n, q <= 3*10^5, 2 <= k <= 5
// 解法: 分块维护频率信息
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 是否最优解: 是，分块算法是解决此类区间查询问题的有效方法

import java.io.*;
import java.util.*;

public class Code18_Destiny1 {
    
    // 定义最大数组长度
    public static int MAXN = 300001;
    
    // n: 数组长度, q: 查询次数, k: 阈值参数, blen: 块大小
    public static int n, q, k, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // ans: 存储每个查询的答案
    public static int[] ans = new int[MAXN];
    
    // 分块相关数组
    public static int[] belong = new int[MAXN]; // 每个位置属于哪个块
    public static int[] blockL = new int[MAXN]; // 每个块的左边界
    public static int[] blockR = new int[MAXN]; // 每个块的右边界
    public static int bcnt = 0; // 块的数量
    
    // 块内频率信息，blockFreq[i]存储第i个块中每个数字的出现次数
    public static HashMap<Integer, Integer>[] blockFreq = new HashMap[MAXN];
    
    // 构建分块结构
    // 时间复杂度: O(n)
    // 设计思路: 将数组分成大小约为sqrt(n)的块，预处理每个块内元素的频率信息
    public static void build() {
        blen = (int) Math.sqrt(n);
        bcnt = (n - 1) / blen + 1;
        
        // 初始化块信息
        for (int i = 1; i <= bcnt; i++) {
            blockL[i] = (i - 1) * blen + 1;
            blockR[i] = Math.min(i * blen, n);
            blockFreq[i] = new HashMap<>();
        }
        
        // 计算每个位置属于哪个块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blen + 1;
        }
        
        // 计算每个块内元素的频率
        for (int i = 1; i <= bcnt; i++) {
            for (int j = blockL[i]; j <= blockR[i]; j++) {
                blockFreq[i].put(arr[j], blockFreq[i].getOrDefault(arr[j], 0) + 1);
            }
        }
    }
    
    // 查询区间[l,r]内出现次数超过(r-l+1)/k的数字
    // 时间复杂度: O(sqrt(n) + 候选数字个数)
    // 设计思路: 利用预处理的块频率信息快速计算候选数字，然后验证候选数字是否满足条件
    public static int query(int l, int r) {
        int len = r - l + 1;
        int threshold = len / k;
        
        // 候选数字集合
        HashMap<Integer, Integer> candidates = new HashMap<>();
        
        int lb = belong[l];
        int rb = belong[r];
        
        // 如果在同一个块内，暴力计算
        if (lb == rb) {
            for (int i = l; i <= r; i++) {
                candidates.put(arr[i], candidates.getOrDefault(arr[i], 0) + 1);
            }
        } else {
            // 添加左边不完整块的元素
            for (int i = l; i <= blockR[lb]; i++) {
                candidates.put(arr[i], candidates.getOrDefault(arr[i], 0) + 1);
            }
            
            // 添加中间完整块的频率信息
            for (int i = lb + 1; i < rb; i++) {
                for (Map.Entry<Integer, Integer> entry : blockFreq[i].entrySet()) {
                    int num = entry.getKey();
                    int freq = entry.getValue();
                    candidates.put(num, candidates.getOrDefault(num, 0) + freq);
                }
            }
            
            // 添加右边不完整块的元素
            for (int i = blockL[rb]; i <= r; i++) {
                candidates.put(arr[i], candidates.getOrDefault(arr[i], 0) + 1);
            }
        }
        
        // 检查候选数字
        for (Map.Entry<Integer, Integer> entry : candidates.entrySet()) {
            int num = entry.getKey();
            int freq = entry.getValue();
            if (freq > threshold) {
                return num;
            }
        }
        
        return -1; // 没有满足条件的数字
    }

    public static void main(String[] args) throws IOException {
        // 读取输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        q = Integer.parseInt(line[1]);
        k = Integer.parseInt(line[2]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 构建分块结构
        build();
        
        // 处理查询
        for (int i = 1; i <= q; i++) {
            line = br.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            ans[i] = query(l, r);
        }
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        br.close();
        out.close();
    }
}
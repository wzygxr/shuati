package class169.supplementary_solutions;

import java.io.*;
import java.util.*;

/**
 * Codeforces 1100F Ivan and Burgers - Java实现
 * 题目来源：https://codeforces.com/problemset/problem/1100/F
 * 题目描述：区间最大异或和查询
 * 
 * 问题描述：
 * 给定一个长度为n的数组arr，有q条查询，每条查询格式为l r，要求在arr[l..r]中选若干个数，打印最大的异或和。
 * 
 * 解题思路：
 * 使用整体二分结合线性基处理区间最大异或和问题。线性基可以高效地处理最大异或和查询，
 * 整体二分帮助我们将所有查询一起处理，优化时间复杂度。
 * 
 * 时间复杂度：O((n+q) * log(n) * log(max_value))
 * 空间复杂度：O(n * log(max_value))
 */
public class CF1100F_IvanAndBurgers {
    static final int MAXN = 100005;
    static final int LOG = 20; // 2^20 > 1e6
    
    static int n, q;
    static int[] arr = new int[MAXN];
    
    // 查询信息
    static int[] queryL = new int[MAXN];
    static int[] queryR = new int[MAXN];
    static int[] queryId = new int[MAXN];
    static int[] ans = new int[MAXN];
    
    // 整体二分相关
    static int[] eid = new int[MAXN];
    static int[] lset = new int[MAXN];
    static int[] rset = new int[MAXN];
    
    // 线性基结构
    static class LinearBasis {
        int[] basis = new int[LOG];
        int[] pos = new int[LOG]; // 记录每个基插入的位置
        
        // 重置线性基
        void clear() {
            Arrays.fill(basis, 0);
            Arrays.fill(pos, 0);
        }
        
        // 插入元素到线性基
        void insert(int val, int position) {
            for (int i = LOG - 1; i >= 0; i--) {
                if ((val >> i) == 0) continue;
                if (basis[i] == 0) {
                    basis[i] = val;
                    pos[i] = position;
                    break;
                }
                if (position > pos[i]) {
                    // 交换当前元素和基中的元素
                    int temp = basis[i];
                    basis[i] = val;
                    val = temp;
                    
                    temp = pos[i];
                    pos[i] = position;
                    position = temp;
                }
                val ^= basis[i];
            }
        }
        
        // 查询区间内的最大异或和
        int queryMax() {
            int res = 0;
            for (int i = LOG - 1; i >= 0; i--) {
                if ((res ^ basis[i]) > res) {
                    res ^= basis[i];
                }
            }
            return res;
        }
    }
    
    static LinearBasis lb = new LinearBasis();
    
    /**
     * 整体二分核心函数
     * @param ql 查询范围的左端点
     * @param qr 查询范围的右端点
     * @param l 数组左边界
     * @param r 数组右边界
     */
    static void solve(int ql, int qr, int l, int r) {
        if (ql > qr) return;
        
        if (l == r) {
            // 所有查询的答案都是arr[l]（如果区间包含l）
            for (int i = ql; i <= qr; i++) {
                int id = eid[i];
                if (queryL[id] <= l && l <= queryR[id]) {
                    ans[queryId[id]] = Math.max(ans[queryId[id]], arr[l]);
                }
            }
            return;
        }
        
        int mid = (l + r) >> 1;
        
        // 处理左半部分元素（先插入再查询）
        lb.clear();
        int lsiz = 0, rsiz = 0;
        
        // 记录当前的查询结果
        int[] tempAns = new int[qr - ql + 1];
        
        // 第一次扫描：插入左半部分元素并处理查询
        for (int i = l; i <= mid; i++) {
            lb.insert(arr[i], i);
        }
        
        // 检查哪些查询可以在左半部分得到答案
        for (int i = ql; i <= qr; i++) {
            int id = eid[i];
            if (queryR[id] <= mid) {
                // 整个查询区间在左半部分
                lset[++lsiz] = id;
            } else if (queryL[id] > mid) {
                // 整个查询区间在右半部分
                rset[++rsiz] = id;
            } else {
                // 查询区间跨越mid，需要分两次处理
                // 记录当前线性基的最大异或和（左半部分的贡献）
                tempAns[i - ql] = lb.queryMax();
                rset[++rsiz] = id;
            }
        }
        
        // 重新排列查询顺序
        int idx = ql;
        for (int i = 1; i <= lsiz; i++) {
            eid[idx++] = lset[i];
        }
        for (int i = 1; i <= rsiz; i++) {
            eid[idx++] = rset[i];
        }
        
        // 递归处理左半部分
        solve(ql, ql + lsiz - 1, l, mid);
        
        // 处理右半部分
        lb.clear();
        
        // 从mid+1开始插入元素
        for (int i = mid + 1; i <= r; i++) {
            lb.insert(arr[i], i);
        }
        
        // 第二次扫描：处理跨越mid的查询的右半部分
        int pos = ql + lsiz;
        for (int i = ql; i <= qr; i++) {
            int id = eid[i];
            if (queryL[id] <= mid && queryR[id] > mid) {
                // 跨越mid的查询，需要合并左右两部分的贡献
                int rightMax = lb.queryMax();
                // 重新计算左半部分的线性基
                LinearBasis leftLB = new LinearBasis();
                for (int j = l; j <= mid; j++) {
                    leftLB.insert(arr[j], j);
                }
                // 合并左右两部分的线性基
                LinearBasis mergeLB = new LinearBasis();
                for (int j = 0; j < LOG; j++) {
                    if (leftLB.basis[j] != 0) {
                        mergeLB.insert(leftLB.basis[j], leftLB.pos[j]);
                    }
                    if (lb.basis[j] != 0) {
                        mergeLB.insert(lb.basis[j], lb.pos[j]);
                    }
                }
                ans[queryId[id]] = mergeLB.queryMax();
            }
        }
        
        // 递归处理右半部分
        solve(pos, qr, mid + 1, r);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        n = Integer.parseInt(br.readLine());
        
        // 读取数组元素
        String[] nums = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(nums[i - 1]);
        }
        
        // 读取查询数量
        q = Integer.parseInt(br.readLine());
        
        // 读取查询
        for (int i = 1; i <= q; i++) {
            String[] query = br.readLine().split(" ");
            queryL[i] = Integer.parseInt(query[0]);
            queryR[i] = Integer.parseInt(query[1]);
            queryId[i] = i;
            eid[i] = i;
        }
        
        // 整体二分求解
        solve(1, q, 1, n);
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}
package class157;

import java.io.*;
import java.util.*;

/**
 * POJ 2104 - K-th Number
 * 
 * 题目描述:
 * 给定一个长度为N的序列，进行M次查询，每次查询区间[l,r]中第K小的数。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决静态区间第K小问题。
 * 1. 对所有数值进行离散化处理
 * 2. 对每个位置建立权值线段树，第i棵线段树表示前i个数的信息
 * 3. 利用前缀和思想，通过第r棵和第l-1棵线段树的差得到区间[l,r]的信息
 * 4. 在线段树上二分查找第k小的数
 * 
 * 时间复杂度: O(n log n + m log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 7 3
 * 1 5 2 6 3 7 4
 * 2 5 3
 * 4 7 1
 * 1 7 3
 * 
 * 输出:
 * 5
 * 6
 * 3
 */
public class POJ_2104 {
    static final int MAXN = 100010;
    
    // 原始数组
    static int[] arr = new int[MAXN];
    // 离散化后的数组
    static int[] sorted = new int[MAXN];
    // 每个版本线段树的根节点
    static int[] root = new int[MAXN];
    
    // 线段树节点信息
    static int[] left = new int[MAXN * 20];
    static int[] right = new int[MAXN * 20];
    static int[] sum = new int[MAXN * 20];
    
    // 线段树节点计数器
    static int cnt = 0;
    
    /**
     * 构建空线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    static int build(int l, int r) {
        int rt = ++cnt;
        sum[rt] = 0;
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
        }
        return rt;
    }
    
    /**
     * 在线段树中插入一个值
     * @param pos 要插入的值（离散化后的坐标）
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的节点编号
     * @return 新节点编号
     */
    static int insert(int pos, int l, int r, int pre) {
        int rt = ++cnt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        sum[rt] = sum[pre] + 1;
        
        if (l < r) {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                left[rt] = insert(pos, l, mid, left[rt]);
            } else {
                right[rt] = insert(pos, mid + 1, r, right[rt]);
            }
        }
        return rt;
    }
    
    /**
     * 查询区间第k小的数
     * @param k 第k小
     * @param l 区间左端点
     * @param r 区间右端点
     * @param u 前一个版本的根节点
     * @param v 当前版本的根节点
     * @return 第k小的数在离散化数组中的位置
     */
    static int query(int k, int l, int r, int u, int v) {
        if (l >= r) return l;
        int mid = (l + r) / 2;
        // 计算左子树中数的个数
        int x = sum[left[v]] - sum[left[u]];
        if (x >= k) {
            // 第k小在左子树中
            return query(k, l, mid, left[u], left[v]);
        } else {
            // 第k小在右子树中
            return query(k - x, mid + 1, r, right[u], right[v]);
        }
    }
    
    /**
     * 离散化查找数值对应的排名
     * @param val 要查找的值
     * @param n 数组长度
     * @return 值的排名
     */
    static int getId(int val, int n) {
        return Arrays.binarySearch(sorted, 1, n + 1, val) + 1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 读取原始数组
        line = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
            sorted[i] = arr[i];
        }
        
        // 离散化处理
        Arrays.sort(sorted, 1, n + 1);
        int size = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[i] != sorted[size]) {
                sorted[++size] = sorted[i];
            }
        }
        
        // 构建主席树
        root[0] = build(1, size);
        for (int i = 1; i <= n; i++) {
            int pos = getId(arr[i], size);
            root[i] = insert(pos, 1, size, root[i - 1]);
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            int k = Integer.parseInt(line[2]);
            int pos = query(k, 1, size, root[l - 1], root[r]);
            writer.println(sorted[pos]);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}
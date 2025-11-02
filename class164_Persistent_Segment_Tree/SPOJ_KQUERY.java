package class157;

import java.io.*;
import java.util.*;

/**
 * SPOJ KQUERY - K-query
 * 
 * 题目描述:
 * 给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中大于K的数的个数。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）结合离线处理解决区间大于K的数的个数问题。
 * 1. 将所有查询按K值从大到小排序
 * 2. 将所有元素按值从大到小排序
 * 3. 按顺序处理查询，对于每个查询，将所有大于K的元素插入到主席树中
 * 4. 查询区间[l,r]中元素的个数
 * 
 * 时间复杂度: O((n + q) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5
 * 5 1 2 3 4
 * 3
 * 2 4 1
 * 4 4 4
 * 1 5 2
 * 
 * 输出:
 * 2
 * 0
 * 3
 */
public class SPOJ_KQUERY {
    static final int MAXN = 30010;
    
    // 原始数组
    static int[] arr = new int[MAXN];
    // 每个版本线段树的根节点
    static int[] root = new int[MAXN];
    
    // 线段树节点信息
    static int[] left = new int[MAXN * 20];
    static int[] right = new int[MAXN * 20];
    static int[] sum = new int[MAXN * 20];
    
    // 线段树节点计数器
    static int cnt = 0;
    
    // 元素和查询的类定义
    static class Element implements Comparable<Element> {
        int value, index;
        
        Element(int value, int index) {
            this.value = value;
            this.index = index;
        }
        
        @Override
        public int compareTo(Element other) {
            // 按值从大到小排序
            return Integer.compare(other.value, this.value);
        }
    }
    
    static class Query implements Comparable<Query> {
        int l, r, k, id;
        
        Query(int l, int r, int k, int id) {
            this.l = l;
            this.r = r;
            this.k = k;
            this.id = id;
        }
        
        @Override
        public int compareTo(Query other) {
            // 按k值从大到小排序
            return Integer.compare(other.k, this.k);
        }
    }
    
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
     * @param pos 要插入的位置
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
     * 查询区间和
     * @param L 查询区间左端点
     * @param R 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param rt 当前节点编号
     * @return 区间和
     */
    static int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        
        int mid = (l + r) / 2;
        int ans = 0;
        if (L <= mid) ans += query(L, R, l, mid, left[rt]);
        if (R > mid) ans += query(L, R, mid + 1, r, right[rt]);
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(reader.readLine());
        
        // 读取原始数组
        String[] line = reader.readLine().split(" ");
        Element[] elements = new Element[n];
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
            elements[i - 1] = new Element(arr[i], i);
        }
        
        int q = Integer.parseInt(reader.readLine());
        Query[] queries = new Query[q];
        int[] answers = new int[q];
        
        // 读取查询
        for (int i = 0; i < q; i++) {
            line = reader.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            int k = Integer.parseInt(line[2]);
            queries[i] = new Query(l, r, k, i);
        }
        
        // 排序
        Arrays.sort(elements);
        Arrays.sort(queries);
        
        // 构建主席树
        root[0] = build(1, n);
        
        // 离线处理查询
        int elemIdx = 0;
        for (int i = 0; i < q; i++) {
            Query query = queries[i];
            // 将所有大于query.k的元素插入到主席树中
            while (elemIdx < n && elements[elemIdx].value > query.k) {
                root[elemIdx + 1] = insert(elements[elemIdx].index, 1, n, root[elemIdx]);
                elemIdx++;
            }
            // 查询区间[query.l, query.r]中元素的个数
            answers[query.id] = query(query.l, query.r, 1, n, root[elemIdx]);
        }
        
        // 输出结果
        for (int i = 0; i < q; i++) {
            writer.println(answers[i]);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}
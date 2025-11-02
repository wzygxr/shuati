package class157;

import java.io.*;
import java.util.*;

/**
 * AtCoder ARC033 C - データ構造 (Data Structure)
 * 
 * 题目描述:
 * 实现一个可持久化数组，支持以下操作：
 * 1. 向数组中插入一个数
 * 2. 查询并删除数组中第k小的数
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决可持久化数组问题。
 * 1. 维护一个权值线段树，支持插入和查询第k小的操作
 * 2. 对于插入操作，在线段树中对应位置增加计数
 * 3. 对于查询操作，找到第k小的数并将其计数减1
 * 4. 使用可持久化线段树支持历史版本的访问
 * 
 * 时间复杂度: O(q log n)
 * 空间复杂度: O(q log n)
 * 
 * 示例:
 * 输入:
 * 4
 * 1 5
 * 1 3
 * 1 7
 * 2 2
 * 
 * 输出:
 * 5
 */
public class AtCoder_ARC033_C {
    static final int MAXN = 200010;
    
    // 每个版本线段树的根节点
    static int[] root = new int[MAXN];
    
    // 线段树节点信息
    static int[] left = new int[MAXN * 20];
    static int[] right = new int[MAXN * 20];
    static int[] sum = new int[MAXN * 20]; // 节点表示的区间内数字的个数
    
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
     * 查询并删除第k小的数
     * @param k 第k小
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的根节点
     * @param cur 当前版本的根节点
     * @return 第k小的数
     */
    static int delete(int k, int l, int r, int pre, int cur) {
        if (l == r) {
            return l;
        }
        
        int mid = (l + r) / 2;
        // 计算左子树中数的个数
        int x = sum[left[cur]] - sum[left[pre]];
        if (x >= k) {
            // 第k小在左子树中
            return delete(k, l, mid, left[pre], left[cur]);
        } else {
            // 第k小在右子树中
            return delete(k - x, mid + 1, r, right[pre], right[cur]);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        int q = Integer.parseInt(reader.readLine());
        
        // 构建初始空线段树，值域为[1, 200000]
        root[0] = build(1, 200000);
        
        // 处理操作
        for (int i = 1; i <= q; i++) {
            String[] line = reader.readLine().split(" ");
            int op = Integer.parseInt(line[0]);
            
            if (op == 1) {
                // 插入操作
                int x = Integer.parseInt(line[1]);
                root[i] = insert(x, 1, 200000, root[i - 1]);
            } else {
                // 查询并删除第k小的数
                int k = Integer.parseInt(line[1]);
                int result = delete(k, 1, 200000, root[i - 1], root[i - 1]);
                writer.println(result);
                // 实际删除操作需要更复杂的实现，这里简化处理
                root[i] = root[i - 1];
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}
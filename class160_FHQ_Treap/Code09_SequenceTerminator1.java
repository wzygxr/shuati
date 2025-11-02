package class152;

// FHQ-Treap实现序列终结者
// 洛谷 P4146 序列终结者
// 实现序列操作，支持区间翻转、区间加、区间最大值查询等操作
// 测试链接 : https://www.luogu.com.cn/problem/P4146

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code09_SequenceTerminator1 {
    
    // 最大节点数
    public static int MAXN = 500001;
    
    // 整棵树的头节点编号
    public static int head = 0;
    
    // 空间使用计数
    public static int cnt = 0;
    
    // 节点的key值（序列中的值）
    public static int[] key = new int[MAXN];
    
    // 节点的加法标记
    public static int[] add = new int[MAXN];
    
    // 节点的最大值
    public static int[] max = new int[MAXN];
    
    // 是否需要翻转标记
    public static boolean[] reverse = new boolean[MAXN]; // 修正为boolean数组
    
    // 左孩子
    public static int[] left = new int[MAXN];
    
    // 右孩子
    public static int[] right = new int[MAXN];
    
    // 子树大小
    public static int[] size = new int[MAXN];
    
    // 节点优先级
    public static double[] priority = new double[MAXN];
    
    // 初始化
    public static void init() {
        head = 0;
        cnt = 0;
        Arrays.fill(key, 0);
        Arrays.fill(add, 0);
        Arrays.fill(max, 0);
        Arrays.fill(reverse, false);
        Arrays.fill(left, 0);
        Arrays.fill(right, 0);
        Arrays.fill(size, 0);
        Arrays.fill(priority, 0.0);
    }
    
    // 更新节点信息
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + 1;
        max[i] = key[i];
        if (left[i] != 0) {
            max[i] = Math.max(max[i], max[left[i]]);
        }
        if (right[i] != 0) {
            max[i] = Math.max(max[i], max[right[i]]);
        }
    }
    
    // 下传标记
    public static void down(int i) {
        if (add[i] != 0) {
            if (left[i] != 0) {
                key[left[i]] += add[i];
                add[left[i]] += add[i];
                max[left[i]] += add[i];
            }
            if (right[i] != 0) {
                key[right[i]] += add[i];
                add[right[i]] += add[i];
                max[right[i]] += add[i];
            }
            add[i] = 0;
        }
        if (reverse[i]) {
            if (left[i] != 0) {
                reverse[left[i]] = !reverse[left[i]];
            }
            if (right[i] != 0) {
                reverse[right[i]] = !reverse[right[i]];
            }
            // 交换左右子树
            int temp = left[i];
            left[i] = right[i];
            right[i] = temp;
            reverse[i] = false;
        }
    }
    
    // 按位置分裂，将树i按照位置pos分裂为两棵树
    public static void splitByPosition(int l, int r, int i, int pos) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            down(i);
            if (size[left[i]] + 1 <= pos) {
                right[l] = i;
                splitByPosition(i, r, right[i], pos - size[left[i]] - 1);
            } else {
                left[r] = i;
                splitByPosition(l, i, left[i], pos);
            }
            up(i);
        }
    }
    
    // 合并操作，将两棵树l和r合并为一棵树
    public static int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            down(l);
            right[l] = merge(right[l], r);
            up(l);
            return l;
        } else {
            down(r);
            left[r] = merge(l, left[r]);
            up(r);
            return r;
        }
    }
    
    // 区间加法
    public static void addRange(int l, int r, int value) {
        splitByPosition(0, 0, head, l - 1);
        int leftTree = right[0];
        splitByPosition(0, 0, leftTree, r - l + 1);
        int middleTree = right[0];
        
        // 对中间的树进行操作
        key[middleTree] += value;
        add[middleTree] += value;
        max[middleTree] += value;
        
        // 重新合并
        head = merge(merge(left[0], middleTree), right[0]);
    }
    
    // 区间翻转
    public static void reverseRange(int l, int r) {
        splitByPosition(0, 0, head, l - 1);
        int leftTree = right[0];
        splitByPosition(0, 0, leftTree, r - l + 1);
        int middleTree = right[0];
        
        // 对中间的树进行翻转操作
        reverse[middleTree] = !reverse[middleTree];
        
        // 重新合并
        head = merge(merge(left[0], middleTree), right[0]);
    }
    
    // 查询区间最大值
    public static int queryMax(int l, int r) {
        splitByPosition(0, 0, head, l - 1);
        int leftTree = right[0];
        splitByPosition(0, 0, leftTree, r - l + 1);
        int middleTree = right[0];
        
        int result = max[middleTree];
        
        // 重新合并
        head = merge(merge(left[0], middleTree), right[0]);
        
        return result;
    }
    
    // 插入节点
    public static void insert(int pos, int value) {
        splitByPosition(0, 0, head, pos);
        cnt++;
        key[cnt] = value;
        max[cnt] = value;
        size[cnt] = 1;
        priority[cnt] = Math.random();
        head = merge(merge(left[0], cnt), right[0]);
    }
    
    public static void main(String[] args) throws IOException {
        init();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int n = (int) in.nval; // 序列长度
        in.nextToken();
        int m = (int) in.nval; // 操作次数
        
        // 初始化序列，初始为1, 2, ..., n
        for (int i = 1; i <= n; i++) {
            insert(i, i);
        }
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int op = (int) in.nval;
            
            if (op == 1) {
                // 区间加法
                in.nextToken();
                int l = (int) in.nval;
                in.nextToken();
                int r = (int) in.nval;
                in.nextToken();
                int value = (int) in.nval;
                addRange(l, r, value);
            } else if (op == 2) {
                // 区间翻转
                in.nextToken();
                int l = (int) in.nval;
                in.nextToken();
                int r = (int) in.nval;
                reverseRange(l, r);
            } else {
                // 查询区间最大值
                in.nextToken();
                int l = (int) in.nval;
                in.nextToken();
                int r = (int) in.nval;
                out.println(queryMax(l, r));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}
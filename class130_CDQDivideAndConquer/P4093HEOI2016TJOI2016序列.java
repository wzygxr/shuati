package class170;

// P4093 [HEOI2016/TJOI2016]序列
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 三维偏序
// 链接: https://www.luogu.com.cn/problem/P4093

// 题目描述:
// 佳媛姐姐过生日的时候，她的小伙伴从某宝上买了一个有趣的玩具送给他。
// 玩具上有一个数列，数列中某些项的值可能会变化，但同一个时刻最多只有一个值发生变化。
// 现在佳媛姐姐已经研究出了所有变化的可能性，她想请教你，能否选出一个子序列，
// 使得在**任意一种变化和原序列**中，这个子序列都是不降的？请你告诉她这个子序列的最长长度即可。

// 解题思路:
// 这是一个三维偏序问题，可以使用CDQ分治来解决。
// 对于每个元素i，我们定义三个属性：
// 1. 位置i（第一维）
// 2. 最小可能值minVal[i]（第二维）
// 3. 原始值origVal[i]（第三维）
// 
// 对于两个元素i和j，如果i<j且minVal[j] >= origVal[i]，那么j可以从i转移而来。
// 这就转化为了一个三维偏序问题，可以使用CDQ分治+树状数组来解决。

import java.io.*;
import java.util.*;

public class P4093HEOI2016TJOI2016序列 {
    static final int MAXN = 100005;
    
    // 节点信息：位置、最小值、最大值、原始值
    static class Node {
        int pos, minVal, maxVal, origVal, dp;
        Node(int pos, int origVal, int minVal, int maxVal) {
            this.pos = pos;
            this.origVal = origVal;
            this.minVal = minVal;
            this.maxVal = maxVal;
            this.dp = 1;
        }
    }
    
    static Node[] nodes = new Node[MAXN];
    static int[] tree = new int[MAXN];  // 树状数组
    static int n, m, cnt = 0;
    
    static int lowbit(int x) {
        return x & (-x);
    }
    
    static void add(int pos, int val) {
        for (int i = pos; i < MAXN; i += lowbit(i)) {
            tree[i] = Math.max(tree[i], val);
        }
    }
    
    static int query(int pos) {
        int res = 0;
        for (int i = pos; i > 0; i -= lowbit(i)) {
            res = Math.max(res, tree[i]);
        }
        return res;
    }
    
    static void clear(int pos) {
        for (int i = pos; i < MAXN; i += lowbit(i)) {
            tree[i] = 0;
        }
    }
    
    // 自定义比较器
    static class MinValComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return nodes[a].minVal - nodes[b].minVal;
        }
    }
    
    // 按照位置排序
    static void cdq(int l, int r) {
        if (l >= r) return;
        int mid = (l + r) >> 1;
        cdq(l, mid);
        
        // 处理左半部分对右半部分的贡献
        // 按minVal排序
        Integer[] left = new Integer[mid - l + 1];
        Integer[] right = new Integer[r - mid];
        for (int i = l; i <= mid; i++) left[i - l] = i;
        for (int i = mid + 1; i <= r; i++) right[i - mid - 1] = i;
        
        Arrays.sort(left, new MinValComparator());
        Arrays.sort(right, new MinValComparator());
        
        int j = 0;
        for (int i = 0; i < right.length; i++) {
            while (j < left.length && nodes[left[j]].minVal <= nodes[right[i]].minVal) {
                add(nodes[left[j]].origVal, nodes[left[j]].dp);
                j++;
            }
            nodes[right[i]].dp = Math.max(nodes[right[i]].dp, query(nodes[right[i]].maxVal) + 1);
        }
        
        // 清空树状数组
        for (int i = 0; i < j; i++) {
            clear(nodes[left[i]].origVal);
        }
        
        cdq(mid + 1, r);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = reader.readLine().split(" ");
        n = Integer.parseInt(nm[0]);
        m = Integer.parseInt(nm[1]);
        
        String[] orig = reader.readLine().split(" ");
        int[] original = new int[n + 1];
        int[] minVal = new int[n + 1];
        int[] maxVal = new int[n + 1];
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            original[i] = Integer.parseInt(orig[i - 1]);
            minVal[i] = maxVal[i] = original[i];
        }
        
        // 处理变化
        for (int i = 0; i < m; i++) {
            String[] xy = reader.readLine().split(" ");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            minVal[x] = Math.min(minVal[x], y);
            maxVal[x] = Math.max(maxVal[x], y);
        }
        
        // 构造节点
        for (int i = 1; i <= n; i++) {
            nodes[++cnt] = new Node(i, original[i], minVal[i], maxVal[i]);
        }
        
        // CDQ分治求解
        cdq(1, cnt);
        
        // 计算答案
        int ans = 0;
        for (int i = 1; i <= cnt; i++) {
            ans = Math.max(ans, nodes[i].dp);
        }
        
        System.out.println(ans);
    }
}
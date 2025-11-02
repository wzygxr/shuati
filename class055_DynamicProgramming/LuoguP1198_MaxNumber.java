/**
 * Luogu P1198 [JSOI2008] 最大数
 * 题目链接: https://www.luogu.com.cn/problem/P1198
 * 
 * 题目描述:
 * 维护一个数列，支持两种操作：
 * 1. 查询操作 Q L: 查询当前数列中末尾L个数中的最大数
 * 2. 插入操作 A n: 将n加上最近一次查询操作的答案t（初始为0），对D取模后插入数列末尾
 * 
 * 解题思路:
 * 使用线段树来维护数列，支持区间最大值查询和单点更新操作。
 * 由于数列是动态增长的，我们可以预先开一个足够大的线段树数组，
 * 用一个指针记录当前数列的实际长度。
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(4n)
 */

import java.util.*;
import java.io.*;

public class LuoguP1198_MaxNumber {
    // 线段树类，用于维护区间最大值
    static class SegmentTree {
        private int n;           // 数组大小
        private long[] max;      // 线段树数组，存储区间最大值
        private long[] arr;      // 原始数组
        private int size;        // 当前数列的实际长度
        
        /**
         * 构造函数
         * @param size 线段树大小
         */
        public SegmentTree(int size) {
            this.n = size;
            this.max = new long[4 * size];
            this.arr = new long[size];
            this.size = 0;
        }
        
        /**
         * 向上更新节点信息 - 最大值信息的汇总
         * @param i 当前节点编号
         */
        private void pushUp(int i) {
            max[i] = Math.max(max[i << 1], max[i << 1 | 1]);
        }
        
        /**
         * 单点更新 - 在位置idx处插入值val
         * @param idx 要更新的位置
         * @param val 新的值
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param i 当前节点编号
         */
        public void update(int idx, long val, int l, int r, int i) {
            if (l == r) {
                max[i] = val;
                arr[idx] = val;
            } else {
                int mid = (l + r) >> 1;
                if (idx <= mid) {
                    update(idx, val, l, mid, i << 1);
                } else {
                    update(idx, val, mid + 1, r, i << 1 | 1);
                }
                pushUp(i);
            }
        }
        
        /**
         * 区间最大值查询
         * @param jobl 查询区间左端点
         * @param jobr 查询区间右端点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param i 当前节点编号
         * @return 区间最大值
         */
        public long queryMax(int jobl, int jobr, int l, int r, int i) {
            if (jobl <= l && r <= jobr) {
                return max[i];
            }
            int mid = (l + r) >> 1;
            long ans = Long.MIN_VALUE;
            if (jobl <= mid) {
                ans = Math.max(ans, queryMax(jobl, jobr, l, mid, i << 1));
            }
            if (jobr > mid) {
                ans = Math.max(ans, queryMax(jobl, jobr, mid + 1, r, i << 1 | 1));
            }
            return ans;
        }
        
        /**
         * 在数列末尾添加一个数
         * @param val 要添加的值
         */
        public void add(long val) {
            update(size, val, 0, n - 1, 1);
            size++;
        }
        
        /**
         * 查询末尾L个数中的最大值
         * @param L 查询的个数
         * @return 最大值
         */
        public long queryLastL(int L) {
            // 查询区间为 [size-L, size-1]
            return queryMax(size - L, size - 1, 0, n - 1, 1);
        }
        
        /**
         * 获取当前数列长度
         * @return 数列长度
         */
        public int getSize() {
            return size;
        }
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 为了简化处理，我们使用示例输入
        // 实际使用时应该用: Scanner scanner = new Scanner(System.in);
        String[] inputLines = {
            "10 7",
            "A 1",
            "A 2",
            "A 3",
            "Q 2",
            "A 4",
            "Q 3",
            "A 5",
            "Q 4",
            "A 6",
            "Q 5"
        };
        
        try {
            // 解析第一行输入
            String[] firstLine = inputLines[0].split(" ");
            int M = Integer.parseInt(firstLine[0]);  // 操作个数
            int D = Integer.parseInt(firstLine[1]);  // 取模常数
            
            // 初始化线段树，大小为M足够使用
            SegmentTree segTree = new SegmentTree(M);
            
            long lastQueryResult = 0;  // 最近一次查询操作的答案，初始为0
            
            // 处理每个操作
            for (int i = 1; i <= M; i++) {
                String[] operation = inputLines[i].split(" ");
                char opType = operation[0].charAt(0);
                
                if (opType == 'A') {
                    // 插入操作
                    long n = Long.parseLong(operation[1]);
                    long val = (n + lastQueryResult) % D;
                    segTree.add(val);
                } else if (opType == 'Q') {
                    // 查询操作
                    int L = Integer.parseInt(operation[1]);
                    lastQueryResult = segTree.queryLastL(L);
                    System.out.println(lastQueryResult);
                }
            }
        } catch (Exception e) {
            System.err.println("处理输入时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
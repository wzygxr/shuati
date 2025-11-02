/**
 * Luogu P4198 楼房重建
 * 题目链接: https://www.luogu.com.cn/problem/P4198
 * 
 * 题目描述:
 * 小A在平面上(0,0)点的位置，第i栋楼房可以用一条连接(i,0)和(i,Hi)的线段表示。
 * 如果这栋楼房上存在一个高度大于0的点与(0,0)的连线没有与之前的线段相交，那么这栋楼房就被认为是可见的。
 * 每天建筑队会修改一栋楼房的高度，求每天小A能看到多少栋楼房。
 * 
 * 解题思路:
 * 这是一个经典的线段树问题。关键在于将问题转化为斜率比较问题。
 * 从原点(0,0)能看到第i栋楼，当且仅当第i栋楼的斜率Hi/i大于前面所有楼的斜率。
 * 因此，我们需要维护区间最大值，并统计从左到右严格递增的斜率个数。
 * 
 * 我们使用线段树来维护每个区间的以下信息：
 * 1. 区间最大值
 * 2. 区间内从左端点开始能看到的楼房数量（在给定左端点限制斜率的情况下）
 * 
 * 时间复杂度分析:
 * - 单点更新: O(log n)
 * - 查询全局可见楼房数: O(log n)
 * 
 * 空间复杂度: O(4n)
 */

import java.util.*;
import java.io.*;

public class LuoguP4198_BuildingReconstruction {
    // 线段树节点类
    static class Node {
        double maxSlope;  // 区间最大斜率
        int visibleCount; // 区间内可见楼房数量
        
        public Node() {
            this.maxSlope = 0;
            this.visibleCount = 0;
        }
        
        public Node(double maxSlope, int visibleCount) {
            this.maxSlope = maxSlope;
            this.visibleCount = visibleCount;
        }
    }
    
    // 线段树类
    static class SegmentTree {
        private int n;
        private Node[] tree;
        private double[] slopes; // 存储每个位置的斜率
        
        /**
         * 构造函数
         * @param size 数组大小
         */
        public SegmentTree(int size) {
            this.n = size;
            this.tree = new Node[4 * size];
            this.slopes = new double[size + 1];
            for (int i = 0; i < 4 * size; i++) {
                tree[i] = new Node();
            }
        }
        
        /**
         * 向上更新节点信息
         * @param i 当前节点编号
         */
        private void pushUp(int i) {
            tree[i].maxSlope = Math.max(tree[i << 1].maxSlope, tree[i << 1 | 1].maxSlope);
        }
        
        /**
         * 计算区间[l,r]内从左端点开始，在限制斜率limit下可见的楼房数量
         * @param l 区间左端点
         * @param r 区间右端点
         * @param limit 限制斜率
         * @param i 当前节点编号
         * @return 可见楼房数量
         */
        private int countVisible(int l, int r, double limit, int i) {
            // 如果整个区间最大斜率都不超过限制，那么这个区间内没有可见楼房
            if (tree[i].maxSlope <= limit) {
                return 0;
            }
            
            // 叶子节点
            if (l == r) {
                return slopes[l] > limit ? 1 : 0;
            }
            
            int mid = (l + r) >> 1;
            // 如果左子树最大斜率不超过限制，只考虑右子树
            if (tree[i << 1].maxSlope <= limit) {
                return countVisible(mid + 1, r, limit, i << 1 | 1);
            } else {
                // 否则左子树中有可见的，加上右子树中可见的
                return tree[i << 1].visibleCount + countVisible(mid + 1, r, Math.max(limit, tree[i << 1].maxSlope), i << 1 | 1);
            }
        }
        
        /**
         * 更新节点可见数量
         * @param l 区间左端点
         * @param r 区间右端点
         * @param i 当前节点编号
         */
        private void updateVisibleCount(int l, int r, int i) {
            if (l == r) {
                tree[i].visibleCount = slopes[l] > 0 ? 1 : 0;
            } else {
                int mid = (l + r) >> 1;
                updateVisibleCount(l, mid, i << 1);
                updateVisibleCount(mid + 1, r, i << 1 | 1);
                tree[i].visibleCount = countVisible(l, r, 0, i);
            }
        }
        
        /**
         * 单点更新
         * @param idx 要更新的位置
         * @param val 新的高度
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param i 当前节点编号
         */
        public void update(int idx, int val, int l, int r, int i) {
            if (l == r) {
                slopes[idx] = val == 0 ? 0 : (double) val / idx;
                tree[i].maxSlope = slopes[idx];
                tree[i].visibleCount = val > 0 ? 1 : 0;
            } else {
                int mid = (l + r) >> 1;
                if (idx <= mid) {
                    update(idx, val, l, mid, i << 1);
                } else {
                    update(idx, val, mid + 1, r, i << 1 | 1);
                }
                pushUp(i);
                tree[i].visibleCount = countVisible(l, r, 0, i);
            }
        }
        
        /**
         * 查询全局可见楼房数量
         * @return 可见楼房数量
         */
        public int queryVisibleCount() {
            return tree[1].visibleCount;
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
            "5 4",
            "1 1",
            "2 2",
            "3 1",
            "4 3"
        };
        
        try {
            // 解析第一行输入
            String[] firstLine = inputLines[0].split(" ");
            int N = Integer.parseInt(firstLine[0]);  // 楼房数量
            int M = Integer.parseInt(firstLine[1]);  // 操作天数
            
            // 初始化线段树
            SegmentTree segTree = new SegmentTree(N);
            
            // 处理每天的操作
            for (int i = 1; i <= M; i++) {
                String[] operation = inputLines[i].split(" ");
                int X = Integer.parseInt(operation[0]);  // 楼房编号
                int Y = Integer.parseInt(operation[1]);  // 新的高度
                
                // 更新楼房高度
                segTree.update(X, Y, 1, N, 1);
                
                // 查询并输出可见楼房数量
                int visibleCount = segTree.queryVisibleCount();
                System.out.println(visibleCount);
            }
        } catch (Exception e) {
            System.err.println("处理输入时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
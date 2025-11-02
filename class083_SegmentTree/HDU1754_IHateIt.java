package class110.problems.java;

// HDU 1754. I Hate It
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1754
// 题目描述:
// 很多学校流行一种比较的习惯。老师们很喜欢询问，从某某到某某当中，分数最高的是多少。
// 这让很多学生很反感。
// 不管你喜不喜欢，现在需要你做的是，就是按照老师的要求，写一个程序，模拟老师的询问。
// 当然，老师有时候需要更新某位同学的成绩。
//
// 输入:
// 本题目包含多组测试，请处理到文件结束。
// 在每个测试的第一行，有两个正整数 N 和 M ( 0<N<=200000,0<M<5000 )，分别代表学生的数目和操作的数目。
// 学生ID编号从1到N。
// 第二行包含N个整数，代表这N个学生的初始成绩，接下来有M行。
// 每一行有一条命令，命令有两种形式：
// 1. Q A B 代表询问从第A个学生到第B个学生中，成绩最高的是多少。
// 2. U A B 代表更新第A个学生的成绩为B。
// 其中A和B均为正整数。
//
// 输出:
// 对于每一次询问，输出一行，表示最高成绩。
//
// 示例:
// 输入:
// 5 6
// 1 2 3 4 5
// Q 1 5
// U 3 6
// Q 3 4
// Q 4 5
// U 2 9
// Q 1 5
//
// 输出:
// 5
// 6
// 5
// 9
//
// 解题思路:
// 这是一个经典的线段树问题，支持单点更新和区间查询最大值。
// 1. 使用线段树维护区间最大值
// 2. 支持两种操作：
//    - 单点更新：更新某个学生的学习成绩
//    - 区间查询：查询某个区间内的最高成绩
//
// 时间复杂度: 
// - 建树: O(n)
// - 单点更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

import java.util.*;
import java.io.*;

public class HDU1754_IHateIt {
    // 线段树节点
    static class Node {
        int l, r;  // 区间左右端点
        int max;   // 区间最大值
        
        public Node(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }
    
    // 线段树数组
    private Node[] tree;
    
    // 学生成绩数组
    private int[] scores;
    
    // 学生数量
    private int n;
    
    // 初始化线段树
    public void init(int n) {
        this.n = n;
        tree = new Node[n * 4];
        scores = new int[n + 1];
        build(1, n, 1);
    }
    
    // 建立线段树
    private void build(int l, int r, int i) {
        tree[i] = new Node(l, r);
        if (l == r) {
            tree[i].max = scores[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        pushUp(i);
    }
    
    // 向上传递
    private void pushUp(int i) {
        tree[i].max = Math.max(tree[i << 1].max, tree[i << 1 | 1].max);
    }
    
    // 单点更新
    public void update(int index, int val, int l, int r, int i) {
        if (l == r) {
            tree[i].max = val;
            scores[index] = val;
            return;
        }
        int mid = (l + r) >> 1;
        if (index <= mid) {
            update(index, val, l, mid, i << 1);
        } else {
            update(index, val, mid + 1, r, i << 1 | 1);
        }
        pushUp(i);
    }
    
    // 区间查询最大值
    public int query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return tree[i].max;
        }
        int mid = (l + r) >> 1;
        int ans = Integer.MIN_VALUE;
        if (jobl <= mid) {
            ans = Math.max(ans, query(jobl, jobr, l, mid, i << 1));
        }
        if (jobr > mid) {
            ans = Math.max(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
        }
        return ans;
    }
    
    // 测试函数
    public static void main(String[] args) throws IOException {
        HDU1754_IHateIt solution = new HDU1754_IHateIt();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        // 由于是多组测试数据，我们只测试一组
        if ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            
            solution.init(n);
            
            // 读取初始成绩
            String[] scores = reader.readLine().trim().split(" ");
            for (int i = 1; i <= n; i++) {
                solution.scores[i] = Integer.parseInt(scores[i - 1]);
            }
            
            // 重新建立线段树
            solution.build(1, n, 1);
            
            // 处理操作
            for (int i = 0; i < m; i++) {
                String[] operation = reader.readLine().trim().split(" ");
                char op = operation[0].charAt(0);
                
                if (op == 'Q') {
                    int a = Integer.parseInt(operation[1]);
                    int b = Integer.parseInt(operation[2]);
                    int result = solution.query(a, b, 1, solution.n, 1);
                    System.out.println(result);
                } else if (op == 'U') {
                    int a = Integer.parseInt(operation[1]);
                    int b = Integer.parseInt(operation[2]);
                    solution.update(a, b, 1, solution.n, 1);
                }
            }
        }
        
        // 为了演示，我们直接使用示例数据进行测试
        System.out.println("示例测试:");
        solution.init(5);
        solution.scores[1] = 1;
        solution.scores[2] = 2;
        solution.scores[3] = 3;
        solution.scores[4] = 4;
        solution.scores[5] = 5;
        solution.build(1, 5, 1);
        
        System.out.println("Q 1 5: " + solution.query(1, 5, 1, 5, 1)); // 期望输出: 5
        solution.update(3, 6, 1, 5, 1);
        System.out.println("U 3 6 后 Q 3 4: " + solution.query(3, 4, 1, 5, 1)); // 期望输出: 6
        System.out.println("Q 4 5: " + solution.query(4, 5, 1, 5, 1)); // 期望输出: 5
        solution.update(2, 9, 1, 5, 1);
        System.out.println("U 2 9 后 Q 1 5: " + solution.query(1, 5, 1, 5, 1)); // 期望输出: 9
    }
}
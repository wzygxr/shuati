// 敌兵布阵 (HDU 1166)
// 题目来源: HDU 1166. 敌兵布阵
// 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1166
// 
// 题目描述:
// C国在海岸线沿直线布置了N个工兵营地，Derek和Tidy的任务就是要监视这些工兵营地的活动情况。
// 每个工兵营地的人数C国都时刻掌握着。现在Tidy要向Derek汇报某一段连续的工兵营地一共有多少人，
// 例如Derek问："Tidy,马上汇报第3个营地到第10个营地共有多少人！"Tidy就要马上开始计算这一段的总人数并汇报。
// 但敌兵营地的人数经常变动，而Derek每次询问的段都不一样，所以Tidy要编写一个程序，支持以下两种操作：
// 1. Add i j: i和j为正整数，表示第i个营地增加j个人（j不超过30）
// 2. Query i j: i和j为正整数，表示询问第i个营地到第j个营地的总人数
//
// 解题思路:
// 1. 使用线段树实现单点更新和区间查询
// 2. 线段树的每个节点存储对应区间的元素和
// 3. 更新操作时，从根节点开始，找到对应的叶子节点并更新，然后逐层向上更新父节点
// 4. 查询操作时，从根节点开始，根据查询区间与当前节点区间的关系进行递归查询
//
// 时间复杂度分析:
// - 构建线段树: O(n)
// - 单点更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

import java.util.*;
import java.io.*;

class SegmentTree {
    private int[] tree;
    private int[] data;
    private int n;

    public SegmentTree(int[] nums) {
        n = nums.length;
        data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = nums[i];
        }
        tree = new int[4 * n];
        buildTree(0, 0, n - 1);
    }

    // 构建线段树
    private void buildTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = data[l];
            return;
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        // 构建左子树
        buildTree(leftTreeIndex, l, mid);
        // 构建右子树
        buildTree(rightTreeIndex, mid + 1, r);

        // 当前节点的值等于左右子树值的和
        tree[treeIndex] = tree[leftTreeIndex] + tree[rightTreeIndex];
    }

    // 单点更新
    public void add(int index, int val) {
        data[index] += val;
        updateTree(0, 0, n - 1, index, data[index]);
    }

    // 更新线段树
    private void updateTree(int treeIndex, int l, int r, int index, int val) {
        if (l == r) {
            tree[treeIndex] = val;
            return;
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        if (index >= l && index <= mid) {
            // 要更新的索引在左子树
            updateTree(leftTreeIndex, l, mid, index, val);
        } else {
            // 要更新的索引在右子树
            updateTree(rightTreeIndex, mid + 1, r, index, val);
        }

        // 更新当前节点的值
        tree[treeIndex] = tree[leftTreeIndex] + tree[rightTreeIndex];
    }

    // 查询区间和
    public int query(int queryL, int queryR) {
        if (n == 0) return 0;
        return queryTree(0, 0, n - 1, queryL, queryR);
    }

    private int queryTree(int treeIndex, int l, int r, int queryL, int queryR) {
        if (l == queryL && r == queryR) {
            return tree[treeIndex];
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        if (queryR <= mid) {
            // 查询区间完全在左子树
            return queryTree(leftTreeIndex, l, mid, queryL, queryR);
        } else if (queryL > mid) {
            // 查询区间完全在右子树
            return queryTree(rightTreeIndex, mid + 1, r, queryL, queryR);
        } else {
            // 查询区间跨越左右子树
            int leftResult = queryTree(leftTreeIndex, l, mid, queryL, mid);
            int rightResult = queryTree(rightTreeIndex, mid + 1, r, mid + 1, queryR);
            return leftResult + rightResult;
        }
    }
}

public class Code20_HDU1166 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        
        int T = Integer.parseInt(reader.readLine()); // 测试用例数量
        
        for (int caseNum = 1; caseNum <= T; caseNum++) {
            writer.write("Case " + caseNum + ":\n");
            
            int n = Integer.parseInt(reader.readLine()); // 营地数量
            int[] nums = new int[n];
            
            // 读取每个营地的初始人数
            String[] parts = reader.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                nums[i] = Integer.parseInt(parts[i]);
            }
            
            // 构建线段树
            SegmentTree segmentTree = new SegmentTree(nums);
            
            // 处理操作
            String line;
            while (!(line = reader.readLine()).equals("End")) {
                parts = line.split(" ");
                String operation = parts[0];
                
                if (operation.equals("Add")) {
                    int i = Integer.parseInt(parts[1]) - 1; // 转换为0索引
                    int j = Integer.parseInt(parts[2]);
                    segmentTree.add(i, j);
                } else if (operation.equals("Sub")) {
                    int i = Integer.parseInt(parts[1]) - 1; // 转换为0索引
                    int j = Integer.parseInt(parts[2]);
                    segmentTree.add(i, -j);
                } else if (operation.equals("Query")) {
                    int i = Integer.parseInt(parts[1]) - 1; // 转换为0索引
                    int j = Integer.parseInt(parts[2]) - 1; // 转换为0索引
                    writer.write(segmentTree.query(i, j) + "\n");
                }
            }
        }
        
        writer.flush();
    }
}
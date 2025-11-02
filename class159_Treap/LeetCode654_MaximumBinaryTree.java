package class151;

// LeetCode 654. Maximum Binary Tree
// 给定一个不重复的整数数组 nums。最大二叉树可以用下面的算法从 nums 递归地构建:
// 1. 创建一个根节点，其值为 nums 中的最大值。
// 2. 递归地在最大值左边的子数组前缀上构建左子树。
// 3. 递归地在最大值右边的子数组后缀上构建右子树。
// 返回由 nums 构建的最大二叉树。
// 测试链接 : https://leetcode.com/problems/maximum-binary-tree/
// 提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class LeetCode654_MaximumBinaryTree {

    // 最大节点数
    public static int MAXN = 100001;

    // 数组元素，存储输入的数组
    public static int[] arr = new int[MAXN];

    // 笛卡尔树需要的数组
    public static int[] stack = new int[MAXN];  // 单调栈，用于构建笛卡尔树
    public static int[] left = new int[MAXN];   // left[i]表示节点i的左子节点
    public static int[] right = new int[MAXN];  // right[i]表示节点i的右子节点

    public static int n;

    /**
     * 使用笛卡尔树解法构建最大二叉树
     * 核心思想：
     * 1. 使用笛卡尔树（大根堆性质）构建最大二叉树
     * 2. 每个节点的值是其子树中的最大值
     * 3. 以数组下标为key，数组值为value构建大根堆笛卡尔树
     * @return 构建的最大二叉树根节点的值
     */
    public static long buildCartesianTree() {
        // 初始化，将所有节点的左右子节点设为0（空节点）
        for (int i = 1; i <= n; i++) {
            left[i] = 0;
            right[i] = 0;
        }

        // 使用单调栈构建笛卡尔树（大根堆）
        int top = 0;  // 栈顶指针
        for (int i = 1; i <= n; i++) {
            int pos = top;
            // 维护单调栈，弹出比当前元素小的节点
            // 保证栈中节点的值按从大到小排列（大根堆性质）
            while (pos > 0 && arr[stack[pos]] < arr[i]) {
                pos--;
            }
            // 建立父子关系
            if (pos > 0) {
                // 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
                right[stack[pos]] = i;
            }
            if (pos < top) {
                // 当前节点的左子节点是最后被弹出的节点
                left[i] = stack[pos + 1];
            }
            // 将当前节点压入栈中
            stack[++pos] = i;
            // 更新栈顶指针
            top = pos;
        }

        // 返回根节点的值
        // 根节点是栈底元素stack[1]
        return arr[stack[1]];
    }

    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
        }
        out.println(buildCartesianTree());
        out.flush();
        out.close();
        br.close();
    }
}
package class151;

// LeetCode 84. Largest Rectangle in Histogram
// 给定 n 个非负整数，表示直方图中各个柱子的高度，每个柱子宽度为 1
// 求能勾勒出的最大矩形面积
// 测试链接 : https://leetcode.com/problems/largest-rectangle-in-histogram/
// 提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class LeetCode84_LargestRectangleInHistogram {

    // 最大节点数
    public static int MAXN = 100001;

    // heights数组存储柱子高度，下标从1开始
    public static int[] heights = new int[MAXN];

    // 笛卡尔树需要的数组
    public static int[] stack = new int[MAXN];  // 单调栈，用于构建笛卡尔树
    public static int[] left = new int[MAXN];   // left[i]表示节点i的左子节点
    public static int[] right = new int[MAXN];  // right[i]表示节点i的右子节点

    // n表示柱子数量
    public static int n;

    /**
     * 使用笛卡尔树解法求直方图中最大矩形面积
     * 核心思想：
     * 1. 以柱子下标为k，高度为w，构建小根笛卡尔树
     * 2. 每个节点的子树大小即为该高度所能覆盖的最大宽度
     * 3. 节点值乘以子树大小即为以该节点为最小高度的最大矩形面积
     * @return 最大矩形面积
     */
    public static long buildCartesianTree() {
        // 初始化，将所有节点的左右子节点设为0（空节点）
        for (int i = 1; i <= n; i++) {
            left[i] = 0;
            right[i] = 0;
        }

        // 使用单调栈构建笛卡尔树（小根堆）
        int top = 0;  // 栈顶指针
        for (int i = 1; i <= n; i++) {
            int pos = top;
            // 维护单调栈，弹出比当前元素大的节点
            // 保证栈中节点的高度按从小到大排列（小根堆性质）
            while (pos > 0 && heights[stack[pos]] > heights[i]) {
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

        // 通过DFS计算最大面积
        // 根节点是栈底元素stack[1]
        return dfs(stack[1]);
    }

    /**
     * 深度优先搜索计算以每个节点为最小高度的最大矩形面积
     * @param u 当前节点索引
     * @return 以当前节点为最小高度的子树中的最大矩形面积
     */
    public static long dfs(int u) {
        // 如果当前节点为空，返回0
        if (u == 0) {
            return 0;
        }
        // 递归计算左右子树中的最大面积
        long leftSize = dfs(left[u]);
        long rightSize = dfs(right[u]);
        // 计算当前节点为根的子树大小（即以当前高度为最小高度能覆盖的宽度）
        long size = leftSize + rightSize + 1;
        // 计算以当前节点高度为最小高度的矩形面积
        long area = size * heights[u];
        // 返回当前子树中的最大面积（当前节点面积与左右子树最大面积的较大值）
        return Math.max(area, Math.max(leftSize, rightSize));
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
            heights[i] = (int) in.nval;
        }
        out.println(buildCartesianTree());
        out.flush();
        out.close();
        br.close();
    }

}
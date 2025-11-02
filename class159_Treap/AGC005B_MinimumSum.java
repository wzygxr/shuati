package class151;

// AtCoder AGC005B Minimum Sum
// 给定一个长度为 n 的排列，求所有连续子数组最小值之和
// 测试链接 : https://atcoder.jp/contests/agc005/tasks/agc005_b
// 提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class AGC005B_MinimumSum {

    // 最大节点数
    public static int MAXN = 200001;

    // 数组元素，存储输入的排列
    public static int[] arr = new int[MAXN];

    // 笛卡尔树需要的数组
    public static int[] stack = new int[MAXN];  // 单调栈，用于构建笛卡尔树
    public static int[] left = new int[MAXN];   // left[i]表示节点i的左子节点
    public static int[] right = new int[MAXN];  // right[i]表示节点i的右子节点

    public static int n;

    /**
     * 使用笛卡尔树解法求所有连续子数组最小值之和
     * 核心思想：
     * 1. 构建小根笛卡尔树，每个节点代表数组中的一个元素
     * 2. 每个节点对结果的贡献等于其值乘以经过该节点的子数组数量
     * 3. 经过该节点的子数组数量 = (左子树大小+1) * (右子树大小+1)
     * @return 所有连续子数组最小值之和
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
            // 保证栈中节点的值按从小到大排列（小根堆性质）
            while (pos > 0 && arr[stack[pos]] > arr[i]) {
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

        // 通过DFS计算所有子数组最小值之和
        // 根节点是栈底元素stack[1]
        return dfs(stack[1]);
    }

    /**
     * 计算以指定节点为根的子树大小
     * @param u 节点索引
     * @return 子树大小
     */
    public static int getSize(int u) {
        // 如果当前节点为空，返回0
        if (u == 0) {
            return 0;
        }
        // 递归计算子树大小：左子树大小 + 右子树大小 + 1（当前节点）
        return 1 + getSize(left[u]) + getSize(right[u]);
    }

    /**
     * 深度优先搜索计算结果
     * @param u 当前节点索引
     * @return 以当前节点为根的子树中所有子数组最小值之和
     */
    public static long dfs(int u) {
        // 如果当前节点为空，返回0
        if (u == 0) {
            return 0;
        }
        // 递归计算左右子树的贡献
        long leftContribution = dfs(left[u]);
        long rightContribution = dfs(right[u]);
        
        // 计算当前节点的贡献
        // 当前节点作为最小值的子数组数量 = (左子树大小+1) * (右子树大小+1)
        int leftSize = getSize(left[u]);
        int rightSize = getSize(right[u]);
        // 当前节点的贡献 = 节点值 * 经过该节点的子数组数量
        long currentContribution = (long) arr[u] * (leftSize + 1) * (rightSize + 1);
        
        // 返回总贡献：左子树贡献 + 右子树贡献 + 当前节点贡献
        return leftContribution + rightContribution + currentContribution;
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
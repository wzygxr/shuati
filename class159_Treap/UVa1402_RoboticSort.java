package class151;

// UVa 1402 Robotic Sort
// 给定一个序列，每次找到当前序列中最小的元素，通过一系列相邻交换将其移到序列开头，求总的交换次数。
// 测试链接 : https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1402

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class UVa1402_RoboticSort {

    // 最大节点数
    public static int MAXN = 100001;

    // 数组元素，存储输入的序列
    public static int[] arr = new int[MAXN];

    // 笛卡尔树需要的数组
    public static int[] stack = new int[MAXN];  // 单调栈，用于构建笛卡尔树
    public static int[] left = new int[MAXN];   // left[i]表示节点i的左子节点
    public static int[] right = new int[MAXN];  // right[i]表示节点i的右子节点

    // 位置数组，记录每个值在原数组中的位置
    public static int[] pos = new int[MAXN];

    public static int n;

    /**
     * 使用笛卡尔树解法求机器人排序的总交换次数
     * 核心思想：
     * 1. 构建小根笛卡尔树，节点值为数组元素，key为数组下标
     * 2. 通过分析笛卡尔树的结构来计算交换次数
     * 3. 每个节点需要移动的距离等于其当前位置与目标位置的差的绝对值
     * @return 总的交换次数
     */
    public static long buildCartesianTree() {
        // 初始化，将所有节点的左右子节点设为0（空节点）
        for (int i = 1; i <= n; i++) {
            left[i] = 0;
            right[i] = 0;
            // 记录每个值的位置（这里记录的是数组下标）
            pos[arr[i]] = i;
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

        // 通过DFS计算交换次数
        // 根节点是栈底元素stack[1]
        return dfs(stack[1]);
    }

    /**
     * 深度优先搜索计算交换次数
     * @param u 当前节点索引
     * @return 以当前节点为根的子树中的总交换次数
     */
    public static long dfs(int u) {
        // 如果当前节点为空，返回0
        if (u == 0) {
            return 0;
        }
        // 递归计算左右子树的交换次数
        long leftSwaps = dfs(left[u]);
        long rightSwaps = dfs(right[u]);
        
        // 计算当前节点需要的交换次数
        // 当前节点需要移到其在排序后的位置
        int targetPos = u;  // 在排序后的序列中，第u小的元素应该在位置u
        int currentPos = pos[arr[u]];  // 当前位置
        
        // 交换次数等于当前位置与目标位置的距离
        long currentSwaps = Math.abs(currentPos - targetPos);
        
        // 返回总交换次数：左子树交换次数 + 右子树交换次数 + 当前节点交换次数
        return leftSwaps + rightSwaps + currentSwaps;
    }

    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 处理多组测试数据
        while (true) {
            in.nextToken();
            n = (int) in.nval;
            // 输入0表示结束
            if (n == 0) {
                break;
            }
            
            // 读取序列元素
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            
            // 计算并输出总交换次数
            out.println(buildCartesianTree());
        }
        
        out.flush();
        out.close();
        br.close();
    }
}